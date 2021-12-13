package com.tns.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tns.models.Voucher;

@Repository
@Transactional
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
	boolean existsById(long id);

	Voucher findByPromoCode(String promoCode);

	List<Voucher> findByPhoneNo(String phoneNo);
}
