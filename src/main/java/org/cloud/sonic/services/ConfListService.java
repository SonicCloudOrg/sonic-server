package org.cloud.sonic.services;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cloud.sonic.models.domain.ConfList;

public interface ConfListService extends IService<ConfList> {

    ConfList searchByKey(String key);

    void save(String key, String content, String extra);


}
