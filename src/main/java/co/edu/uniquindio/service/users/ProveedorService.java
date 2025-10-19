package co.edu.uniquindio.service.users;

import co.edu.uniquindio.dto.users.proveedor.ProveedorDto;
import co.edu.uniquindio.dto.users.proveedor.RegistroProveedorDto;
import co.edu.uniquindio.exceptions.*;
import co.edu.uniquindio.models.entities.users.Proveedor;

import java.util.List;

public interface ProveedorService {


    void registrarProveedor(RegistroProveedorDto registroProveedorDto)
            throws ElementoNoValidoException, ElementoNulosException, ElementoRepetidoException, ElementoEliminadoException;

    Proveedor encontrarProveedor(Long idProveedor)
            throws ElementoNoEncontradoException;

    ProveedorDto encontrarProveedorDto(Long idProveedor)
            throws ElementoNoEncontradoException;

    List<ProveedorDto> listarProveedores();



}
