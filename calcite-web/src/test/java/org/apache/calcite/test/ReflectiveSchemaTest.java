/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.calcite.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.calcite.adapter.java.ReflectiveSchema;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.schema.SchemaPlus;

import com.airsupply.simple.hr.HrSchema;

public class ReflectiveSchemaTest {

	public void test() throws SQLException, ClassNotFoundException {
		Class.forName("org.apache.calcite.jdbc.Driver");
		Properties info = new Properties();
		info.setProperty("lex", "JAVA");
		Connection connection = DriverManager.getConnection("jdbc:calcite:",
				info);
		CalciteConnection calciteConnection = connection
				.unwrap(CalciteConnection.class);

		SchemaPlus rootSchema = calciteConnection.getRootSchema();
		rootSchema.add("hr", new ReflectiveSchema(new HrSchema()));
		Statement statement = calciteConnection.createStatement();
		ResultSet resultSet = statement
				.executeQuery("select d.deptno,min(d.name), min(e.empid)\n"
						+ "from hr.emps as e\n" + "join hr.depts as d\n"
						+ "  on e.deptno = d.deptno\n" + "group by d.deptno\n"
						+ "having count(*) > 1");
		while (resultSet.next()) {
			System.out.println();
			for (int i = 1; i < 3; i++) {
				System.out.print(resultSet.getString(i) + "\t");
			}
		}

		resultSet.close();
		statement.close();
		connection.close();

	}

	public static void main(String[] args) {
		ReflectiveSchemaTest hrQueryTest = new ReflectiveSchemaTest();

		try {
			hrQueryTest.test();
		} catch (ClassNotFoundException | SQLException e) {
		}

	}

}

// End StatementTest.java
