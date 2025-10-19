package co.edu.uniquindio.service.utils;

import co.edu.uniquindio.exceptions.ElementoNoValidoException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

// Servicio utilitario para la gestión de imágenes en Cloudinary.
public interface CloudinaryService {

    // Sube una imagen a Cloudinary y retorna la URL pública de la imagen almacenada.
      String uploadImage(MultipartFile file) throws ElementoNoValidoException;

  
    // Elimina una imagen de Cloudinary a partir de su identificador único (public_id).
    Map eliminarImagen(String idImagen);
}
