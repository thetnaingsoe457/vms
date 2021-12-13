package com.tns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tns.models.VoucherItem;

@Repository
@Transactional
public interface VoucherItemRepository extends JpaRepository<VoucherItem, Long> {
	boolean existsById(long id);
}
