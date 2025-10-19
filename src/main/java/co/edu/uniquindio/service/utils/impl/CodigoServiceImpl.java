package co.edu.uniquindio.service.utils.impl;

import co.edu.uniquindio.dto.common.auth.VerificacionCodigoDto;
import co.edu.uniquindio.exceptions.ElementoNoCoincideException;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.models.embeddable.Codigo;
import co.edu.uniquindio.models.entities.users.Persona;
import co.edu.uniquindio.models.enums.embeddable.TipoCodigo;
import co.edu.uniquindio.repository.users.PersonaRepo;
import co.edu.uniquindio.service.utils.CodigoService;
import co.edu.uniquindio.service.utils.PersonaUtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CodigoServiceImpl implements CodigoService {


    private final PersonaRepo personaRepo;

    @Override
    public Codigo generarCodigoVerificacion2AF() {
        Codigo codigoVerificacion = new Codigo();
        codigoVerificacion.setClave(generacionClave());
        codigoVerificacion.setTipoCodigo(TipoCodigo.VERIFICACION_2FA);
        codigoVerificacion.setFechaExpiracion(LocalDateTime.now().plusMinutes(15));
        return codigoVerificacion;
    }


    @Override
    public Codigo generarCodigoRestablecerPassword() {
        Codigo codigoRestablecerPassword = new Codigo();
        codigoRestablecerPassword.setClave(generacionClave());
        codigoRestablecerPassword.setTipoCodigo(TipoCodigo.RESTABLECER_PASSWORD);
        codigoRestablecerPassword.setFechaExpiracion(LocalDateTime.now().plusMinutes(15));
        return codigoRestablecerPassword;
    }



    @Override
    public void autentificarCodigo(VerificacionCodigoDto verificacionCodigoDto)
            throws ElementoNoEncontradoException, ElementoNoCoincideException {

        Persona personaOpt = buscarPersonaPorEmail(verificacionCodigoDto.email());


        // Verificar fecha de expiración
        if (personaOpt.getUser().getCodigo().getFechaExpiracion().isBefore(LocalDateTime.now())) {
            throw new ElementoNoCoincideException("El código proporcionado ha expirado.");
        }

        // Verificar coincidencia del código
        if (!personaOpt.getUser().getCodigo().getClave().equals(verificacionCodigoDto.codigo())) {
            throw new ElementoNoCoincideException("El código proporcionado no coincide");
        }

        // Limpiar el código usado y guardar cambios
        personaOpt.getUser().setCodigo(null);
        personaRepo.save(personaOpt);
    }


    public Persona buscarPersonaPorEmail(String email) throws ElementoNoEncontradoException {
        return personaRepo.findByUser_Email(email)
                .orElseThrow(() -> new ElementoNoEncontradoException("Persona con el email asociado no encontrado"));
    }


    private String generacionClave() {
        return UUID.randomUUID().toString()
                .replace("-", "")  // Eliminamos guiones para mayor limpieza
                .substring(0, 6)   // Reducimos a 6 caracteres
                .toUpperCase();
    }
}
