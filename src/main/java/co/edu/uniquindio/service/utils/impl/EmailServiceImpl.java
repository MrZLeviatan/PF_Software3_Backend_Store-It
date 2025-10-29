package co.edu.uniquindio.service.utils.impl;

import co.edu.uniquindio.dto.common.email.EmailDto;
import co.edu.uniquindio.service.utils.EmailService;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 🇺🇸 Service responsible for sending HTML emails using SendGrid API (HTTPS, not SMTP)
 * 🇪🇸 Servicio encargado de enviar correos HTML usando la API HTTP de SendGrid (no SMTP)
 */
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Value("${smtp.from.address}")
    private String fromAddress;

    @Value("${smtp.from.name}")
    private String fromName;

    @Value("${app.verification.base-url}")
    private String verificationBaseUrl;

    @Override
    @Async
    public void enviarEmailVerificacionRegistro(EmailDto emailDto) {
        try {
            String htmlTemplate = loadHtmlTemplate("templates/verificacion.html");
            String codigoLimpio = emailDto.cuerpo().replaceAll("[^a-zA-Z0-9]", "");
            String verificationUrl = verificationBaseUrl
                    + "/registroClientes?email=" + URLEncoder.encode(emailDto.destinatario(), StandardCharsets.UTF_8)
                    + "&codigo=" + URLEncoder.encode(codigoLimpio, StandardCharsets.UTF_8);

            String cuerpoPersonalizado = htmlTemplate
                    .replace("{{codigo}}", emailDto.cuerpo())
                    .replace("{{verification_link}}", verificationUrl);

            sendWithSendGrid(emailDto.destinatario(), emailDto.asunto(), cuerpoPersonalizado);

        } catch (IOException e) {
            throw new RuntimeException("Error al cargar la plantilla del correo", e);
        }
    }

    @Override
    @Async
    public void enviarEmailRegistroGoogle(EmailDto emailDto) {
        try {
            String htmlTemplate = loadHtmlTemplate("templates/registroClienteGoogle.html");
            sendWithSendGrid(emailDto.destinatario(), emailDto.asunto(), htmlTemplate);
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar la plantilla del correo", e);
        }
    }

    @Override
    @Async
    public void enviarEmailCodigo(EmailDto emailDto, String rutaHtml) {
        try {
            String htmlTemplate = loadHtmlTemplate("templates/" + rutaHtml);
            String cuerpoPersonalizado = htmlTemplate.replace("{{codigo}}", emailDto.cuerpo());
            sendWithSendGrid(emailDto.destinatario(), emailDto.asunto(), cuerpoPersonalizado);
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar la plantilla del correo", e);
        }
    }

    private void sendWithSendGrid(String destinatario, String asunto, String contenidoHtml) throws IOException {
        Email from = new Email(fromAddress, fromName);
        Email to = new Email(destinatario);
        Content content = new Content("text/html", contenidoHtml);
        Mail mail = new Mail(from, asunto, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sg.api(request);
        System.out.println("📧 SendGrid response: " + response.getStatusCode());
    }

    private String loadHtmlTemplate(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }
}

