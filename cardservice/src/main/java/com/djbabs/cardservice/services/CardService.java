package com.djbabs.cardservice.services;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import com.djbabs.cardservice.entities.Card;
import com.djbabs.cardservice.interfaces.CardDataProvider;
import com.djbabs.cardservice.interfacesImpl.BintCardDataProvider;
import com.djbabs.cardservice.repositories.CardRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CardService {

	private CardRepository cardRepository;

	private CardDataProvider dataProvider;

	public CardService(CardRepository cardRepository) {
		this.cardRepository = cardRepository;
		this.dataProvider = new BintCardDataProvider();
	}

	public Optional<Card> findById(Long id) {
		return cardRepository.findById(id);
	}

	public Optional<Card> findByNumber(String number) {
		return cardRepository.findByNumber(number);
	}

	public List<Card> findAll() {

		return (List<Card>) cardRepository.findAll();
	}

	public Card save(Card card) {

		return cardRepository.save(card);

	}

	public Card getCardDetails(String number) throws JsonMappingException, JsonProcessingException {

		Card card = findByNumber(number).orElse(null);

		if (card != null)
			return card;

		ResponseEntity<String> response = null;

		try {

			response = dataProvider.verifyCard(number);

		} catch (HttpStatusCodeException exception) {

			// check if response is not found and save card details any ways
			if (exception.getStatusCode().value() == 404) {
				card = new Card();
				card.setNumber(number);
				card.setHasData(false);
				card.setScheme(null);
				card.setType(null);
				card.setBank(null);

				return save(card);

			}

			return null;
		}

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(response.getBody());
		JsonNode scheme = root.path("scheme");
		JsonNode type = root.path("type");
		JsonNode bank = root.path("bank");
		JsonNode bankName = bank.path("name");

		// set card details
		card = new Card();
		card.setNumber(number);
		card.setScheme(scheme.textValue());
		card.setType(type.textValue());
		card.setHasData(true);
		card.setBank(bankName.textValue());

		return save(card);

	}

}
