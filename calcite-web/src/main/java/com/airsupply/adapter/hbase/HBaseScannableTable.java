package com.airsupply.adapter.hbase;

import org.apache.calcite.DataContext;
import org.apache.calcite.linq4j.AbstractEnumerable;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Enumerator;
import org.apache.calcite.rel.type.RelProtoDataType;
import org.apache.calcite.schema.ScannableTable;

import java.io.File;

/**
 * Table based on a hbase .
 *
 * <p>
 * It implements the {@link ScannableTable} interface, so Calcite gets data by
 * calling the {@link #scan(DataContext)} method.
 */
public class HBaseScannableTable extends HBaseTable implements ScannableTable {
	/** Creates a HbaseScannableTable. */
	HBaseScannableTable(File file, RelProtoDataType protoRowType) {
		super(file, protoRowType);
	}

	public String toString() {
		return "HbaseScannableTable";
	}

	public Enumerable<Object[]> scan(DataContext root) {
		final int[] fields = HBaseEnumerator.identityList(fieldTypes.size());
		return new AbstractEnumerable<Object[]>() {
			public Enumerator<Object[]> enumerator() {
				return new HBaseEnumerator<Object[]>(
						file,
						null,
						new HBaseEnumerator.ArrayRowConverter(fieldTypes, fields));
			}
		};
	}
}
