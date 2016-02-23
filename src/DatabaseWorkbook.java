import java.awt.EventQueue;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import net.proteanit.sql.DbUtils;

import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DatabaseWorkbook extends JFrame {

	private JPanel contentPane;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					DatabaseWorkbook frame = new DatabaseWorkbook();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	Connection connection=null;
	public DatabaseWorkbook() {
		connection=SqliteConnection.dbConnector();
		setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/Images/killer-cow-icon.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 762, 416);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(134, 104, 451, 204);
		contentPane.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 736, 21);
		contentPane.add(menuBar);
		
		JMenu menu = new JMenu("File");
		menuBar.add(menu);
		
		JMenu menu_1 = new JMenu("New");
		menu.add(menu_1);
		
		JMenuItem menuItem = new JMenuItem("New Workbook");
		menu_1.add(menuItem);
		
		JMenuItem menuItem_1 = new JMenuItem("New Spreadsheet");
		menu_1.add(menuItem_1);
		
		JMenuItem menuItem_4 = new JMenuItem("Import");
		menuItem_4.setIcon(new ImageIcon(this.getClass().getResource("/Images/import2.png")));
		menuItem_4.setSelectedIcon(new ImageIcon(this.getClass().getResource("/Images/import2.png")));
		menu.add(menuItem_4);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setBounds(0, 21, 736, 41);
		contentPane.add(toolBar);
		
		JButton buttonLoadData = new JButton("");
		buttonLoadData.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					String query="select * from Isologismos";
					PreparedStatement pst=connection.prepareStatement(query);
					ResultSet rs=pst.executeQuery();
					table.setModel(DbUtils.resultSetToTableModel(rs));
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		buttonLoadData.setIcon(new ImageIcon(this.getClass().getResource("/Images/import.png")));
		buttonLoadData.setToolTipText("Load data");
		toolBar.add(buttonLoadData);
		
		JButton button = new JButton("");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		button.setIcon(new ImageIcon(this.getClass().getResource("/Images/document_save.png")));
		button.setToolTipText("save");
		toolBar.add(button);
	}
}
