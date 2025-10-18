package co.edu.uniquindio.repository.objects.notificacion;

import co.edu.uniquindio.models.entities.objects.notificaciones.ReferenciaEntidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferenciaEntidadRepo extends JpaRepository<ReferenciaEntidad, Long>, JpaSpecificationExecutor<ReferenciaEntidad> {
}
