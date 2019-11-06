package org.bank.demo.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

@Configuration
@ComponentScan("org.bank.demo")
public class RootApplicationContextConfig {

	@Bean
	public DataSource dataSource() {
		JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
		return dataSourceLookup.getDataSource("jdbc/APP-DS");
	}

	@Bean
	public NamedParameterJdbcTemplate getJdbcTemplate() {
		return new NamedParameterJdbcTemplate(dataSource());
	}
}
