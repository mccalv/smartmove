/**
 * 
 */
package com.closertag.smartmove.server.content.timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.closertag.smartmove.server.content.service.BatchContentsService;

/**
 * @author mccalv
 * 
 */
public class ImportContentJob extends QuartzJobBean {

	private static final Log LOG = LogFactory.getLog(ImportContentJob.class);

	public void executeInternal(JobExecutionContext jobExecutionContext)
			throws JobExecutionException {
		JobDataMap jobDataMap = jobExecutionContext.getJobDetail()
				.getJobDataMap();
		if (jobDataMap.getBooleanValue("enabled")) {

			if (LOG.isDebugEnabled()) {
				LOG.debug("Starting importing all contents");
			}

			BatchContentsService batchContentsService = (BatchContentsService) jobDataMap
					.get("service");

			batchContentsService.importAll();

			if (LOG.isDebugEnabled()) {
				LOG.debug("Starting importing csv contents");
			}
			batchContentsService.importCsvContents();

		} else {
			if (LOG.isWarnEnabled()) {
				LOG.warn("Import content job is disables");
			}

		}
	}

}
