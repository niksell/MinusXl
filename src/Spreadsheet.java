import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;



public class Spreadsheet {
	private int row,column,clickrow=0,clickcolumn=0;
	private int Xd=5;
    private int Yd=5;
    private int FirstTime=0;
    
    private Cell SheetGrid [][] ;
    
    public Spreadsheet(){
    	SheetGrid=new Cell[Xd][Yd];
    }
    public Spreadsheet(int Xd,int Yd){
    	this.Xd=Xd;
    	this.Yd=Yd;
    	SheetGrid=new Cell[Xd][Yd];
    }

    public void TableModelListenerDemo(int type,JTable table,int x,int y) {
  	  table.getModel().setValueAt( getValue(x,y), x, y);
    	table.getModel().addTableModelListener(new TableModelListener() {
    	  @Override
		public void tableChanged(TableModelEvent e) {
    	   
            TableModel model = (TableModel)e.getSource();
            String columnName = model.getColumnName(column);
            
    	    String s1;
            s1= getValue(x,y);
            s1=s1.toLowerCase();
            
    	  }
    	});
    	
        	

    	}
    
    
    public void TableModelListenerDemo(String FilesheetValue,JTable table,int row,int column) {
   
    	table.getModel().setValueAt( FilesheetValue,row, column);
    	String s1;
        s1=FilesheetValue;
        s1=s1.toLowerCase();
        if(s1.equals("true")){
          	 SheetGrid[row][column]=new BooleanCell(s1,row,column);
           }else if(s1.equals("false")){
          	 SheetGrid[row][column]=new BooleanCell(s1,row,column);
           }else{
           	double ascii;
	         	int ItIsAString=0;
	           
	            if(!s1.equals("")){
	            	try {
	            		
	            	     ascii  = Double.parseDouble(s1);
	            	     
	            	} catch (NumberFormatException ex) {
	            	
	            			ItIsAString++;	

	            	}
	            	 
	        
	             
	            	if(ItIsAString<1){
	            		SheetGrid[row][column]=new NumberCell(s1,row,column); 
	            	}else{
	            		SheetGrid[row][column]=new StringCell(s1,row,column);
	            		ItIsAString=0;
	            	}
	            	
	            	
	            }
	            if(FilesheetValue.isEmpty()){       
	                
	            	SheetGrid[row][column]=new StringCell("",row,column);
            		ItIsAString=0;
	            }
           }  
        
      	
      	
          	

      	}
    
    
    public void TableModelListenerDemo(JTable table) {
    	  
    	table.getModel().addTableModelListener(new TableModelListener() {
    	  @Override
		public void tableChanged(TableModelEvent e) {
    	    int firstRow = e.getFirstRow();
    	    int lastRow = e.getLastRow();
    	    int index = e.getColumn();
    	    int row = e.getFirstRow();
            int column = e.getColumn();
            TableModel model = (TableModel)e.getSource();
            String columnName = model.getColumnName(column);
            
    	    String s1;
            s1=(String) (table.getValueAt(row,column));
            s1=s1.toLowerCase();
           
            if(s1.equals("true")){
           	 SheetGrid[row][column]=new BooleanCell(s1,row,column);
            }else if(s1.equals("false")){
           	 SheetGrid[row][column]=new BooleanCell(s1,row,column);
            }else{
            	double ascii;
	         	int ItIsAString=0;
	           
	            if(!s1.equals("")){
	            	try {
	            		
	            	     ascii  = Double.parseDouble(s1);
	            	     
	            	} catch (NumberFormatException ex) {
	            	
	            			ItIsAString++;	

	            	}
	            	 
	        
	             
	            	if(ItIsAString<1){
	            		SheetGrid[row][column]=new NumberCell(s1,row,column); 
	            	}else{
	            		SheetGrid[row][column]=new StringCell(s1,row,column);
	            		ItIsAString=0;
	            	}
	            	
	            	
	            }
            }
    	    
    	    switch (e.getType()) {
    	    case TableModelEvent.INSERT:
    	      for (int i = firstRow; i <= lastRow; i++) {
    	        
    	      }
    	      break;
    	    case TableModelEvent.UPDATE:
    	      if (firstRow == TableModelEvent.HEADER_ROW) {
    	        if (index == TableModelEvent.ALL_COLUMNS) {
    	       
    	        } else {
    	          
    	        }
    	      } else {
    	        for (int i = firstRow; i <= lastRow; i++) {
    	          if (index == TableModelEvent.ALL_COLUMNS) {
    	           
    	          } else {
    	           
    	          }
    	        }
    	      }
    	      break;
    	    case TableModelEvent.DELETE:
    	      for (int i = firstRow; i <= lastRow; i++) {
    	     
    	      }
    	      break;
    	    }
    	  }
    	});
    	table.addMouseListener(new java.awt.event.MouseAdapter() {

        	  @Override
			public void mousePressed(java.awt.event.MouseEvent e) {
        		  clickrow = table.rowAtPoint( e.getPoint() );
        		  clickcolumn = table.columnAtPoint( e.getPoint() );
        		 
        	  }
        	});

    	}


    
    public Cell[][] getGrid(){
    	return this.SheetGrid ;
    }
    
    public int getRow(){
    	return(row);
    }
    
    public int getColumn(){
    	return(column);
    }
    
    
    
    public int getclickRow(){
    	return(clickrow);
    }
    
    public int getclickColumn(){
    	return(clickcolumn);
    }
    
    public void createFunctionCell(int type,int i,int j,String index){
    	SheetGrid[i][j]=new FunctionCell(type,index,i,j);
    }
    public void createFunctionCell(int type,int i,int j,String index,String index1){
    	SheetGrid[i][j]=new FunctionCell(type,index,index1,i,j);
    }
    public void createFunctionCell(int type,int i,int j,ArrayList<String> index){
    	SheetGrid[i][j]=new FunctionCell(type,index,i,j);
    }
    public String getValue(int i,int j){
    	
    		return(SheetGrid[i][j].getValue());
    	
    }
    public String getCellvalue(int i,int j){
    	if(SheetGrid[i][j]!=null){
    		return(SheetGrid[i][j].getValue());
    	}else{
    		return("empty");
    	}
    }
    
    public int getRows(){
    	return(this.Xd);
    }
    
    public int getColumns(){
    	return(this.Yd);
    }
    
    public void saveFile(ArrayList<Spreadsheet> sheetList,int number,String saveAs){
    	CsvExport csv=new CsvExport();
    	csv.process(sheetList, number,saveAs);
    }
    
   
}