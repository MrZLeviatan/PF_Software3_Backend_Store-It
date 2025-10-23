package co.edu.uniquindio.controller.objects.inventario;

import co.edu.uniquindio.dto.MensajeDto;
import co.edu.uniquindio.dto.objects.inventario.producto.ProductoDto;

import co.edu.uniquindio.dto.objects.inventario.producto.RegistroProductoDto;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;

import co.edu.uniquindio.exceptions.ElementoNoValidoException;
import co.edu.uniquindio.exceptions.ElementoNulosException;
import co.edu.uniquindio.exceptions.ElementoRepetidoException;
import co.edu.uniquindio.models.enums.entities.TipoProducto;
import co.edu.uniquindio.service.objects.inventario.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/producto")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;


    // Método para registrar el producto
    @PostMapping("/registrar")
    @PreAuthorize("hasAnyAuthority('GESTOR_COMERCIAL')") // Solo permitido para el rol de Gestor Comercial
    public ResponseEntity<MensajeDto<ProductoDto>> registrarProducto(
            @Valid @ModelAttribute RegistroProductoDto registroProductoDto)
            throws ElementoRepetidoException, ElementoNoEncontradoException, ElementoNoValidoException, ElementoNulosException {

        ProductoDto productoDto = productoService.registroProducto(registroProductoDto);
        return ResponseEntity.ok().body(new MensajeDto<>(true,productoDto));
    }



    // Método para visualizar los productos (General para todos los usuarios)
    @GetMapping("/listar")
    public ResponseEntity<MensajeDto<List<ProductoDto>>> obtenerProductos(){
        List<ProductoDto> listarProductos = productoService.listarProductoDto();
        return ResponseEntity.ok().body(new MensajeDto<>(false,listarProductos));
    }


    // Endpoint para filtrar productos por TipoProducto o Id del Proveedor, con paginación.
    @GetMapping("/listar/filtro")
    public ResponseEntity<MensajeDto<List<ProductoDto>>> filtrarProductos(
            @RequestParam(required = false) TipoProducto tipoProducto,
            @RequestParam(required = false) String idProveedor,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int size) {

        List<ProductoDto> productosFiltrados = productoService.listarProductosFiltro(
                tipoProducto, idProveedor, pagina, size);
        return ResponseEntity.ok(new MensajeDto<>(false, productosFiltrados));
    }


    // Endpoint para obtener un producto específico
    @GetMapping("/{idProducto}")
    public ResponseEntity<MensajeDto<ProductoDto>> obtenerProveedorPorId(@PathVariable Long idProducto)
            throws ElementoNoEncontradoException {

        ProductoDto productoDto = productoService.obtenerProductoDto(idProducto);

        return ResponseEntity.ok().body(new MensajeDto<>(false,productoDto));
    }

}
