package proveedor;

import co.edu.uniquindio.dto.users.proveedor.ProveedorDto;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.mapper.users.ProveedorMapper;
import co.edu.uniquindio.models.entities.users.Proveedor;
import co.edu.uniquindio.repository.users.ProveedorRepo;
import co.edu.uniquindio.service.users.impl.ProveedorServiceImpl;
import co.edu.uniquindio.service.utils.PersonaUtilService;
import co.edu.uniquindio.service.utils.PhoneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProveedorServiceImplTest {

    @Mock
    private PersonaUtilService personaUtilService;

    @Mock
    private PhoneService phoneService;

    @Mock
    private ProveedorRepo proveedorRepo;

    @Mock
    private ProveedorMapper proveedorMapper;

    @InjectMocks
    private ProveedorServiceImpl proveedorService;

    private Proveedor proveedor;
    private ProveedorDto proveedorDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        proveedor = new Proveedor();
        proveedor.setId(1L);
        proveedor.setNombre("TechMarket");
        proveedor.setEmail("proveedor@test.com");
        proveedor.setTelefono("3214567890");
        proveedor.setMarca("SmartTech");

        proveedorDto = new ProveedorDto(
                1L,
                "TechMarket",
                "proveedor@test.com",
                "3214567890",
                "SmartTech",
                null,
                null
        );
    }

    // Caso 1: Encontrar proveedor existente
    @Test
    void testEncontrarProveedorDto_Existente() throws Exception {
        when(proveedorRepo.findById(1L)).thenReturn(Optional.of(proveedor));
        when(proveedorMapper.toDto(proveedor)).thenReturn(proveedorDto);

        ProveedorDto resultado = proveedorService.encontrarProveedorDto(1L);

        assertNotNull(resultado);
        assertEquals("TechMarket", resultado.nombre());
        assertEquals("proveedor@test.com", resultado.email());
        verify(proveedorRepo, times(1)).findById(1L);
    }

    // Caso 2: Proveedor no registrado
    @Test
    void testEncontrarProveedorDto_NoExistente() {
        when(proveedorRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ElementoNoEncontradoException.class, () -> {
            proveedorService.encontrarProveedorDto(99L);
        });

        verify(proveedorRepo, times(1)).findById(99L);
    }

    // Caso 3: Listar proveedores (lista con elementos)
    @Test
    void testListarProveedores_ConElementos() {
        Proveedor proveedor2 = new Proveedor();
        proveedor2.setId(2L);
        proveedor2.setNombre("DataTech");
        proveedor2.setEmail("data@test.com");
        proveedor2.setTelefono("3210000000");
        proveedor2.setMarca("NextGen");

        ProveedorDto proveedorDto2 = new ProveedorDto(
                2L,
                "DataTech",
                "data@test.com",
                "3210000000",
                "NextGen",
                null,
                null
        );

        when(proveedorRepo.findAll()).thenReturn(List.of(proveedor, proveedor2));
        when(proveedorMapper.toDto(proveedor)).thenReturn(proveedorDto);
        when(proveedorMapper.toDto(proveedor2)).thenReturn(proveedorDto2);

        List<ProveedorDto> lista = proveedorService.listarProveedores();

        assertEquals(2, lista.size());
        assertEquals("TechMarket", lista.get(0).nombre());
        assertEquals("DataTech", lista.get(1).nombre());
        verify(proveedorRepo, times(1)).findAll();
    }


    // Caso 4: Listar proveedores (lista vacía)
    @Test
    void testListarProveedores_Vacio() {
        when(proveedorRepo.findAll()).thenReturn(List.of());

        List<ProveedorDto> lista = proveedorService.listarProveedores();

        assertNotNull(lista);
        assertTrue(lista.isEmpty());
        verify(proveedorRepo, times(1)).findAll();
    }
}
