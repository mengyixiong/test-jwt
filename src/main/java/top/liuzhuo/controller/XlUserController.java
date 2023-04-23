package top.liuzhuo.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import top.liuzhuo.dto.LoginDto;
import top.liuzhuo.pojo.XlResource;
import top.liuzhuo.pojo.XlUser;
import top.liuzhuo.service.XlUserService;
import top.liuzhuo.vo.CommonVo;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class XlUserController {

    @Resource
    private XlUserService xlUserService;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @PostMapping(value = "register")
    public CommonVo register(@RequestBody XlUser user) {
        XlUser xlUser = xlUserService.register(user);
        return CommonVo.success("注册成功",xlUser);
    }

    @PostMapping(value = "login")
    public CommonVo login(@RequestBody LoginDto loginVo) {
        String token = xlUserService.login(loginVo.getUserName(), loginVo.getPassWord());
        if (StringUtils.isBlank(token)) {
            return CommonVo.error(500,"用户名或密码错误");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return CommonVo.success("登录成功", tokenMap);
    }

    @GetMapping(value = "/resource/{userId}")
    public CommonVo getResourceList(@PathVariable Long userId) {
        List<XlResource> permissionList = xlUserService.getResourceList(userId);
        return CommonVo.success("'获取成功", permissionList);
    }
}
