package com.djbabs.cardservice.interfacesImpl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.djbabs.cardservice.interfaces.CardDataProvider;
import com.djbabs.cardservice.utils.Apis;

public class BintCardDataProvider  implements CardDataProvider{

	RestTemplate restTemplate ;
	
	public BintCardDataProvider() {
		
		restTemplate = new RestTemplate();
		
	}
	
	@Override
	public ResponseEntity<String> verifyCard(String cardNumber) {
		return restTemplate.getForEntity(Apis.BINT_BASE_URL+"/"+cardNumber, String.class);
	}

	

}
