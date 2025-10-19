package co.edu.uniquindio.service.utils;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

//Servicio utilitario para la integración con Google Identity.
public interface GoogleUtilsService {

    ///Verifica un ID Token de Google y retorna su información decodificada (payload).
    GoogleIdToken.Payload verifyIdToken(String idTokenString);

}

