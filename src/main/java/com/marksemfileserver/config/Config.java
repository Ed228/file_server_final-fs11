package com.marksemfileserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Paths;

@Configuration
public class Config {
    @Value("${group.path}")
    private String path;

    @Bean
    public PathConf pathConf(){
        System.out.println(path);
        return new PathConf(Paths.get(path));
    }
}
