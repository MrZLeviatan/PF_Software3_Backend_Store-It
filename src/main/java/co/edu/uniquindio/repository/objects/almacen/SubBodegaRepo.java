package co.edu.uniquindio.repository.objects.almacen;

import co.edu.uniquindio.models.entities.objects.almacen.SubBodega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SubBodegaRepo extends JpaRepository<SubBodega, Long>, JpaSpecificationExecutor<SubBodega> {
}
