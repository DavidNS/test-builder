package com.dns.resttestbuilder.entity;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Step {

	
	@Id
	@GeneratedValue
	Long id;
	
	Long userID;
	
	String name;
	
	@NotNull(message = "Step order is mandatory")
	@Min(value = 0,message = "Min step order is 0")
	Long stepOrder;

	
	@NotNull(message = "Step kind is mandatory: EDIT_FIELD,MAP_FIELD,SEND_REQUEST,SEND_MAIN_REQUEST")
	@Enumerated(EnumType.STRING)
	StepKind stepKind;
	

	
	@NotNull(message = "Step model is mandatory")
	@Convert(converter = JSONObjectConverter.class)
	@Lob
	Object stepModel;

}
