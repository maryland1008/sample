package org.bank.demo.service;

import org.apache.log4j.Logger;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.bank.demo.domain.Customer;
import org.bank.demo.domain.repository.CustomerRepository;
import org.bank.demo.service.commons.GenericService;
import org.bank.demo.service.CustomerService;

import java.math.BigDecimal;

@Service
public class CustomerService extends GenericService <Customer> {
    private static final Logger serviceLogger = Logger.getLogger(CustomerService.class);
	
	@Autowired
	public void init(CustomerRepository<Customer> repository) {
		this.repository = repository;
		this.logger = serviceLogger;
	}

	public boolean hasField(String fieldName) {
		return ((CustomerRepository<Customer>)repository).hasField(fieldName);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public BigDecimal insertCustomer(Customer bean) {
		return ((CustomerRepository<Customer>)repository).insertCustomer(bean);
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Customer findByPrimaryKey( BigDecimal custId ) {
		return ((CustomerRepository<Customer>)repository).findByPrimaryKey(custId);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<Customer> findByFilter(Map<String,List<?>> params) {
		return ((CustomerRepository<Customer>)repository).findBySqlParameterSource(params);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int deleteByPrimaryKey( BigDecimal custId ) {
		return ((CustomerRepository<Customer>)repository).deleteByPrimaryKey(custId);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public boolean exists( BigDecimal custId ) {
		return ((CustomerRepository<Customer>)repository).exists(custId);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(final Customer bean) {
		if (bean == null) {
			logger.error("Entity is null");
			throw new IllegalArgumentException("Entity is null");
		}
		((CustomerRepository<Customer>)repository).update(bean);
	}	
}
