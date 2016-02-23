import java.util.ArrayList;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class GraphFunction {
	   public CategoryDataset createDataset(ArrayList<String> index1,ArrayList<String> index2 ,int type)
	   {
	            
	      final DefaultCategoryDataset dataset = 
	      new DefaultCategoryDataset( );  
	      if(type==23){
		      for(int i=0;i<index1.size();i++){
		    	  
		    	  dataset.addValue(Double.parseDouble(index2.get(i)),index1.get(i),Double.toString(i));
		    	  
		      }
	      }else if(type==24){
	    	  	for(int i=0;i<index1.size();i++){
		    	  
		    	  dataset.addValue(Double.parseDouble(index2.get(i)),"1",index1.get(i));
		    	  
		      }
	      }

	      return dataset; 
	   }
	   public  CategoryDataset createDataset(ArrayList<String> index1,ArrayList<String> index2,ArrayList<String> index3 ,int type) {
		     
		   final DefaultCategoryDataset dataset = new DefaultCategoryDataset( );  
		   for(int i=0;i<index3.size();i++){

		    	  
		    	  dataset.addValue(Double.parseDouble(index2.get(i)),index3.get(i),index1.get(i));
		    	  
		      }
	                
	        return dataset;
	        
	    }
}
