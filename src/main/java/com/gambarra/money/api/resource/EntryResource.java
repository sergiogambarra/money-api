package com.gambarra.money.api.resource;

import com.gambarra.money.api.exceptionhandler.MoneyExceptionHandler;
import com.gambarra.money.api.repository.EntryRepository;
import com.gambarra.money.api.repository.filter.EntryFilter;
import com.gambarra.money.api.repository.projection.EntryResume;
import com.gambarra.money.api.service.EntryService;
import com.gambarra.money.api.service.exception.PersonNonexistentOrInactiveException;
import com.gambarra.money.api.event.ResourceCreatedEvent;
import com.gambarra.money.api.model.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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
        return entry.isPresent() ? ResponseEntity.ok(entry) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
    public ResponseEntity<Entry> create(@Valid @RequestBody Entry entry, HttpServletResponse response) {
        Entry entrySaved = entryService.save(entry);

        publisher.publishEvent(new ResourceCreatedEvent(this, response, entrySaved.getId()));

        return ResponseEntity.status(HttpStatus.CREATED).body(entrySaved);
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
