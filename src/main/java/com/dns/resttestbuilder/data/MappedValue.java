package com.dns.resttestbuilder.data;

import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
	
	String name;
	
	@Enumerated(EnumType.STRING)
	MapKind mapKind;
	
	HashMap<String, String> keyVsValue;
}
