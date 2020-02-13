package com.djbabs.cardservice.services;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.djbabs.cardservice.entities.Hit;
import com.djbabs.cardservice.repositories.HitRepository;

@Service
public class HitService {
	
	private HitRepository hitRepository;


	public HitService(HitRepository hitRepository) {
		this.hitRepository = hitRepository;
	}

	public Page<Hit> findAll(Pageable pageable) {
		return hitRepository.findAll(pageable);
	}
	
	public Optional<Hit> findByCardNumber(String cardNumber) {
		return hitRepository.findByCardNumber(cardNumber);
	}
	
	public Hit save(Hit hit) {

		return hitRepository.save(hit);
		
	}

	public Hit update(Hit hit) {

		return hitRepository.save(hit);
		
	}


}
