package org.jerry.code.toolkit.constant;

/**
 * @PackageName: com.lzww.cd.pay.backend.constant
 * @ClassName: CommonConstant
 * @Description: 常用常量类
 * @Author: chenl
 * @Date: 2023/4/10 14:43
 */
public interface CommonConstant {


    /**
     * {@code 500 Server Error} (HTTP/1.0 - RFC 1945)
     */
    Integer SC_INTERNAL_SERVER_ERROR_500 = 500;
    /**
     * {@code 200 OK} (HTTP/1.0 - RFC 1945)
     */
    Integer SC_OK_200 = 200;

    /**
     * 访问权限认证未通过 510
     */
    Integer SC_JEECG_NO_AUTHZ = 510;

}
