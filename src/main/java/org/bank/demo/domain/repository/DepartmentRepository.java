package org.bank.demo.domain.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.bank.demo.domain.Department;
import org.bank.demo.domain.repository.commons.GenericRepository;

import java.math.BigDecimal;

public interface DepartmentRepository<T extends Serializable> extends GenericRepository <T>{
	public boolean hasField(String fieldName);
	public Department findByPrimaryKey( BigDecimal deptId );
	public Department findByQuery(String sql, Map<String,List<?>> params);
	public BigDecimal insertDepartment(Department bean);
	public int update(Department bean);
	public List<Department> findBySqlParameterSource(Map<String,List<?>> params);
	public int deleteByPrimaryKey( BigDecimal deptId );
	public boolean exists( BigDecimal deptId );	
}
