package co.edu.uniquindio.repository.objects.inventario;

import co.edu.uniquindio.models.entities.objects.almacen.EspacioProducto;
import co.edu.uniquindio.models.entities.objects.inventario.Lote;
import co.edu.uniquindio.models.enums.entities.EstadoLote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoteRepo extends JpaRepository<Lote, Long>, JpaSpecificationExecutor<Lote> {


    List<Lote> findByEspacioProductoAndEstadoLote(EspacioProducto espacioProducto, EstadoLote estadoLote);

    List<Lote> findByEspacioProducto_Producto_IdOrderByFechaVencimientoAsc(Long id);

    List<Lote> findByEspacioProducto_Producto_IdOrderByFechaIngresoAsc(Long id);
}
