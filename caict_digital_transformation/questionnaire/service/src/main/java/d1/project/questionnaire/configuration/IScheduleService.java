package d1.project.questionnaire.configuration;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.function.Consumer;

/**
 * @author Buter
 * @date 2020/2/21 10:52
 */
public interface IScheduleService {

    /**
     * 安排在特定时间执行特定的任务
     *
     * @param task       执行的回调任务函数，不能为空
     * @param parameters 回调时的参数集合，可以为空
     * @param cron       设置定时器的标准写法，不能为空，格式必须是类似0 0 12 * * ? 这种格式。参考https://blog.csdn.net/win7system/article/details/77869112
     * @return 返回任务标识
     * @throws Exception 校验和其它异常
     */
    ScheduledFuture<?> schedule(Consumer<Map<String, Object>> task, Map<String, Object> parameters, String cron) throws Exception;

    /**
     * 安排在特定时间执行特定的任务
     *
     * @param task       执行的回调任务函数，不能为空
     * @param parameters 回调时的参数集合，可以为空
     * @param startTime  任务开始时间
     * @return 返回任务标识
     * @throws Exception 校验和其它异常
     */
    ScheduledFuture<?> schedule(Consumer<Map<String, Object>> task, Map<String, Object> parameters, Date startTime) throws Exception;

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
    boolean cancel(ScheduledFuture<?> future);

    /**
     * 关闭所有任务，停止线程池，执行后如果还想继续需重新new ScheduleService对象
     * 执行前请最好先cancel所有线程
     */
    void shutdown();
}
