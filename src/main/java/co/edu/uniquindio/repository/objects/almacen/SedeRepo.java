package co.edu.uniquindio.repository.objects.almacen;

import co.edu.uniquindio.models.entities.objects.almacen.Sede;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SedeRepo extends JpaRepository<Sede, Long>, JpaSpecificationExecutor<Sede> {
}
