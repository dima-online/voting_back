package kz.bsbnb;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Locale;

@SpringBootApplication
@EnableSwagger2

@Configuration
@EnableAutoConfiguration
@EnableScheduling
public class BlockChainApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlockChainApplication.class, args);
        Locale.setDefault(Locale.ENGLISH);
        //Сделано для инициализации сертификатов
        //CryptUtil.signXML("<test></test>","C:\\Users\\Serik.Mukashev\\Documents\\voting\\RSA256_cff149024ec3c1bc05abb2bf7a4780e29ee692bc.p12","123456");
    }

    @Bean
    public Docket swaggerSettings() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("kz.bsbnb"))
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/");
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("locale");
        return lci;
    }
    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames("locale/messages");
        source.setDefaultEncoding("UTF-8");
        source.setUseCodeAsDefaultMessage(true);
        return source;
    }

}
