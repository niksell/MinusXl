import org.jfree.ui.ApplicationFrame;

import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart; 
import org.jfree.chart.plot.PlotOrientation;


public class GraphGui extends ApplicationFrame {
	public GraphGui( String applicationTitle , String chartTitle,String Xname,String Yname,ArrayList<String> index1,ArrayList<String> index2,ArrayList<String> indextype,int type )
	   {
	      super( applicationTitle );    
	      GraphFunction Graph=new GraphFunction();
	      if(type==23){
	    	  
		      JFreeChart barChart = ChartFactory.createBarChart3D(
		         chartTitle,           
		         Xname,            
		         Yname,            
		         Graph.createDataset(index1,index2,type),          
		         PlotOrientation.VERTICAL,           
		         true, true, false);
		         
		      ChartPanel chartPanel = new ChartPanel( barChart );        
		      chartPanel.setPreferredSize(new java.awt.Dimension( 560 , 367 ) );        
		      setContentPane( chartPanel ); 
	      }else if(type==24){
	    	 
		      JFreeChart barChart = ChartFactory.createLineChart(
		         chartTitle,           
		         Xname,            
		         Yname,            
		         Graph.createDataset(index1,index2,indextype,type),          
		         PlotOrientation.VERTICAL,           
		         true, true, false);
		         
		      ChartPanel chartPanel = new ChartPanel( barChart );        
		      chartPanel.setPreferredSize(new java.awt.Dimension( 560 , 367 ) );        
		      setContentPane( chartPanel ); 
	      }
	   }

}
