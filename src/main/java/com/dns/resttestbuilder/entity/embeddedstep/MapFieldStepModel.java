package com.dns.resttestbuilder.entity.embeddedstep;

import java.util.List;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MapFieldStepModel   {
	
	@NotEmpty(message = "A least one JSON or reference to another input is required")
	List<String> inJson;
	
	@NotEmpty(message = "Map field step must contains at least one entry")
	@ElementCollection(fetch = FetchType.EAGER)
	Map<String, String> outPlainKeyVsMapCombination;
}
