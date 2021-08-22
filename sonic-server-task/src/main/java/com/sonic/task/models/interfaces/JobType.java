package com.sonic.task.models.interfaces;

/**
 * @author ZhouYiXun
 * @des 定时任务类型
 * @date 2021/8/21 17:09
 */
public interface JobType {
    int TEST_JOB = 1;
    int CLEAN_FILE_JOB = 2;
    int CLEAN_RESULT_JOB = 3;
}
