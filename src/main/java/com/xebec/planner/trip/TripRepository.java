package com.xebec.planner.trip;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

//Nome da entidade e o tipo de variável da chave principal
public interface TripRepository extends JpaRepository<Trip, UUID> {
}
