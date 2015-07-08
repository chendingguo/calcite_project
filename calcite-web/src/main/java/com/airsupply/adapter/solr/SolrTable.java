package com.airsupply.adapter.solr;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rel.type.RelProtoDataType;
import org.apache.calcite.schema.impl.AbstractTable;

/**
 * Base class for table
 */
public abstract class SolrTable extends AbstractTable {
	protected final File file;
	private final RelProtoDataType protoRowType;
	protected List<SolrFieldType> fieldTypes;

	SolrTable(File file, RelProtoDataType protoRowType) {
		this.file = file;
		this.protoRowType = protoRowType;
	}

	public RelDataType getRowType(RelDataTypeFactory typeFactory) {
		if (protoRowType != null) {
			return protoRowType.apply(typeFactory);
		}
		if (fieldTypes == null) {
			fieldTypes = new ArrayList<SolrFieldType>();
			return SolrEnumerator.deduceRowType((JavaTypeFactory) typeFactory,
					file, fieldTypes);
		} else {
			return SolrEnumerator.deduceRowType((JavaTypeFactory) typeFactory,
					file, null);
		}

	}

	/** Various degrees of table "intelligence". */
	public enum Flavor {
		SCANNABLE, FILTERABLE, TRANSLATABLE
	}
}
