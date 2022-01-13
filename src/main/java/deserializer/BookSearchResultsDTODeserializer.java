package deserializer;

import com.google.gson.*;
import utils.JsonUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookSearchResultsDTODeserializer implements JsonDeserializer<BookSearchResultsDTO> {
    @Override
    public BookSearchResultsDTO deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        int numFound = JsonUtils.getInt(jsonObject.get("numFound"));
        int start = JsonUtils.getInt(jsonObject.get("start"));
        int offset = JsonUtils.getInt(jsonObject.get("offset"));
        String query = JsonUtils.getString(jsonObject.get("q"));

        JsonArray docs = jsonObject.get("docs").getAsJsonArray();
        List<BookDTO> results = new ArrayList<>();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(BookDTO.class, new BookDTODeserializerFromSearchResult())
                .create();
        for (JsonElement doc : docs) {
            results.add(gson.fromJson(doc, BookDTO.class));
        }

        return new BookSearchResultsDTO(numFound, start, offset, query, results);
    }
}
