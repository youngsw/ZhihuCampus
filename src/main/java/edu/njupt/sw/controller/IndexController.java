package edu.njupt.sw.controller;

import com.sun.deploy.net.HttpResponse;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/*关闭该入口*/
//@Controller
public class IndexController {

    @RequestMapping("/")
    @ResponseBody
    public String index(HttpSession httpSession) {
        return "Hello " + httpSession.getAttribute("msg");  //接收跳转消息
    }


    /**
     * @param model
     * @return
     * @autho sw
     * @see 路径参数和访问参数
     */
    @RequestMapping("/profile/{groupID}/{userID}")
    @ResponseBody
    public String profile(@PathVariable("userID") int userID,
                          @PathVariable("groupID") String groupID,
                          @RequestParam(value = "type", defaultValue = "1") int type,
                          @RequestParam(value = "key", required = false) String key) {

        return String.format("profile Page of %s / %d ,type:%d key:%s", groupID, userID, type, key);
    }


    @RequestMapping("/vm")
    public String pro(Model model) {
        model.addAttribute("value", "value");

        List<String> colors = Arrays.asList("RED", "GREEN", "YELLOW");
        model.addAttribute("colors", colors);

        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 4; i++) {
            map.put(String.valueOf(i), String.valueOf(i * i));
        }
        model.addAttribute("map", map);

        return "home";
    }

    /**
     * 请求和回复
     *
     * @param model
     * @param response
     * @param request
     * @param httpSession
     * @param sessionID
     * @return
     */
    @RequestMapping(path = {"/request"}, method = {RequestMethod.GET})
    @ResponseBody
    public String request(Model model,
                          HttpServletResponse response,
                          HttpServletRequest request,
                          HttpSession httpSession,
                          @CookieValue("JSESSIONID") String sessionID
    ) {
        StringBuilder sb = new StringBuilder();

        sb.append("Cookievalue:" + sessionID);

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            sb.append(name + ":" + request.getHeader(name) + "<br>");
        }

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                sb.append("Cooloe:" + cookie.getName() + "value" + cookie.getValue());
            }
        }
        sb.append(request.getMethod() + "<br>");
        sb.append(request.getQueryString() + "<br>");
        sb.append(request.getPathInfo() + "<br>");
        sb.append(request.getRequestURI() + "<br>");

        response.addHeader("sw", "hello");
        response.addCookie(new Cookie("username", "sw"));

        return sb.toString();
    }

    @RequestMapping(path = {"/redirect/{code}"}, method = RequestMethod.GET)
    public RedirectView redircet(@PathVariable("code") int code,
                                 HttpSession httpSession) {
        RedirectView red = new RedirectView("/", true);
        if (code == 301) {
            red.setStatusCode(HttpStatus.MULTI_STATUS);
        }
        httpSession.setAttribute("msg", "jump from redirect");
        return red;   //跳转回首页
    }


    @RequestMapping(path = {"/admin"}, method = {RequestMethod.GET})
    @ResponseBody
    public String admin(@RequestParam("key") String key) {
        if ("admin".equals(key)) {
            return "hello admin";
        }
        throw new IllegalArgumentException("参数不对");
    }

    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e) {
        return "error:" + e.getMessage();
    }

    @RequestMapping(path = {"/demo"})
    public String demo() {
        String s = "detail";
        return s;
    }
}
