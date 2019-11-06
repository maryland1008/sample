package org.bank.demo.domain.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.bank.demo.domain.Officer;
import org.bank.demo.domain.repository.commons.GenericRepository;

import java.math.BigDecimal;

public interface OfficerRepository<T extends Serializable> extends GenericRepository <T>{
	public boolean hasField(String fieldName);
	public Officer findByPrimaryKey( BigDecimal officerId );
	public Officer findByQuery(String sql, Map<String,List<?>> params);
	public BigDecimal insertOfficer(Officer bean);
	public int update(Officer bean);
	public List<Officer> findBySqlParameterSource(Map<String,List<?>> params);
	public int deleteByPrimaryKey( BigDecimal officerId );
	public boolean exists( BigDecimal officerId );	
}
