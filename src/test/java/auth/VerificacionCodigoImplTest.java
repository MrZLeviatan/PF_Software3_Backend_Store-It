package auth;

import co.edu.uniquindio.dto.TokenDto;
import co.edu.uniquindio.dto.common.auth.VerificacionCodigoDto;
import co.edu.uniquindio.exceptions.ElementoNoCoincideException;
import co.edu.uniquindio.models.embeddable.Codigo;
import co.edu.uniquindio.models.embeddable.User;
import co.edu.uniquindio.models.entities.users.Persona;
import co.edu.uniquindio.security.JWTUtils;
import co.edu.uniquindio.service.common.impl.AuthServiceImpl;
import co.edu.uniquindio.service.utils.CodigoService;
import co.edu.uniquindio.service.utils.EmailService;
import co.edu.uniquindio.service.utils.GoogleUtilsService;
import co.edu.uniquindio.service.utils.PersonaUtilService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VerificacionCodigoImplTest {
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

    private Persona persona;
    private VerificacionCodigoDto verificacionDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create a fake user
        // Crear un usuario simulado
        persona = new Persona() {};
        User user = new User();
        user.setEmail("cliente@test.com");
        Codigo codigo = new Codigo("123456", LocalDateTime.now().plusMinutes(5), null);
        user.setCodigo(codigo);
        persona.setUser(user);
        persona.setId(1L);
        persona.setNombre("Cliente Test");

        verificacionDto = new VerificacionCodigoDto("cliente@test.com", "123456");
    }

    /**
     * Prueba cuando el código ingresado es correcto y no ha expirado.
     */
    @Test
    void testVerificacionLogin_CodigoCorrecto() throws Exception {
        when(personaUtilService.buscarPersonaPorEmail("cliente@test.com")).thenReturn(persona);
        doNothing().when(codigoService).autentificarCodigo(verificacionDto);


        TokenDto token = authService.verificacionLogin(verificacionDto);

        assertNotNull(token);
        verify(codigoService, times(1)).autentificarCodigo(verificacionDto);
    }

    /**
     * Prueba cuando el código ingresado no coincide con el guardado.
     */
    @Test
    void testVerificacionLogin_CodigoIncorrecto() throws Exception {
        when(personaUtilService.buscarPersonaPorEmail("cliente@test.com")).thenReturn(persona);
        doThrow(new ElementoNoCoincideException("Código incorrecto"))
                .when(codigoService).autentificarCodigo(verificacionDto);

        ElementoNoCoincideException ex = assertThrows(
                ElementoNoCoincideException.class,
                () -> authService.verificacionLogin(verificacionDto)
        );

        assertEquals("Código incorrecto", ex.getMessage());
        verify(codigoService, times(1)).autentificarCodigo(verificacionDto);
    }

    /**
     *  Prueba cuando el código ha expirado.
     */
    @Test
    void testVerificacionLogin_CodigoExpirado() throws Exception {
        when(personaUtilService.buscarPersonaPorEmail("cliente@test.com")).thenReturn(persona);
        doThrow(new ElementoNoCoincideException("Código expirado"))
                .when(codigoService).autentificarCodigo(verificacionDto);

        ElementoNoCoincideException ex = assertThrows(
                ElementoNoCoincideException.class,
                () -> authService.verificacionLogin(verificacionDto)
        );

        assertEquals("Código expirado", ex.getMessage());
        verify(codigoService, times(1)).autentificarCodigo(verificacionDto);
    }
}
