package com.dns.resttestbuilder.steps.embeddedstep;

import java.util.List;

import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.persistence.Lob;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.dns.resttestbuilder.JSONObjectConverter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MapFieldStepModel   {
	
	@NotEmpty(message = "A least one JSON or reference to another input is required")
	List<String> inJsons;
	
	@NotNull(message = "Map field step must contains at least one entry")
	@Convert(converter = JSONObjectConverter.class)
	@Lob
	Object outJson;
}
