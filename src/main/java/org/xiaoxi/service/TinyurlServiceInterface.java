package org.xiaoxi.service;

import org.xiaoxi.dto.TinyurlResult;
import org.xiaoxi.dto.Url;
import org.xiaoxi.entity.Tinyurl;

/**
 * Created by YanYang on 2016/6/18.
 */
public interface TinyurlServiceInterface {

    int insertTinyurl(Tinyurl tinyurl);

    Tinyurl getIdByUrl(String url);

    Tinyurl getUrlById(long id);

    Url transferToShort_url(String long_url);

    Url transferToLong_url(String short_url);
}
