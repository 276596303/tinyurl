package org.xiaoxi.dao;

import org.xiaoxi.entity.BadUrl;

import java.util.List;
import java.util.Set;

/**
 * Created by YanYang on 2016/7/22.
 */
public interface BadUrlDao {

    int insert(BadUrl badUrl);

    List<BadUrl> selectAll();

    int delete(BadUrl badUrl);
}
