package com.autobots.automanager.configuracao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.autobots.automanager.adaptadores.UserDetailsServiceImpl;
import com.autobots.automanager.filtros.Autenticador;
import com.autobots.automanager.filtros.Autorizador;
import com.autobots.automanager.jwt.ProvedorJwt;
import com.autobots.automanager.servicos.autenticacao.AutenticacaoUsuarioServico;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Seguranca extends WebSecurityConfigurerAdapter {

	private static final String[] ROTAS_PUBLICAS = {
			"/login",
			"/swagger-ui.html",
			"/swagger-ui/**",
			"/api-docs/**",
			"/v3/api-docs/**"
	};

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private ProvedorJwt provedorJwt;

	@Autowired
	private AutenticacaoUsuarioServico autenticacaoUsuarioServico;

	@Autowired
	private SegurancaAuthenticationEntryPoint authenticationEntryPoint;

	@Autowired
	private SegurancaAccessDeniedHandler accessDeniedHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable();
		http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
				.accessDeniedHandler(accessDeniedHandler);
		http.authorizeRequests().antMatchers(ROTAS_PUBLICAS).permitAll().anyRequest().authenticated();
		http.addFilter(new Autenticador(authenticationManager(), provedorJwt, autenticacaoUsuarioServico));
		http.addFilter(new Autorizador(authenticationManager(), provedorJwt, userDetailsService));
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}
}
