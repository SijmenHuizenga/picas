package nl.sijmenhuizenga.picas.repos;

import nl.sijmenhuizenga.picas.entities.ImageGroup;
import nl.sijmenhuizenga.picas.entities.Picture;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PictureRepo extends CrudRepository<Picture, Long> {

    Picture findById(long id);

    int countAllByFilepath(String filepath);

    @Query(value = "SELECT date(to_timestamp(creation_timestamp/1000)) as date, COUNT(*) as count from picture group by date order by date", nativeQuery = true)
    List<ImageGroup> countPerDay();

    @Query(value = "SELECT * from picture where date(to_timestamp(creation_timestamp/1000)) = date(:searchdate)", nativeQuery = true)
    List<Picture> findAllOnDate(String searchdate);
}
