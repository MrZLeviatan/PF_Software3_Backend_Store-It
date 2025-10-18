package co.edu.uniquindio.repository.users;

import co.edu.uniquindio.models.entities.users.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProveedorRepo extends JpaRepository<Proveedor, Long>, JpaSpecificationExecutor<Proveedor> {

}
