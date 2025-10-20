package co.edu.uniquindio.repository.objects.inventario;

import co.edu.uniquindio.models.entities.objects.inventario.Producto;
import co.edu.uniquindio.models.enums.entities.TipoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepo extends JpaRepository<Producto, Long>, JpaSpecificationExecutor<Producto> {


    boolean existsByCodigoBarras(String codigoBarras);

    List<Producto> findAllByTipoProducto(TipoProducto tipoProducto);


}
