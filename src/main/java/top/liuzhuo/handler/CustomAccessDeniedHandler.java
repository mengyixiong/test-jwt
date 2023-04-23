package top.liuzhuo.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import top.liuzhuo.constant.ResultCodeEnum;
import top.liuzhuo.vo.CommonVo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {

        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValueAsString(CommonVo.error(ResultCodeEnum.FORBIDDEN.getCode(),
                ResultCodeEnum.FORBIDDEN.getMessage()));
        httpServletResponse.getWriter().flush();
    }
}