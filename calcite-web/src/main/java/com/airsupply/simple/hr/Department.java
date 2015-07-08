package com.airsupply.simple.hr;

import java.util.List;

public class Department {
		public final int deptno;
		public final String name;
		public final List<Employee> employees;

		public Department(int deptno, String name, List<Employee> employees) {
			this.deptno = deptno;
			this.name = name;
			this.employees = employees;
		}

		public String toString() {
			return "Department [deptno: " + deptno + ", name: " + name
					+ ", employees: " + employees + "]";
		}
	}