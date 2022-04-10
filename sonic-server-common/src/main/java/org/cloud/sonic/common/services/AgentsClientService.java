/*
 *  Copyright (C) [SonicCloudOrg] Sonic Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.cloud.sonic.common.services;

import com.alibaba.fastjson.JSONObject;

/**
 * 调度agent的服务，跟{@link AgentsService}不同，{@link AgentsService}是操作数据库的接口，而这里是直接操作Agent的
 * 调度的时候注意指定ip:port，否则根据默认的负载均衡策略调度导致失败
 *
 * @author JayWenStar
 * @date 2022/4/8 11:17 下午
 */
public interface AgentsClientService {

    /**
     * 运行测试套件
     * Consumer调用时，请指定ip:port
     *
     * @param jsonObject 参考 {@link TestSuitesService#runSuite(int, String)} 的实现传入
     */
    void runSuite(JSONObject jsonObject);

}
