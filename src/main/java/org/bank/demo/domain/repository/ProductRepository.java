package org.bank.demo.domain.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.bank.demo.domain.Product;
import org.bank.demo.domain.repository.commons.GenericRepository;


public interface ProductRepository<T extends Serializable> extends GenericRepository <T>{
	public boolean hasField(String fieldName);
	public Product findByPrimaryKey( String productCd );
	public Product findByQuery(String sql, Map<String,List<?>> params);
	public String insertProduct(Product bean);
	public int update(Product bean);
	public List<Product> findBySqlParameterSource(Map<String,List<?>> params);
	public int deleteByPrimaryKey( String productCd );
	public boolean exists( String productCd );	
}
