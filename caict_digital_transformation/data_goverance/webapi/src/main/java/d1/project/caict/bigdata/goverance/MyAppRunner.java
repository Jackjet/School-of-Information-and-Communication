package d1.project.caict.bigdata.goverance;

import com.alibaba.fastjson.JSONObject;
import d1.project.caict.bigdata.goverance.configuration.QuartzManager;
import d1.project.caict.bigdata.goverance.configuration.TaskJob;
import d1.project.caict.bigdata.goverance.entity.WebAdminUser;
import d1.project.caict.bigdata.goverance.entity.task.GroupTask;
import d1.project.caict.bigdata.goverance.service.WebAdminService;
import d1.project.caict.bigdata.goverance.service.task.GroupTaskService;
import org.quartz.JobDataMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * @author Buter
 * @date 2020/3/20 14:59
 */
@Component
public class MyAppRunner implements ApplicationRunner {
    final
    GroupTaskService groupTaskService;
    final
    WebAdminService webAdminService;
    private static final Logger logger = LoggerFactory.getLogger(MyAppRunner.class);

    public MyAppRunner(GroupTaskService groupTaskService, WebAdminService webAdminService) {
        this.groupTaskService = groupTaskService;
        this.webAdminService = webAdminService;
    }

    @Override
    public void run(ApplicationArguments args) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("operationMode", "周期调度");
        jsonObject.put("status", "已上线");
        try {
            Page<GroupTask> all = groupTaskService.findAll(jsonObject);
            all.forEach(entity -> {
                Optional<WebAdminUser> byId = webAdminService.findById(StringUtils.isEmpty(entity.getUpdateById()) ? entity.getCreateById() : entity.getUpdateById());
                if (byId.isPresent()) {
                    WebAdminUser webAdminUser = byId.get();
                    try {
//                        groupTaskService.execute(groupTask.getId(), webAdminUser.getId(), webAdminUser.getName());
                        JobDataMap newJobDataMap = new JobDataMap();
                        newJobDataMap.put("groupTaskService", this.groupTaskService);
                        newJobDataMap.put("webAdminUser", webAdminUser);
                        newJobDataMap.put("groupTask", entity);
                        QuartzManager.addJob(entity.getId(), "GroupTask", entity.getId(), "GroupTask", TaskJob.class, entity.getScheduleCycle(), newJobDataMap);
                    } catch (Exception e) {
                        logger.error("启动任务异常:" + entity.getId());
                    }
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
