import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.sql.*;
import javax.swing.*;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.awt.event.ActionEvent;
public class MinusxlGui {

	private JFrame frmMinusxl;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					MinusxlGui window = new MinusxlGui();
					window.frmMinusxl.setExtendedState(JFrame.MAXIMIZED_BOTH);
					window.frmMinusxl.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	Connection connection=null;
	private JTable table;
	
	/**
	 * Create the application.
	 */
	public MinusxlGui() {
		initialize();
		connection=SqliteConnection.dbConnector();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//frmMinusxl = new JFrame();
		Dimension screensize=Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle rect=new Rectangle(screensize.width/2-screensize.width/4,screensize.height/2-screensize.height/4,
				screensize.width,screensize.height);
		frmMinusxl =new JFrame("Resizing Table");
		frmMinusxl.setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/Images/killer-cow-icon.png")));
		frmMinusxl.setTitle("MinusXL");
		frmMinusxl.setPreferredSize(new Dimension(rect.width,rect.height-50));
		frmMinusxl.setResizable(true);
		frmMinusxl.pack();
		frmMinusxl.setLocation(rect.x, rect.y);
		frmMinusxl.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frmMinusxl.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		JMenuBar menuBar = new JMenuBar();
		frmMinusxl.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenu mnNewMenu_5 = new JMenu("New");
		mnFile.add(mnNewMenu_5);
		
		JMenuItem mntmNewWorkbook = new JMenuItem("New Workbook");
		mntmNewWorkbook.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					JTextField WBname = new JTextField();
					Object[] message = {
						    "WorkBook name:", WBname
					};
					int option = JOptionPane.showConfirmDialog(null, message, "New Workbook", JOptionPane.OK_CANCEL_OPTION);
					if(option == JOptionPane.YES_OPTION){
						WorkbookGui WorkBK=new WorkbookGui();
						WorkBK.setTitle(WBname.getText());
						WorkBK.setVisible(true);
					}else{
						WorkbookGui WorkBK=new WorkbookGui();
						WorkBK.setTitle("New Workbook");
						WorkBK.setVisible(true);
					}
				}catch(Exception e){
					JOptionPane.showConfirmDialog(null, e);
				}
			}
		});
		mnNewMenu_5.add(mntmNewWorkbook);
		
		JMenuItem mntmImport = new JMenuItem("Import");
		mntmImport.setIcon(new ImageIcon(this.getClass().getResource("/Images/import2.png")));
		mnFile.add(mntmImport);
		frmMinusxl.getContentPane().setLayout(null);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setBounds(0, 0, rect.width, 41);
		
		frmMinusxl.getContentPane().add(toolBar);
		
		JButton btnSaveButton = new JButton("");
		btnSaveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					
				}catch(Exception e){
					
				}
			}
		});
		btnSaveButton.setIcon(new ImageIcon(this.getClass().getResource("/Images/document_save.png")));
		btnSaveButton.setToolTipText("save");
		btnSaveButton.setSelectedIcon(new ImageIcon(this.getClass().getResource("/Images/document_save.png")));
		toolBar.add(btnSaveButton);
		
		JButton btnNewWorkBook = new JButton("");
		btnNewWorkBook.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					JTextField WBname = new JTextField();
					Object[] message = {
						    "WorkBook name:", WBname
					};
					int option = JOptionPane.showConfirmDialog(null, message, "New Workbook", JOptionPane.OK_CANCEL_OPTION);
					if(option == JOptionPane.YES_OPTION){
						WorkbookGui WorkBK=new WorkbookGui();
						WorkBK.setTitle(WBname.getText());
						WorkBK.setVisible(true);
					}else{
						WorkbookGui WorkBK=new WorkbookGui();
						WorkBK.setTitle("New Workbook GUI");
						WorkBK.setVisible(true);
					}
				}catch(Exception e){
					JOptionPane.showConfirmDialog(null, e);
				}
			}
		});
		btnNewWorkBook.setToolTipText("New WorkBook");
		btnNewWorkBook.setIcon(new ImageIcon(this.getClass().getResource("/Images/table.png")));
		toolBar.add(btnNewWorkBook);
		
		JButton btnserverConnect = new JButton("");
		btnserverConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				try{
					String query="select * from EmployeeInfo where Username=? and password=?";
					PreparedStatement pst=connection.prepareStatement(query);
					JTextField username = new JTextField();
					JTextField password = new JPasswordField();
					Object[] message = {
						    "Username:", username,
						    "Password:", password
						};					
					int option = JOptionPane.showConfirmDialog(null, message, "Login", JOptionPane.OK_CANCEL_OPTION);

					
					
					pst.setString(1, username.getText());
					pst.setString(2, password.getText());
					ResultSet rs=pst.executeQuery();
					int count =0;
					while(rs.next()){
						count=count+1;
					}
					if(count==1){
						JOptionPane.showMessageDialog(null, "Connection Succeed");
						frmMinusxl.dispose();
						DatabaseWorkbook WorkBK=new DatabaseWorkbook();
						WorkBK.setVisible(true);
					}
					else if(count>1){
						JOptionPane.showMessageDialog(null, "Duplicate");
					}
					else{
						JOptionPane.showMessageDialog(null, "Connection Failed");
					}
					
					rs.close();
					pst.close();
				}catch(Exception e){
					JOptionPane.showMessageDialog(null, e);
					
				}
				
			}
		});
		btnserverConnect.setToolTipText("Server");
		btnserverConnect.setIcon(new ImageIcon(this.getClass().getResource("/Images/be-net-server-icon.png")));
		toolBar.add(btnserverConnect);
		
		JButton button = new JButton("");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					if (Desktop.isDesktopSupported()) {
		                Desktop desktop = Desktop.getDesktop();
		                try {
		                		java.net.URL location =WorkbookGui.class.getProtectionDomain().getCodeSource().getLocation();
	                		
	                			URI uri = new URI(location+"site/untitled/home.html");
		                        desktop.browse(uri);
		                } catch (IOException ex) {
		                        // do nothing
		                } catch (URISyntaxException ex) {
							//do nothing
						}
					} else {
				               //do nothing
				        	}
				}catch(Exception e){
					
				}
			}
		});
		button.setToolTipText("Help site");
		button.setIcon(new ImageIcon(this.getClass().getResource("/Images/cowboy (1).png")));
		toolBar.add(button);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 52, rect.width, rect.height-100);
		frmMinusxl.getContentPane().add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
	}
}

