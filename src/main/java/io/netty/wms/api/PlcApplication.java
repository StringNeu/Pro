package io.netty.wms.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 *  * class_name: PlcApplication
 *  * package: com.tst.plc
 *  * describe: 启动类
 *  * creat_user: yang.zhou@tst-cloud.com
 *  * creat_date: 2022/1/25 13:28
 *  * user: yzhou
 *  * Copyright 2020 tst-cloud.com
 *  
 **/
@SpringBootApplication()
public class PlcApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(PlcApplication.class, args);
    }

}
