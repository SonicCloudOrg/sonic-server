package org.cloud.sonic.controller.services;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cloud.sonic.controller.models.domain.Results;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des 测试结果逻辑层
 * @date 2021/8/21 16:08
 */
public interface ResultsService extends IService<Results> {
    Page<Results> findByProjectId(int projectId, Page<Results> pageable);

    List<Results> findByProjectId(int projectId);

    boolean delete(int id);

    Results findById(int id);

    void clean(int day);

    void suiteResult(int id);

    JSONArray findCaseStatus(int id);

    void subResultCount(int id);

    JSONObject chart(String startTime, String endTime, int projectId);

    void sendDayReport();

    void sendWeekReport();

    void deleteByProjectId(int projectId);
}
