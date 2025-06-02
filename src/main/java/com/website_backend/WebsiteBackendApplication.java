package com.website_backend;

import com.website_backend.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class WebsiteBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(WebsiteBackendApplication.class, args);
  }


}
