package co.edu.uniquindio.repository.objects.notificacion;

import co.edu.uniquindio.models.entities.objects.notificaciones.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepo extends JpaRepository<Notificacion, Long>, JpaSpecificationExecutor<Notificacion> {


    // Obtiene las notificaciones de un receptor, ordenadas por fecha
    List<Notificacion> findByReceptorIdOrderByFechaEnvioDesc(Long receptorId);
}
