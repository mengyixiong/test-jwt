package top.liuzhuo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @NotNull(message = "用户名不能为空")
    private String userName;

    @NotNull(message = "密码不能为空")
    private String passWord;
}
