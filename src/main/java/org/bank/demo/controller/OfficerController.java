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

import org.bank.demo.domain.Officer;
import org.bank.demo.service.OfficerService;
import org.bank.demo.exception.NotFoundException;

@Controller
@RequestMapping("/db")
public class OfficerController {
    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(OfficerController.class);

	@Autowired
	private OfficerService officerService;
	
	@RequestMapping(value = "/officer", method = RequestMethod.GET)
	public String list(Model model) {
	   model.addAttribute("officer", officerService.findAll());
	   return "OfficerList";
	}

	@RequestMapping(value = "/officer", method = RequestMethod.POST)
	public String searchList(@RequestParam("attr") String fieldName, @RequestParam("value") String value, Model model) {
		if(officerService.hasField(fieldName)) {
			return "redirect:/db/officer/" + StringEscapeUtils.escapeHtml(fieldName) + "/" + StringEscapeUtils.escapeHtml(value);
		} else {
			throw new RuntimeException("Attempting to search disallowed field: " + fieldName);
		}
	}
	
	@RequestMapping(value = "/officer/officerId/{officerId}", method = RequestMethod.GET)
	public String getOfficerByOfficerId(@PathVariable("officerId") BigDecimal officerId, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("officerId", Arrays.asList(officerId));
	   model.addAttribute("officer", officerService.findByFilter(filter));
	   return "OfficerList";
	}
	
	@RequestMapping(value = "/officer/endDate/{endDate}", method = RequestMethod.GET)
	public String getOfficerByEndDate(@PathVariable("endDate") Date endDate, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("endDate", Arrays.asList(endDate));
	   model.addAttribute("officer", officerService.findByFilter(filter));
	   return "OfficerList";
	}
	
	@RequestMapping(value = "/officer/firstName/{firstName}", method = RequestMethod.GET)
	public String getOfficerByFirstName(@PathVariable("firstName") String firstName, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("firstName", Arrays.asList(firstName));
	   model.addAttribute("officer", officerService.findByFilter(filter));
	   return "OfficerList";
	}
	
	@RequestMapping(value = "/officer/lastName/{lastName}", method = RequestMethod.GET)
	public String getOfficerByLastName(@PathVariable("lastName") String lastName, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("lastName", Arrays.asList(lastName));
	   model.addAttribute("officer", officerService.findByFilter(filter));
	   return "OfficerList";
	}
	
	@RequestMapping(value = "/officer/startDate/{startDate}", method = RequestMethod.GET)
	public String getOfficerByStartDate(@PathVariable("startDate") Date startDate, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("startDate", Arrays.asList(startDate));
	   model.addAttribute("officer", officerService.findByFilter(filter));
	   return "OfficerList";
	}
	
	@RequestMapping(value = "/officer/title/{title}", method = RequestMethod.GET)
	public String getOfficerByTitle(@PathVariable("title") String title, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("title", Arrays.asList(title));
	   model.addAttribute("officer", officerService.findByFilter(filter));
	   return "OfficerList";
	}
	
	@RequestMapping(value = "/officer/custId/{custId}", method = RequestMethod.GET)
	public String getOfficerByCustId(@PathVariable("custId") BigDecimal custId, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("custId", Arrays.asList(custId));
	   model.addAttribute("officer", officerService.findByFilter(filter));
	   return "OfficerList";
	}
	
	@RequestMapping(value = "/officer/search/{matrixVariable}")
	public String getOfficerByFilter(@MatrixVariable(pathVar="matrixVariable") Map<String,List<?>> filter, Model model) {
	   model.addAttribute("officer", officerService.findByFilter(filter));
	   return "OfficerList";
	}
	
	@RequestMapping(value = "/officerView", method = RequestMethod.GET)
	public String getOfficerByPrimaryKey(@RequestParam("officerId") BigDecimal officerId, Model model) {
		Officer officer =  officerService.findByPrimaryKey(officerId);
		if (officer == null) {
			throw new NotFoundException();
		}		
		model.addAttribute("officer", officer);
		return "OfficerView";
	}	

	@RequestMapping(value = "/officer/add", method = RequestMethod.GET)
	public String addOfficerGet(Model model) {
	   Officer officer = new Officer();
	   model.addAttribute("officer", officer);
	   return "OfficerEdit";
	}	
	
	@RequestMapping(value = "/officer/add", method = RequestMethod.POST)
	public String addOfficerPost(@ModelAttribute("officer") Officer officer, BindingResult result, HttpServletRequest request) {
		String[] suppressedFields = result.getSuppressedFields();
		if (suppressedFields.length > 0) {
			throw new RuntimeException("Attempting to bind disallowed fields: " + StringUtils.arrayToCommaDelimitedString(suppressedFields));
		}
		officerService.insert(officer);
		return "redirect:/db/officerView?officerId=" + officer.getOfficerId();
	}	
	
	@RequestMapping(value = "/officerEdit", method = RequestMethod.GET)
	public String editOfficerByPrimaryKeyGet(@RequestParam("officerId") BigDecimal officerId, Model model) {
	   model.addAttribute("officer", officerService.findByPrimaryKey(officerId));
	   return "OfficerEdit";
	}	

	@RequestMapping(value = "/officerEdit", method = RequestMethod.POST)
	public String editOfficerByPrimaryKeyPost(@ModelAttribute("officer") Officer officer, BindingResult result, HttpServletRequest request) {
		String[] suppressedFields = result.getSuppressedFields();
		if (suppressedFields.length > 0) {
			throw new RuntimeException("Attempting to bind disallowed fields: " + StringUtils.arrayToCommaDelimitedString(suppressedFields));
		}
		officerService.update(officer);
		return "redirect:/db/officerView?officerId=" + officer.getOfficerId();
	}	
	
	@RequestMapping(value = "/officerDelete", method = RequestMethod.GET)
	public String deleteOfficerByPrimaryKey(@RequestParam("officerId") BigDecimal officerId) {
		officerService.deleteByPrimaryKey(officerId);
		return "redirect:/db/officer";
	}

	@InitBinder
	public void initialiseBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateUtils.SIMPLE_DATE_FORMATTER, true));
		binder.setAllowedFields(
			"officerId", 
			"endDate", 
			"firstName", 
			"lastName", 
			"startDate", 
			"title", 
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
