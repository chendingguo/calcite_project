package com.airsupply.simple.hr;

import java.util.Arrays;
import java.util.Collections;

public class HrSchema {

	@Override
	public String toString() {
		return "HrSchema";
	}

	public final Employee[] emps = {
			new Employee(100, 10, "Bill", 10000, 1000),
			new Employee(200, 20, "Eric", 8000, 500),
			new Employee(150, 10, "Sebastian", 7000, null),
			new Employee(110, 10, "Theodore", 11500, 250), };

	public final Department[] depts = {
			new Department(10, "Sales", Arrays.asList(emps[0], emps[2])),
			new Department(30, "Marketing", Collections.<Employee> emptyList()),
			new Department(40, "HR", Collections.singletonList(emps[1])), };

}