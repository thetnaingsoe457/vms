package com.tns.service;

import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tns.model.Voucher;
import com.tns.model.VoucherStatus;
import com.tns.repository.VoucherRepository;

@Service
public class PromoCodeService {
	private static final Logger logger = LoggerFactory.getLogger(PromoCodeService.class);

	@Autowired
	private VoucherRepository voucherRepo;

	@Autowired
	private QRCodeGenerationService qrCodeService;

	private String generatedNumeric = null;
	private String generatedCharacter = null;
	private String promoCode = null;

	public void generate() {
		List<Voucher> voucherList = voucherRepo.findByStatus(VoucherStatus.Paid);
		for (Voucher v : voucherList) {
			v.setStatus(VoucherStatus.Active);
			// Generate Promo Code
			promoCode = generatePromoCode();

			// Duplicate Code Checking
			while (voucherRepo.existsByPromoCode(promoCode)) {
				promoCode = generatePromoCode();
			}
			v.setPromoCode(promoCode);
			logger.info(promoCode + " : Generated Promo Code.");

			// Generate QR Code
			v.setImage(qrCodeService.generate(promoCode));

			voucherRepo.save(v);
			logger.info(promoCode + " : Updated in Database.");
		}
		logger.info(voucherList.size() + " promo codes are generated.");
		voucherList.forEach(v -> {
			logger.info(v.getPromoCode());
		});
	}

	private String generatePromoCode() {
		generatedNumeric = RandomStringUtils.randomNumeric(6);
		generatedCharacter = RandomStringUtils.randomAlphabetic(5).toUpperCase();
		promoCode = generatedNumeric + generatedCharacter;
		return promoCode;
	}
}
