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

import org.bank.demo.domain.Branch;
import org.bank.demo.domain.repository.commons.GenericDAO;
import org.bank.demo.domain.repository.BranchRepository;

import java.math.BigDecimal;

@Repository
public class BranchRepositoryImpl extends GenericDAO<Branch> implements BranchRepository<Branch> {
	
    private static final Logger logger = Logger.getLogger(BranchRepositoryImpl.class);

	private final static String TABLE_NAME = "BRANCH";	

	private final static String SQL_SELECT =  "select BRANCH_ID, ADDRESS, CITY, NAME, STATE, ZIP_CODE  from BRANCH where BRANCH_ID = :branchId";
	
	private final static String SQL_INSERT = "insert into BRANCH (BRANCH_ID, ADDRESS, CITY, NAME, STATE, ZIP_CODE) values (:branchId, :address, :city, :name, :state, :zipCode)";
	
	private final static String SQL_UPDATE = "update BRANCH set ADDRESS = :address, CITY = :city, NAME = :name, STATE = :state, ZIP_CODE = :zipCode where BRANCH_ID = :branchId";

	private final static String SQL_DELETE = "delete from BRANCH where BRANCH_ID = :branchId";

	private final static String SQL_COUNT_ALL =  "select count(*) from BRANCH";

	private final static String SQL_COUNT = "select count(*) from BRANCH where BRANCH_ID = :branchId";

	@SuppressWarnings("unchecked")
	protected final Map<String, String> fieldMap = MapUtils.putAll(new HashMap<String, String>(), new String[][] {
		{ "branchId", "BRANCH_ID" }, 
		{ "address", "ADDRESS" }, 
		{ "city", "CITY" }, 
		{ "name", "NAME" }, 
		{ "state", "STATE" }, 
		{ "zipCode", "ZIP_CODE" }	});

	public BranchRepositoryImpl() {
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
	public Branch findByPrimaryKey( BigDecimal branchId ) {
		MapSqlParameterSource primaryKey = new MapSqlParameterSource();	
		primaryKey.addValue("branchId", branchId);
		return super.doSelect(primaryKey);		
	}

	@Override
	public Branch findByQuery(String sql, Map<String,List<?>> params) {
		MapSqlParameterSource sqlParams = new MapSqlParameterSource();	
		for(String field : fieldMap.keySet()) {
			if(params.containsKey(field)) {
				sqlParams.addValue(field, params.get(field));
			}
		}
		
		RowMapper<Branch> rowMapper = getRowMapper();
		try {
			return jdbcTemplate.queryForObject(sql, sqlParams, rowMapper);
		} catch (EmptyResultDataAccessException e) {
			getLogger().info("Record not found - " + getTableName());
			return null;
		}
	}	

	@Override
	public List<Branch> findBySqlParameterSource(Map<String,List<?>> params) {
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
		
		String sql = "select * from BRANCH";
		
		if(sb.length()>0) {
			sql += sb.toString();
		} 
		
		try {
			List<Branch> list = jdbcTemplate.query(sql, sqlParams, new BranchRowMapper());
			return list;
		} catch (EmptyResultDataAccessException e) {
			logger.info("No data found in BRANCH");
			return null;
		}	
	}

	@Override
	public int deleteByPrimaryKey( BigDecimal branchId ) {
		MapSqlParameterSource primaryKey = new MapSqlParameterSource();	
		primaryKey.addValue("branchId", branchId);
		return super.doDelete(primaryKey);		
	}

	@Override
	public boolean exists( BigDecimal branchId ) {
		MapSqlParameterSource primaryKey = new MapSqlParameterSource();	
		primaryKey.addValue("branchId", branchId);
		return super.doExists(primaryKey);
	}

	@Override
	public List<Branch> findAll() {
		String sql = "select * from BRANCH";
		try {
			List<Branch> list = jdbcTemplate.query(sql, new BranchRowMapper());
			return list;
		} catch (EmptyResultDataAccessException e) {
			logger.info("No data found in BRANCH");
			return null;
		}	
	}
	
	@Override
	public BigDecimal insertBranch(Branch bean) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int result = jdbcTemplate.update(getSqlInsert(), getValuesForInsert(bean), keyHolder,
				new String[] { "BRANCH_ID" });
		if (result != 1) {
			getLogger().error("INSERT failed - " + getTableName());
			throw new RuntimeException("Unexpected return value after INSERT : " + result + " (1 expected) ");
		} else {
			return new BigDecimal(keyHolder.getKey().intValue());
		}
	}	
	
	@Override
	public void insert(Branch branch) {
		super.doInsert(branch);
	}	

	@Override
	public int update(Branch branch) {
		return super.doUpdate(branch);
	}	

	@Override
	public int delete( Branch branch ) {
		return super.doDelete(branch);
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
	protected SqlParameterSource getValuesForInsert(Branch branch)  {
		MapSqlParameterSource params = new MapSqlParameterSource();	
		params.addValue("branchId", branch.getBranchId()); // "BRANCH_ID" : java.math.BigDecimal
		params.addValue("address", branch.getAddress()); // "ADDRESS" : java.lang.String
		params.addValue("city", branch.getCity()); // "CITY" : java.lang.String
		params.addValue("name", branch.getName()); // "NAME" : java.lang.String
		params.addValue("state", branch.getState()); // "STATE" : java.lang.String
		params.addValue("zipCode", branch.getZipCode()); // "ZIP_CODE" : java.lang.String
	
		return params;
	}
	
    //----------------------------------------------------------------------
	@Override
	protected SqlParameterSource getValuesForUpdate(Branch branch) {
		MapSqlParameterSource params = new MapSqlParameterSource();	
		params.addValue("branchId", branch.getBranchId()); // "BRANCH_ID" : java.math.BigDecimal
		params.addValue("address", branch.getAddress()); // "ADDRESS" : java.lang.String
		params.addValue("city", branch.getCity()); // "CITY" : java.lang.String
		params.addValue("name", branch.getName()); // "NAME" : java.lang.String
		params.addValue("state", branch.getState()); // "STATE" : java.lang.String
		params.addValue("zipCode", branch.getZipCode()); // "ZIP_CODE" : java.lang.String
	
		return params;
	}
	//----------------------------------------------------------------------
	@Override
	protected SqlParameterSource getValuesForPrimaryKey(Branch branch)  {
		MapSqlParameterSource params = new MapSqlParameterSource();	
		params.addValue("branchId", branch.getBranchId()); // "BRANCH_ID" : java.math.BigDecimal
	
		return params;
	}
	//----------------------------------------------------------------------
	@Override
	protected RowMapper<Branch> getRowMapper()  {
		//--- RowMapper to populate a new bean instance
		return new BranchRowMapper() ;
	}

    //----------------------------------------------------------------------
	/**
	 * Populates the given bean with the data retrieved from the given ResultSet
	 * @param rs
	 * @param branch
	 * @throws SQLException
	 */
	private void populateBean(ResultSet rs, Branch branch) throws SQLException {

		//--- Set data from ResultSet to Bean attributes
		branch.setBranchId(rs.getBigDecimal("BRANCH_ID")); // java.math.BigDecimal
		if ( rs.wasNull() ) { branch.setBranchId(null); }; // not primitive number => keep null value if any
		branch.setAddress(rs.getString("ADDRESS")); // java.lang.String
		branch.setCity(rs.getString("CITY")); // java.lang.String
		branch.setName(rs.getString("NAME")); // java.lang.String
		branch.setState(rs.getString("STATE")); // java.lang.String
		branch.setZipCode(rs.getString("ZIP_CODE")); // java.lang.String
	}

    //----------------------------------------------------------------------
	/**
	 * Specific inner class for 'RowMapper' implementation
	 */
	private class BranchRowMapper implements RowMapper<Branch> {

		public Branch mapRow(ResultSet rs, int rowNum) throws SQLException {
			Branch bean = new Branch();
			populateBean(rs, bean);
			return bean;
		}
	}
}
