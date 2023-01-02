/*
 *   sonic-server  Sonic Cloud Real Machine Platform.
 *   Copyright (C) 2022 SonicCloudOrg
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published
 *   by the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.cloud.sonic.controller.services.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cloud.sonic.controller.mapper.ResultDetailMapper;
import org.cloud.sonic.controller.models.domain.Devices;
import org.cloud.sonic.controller.models.domain.ResultDetail;
import org.cloud.sonic.controller.services.DevicesService;
import org.cloud.sonic.controller.services.ResultDetailService;
import org.cloud.sonic.controller.services.ResultsService;
import org.cloud.sonic.controller.services.impl.base.SonicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des 测试结果详情逻辑实现
 * @date 2021/8/21 20:55
 */
@Service
public class ResultDetailServiceImpl extends SonicServiceImpl<ResultDetailMapper, ResultDetail> implements ResultDetailService {

    @Autowired
    private ResultDetailMapper resultDetailMapper;
    @Autowired
    private DevicesService devicesService;
    @Autowired
    private ResultsService resultsService;

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

        if (resultInfo.getType().equals("status")) {
            baseMapper.delete(new LambdaQueryWrapper<ResultDetail>()
                    .eq(ResultDetail::getResultId, resultInfo.getResultId())
                    .eq(ResultDetail::getType, resultInfo.getType())
                    .eq(ResultDetail::getCaseId, resultInfo.getCaseId())
                    .eq(ResultDetail::getDeviceId, resultInfo.getDeviceId())
            );
        }

        save(resultInfo);
        if (jsonMsg.getString("msg").equals("status")) {
            resultsService.suiteResult(jsonMsg.getInteger("rid"));
        }
    }

    @Override
    public Page<ResultDetail> findAll(int resultId, int caseId, String type, int deviceId, Page<ResultDetail> pageable) {

        LambdaQueryChainWrapper<ResultDetail> lambdaQuery = lambdaQuery();

        if (resultId != 0) {
            lambdaQuery.eq(ResultDetail::getResultId, resultId);
        }
        if (caseId != 0) {
            lambdaQuery.eq(ResultDetail::getCaseId, caseId);
        }
        if (type != null && type.length() > 0) {
            lambdaQuery.eq(ResultDetail::getType, type);
        }
        if (deviceId != 0) {
            lambdaQuery.eq(ResultDetail::getDeviceId, deviceId);
        }

        return lambdaQuery.orderByAsc(ResultDetail::getTime)
                .page(pageable);
    }

    @Override
    public List<ResultDetail> findAll(int resultId, int caseId, String type, int deviceId) {

        LambdaQueryChainWrapper<ResultDetail> lambdaQuery = lambdaQuery();

        if (resultId != 0) {
            lambdaQuery.eq(ResultDetail::getResultId, resultId);
        }
        if (caseId != 0) {
            lambdaQuery.eq(ResultDetail::getCaseId, caseId);
        }
        if (type != null && type.length() > 0) {
            lambdaQuery.eq(ResultDetail::getType, type);
        }
        if (deviceId != 0) {
            lambdaQuery.eq(ResultDetail::getDeviceId, deviceId);
        }
        return lambdaQuery.orderByAsc(ResultDetail::getTime).list();
    }


    @Override
    public void deleteByResultId(int resultId) {
        baseMapper.delete(new QueryWrapper<ResultDetail>().eq("result_id", resultId));
    }

    @Override
    public List<JSONObject> findTimeByResultIdGroupByCaseId(int resultId) {
        return resultDetailMapper.findTimeByResultIdGroupByCaseId(resultId);
    }

    @Override
    public List<JSONObject> findStatusByResultIdGroupByCaseId(int resultId) {
        return resultDetailMapper.findStatusByResultIdGroupByCaseId(resultId);
    }

    @Override
    public List<JSONObject> findTopCases(String startTime, String endTime, int projectId) {
        return resultDetailMapper.findTopCases(startTime, endTime, projectId);
    }

    @Override
    public List<JSONObject> findTopDevices(String startTime, String endTime, int projectId) {
        return resultDetailMapper.findTopDevices(startTime, endTime, projectId);
    }
}
