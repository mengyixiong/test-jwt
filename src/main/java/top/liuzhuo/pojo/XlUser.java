package top.liuzhuo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class XlUser {
    private Long userId;
    private String userCode;
    private String passWord;
    private Integer status;
}
