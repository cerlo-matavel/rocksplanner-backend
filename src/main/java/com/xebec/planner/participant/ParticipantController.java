package com.xebec.planner.participant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/participants")
public class ParticipantController{

    @Autowired
    private ParticipantRepository participantRepository;

    @PostMapping("/{id}/confirm")
    public ResponseEntity<Participant> confirmParticipant(@PathVariable UUID id
            , @RequestBody ParticipantRequestPayload payload){
        Optional<Participant> participant = this.participantRepository.findById(id);

        if(participant.isPresent()) {
            Participant confirmedParticipant = participant.get();

            confirmedParticipant.setConfirmed(true);
            confirmedParticipant.setName(payload.name());

            this.participantRepository.save(confirmedParticipant);

            return ResponseEntity.ok(confirmedParticipant);
        }
        return ResponseEntity.notFound().build();
    }
}
