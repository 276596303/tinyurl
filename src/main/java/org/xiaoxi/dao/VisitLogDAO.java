package org.xiaoxi.dao;

import org.apache.ibatis.annotations.Param;
import org.xiaoxi.entity.VisitLog;

import java.util.Date;
import java.util.List;

/**
 * Created by YanYang on 2016/7/20.
 */
public interface VisitLogDAO {

    int insert(VisitLog visitLog);

    VisitLog selectUrlCountAndTimeSection(@Param("host")String host,
                                               @Param("startTime")Date startTime,
                                               @Param("endTime")Date endTime);

    List<VisitLog> selectAllUrlCountTimeSection(@Param("startTime")Date startTime,
                                             @Param("endTime")Date endTime);



}
