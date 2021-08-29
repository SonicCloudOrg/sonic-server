package com.sonic.controller.services;

import com.alibaba.fastjson.JSONObject;
import com.sonic.controller.models.ResultDetail;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des 测试结果详情逻辑层
 * @date 2021/8/21 16:08
 */
public interface ResultDetailService {
    void saveByTransport(JSONObject jsonObject);

    List<ResultDetail> findByResultIdAndType(int resultId, String type);

    void deleteByResultId(int resultId);
}
