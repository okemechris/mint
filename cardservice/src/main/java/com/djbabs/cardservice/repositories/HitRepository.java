package com.djbabs.cardservice.repositories;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.djbabs.cardservice.entities.Hit;

public interface HitRepository extends PagingAndSortingRepository<Hit,Long>{

	Optional<Hit> findByCardNumber(String cardNumber);
}