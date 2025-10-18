package co.edu.uniquindio.repository.objects.solicitudes.incidentes;

import co.edu.uniquindio.models.entities.objects.solicitudes.incidentes.IncidenteMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidenteMovimientoRepo extends JpaRepository<IncidenteMovimiento, Long>, JpaSpecificationExecutor<IncidenteMovimiento> {
}
