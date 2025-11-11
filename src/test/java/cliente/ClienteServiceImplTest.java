package cliente;

import co.edu.uniquindio.dto.common.UbicacionDto;
import co.edu.uniquindio.dto.common.email.EmailDto;
import co.edu.uniquindio.dto.common.user.CrearUserDto;
import co.edu.uniquindio.dto.users.cliente.RegistroClienteDto;
import co.edu.uniquindio.exceptions.ElementoRepetidoException;
import co.edu.uniquindio.mapper.users.ClienteMapper;
import co.edu.uniquindio.models.embeddable.Codigo;
import co.edu.uniquindio.models.embeddable.User;
import co.edu.uniquindio.models.entities.users.Cliente;
import co.edu.uniquindio.models.enums.users.TipoCliente;
import co.edu.uniquindio.repository.objects.compra.CarritoCompraRepo;
import co.edu.uniquindio.repository.users.ClienteRepo;
import co.edu.uniquindio.service.objects.compra.CarritoCompraService;
import co.edu.uniquindio.service.users.impl.ClienteServiceImpl;
import co.edu.uniquindio.service.utils.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test de la clase ClienteServiceImpl
 */
class ClienteServiceImplTest {

    // ---- Mocks de dependencias externas ---- //
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private GoogleUtilsService googleUtilsService;
    @Mock private PhoneService phoneService;
    @Mock private PersonaUtilService personaUtilService;
    @Mock private CodigoService codigoService;
    @Mock private EmailService emailService;
    @Mock private CarritoCompraService carritoCompraService;
    @Mock private CarritoCompraRepo carritoCompraRepo;
    @Mock private ClienteMapper clienteMapper;
    @Mock private ClienteRepo clienteRepo;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private RegistroClienteDto registroClienteDto;
    private Cliente clienteMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Datos válidos simulados para el cliente / Fake valid client data
        CrearUserDto userDto = new CrearUserDto("john@example.com", "Password123!");
        UbicacionDto ubicacionDto = new UbicacionDto("Colombia", "Armenia", 4.533, -75.681);

        registroClienteDto = new RegistroClienteDto(
                "John Doe",
                "3001234567",
                "+57",
                null,
                null,
                userDto,
                ubicacionDto,
                TipoCliente.NATURAL,
                null
        );

        clienteMock = new Cliente();
        clienteMock.setTipoCliente(TipoCliente.NATURAL);

        // ⚠️ Muy importante: inicializar el objeto embebido User
        User user = new co.edu.uniquindio.models.embeddable.User();
        user.setEmail("john@example.com");
        user.setPassword("Password123!");
        clienteMock.setUser(user);
    }

    // Caso 1: Registro exitoso / Successful registration
    @Test
    void registrarCliente_Exitoso() throws Exception {
        when(phoneService.obtenerTelefonoFormateado("3001234567", "+57"))
                .thenReturn("+573001234567");
        when(passwordEncoder.encode("Password123!")).thenReturn("encodedPassword");
        when(clienteMapper.toEntity(any())).thenReturn(clienteMock);
        when(codigoService.generarCodigoVerificacion2AF())
                .thenReturn(new Codigo("123456", null, null));

        assertDoesNotThrow(() -> clienteService.registrarCliente(registroClienteDto));

        // Verificamos que se haya enviado el email y guardado el cliente
        verify(emailService, times(1)).enviarEmailVerificacionRegistro(any(EmailDto.class));
        verify(clienteRepo, times(1)).save(any(Cliente.class));
    }

    // Caso 2: Email ya registrado / Email already exists
    @Test
    void registrarCliente_EmailYaRegistrado() throws Exception {
        doThrow(new ElementoRepetidoException("El email ya está registrado"))
                .when(personaUtilService).validarEmailNoRepetido("john@example.com");

        ElementoRepetidoException ex = assertThrows(
                ElementoRepetidoException.class,
                () -> clienteService.registrarCliente(registroClienteDto)
        );

        assertEquals("El email ya está registrado", ex.getMessage());
        verify(clienteRepo, never()).save(any());
    }

    // Caso 3: Teléfono ya registrado / Phone number already exists
    @Test
    void registrarCliente_TelefonoYaRegistrado() throws Exception {
        when(phoneService.obtenerTelefonoFormateado("3001234567", "+57"))
                .thenReturn("+573001234567");

        doThrow(new ElementoRepetidoException("El teléfono ya está registrado"))
                .when(personaUtilService).validarTelefonoNoRepetido("+573001234567", null);

        ElementoRepetidoException ex = assertThrows(
                ElementoRepetidoException.class,
                () -> clienteService.registrarCliente(registroClienteDto)
        );

        assertEquals("El teléfono ya está registrado", ex.getMessage());
        verify(clienteRepo, never()).save(any());
    }

    // Caso 4: Credenciales no válidas / Invalid credentials (password vacío)
    @Test
    void registrarCliente_CredencialesNoValidas() {
        CrearUserDto userInvalido = new CrearUserDto("invalid@example.com", ""); // contraseña vacía
        UbicacionDto ubicacionDto = new UbicacionDto("Colombia", "Armenia", 4.5, -75.6);

        RegistroClienteDto dtoInvalido = new RegistroClienteDto(
                "Jane Doe",
                "3105554444",
                "+57",
                null,
                null,
                userInvalido,
                ubicacionDto,
                TipoCliente.NATURAL,
                null
        );

        assertThrows(NullPointerException.class,
                () -> clienteService.registrarCliente(dtoInvalido));

        verify(clienteRepo, never()).save(any());
    }
}

