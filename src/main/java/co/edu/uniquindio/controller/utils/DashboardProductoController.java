package co.edu.uniquindio.controller.utils;


import co.edu.uniquindio.dto.MensajeDto;
import co.edu.uniquindio.dto.objects.inventario.producto.ResumenDashboardDto;
import co.edu.uniquindio.service.common.DashboardProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardProductoController {

    private final DashboardProductoService dashboardProductoService;


    @GetMapping("/productos")
    public ResponseEntity<MensajeDto<ResumenDashboardDto>> obtenerResumenProductos(){
        return ResponseEntity.ok(new MensajeDto<>(false, dashboardProductoService.obtenerResumenProductos()));
    }
}
