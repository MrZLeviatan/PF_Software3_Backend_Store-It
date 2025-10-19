package co.edu.uniquindio.repository.users;

import co.edu.uniquindio.models.entities.users.PersonalBodega;
import co.edu.uniquindio.models.enums.users.TipoPersonalBodega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonalBodegaRepo extends JpaRepository<PersonalBodega, Long>, JpaSpecificationExecutor<PersonalBodega> {

    List<PersonalBodega> findByDatosLaborales_Bodega_IdAndTipoPersonalBodega(Long bodegaId, TipoPersonalBodega tipoPersonal);


}
