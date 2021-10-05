package com.example.mall.controller.admin;

import com.example.mall.common.Constants;
import com.example.mall.common.ServiceResultEnum;
import com.example.mall.controller.vo.UserVO;
import com.example.mall.entity.Article;
import com.example.mall.entity.User;
import com.example.mall.mapper.MallUserMapper;
import com.example.mall.service.UserService;
import com.example.mall.util.MD5Util;
import com.example.mall.util.PageQueryUtil;
import com.example.mall.util.Result;
import com.example.mall.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 *
 */
@Controller
@RequestMapping("/admin")
public class UserController {

    @Autowired
    private UserService mallUserService;

    @Autowired
    private MallUserMapper userMapper;

    @RequestMapping(method = RequestMethod.GET, value = "/users")
    public String usersPage(HttpServletRequest request) {
        request.setAttribute("path", "users");
        request.setAttribute("userType", Constants.USER_ROLE);
        return "admin/user";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/admins")
    public String adminsPage(HttpServletRequest request) {
        request.setAttribute("path", "admins");
        request.setAttribute("userType", Constants.ADMIN_ROLE);
        return "admin/admin";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/users/list", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        params.put("userType", Constants.USER_ROLE);
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(mallUserService.getMallUsersPage(pageUtil));
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/admins/list", method = RequestMethod.GET)
    @ResponseBody
    public Result adminsList(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        params.put("userType", Constants.ADMIN_ROLE);
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(mallUserService.getMallUsersPage(pageUtil));
    }

    /**
     * 用户禁用与解除禁用(0-未锁定 1-已锁定)
     */
    @RequestMapping(value = "/users/lock/{lockStatus}", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids, @PathVariable int lockStatus) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (lockStatus != 0 && lockStatus != 1) {
            return ResultGenerator.genFailResult("操作非法！");
        }
        if (mallUserService.lockUsers(ids, lockStatus)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("禁用失败");
        }
    }

    /**
     * 添加/更新
     */
    @RequestMapping(value = "/users/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(@RequestBody User user, HttpSession session) {

        if (user.getUserId() == null) {
            User check = userMapper.selectByLoginName(user.getLoginName());
            if (check != null) {
                return ResultGenerator.genFailResult("账号已存在");
            }

            UserVO mallUser = (UserVO) session.getAttribute("mallUser");
            if (mallUser == null) {
                return ResultGenerator.genFailResult("用户未登录");
            }
            String passwordMD5 = MD5Util.MD5Encode(user.getPasswordMd5(), "UTF-8");
            user.setPasswordMd5(passwordMD5);
            user.setIsDeleted(0);
            user.setLockedFlag(0);
            userMapper.insertSelective(user);
        } else {
            User check = userMapper.selectByLoginName(user.getLoginName());
            if (check != null && !Objects.equals(check.getUserId(), user.getUserId())) {
                return ResultGenerator.genFailResult("账号已存在");
            }
            if (!StringUtils.isEmpty(user.getPasswordMd5())) {
                String passwordMD5 = MD5Util.MD5Encode(user.getPasswordMd5(), "UTF-8");
                user.setPasswordMd5(passwordMD5);
            }
            userMapper.updateByPrimaryKeySelective(user);
        }
        return ResultGenerator.genSuccessResult();
    }

}