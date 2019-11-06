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

import org.bank.demo.domain.AccTransaction;
import org.bank.demo.service.AccTransactionService;
import org.bank.demo.exception.NotFoundException;

@Controller
@RequestMapping("/db")
public class AccTransactionController {
    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(AccTransactionController.class);

	@Autowired
	private AccTransactionService accTransactionService;
	
	@RequestMapping(value = "/accTransaction", method = RequestMethod.GET)
	public String list(Model model) {
	   model.addAttribute("accTransaction", accTransactionService.findAll());
	   return "AccTransactionList";
	}

	@RequestMapping(value = "/accTransaction", method = RequestMethod.POST)
	public String searchList(@RequestParam("attr") String fieldName, @RequestParam("value") String value, Model model) {
		if(accTransactionService.hasField(fieldName)) {
			return "redirect:/db/accTransaction/" + StringEscapeUtils.escapeHtml(fieldName) + "/" + StringEscapeUtils.escapeHtml(value);
		} else {
			throw new RuntimeException("Attempting to search disallowed field: " + fieldName);
		}
	}
	
	@RequestMapping(value = "/accTransaction/txnId/{txnId}", method = RequestMethod.GET)
	public String getAccTransactionByTxnId(@PathVariable("txnId") BigDecimal txnId, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("txnId", Arrays.asList(txnId));
	   model.addAttribute("accTransaction", accTransactionService.findByFilter(filter));
	   return "AccTransactionList";
	}
	
	@RequestMapping(value = "/accTransaction/amount/{amount}", method = RequestMethod.GET)
	public String getAccTransactionByAmount(@PathVariable("amount") Double amount, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("amount", Arrays.asList(amount));
	   model.addAttribute("accTransaction", accTransactionService.findByFilter(filter));
	   return "AccTransactionList";
	}
	
	@RequestMapping(value = "/accTransaction/fundsAvailDate/{fundsAvailDate}", method = RequestMethod.GET)
	public String getAccTransactionByFundsAvailDate(@PathVariable("fundsAvailDate") Date fundsAvailDate, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("fundsAvailDate", Arrays.asList(fundsAvailDate));
	   model.addAttribute("accTransaction", accTransactionService.findByFilter(filter));
	   return "AccTransactionList";
	}
	
	@RequestMapping(value = "/accTransaction/txnDate/{txnDate}", method = RequestMethod.GET)
	public String getAccTransactionByTxnDate(@PathVariable("txnDate") Date txnDate, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("txnDate", Arrays.asList(txnDate));
	   model.addAttribute("accTransaction", accTransactionService.findByFilter(filter));
	   return "AccTransactionList";
	}
	
	@RequestMapping(value = "/accTransaction/txnTypeCd/{txnTypeCd}", method = RequestMethod.GET)
	public String getAccTransactionByTxnTypeCd(@PathVariable("txnTypeCd") String txnTypeCd, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("txnTypeCd", Arrays.asList(txnTypeCd));
	   model.addAttribute("accTransaction", accTransactionService.findByFilter(filter));
	   return "AccTransactionList";
	}
	
	@RequestMapping(value = "/accTransaction/accountId/{accountId}", method = RequestMethod.GET)
	public String getAccTransactionByAccountId(@PathVariable("accountId") BigDecimal accountId, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("accountId", Arrays.asList(accountId));
	   model.addAttribute("accTransaction", accTransactionService.findByFilter(filter));
	   return "AccTransactionList";
	}
	
	@RequestMapping(value = "/accTransaction/executionBranchId/{executionBranchId}", method = RequestMethod.GET)
	public String getAccTransactionByExecutionBranchId(@PathVariable("executionBranchId") BigDecimal executionBranchId, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("executionBranchId", Arrays.asList(executionBranchId));
	   model.addAttribute("accTransaction", accTransactionService.findByFilter(filter));
	   return "AccTransactionList";
	}
	
	@RequestMapping(value = "/accTransaction/tellerEmpId/{tellerEmpId}", method = RequestMethod.GET)
	public String getAccTransactionByTellerEmpId(@PathVariable("tellerEmpId") BigDecimal tellerEmpId, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("tellerEmpId", Arrays.asList(tellerEmpId));
	   model.addAttribute("accTransaction", accTransactionService.findByFilter(filter));
	   return "AccTransactionList";
	}
	
	@RequestMapping(value = "/accTransaction/search/{matrixVariable}")
	public String getAccTransactionByFilter(@MatrixVariable(pathVar="matrixVariable") Map<String,List<?>> filter, Model model) {
	   model.addAttribute("accTransaction", accTransactionService.findByFilter(filter));
	   return "AccTransactionList";
	}
	
	@RequestMapping(value = "/accTransactionView", method = RequestMethod.GET)
	public String getAccTransactionByPrimaryKey(@RequestParam("txnId") BigDecimal txnId, Model model) {
		AccTransaction accTransaction =  accTransactionService.findByPrimaryKey(txnId);
		if (accTransaction == null) {
			throw new NotFoundException();
		}		
		model.addAttribute("accTransaction", accTransaction);
		return "AccTransactionView";
	}	

	@RequestMapping(value = "/accTransaction/add", method = RequestMethod.GET)
	public String addAccTransactionGet(Model model) {
	   AccTransaction accTransaction = new AccTransaction();
	   model.addAttribute("accTransaction", accTransaction);
	   return "AccTransactionEdit";
	}	
	
	@RequestMapping(value = "/accTransaction/add", method = RequestMethod.POST)
	public String addAccTransactionPost(@ModelAttribute("accTransaction") AccTransaction accTransaction, BindingResult result, HttpServletRequest request) {
		String[] suppressedFields = result.getSuppressedFields();
		if (suppressedFields.length > 0) {
			throw new RuntimeException("Attempting to bind disallowed fields: " + StringUtils.arrayToCommaDelimitedString(suppressedFields));
		}
		accTransactionService.insert(accTransaction);
		return "redirect:/db/accTransactionView?txnId=" + accTransaction.getTxnId();
	}	
	
	@RequestMapping(value = "/accTransactionEdit", method = RequestMethod.GET)
	public String editAccTransactionByPrimaryKeyGet(@RequestParam("txnId") BigDecimal txnId, Model model) {
	   model.addAttribute("accTransaction", accTransactionService.findByPrimaryKey(txnId));
	   return "AccTransactionEdit";
	}	

	@RequestMapping(value = "/accTransactionEdit", method = RequestMethod.POST)
	public String editAccTransactionByPrimaryKeyPost(@ModelAttribute("accTransaction") AccTransaction accTransaction, BindingResult result, HttpServletRequest request) {
		String[] suppressedFields = result.getSuppressedFields();
		if (suppressedFields.length > 0) {
			throw new RuntimeException("Attempting to bind disallowed fields: " + StringUtils.arrayToCommaDelimitedString(suppressedFields));
		}
		accTransactionService.update(accTransaction);
		return "redirect:/db/accTransactionView?txnId=" + accTransaction.getTxnId();
	}	
	
	@RequestMapping(value = "/accTransactionDelete", method = RequestMethod.GET)
	public String deleteAccTransactionByPrimaryKey(@RequestParam("txnId") BigDecimal txnId) {
		accTransactionService.deleteByPrimaryKey(txnId);
		return "redirect:/db/accTransaction";
	}

	@InitBinder
	public void initialiseBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateUtils.SIMPLE_DATE_FORMATTER, true));
		binder.setAllowedFields(
			"txnId", 
			"amount", 
			"fundsAvailDate", 
			"txnDate", 
			"txnTypeCd", 
			"accountId", 
			"executionBranchId", 
			"tellerEmpId"		
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
