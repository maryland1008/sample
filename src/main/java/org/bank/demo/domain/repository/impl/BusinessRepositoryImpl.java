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

import org.bank.demo.domain.Business;
import org.bank.demo.domain.repository.commons.GenericDAO;
import org.bank.demo.domain.repository.BusinessRepository;

import java.math.BigDecimal;

@Repository
public class BusinessRepositoryImpl extends GenericDAO<Business> implements BusinessRepository<Business> {
	
    private static final Logger logger = Logger.getLogger(BusinessRepositoryImpl.class);

	private final static String TABLE_NAME = "BUSINESS";	

	private final static String SQL_SELECT =  "select INCORP_DATE, NAME, STATE_ID, CUST_ID  from BUSINESS where CUST_ID = :custId";
	
	private final static String SQL_INSERT = "insert into BUSINESS (INCORP_DATE, NAME, STATE_ID, CUST_ID) values (:incorpDate, :name, :stateId, :custId)";
	
	private final static String SQL_UPDATE = "update BUSINESS set INCORP_DATE = :incorpDate, NAME = :name, STATE_ID = :stateId where CUST_ID = :custId";

	private final static String SQL_DELETE = "delete from BUSINESS where CUST_ID = :custId";

	private final static String SQL_COUNT_ALL =  "select count(*) from BUSINESS";

	private final static String SQL_COUNT = "select count(*) from BUSINESS where CUST_ID = :custId";

	@SuppressWarnings("unchecked")
	protected final Map<String, String> fieldMap = MapUtils.putAll(new HashMap<String, String>(), new String[][] {
		{ "incorpDate", "INCORP_DATE" }, 
		{ "name", "NAME" }, 
		{ "stateId", "STATE_ID" }, 
		{ "custId", "CUST_ID" }	});

	public BusinessRepositoryImpl() {
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
	public Business findByPrimaryKey( BigDecimal custId ) {
		MapSqlParameterSource primaryKey = new MapSqlParameterSource();	
		primaryKey.addValue("custId", custId);
		return super.doSelect(primaryKey);		
	}

	@Override
	public Business findByQuery(String sql, Map<String,List<?>> params) {
		MapSqlParameterSource sqlParams = new MapSqlParameterSource();	
		for(String field : fieldMap.keySet()) {
			if(params.containsKey(field)) {
				sqlParams.addValue(field, params.get(field));
			}
		}
		
		RowMapper<Business> rowMapper = getRowMapper();
		try {
			return jdbcTemplate.queryForObject(sql, sqlParams, rowMapper);
		} catch (EmptyResultDataAccessException e) {
			getLogger().info("Record not found - " + getTableName());
			return null;
		}
	}	

	@Override
	public List<Business> findBySqlParameterSource(Map<String,List<?>> params) {
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
		
		String sql = "select * from BUSINESS";
		
		if(sb.length()>0) {
			sql += sb.toString();
		} 
		
		try {
			List<Business> list = jdbcTemplate.query(sql, sqlParams, new BusinessRowMapper());
			return list;
		} catch (EmptyResultDataAccessException e) {
			logger.info("No data found in BUSINESS");
			return null;
		}	
	}

	@Override
	public int deleteByPrimaryKey( BigDecimal custId ) {
		MapSqlParameterSource primaryKey = new MapSqlParameterSource();	
		primaryKey.addValue("custId", custId);
		return super.doDelete(primaryKey);		
	}

	@Override
	public boolean exists( BigDecimal custId ) {
		MapSqlParameterSource primaryKey = new MapSqlParameterSource();	
		primaryKey.addValue("custId", custId);
		return super.doExists(primaryKey);
	}

	@Override
	public List<Business> findAll() {
		String sql = "select * from BUSINESS";
		try {
			List<Business> list = jdbcTemplate.query(sql, new BusinessRowMapper());
			return list;
		} catch (EmptyResultDataAccessException e) {
			logger.info("No data found in BUSINESS");
			return null;
		}	
	}
	
	@Override
	public BigDecimal insertBusiness(Business bean) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int result = jdbcTemplate.update(getSqlInsert(), getValuesForInsert(bean), keyHolder,
				new String[] { "CUST_ID" });
		if (result != 1) {
			getLogger().error("INSERT failed - " + getTableName());
			throw new RuntimeException("Unexpected return value after INSERT : " + result + " (1 expected) ");
		} else {
			return new BigDecimal(keyHolder.getKey().intValue());
		}
	}	
	
	@Override
	public void insert(Business business) {
		super.doInsert(business);
	}	

	@Override
	public int update(Business business) {
		return super.doUpdate(business);
	}	

	@Override
	public int delete( Business business ) {
		return super.doDelete(business);
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
	protected SqlParameterSource getValuesForInsert(Business business)  {
		MapSqlParameterSource params = new MapSqlParameterSource();	
		params.addValue("incorpDate", business.getIncorpDate()); // "INCORP_DATE" : java.util.Date
		params.addValue("name", business.getName()); // "NAME" : java.lang.String
		params.addValue("stateId", business.getStateId()); // "STATE_ID" : java.lang.String
		params.addValue("custId", business.getCustId()); // "CUST_ID" : java.math.BigDecimal
	
		return params;
	}
	
    //----------------------------------------------------------------------
	@Override
	protected SqlParameterSource getValuesForUpdate(Business business) {
		MapSqlParameterSource params = new MapSqlParameterSource();	
		params.addValue("incorpDate", business.getIncorpDate()); // "INCORP_DATE" : java.util.Date
		params.addValue("name", business.getName()); // "NAME" : java.lang.String
		params.addValue("stateId", business.getStateId()); // "STATE_ID" : java.lang.String
		params.addValue("custId", business.getCustId()); // "CUST_ID" : java.math.BigDecimal
	
		return params;
	}
	//----------------------------------------------------------------------
	@Override
	protected SqlParameterSource getValuesForPrimaryKey(Business business)  {
		MapSqlParameterSource params = new MapSqlParameterSource();	
		params.addValue("custId", business.getCustId()); // "CUST_ID" : java.math.BigDecimal
	
		return params;
	}
	//----------------------------------------------------------------------
	@Override
	protected RowMapper<Business> getRowMapper()  {
		//--- RowMapper to populate a new bean instance
		return new BusinessRowMapper() ;
	}

    //----------------------------------------------------------------------
	/**
	 * Populates the given bean with the data retrieved from the given ResultSet
	 * @param rs
	 * @param business
	 * @throws SQLException
	 */
	private void populateBean(ResultSet rs, Business business) throws SQLException {

		//--- Set data from ResultSet to Bean attributes
		business.setIncorpDate(rs.getDate("INCORP_DATE")); // java.util.Date
		business.setName(rs.getString("NAME")); // java.lang.String
		business.setStateId(rs.getString("STATE_ID")); // java.lang.String
		business.setCustId(rs.getBigDecimal("CUST_ID")); // java.math.BigDecimal
		if ( rs.wasNull() ) { business.setCustId(null); }; // not primitive number => keep null value if any
	}

    //----------------------------------------------------------------------
	/**
	 * Specific inner class for 'RowMapper' implementation
	 */
	private class BusinessRowMapper implements RowMapper<Business> {

		public Business mapRow(ResultSet rs, int rowNum) throws SQLException {
			Business bean = new Business();
			populateBean(rs, bean);
			return bean;
		}
	}
}
