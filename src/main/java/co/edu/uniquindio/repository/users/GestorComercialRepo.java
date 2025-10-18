package co.edu.uniquindio.repository.users;

import co.edu.uniquindio.models.entities.users.GestorComercial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface GestorComercialRepo extends JpaRepository<GestorComercial, Long>, JpaSpecificationExecutor<GestorComercial> {

}
