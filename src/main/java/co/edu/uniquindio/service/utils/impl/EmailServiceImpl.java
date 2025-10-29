package co.edu.uniquindio.service.utils.impl;

import co.edu.uniquindio.dto.common.email.EmailDto;
import co.edu.uniquindio.service.utils.EmailService;
import lombok.RequiredArgsConstructor;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Servicio encargado de manejar el envío de correos electronicos
 * <br>
 * Esta clase utiliza la librería <b>Simple Java Mail</b> para construir y enviar correos HTML,
 * haciendo uso de plantillas almacenadas en el directorio <code>resources/templates</code>.
 */
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    // Cliente SMTP responsable de enviar los correos electrónicos
    private final Mailer mailer;

    // Dirección de correo del remitente (configurada en application.properties)
    @Value("${smtp.from.address}")
    private String fromAddress;

    // Nombre que se mostrará como remitente
    @Value("${smtp.from.name}")
    private String fromName;

    // URL base usada para generar el link de verificación
    @Value("${app.verification.base-url}")
    private String verificationBaseUrl;

    /**
     * Envía un correo electrónico de verificación de registro a un nuevo usuario.
     * <br>
     * Carga una plantilla HTML desde los recursos, reemplaza los placeholders por valores reales
     * y envía el correo al destinatario de manera asíncrona.
     */
    @Override
    @Async
    public void enviarEmailVerificacionRegistro(EmailDto emailDto) {
        try {
            // 1. Cargar la plantilla HTML desde resources
            String htmlTemplate = loadHtmlTemplate("templates/verificacion.html");

            // 2. Construir la URL de verificación con parámetros dinámicos
            String codigoLimpio = emailDto.cuerpo().replaceAll("[^a-zA-Z0-9]", "");
            String verificationUrl = verificationBaseUrl
                    + "/registroClientes?email=" + URLEncoder.encode(emailDto.destinatario(), StandardCharsets.UTF_8)
                    + "&codigo=" + URLEncoder.encode(codigoLimpio, StandardCharsets.UTF_8);

            // 3. Reemplazar los placeholders del HTML con datos reales
            String cuerpoPersonalizado = htmlTemplate
                    .replace("{{codigo}}", emailDto.cuerpo())
                    .replace("{{verification_link}}", verificationUrl);

            // 4. Crear el objeto Email con Simple Java Mail
            Email email = EmailBuilder.startingBlank()
                    .from(fromName, fromAddress)
                    .to(emailDto.destinatario())
                    .withSubject(emailDto.asunto())
                    .withHTMLText(cuerpoPersonalizado)
                    .buildEmail();

            // 5. Enviar el correo
            mailer.sendMail(email);

        } catch (IOException e) {
            // Manejo de errores si no se puede leer la plantilla
            throw new RuntimeException("Error al cargar la plantilla del correo", e);
        }
    }


    // Envía un correo de bienvenida o confirmación para usuarios registrados mediante Google OAuth.
    @Override
    @Async
    public void enviarEmailRegistroGoogle(EmailDto emailDto) {
        try {
            // 1. Cargar la plantilla para registro con Google
            String htmlTemplate = loadHtmlTemplate("templates/registroClienteGoogle.html");

            // 2. Construir el correo con la plantilla cargada
            Email email = EmailBuilder.startingBlank()
                    .from(fromName, fromAddress)
                    .to(emailDto.destinatario())
                    .withSubject(emailDto.asunto())
                    .withHTMLText(htmlTemplate)
                    .buildEmail();

            // 3. Enviar el correo
            mailer.sendMail(email);

        } catch (IOException e) {
            throw new RuntimeException("Error al cargar la plantilla del correo", e);
        }
    }

    /**
     * Envía un correo con un código dinámico (por ejemplo, para verificación 2FA o restablecimiento de contraseña).
     * <br>
     * Recibe la ruta del archivo HTML para adaptar la plantilla según el contexto.
     */
    @Override
    @Async
    public void enviarEmailCodigo(EmailDto emailDto, String rutaHtml) {
        try {
            // 1. Cargar la plantilla HTML dinámica según la ruta
            String htmlTemplate = loadHtmlTemplate("templates/" + rutaHtml);

            // 2. Insertar el código en la plantilla
            String cuerpoPersonalizado = htmlTemplate
                    .replace("{{codigo}}", emailDto.cuerpo());

            // 3. Crear el email con la información generada
            Email email = EmailBuilder.startingBlank()
                    .from(fromName, fromAddress)
                    .to(emailDto.destinatario())
                    .withSubject(emailDto.asunto())
                    .withHTMLText(cuerpoPersonalizado)
                    .buildEmail();

            // 4. Enviar el correo
            mailer.sendMail(email);

        } catch (IOException e) {
            // Manejo de errores si no se puede leer la plantilla
            throw new RuntimeException("Error al cargar la plantilla del correo", e);
        }
    }

    // Método auxiliar para cargar el contenido de un archivo HTML ubicado en resources
    private String loadHtmlTemplate(String path) throws IOException {
        // Cargar archivo como recurso del classpath
        ClassPathResource resource = new ClassPathResource(path);
        // Leer el contenido del archivo en bytes
        byte[] bytes = resource.getInputStream().readAllBytes();
        // Retornar el contenido convertido a String (UTF-8)
        return new String(bytes, StandardCharsets.UTF_8);
    }

}
