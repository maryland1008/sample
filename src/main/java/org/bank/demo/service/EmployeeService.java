package org.bank.demo.service;

import org.apache.log4j.Logger;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.bank.demo.domain.Employee;
import org.bank.demo.domain.repository.EmployeeRepository;
import org.bank.demo.service.commons.GenericService;
import org.bank.demo.service.EmployeeService;

import java.math.BigDecimal;

@Service
public class EmployeeService extends GenericService <Employee> {
    private static final Logger serviceLogger = Logger.getLogger(EmployeeService.class);
	
	@Autowired
	public void init(EmployeeRepository<Employee> repository) {
		this.repository = repository;
		this.logger = serviceLogger;
	}

	public boolean hasField(String fieldName) {
		return ((EmployeeRepository<Employee>)repository).hasField(fieldName);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public BigDecimal insertEmployee(Employee bean) {
		return ((EmployeeRepository<Employee>)repository).insertEmployee(bean);
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Employee findByPrimaryKey( BigDecimal empId ) {
		return ((EmployeeRepository<Employee>)repository).findByPrimaryKey(empId);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<Employee> findByFilter(Map<String,List<?>> params) {
		return ((EmployeeRepository<Employee>)repository).findBySqlParameterSource(params);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int deleteByPrimaryKey( BigDecimal empId ) {
		return ((EmployeeRepository<Employee>)repository).deleteByPrimaryKey(empId);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public boolean exists( BigDecimal empId ) {
		return ((EmployeeRepository<Employee>)repository).exists(empId);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(final Employee bean) {
		if (bean == null) {
			logger.error("Entity is null");
			throw new IllegalArgumentException("Entity is null");
		}
		((EmployeeRepository<Employee>)repository).update(bean);
	}	
}
