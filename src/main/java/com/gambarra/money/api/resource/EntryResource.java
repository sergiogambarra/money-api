package com.gambarra.money.api.resource;

import com.gambarra.money.api.dto.Attachment;
import com.gambarra.money.api.dto.EntryStatisticByDay;
import com.gambarra.money.api.dto.EntryStatisticCategory;
import com.gambarra.money.api.exceptionhandler.MoneyExceptionHandler;
import com.gambarra.money.api.model.Person;
import com.gambarra.money.api.repository.EntryRepository;
import com.gambarra.money.api.repository.filter.EntryFilter;
import com.gambarra.money.api.repository.projection.EntryResume;
import com.gambarra.money.api.service.EntryService;
import com.gambarra.money.api.service.exception.PersonNonexistentOrInactiveException;
import com.gambarra.money.api.event.ResourceCreatedEvent;
import com.gambarra.money.api.model.Entry;
import com.gambarra.money.api.storage.S3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/lancamentos")
public class EntryResource {

    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private EntryService entryService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private S3 s3;

    @PostMapping("/anexo")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
    public Attachment uploadAnexo(@RequestParam MultipartFile anexo) throws IOException {
        String name = s3.salvarTemporatiamente(anexo);
        return new Attachment(name, s3.configureuUrl(name));
    }

    @GetMapping("/relatorios/por-pessoa")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
    public ResponseEntity<byte[]> reportByPerson(
            @RequestParam @DateTimeFormat(pattern= "yyyy-MM-dd") LocalDate start,
            @RequestParam @DateTimeFormat(pattern= "yyyy-MM-dd") LocalDate end) throws Exception {

        byte[] report = entryService.reportByPerson(start, end);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .body(report);

    }

    @GetMapping("/estatisticas/por-categoria")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
    public List<EntryStatisticCategory> byCategory() {
        return this.entryRepository.byCategory(LocalDate.now());
    }

    @GetMapping("/estatisticas/por-dia")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
    public List<EntryStatisticByDay> byDay() {
        return this.entryRepository.byDay(LocalDate.now());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
    public Page<Entry> search(EntryFilter entryFilter, Pageable pageable) {
        return entryRepository.filter(entryFilter, pageable);
    }

    @GetMapping(params = "resumo")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
    public Page<EntryResume> resume(EntryFilter entryFilter, Pageable pageable) {
        return entryRepository.resume(entryFilter, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
    public Object show(@PathVariable Long id){
        Optional<Entry> entry = entryRepository.findById(id);
        return entry.isPresent() ? ResponseEntity.ok(entry.get()) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
    public ResponseEntity<Entry> create(@Valid @RequestBody Entry entry, HttpServletResponse response) {
        Entry entrySaved = entryService.save(entry);
        publisher.publishEvent(new ResourceCreatedEvent(this, response, entrySaved.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(entrySaved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Entry> update(@Valid @RequestBody Entry entry, @PathVariable Long id) {
        try {
            Entry entrySaved = entryService.update(id, entry);
            return ResponseEntity.ok(entrySaved);
        } catch (IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }
    }


    @ExceptionHandler({PersonNonexistentOrInactiveException.class })
    public ResponseEntity<Object> handlePersonNonexistentOrInactiveException(PersonNonexistentOrInactiveException ex){
        String mensagemUsuario = messageSource.getMessage("person.nonexistent-or-inactive", null, LocaleContextHolder.getLocale());
        String mensagemDev = ex.toString();
        List<MoneyExceptionHandler.Erro> erros = Arrays.asList(new MoneyExceptionHandler.Erro(mensagemUsuario, mensagemDev));

        return ResponseEntity.badRequest().body(erros);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_REMOVER_LANCAMENTO') and #oauth2.hasScope('write')")
    public void remove(@PathVariable Long id){
        entryRepository.deleteById(id);
    }
}
