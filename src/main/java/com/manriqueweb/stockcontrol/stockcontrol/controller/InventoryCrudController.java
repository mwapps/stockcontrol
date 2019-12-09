package com.manriqueweb.stockcontrol.stockcontrol.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.manriqueweb.stockcontrol.stockcontrol.dto.MovementResponse;
import com.manriqueweb.stockcontrol.stockcontrol.dto.MovementsResponse;
import com.manriqueweb.stockcontrol.stockcontrol.dto.ProductResponse;
import com.manriqueweb.stockcontrol.stockcontrol.dto.ProductsResponse;
import com.manriqueweb.stockcontrol.stockcontrol.entity.Product;
import com.manriqueweb.stockcontrol.stockcontrol.service.InventoryCrudServise;

@RestController
@RequestMapping(value = "/inventory")
public class InventoryCrudController {
	
	@Autowired
	private InventoryCrudServise inventoryCrudServise;

	//Product CRUD methods
	@GetMapping(value = "/product/{id}")
	public ProductResponse retrieveProduct(@PathVariable Long id) {
		return inventoryCrudServise.retrieveProduct(id);
	}
	
	@GetMapping(value = "/products/bydescription")
	public ProductsResponse retrieveByDescription(@RequestParam(name = "description", required = true) String description) {
		return inventoryCrudServise.retrieveByDescription(description);
	}

	@GetMapping(value = "/products")
	public ProductsResponse retrieveAllProducts() {
		return inventoryCrudServise.retrieveAllProducts();
	}

	@PostMapping(value = "/product")
	public ProductResponse addNewProduct(@RequestBody Product product) {
		return inventoryCrudServise.addNewProduct(product);
	}

	@PatchMapping(value = "/product")
	public ProductResponse updateProduct(@RequestBody Product product) {
		return inventoryCrudServise.updateProduct(product);
	}

	@DeleteMapping(value = "/product/{id}")
	public ProductResponse deleteProduct(@PathVariable Long id) {
		return inventoryCrudServise.deleteProduct(id);
	}

	//Movement CRUD methods
	@GetMapping(value = "/movement/{id}")
	public MovementResponse retrieveMovement(@PathVariable Integer id) {
		return inventoryCrudServise.retrieveMovement(id);
	}
	
	@GetMapping(value = "/movements")
	public MovementsResponse retrieveAllMovements() {
		return inventoryCrudServise.retrieveAllMovements();
	}
	
	@GetMapping(value = "/movements/bydatesrange")
	public MovementsResponse retrieveBetweenDate(@RequestParam(name = "fromDate", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate, @RequestParam(name = "toDate", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate) {
		return inventoryCrudServise.retrieveBetweenDate(fromDate, toDate);
	}
}
