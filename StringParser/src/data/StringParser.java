package data;

public class StringParser {
	
	private String street;
	private String housenumber;
	final String SPACE = " ";
		
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getHousenumber() {
		return housenumber;
	}

	public void setHousenumber(String housenumber) {
		this.housenumber = housenumber;
	}

	public String splitAndJoin(String elem, String[] arr, int begIndex, int endIndex){
		if(arr.length < begIndex || arr.length < endIndex){
			return "";
		}
		String temp = arr[begIndex];
		for(int i=begIndex+1; i<endIndex;i++ ){
			temp = temp + SPACE + arr[i];
		}
		return temp;
	}

	public String parse(String str){
		String[] arr = str.split(" |,");
		int[] indexs = this.extractSplitIndex(arr);
		this.setStreet(this.splitAndJoin(SPACE, arr, indexs[2], indexs[3]));
		this.setHousenumber(this.splitAndJoin(SPACE, arr, indexs[0], indexs[1]));
		return this.toString();
	}
	
	public int[] extractSplitIndex(String[] arr){
		boolean streetFlag = false;
		boolean houseNoFlag = false;
		int[] indexs = new int[4];
		for(int i=0; i<arr.length; i++){
			if (arr[i].equalsIgnoreCase("no")){
				houseNoFlag = true;
				indexs[0] = i;
			}
			else if(arr[i].matches(".*\\d+.*") &&  !houseNoFlag){
				houseNoFlag = true;
				indexs[0] = i;
			}else if(!streetFlag){
				indexs[2] = i;
				streetFlag = true;
			}
		}
		if(indexs[0] < indexs[2]){
			indexs[1] = indexs[2];
			indexs[3] = arr.length;
		}
		else{
			indexs[1] = arr.length;
			indexs[3] = indexs[0];
		}
		return indexs;
	}
	
	
	@Override
	public String toString() {
		return "{\"street\": \""+this.getStreet()+"\", \"housenumber\": \""+this.getHousenumber()+"\"}";
	}
	
	public static void main(String[] args) {
		StringParser parser = new StringParser();
		
		String[] inputs =  { "sample strings "};

		for(String input: inputs ){
			System.out.println("String "+input +" is parse to "+parser.parse(input));
		}

	}

}
