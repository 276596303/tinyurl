package org.xiaoxi.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.xiaoxi.entity.User;
import org.xiaoxi.entity.UsersTinyUrl;
import org.xiaoxi.entity.UsersTinyUrlViewObject;
import org.xiaoxi.entity.VisitLog;
import org.xiaoxi.service.TinyurlService;
import org.xiaoxi.service.UserService;
import org.xiaoxi.service.UsersTinyUrlService;
import org.xiaoxi.service.VisitLogService;
import org.xiaoxi.utils.Constant;
import org.xiaoxi.utils.DecimalTransfer;
import org.xiaoxi.utils.HostDecodeUtil;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by YanYang on 2016/7/24.
 */
@Controller
public class UsersTinyUrlController {
    private static final Logger logger = LoggerFactory.getLogger(UsersTinyUrlController.class);

    @Autowired
    UserService userService;

    @Autowired
    TinyurlService tinyurlService;

    @Autowired
    UsersTinyUrlService usersTinyUrlService;

    @Autowired
    VisitLogService visitLogService;

    @RequestMapping(value = "/user/tinyurl", method = {RequestMethod.GET, RequestMethod.POST})
    public String usersTinyurl(HttpSession session,
                               Model model) {
        User user = (User)session.getAttribute("user");
        if (user == null) {
            return "redirect:login";
        } else {
            String username = user.getUsername();
            int userId = userService.getByUsername(username).getId();
            //TODO
            List<UsersTinyUrlViewObject> lists = getUsersTinyUrlViewObject(userId, 500);
            model.addAttribute("usersTinyUrlViewObject", lists);
        }
        return "usersTinyUrl";
    }

    private List<UsersTinyUrlViewObject> getUsersTinyUrlViewObject(int userId, int limit) {
        List<UsersTinyUrlViewObject> result = new ArrayList<UsersTinyUrlViewObject>();
        List<UsersTinyUrl> lists = usersTinyUrlService.getTinyUrlByUserId(userId);
        int index = 0;
        if (lists != null && lists.size() > 0) {
            for (UsersTinyUrl usersTinyUrl : lists) {
                index++;
                if (index > limit) {
                    break;
                }
                int shortId = usersTinyUrl.getShortUrlId();
                String shortUrl = Constant.HOST + "/" + DecimalTransfer.idToShortUrl(shortId);
                String longUrl = tinyurlService.getUrlById(shortId).getUrl();
                String host = HostDecodeUtil.getHost(longUrl);
                VisitLog visitLog = visitLogService.getUrlCountTimeSection(host,
                        new Date(new Date().getTime() - 1000*60*60*24*365), new Date());
                if (visitLog != null) {
                    int count = visitLog.getCnt();

                    UsersTinyUrlViewObject viewObject = new UsersTinyUrlViewObject(shortUrl, host, count);
                    result.add(viewObject);
                }
            }
        }
        return result;
    }
}
