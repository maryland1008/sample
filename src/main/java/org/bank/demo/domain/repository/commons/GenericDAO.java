package org.bank.demo.domain.repository.commons;

import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class GenericDAO<T> {

	@Autowired
	protected NamedParameterJdbcTemplate jdbcTemplate;
	
	/**
	 * Constructor for a standard table (without auto-incremented column)
	 */
	protected GenericDAO() {
		super();
	}

	/**
	 * Returns the logger
	 * 
	 * @return
	 */
	protected abstract Logger getLogger();

	/**
	 * Returns the tablename
	 * 
	 * @return
	 */
	protected abstract String getTableName();

	/**
	 * Returns the SQL SELECT REQUEST to be used to retrieve the bean data from
	 * the database
	 * 
	 * @return
	 */
	protected abstract String getSqlSelect();

	/**
	 * Returns the SQL INSERT REQUEST to be used to insert the bean in the
	 * database
	 * 
	 * @return
	 */
	protected abstract String getSqlInsert();

	/**
	 * Returns the SQL UPDATE REQUEST to be used to update the bean in the
	 * database
	 * 
	 * @return
	 */
	protected abstract String getSqlUpdate();

	/**
	 * Returns the SQL DELETE REQUEST to be used to delete the bean from the
	 * database
	 * 
	 * @return
	 */
	protected abstract String getSqlDelete();

	/**
	 * Returns the SQL COUNT REQUEST to be used to check if the bean exists in
	 * the database
	 * 
	 * @return
	 */
	protected abstract String getSqlCount();

	/**
	 * Returns the SQL COUNT REQUEST to be used to count all the beans present
	 * in the database
	 * 
	 * @return
	 */
	protected abstract String getSqlCountAll();

	/**
	 * Returns the values to be used in the SQL INSERT PreparedStatement
	 * 
	 * @param bean
	 * @return
	 */
	protected abstract SqlParameterSource getValuesForInsert(T bean);

	/**
	 * Returns the values to be used in the SQL UPDATE PreparedStatement
	 * 
	 * @param bean
	 * @return
	 */
	protected abstract SqlParameterSource getValuesForUpdate(T bean);

	/**
	 * Returns the values to be used as Primary Key in a SQL WHERE clause in a
	 * PreparedStatement
	 * 
	 * @param bean
	 * @return
	 */
	protected abstract SqlParameterSource getValuesForPrimaryKey(T bean);

	/**
	 * Returns a RowMapper for a new bean instance
	 * 
	 * @return
	 */
	protected abstract RowMapper<T> getRowMapper();

	// -----------------------------------------------------------------------------------------
	/**
	 * Loads a bean from the database using the given primary key (SQL
	 * SELECT)<br>
	 * 
	 * @param primaryKey
	 * @return the bean found or null if not found
	 */
	protected T doSelect(SqlParameterSource primaryKey) {
		// getLogger().info(("Select by Primary Key : " + toString(primaryKey)
		// );
		RowMapper<T> rowMapper = getRowMapper();
		try {
			return jdbcTemplate.queryForObject(getSqlSelect(), primaryKey, rowMapper);
		} catch (EmptyResultDataAccessException e) {
			getLogger().info("Record not found - " + getTableName());
			return null;
		}
	}

	// -----------------------------------------------------------------------------------------
	/**
	 * Inserts the given bean in the database (SQL INSERT)
	 * 
	 * @param bean
	 */
	protected void doInsert(T bean) {
		int result = jdbcTemplate.update(getSqlInsert(), getValuesForInsert(bean));
		if (result != 1) {
			getLogger().error("INSERT failed - " + getTableName());
			throw new RuntimeException("Unexpected return value after INSERT : " + result + " (1 expected) ");
		}
	}

	// -----------------------------------------------------------------------------------------
	/**
	 * Updates the given bean in the database (SQL UPDATE)
	 * 
	 * @param bean
	 *            the bean to be updated
	 * @return the JDBC return code (i.e. the row count affected by the UPDATE
	 *         operation : 0 or 1 )
	 */
	protected int doUpdate(T bean) {
		// --- Execute UPDATE
		int result = jdbcTemplate.update(getSqlUpdate(), getValuesForUpdate(bean));
		if (result != 0 && result != 1) {
			getLogger().error("Unexpected return value after UPDATE - " + getTableName());
			throw new RuntimeException("Unexpected return value after UPDATE : " + result + " (0 or 1 expected) ");
		}
		return result;
	}

	// -----------------------------------------------------------------------------------------
	/**
	 * Deletes the given bean in the database (SQL DELETE)
	 * 
	 * @param bean
	 *            the bean to be deleted (containing the Primary Key)
	 * @return the JDBC return code (i.e. the row count affected by the DELETE
	 *         operation : 0 or 1 )
	 */
	protected int doDelete(T bean) {
		return doDelete(getValuesForPrimaryKey(bean));
	}

	// -----------------------------------------------------------------------------------------
	/**
	 * Deletes the record having with the given Primary Key
	 * 
	 * @param primaryKey
	 *            the Primary Key values
	 * @return the JDBC return code (i.e. the row count affected by the DELETE
	 *         operation : 0 or 1 )
	 */
	protected int doDelete(SqlParameterSource primaryKey) {
		// --- Execute DELETE
		int result = jdbcTemplate.update(getSqlDelete(), primaryKey);
		if (result != 0 && result != 1) {
			getLogger().error("Unexpected return value after DELETE - " + getTableName());
			throw new RuntimeException("Unexpected return value after DELETE : " + result + " (0 or 1 expected) ");
		}
		return result;
	}

	// -----------------------------------------------------------------------------------------
	/**
	 * Checks if the given bean exists in the database (SQL SELECT COUNT(*)
	 * WHERE PRIMARY_KEY = ... )
	 * 
	 * @param bean
	 *            the bean containing the Primary Key
	 * @return
	 */
	protected boolean doExists(T bean) {
		return doExists(getValuesForPrimaryKey(bean));
	}

	// -----------------------------------------------------------------------------------------
	/**
	 * Checks if the given bean exists in the database (SQL SELECT COUNT(*)
	 * WHERE PRIMARY_KEY = ... )
	 * 
	 * @param primaryKey
	 *            the Primary Key values
	 * @return
	 */
	protected boolean doExists(SqlParameterSource primaryKey) {
		long count = jdbcTemplate.queryForObject(getSqlCount(), primaryKey, Long.class);
		return count > 0;
	}

	// -----------------------------------------------------------------------------------------
	/**
	 * Counts all the occurrences in the table ( SQL SELECT COUNT(*) )
	 * 
	 * @return
	 */
	protected long doCountAll() {
		return jdbcTemplate.queryForObject(getSqlCountAll(), new EmptySqlParameterSource(), Long.class);
	}

	// -----------------------------------------------------------------------------------------
	/**
	 * Formats an array of objects in a String ready for printing
	 * 
	 * @param objects
	 * @return
	 */
	public String toString(Object[] objects) {
		if (objects != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			int i = 0;
			for (Object o : objects) {
				if (i > 0) {
					sb.append("|");
				}
				sb.append(o.toString());
				i++;
			}
			sb.append("]");
			return sb.toString();
		} else {
			return "null";
		}
	}
}
