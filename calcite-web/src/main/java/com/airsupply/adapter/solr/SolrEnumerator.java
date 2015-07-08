package com.airsupply.adapter.solr;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.linq4j.Enumerator;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.util.Pair;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import com.airsupply.adapter.solr.metadata.MetaDataManager;
import com.airsupply.adapter.solr.metadata.vo.ColumnVO;
import com.airsupply.adapter.solr.metadata.vo.TableDesc;

/**
 * Enumerator that read the solr data.
 * 
 * @author arisupply
 *
 * @param <E>
 *            rowtype
 */
class SolrEnumerator<E> implements Enumerator<E> {
	SolrDocumentList solrDocumentList;
	private int index = 0;

	private final RowConverter<E> rowConverter;
	private E current;

	private static final FastDateFormat TIME_FORMAT_DATE;
	private static final FastDateFormat TIME_FORMAT_TIME;
	private static final FastDateFormat TIME_FORMAT_TIMESTAMP;
	private static String tableName;

	static {
		TimeZone gmt = TimeZone.getTimeZone("GMT");
		TIME_FORMAT_DATE = FastDateFormat.getInstance("yyyy-MM-dd", gmt);
		TIME_FORMAT_TIME = FastDateFormat.getInstance("hh:mm:ss", gmt);
		TIME_FORMAT_TIMESTAMP = FastDateFormat.getInstance(
				"yyyy-MM-dd hh:mm:ss", gmt);
	}

	public SolrEnumerator(File file, List<SolrFieldType> fieldTypes) {
		this(file, fieldTypes, identityList(fieldTypes.size()));
	}

	@SuppressWarnings("unchecked")
	public SolrEnumerator(File file, List<SolrFieldType> fieldTypes,
			int[] fields) {
		this(file, null, (RowConverter<E>) converter(fieldTypes, fields));
	}

	public SolrEnumerator(File file, String[] filterValues,
			RowConverter<E> rowConverter) {
		this.rowConverter = rowConverter;
		tableName = file.getName().split("[.]")[0];
		TableDesc tableDesc = MetaDataManager.getTableDesc(tableName);
		Map<String, Object> connInfoMap = tableDesc.getConnInfo();
		Map<String,String> queryFilter=tableDesc.getQueryFilter();
		int limit=Integer.parseInt(queryFilter.get("limit"));
		String connUrl = SolrAdapterUtil.getConnectUrl(connInfoMap);
		SolrClient solrClient = SolrClientFactory.getSolrClient(connUrl);
		QueryResponse resp = null;
		try {
			SolrQuery solrQuery=new SolrQuery("*:*");
			solrQuery.setStart(0);
			solrQuery.setRows(limit);
			resp = solrClient.query(solrQuery);
			this.solrDocumentList = resp.getResults();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static RowConverter<?> converter(List<SolrFieldType> fieldTypes,
			int[] fields) {
		if (fields.length == 1) {
			final int field = fields[0];
			return new SingleColumnRowConverter(fieldTypes.get(field), field);
		} else {
			return new ArrayRowConverter(fieldTypes, fields);
		}
	}

	/**
	 * TODO: get the date type of table
	 * 
	 * @param typeFactory
	 * @param file
	 * @param fieldTypes
	 * @return
	 */
	static RelDataType deduceRowType(JavaTypeFactory typeFactory, File file,
			List<SolrFieldType> fieldTypes) {
		final List<RelDataType> types = new ArrayList<RelDataType>();
		final List<String> names = new ArrayList<String>();

		String tableName = file.getName().split("[.]")[0];
		TableDesc tableDesc = MetaDataManager.getTableDesc(tableName);
		List<ColumnVO> columns = tableDesc.getTableColumns();
		for (ColumnVO column : columns) {
			final RelDataType relDataType;
			//must change the name to upper case
			final String name = column.getName().toUpperCase();
			String type = column.getType();
			final SolrFieldType fieldType;

			fieldType = SolrFieldType.of(type);
			if (fieldType == null) {
				System.out.println("WARNING: Found unknown type: " + type
						+ " in file: " + file.getAbsolutePath()
						+ " for column: " + name
						+ ". Will assume the type of column is string");
			}
			if (fieldType == null) {
				relDataType = typeFactory.createJavaType(String.class);
			} else {
				relDataType = fieldType.toType((JavaTypeFactory) typeFactory);
			}
			names.add(name);
			types.add(relDataType);
			if (fieldTypes != null) {
				fieldTypes.add(fieldType);
			}

		}

		if (names.isEmpty()) {
			names.add("line");
			types.add(typeFactory.createJavaType(String.class));
		}
		return typeFactory.createStructType(Pair.zip(names, types));
	}

	@Override
	public E current() {
		return current;
	}

	@Override
	public boolean moveNext() {
		if (index >= solrDocumentList.size()) {
			return false;
		}
		SolrDocument solrDocument = solrDocumentList.get(index);
		
		//readjust the field value array to match the meta data column order
		TableDesc tableDesc=MetaDataManager.getTableDesc(tableName);
		List<ColumnVO> columns=tableDesc.getTableColumns();
		String[] strings=new String[columns.size()];
		int i=0;
		for(ColumnVO column:columns){
			String value=String.valueOf(solrDocument.getFieldValue(column.getName().toLowerCase()));
			strings[i]=value;
			i++;
		}
		current = rowConverter.convertRow(strings);
		// move to next index of the solrDocumentList
		index++;
		return true;

	}

	public List<String> mapTransitionList(Map<String, Object> map) {
		List<String> list = new ArrayList<String>();
		Set<String> set = map.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String key = it.next();
			list.add(String.valueOf(map.get(key)));
		}
		return list;
	}

	@Override
	public void reset() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void close() {

	}

	/** Returns an array of integers {0, ..., n - 1}. */
	static int[] identityList(int n) {
		int[] integers = new int[n];
		for (int i = 0; i < n; i++) {
			integers[i] = i;
		}
		return integers;
	}

	/** Row converter. */
	abstract static class RowConverter<E> {
		abstract E convertRow(String[] rows);

		protected Object convert(SolrFieldType fieldType, String string) {
			if (fieldType == null) {
				return string;
			}
			switch (fieldType) {
			case BOOLEAN:
				if (string.length() == 0) {
					return null;
				}
				return Boolean.parseBoolean(string);
			case BYTE:
				if (string.length() == 0) {
					return null;
				}
				return Byte.parseByte(string);
			case SHORT:
				if (string.length() == 0) {
					return null;
				}
				return Short.parseShort(string);
			case INT:
				if (string.length() == 0) {
					return null;
				}
				return Integer.parseInt(string);
			case LONG:
				if (string.length() == 0) {
					return null;
				}
				return Long.parseLong(string);
			case FLOAT:
				if (string.length() == 0) {
					return null;
				}
				return Float.parseFloat(string);
			case DOUBLE:
				if (string.length() == 0) {
					return null;
				}
				return Double.parseDouble(string);
			case DATE:
				if (string.length() == 0) {
					return null;
				}
				try {
					Date date = TIME_FORMAT_DATE.parse(string);
					return new java.sql.Date(date.getTime());
				} catch (ParseException e) {
					return null;
				}
			case TIME:
				if (string.length() == 0) {
					return null;
				}
				try {
					Date date = TIME_FORMAT_TIME.parse(string);
					return new java.sql.Time(date.getTime());
				} catch (ParseException e) {
					return null;
				}
			case TIMESTAMP:
				if (string.length() == 0) {
					return null;
				}
				try {
					Date date = TIME_FORMAT_TIMESTAMP.parse(string);
					return new java.sql.Timestamp(date.getTime());
				} catch (ParseException e) {
					return null;
				}
			case STRING:
			default:
				return string;
			}
		}
	}

	/** Array row converter. */
	static class ArrayRowConverter extends RowConverter<Object[]> {

		private final SolrFieldType[] fieldTypes;
		private final int[] fields;

		ArrayRowConverter(List<SolrFieldType> fieldTypes, int[] fields) {
            
			this.fieldTypes = fieldTypes.toArray(new SolrFieldType[fieldTypes
					.size()]);
			this.fields = fields;
		}

		public Object[] convertRow(String[] strings) {
			final Object[] objects = new Object[fields.length];
			for (int i = 0; i < fields.length; i++) {
				int field = fields[i];
				objects[i] = convert(fieldTypes[field], strings[field]);
			}
			return objects;
		}
	}

	/** Single column row converter. */
	private static class SingleColumnRowConverter extends RowConverter<Object> {
		private final SolrFieldType fieldType;
		private final int fieldIndex;

		private SingleColumnRowConverter(SolrFieldType fieldType, int fieldIndex) {
			this.fieldType = fieldType;
			this.fieldIndex = fieldIndex;
		}

		public Object convertRow(String[] strings) {
			return convert(fieldType, strings[fieldIndex]);
		}
	}
}
