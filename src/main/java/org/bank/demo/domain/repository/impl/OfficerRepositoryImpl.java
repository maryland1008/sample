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

import org.bank.demo.domain.Officer;
import org.bank.demo.domain.repository.commons.GenericDAO;
import org.bank.demo.domain.repository.OfficerRepository;

import java.math.BigDecimal;

@Repository
public class OfficerRepositoryImpl extends GenericDAO<Officer> implements OfficerRepository<Officer> {
	
    private static final Logger logger = Logger.getLogger(OfficerRepositoryImpl.class);

	private final static String TABLE_NAME = "OFFICER";	

	private final static String SQL_SELECT =  "select OFFICER_ID, END_DATE, FIRST_NAME, LAST_NAME, START_DATE, TITLE, CUST_ID  from OFFICER where OFFICER_ID = :officerId";
	
	private final static String SQL_INSERT = "insert into OFFICER (OFFICER_ID, END_DATE, FIRST_NAME, LAST_NAME, START_DATE, TITLE, CUST_ID) values (:officerId, :endDate, :firstName, :lastName, :startDate, :title, :custId)";
	
	private final static String SQL_UPDATE = "update OFFICER set END_DATE = :endDate, FIRST_NAME = :firstName, LAST_NAME = :lastName, START_DATE = :startDate, TITLE = :title, CUST_ID = :custId where OFFICER_ID = :officerId";

	private final static String SQL_DELETE = "delete from OFFICER where OFFICER_ID = :officerId";

	private final static String SQL_COUNT_ALL =  "select count(*) from OFFICER";

	private final static String SQL_COUNT = "select count(*) from OFFICER where OFFICER_ID = :officerId";

	@SuppressWarnings("unchecked")
	protected final Map<String, String> fieldMap = MapUtils.putAll(new HashMap<String, String>(), new String[][] {
		{ "officerId", "OFFICER_ID" }, 
		{ "endDate", "END_DATE" }, 
		{ "firstName", "FIRST_NAME" }, 
		{ "lastName", "LAST_NAME" }, 
		{ "startDate", "START_DATE" }, 
		{ "title", "TITLE" }, 
		{ "custId", "CUST_ID" }	});

	public OfficerRepositoryImpl() {
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
	public Officer findByPrimaryKey( BigDecimal officerId ) {
		MapSqlParameterSource primaryKey = new MapSqlParameterSource();	
		primaryKey.addValue("officerId", officerId);
		return super.doSelect(primaryKey);		
	}

	@Override
	public Officer findByQuery(String sql, Map<String,List<?>> params) {
		MapSqlParameterSource sqlParams = new MapSqlParameterSource();	
		for(String field : fieldMap.keySet()) {
			if(params.containsKey(field)) {
				sqlParams.addValue(field, params.get(field));
			}
		}
		
		RowMapper<Officer> rowMapper = getRowMapper();
		try {
			return jdbcTemplate.queryForObject(sql, sqlParams, rowMapper);
		} catch (EmptyResultDataAccessException e) {
			getLogger().info("Record not found - " + getTableName());
			return null;
		}
	}	

	@Override
	public List<Officer> findBySqlParameterSource(Map<String,List<?>> params) {
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
		
		String sql = "select * from OFFICER";
		
		if(sb.length()>0) {
			sql += sb.toString();
		} 
		
		try {
			List<Officer> list = jdbcTemplate.query(sql, sqlParams, new OfficerRowMapper());
			return list;
		} catch (EmptyResultDataAccessException e) {
			logger.info("No data found in OFFICER");
			return null;
		}	
	}

	@Override
	public int deleteByPrimaryKey( BigDecimal officerId ) {
		MapSqlParameterSource primaryKey = new MapSqlParameterSource();	
		primaryKey.addValue("officerId", officerId);
		return super.doDelete(primaryKey);		
	}

	@Override
	public boolean exists( BigDecimal officerId ) {
		MapSqlParameterSource primaryKey = new MapSqlParameterSource();	
		primaryKey.addValue("officerId", officerId);
		return super.doExists(primaryKey);
	}

	@Override
	public List<Officer> findAll() {
		String sql = "select * from OFFICER";
		try {
			List<Officer> list = jdbcTemplate.query(sql, new OfficerRowMapper());
			return list;
		} catch (EmptyResultDataAccessException e) {
			logger.info("No data found in OFFICER");
			return null;
		}	
	}
	
	@Override
	public BigDecimal insertOfficer(Officer bean) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int result = jdbcTemplate.update(getSqlInsert(), getValuesForInsert(bean), keyHolder,
				new String[] { "OFFICER_ID" });
		if (result != 1) {
			getLogger().error("INSERT failed - " + getTableName());
			throw new RuntimeException("Unexpected return value after INSERT : " + result + " (1 expected) ");
		} else {
			return new BigDecimal(keyHolder.getKey().intValue());
		}
	}	
	
	@Override
	public void insert(Officer officer) {
		super.doInsert(officer);
	}	

	@Override
	public int update(Officer officer) {
		return super.doUpdate(officer);
	}	

	@Override
	public int delete( Officer officer ) {
		return super.doDelete(officer);
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
	protected SqlParameterSource getValuesForInsert(Officer officer)  {
		MapSqlParameterSource params = new MapSqlParameterSource();	
		params.addValue("officerId", officer.getOfficerId()); // "OFFICER_ID" : java.math.BigDecimal
		params.addValue("endDate", officer.getEndDate()); // "END_DATE" : java.util.Date
		params.addValue("firstName", officer.getFirstName()); // "FIRST_NAME" : java.lang.String
		params.addValue("lastName", officer.getLastName()); // "LAST_NAME" : java.lang.String
		params.addValue("startDate", officer.getStartDate()); // "START_DATE" : java.util.Date
		params.addValue("title", officer.getTitle()); // "TITLE" : java.lang.String
		params.addValue("custId", officer.getCustId()); // "CUST_ID" : java.math.BigDecimal
	
		return params;
	}
	
    //----------------------------------------------------------------------
	@Override
	protected SqlParameterSource getValuesForUpdate(Officer officer) {
		MapSqlParameterSource params = new MapSqlParameterSource();	
		params.addValue("officerId", officer.getOfficerId()); // "OFFICER_ID" : java.math.BigDecimal
		params.addValue("endDate", officer.getEndDate()); // "END_DATE" : java.util.Date
		params.addValue("firstName", officer.getFirstName()); // "FIRST_NAME" : java.lang.String
		params.addValue("lastName", officer.getLastName()); // "LAST_NAME" : java.lang.String
		params.addValue("startDate", officer.getStartDate()); // "START_DATE" : java.util.Date
		params.addValue("title", officer.getTitle()); // "TITLE" : java.lang.String
		params.addValue("custId", officer.getCustId()); // "CUST_ID" : java.math.BigDecimal
	
		return params;
	}
	//----------------------------------------------------------------------
	@Override
	protected SqlParameterSource getValuesForPrimaryKey(Officer officer)  {
		MapSqlParameterSource params = new MapSqlParameterSource();	
		params.addValue("officerId", officer.getOfficerId()); // "OFFICER_ID" : java.math.BigDecimal
	
		return params;
	}
	//----------------------------------------------------------------------
	@Override
	protected RowMapper<Officer> getRowMapper()  {
		//--- RowMapper to populate a new bean instance
		return new OfficerRowMapper() ;
	}

    //----------------------------------------------------------------------
	/**
	 * Populates the given bean with the data retrieved from the given ResultSet
	 * @param rs
	 * @param officer
	 * @throws SQLException
	 */
	private void populateBean(ResultSet rs, Officer officer) throws SQLException {

		//--- Set data from ResultSet to Bean attributes
		officer.setOfficerId(rs.getBigDecimal("OFFICER_ID")); // java.math.BigDecimal
		if ( rs.wasNull() ) { officer.setOfficerId(null); }; // not primitive number => keep null value if any
		officer.setEndDate(rs.getDate("END_DATE")); // java.util.Date
		officer.setFirstName(rs.getString("FIRST_NAME")); // java.lang.String
		officer.setLastName(rs.getString("LAST_NAME")); // java.lang.String
		officer.setStartDate(rs.getDate("START_DATE")); // java.util.Date
		officer.setTitle(rs.getString("TITLE")); // java.lang.String
		officer.setCustId(rs.getBigDecimal("CUST_ID")); // java.math.BigDecimal
		if ( rs.wasNull() ) { officer.setCustId(null); }; // not primitive number => keep null value if any
	}

    //----------------------------------------------------------------------
	/**
	 * Specific inner class for 'RowMapper' implementation
	 */
	private class OfficerRowMapper implements RowMapper<Officer> {

		public Officer mapRow(ResultSet rs, int rowNum) throws SQLException {
			Officer bean = new Officer();
			populateBean(rs, bean);
			return bean;
		}
	}
}
