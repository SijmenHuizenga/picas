package nl.sijmenhuizenga.picas.repos;

import nl.sijmenhuizenga.picas.entities.ImportError;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ImportErrorRepo extends CrudRepository<ImportError, Long> {

    int countAllByFilepathAndResolvedIsFalse(String filepath);
}
