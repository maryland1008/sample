package org.bank.demo.service;

import org.apache.log4j.Logger;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.bank.demo.domain.ProductType;
import org.bank.demo.domain.repository.ProductTypeRepository;
import org.bank.demo.service.commons.GenericService;
import org.bank.demo.service.ProductTypeService;


@Service
public class ProductTypeService extends GenericService <ProductType> {
    private static final Logger serviceLogger = Logger.getLogger(ProductTypeService.class);
	
	@Autowired
	public void init(ProductTypeRepository<ProductType> repository) {
		this.repository = repository;
		this.logger = serviceLogger;
	}

	public boolean hasField(String fieldName) {
		return ((ProductTypeRepository<ProductType>)repository).hasField(fieldName);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public String insertProductType(ProductType bean) {
		return ((ProductTypeRepository<ProductType>)repository).insertProductType(bean);
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public ProductType findByPrimaryKey( String productTypeCd ) {
		return ((ProductTypeRepository<ProductType>)repository).findByPrimaryKey(productTypeCd);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<ProductType> findByFilter(Map<String,List<?>> params) {
		return ((ProductTypeRepository<ProductType>)repository).findBySqlParameterSource(params);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int deleteByPrimaryKey( String productTypeCd ) {
		return ((ProductTypeRepository<ProductType>)repository).deleteByPrimaryKey(productTypeCd);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public boolean exists( String productTypeCd ) {
		return ((ProductTypeRepository<ProductType>)repository).exists(productTypeCd);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(final ProductType bean) {
		if (bean == null) {
			logger.error("Entity is null");
			throw new IllegalArgumentException("Entity is null");
		}
		((ProductTypeRepository<ProductType>)repository).update(bean);
	}	
}
