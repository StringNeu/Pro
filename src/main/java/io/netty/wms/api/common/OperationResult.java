package io.netty.wms.api.common;

import lombok.Data;

/**
 * 操作结果
 * lombok的@Data放在抽象类，子类能够继承特性
 */
@Data
public abstract class OperationResult extends MessageBody {
}