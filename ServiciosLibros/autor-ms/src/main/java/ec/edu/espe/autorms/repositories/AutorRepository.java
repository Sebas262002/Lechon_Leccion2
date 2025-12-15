package ec.edu.espe.autorms.repositories;

import ec.edu.espe.autorms.models.entities.Autor;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

@Transactional
public interface AutorRepository extends CrudRepository<Autor, Long> {

}
