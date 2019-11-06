package org.bank.demo.controller;


import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringEscapeUtils;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import org.bank.demo.domain.Branch;
import org.bank.demo.service.BranchService;
import org.bank.demo.exception.NotFoundException;

@Controller
@RequestMapping("/db")
public class BranchController {
    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(BranchController.class);

	@Autowired
	private BranchService branchService;
	
	@RequestMapping(value = "/branch", method = RequestMethod.GET)
	public String list(Model model) {
	   model.addAttribute("branch", branchService.findAll());
	   return "BranchList";
	}

	@RequestMapping(value = "/branch", method = RequestMethod.POST)
	public String searchList(@RequestParam("attr") String fieldName, @RequestParam("value") String value, Model model) {
		if(branchService.hasField(fieldName)) {
			return "redirect:/db/branch/" + StringEscapeUtils.escapeHtml(fieldName) + "/" + StringEscapeUtils.escapeHtml(value);
		} else {
			throw new RuntimeException("Attempting to search disallowed field: " + fieldName);
		}
	}
	
	@RequestMapping(value = "/branch/branchId/{branchId}", method = RequestMethod.GET)
	public String getBranchByBranchId(@PathVariable("branchId") BigDecimal branchId, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("branchId", Arrays.asList(branchId));
	   model.addAttribute("branch", branchService.findByFilter(filter));
	   return "BranchList";
	}
	
	@RequestMapping(value = "/branch/address/{address}", method = RequestMethod.GET)
	public String getBranchByAddress(@PathVariable("address") String address, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("address", Arrays.asList(address));
	   model.addAttribute("branch", branchService.findByFilter(filter));
	   return "BranchList";
	}
	
	@RequestMapping(value = "/branch/city/{city}", method = RequestMethod.GET)
	public String getBranchByCity(@PathVariable("city") String city, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("city", Arrays.asList(city));
	   model.addAttribute("branch", branchService.findByFilter(filter));
	   return "BranchList";
	}
	
	@RequestMapping(value = "/branch/name/{name}", method = RequestMethod.GET)
	public String getBranchByName(@PathVariable("name") String name, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("name", Arrays.asList(name));
	   model.addAttribute("branch", branchService.findByFilter(filter));
	   return "BranchList";
	}
	
	@RequestMapping(value = "/branch/state/{state}", method = RequestMethod.GET)
	public String getBranchByState(@PathVariable("state") String state, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("state", Arrays.asList(state));
	   model.addAttribute("branch", branchService.findByFilter(filter));
	   return "BranchList";
	}
	
	@RequestMapping(value = "/branch/zipCode/{zipCode}", method = RequestMethod.GET)
	public String getBranchByZipCode(@PathVariable("zipCode") String zipCode, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("zipCode", Arrays.asList(zipCode));
	   model.addAttribute("branch", branchService.findByFilter(filter));
	   return "BranchList";
	}
	
	@RequestMapping(value = "/branch/search/{matrixVariable}")
	public String getBranchByFilter(@MatrixVariable(pathVar="matrixVariable") Map<String,List<?>> filter, Model model) {
	   model.addAttribute("branch", branchService.findByFilter(filter));
	   return "BranchList";
	}
	
	@RequestMapping(value = "/branchView", method = RequestMethod.GET)
	public String getBranchByPrimaryKey(@RequestParam("branchId") BigDecimal branchId, Model model) {
		Branch branch =  branchService.findByPrimaryKey(branchId);
		if (branch == null) {
			throw new NotFoundException();
		}		
		model.addAttribute("branch", branch);
		return "BranchView";
	}	

	@RequestMapping(value = "/branch/add", method = RequestMethod.GET)
	public String addBranchGet(Model model) {
	   Branch branch = new Branch();
	   model.addAttribute("branch", branch);
	   return "BranchEdit";
	}	
	
	@RequestMapping(value = "/branch/add", method = RequestMethod.POST)
	public String addBranchPost(@ModelAttribute("branch") Branch branch, BindingResult result, HttpServletRequest request) {
		String[] suppressedFields = result.getSuppressedFields();
		if (suppressedFields.length > 0) {
			throw new RuntimeException("Attempting to bind disallowed fields: " + StringUtils.arrayToCommaDelimitedString(suppressedFields));
		}
		branchService.insert(branch);
		return "redirect:/db/branchView?branchId=" + branch.getBranchId();
	}	
	
	@RequestMapping(value = "/branchEdit", method = RequestMethod.GET)
	public String editBranchByPrimaryKeyGet(@RequestParam("branchId") BigDecimal branchId, Model model) {
	   model.addAttribute("branch", branchService.findByPrimaryKey(branchId));
	   return "BranchEdit";
	}	

	@RequestMapping(value = "/branchEdit", method = RequestMethod.POST)
	public String editBranchByPrimaryKeyPost(@ModelAttribute("branch") Branch branch, BindingResult result, HttpServletRequest request) {
		String[] suppressedFields = result.getSuppressedFields();
		if (suppressedFields.length > 0) {
			throw new RuntimeException("Attempting to bind disallowed fields: " + StringUtils.arrayToCommaDelimitedString(suppressedFields));
		}
		branchService.update(branch);
		return "redirect:/db/branchView?branchId=" + branch.getBranchId();
	}	
	
	@RequestMapping(value = "/branchDelete", method = RequestMethod.GET)
	public String deleteBranchByPrimaryKey(@RequestParam("branchId") BigDecimal branchId) {
		branchService.deleteByPrimaryKey(branchId);
		return "redirect:/db/branch";
	}

	@InitBinder
	public void initialiseBinder(WebDataBinder binder) {
		binder.setAllowedFields(
			"branchId", 
			"address", 
			"city", 
			"name", 
			"state", 
			"zipCode"		
		);
	}
	
	@ExceptionHandler(NotFoundException.class)
	public ModelAndView handleError(HttpServletRequest req, NotFoundException exception) {
		ModelAndView mav = new ModelAndView();	
		mav.addObject("exception", exception);
		mav.addObject("url", req.getRequestURL()+"?"+req.getQueryString());
		mav.setViewName("notFound");
		return mav;
	}	
}
