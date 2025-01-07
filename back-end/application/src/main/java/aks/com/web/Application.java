package aks.com.web;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Indexed;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Arrays;


/**
 * @author xxl
 * @since 2023/9/16
 */
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@Slf4j
@Indexed
@MapperScan("**.mapper")
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        if (args.length < 1 || Arrays.stream(args).noneMatch(t -> t.contains("mpw.key"))) {
            throw new RuntimeException("缺少密钥");
        }
        try {
            SpringApplication.run(Application.class, args);
            log.info("项目启动成功(ง ˙o˙)ว");
        } catch (Exception e) {
            log.error("启动失败:",e);
        }
    }


}
