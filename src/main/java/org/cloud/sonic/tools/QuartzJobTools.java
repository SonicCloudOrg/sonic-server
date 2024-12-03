package org.cloud.sonic.tools;

import org.quartz.CronExpression;

public final class QuartzJobTools {
    public static String validateOrDisableCronExpression(final String cron) {
        return (cron!=null && !cron.equals("") && CronExpression.isValidExpression(cron) ? cron : "0 0 0 ? * MON 1900");
    }
}
