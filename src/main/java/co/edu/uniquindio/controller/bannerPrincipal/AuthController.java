package co.edu.uniquindio.controller.bannerPrincipal;

import co.edu.uniquindio.dto.MensajeDto;
import co.edu.uniquindio.dto.TokenDto;
import co.edu.uniquindio.dto.common.auth.*;
import co.edu.uniquindio.dto.common.google.GoogleUserResponse;
import co.edu.uniquindio.exceptions.ElementoEliminadoException;
import co.edu.uniquindio.exceptions.ElementoNoCoincideException;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.exceptions.ElementoNoValidoException;
import co.edu.uniquindio.service.common.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ¡Controlador REST encargado de gestionar el login
 *  y verificación de clientes en la aplicación Store-It!.
 */
@RestController
@RequestMapping("/api/auth") // Ubicado aquí si el registro es parte del flujo de autenticación
@RequiredArgsConstructor
public class AuthController {

    // Se llama a la clase auxiliar Auth (con los métodos del logueo, verificación y restablecimiento de Password)
    private final AuthService authService;

    // EndPoint para el logueo tradicional ( Email, Password )
    @PostMapping("/login")
    private ResponseEntity<MensajeDto<String>> login (@Valid @RequestBody LoginDto loginDto)
            throws ElementoNoEncontradoException, ElementoEliminadoException, ElementoNoValidoException, ElementoNoCoincideException {

        // Se llama al método de Logueo (Este envía un mensaje de confirmación por parte del logueo)
        authService.login(loginDto);

        // Retorna una respuesta HTTP 200 con el mensaje de confirmación dentro de un objeto de tipo MensajeDto
        return ResponseEntity.status(200).body(new MensajeDto<>(false,"En espera de verificación login"));
    }

    // EndPoint para el logueo mediante la librería de Google OAuth
    @PostMapping("/login/google")
    private ResponseEntity<MensajeDto<GoogleUserResponse>> loginGoogle (@Valid @RequestBody LoginGoogleDto loginGoogleDto)
            throws ElementoNoEncontradoException, ElementoEliminadoException, ElementoNoValidoException {

        // Este método devuelve un objeto GoogleUserResponse obtenido directamente
        // desde la librería de Google OAuth, conteniendo los datos del usuario autenticado
        GoogleUserResponse googleUserResponse = authService.loginGoogle(loginGoogleDto);

        // Retorna una respuesta HTTP 200 con el token dentro de un objeto de tipo MensajeDto
        return ResponseEntity.status(200).body(new MensajeDto<>(false,googleUserResponse));
    }


    // EndPoint para la verificación del Logueo (Verificación 2FA)
    @PostMapping("/login/verificacion")
    private ResponseEntity<MensajeDto<TokenDto>> verificarLogin(@Valid @RequestBody VerificacionCodigoDto verificacionCodigoDto)
            throws ElementoNoEncontradoException, ElementoNoValidoException, ElementoNoCoincideException {
        // Se llama al método de Verificación de Codigo el cual devuelve el Token de Usuario.
        TokenDto tokenDto = authService.verificacionLogin(verificacionCodigoDto);
        // Retorna una respuesta HTTP 200 con el token dentro de un objeto de tipo MensajeDto
        return ResponseEntity.status(200).body(new MensajeDto<>(false,tokenDto));
    }


    // --------- Restablecer Password ------------


    // EndPoint para solicitar el restablecimiento de contraseña mediante email
    @PostMapping("/password/restablecer")
    public ResponseEntity<MensajeDto<String>> solicitarRestablecimientoPassword(@RequestBody @Valid SolicitudEmailDto dto)
            throws ElementoNoEncontradoException, ElementoEliminadoException, ElementoNoValidoException {
        // Se llama al método para enviar el correo de restablecimiento
        authService.solicitarRestablecimientoPassword(dto);
        // Retorna una respuesta HTTP 200 indicando que la solicitud fue enviada correctamente
        return ResponseEntity.ok().body(new MensajeDto<>(false,"Solicitud de restablecimiento de contraseña enviada"));
    }

    // EndPoint para verificar el código de restablecimiento de contraseña
    @PostMapping("/password/verificacion")
    public ResponseEntity<MensajeDto<String>> verificarCodigoPassword(@RequestBody @Valid VerificacionCodigoDto dto)
            throws ElementoNoEncontradoException, ElementoNoCoincideException {
        // Se llama al método para verificar el código recibido por email
        authService.verificarCodigoPassword(dto);
        // Retorna una respuesta HTTP 200 si el código es válido
        return ResponseEntity.ok().body(new MensajeDto<>(false,"Código verificado exitosamente"));
    }

    // EndPoint para actualizar la contraseña después de la verificación
    @PutMapping("/password/actualizar")
    public ResponseEntity<MensajeDto<String>> actualizarPassword(@RequestBody @Valid ActualizarPasswordDto dto)
            throws ElementoNoEncontradoException {
        // Se llama al método que actualiza la contraseña del usuario en la base de datos
        authService.actualizarPassword(dto);
        // Retorna una respuesta HTTP 200 indicando que la contraseña fue actualizada correctamente
        return ResponseEntity.ok().body(new MensajeDto<>(false,"Contraseña actualizada correctamente"));
    }


}
