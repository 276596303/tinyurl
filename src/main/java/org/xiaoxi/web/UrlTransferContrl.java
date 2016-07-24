package org.xiaoxi.web;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.xiaoxi.async.EventModel;
import org.xiaoxi.async.EventProducer;
import org.xiaoxi.async.EventType;
import org.xiaoxi.entity.User;
import org.xiaoxi.enums.DataCode;
import org.xiaoxi.enums.TinyurlStateEnum;
import org.xiaoxi.service.TinyurlService;
import org.xiaoxi.service.UserService;
import org.xiaoxi.utils.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by YanYang on 2016/6/24.
 */
@Controller
public class UrlTransferContrl {
    private static final Logger LOGGER = LoggerFactory.getLogger(UrlTransferContrl.class);

    @Autowired
    TinyurlService tinyurlService;

    @Autowired
    UserService userService;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    VisitLogAsyncUtil visitLogAsyncUtil;

    @Autowired
    BadUrlUtil badUrlUtil;

    @RequestMapping(value = "/short",
            method = RequestMethod.POST,
            produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public String getTinyUrl(@RequestParam(value = "username")String username,
                             @RequestParam(value = "password")String password,
                             @RequestParam(value = "longUrl")String long_url,
                             HttpServletRequest httpServletRequest,
                             HttpSession session) {

        if (long_url == null || long_url.trim().equals("")) {
            return JSONUtil.getJSONString(0, "网址不能为空");
        }

        //获取域名
        String host = httpServletRequest.getHeader("host");
        try {
            User usr = (User)session.getAttribute("user");
            if (usr != null) {
                int userId = usr.getId();
                if (badUrlUtil.containsBadUrl(long_url)) {
                    return JSONUtil.getJSONString(0, "此网址涉嫌敏感信息");
                }
                String short_url = tinyurlService.transferToShort_url(long_url);
                if (short_url != null) {

                    //TODO 异步把用户转化的 tinyurl 关联到用户
                    Map<String, Object> ext = new HashMap<String, Object>();
                    ext.put("userId", userId);
                    ext.put("shortId", DecimalTransfer.shortUrlToID(short_url));
                    ext.put("createTime", new Date().getTime());
                    eventProducer.fireEvent(new EventModel(EventType.USERS_TINYURL)
                            .setExt(ext));

                    //拼接完整的短网址
                    short_url = "http://" + host + "/" + short_url;

                    return JSONUtil.getJSONString(1, short_url);
                } else {
                    return JSONUtil.getJSONString(0, "转换为短网址失败");
                }
            } else {
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                int userId = userService.validUser(user);
                if (userId != Integer.MIN_VALUE && userId > 0) {
                    User usr1 = new User();
                    usr1.setUsername(username);
                    usr1.setPassword(password);
                    usr1.setId(userId);
                    session.setAttribute("user", usr1);
                    session.setMaxInactiveInterval(60*60*24);
                    if (badUrlUtil.containsBadUrl(long_url)) {
                        return JSONUtil.getJSONString(0, "此网址涉嫌敏感信息");
                    }
                    String short_url = tinyurlService.transferToShort_url(long_url);
                    if (short_url != null) {

                        Map<String, Object> ext = new HashMap<String, Object>();
                        ext.put("userId", userId);
                        ext.put("shortId", DecimalTransfer.shortUrlToID(short_url));
                        ext.put("createTime", new Date().getTime());
                        eventProducer.fireEvent(new EventModel(EventType.USERS_TINYURL)
                                .setExt(ext));

                        //拼接完整的短网址
                        short_url = "http://" + host + "/" + short_url;

                        return JSONUtil.getJSONString(1, short_url);
                    } else {
                        return JSONUtil.getJSONString(0, "转换为短网址失败");
                    }
                } else {
                    return JSONUtil.getJSONString(0, "核查用户名和密码");
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return JSONUtil.getJSONString(0, "系统异常");
        }
    }

    @RequestMapping(value = "/{shortUrl}",
            method = RequestMethod.GET,
            produces = {"application/json;charset=utf-8"})

    public void getLongUrl(@PathVariable(value = "shortUrl")String short_url,
                             HttpServletResponse response) {
        response.setStatus(302);
        try {
            if (short_url.length() > 7) {
                return;
            }

            String long_url = tinyurlService.transferToLong_url(short_url);

            if (long_url != null) {

                // TODO 异步添加访问日志
                String host = HostDecodeUtil.getHost(long_url);
                if (host != null) {
                    long HALF_DAY = 1000 * 60 * 60 * 12;  // 12小时 （单位毫秒）
                    visitLogAsyncUtil.addToBlockingQ(host);   // 此方法中已进行了异步处理
                }

                int httpPos = long_url.indexOf("http://");
                int httpsPos = long_url.indexOf("https://");
                if (httpPos < 0 && httpsPos < 0) {
                    long_url = "http://" + long_url;
                }
                response.sendRedirect(long_url);
            } else {
//                tinyurlResult = new TinyurlResult(false, TinyurlStateEnum.TRANSFER_FAILURE.getStateInfo());
                return;
            }
        } catch (Exception e) {
            return;
        }
    }
}
