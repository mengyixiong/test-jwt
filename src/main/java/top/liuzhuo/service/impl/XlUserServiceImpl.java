package top.liuzhuo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.liuzhuo.mapper.XlUserMapper;
import top.liuzhuo.pojo.XlResource;
import top.liuzhuo.pojo.XlUser;
import top.liuzhuo.service.XlUserService;
import top.liuzhuo.utils.JwtTokenUtil;

import javax.annotation.Resource;
import java.util.List;

@Service
public class XlUserServiceImpl implements XlUserService {

    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private XlUserMapper xlUserMapper;

    @Override
    public XlUser getUserByCode(String username) {
        return null;
    }


    @Override
    public XlUser register(XlUser user) {
        XlUser newXlUser = new XlUser();
        BeanUtils.copyProperties(user, newXlUser);

        //查询是否有相同用户名的用户
        List<XlUser> xlUsers = xlUserMapper.selectList(new LambdaQueryWrapper<XlUser>().eq(XlUser::getUserCode,newXlUser.getUserCode()));
        if (!CollectionUtils.isEmpty(xlUsers)) {
            throw new RuntimeException("用户名已存在");
        }
        //将密码进行加密操作
        String encodePassword = passwordEncoder.encode(user.getPassWord());
        newXlUser.setPassWord(encodePassword);
        xlUserMapper.insert(newXlUser);
        return newXlUser;
    }

    @Override
    public String login(String username, String password) throws AuthenticationException {
        String token = null;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("密码不正确");
        }
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        token = jwtTokenUtil.generateToken(userDetails);
        return token;
    }

    @Override
    public List<XlResource> getResourceList(Long userId) {
        return null;
    }


}
