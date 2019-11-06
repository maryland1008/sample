package org.bank.demo.service;

import org.apache.log4j.Logger;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.bank.demo.domain.Officer;
import org.bank.demo.domain.repository.OfficerRepository;
import org.bank.demo.service.commons.GenericService;
import org.bank.demo.service.OfficerService;

import java.math.BigDecimal;

@Service
public class OfficerService extends GenericService <Officer> {
    private static final Logger serviceLogger = Logger.getLogger(OfficerService.class);
	
	@Autowired
	public void init(OfficerRepository<Officer> repository) {
		this.repository = repository;
		this.logger = serviceLogger;
	}

	public boolean hasField(String fieldName) {
		return ((OfficerRepository<Officer>)repository).hasField(fieldName);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public BigDecimal insertOfficer(Officer bean) {
		return ((OfficerRepository<Officer>)repository).insertOfficer(bean);
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Officer findByPrimaryKey( BigDecimal officerId ) {
		return ((OfficerRepository<Officer>)repository).findByPrimaryKey(officerId);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<Officer> findByFilter(Map<String,List<?>> params) {
		return ((OfficerRepository<Officer>)repository).findBySqlParameterSource(params);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int deleteByPrimaryKey( BigDecimal officerId ) {
		return ((OfficerRepository<Officer>)repository).deleteByPrimaryKey(officerId);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public boolean exists( BigDecimal officerId ) {
		return ((OfficerRepository<Officer>)repository).exists(officerId);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(final Officer bean) {
		if (bean == null) {
			logger.error("Entity is null");
			throw new IllegalArgumentException("Entity is null");
		}
		((OfficerRepository<Officer>)repository).update(bean);
	}	
}
