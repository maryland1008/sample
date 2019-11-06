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

import org.bank.demo.domain.Account;
import org.bank.demo.domain.repository.commons.GenericDAO;
import org.bank.demo.domain.repository.AccountRepository;

import java.math.BigDecimal;

@Repository
public class AccountRepositoryImpl extends GenericDAO<Account> implements AccountRepository<Account> {
	
    private static final Logger logger = Logger.getLogger(AccountRepositoryImpl.class);

	private final static String TABLE_NAME = "ACCOUNT";	

	private final static String SQL_SELECT =  "select ACCOUNT_ID, AVAIL_BALANCE, CLOSE_DATE, LAST_ACTIVITY_DATE, OPEN_DATE, PENDING_BALANCE, STATUS, CUST_ID, OPEN_BRANCH_ID, OPEN_EMP_ID, PRODUCT_CD  from ACCOUNT where ACCOUNT_ID = :accountId";
	
	private final static String SQL_INSERT = "insert into ACCOUNT (ACCOUNT_ID, AVAIL_BALANCE, CLOSE_DATE, LAST_ACTIVITY_DATE, OPEN_DATE, PENDING_BALANCE, STATUS, CUST_ID, OPEN_BRANCH_ID, OPEN_EMP_ID, PRODUCT_CD) values (:accountId, :availBalance, :closeDate, :lastActivityDate, :openDate, :pendingBalance, :status, :custId, :openBranchId, :openEmpId, :productCd)";
	
	private final static String SQL_UPDATE = "update ACCOUNT set AVAIL_BALANCE = :availBalance, CLOSE_DATE = :closeDate, LAST_ACTIVITY_DATE = :lastActivityDate, OPEN_DATE = :openDate, PENDING_BALANCE = :pendingBalance, STATUS = :status, CUST_ID = :custId, OPEN_BRANCH_ID = :openBranchId, OPEN_EMP_ID = :openEmpId, PRODUCT_CD = :productCd where ACCOUNT_ID = :accountId";

	private final static String SQL_DELETE = "delete from ACCOUNT where ACCOUNT_ID = :accountId";

	private final static String SQL_COUNT_ALL =  "select count(*) from ACCOUNT";

	private final static String SQL_COUNT = "select count(*) from ACCOUNT where ACCOUNT_ID = :accountId";

	@SuppressWarnings("unchecked")
	protected final Map<String, String> fieldMap = MapUtils.putAll(new HashMap<String, String>(), new String[][] {
		{ "accountId", "ACCOUNT_ID" }, 
		{ "availBalance", "AVAIL_BALANCE" }, 
		{ "closeDate", "CLOSE_DATE" }, 
		{ "lastActivityDate", "LAST_ACTIVITY_DATE" }, 
		{ "openDate", "OPEN_DATE" }, 
		{ "pendingBalance", "PENDING_BALANCE" }, 
		{ "status", "STATUS" }, 
		{ "custId", "CUST_ID" }, 
		{ "openBranchId", "OPEN_BRANCH_ID" }, 
		{ "openEmpId", "OPEN_EMP_ID" }, 
		{ "productCd", "PRODUCT_CD" }	});

	public AccountRepositoryImpl() {
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
	public Account findByPrimaryKey( BigDecimal accountId ) {
		MapSqlParameterSource primaryKey = new MapSqlParameterSource();	
		primaryKey.addValue("accountId", accountId);
		return super.doSelect(primaryKey);		
	}

	@Override
	public Account findByQuery(String sql, Map<String,List<?>> params) {
		MapSqlParameterSource sqlParams = new MapSqlParameterSource();	
		for(String field : fieldMap.keySet()) {
			if(params.containsKey(field)) {
				sqlParams.addValue(field, params.get(field));
			}
		}
		
		RowMapper<Account> rowMapper = getRowMapper();
		try {
			return jdbcTemplate.queryForObject(sql, sqlParams, rowMapper);
		} catch (EmptyResultDataAccessException e) {
			getLogger().info("Record not found - " + getTableName());
			return null;
		}
	}	

	@Override
	public List<Account> findBySqlParameterSource(Map<String,List<?>> params) {
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
		
		String sql = "select * from ACCOUNT";
		
		if(sb.length()>0) {
			sql += sb.toString();
		} 
		
		try {
			List<Account> list = jdbcTemplate.query(sql, sqlParams, new AccountRowMapper());
			return list;
		} catch (EmptyResultDataAccessException e) {
			logger.info("No data found in ACCOUNT");
			return null;
		}	
	}

	@Override
	public int deleteByPrimaryKey( BigDecimal accountId ) {
		MapSqlParameterSource primaryKey = new MapSqlParameterSource();	
		primaryKey.addValue("accountId", accountId);
		return super.doDelete(primaryKey);		
	}

	@Override
	public boolean exists( BigDecimal accountId ) {
		MapSqlParameterSource primaryKey = new MapSqlParameterSource();	
		primaryKey.addValue("accountId", accountId);
		return super.doExists(primaryKey);
	}

	@Override
	public List<Account> findAll() {
		String sql = "select * from ACCOUNT";
		try {
			List<Account> list = jdbcTemplate.query(sql, new AccountRowMapper());
			return list;
		} catch (EmptyResultDataAccessException e) {
			logger.info("No data found in ACCOUNT");
			return null;
		}	
	}
	
	@Override
	public BigDecimal insertAccount(Account bean) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int result = jdbcTemplate.update(getSqlInsert(), getValuesForInsert(bean), keyHolder,
				new String[] { "ACCOUNT_ID" });
		if (result != 1) {
			getLogger().error("INSERT failed - " + getTableName());
			throw new RuntimeException("Unexpected return value after INSERT : " + result + " (1 expected) ");
		} else {
			return new BigDecimal(keyHolder.getKey().intValue());
		}
	}	
	
	@Override
	public void insert(Account account) {
		super.doInsert(account);
	}	

	@Override
	public int update(Account account) {
		return super.doUpdate(account);
	}	

	@Override
	public int delete( Account account ) {
		return super.doDelete(account);
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
	protected SqlParameterSource getValuesForInsert(Account account)  {
		MapSqlParameterSource params = new MapSqlParameterSource();	
		params.addValue("accountId", account.getAccountId()); // "ACCOUNT_ID" : java.math.BigDecimal
		params.addValue("availBalance", account.getAvailBalance()); // "AVAIL_BALANCE" : java.lang.Double
		params.addValue("closeDate", account.getCloseDate()); // "CLOSE_DATE" : java.util.Date
		params.addValue("lastActivityDate", account.getLastActivityDate()); // "LAST_ACTIVITY_DATE" : java.util.Date
		params.addValue("openDate", account.getOpenDate()); // "OPEN_DATE" : java.util.Date
		params.addValue("pendingBalance", account.getPendingBalance()); // "PENDING_BALANCE" : java.lang.Double
		params.addValue("status", account.getStatus()); // "STATUS" : java.lang.String
		params.addValue("custId", account.getCustId()); // "CUST_ID" : java.math.BigDecimal
		params.addValue("openBranchId", account.getOpenBranchId()); // "OPEN_BRANCH_ID" : java.math.BigDecimal
		params.addValue("openEmpId", account.getOpenEmpId()); // "OPEN_EMP_ID" : java.math.BigDecimal
		params.addValue("productCd", account.getProductCd()); // "PRODUCT_CD" : java.lang.String
	
		return params;
	}
	
    //----------------------------------------------------------------------
	@Override
	protected SqlParameterSource getValuesForUpdate(Account account) {
		MapSqlParameterSource params = new MapSqlParameterSource();	
		params.addValue("accountId", account.getAccountId()); // "ACCOUNT_ID" : java.math.BigDecimal
		params.addValue("availBalance", account.getAvailBalance()); // "AVAIL_BALANCE" : java.lang.Double
		params.addValue("closeDate", account.getCloseDate()); // "CLOSE_DATE" : java.util.Date
		params.addValue("lastActivityDate", account.getLastActivityDate()); // "LAST_ACTIVITY_DATE" : java.util.Date
		params.addValue("openDate", account.getOpenDate()); // "OPEN_DATE" : java.util.Date
		params.addValue("pendingBalance", account.getPendingBalance()); // "PENDING_BALANCE" : java.lang.Double
		params.addValue("status", account.getStatus()); // "STATUS" : java.lang.String
		params.addValue("custId", account.getCustId()); // "CUST_ID" : java.math.BigDecimal
		params.addValue("openBranchId", account.getOpenBranchId()); // "OPEN_BRANCH_ID" : java.math.BigDecimal
		params.addValue("openEmpId", account.getOpenEmpId()); // "OPEN_EMP_ID" : java.math.BigDecimal
		params.addValue("productCd", account.getProductCd()); // "PRODUCT_CD" : java.lang.String
	
		return params;
	}
	//----------------------------------------------------------------------
	@Override
	protected SqlParameterSource getValuesForPrimaryKey(Account account)  {
		MapSqlParameterSource params = new MapSqlParameterSource();	
		params.addValue("accountId", account.getAccountId()); // "ACCOUNT_ID" : java.math.BigDecimal
	
		return params;
	}
	//----------------------------------------------------------------------
	@Override
	protected RowMapper<Account> getRowMapper()  {
		//--- RowMapper to populate a new bean instance
		return new AccountRowMapper() ;
	}

    //----------------------------------------------------------------------
	/**
	 * Populates the given bean with the data retrieved from the given ResultSet
	 * @param rs
	 * @param account
	 * @throws SQLException
	 */
	private void populateBean(ResultSet rs, Account account) throws SQLException {

		//--- Set data from ResultSet to Bean attributes
		account.setAccountId(rs.getBigDecimal("ACCOUNT_ID")); // java.math.BigDecimal
		if ( rs.wasNull() ) { account.setAccountId(null); }; // not primitive number => keep null value if any
		account.setAvailBalance(rs.getDouble("AVAIL_BALANCE")); // java.lang.Double
		if ( rs.wasNull() ) { account.setAvailBalance(null); }; // not primitive number => keep null value if any
		account.setCloseDate(rs.getDate("CLOSE_DATE")); // java.util.Date
		account.setLastActivityDate(rs.getDate("LAST_ACTIVITY_DATE")); // java.util.Date
		account.setOpenDate(rs.getDate("OPEN_DATE")); // java.util.Date
		account.setPendingBalance(rs.getDouble("PENDING_BALANCE")); // java.lang.Double
		if ( rs.wasNull() ) { account.setPendingBalance(null); }; // not primitive number => keep null value if any
		account.setStatus(rs.getString("STATUS")); // java.lang.String
		account.setCustId(rs.getBigDecimal("CUST_ID")); // java.math.BigDecimal
		if ( rs.wasNull() ) { account.setCustId(null); }; // not primitive number => keep null value if any
		account.setOpenBranchId(rs.getBigDecimal("OPEN_BRANCH_ID")); // java.math.BigDecimal
		if ( rs.wasNull() ) { account.setOpenBranchId(null); }; // not primitive number => keep null value if any
		account.setOpenEmpId(rs.getBigDecimal("OPEN_EMP_ID")); // java.math.BigDecimal
		if ( rs.wasNull() ) { account.setOpenEmpId(null); }; // not primitive number => keep null value if any
		account.setProductCd(rs.getString("PRODUCT_CD")); // java.lang.String
	}

    //----------------------------------------------------------------------
	/**
	 * Specific inner class for 'RowMapper' implementation
	 */
	private class AccountRowMapper implements RowMapper<Account> {

		public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
			Account bean = new Account();
			populateBean(rs, bean);
			return bean;
		}
	}
}
