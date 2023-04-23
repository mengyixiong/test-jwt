package top.liuzhuo.pojo;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class JwtUser implements UserDetails {

    private XlUser user;
    private List<XlResource> resourceList;
    
    public JwtUser(XlUser user, List<XlResource> resourceList) {
        this.user = user;
        this.resourceList = resourceList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //返回当前用户的权限
        return resourceList.stream()
                .map(resource ->new SimpleGrantedAuthority(resource.getResourceId()+":"+resource.getResourceName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassWord();
    }

    @Override
    public String getUsername() {
        return user.getUserCode();
    }
    /**
     * 账户是否未过期
     **/
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    /**
     * 账户是否未锁定
     **/
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    /**
     * 密码是否未过期
     **/
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    /**
     * 账户是否激活
     **/
    @Override
    public boolean isEnabled() {
        return user.getStatus().equals(1);
    }
}