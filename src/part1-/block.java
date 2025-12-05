package part1;

public class block {
    
    private int startAddress;
    private int size;
    private String status;
    private int P_num;
    
    public block(){
        this.startAddress = -1;
        this.status = "";
        this.size = 0;
        this.P_num = -1;
    }
    
    
    public block(int startAddress, int size, String status, int P_num){
        this.startAddress = startAddress;
        this.status = status;
        this.size = size;
        this.P_num = P_num;
    }
    
    public block(int startAddress, int size, String status){
        this.startAddress = startAddress;
        this.status = status;
        this.size = size;
        this.P_num = -1;
    }
        
    public block(int size, String status, int P_num){
        this.startAddress = -1;
        this.status = status;
        this.size = size;
        this.P_num = P_num;
    }
    
    public void setStartAddress(int startAddress){
        this.startAddress = startAddress;
    }
    
    public void setStatus(String status){
        this.status = status;
    }
    
    public int getStartAddress(){
        return this.startAddress;
    }
    
    public String getStatus(){
        return this.status;
    }
    
    public void setSize(int size){
        this.size = size;
    }
    
    public int getSize(){
        return this.size;
    }
    
    public void setP_num(int P_num){
        this.P_num = P_num;
    }
    
    public int getP_num(){
        return this.P_num;
    }
    
    public void increaseSize(int additionalSize){
        this.size += additionalSize;
    }
      
}
