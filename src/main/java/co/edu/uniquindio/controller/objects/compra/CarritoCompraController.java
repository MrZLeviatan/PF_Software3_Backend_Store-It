package co.edu.uniquindio.controller.objects.compra;


import co.edu.uniquindio.dto.MensajeDto;
import co.edu.uniquindio.dto.objects.compras.carritoCompra.CarritoCompraDto;
import co.edu.uniquindio.dto.objects.compras.itemCarrito.EliminarItemsCarritoDto;
import co.edu.uniquindio.dto.objects.compras.itemCarrito.ModificarCantidadItemsDto;
import co.edu.uniquindio.dto.objects.compras.itemCarrito.RegistroItemCompraDto;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.exceptions.ElementoNoValidoException;
import co.edu.uniquindio.exceptions.ElementoRepetidoException;
import co.edu.uniquindio.service.objects.compra.CarritoCompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrito-compra")
@RequiredArgsConstructor
public class CarritoCompraController {


    private final CarritoCompraService carritoCompraService;


    @PostMapping("/agregar")
    public ResponseEntity<MensajeDto<String>> agregarProductoCarrito(
            @RequestBody RegistroItemCompraDto registroItemCompraDto
    ) throws ElementoNoEncontradoException, ElementoRepetidoException {
        carritoCompraService.agregarProductoCarritoCompra(registroItemCompraDto);
        return ResponseEntity.ok().body(new MensajeDto<>(false,"Producto agregado al carrito correctamente"));
    }


    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<MensajeDto<CarritoCompraDto>> obtenerCarritoCompraCliente(@PathVariable Long idCliente)
            throws ElementoNoEncontradoException {

        CarritoCompraDto carritoCompraDto = carritoCompraService.obtenerCarritoCompraCliente(idCliente);
        return ResponseEntity.ok().body(new MensajeDto<>(false, carritoCompraDto));
    }


    // Endpoint para agregar cantidad a un producto del carrito
    @PostMapping("/modificar-cantidad")
    public ResponseEntity<MensajeDto<String>> agregarCantidadProductoCarrito(
            @RequestBody ModificarCantidadItemsDto modificarCantidadItemsDto
    ) throws ElementoNoEncontradoException, ElementoNoValidoException {
        carritoCompraService.modificarCantidadProductoCarrito(modificarCantidadItemsDto);
        return ResponseEntity.ok(new MensajeDto<>(false, "Cantidad del producto actualizada correctamente"));
    }



    // Endpoint para eliminar un producto completo del carrito
    @PostMapping("/eliminar")
    public ResponseEntity<MensajeDto<String>> eliminarProductoCarrito(
            @RequestBody EliminarItemsCarritoDto eliminarProductoCarritoDto
    ) throws ElementoNoEncontradoException {
        carritoCompraService.eliminarProductoCarritoCompra(eliminarProductoCarritoDto);
        return ResponseEntity.ok(new MensajeDto<>(false, "Producto eliminado del carrito correctamente"));
    }


}
