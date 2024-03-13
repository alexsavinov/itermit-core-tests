package api.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JacksonException {

        String text = jsonParser.getText();

        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS'Z'";
        if (text.length() == 27) {
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'";
        } else if (text.length() == 24) {
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);

        return LocalDateTime.parse(text, dateTimeFormatter);
    }
}
