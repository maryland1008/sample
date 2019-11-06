package org.bank.demo.domain.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.bank.demo.domain.ProductType;
import org.bank.demo.domain.repository.commons.GenericRepository;


public interface ProductTypeRepository<T extends Serializable> extends GenericRepository <T>{
	public boolean hasField(String fieldName);
	public ProductType findByPrimaryKey( String productTypeCd );
	public ProductType findByQuery(String sql, Map<String,List<?>> params);
	public String insertProductType(ProductType bean);
	public int update(ProductType bean);
	public List<ProductType> findBySqlParameterSource(Map<String,List<?>> params);
	public int deleteByPrimaryKey( String productTypeCd );
	public boolean exists( String productTypeCd );	
}
