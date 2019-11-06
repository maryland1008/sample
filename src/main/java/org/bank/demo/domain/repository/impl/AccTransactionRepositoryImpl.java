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

import org.bank.demo.domain.AccTransaction;
import org.bank.demo.domain.repository.commons.GenericDAO;
import org.bank.demo.domain.repository.AccTransactionRepository;

import java.math.BigDecimal;

@Repository
public class AccTransactionRepositoryImpl extends GenericDAO<AccTransaction> implements AccTransactionRepository<AccTransaction> {
	
    private static final Logger logger = Logger.getLogger(AccTransactionRepositoryImpl.class);

	private final static String TABLE_NAME = "ACC_TRANSACTION";	

	private final static String SQL_SELECT =  "select TXN_ID, AMOUNT, FUNDS_AVAIL_DATE, TXN_DATE, TXN_TYPE_CD, ACCOUNT_ID, EXECUTION_BRANCH_ID, TELLER_EMP_ID  from ACC_TRANSACTION where TXN_ID = :txnId";
	
	private final static String SQL_INSERT = "insert into ACC_TRANSACTION (TXN_ID, AMOUNT, FUNDS_AVAIL_DATE, TXN_DATE, TXN_TYPE_CD, ACCOUNT_ID, EXECUTION_BRANCH_ID, TELLER_EMP_ID) values (:txnId, :amount, :fundsAvailDate, :txnDate, :txnTypeCd, :accountId, :executionBranchId, :tellerEmpId)";
	
	private final static String SQL_UPDATE = "update ACC_TRANSACTION set AMOUNT = :amount, FUNDS_AVAIL_DATE = :fundsAvailDate, TXN_DATE = :txnDate, TXN_TYPE_CD = :txnTypeCd, ACCOUNT_ID = :accountId, EXECUTION_BRANCH_ID = :executionBranchId, TELLER_EMP_ID = :tellerEmpId where TXN_ID = :txnId";

	private final static String SQL_DELETE = "delete from ACC_TRANSACTION where TXN_ID = :txnId";

	private final static String SQL_COUNT_ALL =  "select count(*) from ACC_TRANSACTION";

	private final static String SQL_COUNT = "select count(*) from ACC_TRANSACTION where TXN_ID = :txnId";

	@SuppressWarnings("unchecked")
	protected final Map<String, String> fieldMap = MapUtils.putAll(new HashMap<String, String>(), new String[][] {
		{ "txnId", "TXN_ID" }, 
		{ "amount", "AMOUNT" }, 
		{ "fundsAvailDate", "FUNDS_AVAIL_DATE" }, 
		{ "txnDate", "TXN_DATE" }, 
		{ "txnTypeCd", "TXN_TYPE_CD" }, 
		{ "accountId", "ACCOUNT_ID" }, 
		{ "executionBranchId", "EXECUTION_BRANCH_ID" }, 
		{ "tellerEmpId", "TELLER_EMP_ID" }	});

	public AccTransactionRepositoryImpl() {
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
	public AccTransaction findByPrimaryKey( BigDecimal txnId ) {
		MapSqlParameterSource primaryKey = new MapSqlParameterSource();	
		primaryKey.addValue("txnId", txnId);
		return super.doSelect(primaryKey);		
	}

	@Override
	public AccTransaction findByQuery(String sql, Map<String,List<?>> params) {
		MapSqlParameterSource sqlParams = new MapSqlParameterSource();	
		for(String field : fieldMap.keySet()) {
			if(params.containsKey(field)) {
				sqlParams.addValue(field, params.get(field));
			}
		}
		
		RowMapper<AccTransaction> rowMapper = getRowMapper();
		try {
			return jdbcTemplate.queryForObject(sql, sqlParams, rowMapper);
		} catch (EmptyResultDataAccessException e) {
			getLogger().info("Record not found - " + getTableName());
			return null;
		}
	}	

	@Override
	public List<AccTransaction> findBySqlParameterSource(Map<String,List<?>> params) {
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
		
		String sql = "select * from ACC_TRANSACTION";
		
		if(sb.length()>0) {
			sql += sb.toString();
		} 
		
		try {
			List<AccTransaction> list = jdbcTemplate.query(sql, sqlParams, new AccTransactionRowMapper());
			return list;
		} catch (EmptyResultDataAccessException e) {
			logger.info("No data found in ACC_TRANSACTION");
			return null;
		}	
	}

	@Override
	public int deleteByPrimaryKey( BigDecimal txnId ) {
		MapSqlParameterSource primaryKey = new MapSqlParameterSource();	
		primaryKey.addValue("txnId", txnId);
		return super.doDelete(primaryKey);		
	}

	@Override
	public boolean exists( BigDecimal txnId ) {
		MapSqlParameterSource primaryKey = new MapSqlParameterSource();	
		primaryKey.addValue("txnId", txnId);
		return super.doExists(primaryKey);
	}

	@Override
	public List<AccTransaction> findAll() {
		String sql = "select * from ACC_TRANSACTION";
		try {
			List<AccTransaction> list = jdbcTemplate.query(sql, new AccTransactionRowMapper());
			return list;
		} catch (EmptyResultDataAccessException e) {
			logger.info("No data found in ACC_TRANSACTION");
			return null;
		}	
	}
	
	@Override
	public BigDecimal insertAccTransaction(AccTransaction bean) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int result = jdbcTemplate.update(getSqlInsert(), getValuesForInsert(bean), keyHolder,
				new String[] { "TXN_ID" });
		if (result != 1) {
			getLogger().error("INSERT failed - " + getTableName());
			throw new RuntimeException("Unexpected return value after INSERT : " + result + " (1 expected) ");
		} else {
			return new BigDecimal(keyHolder.getKey().intValue());
		}
	}	
	
	@Override
	public void insert(AccTransaction accTransaction) {
		super.doInsert(accTransaction);
	}	

	@Override
	public int update(AccTransaction accTransaction) {
		return super.doUpdate(accTransaction);
	}	

	@Override
	public int delete( AccTransaction accTransaction ) {
		return super.doDelete(accTransaction);
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
	protected SqlParameterSource getValuesForInsert(AccTransaction accTransaction)  {
		MapSqlParameterSource params = new MapSqlParameterSource();	
		params.addValue("txnId", accTransaction.getTxnId()); // "TXN_ID" : java.math.BigDecimal
		params.addValue("amount", accTransaction.getAmount()); // "AMOUNT" : java.lang.Double
		params.addValue("fundsAvailDate", accTransaction.getFundsAvailDate()); // "FUNDS_AVAIL_DATE" : java.util.Date
		params.addValue("txnDate", accTransaction.getTxnDate()); // "TXN_DATE" : java.util.Date
		params.addValue("txnTypeCd", accTransaction.getTxnTypeCd()); // "TXN_TYPE_CD" : java.lang.String
		params.addValue("accountId", accTransaction.getAccountId()); // "ACCOUNT_ID" : java.math.BigDecimal
		params.addValue("executionBranchId", accTransaction.getExecutionBranchId()); // "EXECUTION_BRANCH_ID" : java.math.BigDecimal
		params.addValue("tellerEmpId", accTransaction.getTellerEmpId()); // "TELLER_EMP_ID" : java.math.BigDecimal
	
		return params;
	}
	
    //----------------------------------------------------------------------
	@Override
	protected SqlParameterSource getValuesForUpdate(AccTransaction accTransaction) {
		MapSqlParameterSource params = new MapSqlParameterSource();	
		params.addValue("txnId", accTransaction.getTxnId()); // "TXN_ID" : java.math.BigDecimal
		params.addValue("amount", accTransaction.getAmount()); // "AMOUNT" : java.lang.Double
		params.addValue("fundsAvailDate", accTransaction.getFundsAvailDate()); // "FUNDS_AVAIL_DATE" : java.util.Date
		params.addValue("txnDate", accTransaction.getTxnDate()); // "TXN_DATE" : java.util.Date
		params.addValue("txnTypeCd", accTransaction.getTxnTypeCd()); // "TXN_TYPE_CD" : java.lang.String
		params.addValue("accountId", accTransaction.getAccountId()); // "ACCOUNT_ID" : java.math.BigDecimal
		params.addValue("executionBranchId", accTransaction.getExecutionBranchId()); // "EXECUTION_BRANCH_ID" : java.math.BigDecimal
		params.addValue("tellerEmpId", accTransaction.getTellerEmpId()); // "TELLER_EMP_ID" : java.math.BigDecimal
	
		return params;
	}
	//----------------------------------------------------------------------
	@Override
	protected SqlParameterSource getValuesForPrimaryKey(AccTransaction accTransaction)  {
		MapSqlParameterSource params = new MapSqlParameterSource();	
		params.addValue("txnId", accTransaction.getTxnId()); // "TXN_ID" : java.math.BigDecimal
	
		return params;
	}
	//----------------------------------------------------------------------
	@Override
	protected RowMapper<AccTransaction> getRowMapper()  {
		//--- RowMapper to populate a new bean instance
		return new AccTransactionRowMapper() ;
	}

    //----------------------------------------------------------------------
	/**
	 * Populates the given bean with the data retrieved from the given ResultSet
	 * @param rs
	 * @param accTransaction
	 * @throws SQLException
	 */
	private void populateBean(ResultSet rs, AccTransaction accTransaction) throws SQLException {

		//--- Set data from ResultSet to Bean attributes
		accTransaction.setTxnId(rs.getBigDecimal("TXN_ID")); // java.math.BigDecimal
		if ( rs.wasNull() ) { accTransaction.setTxnId(null); }; // not primitive number => keep null value if any
		accTransaction.setAmount(rs.getDouble("AMOUNT")); // java.lang.Double
		if ( rs.wasNull() ) { accTransaction.setAmount(null); }; // not primitive number => keep null value if any
		accTransaction.setFundsAvailDate(rs.getDate("FUNDS_AVAIL_DATE")); // java.util.Date
		accTransaction.setTxnDate(rs.getDate("TXN_DATE")); // java.util.Date
		accTransaction.setTxnTypeCd(rs.getString("TXN_TYPE_CD")); // java.lang.String
		accTransaction.setAccountId(rs.getBigDecimal("ACCOUNT_ID")); // java.math.BigDecimal
		if ( rs.wasNull() ) { accTransaction.setAccountId(null); }; // not primitive number => keep null value if any
		accTransaction.setExecutionBranchId(rs.getBigDecimal("EXECUTION_BRANCH_ID")); // java.math.BigDecimal
		if ( rs.wasNull() ) { accTransaction.setExecutionBranchId(null); }; // not primitive number => keep null value if any
		accTransaction.setTellerEmpId(rs.getBigDecimal("TELLER_EMP_ID")); // java.math.BigDecimal
		if ( rs.wasNull() ) { accTransaction.setTellerEmpId(null); }; // not primitive number => keep null value if any
	}

    //----------------------------------------------------------------------
	/**
	 * Specific inner class for 'RowMapper' implementation
	 */
	private class AccTransactionRowMapper implements RowMapper<AccTransaction> {

		public AccTransaction mapRow(ResultSet rs, int rowNum) throws SQLException {
			AccTransaction bean = new AccTransaction();
			populateBean(rs, bean);
			return bean;
		}
	}
}
