package org.cloud.sonic.controller.services.impl;

import org.cloud.sonic.controller.mapper.ConfListMapper;
import org.cloud.sonic.controller.models.domain.ConfList;
import org.cloud.sonic.controller.services.ConfListService;
import org.cloud.sonic.controller.services.impl.base.SonicServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class ConfListServiceImpl extends SonicServiceImpl<ConfListMapper, ConfList> implements ConfListService {


    @Override
    public ConfList searchByKey(String key) {
        return lambdaQuery().eq(ConfList::getConfKey, key).last("limit 1").one();
    }

    @Override
    public void save(String key, String content, String extra) {
        ConfList conf = searchByKey(key);

        if (conf == null) {
            conf = new ConfList();
        }
        conf.setContent(content)
                .setConfKey(key)
                .setExtra(extra);
        saveOrUpdate(conf);
    }
}
