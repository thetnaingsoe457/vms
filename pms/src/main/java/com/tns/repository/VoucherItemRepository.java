package com.tns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tns.model.VoucherItem;

@Repository
public interface VoucherItemRepository extends JpaRepository<VoucherItem, Long> {
	boolean existsById(long id);
}
