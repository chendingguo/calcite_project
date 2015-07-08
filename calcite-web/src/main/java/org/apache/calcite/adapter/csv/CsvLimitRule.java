package org.apache.calcite.adapter.csv;

import org.apache.calcite.plan.RelOptRule;
import org.apache.calcite.plan.RelOptRuleCall;
import org.apache.calcite.rel.logical.LogicalProject;


public class CsvLimitRule extends RelOptRule {
	 public static final RelOptRule INSTANCE = new CsvLimitRule();
	 private CsvLimitRule() {
			super(
					operand(LogicalProject.class,
							operand(CsvTableScan.class, none())),
					"CsvLimitRule");
		}

	@Override
	public void onMatch(RelOptRuleCall call) {
		call.getRule();
		
	}
	

}
