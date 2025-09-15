package com.website_backend.product;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

  private final JdbcTemplate jdbcTemplate;
  private final Logger log = LoggerFactory.getLogger(ProductService.class);
  private final ProductRowMapper rowMapper;

  public ProductService(JdbcTemplate jdbcTemplate, ProductRowMapper rowMapper) {
    this.jdbcTemplate = jdbcTemplate;
    this.rowMapper = rowMapper;
  }

  /**
   * Returns list of product info. On error returns empty list
   * @param category
   * @param minPrice
   * @param maxPrice
   * @return
   */
  public List<ProductInfo> getProducts(String category, int minPrice, int maxPrice){
    List<ProductInfo> productInfoList = new ArrayList<>();
    try {
      productInfoList = jdbcTemplate.query("""
            SELECT * FROM acme_db.product
            WHERE (is_discontinued=0) AND (price BETWEEN ? AND ?) AND (category=?)""",
          rowMapper,
          minPrice, maxPrice, category);
    } catch (Exception e) {
      log.error(e.getLocalizedMessage());
    }
    log.info("OK /products/{} {}",category, productInfoList);
    return productInfoList;
  }

}
