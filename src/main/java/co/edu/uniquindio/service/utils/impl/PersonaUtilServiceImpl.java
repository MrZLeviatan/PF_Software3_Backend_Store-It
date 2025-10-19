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

@Service
@RequiredArgsConstructor
public class PersonaUtilServiceImpl implements PersonaUtilService {

    private final PersonaRepo personaRepo;
    private final EmailService emailService;
    private final CodigoService codigoService;


    @Override
    public void validarEmailNoRepetido(String email) throws ElementoRepetidoException, ElementoEliminadoException {

        // Buscar el correo en el repositorio genérico de Persona.
        Optional<Persona> personaOpt = personaRepo.findByUser_Email(email);

        if (personaOpt.isPresent()) {
            Persona persona = personaOpt.get();

            EstadoCuenta estadoCuenta = persona.getUser().getEstadoCuenta();

            switch (estadoCuenta) {
                case ELIMINADO -> throw new ElementoEliminadoException("Email asociado a una cuenta eliminada");

                case ACTIVO -> throw new ElementoRepetidoException("Email asociado a una cuenta activada");

                case INACTIVA -> {
                    // Si la cuenta está inactiva, genera y reenvía un nuevo código de verificación.
                    String verificacion = UUID.randomUUID()
                            .toString()
                            .substring(0, 6)
                            .toUpperCase();

                    Codigo codigoVerificacion = codigoService.generarCodigoVerificacion2AF();

                    // Guarda el usuario actualizado con el nuevo código de verificación.
                    personaRepo.save(persona);

                    EmailDto emailDto = new EmailDto(
                            persona.getUser().getEmail(),
                            verificacion,
                            "Reverificación de Cuenta - Store-It"
                    );

                    emailService.enviarEmailVerificacionRegistro(emailDto);

                    throw new ElementoRepetidoException(
                            "La cuenta ya existe pero está inactiva. "
                                    + "Hemos enviado un nuevo código de verificación a tu correo."
                    );
                }}}
    }


    @Override
    public void validarTelefonoNoRepetido(String telefono, String telefonoSecundario)
            throws ElementoRepetidoException, ElementoNulosException, ElementoNoValidoException {

        // Validamos que el telefono no sea nulo
        if (telefono == null || telefono.isBlank()) {
            throw new ElementoNulosException("Teléfono no puede ser nulo o blank");
        }

        boolean existe;

        // Validamos la existencia del telefono principal
        existe = personaRepo.existsByTelefono(telefono);

        // Validamos la existencia del teléfono principal y secundario (Si existe)
        if (telefonoSecundario != null || !telefonoSecundario.isBlank()) {
            existe = personaRepo.existsByTelefonoOrTelefonoSecundario(telefono, telefonoSecundario);
        }

        if (existe) {
            Persona personaOpt = personaRepo.findByTelefono(telefono)
                    .or(() -> personaRepo.findByTelefonoSecundario(telefonoSecundario))
                    .orElse(null);

            if (personaOpt != null &&
                    !personaOpt.getUser().getEstadoCuenta().equals(EstadoCuenta.ELIMINADO)) {
                throw new ElementoRepetidoException("Teléfono asociado a una cuenta eliminada");
            }
        }
    }


    @Override
    public Persona buscarPersonaPorEmail(String email) throws ElementoNoEncontradoException {
        return personaRepo.findByUser_Email(email)
                .orElseThrow(() -> new ElementoNoEncontradoException("Persona con el email asociado no encontrado"));
    }


    @Override
    public void guardarPersonaBD(Persona persona) {
        personaRepo.save(persona);
    }
}
