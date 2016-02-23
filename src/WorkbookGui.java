import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import org.jfree.ui.RefineryUtilities;

import java.awt.Toolkit;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.print.DocFlavor.URL;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTabbedPane;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class WorkbookGui extends JFrame {

	private JPanel contentPane;
	private int type=0;
	private int Xd=5;
    private int Yd=5;
    private int clickrow1,clickcolumn1;
    private int FirstTime=0;
    private  static int tabcount=0;
    private Connection connection=null;
    private ArrayList<JTable> tables = new ArrayList<JTable>();
    private static boolean  pressingCTRL=false;//flag, if pressing CTRL it is true, otherwise it is false.
    private static Vector selectedCells = new Vector<int[]>();//int[]because every entry will store {cellX,cellY}

    public static JTabbedPane tab;
	/**
	 * Launch the application.
	 */
    private ArrayList<Workbook> books = new ArrayList<Workbook>();
    public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					WorkbookGui frame = new WorkbookGui();
					frame.setVisible(true);
					MouseListener tableMouseListener = new MouseAdapter() {

					      @Override
					      public void mouseClicked(MouseEvent e) {
					         if(pressingCTRL){//check if user is pressing CTRL key
					            int row = Workbook.getSheetList().get(tabcount).getclickRow();//get mouse-selected row
					            int col = Workbook.getSheetList().get(tabcount).getclickColumn();//get mouse-selected col
					            int[] newEntry = new int[]{row,col};//{row,col}=selected cell
					            if(selectedCells.contains(newEntry)){
					               //cell was already selected, deselect it
					               selectedCells.remove(newEntry);
					            }else{
					               //cell was not selected
					               selectedCells.add(newEntry);
					            }
					         }
					      }
					   };
					tab.addMouseListener(new java.awt.event.MouseAdapter() {
						
				        	
				      	  @Override
						public void mousePressed(java.awt.event.MouseEvent ee) {
				      		
				      		  tabcount=(ee.getPoint().x-5)/100;
				      		
				      	  }
					});
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
	public WorkbookGui() {
		
		Workbook book=new Workbook();
	   books.add(book);
	   Dimension screensize=Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle rect=new Rectangle(screensize.width/2-screensize.width/4,screensize.height/2-screensize.height/4,
				screensize.width,screensize.height);
	    
		connection=SqliteConnection.dbConnector();
		setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/Images/killer-cow-icon.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(0, 0, rect.width, rect.height);
		contentPane = new JPanel(new FlowLayout(FlowLayout.LEADING));
		
		
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, rect.width, 21);
		contentPane.add(menuBar);
		
		JMenu menu = new JMenu("File");
		menuBar.add(menu);
		
		JMenu menu_1 = new JMenu("New");
		menu.add(menu_1);
		
		JMenuItem menuItem_1 = new JMenuItem("New Spreadsheet");
		menuItem_1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {

				try{
					 
					if(FirstTime==0){
						JTextField xField = new JTextField(5);
					    JTextField yField = new JTextField(5);
	
					    JPanel myPanel = new JPanel();
					    myPanel.add(new JLabel("x:"));
					    myPanel.add(xField);
					    myPanel.add(Box.createHorizontalStrut(15)); 
					    myPanel.add(new JLabel("y:"));
					    myPanel.add(yField);
					    String Xd_t=null;
					    String Yd_t=null;
					    int result = JOptionPane.showConfirmDialog(null, myPanel,"Please Enter X and Y Values", JOptionPane.OK_CANCEL_OPTION);
					    if (result == JOptionPane.OK_OPTION) {
					        Xd_t= xField.getText();
					        Yd_t= yField.getText();
					        
					        if(Xd_t.equals("")){
					        	Xd=5;
					        }else{
					        	Xd=Integer.parseInt(Xd_t); 
					        }
					        if(Yd_t.equals("")){
					        	Yd=5;
					        }else{
					        	Yd=Integer.parseInt(Yd_t);
					        }
					    
					    }
					  
					    
					    String[] col=new String[Yd];
					    
					    book.createSpreadsheet(Xd, Yd);
					    
					    JTable table = new JTable();
					
					    tables.add(table);
					    for(int i=0;i<Yd;i++){
					    	String temp=Integer.toString(i);
					    	
					    	col[i]=temp;
					    	
					    }
					    tables.get(FirstTime).setModel(new DefaultTableModel(Workbook.getSheetList().get(FirstTime).getGrid(), col));
					    tables.get(FirstTime).setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
					   
					    Workbook.getSheetList().get(FirstTime).TableModelListenerDemo(tables.get(FirstTime));
					    JScrollPane scrollPane=new JScrollPane(tables.get(FirstTime));
					    String name="Spreadsheet"+(FirstTime+1);
					    
					    tab.addTab(name, scrollPane);
					    LineNumberTableRowHeader tableLinenumber=new LineNumberTableRowHeader(scrollPane,tables.get(FirstTime));
						tableLinenumber.setBackground(Color.LIGHT_GRAY);
						scrollPane.setRowHeaderView(tableLinenumber);
						FirstTime=FirstTime+1;
						
						
					}
					
					else{
						Xd=5;
					    Yd=5;
						
					   // tables.add(table);
						JTextField xField = new JTextField(5);
					    JTextField yField = new JTextField(5);
	
					    JPanel myPanel = new JPanel();
					    myPanel.add(new JLabel("x:"));
					    myPanel.add(xField);
					    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
					    myPanel.add(new JLabel("y:"));
					    myPanel.add(yField);
					    String Xd_t=null;
					    String Yd_t=null;
					    int result = JOptionPane.showConfirmDialog(null, myPanel,"Please Enter X and Y Values", JOptionPane.OK_CANCEL_OPTION);
					    if (result == JOptionPane.OK_OPTION) {
					    	Xd_t= xField.getText();
					        Yd_t= yField.getText();
					        
					        if(Xd_t.equals("")){
					        	Xd=5;
					        }else{
					        	Xd=Integer.parseInt(Xd_t); 
					        }
					        if(Yd_t.equals("")){
					        	Yd=5;
					        }else{
					        	Yd=Integer.parseInt(Yd_t);
					        }
					    }
					    JTable table = new JTable();
					    String[] col=new String[Yd];
					    
					    book.createSpreadsheet(Xd, Yd);
					    
					    //JTable table1 = new JTable();
					    tables.add(table);
					   
					    for(int i=0;i<Yd;i++){
					    	col[i]=Integer.toString(i);
					    }
					    tables.get(FirstTime).setModel(new DefaultTableModel(Workbook.getSheetList().get(FirstTime).getGrid(), col));
					   
					    Workbook.getSheetList().get(FirstTime).TableModelListenerDemo(tables.get(FirstTime));
					    
					    JScrollPane scrollPane=new JScrollPane(tables.get(FirstTime));
					    String name="Spreadsheet"+(FirstTime+1);
					    
					    tab.addTab(name, scrollPane);
					    LineNumberTableRowHeader tableLinenumber=new LineNumberTableRowHeader(scrollPane,tables.get(FirstTime));
						tableLinenumber.setBackground(Color.LIGHT_GRAY);
						scrollPane.setRowHeaderView(tableLinenumber);
						FirstTime=FirstTime+1;
					}
					        
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		menu_1.add(menuItem_1);
		
		JMenuItem menuItem_3 = new JMenuItem("Save As...");
		menuItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					String saveAs;
					JTextField Savename = new JTextField();
					Object[] message = {
						    "Save file:", Savename
					};
					int option = JOptionPane.showConfirmDialog(null, message, "Save as", JOptionPane.OK_CANCEL_OPTION);
					if(option == JOptionPane.YES_OPTION){
						
						saveAs=(Savename.getText()+".csv");
						book.getSheetList().get(tabcount).saveFile(book.getSheetList(),tabcount,saveAs);
						
					}else{
						saveAs="Untitled.csv";
						book.getSheetList().get(tabcount).saveFile(book.getSheetList(),tabcount,saveAs);
					}
					
				}catch(Exception e){
					e.printStackTrace();
					
				}
			}
		});
		menuItem_3.setIcon(new ImageIcon(this.getClass().getResource("/Images/save.png")));
		menu.add(menuItem_3);
		
		JMenuItem menuItem_4 = new JMenuItem("load");
		menuItem_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					
					
					
					
					String load;
					JTextField Loadname = new JTextField();
					Object[] message = {
						    "Load file:", Loadname
					};
					int option = JOptionPane.showConfirmDialog(null, message, "Load", JOptionPane.OK_CANCEL_OPTION);
					if(option == JOptionPane.OK_OPTION){
						
						load=(Loadname.getText()+".csv");
						
						CsvImport csvload=new CsvImport();
						
						String Filesheet[][]=csvload.process(load);
					
						book.createSpreadsheet(Filesheet.length, Filesheet.length);
						JTable table = new JTable();
						String[] col=new String[Filesheet.length];
					    tables.add(table);
					    for(int i=0;i<Yd;i++){
					    	String temp=Integer.toString(i+1);
					    	
					    	col[i]=temp;
					    	
					    }
					    tables.get(FirstTime).setModel(new DefaultTableModel(Workbook.getSheetList().get(FirstTime).getGrid(), col));
					    tables.get(FirstTime).setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
					   
					    Workbook.getSheetList().get(FirstTime).TableModelListenerDemo(tables.get(FirstTime));
					    String name=Loadname.getText();
					    
					    JScrollPane scrollPane=new JScrollPane(tables.get(FirstTime));
					    
					    tab.addTab(name, scrollPane);
					    LineNumberTableRowHeader tableLinenumber=new LineNumberTableRowHeader(scrollPane,tables.get(FirstTime));
						tableLinenumber.setBackground(Color.LIGHT_GRAY);
						scrollPane.setRowHeaderView(tableLinenumber);
					    
					
						String s1;
					    for(int i=0;i<csvload.getRowSize();i++){
					    	for(int j=0;j<csvload.getColumnSize();j++){
					    		s1=Filesheet[i][j];
					    		if(!s1.equals("")){
					    			if(!s1.equals(" ")){
					    					Workbook.getSheetList().get(FirstTime).TableModelListenerDemo(Filesheet[i][j],tables.get(FirstTime),i, j);
					    			}
					    		}
					    	}	
					    }
					    FirstTime=FirstTime+1;
					}else{
						JOptionPane.showMessageDialog(null, "File name NOT accepted");
					}
					
				}catch(Exception e){
					
				}
			}
		});
		menuItem_4.setIcon(new ImageIcon(this.getClass().getResource("/Images/import2.png")));
		menu.add(menuItem_4);
		
		JMenu menu_2 = new JMenu("Formulas");
		menuBar.add(menu_2);
		
		JMenu menu_3 = new JMenu("Math & Trig");
		menu_3.setIcon(new ImageIcon(this.getClass().getResource("/Images/pi_math.png")));
		menu_2.add(menu_3);
		
		JMenuItem menuItem_5 = new JMenuItem("Abs");
		
		//new code
		

		//new code
		menuItem_5.addActionListener(new ActionListener() {
			@Override
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent arg0) {
				type=1;
				clickcolumn1=Workbook.getSheetList().get(tabcount).getclickColumn();
				clickrow1=Workbook.getSheetList().get(tabcount).getclickRow();
				/*new code */
				int i,j;
				i=0;
				j=0;
				//Spreadsheet m = new  Spreadsheet();
				//JOptionPane.showMessageDialog(null, "select cell");
				//System
				JTextField xField = new JTextField(5);
			    JTextField yField = new JTextField(5);
			    //tables(ta);
			    JPanel myPanel = new JPanel();
			    myPanel.add(new JLabel("x:"));
			    myPanel.add(xField);
			    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
			    myPanel.add(new JLabel("y:"));
			    myPanel.add(yField);
			    String Xd_t=null;
			    String Yd_t=null;
			    int result = JOptionPane.showConfirmDialog(null, myPanel,"give coordinates of the cell", JOptionPane.OK_CANCEL_OPTION);
			    if (result == JOptionPane.OK_OPTION) {
			    	Xd_t= xField.getText();
			        Yd_t= yField.getText();
			        i=Integer.parseInt(Xd_t);
			        j=Integer.parseInt(Yd_t);
			    }
			    String index=null;
			   try{
				   index=Workbook.getSheetList().get(tabcount).getValue(i, j);
				   index=index.toLowerCase();
			   

			   double ascii;
			   int ItIsAString=0;
	           
	            if(!index.equals("")){
	            	try {
	            		
	            	     ascii  = Double.parseDouble(index);
	            	     
	            	} catch (NumberFormatException ex) {
	            	
	            			ItIsAString++;	

	            	}
	            	if(ItIsAString<1){
					   Workbook.getSheetList().get(tabcount).createFunctionCell(type,clickrow1,clickcolumn1,index);
					   Workbook.getSheetList().get(tabcount).TableModelListenerDemo(type,tables.get(tabcount),clickrow1, clickcolumn1);
	            	}
	            	else{
	            		JOptionPane.showMessageDialog(null,"Give a number value");
	            	}
	            }
			   }catch(NullPointerException e)
		        {
				   JOptionPane.showMessageDialog(null,"Give a non empty cell");
		        }
			}
			
			
		});
		menu_3.add(menuItem_5);
		
		JMenuItem menuItem_6 = new JMenuItem("Cos");
		menuItem_6.addActionListener(new ActionListener() {
			@Override
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent arg0) {
				try{
					type=7;
					clickcolumn1=Workbook.getSheetList().get(tabcount).getclickColumn();
					clickrow1=Workbook.getSheetList().get(tabcount).getclickRow();
					/*new code */
					int i,j;
					i=0;
					j=0;
					//Spreadsheet m = new  Spreadsheet();
					//JOptionPane.showMessageDialog(null, "select cell");
					//System
					JTextField xField = new JTextField(5);
				    JTextField yField = new JTextField(5);
				    //tables(ta);
				    JPanel myPanel = new JPanel();
				    myPanel.add(new JLabel("x:"));
				    myPanel.add(xField);
				    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
				    myPanel.add(new JLabel("y:"));
				    myPanel.add(yField);
				    String Xd_t=null;
				    String Yd_t=null;
				    int result = JOptionPane.showConfirmDialog(null, myPanel,"give coordinates of the cell", JOptionPane.OK_CANCEL_OPTION);
				    if (result == JOptionPane.OK_OPTION) {
				    	Xd_t= xField.getText();
				        Yd_t= yField.getText();
				        i=Integer.parseInt(Xd_t);
				        j=Integer.parseInt(Yd_t);
				    }
				
				   try{
				   String index=Workbook.getSheetList().get(tabcount).getValue(i, j);
				   index=index.toLowerCase();
				   double ascii;
				   int ItIsAString=0;
		           
		            if(!index.equals("")){
		            	try {
		            		
		            	     ascii  = Double.parseDouble(index);
		            	     
		            	} catch (NumberFormatException ex) {
		            	
		            			ItIsAString++;	

		            	}
		            	if(ItIsAString<1){
						   Workbook.getSheetList().get(tabcount).createFunctionCell(type,clickrow1,clickcolumn1,index);
						   Workbook.getSheetList().get(tabcount).TableModelListenerDemo(type,tables.get(tabcount),clickrow1, clickcolumn1);
		            	}
		            	else{
		            		JOptionPane.showMessageDialog(null,"Give a number value");
		            	}
		            }
				   }catch(NullPointerException e)
			        {
					   JOptionPane.showMessageDialog(null,"Give a non empty cell");
			        }
		        }catch(Exception e){
					
				}
			}
		});
		menu_3.add(menuItem_6);
		
		JMenuItem menuItem_7 = new JMenuItem("Sin");
		menuItem_7.addActionListener(new ActionListener() {
			@Override
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent arg0) {
				try{
					type=6;
					clickcolumn1=Workbook.getSheetList().get(tabcount).getclickColumn();
					clickrow1=Workbook.getSheetList().get(tabcount).getclickRow();
					/*new code */
					int i,j;
					i=0;
					j=0;
					//Spreadsheet m = new  Spreadsheet();
					//JOptionPane.showMessageDialog(null, "select cell");
					//System
					JTextField xField = new JTextField(5);
				    JTextField yField = new JTextField(5);
				    //tables(ta);
				    JPanel myPanel = new JPanel();
				    myPanel.add(new JLabel("x:"));
				    myPanel.add(xField);
				    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
				    myPanel.add(new JLabel("y:"));
				    myPanel.add(yField);
				    String Xd_t=null;
				    String Yd_t=null;
				    int result = JOptionPane.showConfirmDialog(null, myPanel,"give coordinates of the cell", JOptionPane.OK_CANCEL_OPTION);
				    if (result == JOptionPane.OK_OPTION) {
				    	Xd_t= xField.getText();
				        Yd_t= yField.getText();
				        i=Integer.parseInt(Xd_t);
				        j=Integer.parseInt(Yd_t);
				    }
				
				   try{
					   String index=Workbook.getSheetList().get(tabcount).getValue(i, j);
					   index=index.toLowerCase();
					   double ascii;
					   int ItIsAString=0;
			           
			            if(!index.equals("")){
			            	try {
			            		
			            	     ascii  = Double.parseDouble(index);
			            	     
			            	} catch (NumberFormatException ex) {
			            	
			            			ItIsAString++;	
	
			            	}
			            	if(ItIsAString<1){
							   Workbook.getSheetList().get(tabcount).createFunctionCell(type,clickrow1,clickcolumn1,index);
							   Workbook.getSheetList().get(tabcount).TableModelListenerDemo(type,tables.get(tabcount),clickrow1, clickcolumn1);
			            	}
			            	else{
			            		JOptionPane.showMessageDialog(null,"Give a number value");
			            	}
			            }
				   }catch(NullPointerException e)
			        {
					   JOptionPane.showMessageDialog(null,"Give a non empty cell");
			        }
				   }catch(Exception e){
					
				}
			}
		});
		menu_3.add(menuItem_7);
		
		JMenuItem menuItem_8 = new JMenuItem("Tan");
		menuItem_8.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					type=8;
					clickcolumn1=Workbook.getSheetList().get(tabcount).getclickColumn();
					clickrow1=Workbook.getSheetList().get(tabcount).getclickRow();
					/*new code */
					int i,j;
					i=0;
					j=0;
					//Spreadsheet m = new  Spreadsheet();
					//JOptionPane.showMessageDialog(null, "select cell");
					//System
					JTextField xField = new JTextField(5);
				    JTextField yField = new JTextField(5);
				    //tables(ta);
				    JPanel myPanel = new JPanel();
				    myPanel.add(new JLabel("x:"));
				    myPanel.add(xField);
				    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
				    myPanel.add(new JLabel("y:"));
				    myPanel.add(yField);
				    String Xd_t=null;
				    String Yd_t=null;
				    int result = JOptionPane.showConfirmDialog(null, myPanel,"give coordinates of the cell", JOptionPane.OK_CANCEL_OPTION);
				    if (result == JOptionPane.OK_OPTION) {
				    	Xd_t= xField.getText();
				        Yd_t= yField.getText();
				        i=Integer.parseInt(Xd_t);
				        j=Integer.parseInt(Yd_t);
				    }
				
				   try{
				   String index=Workbook.getSheetList().get(tabcount).getValue(i, j);
				   index=index.toLowerCase();
				   double ascii;
				   int ItIsAString=0;
		           
		            if(!index.equals("")){
		            	try {
		            		
		            	     ascii  = Double.parseDouble(index);
		            	     
		            	} catch (NumberFormatException ex) {
		            	
		            			ItIsAString++;	

		            	}
		            	if(ItIsAString<1){
						   Workbook.getSheetList().get(tabcount).createFunctionCell(type,clickrow1,clickcolumn1,index);
						   Workbook.getSheetList().get(tabcount).TableModelListenerDemo(type,tables.get(tabcount),clickrow1, clickcolumn1);
		            	}
		            	else{
		            		JOptionPane.showMessageDialog(null,"Give a number value");
		            	}
		            }
				   }catch(NullPointerException e)
			        {
					   JOptionPane.showMessageDialog(null,"Give a non empty cell");
			        }
				}catch(Exception e){
					
				}
			}
		});
		menu_3.add(menuItem_8);
		
		JMenuItem menuItem_9 = new JMenuItem("Pow");
		menuItem_9.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					type=9;
					clickcolumn1=Workbook.getSheetList().get(tabcount).getclickColumn();
					clickrow1=Workbook.getSheetList().get(tabcount).getclickRow();
					
					ArrayList<Integer> i = new ArrayList<Integer>();
					ArrayList<Integer> j = new ArrayList<Integer>();
					
					JTextField xField = new JTextField(5);
				    JTextField yField = new JTextField(5);
				    JPanel myPanel = new JPanel();
				    myPanel.add(new JLabel("x:"));
				    myPanel.add(xField);
				    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
				    myPanel.add(new JLabel("y:"));
				    myPanel.add(yField);
				    String Xd_t=null;
				    String Yd_t=null;
				    int result = JOptionPane.showConfirmDialog(null, myPanel,"give coordinates of the cell", JOptionPane.OK_CANCEL_OPTION);
				    if (result == JOptionPane.OK_OPTION) {
				    	Xd_t= xField.getText();
				        Yd_t= yField.getText();
				        
				        String Xd[]=Xd_t.split(",");
				        for(int l=0;l<Xd.length;l++){
				        	i.add(Integer.parseInt(Xd[l]));
				        }
				        String Yd[]=Yd_t.split(",");
				        for(int l=0;l<Yd.length;l++){
				        	j.add(Integer.parseInt(Yd[l]));
				        }
				    }
				
				   try{
				   String index=Workbook.getSheetList().get(tabcount).getValue(i.get(0), j.get(0));
				   String index1=Workbook.getSheetList().get(tabcount).getValue(i.get(1), j.get(1));
				   index=index.toLowerCase();
				   index1=index1.toLowerCase();
				   double ascii;
				   double ascii1;
				   int ItIsAString=0;
		           
		            if(!index.equals("") && !index1.equals("")){
		            	try {
		            		
		            	     ascii  = Double.parseDouble(index);
		            	     
		            	} catch (NumberFormatException ex) {
		            	
		            			ItIsAString++;	

		            	}
		            	try {
		            		
		            	     ascii1  = Double.parseDouble(index1);
		            	     
		            	} catch (NumberFormatException ex) {
		            	
		            			ItIsAString++;	

		            	}
		            	if(ItIsAString<1){
						   Workbook.getSheetList().get(tabcount).createFunctionCell(type,clickrow1,clickcolumn1,index);
						   Workbook.getSheetList().get(tabcount).TableModelListenerDemo(type,tables.get(tabcount),clickrow1, clickcolumn1);
		            	}
		            	else{
		            		JOptionPane.showMessageDialog(null,"Give a number value");
		            	}
		            }
				   }catch(NullPointerException e)
			        {
					   JOptionPane.showMessageDialog(null,"Give a non empty cell");
			        }
				   
				}catch(Exception e){
					
				}
			}
		});
		menu_3.add(menuItem_9);
		
		JMenuItem menuItem_10 = new JMenuItem("Sum");
		menuItem_10.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					 
					type=10;
					clickcolumn1=Workbook.getSheetList().get(tabcount).getclickColumn();
					clickrow1=Workbook.getSheetList().get(tabcount).getclickRow();
					
					ArrayList<Integer> i = new ArrayList<Integer>();
					ArrayList<Integer> j = new ArrayList<Integer>();
					
					JTextField xField = new JTextField(5);
				    JTextField yField = new JTextField(5);
				    JPanel myPanel = new JPanel();
				    myPanel.add(new JLabel("x:"));
				    myPanel.add(xField);
				    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
				    myPanel.add(new JLabel("y:"));
				    myPanel.add(yField);
				    String Xd_t=null;
				    String Yd_t=null;
				    int result = JOptionPane.showConfirmDialog(null, myPanel,"give coordinates of the cell", JOptionPane.OK_CANCEL_OPTION);
				    if (result == JOptionPane.OK_OPTION) {
				    	Xd_t= xField.getText();
				        Yd_t= yField.getText();
				        
				        String Xd[]=Xd_t.split(",");
				        for(int l=0;l<Xd.length;l++){
				        	i.add(Integer.parseInt(Xd[l]));
				        }
				        String Yd[]=Yd_t.split(",");
				        for(int l=0;l<Yd.length;l++){
				        	j.add(Integer.parseInt(Yd[l]));
				        }
				    }
				    
				  try{
				   String index=Workbook.getSheetList().get(tabcount).getValue(i.get(0), j.get(0));
				   String index1=Workbook.getSheetList().get(tabcount).getValue(i.get(1), j.get(1));
				   index=index.toLowerCase();
				   index1=index1.toLowerCase();
				   double ascii;
				   double ascii1;
				   int ItIsAString=0;
		           
		            if(!index.equals("") && !index1.equals("")){
		            	try {
		            		
		            	     ascii  = Double.parseDouble(index);
		            	     
		            	} catch (NumberFormatException ex) {
		            	
		            			ItIsAString++;	

		            	}
		            	try {
		            		
		            	     ascii1  = Double.parseDouble(index1);
		            	     
		            	} catch (NumberFormatException ex) {
		            	
		            			ItIsAString++;	

		            	}
		            	if(ItIsAString<1){
						   Workbook.getSheetList().get(tabcount).createFunctionCell(type,clickrow1,clickcolumn1,index);
						   Workbook.getSheetList().get(tabcount).TableModelListenerDemo(type,tables.get(tabcount),clickrow1, clickcolumn1);
		            	}
		            	else{
		            		JOptionPane.showMessageDialog(null,"Give a number value");
		            	}
		            }
				  }catch(NullPointerException e)
			        {
					   JOptionPane.showMessageDialog(null,"Give a non empty cell");
			        }
				}catch(Exception e){
					
				}
			}
		});
		menu_3.add(menuItem_10);
		
		JMenuItem menuItem_11 = new JMenuItem("Mult");
		menuItem_11.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					type=11;
					clickcolumn1=Workbook.getSheetList().get(tabcount).getclickColumn();
					clickrow1=Workbook.getSheetList().get(tabcount).getclickRow();
					/*new code */
					ArrayList<Integer> i = new ArrayList<Integer>();
					ArrayList<Integer> j = new ArrayList<Integer>();
					
					//Spreadsheet m = new  Spreadsheet();
					//JOptionPane.showMessageDialog(null, "select cell");
					//System
					JTextField xField = new JTextField(5);
				    JTextField yField = new JTextField(5);
				    //tables(ta);
				    JPanel myPanel = new JPanel();
				    myPanel.add(new JLabel("x:"));
				    myPanel.add(xField);
				    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
				    myPanel.add(new JLabel("y:"));
				    myPanel.add(yField);
				    String Xd_t=null;
				    String Yd_t=null;
				    int result = JOptionPane.showConfirmDialog(null, myPanel,"give coordinates of the cell", JOptionPane.OK_CANCEL_OPTION);
				    if (result == JOptionPane.OK_OPTION) {
				    	Xd_t= xField.getText();
				        Yd_t= yField.getText();
				        
				        String Xd[]=Xd_t.split(",");
				        for(int l=0;l<Xd.length;l++){
				        	i.add(Integer.parseInt(Xd[l]));
				        }
				        String Yd[]=Yd_t.split(",");
				        for(int l=0;l<Yd.length;l++){
				        	j.add(Integer.parseInt(Yd[l]));
				        }
				    }
				
				   try{
				   String index=Workbook.getSheetList().get(tabcount).getValue(i.get(0), j.get(0));
				   String index1=Workbook.getSheetList().get(tabcount).getValue(i.get(1), j.get(1));
				   index=index.toLowerCase();
				   index1=index1.toLowerCase();
				   double ascii;
				   double ascii1;
				   int ItIsAString=0;
		           
		            if(!index.equals("") && !index1.equals("")){
		            	try {
		            		
		            	     ascii  = Double.parseDouble(index);
		            	     
		            	} catch (NumberFormatException ex) {
		            	
		            			ItIsAString++;	

		            	}
		            	try {
		            		
		            	     ascii1  = Double.parseDouble(index1);
		            	     
		            	} catch (NumberFormatException ex) {
		            	
		            			ItIsAString++;	

		            	}
		            	if(ItIsAString<1){
						   Workbook.getSheetList().get(tabcount).createFunctionCell(type,clickrow1,clickcolumn1,index);
						   Workbook.getSheetList().get(tabcount).TableModelListenerDemo(type,tables.get(tabcount),clickrow1, clickcolumn1);
		            	}
		            	else{
		            		JOptionPane.showMessageDialog(null,"Give a number value");
		            	}
		            }
				   }catch(NullPointerException e)
			        {
					   JOptionPane.showMessageDialog(null,"Give a non empty cell");
			        }
				}catch(Exception e){
					
				}
			}
		});
		menu_3.add(menuItem_11);
		
		JMenuItem menuItem_12 = new JMenuItem("Log");
		menuItem_12.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					type=2;
					clickcolumn1=Workbook.getSheetList().get(tabcount).getclickColumn();
					clickrow1=Workbook.getSheetList().get(tabcount).getclickRow();
					/*new code */
					int i,j;
					i=0;
					j=0;
					//Spreadsheet m = new  Spreadsheet();
					//JOptionPane.showMessageDialog(null, "select cell");
					//System
					JTextField xField = new JTextField(5);
				    JTextField yField = new JTextField(5);
				    //tables(ta);
				    JPanel myPanel = new JPanel();
				    myPanel.add(new JLabel("x:"));
				    myPanel.add(xField);
				    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
				    myPanel.add(new JLabel("y:"));
				    myPanel.add(yField);
				    String Xd_t=null;
				    String Yd_t=null;
				    int result = JOptionPane.showConfirmDialog(null, myPanel,"give coordinates of the cell", JOptionPane.OK_CANCEL_OPTION);
				    if (result == JOptionPane.OK_OPTION) {
				    	Xd_t= xField.getText();
				        Yd_t= yField.getText();
				        i=Integer.parseInt(Xd_t);
				        j=Integer.parseInt(Yd_t);
				    }
				
				   try{
				   String index=Workbook.getSheetList().get(tabcount).getValue(i, j);
				   index=index.toLowerCase();
				   double ascii;
				   int ItIsAString=0;
		           
		            if(!index.equals("")){
		            	try {
		            		
		            	     ascii  = Double.parseDouble(index);
		            	     
		            	} catch (NumberFormatException ex) {
		            	
		            			ItIsAString++;	

		            	}
		            	if(ItIsAString<1){
						   Workbook.getSheetList().get(tabcount).createFunctionCell(type,clickrow1,clickcolumn1,index);
						   Workbook.getSheetList().get(tabcount).TableModelListenerDemo(type,tables.get(tabcount),clickrow1, clickcolumn1);
		            	}
		            	else{
		            		JOptionPane.showMessageDialog(null,"Give a number value");
		            	}
		            }
				   }catch(NullPointerException e)
			        {
					   JOptionPane.showMessageDialog(null,"Give a non empty cell");
			        }
				}catch(Exception e){
					
				}
			}
		});
		menu_3.add(menuItem_12);
		
		JMenuItem menuItem_13 = new JMenuItem("Log10");
		menuItem_13.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					type=3;
					clickcolumn1=Workbook.getSheetList().get(tabcount).getclickColumn();
					clickrow1=Workbook.getSheetList().get(tabcount).getclickRow();
					/*new code */
					int i,j;
					i=0;
					j=0;
					//Spreadsheet m = new  Spreadsheet();
					//JOptionPane.showMessageDialog(null, "select cell");
					//System
					JTextField xField = new JTextField(5);
				    JTextField yField = new JTextField(5);
				    //tables(ta);
				    JPanel myPanel = new JPanel();
				    myPanel.add(new JLabel("x:"));
				    myPanel.add(xField);
				    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
				    myPanel.add(new JLabel("y:"));
				    myPanel.add(yField);
				    String Xd_t=null;
				    String Yd_t=null;
				    int result = JOptionPane.showConfirmDialog(null, myPanel,"give coordinates of the cell", JOptionPane.OK_CANCEL_OPTION);
				    if (result == JOptionPane.OK_OPTION) {
				    	Xd_t= xField.getText();
				        Yd_t= yField.getText();
				        i=Integer.parseInt(Xd_t);
				        j=Integer.parseInt(Yd_t);
				    }
				
				   try{
				   String index=Workbook.getSheetList().get(tabcount).getValue(i, j);
				   index=index.toLowerCase();
				   double ascii;
				   int ItIsAString=0;
		           
		            if(!index.equals("")){
		            	try {
		            		
		            	     ascii  = Double.parseDouble(index);
		            	     
		            	} catch (NumberFormatException ex) {
		            	
		            			ItIsAString++;	

		            	}
		            	if(ItIsAString<1){
						   Workbook.getSheetList().get(tabcount).createFunctionCell(type,clickrow1,clickcolumn1,index);
						   Workbook.getSheetList().get(tabcount).TableModelListenerDemo(type,tables.get(tabcount),clickrow1, clickcolumn1);
		            	}
		            	else{
		            		JOptionPane.showMessageDialog(null,"Give a number value");
		            	}
		            }
				   }catch(NullPointerException e)
			        {
					   JOptionPane.showMessageDialog(null,"Give a non empty cell");
			        }
				}catch(Exception e){
					
				}
			}
		});
		menu_3.add(menuItem_13);
		
		JMenu menu_4 = new JMenu("Alphanumeric");
		menu_4.setIcon(new ImageIcon(this.getClass().getResource("/Images/font.png")));
		menu_2.add(menu_4);
		
		JMenuItem menuItem_14 = new JMenuItem("Concat");
		menuItem_14.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					type=16;
					clickcolumn1=Workbook.getSheetList().get(tabcount).getclickColumn();
					clickrow1=Workbook.getSheetList().get(tabcount).getclickRow();
					/*new code */
					ArrayList<Integer> i = new ArrayList<Integer>();
					ArrayList<Integer> j = new ArrayList<Integer>();
					
					//Spreadsheet m = new  Spreadsheet();
					//JOptionPane.showMessageDialog(null, "select cell");
					//System
					JTextField xField = new JTextField(5);
				    JTextField yField = new JTextField(5);
				    //tables(ta);
				    JPanel myPanel = new JPanel();
				    myPanel.add(new JLabel("x:"));
				    myPanel.add(xField);
				    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
				    myPanel.add(new JLabel("y:"));
				    myPanel.add(yField);
				    String Xd_t=null;
				    String Yd_t=null;
				    int result = JOptionPane.showConfirmDialog(null, myPanel,"give coordinates of the cell", JOptionPane.OK_CANCEL_OPTION);
				    if (result == JOptionPane.OK_OPTION) {
				    	Xd_t= xField.getText();
				        Yd_t= yField.getText();
				        
				        String Xd[]=Xd_t.split(",");
				        for(int l=0;l<Xd.length;l++){
				        	i.add(Integer.parseInt(Xd[l]));
				        }
				        String Yd[]=Yd_t.split(",");
				        for(int l=0;l<Yd.length;l++){
				        	j.add(Integer.parseInt(Yd[l]));
				        }
				    }
				
				   try{
				   String index=Workbook.getSheetList().get(tabcount).getValue(i.get(0), j.get(0));
				   String index1=Workbook.getSheetList().get(tabcount).getValue(i.get(1), j.get(1));
				   Workbook.getSheetList().get(tabcount).createFunctionCell(type,clickrow1,clickcolumn1,index,index1);
				   Workbook.getSheetList().get(tabcount).TableModelListenerDemo(type,tables.get(tabcount),clickrow1, clickcolumn1);
				   }catch(NullPointerException e)
			        {
					   JOptionPane.showMessageDialog(null,"Give a non empty cell");
			        }
				}catch(Exception e){
					
				}
			}
		});
		menu_4.add(menuItem_14);
		
		JMenuItem menuItem_15 = new JMenuItem("Includes");
		menuItem_15.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					type=17;
					clickcolumn1=Workbook.getSheetList().get(tabcount).getclickColumn();
					clickrow1=Workbook.getSheetList().get(tabcount).getclickRow();
					/*new code */
					ArrayList<Integer> i = new ArrayList<Integer>();
					ArrayList<Integer> j = new ArrayList<Integer>();
					
					//Spreadsheet m = new  Spreadsheet();
					//JOptionPane.showMessageDialog(null, "select cell");
					//System
					JTextField xField = new JTextField(5);
				    JTextField yField = new JTextField(5);
				    //tables(ta);
				    JPanel myPanel = new JPanel();
				    myPanel.add(new JLabel("x:"));
				    myPanel.add(xField);
				    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
				    myPanel.add(new JLabel("y:"));
				    myPanel.add(yField);
				    String Xd_t=null;
				    String Yd_t=null;
				    int result = JOptionPane.showConfirmDialog(null, myPanel,"give coordinates of the cell", JOptionPane.OK_CANCEL_OPTION);
				    if (result == JOptionPane.OK_OPTION) {
				    	Xd_t= xField.getText();
				        Yd_t= yField.getText();
				        
				        String Xd[]=Xd_t.split(",");
				        for(int l=0;l<Xd.length;l++){
				        	i.add(Integer.parseInt(Xd[l]));
				        }
				        String Yd[]=Yd_t.split(",");
				        for(int l=0;l<Yd.length;l++){
				        	j.add(Integer.parseInt(Yd[l]));
				        }
				    }
				
				  try{
				   String index=Workbook.getSheetList().get(tabcount).getValue(i.get(0), j.get(0));
				   String index1=Workbook.getSheetList().get(tabcount).getValue(i.get(1), j.get(1));
				   Workbook.getSheetList().get(tabcount).createFunctionCell(type,clickrow1,clickcolumn1,index,index1);
				   Workbook.getSheetList().get(tabcount).TableModelListenerDemo(type,tables.get(tabcount),clickrow1, clickcolumn1);
				}catch(NullPointerException e)
		        {
				   JOptionPane.showMessageDialog(null,"Give a non empty cell");
		        }
				}catch(Exception e){
					
				}
			}
		});
		menu_4.add(menuItem_15);
		
		JMenuItem menuItem_16 = new JMenuItem("Trim");
		menuItem_16.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					type=5;
					clickcolumn1=Workbook.getSheetList().get(tabcount).getclickColumn();
					clickrow1=Workbook.getSheetList().get(tabcount).getclickRow();
					/*new code */
					int i,j;
					i=0;
					j=0;
					//Spreadsheet m = new  Spreadsheet();
					//JOptionPane.showMessageDialog(null, "select cell");
					//System
					JTextField xField = new JTextField(5);
				    JTextField yField = new JTextField(5);
				    //tables(ta);
				    JPanel myPanel = new JPanel();
				    myPanel.add(new JLabel("x:"));
				    myPanel.add(xField);
				    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
				    myPanel.add(new JLabel("y:"));
				    myPanel.add(yField);
				    String Xd_t=null;
				    String Yd_t=null;
				    int result = JOptionPane.showConfirmDialog(null, myPanel,"give coordinates of the cell", JOptionPane.OK_CANCEL_OPTION);
				    if (result == JOptionPane.OK_OPTION) {
				    	Xd_t= xField.getText();
				        Yd_t= yField.getText();
				        i=Integer.parseInt(Xd_t);
				        j=Integer.parseInt(Yd_t);
				    }
				
				   try{
					   String index=Workbook.getSheetList().get(tabcount).getValue(i, j);
					   Workbook.getSheetList().get(tabcount).createFunctionCell(type,clickrow1,clickcolumn1,index);
					   Workbook.getSheetList().get(tabcount).TableModelListenerDemo(type,tables.get(tabcount),clickrow1, clickcolumn1);
				   }catch(NullPointerException e)
			        {
					   JOptionPane.showMessageDialog(null,"Give a non empty cell");
			        }
				}catch(Exception e){
					
				}
			}
		});
		menu_4.add(menuItem_16);
		
		JMenuItem menuItem_17 = new JMenuItem("Remove");
		menuItem_17.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					type=15;
					clickcolumn1=Workbook.getSheetList().get(tabcount).getclickColumn();
					clickrow1=Workbook.getSheetList().get(tabcount).getclickRow();
					/*new code */
					ArrayList<Integer> i = new ArrayList<Integer>();
					ArrayList<Integer> j = new ArrayList<Integer>();
					
					//Spreadsheet m = new  Spreadsheet();
					//JOptionPane.showMessageDialog(null, "select cell");
					//System
					JTextField xField = new JTextField(5);
				    JTextField yField = new JTextField(5);
				    //tables(ta);
				    JPanel myPanel = new JPanel();
				    myPanel.add(new JLabel("x:"));
				    myPanel.add(xField);
				    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
				    myPanel.add(new JLabel("y:"));
				    myPanel.add(yField);
				    String Xd_t=null;
				    String Yd_t=null;
				    int result = JOptionPane.showConfirmDialog(null, myPanel,"give coordinates of the cell", JOptionPane.OK_CANCEL_OPTION);
				    if (result == JOptionPane.OK_OPTION) {
				    	Xd_t= xField.getText();
				        Yd_t= yField.getText();
				        
				        String Xd[]=Xd_t.split(",");
				        for(int l=0;l<Xd.length;l++){
				        	i.add(Integer.parseInt(Xd[l]));
				        }
				        String Yd[]=Yd_t.split(",");
				        for(int l=0;l<Yd.length;l++){
				        	j.add(Integer.parseInt(Yd[l]));
				        }
				    }
				
				   try{
					   String index=Workbook.getSheetList().get(tabcount).getValue(i.get(0), j.get(0));
					   String index1=Workbook.getSheetList().get(tabcount).getValue(i.get(1), j.get(1));
					   Workbook.getSheetList().get(tabcount).createFunctionCell(type,clickrow1,clickcolumn1,index,index1);
					   Workbook.getSheetList().get(tabcount).TableModelListenerDemo(type,tables.get(tabcount),clickrow1, clickcolumn1);
				   }catch(NullPointerException e)
			        {
					   JOptionPane.showMessageDialog(null,"Give a non empty cell");
			        }
				}catch(Exception e){
					
				}
			}
		});
		menu_4.add(menuItem_17);
		
		JMenu menu_5 = new JMenu("Logical");
		menu_5.setIcon(new ImageIcon(this.getClass().getResource("/Images/question.png")));
		menu_2.add(menu_5);
		
		JMenuItem menuItem_18 = new JMenuItem("AND");
		menuItem_18.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					type=12;
					clickcolumn1=Workbook.getSheetList().get(tabcount).getclickColumn();
					clickrow1=Workbook.getSheetList().get(tabcount).getclickRow();
					/*new code */
					ArrayList<Integer> i = new ArrayList<Integer>();
					ArrayList<Integer> j = new ArrayList<Integer>();
					
					//Spreadsheet m = new  Spreadsheet();
					//JOptionPane.showMessageDialog(null, "select cell");
					//System
					JTextField xField = new JTextField(5);
				    JTextField yField = new JTextField(5);
				    //tables(ta);
				    JPanel myPanel = new JPanel();
				    myPanel.add(new JLabel("x:"));
				    myPanel.add(xField);
				    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
				    myPanel.add(new JLabel("y:"));
				    myPanel.add(yField);
				    String Xd_t=null;
				    String Yd_t=null;
				    int result = JOptionPane.showConfirmDialog(null, myPanel,"give coordinates of the cell", JOptionPane.OK_CANCEL_OPTION);
				    if (result == JOptionPane.OK_OPTION) {
				    	Xd_t= xField.getText();
				        Yd_t= yField.getText();
				        
				        String Xd[]=Xd_t.split(",");
				        for(int l=0;l<Xd.length;l++){
				        	i.add(Integer.parseInt(Xd[l]));
				        }
				        String Yd[]=Yd_t.split(",");
				        for(int l=0;l<Yd.length;l++){
				        	j.add(Integer.parseInt(Yd[l]));
				        }
				    }
				
				   try{
					   String index=Workbook.getSheetList().get(tabcount).getValue(i.get(0), j.get(0));
					   String index1=Workbook.getSheetList().get(tabcount).getValue(i.get(1), j.get(1));
					   index=index.toLowerCase();
					   index1=index1.toLowerCase();
					   int itsBoolean=0;
			            if(index.equals("true") ){
			           	 
			            }else if(index.equals("false")){
			           	 
			            }else{
			            	itsBoolean++;
			            }
			            if(index1.equals("true")){
				           	 
			            }else if(index1.equals("false")){
			           	 
			            }else{
			            	itsBoolean++;
			            }
			            if(itsBoolean==0){
						   Workbook.getSheetList().get(tabcount).createFunctionCell(type,clickrow1,clickcolumn1,index,index1);
						   Workbook.getSheetList().get(tabcount).TableModelListenerDemo(type,tables.get(tabcount),clickrow1, clickcolumn1);
			            }else{
			            	JOptionPane.showMessageDialog(null,"Give a Boolean value");
			            }
				   }catch(NullPointerException e)
			        {
					   JOptionPane.showMessageDialog(null,"Give a non empty cell");
			        }
				}catch(Exception e){
					
				}
			}
		});
		menu_5.add(menuItem_18);
		
		JMenuItem menuItem_19 = new JMenuItem("OR");
		menuItem_19.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					type=13;
					clickcolumn1=Workbook.getSheetList().get(tabcount).getclickColumn();
					clickrow1=Workbook.getSheetList().get(tabcount).getclickRow();
					/*new code */
					ArrayList<Integer> i = new ArrayList<Integer>();
					ArrayList<Integer> j = new ArrayList<Integer>();
					
					//Spreadsheet m = new  Spreadsheet();
					//JOptionPane.showMessageDialog(null, "select cell");
					//System
					JTextField xField = new JTextField(5);
				    JTextField yField = new JTextField(5);
				    //tables(ta);
				    JPanel myPanel = new JPanel();
				    myPanel.add(new JLabel("x:"));
				    myPanel.add(xField);
				    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
				    myPanel.add(new JLabel("y:"));
				    myPanel.add(yField);
				    String Xd_t=null;
				    String Yd_t=null;
				    int result = JOptionPane.showConfirmDialog(null, myPanel,"give coordinates of the cell", JOptionPane.OK_CANCEL_OPTION);
				    if (result == JOptionPane.OK_OPTION) {
				    	Xd_t= xField.getText();
				        Yd_t= yField.getText();
				        
				        String Xd[]=Xd_t.split(",");
				        for(int l=0;l<Xd.length;l++){
				        	i.add(Integer.parseInt(Xd[l]));
				        }
				        String Yd[]=Yd_t.split(",");
				        for(int l=0;l<Yd.length;l++){
				        	j.add(Integer.parseInt(Yd[l]));
				        }
				    }
				
				   
				    try{
						   String index=Workbook.getSheetList().get(tabcount).getValue(i.get(0), j.get(0));
						   String index1=Workbook.getSheetList().get(tabcount).getValue(i.get(1), j.get(1));
						   index=index.toLowerCase();
						   index1=index1.toLowerCase();
						   int itsBoolean=0;
				            if(index.equals("true") ){
				           	 
				            }else if(index.equals("false")){
				           	 
				            }else{
				            	itsBoolean++;
				            }
				            if(index1.equals("true")){
					           	 
				            }else if(index1.equals("false")){
				           	 
				            }else{
				            	itsBoolean++;
				            }
				            if(itsBoolean==0){
							   Workbook.getSheetList().get(tabcount).createFunctionCell(type,clickrow1,clickcolumn1,index,index1);
							   Workbook.getSheetList().get(tabcount).TableModelListenerDemo(type,tables.get(tabcount),clickrow1, clickcolumn1);
				            }else{
				            	JOptionPane.showMessageDialog(null,"Give a Boolean value");
				            }
					   }catch(NullPointerException e)
				        {
						   JOptionPane.showMessageDialog(null,"Give a non empty cell");
				        }
				}catch(Exception e){
					
				}
			}
		});
		menu_5.add(menuItem_19);
		
		JMenuItem menuItem_20 = new JMenuItem("XOR");
		menuItem_20.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					type=14;
					clickcolumn1=Workbook.getSheetList().get(tabcount).getclickColumn();
					clickrow1=Workbook.getSheetList().get(tabcount).getclickRow();
					/*new code */
					ArrayList<Integer> i = new ArrayList<Integer>();
					ArrayList<Integer> j = new ArrayList<Integer>();
					
					//Spreadsheet m = new  Spreadsheet();
					//JOptionPane.showMessageDialog(null, "select cell");
					//System
					JTextField xField = new JTextField(5);
				    JTextField yField = new JTextField(5);
				    //tables(ta);
				    JPanel myPanel = new JPanel();
				    myPanel.add(new JLabel("x:"));
				    myPanel.add(xField);
				    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
				    myPanel.add(new JLabel("y:"));
				    myPanel.add(yField);
				    String Xd_t=null;
				    String Yd_t=null;
				    int result = JOptionPane.showConfirmDialog(null, myPanel,"give coordinates of the cell", JOptionPane.OK_CANCEL_OPTION);
				    if (result == JOptionPane.OK_OPTION) {
				    	Xd_t= xField.getText();
				        Yd_t= yField.getText();
				        
				        String Xd[]=Xd_t.split(",");
				        for(int l=0;l<Xd.length;l++){
				        	i.add(Integer.parseInt(Xd[l]));
				        }
				        String Yd[]=Yd_t.split(",");
				        for(int l=0;l<Yd.length;l++){
				        	j.add(Integer.parseInt(Yd[l]));
				        }
				    }
				
				   
				    try{
						   String index=Workbook.getSheetList().get(tabcount).getValue(i.get(0), j.get(0));
						   String index1=Workbook.getSheetList().get(tabcount).getValue(i.get(1), j.get(1));
						   index=index.toLowerCase();
						   index1=index1.toLowerCase();
						   int itsBoolean=0;
				            if(index.equals("true") ){
				           	 
				            }else if(index.equals("false")){
				           	 
				            }else{
				            	itsBoolean++;
				            }
				            if(index1.equals("true")){
					           	 
				            }else if(index1.equals("false")){
				           	 
				            }else{
				            	itsBoolean++;
				            }
				            if(itsBoolean==0){
							   Workbook.getSheetList().get(tabcount).createFunctionCell(type,clickrow1,clickcolumn1,index,index1);
							   Workbook.getSheetList().get(tabcount).TableModelListenerDemo(type,tables.get(tabcount),clickrow1, clickcolumn1);
				            }else{
				            	JOptionPane.showMessageDialog(null,"Give a Boolean value");
				            }
					   }catch(NullPointerException e)
				        {
						   JOptionPane.showMessageDialog(null,"Give a non empty cell");
				        }
				}catch(Exception e){
					
				}
			}
		});
		menu_5.add(menuItem_20);
		
		JMenuItem menuItem_21 = new JMenuItem("NOT");
		menuItem_21.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					type=4;
					clickcolumn1=Workbook.getSheetList().get(tabcount).getclickColumn();
					clickrow1=Workbook.getSheetList().get(tabcount).getclickRow();
					/*new code */
					int i,j;
					i=0;
					j=0;
					//Spreadsheet m = new  Spreadsheet();
					//JOptionPane.showMessageDialog(null, "select cell");
					//System
					JTextField xField = new JTextField(5);
				    JTextField yField = new JTextField(5);
				    //tables(ta);
				    JPanel myPanel = new JPanel();
				    myPanel.add(new JLabel("x:"));
				    myPanel.add(xField);
				    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
				    myPanel.add(new JLabel("y:"));
				    myPanel.add(yField);
				    String Xd_t=null;
				    String Yd_t=null;
				    int result = JOptionPane.showConfirmDialog(null, myPanel,"give coordinates of the cell", JOptionPane.OK_CANCEL_OPTION);
				    if (result == JOptionPane.OK_OPTION) {
				    	Xd_t= xField.getText();
				        Yd_t= yField.getText();
				        i=Integer.parseInt(Xd_t);
				        j=Integer.parseInt(Yd_t);
				    }
				
				    try{
						   String index=Workbook.getSheetList().get(tabcount).getValue(i, j);
						   
						   index=index.toLowerCase();
						   
						   int itsBoolean=0;
				            if(index.equals("true") ){
				           	 
				            }else if(index.equals("false")){
				           	 
				            }else{
				            	itsBoolean++;
				            }
				            
				            if(itsBoolean==0){
				            	Workbook.getSheetList().get(tabcount).createFunctionCell(type,clickrow1,clickcolumn1,index);
								   Workbook.getSheetList().get(tabcount).TableModelListenerDemo(type,tables.get(tabcount),clickrow1, clickcolumn1);
				            }else{
				            	JOptionPane.showMessageDialog(null,"Give a Boolean value");
				            }
					   }catch(NullPointerException e)
				        {
						   JOptionPane.showMessageDialog(null,"Give a non empty cell");
				        }
				  
				}catch(Exception e){
					
				}
			}
		});
		menu_5.add(menuItem_21);
		
		JMenu menu_6 = new JMenu("Statistical");
		menu_6.setIcon(new ImageIcon(this.getClass().getResource("/Images/Chart xy.png")));
		menu_2.add(menu_6);
		
		JMenuItem menuItem_22 = new JMenuItem("Max");
		menuItem_22.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					type=18;
					clickcolumn1=Workbook.getSheetList().get(tabcount).getclickColumn();
					clickrow1=Workbook.getSheetList().get(tabcount).getclickRow();
					/*new code */
					ArrayList<Integer> i = new ArrayList<Integer>();
					ArrayList<Integer> j = new ArrayList<Integer>();
					
					//Spreadsheet m = new  Spreadsheet();
					//JOptionPane.showMessageDialog(null, "select cell");
					//System
					JTextField xField = new JTextField(5);
				    JTextField yField = new JTextField(5);
				    //tables(ta);
				    JPanel myPanel = new JPanel();
				    myPanel.add(new JLabel("x:"));
				    myPanel.add(xField);
				    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
				    myPanel.add(new JLabel("y:"));
				    myPanel.add(yField);
				    String Xd_t=null;
				    String Yd_t=null;
				    int result = JOptionPane.showConfirmDialog(null, myPanel,"give coordinates of the cell", JOptionPane.OK_CANCEL_OPTION);
				    if (result == JOptionPane.OK_OPTION) {
				    	Xd_t= xField.getText();
				        Yd_t= yField.getText();
				        
				        String Xd[]=Xd_t.split(",");
				        for(int l=0;l<Xd.length;l++){
				        	i.add(Integer.parseInt(Xd[l]));
				        }
				        String Yd[]=Yd_t.split(",");
				        for(int l=0;l<Yd.length;l++){
				        	j.add(Integer.parseInt(Yd[l]));
				        }
				    }
				    int notInt=0;
				    int isNull=0;
				    ArrayList<String> index = new ArrayList<String>();
				    for(int l=0;l<j.size();l++){
				    	try{
				    		index.add(Workbook.getSheetList().get(tabcount).getValue(i.get(l), j.get(l)));
				    	}catch(NullPointerException e)
				        {
				    		isNull=1;
						   JOptionPane.showMessageDialog(null,"Give a non empty cell");
				        }
				    }
				    if(isNull==0){
					    for(int l=0;l<j.size();l++){
					    	index.get(l);
					    	int ItIsAString=0;
					    	double ascii;
						    if(!index.get(l).equals("")){
				            	try {
				            		
				            	     ascii  = Double.parseDouble(index.get(l));
				            	     
				            	} catch (NumberFormatException ex) {
				            	
				            			ItIsAString++;	
		
				            	}
				            	if(ItIsAString<1){
				            		
				            	}else{
				            		notInt=1;
				            	}
						    }
					    }
					   //String index=book.getSheetList().get(ta).getValue(i.get(0), j.get(0));
					   //String index1=book.getSheetList().get(ta).getValue(i.get(1), j.get(1));
					   if(notInt==0){
						   Workbook.getSheetList().get(tabcount).createFunctionCell(type,clickrow1,clickcolumn1,index);
						   Workbook.getSheetList().get(tabcount).TableModelListenerDemo(type,tables.get(tabcount),clickrow1, clickcolumn1);
					   }else{
						   JOptionPane.showMessageDialog(null,"Give only number values");
					   }
				    }
				}catch(Exception e){
					
				}
			}
		});
		menu_6.add(menuItem_22);
		
		JMenuItem menuItem_23 = new JMenuItem("Min");
		menuItem_23.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					type=19;
					clickcolumn1=Workbook.getSheetList().get(tabcount).getclickColumn();
					clickrow1=Workbook.getSheetList().get(tabcount).getclickRow();
					/*new code */
					ArrayList<Integer> i = new ArrayList<Integer>();
					ArrayList<Integer> j = new ArrayList<Integer>();
					
					//Spreadsheet m = new  Spreadsheet();
					//JOptionPane.showMessageDialog(null, "select cell");
					//System
					JTextField xField = new JTextField(5);
				    JTextField yField = new JTextField(5);
				    //tables(ta);
				    JPanel myPanel = new JPanel();
				    myPanel.add(new JLabel("x:"));
				    myPanel.add(xField);
				    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
				    myPanel.add(new JLabel("y:"));
				    myPanel.add(yField);
				    String Xd_t=null;
				    String Yd_t=null;
				    int result = JOptionPane.showConfirmDialog(null, myPanel,"give coordinates of the cell", JOptionPane.OK_CANCEL_OPTION);
				    if (result == JOptionPane.OK_OPTION) {
				    	Xd_t= xField.getText();
				        Yd_t= yField.getText();
				        
				        String Xd[]=Xd_t.split(",");
				        for(int l=0;l<Xd.length;l++){
				        	i.add(Integer.parseInt(Xd[l]));
				        }
				        String Yd[]=Yd_t.split(",");
				        for(int l=0;l<Yd.length;l++){
				        	j.add(Integer.parseInt(Yd[l]));
				        }
				    }
				
				    int notInt=0;
				    int isNull=0;
				    ArrayList<String> index = new ArrayList<String>();
				    for(int l=0;l<j.size();l++){
				    	try{
				    		index.add(Workbook.getSheetList().get(tabcount).getValue(i.get(l), j.get(l)));
				    	}catch(NullPointerException e)
				        {
				    		isNull=1;
						   JOptionPane.showMessageDialog(null,"Give a non empty cell");
				        }
				    }
				    if(isNull==0){
					    for(int l=0;l<j.size();l++){
					    	index.get(l);
					    	int ItIsAString=0;
					    	double ascii;
						    if(!index.get(l).equals("")){
				            	try {
				            		
				            	     ascii  = Double.parseDouble(index.get(l));
				            	     
				            	} catch (NumberFormatException ex) {
				            	
				            			ItIsAString++;	
		
				            	}
				            	if(ItIsAString<1){
				            		
				            	}else{
				            		notInt=1;
				            	}
						    }
					    }
					   //String index=book.getSheetList().get(ta).getValue(i.get(0), j.get(0));
					   //String index1=book.getSheetList().get(ta).getValue(i.get(1), j.get(1));
					   if(notInt==0){
						   Workbook.getSheetList().get(tabcount).createFunctionCell(type,clickrow1,clickcolumn1,index);
						   Workbook.getSheetList().get(tabcount).TableModelListenerDemo(type,tables.get(tabcount),clickrow1, clickcolumn1);
					   }else{
						   JOptionPane.showMessageDialog(null,"Give only number values");
					   }
				    }
				}catch(Exception e){
					
				}
			}
		});
		menu_6.add(menuItem_23);
		
		JMenuItem menuItem_24 = new JMenuItem("Mean");
		menuItem_24.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					type=20;
					clickcolumn1=Workbook.getSheetList().get(tabcount).getclickColumn();
					clickrow1=Workbook.getSheetList().get(tabcount).getclickRow();
					/*new code */
					ArrayList<Integer> i = new ArrayList<Integer>();
					ArrayList<Integer> j = new ArrayList<Integer>();
					
					//Spreadsheet m = new  Spreadsheet();
					//JOptionPane.showMessageDialog(null, "select cell");
					//System
					JTextField xField = new JTextField(5);
				    JTextField yField = new JTextField(5);
				    //tables(ta);
				    JPanel myPanel = new JPanel();
				    myPanel.add(new JLabel("x:"));
				    myPanel.add(xField);
				    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
				    myPanel.add(new JLabel("y:"));
				    myPanel.add(yField);
				    String Xd_t=null;
				    String Yd_t=null;
				    int result = JOptionPane.showConfirmDialog(null, myPanel,"give coordinates of the cell", JOptionPane.OK_CANCEL_OPTION);
				    if (result == JOptionPane.OK_OPTION) {
				    	Xd_t= xField.getText();
				        Yd_t= yField.getText();
				        
				        String Xd[]=Xd_t.split(",");
				        for(int l=0;l<Xd.length;l++){
				        	i.add(Integer.parseInt(Xd[l]));
				        }
				        String Yd[]=Yd_t.split(",");
				        for(int l=0;l<Yd.length;l++){
				        	j.add(Integer.parseInt(Yd[l]));
				        }
				    }
				
				    int notInt=0;
				    int isNull=0;
				    ArrayList<String> index = new ArrayList<String>();
				    for(int l=0;l<j.size();l++){
				    	try{
				    		index.add(Workbook.getSheetList().get(tabcount).getValue(i.get(l), j.get(l)));
				    	}catch(NullPointerException e)
				        {
				    		isNull=1;
						   JOptionPane.showMessageDialog(null,"Give a non empty cell");
				        }
				    }
				    if(isNull==0){
					    for(int l=0;l<j.size();l++){
					    	index.get(l);
					    	int ItIsAString=0;
					    	double ascii;
						    if(!index.get(l).equals("")){
				            	try {
				            		
				            	     ascii  = Double.parseDouble(index.get(l));
				            	     
				            	} catch (NumberFormatException ex) {
				            	
				            			ItIsAString++;	
		
				            	}
				            	if(ItIsAString<1){
				            		
				            	}else{
				            		notInt=1;
				            	}
						    }
					    }
					   //String index=book.getSheetList().get(ta).getValue(i.get(0), j.get(0));
					   //String index1=book.getSheetList().get(ta).getValue(i.get(1), j.get(1));
					   if(notInt==0){
						   Workbook.getSheetList().get(tabcount).createFunctionCell(type,clickrow1,clickcolumn1,index);
						   Workbook.getSheetList().get(tabcount).TableModelListenerDemo(type,tables.get(tabcount),clickrow1, clickcolumn1);
					   }else{
						   JOptionPane.showMessageDialog(null,"Give only number values");
					   }
				    }
				}catch(Exception e){
					
				}
			}
		});
		menu_6.add(menuItem_24);
		
		JMenuItem menuItem_25 = new JMenuItem("Median");
		menuItem_25.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					type=21;
					clickcolumn1=Workbook.getSheetList().get(tabcount).getclickColumn();
					clickrow1=Workbook.getSheetList().get(tabcount).getclickRow();
					/*new code */
					ArrayList<Integer> i = new ArrayList<Integer>();
					ArrayList<Integer> j = new ArrayList<Integer>();
					
					//Spreadsheet m = new  Spreadsheet();
					//JOptionPane.showMessageDialog(null, "select cell");
					//System
					JTextField xField = new JTextField(5);
				    JTextField yField = new JTextField(5);
				    //tables(ta);
				    JPanel myPanel = new JPanel();
				    myPanel.add(new JLabel("x:"));
				    myPanel.add(xField);
				    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
				    myPanel.add(new JLabel("y:"));
				    myPanel.add(yField);
				    String Xd_t=null;
				    String Yd_t=null;
				    int result = JOptionPane.showConfirmDialog(null, myPanel,"give coordinates of the cell", JOptionPane.OK_CANCEL_OPTION);
				    if (result == JOptionPane.OK_OPTION) {
				    	Xd_t= xField.getText();
				        Yd_t= yField.getText();
				        
				        String Xd[]=Xd_t.split(",");
				        for(int l=0;l<Xd.length;l++){
				        	i.add(Integer.parseInt(Xd[l]));
				        }
				        String Yd[]=Yd_t.split(",");
				        for(int l=0;l<Yd.length;l++){
				        	j.add(Integer.parseInt(Yd[l]));
				        }
				    }
				
				    int notInt=0;
				    int isNull=0;
				    ArrayList<String> index = new ArrayList<String>();
				    for(int l=0;l<j.size();l++){
				    	try{
				    		index.add(Workbook.getSheetList().get(tabcount).getValue(i.get(l), j.get(l)));
				    	}catch(NullPointerException e)
				        {
				    		isNull=1;
						   JOptionPane.showMessageDialog(null,"Give a non empty cell");
				        }
				    }
				    if(isNull==0){
					    for(int l=0;l<j.size();l++){
					    	index.get(l);
					    	int ItIsAString=0;
					    	double ascii;
						    if(!index.get(l).equals("")){
				            	try {
				            		
				            	     ascii  = Double.parseDouble(index.get(l));
				            	     
				            	} catch (NumberFormatException ex) {
				            	
				            			ItIsAString++;	
		
				            	}
				            	if(ItIsAString<1){
				            		
				            	}else{
				            		notInt=1;
				            	}
						    }
					    }
					   //String index=book.getSheetList().get(ta).getValue(i.get(0), j.get(0));
					   //String index1=book.getSheetList().get(ta).getValue(i.get(1), j.get(1));
					   if(notInt==0){
						   Workbook.getSheetList().get(tabcount).createFunctionCell(type,clickrow1,clickcolumn1,index);
						   Workbook.getSheetList().get(tabcount).TableModelListenerDemo(type,tables.get(tabcount),clickrow1, clickcolumn1);
					   }else{
						   JOptionPane.showMessageDialog(null,"Give only number values");
					   }
				    }
				}catch(Exception e){
					
				}
			}
		});
		menu_6.add(menuItem_25);
		
		JMenuItem menuItem_26 = new JMenuItem("StdDev");
		menuItem_26.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					type=22;
					clickcolumn1=Workbook.getSheetList().get(tabcount).getclickColumn();
					clickrow1=Workbook.getSheetList().get(tabcount).getclickRow();
					/*new code */
					ArrayList<Integer> i = new ArrayList<Integer>();
					ArrayList<Integer> j = new ArrayList<Integer>();
					
					//Spreadsheet m = new  Spreadsheet();
					//JOptionPane.showMessageDialog(null, "select cell");
					//System
					JTextField xField = new JTextField(5);
				    JTextField yField = new JTextField(5);
				    //tables(ta);
				    JPanel myPanel = new JPanel();
				    myPanel.add(new JLabel("x:"));
				    myPanel.add(xField);
				    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
				    myPanel.add(new JLabel("y:"));
				    myPanel.add(yField);
				    String Xd_t=null;
				    String Yd_t=null;
				    int result = JOptionPane.showConfirmDialog(null, myPanel,"give coordinates of the cell", JOptionPane.OK_CANCEL_OPTION);
				    if (result == JOptionPane.OK_OPTION) {
				    	Xd_t= xField.getText();
				        Yd_t= yField.getText();
				        
				        String Xd[]=Xd_t.split(",");
				        for(int l=0;l<Xd.length;l++){
				        	i.add(Integer.parseInt(Xd[l]));
				        }
				        String Yd[]=Yd_t.split(",");
				        for(int l=0;l<Yd.length;l++){
				        	j.add(Integer.parseInt(Yd[l]));
				        }
				    }
				
				    int notInt=0;
				    int isNull=0;
				    ArrayList<String> index = new ArrayList<String>();
				    for(int l=0;l<j.size();l++){
				    	try{
				    		index.add(Workbook.getSheetList().get(tabcount).getValue(i.get(l), j.get(l)));
				    	}catch(NullPointerException e)
				        {
				    		isNull=1;
						   JOptionPane.showMessageDialog(null,"Give a non empty cell");
				        }
				    }
				    if(isNull==0){
					    for(int l=0;l<j.size();l++){
					    	index.get(l);
					    	int ItIsAString=0;
					    	double ascii;
						    if(!index.get(l).equals("")){
				            	try {
				            		
				            	     ascii  = Double.parseDouble(index.get(l));
				            	     
				            	} catch (NumberFormatException ex) {
				            	
				            			ItIsAString++;	
		
				            	}
				            	if(ItIsAString<1){
				            		
				            	}else{
				            		notInt=1;
				            	}
						    }
					    }
					   //String index=book.getSheetList().get(ta).getValue(i.get(0), j.get(0));
					   //String index1=book.getSheetList().get(ta).getValue(i.get(1), j.get(1));
					   if(notInt==0){
						   Workbook.getSheetList().get(tabcount).createFunctionCell(type,clickrow1,clickcolumn1,index);
						   Workbook.getSheetList().get(tabcount).TableModelListenerDemo(type,tables.get(tabcount),clickrow1, clickcolumn1);
					   }else{
						   JOptionPane.showMessageDialog(null,"Give only number values");
					   }
				    }
				}catch(Exception e){
					
				}
			}
		});
		menu_6.add(menuItem_26);
		
		JMenu menu_7 = new JMenu("Graphs");
		menuBar.add(menu_7);
		
		JMenuItem menuItem_27 = new JMenuItem("");
		menuItem_27.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					type=23;
					clickcolumn1=Workbook.getSheetList().get(tabcount).getclickColumn();
					clickrow1=Workbook.getSheetList().get(tabcount).getclickRow();
					ArrayList<Integer> i = new ArrayList<Integer>();
					ArrayList<Integer> j = new ArrayList<Integer>();
					String ChartName;
					String Xdname;
					String Ydname;
					JTextField Chartname = new JTextField();
					JTextField Xname = new JTextField();
					JTextField Yname = new JTextField();
					Object[] message = {
						    "Bar-Chart Title:", Chartname,
						    "X dimension:", Xname,
						    "Y dimension:", Yname
						};					
					int option = JOptionPane.showConfirmDialog(null, message, "Give names for the Bar-Chart", JOptionPane.OK_CANCEL_OPTION);

					JTextField xField = new JTextField(10);
				    JTextField yField = new JTextField(10);
				    //tables(ta);
				    ChartName=Chartname.getText();
				    Xdname=Xname.getText();
				    Ydname=Yname.getText();
				    JPanel myPanel = new JPanel();
				    
				    myPanel.add(new JLabel("x:"));
				    myPanel.add(xField);
				    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
				    myPanel.add(new JLabel("y:"));
				    myPanel.add(yField);
				    String Xd_t=null;
				    String Yd_t=null;
				    int result = JOptionPane.showConfirmDialog(null, myPanel,"Give coordinates for the values of "+Xname.getText(), JOptionPane.OK_CANCEL_OPTION);
				    if (result == JOptionPane.OK_OPTION) {
				    	Xd_t= xField.getText();
				        Yd_t= yField.getText();
				        
				        String Xd[]=Xd_t.split(",");
				        for(int l=0;l<Xd.length;l++){
				        	i.add(Integer.parseInt(Xd[l]));
				        }
				        String Yd[]=Yd_t.split(",");
				        for(int l=0;l<Yd.length;l++){
				        	j.add(Integer.parseInt(Yd[l]));
				        }
				    }
				
				    ArrayList<String> indexXd = new ArrayList<String>();
				    for(int l=0;l<j.size();l++){
				    	indexXd.add(Workbook.getSheetList().get(tabcount).getValue(i.get(l), j.get(l)));
				    }
				    i = new ArrayList<Integer>();
					j = new ArrayList<Integer>();
				    JTextField xField1 = new JTextField(10);
				    JTextField yField1 = new JTextField(10);
				    //tables(ta);
				    JPanel myPanel1 = new JPanel();
				    
				    myPanel1.add(new JLabel("x:"));
				    myPanel1.add(xField1);
				    myPanel1.add(Box.createHorizontalStrut(15)); // a spacer
				    myPanel1.add(new JLabel("y:"));
				    myPanel1.add(yField1);
				    String Xd_t1=null;
				    String Yd_t1=null;
				    int result1 = JOptionPane.showConfirmDialog(null, myPanel1,"Give coordinates for the values of "+Yname.getText(), JOptionPane.OK_CANCEL_OPTION);
				    if (result1 == JOptionPane.OK_OPTION) {
				    	Xd_t1= xField1.getText();
				        Yd_t1= yField1.getText();
				        
				        String Xd1[]=Xd_t1.split(",");
				        for(int l=0;l<Xd1.length;l++){
				        	i.add(Integer.parseInt(Xd1[l]));
				        }
				        String Yd1[]=Yd_t1.split(",");
				        for(int l=0;l<Yd1.length;l++){
				        	j.add(Integer.parseInt(Yd1[l]));
				        }
				    }
				    ArrayList<String> indext = new ArrayList<String>();
				    ArrayList<String> indexYd = new ArrayList<String>();
				    for(int l=0;l<j.size();l++){
				    	indexYd.add(Workbook.getSheetList().get(tabcount).getValue(i.get(l), j.get(l)));
				    }
				    GraphGui Graphsheet=new GraphGui("MinusXl Graph Visualisation",ChartName,Xdname,Ydname,indexXd,indexYd,indext,type);
				    Graphsheet.pack( );        
				    RefineryUtilities.centerFrameOnScreen( Graphsheet );        
				    Graphsheet.setVisible( true ); 
				   //book.getSheetList().get(ta).createFunctionCell(type,clickrow1,clickcolumn1,index1);
				   //book.getSheetList().get(ta).TableModelListenerDemo(type,tables.get(ta),clickrow1, clickcolumn1);
				}catch(Exception e){
					
				}
			}
		});
		menuItem_27.setIcon(new ImageIcon(this.getClass().getResource("/Images/3d_bar_chart.png")));
		menu_7.add(menuItem_27);
		
		JMenuItem menuItem_28 = new JMenuItem("");
		menuItem_28.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					type=24;
					clickcolumn1=Workbook.getSheetList().get(tabcount).getclickColumn();
					clickrow1=Workbook.getSheetList().get(tabcount).getclickRow();
					ArrayList<Integer> i = new ArrayList<Integer>();
					ArrayList<Integer> j = new ArrayList<Integer>();
					String ChartName;
					String Xdname;
					String Ydname;
					JTextField Chartname = new JTextField();
					JTextField Xname = new JTextField();
					JTextField Yname = new JTextField();
					Object[] message = {
						    "Bar-Chart Title:", Chartname,
						    "X dimension:", Xname,
						    "Y dimension:", Yname
						};					
					int option = JOptionPane.showConfirmDialog(null, message, "Give names for the Bar-Chart", JOptionPane.OK_CANCEL_OPTION);

					JTextField xField = new JTextField(10);
				    JTextField yField = new JTextField(10);
				    //tables(ta);
				    ChartName=Chartname.getText();
				    Xdname=Xname.getText();
				    Ydname=Yname.getText();
				    JPanel myPanel = new JPanel();
				    
				    myPanel.add(new JLabel("x:"));
				    myPanel.add(xField);
				    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
				    myPanel.add(new JLabel("y:"));
				    myPanel.add(yField);
				    String Xd_t=null;
				    String Yd_t=null;
				    int result = JOptionPane.showConfirmDialog(null, myPanel,"Give coordinates for the values of "+Xname.getText(), JOptionPane.OK_CANCEL_OPTION);
				    if (result == JOptionPane.OK_OPTION) {
				    	Xd_t= xField.getText();
				        Yd_t= yField.getText();
				        
				        String Xd[]=Xd_t.split(",");
				        for(int l=0;l<Xd.length;l++){
				        	i.add(Integer.parseInt(Xd[l]));
				        }
				        String Yd[]=Yd_t.split(",");
				        for(int l=0;l<Yd.length;l++){
				        	j.add(Integer.parseInt(Yd[l]));
				        }
				    }
				
				    ArrayList<String> indexXd = new ArrayList<String>();
				    for(int l=0;l<j.size();l++){
				    	indexXd.add(Workbook.getSheetList().get(tabcount).getValue(i.get(l), j.get(l)));
				    }
				    i = new ArrayList<Integer>();
					j = new ArrayList<Integer>();
				    JTextField xField1 = new JTextField(10);
				    JTextField yField1 = new JTextField(10);
				    JPanel myPanel1 = new JPanel();
				    
				    myPanel1.add(new JLabel("x:"));
				    myPanel1.add(xField1);
				    myPanel1.add(Box.createHorizontalStrut(15)); // a spacer
				    myPanel1.add(new JLabel("y:"));
				    myPanel1.add(yField1);
				    String Xd_t1=null;
				    String Yd_t1=null;
				    int result1 = JOptionPane.showConfirmDialog(null, myPanel1,"Give coordinates for the values of "+Yname.getText(), JOptionPane.OK_CANCEL_OPTION);
				    if (result1 == JOptionPane.OK_OPTION) {
				    	Xd_t1= xField1.getText();
				        Yd_t1= yField1.getText();
				        
				        String Xd1[]=Xd_t1.split(",");
				        for(int l=0;l<Xd1.length;l++){
				        	i.add(Integer.parseInt(Xd1[l]));
				        }
				        String Yd1[]=Yd_t1.split(",");
				        for(int l=0;l<Yd1.length;l++){
				        	j.add(Integer.parseInt(Yd1[l]));
				        }
				    }
				    ArrayList<String> indexYd = new ArrayList<String>();
				    for(int l=0;l<j.size();l++){
				    	indexYd.add(Workbook.getSheetList().get(tabcount).getValue(i.get(l), j.get(l)));
				    }
				    i = new ArrayList<Integer>();
					j = new ArrayList<Integer>();
				    JTextField xField2 = new JTextField(5);
				    JTextField yField2 = new JTextField(5);
				 
				    JPanel myPanel2 = new JPanel();
				    
				    myPanel2.add(new JLabel("x:"));
				    myPanel2.add(xField2);
				    myPanel2.add(Box.createHorizontalStrut(15)); // a spacer
				    myPanel2.add(new JLabel("y:"));
				    myPanel2.add(yField2);
				    String Xd_t2=null;
				    String Yd_t2=null;
				    int result2 = JOptionPane.showConfirmDialog(null, myPanel2,"Give type for the previous number coordinates ", JOptionPane.OK_CANCEL_OPTION);
				    if (result2 == JOptionPane.OK_OPTION) {
				    	Xd_t2= xField2.getText();
				        Yd_t2= yField2.getText();
				        
				        String Xd2[]=Xd_t2.split(",");
				        for(int l=0;l<Xd2.length;l++){
				        	i.add(Integer.parseInt(Xd2[l]));
				        }
				        String Yd2[]=Yd_t2.split(",");
				        for(int l=0;l<Yd2.length;l++){
				        	j.add(Integer.parseInt(Yd2[l]));
				        }
				    }
				    ArrayList<String> indexType = new ArrayList<String>();
				    for(int l=0;l<j.size();l++){
				    	indexType.add(Workbook.getSheetList().get(tabcount).getValue(i.get(l), j.get(l)));
				    }
				    
				    GraphGui Graphsheet=new GraphGui("MinusXl Graph Visualisation",ChartName,Xdname,Ydname,indexXd,indexYd,indexType,type);
				    Graphsheet.pack( );        
				    RefineryUtilities.centerFrameOnScreen( Graphsheet );        
				    Graphsheet.setVisible( true );
				}catch(Exception e){
					
				}
			}
		});
		menuItem_28.setIcon(new ImageIcon(this.getClass().getResource("/Images/chart_line (1).png")));
		menu_7.add(menuItem_28);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setBounds(0, 21, rect.width, 41);
		contentPane.add(toolBar);
		
		JButton button = new JButton("");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					String saveAs;
					JTextField Savename = new JTextField();
					Object[] message = {
						    "Save file:", Savename
					};
					int option = JOptionPane.showConfirmDialog(null, message, "Save as", JOptionPane.OK_CANCEL_OPTION);
					if(option == JOptionPane.YES_OPTION){
						
						saveAs=(Savename.getText()+".csv");
						book.getSheetList().get(tabcount).saveFile(book.getSheetList(),tabcount,saveAs);
						
					}else{
						saveAs="Untitled.csv";
						book.getSheetList().get(tabcount).saveFile(book.getSheetList(),tabcount,saveAs);
					}
					
				}catch(Exception e){
					e.printStackTrace();
					
				}
			}
		});
		button.setIcon(new ImageIcon(this.getClass().getResource("/Images/document_save.png")));
		button.setToolTipText("save");
		toolBar.add(button);
		
		JButton button_1 = new JButton("");
		button_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				try{
					 
					if(FirstTime==0){
						JTextField xField = new JTextField(5);
					    JTextField yField = new JTextField(5);
	
					    JPanel myPanel = new JPanel();
					    myPanel.add(new JLabel("x:"));
					    myPanel.add(xField);
					    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
					    myPanel.add(new JLabel("y:"));
					    myPanel.add(yField);
					    String Xd_t=null;
					    String Yd_t=null;
					    int result = JOptionPane.showConfirmDialog(null, myPanel,"Please Enter X and Y Values", JOptionPane.OK_CANCEL_OPTION);
					    if (result == JOptionPane.OK_OPTION) {
					        Xd_t= xField.getText();
					        Yd_t= yField.getText();
					        
					        if(Xd_t.equals("")){
					        	Xd=5;
					        }else{
					        	Xd=Integer.parseInt(Xd_t); 
					        }
					        if(Yd_t.equals("")){
					        	Yd=5;
					        }else{
					        	Yd=Integer.parseInt(Yd_t);
					        }
					       
					    }
					  
					    
					    String[] col=new String[Yd];
					    
					    book.createSpreadsheet(Xd, Yd);
					    
					    JTable table = new JTable();
					    
					    tables.add(table);
					    for(int i=0;i<Yd;i++){
					    	String temp=Integer.toString(i);
					    	
					    	col[i]=temp;
					    	
					    }
					    
					    tables.get(FirstTime).setModel(new DefaultTableModel(Workbook.getSheetList().get(FirstTime).getGrid(), col));
					    tables.get(FirstTime).setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
					    tables.get(FirstTime).setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
					    tables.get(FirstTime).setCellSelectionEnabled(true);
					    

					      
					    Workbook.getSheetList().get(FirstTime).TableModelListenerDemo(tables.get(FirstTime));
					    JScrollPane scrollPane=new JScrollPane(tables.get(FirstTime));
					    String name="Spreadsheet"+(FirstTime+1);
					    
					    tab.addTab(name, scrollPane);
					    LineNumberTableRowHeader tableLinenumber=new LineNumberTableRowHeader(scrollPane,tables.get(FirstTime));
						tableLinenumber.setBackground(Color.LIGHT_GRAY);
						scrollPane.setRowHeaderView(tableLinenumber);
					    FirstTime=FirstTime+1;
						
						
					}
					
					else{
						Xd=5;
					    Yd=5;
						
						JTextField xField = new JTextField(5);
					    JTextField yField = new JTextField(5);
	
					    JPanel myPanel = new JPanel();
					    myPanel.add(new JLabel("x:"));
					    myPanel.add(xField);
					    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
					    myPanel.add(new JLabel("y:"));
					    myPanel.add(yField);
					    String Xd_t=null;
					    String Yd_t=null;
					    int result = JOptionPane.showConfirmDialog(null, myPanel,"Please Enter X and Y Values", JOptionPane.OK_CANCEL_OPTION);
					    if (result == JOptionPane.OK_OPTION) {
					    	Xd_t= xField.getText();
					        Yd_t= yField.getText();
					        
					        if(Xd_t.equals("")){
					        	Xd=5;
					        }else{
					        	Xd=Integer.parseInt(Xd_t); 
					        }
					        if(Yd_t.equals("")){
					        	Yd=5;
					        }else{
					        	Yd=Integer.parseInt(Yd_t);
					        }
				   }
					    JTable table = new JTable();
					    String[] col=new String[Yd];
					    
					    book.createSpreadsheet(Xd, Yd);
					  
					    tables.add(table);
					    for(int i=0;i<Yd;i++){
					    	col[i]=Integer.toString(i);
					    }
					    tables.get(FirstTime).setModel(new DefaultTableModel(Workbook.getSheetList().get(FirstTime).getGrid(), col));
					   
					    Workbook.getSheetList().get(FirstTime).TableModelListenerDemo(tables.get(FirstTime));
					    
					    JScrollPane scrollPane=new JScrollPane(tables.get(FirstTime));
					    String name="Spreadsheet"+(FirstTime+1);
					    
					    tab.addTab(name, scrollPane);
					    LineNumberTableRowHeader tableLinenumber=new LineNumberTableRowHeader(scrollPane,tables.get(FirstTime));
						tableLinenumber.setBackground(Color.LIGHT_GRAY);
						scrollPane.setRowHeaderView(tableLinenumber);
						FirstTime=FirstTime+1;
					}
					        
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
		});
		button_1.setIcon(new ImageIcon(this.getClass().getResource("/Images/Document Spreadsheet.png")));
		button_1.setToolTipText("New Spreadsheet");
		toolBar.add(button_1);
		
		JButton button_3 = new JButton("");
		button_3.addActionListener(new ActionListener() {
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
						//Workbook.dispose();
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
		
		button_3.setIcon(new ImageIcon(this.getClass().getResource("/Images/be-net-server-icon.png")));
		button_3.setToolTipText("Server");
		toolBar.add(button_3);
		
		JButton button_4 = new JButton("");
		button_4.addActionListener(new ActionListener() {
			@Override
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
		button_4.setIcon(new ImageIcon(this.getClass().getResource("/Images/cowboy (1).png")));
		button_4.setSelectedIcon(new ImageIcon(this.getClass().getResource("/Images/question.png")));
		toolBar.add(button_4);
		//table= new JTable();
		tab = new JTabbedPane(SwingConstants.TOP);
		tab.setBounds(10, 64, rect.width, rect.height);
		contentPane.add(tab);
	
	}
}
