package kz.bsbnb.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;

/**
 * Created by Olzhas.Pazyldayev on 23.08.2016.
 */

public class JsonUtil {
    public static final ObjectMapper mapper = new ObjectMapper();
    public static final ObjectWriter json = mapper.writerWithDefaultPrettyPrinter();

    public static String toJson(Object object) throws JsonProcessingException {
        return json.writeValueAsString(object);
    }

    public static String toJsonNonNull(Object object) throws JsonProcessingException {
        return mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL).writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    @SuppressWarnings("unchecked")
    public static Object fromJson(String jsonString, Class clazz) throws IOException {
        return mapper.readValue(jsonString, clazz);
    }
}
