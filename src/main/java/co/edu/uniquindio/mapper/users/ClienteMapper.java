package co.edu.uniquindio.mapper.users;

import co.edu.uniquindio.dto.users.cliente.ClienteDto;
import co.edu.uniquindio.dto.users.cliente.RegistroClienteDto;
import co.edu.uniquindio.dto.users.cliente.RegistroClienteGoogleDto;
import co.edu.uniquindio.mapper.objects.compra.CarritoCompraMapper;
import co.edu.uniquindio.mapper.objects.compra.CompraMapper;
import co.edu.uniquindio.mapper.objects.compra.FacturaMapper;
import co.edu.uniquindio.models.entities.users.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {
        PersonaMapper.class,
                FacturaMapper.class,
                CompraMapper.class,
                CarritoCompraMapper.class
        })
public interface ClienteMapper {


    @Mapping(source = ".", target = "persona")
    ClienteDto toDto(Cliente cliente);

    // Convierte un DTO (CrearClienteDto) a una entidad (Cliente) para un registro normal.
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ubicacion", source = "ubicacion")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "user.estadoCuenta", constant = "INACTIVA")
    @Mapping(target = "user.codigo", ignore = true)
    @Mapping(target = "user.tipoRegistro", constant = "TRADICIONAL")
    @Mapping(target = "notificaciones", expression = "java(new java.util.ArrayList<>())")
    @Mapping(target = "facturas", expression = "java(new java.util.ArrayList<>())")
    @Mapping(target = "compras", expression = "java(new java.util.ArrayList<>())")
    @Mapping(target = "carritoCompra", ignore = true)
    Cliente toEntity(RegistroClienteDto registroClienteDto);

    // Convierte un DTO (CrearClienteGoogleDto) a una entidad (Cliente) para un registro con Google.
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nit", ignore = true)
    @Mapping(target = "ubicacion", source = "ubicacion")
    @Mapping(target = "tipoCliente", constant = "NATURAL")
    @Mapping(target = "user.email", source = "email")
    @Mapping(target = "user.password", source = "password")
    @Mapping(target = "user.estadoCuenta", constant = "ACTIVO")
    @Mapping(target = "user.codigo", ignore = true)
    @Mapping(target = "user.tipoRegistro", constant = "GOOGLE")
    @Mapping(target = "notificaciones", expression = "java(new java.util.ArrayList<>())")
    @Mapping(target = "facturas", expression = "java(new java.util.ArrayList<>())")
    @Mapping(target = "compras", expression = "java(new java.util.ArrayList<>())")
    @Mapping(target = "carritoCompra", ignore = true)
    Cliente toEntityGoogle(RegistroClienteGoogleDto registroClienteGoogleDto);



}
