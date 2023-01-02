package org.cloud.sonic.controller.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.cloud.sonic.controller.models.domain.ResultDetail;

import java.util.List;

/**
 * Mapper 接口
 *
 * @author JayWenStar
 * @since 2021-12-17
 */
@Mapper
public interface ResultDetailMapper extends BaseMapper<ResultDetail> {

    @Select("select case_id, max(time) as endTime,min(time) as startTime from result_detail " +
            "where result_id=#{resultId} and type = 'step' group by case_id")
    List<JSONObject> findTimeByResultIdGroupByCaseId(@Param("resultId") int resultId);

    @Select("select t2.device_id, t2.case_id, IFNULL(t1.status,4) as status " +
            "from (select case_id, device_id, status from result_detail where result_id=#{resultId} and type = 'status')t1" +
            " right join" +
            "(select case_id, device_id from result_detail where result_id=#{resultId} and type = 'step' group by case_id, device_id)t2" +
            " on t1.device_id = t2.device_id and t1.case_id = t2.case_id")
    List<JSONObject> findStatusByResultIdGroupByCaseId(@Param("resultId") int resultId);

    @Select("select t.case_id,sum(t.diff) as total from (select case_id,result_id,TIMESTAMPDIFF(SECOND,min(time),max(time)) as diff " +
            "from result_detail where result_id in " +
            "(select id from results where end_time > #{startTime} and end_time <= #{endTime} and project_id=#{projectId}) " +
            "and type='step' group by result_id,case_id)t group by t.case_id order by total desc limit 5")
    List<JSONObject> findTopCases(@Param("startTime") String startTime,
                                  @Param("endTime") String endTime,
                                  @Param("projectId") int projectId);

    @Select("select t.device_id,sum(t.diff) as total from (select device_id,case_id,result_id,TIMESTAMPDIFF(SECOND,min(time),max(time)) as diff " +
            "from result_detail where result_id in " +
            "(select id from results where end_time > #{startTime} and end_time <= #{endTime} and project_id = #{projectId}) " +
            "and type='step' group by result_id,case_id,device_id)t group by t.device_id order by total desc limit 5")
    List<JSONObject> findTopDevices(@Param("startTime") String startTime,
                                    @Param("endTime") String endTime,
                                    @Param("projectId") int projectId);
}
