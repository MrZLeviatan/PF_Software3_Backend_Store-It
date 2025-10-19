package co.edu.uniquindio.service.users.impl;

import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.models.entities.users.GestorComercial;
import co.edu.uniquindio.repository.users.GestorComercialRepo;
import co.edu.uniquindio.service.users.GestorComercialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GestorComercialServiceImpl implements GestorComercialService {

    private final GestorComercialRepo gestorComercialRepo;


    @Override
    public GestorComercial obtenerGestorComercialId(Long id) throws ElementoNoEncontradoException {
        return gestorComercialRepo.findById(id)
                .orElseThrow(() -> new ElementoNoEncontradoException("Gestor comercial no encontrado"));
    }
}
