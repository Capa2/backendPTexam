package rest;

import entities.LibraryItem;
import entities.Role;
import entities.User;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

class LibraryResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();

        httpServer.shutdownNow();
    }

    @BeforeEach
    void setUp() {
        EntityManager em = emf.createEntityManager();

        Role userRole = new Role("user");
        Role adminRole = new Role("admin");

        User user = new User("user", "test");
        User admin = new User("admin", "test");
        User both = new User("user_admin", "test");

        user.addRole(userRole);
        admin.addRole(adminRole);
        both.addRole(userRole);
        both.addRole(adminRole);

        user.addToLibrary(new LibraryItem("OL679360W"));

        try {
            em.getTransaction().begin();
            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
            em.persist(both);
            em.getTransaction().commit();
        }
        finally {
            em.close();
        }
    }

    @AfterEach
    void tearDown() {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.createNativeQuery("DELETE FROM user_roles").executeUpdate();
            em.createNativeQuery("DELETE FROM roles").executeUpdate();
            em.createNativeQuery("DELETE FROM user_library").executeUpdate();
            em.createNativeQuery("DELETE FROM users").executeUpdate();
            em.createNativeQuery("DELETE FROM library_item").executeUpdate();
            em.getTransaction().commit();
        }
        finally {
            em.close();
        }
    }

    //This is how we hold on to the token after login, similar to that a client must store the token somewhere
    private static String securityToken;

    //Utility method to login and set the returned securityToken
    private static void login(String role, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", role, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                .when().post("/login")
                .then()
                .extract().path("token");
        //System.out.println("TOKEN ---> " + securityToken);
    }

    private void logOut() {
        securityToken = null;
    }

    @Test
    void addBookForUser() {
        login("user", "test");
        String bookKey = "OL453936W";
        given()
                .accept(MediaType.APPLICATION_JSON)
                .header("x-access-token", securityToken)
                .when()
                .post("/library/add/" + bookKey)
                .then()
                .statusCode(200)
                .body("bookKey", equalTo(bookKey));
    }

    @Test
    void addBookForAdmin() {
        login("admin", "test");
        String bookKey = "OL679360W";
        given()
                .accept(MediaType.APPLICATION_JSON)
                .header("x-access-token", securityToken)
                .when()
                .post("/library/add/" + bookKey)
                .then()
                .statusCode(401)
                .body("code", equalTo(401))
                .body("message", equalTo("You are not authorized to perform the requested operation"));
    }

    @Test
    void addBookNotLoggedIn() {
        logOut();
        String bookKey = "OL679360W";
        given()
                .accept(MediaType.APPLICATION_JSON)
                .when()
                .post("/library/add/" + bookKey)
                .then()
                .statusCode(403)
                .body("code", equalTo(403))
                .body("message", equalTo("Not authenticated - do login"));
    }

    @Test
    void getLibrary() {
        login("user", "test");
        given()
                .accept(MediaType.APPLICATION_JSON)
                .header("x-access-token", securityToken)
                .when()
                .get("/library/get")
                .then()
                .statusCode(200)
                .body("size", equalTo(1));
    }

    @Test
    void updateBook() {
        login("user", "test");
        String bookKey = "OL679360W";
        LibraryItemDTO item = new LibraryItemDTO(bookKey,"READING", 4);
        given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-access-token", securityToken)
                .body(item)
                .when()
                .put("/library/edit/" + bookKey)
                .then()
                .statusCode(200)
                .body("status", equalTo("READING"))
                .body("rating", equalTo(4));
    }

    @Test
    void deleteBook() {
        login("user", "test");
        String bookKey = "OL679360W";
        given()
                .accept(MediaType.APPLICATION_JSON)
                .header("x-access-token", securityToken)
                .when()
                .delete("/library/delete/" + bookKey)
                .then()
                .statusCode(200)
                .body("bookKey", equalTo(bookKey));
    }
}
