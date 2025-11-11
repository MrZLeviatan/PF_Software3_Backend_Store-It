package co.edu.uniquindio.repository.objects.compra;

import co.edu.uniquindio.models.entities.objects.compra.DetalleCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleCompraRepo extends JpaRepository<DetalleCompra, Long>, JpaSpecificationExecutor<DetalleCompra> {


    // Total de unidades vendidas por producto
    @Query("""
        SELECT d.producto.id, SUM(d.cantidad)
        FROM DetalleCompra d
        GROUP BY d.producto.id
        ORDER BY SUM(d.cantidad) DESC
    """)
    List<Object[]> obtenerProductosMasVendidos();

}
