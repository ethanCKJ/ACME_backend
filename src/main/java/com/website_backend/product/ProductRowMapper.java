package com.website_backend.product;

import com.website_backend.orders.enums.ProductCategory;
import com.website_backend.browse.ProductInfo;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ProductRowMapper implements RowMapper<ProductInfo> {

  @Override
  public ProductInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
    ProductInfo productInfo = new ProductInfo();
    productInfo.setId(rs.getInt("id"));
    productInfo.setProductInfo(rs.getString("product_info"));
    productInfo.setStock(rs.getInt("stock"));
    productInfo.setPrice(rs.getDouble("price"));
    // valueOf converts string to enum
    productInfo.setProductCategory(ProductCategory.valueOf(rs.getString("category")));
    productInfo.setProductName(rs.getString("product_name"));
    productInfo.setImageName(rs.getString("image_name"));
    productInfo.setPopularity(rs.getInt("popularity"));
    productInfo.setDiscontinued(false);
    return productInfo;
  }
}
