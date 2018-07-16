package me.afua.securitydemo.security;

import me.afua.securitydemo.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    String[] administrators = {"/h2/**","/users"};
    String [] everyone = {"/","/img/**","/css/**","/register"};
    String [] suspended = {"/"};


    @Autowired
    AppUserRepository userRepo;

    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return new SSUDS(userRepo);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()

                .antMatchers(everyone).permitAll()
                .antMatchers(administrators).hasAuthority("ADMIN")
                .antMatchers(suspended).hasAuthority("SUSPENDED")
                .anyRequest()
                .authenticated()
        .and()
        .formLogin().loginPage("/login").permitAll()
        .and()
        .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll();


        //For H2, not live production
        http.headers().frameOptions().disable();
        http.csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        auth.inMemoryAuthentication()
                .withUser("defaultuser").password(encoder.encode("defaultpassword")).authorities("USER")
                .and()
                .withUser("administrator").password(encoder.encode("administratorpassword")).authorities("ADMIN")
                .and()
                .passwordEncoder(encoder);

        auth.userDetailsService(userDetailsServiceBean()).passwordEncoder(encoder);
    }
}
