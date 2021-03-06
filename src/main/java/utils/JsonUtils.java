package utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    // refactored as helper methods

    public static int getInt(JsonElement je) {
        int defaultValue = 0;
        try {
            return je != null && je.isJsonPrimitive() ? je.getAsInt() : defaultValue;
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    public static String getString(JsonElement je) {
        String defaultValue = "";
        try {
            return je != null && je.isJsonPrimitive() ? je.getAsString() : defaultValue;
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    public static List<String> getStringList(JsonElement je) {
        JsonArray array = je != null && je.isJsonArray() ? je.getAsJsonArray() : null;
        List<String> list = new ArrayList<>();
        if (array != null) {
            array.forEach(s -> list.add(getString(s)));
        }
        return list;
    }

    public static List<JsonElement> getElementList(JsonElement je) {
        JsonArray array = je != null && je.isJsonArray() ? je.getAsJsonArray() : null;
        List<JsonElement> list = new ArrayList<>();
        if (array != null) {
            array.forEach(list::add);
        }
        return list;
    }

    // specific to OpenLibrary
    public static String getDescription(JsonElement je) {
        String defaultValue = "";
        if (je != null) {
            if (je.isJsonPrimitive()) {
                return getString(je);
            }
            else {
                return getString(je.getAsJsonObject().get("value"));
            }
        }
        return defaultValue;
    }

    // could add getLinks as it will be copy-pasted from Work to Author.
}
