package co.edu.uniquindio.controller.gestorComercial;

import co.edu.uniquindio.dto.MensajeDto;
import co.edu.uniquindio.dto.objects.almacen.espacioProducto.RegistroEspacioProductoDto;
import co.edu.uniquindio.dto.objects.inventario.lote.RegistroLoteDto;
import co.edu.uniquindio.dto.objects.inventario.producto.RegistroProductoDto;
import co.edu.uniquindio.dto.objects.solicitudes.solicitud.RegistroSolicitudDto;
import co.edu.uniquindio.dto.objects.solicitudes.solicitud.SolicitudDto;
import co.edu.uniquindio.dto.users.proveedor.RegistroProveedorDto;
import co.edu.uniquindio.exceptions.*;
import co.edu.uniquindio.service.objects.almacen.EspacioProductoService;
import co.edu.uniquindio.service.objects.inventario.LoteService;
import co.edu.uniquindio.service.objects.inventario.ProductoService;
import co.edu.uniquindio.service.objects.solicitudes.SolicitudService;
import co.edu.uniquindio.service.users.ProveedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gestor-comercial")
public class GestorComercialController {


    // -------- Lotes -------------

    private final LoteService loteService;

    @PostMapping("/lotes/registrar")
    public ResponseEntity<MensajeDto<String>> registrarLote(
            @RequestBody @Valid RegistroLoteDto registroLoteDto)
            throws ElementoNoEncontradoException, ElementoNoValidoException {

        loteService.registroLote(registroLoteDto);
        return ResponseEntity.ok().body(new MensajeDto<>(false,"Registro exitoso"));
    }


    // -------- EspacioProducto -------------

    private final EspacioProductoService espacioProductoService;

    @PostMapping("/espacio-producto/registro")
    public ResponseEntity<MensajeDto<String>> registrarProveedor(
            @Valid @RequestBody RegistroEspacioProductoDto registroEspacioProductoDto)
            throws ElementoNoValidoException, ElementoNoEncontradoException {

        espacioProductoService.registroEspacioProducto(registroEspacioProductoDto);
        return ResponseEntity.ok().body(new MensajeDto<>(false,"Registro logrado exitosamente."));
    }


    // -------- Producto -------------

    private final ProductoService productoService;

    @PostMapping("/producto/registrar")
    public ResponseEntity<MensajeDto<String>> registrarProducto(
            @Valid @ModelAttribute RegistroProductoDto registroProductoDto)
            throws ElementoRepetidoException, ElementoNoEncontradoException, ElementoNoValidoException, ElementoNulosException {

        productoService.registroProducto(registroProductoDto);
        return ResponseEntity.ok().body(new MensajeDto<>(true,"Registro de producto exitoso"));
    }

    // -------- Proveedor -------------

    private final ProveedorService proveedorService;

    // Si no hay proveedor para registrar los productos
    @PostMapping("/proveedor/registro")
    public ResponseEntity<MensajeDto<String>> registrarProveedor(
            @Valid @RequestBody RegistroProveedorDto registroProveedorDto)
            throws ElementoNoValidoException, ElementoRepetidoException, ElementoNulosException, ElementoEliminadoException {

        proveedorService.registrarProveedor(registroProveedorDto);
        return ResponseEntity.ok().body(new MensajeDto<>(false,"Registro logrado exitosamente."));
    }


    // -------- Solicitudes -------------

    private final SolicitudService solicitudService;

    @PostMapping("/solicitud/ampliar")
    public ResponseEntity<MensajeDto<String>> ampliarEspacio(
            @RequestBody @Valid RegistroSolicitudDto registroSolicitudDto)
            throws ElementoNoEncontradoException {

        solicitudService.registrarSolicitud(registroSolicitudDto);
        return ResponseEntity.ok().body(new MensajeDto<>(false,"Ampliar espacio en proceso"));
    }


    @GetMapping("/{idGestor}/listar")
    public ResponseEntity<MensajeDto<List<SolicitudDto>>> obtenerSolicitud(@PathVariable Long idGestor)
            throws ElementoNoEncontradoException {

        List<SolicitudDto> solicitudDtos = solicitudService.obtenerSolicitudesGestor(idGestor);
        return ResponseEntity.ok().body(new MensajeDto<>(false,solicitudDtos));
    }




}
