package ec.edu.espe.autorms.controllers;

import ec.edu.espe.autorms.models.entities.Autor;
import ec.edu.espe.autorms.services.AutorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/autores")
public class AutorController {

    @Autowired
    private AutorService service;

    @GetMapping
    public ResponseEntity<List<Autor>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<Autor> create(@Valid @RequestBody Autor autor) {
        Autor savedAutor = service.save(autor);
        return ResponseEntity.status(201).body(savedAutor);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Autor> autorOptional = service.findById(id);
        if (autorOptional.isPresent()) {
            return ResponseEntity.ok(autorOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Autor> update(@PathVariable Long id,@Valid @RequestBody Autor autor) {
        Optional<Autor> existingAutor = service.findById(id);
        if (existingAutor.isPresent()) {
            Autor autorToUpdate = existingAutor.get();
            autorToUpdate.setNombre(autor.getNombre());
            autorToUpdate.setNacionalidad(autor.getNacionalidad());
            autorToUpdate.setFechaNacimiento(autor.getFechaNacimiento());
            autorToUpdate.setStatus(autor.getStatus());
            Autor updatedAutor = service.save(autorToUpdate);
            return ResponseEntity.ok(updatedAutor);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Autor> libroOptional = service.findById(id);
        if (libroOptional.isPresent()) {
            service.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
