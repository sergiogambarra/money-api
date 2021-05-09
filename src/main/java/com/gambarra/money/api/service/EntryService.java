package com.gambarra.money.api.service;

import com.gambarra.money.api.dto.EntryStatisticPerson;
import com.gambarra.money.api.mail.Mailer;
import com.gambarra.money.api.model.Entry;
import com.gambarra.money.api.model.Person;
import com.gambarra.money.api.model.User;
import com.gambarra.money.api.repository.EntryRepository;
import com.gambarra.money.api.repository.PersonRepository;
import com.gambarra.money.api.repository.UserRepository;
import com.gambarra.money.api.service.exception.PersonNonexistentOrInactiveException;
import com.gambarra.money.api.storage.S3;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Service
public class EntryService {

    private static final String DESTINATARIOS = "ROLE_PESQUISAR_LANCAMENTO";

    private static final Logger logger = LoggerFactory.getLogger(EntryService.class);

    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private Mailer mailer;

    @Autowired
    private S3 s3;

//    @Scheduled(fixedDelay = 1000 * 60 * 30)
    @Scheduled(cron = "0 0 6 * * *")
    public void alertAboutEntryOverdue(){
        if (logger.isDebugEnabled()){
            logger.debug("Preparando envio de emails de aviso de lançamentos vencidos.");
        }

        List<Entry> vencidos = entryRepository
                .findByDueDateLessThanEqualAndPayDateIsNull(LocalDate.now());

        if (vencidos.isEmpty()) {
            logger.debug("Sem lançamentos vencidos para aviso.");
            return;
        }

        logger.info("Existem {} lançamentos vencidos." , vencidos.size());

        List<User> destinatarios = userRepository
                .findByPermissionsDescription(DESTINATARIOS);

        if (destinatarios.isEmpty()) {
            logger.warn("Existem lançamentos vencidos, mas o sistema nao encontrou destinatarios.");
            return;
        }

        mailer.avisarSobreLancamentosVencidos(vencidos, destinatarios);

        logger.info("Envio de email de aviso concluído.");
    }

    public byte[] reportByPerson (LocalDate start, LocalDate end) throws Exception {

        List<EntryStatisticPerson> personList = entryRepository.byPerson(start, end);

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("DT_INICIO", Date.valueOf(start));
        parametros.put("DT_FIM", Date.valueOf(end));
        parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));


        //load file and compile it
        File file = ResourceUtils.getFile("classpath:entries-by-person.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(personList);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }


    public Entry update(Long id, Entry entry) {
        Entry entrySaved = searchExistingEntry(id);
        if (!entry.getPerson().equals(entrySaved.getPerson())){
            validatePerson(entry);
        }

        if (StringUtils.isEmpty(entry.getAttachment())
                && StringUtils.hasText(entry.getAttachment())){
            s3.remove(entrySaved.getAttachment());
        } else if (StringUtils.hasText(entry.getAttachment())
            && !entry.getAttachment().equals(entrySaved.getAttachment())) {
            s3.replace(entrySaved.getAttachment(), entry.getAttachment());
        }

        BeanUtils.copyProperties(entry, entrySaved, "id");

        return entryRepository.save(entrySaved);
    }

    private Entry searchExistingEntry(Long id) {
        Optional<Entry> entrySaved = entryRepository.findById(id);
        if (!entrySaved.isPresent()){
            throw new EmptyResultDataAccessException(1);
        }
        return entrySaved.get();
    }

    private void validatePerson(Entry entry) {
        Person person = null;
        if (entry.getPerson().getId() != null){
            person = personRepository.getOne(entry.getPerson().getId());
        }

        if (person == null || person.isInactive()){
            throw new PersonNonexistentOrInactiveException();
        }
    }

    public Entry save(Entry entry) {
        Person person = personRepository.findById(entry.getPerson().getId()).orElse(null);

        if (StringUtils.hasText(entry.getAttachment())){
            s3.save(entry.getAttachment());
        }
        return entryRepository.save(entry);
    }
}
