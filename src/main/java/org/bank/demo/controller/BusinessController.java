package org.bank.demo.controller;


import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;

import java.math.BigDecimal;
import java.util.Date;

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
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.bank.demo.util.DateUtils;

import org.bank.demo.domain.Business;
import org.bank.demo.service.BusinessService;
import org.bank.demo.exception.NotFoundException;

@Controller
@RequestMapping("/db")
public class BusinessController {
    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(BusinessController.class);

	@Autowired
	private BusinessService businessService;
	
	@RequestMapping(value = "/business", method = RequestMethod.GET)
	public String list(Model model) {
	   model.addAttribute("business", businessService.findAll());
	   return "BusinessList";
	}

	@RequestMapping(value = "/business", method = RequestMethod.POST)
	public String searchList(@RequestParam("attr") String fieldName, @RequestParam("value") String value, Model model) {
		if(businessService.hasField(fieldName)) {
			return "redirect:/db/business/" + StringEscapeUtils.escapeHtml(fieldName) + "/" + StringEscapeUtils.escapeHtml(value);
		} else {
			throw new RuntimeException("Attempting to search disallowed field: " + fieldName);
		}
	}
	
	@RequestMapping(value = "/business/incorpDate/{incorpDate}", method = RequestMethod.GET)
	public String getBusinessByIncorpDate(@PathVariable("incorpDate") Date incorpDate, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("incorpDate", Arrays.asList(incorpDate));
	   model.addAttribute("business", businessService.findByFilter(filter));
	   return "BusinessList";
	}
	
	@RequestMapping(value = "/business/name/{name}", method = RequestMethod.GET)
	public String getBusinessByName(@PathVariable("name") String name, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("name", Arrays.asList(name));
	   model.addAttribute("business", businessService.findByFilter(filter));
	   return "BusinessList";
	}
	
	@RequestMapping(value = "/business/stateId/{stateId}", method = RequestMethod.GET)
	public String getBusinessByStateId(@PathVariable("stateId") String stateId, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("stateId", Arrays.asList(stateId));
	   model.addAttribute("business", businessService.findByFilter(filter));
	   return "BusinessList";
	}
	
	@RequestMapping(value = "/business/custId/{custId}", method = RequestMethod.GET)
	public String getBusinessByCustId(@PathVariable("custId") BigDecimal custId, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("custId", Arrays.asList(custId));
	   model.addAttribute("business", businessService.findByFilter(filter));
	   return "BusinessList";
	}
	
	@RequestMapping(value = "/business/search/{matrixVariable}")
	public String getBusinessByFilter(@MatrixVariable(pathVar="matrixVariable") Map<String,List<?>> filter, Model model) {
	   model.addAttribute("business", businessService.findByFilter(filter));
	   return "BusinessList";
	}
	
	@RequestMapping(value = "/businessView", method = RequestMethod.GET)
	public String getBusinessByPrimaryKey(@RequestParam("custId") BigDecimal custId, Model model) {
		Business business =  businessService.findByPrimaryKey(custId);
		if (business == null) {
			throw new NotFoundException();
		}		
		model.addAttribute("business", business);
		return "BusinessView";
	}	

	@RequestMapping(value = "/business/add", method = RequestMethod.GET)
	public String addBusinessGet(Model model) {
	   Business business = new Business();
	   model.addAttribute("business", business);
	   return "BusinessEdit";
	}	
	
	@RequestMapping(value = "/business/add", method = RequestMethod.POST)
	public String addBusinessPost(@ModelAttribute("business") Business business, BindingResult result, HttpServletRequest request) {
		String[] suppressedFields = result.getSuppressedFields();
		if (suppressedFields.length > 0) {
			throw new RuntimeException("Attempting to bind disallowed fields: " + StringUtils.arrayToCommaDelimitedString(suppressedFields));
		}
		businessService.insert(business);
		return "redirect:/db/businessView?custId=" + business.getCustId();
	}	
	
	@RequestMapping(value = "/businessEdit", method = RequestMethod.GET)
	public String editBusinessByPrimaryKeyGet(@RequestParam("custId") BigDecimal custId, Model model) {
	   model.addAttribute("business", businessService.findByPrimaryKey(custId));
	   return "BusinessEdit";
	}	

	@RequestMapping(value = "/businessEdit", method = RequestMethod.POST)
	public String editBusinessByPrimaryKeyPost(@ModelAttribute("business") Business business, BindingResult result, HttpServletRequest request) {
		String[] suppressedFields = result.getSuppressedFields();
		if (suppressedFields.length > 0) {
			throw new RuntimeException("Attempting to bind disallowed fields: " + StringUtils.arrayToCommaDelimitedString(suppressedFields));
		}
		businessService.update(business);
		return "redirect:/db/businessView?custId=" + business.getCustId();
	}	
	
	@RequestMapping(value = "/businessDelete", method = RequestMethod.GET)
	public String deleteBusinessByPrimaryKey(@RequestParam("custId") BigDecimal custId) {
		businessService.deleteByPrimaryKey(custId);
		return "redirect:/db/business";
	}

	@InitBinder
	public void initialiseBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateUtils.SIMPLE_DATE_FORMATTER, true));
		binder.setAllowedFields(
			"incorpDate", 
			"name", 
			"stateId", 
			"custId"		
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
