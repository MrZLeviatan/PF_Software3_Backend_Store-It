package co.edu.uniquindio.models.embeddable;

import co.edu.uniquindio.models.enums.embeddable.TipoCodigo;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable // Esta clase será embebida en otras entidades, no se convierte en una tabla
public class Codigo {

    @Column(name = "clave") // Clave para la autentificación 2FA en los diferentes contextos (puede ser borrado con el tiempo o uso)
    private String clave;

    @Column(name = "fecha_restablecimiento_expiracion") // Fecha de expiración del código
    private LocalDateTime fechaExpiracion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_codigo")   // Tipo de contexto del código
    private TipoCodigo tipoCodigo;

}
