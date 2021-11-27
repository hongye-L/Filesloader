package Files.Controller;

import Files.Model.Files;
import Files.Service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The type File controller.
 */
@RestController
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;

    /**
     * Upload file files.
     *
     * @param file the file
     * @return the files
     */
    @PostMapping("/uploadFile")
    public Files uploadFile(@RequestParam("file") MultipartFile file){
        String fileName = fileService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        return new Files(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    /**
     * Upload multiple files list.
     *
     * @param files the files
     * @return the list
     */
    @PostMapping("/uploadMultipleFiles")
    public List<Files> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        List<Files> list = new ArrayList<>();
        if (files != null) {
            for (MultipartFile multipartFile:files) {
                Files uploadFileResponse = uploadFile(multipartFile);
                list.add(uploadFileResponse);
            }
        }
        return list;
    }

    /**
     * Download file response entity.
     *
     * @param fileName the file name
     * @param request  the request
     * @return the response entity
     */
    @GetMapping("/downloadFile/{fileName:.*}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = fileService.loadFileAsResource(fileName);
        String contentType = null;
        try {
            request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            logger.info("Could not determine file type.");
        }
        if(contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
