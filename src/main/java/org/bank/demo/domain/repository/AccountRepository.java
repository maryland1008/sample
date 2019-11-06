package org.bank.demo.domain.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.bank.demo.domain.Account;
import org.bank.demo.domain.repository.commons.GenericRepository;

import java.math.BigDecimal;

public interface AccountRepository<T extends Serializable> extends GenericRepository <T>{
	public boolean hasField(String fieldName);
	public Account findByPrimaryKey( BigDecimal accountId );
	public Account findByQuery(String sql, Map<String,List<?>> params);
	public BigDecimal insertAccount(Account bean);
	public int update(Account bean);
	public List<Account> findBySqlParameterSource(Map<String,List<?>> params);
	public int deleteByPrimaryKey( BigDecimal accountId );
	public boolean exists( BigDecimal accountId );	
}
