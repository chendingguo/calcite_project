package com.airsupply.simple.hr;
public  class Employee {
		public final int empid;
		public final int deptno;
		public final String name;
		public final float salary;
		public final Integer commission;

		public Employee(int empid, int deptno, String name, float salary,
				Integer commission) {
			this.empid = empid;
			this.deptno = deptno;
			this.name = name;
			this.salary = salary;
			this.commission = commission;
		}

		public String toString() {
			return "Employee [empid: " + empid + ", deptno: " + deptno
					+ ", name: " + name + "]";
		}
	}