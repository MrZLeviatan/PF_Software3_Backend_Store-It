package co.edu.uniquindio.constants;

import co.edu.uniquindio.models.entities.objects.inventario.Producto;
import co.edu.uniquindio.models.entities.users.Proveedor;
import co.edu.uniquindio.models.enums.entities.TipoProducto;
import co.edu.uniquindio.repository.objects.inventario.ProductoRepo;
import co.edu.uniquindio.repository.users.ProveedorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatosInicialesRunner implements CommandLineRunner {

    @Autowired
    private ProductoRepo productoRepo;

    @Autowired
    private ProveedorRepo proveedorRepo;


    @Override
    public void run(String... args) throws Exception {

        /**

        Proveedor proveedor = new Proveedor();
        proveedor.setNombre("Walmark");
        proveedor.setEmail("walmark@gmail.com");
        proveedor.setTelefono("+57 3144688781");
        proveedor.setMarca("Lenovo");

        proveedorRepo.save(proveedor);


        Producto p1 = new Producto();
        p1.setCodigoBarras("CD901");
        p1.setNombre("Computadora");
        p1.setValorCompra(1000.0);
        p1.setImagen("https://res.cloudinary.com/dehltwwbu/image/upload/v1757285219/Store-IT/ImagenesProductos/qquys2uo7bqw67yduhde.png");
        p1.setTipoProducto(TipoProducto.ELECTRODOMESTICOS);
        p1.setProveedor(proveedor);


        productoRepo.save(p1);

    **/

    }

}