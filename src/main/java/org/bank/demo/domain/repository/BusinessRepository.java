package org.bank.demo.domain.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.bank.demo.domain.Business;
import org.bank.demo.domain.repository.commons.GenericRepository;

import java.math.BigDecimal;

public interface BusinessRepository<T extends Serializable> extends GenericRepository <T>{
	public boolean hasField(String fieldName);
	public Business findByPrimaryKey( BigDecimal custId );
	public Business findByQuery(String sql, Map<String,List<?>> params);
	public BigDecimal insertBusiness(Business bean);
	public int update(Business bean);
	public List<Business> findBySqlParameterSource(Map<String,List<?>> params);
	public int deleteByPrimaryKey( BigDecimal custId );
	public boolean exists( BigDecimal custId );	
}
