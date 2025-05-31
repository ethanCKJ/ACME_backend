package com.website_backend.config;

import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion.VersionFlag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
/**
 * Creates JsonSchema objects for applying the Json schema on input json data
 */
public class JsonSchemaConfig {
  private String SCHEMA_FOLDER = "/jsonschema/";
  @Bean
  public JsonSchema orderSchema(){
    return JsonSchemaFactory
        .getInstance(VersionFlag.V7)
        .getSchema(getClass().getResourceAsStream(SCHEMA_FOLDER + "order.json"));
  }

}
