package com.rocketseat.planner.trip;

import com.rocketseat.planner.participant.ParticipantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

        this.participantService.registerParticipantsToTrip(payload.emails_to_invite(), newTrip.getId());

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
}
