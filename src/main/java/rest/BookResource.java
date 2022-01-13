package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import facades.SearchFacade;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/book")
public class BookResource {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final SearchFacade searchFacade = SearchFacade.getSearchFacade();

    @GET
    @Produces("text/plain")
    public String hello() {
        return "Welcome to the book resource.";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{key}")
    public String getBook(@PathParam("key") String key) throws IOException {
        BookWithDetailsDTO book = searchFacade.getBookDetails(key);
        return GSON.toJson(book);
    }
}
