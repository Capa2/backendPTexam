package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import deserializer.WikipediaArticleDeserializer;
import dtos.funstuff.DadJokeDTO;
import entities.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import utils.EMF_Creator;
import utils.HttpUtils;

/**
 * @author lam@cphbusiness.dk
 */
@Path("info")
public class DemoResource {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }

    //Just to verify if the database is setup
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    public String allUsers() {

        EntityManager em = EMF.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery ("select u from User u",entities.User.class);
            List<User> users = query.getResultList();
            return "[" + users.size() + "]";
        } finally {
            em.close();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user")
    @RolesAllowed("user")
    public String getFromUser() {
        String thisUser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to User: " + thisUser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed("admin")
    public String getFromAdmin() {
        String thisUser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to (admin) User: " + thisUser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("sequential")
    public String getSequentialDemo() throws IOException {
        List<String> urls = new ArrayList<>();
        urls.add("https://en.wikipedia.org/api/rest_v1/page/random/summary");
        urls.add("https://en.wikipedia.org/api/rest_v1/page/random/summary");

        List<String> rawJsonStrings = HttpUtils.runSequential(urls);
        List<WikipediaArticleDTO> resDTOs = new ArrayList<>();

        Gson WikipediaArticleGson = new GsonBuilder()
                .registerTypeAdapter(WikipediaArticleDTO.class, new WikipediaArticleDeserializer())
                .setPrettyPrinting()
                .create();
        rawJsonStrings.forEach(s -> resDTOs.add(WikipediaArticleGson.fromJson(s, WikipediaArticleDTO.class)));
        return GSON.toJson(resDTOs);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("parallel")
    public String getParallelDemo() throws IOException, ExecutionException, InterruptedException {
        List<String> urls = new ArrayList<>();
        urls.add("https://icanhazdadjoke.com");
        urls.add("https://icanhazdadjoke.com");

        List<String> rawJsonStrings = HttpUtils.runParallel(urls);
        List<DadJokeDTO> resDTOs = new ArrayList<>();

        rawJsonStrings.forEach(s -> resDTOs.add(GSON.fromJson(s, DadJokeDTO.class)));
        return GSON.toJson(resDTOs);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("jokes")
    public String getJokes() throws ExecutionException, InterruptedException {
        return HttpUtils.fetchJokes();
    }
}
