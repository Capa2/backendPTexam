package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import facades.UserFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Path("library")
public class LibraryResource {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final UserFacade userFacade = UserFacade.getUserFacade(EMF);

    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces("text/plain")
    public String hello() {
        return "Welcome to the user library.";
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("add/{key}")
    @RolesAllowed("user")
    public String addBook(@PathParam("key") String key) throws IOException {
        String username = securityContext.getUserPrincipal().getName();
        LibraryItemDTO itemDTO = new LibraryItemDTO(key);
        LibraryItemDTO resultDTO = userFacade.addBook(username, itemDTO);
        return GSON.toJson(resultDTO);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("get/{key}")
    @RolesAllowed("user")
    public String getBook(@PathParam("key") String key) {
        String username = securityContext.getUserPrincipal().getName();
        LibraryItemDTO res = userFacade.getBook(username, key);
        return GSON.toJson(res);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("get")
    @RolesAllowed("user")
    public String getLibrary() throws IOException, ExecutionException, InterruptedException {
        String username = securityContext.getUserPrincipal().getName();
        LibraryDTO res = userFacade.getLibrary(username);
        return GSON.toJson(res);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("edit/{key}")
    @RolesAllowed("user")
    public String updateBook(@PathParam("key") String key, String jsonString) {
        String username = securityContext.getUserPrincipal().getName();
        LibraryItemDTO itemDTO = GSON.fromJson(jsonString, LibraryItemDTO.class);
        // it's unnecessary to even have the URL key, so we could get rid of it.
        if (!itemDTO.getBookKey().equals(key)) {
            throw new WebApplicationException("Key in URL and in JSON object don't match", 400);
        }
        LibraryItemDTO resultDTO = userFacade.updateBook(username, itemDTO);
        return GSON.toJson(resultDTO);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("edit/{key}/status/{status}")
    @RolesAllowed("user")
    public String updateStatus(@PathParam("key") String key, @PathParam("status") String status) {
        String username = securityContext.getUserPrincipal().getName();
        LibraryItemDTO oldItem = userFacade.getBook(username, key);
        oldItem.setStatus(status);
        LibraryItemDTO newItem = userFacade.updateBook(username, oldItem);
        return GSON.toJson(newItem);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("edit/{key}/rating/{rating}")
    @RolesAllowed("user")
    public String updateRating(@PathParam("key") String key, @PathParam("rating") int rating) {
        String username = securityContext.getUserPrincipal().getName();
        LibraryItemDTO oldItem = userFacade.getBook(username, key);
        oldItem.setRating(rating);
        LibraryItemDTO newItem = userFacade.updateBook(username, oldItem);
        return GSON.toJson(newItem);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("delete/{key}")
    @RolesAllowed("user")
    public String deleteBook(@PathParam("key") String key) {
        String username = securityContext.getUserPrincipal().getName();
        LibraryItemDTO res = userFacade.deleteBook(username, key);
        return GSON.toJson(res);
    }
}
