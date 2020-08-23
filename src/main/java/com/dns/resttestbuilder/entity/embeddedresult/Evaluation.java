package com.dns.resttestbuilder.entity.embeddedresult;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Evaluation {
	
	Boolean formatPassed;
	
	Boolean timePassed;
}
