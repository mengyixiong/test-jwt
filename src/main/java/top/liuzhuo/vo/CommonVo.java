package top.liuzhuo.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonVo<T> {
    private Integer code;

    private String msg;

    private T data;

    public static <T> CommonVo<T> success(String msg, T data) {
        return new CommonVo(0, "成功", data);
    }

    public static CommonVo error(Integer code, String msg) {
        return new CommonVo(code, msg, null);
    }
}
