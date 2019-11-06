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

import org.bank.demo.domain.Customer;
import org.bank.demo.service.CustomerService;
import org.bank.demo.exception.NotFoundException;

@Controller
@RequestMapping("/db")
public class CustomerController {
    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(CustomerController.class);

	@Autowired
	private CustomerService customerService;
	
	@RequestMapping(value = "/customer", method = RequestMethod.GET)
	public String list(Model model) {
	   model.addAttribute("customer", customerService.findAll());
	   return "CustomerList";
	}

	@RequestMapping(value = "/customer", method = RequestMethod.POST)
	public String searchList(@RequestParam("attr") String fieldName, @RequestParam("value") String value, Model model) {
		if(customerService.hasField(fieldName)) {
			return "redirect:/db/customer/" + StringEscapeUtils.escapeHtml(fieldName) + "/" + StringEscapeUtils.escapeHtml(value);
		} else {
			throw new RuntimeException("Attempting to search disallowed field: " + fieldName);
		}
	}
	
	@RequestMapping(value = "/customer/custId/{custId}", method = RequestMethod.GET)
	public String getCustomerByCustId(@PathVariable("custId") BigDecimal custId, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("custId", Arrays.asList(custId));
	   model.addAttribute("customer", customerService.findByFilter(filter));
	   return "CustomerList";
	}
	
	@RequestMapping(value = "/customer/address/{address}", method = RequestMethod.GET)
	public String getCustomerByAddress(@PathVariable("address") String address, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("address", Arrays.asList(address));
	   model.addAttribute("customer", customerService.findByFilter(filter));
	   return "CustomerList";
	}
	
	@RequestMapping(value = "/customer/city/{city}", method = RequestMethod.GET)
	public String getCustomerByCity(@PathVariable("city") String city, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("city", Arrays.asList(city));
	   model.addAttribute("customer", customerService.findByFilter(filter));
	   return "CustomerList";
	}
	
	@RequestMapping(value = "/customer/custTypeCd/{custTypeCd}", method = RequestMethod.GET)
	public String getCustomerByCustTypeCd(@PathVariable("custTypeCd") String custTypeCd, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("custTypeCd", Arrays.asList(custTypeCd));
	   model.addAttribute("customer", customerService.findByFilter(filter));
	   return "CustomerList";
	}
	
	@RequestMapping(value = "/customer/fedId/{fedId}", method = RequestMethod.GET)
	public String getCustomerByFedId(@PathVariable("fedId") String fedId, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("fedId", Arrays.asList(fedId));
	   model.addAttribute("customer", customerService.findByFilter(filter));
	   return "CustomerList";
	}
	
	@RequestMapping(value = "/customer/postalCode/{postalCode}", method = RequestMethod.GET)
	public String getCustomerByPostalCode(@PathVariable("postalCode") String postalCode, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("postalCode", Arrays.asList(postalCode));
	   model.addAttribute("customer", customerService.findByFilter(filter));
	   return "CustomerList";
	}
	
	@RequestMapping(value = "/customer/state/{state}", method = RequestMethod.GET)
	public String getCustomerByState(@PathVariable("state") String state, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("state", Arrays.asList(state));
	   model.addAttribute("customer", customerService.findByFilter(filter));
	   return "CustomerList";
	}
	
	@RequestMapping(value = "/customer/search/{matrixVariable}")
	public String getCustomerByFilter(@MatrixVariable(pathVar="matrixVariable") Map<String,List<?>> filter, Model model) {
	   model.addAttribute("customer", customerService.findByFilter(filter));
	   return "CustomerList";
	}
	
	@RequestMapping(value = "/customerView", method = RequestMethod.GET)
	public String getCustomerByPrimaryKey(@RequestParam("custId") BigDecimal custId, Model model) {
		Customer customer =  customerService.findByPrimaryKey(custId);
		if (customer == null) {
			throw new NotFoundException();
		}		
		model.addAttribute("customer", customer);
		return "CustomerView";
	}	

	@RequestMapping(value = "/customer/add", method = RequestMethod.GET)
	public String addCustomerGet(Model model) {
	   Customer customer = new Customer();
	   model.addAttribute("customer", customer);
	   return "CustomerEdit";
	}	
	
	@RequestMapping(value = "/customer/add", method = RequestMethod.POST)
	public String addCustomerPost(@ModelAttribute("customer") Customer customer, BindingResult result, HttpServletRequest request) {
		String[] suppressedFields = result.getSuppressedFields();
		if (suppressedFields.length > 0) {
			throw new RuntimeException("Attempting to bind disallowed fields: " + StringUtils.arrayToCommaDelimitedString(suppressedFields));
		}
		customerService.insert(customer);
		return "redirect:/db/customerView?custId=" + customer.getCustId();
	}	
	
	@RequestMapping(value = "/customerEdit", method = RequestMethod.GET)
	public String editCustomerByPrimaryKeyGet(@RequestParam("custId") BigDecimal custId, Model model) {
	   model.addAttribute("customer", customerService.findByPrimaryKey(custId));
	   return "CustomerEdit";
	}	

	@RequestMapping(value = "/customerEdit", method = RequestMethod.POST)
	public String editCustomerByPrimaryKeyPost(@ModelAttribute("customer") Customer customer, BindingResult result, HttpServletRequest request) {
		String[] suppressedFields = result.getSuppressedFields();
		if (suppressedFields.length > 0) {
			throw new RuntimeException("Attempting to bind disallowed fields: " + StringUtils.arrayToCommaDelimitedString(suppressedFields));
		}
		customerService.update(customer);
		return "redirect:/db/customerView?custId=" + customer.getCustId();
	}	
	
	@RequestMapping(value = "/customerDelete", method = RequestMethod.GET)
	public String deleteCustomerByPrimaryKey(@RequestParam("custId") BigDecimal custId) {
		customerService.deleteByPrimaryKey(custId);
		return "redirect:/db/customer";
	}

	@InitBinder
	public void initialiseBinder(WebDataBinder binder) {
		binder.setAllowedFields(
			"custId", 
			"address", 
			"city", 
			"custTypeCd", 
			"fedId", 
			"postalCode", 
			"state"		
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
