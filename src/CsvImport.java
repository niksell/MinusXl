import java.util.*;

import javax.swing.JOptionPane;

import java.io.*;

public class CsvImport {
	Scanner inputstream=null;
	private int N=0;
	private int max=0;
	private String fileSheets[][];
	//String FileSheet[][];
	public String[][] process(String sheetName){
		try{
			
			inputstream=new Scanner(new FileInputStream("C:\\Users\\george\\Documents\\"+sheetName));
		}catch(FileNotFoundException e){

			System.out.println("error opening file");
			JOptionPane.showMessageDialog(null,"error opening file");
		}
		String line=null;
		
	
		int k=0;
		
		while (inputstream.hasNextLine()){
			line=inputstream.nextLine();
			String s1[]=line.split(";");
			
			k=0;
			for(int i=0;i<s1.length;i++){
				k++;
			}
			if(k>max){
				max=k;
			}
			N++;
		}
		inputstream.close();
		try{
			
			inputstream=new Scanner(new FileInputStream("C:\\Users\\george\\Documents\\"+sheetName));
		}catch(FileNotFoundException e){

			System.out.println("error opening file");
		}		
		fileSheets = new String[N][max];
		
		line=null;
		;
		N=0;
		while (inputstream.hasNextLine()){
			line=inputstream.nextLine();
			String s1[]=line.split(";");
			
			
			for(int i=0;i<s1.length;i++){
				
				fileSheets[N][i]=s1[i];
				
			}
			N++;
		}
		inputstream.close();
		return(fileSheets);
	}
	
	public int getRowSize(){
		return(N);
	}
	
	public int getColumnSize(){
		return(max);
	}
}