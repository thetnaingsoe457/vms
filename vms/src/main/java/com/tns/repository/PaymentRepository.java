package com.tns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tns.models.Payment;

@Repository
@Transactional
public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
