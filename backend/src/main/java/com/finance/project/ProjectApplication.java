package com.finance.project;

import com.finance.project.modules.person.application.CreatePersonService;
import com.finance.project.modules.group.application.CreateGroupService;
import com.finance.project.modules.person.domain.IPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication
@RestController
public class ProjectApplication implements ApplicationRunner {

    @Autowired
    CreatePersonService createPersonService;
    @Autowired
    CreateGroupService createGroupService;
    @Autowired
    IPersonRepository personRepository;

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }


    @Override
    public void run(ApplicationArguments arg0) throws Exception {
        // Los tests levantan varios contextos de Spring contra la misma H2 en memoria;
        // sin este guard, la segunda ejecucion del seeder falla con "Address already exists".
        if (personRepository.count() > 0) {
            System.out.println("Database already initialized, skipping bootstrap");
            return;
        }
        System.out.println("Initializing Database");
        Bootstrapping.loadData(createPersonService, createGroupService);
        System.out.println("Database Created");
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:3000")
                        .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH");
            }
        };
    }


}

