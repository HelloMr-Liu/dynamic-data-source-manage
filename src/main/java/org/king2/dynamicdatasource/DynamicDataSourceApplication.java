package org.king2.dynamicdatasource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * 描述：动态操作数据源启动类
 * @author 刘梓江
 * @date 2021/5/19 13:54
 */
@EnableScheduling
@SpringBootApplication
public class DynamicDataSourceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DynamicDataSourceApplication.class, args);
    }
}
