package ec.edu.espe.autorms.services;

import ec.edu.espe.autorms.models.entities.Autor;

import java.util.List;
import java.util.Optional;

public interface AutorService {
    List<Autor> findAll();
    Optional<Autor> findById(Long id);
    Autor save(Autor autor);
    void delete(Long id);
}
