package org.bank.demo.service;

import org.apache.log4j.Logger;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.bank.demo.domain.Business;
import org.bank.demo.domain.repository.BusinessRepository;
import org.bank.demo.service.commons.GenericService;
import org.bank.demo.service.BusinessService;

import java.math.BigDecimal;

@Service
public class BusinessService extends GenericService <Business> {
    private static final Logger serviceLogger = Logger.getLogger(BusinessService.class);
	
	@Autowired
	public void init(BusinessRepository<Business> repository) {
		this.repository = repository;
		this.logger = serviceLogger;
	}

	public boolean hasField(String fieldName) {
		return ((BusinessRepository<Business>)repository).hasField(fieldName);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public BigDecimal insertBusiness(Business bean) {
		return ((BusinessRepository<Business>)repository).insertBusiness(bean);
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Business findByPrimaryKey( BigDecimal custId ) {
		return ((BusinessRepository<Business>)repository).findByPrimaryKey(custId);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<Business> findByFilter(Map<String,List<?>> params) {
		return ((BusinessRepository<Business>)repository).findBySqlParameterSource(params);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int deleteByPrimaryKey( BigDecimal custId ) {
		return ((BusinessRepository<Business>)repository).deleteByPrimaryKey(custId);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public boolean exists( BigDecimal custId ) {
		return ((BusinessRepository<Business>)repository).exists(custId);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(final Business bean) {
		if (bean == null) {
			logger.error("Entity is null");
			throw new IllegalArgumentException("Entity is null");
		}
		((BusinessRepository<Business>)repository).update(bean);
	}	
}
