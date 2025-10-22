package co.edu.uniquindio.controller.objects.almacen;

import co.edu.uniquindio.dto.MensajeDto;
import co.edu.uniquindio.dto.objects.almacen.espacioProducto.EspacioProductoDto;
import co.edu.uniquindio.dto.objects.almacen.espacioProducto.RegistroEspacioProductoDto;
import co.edu.uniquindio.exceptions.*;
import co.edu.uniquindio.service.objects.almacen.EspacioProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/espacio-producto")
@RequiredArgsConstructor
public class EspacioProductoController {


    private final EspacioProductoService espacioProductoService;


    @PostMapping(value ="/registrar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('GESTOR_COMERCIAL')") // Solo permitido para el rol de Gestor Comercial
    public ResponseEntity<MensajeDto<EspacioProductoDto>> registraEspacioProducto(
            @Valid @RequestPart("espacioProducto") RegistroEspacioProductoDto registroEspacioProductoDto)
            throws ElementoNoValidoException, ElementoNoEncontradoException {

        EspacioProductoDto espacioProductoDto = espacioProductoService.registroEspacioProducto(registroEspacioProductoDto);
        return ResponseEntity.ok().body(new MensajeDto<>(false,espacioProductoDto));
    }


    @GetMapping("/{idEspacio}")
    public ResponseEntity<MensajeDto<EspacioProductoDto>> obtenerEspacioProducto(@PathVariable Long idEspacio)
            throws ElementoNoEncontradoException {

        EspacioProductoDto espacioProductoDto = espacioProductoService.obtenerEspacioProductoDto(idEspacio);
        return ResponseEntity.ok().body(new MensajeDto<>(false,espacioProductoDto));
    }


    // Endpoint para la busquea de los espacios del producto mediante el Id del producto
    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<MensajeDto<EspacioProductoDto>> obtenerEspacioDeProducto(@PathVariable Long idProducto)
            throws ElementoNoEncontradoException {

        EspacioProductoDto espacioProductoDto = espacioProductoService.obtenerEspacioDelProducto(idProducto);
        return ResponseEntity.ok().body(new MensajeDto<>(false,espacioProductoDto));
    }

}
