package deserializer;

import com.google.gson.*;
import utils.JsonUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookDetailsWorkDeserializer implements JsonDeserializer {
    @Override
    public Object deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String description = JsonUtils.getDescription(jsonObject.get("description"));

        List<LinkDTO> links = new ArrayList<>();
        for (JsonElement je : JsonUtils.getElementList(jsonObject.get("links"))) {
            JsonObject link = je.getAsJsonObject();
            String title = JsonUtils.getString(link.get("title"));
            String url = JsonUtils.getString(link.get("url"));
            links.add(new LinkDTO(title, url));
        }

        return new BookDetailsWorkDTO(description, links);
    }
}
