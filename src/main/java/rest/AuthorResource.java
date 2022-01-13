package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import facades.SearchFacade;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/author")
public class AuthorResource {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final SearchFacade searchFacade = SearchFacade.getSearchFacade();

    @GET
    @Produces("text/plain")
    public String hello() {
        return "Welcome to the author resource.";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public String getAuthor(@PathParam("id") String id) throws IOException {
        AuthorDTO result = searchFacade.getAuthor(id);
        return GSON.toJson(result);
    }
}
