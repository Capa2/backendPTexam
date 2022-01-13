package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import facades.SearchFacade;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/nyt/list")
public class BestSellerListResource {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final SearchFacade searchFacade = SearchFacade.getSearchFacade();

    @GET
    @Produces("text/plain")
    public String hello() {
        return "Welcome to the best seller list resource.";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{list}")
    public String getList(@PathParam("list") String list) throws IOException {
        BestSellerListDTO listDTO = searchFacade.getBestSellerList(list);
        return GSON.toJson(listDTO);
    }
}
