package com.crud.tasks.trello.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "company")
@Getter
@Setter
public class CompanyConfig {
    private String name;
    private String address;
    private String phone;
    private String email;
}
