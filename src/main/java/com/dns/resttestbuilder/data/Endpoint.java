package com.dns.resttestbuilder.data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Endpoint {
	
	@Id
	@GeneratedValue
	Long id;
	
	String alias;
	
	@NotNull
	String url;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	String method;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	JSONModel inModel;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	JSONModel outModel;
	
	
}
