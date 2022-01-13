package facades;

import dtos.TripDTO;
import entities.Trip;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.WebApplicationException;

import utils.EMF_Creator;

public class TripFacade {

    private static TripFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private TripFacade() {
    }


    /**
     * @param _emf
     * @return an instance of this facade class.
     */
    public static TripFacade getTripFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new TripFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public TripDTO create(TripDTO tripDTO) {
        Trip trip = new Trip(tripDTO);
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(trip);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new TripDTO(trip);
    }

    public TripDTO joinTrip(long id, String email) throws WebApplicationException { //throws TripNotFoundException {
        EntityManager em = emf.createEntityManager();
        Trip trip = em.find(Trip.class, id);
        if (trip.isParticipant(email)) throw new WebApplicationException("Cannot add participant because it already " +
                "exists");
        trip.joinTrip(email);
        try {
            em.getTransaction().begin();
            em.merge(trip);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new TripDTO(trip);
    }

    public TripDTO leaveTrip(long id, String email) throws WebApplicationException { //throws TripNotFoundException {
        EntityManager em = emf.createEntityManager();
        Trip trip = em.find(Trip.class, id);
        if (!trip.isParticipant(email)) throw new WebApplicationException("Cannot remove participant because it " +
                "doesn't exists");
        trip.leaveTrip(email);
        try {
            em.getTransaction().begin();
            em.merge(trip);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new TripDTO(trip);
    }

    public boolean isParticipant(long id, String email) { //throws
        EntityManager em = emf.createEntityManager();
        Trip trip = em.find(Trip.class, id);
        return trip.isParticipant(email);
    }

    public TripDTO getById(long id) { //throws TripNotFoundException {
        EntityManager em = emf.createEntityManager();
        Trip trip = em.find(Trip.class, id);
//        if (trip == null)
//            throw new TripNotFoundException("The Trip entity with ID: "+id+" Was not found");
        return new TripDTO(trip);
    }

    public List<TripDTO> getAll() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Trip> query = em.createQuery("SELECT t FROM Trip t", Trip.class);
        List<Trip> trips = query.getResultList();
        return TripDTO.getDTOs(trips);
    }

    public TripDTO deleteById(long id) {
        EntityManager em = emf.createEntityManager();
        try {
            Trip trip = em.find(Trip.class, id);
            if (trip == null) {
                throw new WebApplicationException("Trip with id " + id + " doesn't exist", 404);
            }
            em.getTransaction().begin();
            em.remove(trip);
            em.getTransaction().commit();
            return new TripDTO(trip);
        } finally {
            em.close();
        }
    }

    public long getTripCount() {
        EntityManager em = getEntityManager();
        try {
            long tripCount = (long) em.createQuery("SELECT COUNT(t) FROM Trip t").getSingleResult();
            return tripCount;
        } finally {
            em.close();
        }
    }

    public static void main(String[] args) {
        emf = EMF_Creator.createEntityManagerFactory();
        TripFacade fe = getTripFacade(emf);
        fe.getAll().forEach(dto -> System.out.println(dto));
    }

}
