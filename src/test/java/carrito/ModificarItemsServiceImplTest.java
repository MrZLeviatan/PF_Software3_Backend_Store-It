package carrito;

import co.edu.uniquindio.dto.objects.compras.itemCarrito.ModificarCantidadItemsDto;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.exceptions.ElementoNoValidoException;
import co.edu.uniquindio.models.entities.objects.almacen.EspacioProducto;
import co.edu.uniquindio.models.entities.objects.compra.CarritoCompra;
import co.edu.uniquindio.models.entities.objects.compra.ItemsCarrito;
import co.edu.uniquindio.models.entities.objects.inventario.Lote;
import co.edu.uniquindio.models.entities.objects.inventario.Producto;
import co.edu.uniquindio.models.entities.users.Cliente;
import co.edu.uniquindio.models.enums.entities.EstadoLote;
import co.edu.uniquindio.models.enums.entities.TipoProducto;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ModificarItemsServiceImplTest {

    @Mock
    private CarritoCompraRepo carritoCompraRepo;
    @Mock
    private ProductoRepo productoRepo;
    @Mock
    private ClienteRepo clienteRepo;
    @Mock
    private LoteRepo loteRepo;

    @InjectMocks
    private CarritoCompraServiceImpl carritoCompraService;

    private Cliente cliente;
    private Producto producto;
    private CarritoCompra carritoCompra;
    private ItemsCarrito itemCarrito;
    private EspacioProducto espacioProducto;
    private Lote loteActivo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Simulated client / Cliente simulado
        cliente = new Cliente();
        cliente.setId(1L);

        // Shopping cart linked to the client / Carrito asociado al cliente
        carritoCompra = new CarritoCompra();
        carritoCompra.setId(1L);
        carritoCompra.setCliente(cliente);

        // Simulated product / Producto simulado
        producto = new Producto();
        producto.setId(10L);
        producto.setNombre("Laptop Lenovo");
        producto.setValorVenta(2000.0);
        producto.setTipoProducto(TipoProducto.ELECTRODOMESTICOS);

        // Simulated space and lot / Espacio y lote simulados
        espacioProducto = new EspacioProducto();
        loteActivo = new Lote();
        loteActivo.setCantidadDisponible(10);
        loteActivo.setEstadoLote(EstadoLote.ACTIVO);
        loteActivo.setFechaIngreso(LocalDate.now());

        // Usamos una lista modificable para evitar UnsupportedOperationException
        espacioProducto.setLotes(new java.util.ArrayList<>(List.of(loteActivo)));
        producto.setEspacioProducto(espacioProducto);

        // Existing item in cart / Item existente en el carrito
        itemCarrito = new ItemsCarrito();
        itemCarrito.setProducto(producto);
        itemCarrito.setCantidad(1);
        itemCarrito.setValorTotal(2000.0);
        itemCarrito.setCarritoCompra(carritoCompra);

        // Lista modificable para los ítems del carrito
        carritoCompra.setItemsCarrito(new java.util.ArrayList<>(List.of(itemCarrito)));
        cliente.setCarritoCompra(carritoCompra);
    }

    @Test
    void modificarCantidadProductoCarrito_exitoso() throws Exception {
        // Configurar mocks
        when(clienteRepo.findById(1L)).thenReturn(Optional.of(cliente));
        when(productoRepo.findById(10L)).thenReturn(Optional.of(producto));

        ModificarCantidadItemsDto dto = new ModificarCantidadItemsDto(10L, 1L, 5);

        // Ejecutar
        carritoCompraService.modificarCantidadProductoCarrito(dto);

        // Verificar
        verify(carritoCompraRepo, times(1)).save(any(CarritoCompra.class));
        assertEquals(5, itemCarrito.getCantidad());
    }

    @Test
    void modificarCantidadProductoCarrito_clienteNoEncontrado() {
        when(clienteRepo.findById(1L)).thenReturn(Optional.empty());

        ModificarCantidadItemsDto dto = new ModificarCantidadItemsDto(10L, 1L, 3);

        assertThrows(ElementoNoEncontradoException.class, () ->
                carritoCompraService.modificarCantidadProductoCarrito(dto));
    }

    @Test
    void modificarCantidadProductoCarrito_sinEspacioAsignado() {
        producto.setEspacioProducto(null);
        when(clienteRepo.findById(1L)).thenReturn(Optional.of(cliente));
        when(productoRepo.findById(10L)).thenReturn(Optional.of(producto));

        ModificarCantidadItemsDto dto = new ModificarCantidadItemsDto(10L, 1L, 2);

        assertThrows(ElementoNoValidoException.class, () ->
                carritoCompraService.modificarCantidadProductoCarrito(dto));
    }

    @Test
    void modificarCantidadProductoCarrito_sinLotesDisponibles() {
        espacioProducto.setLotes(List.of());
        when(clienteRepo.findById(1L)).thenReturn(Optional.of(cliente));
        when(productoRepo.findById(10L)).thenReturn(Optional.of(producto));

        ModificarCantidadItemsDto dto = new ModificarCantidadItemsDto(10L, 1L, 1);

        assertThrows(ElementoNoValidoException.class, () ->
                carritoCompraService.modificarCantidadProductoCarrito(dto));
    }

    @Test
    void modificarCantidadProductoCarrito_noSuficienteInventario() {
        loteActivo.setCantidadDisponible(3);
        when(clienteRepo.findById(1L)).thenReturn(Optional.of(cliente));
        when(productoRepo.findById(10L)).thenReturn(Optional.of(producto));

        ModificarCantidadItemsDto dto = new ModificarCantidadItemsDto(10L, 1L, 10);

        assertThrows(ElementoNoValidoException.class, () ->
                carritoCompraService.modificarCantidadProductoCarrito(dto));
    }
}
