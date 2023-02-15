package d1.project.cms;

import d1.framework.webapi.BaseApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author baozh
 */
@SpringBootApplication
public class CmsServiceApplication extends BaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(CmsServiceApplication.class, args);
    }
}
