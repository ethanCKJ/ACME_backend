package com.website_backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import org.springframework.jdbc.core.RowMapper;

public class PriceRowMapper implements RowMapper<HashMap> {

  @Override
  public HashMap mapRow(ResultSet rs, int rowNum) throws SQLException {
    HashMap<Integer, Double> productIdToPrice = new HashMap<>();
    productIdToPrice.put(rs.getInt("id"), rs.getDouble("price"));
    return productIdToPrice;
  }
}
