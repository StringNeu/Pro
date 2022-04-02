### WMS对接WCS的api项目
#### 项目目录结构
- wms.api 主包
 - auth 身份验证
 - client 客户端
   - codec 编解码
 - common 基础包
 - keepalive 心跳
 - server 服务端
 - util 工具
 - wms 操作实现类
 
#### 启动方式
- 运行 Server 类 
- 运行 WmsClient 类
- 运行 WcsClient 类
#### jar包启动方式
- 运行 Server 类     
java -cp wms-tcp-api-1.0-SNAPSHOT.jar io.netty.wms.api.server.Server
- 运行 WmsClient 类  
java -cp wms-tcp-api-1.0-SNAPSHOT.jar io.netty.wms.api.client.WmsClient
- 运行 WcsClient 类
java -cp wms-tcp-api-1.0-SNAPSHOT.jar io.netty.wms.api.client.WcsClient

#### 设计方案
- 采用自定义长度解决Tcp粘包和半包问题
1 使用LengthFieldBasedFrameDecoder方式处理粘包和半包
https://blog.csdn.net/liyantianmin/article/details/85603347
配置为lengthFieldOffset 0 ; lengthFieldLength 2; lengthAdjustment 0 ; initialBytesToStrip 2;
例如：数据包长度(14) - lengthFieldOffset - lengthFieldLength - 长度域的值(12) 
- 增加MessageHeader和MessageBody数据结构
1) MessageHeader 包含version，streamId,opCode三个字段要按顺序进行编码
2) MessageBody 是具体的请求字段类，采用json形式
