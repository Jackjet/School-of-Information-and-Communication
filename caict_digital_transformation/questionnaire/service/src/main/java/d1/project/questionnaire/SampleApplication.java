package d1.project.questionnaire;

import d1.framework.webapi.BaseApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author Kikki 2019/8/26 10:15
 */
@SpringBootApplication
@EnableJpaAuditing
public class SampleApplication extends BaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }
}
