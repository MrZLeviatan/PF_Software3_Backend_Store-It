package carrito;

import co.edu.uniquindio.dto.objects.compras.itemCarrito.RegistroItemCompraDto;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.exceptions.ElementoRepetidoException;
import co.edu.uniquindio.models.entities.objects.almacen.EspacioProducto;
import co.edu.uniquindio.models.entities.objects.compra.CarritoCompra;
import co.edu.uniquindio.models.entities.objects.compra.ItemsCarrito;
import co.edu.uniquindio.models.entities.objects.inventario.Lote;
import co.edu.uniquindio.models.entities.objects.inventario.Producto;
import co.edu.uniquindio.models.entities.users.Cliente;
import co.edu.uniquindio.models.enums.entities.EstadoLote;
import co.edu.uniquindio.repository.objects.compra.CarritoCompraRepo;
import co.edu.uniquindio.repository.objects.inventario.LoteRepo;
import co.edu.uniquindio.repository.objects.inventario.ProductoRepo;
import co.edu.uniquindio.repository.users.ClienteRepo;
import co.edu.uniquindio.service.objects.compra.impl.CarritoCompraServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class CarritoCompraServiceImplTest {

    @Mock
    private ClienteRepo clienteRepo;

    @Mock
    private ProductoRepo productoRepo;

    @Mock
    private LoteRepo loteRepo;

    @Mock
    private CarritoCompraRepo carritoCompraRepo;

    @InjectMocks
    private CarritoCompraServiceImpl carritoCompraService;

    private Cliente cliente;
    private Producto producto;
    private EspacioProducto espacioProducto;
    private Lote lote;
    private CarritoCompra carrito;
    private RegistroItemCompraDto registroDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Cliente inicial
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan Pérez");

        carrito = new CarritoCompra();
        carrito.setId(10L);
        carrito.setCliente(cliente);
        carrito.setItemsCarrito(new ArrayList<>());
        cliente.setCarritoCompra(carrito);

        // Producto inicial
        producto = new Producto();
        producto.setId(5L);
        producto.setNombre("Mouse gamer");
        producto.setValorVenta(80.0);

        // EspacioProducto y Lote activo
        espacioProducto = new EspacioProducto();
        producto.setEspacioProducto(espacioProducto);

        lote = new Lote();
        lote.setId(100L);
        lote.setEstadoLote(EstadoLote.ACTIVO);
        lote.setEspacioProducto(espacioProducto);

        // DTO de registro
        registroDto = new RegistroItemCompraDto(
                producto.getId(),
                2,
                cliente.getId()
        );
    }

    // Caso exitoso: producto agregado correctamente
    @Test
    void testAgregarProductoCarritoCompra_Exitoso() throws Exception {
        when(clienteRepo.findById(1L)).thenReturn(Optional.of(cliente));
        when(productoRepo.findById(5L)).thenReturn(Optional.of(producto));
        when(loteRepo.findByEspacioProductoAndEstadoLote(espacioProducto, EstadoLote.ACTIVO))
                .thenReturn(List.of(lote));

        carritoCompraService.agregarProductoCarritoCompra(registroDto);

        assertEquals(1, carrito.getItemsCarrito().size());
        ItemsCarrito item = carrito.getItemsCarrito().get(0);
        assertEquals(2, item.getCantidad());
        assertEquals(160.0, item.getValorTotal());
        verify(carritoCompraRepo, times(1)).save(carrito);
    }

    // Caso error: producto no existe
    @Test
    void testAgregarProductoCarritoCompra_ProductoNoExiste() {
        when(clienteRepo.findById(1L)).thenReturn(Optional.of(cliente));
        when(productoRepo.findById(5L)).thenReturn(Optional.empty());

        assertThrows(ElementoNoEncontradoException.class, () ->
                carritoCompraService.agregarProductoCarritoCompra(registroDto)
        );

        verify(productoRepo, times(1)).findById(5L);
        verifyNoInteractions(loteRepo, carritoCompraRepo);
    }

    // Caso error: no hay lotes disponibles
    @Test
    void testAgregarProductoCarritoCompra_SinLotesDisponibles() {
        when(clienteRepo.findById(1L)).thenReturn(Optional.of(cliente));
        when(productoRepo.findById(5L)).thenReturn(Optional.of(producto));
        when(loteRepo.findByEspacioProductoAndEstadoLote(espacioProducto, EstadoLote.ACTIVO))
                .thenReturn(new ArrayList<>()); // sin lotes

        assertThrows(ElementoNoEncontradoException.class, () ->
                carritoCompraService.agregarProductoCarritoCompra(registroDto)
        );

        verify(loteRepo, times(1))
                .findByEspacioProductoAndEstadoLote(espacioProducto, EstadoLote.ACTIVO);
    }

    // Caso error: producto ya existente en el carrito
    @Test
    void testAgregarProductoCarritoCompra_ProductoYaEnCarrito() {
        // Agregamos manualmente un item existente
        ItemsCarrito itemExistente = new ItemsCarrito();
        itemExistente.setProducto(producto);
        itemExistente.setCantidad(1);
        itemExistente.setValorTotal(80.0);
        itemExistente.setCarritoCompra(carrito);
        carrito.getItemsCarrito().add(itemExistente);

        when(clienteRepo.findById(1L)).thenReturn(Optional.of(cliente));
        when(productoRepo.findById(5L)).thenReturn(Optional.of(producto));
        when(loteRepo.findByEspacioProductoAndEstadoLote(espacioProducto, EstadoLote.ACTIVO))
                .thenReturn(List.of(lote));

        assertThrows(ElementoRepetidoException.class, () ->
                carritoCompraService.agregarProductoCarritoCompra(registroDto)
        );

        verify(carritoCompraRepo, never()).save(any());
    }
}
