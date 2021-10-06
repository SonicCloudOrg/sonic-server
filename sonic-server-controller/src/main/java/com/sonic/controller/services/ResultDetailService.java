package com.sonic.controller.services;

import com.alibaba.fastjson.JSONObject;
import com.sonic.controller.models.ResultDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des 测试结果详情逻辑层
 * @date 2021/8/21 16:08
 */
public interface ResultDetailService {
    void save(ResultDetail resultDetail);

    void saveByTransport(JSONObject jsonObject);

    Page<ResultDetail> findAll(int resultId, int caseId, String type, int deviceId, Pageable pageable);

    List<ResultDetail> findAll(int resultId, int caseId, String type, int deviceId);

    void deleteByResultId(int resultId);
}
