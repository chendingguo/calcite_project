package com.airsupply.adapter.solr;

import org.apache.calcite.DataContext;
import org.apache.calcite.linq4j.AbstractEnumerable;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Enumerator;
import org.apache.calcite.rel.type.RelProtoDataType;
import org.apache.calcite.schema.ScannableTable;

import java.io.File;

/**
 * Table based on a Solr .
 *
 * <p>
 * It implements the {@link ScannableTable} interface, so Calcite gets data by
 * calling the {@link #scan(DataContext)} method.
 */
public class SolrScannableTable extends SolrTable implements ScannableTable {
	/** Creates a SolrScannableTable. */
	SolrScannableTable(File file, RelProtoDataType protoRowType) {
		super(file, protoRowType);
	}

	public String toString() {
		return "SolrScannableTable";
	}

	public Enumerable<Object[]> scan(DataContext root) {
		final int[] fields = SolrEnumerator.identityList(fieldTypes.size());
		return new AbstractEnumerable<Object[]>() {
			public Enumerator<Object[]> enumerator() {
				return new SolrEnumerator<Object[]>(
						file,
						null,
						new SolrEnumerator.ArrayRowConverter(fieldTypes, fields));
			}
		};
	}
}
