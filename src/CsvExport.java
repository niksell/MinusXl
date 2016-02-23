import java.util.*;

import javax.swing.JOptionPane;

import java.io.*;
public class CsvExport {
	PrintWriter outputstream=null;
	private int i,j;
	public void process(ArrayList<Spreadsheet> sheetList,int number,String saveAs){
			try{
				outputstream=new PrintWriter (new FileOutputStream("C:\\Users\\george\\Documents\\"+saveAs));
			}catch(FileNotFoundException e){
	
				System.out.println("error opening file");
				
			}
			
			for(i=0;i<sheetList.get(number).getRows();i++){
				for(j=0;j<sheetList.get(number).getColumns();j++){
					
					if(sheetList.get(number).getCellvalue(i, j)!="empty"){
						outputstream.print(sheetList.get(number).getCellvalue(i, j)+";");
					}
					else{
					
						outputstream.print(" ;");
					}
				}
				
				outputstream.println();
			}
			outputstream.flush();
			outputstream.close();
	}
}