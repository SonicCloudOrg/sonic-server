package com.sonic.controller.services.impl;

import com.alibaba.fastjson.JSONObject;
import com.sonic.controller.dao.ResultDetailRepository;
import com.sonic.controller.models.Devices;
import com.sonic.controller.models.ResultDetail;
import com.sonic.controller.services.DevicesService;
import com.sonic.controller.services.ResultDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des 测试结果详情逻辑实现
 * @date 2021/8/21 20:55
 */
@Service
public class ResultDetailServiceImpl implements ResultDetailService {
    @Autowired
    private ResultDetailRepository resultDetailRepository;
    @Autowired
    private DevicesService devicesService;

    @Override
    public void save(ResultDetail resultDetail) {
        resultDetailRepository.save(resultDetail);
    }

    @Override
    public void saveByTransport(JSONObject jsonMsg) {
        Devices resultDevice = devicesService.findByAgentIdAndUdId(jsonMsg.getInteger("agentId")
                , jsonMsg.getString("udId"));
        ResultDetail resultInfo = new ResultDetail();
        resultInfo.setType(jsonMsg.getString("msg"));
        resultInfo.setLog(jsonMsg.getString("log"));
        resultInfo.setDes(jsonMsg.getString("des"));
        resultInfo.setStatus(jsonMsg.getInteger("status"));
        resultInfo.setResultId(jsonMsg.getInteger("rid"));
        resultInfo.setCaseId(jsonMsg.getInteger("cid"));
        resultInfo.setTime(jsonMsg.getDate("time"));
        resultInfo.setDeviceId(resultDevice == null ? 0 : resultDevice.getId());
        save(resultInfo);
    }

    @Override
    public List<ResultDetail> findByResultIdAndType(int resultId, String type) {
        return null;
    }

    @Override
    public void deleteByResultId(int resultId) {

    }
}
