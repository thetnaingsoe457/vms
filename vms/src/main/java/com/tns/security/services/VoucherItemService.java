package com.tns.security.services;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.tns.exception.DataNotFoundException;
import com.tns.models.VoucherItem;
import com.tns.repository.VoucherItemRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VoucherItemService {
	private final VoucherItemRepository repo;

	@Cacheable(value = "allVoucherItemCache", unless = "#result.size() == 0")
	public List<VoucherItem> findAll() {
		return repo.findAll();
	}

	@Cacheable(value = "voucherItemCache", key = "#id")
	public VoucherItem findById(long id) {
		return repo.findById(id)
				.orElseThrow(() -> new DataNotFoundException(id, "Voucher Item is not found in database."));
	}

	@Caching(put = { @CachePut(value = "voucherItemCache", key = "#voucherItem.id") }, evict = {
			@CacheEvict(value = "allVoucherItemCache", allEntries = true) })
	public VoucherItem save(VoucherItem voucherItem) {
		return repo.save(voucherItem);
	}
}
