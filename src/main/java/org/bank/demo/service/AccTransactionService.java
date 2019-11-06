package org.bank.demo.service;

import org.apache.log4j.Logger;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.bank.demo.domain.AccTransaction;
import org.bank.demo.domain.repository.AccTransactionRepository;
import org.bank.demo.service.commons.GenericService;
import org.bank.demo.service.AccTransactionService;

import java.math.BigDecimal;

@Service
public class AccTransactionService extends GenericService <AccTransaction> {
    private static final Logger serviceLogger = Logger.getLogger(AccTransactionService.class);
	
	@Autowired
	public void init(AccTransactionRepository<AccTransaction> repository) {
		this.repository = repository;
		this.logger = serviceLogger;
	}

	public boolean hasField(String fieldName) {
		return ((AccTransactionRepository<AccTransaction>)repository).hasField(fieldName);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public BigDecimal insertAccTransaction(AccTransaction bean) {
		return ((AccTransactionRepository<AccTransaction>)repository).insertAccTransaction(bean);
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public AccTransaction findByPrimaryKey( BigDecimal txnId ) {
		return ((AccTransactionRepository<AccTransaction>)repository).findByPrimaryKey(txnId);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<AccTransaction> findByFilter(Map<String,List<?>> params) {
		return ((AccTransactionRepository<AccTransaction>)repository).findBySqlParameterSource(params);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int deleteByPrimaryKey( BigDecimal txnId ) {
		return ((AccTransactionRepository<AccTransaction>)repository).deleteByPrimaryKey(txnId);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public boolean exists( BigDecimal txnId ) {
		return ((AccTransactionRepository<AccTransaction>)repository).exists(txnId);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(final AccTransaction bean) {
		if (bean == null) {
			logger.error("Entity is null");
			throw new IllegalArgumentException("Entity is null");
		}
		((AccTransactionRepository<AccTransaction>)repository).update(bean);
	}	
}
