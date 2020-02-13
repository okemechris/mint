package com.djbabs.cardservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.djbabs.cardservice.entities.Card;
import com.djbabs.cardservice.entities.Hit;
import com.djbabs.cardservice.pojo.GenericResponse;
import com.djbabs.cardservice.services.CardService;
import com.djbabs.cardservice.services.HitService;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping(path = "api")
public class CardController {

	
	@Autowired
	private CardService cardService;
	
	@Autowired
	private HitService hitService;
	
	
	@GetMapping("/card-scheme/verify/{cardNumber}")
    public GenericResponse veryCard(@PathVariable String cardNumber) {
		
		Card card = null;
		
        try {
			card =  cardService.getCardDetails(cardNumber);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
       //save or update hit
       if(card != null) {
    	   
    	   Hit hit = hitService.findByCardNumber(cardNumber).orElse(null);
    	   
    	   if(hit != null) {
    		   
    		   hit.setCount(hit.getCount() + 1);
    		   
    	   }else {
    		   
    		   hit = new Hit();
    		   hit.setCardNumber(cardNumber);
    		   hit.setCount(1);
    	   }
    	   
    	   hitService.save(hit);
    	   
       }
       
       return new GenericResponse(card == null, card);
        
        
    }
	
	
	
	
	
	
	
	
}//class
