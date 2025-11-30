package com.espe.test.controllers;

import com.espe.test.models.entities.Libro;
import com.espe.test.services.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

@RestController
public class LibroController {
    @Autowired
    private LibroService service;

    @GetMapping("/")
    public ResponseEntity<List<Libro>> listar(){
        return ResponseEntity.ok(service.buscarTodos());
    }

    @PostMapping("/")
    public ResponseEntity<?> crear(@RequestBody Libro libro){
        Libro libroDB=service.guardar(libro);
        return ResponseEntity.status(HttpStatus.CREATED).body(libroDB);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@RequestBody Libro libro, @PathVariable Long id){
        Optional<Libro> o = service.buscarPorID(id);
        if(o.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Libro libroDB = o.get();
        libroDB.setTitulo(libro.getTitulo());
        libroDB.setAutor(libro.getAutor());
        libroDB.setGenero(libro.getGenero());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(libroDB));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        Optional<Libro> o = service.buscarPorID(id);
        if(o.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
