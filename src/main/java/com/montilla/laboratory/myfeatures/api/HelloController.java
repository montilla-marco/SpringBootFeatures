package com.montilla.laboratory.myfeatures.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	
	@GetMapping("/hello")
	public String sayHello(String name) {
		return "Hello "+name;
	}
	
	
	@GetMapping("/admin/hello")
	public String sayAdminHello(String name) {
		return "Hello Admin "+name;
	}


}
