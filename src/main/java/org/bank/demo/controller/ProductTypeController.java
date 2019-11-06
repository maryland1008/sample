package org.bank.demo.controller;


import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;


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

import org.bank.demo.domain.ProductType;
import org.bank.demo.service.ProductTypeService;
import org.bank.demo.exception.NotFoundException;

@Controller
@RequestMapping("/db")
public class ProductTypeController {
    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(ProductTypeController.class);

	@Autowired
	private ProductTypeService productTypeService;
	
	@RequestMapping(value = "/productType", method = RequestMethod.GET)
	public String list(Model model) {
	   model.addAttribute("productType", productTypeService.findAll());
	   return "ProductTypeList";
	}

	@RequestMapping(value = "/productType", method = RequestMethod.POST)
	public String searchList(@RequestParam("attr") String fieldName, @RequestParam("value") String value, Model model) {
		if(productTypeService.hasField(fieldName)) {
			return "redirect:/db/productType/" + StringEscapeUtils.escapeHtml(fieldName) + "/" + StringEscapeUtils.escapeHtml(value);
		} else {
			throw new RuntimeException("Attempting to search disallowed field: " + fieldName);
		}
	}
	
	@RequestMapping(value = "/productType/productTypeCd/{productTypeCd}", method = RequestMethod.GET)
	public String getProductTypeByProductTypeCd(@PathVariable("productTypeCd") String productTypeCd, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("productTypeCd", Arrays.asList(productTypeCd));
	   model.addAttribute("productType", productTypeService.findByFilter(filter));
	   return "ProductTypeList";
	}
	
	@RequestMapping(value = "/productType/name/{name}", method = RequestMethod.GET)
	public String getProductTypeByName(@PathVariable("name") String name, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("name", Arrays.asList(name));
	   model.addAttribute("productType", productTypeService.findByFilter(filter));
	   return "ProductTypeList";
	}
	
	@RequestMapping(value = "/productType/search/{matrixVariable}")
	public String getProductTypeByFilter(@MatrixVariable(pathVar="matrixVariable") Map<String,List<?>> filter, Model model) {
	   model.addAttribute("productType", productTypeService.findByFilter(filter));
	   return "ProductTypeList";
	}
	
	@RequestMapping(value = "/productTypeView", method = RequestMethod.GET)
	public String getProductTypeByPrimaryKey(@RequestParam("productTypeCd") String productTypeCd, Model model) {
		ProductType productType =  productTypeService.findByPrimaryKey(productTypeCd);
		if (productType == null) {
			throw new NotFoundException();
		}		
		model.addAttribute("productType", productType);
		return "ProductTypeView";
	}	

	@RequestMapping(value = "/productType/add", method = RequestMethod.GET)
	public String addProductTypeGet(Model model) {
	   ProductType productType = new ProductType();
	   model.addAttribute("productType", productType);
	   return "ProductTypeEdit";
	}	
	
	@RequestMapping(value = "/productType/add", method = RequestMethod.POST)
	public String addProductTypePost(@ModelAttribute("productType") ProductType productType, BindingResult result, HttpServletRequest request) {
		String[] suppressedFields = result.getSuppressedFields();
		if (suppressedFields.length > 0) {
			throw new RuntimeException("Attempting to bind disallowed fields: " + StringUtils.arrayToCommaDelimitedString(suppressedFields));
		}
		productTypeService.insert(productType);
		return "redirect:/db/productTypeView?productTypeCd=" + StringEscapeUtils.escapeHtml(productType.getProductTypeCd());
	}	
	
	@RequestMapping(value = "/productTypeEdit", method = RequestMethod.GET)
	public String editProductTypeByPrimaryKeyGet(@RequestParam("productTypeCd") String productTypeCd, Model model) {
	   model.addAttribute("productType", productTypeService.findByPrimaryKey(productTypeCd));
	   return "ProductTypeEdit";
	}	

	@RequestMapping(value = "/productTypeEdit", method = RequestMethod.POST)
	public String editProductTypeByPrimaryKeyPost(@ModelAttribute("productType") ProductType productType, BindingResult result, HttpServletRequest request) {
		String[] suppressedFields = result.getSuppressedFields();
		if (suppressedFields.length > 0) {
			throw new RuntimeException("Attempting to bind disallowed fields: " + StringUtils.arrayToCommaDelimitedString(suppressedFields));
		}
		productTypeService.update(productType);
		return "redirect:/db/productTypeView?productTypeCd=" + StringEscapeUtils.escapeHtml(productType.getProductTypeCd());
	}	
	
	@RequestMapping(value = "/productTypeDelete", method = RequestMethod.GET)
	public String deleteProductTypeByPrimaryKey(@RequestParam("productTypeCd") String productTypeCd) {
		productTypeService.deleteByPrimaryKey(productTypeCd);
		return "redirect:/db/productType";
	}

	@InitBinder
	public void initialiseBinder(WebDataBinder binder) {
		binder.setAllowedFields(
			"productTypeCd", 
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
