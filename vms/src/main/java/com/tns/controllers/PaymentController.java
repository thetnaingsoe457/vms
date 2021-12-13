package com.tns.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tns.models.PaymentDTO;
import com.tns.security.services.PaymentService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/payment")
@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
public class PaymentController {

	@Autowired
	PaymentService service;

	@PostMapping("/confirm")
	PaymentDTO confirmPayment(@RequestBody PaymentDTO paymentDTO) {
		return service.payment(paymentDTO);
	}
}
