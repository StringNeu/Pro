package io.netty.example.study.auth;

import io.netty.example.study.common.OperationResult;
import lombok.Data;

@Data
public class AuthOperationResult extends OperationResult {

    //@Data，能够根据private final 属性自动创建构造对象
    private final boolean passAuth;

}