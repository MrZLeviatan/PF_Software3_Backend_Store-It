package co.edu.uniquindio.controller.users;

import co.edu.uniquindio.dto.MensajeDto;
import co.edu.uniquindio.dto.users.proveedor.ProveedorDto;
import co.edu.uniquindio.dto.users.proveedor.RegistroProveedorDto;
import co.edu.uniquindio.exceptions.*;
import co.edu.uniquindio.service.users.ProveedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedor")
@RequiredArgsConstructor
public class ProveedorController {


    private final ProveedorService proveedorService;


    // Si no hay proveedor para registrar los productos
    @PostMapping("/registrar")
    @PreAuthorize("hasAnyAuthority('GESTOR_COMERCIAL')") // Solo permitido para el rol de Gestor Comercial
    public ResponseEntity<MensajeDto<ProveedorDto>> registrarProveedor(
            @Valid @RequestBody RegistroProveedorDto registroProveedorDto)
            throws ElementoNoValidoException, ElementoRepetidoException, ElementoNulosException, ElementoEliminadoException {

        ProveedorDto proveedorDto = proveedorService.registrarProveedor(registroProveedorDto);
        return ResponseEntity.ok().body(new MensajeDto<>(false,proveedorDto));
    }



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
