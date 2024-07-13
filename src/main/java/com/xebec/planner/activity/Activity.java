package com.xebec.planner.activity;

import com.xebec.planner.trip.Trip;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "activity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "occurs_at", nullable = false)
    private LocalDateTime occursAt;

    //Uma viagem pode ter varias actividades, porem, varias actividades
    // podem pertencer a mesma viagem
    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    public Activity(String title, String occursAt, Trip trip) {
        this.title = title;
        this.occursAt = LocalDateTime.parse(occursAt, DateTimeFormatter.ISO_DATE_TIME);
        this.trip = trip;
    }
}
