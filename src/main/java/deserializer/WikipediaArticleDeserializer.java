package deserializer;

import com.google.gson.*;

import java.lang.reflect.Type;

public class WikipediaArticleDeserializer implements JsonDeserializer<WikipediaArticleDTO> {
    @Override
    public WikipediaArticleDTO deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String title = jsonObject.get("title").getAsString();
        String description = jsonObject.get("description").getAsString();
        int pageid = jsonObject.get("pageid").getAsInt();
        String extract = jsonObject.get("extract").getAsString();
        String url = jsonObject.get("content_urls").getAsJsonObject().get("desktop").getAsJsonObject().get("page").getAsString();
        JsonObject thumbnail = jsonObject.get("thumbnail").getAsJsonObject();
        String thumbnail_source = thumbnail.get("source").getAsString();
        int thumbnail_width = thumbnail.get("width").getAsInt();
        int thumbnail_height = thumbnail.get("height").getAsInt();
        return new WikipediaArticleDTO(title, description, pageid, extract, url, thumbnail_source, thumbnail_width, thumbnail_height);
    }
}
