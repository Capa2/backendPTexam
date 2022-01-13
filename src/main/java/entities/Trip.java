package entities;

import dtos.TripDTO;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "trip")
@Entity
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pid", nullable = false)
    private long id;

    @Column(name = "dateTime", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime dateTime;

    @Column(name = "name", nullable = false, length = 128)
    private String name;

    @Column(name = "location", nullable = false, length = 256)
    private String location;

    @Column(name = "duration", nullable = false, length = 64)
    private Long duration;

    @ElementCollection
    @Column(name = "packingList", nullable = false)
    private List<String> packingList;

    public Trip() {
    }

    public Trip(LocalDateTime dateTime, String name, String location, Long duration, List<String> packingList) {
        this.dateTime = dateTime;
        this.name = name;
        this.location = location;
        this.duration = duration;
        this.packingList = packingList;
    }

    public Trip(TripDTO dto) {
        this.dateTime = dto.getDateTime();
        this.name = dto.getName();
        this.location = dto.getLocation();
        this.duration = dto.getDuration();
        this.packingList = dto.getPackingList();
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
