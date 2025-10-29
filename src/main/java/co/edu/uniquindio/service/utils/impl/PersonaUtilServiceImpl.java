package co.edu.uniquindio.service.utils.impl;

import co.edu.uniquindio.dto.common.email.EmailDto;
import co.edu.uniquindio.exceptions.*;
import co.edu.uniquindio.models.embeddable.Codigo;
import co.edu.uniquindio.models.entities.users.Persona;
import co.edu.uniquindio.models.enums.users.EstadoCuenta;
import co.edu.uniquindio.repository.users.PersonaRepo;
import co.edu.uniquindio.service.utils.CodigoService;
import co.edu.uniquindio.service.utils.EmailService;
import co.edu.uniquindio.service.utils.PersonaUtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de utilidades para la gestión y validación de personas dentro del sistema.
 * <br>
 * Incluye validaciones de email, teléfonos, y acciones auxiliares de registro y persistencia.
 */
@Service
@RequiredArgsConstructor
public class PersonaUtilServiceImpl implements PersonaUtilService {

    // Repositorio encargado de las operaciones CRUD sobre la entidad Persona.
    private final PersonaRepo personaRepo;
    // Servicio encargado del envío de correos electrónicos (verificación, recuperación, etc.)
    private final EmailService emailService;
    // Servicio encargado de la generación y validación de códigos de verificación.
    private final CodigoService codigoService;


    /**
     * Verifica que un email no esté repetido ni asociado a una cuenta eliminada o activa.
     * <br>
     * Si el correo pertenece a una cuenta inactiva, reenvía un nuevo código de verificación.
     */
    @Override
    public void validarEmailNoRepetido(String email) throws ElementoRepetidoException, ElementoEliminadoException {

        // 1. Buscar el correo en el repositorio genérico de Persona.
        Optional<Persona> personaOpt = personaRepo.findByUser_Email(email);

        if (personaOpt.isPresent()) {
            Persona persona = personaOpt.get();
            EstadoCuenta estadoCuenta = persona.getUser().getEstadoCuenta();

            switch (estadoCuenta) {
                // Si el usuario fue eliminado
                case ELIMINADO -> throw new ElementoEliminadoException("Email asociado a una cuenta eliminada");
                // Si el usuario está activo
                case ACTIVO -> throw new ElementoRepetidoException("Email asociado a una cuenta activada");
                // Si la cuenta está inactiva
                case INACTIVA -> {
                    // Si la cuenta está inactiva, genera y reenvía un nuevo código de verificación.
                    String verificacion = UUID.randomUUID()
                            .toString()
                            .substring(0, 6)
                            .toUpperCase();

                    Codigo codigoVerificacion = codigoService.generarCodigoVerificacion2AF();

                    // Guarda el usuario actualizado con el nuevo código de verificación.
                    personaRepo.save(persona);

                    // Enviar nuevo correo de verificación
                    EmailDto emailDto = new EmailDto(
                            persona.getUser().getEmail(),
                            verificacion,
                            "Reverificación de Cuenta - Store-It"
                    );

                    emailService.enviarEmailVerificacionRegistro(emailDto);

                    // Se lanza una excepción para él avisó del reenvío del email.
                    throw new ElementoRepetidoException(
                            "La cuenta ya existe pero está inactiva. "
                                    + "Hemos enviado un nuevo código de verificación a tu correo."
                    );
                }}}
    }


    // Verifica que los números telefónicos (principal y secundario) no estén asociados a otra cuenta activa.
    @Override
    public void validarTelefonoNoRepetido(String telefono, String telefonoSecundario)
            throws ElementoRepetidoException, ElementoNulosException, ElementoNoValidoException {

        // Validamos que el telefono no sea nulo o vacío
        if (telefono == null || telefono.isBlank()) {
            throw new ElementoNulosException("Teléfono no puede ser nulo o blank");
        }

        boolean existe;

        // Validamos la existencia del telefono principal
        existe = personaRepo.existsByTelefono(telefono);

        // Validamos la existencia del teléfono principal y secundario (Si existe)
        if (telefonoSecundario != null && !telefonoSecundario.isBlank()) {
            existe = personaRepo.existsByTelefonoOrTelefonoSecundario(telefono, telefonoSecundario);
        }

        //  Validar si alguno ya pertenece a otra cuenta activa
        if (existe) {
            Persona personaOpt = personaRepo.findByTelefono(telefono)
                    .or(() -> personaRepo.findByTelefonoSecundario(telefonoSecundario))
                    .orElse(null);

            if (personaOpt != null &&
                    !personaOpt.getUser().getEstadoCuenta().equals(EstadoCuenta.ELIMINADO)) {
                // Lanza la exceptión si el telefono ya esta asociado a una cuenta existente
                throw new ElementoRepetidoException("Teléfono asociado a una cuenta eliminada");
            }
        }
    }

    // Busca una persona en la base de datos según su correo electrónico.
    @Override
    public Persona buscarPersonaPorEmail(String email) throws ElementoNoEncontradoException {
        return personaRepo.findByUser_Email(email)
                .orElseThrow(() -> new ElementoNoEncontradoException("Persona con el email asociado no encontrado"));
    }


    // Busca una persona en la base de datos según su ID.
    @Override
    public Persona buscarPersonaPorId(Long id) throws ElementoNoEncontradoException {
        return personaRepo.findById(id)
                .orElseThrow(() -> new ElementoNoEncontradoException("Persona con el email asociado no encontrado"));
    }

    // Guarda o actualiza una persona en la base de datos.
    @Override
    public void guardarPersonaBD(Persona persona) {
        personaRepo.save(persona);
    }
}
