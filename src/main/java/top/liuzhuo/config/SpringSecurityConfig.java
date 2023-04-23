package top.liuzhuo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import top.liuzhuo.filter.JwtAuthenticationTokenFilter;
import top.liuzhuo.handler.CustomAccessDeniedHandler;
import top.liuzhuo.handler.CustomAuthenticationEntryPoint;
import top.liuzhuo.pojo.JwtUser;
import top.liuzhuo.pojo.XlResource;
import top.liuzhuo.pojo.XlUser;
import top.liuzhuo.service.XlUserService;

import javax.annotation.Resource;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Resource
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Resource
    private XlUserService xlUserService;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()// 由于使用的是JWT，我们这里不需要csrf
                .sessionManagement()// 基于token，所以不需要session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 允许对于网站静态资源的无授权访问
                .antMatchers(HttpMethod.GET,
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/swagger-resources/**",
                        "/v2/api-docs/**"
                )
                .permitAll()
                // 对登录注册要允许匿名访问
                .antMatchers("/ucenter/xl-user/login", "/ucenter/xl-user/register")
                .permitAll()
                //跨域请求会先进行一次options请求
                .antMatchers(HttpMethod.OPTIONS)
                .permitAll()
                .anyRequest()// 除上面外的所有请求全部需要鉴权认证
                .authenticated();
        // 禁用缓存
        httpSecurity.headers().cacheControl();
        // 添加JWT filter
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        //添加自定义未授权和未登录结果返回
        httpSecurity.exceptionHandling()
                .accessDeniedHandler(customAccessDeniedHandler)
                .authenticationEntryPoint(customAuthenticationEntryPoint);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        //获取登录用户信息
        return username -> {
            XlUser user = xlUserService.getUserByCode(username);
            if (user != null) {
                List<XlResource> permissionList = xlUserService.getResourceList(user.getUserId());
                return new JwtUser(user,permissionList);
            }
            throw new UsernameNotFoundException("用户名或密码错误");
        };
    }

    /**
     * 装载BCrypt密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * JWT filter
     */
    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(){
        return new JwtAuthenticationTokenFilter();
    }
}