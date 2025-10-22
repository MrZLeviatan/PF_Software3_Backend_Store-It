package co.edu.uniquindio.constants;

import co.edu.uniquindio.exceptions.ElementoNoValidoException;
import co.edu.uniquindio.models.embeddable.UnidadAlmacenamiento;
import co.edu.uniquindio.models.entities.objects.almacen.EspacioProducto;
import co.edu.uniquindio.models.entities.objects.inventario.Lote;
import co.edu.uniquindio.models.entities.objects.inventario.Producto;
import co.edu.uniquindio.models.entities.users.Proveedor;
import co.edu.uniquindio.models.enums.embeddable.EstadoUnidad;
import co.edu.uniquindio.models.enums.entities.EstadoEspacioProducto;
import co.edu.uniquindio.models.enums.entities.EstadoLote;
import co.edu.uniquindio.models.enums.entities.TipoProducto;
import co.edu.uniquindio.repository.objects.almacen.EspacioProductoRepo;
import co.edu.uniquindio.repository.objects.inventario.LoteRepo;
import co.edu.uniquindio.repository.objects.inventario.ProductoRepo;
import co.edu.uniquindio.repository.users.GestorComercialRepo;
import co.edu.uniquindio.repository.users.ProveedorRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DatosInicialesRunner implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;

    private final ProductoRepo productoRepo;

    private final GestorComercialRepo gestorComercialRepo;

    private final ProveedorRepo proveedorRepo;

    private final EspacioProductoRepo espacioProductoRepo;

    private final LoteRepo loteRepo;



    @Override
    @Transactional
    public void run(String... args) throws Exception {


        // Plantulla para crear Gestor Comercial
/**
        GestorComercial gestorComercial = new GestorComercial();
        gestorComercial.setNombre("Juan Carlos");
        gestorComercial.setTelefono("+57 3294688782");

        User user = new User();
        user.setEmail("nikis281002@gmail.com");

        String password = "passwordGestor";

        String passwordCodificado = passwordEncoder.encode(password);

        user.setPassword(passwordCodificado);
        user.setTipoRegistro(TipoRegistro.TRADICIONAL);
        user.setEstadoCuenta(EstadoCuenta.ACTIVO);

        gestorComercial.setUser(user);

        DatosLaborales datosLaborales = new DatosLaborales();
        datosLaborales.setFechaInicioContrato(LocalDate.now());
        datosLaborales.setSueldo(100.00);

        gestorComercial.setDatosLaborales(datosLaborales);

        gestorComercialRepo.save(gestorComercial);

 **/

/**

        // Quemar datos producto
        Proveedor proveedor = new Proveedor();
        proveedor.setNombre("HomeCenter");
        proveedor.setEmail("homecenter@gmail.com");
        proveedor.setTelefono("+57 31536827834");
        proveedor.setMarca("HomeCenter");
        proveedorRepo.save(proveedor);

        // 🔹 Crear y guardar un producto (Product)
        // 🇪🇸 Crea un producto base para asociar al espacio.
        // 🇬🇧 Creates a base product to link with storage space.
        Producto p1 = new Producto();
        p1.setCodigoBarras("FFGHJ1");
        p1.setNombre("Nevera LG Smart");
        p1.setValorCompra(1902.0);
        p1.setImagen("https://res.cloudinary.com/dehltwwbu/image/upload/v1761074132/images_zstipv.jpg");
        p1.setTipoProducto(TipoProducto.ELECTRODOMESTICOS);
        p1.setProveedor(proveedor);
        productoRepo.save(p1);

        // 🔹 Crear una unidad de almacenamiento (Embedded Unit)
        // 🇪🇸 Define las dimensiones y estado de la unidad embebida.
        // 🇬🇧 Defines the storage unit dimensions and state.
        UnidadAlmacenamiento unidad = new UnidadAlmacenamiento();
        unidad.setLargo(3.0);
        unidad.setAncho(2.5);
        unidad.setAlto(2.0);
        unidad.setVolumenOcupado(0.0);
        unidad.setEstadoUnidad(EstadoUnidad.DISPONIBLE);

        // 🔹 Crear y asociar un espacio producto
        // 🇪🇸 Crea un espacio que contenga el producto con la unidad embebida.
        // 🇬🇧 Creates a space that holds the product with the embedded unit.
        EspacioProducto espacioProducto = new EspacioProducto();
        espacioProducto.setUnidadAlmacenamiento(unidad);
        espacioProducto.setProducto(p1);
        espacioProducto.setCantidadTotal(20);
        espacioProducto.setEstadoEspacio(EstadoEspacioProducto.ACTIVO);

        // Guardar el espacio producto
        espacioProductoRepo.save(espacioProducto);

**/

        EspacioProducto  espacioProducto = espacioProductoRepo.findById(1L).orElseThrow();

        Lote lote = new Lote();
        lote.setCodigoLote("FFGHJ1-A2");
        lote.setFechaIngreso(LocalDate.now());
        lote.setFechaVencimiento(LocalDate.now().plusMonths(12));
        lote.setEstadoLote(EstadoLote.ACTIVO);
        lote.setCantidadDisponible(10);
        lote.setAreaTotal(5.0); // debe ser menor al volumen disponible



        // Intentar agregar el lote al espacio
        try {
            espacioProducto.agregarLote(lote);
            loteRepo.save(lote);
            espacioProductoRepo.save(espacioProducto);
            System.out.println("✅ Lote agregado correctamente al espacio producto.");
        } catch (ElementoNoValidoException e) {
            System.err.println("❌ Error al agregar lote: " + e.getMessage());
        }

    }

}