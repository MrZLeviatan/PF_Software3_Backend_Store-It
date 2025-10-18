package co.edu.uniquindio.repository.objects.inventario.documento;

import co.edu.uniquindio.models.entities.objects.inventario.documento.DocumentoRetiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentoRetiroRepo extends JpaRepository<DocumentoRetiro, Long>, JpaSpecificationExecutor<DocumentoRetiro> {
}
