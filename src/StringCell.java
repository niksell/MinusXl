
public class StringCell extends Cell {
	private String value;
	private int row,column;
	public StringCell(String value,int row,int column){
		this.value=value;
		this.row=row;
		this.column=column;
	}
	
	@Override
	public String getValue(){
		return(this.value);
	}
}
