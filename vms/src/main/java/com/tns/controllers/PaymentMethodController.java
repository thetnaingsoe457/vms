package com.tns.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tns.exception.DataNotFoundException;
import com.tns.models.PaymentMethod;
import com.tns.security.services.PaymentMethodService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/paymentmethods")
public class PaymentMethodController {
	@Autowired
	private PaymentMethodService service;

	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<PaymentMethod> getAllMethods() {
		List<PaymentMethod> list = service.findAll();
		return list;
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getArticleById(@PathVariable("id") Long id) {
		PaymentMethod paymentMethod = service.get(id)
				.orElseThrow(() -> new DataNotFoundException(id, "Payment Method is not in database!"));
		return ResponseEntity.ok(paymentMethod);
	}
}
