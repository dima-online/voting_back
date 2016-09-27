package kz.bsbnb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@EnableConfigurationProperties
public class BlockChainApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlockChainApplication.class, args);
    }
}
