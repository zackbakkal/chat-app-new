package com.zack.projects.chatapp.security;

import java.util.concurrent.TimeUnit;

import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.zack.projects.chatapp.service.ApplicationUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private ApplicationUserService applicationUserService;
	
	@Autowired
	private final PasswordEncoder passwordEncoder;

	@Autowired
	private CustomLogoutSuccessHandler customLogoutSuccessHandler;

	public SecurityConfig(ApplicationUserService applicationUserService, PasswordEncoder passwordEncoder) {
		super();
		this.applicationUserService = applicationUserService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
			.csrf().disable()
			.authorizeRequests()
			.antMatchers("/", "index", " /css/*", "/js/*", "/register", "/adduser").permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.formLogin()
				.loginPage("/login")
				.permitAll()
				.defaultSuccessUrl("/chatapp", true)
				.passwordParameter("password")
				.usernameParameter("username")
//				.failureHandler(authenticationFailureHandler())
				.failureUrl("/login-error")		
			.and()
			.rememberMe().tokenValiditySeconds((int)TimeUnit.DAYS.toSeconds(21))
				.key("usesecuredkey")
				.rememberMeParameter("remember-me")
			.and()
			.logout()
				.logoutUrl("/logout")
				.logoutSuccessHandler(customLogoutSuccessHandler)
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
				.clearAuthentication(true)
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID", "remember-me")
			;
	}
	
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/css/**", "/js/**");
	}
	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthProvider = new DaoAuthenticationProvider();
		
		daoAuthProvider.setPasswordEncoder(passwordEncoder);
		daoAuthProvider.setUserDetailsService(applicationUserService);
		
		return daoAuthProvider;
		
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(daoAuthenticationProvider());
	}
	
	@Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }
}
