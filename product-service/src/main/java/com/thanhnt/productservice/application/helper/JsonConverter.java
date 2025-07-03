package com.thanhnt.productservice.application.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thanhnt.productservice.application.exception.ProductException;
import com.thanhnt.productservice.domain.entity.common.ErrorCode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.IOException;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;

@Converter(autoApply = true)
@RequiredArgsConstructor
public class JsonConverter implements AttributeConverter<HashMap<String, String>, String> {
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(HashMap<String, String> attribute) {
    try {
      return objectMapper.writeValueAsString(attribute);
    } catch (JsonProcessingException e) {
      throw new ProductException(ErrorCode.PRODUCT_CONVERT_ERROR);
    }
  }

  @Override
  public HashMap<String, String> convertToEntityAttribute(String dbData) {
    if (dbData == null) return new HashMap<>();
    try {
      return objectMapper.readValue(dbData, new TypeReference<HashMap<String, String>>() {});
    } catch (IOException e) {
      throw new ProductException(ErrorCode.PRODUCT_CONVERT_ERROR);
    }
  }
}
