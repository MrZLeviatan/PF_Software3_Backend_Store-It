package proveedor;

import co.edu.uniquindio.dto.users.proveedor.ProveedorDto;
import co.edu.uniquindio.dto.users.proveedor.RegistroProveedorDto;
import co.edu.uniquindio.exceptions.ElementoRepetidoException;
import co.edu.uniquindio.mapper.users.ProveedorMapper;
import co.edu.uniquindio.models.entities.users.Proveedor;
import co.edu.uniquindio.repository.users.ProveedorRepo;
import co.edu.uniquindio.service.users.impl.ProveedorServiceImpl;
import co.edu.uniquindio.service.utils.PersonaUtilService;
import co.edu.uniquindio.service.utils.PhoneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistroProveedorServiceImplTest {

    private ProveedorServiceImpl proveedorService;
    private PersonaUtilService personaUtilService;
    private PhoneService phoneService;
    private ProveedorRepo proveedorRepo;
    private ProveedorMapper proveedorMapper;

    @BeforeEach
    void setUp() {
        personaUtilService = mock(PersonaUtilService.class);
        phoneService = mock(PhoneService.class);
        proveedorRepo = mock(ProveedorRepo.class);
        proveedorMapper = mock(ProveedorMapper.class);
        proveedorService = new ProveedorServiceImpl(personaUtilService, phoneService, proveedorRepo, proveedorMapper);
    }

    // Test 1: Registro Exitoso
    @Test
    void testRegistrarProveedor_Exitoso() throws Exception {
        // Datos de entrada simulados
        RegistroProveedorDto dto = new RegistroProveedorDto("TechMarket", "proveedor@test.com", "3214567890", "SmartTech");
        Proveedor proveedorEntity = new Proveedor();
        ProveedorDto proveedorDto = new ProveedorDto(1L, "TechMarket", "proveedor@test.com", "3214567890", "SmartTech",null,null);

        // Simular comportamiento de dependencias
        doNothing().when(personaUtilService).validarEmailNoRepetido(dto.email());
        when(proveedorMapper.toEntity(dto)).thenReturn(proveedorEntity);
        when(proveedorRepo.save(proveedorEntity)).thenReturn(proveedorEntity);
        when(proveedorMapper.toDto(proveedorEntity)).thenReturn(proveedorDto);

        // Ejecutar el método
        ProveedorDto resultado = proveedorService.registrarProveedor(dto);

        // Verificaciones
        assertNotNull(resultado);
        assertEquals("TechMarket", resultado.nombre());
        assertEquals("proveedor@test.com", resultado.email());
        verify(proveedorRepo, times(1)).save(proveedorEntity);
        verify(personaUtilService, times(1)).validarEmailNoRepetido(dto.email());
    }

    // Test 2: Email Repetido
    @Test
    void testRegistrarProveedor_EmailRepetido() throws Exception {
        RegistroProveedorDto dto = new RegistroProveedorDto("TechMarket", "proveedor@test.com", "3214567890", "SmartTech");

        // Simular excepción de email repetido
        doThrow(new ElementoRepetidoException("El email ya está registrado"))
                .when(personaUtilService).validarEmailNoRepetido(dto.email());

        // Ejecutar y verificar excepción
        ElementoRepetidoException ex = assertThrows(
                ElementoRepetidoException.class,
                () -> proveedorService.registrarProveedor(dto)
        );

        assertEquals("El email ya está registrado", ex.getMessage());
        verify(proveedorRepo, never()).save(any());
    }

}
