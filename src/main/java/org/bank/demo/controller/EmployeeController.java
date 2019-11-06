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

import org.bank.demo.domain.Employee;
import org.bank.demo.service.EmployeeService;
import org.bank.demo.exception.NotFoundException;

@Controller
@RequestMapping("/db")
public class EmployeeController {
    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(EmployeeController.class);

	@Autowired
	private EmployeeService employeeService;
	
	@RequestMapping(value = "/employee", method = RequestMethod.GET)
	public String list(Model model) {
	   model.addAttribute("employee", employeeService.findAll());
	   return "EmployeeList";
	}

	@RequestMapping(value = "/employee", method = RequestMethod.POST)
	public String searchList(@RequestParam("attr") String fieldName, @RequestParam("value") String value, Model model) {
		if(employeeService.hasField(fieldName)) {
			return "redirect:/db/employee/" + StringEscapeUtils.escapeHtml(fieldName) + "/" + StringEscapeUtils.escapeHtml(value);
		} else {
			throw new RuntimeException("Attempting to search disallowed field: " + fieldName);
		}
	}
	
	@RequestMapping(value = "/employee/empId/{empId}", method = RequestMethod.GET)
	public String getEmployeeByEmpId(@PathVariable("empId") BigDecimal empId, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("empId", Arrays.asList(empId));
	   model.addAttribute("employee", employeeService.findByFilter(filter));
	   return "EmployeeList";
	}
	
	@RequestMapping(value = "/employee/endDate/{endDate}", method = RequestMethod.GET)
	public String getEmployeeByEndDate(@PathVariable("endDate") Date endDate, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("endDate", Arrays.asList(endDate));
	   model.addAttribute("employee", employeeService.findByFilter(filter));
	   return "EmployeeList";
	}
	
	@RequestMapping(value = "/employee/firstName/{firstName}", method = RequestMethod.GET)
	public String getEmployeeByFirstName(@PathVariable("firstName") String firstName, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("firstName", Arrays.asList(firstName));
	   model.addAttribute("employee", employeeService.findByFilter(filter));
	   return "EmployeeList";
	}
	
	@RequestMapping(value = "/employee/lastName/{lastName}", method = RequestMethod.GET)
	public String getEmployeeByLastName(@PathVariable("lastName") String lastName, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("lastName", Arrays.asList(lastName));
	   model.addAttribute("employee", employeeService.findByFilter(filter));
	   return "EmployeeList";
	}
	
	@RequestMapping(value = "/employee/startDate/{startDate}", method = RequestMethod.GET)
	public String getEmployeeByStartDate(@PathVariable("startDate") Date startDate, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("startDate", Arrays.asList(startDate));
	   model.addAttribute("employee", employeeService.findByFilter(filter));
	   return "EmployeeList";
	}
	
	@RequestMapping(value = "/employee/title/{title}", method = RequestMethod.GET)
	public String getEmployeeByTitle(@PathVariable("title") String title, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("title", Arrays.asList(title));
	   model.addAttribute("employee", employeeService.findByFilter(filter));
	   return "EmployeeList";
	}
	
	@RequestMapping(value = "/employee/assignedBranchId/{assignedBranchId}", method = RequestMethod.GET)
	public String getEmployeeByAssignedBranchId(@PathVariable("assignedBranchId") BigDecimal assignedBranchId, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("assignedBranchId", Arrays.asList(assignedBranchId));
	   model.addAttribute("employee", employeeService.findByFilter(filter));
	   return "EmployeeList";
	}
	
	@RequestMapping(value = "/employee/deptId/{deptId}", method = RequestMethod.GET)
	public String getEmployeeByDeptId(@PathVariable("deptId") BigDecimal deptId, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("deptId", Arrays.asList(deptId));
	   model.addAttribute("employee", employeeService.findByFilter(filter));
	   return "EmployeeList";
	}
	
	@RequestMapping(value = "/employee/superiorEmpId/{superiorEmpId}", method = RequestMethod.GET)
	public String getEmployeeBySuperiorEmpId(@PathVariable("superiorEmpId") BigDecimal superiorEmpId, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("superiorEmpId", Arrays.asList(superiorEmpId));
	   model.addAttribute("employee", employeeService.findByFilter(filter));
	   return "EmployeeList";
	}
	
	@RequestMapping(value = "/employee/search/{matrixVariable}")
	public String getEmployeeByFilter(@MatrixVariable(pathVar="matrixVariable") Map<String,List<?>> filter, Model model) {
	   model.addAttribute("employee", employeeService.findByFilter(filter));
	   return "EmployeeList";
	}
	
	@RequestMapping(value = "/employeeView", method = RequestMethod.GET)
	public String getEmployeeByPrimaryKey(@RequestParam("empId") BigDecimal empId, Model model) {
		Employee employee =  employeeService.findByPrimaryKey(empId);
		if (employee == null) {
			throw new NotFoundException();
		}		
		model.addAttribute("employee", employee);
		return "EmployeeView";
	}	

	@RequestMapping(value = "/employee/add", method = RequestMethod.GET)
	public String addEmployeeGet(Model model) {
	   Employee employee = new Employee();
	   model.addAttribute("employee", employee);
	   return "EmployeeEdit";
	}	
	
	@RequestMapping(value = "/employee/add", method = RequestMethod.POST)
	public String addEmployeePost(@ModelAttribute("employee") Employee employee, BindingResult result, HttpServletRequest request) {
		String[] suppressedFields = result.getSuppressedFields();
		if (suppressedFields.length > 0) {
			throw new RuntimeException("Attempting to bind disallowed fields: " + StringUtils.arrayToCommaDelimitedString(suppressedFields));
		}
		employeeService.insert(employee);
		return "redirect:/db/employeeView?empId=" + employee.getEmpId();
	}	
	
	@RequestMapping(value = "/employeeEdit", method = RequestMethod.GET)
	public String editEmployeeByPrimaryKeyGet(@RequestParam("empId") BigDecimal empId, Model model) {
	   model.addAttribute("employee", employeeService.findByPrimaryKey(empId));
	   return "EmployeeEdit";
	}	

	@RequestMapping(value = "/employeeEdit", method = RequestMethod.POST)
	public String editEmployeeByPrimaryKeyPost(@ModelAttribute("employee") Employee employee, BindingResult result, HttpServletRequest request) {
		String[] suppressedFields = result.getSuppressedFields();
		if (suppressedFields.length > 0) {
			throw new RuntimeException("Attempting to bind disallowed fields: " + StringUtils.arrayToCommaDelimitedString(suppressedFields));
		}
		employeeService.update(employee);
		return "redirect:/db/employeeView?empId=" + employee.getEmpId();
	}	
	
	@RequestMapping(value = "/employeeDelete", method = RequestMethod.GET)
	public String deleteEmployeeByPrimaryKey(@RequestParam("empId") BigDecimal empId) {
		employeeService.deleteByPrimaryKey(empId);
		return "redirect:/db/employee";
	}

	@InitBinder
	public void initialiseBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateUtils.SIMPLE_DATE_FORMATTER, true));
		binder.setAllowedFields(
			"empId", 
			"endDate", 
			"firstName", 
			"lastName", 
			"startDate", 
			"title", 
			"assignedBranchId", 
			"deptId", 
			"superiorEmpId"		
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
