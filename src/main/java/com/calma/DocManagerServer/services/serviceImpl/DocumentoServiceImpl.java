package com.calma.DocManagerServer.services.serviceImpl;

import com.calma.DocManagerServer.model.PracticanteVoluntario;
import com.calma.DocManagerServer.repository.PracticanteVoluntarioRepository;
import com.calma.DocManagerServer.services.DocumentoService;
import com.calma.DocManagerServer.services.PracticanteVoluntarioService;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocumentoServiceImpl implements DocumentoService {

    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;
    private static final Map<String, Object> datos = new HashMap<>();

    private static final String BASE_URL = "http://localhost:8080/api/downloadPdf";
    private static final String BUTTON_STYLE = "background-color: #4CAF50; color: white; padding: 10px 20px; text-align: center; text-decoration: none; display: inline-block; border-radius: 4px;";

    @Override
    public String generarPdf(Map<String, Object> datos, String templateName, String outputPath) {
        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            String htmlContent = processThymeleafTemplate(templateName, datos);
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

    @Override
    public InputStreamResource obtenerRecursoPdfTemporal(String pdfPath) throws IOException {
        File file = new File(pdfPath);
        if (!file.exists()) {
            throw new IOException("El archivo no existe en la ruta especificada: " + pdfPath);
        }
        return new InputStreamResource(new FileInputStream(file));
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

    public Map<String, Object> recibirDatos(String id) {
        Optional<PracticanteVoluntario> optionalPracticante = service.findById(Long.valueOf(id));

        if (optionalPracticante.isPresent()) {
            PracticanteVoluntario practicante = optionalPracticante.get();

            Map<String, Object> datos = new HashMap<>();
            datos.put("nombresApellidos", practicante.getNombresApellidos());
            datos.put("correoElectronico", practicante.getCorreoElectronico());
            datos.put("dni", practicante.getDni());
            datos.put("celular", practicante.getCelular());
            datos.put("universidadOInstituto", practicante.getUniversidadOInstituto());
            datos.put("codigoEstudiante", practicante.getCodigoEstudiante());
            datos.put("carrera", practicante.getCarrera());
            datos.put("tipoPracticas", practicante.getTipoPracticas());
            datos.put("area", practicante.getArea());
            datos.put("liderArea", practicante.getLiderArea());
            datos.put("puesto", practicante.getPuesto());
            datos.put("fechaIngreso", practicante.getFechaIngreso());
            datos.put("fechaSalida", practicante.getFechaSalida());

            return datos;
        } else {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Practicante no encontrado");
            return error;
        }
    }

    @Autowired
    private PracticanteVoluntarioService service;

    @Override
    public void enviarCarta(String email, String id) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El correo electrónico no puede estar vacío.");
        }

        Map<String, Object> data = recibirDatos(id);
        String emailLiderArea = "sabeteta03@gmail.com";

        // Generar ambos PDFs
        String pdfPathCarta = crearArchivoTemporal("cartaAceptacion", ".pdf");
        String pdfPathConstancia = crearArchivoTemporal("constanciaAceptacion", ".pdf");

        String linkDescargaCarta = generarPdf(data, "templateCartaAceptacion", pdfPathCarta);
        String linkDescargaConstancia = generarPdf(data, "templateConstanciaAceptacion", pdfPathConstancia);

        String htmlContent = generarContenidoCorreo(linkDescargaCarta, linkDescargaConstancia);

        // Enviar al practicante
        enviarCorreo(email, htmlContent, "Documentos de Aceptación");

        // Enviar al líder de área
        enviarCorreo(emailLiderArea, htmlContent, "Documentos de Aceptación para el Líder de Área");
    }

    private String generarContenidoCorreo(String linkCarta, String linkConstancia) {
        return "<h1>Documentos de Aceptación</h1>" +
                "<p>Estimado/a, adjunto encontrará los documentos de aceptación.</p>" +
                "<p><a href='" + linkCarta + "' style='" + BUTTON_STYLE + "'>Descargar Carta de Aceptación</a></p>" +
                "<p><a href='" + linkConstancia + "' style='" + BUTTON_STYLE + "'>Descargar Constancia de Aceptación</a></p>";
    }

    private void enviarCorreo(String email, String htmlContent, String subject) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo", e);
        }
    }

    @Autowired
    private PracticanteVoluntarioRepository repository;

    @Autowired
    private BuscarService buscarService;


    @Override
    public PracticanteVoluntario save(PracticanteVoluntario practicante) {
        StringBuilder diferencias = new StringBuilder();

        try {
            Map<String, String> resultado = buscarService.buscarRegistro("C:\\prueba\\Prueba1.xlsx", practicante.getCorreoElectronico());


            if (!resultado.get("nombresApellidos").equals(practicante.getNombresApellidos())) {
                diferencias.append("Nombres y Apellidos no coinciden. ");
            }
            if (!resultado.get("correoElectronico").equals(practicante.getCorreoElectronico())) {
                diferencias.append("Correo electrónico no coincide. ");
            }
            if (!resultado.get("dni").equals(practicante.getDni())) {
                diferencias.append("DNI no coincide. ");
            }
            if (!resultado.get("celular").equals(practicante.getCelular())) {
                diferencias.append("Celular no coincide. ");
            }
            if (!resultado.get("universidadOInstituto").equals(practicante.getUniversidadOInstituto())) {
                diferencias.append("Universidad o Instituto no coincide. ");
            }
            if (!resultado.get("codigoEstudiante").equals(practicante.getCodigoEstudiante())) {
                diferencias.append("Código de estudiante no coincide. ");
            }
            if (!resultado.get("carrera").equals(practicante.getCarrera())) {
                diferencias.append("Carrera no coincide. ");
            }
            if (!resultado.get("tipoPracticas").equals(practicante.getTipoPracticas())) {
                diferencias.append("Tipo de prácticas no coincide. ");
            }
            if (!resultado.get("area").equals(practicante.getArea())) {
                diferencias.append("Área no coincide. ");
            }
            if (!resultado.get("liderArea").equals(practicante.getLiderArea())) {
                diferencias.append("Líder de área no coincide. ");
            }
            if (!resultado.get("puesto").equals(practicante.getPuesto())) {
                diferencias.append("Puesto no coincide. ");
            }
            if (!resultado.get("fechaIngreso").equals(practicante.getFechaIngreso())) {
                diferencias.append("Fecha de ingreso no coincide. ");
            }
            if (!resultado.get("fechaSalida").equals(practicante.getFechaSalida())) {
                diferencias.append("Fecha de salida no coincide. ");
            }

            // Si existen diferencias, lanzamos una excepción con los detalles
            if (diferencias.length() > 0) {
                throw new RuntimeException("Las siguientes diferencias fueron encontradas: " + diferencias.toString());
            }
            PracticanteVoluntario savedPracticante = repository.save(practicante);

            enviarCarta(savedPracticante.getCorreoElectronico(), String.valueOf(savedPracticante.getIdPracticanteVoluntario()));

            return savedPracticante;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
