package co.edu.uniquindio.repository.users;

import co.edu.uniquindio.models.entities.users.AdminBodega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface AdminBodegaRepo extends JpaRepository<AdminBodega, Long>, JpaSpecificationExecutor<AdminBodega> {

}
