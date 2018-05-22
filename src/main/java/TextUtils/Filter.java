package TextUtils;

public class Filter {

	private String input;
	
	public Filter(String input)
	{
		this.input = input.toLowerCase();
	}
	
	public String getNumber()
	{
		String modinput = this.input;
		modinput = modinput.replaceAll("[^\\d.]", "");
		
		return modinput;
	}
	
	
	
}
