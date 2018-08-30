package com.taobao.brand.bear.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author jinshuan.li 2018/8/24 18:48
 */
@Slf4j
public class ExampleJob implements Job {

    /**
     * <p> Called by the <code>{@link Scheduler}</code> when a <code>{@link Trigger}</code> fires that is associated
     * with the <code>Job</code>. </p> <p> <p> The implementation may wish to set a {@link
     * JobExecutionContext#setResult(Object) result} object on the {@link JobExecutionContext} before this method exits.
     *  The result itself is meaningless to Quartz, but may be informative to <code>{@link JobListener}s</code> or
     * <code>{@link TriggerListener}s</code> that are watching the job's execution. </p>
     *
     * @param context
     * @throws JobExecutionException if there is an exception while executing the job.
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        log.info(context.toString());
    }
}
