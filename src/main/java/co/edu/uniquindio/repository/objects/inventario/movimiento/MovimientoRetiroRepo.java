package co.edu.uniquindio.repository.objects.inventario.movimiento;

import co.edu.uniquindio.models.entities.objects.inventario.movimiento.MovimientoRetiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimientoRetiroRepo extends JpaRepository<MovimientoRetiro, Long>, JpaSpecificationExecutor<MovimientoRetiro> {
}
