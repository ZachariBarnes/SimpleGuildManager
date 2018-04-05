package main.utils;

import java.io.File;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;

import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class XLS2JTable {
	private static boolean success = true;

	public static void toJtable(JTable table, File file) {
		File xlsFile = file;
		//WritableWorkbook writableWorkbook;
		Workbook readableWorkbook;
		String[] CellColumns = {"A","B","C","D","E","F","G","H","I","J"};
		try {
			readableWorkbook = Workbook.getWorkbook(xlsFile);
			//writableWorkbook = Workbook.createWorkbook(xlsFile);
			//WritableSheet writableSheet = writableWorkbook.createSheet("Guild Members Data", 0);

			for (int i = 0; i < readableWorkbook.getSheet(0).getColumns(); i++) {
			//for (int i = 0; i < table.getColumnCount(); i++) {
				for (int j = 0; j < readableWorkbook.getSheet(0).getRows(); j++) {
				//for (int j = 0; j < table.getRowCount(); j++) {
					Object object = readableWorkbook.getCell("Sheet1!"+CellColumns[i]+j).getContents(); // Should fetch cell 'Sheet1!A1'
					//Object object = table.getValueAt(j, i);
					JLabel value = new JLabel(String.valueOf(object));
					table.add(value);
					//table.setValueAt(object.toString(), j, i);
					//writableSheet.addCell(new Label(i, j, String.valueOf(object)));
				}
			}
			table.revalidate();
			//writableWorkbook.write();
			readableWorkbook.close();
			//writableWorkbook.close();
			success = true;
		} catch (IOException | BiffException e) {
			success = false;
			e.printStackTrace();
		}
	}

	public static void loadXLS(JTable table, File file) {
		SwingWorker<Void, Void> saveWorker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				toJtable(table, file);
				return null;
			}

			@Override
			public void done() {
				if (success == true)
					JOptionPane.showMessageDialog(null, "Load completed successfully.", "Messages", 0);
				else
					JOptionPane.showMessageDialog(null,
							"Something is wrong with the loading process.",
							"Error", 0);
			}
		};
		saveWorker.execute();
	}
}
