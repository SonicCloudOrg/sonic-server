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
package org.cloud.sonic.common.tools;

import org.springframework.lang.NonNull;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * The default thread pool policy of Dubbo has bugs, Replace it with this thread pool.
 *
 * @author JayWenStar
 * @date 2022/5/19 1:31 上午
 */
public class DubboThreadPool {

    private static final int DEFAULT_POOL_SIZE = Runtime.getRuntime().availableProcessors() << 1;
    private static final long DEFAULT_ALIVE_TIME = 600L;
    private static final int DEFAULT_QUEUE_SIZE = Integer.MAX_VALUE;
    private static ExecutorService executorService;

    static {
        executorService = new ThreadPoolExecutor(
                DEFAULT_POOL_SIZE,
                DEFAULT_POOL_SIZE << 1,
                DEFAULT_ALIVE_TIME,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(DEFAULT_QUEUE_SIZE),
                new CustomizableThreadFactory("dubbo-pool-"),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    @NonNull
    public static ExecutorService get() {
        return executorService;
    }
}
