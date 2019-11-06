package org.bank.demo.domain.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.bank.demo.domain.Customer;
import org.bank.demo.domain.repository.commons.GenericRepository;

import java.math.BigDecimal;

public interface CustomerRepository<T extends Serializable> extends GenericRepository <T>{
	public boolean hasField(String fieldName);
	public Customer findByPrimaryKey( BigDecimal custId );
	public Customer findByQuery(String sql, Map<String,List<?>> params);
	public BigDecimal insertCustomer(Customer bean);
	public int update(Customer bean);
	public List<Customer> findBySqlParameterSource(Map<String,List<?>> params);
	public int deleteByPrimaryKey( BigDecimal custId );
	public boolean exists( BigDecimal custId );	
}
