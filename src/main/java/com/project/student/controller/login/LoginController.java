package com.project.student.controller.login;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.project.student.common.utils.JwtUtil;
import com.project.student.common.utils.PasswordUtils;
import com.project.student.config.utils.RedisUtils;
import com.project.student.config.utils.ShiroUtils;
import com.project.student.domain.*;
import com.project.student.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @description: 登陆
 * @date 2024/2/26 21:20
 */
@Controller
@ResponseBody
@RequestMapping("login")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private StudentCollegeService studentCollegeService;

    @PostMapping()
    public Result login(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
        String username = jsonObject.getString("loginAccount");
        String password = jsonObject.getString("password");
        Integer type = jsonObject.getInteger("type");
        QueryWrapper<User> query = new QueryWrapper<>();
        query.lambda().eq(User::getLoginAccount,username);
        User user = userService.getOne(query);
        if (user == null) {
            return Result.fail("用户名不存在！");
        }
        //比较加密后得密码
        boolean decrypt = PasswordUtils.decrypt(password, user.getPassword() + "$" + user.getSalt());
        if (!decrypt) {
            return Result.fail("用户名或密码错误！");
        }
        if (user.getStatus() == 1) {
            return Result.fail("用户被禁用！");
        }
        //密码正确生成token返回
        String token = JwtUtil.sign(user.getId(), user.getPassword());
        JSONObject json = new JSONObject();
        json.put("token", token);
        return Result.success(json);
    }

    @GetMapping("logout")
    public Result logout() {
        return Result.success();
    }

    @GetMapping("getManageData")
    public Result getManageData() {
        JSONObject jsonObject = new JSONObject();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUserType,2);
        int teacher = userService.count(queryWrapper);
        jsonObject.put("teacher",teacher);
        QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.lambda().eq(User::getUserType,1);
        int student = userService.count(queryWrapper1);
        jsonObject.put("student",student);
        //获取各学院教师数量
        List<StudentCollege> collegeList = studentCollegeService.list();
        List<JSONObject> list = new ArrayList<>();
        List<JSONObject> list1 = new ArrayList<>();
        for (StudentCollege studentCollege : collegeList) {
            JSONObject object = new JSONObject();
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(User::getCollegeId,studentCollege.getId())
                    .eq(User::getUserType,2);
            int count = userService.count(wrapper);
            object.put("name",studentCollege.getName());
            object.put("value",count);
            list.add(object);
        }
        for (StudentCollege studentCollege : collegeList) {
            JSONObject object = new JSONObject();
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(User::getCollegeId,studentCollege.getId()).eq(User::getGraduation,0)
                    .eq(User::getUserType,1);
            int count = userService.count(wrapper);
            object.put("name",studentCollege.getName());
            object.put("value",count);
            list1.add(object);
        }
        jsonObject.put("teacherData",list);
        jsonObject.put("studentData",list1);
        return Result.success(jsonObject);
    }

}
