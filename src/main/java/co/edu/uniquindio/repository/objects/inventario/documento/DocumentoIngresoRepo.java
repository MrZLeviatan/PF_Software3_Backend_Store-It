package co.edu.uniquindio.repository.objects.inventario.documento;

import co.edu.uniquindio.models.entities.objects.inventario.documento.DocumentoIngreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentoIngresoRepo extends JpaRepository<DocumentoIngreso, Long>, JpaSpecificationExecutor<DocumentoIngreso> {
}
