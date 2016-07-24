package org.xiaoxi.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.xiaoxi.entity.User;

import javax.servlet.http.HttpSession;

/**
 * Created by YanYang on 2016/7/23.
 */
@Controller
public class IndexController {

    @RequestMapping(value = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(HttpSession session,
                        Model model) {

        User user = (User)session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }
        return "index";
    }

    @RequestMapping(value = {"/route/login/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String loginPage() {
        return "login";
    }

    @RequestMapping(value = {"/route/register/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String registerPage() {
        return "register";
    }
}
