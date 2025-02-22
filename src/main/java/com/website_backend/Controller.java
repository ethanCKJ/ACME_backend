package com.website_backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;
import com.website_backend.Data.ProductInfo;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
  @Autowired
  JdbcTemplate jdbcTemplate;
  ProductRowMapper productRowMapper = new ProductRowMapper();

  ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  JsonSchema orderSchema;

  public Controller(){

  }
  @GetMapping("/debug")
  public ResponseEntity<?> getDebug(){
    System.out.println(jdbcTemplate.queryForList("SELECT * FROM product"));
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/products")
  public ResponseEntity<?> getProducts(){
    List<ProductInfo> productInfoList = jdbcTemplate.query("SELECT * FROM product", productRowMapper);
    return new ResponseEntity<>(productInfoList, HttpStatus.OK);
  }

  @PostMapping("/order")
  public ResponseEntity<?> placeOrder(@RequestBody String json){
    JsonNode jsonNode;
    try{
      jsonNode = objectMapper.readTree(json);
      Set<ValidationMessage> validationMessageSet = orderSchema.validate(jsonNode);

    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
