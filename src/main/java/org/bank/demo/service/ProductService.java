package org.bank.demo.service;

import org.apache.log4j.Logger;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.bank.demo.domain.Product;
import org.bank.demo.domain.repository.ProductRepository;
import org.bank.demo.service.commons.GenericService;
import org.bank.demo.service.ProductService;


@Service
public class ProductService extends GenericService <Product> {
    private static final Logger serviceLogger = Logger.getLogger(ProductService.class);
	
	@Autowired
	public void init(ProductRepository<Product> repository) {
		this.repository = repository;
		this.logger = serviceLogger;
	}

	public boolean hasField(String fieldName) {
		return ((ProductRepository<Product>)repository).hasField(fieldName);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public String insertProduct(Product bean) {
		return ((ProductRepository<Product>)repository).insertProduct(bean);
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Product findByPrimaryKey( String productCd ) {
		return ((ProductRepository<Product>)repository).findByPrimaryKey(productCd);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<Product> findByFilter(Map<String,List<?>> params) {
		return ((ProductRepository<Product>)repository).findBySqlParameterSource(params);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int deleteByPrimaryKey( String productCd ) {
		return ((ProductRepository<Product>)repository).deleteByPrimaryKey(productCd);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public boolean exists( String productCd ) {
		return ((ProductRepository<Product>)repository).exists(productCd);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(final Product bean) {
		if (bean == null) {
			logger.error("Entity is null");
			throw new IllegalArgumentException("Entity is null");
		}
		((ProductRepository<Product>)repository).update(bean);
	}	
}
