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

import org.bank.demo.domain.ProductType;
import org.bank.demo.domain.repository.commons.GenericDAO;
import org.bank.demo.domain.repository.ProductTypeRepository;


@Repository
public class ProductTypeRepositoryImpl extends GenericDAO<ProductType> implements ProductTypeRepository<ProductType> {
	
    private static final Logger logger = Logger.getLogger(ProductTypeRepositoryImpl.class);

	private final static String TABLE_NAME = "PRODUCT_TYPE";	

	private final static String SQL_SELECT =  "select PRODUCT_TYPE_CD, NAME  from PRODUCT_TYPE where PRODUCT_TYPE_CD = :productTypeCd";
	
	private final static String SQL_INSERT = "insert into PRODUCT_TYPE (PRODUCT_TYPE_CD, NAME) values (:productTypeCd, :name)";
	
	private final static String SQL_UPDATE = "update PRODUCT_TYPE set NAME = :name where PRODUCT_TYPE_CD = :productTypeCd";

	private final static String SQL_DELETE = "delete from PRODUCT_TYPE where PRODUCT_TYPE_CD = :productTypeCd";

	private final static String SQL_COUNT_ALL =  "select count(*) from PRODUCT_TYPE";

	private final static String SQL_COUNT = "select count(*) from PRODUCT_TYPE where PRODUCT_TYPE_CD = :productTypeCd";

	@SuppressWarnings("unchecked")
	protected final Map<String, String> fieldMap = MapUtils.putAll(new HashMap<String, String>(), new String[][] {
		{ "productTypeCd", "PRODUCT_TYPE_CD" }, 
		{ "name", "NAME" }	});

	public ProductTypeRepositoryImpl() {
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
	public ProductType findByPrimaryKey( String productTypeCd ) {
		MapSqlParameterSource primaryKey = new MapSqlParameterSource();	
		primaryKey.addValue("productTypeCd", productTypeCd);
		return super.doSelect(primaryKey);		
	}

	@Override
	public ProductType findByQuery(String sql, Map<String,List<?>> params) {
		MapSqlParameterSource sqlParams = new MapSqlParameterSource();	
		for(String field : fieldMap.keySet()) {
			if(params.containsKey(field)) {
				sqlParams.addValue(field, params.get(field));
			}
		}
		
		RowMapper<ProductType> rowMapper = getRowMapper();
		try {
			return jdbcTemplate.queryForObject(sql, sqlParams, rowMapper);
		} catch (EmptyResultDataAccessException e) {
			getLogger().info("Record not found - " + getTableName());
			return null;
		}
	}	

	@Override
	public List<ProductType> findBySqlParameterSource(Map<String,List<?>> params) {
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
		
		String sql = "select * from PRODUCT_TYPE";
		
		if(sb.length()>0) {
			sql += sb.toString();
		} 
		
		try {
			List<ProductType> list = jdbcTemplate.query(sql, sqlParams, new ProductTypeRowMapper());
			return list;
		} catch (EmptyResultDataAccessException e) {
			logger.info("No data found in PRODUCT_TYPE");
			return null;
		}	
	}

	@Override
	public int deleteByPrimaryKey( String productTypeCd ) {
		MapSqlParameterSource primaryKey = new MapSqlParameterSource();	
		primaryKey.addValue("productTypeCd", productTypeCd);
		return super.doDelete(primaryKey);		
	}

	@Override
	public boolean exists( String productTypeCd ) {
		MapSqlParameterSource primaryKey = new MapSqlParameterSource();	
		primaryKey.addValue("productTypeCd", productTypeCd);
		return super.doExists(primaryKey);
	}

	@Override
	public List<ProductType> findAll() {
		String sql = "select * from PRODUCT_TYPE";
		try {
			List<ProductType> list = jdbcTemplate.query(sql, new ProductTypeRowMapper());
			return list;
		} catch (EmptyResultDataAccessException e) {
			logger.info("No data found in PRODUCT_TYPE");
			return null;
		}	
	}
	
	@Override
	public String insertProductType(ProductType bean) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int result = jdbcTemplate.update(getSqlInsert(), getValuesForInsert(bean), keyHolder,
				new String[] { "PRODUCT_TYPE_CD" });
		if (result != 1) {
			getLogger().error("INSERT failed - " + getTableName());
			throw new RuntimeException("Unexpected return value after INSERT : " + result + " (1 expected) ");
		} else {
			return keyHolder.getKeys().get("PRODUCT_TYPE_CD").toString();
		}
	}	
	
	@Override
	public void insert(ProductType productType) {
		super.doInsert(productType);
	}	

	@Override
	public int update(ProductType productType) {
		return super.doUpdate(productType);
	}	

	@Override
	public int delete( ProductType productType ) {
		return super.doDelete(productType);
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
	protected SqlParameterSource getValuesForInsert(ProductType productType)  {
		MapSqlParameterSource params = new MapSqlParameterSource();	
		params.addValue("productTypeCd", productType.getProductTypeCd()); // "PRODUCT_TYPE_CD" : java.lang.String
		params.addValue("name", productType.getName()); // "NAME" : java.lang.String
	
		return params;
	}
	
    //----------------------------------------------------------------------
	@Override
	protected SqlParameterSource getValuesForUpdate(ProductType productType) {
		MapSqlParameterSource params = new MapSqlParameterSource();	
		params.addValue("productTypeCd", productType.getProductTypeCd()); // "PRODUCT_TYPE_CD" : java.lang.String
		params.addValue("name", productType.getName()); // "NAME" : java.lang.String
	
		return params;
	}
	//----------------------------------------------------------------------
	@Override
	protected SqlParameterSource getValuesForPrimaryKey(ProductType productType)  {
		MapSqlParameterSource params = new MapSqlParameterSource();	
		params.addValue("productTypeCd", productType.getProductTypeCd()); // "PRODUCT_TYPE_CD" : java.lang.String
	
		return params;
	}
	//----------------------------------------------------------------------
	@Override
	protected RowMapper<ProductType> getRowMapper()  {
		//--- RowMapper to populate a new bean instance
		return new ProductTypeRowMapper() ;
	}

    //----------------------------------------------------------------------
	/**
	 * Populates the given bean with the data retrieved from the given ResultSet
	 * @param rs
	 * @param productType
	 * @throws SQLException
	 */
	private void populateBean(ResultSet rs, ProductType productType) throws SQLException {

		//--- Set data from ResultSet to Bean attributes
		productType.setProductTypeCd(rs.getString("PRODUCT_TYPE_CD")); // java.lang.String
		productType.setName(rs.getString("NAME")); // java.lang.String
	}

    //----------------------------------------------------------------------
	/**
	 * Specific inner class for 'RowMapper' implementation
	 */
	private class ProductTypeRowMapper implements RowMapper<ProductType> {

		public ProductType mapRow(ResultSet rs, int rowNum) throws SQLException {
			ProductType bean = new ProductType();
			populateBean(rs, bean);
			return bean;
		}
	}
}
