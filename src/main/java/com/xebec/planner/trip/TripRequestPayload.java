package com.xebec.planner.trip;

/*Sempre que tenho dados que são imutáveis, isto é, que não precisarão de ser alterados depois de serem criados, posso
simplesmente usar as classes record. Essas classes já disponibilizam getters, setters e por aí fora*/

import java.util.List;

public record TripRequestPayload(String destination,
                                 String starts_at,
                                 String ends_at,
                                 List<String> emails_to_invite,
                                 String owner_email,
                                 String owner_name){
}
