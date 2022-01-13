package dtos;

import entities.Trip;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TripDTO {

    private long id;
    private LocalDateTime dateTime;
    private String name;
    private String location;
    private Long duration;
    private List<String> packingList;

    public TripDTO(Trip entity) {
        this.dateTime = entity.getDateTime();
        this.name = entity.getName();
        this.location = entity.getLocation();
        this.duration = entity.getDuration();
        this.packingList = entity.getPackingList();
    }

    public TripDTO(LocalDateTime dateTime, String name, String location, Long duration, List<String> packingList) {
        this.dateTime = dateTime;
        this.name = name;
        this.location = location;
        this.duration = duration;
        this.packingList = packingList;
    }

    public static List<TripDTO> getDTOs(List<Trip> entities) {
        List<TripDTO> dtos = new ArrayList<>();
        entities.forEach(e -> dtos.add(new TripDTO(e)));
        return dtos;
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Long getDuration() {
        return duration;
    }

    public List<String> getPackingList() {
        return packingList;
    }
}
