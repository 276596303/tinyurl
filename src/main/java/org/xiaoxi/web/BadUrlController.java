package org.xiaoxi.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.xiaoxi.dao.BadUrlDao;
import org.xiaoxi.entity.BadUrl;
import org.xiaoxi.utils.BadUrlUtil;

import java.util.List;

/**
 * Created by YanYang on 2016/7/22.
 */
@Controller
public class BadUrlController {
    private static final Logger logger = LoggerFactory.getLogger(BadUrlController.class);
    private static final String[] USER_NAMES = {"xiaoxi", "hongxiyang"};

    private static final String[] PASS_WORDS= {"hongxiyang"};

    @Autowired
    BadUrlUtil badUrlUtil;

    @Autowired
    BadUrlDao badUrlDao;

    @RequestMapping(value = "/badurl/add", method = RequestMethod.POST)
    public String addBadUrl(@RequestParam("username")String username,
                            @RequestParam("password")String password,
                            @RequestParam("badurl")String badurl,
                            Model model) {

        try {
            boolean hasName = false;
            boolean hasPassword = false;
            for (String str : USER_NAMES) {
                if (str.equals(username)){
                    hasName = true;
                    break;
                }
            }
            for (String str : PASS_WORDS) {
                if (str.equals(password)) {
                    hasPassword = true;
                }
            }

            if (!hasName || !hasPassword) {
                return "redirect:/badurl";
            }

            BadUrl badUrl = new BadUrl();
            badUrl.setUrl(badurl);
            badUrlDao.insert(badUrl);
            badUrlUtil.addBadUrl(badurl);
            return "redirect:/badurl";

        } catch (Exception e) {
            logger.error("添加badurl异常");
            return "redirect:/badurl";
        }
    }

    @RequestMapping(value = "/badurl", method = RequestMethod.GET)
    public String showAllBadUrl(Model model) {
        try {
            List<BadUrl> badUrls = badUrlDao.selectAll();
            model.addAttribute("badUrls", badUrls);
            return "badUrls";
        } catch (Exception e) {
            logger.error("show all badurl controller exception" + e.getMessage());
            return "badUrls";
        }
    }
}
