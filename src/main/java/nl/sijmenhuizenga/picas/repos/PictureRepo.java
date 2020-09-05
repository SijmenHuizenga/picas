package nl.sijmenhuizenga.picas.repos;

import nl.sijmenhuizenga.picas.entities.Picture;
import org.springframework.data.repository.CrudRepository;

public interface PictureRepo extends CrudRepository<Picture, Long> {

    Picture findById(long id);

}
