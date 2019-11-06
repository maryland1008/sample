package org.bank.demo.domain.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.apache.log4j.Logger;

import org.apache.commons.collections.MapUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import org.bank.demo.domain.Employee;
import org.bank.demo.domain.repository.commons.GenericDAO;
import org.bank.demo.domain.repository.EmployeeRepository;

import java.math.BigDecimal;

@Repository
public class EmployeeRepositoryImpl extends GenericDAO<Employee> implements EmployeeRepository<Employee> {
	
    private static final Logger logger = Logger.getLogger(EmployeeRepositoryImpl.class);

	private final static String TABLE_NAME = "EMPLOYEE";	

	private final static String SQL_SELECT =  "select EMP_ID, END_DATE, FIRST_NAME, LAST_NAME, START_DATE, TITLE, ASSIGNED_BRANCH_ID, DEPT_ID, SUPERIOR_EMP_ID  from EMPLOYEE where EMP_ID = :empId";
	
	private final static String SQL_INSERT = "insert into EMPLOYEE (EMP_ID, END_DATE, FIRST_NAME, LAST_NAME, START_DATE, TITLE, ASSIGNED_BRANCH_ID, DEPT_ID, SUPERIOR_EMP_ID) values (:empId, :endDate, :firstName, :lastName, :startDate, :title, :assignedBranchId, :deptId, :superiorEmpId)";
	
	private final static String SQL_UPDATE = "update EMPLOYEE set END_DATE = :endDate, FIRST_NAME = :firstName, LAST_NAME = :lastName, START_DATE = :startDate, TITLE = :title, ASSIGNED_BRANCH_ID = :assignedBranchId, DEPT_ID = :deptId, SUPERIOR_EMP_ID = :superiorEmpId where EMP_ID = :empId";

	private final static String SQL_DELETE = "delete from EMPLOYEE where EMP_ID = :empId";

	private final static String SQL_COUNT_ALL =  "select count(*) from EMPLOYEE";

	private final static String SQL_COUNT = "select count(*) from EMPLOYEE where EMP_ID = :empId";

	@SuppressWarnings("unchecked")
	protected final Map<String, String> fieldMap = MapUtils.putAll(new HashMap<String, String>(), new String[][] {
		{ "empId", "EMP_ID" }, 
		{ "endDate", "END_DATE" }, 
		{ "firstName", "FIRST_NAME" }, 
		{ "lastName", "LAST_NAME" }, 
		{ "startDate", "START_DATE" }, 
		{ "title", "TITLE" }, 
		{ "assignedBranchId", "ASSIGNED_BRANCH_ID" }, 
		{ "deptId", "DEPT_ID" }, 
		{ "superiorEmpId", "SUPERIOR_EMP_ID" }	});

	public EmployeeRepositoryImpl() {
		super();
	}

    //----------------------------------------------------------------------
	// Interface methods implementation
    //----------------------------------------------------------------------	
	@Override
	public boolean hasField(String fieldName) {
		return fieldMap.containsKey(fieldName);
	}
	
	@Override
	public Employee findByPrimaryKey( BigDecimal empId ) {
		MapSqlParameterSource primaryKey = new MapSqlParameterSource();	
		primaryKey.addValue("empId", empId);
		return super.doSelect(primaryKey);		
	}

	@Override
	public Employee findByQuery(String sql, Map<String,List<?>> params) {
		MapSqlParameterSource sqlParams = new MapSqlParameterSource();	
		for(String field : fieldMap.keySet()) {
			if(params.containsKey(field)) {
				sqlParams.addValue(field, params.get(field));
			}
		}
		
		RowMapper<Employee> rowMapper = getRowMapper();
		try {
			return jdbcTemplate.queryForObject(sql, sqlParams, rowMapper);
		} catch (EmptyResultDataAccessException e) {
			getLogger().info("Record not found - " + getTableName());
			return null;
		}
	}	

	@Override
	public List<Employee> findBySqlParameterSource(Map<String,List<?>> params) {
		MapSqlParameterSource sqlParams = new MapSqlParameterSource();	
		for(String field : fieldMap.keySet()) {
			if(params.containsKey(field)) {
				sqlParams.addValue(field, params.get(field));
			}
		}
		
		StringBuffer sb = new StringBuffer();
		
		for(String field : fieldMap.keySet()) {
			if(fieldMap.containsKey(field) && sqlParams.hasValue(field)) {
				if(sb.length()>0) {
					sb.append(" and " + fieldMap.get(field) + " in (:" + field + ")");
				} else {
					sb.append(" where " + fieldMap.get(field) + " in (:" + field + ")");
				}
			}
		}
		
		String sql = "select * from EMPLOYEE";
		
		if(sb.length()>0) {
			sql += sb.toString();
		} 
		
		try {
			List<Employee> list = jdbcTemplate.query(sql, sqlParams, new EmployeeRowMapper());
			return list;
		} catch (EmptyResultDataAccessException e) {
			logger.info("No data found in EMPLOYEE");
			return null;
		}	
	}

	@Override
	public int deleteByPrimaryKey( BigDecimal empId ) {
		MapSqlParameterSource primaryKey = new MapSqlParameterSource();	
		primaryKey.addValue("empId", empId);
		return super.doDelete(primaryKey);		
	}

	@Override
	public boolean exists( BigDecimal empId ) {
		MapSqlParameterSource primaryKey = new MapSqlParameterSource();	
		primaryKey.addValue("empId", empId);
		return super.doExists(primaryKey);
	}

	@Override
	public List<Employee> findAll() {
		String sql = "select * from EMPLOYEE";
		try {
			List<Employee> list = jdbcTemplate.query(sql, new EmployeeRowMapper());
			return list;
		} catch (EmptyResultDataAccessException e) {
			logger.info("No data found in EMPLOYEE");
			return null;
		}	
	}
	
	@Override
	public BigDecimal insertEmployee(Employee bean) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int result = jdbcTemplate.update(getSqlInsert(), getValuesForInsert(bean), keyHolder,
				new String[] { "EMP_ID" });
		if (result != 1) {
			getLogger().error("INSERT failed - " + getTableName());
			throw new RuntimeException("Unexpected return value after INSERT : " + result + " (1 expected) ");
		} else {
			return new BigDecimal(keyHolder.getKey().intValue());
		}
	}	
	
	@Override
	public void insert(Employee employee) {
		super.doInsert(employee);
	}	

	@Override
	public int update(Employee employee) {
		return super.doUpdate(employee);
	}	

	@Override
	public int delete( Employee employee ) {
		return super.doDelete(employee);
	}

	@Override
	public long count() {
		return super.doCountAll();
	}

    //----------------------------------------------------------------------
	// Abstract methods implementation
    //----------------------------------------------------------------------
	@Override
	protected Logger getLogger() {
	    return logger;
	}
	
	@Override
	protected String getTableName() {
	    return TABLE_NAME;
	}

	@Override
	protected String getSqlSelect() {
		return SQL_SELECT ;
	}
    //----------------------------------------------------------------------
	@Override
	protected String getSqlInsert() {
		return SQL_INSERT ;
	}
    //----------------------------------------------------------------------
	@Override
	protected String getSqlUpdate() {
		return SQL_UPDATE ;
	}
    //----------------------------------------------------------------------
	@Override
	protected String getSqlDelete() {
		return SQL_DELETE ;
	}
    //----------------------------------------------------------------------
	@Override
	protected String getSqlCount() {
		return SQL_COUNT ;
	}
    //----------------------------------------------------------------------
	@Override
	protected String getSqlCountAll() {
		return SQL_COUNT_ALL ;
	}
	//----------------------------------------------------------------------
	@Override
	protected SqlParameterSource getValuesForInsert(Employee employee)  {
		MapSqlParameterSource params = new MapSqlParameterSource();	
		params.addValue("empId", employee.getEmpId()); // "EMP_ID" : java.math.BigDecimal
		params.addValue("endDate", employee.getEndDate()); // "END_DATE" : java.util.Date
		params.addValue("firstName", employee.getFirstName()); // "FIRST_NAME" : java.lang.String
		params.addValue("lastName", employee.getLastName()); // "LAST_NAME" : java.lang.String
		params.addValue("startDate", employee.getStartDate()); // "START_DATE" : java.util.Date
		params.addValue("title", employee.getTitle()); // "TITLE" : java.lang.String
		params.addValue("assignedBranchId", employee.getAssignedBranchId()); // "ASSIGNED_BRANCH_ID" : java.math.BigDecimal
		params.addValue("deptId", employee.getDeptId()); // "DEPT_ID" : java.math.BigDecimal
		params.addValue("superiorEmpId", employee.getSuperiorEmpId()); // "SUPERIOR_EMP_ID" : java.math.BigDecimal
	
		return params;
	}
	
    //----------------------------------------------------------------------
	@Override
	protected SqlParameterSource getValuesForUpdate(Employee employee) {
		MapSqlParameterSource params = new MapSqlParameterSource();	
		params.addValue("empId", employee.getEmpId()); // "EMP_ID" : java.math.BigDecimal
		params.addValue("endDate", employee.getEndDate()); // "END_DATE" : java.util.Date
		params.addValue("firstName", employee.getFirstName()); // "FIRST_NAME" : java.lang.String
		params.addValue("lastName", employee.getLastName()); // "LAST_NAME" : java.lang.String
		params.addValue("startDate", employee.getStartDate()); // "START_DATE" : java.util.Date
		params.addValue("title", employee.getTitle()); // "TITLE" : java.lang.String
		params.addValue("assignedBranchId", employee.getAssignedBranchId()); // "ASSIGNED_BRANCH_ID" : java.math.BigDecimal
		params.addValue("deptId", employee.getDeptId()); // "DEPT_ID" : java.math.BigDecimal
		params.addValue("superiorEmpId", employee.getSuperiorEmpId()); // "SUPERIOR_EMP_ID" : java.math.BigDecimal
	
		return params;
	}
	//----------------------------------------------------------------------
	@Override
	protected SqlParameterSource getValuesForPrimaryKey(Employee employee)  {
		MapSqlParameterSource params = new MapSqlParameterSource();	
		params.addValue("empId", employee.getEmpId()); // "EMP_ID" : java.math.BigDecimal
	
		return params;
	}
	//----------------------------------------------------------------------
	@Override
	protected RowMapper<Employee> getRowMapper()  {
		//--- RowMapper to populate a new bean instance
		return new EmployeeRowMapper() ;
	}

    //----------------------------------------------------------------------
	/**
	 * Populates the given bean with the data retrieved from the given ResultSet
	 * @param rs
	 * @param employee
	 * @throws SQLException
	 */
	private void populateBean(ResultSet rs, Employee employee) throws SQLException {

		//--- Set data from ResultSet to Bean attributes
		employee.setEmpId(rs.getBigDecimal("EMP_ID")); // java.math.BigDecimal
		if ( rs.wasNull() ) { employee.setEmpId(null); }; // not primitive number => keep null value if any
		employee.setEndDate(rs.getDate("END_DATE")); // java.util.Date
		employee.setFirstName(rs.getString("FIRST_NAME")); // java.lang.String
		employee.setLastName(rs.getString("LAST_NAME")); // java.lang.String
		employee.setStartDate(rs.getDate("START_DATE")); // java.util.Date
		employee.setTitle(rs.getString("TITLE")); // java.lang.String
		employee.setAssignedBranchId(rs.getBigDecimal("ASSIGNED_BRANCH_ID")); // java.math.BigDecimal
		if ( rs.wasNull() ) { employee.setAssignedBranchId(null); }; // not primitive number => keep null value if any
		employee.setDeptId(rs.getBigDecimal("DEPT_ID")); // java.math.BigDecimal
		if ( rs.wasNull() ) { employee.setDeptId(null); }; // not primitive number => keep null value if any
		employee.setSuperiorEmpId(rs.getBigDecimal("SUPERIOR_EMP_ID")); // java.math.BigDecimal
		if ( rs.wasNull() ) { employee.setSuperiorEmpId(null); }; // not primitive number => keep null value if any
	}

    //----------------------------------------------------------------------
	/**
	 * Specific inner class for 'RowMapper' implementation
	 */
	private class EmployeeRowMapper implements RowMapper<Employee> {

		public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
			Employee bean = new Employee();
			populateBean(rs, bean);
			return bean;
		}
	}
}
