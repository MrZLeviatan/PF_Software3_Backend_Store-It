package co.edu.uniquindio.repository.objects.inventario;

import co.edu.uniquindio.models.entities.objects.inventario.Lote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LoteRepo extends JpaRepository<Lote, Long>, JpaSpecificationExecutor<Lote> {
}
