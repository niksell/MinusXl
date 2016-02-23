import java.util.ArrayList;

class FunctionCell extends Cell{
	private String value,value1;
	private double truevalue;
	private boolean trueboolean;
	private int row,column;
	
	FunctionCell(int type,String index,int i,int j){
		this.value=index;
		this.row=i;
		this.column=j;
		Function f=new Function();
		if(type==1){
			truevalue=f.calculateAbs(this.value);
			this.value=Double.toString(truevalue);
		}else if(type==2){
			
			truevalue=f.calculateLog(this.value);
			this.value=Double.toString(truevalue);
		}else if(type==3){
			
			truevalue=f.calculateLog10(this.value);
			this.value=Double.toString(truevalue);
		}else if(type==4){
			
			trueboolean=f.calculateNot(this.value);
			this.value=Boolean.toString(trueboolean);
		}else if(type==5){
			
			this.value=f.calculateTrim(this.value);
			
		}else if(type==6){
			
			truevalue=f.calculateSin(this.value);
			this.value=Double.toString(truevalue);
			
		}else if(type==7){
			
			truevalue=f.calculateCos(this.value);
			this.value=Double.toString(truevalue);
			
		}else if(type==8){
			
			truevalue=f.calculateCos(this.value);
			this.value=Double.toString(truevalue);
			
		}
		
	}
	FunctionCell(int type,String index,String index1,int i,int j){
		this.value=index;
		this.value1=index1;
		this.row=i;
		this.column=j;
		Function f=new Function();
		if(type==9){
			truevalue=f.calculatePow(this.value,this.value1);
			this.value=Double.toString(truevalue);			
		}else if(type==10){
			truevalue=f.calculateSum(this.value,this.value1);
			this.value=Double.toString(truevalue);			
		}else if(type==11){
			truevalue=f.calculateMult(this.value,this.value1);
			this.value=Double.toString(truevalue);			
		}else if(type==12){
			trueboolean=f.calculateAnd(this.value,this.value1);
			this.value=Boolean.toString(trueboolean);			
		}else if(type==13){
			trueboolean=f.calculateOr(this.value,this.value1);
			this.value=Boolean.toString(trueboolean);			
		}else if(type==14){
			trueboolean=f.calculateXor(this.value,this.value1);
			this.value=Boolean.toString(trueboolean);			
		}else if(type==15){
			this.value=f.calculateRemove(this.value,this.value1);
						
		}else if(type==16){
			this.value=f.calculateConcat(this.value,this.value1);
						
		}else if(type==17){
			trueboolean=f.calculateIncludes(this.value,this.value1);
			this.value=Boolean.toString(trueboolean);
		}
	}
	FunctionCell(int type,ArrayList<String> index,int i,int j){
		Function f=new Function();
		if(type==18){
			truevalue=f.calculateMax( index);
			this.value=Double.toString(truevalue);		
		}else if(type==19){
			truevalue=f.calculateMin( index);
			this.value=Double.toString(truevalue);		
		}else if(type==20){
			truevalue=f.calculateMean( index);
			this.value=Double.toString(truevalue);		
		}else if(type==21){
			truevalue=f.calculateMedian( index);
			this.value=Double.toString(truevalue);		
		}else if(type==22){
			truevalue=f.calculateStd( index);
			this.value=Double.toString(truevalue);		
		}
	}
	@Override
	public String getValue(){
		return(this.value);
	}
}
