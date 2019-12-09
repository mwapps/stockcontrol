package com.manriqueweb.stockcontrol.stockcontrol.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.manriqueweb.stockcontrol.stockcontrol.dto.RequestResponse;
import com.manriqueweb.stockcontrol.stockcontrol.service.UpdateInventoryService;

@RestController
@RequestMapping(value = "/stock")
public class UpdateInventoryController {
	
	@Autowired
	private UpdateInventoryService updateInventoryService;

	@PostMapping(value = "/updatestock")
	public RequestResponse updateInventory(
			final @RequestParam(name = "productId", required = true) Long productId, 
			final @RequestParam(name = "date", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime datemovement, 
			final @RequestParam(name = "quantity", required = true) Integer quantity, 
			final @RequestParam(name = "concept", required = true) String concept){
		
		return updateInventoryService.updateInventory(productId, datemovement, quantity, concept);
	}

}
