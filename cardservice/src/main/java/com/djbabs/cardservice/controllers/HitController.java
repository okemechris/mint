package com.djbabs.cardservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.djbabs.cardservice.entities.Card;
import com.djbabs.cardservice.pojo.GenericResponse;
import com.djbabs.cardservice.services.HitService;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping(path = "api")
public class HitController {

	@Autowired
	private HitService hitService;
	
	
	@GetMapping("/card-scheme/stats")
    public GenericResponse getHits(@PathVariable String cardNumber) {
		
		Card card = null;
		
        try {
			card =  cardService.getCardDetails(cardNumber);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
       return new GenericResponse(card == null, card);
        
        
    }
	
}
