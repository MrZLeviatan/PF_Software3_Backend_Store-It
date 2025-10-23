package co.edu.uniquindio.controller.objects.inventario;

import co.edu.uniquindio.dto.MensajeDto;
import co.edu.uniquindio.dto.objects.inventario.lote.LoteDto;
import co.edu.uniquindio.dto.objects.inventario.lote.RegistroLoteDto;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.exceptions.ElementoNoValidoException;
import co.edu.uniquindio.service.objects.inventario.LoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lote")
@RequiredArgsConstructor
public class LoteController {


    private final LoteService loteService;


    @PostMapping("/registrar")
    public ResponseEntity<MensajeDto<String>> registrarLote(
            @RequestBody @Valid RegistroLoteDto registroLoteDto)
            throws ElementoNoEncontradoException, ElementoNoValidoException {

        loteService.registroLote(registroLoteDto);
        return ResponseEntity.ok().body(new MensajeDto<>(false,"Registro exitoso"));
    }

    @GetMapping("/{idLote}")
    public ResponseEntity<MensajeDto<LoteDto>> obtenerLoteId(@PathVariable Long idLote)
            throws ElementoNoEncontradoException {
        LoteDto loteDto = loteService.obtenerLoteDtoId(idLote);
        return ResponseEntity.ok().body(new MensajeDto<>(false, loteDto));
    }


    @GetMapping("/espacio-producto/{idEspacio}/lotes")
    public ResponseEntity<MensajeDto<List<LoteDto>>> obtenerLotesEspacio(@PathVariable Long idEspacio)
            throws ElementoNoEncontradoException {

        List<LoteDto> lotes = loteService.obtenerLotesEspacio(idEspacio);
        return ResponseEntity.ok().body(new MensajeDto<>(false,lotes));
    }

}
