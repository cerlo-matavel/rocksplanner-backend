package com.rocketseat.planner.trip;

import com.rocketseat.planner.participant.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {

    private Logger logger = LoggerFactory.getLogger(TripController.class);
//    @Autowired
    private ParticipantService participantService;
//    @Autowired
    private TripRepository tripRepository;

    @Autowired
    public TripController(TripRepository tripRepository, ParticipantService participantService) {
        this.tripRepository = tripRepository;
        this.participantService = participantService;
    }

    @PostMapping
    public ResponseEntity<TripCreateResponsePayload> createTrip(@RequestBody TripRequestPayload payload){
        /*É preciso criar uma classe com dados imutáveis(Record) que será usada para representar os dados que
        serão recebidos no request*/

        Trip newTrip = new Trip(payload);

        logger.info("New trip created: " + payload);
        this.tripRepository.save(newTrip);

        this.participantService.registerParticipantsToTrip(payload.emails_to_invite(), newTrip);

        return ResponseEntity.ok(
                new TripCreateResponsePayload(newTrip.getId())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id){
        Optional<Trip> trip = this.tripRepository.findById(id);

        return trip.map(ResponseEntity::ok)
                .orElseGet( () -> ResponseEntity.notFound().build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID id, @RequestBody TripRequestPayload payload){
        Optional<Trip> trip = this.tripRepository.findById(id);

        if(trip.isPresent()){
            Trip updatedTrip = trip.get();
            updatedTrip.setEndsAt(LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
            updatedTrip.setStartsAt(LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
            updatedTrip.setDestination(payload.destination());

            this.tripRepository.save(updatedTrip);

            return ResponseEntity.ok(updatedTrip);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/confirm")
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID id){
        Optional<Trip> trip = this.tripRepository.findById(id);

        if(trip.isPresent()){
            Trip confirmedTrip = trip.get();
            confirmedTrip.setIsConfirmed(true);

            this.tripRepository.save(confirmedTrip);

            this.participantService.triggerConfirmationEmailToParticipants(id);

            return ResponseEntity.ok(confirmedTrip);
        }
        return trip.map(ResponseEntity::ok)
                .orElseGet( () -> ResponseEntity.notFound().build()
                );
    }

    @PostMapping("/{id}/invite")
    public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID id, @RequestBody ParticipantRequestPayload payload){
        Optional<Trip> trip = this.tripRepository.findById(id);

        if(trip.isPresent()){
            Trip rawTrip = trip.get();

            ParticipantCreateResponse participantResponse = this.participantService.registerParticipantToTrip(payload.email(), rawTrip);

            if (rawTrip.getIsConfirmed()) this.participantService.triggerConfirmationEmailToParticipant(payload.email());

            return ResponseEntity.ok(participantResponse);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantData>> getAllParticipants(@PathVariable UUID id){
        List<ParticipantData> participantList = this.participantService.getAllParticipantsFromTrip(id);

        return ResponseEntity.ok(participantList);
    }
}
