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

import org.bank.demo.domain.Department;
import org.bank.demo.domain.repository.commons.GenericDAO;
import org.bank.demo.domain.repository.DepartmentRepository;

import java.math.BigDecimal;

@Repository
public class DepartmentRepositoryImpl extends GenericDAO<Department> implements DepartmentRepository<Department> {
	
    private static final Logger logger = Logger.getLogger(DepartmentRepositoryImpl.class);

	private final static String TABLE_NAME = "DEPARTMENT";	

	private final static String SQL_SELECT =  "select DEPT_ID, NAME  from DEPARTMENT where DEPT_ID = :deptId";
	
	private final static String SQL_INSERT = "insert into DEPARTMENT (DEPT_ID, NAME) values (:deptId, :name)";
	
	private final static String SQL_UPDATE = "update DEPARTMENT set NAME = :name where DEPT_ID = :deptId";

	private final static String SQL_DELETE = "delete from DEPARTMENT where DEPT_ID = :deptId";

	private final static String SQL_COUNT_ALL =  "select count(*) from DEPARTMENT";

	private final static String SQL_COUNT = "select count(*) from DEPARTMENT where DEPT_ID = :deptId";

	@SuppressWarnings("unchecked")
	protected final Map<String, String> fieldMap = MapUtils.putAll(new HashMap<String, String>(), new String[][] {
		{ "deptId", "DEPT_ID" }, 
		{ "name", "NAME" }	});

	public DepartmentRepositoryImpl() {
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
	public Department findByPrimaryKey( BigDecimal deptId ) {
		MapSqlParameterSource primaryKey = new MapSqlParameterSource();	
		primaryKey.addValue("deptId", deptId);
		return super.doSelect(primaryKey);		
	}

	@Override
	public Department findByQuery(String sql, Map<String,List<?>> params) {
		MapSqlParameterSource sqlParams = new MapSqlParameterSource();	
		for(String field : fieldMap.keySet()) {
			if(params.containsKey(field)) {
				sqlParams.addValue(field, params.get(field));
			}
		}
		
		RowMapper<Department> rowMapper = getRowMapper();
		try {
			return jdbcTemplate.queryForObject(sql, sqlParams, rowMapper);
		} catch (EmptyResultDataAccessException e) {
			getLogger().info("Record not found - " + getTableName());
			return null;
		}
	}	

	@Override
	public List<Department> findBySqlParameterSource(Map<String,List<?>> params) {
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
		
		String sql = "select * from DEPARTMENT";
		
		if(sb.length()>0) {
			sql += sb.toString();
		} 
		
		try {
			List<Department> list = jdbcTemplate.query(sql, sqlParams, new DepartmentRowMapper());
			return list;
		} catch (EmptyResultDataAccessException e) {
			logger.info("No data found in DEPARTMENT");
			return null;
		}	
	}

	@Override
	public int deleteByPrimaryKey( BigDecimal deptId ) {
		MapSqlParameterSource primaryKey = new MapSqlParameterSource();	
		primaryKey.addValue("deptId", deptId);
		return super.doDelete(primaryKey);		
	}

	@Override
	public boolean exists( BigDecimal deptId ) {
		MapSqlParameterSource primaryKey = new MapSqlParameterSource();	
		primaryKey.addValue("deptId", deptId);
		return super.doExists(primaryKey);
	}

	@Override
	public List<Department> findAll() {
		String sql = "select * from DEPARTMENT";
		try {
			List<Department> list = jdbcTemplate.query(sql, new DepartmentRowMapper());
			return list;
		} catch (EmptyResultDataAccessException e) {
			logger.info("No data found in DEPARTMENT");
			return null;
		}	
	}
	
	@Override
	public BigDecimal insertDepartment(Department bean) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int result = jdbcTemplate.update(getSqlInsert(), getValuesForInsert(bean), keyHolder,
				new String[] { "DEPT_ID" });
		if (result != 1) {
			getLogger().error("INSERT failed - " + getTableName());
			throw new RuntimeException("Unexpected return value after INSERT : " + result + " (1 expected) ");
		} else {
			return new BigDecimal(keyHolder.getKey().intValue());
		}
	}	
	
	@Override
	public void insert(Department department) {
		super.doInsert(department);
	}	

	@Override
	public int update(Department department) {
		return super.doUpdate(department);
	}	

	@Override
	public int delete( Department department ) {
		return super.doDelete(department);
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
	protected SqlParameterSource getValuesForInsert(Department department)  {
		MapSqlParameterSource params = new MapSqlParameterSource();	
		params.addValue("deptId", department.getDeptId()); // "DEPT_ID" : java.math.BigDecimal
		params.addValue("name", department.getName()); // "NAME" : java.lang.String
	
		return params;
	}
	
    //----------------------------------------------------------------------
	@Override
	protected SqlParameterSource getValuesForUpdate(Department department) {
		MapSqlParameterSource params = new MapSqlParameterSource();	
		params.addValue("deptId", department.getDeptId()); // "DEPT_ID" : java.math.BigDecimal
		params.addValue("name", department.getName()); // "NAME" : java.lang.String
	
		return params;
	}
	//----------------------------------------------------------------------
	@Override
	protected SqlParameterSource getValuesForPrimaryKey(Department department)  {
		MapSqlParameterSource params = new MapSqlParameterSource();	
		params.addValue("deptId", department.getDeptId()); // "DEPT_ID" : java.math.BigDecimal
	
		return params;
	}
	//----------------------------------------------------------------------
	@Override
	protected RowMapper<Department> getRowMapper()  {
		//--- RowMapper to populate a new bean instance
		return new DepartmentRowMapper() ;
	}

    //----------------------------------------------------------------------
	/**
	 * Populates the given bean with the data retrieved from the given ResultSet
	 * @param rs
	 * @param department
	 * @throws SQLException
	 */
	private void populateBean(ResultSet rs, Department department) throws SQLException {

		//--- Set data from ResultSet to Bean attributes
		department.setDeptId(rs.getBigDecimal("DEPT_ID")); // java.math.BigDecimal
		if ( rs.wasNull() ) { department.setDeptId(null); }; // not primitive number => keep null value if any
		department.setName(rs.getString("NAME")); // java.lang.String
	}

    //----------------------------------------------------------------------
	/**
	 * Specific inner class for 'RowMapper' implementation
	 */
	private class DepartmentRowMapper implements RowMapper<Department> {

		public Department mapRow(ResultSet rs, int rowNum) throws SQLException {
			Department bean = new Department();
			populateBean(rs, bean);
			return bean;
		}
	}
}
