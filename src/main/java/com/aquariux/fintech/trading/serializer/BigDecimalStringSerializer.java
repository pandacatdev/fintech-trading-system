package com.aquariux.fintech.trading.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalStringSerializer extends JsonSerializer<BigDecimal> {

  @Override
  public void serialize(BigDecimal bigDecimal, JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider) throws IOException {
    jsonGenerator.writeString(bigDecimal.setScale(10, RoundingMode.DOWN).toPlainString());
  }
}
