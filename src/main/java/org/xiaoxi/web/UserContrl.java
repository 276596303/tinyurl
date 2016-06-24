package org.xiaoxi.web;

import com.sun.org.glassfish.gmbal.ParameterNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xiaoxi.dto.TinyurlResult;
import org.xiaoxi.dto.Token;
import org.xiaoxi.entity.Tinyurl;
import org.xiaoxi.entity.User;
import org.xiaoxi.enums.DataCode;
import org.xiaoxi.enums.UserServiceState;
import org.xiaoxi.service.UserServiceInterface;

import java.util.concurrent.ExecutionException;

/**
 * Created by YanYang on 2016/6/23.
 */
@Controller
@RequestMapping(value = "/user")
public class UserContrl {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserContrl.class);

    @Autowired
    private UserServiceInterface userService;

    @RequestMapping(value = "/register/",
            method = RequestMethod.POST,
            produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public TinyurlResult userRegister(@RequestParam(value = "username")String username,
                                @RequestParam(value = "password")String password) {
        String token = "";
        TinyurlResult<Token> tinyurlResult = null;
        try {
            token = userService.insert(username, password);
            if (token == null || token.equals("")) {
                tinyurlResult = new TinyurlResult<Token>(false, UserServiceState.FAILURE.getInfo());
            } else {
                Token tk = new Token(username, token);
                tinyurlResult = new TinyurlResult<Token>(true, tk, DataCode.TOKEN.getCode(), DataCode.TOKEN.getDesc());
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

        }
        return tinyurlResult;
    }

    @RequestMapping(value = "/exist/",
            method = RequestMethod.POST,
            produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public boolean isExist(@RequestParam(value = "username")String username) {
        boolean isExist = true;
        try {
            if (userService.isExist(username)) {
                isExist = true;
            } else {
                isExist = false;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return isExist;
    }
}
