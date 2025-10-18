package co.edu.uniquindio.dto.users.cliente;

import co.edu.uniquindio.dto.common.UbicacionDto;
import co.edu.uniquindio.dto.objects.compras.carritoCompra.CarritoCompraDto;
import co.edu.uniquindio.dto.objects.compras.compra.CompraDto;
import co.edu.uniquindio.dto.objects.compras.facturas.FacturaDto;
import co.edu.uniquindio.dto.users.PersonaDto;
import co.edu.uniquindio.models.enums.users.TipoCliente;

import java.util.List;

public record ClienteDto(


        PersonaDto persona,
        // Tipo de cliente, ya sea NATURAL o JURIDICO.
        TipoCliente tipoCliente,
        // Número de identificación tributaria, relevante para clientes jurídicos.
        String nit,
        // Objeto DTO que contiene la información de la ubicación del cliente.
        UbicacionDto ubicacion,
        // Lista de facturasDto relacionadas al Cliente
        List<FacturaDto> facturas,
        // Compras realizadas por el Cliente
        List<CompraDto> compras,
        // Contiene el carrito compra principal del Cliente
        CarritoCompraDto carritoCompra

) {
}
