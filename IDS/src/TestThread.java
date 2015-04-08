public class TestThread extends Thread {
	
	private String code = "";
  
	public String getCode() {
		return code;
	}

	public TestThread(String name){
		super(name);
	}
	
	public void run(){
	    //for(int i = 0; i < 10; i++)
	      //System.out.println(this.getName());
		  System.out.println("début lecture nfc");
		  System.out.println(this.getName()); //A
		  TerminalNFC.lecture();
		  System.out.println("fin lecture nfc");
	  }       
}