package co.edu.uniquindio.repository.objects.compra;

import co.edu.uniquindio.models.entities.objects.compra.ItemsCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemCarritoRepo extends JpaRepository<ItemsCarrito, Long>, JpaSpecificationExecutor<ItemsCarrito> {
}
