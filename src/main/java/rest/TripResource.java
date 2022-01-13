package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.TripDTO;
import errorhandling.API_Exception;
import errorhandling.API_ExceptionMapper;
import facades.TripFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;


@Path("trip")
public class TripResource {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final TripFacade tripFacade = TripFacade.getTripFacade(EMF);

    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces("text/plain")
    public String hello() {
        return "Welcome to the trip endpoint";
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("create")
    @RolesAllowed("admin")
    public String createTrip(String jsonString) throws API_Exception {
        LocalDateTime dateTime;
        String name;
        String location;
        long duration;
        List<String> packingList;
        List<String> participantEmails;

        try {
            JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
            String dateTimeString = json.get("dateTime").getAsString();
            name = json.get("name").getAsString();
            location = json.get("location").getAsString();
            duration = json.get("duration").getAsLong() * 60 * 60;
            String packingListString = json.get("packingList").getAsString();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            dateTime = LocalDateTime.parse(dateTimeString, formatter);
            //dateTime = LocalDateTime.parse(dateTimeString);
            packingList = Arrays.asList(packingListString.split(","));
        } catch (Exception e) {
            throw new API_Exception("Malformed JSON Supplied", 400, e);
        }

        TripDTO tripDTO = new TripDTO(dateTime, name, location, duration, packingList);
        TripDTO resultDTO = tripFacade.create(tripDTO);
        return GSON.toJson(resultDTO);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("get/{id}")
    public String getTrip(@PathParam("id") long id) {
        TripDTO dto = tripFacade.getById(id);
        return GSON.toJson(dto);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("join/{id}/{email}")
    public String joinTrip(@PathParam("id") long id, @PathParam("email") String email) {
        TripDTO dto = tripFacade.joinTrip(id, email);
        boolean bool = tripFacade.isParticipant(id, email);
        return GSON.toJson(bool);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("leave/{id}/{email}")
    public String leaveTrip(@PathParam("id") long id, @PathParam("email") String email) {
        TripDTO dto = tripFacade.leaveTrip(id, email);
        boolean bool = tripFacade.isParticipant(id, email);
        return GSON.toJson(bool);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("isParticipant/{id}/{email}")
    public String isParticipant(@PathParam("id") long id, @PathParam("email") String email) {
        boolean bool = tripFacade.isParticipant(id, email);
        return GSON.toJson(bool);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("get")
    public String getTrips() {
        List<TripDTO> dtos = tripFacade.getAll();
        return GSON.toJson(dtos);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("delete/{id}")
    @RolesAllowed("admin")
    public String deleteBook(@PathParam("id") long id) {
        TripDTO dto = tripFacade.deleteById(id);
        return GSON.toJson(dto);
    }
}
