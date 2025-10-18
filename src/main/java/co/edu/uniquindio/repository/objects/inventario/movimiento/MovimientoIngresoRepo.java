package co.edu.uniquindio.repository.objects.inventario.movimiento;

import co.edu.uniquindio.models.entities.objects.inventario.movimiento.MovimientoIngreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimientoIngresoRepo extends JpaRepository<MovimientoIngreso, Long>, JpaSpecificationExecutor<MovimientoIngreso> {
}
