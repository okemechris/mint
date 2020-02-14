package com.djbabs.cardservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.djbabs.cardservice.entities.Hit;
import com.djbabs.cardservice.pojo.HitResponse;
import com.djbabs.cardservice.services.HitService;

@RestController
@RequestMapping(path = "card-scheme")
public class HitController {

	@Autowired
	private HitService hitService;
	
	
	@GetMapping("/stats")
    public HitResponse getHits(@RequestParam("start") int start, @RequestParam("limit") int limit ) {
		
		Page<Hit> page =  hitService.findAll(PageRequest.of(start,limit));
		
		HitResponse response = new HitResponse();
		
		response.setSuccess(true);
		response.setLimit(limit);
		response.setSize(page.getTotalPages());
		response.setStart(start);
		
		for(Hit h : page.getContent()) {
			response.getPayload().put(h.getCardNumber(), h.getCount());
		}
        
        return response;
    }
	
}
