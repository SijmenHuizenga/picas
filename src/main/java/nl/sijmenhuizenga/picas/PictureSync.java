package nl.sijmenhuizenga.picas;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.bmp.BmpHeaderDirectory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.gif.GifHeaderDirectory;
import com.drew.metadata.jpeg.JpegDirectory;
import com.drew.metadata.png.PngDirectory;
import com.drew.metadata.webp.WebpDirectory;
import nl.sijmenhuizenga.picas.entities.ImageSize;
import nl.sijmenhuizenga.picas.entities.ImportError;
import nl.sijmenhuizenga.picas.entities.Picture;
import nl.sijmenhuizenga.picas.repos.ImportErrorRepo;
import nl.sijmenhuizenga.picas.repos.PictureRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Component
public class PictureSync {

    private Path storagedir;
    private PictureRepo pictures;
    private ImportErrorRepo errors;

    private List<String> supportedFilenames = Arrays.asList("png", "jpg", "bmp", "gif");

    public PictureSync(@Value("${mediadir}") Path storagedir, @Autowired PictureRepo pictures, @Autowired ImportErrorRepo errors) {
        this.storagedir = storagedir;
        this.pictures = pictures;
        this.errors = errors;
    }

    @Scheduled(fixedDelay = 3000) // 3 seconds
    public void syncPeriodically() {
        diskToDatabaseFullSync();

    }

    public void diskToDatabaseFullSync() {
        try {
            Files.walk(storagedir)
                    .filter(Files::isRegularFile)
                    .filter(f -> supportedFilenames.contains(getExtension(f.toString())))
                    .forEach(path -> {
                        if(isFilePreviouslyImported(relative(path))) {
                            return;
                        }
                        syncFileToDbFirstTime(path);
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void syncFileToDbRetry(ImportError previousError) {

    }

    private void syncFileToDbFirstTime(Path path) {
        try {
            pictures.save(fileToPicture(path));
        } catch (Exception e) {
            errors.save(new ImportError(relative(path), e));
        }
    }

    private Picture fileToPicture(Path path) throws IOException {
        Metadata metadata = getMetadata(path);
        Picture p = new Picture(relative(path));
        p.setSize(getImageSize(metadata, path));
        return p;
    }

    private boolean isFilePreviouslyImported(String relativePath) {
        return pictures.countAllByFilepath(relativePath) > 0 || errors.countAllByFilepathAndResolvedIsFalse(relativePath) > 0;
    }

    private Metadata getMetadata(Path path) throws IOException {
        try {
            return ImageMetadataReader.readMetadata(path.toFile());
        } catch (Exception e) {
            throw new IOException("Failed reading image metadata", e);
        }
    }

    private String relative(Path path) {
        return storagedir.relativize(path).toString();
    }

    private ImageSize getImageSize(Metadata metadata, Path path) throws IOException{
        ImageSize size = getImageSizeFromMetadata(metadata);
        if(size == null) {
            try {
                size = getImageSizeFromFile(path.toFile());
            } catch (Exception e) {
                throw new IOException("Could not determine image width and height", e);
            }
        }
        if(size.is0()) {
            throw new IOException("Image width and height 0");
        }
        return size;
    }

    private ImageSize getImageSizeFromMetadata(Metadata metadata) {
        ExifSubIFDDirectory exif = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        if(exif != null) {
            try {
                int width = exif.getInt(ExifSubIFDDirectory.TAG_IMAGE_WIDTH);
                int height = exif.getInt(ExifSubIFDDirectory.TAG_IMAGE_HEIGHT);
                return new ImageSize(width, height);
            } catch (MetadataException e) {
                // values not found not found
            }
        }
        JpegDirectory jpeg = metadata.getFirstDirectoryOfType(JpegDirectory.class);
        if(jpeg != null) {
            try {
                int width = jpeg.getInt(JpegDirectory.TAG_IMAGE_WIDTH);
                int height = jpeg.getInt(JpegDirectory.TAG_IMAGE_HEIGHT);
                return new ImageSize(width, height);
            } catch (MetadataException e) {
                // values not found not found
            }
        }
        PngDirectory png = metadata.getFirstDirectoryOfType(PngDirectory.class);
        if(png != null) {
            try {
                int width = png.getInt(PngDirectory.TAG_IMAGE_WIDTH);
                int height = png.getInt(PngDirectory.TAG_IMAGE_HEIGHT);
                return new ImageSize(width, height);
            } catch (MetadataException e) {
                // values not found not found
            }
        }
        BmpHeaderDirectory bmp = metadata.getFirstDirectoryOfType(BmpHeaderDirectory.class);
        if(bmp != null) {
            try {
                int width = bmp.getInt(BmpHeaderDirectory.TAG_IMAGE_WIDTH);
                int height = bmp.getInt(BmpHeaderDirectory.TAG_IMAGE_HEIGHT);
                return new ImageSize(width, height);
            } catch (MetadataException e) {
                // values not found not found
            }
        }
        GifHeaderDirectory gif = metadata.getFirstDirectoryOfType(GifHeaderDirectory.class);
        if(gif != null) {
            try {
                int width = gif.getInt(GifHeaderDirectory.TAG_IMAGE_WIDTH);
                int height = gif.getInt(GifHeaderDirectory.TAG_IMAGE_HEIGHT);
                return new ImageSize(width, height);
            } catch (MetadataException e) {
                // values not found not found
            }
        }
        WebpDirectory webp = metadata.getFirstDirectoryOfType(WebpDirectory.class);
        if(webp != null) {
            try {
                int width = webp.getInt(WebpDirectory.TAG_IMAGE_WIDTH);
                int height = webp.getInt(WebpDirectory.TAG_IMAGE_HEIGHT);
                return new ImageSize(width, height);
            } catch (MetadataException e) {
                // values not found not found
            }
        }
        return null;
    }

    // thanks to https://stackoverflow.com/a/12164026/2328729
    public static ImageSize getImageSizeFromFile(File imgFile) throws IOException {
        int pos = imgFile.getName().lastIndexOf(".");
        if (pos == -1)
            throw new IOException("No extension for file: " + imgFile.getAbsolutePath());
        String suffix = imgFile.getName().substring(pos + 1);
        Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);
        if(iter.hasNext()) {
            ImageReader reader = iter.next();
            try {
                ImageInputStream stream = new FileImageInputStream(imgFile);
                reader.setInput(stream);
                int width = reader.getWidth(reader.getMinIndex());
                int height = reader.getHeight(reader.getMinIndex());
                return new ImageSize(width, height);
            } catch (IOException e) {
                throw new IOException("Error reading: " + imgFile.getAbsolutePath(), e);
            } finally {
                reader.dispose();
            }
        }

        throw new IOException("Not a known image file: " + imgFile.getAbsolutePath());
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
