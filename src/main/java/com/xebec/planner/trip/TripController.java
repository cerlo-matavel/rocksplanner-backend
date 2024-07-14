package com.xebec.planner.trip;

import com.xebec.planner.activity.ActivityData;
import com.xebec.planner.activity.ActivityRequestPayload;
import com.xebec.planner.activity.ActivityResponse;
import com.xebec.planner.activity.ActivityService;
import com.xebec.planner.link.LinkData;
import com.xebec.planner.link.LinkRequestPayload;
import com.xebec.planner.link.LinkResponse;
import com.xebec.planner.link.LinkService;
import com.xebec.planner.participant.ParticipantCreateResponse;
import com.xebec.planner.participant.ParticipantData;
import com.xebec.planner.participant.ParticipantRequestPayload;
import com.xebec.planner.participant.ParticipantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {

    private Logger logger = LoggerFactory.getLogger(TripController.class);

    private ParticipantService participantService;
    private TripRepository tripRepository;
    private ActivityService activityService;
    private LinkService linkService;

    @Autowired
    public TripController(TripRepository tripRepository,
                          ParticipantService participantService,
                          ActivityService activityService,
                          LinkService linkService) {
        this.tripRepository = tripRepository;
        this.participantService = participantService;
        this.activityService = activityService;
        this.linkService = linkService;
    }


    // Trips
    @PostMapping
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestPayload payload){
        /*É preciso criar uma classe com dados imutáveis(Record) que será usada para representar os dados que
        serão recebidos no request*/

        Trip newTrip = new Trip(payload);

        logger.info("New trip created: " + payload);

        this.tripRepository.save(newTrip);

        this.participantService.registerParticipantsToTrip(payload.emails_to_invite(), newTrip);

        return ResponseEntity.ok(
                new TripCreateResponse(newTrip.getId())
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


    //Participant

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


    //Activities

    @PostMapping("/{id}/activities")
    public ResponseEntity<ActivityResponse> registerActivity(@PathVariable UUID id, @RequestBody ActivityRequestPayload payload){
        Optional<Trip> trip = this.tripRepository.findById(id);

        if(trip.isPresent()){
            Trip rawTrip = trip.get();

            ActivityResponse activityResponse = this.activityService.registerActivity(payload, rawTrip);

            return ResponseEntity.ok(activityResponse);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/activities")
    public ResponseEntity<List<ActivityData>> getAllActivities(@PathVariable UUID id){

        List<ActivityData> activityDataList = this.activityService.getAllActivitiesFromTrip(id);

        return ResponseEntity.ok(activityDataList);
    }


    //Links
    @PostMapping("/{id}/links")
    public ResponseEntity<LinkResponse> registerLink(@PathVariable UUID id, @RequestBody LinkRequestPayload payload){
        Optional<Trip> trip = this.tripRepository.findById(id);

        if(trip.isPresent()){
            Trip rawTrip = trip.get();

            LinkResponse linkResponse = this.linkService.registerLink(payload, rawTrip);

            return ResponseEntity.ok(linkResponse);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/links")
    public ResponseEntity<List<LinkData>> getAllLinks(@PathVariable UUID id){

        List<LinkData> linkDataList = this.linkService.getAllLinksFromTrip(id);

        return ResponseEntity.ok(linkDataList);
    }
}
