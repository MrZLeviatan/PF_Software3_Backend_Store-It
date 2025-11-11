package auth;

import co.edu.uniquindio.dto.common.auth.LoginDto;
import co.edu.uniquindio.dto.common.email.EmailDto;
import co.edu.uniquindio.exceptions.*;
import co.edu.uniquindio.models.embeddable.Codigo;
import co.edu.uniquindio.models.embeddable.User;
import co.edu.uniquindio.models.entities.users.Cliente;
import co.edu.uniquindio.models.enums.users.EstadoCuenta;
import co.edu.uniquindio.service.common.impl.AuthServiceImpl;
import co.edu.uniquindio.service.utils.CodigoService;
import co.edu.uniquindio.service.utils.EmailService;
import co.edu.uniquindio.service.utils.PersonaUtilService;
import co.edu.uniquindio.security.JWTUtils;
import co.edu.uniquindio.service.utils.GoogleUtilsService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


/**
 * 🔹 Test para el servicio de autenticación AuthServiceImpl.
 * Verifica los distintos escenarios del inicio de sesión.
 */
public class AuthServiceImplTest {

    @Mock
    private JWTUtils jwtUtils;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private EmailService emailService;
    @Mock
    private CodigoService codigoService;
    @Mock
    private PersonaUtilService personaUtilService;
    @Mock
    private GoogleUtilsService googleUtilsService;

    @InjectMocks
    private AuthServiceImpl authService;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Creating a test client
        cliente = new Cliente();
        User user = new User();
        user.setEmail("test@correo.com");
        user.setPassword("123456");
        user.setEstadoCuenta(EstadoCuenta.ACTIVO);
        cliente.setUser(user);
    }

    /**
     * Caso 1: Login correcto con credenciales válidas.
     */
    @Test
    void login_exitoso() throws Exception {
        LoginDto dto = new LoginDto("test@correo.com", "123456");

        when(personaUtilService.buscarPersonaPorEmail(dto.email())).thenReturn(cliente);
        when(passwordEncoder.matches(dto.password(), cliente.getUser().getPassword())).thenReturn(true);
        when(codigoService.generarCodigoVerificacion2AF()).thenReturn(new Codigo());

        // Simulación del guardado y envío de correo
        doNothing().when(emailService).enviarEmailCodigo(any(EmailDto.class), anyString());
        doNothing().when(personaUtilService).guardarPersonaBD(any());

        // Ejecutamos el método
        authService.login(dto);

        // Verificamos que se generó código y se envió correo
        verify(codigoService, times(1)).generarCodigoVerificacion2AF();
        verify(emailService, times(1)).enviarEmailCodigo(any(EmailDto.class), eq("verificacion-login.html"));
    }

    /**
     * Caso 2: Contraseña incorrecta.
     */
    @Test
    void login_contraseñaIncorrecta() throws Exception {
        LoginDto dto = new LoginDto("test@correo.com", "wrongpass");

        when(personaUtilService.buscarPersonaPorEmail(dto.email())).thenReturn(cliente);
        when(passwordEncoder.matches(dto.password(), cliente.getUser().getPassword())).thenReturn(false);

        assertThrows(ElementoNoCoincideException.class, () -> authService.login(dto));

        verify(emailService, never()).enviarEmailCodigo(any(), anyString());
    }

    /**
     * Caso 3: Persona no registrada (email no existe).
     */
    @Test
    void login_personaNoRegistrada() throws Exception {
        LoginDto dto = new LoginDto("noexiste@correo.com", "123456");

        when(personaUtilService.buscarPersonaPorEmail(dto.email())).thenThrow(new ElementoNoEncontradoException("No existe"));

        assertThrows(ElementoNoEncontradoException.class, () -> authService.login(dto));

        verify(emailService, never()).enviarEmailCodigo(any(), anyString());
    }

    /**
     * Caso 4: Cuenta inactiva.
     */
    @Test
    void login_cuentaInactiva() throws Exception {
        cliente.getUser().setEstadoCuenta(EstadoCuenta.INACTIVA);
        LoginDto dto = new LoginDto("test@correo.com", "123456");

        when(personaUtilService.buscarPersonaPorEmail(dto.email())).thenReturn(cliente);

        assertThrows(NullPointerException.class, () -> authService.login(dto));

        verify(emailService, never()).enviarEmailCodigo(any(), anyString());
    }
}
