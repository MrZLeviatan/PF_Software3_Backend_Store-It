package co.edu.uniquindio.service.common.impl;

import co.edu.uniquindio.dto.TokenDto;
import co.edu.uniquindio.dto.common.auth.*;
import co.edu.uniquindio.dto.common.email.EmailDto;
import co.edu.uniquindio.dto.common.google.GoogleUserResponse;
import co.edu.uniquindio.exceptions.ElementoEliminadoException;
import co.edu.uniquindio.exceptions.ElementoNoCoincideException;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.exceptions.ElementoNoValidoException;
import co.edu.uniquindio.models.embeddable.Codigo;
import co.edu.uniquindio.models.entities.users.Cliente;
import co.edu.uniquindio.models.entities.users.Persona;
import co.edu.uniquindio.models.enums.users.EstadoCuenta;
import co.edu.uniquindio.security.JWTUtils;
import co.edu.uniquindio.service.common.AuthService;
import co.edu.uniquindio.service.utils.CodigoService;
import co.edu.uniquindio.service.utils.EmailService;
import co.edu.uniquindio.service.utils.GoogleUtilsService;
import co.edu.uniquindio.service.utils.PersonaUtilService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final JWTUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final CodigoService codigoService;
    private final PersonaUtilService personaUtilService;
    private final GoogleUtilsService googleUtilsService;


    @Override
    public void login(LoginDto loginDto)
            throws ElementoNoEncontradoException, ElementoNoCoincideException, ElementoEliminadoException, ElementoNoValidoException {

        // 1, Buscar a una persona mediante el email
        Persona personaOpt = personaUtilService.buscarPersonaPorEmail(loginDto.email());

        // Verificar si el password coincide con el propietario del email
        if (autentificarPassword(personaOpt, loginDto.password())) {

            // 2. Asignar el código de verificación al usuario.
            Codigo codigoVerificacion = codigoService.generarCodigoVerificacion2AF();
            personaOpt.getUser().setCodigo(codigoVerificacion);

            // 3. Enviar código de verificación al email del cliente.
            EmailDto emailDto = new EmailDto(
                    personaOpt.getUser().getEmail(),codigoVerificacion.getClave(),
                    "Código de inicio de Sesión - Store IT!: " + codigoVerificacion.getClave());
            emailService.enviarEmailCodigo(emailDto, "verificacion-login.html");

            // 4. Guardamos usuario con el codigo 2FA
            personaUtilService.guardarPersonaBD(personaOpt);
        }
    }


    public boolean autentificarPassword(Persona persona, String password)
            throws  ElementoEliminadoException, ElementoNoValidoException, ElementoNoCoincideException {

        // 1. Verificar a la persona y su estado.
        validarEstadoPersona(persona);

        // 2. Verificamos si las credenciales coinciden.
        if (!passwordEncoder.matches(password, persona.getUser().getPassword())) {
            throw new ElementoNoCoincideException("La contraseña ingresada es incorrecta");}

        return true;
    }


    @Override
    public GoogleUserResponse loginGoogle(LoginGoogleDto loginGoogleDto)
            throws ElementoNoValidoException, ElementoNoEncontradoException, ElementoEliminadoException {

        // 1. Validar token de Google usando la librería oficial
        GoogleIdToken.Payload payload = googleUtilsService.verifyIdToken(loginGoogleDto.idToken());
        if (payload == null) {
            throw new ElementoNoValidoException("El token de autenticación de Google no es válido.");
        }

        // Tomamos los datos proporcionados por Google
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String picture = (String) payload.get("picture");

        // 2. Buscar persona en nuestra BD
        Persona personaOpt = personaUtilService.buscarPersonaPorEmail(email);

        // 3. Validar estado de la cuenta
        validarEstadoPersona(personaOpt);

        // 4. Generar código de verificación 2FA
        Codigo codigoVerificacion = codigoService.generarCodigoVerificacion2AF();
        personaOpt.getUser().setCodigo(codigoVerificacion);

        // 5. Enviar código al correo
        EmailDto emailDto = new EmailDto(
                personaOpt.getUser().getEmail(),
                codigoVerificacion.getClave(),
                "Código de inicio de Sesión (Google) - Store IT!: " + codigoVerificacion.getClave()
        );
        emailService.enviarEmailCodigo(emailDto, "verificacion-login.html");

        // 6. Guardar usuario con el código
        personaUtilService.guardarPersonaBD(personaOpt);
        return new GoogleUserResponse(email, name, picture);
    }



    private void validarEstadoPersona(Persona persona) throws
            ElementoEliminadoException, ElementoNoValidoException {

        // 1. Verificar si la cuenta no ha sido activada
        if (persona.getUser().getEstadoCuenta().equals(EstadoCuenta.INACTIVA)){

            // Generamos un nuevo código de verificación
            Codigo codigoVerificacion = codigoService.generarCodigoVerificacion2AF();

            persona.getUser().setCodigo(codigoVerificacion);

            // Guardamos la nueva asignación de código
            personaUtilService.guardarPersonaBD(persona);

            // Enviar correo con el nuevo código
            EmailDto emailDto = new EmailDto(
                    persona.getUser().getEmail(),
                    codigoVerificacion.getClave(),
                    "Reverificación de Cuenta - Store-It"
            );
            emailService.enviarEmailVerificacionRegistro(emailDto);

            throw new ElementoNoValidoException(
                    "La cuenta ya existe pero está inactiva. "
                            + "Hemos enviado un nuevo código de verificación a tu correo."
            );
        }

        // 2. Verificar si la cuenta ha sido eliminada
        if (persona.getUser().getEstadoCuenta() == EstadoCuenta.ELIMINADO) {

            // Solo si es un Cliente, lanzar excepción especial para reactivar
            if (persona instanceof Cliente){
                throw new ElementoNoValidoException("La cuenta está eliminada, por favor comunicarse con un asesor");}

            // Para otros usuarios (Agente, Personal, RRHH) simplemente cuenta eliminada
            throw new ElementoEliminadoException("La cuenta está eliminada y no puede ser utilizada");
        }
    }



    @Override
    public TokenDto verificacionLogin(VerificacionCodigoDto verificacionLoginDto)
            throws ElementoNoEncontradoException, ElementoNoValidoException, ElementoNoCoincideException {

        String token;

        Persona personaOpt = personaUtilService.buscarPersonaPorEmail(verificacionLoginDto.email());

        codigoService.autentificarCodigo(verificacionLoginDto);

        // 4. Generar y retornar el token JWT con los datos del cliente
        token = jwtUtils.generateToken(personaOpt.getId().toString(),jwtUtils.generarTokenLogin(personaOpt));

        return new TokenDto(token);
    }


    @Override
    public void solicitarRestablecimientoPassword(SolicitudEmailDto solicitudEmailDto)
            throws ElementoNoEncontradoException, ElementoEliminadoException, ElementoNoValidoException {

        // Buscamos la persona mediante su email
        Persona personaOpt = personaUtilService.buscarPersonaPorEmail(solicitudEmailDto.email());

        // Validamos el estado de la persona (Si está activa)
        validarEstadoPersona(personaOpt);

        // 2. Asignar el código de verificación al usuario.
        Codigo codigoVerificacion = codigoService.generarCodigoRestablecerPassword();
        personaOpt.getUser().setCodigo(codigoVerificacion);

        // 3. Enviar código de verificación al email del cliente.
        EmailDto emailDto = new EmailDto(
                personaOpt.getUser().getEmail(),codigoVerificacion.getClave(),
                "Código de restablecimiento de Contraseña - Store-IT!");
        emailService.enviarEmailCodigo(emailDto,"codigoRestablecerPassword.html");

        personaUtilService.guardarPersonaBD(personaOpt);

    }


    @Override
    public void verificarCodigoPassword(VerificacionCodigoDto verificacionCodigoDto)
            throws ElementoNoEncontradoException, ElementoNoValidoException, ElementoNoCoincideException {

        // Validar código de restablecer password
        codigoService.autentificarCodigo(verificacionCodigoDto);
    }


    @Override
    public void actualizarPassword(ActualizarPasswordDto actualizarPasswordDto)
            throws ElementoNoEncontradoException {

        // Buscamos la persona mediante su email
        Persona personaPot = personaUtilService.buscarPersonaPorEmail(actualizarPasswordDto.email());

        // Actualizar la contraseña (encriptándola)
        personaPot.getUser().setPassword(passwordEncoder.encode(actualizarPasswordDto.nuevaPassword()));

        // Se guarda la nueva contraseña
        personaUtilService.guardarPersonaBD(personaPot);
    }


}
