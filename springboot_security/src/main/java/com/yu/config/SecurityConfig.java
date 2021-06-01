package com.yu.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //授权
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //首页所有人可以访问，功能页只有对应权限的人才能访问
        //请求授权的规则~
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/level1/**").hasRole("vip1")
                .antMatchers("/level2/**").hasRole("vip2")
                .antMatchers("/level3/**").hasRole("vip3");

        //开启自动配置的登陆功能
        http.formLogin()
                .loginPage("/toLogin")  //自定义登录页面，一但定制loginPage；那么loginPage的post请求就是登陆
                .loginProcessingUrl("/login")  // 自定义的登录接口地址，若不配置则是自定义登录页面地址
                .usernameParameter("user") //登录表单用户名name的值，默认username
                .passwordParameter("pwd") //登录表单密码name的值，默认password
                // 自定义登录成功处理
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        response.setContentType("application/json;charset=utf-8");
                        //获取登录用户名
                        UserDetails principal = (UserDetails) authentication.getPrincipal();
                        String username = principal.getUsername();
                        HttpSession session = request.getSession();
                        session.setAttribute("username", username);
                        response.sendRedirect("/index");
                    }
                })
                //自定义登录失败处理
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                        response.setContentType("application/json;charset=utf-8");
                        PrintWriter out = response.getWriter();
                        out.write("登录失败");
                        out.flush();
                    }
                })
        ;

        //关闭跨域访问，解决注销404问题 或者 以post方式提交注销请求
        http.csrf().disable();

        //开启自动配置的注销功能。
        //Spring Security默认访问 /logout 表示用户注销，清空session，注销成功会返回 /login?logout 页面；
        http.logout().logoutSuccessUrl("/");//配置注销成功以后来到首页

        //开启记住我功能
        //登陆成功以后，将cookie发给浏览器保存，以后访问页面带上这个cookie，只要通过检查就可以免登录，点击注销会删除cookie
        http.rememberMe().rememberMeParameter("remember");
    }

    //认证，springboot2.1.x可以直接使用
    //密码编码：PasswordEncoder
    //在spring security 5.0+ 新增了很多加密方法
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //and方法可以一直添加多个
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("yu").password(new BCryptPasswordEncoder().encode("123456")).roles("vip1", "vip2")
                .and()
                .withUser("admin").password(new BCryptPasswordEncoder().encode("admin")).roles("vip1", "vip2", "vip3");
    }

    //忽略拦截
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/static/**");
//    }
}
