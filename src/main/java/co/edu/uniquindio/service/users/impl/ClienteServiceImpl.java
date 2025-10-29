package co.edu.uniquindio.service.users.impl;

import co.edu.uniquindio.dto.common.auth.VerificacionCodigoDto;
import co.edu.uniquindio.dto.common.email.EmailDto;
import co.edu.uniquindio.dto.common.google.GoogleUserResponse;
import co.edu.uniquindio.dto.users.cliente.RegistroClienteDto;
import co.edu.uniquindio.dto.users.cliente.RegistroClienteGoogleDto;
import co.edu.uniquindio.exceptions.*;
import co.edu.uniquindio.mapper.users.ClienteMapper;
import co.edu.uniquindio.models.embeddable.Codigo;
import co.edu.uniquindio.models.entities.objects.compra.CarritoCompra;
import co.edu.uniquindio.models.entities.users.Cliente;
import co.edu.uniquindio.models.enums.users.EstadoCuenta;
import co.edu.uniquindio.models.enums.users.TipoCliente;
import co.edu.uniquindio.repository.objects.compra.CarritoCompraRepo;
import co.edu.uniquindio.repository.users.ClienteRepo;
import co.edu.uniquindio.service.objects.compra.CarritoCompraService;
import co.edu.uniquindio.service.users.ClienteService;
import co.edu.uniquindio.service.utils.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Servicio que maneja todas las operaciones relacionadas con el registro, verificación
 * y autenticación de clientes dentro del sistema Store-It.
 */
@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    // Servicios Externos
    private final PasswordEncoder passwordEncoder;
    private final GoogleUtilsService googleUtilsService;

    // Servicios Propios
    private final PhoneService phoneService;
    private final PersonaUtilService personaUtilService;
    private final CodigoService codigoService;
    private final EmailService emailService;
    private final CarritoCompraService carritoCompraService;
    private final CarritoCompraRepo carritoCompraRepo;

    // Mapper y Repo
    private final ClienteMapper clienteMapper;
    private final ClienteRepo clienteRepo;


    // Registro de cliente tradicional
    @Override
    public void registrarCliente(RegistroClienteDto registroClienteDto)
            throws ElementoNoValidoException, ElementoNulosException, ElementoRepetidoException, ElementoEliminadoException {

        // Formateamos el teléfono principal ( COD + Num /)
        String telefonoFormateado = phoneService.obtenerTelefonoFormateado(
                registroClienteDto.telefono(),registroClienteDto.codigoPais());

        // Formateamos el teléfono secundario si se encuentra
        String telefonoSecundarioFormateado = null;
        if (registroClienteDto.telefonoSecundario() != null && !registroClienteDto.telefonoSecundario().isEmpty()) {
            telefonoSecundarioFormateado = phoneService.obtenerTelefonoFormateado(
                    registroClienteDto.telefonoSecundario(), registroClienteDto.codigoPaisSecundario());
        }

        // Validamos los correos y teléfonos no sean repetidos
        personaUtilService.validarEmailNoRepetido(registroClienteDto.user().email());
        personaUtilService.validarTelefonoNoRepetido(telefonoFormateado,telefonoSecundarioFormateado);

        // Encriptamos el password usando Enconder
        String passwordEncriptada = passwordEncoder.encode(registroClienteDto.user().password());

        // Se mapea el Cliente
        Cliente cliente = clienteMapper.toEntity(registroClienteDto);

        // Validamos si es jurídico o Natural
        if (cliente.getTipoCliente().equals(TipoCliente.JURIDICO) &&
                (cliente.getNit().isEmpty() || cliente.getNit().isBlank())) {
            throw new ElementoNulosException("Faltan datos obligatorios para completar el registro de la persona.");
        }

        // Se agrega el/los telefono/s formateados
        cliente.setTelefono(telefonoFormateado);
        if (telefonoSecundarioFormateado != null) {
            cliente.setTelefonoSecundario(telefonoSecundarioFormateado);
        }

        // Se agrega el password encriptado
        cliente.getUser().setPassword(passwordEncriptada);

        // Se envia el codigo de verificación al correo
        Codigo codigoVerificacion = codigoService.generarCodigoVerificacion2AF();
        cliente.getUser().setCodigo(codigoVerificacion);

        EmailDto emailDto = new EmailDto(
                cliente.getUser().getEmail(),
                codigoVerificacion.getClave(),
                "Bienvenido a Store-It - Gestión de Bodegas");
        emailService.enviarEmailVerificacionRegistro(emailDto);

        // Se guarda el cliente
        clienteRepo.save(cliente);
    }


    // Validador de Token Google
    @Override
    public GoogleUserResponse validarToken(String token)
            throws ElementoIncorrectoException {

        GoogleIdToken.Payload payload = googleUtilsService.verifyIdToken(token);

        if (payload == null) {
            throw new ElementoIncorrectoException("Token inválido o expirado");
        }

        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String picture = (String) payload.get("picture");

        return new GoogleUserResponse(email, name, picture);
    }



    @Override
    public void registroClienteGoogle(RegistroClienteGoogleDto registroClienteGoogleDto)
            throws ElementoNoValidoException, ElementoNulosException, ElementoRepetidoException, ElementoEliminadoException {

        // Formateamos el teléfono principal ( COD + Num /)
        String telefonoFormateado = phoneService.obtenerTelefonoFormateado(
                registroClienteGoogleDto.telefono(),registroClienteGoogleDto.codigoPais());

        // Formateamos el teléfono secundario si se encuentra
        String telefonoSecundarioFormateado = null;
        if (registroClienteGoogleDto.telefonoSecundario() != null && !registroClienteGoogleDto.telefonoSecundario().isEmpty()) {
            telefonoSecundarioFormateado = phoneService.obtenerTelefonoFormateado(
                    registroClienteGoogleDto.telefonoSecundario(), registroClienteGoogleDto.codigoPaisSecundario());
        }

        // Validamos los correos y teléfonos no sean repetidos
        personaUtilService.validarEmailNoRepetido(registroClienteGoogleDto.email());
        personaUtilService.validarTelefonoNoRepetido(telefonoFormateado,telefonoSecundarioFormateado);

        // Se mapea el Cliente registrado mediante Google
        Cliente cliente = clienteMapper.toEntityGoogle(registroClienteGoogleDto);

        // Se agrega el/los telefono/s formateados
        cliente.setTelefono(telefonoFormateado);
        if (telefonoSecundarioFormateado != null) {
            cliente.setTelefonoSecundario(telefonoSecundarioFormateado);
        }

        // Se agrega el password encriptado
        cliente.getUser().setPassword(passwordEncoder.encode(registroClienteGoogleDto.password()));

        // Se envia la notificación al Correo del Registro y Verificación Exitoso
        EmailDto emailDto = new EmailDto(
                cliente.getUser().getEmail(),
                null,
                "Bienvenido a Store-It - Gestión de Bodegas");
        emailService.enviarEmailRegistroGoogle(emailDto);

        clienteRepo.save(cliente);
    }


    @Override
    public void verificacionCliente(VerificacionCodigoDto verificacionCodigoDto)
            throws ElementoNoEncontradoException, ElementoIncorrectoException, ElementoNoCoincideException {

        // Se obtiene el cliente mediante el email
        Cliente cliente = obtenerClientePorEmail(verificacionCodigoDto.email());

        // Verifica el estado del user del cliente
        if (cliente.getUser().getEstadoCuenta().equals(EstadoCuenta.ACTIVO)) {
            throw new ElementoIncorrectoException("La cuenta ya fue activada previamente.");
        }

        // Si el Código expiro se envia uno nuevamente al Cliente
        if (cliente.getUser().getCodigo().getFechaExpiracion().isBefore(LocalDateTime.now())) {
            Codigo codigoVerificacion = codigoService.generarCodigoVerificacion2AF();
            cliente.getUser().setCodigo(codigoVerificacion);
            clienteRepo.save(cliente);

            EmailDto emailDto = new EmailDto(
                    cliente.getUser().getEmail(),
                    codigoVerificacion.getClave(),
                    "Reverificación de Cuenta - Store-It");
            emailService.enviarEmailVerificacionRegistro(emailDto);

            throw new ElementoNoCoincideException("El código proporcionado ha expirado.");
        }

        // Verifica el codigo si coincide
        if (!cliente.getUser().getCodigo().getClave().equals(verificacionCodigoDto.codigo())) {
            throw new ElementoNoCoincideException("El código proporcionado no es válido.");
        }

        // Se genera un carrito de compra y se alistan
        CarritoCompra carritoCompra = carritoCompraService.generarCarritoCliente(cliente);
        cliente.setCarritoCompra(carritoCompra);

        // Se cambia el estado de la cuenta del Cliente
        cliente.getUser().setEstadoCuenta(EstadoCuenta.ACTIVO);
        cliente.getUser().setCodigo(null);

        // Se guarda primero el carrito, luego el cliente (para evitar detached entities)
        carritoCompraRepo.save(carritoCompra);
        clienteRepo.save(cliente);
    }


    @Override
    public void verificacionClienteLink(String email, String codigo)
            throws ElementoNoEncontradoException, ElementoIncorrectoException, ElementoNoCoincideException {

        // Se obtiene el cliente mediante el email
        Cliente cliente = obtenerClientePorEmail(email);

        // Verifica el estado del user del cliente
        if (cliente.getUser().getEstadoCuenta().equals(EstadoCuenta.ACTIVO)) {
            throw new ElementoIncorrectoException("La cuenta ya fue activada previamente.");
        }

        // Si el Código expiro se envia uno nuevamente al Cliente
        if (cliente.getUser().getCodigo().getFechaExpiracion().isBefore(LocalDateTime.now())) {
            Codigo codigoVerificacion = codigoService.generarCodigoVerificacion2AF();
            cliente.getUser().setCodigo(codigoVerificacion);
            clienteRepo.save(cliente);

            EmailDto emailDto = new EmailDto(
                    cliente.getUser().getEmail(),
                    codigoVerificacion.getClave(),
                    "Reverificación de Cuenta - Store-It");
            emailService.enviarEmailVerificacionRegistro(emailDto);

            throw new ElementoNoCoincideException("El código proporcionado ha expirado.");
        }

        // Verifica el codigo si coincide
        if (!cliente.getUser().getCodigo().getClave().equals(codigo)) {
            throw new ElementoNoCoincideException("El código proporcionado no es válido.");
        }

        // Se genera un carrito de compra y se alistan
        CarritoCompra carritoCompra = carritoCompraService.generarCarritoCliente(cliente);
        cliente.setCarritoCompra(carritoCompra);

        // Se cambia el estado de la cuenta del Cliente
        cliente.getUser().setEstadoCuenta(EstadoCuenta.ACTIVO);
        cliente.getUser().setCodigo(null);

        // Se guarda primero el carrito, luego el cliente (para evitar detached entities)
        carritoCompraRepo.save(carritoCompra);
        clienteRepo.save(cliente);
    }


    @Override
    public Cliente obtenerClienteId(Long id) throws ElementoNoEncontradoException {
        return clienteRepo.findById(id)
                .orElseThrow(() ->
                        new ElementoNoEncontradoException("Cliente no encontrado"));
    }


    // Obtener un cliente desde el email.
    private Cliente obtenerClientePorEmail(String email)
            throws ElementoNoEncontradoException {
        return clienteRepo.findByUser_Email(email)
                .orElseThrow(() ->
                        new ElementoNoEncontradoException("Cliente con el email "+ email +" no encontrado"));
    }



}
