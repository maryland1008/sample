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

import org.bank.demo.domain.Individual;
import org.bank.demo.service.IndividualService;
import org.bank.demo.exception.NotFoundException;

@Controller
@RequestMapping("/db")
public class IndividualController {
    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(IndividualController.class);

	@Autowired
	private IndividualService individualService;
	
	@RequestMapping(value = "/individual", method = RequestMethod.GET)
	public String list(Model model) {
	   model.addAttribute("individual", individualService.findAll());
	   return "IndividualList";
	}

	@RequestMapping(value = "/individual", method = RequestMethod.POST)
	public String searchList(@RequestParam("attr") String fieldName, @RequestParam("value") String value, Model model) {
		if(individualService.hasField(fieldName)) {
			return "redirect:/db/individual/" + StringEscapeUtils.escapeHtml(fieldName) + "/" + StringEscapeUtils.escapeHtml(value);
		} else {
			throw new RuntimeException("Attempting to search disallowed field: " + fieldName);
		}
	}
	
	@RequestMapping(value = "/individual/birthDate/{birthDate}", method = RequestMethod.GET)
	public String getIndividualByBirthDate(@PathVariable("birthDate") Date birthDate, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("birthDate", Arrays.asList(birthDate));
	   model.addAttribute("individual", individualService.findByFilter(filter));
	   return "IndividualList";
	}
	
	@RequestMapping(value = "/individual/firstName/{firstName}", method = RequestMethod.GET)
	public String getIndividualByFirstName(@PathVariable("firstName") String firstName, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("firstName", Arrays.asList(firstName));
	   model.addAttribute("individual", individualService.findByFilter(filter));
	   return "IndividualList";
	}
	
	@RequestMapping(value = "/individual/lastName/{lastName}", method = RequestMethod.GET)
	public String getIndividualByLastName(@PathVariable("lastName") String lastName, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("lastName", Arrays.asList(lastName));
	   model.addAttribute("individual", individualService.findByFilter(filter));
	   return "IndividualList";
	}
	
	@RequestMapping(value = "/individual/custId/{custId}", method = RequestMethod.GET)
	public String getIndividualByCustId(@PathVariable("custId") BigDecimal custId, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("custId", Arrays.asList(custId));
	   model.addAttribute("individual", individualService.findByFilter(filter));
	   return "IndividualList";
	}
	
	@RequestMapping(value = "/individual/search/{matrixVariable}")
	public String getIndividualByFilter(@MatrixVariable(pathVar="matrixVariable") Map<String,List<?>> filter, Model model) {
	   model.addAttribute("individual", individualService.findByFilter(filter));
	   return "IndividualList";
	}
	
	@RequestMapping(value = "/individualView", method = RequestMethod.GET)
	public String getIndividualByPrimaryKey(@RequestParam("custId") BigDecimal custId, Model model) {
		Individual individual =  individualService.findByPrimaryKey(custId);
		if (individual == null) {
			throw new NotFoundException();
		}		
		model.addAttribute("individual", individual);
		return "IndividualView";
	}	

	@RequestMapping(value = "/individual/add", method = RequestMethod.GET)
	public String addIndividualGet(Model model) {
	   Individual individual = new Individual();
	   model.addAttribute("individual", individual);
	   return "IndividualEdit";
	}	
	
	@RequestMapping(value = "/individual/add", method = RequestMethod.POST)
	public String addIndividualPost(@ModelAttribute("individual") Individual individual, BindingResult result, HttpServletRequest request) {
		String[] suppressedFields = result.getSuppressedFields();
		if (suppressedFields.length > 0) {
			throw new RuntimeException("Attempting to bind disallowed fields: " + StringUtils.arrayToCommaDelimitedString(suppressedFields));
		}
		individualService.insert(individual);
		return "redirect:/db/individualView?custId=" + individual.getCustId();
	}	
	
	@RequestMapping(value = "/individualEdit", method = RequestMethod.GET)
	public String editIndividualByPrimaryKeyGet(@RequestParam("custId") BigDecimal custId, Model model) {
	   model.addAttribute("individual", individualService.findByPrimaryKey(custId));
	   return "IndividualEdit";
	}	

	@RequestMapping(value = "/individualEdit", method = RequestMethod.POST)
	public String editIndividualByPrimaryKeyPost(@ModelAttribute("individual") Individual individual, BindingResult result, HttpServletRequest request) {
		String[] suppressedFields = result.getSuppressedFields();
		if (suppressedFields.length > 0) {
			throw new RuntimeException("Attempting to bind disallowed fields: " + StringUtils.arrayToCommaDelimitedString(suppressedFields));
		}
		individualService.update(individual);
		return "redirect:/db/individualView?custId=" + individual.getCustId();
	}	
	
	@RequestMapping(value = "/individualDelete", method = RequestMethod.GET)
	public String deleteIndividualByPrimaryKey(@RequestParam("custId") BigDecimal custId) {
		individualService.deleteByPrimaryKey(custId);
		return "redirect:/db/individual";
	}

	@InitBinder
	public void initialiseBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateUtils.SIMPLE_DATE_FORMATTER, true));
		binder.setAllowedFields(
			"birthDate", 
			"firstName", 
			"lastName", 
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
