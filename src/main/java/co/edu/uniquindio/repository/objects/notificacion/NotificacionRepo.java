package co.edu.uniquindio.repository.objects.notificacion;

import co.edu.uniquindio.models.entities.objects.notificaciones.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacionRepo extends JpaRepository<Notificacion, Long>, JpaSpecificationExecutor<Notificacion> {
}
