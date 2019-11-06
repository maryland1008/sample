package org.bank.demo.service;

import org.apache.log4j.Logger;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.bank.demo.domain.Department;
import org.bank.demo.domain.repository.DepartmentRepository;
import org.bank.demo.service.commons.GenericService;
import org.bank.demo.service.DepartmentService;

import java.math.BigDecimal;

@Service
public class DepartmentService extends GenericService <Department> {
    private static final Logger serviceLogger = Logger.getLogger(DepartmentService.class);
	
	@Autowired
	public void init(DepartmentRepository<Department> repository) {
		this.repository = repository;
		this.logger = serviceLogger;
	}

	public boolean hasField(String fieldName) {
		return ((DepartmentRepository<Department>)repository).hasField(fieldName);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public BigDecimal insertDepartment(Department bean) {
		return ((DepartmentRepository<Department>)repository).insertDepartment(bean);
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Department findByPrimaryKey( BigDecimal deptId ) {
		return ((DepartmentRepository<Department>)repository).findByPrimaryKey(deptId);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<Department> findByFilter(Map<String,List<?>> params) {
		return ((DepartmentRepository<Department>)repository).findBySqlParameterSource(params);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int deleteByPrimaryKey( BigDecimal deptId ) {
		return ((DepartmentRepository<Department>)repository).deleteByPrimaryKey(deptId);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public boolean exists( BigDecimal deptId ) {
		return ((DepartmentRepository<Department>)repository).exists(deptId);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(final Department bean) {
		if (bean == null) {
			logger.error("Entity is null");
			throw new IllegalArgumentException("Entity is null");
		}
		((DepartmentRepository<Department>)repository).update(bean);
	}	
}
