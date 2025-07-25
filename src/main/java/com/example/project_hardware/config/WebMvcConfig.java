package com.example.project_hardware.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    // application.properties 에서 설정한 업로드 폴더 경로 주입하여 uploadPath에 넣음
    @Value("${board.imgdir}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //웹 요청 경로 "/img/"로 들어오면 실제 파일 저장 위치를 액세스
        registry.addResourceHandler("/img/**") //웹에서 접근할 URL 경로 패턴
                .addResourceLocations("file:" + uploadPath + "/"); //해당 URL 패턴이 가리킬 실제 파일 시스템 경로
    }
}
