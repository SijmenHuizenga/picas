package nl.sijmenhuizenga.picas;

import nl.sijmenhuizenga.picas.entities.Picture;
import nl.sijmenhuizenga.picas.repos.PictureRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Component
public class PictureSync {

    private Path storagedir;
    private PictureRepo pictures;

    private List<String> supportedFilenames = Arrays.asList("png", "jpg", "bmp", "gif");

    public PictureSync(@Value("${mediadir}") Path storagedir, @Autowired PictureRepo pictures) {
        this.storagedir = storagedir;
        this.pictures = pictures;
    }

    @Scheduled(fixedDelay = 3000) // 3 seconds
    public void syncPeriodically() {
        diskToDatabaseFullSync();

    }

    public void diskToDatabaseFullSync() {
        try {
            Files.walk(storagedir)
                    .filter(Files::isRegularFile)
                    // todo: filter based on file header instead of extension
                    .filter(f -> supportedFilenames.contains(getExtension(f.toString())))
                    .map(f -> storagedir.relativize(f))
                    .map(Path::toString)
                    .forEach(this::syncFileToDb);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void syncFileToDb(String path) {
        if(pictures.countAllByFilepath(path) > 0) {
            return;
        }
        Picture p = new Picture(path);
        // todo: insert metadata
        pictures.save(p);
    }

    // Thanks to https://stackoverflow.com/a/36283185/2328729
    public static String getExtension(String fileName) {
        char ch;
        int len;
        if(fileName==null ||
                (len = fileName.length())==0 ||
                (ch = fileName.charAt(len-1))=='/' || ch=='\\' || //in the case of a directory
                ch=='.' ) //in the case of . or ..
            return "";
        int dotInd = fileName.lastIndexOf('.'),
                sepInd = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
        if( dotInd<=sepInd )
            return "";
        else
            return fileName.substring(dotInd+1).toLowerCase();
    }

}
