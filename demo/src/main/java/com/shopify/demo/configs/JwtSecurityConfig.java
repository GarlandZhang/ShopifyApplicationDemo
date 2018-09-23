package com.shopify.demo.configs;

import com.shopify.demo.security.JwtAuthenticationEntryPoint;
import com.shopify.demo.security.JwtAuthenticationProvider;
import com.shopify.demo.security.JwtAuthenticationTokenFilter;
import com.shopify.demo.security.JwtSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration
public class JwtSecurityConfig extends WebSecurityConfigurerAdapter {
    // WebSecurityConfigurerAdapter bootstraps automatically by bootstrap
    // here we are overriding default security configurations

    @Autowired
    private JwtAuthenticationProvider authenticationProvider;

    @Autowired
    private JwtAuthenticationEntryPoint entryPoint;

    // creates custom authentication manager, which requires custom Authentication Provider
    @Bean
    public AuthenticationManager authenticationManager() {

        return new ProviderManager(Collections.singletonList(authenticationProvider));
    }

    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilter() {

        JwtAuthenticationTokenFilter filter = new JwtAuthenticationTokenFilter();
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(new JwtSuccessHandler()); // custom success handler,
        // to redirect request to this handler to do further processing

        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeRequests().antMatchers("**/rest/**").authenticated() //which endpoints need auth
                .and()
                .exceptionHandling().authenticationEntryPoint(entryPoint) //redirect when user access unauthorized endpoint
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //makes session stateless so there is no re

        http.addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class); //before the built-in spring filter is applied, load our custom filter first

        http.headers().cacheControl();
    }
}
