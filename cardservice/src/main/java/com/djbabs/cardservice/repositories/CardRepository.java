package com.djbabs.cardservice.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.djbabs.cardservice.entities.Card;

public interface CardRepository extends CrudRepository<Card,Long>{

	Optional<Card> findByNumber(String number);
}
