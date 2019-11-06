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

import org.bank.demo.domain.Customer;
import org.bank.demo.domain.repository.commons.GenericDAO;
import org.bank.demo.domain.repository.CustomerRepository;

import java.math.BigDecimal;

@Repository
public class CustomerRepositoryImpl extends GenericDAO<Customer> implements CustomerRepository<Customer> {
	
    private static final Logger logger = Logger.getLogger(CustomerRepositoryImpl.class);

	private final static String TABLE_NAME = "CUSTOMER";	

	private final static String SQL_SELECT =  "select CUST_ID, ADDRESS, CITY, CUST_TYPE_CD, FED_ID, POSTAL_CODE, STATE  from CUSTOMER where CUST_ID = :custId";
	
	private final static String SQL_INSERT = "insert into CUSTOMER (CUST_ID, ADDRESS, CITY, CUST_TYPE_CD, FED_ID, POSTAL_CODE, STATE) values (:custId, :address, :city, :custTypeCd, :fedId, :postalCode, :state)";
	
	private final static String SQL_UPDATE = "update CUSTOMER set ADDRESS = :address, CITY = :city, CUST_TYPE_CD = :custTypeCd, FED_ID = :fedId, POSTAL_CODE = :postalCode, STATE = :state where CUST_ID = :custId";

	private final static String SQL_DELETE = "delete from CUSTOMER where CUST_ID = :custId";

	private final static String SQL_COUNT_ALL =  "select count(*) from CUSTOMER";

	private final static String SQL_COUNT = "select count(*) from CUSTOMER where CUST_ID = :custId";

	@SuppressWarnings("unchecked")
	protected final Map<String, String> fieldMap = MapUtils.putAll(new HashMap<String, String>(), new String[][] {
		{ "custId", "CUST_ID" }, 
		{ "address", "ADDRESS" }, 
		{ "city", "CITY" }, 
		{ "custTypeCd", "CUST_TYPE_CD" }, 
		{ "fedId", "FED_ID" }, 
		{ "postalCode", "POSTAL_CODE" }, 
		{ "state", "STATE" }	});

	public CustomerRepositoryImpl() {
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
	public Customer findByPrimaryKey( BigDecimal custId ) {
		MapSqlParameterSource primaryKey = new MapSqlParameterSource();	
		primaryKey.addValue("custId", custId);
		return super.doSelect(primaryKey);		
	}

	@Override
	public Customer findByQuery(String sql, Map<String,List<?>> params) {
		MapSqlParameterSource sqlParams = new MapSqlParameterSource();	
		for(String field : fieldMap.keySet()) {
			if(params.containsKey(field)) {
				sqlParams.addValue(field, params.get(field));
			}
		}
		
		RowMapper<Customer> rowMapper = getRowMapper();
		try {
			return jdbcTemplate.queryForObject(sql, sqlParams, rowMapper);
		} catch (EmptyResultDataAccessException e) {
			getLogger().info("Record not found - " + getTableName());
			return null;
		}
	}	

	@Override
	public List<Customer> findBySqlParameterSource(Map<String,List<?>> params) {
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
		
		String sql = "select * from CUSTOMER";
		
		if(sb.length()>0) {
			sql += sb.toString();
		} 
		
		try {
			List<Customer> list = jdbcTemplate.query(sql, sqlParams, new CustomerRowMapper());
			return list;
		} catch (EmptyResultDataAccessException e) {
			logger.info("No data found in CUSTOMER");
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
	public List<Customer> findAll() {
		String sql = "select * from CUSTOMER";
		try {
			List<Customer> list = jdbcTemplate.query(sql, new CustomerRowMapper());
			return list;
		} catch (EmptyResultDataAccessException e) {
			logger.info("No data found in CUSTOMER");
			return null;
		}	
	}
	
	@Override
	public BigDecimal insertCustomer(Customer bean) {
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
	public void insert(Customer customer) {
		super.doInsert(customer);
	}	

	@Override
	public int update(Customer customer) {
		return super.doUpdate(customer);
	}	

	@Override
	public int delete( Customer customer ) {
		return super.doDelete(customer);
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
	protected SqlParameterSource getValuesForInsert(Customer customer)  {
		MapSqlParameterSource params = new MapSqlParameterSource();	
		params.addValue("custId", customer.getCustId()); // "CUST_ID" : java.math.BigDecimal
		params.addValue("address", customer.getAddress()); // "ADDRESS" : java.lang.String
		params.addValue("city", customer.getCity()); // "CITY" : java.lang.String
		params.addValue("custTypeCd", customer.getCustTypeCd()); // "CUST_TYPE_CD" : java.lang.String
		params.addValue("fedId", customer.getFedId()); // "FED_ID" : java.lang.String
		params.addValue("postalCode", customer.getPostalCode()); // "POSTAL_CODE" : java.lang.String
		params.addValue("state", customer.getState()); // "STATE" : java.lang.String
	
		return params;
	}
	
    //----------------------------------------------------------------------
	@Override
	protected SqlParameterSource getValuesForUpdate(Customer customer) {
		MapSqlParameterSource params = new MapSqlParameterSource();	
		params.addValue("custId", customer.getCustId()); // "CUST_ID" : java.math.BigDecimal
		params.addValue("address", customer.getAddress()); // "ADDRESS" : java.lang.String
		params.addValue("city", customer.getCity()); // "CITY" : java.lang.String
		params.addValue("custTypeCd", customer.getCustTypeCd()); // "CUST_TYPE_CD" : java.lang.String
		params.addValue("fedId", customer.getFedId()); // "FED_ID" : java.lang.String
		params.addValue("postalCode", customer.getPostalCode()); // "POSTAL_CODE" : java.lang.String
		params.addValue("state", customer.getState()); // "STATE" : java.lang.String
	
		return params;
	}
	//----------------------------------------------------------------------
	@Override
	protected SqlParameterSource getValuesForPrimaryKey(Customer customer)  {
		MapSqlParameterSource params = new MapSqlParameterSource();	
		params.addValue("custId", customer.getCustId()); // "CUST_ID" : java.math.BigDecimal
	
		return params;
	}
	//----------------------------------------------------------------------
	@Override
	protected RowMapper<Customer> getRowMapper()  {
		//--- RowMapper to populate a new bean instance
		return new CustomerRowMapper() ;
	}

    //----------------------------------------------------------------------
	/**
	 * Populates the given bean with the data retrieved from the given ResultSet
	 * @param rs
	 * @param customer
	 * @throws SQLException
	 */
	private void populateBean(ResultSet rs, Customer customer) throws SQLException {

		//--- Set data from ResultSet to Bean attributes
		customer.setCustId(rs.getBigDecimal("CUST_ID")); // java.math.BigDecimal
		if ( rs.wasNull() ) { customer.setCustId(null); }; // not primitive number => keep null value if any
		customer.setAddress(rs.getString("ADDRESS")); // java.lang.String
		customer.setCity(rs.getString("CITY")); // java.lang.String
		customer.setCustTypeCd(rs.getString("CUST_TYPE_CD")); // java.lang.String
		customer.setFedId(rs.getString("FED_ID")); // java.lang.String
		customer.setPostalCode(rs.getString("POSTAL_CODE")); // java.lang.String
		customer.setState(rs.getString("STATE")); // java.lang.String
	}

    //----------------------------------------------------------------------
	/**
	 * Specific inner class for 'RowMapper' implementation
	 */
	private class CustomerRowMapper implements RowMapper<Customer> {

		public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
			Customer bean = new Customer();
			populateBean(rs, bean);
			return bean;
		}
	}
}
