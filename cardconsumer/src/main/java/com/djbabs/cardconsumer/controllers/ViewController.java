package com.djbabs.cardconsumer.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.djbabs.cardconsumer.pojo.HitResponse;

@Controller
public class ViewController {

	@Value(value = "${welcome.message}")
	private String message;
	@Value(value = "${server.base.url}")
	private String baseUrl;

	@GetMapping("/")
	public String main(Model model) {
		model.addAttribute("message", message);

		return "welcome"; // view
	}
	
	
	@GetMapping("/hits")
	public String getHits(Model model ,@RequestParam("start") Optional<Integer> start,@RequestParam("limit") Optional<Integer> limit) {
	
		int currentPage = start.orElse(1);
        int pageSize = limit.orElse(5);
        
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<HitResponse> response = restTemplate.getForEntity(baseUrl+"/card-scheme/stats?start="+(currentPage-1)+"&limit="+pageSize, HitResponse.class);
	 
		
		model.addAttribute("data", response.getBody());
		 
        int totalPages = response.getBody().getSize();
        
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
		

		return "hits"; // view
	}

}
