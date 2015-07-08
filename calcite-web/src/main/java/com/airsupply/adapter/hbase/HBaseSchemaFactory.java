package com.airsupply.adapter.hbase;

import org.apache.calcite.model.ModelHandler;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaFactory;
import org.apache.calcite.schema.SchemaPlus;

import java.io.File;
import java.util.Map;

/**
 * Factory that creates a {@link HbaseSchema}.
 *
 * <p>
 * Allows a custom schema to be included in a <code><i>model</i>.json</code>
 * file.
 * </p>
 */
public class HBaseSchemaFactory implements SchemaFactory {
	// public constructor, per factory contract
	public HBaseSchemaFactory() {
	}

	public Schema create(SchemaPlus parentSchema, String name,
			Map<String, Object> operand) {
		final String directory = (String) operand.get("directory");
		final File base = (File) operand
				.get(ModelHandler.ExtraOperand.BASE_DIRECTORY.camelName);
		File directoryFile = new File(directory);
		if (base != null && !directoryFile.isAbsolute()) {
			directoryFile = new File(base, directory);
		}
		String flavorName = (String) operand.get("flavor");
		HBaseTable.Flavor flavor;
		if (flavorName == null) {
			flavor = HBaseTable.Flavor.SCANNABLE;
		} else {
			flavor = HBaseTable.Flavor.valueOf(flavorName.toUpperCase());
		}
		return new HBaseSchema(directoryFile, flavor);
	}
}
