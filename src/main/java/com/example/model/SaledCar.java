package com.example.model;

import java.math.BigDecimal;

import com.example.enums.CurrencyType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "saled_car",uniqueConstraints = {@UniqueConstraint(columnNames = {"gallerist_id" , "car_id" , "customer_id"},name="uq_gallerist_car_customer")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaledCar extends BaseEntity {

	@ManyToOne
	private Gallerist gallerist;

	@ManyToOne
	private Car car;

	@ManyToOne
	private Customer customer;

	/** Amount taken from the customer's account, in the account's currency. */
	private BigDecimal paidAmount;

	@Enumerated(EnumType.STRING)
	private CurrencyType paidCurrencyType;

	/** Units of the account currency paid per 1 unit of the car currency; 1 when they are the same. */
	private BigDecimal exchangeRate;
}
