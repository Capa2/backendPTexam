package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import facades.SearchFacade;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/search")
public class SearchResource {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final SearchFacade searchFacade = SearchFacade.getSearchFacade();

    @GET
    @Produces("text/plain")
    public String hello() {
        return "Welcome to the search engine.";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{search}")
    public String searchBasic(@PathParam("search") @Encoded String search,
                              @DefaultValue("25") @QueryParam("limit") int limit,
                              @DefaultValue("0") @QueryParam("offset") int offset)
            throws IOException {
        BookSearchResultsDTO result = searchFacade.getBookSearchResult(search, limit, offset);
        return GSON.toJson(result);
    }
}
