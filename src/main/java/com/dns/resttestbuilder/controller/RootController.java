package com.dns.resttestbuilder.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class RootController {
	
	@GetMapping
	String all() {
		return "Holaaaaaaaaaaaaaaaaaaaaa";
	}
}
