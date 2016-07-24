package org.xiaoxi.web;

import com.sun.javafx.sg.prism.NGShape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xiaoxi.entity.User;
import org.xiaoxi.enums.DataCode;
import org.xiaoxi.enums.UserServiceState;
import org.xiaoxi.service.UserService;
import org.xiaoxi.utils.JSONUtil;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by YanYang on 2016/6/23.
 */
@Controller
@RequestMapping(value = "/user")
public class UserContrl {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserContrl.class);

    @Autowired
    UserService userService;

    @RequestMapping(value = "/register",
            method = RequestMethod.POST,
            produces = {"application/json;charset=utf-8"})
    public String userRegister(@RequestParam(value = "username")String username,
                               @RequestParam(value = "password")String password,
                               HttpSession session,
                               Model model) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        Map<String, String> map = userService.register(user);
        if (map.containsKey("usernameMessage")) {
            model.addAttribute("usernameMessage", map.get("usernameMessage").toString());
            return "register";
        }
        if (map.containsKey("passwordMessage")) {
            model.addAttribute("passwordMessage", map.get("passwordMessage").toString());
            return "register";
        }
        if (map.containsKey("success")) {
            if (Integer.valueOf(map.get("success")) > 0) {
                User user1 = new User();
                user1.setUsername(username);
                user1.setPassword(password);
                user1.setId(Integer.valueOf(map.get("success")));
                session.setAttribute("user", user1);
                session.setMaxInactiveInterval(60 * 60 * 24);
                model.addAttribute("user", user1);
                return "index";
            }
        }
        model.addAttribute("exception", "注册异常");
        return "register";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST,
            produces = {"application/json;charset=utf-8"})
    public String login(HttpSession session,
                        Model model,
                        @RequestParam("username")String username,
                        @RequestParam("password")String password) {
        Object user = session.getAttribute("user");
        if (user != null && username.equals(((User)user).getUsername())
                && password.equals(((User)user).getPassword())){
            session.setMaxInactiveInterval(60*60*24);
            model.addAttribute("user", user);
            return "redirect:/index";
        } else {
            try {
                User usr = new User();
                usr.setUsername(username);
                usr.setPassword(password);
                int userId = userService.validUser(usr);
                if (userId != Integer.MIN_VALUE && userId > 0) {
                    usr.setId(userId);
                    session.setAttribute("user", usr);
                    session.setMaxInactiveInterval(60*60*24);
                    model.addAttribute("user", usr);
                    return "redirect:/index";
                }
            } catch (Exception e) {
                LOGGER.error("验证用户异常");
                return "redirect:index";
            }
            return "redirect:/index";
        }
    }

    @RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "index";
    }

    @RequestMapping(value = "/exist",
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
