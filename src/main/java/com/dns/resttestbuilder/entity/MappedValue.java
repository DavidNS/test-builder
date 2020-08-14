package com.dns.resttestbuilder.entity;

import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MappedValue {

	@Id
	@GeneratedValue
	Long id;
	
	Long userID;
	
	HashMap<String, String> keyVsValue;
}
