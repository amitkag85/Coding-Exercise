package com.city.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.city.controller.CityController;

@ExtendWith(SpringExtension.class)
@WebMvcTest({CityController.class})
public class CityServiceTest {
	
	

}
