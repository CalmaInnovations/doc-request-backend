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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DocumentoServiceImpl implements DocumentoService {

    private final NumeroOficioRepository numeroOficioRepository;
    private final PracticanteVoluntarioService service;
    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;
    private final PracticanteVoluntarioRepository repository;
    private final BuscarService buscarService;
    private static final String UPLOAD_DIR = "src/main/resources/files/CARTA_DE_ACEPTACIÓN.xlsx";

    //private static final String BASE_URL = "https://improved-xylophone-pjrw9vj7j74q3vjw-8080.app.github.dev/api/downloadPdf";
    private static final String BASE_URL = "http://localhost:8080/api/downloadPdf";

    private static final String BUTTON_STYLE = "background-color: #00BFFF; color: white; padding: 10px 20px; text-align: center; text-decoration: none; display: inline-block; border-radius: 4px; border: none;";

    //Genera PDF y retorna el link de descarga PDF SERVICE
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
            return generarLinkDescarga(outputPath);
        } catch (IOException e) {
            throw new RuntimeException("Error al generar el PDF", e);
        }
    }
    //Obtiene el recurso PDF temporal PDF SERVICE
    @Override
    public InputStreamResource obtenerRecursoPdfTemporal(String pdfPath) throws IOException {
        File file = new File(pdfPath);
        if (!file.exists()) {
            throw new IOException("El archivo no existe en la ruta especificada: " + pdfPath);
        }
        return new InputStreamResource(new FileInputStream(file));
    }
    //Enlace de link de descarga y la ruta de la plantilla pdf PDFSERVICE
    private String generarLinkDescarga(String pdfPath) {
        return DocumentoServiceImpl.BASE_URL + "?path=" + pdfPath;
    }
    //Crear un archivo temporal pdf PDF SERVICE
    private String crearArchivoTemporal(String prefix) {
        try {
            File tempPDF = File.createTempFile(prefix, ".pdf");
            return tempPDF.getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException("Error al crear el archivo temporal", e);
        }
    }
    //Procesa la plantilla thymeleaf con los parametros adicionado PDF SERVICE
    @Override
    public String processThymeleafTemplate(String templateName, Map<String, Object> params) {
        Context context = new Context();
        context.setVariables(params);
        return templateEngine.process(templateName, context);
    }
    //PDF
    private int obtenerSiguienteNumeroSecuencial() {
        synchronized (this) {
            Optional<NumeroOficio> optionalNumeroOficio = numeroOficioRepository.findById(1L);

            NumeroOficio numeroOficio;
            if (optionalNumeroOficio.isEmpty()) {
                numeroOficio = new NumeroOficio();
                numeroOficio.setNumeroOficio(1);
            } else {
                numeroOficio = optionalNumeroOficio.get();
                numeroOficio.setNumeroOficio(numeroOficio.getNumeroOficio()+ 1);
            }

            numeroOficio = numeroOficioRepository.save(numeroOficio);

            return numeroOficio.getNumeroOficio();
        }
    }


    private String generarNumeroOficio() {

        int numeroSecuencial = obtenerSiguienteNumeroSecuencial();
        return String.format("%03d", numeroSecuencial);
    }

    public String formatearFecha(String fechaEnString) {
        LocalDate fecha = LocalDate.parse(fechaEnString);
        return fecha.format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy"));
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
            datos.put("fechaIngreso", formatearFecha(practicante.getFechaIngreso()));
            datos.put("fechaSalida", formatearFecha(practicante.getFechaSalida()));

            return datos;
        } else {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Practicante no encontrado");
            return error;
        }
    }

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
        String pdfPathCarta = crearArchivoTemporal("cartaAceptacion");
        String pdfPathConstancia = crearArchivoTemporal("constanciaAceptacion");

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
    //Realiza una comparación de datos entre el formulario y el excel
    //Valida si los campos son iguales convirtiendolos en mayúsculas y omitiendo los espacios iniciales
    // y finales
    boolean compararDatos(String resultadoData, String datoPracticate) {
        return resultadoData.trim().equalsIgnoreCase(datoPracticate.trim());
    }
    //Valida un campo con otro, en caso de error se añade a la lista de errores el campo que no coincide
    private void validarCampo(Map<String, String> datosRegistro, String clave, String valorComparar,
                              String nombreCampo, List<String> errores) {
        if (!compararDatos(datosRegistro.get(clave), valorComparar)) {
            errores.add(nombreCampo + " no coincide.");
        }
    }
    //Valida que los datos del formulario sean iguales a los datos del excel
    private void validarCoincidenciaDatos(Map<String, String> datosRegistro, PracticanteVoluntario practicante) {
        List<String> errores = new ArrayList<>();

        validarCampo(datosRegistro, "NOMBRES Y APELLIDOS", practicante.getNombresApellidos(), "Nombres y Apellidos", errores);
        validarCampo(datosRegistro, "CORREO ELECTRÓNICO", practicante.getCorreoElectronico(), "Correo electrónico", errores);
        validarCampo(datosRegistro, "DNI", practicante.getDni(), "DNI", errores);
        validarCampo(datosRegistro, "CELULAR", practicante.getCelular(), "Celular", errores);
        validarCampo(datosRegistro, "Univesidad o instituto", practicante.getUniversidadOInstituto(), "Universidad o Instituto", errores);
        validarCampo(datosRegistro, "CODIGO DE ESTUDIANTE (EN CASO NO TENGA LLENAR CON 0000)", practicante.getCodigoEstudiante(), "Código de estudiante", errores);
        validarCampo(datosRegistro, "CARRERA", practicante.getCarrera(), "Carrera", errores);
        validarCampo(datosRegistro, "TIPO DE PRÁCTICAS", practicante.getTipoPracticas(), "Tipo de prácticas", errores);
        validarCampo(datosRegistro, "ÁREA", practicante.getArea(), "Área", errores);
        validarCampo(datosRegistro, "LÍDER DEL ÁREA", practicante.getLiderArea(), "Líder de área", errores);
        validarCampo(datosRegistro, "PUESTO", practicante.getPuesto(), "Puesto", errores);
        validarCampo(datosRegistro, "INGRESO", practicante.getFechaIngreso(), "Fecha de ingreso", errores);
        validarCampo(datosRegistro, "SALIDA", practicante.getFechaSalida(), "Fecha de salida", errores);

        if (!errores.isEmpty()) {
            throw new DatosNoCoincidenException(errores);
        }
    }
    //guarda la solicitud practicante de la informacion del excel en la base de datos
    private PracticanteVoluntario guardarPracticante(Map<String, String> datosRegistro) {
        PracticanteVoluntario practicante = new PracticanteVoluntario();

        practicante.setNombresApellidos(datosRegistro.get("NOMBRES Y APELLIDOS"));
        practicante.setCorreoElectronico(datosRegistro.get("CORREO ELECTRÓNICO"));
        practicante.setDni(datosRegistro.get("DNI"));
        practicante.setCelular(datosRegistro.get("CELULAR"));
        practicante.setUniversidadOInstituto(datosRegistro.get("Univesidad o instituto"));
        practicante.setCodigoEstudiante(datosRegistro.get("CODIGO DE ESTUDIANTE (EN CASO NO TENGA LLENAR CON 0000)"));
        practicante.setCarrera(datosRegistro.get("CARRERA"));
        practicante.setTipoPracticas(datosRegistro.get("TIPO DE PRÁCTICAS"));
        practicante.setArea(datosRegistro.get("ÁREA"));
        practicante.setLiderArea(datosRegistro.get("LÍDER DEL ÁREA"));
        practicante.setPuesto(datosRegistro.get("PUESTO"));
        practicante.setFechaIngreso(datosRegistro.get("INGRESO"));
        practicante.setFechaSalida(datosRegistro.get("SALIDA"));

        return repository.save(practicante);
    }


    @Override
    public PracticanteVoluntario save(PracticanteVoluntario practicante) {
        try {
            //Obtener datos del registro
            Map<String, String> datosRegistro = buscarService.buscarRegistro(UPLOAD_DIR, practicante.getCorreoElectronico());

            //Validar que ambos datos sean iguales
            validarCoincidenciaDatos(datosRegistro, practicante);

            //Crea y guarda la información del practicante del excel en la base de datos
            PracticanteVoluntario practicanteGuardado = guardarPracticante(datosRegistro);

            //Envia la carta
            enviarCarta(practicanteGuardado.getCorreoElectronico(),
                    String.valueOf(practicanteGuardado.getIdPracticanteVoluntario()));

            return practicanteGuardado;
        } catch (IOException e) {
            throw new RuntimeException("Error al procesar el registro del practicante", e);
        }
    }
}
