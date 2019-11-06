package org.bank.demo.domain.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.bank.demo.domain.AccTransaction;
import org.bank.demo.domain.repository.commons.GenericRepository;

import java.math.BigDecimal;

public interface AccTransactionRepository<T extends Serializable> extends GenericRepository <T>{
	public boolean hasField(String fieldName);
	public AccTransaction findByPrimaryKey( BigDecimal txnId );
	public AccTransaction findByQuery(String sql, Map<String,List<?>> params);
	public BigDecimal insertAccTransaction(AccTransaction bean);
	public int update(AccTransaction bean);
	public List<AccTransaction> findBySqlParameterSource(Map<String,List<?>> params);
	public int deleteByPrimaryKey( BigDecimal txnId );
	public boolean exists( BigDecimal txnId );	
}
