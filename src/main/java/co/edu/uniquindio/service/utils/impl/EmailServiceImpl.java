package co.edu.uniquindio.service.utils.impl;

import co.edu.uniquindio.dto.common.email.EmailDto;
import co.edu.uniquindio.models.entities.objects.compra.Factura;
import co.edu.uniquindio.models.entities.users.Cliente;
import co.edu.uniquindio.service.utils.EmailService;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
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


    @Override
    @Async
    public void enviarFacturaEmail(Cliente cliente, Factura factura) {
        try {
            // Load HTML template
            String htmlTemplate = loadHtmlTemplate("templates/factura.html");

            // Build HTML items
            StringBuilder itemsHtml = new StringBuilder();
            factura.getCompra().getDetalleCompra().forEach(detalle -> {
                itemsHtml.append("<tr>")
                        .append("<td>").append(detalle.getProducto().getNombre()).append("</td>")
                        .append("<td>").append(detalle.getCantidad()).append("</td>")
                        .append("<td>$").append(detalle.getProducto().getValorVenta()).append("</td>")
                        .append("<td>$").append(detalle.getValorDetalle()).append("</td>")
                        .append("</tr>");
            });

            // Replace variables
            String cuerpoHtml = htmlTemplate
                    .replace("{{cliente}}", cliente.getNombre())
                    .replace("{{correo}}", cliente.getUser().getEmail())
                    .replace("{{fecha}}", factura.getFechaCompra().toString())
                    .replace("{{idCompra}}", String.valueOf(factura.getCompra().getId()))
                    .replace("{{items}}", itemsHtml.toString())
                    .replace("{{subTotal}}", String.format("%.2f", factura.getSubTotal()))
                    .replace("{{iva}}", String.format("%.2f", factura.getIva()))
                    .replace("{{total}}", String.format("%.2f", factura.getTotalPaga()));

            // Generate PDF from HTML
            byte[] pdfBytes = HtmlToPdfConverter.convertHtmlToPdf(cuerpoHtml);

            // Send email with SendGrid attachment
            sendWithAttachment(
                    cliente.getUser().getEmail(),
                    "Factura de tu compra en Store-It!",
                    "Adjunto encontrarás tu factura en formato PDF.",
                    pdfBytes,
                    "factura_" + factura.getId() + ".pdf"
            );

        } catch (IOException e) {
            throw new RuntimeException("Error generando factura PDF", e);
        }
    }


    private void sendWithAttachment(String destinatario, String asunto, String mensaje, byte[] pdfBytes, String fileName) throws IOException {
        Email from = new Email(fromAddress, fromName);
        Email to = new Email(destinatario);
        Content content = new Content("text/plain", mensaje);

        Mail mail = new Mail(from, asunto, to, content);

        Attachments attachment = new Attachments();
        String encoded = java.util.Base64.getEncoder().encodeToString(pdfBytes);
        attachment.setContent(encoded);
        attachment.setType("application/pdf");
        attachment.setFilename(fileName);
        attachment.setDisposition("attachment");
        mail.addAttachments(attachment);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sg.api(request);
        System.out.println("📧 SendGrid response: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());
        System.out.println("Response headers: " + response.getHeaders());


    }
}

