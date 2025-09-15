package cl.aburgosc.sistemainventariojoyeria.ui.tablecellrenderer;

import java.awt.Component;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import cl.aburgosc.sistemainventariojoyeria.ui.tablemodel.DynamicTableModel;
import cl.aburgosc.sistemainventariojoyeria.ui.util.UIColumn;

public class DynamicRenderer extends DefaultTableCellRenderer {

	private final DynamicTableModel<?> model;

	public DynamicRenderer(DynamicTableModel<?> model) {
		this.model = model;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		try {
			Field field = model.getFieldByColumn(column);

			if (field != null && field.isAnnotationPresent(UIColumn.class)) {
				UIColumn annotation = field.getAnnotation(UIColumn.class);
				if (annotation.isMoney() && value instanceof BigDecimal) {
					NumberFormat moneyFormat = NumberFormat.getCurrencyInstance(new Locale("es", "CL"));
					moneyFormat.setMaximumFractionDigits(0); // sin decimales
					setText(moneyFormat.format(value));
				}

			}
		} catch (Exception ignored) {
		}

		return this;
	}

}
