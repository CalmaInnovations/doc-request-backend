package com.calma.DocManagerServer.services.serviceImpl;

import com.calma.DocManagerServer.exception.DatosNoCoincidenException;
import com.calma.DocManagerServer.model.NumeroOficio;
import com.calma.DocManagerServer.model.PracticanteVoluntario;
import com.calma.DocManagerServer.repository.NumeroOficioRepository;
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
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DocumentoServiceImpl implements DocumentoService {
    @Autowired
    private NumeroOficioRepository numeroOficioRepository;

    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;
    private static final String UPLOAD_DIR = "src/main/resources/files/Prueba1.xlsx";

    private static final String BASE_URL = "http://localhost:8080/api/downloadPdf";
    private static final String BUTTON_STYLE = "background-color: #00BFFF; color: white; padding: 10px 20px; text-align: center; text-decoration: none; display: inline-block; border-radius: 4px; border: none;";

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


    private String generarNumeroOficio() {

        int numeroSecuencial = obtenerSiguienteNumeroSecuencial();
        return String.format("%03d", numeroSecuencial);
    }

    private int obtenerSiguienteNumeroSecuencial() {
        synchronized (this) {
            Optional<NumeroOficio> optionalNumeroOficio = numeroOficioRepository.findById(1L);

            NumeroOficio numeroOficio;
            if (optionalNumeroOficio.isEmpty()) {
                numeroOficio = new NumeroOficio();
                numeroOficio.setNumeroOficio(1);
            } else {
                numeroOficio = optionalNumeroOficio.get();
                numeroOficio.setNumeroOficio(numeroOficio.getNumeroOficio()+ + 1);
            }

            numeroOficio = numeroOficioRepository.save(numeroOficio);

            return numeroOficio.getNumeroOficio();
        }
    }

    public Map<String, Object> recibirDatos(String id) {
        Optional<PracticanteVoluntario> optionalPracticante = service.findById(Long.valueOf(id));
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

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
        data.put("fechaDocumento", LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
        data.put("numeroOficio", generarNumeroOficio());
        // Generar ambos PDFs
        String pdfPathCarta = crearArchivoTemporal("cartaAceptacion", ".pdf");
        String pdfPathConstancia = crearArchivoTemporal("constanciaAceptacion", ".pdf");

        String linkDescargaCarta = generarPdf(data, "templateCartaAceptacion", pdfPathCarta);
        String linkDescargaConstancia = generarPdf(data, "templateConstanciaAceptacion", pdfPathConstancia);

        String htmlContent = generarContenidoCorreo((String) data.get("nombresApellidos"),linkDescargaCarta, linkDescargaConstancia, (String) data.get("area"));
        String htmlContentLider = generarContenidoCorreoJefe((String) data.get("liderArea"), (String) data.get("nombresApellidos"), linkDescargaCarta, linkDescargaConstancia, (String) data.get("area"));
        // Enviar al practicante
        enviarCorreo(email, htmlContent, "Documentos de Aceptación");

        // Enviar al líder de área
        enviarCorreo(emailLiderArea, htmlContentLider, "Documentos de Aceptación para el Líder de Área");
    }

    private String generarContenidoCorreo(String nombrePasante, String linkCarta, String linkConstancia, String area) {
        return "<h1>Documentos de Aceptación</h1>" +
                "<p>Buenas tardes estimado(a) " + nombrePasante + ",</p>" +
                "<p>Adjunto encontrará el documento firmado, entregado para los fines correspondientes. Agradecemos de antemano que confirme la recepción del mismo a través de este medio.</p>" +
                "<p>En caso de que tenga alguna inquietud o requiera realizar alguna corrección, por favor no dude en ponerse en contacto con el área en un plazo de 2 días. Luego de la emisión del documento, si no recibimos tu confirmación, consideraremos que el documento fue entregado correctamente. De esta manera, no estaremos ligados a futuros reclamos relacionados con la entrega del certificado.</p>" +
                "<p>Quedamos atentos a su respuesta.</p>" +
                "<p><a href='" + linkCarta + "' style='" + BUTTON_STYLE + "'>Descargar Carta de Aceptación</a></p>" +
                "<p><a href='" + linkConstancia + "' style='" + BUTTON_STYLE + "'>Descargar Constancia de Aceptación</a></p>" +
                "<p>Muchas gracias por su atención,</p>" +
                "<p>Saludos cordiales,</p>" +
                "<p>"+ area + "</p>";
    }

    private String generarContenidoCorreoJefe(String nombreJefe, String nombrePasante, String linkCarta, String linkConstancia, String area) {
        return "<h1>Documentos de Aceptación - Prueba Experimental</h1>" +
                "<p>Estimado/a " + nombreJefe + ",</p>" +
                "<p>Le informamos que el documento firmado para el pasante " + nombrePasante + " ha sido generado como parte de una prueba experimental en el proceso de aceptación de documentos.</p>" +
                "<p>Adjunto encontrará los enlaces para descargar la Carta de Aceptación y la Constancia de Aceptación del pasante. Esta prueba tiene como objetivo verificar la correcta generación y entrega de estos documentos.</p>" +
                "<p>Es importante señalar que, si bien este proceso está siendo implementado, se trata de una prueba experimental y cualquier comentario o recomendación para mejorar el procedimiento será bien recibido.</p>" +
                "<p>En caso de que se requiera realizar alguna corrección o ajuste en los documentos, no dude en contactarnos a la mayor brevedad posible.</p>" +
                "<p><a href='" + linkCarta + "' style='" + BUTTON_STYLE + "'>Descargar Carta de Aceptación</a></p>" +
                "<p><a href='" + linkConstancia + "' style='" + BUTTON_STYLE + "'>Descargar Constancia de Aceptación</a></p>" +
                "<p>Agradecemos su atención y quedamos atentos a cualquier comentario que pueda tener.</p>" +
                "<p>Saludos cordiales,</p>" +
                "<p>'"+ area+"' </p>";
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

    boolean compararDatos(String resultadoData, String datoPracticate) {
        return resultadoData.trim().toUpperCase().equals(datoPracticate.trim().toUpperCase());
    }
    @Override
    public PracticanteVoluntario save(PracticanteVoluntario practicante) {
        StringBuilder diferencias = new StringBuilder();
        List<String> errores = new ArrayList<>();

        try {
            Map<String, String> resultado = buscarService.buscarRegistro(UPLOAD_DIR , practicante.getCorreoElectronico());

            // Función auxiliar para comparar las cadenas


            // Comparar cada campo y agregar errores si hay diferencias
            if (!compararDatos(resultado.get("nombresApellidos"), practicante.getNombresApellidos())) {
                errores.add("Nombres y Apellidos no coinciden.");
            }
            if (!compararDatos(resultado.get("correoElectronico"), practicante.getCorreoElectronico())) {
                errores.add("Correo electrónico no coincide.");
            }
            if (!compararDatos(resultado.get("dni"), practicante.getDni())) {
                errores.add("DNI no coincide.");
            }
            if (!compararDatos(resultado.get("celular"), practicante.getCelular())) {
                errores.add("Celular no coincide.");
            }
            if (!compararDatos(resultado.get("universidadOInstituto"), practicante.getUniversidadOInstituto())) {
                errores.add("Universidad o Instituto no coincide.");
            }
            if (!compararDatos(resultado.get("codigoEstudiante"), practicante.getCodigoEstudiante())) {
                errores.add("Código de estudiante no coincide.");
            }
            if (!compararDatos(resultado.get("carrera"), practicante.getCarrera())) {
                errores.add("Carrera no coincide.");
            }
            if (!compararDatos(resultado.get("tipoPracticas"), practicante.getTipoPracticas())) {
                errores.add("Tipo de prácticas no coincide.");
            }
            if (!compararDatos(resultado.get("area"), practicante.getArea())) {
                errores.add("Área no coincide.");
            }
            if (!compararDatos(resultado.get("liderArea"), practicante.getLiderArea())) {
                errores.add("Líder de área no coincide.");
            }
            if (!compararDatos(resultado.get("puesto"), practicante.getPuesto())) {
                errores.add("Puesto no coincide.");
            }
            if (!compararDatos(resultado.get("fechaIngreso"), practicante.getFechaIngreso())) {
                errores.add("Fecha de ingreso no coincide.");
            }
            if (!compararDatos(resultado.get("fechaSalida"), practicante.getFechaSalida())) {
                errores.add("Fecha de salida no coincide.");
            }

            // Si existen diferencias, devolvemos las respuestas con los errores
            if (!errores.isEmpty()) {
                throw new DatosNoCoincidenException(errores);
            }

            PracticanteVoluntario practicanteVoluntario = new PracticanteVoluntario();

            practicanteVoluntario.setNombresApellidos(resultado.get("nombresApellidos"));
            practicanteVoluntario.setCorreoElectronico(resultado.get("correoElectronico"));
            practicanteVoluntario.setDni(resultado.get("dni"));
            practicanteVoluntario.setCelular(resultado.get("celular"));
            practicanteVoluntario.setUniversidadOInstituto(resultado.get("universidadOInstituto"));
            practicanteVoluntario.setCodigoEstudiante(resultado.get("codigoEstudiante"));
            practicanteVoluntario.setCarrera(resultado.get("carrera"));
            practicanteVoluntario.setTipoPracticas(resultado.get("tipoPracticas"));
            practicanteVoluntario.setArea(resultado.get("area"));
            practicanteVoluntario.setLiderArea(resultado.get("liderArea"));
            practicanteVoluntario.setPuesto(resultado.get("puesto"));
            practicanteVoluntario.setFechaIngreso(resultado.get("fechaIngreso"));
            practicanteVoluntario.setFechaSalida(resultado.get("fechaSalida"));


            // Si no hay errores, guardamos el practicante y enviamos la carta
            PracticanteVoluntario savedPracticante = repository.save(practicanteVoluntario);
            enviarCarta(practicanteVoluntario.getCorreoElectronico(), String.valueOf(practicanteVoluntario.getIdPracticanteVoluntario()));

            // Retornamos el objeto guardado
            return savedPracticante;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
