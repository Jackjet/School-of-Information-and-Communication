package d1.project.questionnaire.configuration;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.function.Consumer;

/**
 * @author Buter
 * @date 2020/2/21 10:21
 */
public class ScheduleServiceImpl implements IScheduleService {
    private static final int DEFAULT_THREAD_COUNT = 4;
    private ThreadPoolTaskScheduler taskScheduler;

    /**
     * 初始化任务服务
     *
     * @param threadCount 缺省是4个线程，如果任务比较多，可以提高这个值
     */
    public ScheduleServiceImpl(int threadCount) {
        taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.initialize();
        // 定时任务执行线程池核心线程数
        taskScheduler.setPoolSize(threadCount);
        taskScheduler.setRemoveOnCancelPolicy(true);
        taskScheduler.setThreadNamePrefix("TaskSchedulerThreadPool-");
    }

    public ScheduleServiceImpl() {
        this(DEFAULT_THREAD_COUNT);
    }

    @Override
    public ScheduledFuture<?> schedule(Consumer<Map<String, Object>> task, Map<String, Object> parameters, String cron) throws Exception {
        if (task == null || StringUtils.isEmpty(cron)) {
            throw new Exception("任务回调函数和时间安排都不能为空");
        }
        CronTask cronTask = new CronTask(() -> task.accept(parameters), cron);
        return taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger());
    }

    @Override
    public ScheduledFuture<?> schedule(Consumer<Map<String, Object>> task, Map<String, Object> parameters, Date startTime) throws Exception {
        if (task == null || startTime == null) {
            throw new Exception("任务回调函数和时间安排都不能为空");
        }
        return taskScheduler.schedule(() -> task.accept(parameters), startTime);
    }


    /**
     * 取消一个特定的任务
     * 当你想要取消你已提交给执行者的任务，使用Future接口的cancel()方法
     * 根据cancel()方法参数和任务的状态不同，这个方法的行为将不同
     * 1、如果这个任务已经完成或之前的已经被取消或由于其他原因不能被取消，
     * 那么这个方法将会返回false并且这个任务不会被取消。
     * 2、如果这个任务正在等待执行者获取执行它的线程，那么这个任务将被取消而且不会开始他的执行。
     * 如果这个任务已经正在运行，则视方法的参数情况而定。
     * cancel()方法接收一个Boolean值参数。
     * 如果参数为true并且任务正在运行，那么这个任务将被取消。
     * 如果参数为false并且任务正在运行，那么这个任务将不会被取消。
     *
     * @return 是否取消成功
     */
    @Override
    public boolean cancel(ScheduledFuture<?> future) {
        return future.cancel(true);
    }

    /**
     * 关闭所有任务，停止线程池，执行后如果还想继续需重新new ScheduleService对象
     * 执行前请最好先cancel所有线程
     */
    @Override
    public void shutdown() {
        taskScheduler.shutdown();
    }
}
