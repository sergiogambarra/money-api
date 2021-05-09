package com.gambarra.money.api.mail;

import com.gambarra.money.api.model.Entry;
import com.gambarra.money.api.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class Mailer {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine thymeleaf;



//    @EventListener
//    private void teste(ApplicationReadyEvent event) {
//        this.sendEmail("teste.sergio@gambarra.com.br",
//                Arrays.asList("sergio@gambarra.com.br"),
//                "Testando", "Olá! <br/>Teste OK!");
//        System.out.println("Terminado o envio de e-mail...");
//    }
//

//    @Autowired
//    private EntryRepository repo;
//
//    @EventListener
//    private void teste(ApplicationReadyEvent event) {
//
//        String template = "mail/aviso-lancamentos-vencidos";
//
//        List<Entry> lista = repo.findAll();
//
//        Map<String, Object> variaveis = new HashMap<>();
//        variaveis.put("entries", lista);
//
//        this.sendEmail("teste.sergio@gambarra.com.br",
//                Arrays.asList("sergio@gambarra.com.br"),
//                "Testando", template, variaveis);
//        System.out.println("Terminado o envio de e-mail...");
//    }

    public void avisarSobreLancamentosVencidos(List<Entry> vencidos, List<User> destinatarios) {
        Map<String, Object> variaveis = new HashMap<>();
        variaveis.put("entries", vencidos);

        List<String> emails = destinatarios.stream().map(u -> u.getEmail()).collect(Collectors.toList());

        this.sendEmail("teste.sergio@gambarra.com.br",
                emails,
                "Lançamentos vencidos.",
                "mail/aviso-lancamentos-vencidos",
                variaveis);

    }

    public void sendEmail (String remetente,
                           List<String> destinatarios, String assunto, String template,
                           Map<String, Object> variables ){

        Context context = new Context(new Locale("pt", "BR"));

        variables.entrySet().forEach(e -> context.setVariable(e.getKey(), e.getValue()));

        String message = thymeleaf.process(template, context);

        this.sendEmail(remetente, destinatarios, assunto, message);

    }

    public void sendEmail (String remetente,
                           List<String> destinatarios, String assunto, String mensagem){

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setFrom(remetente);
            helper.setTo(destinatarios.toArray(new String[destinatarios.size()]));
            helper.setSubject(assunto);
            helper.setText(mensagem, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e){
            throw new RuntimeException("Problemas com o envio de e-mail!", e);
        }



    }
}
