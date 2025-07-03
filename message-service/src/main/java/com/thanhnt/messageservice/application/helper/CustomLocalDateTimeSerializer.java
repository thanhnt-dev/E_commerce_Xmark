package com.thanhnt.messageservice.application.helper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomLocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

  private final DateTimeFormatter formatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

  @Override
  public void serialize(
      LocalDateTime localDateTime,
      JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider)
      throws IOException {
    String formattedDateTime = localDateTime.format(formatter);
    jsonGenerator.writeString(formattedDateTime);
  }
}
