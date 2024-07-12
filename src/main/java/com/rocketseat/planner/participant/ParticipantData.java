package com.rocketseat.planner.participant;

import java.util.UUID;

//objecto usado somente para a transferência de dados entre as entidades do sistema
public record ParticipantData(UUID id, String name, String email, boolean isConfirmed) {
}
