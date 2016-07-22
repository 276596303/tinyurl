package org.xiaoxi.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiaoxi.dao.VisitLogDAO;
import org.xiaoxi.entity.VisitLog;
import org.xiaoxi.utils.DecimalTransfer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by YanYang on 2016/7/21.
 */
@Service
public class VisitLogService {
    private static final Logger logger = LoggerFactory.getLogger(VisitLogService.class);

    @Autowired
    VisitLogDAO visitLogDAO;

    public boolean addVisitLog(VisitLog visitLog) {
        int count = 0;
        try {
            count = visitLogDAO.insert(visitLog);
        } catch (Exception e) {
            logger.error("插入访问日志失败");
            return false;
        }
        return count > 0 ? true : false;
    }

    public VisitLog getUrlCountTimeSection(String host, Date startTime, Date endTime) {
        VisitLog visitLog = null;
        try {
            visitLog = visitLogDAO.selectUrlCountAndTimeSection(host, startTime, endTime);
        } catch (Exception e) {
            logger.error("获取网址 " + host + " 某段时间内的访问数失败");
            return null;
        }
        return visitLog;
    }

    public List<VisitLog> getAllUrlCountTimeSection(Date startTime, Date endTime) {
        List<VisitLog> result = null;
        try {
            result = visitLogDAO.selectAllUrlCountTimeSection(startTime, endTime);
        } catch (Exception e) {
            logger.error("获取某段时间内所有url的访问数失败");
            return null;
        }
        return result;
    }

    public List<VisitLog> getAllUrlCountDataOneWeek() {
        Date currentTime = new Date();
        Date aWeekAgo = new Date(currentTime.getTime() - 1000 * 60 * 60 * 24 * 7);
        List<VisitLog> visitLogs = visitLogDAO.selectAllUrlCountTimeSection(aWeekAgo, currentTime);

        return visitLogs;
    }

    public List<VisitLog> getAllUrlCountDataOneMonth() {
        Date currentTime = new Date();
        Date aMonthAgo = new Date(currentTime.getTime() - (long)1000 * 60 * 60 * 24 * 30);
        List<VisitLog> visitLogs = visitLogDAO.selectAllUrlCountTimeSection(aMonthAgo, currentTime);

        return visitLogs;
    }

    public List<VisitLog> getAllUrlCountDataOneYear() {
        Date currentTime = new Date();
        Date aYearAgo = new Date(currentTime.getTime() - (long)1000 * 60 * 60 * 24 * 365);
        List<VisitLog> visitLogs = visitLogDAO.selectAllUrlCountTimeSection(aYearAgo, currentTime);

        return visitLogs;
    }
}
