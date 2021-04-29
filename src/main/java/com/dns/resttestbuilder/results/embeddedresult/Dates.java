package com.dns.resttestbuilder.results.embeddedresult;

import java.util.Date;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dates {
	
	Date requestDate;

	Date responseDate;

	Date parallTestEndDate;

	Date testEndDate;
	
}
