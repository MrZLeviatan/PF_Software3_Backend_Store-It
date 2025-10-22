package co.edu.uniquindio.service.users;

import co.edu.uniquindio.dto.users.proveedor.ProveedorDto;
import co.edu.uniquindio.dto.users.proveedor.RegistroProveedorDto;
import co.edu.uniquindio.exceptions.*;
import co.edu.uniquindio.models.entities.users.Proveedor;

import java.util.List;

public interface ProveedorService {


    // Método para que el Gestor Comercial pueda guardar los proveedores relacionados a los productos.
    ProveedorDto registrarProveedor(RegistroProveedorDto registroProveedorDto)
            throws ElementoNoValidoException, ElementoNulosException, ElementoRepetidoException, ElementoEliminadoException;

    Proveedor encontrarProveedor(Long idProveedor)
            throws ElementoNoEncontradoException;

    //Se busca el proveedor mediante su ID y se mapea a proveedorDto
    ProveedorDto encontrarProveedorDto(Long idProveedor)
            throws ElementoNoEncontradoException;

    // Se listan los proveedores con mapeo al proveedorDto
    List<ProveedorDto> listarProveedores();


}
