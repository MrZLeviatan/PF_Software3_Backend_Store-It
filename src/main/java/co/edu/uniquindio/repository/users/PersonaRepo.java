package co.edu.uniquindio.repository.users;

import co.edu.uniquindio.models.entities.users.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio encargado de la gestión de la entidad {@link Persona}.
 * <br>
 * Proporciona métodos personalizados para la búsqueda y validación de personas según su información de contacto, como email y teléfonos.
 */
@Repository
public interface PersonaRepo extends JpaRepository<Persona, Long>, JpaSpecificationExecutor<Persona> {

    // Busca una persona según el correo electrónico asociado a su usuario.
    Optional<Persona> findByUser_Email(String email);

    // Busca una persona según su número de teléfono principal.
    Optional<Persona> findByTelefono(String telefono);

    // Busca una persona según su número de teléfono secundario.
    Optional<Persona> findByTelefonoSecundario(String telefonoSecundario);

    // Verifica si ya existe una persona registrada con el número de teléfono dado.
    boolean existsByTelefono(String telefono);

    // Verifica si ya existe una persona registrada con el número de teléfono principal o secundario.
    boolean existsByTelefonoOrTelefonoSecundario(String telefono, String telefonoSecundario);

}

