import java.util.ArrayList;
import java.util.*;
public class Function{
	
	
	public double calculateAbs(String a){
		System.out.println(a);
		double temp=Double.parseDouble(a);
		if(temp<0){
			temp=-temp;
		}
		
		return temp;
		
	}
	
	public double calculatePow(String a,String b){
		double temp1=Double.parseDouble(a);
		double temp2=Double.parseDouble(b);
		double temp;
		
			temp=Math.pow(temp1, temp2);
			
		return(temp);
	}
	
	public double calculateSin(String a){
		double temp=0.0;
		double temp1=Double.parseDouble(a);
		if((temp1<361) ||(temp1 >=0 ) ){
			temp=Math.sin(temp1);
		}
		return(temp);
	}
	
	public double calculateCos(String a){
		double temp=0.0;
		double temp1=Double.parseDouble(a);
		if((temp1<361) ||(temp1 >=0 ) ){
			temp=Math.cos(temp1);
		}
		return(temp);
	}	
	
	public double calculateTan(String a){
		double temp=0.0;
		double temp1=Double.parseDouble(a);
		if((temp1<361) ||(temp1 >=0 ) ){
			temp=Math.tan(temp1);
		}
		return(temp);
	}
	
	public double calculateSum(String a,String b){
		double temp;
		double temp1=Double.parseDouble(a);
		double temp2=Double.parseDouble(b);
		temp=temp1+temp2;
		return(temp);
	}
	
	public double calculateMult(String a,String b){
		double temp;
		double temp1=Double.parseDouble(a);
		double temp2=Double.parseDouble(b);
		temp=temp1*temp2;
		return(temp);
	}
	
	public double calculateLog10(String a){
		double temp;
		double temp1=Double.parseDouble(a);
		temp=Math.log10(temp1);
		return(temp);
	}
	
	public double calculateLog(String a){
		double temp;
		double temp1=Double.parseDouble(a);
		temp=Math.log(temp1);
		return(temp);
	}
	
	
	public boolean calculateAnd(String a,String b){
		boolean temp=false;
		boolean temp1=Boolean.parseBoolean(a);
		boolean temp2=Boolean.parseBoolean(b);
		if(temp1 && temp2){
			temp=true;
			
		}
		return(temp);
	}
	
	public boolean calculateOr(String a,String b){
		boolean temp=false;
		boolean temp1=Boolean.parseBoolean(a);
		boolean temp2=Boolean.parseBoolean(b);
		if(temp1 || temp2){
			temp=true;
			
		}
		return(temp);
	}
	
	public boolean calculateXor(String a,String b){
		boolean temp=false;
		boolean temp1=Boolean.parseBoolean(a);
		boolean temp2=Boolean.parseBoolean(b);
		if(temp1 ^ temp2){
			temp=true;
			
		}
		return(temp);
	}
	
	
	public boolean calculateNot(String a){
		boolean temp1=Boolean.parseBoolean(a);
		return(!temp1);
	}
	
	
	public double calculateMax(ArrayList<String> a){
		double temp;
		double temp2=Double.parseDouble(a.get(0));;
		for(int i=1;i<a.size();i++){
			temp=Double.parseDouble(a.get(i));
			
			if(temp>temp2){
				temp2=temp;
				
			}
		}
		return(temp2);
	}
	
	public double calculateMin(ArrayList<String> a){
		double temp;
		double temp2=Double.parseDouble(a.get(0));;
		for(int i=1;i<a.size();i++){
			temp=Double.parseDouble(a.get(i));
			if(temp<temp2){
				temp2=temp;
			}
		
		}
		return(temp2);
	}
	
	public String calculateConcat(String a,String b){
		String temp;
		temp=a+b;
		
		return(temp);
		
	}
	
	public boolean calculateIncludes(String a,String b){
		boolean temp=false;
		if(a.indexOf(b) >= 0){
			temp=true;
		}
		return(temp);
		
	}
	
	public String calculateTrim(String a){
		String temp;
		temp=a.replaceAll("\\s","");
		return(temp);
		
	}
	
	public String calculateRemove(String a,String b){
		String temp;
		temp = a.replace(b,"");
		return(temp);
		
	}
	
	
	public double calculateMean(ArrayList<String> a){
		double sum=0;
		double temp=0;
		for(int i=0;i<a.size();i++){
			temp=Double.parseDouble(a.get(i));
			sum+=temp;
		
		}
		sum=sum/a.size();
		return(sum);
	}
	
	public double calculateMedian(ArrayList<String> a){
		double temp=0,temp1=0;
		int temp2=0,temp3=0;
		
			Collections.sort(a);
			if(a.size() %2!=0){
				temp2=(a.size()+1)/2;
				temp2-=1;
				temp=Double.parseDouble(a.get(temp2));
			}else{
				temp2=a.size()/2;
				temp2++;
				temp3=a.size()/2;
				temp=Double.parseDouble(a.get(temp3-1));
				temp1=Double.parseDouble(a.get(temp2-1));
				temp=(temp+temp1)/2;
			}
		return(temp);
	}
	public double calculateStd(ArrayList<String> a){
		double sum=0;
		double temp=0,temps=0,temp4=0,std=0;
		
		for(int i=0;i<a.size();i++){
			temp=Double.parseDouble(a.get(i));
			sum+=temp;
		
		}
		sum=sum/a.size();
		double sumStd=0;
		for(int i=0;i<a.size();i++){
			temp4=Double.parseDouble(a.get(i))-sum;
			temps=temps+Math.pow(temp4, 2);
		}
		std=temps/(a.size()-1);
		return std;
	}
}



	