package io.netty.wms.api.auth;

import io.netty.wms.api.common.Operation;
import io.netty.wms.api.common.OperationResult;
import lombok.Data;
import lombok.extern.java.Log;

/**
 * 校验方法
 */
@Data
@Log
public class AuthOperation extends Operation {

    private final String userName;
    private final String password;
    //非final，@Data不会自动构建构造方法
    private String simpleUserName = "admin";


    @Override
    public OperationResult execute() {
        if (simpleUserName.equalsIgnoreCase(this.userName)) {
            AuthOperationResult authOperationResponse = new AuthOperationResult(true);
            return authOperationResponse;
        }
        return new AuthOperationResult(false);
    }
}