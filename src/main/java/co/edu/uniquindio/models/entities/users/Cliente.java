package co.edu.uniquindio.models.entities.users;

import co.edu.uniquindio.models.embeddable.Ubicacion;
import co.edu.uniquindio.models.entities.objects.compra.CarritoCompra;
import co.edu.uniquindio.models.entities.objects.compra.Compra;
import co.edu.uniquindio.models.entities.objects.compra.Factura;
import co.edu.uniquindio.models.entities.objects.notificaciones.Notificacion;
import co.edu.uniquindio.models.enums.users.TipoCliente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "clientes")
@DiscriminatorValue("CLIENTE")
@Comment("Entidad que representa a un cliente que firma contratos con la empresa.")
public class Cliente extends Persona{


    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cliente", nullable = false)    // Se define que tipo de cliente es (para futuras opciones, de envíos o pagos)
    @Comment("Tipo Cliente que contrata el servicio. Natural o Jurídica.")
    private TipoCliente tipoCliente;

    @Column(name = "NIT_empresa")   // Si es un cliente jurídico, se necesita el Nit ( para nada xd )
    @Comment("NIT del cliente si es Juridico.")
    private String nit;

    @Embedded   // Ubicación del cliente, para futuros procesos de envíos
    private Ubicacion ubicacion;

    @OneToMany(mappedBy = "cliente")
    @Comment("Facturas asociadas al Cliente")
    private List<Factura> facturas;

    @OneToMany(mappedBy = "cliente")
    @Comment("Compras asociadas al Cliente")
    private List<Compra> compras;

    @OneToOne(mappedBy = "cliente")
    @Comment("Carrito de Compra único del Cliente")
    private CarritoCompra carritoCompra;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    @Comment("Lista de notificaciones asociadas al cliente.")
    private List<Notificacion> notificaciones;



}
