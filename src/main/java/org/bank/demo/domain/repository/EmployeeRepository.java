package org.bank.demo.domain.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.bank.demo.domain.Employee;
import org.bank.demo.domain.repository.commons.GenericRepository;

import java.math.BigDecimal;

public interface EmployeeRepository<T extends Serializable> extends GenericRepository <T>{
	public boolean hasField(String fieldName);
	public Employee findByPrimaryKey( BigDecimal empId );
	public Employee findByQuery(String sql, Map<String,List<?>> params);
	public BigDecimal insertEmployee(Employee bean);
	public int update(Employee bean);
	public List<Employee> findBySqlParameterSource(Map<String,List<?>> params);
	public int deleteByPrimaryKey( BigDecimal empId );
	public boolean exists( BigDecimal empId );	
}
