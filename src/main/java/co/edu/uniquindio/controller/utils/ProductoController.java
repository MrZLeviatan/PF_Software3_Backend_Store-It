package co.edu.uniquindio.controller.utils;

import co.edu.uniquindio.dto.MensajeDto;
import co.edu.uniquindio.dto.objects.inventario.producto.ProductoDto;

import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;

import co.edu.uniquindio.service.objects.inventario.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/producto")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;


    @GetMapping("/listar")
    public ResponseEntity<MensajeDto<List<ProductoDto>>> obtenerProductos(){
        List<ProductoDto> listarProductos = productoService.listarProductoDto();
        return ResponseEntity.ok().body(new MensajeDto<>(false,listarProductos));
    }


    @GetMapping("/{idProducto}")
    public ResponseEntity<MensajeDto<ProductoDto>> obtenerProveedorPorId(@PathVariable Long idProducto)
            throws ElementoNoEncontradoException {

        ProductoDto productoDto = productoService.obtenerProductoDto(idProducto);

        return ResponseEntity.ok().body(new MensajeDto<>(false,productoDto));
    }

}
