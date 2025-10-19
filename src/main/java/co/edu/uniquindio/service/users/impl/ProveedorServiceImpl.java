package co.edu.uniquindio.service.users.impl;

import co.edu.uniquindio.dto.users.proveedor.ProveedorDto;
import co.edu.uniquindio.dto.users.proveedor.RegistroProveedorDto;
import co.edu.uniquindio.exceptions.*;
import co.edu.uniquindio.mapper.users.ProveedorMapper;
import co.edu.uniquindio.models.entities.users.Proveedor;
import co.edu.uniquindio.repository.users.ProveedorRepo;
import co.edu.uniquindio.service.users.ProveedorService;
import co.edu.uniquindio.service.utils.PersonaUtilService;
import co.edu.uniquindio.service.utils.PhoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProveedorServiceImpl implements ProveedorService {

    // Servicios Propios
    private final PersonaUtilService personaUtilService;
    private final PhoneService phoneService;

    private final ProveedorRepo proveedorRepo;
    private final ProveedorMapper proveedorMapper;


    @Override
    public void registrarProveedor(RegistroProveedorDto registroProveedorDto)
            throws ElementoNoValidoException, ElementoNulosException, ElementoRepetidoException, ElementoEliminadoException {

        // Formateamos el teléfono principal ( COD + Num /)
        String telefonoFormateado = phoneService.obtenerTelefonoFormateado(
                registroProveedorDto.telefono(),registroProveedorDto.codigoPais());

        // Validamos los correos y teléfonos no sean repetidos
        personaUtilService.validarEmailNoRepetido(registroProveedorDto.email());
        personaUtilService.validarTelefonoNoRepetido(telefonoFormateado,null);

        // Se mapea el Proveedor
        Proveedor proveedor = proveedorMapper.toEntity(registroProveedorDto);

        // Se guarda en la base
        proveedorRepo.save(proveedor);
    }


    @Override
    public Proveedor encontrarProveedor(Long idProveedor)
            throws ElementoNoEncontradoException {
        return proveedorRepo.findById(idProveedor)
                .orElseThrow(() ->
                        new ElementoNoEncontradoException("Proveedor no encontrado"));
    }


    @Override
    public ProveedorDto encontrarProveedorDto(Long idProveedor)
            throws ElementoNoEncontradoException {
        return proveedorMapper.toDto(encontrarProveedor(idProveedor));
    }


    @Override
    public List<ProveedorDto> listarProveedores() {
        return proveedorRepo.findAll()
                .stream()
                .map(proveedorMapper::toDto)
                .toList();
    }
}
