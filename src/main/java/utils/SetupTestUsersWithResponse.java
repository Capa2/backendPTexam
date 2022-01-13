package utils;


import entities.Role;
import entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class SetupTestUsersWithResponse {

    public static void main(String[] args) {
        System.out.println("Populator initialized. Call populate() manually.");
    }

    public String populate() {
        try {
            EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
            EntityManager em = emf.createEntityManager();
            // change passwords
            User user = new User("user", "user", "Guldbergsgade 33, 3. t.v. 2200 København, Danmark", "41029210",
                    "techboi93@gmail.com", 1993, "male");
            User admin = new User("admin", "admin", "Rolighedsvej 12, st. t.v. 2100 København, Danmark", "81820512",
                    "mainguy4@gmail.com", 2001, "male");
            //User both = new User("user_admin", "test3");

            em.getTransaction().begin();
            Role userRole = new Role("user");
            Role adminRole = new Role("admin");
            user.addRole(userRole);
            admin.addRole(adminRole);
            //both.addRole(userRole);
            //both.addRole(adminRole);
            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
            //em.persist(both);
            em.getTransaction().commit();
            System.out.println("PW: " + user.getUserPass());
            System.out.println("Testing user with OK password: " + user.verifyPassword("user"));
            System.out.println("Testing user with wrong password: " + user.verifyPassword("wong"));
            System.out.println("Created TEST Users");
            return "Successfully created test users";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
