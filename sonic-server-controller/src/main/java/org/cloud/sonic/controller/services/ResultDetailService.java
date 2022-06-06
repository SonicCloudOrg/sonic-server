package org.cloud.sonic.controller.services;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cloud.sonic.controller.models.domain.ResultDetail;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des 测试结果详情逻辑层
 * @date 2021/8/21 16:08
 */
public interface ResultDetailService extends IService<ResultDetail> {

    void saveByTransport(JSONObject jsonObject);

    Page<ResultDetail> findAll(int resultId, int caseId, String type, int deviceId, Page<ResultDetail> pageable);

    List<ResultDetail> findAll(int resultId, int caseId, String type, int deviceId);

    void deleteByResultId(int resultId);

    List<JSONObject> findTimeByResultIdGroupByCaseId(int resultId);

    List<JSONObject> findStatusByResultIdGroupByCaseId(int resultId);

    List<JSONObject> findTopCases(String startTime, String endTime, int projectId);

    List<JSONObject> findTopDevices(String startTime, String endTime, int projectId);
}
