package com.calma.DocManagerServer.services.serviceImpl;

import com.calma.DocManagerServer.services.PdfService;
import com.itextpdf.html2pdf.HtmlConverter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class PdfServiceImpl implements PdfService {
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private JavaMailSender mailSender;
    @Override
    public void generarPdf(Map<String, Object> datos, String outputPath) throws IOException {
        String htmlContent = processThymeleafTemplate("cartaAceptacion", datos);

        // Convertir el HTML generado en un archivo PDF
        HtmlConverter.convertToPdf(htmlContent, new FileOutputStream(outputPath));

    }

    public String processThymeleafTemplate(String templateName, Map<String, Object> params) {
        Context context = new Context();
        context.setVariables(params);
        return templateEngine.process(templateName, context); // Procesar plantilla y devolver HTML
    }

    @Override
    public void enviarCarta(String email, String downloadUrl) throws MessagingException {
        // Enviar correo con carta de aceptación adjunta
        try {

            String htmlContent = "<h1>Carta de Aceptación</h1>" +
                    "<p>Estimado/a, adjunto encontrará su carta de aceptación.</p>" +
                    "<p><a href=\"" + downloadUrl + "\" " +
                    "style=\"background-color: #4CAF50; color: white; padding: 10px 20px; text-align: center; " +
                    "text-decoration: none; display: inline-block; border-radius: 4px;\">Descargar Carta</a></p>";


            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(email);
            helper.setSubject("Carta de Aceptación");
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);


        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
