package co.edu.uniquindio.models.embeddable;

import co.edu.uniquindio.models.enums.users.EstadoCuenta;
import co.edu.uniquindio.models.enums.embeddable.TipoRegistro;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable     // Esta clase será embebida en otras entidades, no se convierte en una tabla
public class User {

    @Email
    @Column(name = "email", nullable = false, unique = true) // Cada email debe ser único y no puede ser nulo
    @Comment("Dirección de correo electrónico única para autenticación.")
    private String email;

    @Column(name = "password", nullable = false)       // El password no puede ser nulo
    @Comment("Contraseña cifrada del usuario para autenticación.")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_cuenta", nullable = false)   // Estado de la cuenta en el proceso
    @Comment("Estado de la cuenta: ACTIVA, INACTIVA, ELIMINADA.")
    private EstadoCuenta estadoCuenta;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_registro") // Si se registra mediante Google o Tradicionalmente ( se podrá agregar mas métodos en el futuro )
    @Comment("Indica el tipo de registro del usuario.")
    private TipoRegistro tipoRegistro;

    @Embedded       // Código para la activación de 2FA en diferentes procesos
    private Codigo codigo;

}
