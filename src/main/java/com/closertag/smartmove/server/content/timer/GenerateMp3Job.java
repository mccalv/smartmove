package com.closertag.smartmove.server.content.timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.closertag.smartmove.server.content.service.BatchContentsService;

public class GenerateMp3Job extends QuartzJobBean {
	private static final Log LOG = LogFactory.getLog(GenerateMp3Job.class);

	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) {
	
		// if()
		JobDataMap jobDataMap = jobExecutionContext.getJobDetail()
				.getJobDataMap();
		if (jobDataMap.getBooleanValue
				
				("enabled")) {
	
			if (LOG.isDebugEnabled()) {
				LOG.debug("Starting job for Mp3 creation");
			}

			
			
			((BatchContentsService) jobDataMap.get("service")).createMp3(2);
		}else{
			if (LOG.isWarnEnabled()) {
				LOG.warn("Creating MP3 jobs is disabled");
			}
			
			
		}

	}

}
