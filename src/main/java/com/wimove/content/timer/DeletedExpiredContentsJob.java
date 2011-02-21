package com.wimove.content.timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.wimove.content.service.BatchContentsService;

public class DeletedExpiredContentsJob extends QuartzJobBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2712930144479564376L;

	private static final Log LOG = LogFactory
			.getLog(DeletedExpiredContentsJob.class);

	public void executeInternal(JobExecutionContext jobExecutionContext)
			throws JobExecutionException {

		JobDataMap jobDataMap = jobExecutionContext.getJobDetail()
				.getJobDataMap();
		if (jobDataMap.getBooleanValue("enabled")) {

			if (LOG.isDebugEnabled()) {
				LOG.debug("Starting delete unused contents");
			}

			((BatchContentsService) jobDataMap.get("service"))
					.removeExpiredItems();
		} else {
			if (LOG.isWarnEnabled()) {
				LOG.warn("Removing expired content job is disabled");
			}

		}
	}

}
