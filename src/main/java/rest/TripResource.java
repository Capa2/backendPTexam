package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.TripDTO;
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
    @Produces(MediaType.APPLICATION_JSON)
    @Path("add")
    @RolesAllowed("admin")
    public String addTrip(
            @PathParam("dateTime") String dateTimeString,
            @PathParam("name") String name,
            @PathParam("location") String location,
            @PathParam("duration") Long duration,
            @PathParam("packingList") String packingList) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
        List<String> packingListArray = Arrays.asList(packingList.split(","));

        TripDTO tripDTO = new TripDTO(dateTime, name, location, duration, packingListArray);
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
    @Path("get")
    public String getTrips() {
        List<TripDTO> dtos = tripFacade.getAll();
        return GSON.toJson(dtos);
    }
}
