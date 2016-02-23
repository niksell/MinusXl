
public class NumberCell extends Cell{
	
	private String value;
	private double trueValue;
	private int row,column;
	NumberCell(String value,int row,int column){
		this.value=value;
		this.row=row;
		this.column=column;
	
	}
	
	@Override
	public String getValue(){
		
		return(this.value);
	}
}
