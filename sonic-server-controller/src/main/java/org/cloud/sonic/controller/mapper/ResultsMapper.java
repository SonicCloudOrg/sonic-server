package org.cloud.sonic.controller.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.cloud.sonic.controller.models.domain.Results;

import java.util.List;

/**
 * Mapper 接口
 *
 * @author JayWenStar
 * @since 2021-12-17
 */
@Mapper
public interface ResultsMapper extends BaseMapper<Results> {

    @Select("select t2.date,round(IFNULL((t1.count/t2.count),0)*100,2) as rate from " +
            "(select DATE_FORMAT(end_time,'%Y-%m-%d') as date,count(status) as count from results where end_time > #{startTime} and end_time <= #{endTime} and project_id=#{projectId} and status=1 " +
            "group by status,DATE_FORMAT(end_time,'%Y-%m-%d'))t1 " +
            "right join" +
            "(select DATE_FORMAT(end_time,'%Y-%m-%d') as date,count(status) as count from results where end_time > #{startTime} and end_time <= #{endTime} and project_id=#{projectId} " +
            "group by DATE_FORMAT(end_time,'%Y-%m-%d'))t2 " +
            "on t1.date = t2.date")
    List<JSONObject> findDayPassRate(@Param("startTime") String startTime,
                                     @Param("endTime") String endTime,
                                     @Param("projectId") int projectId);

    @Select("select status,count(*) as total from results where end_time > #{startTime} and end_time<= #{endTime} and project_id=#{projectId} group by status")
    List<JSONObject> findDayStatus(@Param("startTime") String startTime,
                                   @Param("endTime") String endTime,
                                   @Param("projectId") int projectId);

    @Select("select count(*) as total from results where end_time > #{startTime} and end_time <= #{endTime} and project_id=#{projectId}")
    int findRunCount(@Param("startTime") String startTime,
                     @Param("endTime") String endTime,
                     @Param("projectId") int projectId);

}
