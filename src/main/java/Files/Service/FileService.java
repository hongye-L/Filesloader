package Files.Service;

import Files.Errors.FileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.Files;
import java.util.concurrent.CompletableFuture;

/**
 * The type File service.
 */
@Service
@Component
public class FileService {
    private Path fileStorageLocation; // 文件在本地存储的地址

    /**
     * Instantiates a new File service.
     *
     * @param path the path
     */
    public FileService(@Value("${file.upload.path}") String path) {
        this.fileStorageLocation = Paths.get(path).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            throw new FileException("Could not create the directory", e);
        }
    }

    /**
     * 存储文件到系统
     *
     * @param file 文件
     * @return 文件名 string
     */
//现代化改造：多线程
    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    /**
     * Load file as resource resource.
     *
     * @param fileName the file name
     * @return the resource
     */
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new FileException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileException("File not found " + fileName, ex);
        }
    }
}
