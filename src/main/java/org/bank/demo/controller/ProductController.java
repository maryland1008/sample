package org.bank.demo.controller;


import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;

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

import org.bank.demo.domain.Product;
import org.bank.demo.service.ProductService;
import org.bank.demo.exception.NotFoundException;

@Controller
@RequestMapping("/db")
public class ProductController {
    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(ProductController.class);

	@Autowired
	private ProductService productService;
	
	@RequestMapping(value = "/product", method = RequestMethod.GET)
	public String list(Model model) {
	   model.addAttribute("product", productService.findAll());
	   return "ProductList";
	}

	@RequestMapping(value = "/product", method = RequestMethod.POST)
	public String searchList(@RequestParam("attr") String fieldName, @RequestParam("value") String value, Model model) {
		if(productService.hasField(fieldName)) {
			return "redirect:/db/product/" + StringEscapeUtils.escapeHtml(fieldName) + "/" + StringEscapeUtils.escapeHtml(value);
		} else {
			throw new RuntimeException("Attempting to search disallowed field: " + fieldName);
		}
	}
	
	@RequestMapping(value = "/product/productCd/{productCd}", method = RequestMethod.GET)
	public String getProductByProductCd(@PathVariable("productCd") String productCd, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("productCd", Arrays.asList(productCd));
	   model.addAttribute("product", productService.findByFilter(filter));
	   return "ProductList";
	}
	
	@RequestMapping(value = "/product/dateOffered/{dateOffered}", method = RequestMethod.GET)
	public String getProductByDateOffered(@PathVariable("dateOffered") Date dateOffered, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("dateOffered", Arrays.asList(dateOffered));
	   model.addAttribute("product", productService.findByFilter(filter));
	   return "ProductList";
	}
	
	@RequestMapping(value = "/product/dateRetired/{dateRetired}", method = RequestMethod.GET)
	public String getProductByDateRetired(@PathVariable("dateRetired") Date dateRetired, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("dateRetired", Arrays.asList(dateRetired));
	   model.addAttribute("product", productService.findByFilter(filter));
	   return "ProductList";
	}
	
	@RequestMapping(value = "/product/name/{name}", method = RequestMethod.GET)
	public String getProductByName(@PathVariable("name") String name, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("name", Arrays.asList(name));
	   model.addAttribute("product", productService.findByFilter(filter));
	   return "ProductList";
	}
	
	@RequestMapping(value = "/product/productTypeCd/{productTypeCd}", method = RequestMethod.GET)
	public String getProductByProductTypeCd(@PathVariable("productTypeCd") String productTypeCd, Model model) {
	   Map<String,List<?>> filter = new HashMap<String,List<?>>();
	   filter.put("productTypeCd", Arrays.asList(productTypeCd));
	   model.addAttribute("product", productService.findByFilter(filter));
	   return "ProductList";
	}
	
	@RequestMapping(value = "/product/search/{matrixVariable}")
	public String getProductByFilter(@MatrixVariable(pathVar="matrixVariable") Map<String,List<?>> filter, Model model) {
	   model.addAttribute("product", productService.findByFilter(filter));
	   return "ProductList";
	}
	
	@RequestMapping(value = "/productView", method = RequestMethod.GET)
	public String getProductByPrimaryKey(@RequestParam("productCd") String productCd, Model model) {
		Product product =  productService.findByPrimaryKey(productCd);
		if (product == null) {
			throw new NotFoundException();
		}		
		model.addAttribute("product", product);
		return "ProductView";
	}	

	@RequestMapping(value = "/product/add", method = RequestMethod.GET)
	public String addProductGet(Model model) {
	   Product product = new Product();
	   model.addAttribute("product", product);
	   return "ProductEdit";
	}	
	
	@RequestMapping(value = "/product/add", method = RequestMethod.POST)
	public String addProductPost(@ModelAttribute("product") Product product, BindingResult result, HttpServletRequest request) {
		String[] suppressedFields = result.getSuppressedFields();
		if (suppressedFields.length > 0) {
			throw new RuntimeException("Attempting to bind disallowed fields: " + StringUtils.arrayToCommaDelimitedString(suppressedFields));
		}
		productService.insert(product);
		return "redirect:/db/productView?productCd=" + StringEscapeUtils.escapeHtml(product.getProductCd());
	}	
	
	@RequestMapping(value = "/productEdit", method = RequestMethod.GET)
	public String editProductByPrimaryKeyGet(@RequestParam("productCd") String productCd, Model model) {
	   model.addAttribute("product", productService.findByPrimaryKey(productCd));
	   return "ProductEdit";
	}	

	@RequestMapping(value = "/productEdit", method = RequestMethod.POST)
	public String editProductByPrimaryKeyPost(@ModelAttribute("product") Product product, BindingResult result, HttpServletRequest request) {
		String[] suppressedFields = result.getSuppressedFields();
		if (suppressedFields.length > 0) {
			throw new RuntimeException("Attempting to bind disallowed fields: " + StringUtils.arrayToCommaDelimitedString(suppressedFields));
		}
		productService.update(product);
		return "redirect:/db/productView?productCd=" + StringEscapeUtils.escapeHtml(product.getProductCd());
	}	
	
	@RequestMapping(value = "/productDelete", method = RequestMethod.GET)
	public String deleteProductByPrimaryKey(@RequestParam("productCd") String productCd) {
		productService.deleteByPrimaryKey(productCd);
		return "redirect:/db/product";
	}

	@InitBinder
	public void initialiseBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateUtils.SIMPLE_DATE_FORMATTER, true));
		binder.setAllowedFields(
			"productCd", 
			"dateOffered", 
			"dateRetired", 
			"name", 
			"productTypeCd"		
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
