package com.tns.security.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import com.tns.exception.CreditCardException;
import com.tns.models.CreditCard;

@Service
public class CreditCardService {

	public void validate(final CreditCard card) throws CreditCardException {
		this.validateNumber(card.getNumber());
		this.validateCardType(card.getNumber());
		// this.validateVerificationDigit(card.getNumber());
		this.validateExpiryDate(card.getExpiration());
		this.validateSecurityKey(card.getSecurityCode());
	}

	protected void validateVerificationDigit(final String number) throws CreditCardException {
		List<Integer> digits = Arrays.asList(number.replaceAll("\\s", "").split("")).stream().map(Integer::parseInt)
				.collect(Collectors.toList());

		Integer oddDigitsSum = IntStream.range(0, digits.size() - 1).filter(i -> i % 2 != 0).map(i -> {
			int doubledValue = 2 * digits.get(i);
			int x1 = doubledValue / 10;
			int x2 = doubledValue % 10;
			return x1 + x2;
		}).sum();

		Integer evenDigitsSum = IntStream.range(0, digits.size() - 1).filter(i -> i % 2 == 0).map(i -> digits.get(i))
				.sum();

		Integer totalSum = oddDigitsSum + evenDigitsSum;

		Integer checkDigit = 10 - (totalSum % 10);

		if (!digits.get(digits.size() - 1).equals(checkDigit)) {
			throw new CreditCardException("Invalid verification digit for number: " + number);
		}
	}

	protected void validateCardType(final String number) throws CreditCardException {
		String numberNoSpace = number.replaceAll("\\s", "");
		int firstDigit = Character.getNumericValue(numberNoSpace.charAt(0));

		if (firstDigit < 4 || firstDigit > 5) {
			throw new CreditCardException("Invalid card type for number: " + number);
		}

		int secondDigit = Character.getNumericValue(numberNoSpace.charAt(1));

		if (firstDigit == 5) {
			if (secondDigit < 1 || secondDigit > 5) {
				throw new CreditCardException("Invalid card type for number: " + number);
			}
		}
	}

	protected void validateNumber(final String number) throws CreditCardException {
		// remove spaces from a card number
		String numberNoSpace = number.replaceAll("\\s", "");

		// check that a card number is 16 chars long
		if (numberNoSpace.length() != 16) {
			throw new CreditCardException("Invalid number length for number: " + number);
		}

		// check that every card number char is a digit
		String[] numberArr = numberNoSpace.split("");
		try {
			IntStream.range(0, numberArr.length).forEach(i -> {
				Integer.parseInt(numberArr[i]);
			});
		} catch (NumberFormatException e) {
			throw new CreditCardException("Invalid digits for number: " + number);
		}
	}

	protected void validateExpiryDate(final String date) throws CreditCardException {
		try {
			DateFormat informat = new SimpleDateFormat("MM/yy");
			DateFormat outformat = new SimpleDateFormat("MM/yyyy");

			String formattedDate = "1/" + outformat.format(informat.parse(date));

			DateTimeFormatter df = DateTimeFormatter.ofPattern("d/MM/yyyy");

			LocalDate cardDate = LocalDate.parse(formattedDate, df);
			LocalDate currentDate = LocalDate.now();

			if (cardDate.isBefore(currentDate)) {
				throw new CreditCardException("Invalid, date in the past : " + date);
			}
		} catch (DateTimeParseException | ParseException e) {
			throw new CreditCardException("Invalid date format: " + date);
		}
	}

	protected void validateSecurityKey(final String key) throws CreditCardException {
		try {
			if (Pattern.matches("^([0-9][0-9]|[0-9])$", key)) {
				throw new CreditCardException("Invalid Security key format: " + key);
			}
		} catch (Exception e) {
			throw new CreditCardException("Invalid Security key format: " + key);
		}
	}
}
