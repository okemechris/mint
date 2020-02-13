package com.djbabs.cardservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.djbabs.cardservice.entities.Card;
import com.djbabs.cardservice.entities.Hit;
import com.djbabs.cardservice.pojo.GenericResponse;
import com.djbabs.cardservice.pojo.CardMessage;
import com.djbabs.cardservice.services.CardService;
import com.djbabs.cardservice.services.HitService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(path = "api")
public class CardController {

	@Autowired
	private CardService cardService;

	@Autowired
	private HitService hitService;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	private ObjectMapper objectMapper = new ObjectMapper();

	@GetMapping("/card-scheme/verify/{cardNumber}")
	public GenericResponse veryCard(@PathVariable String cardNumber) {

		Card card = null;
		CardMessage message = new CardMessage();
		
		try {
			card = cardService.getCardDetails(cardNumber);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// save or update hit
		if (card != null) {

			Hit hit = hitService.findByCardNumber(cardNumber).orElse(null);

			if (hit != null) {

				hit.setCount(hit.getCount() + 1);

			} else {

				hit = new Hit();
				hit.setCardNumber(cardNumber);
				hit.setCount(1);
			}

			hitService.save(hit);

			// send kafka message
			message.setBank(card.getBank());
			message.setScheme(card.getScheme());
			message.setType(card.getType());

			try {
				kafkaTemplate.send("com.ng.vela.even.card_verified", objectMapper.writeValueAsString(message));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return new GenericResponse(card != null, message);

	}

}// class
