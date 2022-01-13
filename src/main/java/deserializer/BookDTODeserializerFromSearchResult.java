package deserializer;

import com.google.gson.*;
import facades.SearchFacade;
import utils.JsonUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookDTODeserializerFromSearchResult implements JsonDeserializer<BookDTO> {

    private final SearchFacade searchFacade = SearchFacade.getSearchFacade();

    @Override
    public BookDTO deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        String title = JsonUtils.getString(jsonObject.get("title"));
        int first_publish_year = JsonUtils.getInt(jsonObject.get("first_publish_year"));
        int number_of_pages_median = JsonUtils.getInt(jsonObject.get("number_of_pages_median"));
        int coverId = JsonUtils.getInt(jsonObject.get("cover_i"));
        List<String> thumbnail_urls = searchFacade.getCoverUrlsById(coverId);

        List<String> subjectKeys = JsonUtils.getStringList(jsonObject.get("subject_key"));
        List<String> subjectNames = JsonUtils.getStringList(jsonObject.get("subject_facet"));
        List<SubjectDTO> subjects = getSubjects(subjectKeys, subjectNames);

        List<String> authorKeys = JsonUtils.getStringList(jsonObject.get("author_key"));
        List<String> authorNames = JsonUtils.getStringList(jsonObject.get("author_name"));
        List<AuthorDTO> authors = getAuthors(authorKeys, authorNames);

        String[] workKeyArray = jsonObject.get("key").getAsString().split("/");
        String workKey = workKeyArray[workKeyArray.length-1];

        return new BookDTO(workKey, title, first_publish_year, number_of_pages_median, thumbnail_urls, authors, subjects);
    }

    // TODO: The subjects don't match their key because the keys are sorted alphabetically.
    //  So are the facets but lowercase are sorted last which desyncs the list.
    //  The list called "subjects" is not sorted at all so it won't match the keys.
    //  You might have to just omit the key and search by name later.
    private List<SubjectDTO> getSubjects(List<String> keys, List<String> names) {
        List<SubjectDTO> subjects = new ArrayList<>();
        for (int i = 0; i < keys.size(); i++) {
            subjects.add(new SubjectDTO(keys.get(i), names.get(i)));
        }
        return subjects;
    }

    private List<AuthorDTO> getAuthors(List<String> keys, List<String> names) {
        List<AuthorDTO> authors = new ArrayList<>();
        for (int i = 0; i < keys.size(); i++) {
            authors.add(new AuthorDTO(keys.get(i), names.get(i)));
        }
        return authors;
    }
}
