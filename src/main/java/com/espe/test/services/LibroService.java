package com.espe.test.services;

import com.espe.test.models.entities.Libro;

import java.util.List;
import java.util.Optional;

public interface LibroService {


    List<Libro> buscarTodos();

    Optional<Libro> buscarPorID(Long id);
    Libro guardar(Libro libro);
    void eliminar(Long id);
}
