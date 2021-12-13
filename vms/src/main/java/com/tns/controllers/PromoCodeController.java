package com.tns.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tns.payload.request.PromoCodeRequest;
import com.tns.payload.request.PurchaseHistoryRequest;
import com.tns.payload.request.PurchaseRequest;
import com.tns.security.services.VoucherService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/promocode")
@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
public class PromoCodeController {
	@Autowired
	private VoucherService service;

	@PostMapping("/verify")
	ResponseEntity<?> verifyPromoCode(@RequestBody PromoCodeRequest promoCodeRequest) {
		return ResponseEntity.ok(service.verifyPromoCode(promoCodeRequest));
	}

	@PostMapping("/purchase")
	ResponseEntity<?> purchase(@RequestBody PurchaseRequest purchaseRequest) {
		return ResponseEntity.ok(service.purchase(purchaseRequest));
	}

	@PostMapping("/purchaseHistory")
	ResponseEntity<?> verifyPromoCode(@RequestBody PurchaseHistoryRequest purchaseHistoryRequest) {
		return ResponseEntity.ok(service.getPurchaseHistory(purchaseHistoryRequest));
	}
}
