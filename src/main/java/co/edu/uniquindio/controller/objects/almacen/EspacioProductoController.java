package co.edu.uniquindio.controller.objects.almacen;

import co.edu.uniquindio.dto.MensajeDto;
import co.edu.uniquindio.dto.objects.almacen.espacioProducto.EspacioProductoDto;
import co.edu.uniquindio.exceptions.*;
import co.edu.uniquindio.service.objects.almacen.EspacioProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/espacio-producto")
@RequiredArgsConstructor
public class EspacioProductoController {


    private final EspacioProductoService espacioProductoService;



    @GetMapping("/{idEspacio}")
    public ResponseEntity<MensajeDto<EspacioProductoDto>> obtenerEspacioProducto(@PathVariable Long idEspacio)
            throws ElementoNoEncontradoException {

        EspacioProductoDto espacioProductoDto = espacioProductoService.obtenerEspacioProductoDto(idEspacio);
        return ResponseEntity.ok().body(new MensajeDto<>(false,espacioProductoDto));
    }


    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<MensajeDto<EspacioProductoDto>> obtenerEspacioDeProducto(@PathVariable Long idProducto)
            throws ElementoNoEncontradoException {

        EspacioProductoDto espacioProductoDto = espacioProductoService.obtenerEspacioDelProducto(idProducto);
        return ResponseEntity.ok().body(new MensajeDto<>(false,espacioProductoDto));
    }

}
