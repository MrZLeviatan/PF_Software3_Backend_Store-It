package productos;

import co.edu.uniquindio.dto.objects.inventario.producto.RegistroProductoDto;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.exceptions.ElementoNulosException;
import co.edu.uniquindio.exceptions.ElementoRepetidoException;
import co.edu.uniquindio.models.entities.objects.inventario.Producto;
import co.edu.uniquindio.models.entities.users.Proveedor;
import co.edu.uniquindio.models.enums.entities.TipoProducto;
import co.edu.uniquindio.repository.objects.inventario.ProductoRepo;
import co.edu.uniquindio.service.objects.inventario.impl.ProductoServiceImpl;
import co.edu.uniquindio.service.users.ProveedorService;
import co.edu.uniquindio.service.utils.CloudinaryService;
import co.edu.uniquindio.mapper.objects.inventario.ProductoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductoServiceImplTest {

    @Mock
    private ProductoRepo productoRepo;

    @Mock
    private ProductoMapper productoMapper;

    @Mock
    private ProveedorService proveedorService;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private RegistroProductoDto registroDto;
    private Producto producto;
    private Proveedor proveedor;
    private MultipartFile imagenMock;

    private List<Producto> productos = new ArrayList<>();


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        proveedor = new Proveedor();
        proveedor.setId(1L);
        proveedor.setProductos(new ArrayList<>()); // <--- esto arregla el test


        // Mock de MultipartFile
        imagenMock = mock(MultipartFile.class);
        when(imagenMock.isEmpty()).thenReturn(false);

        registroDto = new RegistroProductoDto(
                "1234567890",
                "Producto X",
                100.0,
                TipoProducto.ALIMENTO,
                1L,
                imagenMock
        );

        producto = new Producto();
        producto.setCodigoBarras(registroDto.codigoBarras());

    }



    @Test
    void registroProducto_CodigoRepetido() {
        when(productoRepo.existsByCodigoBarras(registroDto.codigoBarras())).thenReturn(true);

        assertThrows(ElementoRepetidoException.class, () -> productoService.registroProducto(registroDto));
        verify(productoRepo, never()).save(any());
    }

    @Test
    void registroProducto_ImagenNula() {
        when(productoRepo.existsByCodigoBarras(registroDto.codigoBarras())).thenReturn(false);
        when(imagenMock.isEmpty()).thenReturn(true);

        assertThrows(ElementoNulosException.class, () -> productoService.registroProducto(registroDto));
        verify(productoRepo, never()).save(any());
    }

    @Test
    void registroProducto_ProveedorNoEncontrado() throws Exception {
        when(productoRepo.existsByCodigoBarras(registroDto.codigoBarras())).thenReturn(false);
        when(productoMapper.toEntity(registroDto)).thenReturn(producto);
        when(cloudinaryService.uploadImage(imagenMock)).thenReturn("urlImagen");
        when(proveedorService.encontrarProveedor(1L)).thenThrow(new ElementoNoEncontradoException("Proveedor no encontrado"));

        assertThrows(ElementoNoEncontradoException.class, () -> productoService.registroProducto(registroDto));
        verify(productoRepo, never()).save(any());
    }
}

