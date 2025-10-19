package co.edu.uniquindio.controller.utils;

import co.edu.uniquindio.dto.MensajeDto;
import co.edu.uniquindio.dto.users.proveedor.ProveedorDto;
import co.edu.uniquindio.dto.users.proveedor.RegistroProveedorDto;
import co.edu.uniquindio.exceptions.*;
import co.edu.uniquindio.service.users.ProveedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedor")
@RequiredArgsConstructor
public class ProveedorController {


    private final ProveedorService proveedorService;


    @GetMapping("/{idProveedor}")
    public ResponseEntity<MensajeDto<ProveedorDto>> obtenerProveedorPorId(@PathVariable Long idProveedor)
            throws ElementoNoEncontradoException {

        ProveedorDto proveedorDto = proveedorService.encontrarProveedorDto(idProveedor);

        return ResponseEntity.ok().body(new MensajeDto<>(false,proveedorDto));
    }


    @GetMapping("/listar")
    public ResponseEntity<MensajeDto<List<ProveedorDto>>> obtenerProveedores() {

        List<ProveedorDto> proveedores = proveedorService.listarProveedores();
        return ResponseEntity.ok().body(new MensajeDto<>(false,proveedores));
    }








}
