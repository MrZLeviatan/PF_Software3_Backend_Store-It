package productos;

import co.edu.uniquindio.dto.objects.inventario.producto.ProductoDto;
import co.edu.uniquindio.dto.objects.inventario.producto.RegistroProductoDto;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.exceptions.ElementoNulosException;
import co.edu.uniquindio.exceptions.ElementoRepetidoException;
import co.edu.uniquindio.mapper.objects.inventario.ProductoMapper;
import co.edu.uniquindio.models.entities.objects.inventario.Producto;
import co.edu.uniquindio.models.entities.users.Proveedor;
import co.edu.uniquindio.models.enums.entities.TipoProducto;
import co.edu.uniquindio.repository.objects.inventario.ProductoRepo;
import co.edu.uniquindio.service.objects.inventario.impl.ProductoServiceImpl;
import co.edu.uniquindio.service.users.ProveedorService;
import co.edu.uniquindio.service.utils.CloudinaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for ProductoServiceImpl
 * Clase de prueba para el servicio ProductoServiceImpl
 */
class ProductoServiceImplTest {

    @Mock
    private ProveedorService proveedorService;

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private ProductoRepo productoRepo;

    @Mock
    private ProductoMapper productoMapper;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private RegistroProductoDto registroProductoDto;
    private Proveedor proveedor;
    private Producto producto;
    private ProductoDto productoDto;
    private MultipartFile imagenMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Inicialización del proveedor
        proveedor = new Proveedor();
        proveedor.setId(1L);
        proveedor.setNombre("Proveedor 1");
        proveedor.setEmail("proveedor@test.com");
        proveedor.setTelefono("3214567890");
        proveedor.setMarca("TechBrand");
        proveedor.setProductos(new ArrayList<>());

        // Inicialización del producto
        producto = new Producto();
        producto.setCodigoBarras("ABC123");
        producto.setNombre("Teclado mecánico");
        producto.setValorCompra(100.0);
        producto.setTipoProducto(TipoProducto.ELECTRODOMESTICOS);
        producto.setProveedor(proveedor);

        // DTO esperado al mapear
        productoDto = new ProductoDto(
                1L,
                "ABC123",
                "Teclado mecánico",
                100.0,
                120.0,
                "http://imagen.cloudinary.com/producto.png",
                TipoProducto.ELECTRODOMESTICOS,
                null,
                1L
        );

        // Mock de imagen
        imagenMock = mock(MultipartFile.class);
        when(imagenMock.isEmpty()).thenReturn(false);

        // DTO de entrada
        registroProductoDto = new RegistroProductoDto(
                "ABC123",
                "Teclado mecánico",
                100.0,
                TipoProducto.ELECTRODOMESTICOS,
                1L,
                imagenMock
        );
    }

    // Caso exitoso: el producto se registra correctamente
    @Test
    void testRegistroProducto_Exitoso() throws Exception {
        when(productoRepo.existsByCodigoBarras("ABC123")).thenReturn(false);
        when(productoMapper.toEntity(registroProductoDto)).thenReturn(producto);
        when(cloudinaryService.uploadImage(imagenMock)).thenReturn("http://imagen.cloudinary.com/producto.png");
        when(proveedorService.encontrarProveedor(1L)).thenReturn(proveedor);
        when(productoMapper.toDto(producto)).thenReturn(productoDto);

        ProductoDto resultado = productoService.registroProducto(registroProductoDto);

        assertNotNull(resultado);
        assertEquals("Teclado mecánico", resultado.nombre());
        assertEquals("ABC123", resultado.codigoBarras());
        verify(productoRepo, times(1)).save(producto);
        verify(cloudinaryService, times(1)).uploadImage(imagenMock);
        verify(proveedorService, times(1)).encontrarProveedor(1L);
    }

    // Caso error: código de barras ya registrado
    @Test
    void testRegistroProducto_CodigoBarrasRepetido() {
        when(productoRepo.existsByCodigoBarras("ABC123")).thenReturn(true);

        assertThrows(ElementoRepetidoException.class, () ->
                productoService.registroProducto(registroProductoDto)
        );

        verify(productoRepo, times(1)).existsByCodigoBarras("ABC123");
        verifyNoMoreInteractions(productoMapper, cloudinaryService, proveedorService);
    }

    // Caso error: proveedor no válido
    @Test
    void testRegistroProducto_ProveedorNoValido() throws Exception {
        when(productoRepo.existsByCodigoBarras("ABC123")).thenReturn(false);
        when(productoMapper.toEntity(registroProductoDto)).thenReturn(producto);
        when(cloudinaryService.uploadImage(imagenMock)).thenReturn("http://imagen.cloudinary.com/producto.png");
        when(proveedorService.encontrarProveedor(1L))
                .thenThrow(new ElementoNoEncontradoException("Proveedor no encontrado"));

        assertThrows(ElementoNoEncontradoException.class, () ->
                productoService.registroProducto(registroProductoDto)
        );

        verify(proveedorService, times(1)).encontrarProveedor(1L);
    }

    // Caso error: imagen no válida o nula
    @Test
    void testRegistroProducto_ImagenInvalida() {
        RegistroProductoDto dtoSinImagen = new RegistroProductoDto(
                "ABC123",
                "Teclado mecánico",
                100.0,
                TipoProducto.ELECTRODOMESTICOS,
                1L,
                null // Imagen nula
        );

        when(productoRepo.existsByCodigoBarras("ABC123")).thenReturn(false);
        when(productoMapper.toEntity(dtoSinImagen)).thenReturn(producto);

        assertThrows(ElementoNulosException.class, () ->
                productoService.registroProducto(dtoSinImagen)
        );

        verify(productoRepo, times(1)).existsByCodigoBarras("ABC123");
        verifyNoInteractions(cloudinaryService);
    }
}
