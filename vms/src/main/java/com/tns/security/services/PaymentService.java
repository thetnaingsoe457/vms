package com.tns.security.services;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tns.exception.CustomValidationException;
import com.tns.exception.DataNotFoundException;
import com.tns.models.CreditCard;
import com.tns.models.Payment;
import com.tns.models.PaymentDTO;
import com.tns.models.PaymentMethod;
import com.tns.models.Voucher;
import com.tns.models.VoucherStatus;
import com.tns.repository.PaymentMethodRepository;
import com.tns.repository.PaymentRepository;
import com.tns.repository.VoucherRepository;

@Service
public class PaymentService {
	@Autowired
	PaymentRepository repo;

	@Autowired
	VoucherRepository voucherRepo;

	@Autowired
	VoucherService voucherService;

	@Autowired
	PaymentMethodRepository paymentMethodRepo;

	@Autowired
	PaymentMethodService paymentMethodService;

	@Autowired
	CreditCardService creditCardService;

	@Autowired
	private ModelMapper modelMapper;

	Payment payment = null;
	PaymentDTO paymentDTO = null;
	CreditCard creditCard = null;
	double amount = 0;
	double discountAmount = 0;

	public Payment savePayment(Payment payment) {
		return repo.save(payment);
	}

	@Transactional
	public PaymentDTO payment(PaymentDTO paymentDTO) {
		// Convert to Payment
		payment = new Payment();
		convertToPayment(payment, paymentDTO);

		Voucher voucher = payment.getVoucher();
		// Check Voucher Status
		if (VoucherStatus.Deactivated.equals(voucher.getStatus()))
			throw new CustomValidationException(voucher.getId() + "", "Voucher is deactivated.");
		if (VoucherStatus.Paid.equals(voucher.getStatus()))
			throw new CustomValidationException(voucher.getId() + "", "Voucher is already paid.");

		// Credit Card Validation
		creditCard = new CreditCard(paymentDTO.getCardNumber(), paymentDTO.getExpiration(),
				paymentDTO.getSecurityCode(), paymentDTO.getCountry(), paymentDTO.getPostalCode());
		creditCardService.validate(creditCard);

		// Amount Validation
		amount = payment.getVoucher().getAmount();
		discountAmount = amount * payment.getPaymentMethod().getDiscountPercentage() / 100;
		if (payment.getAmount() != amount)
			throw new CustomValidationException(Double.toString(payment.getAmount()), "Amount is not valid.");
		if (payment.getDiscountAmount() != discountAmount)
			throw new CustomValidationException(Double.toString(payment.getDiscountAmount()),
					"Discount Amount is not valid.");
		if (payment.getPaidAmount() != (amount - discountAmount))
			throw new CustomValidationException(Double.toString(payment.getPaidAmount()), "Paid Amount is not valid.");

		// Update Voucher
		voucher.setPaymentMethod(payment.getPaymentMethod());
		voucher.setPaid(true);
		voucher.setStatus(VoucherStatus.Paid);
		voucherService.save(voucher);

		// Save Data
		savePayment(payment);
		return convertToDto(payment);
	}

	private PaymentDTO convertToDto(Payment payment) {
		paymentDTO = modelMapper.map(payment, PaymentDTO.class);
		paymentDTO.setVoucherId(payment.getVoucher().getId());
		paymentDTO.setPaymentMethodId(payment.getPaymentMethod().getId());
		return paymentDTO;
	}

	private Payment convertToPayment(Payment payment, PaymentDTO paymentDTO) {
		payment.setAmount(paymentDTO.getAmount());
		payment.setDiscountAmount(paymentDTO.getDiscountAmount());
		payment.setPaidAmount(paymentDTO.getPaidAmount());

		payment.setCardNumber(paymentDTO.getCardNumber());
		payment.setExpiration(paymentDTO.getExpiration());
		payment.setSecurityCode(paymentDTO.getSecurityCode());
		payment.setCountry(paymentDTO.getCountry());
		payment.setPostalCode(paymentDTO.getPostalCode());

		if (payment.getId() == null && paymentDTO.getPaymentMethodId() != null) {
			PaymentMethod paymentMethod = paymentMethodRepo.findById(paymentDTO.getPaymentMethodId())
					.orElseThrow(() -> new DataNotFoundException(paymentDTO.getPaymentMethodId(),
							"Payment Method is not found in database."));
			payment.setPaymentMethod(paymentMethod);
		}

		if (payment.getId() == null && paymentDTO.getVoucherId() != null) {
			payment.setVoucher(voucherRepo.findById(paymentDTO.getVoucherId()).orElseThrow(
					() -> new DataNotFoundException(paymentDTO.getVoucherId(), "Voucher is not found in database.")));
		}

		return payment;
	}
}
