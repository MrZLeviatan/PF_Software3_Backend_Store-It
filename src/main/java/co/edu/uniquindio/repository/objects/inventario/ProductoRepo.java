package co.edu.uniquindio.repository.objects.inventario;

import co.edu.uniquindio.models.entities.objects.inventario.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepo extends JpaRepository<Producto, Long>, JpaSpecificationExecutor<Producto> {


    boolean existsByCodigoBarras(String codigoBarras);

}
