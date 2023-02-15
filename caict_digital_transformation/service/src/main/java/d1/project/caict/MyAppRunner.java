package d1.project.caict;

import d1.project.caict.business.Init;
import d1.project.caict.system.service.PostgreSqlBackupService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author Buter
 * @date 2020/3/20 14:59
 */
@Component
public class MyAppRunner implements ApplicationRunner {
    private final PostgreSqlBackupService postgreSqlBackupService;

    public MyAppRunner(PostgreSqlBackupService postgreSqlBackupService) {
        this.postgreSqlBackupService = postgreSqlBackupService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Init.init();
        postgreSqlBackupService.startAndRun();
    }
}
