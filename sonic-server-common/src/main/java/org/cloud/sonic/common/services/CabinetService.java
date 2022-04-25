package org.cloud.sonic.common.services;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cloud.sonic.common.models.domain.Cabinet;

import java.util.List;

public interface CabinetService extends IService<Cabinet> {
    List<Cabinet> findCabinets();

    void saveCabinet(Cabinet cabinet);

    Cabinet getIdByKey(String key);
}
