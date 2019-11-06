package org.bank.demo.service.commons;

import java.io.Serializable;
import org.apache.log4j.Logger;
import java.util.List;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.bank.demo.domain.repository.commons.GenericRepository;

public abstract class GenericService<T extends Serializable> {
    protected Logger logger;
	protected GenericRepository<T> repository;
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int delete(final T bean) {
		if (bean == null) {
			logger.error("Entity is null");
			throw new IllegalArgumentException("Entity is null");
		}
		return repository.delete(bean);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void insert(final T bean) {
		if (bean == null) {
			logger.error("Entity is null");
			throw new IllegalArgumentException("Entity is null");
		}
		repository.insert(bean);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<T> findAll() {
		return repository.findAll();
	}
}
