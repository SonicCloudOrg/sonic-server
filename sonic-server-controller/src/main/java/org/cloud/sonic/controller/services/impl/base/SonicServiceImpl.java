package org.cloud.sonic.controller.services.impl.base;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.common.exception.ServerErrorException;
import org.cloud.sonic.common.tools.ReflectionTool;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Objects;

/**
 * 主要是为了兼容原来jpa的一些语法
 *
 * @author JayWenStar
 * @date 2021/12/17 11:38 下午
 */
@Slf4j
public class SonicServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {

    protected boolean existsById(Serializable id) {
        Field idField = getIdField(getDomainClass());
        Objects.requireNonNull(idField, "There is not @TableId field in Object");
        return baseMapper.selectCount(new QueryWrapper<T>().eq("id", id)) > 0;
    }

    /**
     * 如果id属性为空就insert，否则update
     */
    @Override
    public boolean save(T domain) {
        try {
            Field idField = getIdField(getDomainClass());
            Objects.requireNonNull(idField, "There is not @TableId field in Object");
            Integer id = (Integer) idField.get(domain);
            // 如果id为0，则设置为null
            if (id == null || id.equals(0)) {
                idField.set(domain, null);
                return baseMapper.insert(domain) > 0;
            }
            return baseMapper.updateById(domain) > 0;
        } catch (IllegalAccessException e) {
            throw new ServerErrorException("Handle id in Object failed.");
        }
    }

    protected <T> LambdaUpdateChainWrapper<T> lambdaUpdate(BaseMapper<T> baseMapper) {
        return ChainWrappers.lambdaUpdateChain(baseMapper);
    }

    protected <T> LambdaQueryChainWrapper<T> lambdaQuery(BaseMapper<T> baseMapper) {
        return ChainWrappers.lambdaQueryChain(baseMapper);
    }

    @SuppressWarnings("unchecked")
    private Class<T> getDomainClass() {
        ParameterizedType type = ReflectionTool.getParameterizedTypeBySuperClass(SonicServiceImpl.class, this.getClass());
        Objects.requireNonNull(type, "Cannot fetch actual type because parameterized type is null");
        return (Class<T>) type.getActualTypeArguments()[1];
    }

    private Field getIdField(Class<T> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getAnnotation(TableId.class) != null) {
                return field;
            }
        }
        return null;
    }
}
