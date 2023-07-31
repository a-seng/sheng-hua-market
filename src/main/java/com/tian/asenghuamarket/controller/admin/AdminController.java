package com.tian.asenghuamarket.controller.admin;


import com.tian.asenghuamarket.Dto.AdminUser;
import com.tian.asenghuamarket.common.ServiceResultEnum;
import com.tian.asenghuamarket.mapper.AdminUserMapper;
import com.tian.asenghuamarket.service.AdminUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.signature.qual.PolySignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminUserService adminUserService;

    @Autowired
    private AdminUserMapper adminUserMapper;

    @GetMapping({"/login"})
    public String login(){
        return "admin/login";
    }

    @GetMapping({"","/","/index","/index.html"})
    public String index(HttpServletRequest request){
        request.setAttribute("path","index");
        return "admin/index";
    }

    @PostMapping(value = "/login")
    public String login(@RequestParam("userName")String userName,
                        @RequestParam("password")String password,
                        @RequestParam("verifyCode")String verifyCode,
                        HttpSession session){
        if(StringUtils.isEmpty(verifyCode)){
            session.setAttribute("errorMsg","验证码不能胃口");
            return "admin/login";
        }
        if(StringUtils.isEmpty(userName)||StringUtils.isEmpty(password)){
            session.setAttribute("errorMsg","用户名或密码不能为空");
            return "admin/login";
        }

        String kaptchaCode = session.getAttribute("vertifyCode")+"";
        if(!StringUtils.equalsIgnoreCase(kaptchaCode,verifyCode)){
            session.setAttribute("errorMsg","验证码错误");
            return "admin/login";
        }

        AdminUser adminUser = adminUserService.login(userName,password);
        if(adminUser!=null){
            session.setAttribute("loginUser",adminUser.getNickName());
            session.setAttribute("loginUserId",adminUser.getAdminUserId());
            return "redirect:/admin/index";
        }else {
            session.setAttribute("errorMsg","登录失败");
            return "admin/login";
        }
    }

    @GetMapping("/profile")
    public String profile(HttpServletRequest request){
        Integer loginUserId = (Integer) request.getSession().getAttribute("loginUserId");
        AdminUser adminUser = adminUserMapper.selectById(loginUserId);
        if(adminUser==null){
            return "admin/login";
        }
        request.setAttribute("path","profile");
        request.setAttribute("loginUserName",adminUser.getLoginUserName());
        request.setAttribute("nickNamme",adminUser.getNickName());
        return "amdin/profile";
    }

    /**
     *更新密码
     */
    @PostMapping("/profile/password")
    @ResponseBody
    public String passwordUpdate(HttpServletRequest request, @RequestParam("originalPassword") String originalPassword,
                                 @RequestParam("newPassword") String newPassword) {
            if(StringUtils.isEmpty(originalPassword) || StringUtils.isEmpty(newPassword)){
                return "参数不能为空";
            }
            Integer loginUserId=(int)request.getSession().getAttribute("loginUserId");
            if(adminUserService.updatePassword(loginUserId,originalPassword,newPassword)){
                //修改成功后清空session中的数据，前端控制跳转至登录页
                request.getSession().removeAttribute("loginUserId");
                request.getSession().removeAttribute("loginUser");
                request.getSession().removeAttribute("errorMsg");
                return ServiceResultEnum.SUCCESS.getResult();
            }else{
                return "修改失败";
            }
    }

    @PostMapping("/profile/name")
    @ResponseBody
    public String nameUpdate(HttpServletRequest request,
                             @RequestParam("loginUserName")String loginUserName,
                             @RequestParam("nickName")String nickName){
        if(StringUtils.isEmpty(loginUserName)|| StringUtils.isEmpty(nickName)){
            return "参数不能为空";
        }
        Integer loginUserId=(int) request.getSession().getAttribute("loginUserId");
        if(adminUserService.updateName(loginUserId,loginUserName,nickName)){
            return ServiceResultEnum.SUCCESS.getResult();
        }else{
            return "修改失败";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().removeAttribute("loginUserId");
        request.getSession().removeAttribute("loginUser");
        request.getSession().removeAttribute("errorMsg");
        return "admin/login";
    }

}
