package co.edu.uniquindio.repository.objects.solicitudes.incidentes;

import co.edu.uniquindio.models.entities.objects.solicitudes.incidentes.IncidenteLote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidenteLoteRepo extends JpaRepository<IncidenteLote, Long>, JpaSpecificationExecutor<IncidenteLote> {
}
