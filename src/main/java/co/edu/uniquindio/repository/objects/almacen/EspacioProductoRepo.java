package co.edu.uniquindio.repository.objects.almacen;

import co.edu.uniquindio.models.entities.objects.almacen.EspacioProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface EspacioProductoRepo extends JpaRepository<EspacioProducto, Long>, JpaSpecificationExecutor<EspacioProducto> {


    Optional<EspacioProducto> findByProducto_Id(Long idProducto);
}
