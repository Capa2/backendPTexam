package utils;


import entities.Role;
import entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class SetupTestUsers {

    public static void main(String[] args) {

        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        // change passwords
        User user = new User(
                "user@gmail.com",
                "user",
                "Alexendria Ivinalititavitch",
                "Guldbergsgade 33, 3. t.v. 2200, København, Danmark",
                "41029210",
                1999,
                "female");
        User admin = new User(
                "admin@gmail.com",
                "admin",
                "Gregory Illinivich",
                "Rolighedsvej 12, st. t.v. 2100 København, Danmark",
                "81820512",
                1969,
                "male");
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
        System.out.println("PW: " + user.getPassword());
        System.out.println("Testing user with OK password: " + user.verifyPassword("user"));
        System.out.println("Testing user with wrong password: " + user.verifyPassword("wong"));
        System.out.println("Created TEST Users");
    }
}
