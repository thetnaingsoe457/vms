package com.tns.security.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.tns.advice.DateUtils;
import com.tns.exception.CustomValidationException;
import com.tns.exception.DataNotFoundException;
import com.tns.models.EBuyType;
import com.tns.models.Voucher;
import com.tns.models.VoucherDTO;
import com.tns.models.VoucherItem;
import com.tns.models.VoucherStatus;
import com.tns.payload.request.PromoCodeRequest;
import com.tns.payload.request.PurchaseHistoryRequest;
import com.tns.payload.request.PurchaseRequest;
import com.tns.payload.response.PromoCodeResponse;
import com.tns.payload.response.PurchaseHistoryResponse;
import com.tns.payload.response.PurchaseHistoryVoucherResponse;
import com.tns.payload.response.PurchaseResponse;
import com.tns.repository.VoucherItemRepository;
import com.tns.repository.VoucherRepository;

@Service
public class VoucherService {
	@Autowired
	private VoucherRepository repo;

	@Autowired
	VoucherItemService voucherItemService;

	@Autowired
	VoucherItemRepository voucherItemRepo;

	@Autowired
	private ModelMapper modelMapper;

	List<Voucher> voucherList = null;
	VoucherItem voucherItem = null;
	Voucher voucher = null;
	VoucherDTO voucherDTO = null;

	@Cacheable(value = "allVoucherCache", unless = "#result.size() == 0")
	public List<Voucher> findAllVoucher() {
		return repo.findAll();
	}

	@Cacheable(value = "voucherCache", key = "#id")
	public Voucher findVoucherById(long id) {
		return repo.findById(id).orElseThrow(() -> new DataNotFoundException(id, "Voucher is not found in database."));
	}

	@Caching(put = { @CachePut(value = "voucherCache", key = "#voucher.id") }, evict = {
			@CacheEvict(value = "allVoucherCache", allEntries = true) })
	public Voucher save(Voucher voucher) {
		return repo.save(voucher);
	}

	public List<VoucherDTO> findAllVoucherDTO() {
		voucherList = findAllVoucher();
		return voucherList.stream().map(this::convertToDto).collect(Collectors.toList());
	}

	public VoucherDTO findVoucherDTOById(long id) {
		return convertToDto(findVoucherById(id));
	}

	@Transactional
	public VoucherDTO saveVoucher(VoucherDTO voucherDto) {
		voucher = new Voucher();
		voucher = convertToVoucher(voucher, voucherDto);

		// Validation
		validateVoucher(voucher);

		// Calculate
		calculateVoucher(voucher);

		// Update VoucherItem
		voucherItem = voucher.getVoucherItem();
		voucherItem.setInStock(voucherItem.getInStock() - voucher.getQuantity());
		voucherItem.setOutStock(voucherItem.getOutStock() + voucher.getQuantity());
		voucherItemService.save(voucherItem);

		// Save Data
		save(voucher);
		return convertToDto(voucher);
	}

	@Transactional
	public VoucherDTO updateVoucher(VoucherDTO voucherDto, Long id) {
		Voucher voucher = repo.findById(id)
				.orElseThrow(() -> new DataNotFoundException(id, "Voucher is not found in database."));
		voucher = convertToVoucher(voucher, voucherDto);

		// Validation
		validateVoucher(voucher);

		// Calculate
		calculateVoucher(voucher);

		// Update VoucherItem
		voucherItem = voucher.getVoucherItem();
		voucherItem.setInStock(voucherItem.getInStock() - voucher.getQuantity());
		voucherItem.setOutStock(voucherItem.getOutStock() + voucher.getQuantity());
		voucherItemService.save(voucherItem);

		// Update Data
		save(voucher);

		return convertToDto(voucher);
	}

	public PromoCodeResponse verifyPromoCode(PromoCodeRequest promoCodeRequest) {
		Voucher voucher = repo.findByPromoCode(promoCodeRequest.getPromoCode());
		if (voucher == null)
			throw new CustomValidationException(promoCodeRequest.getPromoCode(), "Promo Code is not found in database");
		return new PromoCodeResponse(voucher.getStatus().getName());
	}

	public PurchaseResponse purchase(PurchaseRequest purchaseRequest) {
		Voucher voucher = repo.findByPromoCode(purchaseRequest.getPromoCode());
		if (voucher == null)
			throw new CustomValidationException(purchaseRequest.getPromoCode(), "Promo Code is not found in database");
		// Update Status
		voucher.setStatus(VoucherStatus.Used);
		save(voucher);
		return new PurchaseResponse(purchaseRequest.getPromoCode(), voucher.getStatus().getName());
	}

	public PurchaseHistoryResponse getPurchaseHistory(PurchaseHistoryRequest purchaseHistoryRequest) {
		List<Voucher> voucherList = repo.findByPhoneNo(purchaseHistoryRequest.getPhoneNo());
		if (voucherList == null || voucherList.size() < 1)
			throw new CustomValidationException(purchaseHistoryRequest.getPhoneNo(),
					"There is no purchase history for the phone number.");

		List<Voucher> usedVoucherList = voucherList.stream().filter(v -> VoucherStatus.Used.equals(v.getStatus()))
				.collect(Collectors.toList());
		List<Voucher> unUsedVoucherList = voucherList.stream().filter(v -> VoucherStatus.Active.equals(v.getStatus()))
				.collect(Collectors.toList());

		return new PurchaseHistoryResponse(purchaseHistoryRequest.getPhoneNo(), changeToList(usedVoucherList),
				changeToList(unUsedVoucherList));
	}

	public List<PurchaseHistoryVoucherResponse> changeToList(List<Voucher> list) {
		List<PurchaseHistoryVoucherResponse> purchaseHistoryVoucherList = new ArrayList<>();
		list.forEach(v -> purchaseHistoryVoucherList
				.add(new PurchaseHistoryVoucherResponse(v.getStatus().toString(), v.getImage(), v.getPromoCode())));
		return purchaseHistoryVoucherList;
	}

	public VoucherDTO deactiveVoucher(long id) {
		voucher = repo.findById(id)
				.orElseThrow(() -> new DataNotFoundException(id, "Voucher is not found in database."));
		voucher.setStatus(VoucherStatus.Deactivated);

		// Update Data
		save(voucher);

		return convertToDto(voucher);
	}

	private VoucherDTO convertToDto(Voucher voucher) {
		voucherDTO = modelMapper.map(voucher, VoucherDTO.class);
		voucherDTO.setVoucherItemId(voucher.getVoucherItem().getId());
		return voucherDTO;
	}

	private void validateVoucher(Voucher voucher) {
		// Validate Voucher in update case
		if (voucher.getStatus() != null && VoucherStatus.Paid.equals(voucher.getStatus()))
			throw new CustomValidationException(voucher.getStatus().toString(), "Paid Voucher can not be edit.");
		if (voucher.getStatus() != null && VoucherStatus.Deactivated.equals(voucher.getStatus()))
			throw new CustomValidationException(voucher.getStatus().toString(), "Deactivated Voucher can not be edit.");

		voucherItem = voucher.getVoucherItem();
		int quantity = voucher.getQuantity();
		// check quantity
		if (quantity < 1) {
			throw new CustomValidationException("0", "At least one quantity is required.");
		}
		// check stock
		if (quantity > voucherItem.getInStock()) {
			throw new CustomValidationException(quantity + "",
					"Quantity exceeded InStock item, " + voucherItem.getInStock());
		}
		// check buytype and limit
		if (EBuyType.OnlyMeUsage.equals(voucher.getBuyType())) {
			if (quantity > voucherItem.getLimitedSelfQuantity())
				throw new CustomValidationException(quantity + "",
						"Quantity exceeded limited amount for OnlyMe Usage, " + voucherItem.getLimitedSelfQuantity());
		} else {
			if (quantity > voucherItem.getLimitedOtherQuantity())
				throw new CustomValidationException(quantity + "",
						"Quantity exceeded limited amount for GiftToOther Usage, "
								+ voucherItem.getLimitedOtherQuantity());
		}
	}

	private Voucher calculateVoucher(Voucher voucher) {
		// Business Logic
		voucher.setStatus(VoucherStatus.Submitted);
		voucher.setExpiryDate(DateUtils.addDay(new Date(), voucher.getVoucherItem().getExpiryDay()));
		voucher.setAmount(voucher.getVoucherItem().getAmountPerItem() * voucher.getQuantity());
		return voucher;
	}

	private Voucher convertToVoucher(Voucher voucher, VoucherDTO voucherDto) {
		voucher.setTitle(voucherDto.getTitle());
		voucher.setDescription(voucherDto.getDescription());
		voucher.setExpiryDate(voucherDto.getExpiryDate());
		voucher.setQuantity(voucherDto.getQuantity());
		voucher.setAmount(voucherDto.getAmount());
		voucher.setBuyType(voucherDto.getBuyType());
		voucher.setName(voucherDto.getName());
		voucher.setPhoneNo(voucherDto.getPhoneNo());

		voucher.setVoucherItem(voucherItemRepo.findById(voucherDto.getVoucherItemId())
				.orElseThrow(() -> new DataNotFoundException(voucherDto.getVoucherItemId(),
						"Voucher Item is not found in database.")));
		return voucher;
	}
}
