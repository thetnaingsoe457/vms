package com.tns.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tns.models.VoucherDTO;
import com.tns.security.services.VoucherService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/voucher")
@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
public class VoucherController {
	@Autowired
	private VoucherService service;

	@GetMapping("/all")
	List<VoucherDTO> all() {
		return service.findAllVoucherDTO();
	}

	@PostMapping("/create")
	VoucherDTO newVoucher(@RequestBody VoucherDTO voucherDTO) {
		return service.saveVoucher(voucherDTO);
	}

	@GetMapping("/get/{id}")
	VoucherDTO getById(@PathVariable Long id) {
		return service.findVoucherDTOById(id);
	}

	@PutMapping("/update/{id}")
	VoucherDTO updateVoucher(@RequestBody VoucherDTO voucherDTO, @PathVariable Long id) {
		return service.updateVoucher(voucherDTO, id);
	}

	@DeleteMapping("/deactivate/{id}")
	void deactivateVoucher(@PathVariable Long id) {
		service.deactiveVoucher(id);
	}
}
