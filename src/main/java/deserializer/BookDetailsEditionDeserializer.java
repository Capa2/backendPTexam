package deserializer;

import com.google.gson.*;
import utils.JsonUtils;

import java.lang.reflect.Type;
import java.util.List;

public class BookDetailsEditionDeserializer implements JsonDeserializer {
    @Override
    public Object deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        String subtitle = JsonUtils.getString(jsonObject.get("subtitle"));
        String description = JsonUtils.getDescription(jsonObject.get("description"));
        List<String> series = JsonUtils.getStringList(jsonObject.get("series"));
        String edition_name = JsonUtils.getString(jsonObject.get("edition_name"));
        String physical_format = JsonUtils.getString(jsonObject.get("physical_format"));

        return new BookDetailsEditionDTO(subtitle, description, series, edition_name, physical_format);
    }
}
