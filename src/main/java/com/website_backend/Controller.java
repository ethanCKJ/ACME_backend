package com.website_backend;

import java.sql.Statement;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

//  @Autowired // variable share same name as function
//  DataSource jdbcDataSource;
//  private final JdbcTemplate jdbcTemplate;
//
//  @Autowired
//  public Controller(DataSource dataSource){
//    this.jdbcTemplate = new JdbcTemplate(dataSource);
//  }
//
//  @GetMapping("/")
//  public ResponseEntity<?> getDebug(){
//    System.out.println(jdbcTemplate.queryForList("SELECT * FROM Product"));
//    return new ResponseEntity<>(HttpStatus.OK);
//  }
}
