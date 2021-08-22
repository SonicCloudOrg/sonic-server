package com.sonic.controller.services.impl;

import com.sonic.controller.models.ResultDetail;
import com.sonic.controller.services.ResultDetailService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des 测试结果详情逻辑实现
 * @date 2021/8/21 20:55
 */
@Service
public class ResultDetailServiceImpl implements ResultDetailService {
    @Override
    public List<ResultDetail> findByResultIdAndType(int resultId, String type) {
        return null;
    }

    @Override
    public void deleteByResultId(int resultId) {

    }
}
