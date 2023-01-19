package org.cloud.sonic.controller.services;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cloud.sonic.controller.models.domain.GlobalParams;
import org.cloud.sonic.controller.models.domain.TestSuitesParams;

import java.util.List;

public interface TestSuitesParamsService extends IService<TestSuitesParams> {
    List<TestSuitesParams> findAll(int suiteId);

    boolean delete(int id);

    TestSuitesParams findById(int id);

    boolean deleteBySuiteId(int suiteId);


}
