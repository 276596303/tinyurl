package org.xiaoxi.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.xiaoxi.entity.VisitLog;
import org.xiaoxi.service.VisitLogService;

import java.util.List;

/**
 * Created by YanYang on 2016/7/21.
 */
@Controller
public class VisitLogController {
    private static final Logger logger = LoggerFactory.getLogger(VisitLogController.class);

    @Autowired
    VisitLogService visitLogService;

    @RequestMapping(value = "/visitLog/week/", method = {RequestMethod.GET, RequestMethod.POST})
    public String getvisitLogOneWeek(Model model) {

        List<VisitLog> visitLogs = visitLogService.getAllUrlCountDataOneWeek();
        model.addAttribute("visitLogs", visitLogs);
        return "visitLog";

    }

    @RequestMapping(value = "/visitLog/month/", method = {RequestMethod.GET, RequestMethod.POST})
    public String getvisitLogOneMonth(Model model) {

        List<VisitLog> visitLogs = visitLogService.getAllUrlCountDataOneMonth();
        model.addAttribute("visitLogs", visitLogs);
        return "visitLog";
    }

    @RequestMapping(value = "/visitLog/year/", method = {RequestMethod.GET, RequestMethod.POST})
    public String getvisitLogOneYear(Model model) {

        List<VisitLog> visitLogs = visitLogService.getAllUrlCountDataOneYear();
        model.addAttribute("visitLogs", visitLogs);
        return "visitLog";
    }

}
