package kz.bsbnb.security;

import kz.bsbnb.util.SecurityConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

/**
 * Created by Olzhas.Pazyldayev on 12.05.2017.
 */
@Configuration
@EnableWebSecurity
@EnableJdbcHttpSession
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private SecurityConfigProperties securityConfigProperties;

    private String[] unfilteredUrl = {"/", "/token", "/login", "/register", "/signed-login", "/logout",
            "/user/login", "/user/registration/**", "/user/remind", "/user/verifyIIN/{iin}", "/public/*",
            "/vote/**", "/user/**" , "/voting/**", "/**", "/admin/**", "files/**", "/question/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // todo: synchronize with db
        http.sessionManagement()
                .and().cors()
                .and().authorizeRequests()
                .antMatchers(unfilteredUrl)
                .permitAll()
                //.antMatchers(frontUrl).access("hasIpAddress('" + securityConfigProperties.getFrontServerIp() + "') OR hasIpAddress('127.0.0.1')")
                //.antMatchers(cdcbUrl).access("hasIpAddress('" + securityConfigProperties.getCdcbServerIp() + "') OR hasIpAddress('" + securityConfigProperties.getFrontServerIp() + "') OR hasIpAddress('127.0.0.1')")
                .anyRequest().authenticated()
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/");

        if (securityConfigProperties.isCsrfToken()) {
            http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .and().addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
                    .addFilterAfter(new CustomSessionManagementFilter(unfilteredUrl), CsrfHeaderFilter.class);
        } else {
            http.csrf().disable().httpBasic();
        }
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService);
    }
}