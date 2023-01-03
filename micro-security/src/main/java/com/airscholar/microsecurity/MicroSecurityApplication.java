package com.airscholar.microsecurity;

import com.airscholar.microsecurity.config.RedisHashComponent;
import com.airscholar.microsecurity.dto.ApiKey;
import com.airscholar.microsecurity.util.AppConstants;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class MicroSecurityApplication {

    private RedisHashComponent redisHashComponent;

    public MicroSecurityApplication(RedisHashComponent redisHashComponent) {
        this.redisHashComponent = redisHashComponent;
    }

    //run this code after the application is started
    @PostConstruct
    public void initKeyToRedis(){
        List<ApiKey> apiKeys = new ArrayList<>();
        apiKeys.add(new ApiKey("3ELM3A34FO8CMS", List.of(AppConstants.STUDENT_SERVICE_KEY, AppConstants.COURSE_SERVICE_KEY)));
//        apiKeys.add(new ApiKey("F4G5H6J7K8L9M0", List.of(AppConstants.COURSE_SERVICE_KEY)));

        List<Object> lists = redisHashComponent.hValues(AppConstants.RECORD_KEY);

        if(lists.isEmpty()){
            apiKeys.forEach(apiKey -> redisHashComponent.hSet(AppConstants.RECORD_KEY, apiKey.getKey(), apiKey));
        }
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder){
        return builder.routes()
                .route(AppConstants.STUDENT_SERVICE_KEY, r -> r.path("/api/student-service/**")
                        .filters(f -> f.stripPrefix(2)).uri("http://localhost:8081"))
                .route(AppConstants.COURSE_SERVICE_KEY, r -> r.path("/api/course-service/**")
                        .filters(f -> f.stripPrefix(2)).uri("http://localhost:8082"))
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(MicroSecurityApplication.class, args);
    }

}
