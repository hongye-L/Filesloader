package Files;

import Files.util.FileProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableConfigurationProperties({
        FileProperty.class
})
@EnableAsync
public class SpringbootUploadApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootUploadApplication.class, args);
    }
}
