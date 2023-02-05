package org.cloud.sonic.controller.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.controller.ControllerApplication;
import org.cloud.sonic.controller.models.domain.Jobs;
import org.cloud.sonic.controller.models.interfaces.JobStatus;
import org.cloud.sonic.controller.services.JobsService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest(classes = ControllerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class JobServiceImplTest {
    @Autowired
    private JobsService jobsService;

    @Test
    public void testFindSysJob() {
        List<JSONObject> list = jobsService.findSysJobs();
        Assert.assertEquals(4, list.size());
    }

    @Test
    public void testSaveJob() {
        Jobs jobs = new Jobs();
        jobs.setName("Hello");
        jobs.setSuiteId(1);
        jobs.setProjectId(1);
        jobs.setCronExpression("0 0 0 * * ?");
        jobsService.saveJobs(jobs);
        Assert.assertEquals("testJob", jobs.getType());
        Assert.assertEquals(JobStatus.ENABLE, (int) jobs.getStatus());
        Assert.assertEquals(jobs, jobsService.findById(jobs.getId()));
        jobsService.delete(jobs.getId());
    }
}
