package com.tns.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.tns.model.Voucher;
import com.tns.model.VoucherStatus;

@RepositoryRestResource
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
	boolean existsById(long id);

	boolean existsByPromoCode(String promoCode);

	List<Voucher> findByStatus(VoucherStatus status);

}
