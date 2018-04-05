package main.java.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.DefaultCaret;
import javax.swing.text.TableView.TableRow;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import main.java.Account;
import main.java.HttpClient;
import main.java.MainClass;
import main.java.Validator;
import main.resources.ResourcesLoader;
import main.resources.ResourcesLoader.StatusLoadWorker;
import main.utils.InvalidGuildIDException;
import main.utils.JTable2XLS;
import main.utils.ProfileWithoutGuildIDException;
import main.utils.TableColumnAdjuster;
import main.utils.XLS2JTable;
import main.utils.XLSFileFilter;
import net.miginfocom.swing.MigLayout;


public class MainPanel extends JPanel implements ActionListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public JMenuBar menuBar;
	public JMenu menuOption, menuGetLastest, menuAbout;
	public static JMenu menuLoadStatus;

	public static JCheckBoxMenuItem checkBoxMenuItemPoeStatistics;

	private static JCheckBoxMenuItem checkBoxMenuItemPoeTrade;

	private JCheckBoxMenuItem checkBoxMenuItemAutoFit;

	private JTable table;
	private TableColumnAdjuster adjuster;
	private DefaultTableModel tableModel;
	private JPanel panel, gibTake;
	private JPanel controlPanel, statusPanel;

	public static JLabel poeStatisticsStatus, poeTradeStatus, lastestVersion;

	private JButton buttonGet, buttonXLS;
	private JButton buttonAddMember, buttonRmMember;
	private JButton buttonPromote, buttonDemote;
	private JButton buttonReset, buttonGib, buttonTake;
	private JTextField textFamilyName;
	private Border textBorder;
	private ButtonGroup buttonGroup;
	private JRadioButton radioGuildID, radioProfileName;
	public static JTextField textFieldGuildID;
	private JTextField textFieldProfileName;
	public JComboBox<Integer> comboBoxThreadAmount;
	private ListSelectionListener selectionListener;
	private DefaultTableCellRenderer defaultRenderer;
	private DefaultTableCellRenderer selectionRenderer;

	private String path = "";
	
	private static Integer count = 0;
	private int selectedRow = -1;
	private int[] selectedRange = new int[]{-1,-1};
	private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

	public static JTextArea logOutput;

	/**
	 * 
	 */
	public MainPanel() {
		setLayout(new BorderLayout());
		tableModel = new DefaultTableModel(new Object[] { "ID", "Family Name", "Member Type",
				"Status", "Total Contribution", "Joined Date", "Last Contributed" }, 0) {

						/**
					 *
					 */
			private static final long serialVersionUID = 1L;

			@Override
			public Class<?> getColumnClass(int column) {
				switch (column) {
				case 0:
					return Integer.class;
				case 1:
					return String.class;
				case 2:
					return String.class;
				case 3:
					return String.class;
				case 4:
					return int.class;
				case 5:
					return String.class;
				case 6:
					return String.class;
				case 7:
					return String.class;
				default:
					return String.class;
				} 
			}


			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
				// This causes all cells to be not editable
			}
			
		};

		table = new JTable(tableModel) {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean getScrollableTracksViewportWidth() {
				return getPreferredSize().width < getParent().getWidth();
			}
		};
		
		TableRowSorter<TableModel> trs = new TableRowSorter<TableModel>(tableModel);
		trs.setComparator(0, new Comparator<Object>() {
		      @Override
		      public int compare(Object o1, Object o2) {
		    	  String s1=o1.toString(); String s2 = o2.toString();
		         Integer i1 = Integer.parseInt(s1);
		         Integer i2 = Integer.parseInt(s2);
		         return i1.compareTo(i2);
		     }
		 });
		//trs.setComparator(4, new IntComparator());
		trs.setComparator(4, new Comparator<Object>() {
		      @Override
		      public int compare(Object o1, Object o2) {
		    	  String s1=o1.toString(); String s2 = o2.toString();
		         Integer i1 = Integer.parseInt(s1);
		         Integer i2 = Integer.parseInt(s2);
		         return i1.compareTo(i2);
		     }
		 });
		table.setRowSorter(trs);
		table.setAutoCreateRowSorter(false);
		
		
		table.setPreferredScrollableViewportSize(new Dimension(700, 300));
		table.setFillsViewportHeight(true);
		//table.setAutoCreateRowSorter(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setRowHeight(25);
		table.getRowSorter().toggleSortOrder(0);
		table.setSelectionBackground(Color.BLUE);
		table.setSelectionForeground(Color.WHITE);

		adjuster = new TableColumnAdjuster(table);
		adjuster.adjustColumns();

		DefaultTableCellRenderer buttonRenderer = new DefaultTableCellRenderer();
		buttonRenderer.setHorizontalAlignment(JButton.CENTER);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
		leftRenderer.setHorizontalAlignment(JLabel.LEFT);
		table.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
		table.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
		for(int i = 2; i < table.getRowCount(); i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		centerRenderer .setHorizontalAlignment(JLabel.CENTER);
		table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
		
		defaultRenderer = new DefaultTableCellRenderer();
		selectionRenderer  = new DefaultTableCellRenderer();
		selectionRenderer.setForeground(Color.YELLOW);
		selectionRenderer.setBackground(Color.BLUE);

		add(new JScrollPane(table), BorderLayout.CENTER);

		selectionListener = new SharedListSelectionHandler(); 		

		menuAbout = new JMenu("About");
		menuAbout.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (Desktop.isDesktopSupported())
					try {
						Desktop.getDesktop()
								.browse(new URI("https://github.com/survfate/poesimpleguild/blob/master/README.md"));
					} catch (IOException | URISyntaxException exception) {
						exception.printStackTrace();
					}
			}
		});

		panel = new JPanel();
		panel.setLayout(new BorderLayout(5, 5));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		add(panel, BorderLayout.NORTH);

		controlPanel = new JPanel();
		controlPanel.setLayout(new MigLayout("center", "15[fill][fill]15[fill]15", ""));
		panel.add(controlPanel);
		
		controlPanel.add(new JLabel("Family Name:"));
		
		textFamilyName = new JTextField(10);
		textBorder = BorderFactory.createLineBorder(Color.BLACK);
		textFamilyName.setBorder(textBorder);
		textFamilyName.setEditable(true);
		textFamilyName.addActionListener(this);
		textFamilyName.setMargin(new Insets(1,0,1,0));
		controlPanel.add(textFamilyName);
		
		buttonAddMember = new JButton("Add New Member");
		buttonAddMember.setEnabled(true);
		buttonAddMember.addActionListener(this);
		controlPanel.add(buttonAddMember);
		
		buttonRmMember = new JButton("Banish Member");
		buttonRmMember.setEnabled(false);
		buttonRmMember.addActionListener(this);
		controlPanel.add(buttonRmMember, "wrap");
		
		buttonPromote = new JButton("Promote");
		buttonPromote.setEnabled(false);
		buttonPromote.addActionListener(this);
		controlPanel.add(buttonPromote);
		
		buttonDemote = new JButton("Demote");
		buttonDemote.setEnabled(false);
		buttonDemote.addActionListener(this);
		controlPanel.add(buttonDemote);
		
		buttonReset = new JButton("Reset");
		buttonReset.setEnabled(false);
		buttonReset.addActionListener(this);
		controlPanel.add(buttonReset);
		
		gibTake = new JPanel();
		gibTake.setLayout(new MigLayout("right", "", ""));
		
		buttonGib = new JButton("+");
		buttonGib.setMinimumSize(new Dimension(60,1));
		buttonGib.setFont(new Font("Arial", Font.PLAIN, 19));
		buttonGib.setEnabled(false);
		buttonGib.addActionListener(this);
		gibTake.add(buttonGib, "west");
		
		buttonTake = new JButton("-");
		buttonTake.setMinimumSize(new Dimension(60, 1));
		buttonTake.setFont(new Font("Arial", Font.PLAIN, 19));
		buttonTake.setEnabled(false);
		buttonTake.addActionListener(this);
		gibTake.add(buttonTake , "east");
		controlPanel.add(gibTake);
			
		statusPanel = new JPanel();

		statusPanel.setLayout(new MigLayout("right", "", ""));

		buttonGet = new JButton("Load Members");
		buttonGet.addActionListener(this);
		buttonGet.setEnabled(true);
		statusPanel.add(buttonGet, "north");
		
		buttonXLS = new JButton("Save Members");
		buttonXLS.setEnabled(true);
		buttonXLS.addActionListener(this);
		statusPanel.add(buttonXLS, "south");
		
		controlPanel.add(statusPanel, "dock east, gapbottom 15, gapleft 30");
		
		table.setRowSelectionAllowed(true);
		table.getSelectionModel().addListSelectionListener(selectionListener);
		
		logOutput = new JTextArea(3, 20);
		DefaultCaret ouputCaret = (DefaultCaret) logOutput.getCaret();
		ouputCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		logOutput.setEditable(false);
		add(new JScrollPane(logOutput), BorderLayout.SOUTH);
		
		
	}

	public static void createAndShowGUI() {
		try {
			org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
			// UIManager.setLookAndFeel(org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.getBeautyEyeLNFCrossPlatform());

			UIManager.put("RootPane.setupButtonVisible", false);

			// UIManager.getDefaults().put("TextArea.font",
			// UIManager.getFont("TextField.font"));

			Font robotoFont = ResourcesLoader.robotoFont;

			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(robotoFont.deriveFont(13F));

			setUIFont(new FontUIResource(robotoFont.deriveFont(13F)));

		} catch (Exception e) {
			// If not available, you can set the GUI to another look
			// and feel.
		}
		// UIManager.put("swing.boldMetal", Boolean.FALSE);
		JFrame frame = new JFrame("Guild Contirbution Manager" + MainClass.VERSION);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		MainPanel newContentPane = new MainPanel();
		newContentPane.setOpaque(true);
		frame.setJMenuBar(newContentPane.menuBar);
		frame.setContentPane(newContentPane);

		frame.pack();
		frame.setVisible(true);
	}

	public static void setUIFont(javax.swing.plaf.FontUIResource f) {
		@SuppressWarnings("rawtypes")
		java.util.Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value != null && value instanceof javax.swing.plaf.FontUIResource)
				UIManager.put(key, f);
		}
	}
	
	public void toJtable(JTable table, File file) {
		File xlsFile = file;
		Workbook readableWorkbook;
		Boolean success = false;
		String[] CellColumns = {"A","B","C","D","E","F","G","H","I","J"};
		//logOutput.append("I was Called!\n" );
		try {
			readableWorkbook = Workbook.getWorkbook(xlsFile);
			Sheet XLSheet = readableWorkbook.getSheet(0);			
			for (int i = 1; i < XLSheet.getRows()+1 ; i++) {
					Account player = new Account();
					for (int j = 0; j < XLSheet.getColumns() ; j++) {
						Object object = XLSheet.getCell(CellColumns[j]+i).getContents(); 
						if(j==4) {
							int value = Integer.valueOf(object.toString());
							player.setValue(j, value);
						}
						else
						{
							String value = String.valueOf(object);
							player.setValue(j, value);
							}
						
					}
				count++;
				Runnable memberWorker = new Member2TableRowRunnable(player, tableModel, count);
				logOutput.append(player.getFamilyName()+" Loaded.\n");
				memberWorker.run();
			}
			updateTable();
			//writableWorkbook.write();
			readableWorkbook.close();
			//writableWorkbook.close();
			success = true;
		} catch (IOException | BiffException | ParseException e) {
			success = false;
			e.printStackTrace();
			logOutput.append(e.getStackTrace().toString());
		}
		updateTable();
		if (success != true){
			JOptionPane.showMessageDialog(null,
					"Something is wrong with the loading process.",
					"Error", 0);
			}
		
	}
	

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == buttonRmMember) {
			banishMember();
		} else if (event.getSource() == buttonGet) {
			JFileChooser fileChooser = new JFileChooser();
			if(path.isEmpty()) {
				path = "%USERPROFILE%" + "\\"+ "GuildContribution" + ".xls";
			}
			fileChooser.setSelectedFile(new File(path));
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.addChoosableFileFilter(new XLSFileFilter());
			int option = fileChooser.showOpenDialog(this);
			if (option == JFileChooser.APPROVE_OPTION) {
				toJtable(table, fileChooser.getSelectedFile());
				path = fileChooser.getSelectedFile().getPath();
				table.repaint();
				table.revalidate();
			}
		} else if (event.getSource() == buttonXLS) {
			JFileChooser fileChooser = new JFileChooser();
			if(path.isEmpty()) {
				path = "%USERPROFILE%" + "\\"+ "GuildContribution" + ".xls";
			}
			fileChooser.setSelectedFile(new File(path));
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.addChoosableFileFilter(new XLSFileFilter());
			int option = fileChooser.showSaveDialog(this);
			if (option == JFileChooser.APPROVE_OPTION) {
				JTable2XLS.saveXLS(table, fileChooser.getSelectedFile());
				path = fileChooser.getSelectedFile().getPath();
				logOutput.append("\n" + path);
			}
		} else if(event.getSource() == buttonAddMember || event.getSource() == textFamilyName) {
			createNewAccount();
		} else if(event.getSource() == buttonReset) {
			Reset();
		} else if(event.getSource() == buttonDemote) {
			Demote();
		} else if (event.getSource() == buttonGib ) {
			addPoint();
		} else if (event.getSource() == buttonTake) {
			RemovePoint();
		} else if (event.getSource() == buttonPromote) {
			Promote();
		}
	}

	private void createNewAccount() {		
		if(!textFamilyName.getText().isEmpty()) {
			count++;
			Runnable memberWorker = new NewMember2TableRowRunnable(textFamilyName.getText(), tableModel, count);
			logOutput.append(textFamilyName.getText()+"\n");
			memberWorker.run();
			textFamilyName.setText("");
		}
		else {
			logOutput.append("Family Name Must not be Blank.\n");
		}
	}
	
	private static class TaskListener implements PropertyChangeListener {
		TaskListener() {
		}

		@Override
		public void propertyChange(PropertyChangeEvent e) {
			// System.out.println(e.toString());
		}
	}

	// SwingWorker for geting Guild information
	public class GuildLoadWorker extends SwingWorker<Void, Void> {
		GuildLoadWorker() {
			addPropertyChangeListener(new TaskListener());
		}

		@Override
		protected Void doInBackground() throws ParseException, URISyntaxException, IOException, InvalidGuildIDException,
				InterruptedException, ExecutionException, ProfileWithoutGuildIDException {

			long tStart = System.currentTimeMillis();
			enableControl(false);

			tableModel.getDataVector().removeAllElements();
			tableModel.fireTableDataChanged();
			return null;
		}

		@Override
		protected void done() {
			try {
				get();
			} catch (ExecutionException e) {
				e.getCause().printStackTrace();
				if (e.getCause().getClass().getSimpleName().equals("InvalidGuildIDException")
						|| e.getCause().getClass().getSimpleName().equals("UnknownHostException")
						|| e.getCause().getClass().getSimpleName().equals("NullPointerException"))
					JOptionPane.showMessageDialog(null, "Invalid Guild ID or Profile Name! Please try again.", "Error",
							0);

				else if (e.getCause().getClass().getSimpleName().equals("ProfileWithoutGuildIDException"))
					JOptionPane.showMessageDialog(null, "This Profile does not belong in a Guild! Please try again.",
							"Error", 0);

				else if (e.getCause().getClass().getSimpleName().equals("SocketTimeoutException")) {
					JOptionPane.showMessageDialog(null, "Connection timed out! Please try again later.", "Error", 0);
					this.cancel(true); // Cancel the SwingWorker
					tableModel.fireTableDataChanged();
					enableControl(true);
				}

				else if (e.getCause().getClass().getSimpleName().equals("IOException"))
					; // ignore
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			tableModel.fireTableDataChanged();
			enableControl(true);
		}
	}

	public static class Member2TableRowRunnable implements Runnable {
		private final Account member;
		private final DefaultTableModel tableModel;
		private int index;

		Member2TableRowRunnable(Account member, DefaultTableModel tableModel, int index) {
			this.member = member;

			this.tableModel = tableModel;
			this.index = index;
		}

		@Override
		public void run() {

				Account account = member;
				// Instantiate account object
				String accountName = account.getFamilyName();
				String memberType = account.getMemberType();
				String status = account.getSatus();
				int contribution = account.getContribution();
				String joinedDate = account.getJoinedDate();
				String lastVisitedDate = account.getLastContribution();
				tableModel.addRow(new Object[] { new Integer(index), accountName, memberType, status, new Integer(contribution), 
						 joinedDate, lastVisitedDate });	
		}
	}
	
	public static class NewMember2TableRowRunnable implements Runnable {
		private final String name;
		private final DefaultTableModel tableModel;
		private int index;

		NewMember2TableRowRunnable(String name, DefaultTableModel tableModel, int index) {
			this.name = name;
			this.tableModel = tableModel;
			this.index = index;
		}

		@Override
		public void run() {
			//Row Order
//"ID", "Family Name", "Member Type","Status", "Total Contribution", "Buttons", "Joined Date", "Last Contributed"
			Validator validator = new Validator();
			//Validate name;
			List<String> errors = validator.validateFamilyName(name);
			if (errors.isEmpty()) {
				Account account = null;
				// Instantiate account object
				try {
				account = new Account(name);
				} catch (Exception e) {
					logOutput.append("Exception caught:" + e.getStackTrace());
				}
				String accountName = account.getFamilyName();
				String memberType = account.getMemberType();
				String status = account.getSatus();
				int contribution = account.getContribution();
				String joinedDate = account.getJoinedDate();
				String lastVisitedDate = account.getLastContribution();
				tableModel.addRow(new Object[] { new Integer(index), accountName, memberType, status, new Integer(contribution), 
						 joinedDate, lastVisitedDate });
			} else {
				logOutput.append("Family Name is Invalid.");
				count--;
				for (String errorMsg : errors) {
					logOutput.append(errorMsg);
				}
			}
		}
	}
	
	public void banishMember() {
		int Amt = table.getSelectedRowCount();
		if(Amt > 1) {
			logOutput.append("ERROR: You May only Delete one Person At a time");
		}
		else {
			killRow(table.getSelectedRow());
		}
	}
	
	public void updateTable() {
		for(int i=0; i< table.getRowCount(); i++)
		{
			if(table.getRowCount() > 1) {
			table.setValueAt(String.valueOf(i+1), i, 0);
			}
		}
		count = table.getRowCount();
		table.setUpdateSelectionOnSort(true);
		selectedRange[0] = -1;
		selectedRange[1] = -1;
		tableModel.fireTableDataChanged();
		table.clearSelection();
		table.revalidate();
		table.repaint();
		enableControl(false);
	}
	
	public void updateTableLite() {
		tableModel.fireTableDataChanged();
		table.revalidate();
		table.repaint();
	}
	
	public void killRow(int i) {
		
		if (table.getRowCount()!=1) {
		logOutput.append("DELETE " + table.getValueAt(i, 1) + "\n");
		tableModel.removeRow(i);
		}
		else {
			tableModel.getDataVector().removeAllElements();
		}
		
	}
	
	public void Promote() {
		for(int i=selectedRange[0];i <= selectedRange[1]; i++) {
			promoteOfficer(i);
			}
		updateTable();
	}
	
	public void promoteOfficer(int i) {
		if(table.getValueAt(i, 2).equals("Member")){
		table.setValueAt("Officer", i, 2);	
		}
		else if(table.getValueAt(i, 2).equals("Officer")) {
		table.setValueAt("Leadership", i, 2);
		}
	}
	public void demoteOfficer(int i) {
		if(table.getValueAt(i, 2).equals("Officer")){
		table.setValueAt("Member", i, 2);	
		}
		else if(table.getValueAt(i, 2).equals("Leadership")) {
		table.setValueAt("Officer", i, 2);
		}
	}
	
	public void Demote() {
		for(int i=selectedRange[0];i <= selectedRange[1]; i++) {
			demoteOfficer(i);
			}
		updateTable();
	}
	public void addPoint() {
		for(int i=selectedRange[0];i <= selectedRange[1]; i++) {
			gibPoint(i);
			}
		updateTableLite();
	}

	public void RemovePoint() {
		for(int i=selectedRange[0];i <= selectedRange[1]; i++) {
			takePoint(i);
			}
		updateTableLite();
	}
	
	public void Reset() {
		for(int i=selectedRange[0];i <= selectedRange[1]; i++) {
			takeAllPoint(i);
			}
		updateTable();
	}

	public void gibPoint(int i) {
		int point  = Integer.parseInt(table.getValueAt(i, 4).toString());
		String date = dateFormat.format(new Date());
		point++;
		table.setValueAt(String.valueOf(point), i, 4);
		table.setValueAt("Active", i, 3);
		table.setValueAt(date, i, 6);
	}

	public void takePoint(int i) {
		int point  = Integer.parseInt(table.getValueAt(i, 4).toString());
		point--;
		table.setValueAt(String.valueOf(point), i, 4);
	}
	
	public void takeAllPoint(int i) {
		int point  = 0;
		table.setValueAt(String.valueOf(point), i, 4);
	}



	public void enableControl(boolean enable) {
		buttonRmMember.setEnabled(enable);
		buttonPromote.setEnabled(enable);
		buttonDemote.setEnabled(enable);
		buttonReset.setEnabled(enable);
		buttonGib.setEnabled(enable);
		buttonTake.setEnabled(enable);
	}
	
	
	
	
	class SharedListSelectionHandler implements ListSelectionListener {
	    public void valueChanged(ListSelectionEvent e) {

	        int firstIndex = table.getSelectedRows()[0];
	        int lastIndex = table.getSelectedRows()[table.getSelectedRowCount()];
	        if(lastIndex-firstIndex>1) {
	        	selectedRange[0] = firstIndex;
	        	selectedRange[1] = lastIndex;
	        }
	        else {
	        	selectedRange[0] = firstIndex;
	        	selectedRange[1] = firstIndex;
	        }
	        
    		if(table.getSelectedRowCount() == 1) {
    			selectedRange[0] = table.getSelectedRow();
    			selectedRange[1] = table.getSelectedRow();
    		}
    		else if(table.getSelectedRowCount() != (selectedRange[1]-selectedRange[0])){
    			selectedRange[0] = -1;
    			selectedRange[1] = -1;
    		}
	        
	        if(selectedRange[0] > -1) {
	        	enableControl(true);
	        }
	        else {
	        	enableControl(false);
	        }
	    }
	}
    
}
