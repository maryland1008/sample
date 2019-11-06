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

import org.bank.demo.domain.Individual;
import org.bank.demo.domain.repository.commons.GenericDAO;
import org.bank.demo.domain.repository.IndividualRepository;

import java.math.BigDecimal;

@Repository
public class IndividualRepositoryImpl extends GenericDAO<Individual> implements IndividualRepository<Individual> {
	
    private static final Logger logger = Logger.getLogger(IndividualRepositoryImpl.class);

	private final static String TABLE_NAME = "INDIVIDUAL";	

	private final static String SQL_SELECT =  "select BIRTH_DATE, FIRST_NAME, LAST_NAME, CUST_ID  from INDIVIDUAL where CUST_ID = :custId";
	
	private final static String SQL_INSERT = "insert into INDIVIDUAL (BIRTH_DATE, FIRST_NAME, LAST_NAME, CUST_ID) values (:birthDate, :firstName, :lastName, :custId)";
	
	private final static String SQL_UPDATE = "update INDIVIDUAL set BIRTH_DATE = :birthDate, FIRST_NAME = :firstName, LAST_NAME = :lastName where CUST_ID = :custId";

	private final static String SQL_DELETE = "delete from INDIVIDUAL where CUST_ID = :custId";

	private final static String SQL_COUNT_ALL =  "select count(*) from INDIVIDUAL";

	private final static String SQL_COUNT = "select count(*) from INDIVIDUAL where CUST_ID = :custId";

	@SuppressWarnings("unchecked")
	protected final Map<String, String> fieldMap = MapUtils.putAll(new HashMap<String, String>(), new String[][] {
		{ "birthDate", "BIRTH_DATE" }, 
		{ "firstName", "FIRST_NAME" }, 
		{ "lastName", "LAST_NAME" }, 
		{ "custId", "CUST_ID" }	});

	public IndividualRepositoryImpl() {
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
	public Individual findByPrimaryKey( BigDecimal custId ) {
		MapSqlParameterSource primaryKey = new MapSqlParameterSource();	
		primaryKey.addValue("custId", custId);
		return super.doSelect(primaryKey);		
	}

	@Override
	public Individual findByQuery(String sql, Map<String,List<?>> params) {
		MapSqlParameterSource sqlParams = new MapSqlParameterSource();	
		for(String field : fieldMap.keySet()) {
			if(params.containsKey(field)) {
				sqlParams.addValue(field, params.get(field));
			}
		}
		
		RowMapper<Individual> rowMapper = getRowMapper();
		try {
			return jdbcTemplate.queryForObject(sql, sqlParams, rowMapper);
		} catch (EmptyResultDataAccessException e) {
			getLogger().info("Record not found - " + getTableName());
			return null;
		}
	}	

	@Override
	public List<Individual> findBySqlParameterSource(Map<String,List<?>> params) {
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
		
		String sql = "select * from INDIVIDUAL";
		
		if(sb.length()>0) {
			sql += sb.toString();
		} 
		
		try {
			List<Individual> list = jdbcTemplate.query(sql, sqlParams, new IndividualRowMapper());
			return list;
		} catch (EmptyResultDataAccessException e) {
			logger.info("No data found in INDIVIDUAL");
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
	public List<Individual> findAll() {
		String sql = "select * from INDIVIDUAL";
		try {
			List<Individual> list = jdbcTemplate.query(sql, new IndividualRowMapper());
			return list;
		} catch (EmptyResultDataAccessException e) {
			logger.info("No data found in INDIVIDUAL");
			return null;
		}	
	}
	
	@Override
	public BigDecimal insertIndividual(Individual bean) {
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
	public void insert(Individual individual) {
		super.doInsert(individual);
	}	

	@Override
	public int update(Individual individual) {
		return super.doUpdate(individual);
	}	

	@Override
	public int delete( Individual individual ) {
		return super.doDelete(individual);
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
	protected SqlParameterSource getValuesForInsert(Individual individual)  {
		MapSqlParameterSource params = new MapSqlParameterSource();	
		params.addValue("birthDate", individual.getBirthDate()); // "BIRTH_DATE" : java.util.Date
		params.addValue("firstName", individual.getFirstName()); // "FIRST_NAME" : java.lang.String
		params.addValue("lastName", individual.getLastName()); // "LAST_NAME" : java.lang.String
		params.addValue("custId", individual.getCustId()); // "CUST_ID" : java.math.BigDecimal
	
		return params;
	}
	
    //----------------------------------------------------------------------
	@Override
	protected SqlParameterSource getValuesForUpdate(Individual individual) {
		MapSqlParameterSource params = new MapSqlParameterSource();	
		params.addValue("birthDate", individual.getBirthDate()); // "BIRTH_DATE" : java.util.Date
		params.addValue("firstName", individual.getFirstName()); // "FIRST_NAME" : java.lang.String
		params.addValue("lastName", individual.getLastName()); // "LAST_NAME" : java.lang.String
		params.addValue("custId", individual.getCustId()); // "CUST_ID" : java.math.BigDecimal
	
		return params;
	}
	//----------------------------------------------------------------------
	@Override
	protected SqlParameterSource getValuesForPrimaryKey(Individual individual)  {
		MapSqlParameterSource params = new MapSqlParameterSource();	
		params.addValue("custId", individual.getCustId()); // "CUST_ID" : java.math.BigDecimal
	
		return params;
	}
	//----------------------------------------------------------------------
	@Override
	protected RowMapper<Individual> getRowMapper()  {
		//--- RowMapper to populate a new bean instance
		return new IndividualRowMapper() ;
	}

    //----------------------------------------------------------------------
	/**
	 * Populates the given bean with the data retrieved from the given ResultSet
	 * @param rs
	 * @param individual
	 * @throws SQLException
	 */
	private void populateBean(ResultSet rs, Individual individual) throws SQLException {

		//--- Set data from ResultSet to Bean attributes
		individual.setBirthDate(rs.getDate("BIRTH_DATE")); // java.util.Date
		individual.setFirstName(rs.getString("FIRST_NAME")); // java.lang.String
		individual.setLastName(rs.getString("LAST_NAME")); // java.lang.String
		individual.setCustId(rs.getBigDecimal("CUST_ID")); // java.math.BigDecimal
		if ( rs.wasNull() ) { individual.setCustId(null); }; // not primitive number => keep null value if any
	}

    //----------------------------------------------------------------------
	/**
	 * Specific inner class for 'RowMapper' implementation
	 */
	private class IndividualRowMapper implements RowMapper<Individual> {

		public Individual mapRow(ResultSet rs, int rowNum) throws SQLException {
			Individual bean = new Individual();
			populateBean(rs, bean);
			return bean;
		}
	}
}
