package org.bank.demo.domain.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.bank.demo.domain.Branch;
import org.bank.demo.domain.repository.commons.GenericRepository;

import java.math.BigDecimal;

public interface BranchRepository<T extends Serializable> extends GenericRepository <T>{
	public boolean hasField(String fieldName);
	public Branch findByPrimaryKey( BigDecimal branchId );
	public Branch findByQuery(String sql, Map<String,List<?>> params);
	public BigDecimal insertBranch(Branch bean);
	public int update(Branch bean);
	public List<Branch> findBySqlParameterSource(Map<String,List<?>> params);
	public int deleteByPrimaryKey( BigDecimal branchId );
	public boolean exists( BigDecimal branchId );	
}
