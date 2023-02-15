package d1.project.cms.configuration;

import net.dcrun.component.file.server.FileServerService;
import net.dcrun.component.file.util.FileUtilService;
import net.dcrun.component.osinfo.OsInfoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author baozh
 */
@Configuration
public class DcRunConfiguration {
    @Bean
    OsInfoService osInfoService(){
        return new OsInfoService();
    }

    @Bean
    FileUtilService fileUtilService(){
        return new FileUtilService();
    }

    @Bean
    FileServerService fileServerService(){
        return new FileServerService();
    }
}
