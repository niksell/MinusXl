
public class BooleanCell extends Cell{
	private String value;
	private boolean trueValue;
	int row;
	int column;
	BooleanCell(String value,int row,int column){
		this.value=value;
		this.row=row;
		this.column=column;
		System.out.println("the value is boolean"+value);
	}
	
	@Override
	public String getValue(){
		
		return(this.value);
	}
}
