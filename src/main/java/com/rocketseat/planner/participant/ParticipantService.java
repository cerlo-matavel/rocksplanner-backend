package com.rocketseat.planner.participant;

import com.rocketseat.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    public void registerParticipantsToTrip(List<String> participantsToInvite, Trip trip){
        List<Participant> participants = participantsToInvite.stream().map(email -> new Participant(email, trip))
                .collect(Collectors.toList());
        ;
        this.participantRepository.saveAll(participants);

        for (Participant participant : participants) {
            System.out.println(participant.getId());
        }
    }

    //Responsável por recuperar todos os participantes da viagem e mandar emails para estes
    public void triggerConfirmationEmailToParticipants(UUID tripId){}


    public void triggerConfirmationEmailToParticipant(String email){}

    public ParticipantCreateResponse registerParticipantToTrip(String email, Trip trip){
        Participant participant = new Participant(email, trip);
        this.participantRepository.save(participant);

        return new ParticipantCreateResponse(participant.getId());
    }

    public List<ParticipantData> getAllParticipantsFromTrip(UUID tripId) {
        return this.participantRepository.findByTripId(tripId).stream()
                .map( participant -> new ParticipantData(
                        participant.getId(),
                        participant.getName(),
                        participant.getEmail(),
                        participant.isConfirmed())).toList();
    }
}
