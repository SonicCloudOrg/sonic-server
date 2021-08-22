package com.sonic.controller.services;

import com.sonic.controller.models.ResultDetail;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des 测试结果详情逻辑层
 * @date 2021/8/21 16:08
 */
public interface ResultDetailService {
    List<ResultDetail> findByResultIdAndType(int resultId, String type);

    void deleteByResultId(int resultId);
}
