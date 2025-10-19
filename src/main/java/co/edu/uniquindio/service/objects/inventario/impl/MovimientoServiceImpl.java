package co.edu.uniquindio.service.objects.inventario.impl;

import co.edu.uniquindio.dto.objects.inventario.lote.RegistroLoteDto;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.mapper.objects.inventario.movimiento.MovimientoIngresoMapper;
import co.edu.uniquindio.models.entities.objects.inventario.Lote;
import co.edu.uniquindio.models.entities.objects.inventario.movimiento.MovimientoIngreso;
import co.edu.uniquindio.models.entities.users.GestorComercial;
import co.edu.uniquindio.models.enums.entities.EstadoMovimientoIngreso;
import co.edu.uniquindio.models.enums.entities.TipoMovimiento;
import co.edu.uniquindio.repository.objects.inventario.movimiento.MovimientoIngresoRepo;
import co.edu.uniquindio.service.objects.inventario.DocumentoService;
import co.edu.uniquindio.service.objects.inventario.MovimientoService;
import co.edu.uniquindio.service.users.GestorComercialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoIngresoRepo movimientoIngresoRepo;
    private final MovimientoIngresoMapper movimientoIngresoMapper;

    private final GestorComercialService gestorComercialService;
    private final DocumentoService documentoService;



    @Override
    public void registroMovimientoIngreso(Lote lote, RegistroLoteDto registroLoteDto)
            throws ElementoNoEncontradoException {

        // Se registra el movimiento Ingreso
        MovimientoIngreso movimientoIngreso = new MovimientoIngreso();

        movimientoIngreso.setFechaSolicitud(LocalDate.now());
        movimientoIngreso.setCantidad(lote.getCantidadDisponible());
        movimientoIngreso.setObservaciones(registroLoteDto.observaciones());
        movimientoIngreso.setTipoMovimiento(TipoMovimiento.INGRESO);

        // Se asignan las asociaciones
        movimientoIngreso.setLote(lote);
        lote.agregarMovimiento(movimientoIngreso);

        GestorComercial gestorComercial = gestorComercialService.obtenerGestorComercialId(registroLoteDto.idGestorComercial());
        gestorComercial.agregarMovimiento(movimientoIngreso);

        movimientoIngreso.setProveedor(lote.getEspacioProducto().getProducto().getProveedor());
        movimientoIngreso.setEstadoMovimientoIngreso(EstadoMovimientoIngreso.PENDIENTE);

        // Se guarda en la BD
        movimientoIngresoRepo.save(movimientoIngreso);

        // Registra el nuevo movimiento
        documentoService.registrarDocumentoIngreso(lote,registroLoteDto,movimientoIngreso);

    }
}
