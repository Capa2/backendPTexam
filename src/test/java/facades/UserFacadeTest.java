package facades;

import entities.LibraryItem;
import entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.WebApplicationException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class UserFacadeTest {

    private static EntityManagerFactory emf;
    private static UserFacade facade;

    private static User u1;
    private static User u2;
    private static LibraryItem li1;

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = UserFacade.getUserFacade(emf);
    }

    @BeforeEach
    void setUp() {
        EntityManager em = emf.createEntityManager();

        u1 = new User("test-man", "pwd");
        u2 = new User("test-mann", "pwd");
        li1 = new LibraryItem("OL679360W");

        u2.addToLibrary(li1);

        try {
            em.getTransaction().begin();
            em.persist(u1);
            em.persist(u2);
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

    @Test
    void addBook() throws IOException {
        LibraryItemDTO item = new LibraryItemDTO("OL679360W");
        facade.addBook(u1.getUserName(), item);

        EntityManager em = emf.createEntityManager();
        User user = em.find(User.class, u1.getUserName());
        assertEquals(item.getBookKey(), user.getLibraryItems().get(0).getBookKey());
        em.close();
    }

    @Test
    void addBook_existing() {
        WebApplicationException e = assertThrows(WebApplicationException.class, () -> {
            facade.addBook(u2.getUserName(), new LibraryItemDTO(li1));
        });
        assertEquals(409, e.getResponse().getStatus());
        assertEquals("Item already in library", e.getMessage());
    }

    @Test
    void getLibrary() throws IOException, ExecutionException, InterruptedException {
        assertEquals(0, facade.getLibrary(u1.getUserName()).getSize());
        LibraryItemDTO item = new LibraryItemDTO("OL679360W");
        facade.addBook(u1.getUserName(), item);
        assertEquals(1, facade.getLibrary(u1.getUserName()).getSize());
    }

    @Test
    void updateBook() {
        assertEquals("TO-READ", li1.getStatus());
        LibraryItemDTO newItem = new LibraryItemDTO("OL679360W", "READING", 4);
        facade.updateBook(u2.getUserName(), newItem);

        EntityManager em = emf.createEntityManager();
        User user = em.find(User.class, u2.getUserName());
        assertEquals(newItem.getStatus(), user.getLibraryItems().get(0).getStatus());
        assertEquals(newItem.getRating(), user.getLibraryItems().get(0).getRating());
    }

    @Test
    void updateBook_badKey() {
        assertEquals("TO-READ", li1.getStatus());
        LibraryItemDTO newItem = new LibraryItemDTO("badKey", "READING", 4);
        WebApplicationException e = assertThrows(WebApplicationException.class, () -> {
            facade.updateBook(u2.getUserName(), newItem);
        });
        assertEquals(404, e.getResponse().getStatus());
        assertEquals("Item not found in your library", e.getMessage());
    }

    @Test
    void deleteBook() throws IOException, ExecutionException, InterruptedException {
        assertEquals(1, facade.getLibrary(u2.getUserName()).getSize());
        facade.deleteBook(u2.getUserName(), li1.getBookKey());
        assertEquals(0, facade.getLibrary(u2.getUserName()).getSize());
    }

    @Test
    void deleteBook_badKey() throws IOException, ExecutionException, InterruptedException {
        assertEquals(1, facade.getLibrary(u2.getUserName()).getSize());
        WebApplicationException e = assertThrows(WebApplicationException.class, () -> {
            facade.deleteBook(u2.getUserName(), "badKey");
        });
        assertEquals(404, e.getResponse().getStatus());
        assertEquals("Item not found in your library", e.getMessage());
    }
}
