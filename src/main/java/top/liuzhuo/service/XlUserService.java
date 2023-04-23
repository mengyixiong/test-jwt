package top.liuzhuo.service;

import top.liuzhuo.pojo.XlResource;
import top.liuzhuo.pojo.XlUser;

import java.util.List;

public interface XlUserService {
    XlUser getUserByCode(String username);


    /**
     * 注册功能
     * @param user
     * @return XlUser
     */
    XlUser register(XlUser user);

    /**
     * 登录功能
     * @param username 用户名
     * @param password 密码
     * @return 生成的JWT的token
     */
    String login(String username, String password);

    /**
     * 获取用户所有可访问的资源
     * @param userId 用户id
     * @return list
     */
    List<XlResource> getResourceList(Long userId);

}
