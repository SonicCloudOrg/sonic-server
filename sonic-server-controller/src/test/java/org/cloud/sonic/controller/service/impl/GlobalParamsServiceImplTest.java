package org.cloud.sonic.controller.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import org.cloud.sonic.controller.mapper.GlobalParamsMapper;
import org.cloud.sonic.controller.models.domain.GlobalParams;
import org.cloud.sonic.controller.services.impl.GlobalParamsServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class GlobalParamsServiceImplTest {

    @InjectMocks
    private GlobalParamsServiceImpl globalParamsService;

    @Mock
    private GlobalParamsMapper globalParamsMapper;

    @Test
    public void testFindAll() {
        GlobalParams globalParams = new GlobalParams()
                .setProjectId(1)
                .setId(1)
                .setParamsKey("hello")
                .setParamsValue("world");

        Mockito.when(new LambdaQueryChainWrapper<>(globalParamsMapper)
                .eq(GlobalParams::getProjectId, Mockito.any()).list())
                .thenReturn(Arrays.asList(globalParams));

        List<GlobalParams> list = globalParamsService.findAll(1);

        Assert.assertEquals(1, list.size());
        Assert.assertEquals("hello", list.get(0).getParamsKey());
    }

    @Test
    public void testDelete() {

        Mockito.when(globalParamsMapper.deleteById(Mockito.anyInt()))
                .thenReturn(1);

        Assert.assertEquals(true, globalParamsService.delete(999));
    }

    @Test
    public void testFindById() {
        GlobalParams globalParams = new GlobalParams()
                .setProjectId(1)
                .setId(1)
                .setParamsKey("hello")
                .setParamsValue("world");

        Mockito.when(globalParamsMapper.selectById(Mockito.anyInt()))
                .thenReturn(globalParams);

        Assert.assertEquals(globalParams, globalParamsService.findById(1));
    }

    @Test
    public void testDeleteByProjectId() {

        Mockito.when(globalParamsMapper.delete(
                new LambdaQueryWrapper<GlobalParams>()
                        .eq(GlobalParams::getProjectId, Mockito.any())))
                .thenReturn(1);

        Assert.assertEquals(true, globalParamsService.deleteByProjectId(999));
    }
}
