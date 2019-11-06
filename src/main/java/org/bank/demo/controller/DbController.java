package org.bank.demo.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/db")
public class DbController {
    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(DbController.class);
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String list() {
	   return "dbObjects";
	}
	
}
