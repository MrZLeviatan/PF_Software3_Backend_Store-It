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

/**
 * Servicio encargado de manejar toda la lógica de autenticación y autorización del sistema.
 * <br>
 *  Esta clase implementa la interfaz {@link AuthService} y se encarga de orquestar las operaciones
 *  relacionadas con el inicio de sesión tradicional, autenticación mediante Google OAuth,
 *  verificación en dos pasos (2FA) y procesos de restablecimiento de contraseñas.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    // Utilidad para la generación, validación y decodificación de tokens JWT
    private final JWTUtils jwtUtils;
    // Codificador de contraseñas (utilizado para encriptar y verificar contraseñas de usuarios)
    private final PasswordEncoder passwordEncoder;
    // Servicio encargado del envío de correos electrónicos (verificación, restablecimiento, etc.)
    private final EmailService emailService;
    // Servicio encargado de la generación, validación y gestión de códigos de verificación (2FA, restablecimiento)
    private final CodigoService codigoService;
    // Servicio auxiliar para la gestión y validación de datos de usuarios/personas
    private final PersonaUtilService personaUtilService;
    // Servicio encargado de la autenticación mediante Google OAuth y validación de usuarios externos
    private final GoogleUtilsService googleUtilsService;


    /**
     * Método encargado de gestionar el proceso de inicio de sesión tradicional (Email y Password).
     * <br>
     * Este método valida las credenciales del usuario verificando el correo electrónico y la contraseña,
     * genera un código de verificación de dos factores (2FA) para reforzar la seguridad,
     * y lo envía al correo electrónico del usuario antes de completar el proceso de autenticación.
     */
    @Override
    public void login(LoginDto loginDto)
            throws ElementoNoEncontradoException, ElementoNoCoincideException,
            ElementoEliminadoException, ElementoNoValidoException {

        // 1, Buscar a una persona mediante el email
        Persona personaOpt = personaUtilService.buscarPersonaPorEmail(loginDto.email());

        // 2.Verificar si el password coincide con el propietario del email
        if (autentificarPassword(personaOpt, loginDto.password())) {

            // 3. Asignar el código de verificación al usuario.
            Codigo codigoVerificacion = codigoService.generarCodigoVerificacion2AF();
            // 3.1 Se implanta el código al usuario.
            personaOpt.getUser().setCodigo(codigoVerificacion);

            // 4. Enviar código de verificación al email del cliente.
            EmailDto emailDto = new EmailDto(
                    personaOpt.getUser().getEmail(),codigoVerificacion.getClave(),
                    "Código de inicio de Sesión - Store IT!: " + codigoVerificacion.getClave());
            emailService.enviarEmailCodigo(emailDto, "verificacion-login.html");

            // 5. Guardamos usuario con el codigo 2FA
            personaUtilService.guardarPersonaBD(personaOpt);
        }
    }

    /**
     * Método auxiliar encargado de autentificar si el Password coincide con el Usuario a loguearse.
     * <br>
     * Este método valida las credenciales del usuario verificando su estado actual y comparando la contraseña ingresada
     * con la almacenada en la base de datos, utilizando la librería {@link PasswordEncoder} para realizar la verificación
     * segura mediante encriptación.
     */
    public boolean autentificarPassword(Persona persona, String password)
            throws  ElementoEliminadoException, ElementoNoValidoException, ElementoNoCoincideException {

        // 1. Verificar a la persona y su estado.
        validarEstadoPersona(persona);
        // 2. Verificamos si las credenciales coinciden.
        if (!passwordEncoder.matches(password, persona.getUser().getPassword())) {
            // Lanza excepción si la contraseña no coincide
            throw new ElementoNoCoincideException("La contraseña ingresada es incorrecta");}
        // 3. Si las credenciales son válidas, retornar true
        return true;
    }

    /**
     * Método encargado de manejar el proceso de autenticación mediante Google Sign-In.
     * <br>
     * Esta función permite a los usuarios iniciar sesión en la aplicación utilizando su cuenta de Google.
     * El método verifica la validez del token proporcionado por Google, busca al usuario en la base de datos,
     * valida su estado, genera un código de verificación 2FA y lo envía por correo electrónico antes de permitir el acceso.
     */
    @Override
    public GoogleUserResponse loginGoogle(LoginGoogleDto loginGoogleDto)
            throws ElementoNoValidoException, ElementoNoEncontradoException, ElementoEliminadoException {

        // 1. Validar token de Google usando la librería oficial
        GoogleIdToken.Payload payload = googleUtilsService.verifyIdToken(loginGoogleDto.idToken());
        if (payload == null) {
            // Lanza excepción si el token de validación de Google llena a ser nulo
            throw new ElementoNoValidoException("El token de autenticación de Google no es válido.");
        }

        // 2. Extraer los datos principales del usuario desde el token
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String picture = (String) payload.get("picture");

        // 3. Buscar al usuario en la base de datos por su email
        Persona personaOpt = personaUtilService.buscarPersonaPorEmail(email);

        // 4. Validar que la cuenta no esté eliminada o bloqueada
        validarEstadoPersona(personaOpt);

        // 5. Generar código de verificación 2FA (autenticación en dos pasos)
        Codigo codigoVerificacion = codigoService.generarCodigoVerificacion2AF();
        personaOpt.getUser().setCodigo(codigoVerificacion);

        // 6. Enviar el código al correo electrónico del usuario
        EmailDto emailDto = new EmailDto(
                personaOpt.getUser().getEmail(),
                codigoVerificacion.getClave(),
                "Código de inicio de Sesión (Google) - Store IT!: " + codigoVerificacion.getClave()
        );
        emailService.enviarEmailCodigo(emailDto, "verificacion-login.html");

        // 7. Guardar la actualización del usuario con el nuevo código
        personaUtilService.guardarPersonaBD(personaOpt);
        // 8. Retornar respuesta con los datos principales del usuario de Google
        return new GoogleUserResponse(email, name, picture);
    }


    /**
     * Método encargado de validar el estado actual de una persona antes de proceder con cualquier operación de autenticación.
     * <br>
     * Si la cuenta está inactiva, genera un nuevo código de verificación y lo envía por correo.
     * <br>
     * Si la cuenta fue eliminada, lanza una excepción indicando que no puede ser utilizada.
     */
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

            // Si la cuenta está inactiva, se lanza una excepción para el aviso y envío del email
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


    /**
     * Método encargado de verificar el código 2FA enviado al correo del usuario
     * y generar el token JWT de autenticación.
     */
    @Override
    public TokenDto verificacionLogin(VerificacionCodigoDto verificacionLoginDto)
            throws ElementoNoEncontradoException, ElementoNoValidoException, ElementoNoCoincideException {

        // 1. Buscar al usuario por su correo electrónico
        Persona personaOpt = personaUtilService.buscarPersonaPorEmail(verificacionLoginDto.email());

        // 2. Validar el código de verificación 2FA
        codigoService.autentificarCodigo(verificacionLoginDto);

        // 3. Generar el token JWT con los datos del usuario
        String token = jwtUtils.generateToken(
                personaOpt.getId().toString(),
                jwtUtils.generarTokenLogin(personaOpt)
        );
        // 4. Retornar token al cliente
        return new TokenDto(token);
    }

    /**
     * Método encargado de solicitar el restablecimiento de contraseña.
     * <br>
     * Este proceso envía un código de verificación al correo electrónico del usuario,
     * el cual deberá ser ingresado para continuar con la recuperación de la cuenta.
     */
    @Override
    public void solicitarRestablecimientoPassword(SolicitudEmailDto solicitudEmailDto)
            throws ElementoNoEncontradoException, ElementoEliminadoException, ElementoNoValidoException {

        // 1. Buscar al usuario mediante su correo electrónico
        Persona personaOpt = personaUtilService.buscarPersonaPorEmail(solicitudEmailDto.email());

        // 2. Validar estado de la cuenta
        validarEstadoPersona(personaOpt);

        // 3. Generar un código de verificación para restablecer la contraseña
        Codigo codigoVerificacion = codigoService.generarCodigoRestablecerPassword();
        personaOpt.getUser().setCodigo(codigoVerificacion);

        // 4. Enviar el código de verificación al correo del usuario
        EmailDto emailDto = new EmailDto(
                personaOpt.getUser().getEmail(),codigoVerificacion.getClave(),
                "Código de restablecimiento de Contraseña - Store-IT!");
        emailService.enviarEmailCodigo(emailDto,"codigoRestablecerPassword.html");

        // 5. Guardar los cambios en la base de datos
        personaUtilService.guardarPersonaBD(personaOpt);

    }

    /**
     * Método encargado de verificar la validez del código enviado para restablecer la contraseña.
     */
    @Override
    public void verificarCodigoPassword(VerificacionCodigoDto verificacionCodigoDto)
            throws ElementoNoEncontradoException, ElementoNoCoincideException {

        // Validar código de restablecer password
        codigoService.autentificarCodigo(verificacionCodigoDto);
    }


    /**
     * Método encargado de actualizar la contraseña de un usuario tras haber validado su identidad.
     */
    @Override
    public void actualizarPassword(ActualizarPasswordDto actualizarPasswordDto)
            throws ElementoNoEncontradoException {

        // 1. Buscar al usuario mediante su correo electrónico
        Persona personaPot = personaUtilService.buscarPersonaPorEmail(actualizarPasswordDto.email());

        // 2. Encriptar la nueva contraseña antes de guardarla
        personaPot.getUser().setPassword(passwordEncoder.encode(actualizarPasswordDto.nuevaPassword()));

        // 3. Guardar los cambios en la base de datos
        personaUtilService.guardarPersonaBD(personaPot);
    }


}
