package com.dns.resttestbuilder.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dns.resttestbuilder.configuration.DefaultData;
import com.dns.resttestbuilder.data.User;
import com.dns.resttestbuilder.repository.UserRepository;

@RestController
@RequestMapping("/user/")
public class UserService {

	@Autowired
	DefaultData defaultData;
	
	@Autowired
	UserRepository repository;

	@GetMapping
	List<User> all() {
		return repository.findAll();
	}

	@PostMapping
	User newEmployee(@RequestBody User newEmployee) {
		return repository.save(newEmployee);
	}

	@GetMapping("{id}")
	User one(@PathVariable Long id) throws Exception {

		return repository.findById(id).orElseThrow(() -> new Exception());
	}

	@PutMapping
	User 
	replaceEmployee(@RequestBody User user) {
		return repository.findByName(user.getName()).stream().map(userDB -> {
			return userDB;
		}).findFirst().orElseGet(()->{
			return repository.save(user);
		});
	}

	@DeleteMapping("{id}")
	void deleteEmployee(@PathVariable Long id) {
		repository.deleteById(id);
	}

}
