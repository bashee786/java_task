package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
	@EnableWebSecurity
	@EnableGlobalMethodSecurity(prePostEnabled = true)
	public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

		private final static String NON_ADMIN = "NON_ADMIN";
		private final static String ADMIN = "ADMIN";
		@Autowired
		private JwtTokenProvider tokenProvider;

		@Bean
		public PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}
		
		@Bean
		@Override
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return super.authenticationManagerBean();
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.cors();
			http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
					.authorizeRequests().antMatchers("/user/*").permitAll()
					.antMatchers("/add/book").hasAuthority(ADMIN)
					.antMatchers("/delete/book").hasAuthority(ADMIN)
					.antMatchers("/view/book").hasAuthority(ADMIN)
					
					.antMatchers("/search/book").hasAnyAuthority(ADMIN, NON_ADMIN)
					.antMatchers("/issued/books").hasAnyAuthority(ADMIN, NON_ADMIN)
					
					.anyRequest().authenticated();
			http.apply(new JwtTokenConfigurer(tokenProvider));
		}
}
