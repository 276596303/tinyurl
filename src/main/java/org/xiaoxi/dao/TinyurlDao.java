package org.xiaoxi.dao;

import org.apache.ibatis.annotations.Param;
import org.xiaoxi.entity.Tinyurl;

/**
 * Created by YanYang on 2016/6/18.
 */
public interface TinyurlDao {
    int insertUrl(Tinyurl tinyurl);
    Tinyurl getIdByUrl(@Param("url")String url);
    Tinyurl getUrlById(@Param("id")long id);
}
