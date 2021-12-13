package com.tns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tns.models.PaymentMethod;

@Repository
@Transactional
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
	boolean existsById(long id);
}
