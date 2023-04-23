package top.liuzhuo.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import top.liuzhuo.constant.ResultCodeEnum;
import top.liuzhuo.vo.CommonVo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValueAsString(CommonVo.error(ResultCodeEnum.UNAUTHORIZED.getCode(),
                ResultCodeEnum.UNAUTHORIZED.getMessage()));
        httpServletResponse.getWriter().flush();
    }
}