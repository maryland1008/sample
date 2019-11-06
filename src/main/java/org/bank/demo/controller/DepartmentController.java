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

import org.bank.demo.domain.Department;
import org.bank.demo.service.DepartmentService;
import org.bank.demo.exception.NotFoundException;

@Controller
@RequestMapping("/db")
public class DepartmentController {
    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(DepartmentController.class);

	@Autowired
	private DepartmentService departmentService;
	
	@RequestMapping(value = "/department", method = RequestMethod.GET)
	public String list(Model model) {
	   model.addAttribute("department", departmentService.findAll());
	   return "DepartmentList";
	}

	@RequestMapping(value = "/department", method = RequestMethod.POST)
	public String searchList(@RequestParam("attr") String fieldName, @RequestParam("value") String value, Model model) {
		if(departmentService.hasField(fieldName)) {
			return "redirect:/db/department/" + StringEscapeUtils.escapeHtml(fieldName) + "/" + StringEscapeUtils.escapeHtml(value);
		} else {
			throw new RuntimeException("Attempting to search disallowed field: " + fieldName);
		}
	}
	
	@RequestMapping(value = "/department/deptId/{deptId}", method = RequestMethod.GET)
	public String getDepartmentByDeptId(@PathVariable("deptId") BigDecimal deptId, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("deptId", Arrays.asList(deptId));
	   model.addAttribute("department", departmentService.findByFilter(filter));
	   return "DepartmentList";
	}
	
	@RequestMapping(value = "/department/name/{name}", method = RequestMethod.GET)
	public String getDepartmentByName(@PathVariable("name") String name, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("name", Arrays.asList(name));
	   model.addAttribute("department", departmentService.findByFilter(filter));
	   return "DepartmentList";
	}
	
	@RequestMapping(value = "/department/search/{matrixVariable}")
	public String getDepartmentByFilter(@MatrixVariable(pathVar="matrixVariable") Map<String,List<?>> filter, Model model) {
	   model.addAttribute("department", departmentService.findByFilter(filter));
	   return "DepartmentList";
	}
	
	@RequestMapping(value = "/departmentView", method = RequestMethod.GET)
	public String getDepartmentByPrimaryKey(@RequestParam("deptId") BigDecimal deptId, Model model) {
		Department department =  departmentService.findByPrimaryKey(deptId);
		if (department == null) {
			throw new NotFoundException();
		}		
		model.addAttribute("department", department);
		return "DepartmentView";
	}	

	@RequestMapping(value = "/department/add", method = RequestMethod.GET)
	public String addDepartmentGet(Model model) {
	   Department department = new Department();
	   model.addAttribute("department", department);
	   return "DepartmentEdit";
	}	
	
	@RequestMapping(value = "/department/add", method = RequestMethod.POST)
	public String addDepartmentPost(@ModelAttribute("department") Department department, BindingResult result, HttpServletRequest request) {
		String[] suppressedFields = result.getSuppressedFields();
		if (suppressedFields.length > 0) {
			throw new RuntimeException("Attempting to bind disallowed fields: " + StringUtils.arrayToCommaDelimitedString(suppressedFields));
		}
		departmentService.insert(department);
		return "redirect:/db/departmentView?deptId=" + department.getDeptId();
	}	
	
	@RequestMapping(value = "/departmentEdit", method = RequestMethod.GET)
	public String editDepartmentByPrimaryKeyGet(@RequestParam("deptId") BigDecimal deptId, Model model) {
	   model.addAttribute("department", departmentService.findByPrimaryKey(deptId));
	   return "DepartmentEdit";
	}	

	@RequestMapping(value = "/departmentEdit", method = RequestMethod.POST)
	public String editDepartmentByPrimaryKeyPost(@ModelAttribute("department") Department department, BindingResult result, HttpServletRequest request) {
		String[] suppressedFields = result.getSuppressedFields();
		if (suppressedFields.length > 0) {
			throw new RuntimeException("Attempting to bind disallowed fields: " + StringUtils.arrayToCommaDelimitedString(suppressedFields));
		}
		departmentService.update(department);
		return "redirect:/db/departmentView?deptId=" + department.getDeptId();
	}	
	
	@RequestMapping(value = "/departmentDelete", method = RequestMethod.GET)
	public String deleteDepartmentByPrimaryKey(@RequestParam("deptId") BigDecimal deptId) {
		departmentService.deleteByPrimaryKey(deptId);
		return "redirect:/db/department";
	}

	@InitBinder
	public void initialiseBinder(WebDataBinder binder) {
		binder.setAllowedFields(
			"deptId", 
			"name"		
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
