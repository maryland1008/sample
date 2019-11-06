package org.bank.demo.service;

import org.apache.log4j.Logger;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.bank.demo.domain.Individual;
import org.bank.demo.domain.repository.IndividualRepository;
import org.bank.demo.service.commons.GenericService;
import org.bank.demo.service.IndividualService;

import java.math.BigDecimal;

@Service
public class IndividualService extends GenericService <Individual> {
    private static final Logger serviceLogger = Logger.getLogger(IndividualService.class);
	
	@Autowired
	public void init(IndividualRepository<Individual> repository) {
		this.repository = repository;
		this.logger = serviceLogger;
	}

	public boolean hasField(String fieldName) {
		return ((IndividualRepository<Individual>)repository).hasField(fieldName);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public BigDecimal insertIndividual(Individual bean) {
		return ((IndividualRepository<Individual>)repository).insertIndividual(bean);
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Individual findByPrimaryKey( BigDecimal custId ) {
		return ((IndividualRepository<Individual>)repository).findByPrimaryKey(custId);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<Individual> findByFilter(Map<String,List<?>> params) {
		return ((IndividualRepository<Individual>)repository).findBySqlParameterSource(params);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int deleteByPrimaryKey( BigDecimal custId ) {
		return ((IndividualRepository<Individual>)repository).deleteByPrimaryKey(custId);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public boolean exists( BigDecimal custId ) {
		return ((IndividualRepository<Individual>)repository).exists(custId);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(final Individual bean) {
		if (bean == null) {
			logger.error("Entity is null");
			throw new IllegalArgumentException("Entity is null");
		}
		((IndividualRepository<Individual>)repository).update(bean);
	}	
}
