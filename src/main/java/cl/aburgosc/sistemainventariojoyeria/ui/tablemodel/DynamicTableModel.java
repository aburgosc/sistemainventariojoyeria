package cl.aburgosc.sistemainventariojoyeria.ui.tablemodel;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.table.DefaultTableModel;

import cl.aburgosc.sistemainventariojoyeria.ui.tablecellrenderer.DynamicRenderer;
import cl.aburgosc.sistemainventariojoyeria.ui.util.UIColumn;

public class DynamicTableModel<T> extends DefaultTableModel {

	private final List<Field> fields;
	private final List<T> data;

	public DynamicTableModel(Class<T> clazz, List<T> data) {
		this.fields = new ArrayList<>();
		this.data = new ArrayList<>();
		if (data != null)
			this.data.addAll(data);

		for (Field f : clazz.getDeclaredFields()) {
			if (f.isAnnotationPresent(UIColumn.class)) {
				f.setAccessible(true);
				fields.add(f);
			}
		}
	}

	@Override
	public int getRowCount() {
		return data != null ? data.size() : 0;
	}

	@Override
	public int getColumnCount() {
		return fields.size();
	}

	@Override
	public String getColumnName(int column) {
		return fields.get(column).getAnnotation(UIColumn.class).header();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return fields.get(columnIndex).getType();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Field field = fields.get(columnIndex);
		field.setAccessible(true);
		try {
			Object value = field.get(data.get(rowIndex));

			UIColumn col = field.getAnnotation(UIColumn.class);
			if (col != null && col.isMoney() && value instanceof BigDecimal) {
				Locale cl = new Locale.Builder().setLanguage("es").setRegion("CL").build();
				NumberFormat moneyFormat = NumberFormat.getCurrencyInstance(cl);
				moneyFormat.setMaximumFractionDigits(0);
				return moneyFormat.format(((BigDecimal) value).setScale(2, RoundingMode.HALF_UP));
			}

			return value;
		} catch (IllegalAccessException e) {
			return null;
		}
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (rowIndex < 0 || rowIndex >= data.size() || columnIndex < 0 || columnIndex >= fields.size())
			return;

		Field field = fields.get(columnIndex);
		field.setAccessible(true);
		try {
			if (field.getType() == BigDecimal.class) {
				if (aValue instanceof Number) {
					field.set(data.get(rowIndex), BigDecimal.valueOf(((Number) aValue).doubleValue()));
				} else if (aValue instanceof String) {
					field.set(data.get(rowIndex), new BigDecimal((String) aValue));
				}
			} else if (field.getType() == int.class || field.getType() == Integer.class) {
				if (aValue instanceof Number) {
					field.set(data.get(rowIndex), ((Number) aValue).intValue());
				} else if (aValue instanceof String) {
					field.set(data.get(rowIndex), Integer.parseInt((String) aValue));
				}
			} else {
				field.set(data.get(rowIndex), aValue);
			}

			fireTableCellUpdated(rowIndex, columnIndex);
		} catch (IllegalAccessException | NumberFormatException e) {
			e.printStackTrace();
		}
	}

	public void setData(List<T> newData) {
		this.data.clear();
		if (newData != null)
			this.data.addAll(newData);
		fireTableDataChanged();
	}

	public Field getFieldByColumn(int columnIndex) {
		return fields.get(columnIndex);
	}

	public DynamicRenderer getRenderer() {
		return new DynamicRenderer(this);
	}
}
