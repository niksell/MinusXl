import java.sql.Connection;
import java.util.ArrayList;

public class Workbook {
	private int Xd=5;
    private int Yd=5;
    private int FirstTime=0;
    private static int ta=0;
    private Connection connection=null;
	
    private static ArrayList<Spreadsheet> SheetList=new ArrayList<Spreadsheet>() ;
    
		
		
		public void createSpreadsheet(int x,int y){
			Spreadsheet Sheet= new Spreadsheet(x,y);
			SheetList.add(Sheet);
			
			
		}
		
	
		
		public static ArrayList<Spreadsheet> getSheetList(){
			return(SheetList);
		}
		
		public int f1(int x,int y ){
			int t=0;
			
			
			return t;
		}
		
}