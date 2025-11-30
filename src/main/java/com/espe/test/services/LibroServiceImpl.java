package com.espe.test.services;

import com.espe.test.models.entities.Libro;
import com.espe.test.repositories.LibroRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LibroServiceImpl implements LibroService {


    @Autowired
    private LibroRepository repository;


/* <<<<<<<<<<<<<<  ✨ Windsurf Command ⭐ >>>>>>>>>>>>>>>> */
    /**

/* <<<<<<<<<<  dba9fb5a-03ee-4a77-a3dc-864ed2810ad1  >>>>>>>>>>> */
    @Override
    @Transactional(readOnly = true)
    public List<Libro> buscarTodos() {
        return (List<Libro>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Libro> buscarPorID(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Libro guardar(Libro libro) {
        return repository.save(libro);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
