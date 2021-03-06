package security;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.RoleDTO;
import dtos.UserDTO;
import errorhandling.API_Exception;
import facades.UserFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/signup")
public class SignupEndpoint {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    public static final UserFacade USER_FACADE = UserFacade.getUserFacade(EMF);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String signup(String jsonString) throws API_Exception {
        String email;
        String password;
        String name;
        String address;
        String phone;
        int birthYear;
        String gender;
        try {
            JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
            email = json.get("email").getAsString();
            password = json.get("password").getAsString();
            name = json.get("name").getAsString();
            address = json.get("address").getAsString();
            phone = json.get("phone").getAsString();
            birthYear = json.get("birthYear").getAsInt();
            gender = json.get("gender").getAsString();
        } catch (Exception e) {
            throw new API_Exception("Malformed JSON Supplied", 400, e);
        }
        UserDTO user = USER_FACADE.signup(email, password, name, address, phone, birthYear, gender);
        return new Gson().toJson(user);
    }
}
