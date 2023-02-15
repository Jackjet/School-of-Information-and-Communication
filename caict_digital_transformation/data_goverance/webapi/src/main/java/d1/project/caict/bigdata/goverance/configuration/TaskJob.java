package d1.project.caict.bigdata.goverance.configuration;

import d1.project.caict.bigdata.goverance.entity.WebAdminUser;
import d1.project.caict.bigdata.goverance.entity.task.GroupTask;
import d1.project.caict.bigdata.goverance.service.task.GroupTaskService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(TaskJob.class);

    @Override

    public void execute(JobExecutionContext context) {
        GroupTaskService groupTaskService = (GroupTaskService) context.getJobDetail().getJobDataMap().get("groupTaskService");
        WebAdminUser wbAdminUser = (WebAdminUser) context.getJobDetail().getJobDataMap().get("webAdminUser");
        GroupTask groupTask = (GroupTask) context.getJobDetail().getJobDataMap().get("groupTask");
        try {
            groupTaskService.execute(groupTask.getId(), wbAdminUser.getId(), wbAdminUser.getName());
        } catch (Exception e) {
            logger.error("任务组定时任务异常：" + groupTask.getId());
        }
    }
}
