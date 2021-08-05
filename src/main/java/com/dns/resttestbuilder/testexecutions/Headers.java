package com.dns.resttestbuilder.testexecutions;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Headers {
	
	Map<String, String> addHeadders;
	List<String> deleteHeadders;
}
