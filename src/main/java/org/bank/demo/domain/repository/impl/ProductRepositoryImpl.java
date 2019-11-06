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

import org.bank.demo.domain.Product;
import org.bank.demo.domain.repository.commons.GenericDAO;
import org.bank.demo.domain.repository.ProductRepository;


@Repository
public class ProductRepositoryImpl extends GenericDAO<Product> implements ProductRepository<Product> {
	
    private static final Logger logger = Logger.getLogger(ProductRepositoryImpl.class);

	private final static String TABLE_NAME = "PRODUCT";	

	private final static String SQL_SELECT =  "select PRODUCT_CD, DATE_OFFERED, DATE_RETIRED, NAME, PRODUCT_TYPE_CD  from PRODUCT where PRODUCT_CD = :productCd";
	
	private final static String SQL_INSERT = "insert into PRODUCT (PRODUCT_CD, DATE_OFFERED, DATE_RETIRED, NAME, PRODUCT_TYPE_CD) values (:productCd, :dateOffered, :dateRetired, :name, :productTypeCd)";
	
	private final static String SQL_UPDATE = "update PRODUCT set DATE_OFFERED = :dateOffered, DATE_RETIRED = :dateRetired, NAME = :name, PRODUCT_TYPE_CD = :productTypeCd where PRODUCT_CD = :productCd";

	private final static String SQL_DELETE = "delete from PRODUCT where PRODUCT_CD = :productCd";

	private final static String SQL_COUNT_ALL =  "select count(*) from PRODUCT";

	private final static String SQL_COUNT = "select count(*) from PRODUCT where PRODUCT_CD = :productCd";

	@SuppressWarnings("unchecked")
	protected final Map<String, String> fieldMap = MapUtils.putAll(new HashMap<String, String>(), new String[][] {
		{ "productCd", "PRODUCT_CD" }, 
		{ "dateOffered", "DATE_OFFERED" }, 
		{ "dateRetired", "DATE_RETIRED" }, 
		{ "name", "NAME" }, 
		{ "productTypeCd", "PRODUCT_TYPE_CD" }	});

	public ProductRepositoryImpl() {
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
	public Product findByPrimaryKey( String productCd ) {
		MapSqlParameterSource primaryKey = new MapSqlParameterSource();	
		primaryKey.addValue("productCd", productCd);
		return super.doSelect(primaryKey);		
	}

	@Override
	public Product findByQuery(String sql, Map<String,List<?>> params) {
		MapSqlParameterSource sqlParams = new MapSqlParameterSource();	
		for(String field : fieldMap.keySet()) {
			if(params.containsKey(field)) {
				sqlParams.addValue(field, params.get(field));
			}
		}
		
		RowMapper<Product> rowMapper = getRowMapper();
		try {
			return jdbcTemplate.queryForObject(sql, sqlParams, rowMapper);
		} catch (EmptyResultDataAccessException e) {
			getLogger().info("Record not found - " + getTableName());
			return null;
		}
	}	

	@Override
	public List<Product> findBySqlParameterSource(Map<String,List<?>> params) {
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
		
		String sql = "select * from PRODUCT";
		
		if(sb.length()>0) {
			sql += sb.toString();
		} 
		
		try {
			List<Product> list = jdbcTemplate.query(sql, sqlParams, new ProductRowMapper());
			return list;
		} catch (EmptyResultDataAccessException e) {
			logger.info("No data found in PRODUCT");
			return null;
		}	
	}

	@Override
	public int deleteByPrimaryKey( String productCd ) {
		MapSqlParameterSource primaryKey = new MapSqlParameterSource();	
		primaryKey.addValue("productCd", productCd);
		return super.doDelete(primaryKey);		
	}

	@Override
	public boolean exists( String productCd ) {
		MapSqlParameterSource primaryKey = new MapSqlParameterSource();	
		primaryKey.addValue("productCd", productCd);
		return super.doExists(primaryKey);
	}

	@Override
	public List<Product> findAll() {
		String sql = "select * from PRODUCT";
		try {
			List<Product> list = jdbcTemplate.query(sql, new ProductRowMapper());
			return list;
		} catch (EmptyResultDataAccessException e) {
			logger.info("No data found in PRODUCT");
			return null;
		}	
	}
	
	@Override
	public String insertProduct(Product bean) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int result = jdbcTemplate.update(getSqlInsert(), getValuesForInsert(bean), keyHolder,
				new String[] { "PRODUCT_CD" });
		if (result != 1) {
			getLogger().error("INSERT failed - " + getTableName());
			throw new RuntimeException("Unexpected return value after INSERT : " + result + " (1 expected) ");
		} else {
			return keyHolder.getKeys().get("PRODUCT_CD").toString();
		}
	}	
	
	@Override
	public void insert(Product product) {
		super.doInsert(product);
	}	

	@Override
	public int update(Product product) {
		return super.doUpdate(product);
	}	

	@Override
	public int delete( Product product ) {
		return super.doDelete(product);
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
	protected SqlParameterSource getValuesForInsert(Product product)  {
		MapSqlParameterSource params = new MapSqlParameterSource();	
		params.addValue("productCd", product.getProductCd()); // "PRODUCT_CD" : java.lang.String
		params.addValue("dateOffered", product.getDateOffered()); // "DATE_OFFERED" : java.util.Date
		params.addValue("dateRetired", product.getDateRetired()); // "DATE_RETIRED" : java.util.Date
		params.addValue("name", product.getName()); // "NAME" : java.lang.String
		params.addValue("productTypeCd", product.getProductTypeCd()); // "PRODUCT_TYPE_CD" : java.lang.String
	
		return params;
	}
	
    //----------------------------------------------------------------------
	@Override
	protected SqlParameterSource getValuesForUpdate(Product product) {
		MapSqlParameterSource params = new MapSqlParameterSource();	
		params.addValue("productCd", product.getProductCd()); // "PRODUCT_CD" : java.lang.String
		params.addValue("dateOffered", product.getDateOffered()); // "DATE_OFFERED" : java.util.Date
		params.addValue("dateRetired", product.getDateRetired()); // "DATE_RETIRED" : java.util.Date
		params.addValue("name", product.getName()); // "NAME" : java.lang.String
		params.addValue("productTypeCd", product.getProductTypeCd()); // "PRODUCT_TYPE_CD" : java.lang.String
	
		return params;
	}
	//----------------------------------------------------------------------
	@Override
	protected SqlParameterSource getValuesForPrimaryKey(Product product)  {
		MapSqlParameterSource params = new MapSqlParameterSource();	
		params.addValue("productCd", product.getProductCd()); // "PRODUCT_CD" : java.lang.String
	
		return params;
	}
	//----------------------------------------------------------------------
	@Override
	protected RowMapper<Product> getRowMapper()  {
		//--- RowMapper to populate a new bean instance
		return new ProductRowMapper() ;
	}

    //----------------------------------------------------------------------
	/**
	 * Populates the given bean with the data retrieved from the given ResultSet
	 * @param rs
	 * @param product
	 * @throws SQLException
	 */
	private void populateBean(ResultSet rs, Product product) throws SQLException {

		//--- Set data from ResultSet to Bean attributes
		product.setProductCd(rs.getString("PRODUCT_CD")); // java.lang.String
		product.setDateOffered(rs.getDate("DATE_OFFERED")); // java.util.Date
		product.setDateRetired(rs.getDate("DATE_RETIRED")); // java.util.Date
		product.setName(rs.getString("NAME")); // java.lang.String
		product.setProductTypeCd(rs.getString("PRODUCT_TYPE_CD")); // java.lang.String
	}

    //----------------------------------------------------------------------
	/**
	 * Specific inner class for 'RowMapper' implementation
	 */
	private class ProductRowMapper implements RowMapper<Product> {

		public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
			Product bean = new Product();
			populateBean(rs, bean);
			return bean;
		}
	}
}
