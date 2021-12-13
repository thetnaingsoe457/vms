package com.tns.security.services;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.tns.models.PaymentMethod;
import com.tns.repository.PaymentMethodRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PaymentMethodService {
	private final PaymentMethodRepository repo;

	@Cacheable(value = "allPaymentMethodCache")
	public List<PaymentMethod> findAll() {
		return repo.findAll();
	}

	@Cacheable(value = "paymentMethodCache")
	public Optional<PaymentMethod> get(long id) {
		return repo.findById(id);
	}
}
