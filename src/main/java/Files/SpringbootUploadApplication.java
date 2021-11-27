package Files;

import Files.util.FileProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * The type Springboot upload application.
 * @author 竑也
 */
@SpringBootApplication
@EnableConfigurationProperties({
        FileProperty.class
})
@EnableAsync
public class SpringbootUploadApplication {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(SpringbootUploadApplication.class, args);
    }
}
