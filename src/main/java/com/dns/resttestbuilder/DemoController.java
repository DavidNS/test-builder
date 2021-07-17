package com.dns.resttestbuilder;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class DemoController {
	
	@GetMapping
	String base() {
		return "Hellooooooooo";
	}
	
	@GetMapping("{something}")
	String extended(@PathVariable String something) {
		return "Hellooooooooo "+something;
	}
}
