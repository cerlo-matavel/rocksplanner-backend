package com.rocketseat.planner.participant;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ParticipantService {

    public void registerParticipantsToTrip(List<String> participantsToInvite, UUID tripId){

    }

    //Responsável por recuperar todos os participantes da viagem e mandar emails para estes
    public void triggerConfirmationEmailToParticipants(UUID tripId){

    }
}
