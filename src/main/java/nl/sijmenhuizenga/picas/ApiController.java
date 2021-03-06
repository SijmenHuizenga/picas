package nl.sijmenhuizenga.picas;

import nl.sijmenhuizenga.picas.entities.ImageGroup;
import nl.sijmenhuizenga.picas.entities.Picture;
import nl.sijmenhuizenga.picas.repos.PictureRepo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    private final PictureRepo pictures;

    public ApiController(PictureRepo pictures) {
        this.pictures = pictures;
    }

    @GetMapping("/days")
    public Iterable<ImageGroup> getDaysCount() {
        return pictures.countPerDay();
    }

    @GetMapping("/day/{date}")
    public Iterable<Picture> getPicturesOnDay(@PathVariable String date) {
        return pictures.findAllOnDate(date);
    }

}
