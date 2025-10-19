package co.edu.uniquindio.controller.banner;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/store-it")
@RequiredArgsConstructor
public class RegistroClienteController {


    private final ClienteService clienteService;


    // Endpoint para el registro de clientes con datos tradicionales (no Google).
    @PostMapping("/clientes/registro")
    public ResponseEntity<MensajeDto<String>> registrarCliente(
            @Valid @RequestBody RegistroClienteDto registroClienteDto)
            throws ElementoRepetidoException, ElementoNulosException, ElementoEliminadoException, ElementoNoValidoException {

        clienteService.registrarCliente(registroClienteDto);

        return ResponseEntity.ok().body(new MensajeDto<>(false,"Registro logrado exitosamente."));
    }

    // Endpoint para validar un token de Google.
    @PostMapping("/clientes/validate-google")
    public ResponseEntity<MensajeDto<GoogleUserResponse>> validarGoogle(@RequestBody GoogleTokenRequest googleTokenRequest)
            throws ElementoIncorrectoException {

        GoogleUserResponse googleUserResponse = clienteService.validarToken(googleTokenRequest.idToken());

        return ResponseEntity.status(200).body(new MensajeDto<>(false,googleUserResponse));
    }


    // Endpoint para el registro de clientes utilizando una cuenta de Google.
    @PostMapping("/clientes/registro/google")
    public ResponseEntity<MensajeDto<String>> registrarClienteGoogle(
            @Valid @RequestBody RegistroClienteGoogleDto registroClienteGoogleDto)
            throws ElementoRepetidoException, ElementoNulosException, ElementoEliminadoException, ElementoNoValidoException {

        clienteService.registroClienteGoogle(registroClienteGoogleDto);

        return ResponseEntity.ok().body(new MensajeDto<>(false,"Registro logrado exitosamente."));
    }


    // Endpoint para verificar la cuenta de un cliente con un código.
    @PostMapping("/clientes/verificar")
    public ResponseEntity<MensajeDto<String>> verificarRegistroCliente(
            @Valid @RequestBody VerificacionCodigoDto verificacionCodigoDto)
            throws ElementoNoEncontradoException, ElementoNoCoincideException, ElementoIncorrectoException {

        clienteService.verificacionCliente(verificacionCodigoDto);
        return ResponseEntity.ok(new MensajeDto<>(false ,"Cliente verificado con éxito."));
    }
}
