package org.xiaoxi.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xiaoxi.dao.BadUrlDao;
import org.xiaoxi.entity.BadUrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by YanYang on 2016/7/22.
 */
@Component
public class BadUrlUtil implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(BadUrlUtil.class);

    private Trie trie = trie = new Trie();
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock writeLock = lock.writeLock();
    private Lock readLock = lock.readLock();

    @Autowired
    BadUrlDao badUrlDao;

    public void afterPropertiesSet() throws Exception {
        List<BadUrl> badUrls = badUrlDao.selectAll();
        for (BadUrl badUrl : badUrls) {
            if (badUrl == null || badUrl.getUrl() == null) {
                continue;
            }
            addBadUrl(badUrl.getUrl());
        }
    }

    public void addBadUrl(String badUrl) {
        writeLock.tryLock();
        try {
            trie.add(badUrl);
        } catch (Exception e) {
            logger.error("在字典树中添加 badurl 异常" + e.getMessage());
        } finally {
            writeLock.unlock();
        }
    }

    public boolean containsBadUrl(String url) {
        readLock.tryLock();
        try {
            return trie.contains(url);
        } catch (Exception e) {
            logger.error("在字典树中判断是否存在 badurl 异常" + e.getMessage());
            return false;
        } finally {
            readLock.unlock();
        }
    }

    class Trie {
        private Map<String, Object> root = new HashMap<String, Object>();

        private final String END = "END";

        public void add(String word) {
            Map<String, Object> cur = root;
            Map<String, Object> newMap;
            for (int i = 0; i < word.length(); i++) {
                String c = word.substring(i, i + 1);
                if (cur.containsKey(c)) {
                    cur = (Map)cur.get(c);
                } else {
                    newMap = new HashMap<String, Object>();
                    cur.put(c, newMap);
                    cur = newMap;
                }
            }
            if (!cur.containsKey(END)) {
                cur.put(END, 1);
            }
        }

        public boolean contains(String word) {
            Map<String, Object> cur = root;
            for (int i = 0; i < word.length(); i++) {
                String c = word.substring(i, i + 1);
                if (!cur.containsKey(c)) {
                    return false;
                }
                cur = (Map)cur.get(c);
            }
            if (cur.containsKey(END) && 1 == (Integer)cur.get(END)) {
                return true;
            }
            return false;
        }
    }
}
