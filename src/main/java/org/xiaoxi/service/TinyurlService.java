package org.xiaoxi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiaoxi.cache.impl.UrlCacheImpl;
import org.xiaoxi.dao.TinyurlDao;
import org.xiaoxi.entity.Tinyurl;
import org.xiaoxi.exception.TinyurlException;
import org.xiaoxi.utils.DecimalTransfer;

/**
 * Created by YanYang on 2016/6/18.
 */
@Service
public class TinyurlService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TinyurlService.class);

    @Autowired
    private TinyurlDao tinyurlDao;

    @Autowired
    private UrlCacheImpl urlCache;

    public int insertTinyurl(Tinyurl tinyurl) {
        return tinyurlDao.insertUrl(tinyurl);
    }

    public Tinyurl getIdByUrl(String url) {
        return tinyurlDao.getIdByUrl(url);
    }

    public Tinyurl getUrlById(long id) {
        return tinyurlDao.getUrlById(id);
    }

    public String transferToShort_url(String long_url) throws TinyurlException{
        try {
            String short_url = null;
            //先查缓存
            short_url = urlCache.getShort_url(long_url);
            //缓存命中
            if (short_url != null && !short_url.equals("")) {
                return short_url;
            }
            //缓存未命中，查数据库
            Tinyurl tinyurl = getIdByUrl(long_url);
            if (tinyurl == null) {
                tinyurl = new Tinyurl(long_url);
                long id = insertTinyurl(tinyurl);  //返回的id是插入影响行数，插入后生成的id会放入tinurl对象中
                if (id > 0) {
                    long short_id = tinyurl.getId();
                    short_url = DecimalTransfer.idToShortUrl(short_id);
                    // 加缓存
                    urlCache.set(short_url, long_url);
                    return short_url;
                } else {
                    throw new TinyurlException(long_url + " 插入失败");
                }
            }
            short_url = DecimalTransfer.idToShortUrl(tinyurl.getId());
            return short_url;
        } catch (TinyurlException e) {
            return null;
        } catch (Exception e1) {
            LOGGER.error(e1.getMessage(), e1);
            return null;
        }
    }

    public String transferToLong_url(String short_url) throws TinyurlException{
        try {
            if (short_url != null && !short_url.equals("")) {
                long id = DecimalTransfer.shortUrlToID(short_url);
                // 查询缓存
                String long_url = urlCache.getLong_url(short_url);
                //缓存命中
                if (long_url != null && !long_url.equals("")) {
                    return long_url;
                }
                //缓存未命中
                Tinyurl tinyurl = getUrlById(id);
                if (tinyurl != null) {
                    urlCache.set(short_url, tinyurl.getUrl());
                    return tinyurl.getUrl();
                } else {
                    throw new TinyurlException("get url by id failed");
                }
            } else {
                throw new TinyurlException("short_url is illegal");
            }
        } catch (TinyurlException e) {
            throw e;
        } catch (Exception e1) {
            LOGGER.error(e1.getMessage(), e1);
            throw new TinyurlException("transferToLong_url" + e1.getMessage());
        }
    }
}
