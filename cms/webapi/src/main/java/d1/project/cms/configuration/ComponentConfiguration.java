package d1.project.cms.configuration;

import net.dcrun.component.file.server.FileServerService;
import net.dcrun.component.file.server.IFileServerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ComponentConfiguration {
    @Bean
    IFileServerService fileServerService() {
        return new FileServerService();
    }
}
