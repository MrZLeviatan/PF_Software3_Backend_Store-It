package co.edu.uniquindio.repository.objects.compra;

import co.edu.uniquindio.models.entities.objects.compra.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FacturaRepo extends JpaRepository<Factura, Long>, JpaSpecificationExecutor<Factura> {
}
