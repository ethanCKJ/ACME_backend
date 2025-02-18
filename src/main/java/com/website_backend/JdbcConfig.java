package com.website_backend;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class JdbcConfig {
//  @Bean
//  public DataSource jdbcDataSource()  {
//    Properties properties = new Properties();
//    String username;
//    String password;
//    try (InputStream inputStream = new FileInputStream("config.properties")){
//      properties.load(inputStream);
//      username = properties.getProperty("sql_username");
//      password = properties.getProperty("sql_password");
//    } catch (Exception e) {
//      throw new RuntimeException(e);
//    }
//    // DriverManager.getConnection is unsuitable as it returns a Connection while jdbcTemplate uses datasource
//    DriverManagerDataSource dataSource = new DriverManagerDataSource();
//    dataSource.setDriverClassName("com.mysql.jdbc.Driver");
//    dataSource.setUrl("jdbc:mysql://localhost:3306/acme_db");
//    dataSource.setUsername(username);
//    dataSource.setPassword(password);
//    return dataSource;
//  }
}
