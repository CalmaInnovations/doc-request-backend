package com.calma.DocManagerServer.services.serviceImpl;

import com.calma.DocManagerServer.services.DocumentoService;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DocumentoServiceImpl implements DocumentoService {

    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;
    private static final Map<String, Object> datos = new HashMap<>();


    private static final String BASE_URL = "http://localhost:8080/api/downloadPdf";
    private static final String BUTTON_STYLE = "background-color: #4CAF50; color: white; padding: 10px 20px; text-align: center; text-decoration: none; display: inline-block; border-radius: 4px;";

    @Override
    public String generarPdf(Map<String, Object> datos, String outputPath) {
        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            String htmlContent = processThymeleafTemplate("templateCartaAceptacion", datos);
            PdfWriter writer = new PdfWriter(fos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            pdfDoc.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdfDoc);
            document.setMargins(0, 0, 0, 0);
            HtmlConverter.convertToPdf(htmlContent, fos);
            return generarLinkDescarga(BASE_URL, outputPath);
        } catch (IOException e) {
            throw new RuntimeException("Error al generar el PDF", e);
        }
    }

    private String generarLinkDescarga(String baseUrl, String pdfPath) {
        return baseUrl + "?path=" + pdfPath;
    }

    private String crearArchivoTemporal(String prefix, String suffix) {
        try {
            File tempPDF = File.createTempFile(prefix, suffix);
            return tempPDF.getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException("Error al crear el archivo temporal", e);
        }
    }

    public String processThymeleafTemplate(String templateName, Map<String, Object> params) {
        Context context = new Context();
        context.setVariables(params);
        return templateEngine.process(templateName, context);
    }
    /*  Recibir data  */
    public void recibirDatos(){

        datos.put("nombresApellidos",  "FUNDACIÓN CALMA");
        datos.put("correoElectronico","20600792220");
        datos.put("dni","Sebastian Adauco Beteta Espinoza");
        datos.put("celular", "75253829");
        datos.put("universidadOInstituto", "Automatización");
        datos.put("codigoEstudiante", "Prácticas Pre Profesionales");
        datos.put("carrera", "Ingeniería de Software");
        datos.put("tipoPracticas", "03 de octubre de 2024");
        datos.put("area", "03 de abril de 2025");
        datos.put("liderArea", "Cindy Shirley Martinez Jimenez");
        datos.put("puesto", "San Isidro, 10 de octubre de 2024");
        datos.put("fechaIngreso",  "San Isidro, 10 de octubre de 2024");
        datos.put("fechaSalida", "San Isidro, 10 de octubre de 2024");
    }

    @Override
    public void enviarCarta(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El correo electrónico no puede estar vacío.");
        }
        recibirDatos();
        String email2 = "sabeteta03@gmail.com";
        String pdfPath = crearArchivoTemporal("cartaAceptacion", ".pdf");
        String linkDescarga = generarPdf(datos, pdfPath);

        String htmlContent = generarContenidoCorreo(linkDescarga);

        enviarCorreo(email, email2,  htmlContent);

    }


    private String generarContenidoCorreo(String downloadUrl) {
        return "<h1>Carta de Aceptación</h1>" +
                "<p>Estimado/a, adjunto encontrará su carta de aceptación.</p>" +
                "<p><a href=\"" + downloadUrl + "\" style=\"" + BUTTON_STYLE + "\">Descargar Carta</a></p>";
    }

    private void enviarCorreo(String email, String email2, String htmlContent) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(email);
            helper.setSubject("Carta de Aceptación");
            helper.setText(htmlContent, true);


            helper.setTo(email2);
            helper.setSubject("Carta de Aceptación para el Lider de Area");
            helper.setText(htmlContent, true);



            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo", e);
        }
    }
}
