package com.mysite.sbb;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration // 스프링의 환경설정 파일임을 의미
@EnableWebSecurity  // 모든 요청 URL이 스프링 시큐리티의 제어를 받도록 함
public class SecurityConfig {

}
