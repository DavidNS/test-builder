package com.dns.resttestbuilder.controller.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.dns.resttestbuilder.entity.StepKind;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StepRest<T> {
	
	Long id;
	
	Long userID;	
	
	String name;
	
	@NotNull(message = "Step order is mandatory")
	@Min(value = 0,message = "Min step order is 0")
	Long stepOrder;
	
	@NotNull(message = "Step kind is mandatory: EDIT_FIELD || MAP_FIELD || SEND_REQUEST || SEND_MAIN_REQUEST")
	StepKind stepKind;
	
	@NotNull(message = "Step model is mandatory")
	T stepModel;
}
