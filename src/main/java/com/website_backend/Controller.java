package com.website_backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;
import com.website_backend.Data.Order;
import com.website_backend.Data.ProductCategory;
import com.website_backend.Data.ProductInfo;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
  @Autowired
  JdbcTemplate jdbcTemplate;
  ProductRowMapper productRowMapper = new ProductRowMapper();

  ObjectMapper objectMapper = new ObjectMapper();
  @Autowired
  OrderValidator orderValidator;

  @Autowired
  JsonSchema orderSchema;

  public Controller(){

  }
  @GetMapping("/debug")
  public ResponseEntity<?> getDebug(){
    System.out.println(jdbcTemplate.queryForList("SELECT * FROM product"));
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/products/{category}")
  public ResponseEntity<?> getProducts(@PathVariable String category, @RequestParam int minPrice, @RequestParam int maxPrice){
    // TODO: Move to separate endpoint or handler
    // stock not found
    List<ProductInfo> productInfoList = jdbcTemplate.query("""
    SELECT *FROM product
    WHERE (is_discontinued=0) AND (price BETWEEN ? AND ?) AND (category=?) AND (stock >= 1)""", productRowMapper,minPrice, maxPrice, category);
    return new ResponseEntity<>(productInfoList, HttpStatus.OK);
  }

  @PostMapping("/order")
  public ResponseEntity<?> placeOrder(@RequestBody String json){
    JsonNode jsonNode;
    try{
      jsonNode = objectMapper.readTree(json);
      Set<ValidationMessage> msg = orderSchema.validate(jsonNode);
      if (msg.isEmpty()){
        Order order = objectMapper.treeToValue(jsonNode, Order.class);
        boolean isOrderValid = orderValidator.isOrderValid(order);
        if (isOrderValid){
          return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
          return new ResponseEntity<>("Product does not exist or out of stock",HttpStatus.BAD_REQUEST);
        }
      }
      else{
        return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
      }
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
