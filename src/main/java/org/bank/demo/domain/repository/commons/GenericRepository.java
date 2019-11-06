package org.bank.demo.domain.repository.commons;

import java.util.List;
import java.io.Serializable;

public interface GenericRepository<T extends Serializable> {

	// ----------------------------------------------------------------------
	/**
	 * Find all
	 * 
	 * @return a list of bean found or null if not found
	 */
	public List<T> findAll();

	// ----------------------------------------------------------------------
	/**
	 * Inserts the given bean in the database
	 * 
	 * @param bean
	 */
	public void insert(T bean);

	// ----------------------------------------------------------------------
	/**
	 * Deletes the given bean in the database
	 * 
	 * @param bean
	 * @return
	 */
	public int delete(T bean);

	// ----------------------------------------------------------------------
	/**
	 * Counts all the records present in the database table
	 * 
	 * @return
	 */
	public long count();
}
