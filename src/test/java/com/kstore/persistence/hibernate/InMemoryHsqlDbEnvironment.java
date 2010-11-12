/**
 * Object Search Framework
 *
 * Copyright (C) 2010 Julian Klas
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.kstore.persistence.hibernate;

import org.hibernate.cfg.Configuration;

public class InMemoryHsqlDbEnvironment {

	static{
		new Configuration().
		setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect").
		setProperty("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver").
		setProperty("hibernate.connection.url", "jdbc:hsqldb:mem:kstore").
		setProperty("hibernate.connection.username", "sa").
		setProperty("hibernate.connection.password", "").
		setProperty("hibernate.connection.pool_size", "1").
		setProperty("hibernate.connection.autocommit", "true").
		setProperty("hibernate.cache.provider_class", "org.hibernate.cache.HashtableCacheProvider").
		setProperty("hibernate.hbm2ddl.auto", "create-drop").
		setProperty("hibernate.show_sql", "false");
	}

}
