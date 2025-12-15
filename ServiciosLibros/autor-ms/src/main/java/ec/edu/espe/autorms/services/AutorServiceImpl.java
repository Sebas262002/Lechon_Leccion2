package ec.edu.espe.autorms.services;

import ec.edu.espe.autorms.models.entities.Autor;
import ec.edu.espe.autorms.repositories.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AutorServiceImpl implements AutorService {

    @Autowired
    private AutorRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Autor> findAll() {
        return (List<Autor>) repository.findAll();
    }

    @Override
    public Optional<Autor> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Autor save(Autor autor) {
        return repository.save(autor);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
