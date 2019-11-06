package org.bank.demo.service;

import org.apache.log4j.Logger;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.bank.demo.domain.Branch;
import org.bank.demo.domain.repository.BranchRepository;
import org.bank.demo.service.commons.GenericService;
import org.bank.demo.service.BranchService;

import java.math.BigDecimal;

@Service
public class BranchService extends GenericService <Branch> {
    private static final Logger serviceLogger = Logger.getLogger(BranchService.class);
	
	@Autowired
	public void init(BranchRepository<Branch> repository) {
		this.repository = repository;
		this.logger = serviceLogger;
	}

	public boolean hasField(String fieldName) {
		return ((BranchRepository<Branch>)repository).hasField(fieldName);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public BigDecimal insertBranch(Branch bean) {
		return ((BranchRepository<Branch>)repository).insertBranch(bean);
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Branch findByPrimaryKey( BigDecimal branchId ) {
		return ((BranchRepository<Branch>)repository).findByPrimaryKey(branchId);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<Branch> findByFilter(Map<String,List<?>> params) {
		return ((BranchRepository<Branch>)repository).findBySqlParameterSource(params);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int deleteByPrimaryKey( BigDecimal branchId ) {
		return ((BranchRepository<Branch>)repository).deleteByPrimaryKey(branchId);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public boolean exists( BigDecimal branchId ) {
		return ((BranchRepository<Branch>)repository).exists(branchId);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(final Branch bean) {
		if (bean == null) {
			logger.error("Entity is null");
			throw new IllegalArgumentException("Entity is null");
		}
		((BranchRepository<Branch>)repository).update(bean);
	}	
}
