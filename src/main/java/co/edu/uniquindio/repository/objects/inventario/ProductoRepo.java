package co.edu.uniquindio.repository.objects.inventario;

import co.edu.uniquindio.models.entities.objects.inventario.Producto;
import co.edu.uniquindio.models.enums.entities.TipoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepo extends JpaRepository<Producto, Long>, JpaSpecificationExecutor<Producto> {

    // Verifica la existencia de un mismo código de barras para un producto.
    boolean existsByCodigoBarras(String codigoBarras);

    // Se obtienen los productos con el mismo tipo de producto.
    List<Producto> findAllByTipoProducto(TipoProducto tipoProducto);


}
