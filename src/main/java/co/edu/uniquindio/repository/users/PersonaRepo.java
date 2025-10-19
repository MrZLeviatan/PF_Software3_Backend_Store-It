package co.edu.uniquindio.repository.users;

import co.edu.uniquindio.models.entities.users.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonaRepo extends JpaRepository<Persona, Long>, JpaSpecificationExecutor<Persona> {

    Optional<Persona> findByUser_Email(String email);

    Optional<Persona> findByTelefono(String telefono);

    Optional<Persona> findByTelefonoSecundario(String telefonoSecundario);

    boolean existsByTelefono(String telefono);

    boolean existsByTelefonoOrTelefonoSecundario(String telefono, String telefonoSecundario);

}

