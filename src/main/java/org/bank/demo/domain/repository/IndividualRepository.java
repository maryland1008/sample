package org.bank.demo.domain.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.bank.demo.domain.Individual;
import org.bank.demo.domain.repository.commons.GenericRepository;

import java.math.BigDecimal;

public interface IndividualRepository<T extends Serializable> extends GenericRepository <T>{
	public boolean hasField(String fieldName);
	public Individual findByPrimaryKey( BigDecimal custId );
	public Individual findByQuery(String sql, Map<String,List<?>> params);
	public BigDecimal insertIndividual(Individual bean);
	public int update(Individual bean);
	public List<Individual> findBySqlParameterSource(Map<String,List<?>> params);
	public int deleteByPrimaryKey( BigDecimal custId );
	public boolean exists( BigDecimal custId );	
}
