package org.suren.util.swing;

import java.util.Arrays;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class JTableUtil
{

	public static Object getValue(JTable table, Object header)
	{
		int row = table.getSelectedRow();

		return getValue(table, header, row);
	}

	public static Object getValue(JTable table, Object header, int row)
	{
		TableColumnModel model = table.getTableHeader().getColumnModel();
		int count = model.getColumnCount();
		Object result = null;
		if (-1 == row) return result;

		for (int i = 0; i < count; i++)
		{
			if (model.getColumn(i).getHeaderValue().equals(header))
			{
				result = table.getValueAt(row, i);
			}
		}

		return result;
	}

	public static void setValue(JTable table, Object header, Object value)
	{
		int row = table.getSelectedRow();

		setValue(table, header, row, value);
	}

	public static void setValue(JTable table, Object header, int row, Object value)
	{
		TableColumnModel model = table.getTableHeader().getColumnModel();
		int count = model.getColumnCount();

		for (int i = 0; i < count; i++)
		{
			if (model.getColumn(i).getHeaderValue().equals(header))
			{
				table.setValueAt(value, row, i);
				break;
			}
		}
	}

	public static void setValue(JTable table, Object headerValue, Object headerCell, Object cell,
			Object value)
	{
		TableColumnModel model = table.getTableHeader().getColumnModel();
		int count = model.getColumnCount();
		int row = table.getRowCount();

		for (int i = 0; i < count; i++)
		{
			if (model.getColumn(i).getHeaderValue().equals(headerCell))
			{
				for (int j = 0; j < row; j++)
				{
					if (cell.equals(table.getValueAt(j, i)))
					{
						setValue(table, headerValue, j, value);
						break;
					}
				}

				break;
			}
		}
	}

	public static void columnFit(JTable table)
	{
		DefaultTableModel model = (DefaultTableModel) table.getModel();

		int rows = model.getRowCount();
		int cols = model.getColumnCount();

		for (int i = 0; i < cols; i++)
		{
			int max = 0;
			for (int j = 0; j < rows; j++)
			{
				Object obj = table.getValueAt(j, i);
				if (obj == null) continue;

				String tmp = obj.toString();
				if (tmp.length() > max) max = tmp.length();
			}

			table.getColumnModel().getColumn(i).setWidth(max + 75);
			table.getTableHeader().setResizingColumn(table.getColumnModel().getColumn(i));
		}
	}

	public static String getRowValue(JTable table, int[] rows, Object nullValue)
	{
		String content = "";
		if (table == null) return content;

		int colTotal = table.getColumnCount();

		if (rows == null) rows = table.getSelectedRows();
		Object tmp;

		for (int i : rows)
		{
			for (int j = 0; j < colTotal; j++)
			{
				tmp = table.getValueAt(i, j);

				content += (tmp == null ? nullValue : tmp + "\t");
			}

			content += "\n";
		}

		return content;
	}

	public static String getRowValue(JTable table, Object nullValue)
	{
		return getRowValue(table, null, nullValue);
	}

	public static String getRowValue(JTable table)
	{
		return getRowValue(table, "");
	}

	public static void deleteSelectRow(JTable table)
	{
		int[] rows = table.getSelectedRows();

		deleteRows(table, rows);
	}

	public static void deleteRows(JTable table, int[] rows)
	{
		if (rows == null) return;

		Arrays.sort(rows);

		DefaultTableModel model = (DefaultTableModel) table.getModel();

		for (int i = rows.length - 1; i >= 0; i--)
		{
			model.removeRow(rows[i]);
		}
	}

	public static void deleteRow(JTable table, int column, Object value)
	{
		int count = table.getRowCount();
		int[] rows = new int[count];
		int index = 0;

		for (int i = 0; i < count; i++)
		{
			if (value.equals(table.getValueAt(i, column)))
			{
				rows[index++] = i;
			}
		}

		deleteRows(table, Arrays.copyOf(rows, index));
	}

	public static void deleteRow(JTable table, Object header, Object value)
	{
		table.setAutoCreateRowSorter(false);

		int cols = table.getColumnCount();
		TableColumnModel model = table.getColumnModel();

		for (int i = 0; i < cols; i++)
		{
			if (header.equals(model.getColumn(i).getHeaderValue()))
			{
				deleteRow(table, i, value);
				break;
			}
		}

		table.setAutoCreateRowSorter(true);
	}
}
