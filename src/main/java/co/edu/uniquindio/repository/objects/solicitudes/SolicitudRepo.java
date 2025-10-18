package co.edu.uniquindio.repository.objects.solicitudes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudRepo extends JpaRepository<SolicitudRepo, Long>, JpaSpecificationExecutor<SolicitudRepo> {
}
