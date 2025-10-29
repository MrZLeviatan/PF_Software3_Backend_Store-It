package co.edu.uniquindio.controller.bannerPrincipal;

import co.edu.uniquindio.dto.MensajeDto;
import co.edu.uniquindio.dto.common.auth.VerificacionCodigoDto;
import co.edu.uniquindio.dto.common.google.GoogleTokenRequest;
import co.edu.uniquindio.dto.common.google.GoogleUserResponse;
import co.edu.uniquindio.dto.users.cliente.RegistroClienteDto;
import co.edu.uniquindio.dto.users.cliente.RegistroClienteGoogleDto;
import co.edu.uniquindio.exceptions.*;
import co.edu.uniquindio.service.users.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ¡Controlador REST encargado de gestionar el registro, validación
 * y verificación de clientes en la aplicación Store-It!.
 */
@RestController
@RequestMapping("/api/store-it")
@RequiredArgsConstructor
public class RegistroClienteController {


    // Servicio encargado de la lógica de negocio relacionada con los clientes.
    private final ClienteService clienteService;


    //  Endpoint para el registro de clientes utilizando credenciales tradicionales (sin autenticación externa).
    @PostMapping("/clientes/registro")
    public ResponseEntity<MensajeDto<String>> registrarCliente(
            @Valid @RequestBody RegistroClienteDto registroClienteDto)
            throws ElementoRepetidoException, ElementoNulosException, ElementoEliminadoException, ElementoNoValidoException {
        // Llamar al servicio de clientes para registrar al nuevo cliente.
        clienteService.registrarCliente(registroClienteDto);
        // Retornar una respuesta HTTP 200 con un mensaje de éxito.
        return ResponseEntity.ok().body(new MensajeDto<>(false,"Registro logrado exitosamente."));
    }


    // Endpoint para validar un token de Google. Enviado desde el frontend,
    @PostMapping("/clientes/validate-google")
    public ResponseEntity<MensajeDto<GoogleUserResponse>> validarGoogle(@RequestBody GoogleTokenRequest googleTokenRequest)
            throws ElementoIncorrectoException {

        // Validar el token con Google y obtener la información del usuario.
        GoogleUserResponse googleUserResponse = clienteService.validarToken(googleTokenRequest.idToken());

        // Devolver los datos del usuario dentro de una respuesta exitosa.
        return ResponseEntity.status(200).body(new MensajeDto<>(false,googleUserResponse));
    }


    // Endpoint para el registro de clientes utilizando una cuenta de Google.
    @PostMapping("/clientes/registro/google")
    public ResponseEntity<MensajeDto<String>> registrarClienteGoogle(
            @Valid @RequestBody RegistroClienteGoogleDto registroClienteGoogleDto)
            throws ElementoRepetidoException, ElementoNulosException, ElementoEliminadoException, ElementoNoValidoException {

        // Llamar al servicio de clientes para registrar con Google.
        clienteService.registroClienteGoogle(registroClienteGoogleDto);

        return ResponseEntity.ok().body(new MensajeDto<>(false,"Registro logrado exitosamente."));
    }


    // Endpoint para verificar la cuenta de un cliente con un código.
    @PostMapping("/clientes/verificar")
    public ResponseEntity<MensajeDto<String>> verificarRegistroCliente(
            @Valid @RequestBody VerificacionCodigoDto verificacionCodigoDto)
            throws ElementoNoEncontradoException, ElementoNoCoincideException, ElementoIncorrectoException {
        // Verificar el código de registro asociado al cliente.
        clienteService.verificacionCliente(verificacionCodigoDto);
        return ResponseEntity.ok(new MensajeDto<>(false ,"Cliente verificado con éxito."));
    }


    // Endpoint para verificar una cuenta de cliente a través de un enlace de verificación por correo electrónico.
    @GetMapping("/clientes/verificar/email/{email}/codigo/{codigo}")
    public ResponseEntity<MensajeDto<String>> verificarClienteEmail(
            @PathVariable String email,
            @PathVariable String codigo)
            throws ElementoIncorrectoException, ElementoNoCoincideException, ElementoNoEncontradoException {

        // Validar la cuenta del cliente a través del enlace recibido en su correo.
        clienteService.verificacionClienteLink(email,codigo);
        return ResponseEntity.ok(new MensajeDto<>(false ,"Cliente verificado con éxito."));

    }

}
