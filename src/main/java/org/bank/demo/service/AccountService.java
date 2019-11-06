package org.bank.demo.service;

import org.apache.log4j.Logger;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.bank.demo.domain.Account;
import org.bank.demo.domain.repository.AccountRepository;
import org.bank.demo.service.commons.GenericService;
import org.bank.demo.service.AccountService;

import java.math.BigDecimal;

@Service
public class AccountService extends GenericService <Account> {
    private static final Logger serviceLogger = Logger.getLogger(AccountService.class);
	
	@Autowired
	public void init(AccountRepository<Account> repository) {
		this.repository = repository;
		this.logger = serviceLogger;
	}

	public boolean hasField(String fieldName) {
		return ((AccountRepository<Account>)repository).hasField(fieldName);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public BigDecimal insertAccount(Account bean) {
		return ((AccountRepository<Account>)repository).insertAccount(bean);
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Account findByPrimaryKey( BigDecimal accountId ) {
		return ((AccountRepository<Account>)repository).findByPrimaryKey(accountId);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<Account> findByFilter(Map<String,List<?>> params) {
		return ((AccountRepository<Account>)repository).findBySqlParameterSource(params);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int deleteByPrimaryKey( BigDecimal accountId ) {
		return ((AccountRepository<Account>)repository).deleteByPrimaryKey(accountId);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public boolean exists( BigDecimal accountId ) {
		return ((AccountRepository<Account>)repository).exists(accountId);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(final Account bean) {
		if (bean == null) {
			logger.error("Entity is null");
			throw new IllegalArgumentException("Entity is null");
		}
		((AccountRepository<Account>)repository).update(bean);
	}	
}
