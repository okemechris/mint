package com.djbabs.cardservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@RequestMapping(path = "card-scheme")
public class CardController {

	@Value(value = "${card.kafka.topic}")
	private String topic;
	
	@Autowired
	private CardService cardService;

	@Autowired
	private HitService hitService;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	private ObjectMapper objectMapper = new ObjectMapper();

	@GetMapping("/verify/{cardNumber}")
	public GenericResponse veryCard(@PathVariable String cardNumber) {

		Card card = null;
		CardMessage message = new CardMessage();

		try {
			card = cardService.getCardDetails(cardNumber);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		doCardJob(card,message,cardNumber);

		return new GenericResponse(card != null, message);

	}

	void doCardJob(Card card, CardMessage message, String cardNumber) {
		
		if (card != null) {

			Hit hit = hitService.findByCardNumber(cardNumber).orElse(null);

			if (hit != null) {
				// if its valid increase count
				if (card.isHasData())
					hit.setCount(hit.getCount() + 1);

			} else {

				hit = new Hit();
				hit.setCardNumber(cardNumber);
				// if its valid increase count
				if (card.isHasData())
					hit.setCount(1);
			}

			hitService.save(hit);

			// send kafka message
			message.setBank(card.getBank());
			message.setScheme(card.getScheme());
			message.setType(card.getType());

			try {
				kafkaTemplate.send(topic, objectMapper.writeValueAsString(message));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}// class
