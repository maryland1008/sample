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

import org.bank.demo.domain.Account;
import org.bank.demo.service.AccountService;
import org.bank.demo.exception.NotFoundException;

@Controller
@RequestMapping("/db")
public class AccountController {
    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(AccountController.class);

	@Autowired
	private AccountService accountService;
	
	@RequestMapping(value = "/account", method = RequestMethod.GET)
	public String list(Model model) {
	   model.addAttribute("account", accountService.findAll());
	   return "AccountList";
	}

	@RequestMapping(value = "/account", method = RequestMethod.POST)
	public String searchList(@RequestParam("attr") String fieldName, @RequestParam("value") String value, Model model) {
		if(accountService.hasField(fieldName)) {
			return "redirect:/db/account/" + StringEscapeUtils.escapeHtml(fieldName) + "/" + StringEscapeUtils.escapeHtml(value);
		} else {
			throw new RuntimeException("Attempting to search disallowed field: " + fieldName);
		}
	}
	
	@RequestMapping(value = "/account/accountId/{accountId}", method = RequestMethod.GET)
	public String getAccountByAccountId(@PathVariable("accountId") BigDecimal accountId, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("accountId", Arrays.asList(accountId));
	   model.addAttribute("account", accountService.findByFilter(filter));
	   return "AccountList";
	}
	
	@RequestMapping(value = "/account/availBalance/{availBalance}", method = RequestMethod.GET)
	public String getAccountByAvailBalance(@PathVariable("availBalance") Double availBalance, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("availBalance", Arrays.asList(availBalance));
	   model.addAttribute("account", accountService.findByFilter(filter));
	   return "AccountList";
	}
	
	@RequestMapping(value = "/account/closeDate/{closeDate}", method = RequestMethod.GET)
	public String getAccountByCloseDate(@PathVariable("closeDate") Date closeDate, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("closeDate", Arrays.asList(closeDate));
	   model.addAttribute("account", accountService.findByFilter(filter));
	   return "AccountList";
	}
	
	@RequestMapping(value = "/account/lastActivityDate/{lastActivityDate}", method = RequestMethod.GET)
	public String getAccountByLastActivityDate(@PathVariable("lastActivityDate") Date lastActivityDate, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("lastActivityDate", Arrays.asList(lastActivityDate));
	   model.addAttribute("account", accountService.findByFilter(filter));
	   return "AccountList";
	}
	
	@RequestMapping(value = "/account/openDate/{openDate}", method = RequestMethod.GET)
	public String getAccountByOpenDate(@PathVariable("openDate") Date openDate, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("openDate", Arrays.asList(openDate));
	   model.addAttribute("account", accountService.findByFilter(filter));
	   return "AccountList";
	}
	
	@RequestMapping(value = "/account/pendingBalance/{pendingBalance}", method = RequestMethod.GET)
	public String getAccountByPendingBalance(@PathVariable("pendingBalance") Double pendingBalance, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("pendingBalance", Arrays.asList(pendingBalance));
	   model.addAttribute("account", accountService.findByFilter(filter));
	   return "AccountList";
	}
	
	@RequestMapping(value = "/account/status/{status}", method = RequestMethod.GET)
	public String getAccountByStatus(@PathVariable("status") String status, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("status", Arrays.asList(status));
	   model.addAttribute("account", accountService.findByFilter(filter));
	   return "AccountList";
	}
	
	@RequestMapping(value = "/account/custId/{custId}", method = RequestMethod.GET)
	public String getAccountByCustId(@PathVariable("custId") BigDecimal custId, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("custId", Arrays.asList(custId));
	   model.addAttribute("account", accountService.findByFilter(filter));
	   return "AccountList";
	}
	
	@RequestMapping(value = "/account/openBranchId/{openBranchId}", method = RequestMethod.GET)
	public String getAccountByOpenBranchId(@PathVariable("openBranchId") BigDecimal openBranchId, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("openBranchId", Arrays.asList(openBranchId));
	   model.addAttribute("account", accountService.findByFilter(filter));
	   return "AccountList";
	}
	
	@RequestMapping(value = "/account/openEmpId/{openEmpId}", method = RequestMethod.GET)
	public String getAccountByOpenEmpId(@PathVariable("openEmpId") BigDecimal openEmpId, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("openEmpId", Arrays.asList(openEmpId));
	   model.addAttribute("account", accountService.findByFilter(filter));
	   return "AccountList";
	}
	
	@RequestMapping(value = "/account/productCd/{productCd}", method = RequestMethod.GET)
	public String getAccountByProductCd(@PathVariable("productCd") String productCd, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("productCd", Arrays.asList(productCd));
	   model.addAttribute("account", accountService.findByFilter(filter));
	   return "AccountList";
	}
	
	@RequestMapping(value = "/account/search/{matrixVariable}")
	public String getAccountByFilter(@MatrixVariable(pathVar="matrixVariable") Map<String,List<?>> filter, Model model) {
	   model.addAttribute("account", accountService.findByFilter(filter));
	   return "AccountList";
	}
	
	@RequestMapping(value = "/accountView", method = RequestMethod.GET)
	public String getAccountByPrimaryKey(@RequestParam("accountId") BigDecimal accountId, Model model) {
		Account account =  accountService.findByPrimaryKey(accountId);
		if (account == null) {
			throw new NotFoundException();
		}		
		model.addAttribute("account", account);
		return "AccountView";
	}	

	@RequestMapping(value = "/account/add", method = RequestMethod.GET)
	public String addAccountGet(Model model) {
	   Account account = new Account();
	   model.addAttribute("account", account);
	   return "AccountEdit";
	}	
	
	@RequestMapping(value = "/account/add", method = RequestMethod.POST)
	public String addAccountPost(@ModelAttribute("account") Account account, BindingResult result, HttpServletRequest request) {
		String[] suppressedFields = result.getSuppressedFields();
		if (suppressedFields.length > 0) {
			throw new RuntimeException("Attempting to bind disallowed fields: " + StringUtils.arrayToCommaDelimitedString(suppressedFields));
		}
		accountService.insert(account);
		return "redirect:/db/accountView?accountId=" + account.getAccountId();
	}	
	
	@RequestMapping(value = "/accountEdit", method = RequestMethod.GET)
	public String editAccountByPrimaryKeyGet(@RequestParam("accountId") BigDecimal accountId, Model model) {
	   model.addAttribute("account", accountService.findByPrimaryKey(accountId));
	   return "AccountEdit";
	}	

	@RequestMapping(value = "/accountEdit", method = RequestMethod.POST)
	public String editAccountByPrimaryKeyPost(@ModelAttribute("account") Account account, BindingResult result, HttpServletRequest request) {
		String[] suppressedFields = result.getSuppressedFields();
		if (suppressedFields.length > 0) {
			throw new RuntimeException("Attempting to bind disallowed fields: " + StringUtils.arrayToCommaDelimitedString(suppressedFields));
		}
		accountService.update(account);
		return "redirect:/db/accountView?accountId=" + account.getAccountId();
	}	
	
	@RequestMapping(value = "/accountDelete", method = RequestMethod.GET)
	public String deleteAccountByPrimaryKey(@RequestParam("accountId") BigDecimal accountId) {
		accountService.deleteByPrimaryKey(accountId);
		return "redirect:/db/account";
	}

	@InitBinder
	public void initialiseBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateUtils.SIMPLE_DATE_FORMATTER, true));
		binder.setAllowedFields(
			"accountId", 
			"availBalance", 
			"closeDate", 
			"lastActivityDate", 
			"openDate", 
			"pendingBalance", 
			"status", 
			"custId", 
			"openBranchId", 
			"openEmpId", 
			"productCd"		
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
