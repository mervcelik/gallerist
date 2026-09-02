package com.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "gallerist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Gallerist extends BaseEntity {
	
	private String firstName;

	private String lastName;
	
	@OneToOne
	private Address address;
}
