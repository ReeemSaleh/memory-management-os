/* Authors: 
- Reem Saleh Saeed Almalki
- Asail Mashhour Alamoudi
- Shahad Maher Magram
- Seham Khaldoun Nahlawi
*/

// Compiler: Apache NetBeans IDE 16

// Hardware Configuration: 
// Processor: Intel(R) Core(TM) i7-10510U CPU @ 1.80GHz   2.30 GHz
// RAM: 16.0 GB (15.8 GB usable)
// System Type: 64-bit operating system, x64-based processor

// Operating System: Windows 11 Home Insider Preview

package part1;

import java.util.Scanner;
import java.util.ArrayList;

public class mainMemorySimulator {
    
    private int memorySize;
    private ArrayList<block> blocks;
    
    public mainMemorySimulator(){
        
    }
    
    public void setMemorySize(int memorySize){
        this.memorySize = memorySize;
        this.blocks = new ArrayList<>();
        this.blocks.add(new block(0, memorySize, "Unused"));
    }
    

//------------------------------------------------------------------------------------------------------------------------------------  
    
          
    // create object of mainMemorySimulator with specific space
    static mainMemorySimulator MS = new mainMemorySimulator();


    public static void main(String[] args) {
          
        Scanner scan = new Scanner(System.in); 
        Scanner input = new Scanner(System.in); 

        
        
        // print menu
        System.out.println("""
                           Welcome to Memory Management Program
                           
                           ----------------------------------------------------------------------------------------------------------------------
                           command menu: 
                           
                           RQ   -------> Request for a contiguous block of memory.
                           \t      Flag:
                           \t      F-first fit
                           \t      B-best fit
                           \t      W-worst fit
                           Ex: RQ P0 40000 W, The first parameter to the RQ command is the new process that requires the memory, followed by the 
                           amount of memory being requested, and finally the strategy. (In this situation, "W" refers to worst fit.)
                           
                           RL   -------> Release of a contiguous block of memory.
                           Ex: RL P0, This command will release the memory that has been allocated to process P0.
                           
                           C    -------> Compact unused holes of memory into one single block.
                           STAT -------> Report the regions of free and allocated memory.
                           X    -------> Exit.
                           
                           ---------------------------------------------------------------------------------------------------------------------- 
                           
                           """);
        
        
        // take memory size from user in unit format
        
        System.out.print("Enter the memory size (MAX) in unit format for example 1 MB\n(Note: your memory size will be in range from 0 ... MAX - 1): ");
        
        String[] memorySize = scan.nextLine().toUpperCase().split(" ");
        int mSize = Integer.parseInt(memorySize[0]);
        String unit = memorySize[1];
        
        
        // convert unit to bytes
        if (unit.equalsIgnoreCase("KB"))
            mSize *= 1024;
        else if (unit.equalsIgnoreCase("MB"))
            mSize *= Math.pow(1024, 2);
        else if (unit.equalsIgnoreCase("GB"))
            mSize *= Math.pow(1024, 3);    
                
        MS.setMemorySize(mSize);
        

        System.out.println("\n\n./allocator " + MS.memorySize + "\n");
        
        while (true){
            

            System.out.print("allocator> ");
            // take a statement from user and split the statment
            String[] statement = input.nextLine().toUpperCase().split(" ");
            String command = statement[0];
            // define varibles
            String flag;
            int P_num, size;
            
            
            

            switch (command) {
                
                case "RQ" -> {
                    // fill out the varibles
                    P_num = Integer.parseInt(statement[1].replace("P", ""));
                    size =  Integer.parseInt(statement[2]);
                    flag = statement[3];
                    // if user enter a wrong statement structure will notify 
                    if (statement.length != 4 || !(flag.equalsIgnoreCase("F") || !flag.equalsIgnoreCase("B") || !flag.equalsIgnoreCase("W"))){
                        System.err.println("You write a wrong statment. try again");
                        continue;
                    }
                    // call function
                    MS.requestBlock(P_num, size, flag);
                }
                    
                case "RL" -> {
                    // fill out the varibles
                    P_num = Integer.parseInt(statement[1].replace("P", ""));
                    // if user enter a wrong statement structure will notify 
                    if (statement.length != 2){
                        System.err.println("You write a wrong statment. try again");
                        continue;
                    }
                    // call function
                    MS.releaseBlock(P_num);
                    // combine the block released with the adjacent hole into a single hole.
                    MS.combineholes();
                }
                    
                case "C" -> MS.compact();
                    
                case "STAT" -> {
                    // pritn the memory status
                    System.out.println("");
                    MS.stat();
                    System.out.println("");
                }
                    
                case "X" -> {
                    // close the program
                    System.out.println("-----------------------------------------------------------------\nThank you for using this program :)");
                    scan.close();
                    input.close();
                    System.exit(0);
                }
                
                // if the command is entered wrong     
                default -> System.err.println("The entered command is incorrect");
 
            }
         
        }        
    }
    
//------------------------------------------------------------------------------------------------------------------------------------ 
    
    private void requestBlock(int P_num, int size, String flag){
        
        block New = new block(size, " Process P"+P_num , P_num);
        
        int indx = -1;
        if (flag.equalsIgnoreCase("B")){ // Best Fit
            
            indx = bestFit(New); // get index of free block that satisfies th ploicy
        
        } else if (flag.equalsIgnoreCase("W")){ // Worst Fit
            
            indx = worstFit(New); // get index of free block that satisfies th ploicy
            
        } else if (flag.equalsIgnoreCase("F")){ // First Fit
            
            indx = firstFit(New); // get index of free block that satisfies th ploicy
            
        }
        
        
        if (indx == -1) { // no free block satisfies the policy
            
            System.err.println("\nRequest Failed"); 
                
        } else {
            
            block Chosen = this.blocks.get(indx); // get the chosen block returned from the policy
            int start = Chosen.getStartAddress();
            New.setStartAddress(start);
            
            // eliminating internal fragmentation
            
            this.blocks.remove(indx);
            this.blocks.add(indx, New);
            
            int remainingSize = Chosen.getSize() - size;
            if (remainingSize != 0){
                int startFree = start + size;
                this.blocks.add(indx+1, new block(startFree, remainingSize, "Unused", P_num));
            }
            
            
            System.out.println("\nRequest Succeded");
            
        }    
    }
    
//------------------------------------------------------------------------------------------------------------------------------------ 
    
    private void releaseBlock(int P_num){
        
        for(int i=0; i<blocks.size(); ++i){
            
            if (!"Unused".equalsIgnoreCase(blocks.get(i).getStatus()) && P_num == blocks.get(i).getP_num()){
                
                blocks.get(i).setStatus("Unused");
                
                System.out.println("\nRelease Succeded"); 
                return;
            }  
        }
        System.err.println("\nRelease Faild"); 
    }
    
//------------------------------------------------------------------------------------------------------------------------------------ 

    private int firstFit(block New){
        
        // loop through all blocks to find the first free block that matches the new block
        for(int i=0; i<blocks.size(); ++i){
            if (blocks.get(i).getStatus().equals("Unused")){
                if (blocks.get(i).getSize() >= New.getSize()){
                    return i; // return the index of the matched block
                }
            }
        }
        
        // if no match found, return -1
        return -1;   
    }
    
//------------------------------------------------------------------------------------------------------------------------------------ 
    
    private int bestFit(block New){
        
        // initialize the index and minimum size variables, minimum size should be greater than the memory size 
        int indx = -1, min = MS.memorySize +1;
        
        // loop through all the blocks to find the free block with the smallest size that matches the new block
        for(int i=0; i<blocks.size(); ++i){
            if (blocks.get(i).getStatus().equals("Unused")){
                if (blocks.get(i).getSize() >= New.getSize() && blocks.get(i).getSize() < min){
                    min = blocks.get(i).getSize();
                    indx = i;
                }
            }
        }
        
        // return the index of the free block with the smallest size
        return indx;
    }
    
//------------------------------------------------------------------------------------------------------------------------------------ 
        
    private int worstFit(block New){
        
        // Initialize the index and maximum size variables
        int indx = -1, max = 0;

        // loop through all the blocks to find the free block with the largest size that matches the new block
        for(int i=0; i<blocks.size(); ++i){
            if (blocks.get(i).getStatus().equals("Unused")){
                if (blocks.get(i).getSize() >= New.getSize() && blocks.get(i).getSize() > max){
                    // update the maximum size
                    max = blocks.get(i).getSize();
                    // update the index
                    indx = i;
                }
            }
        }
        
        // return the index of the free block with the largest size
        return indx;
    }
    
//------------------------------------------------------------------------------------------------------------------------------------ 
        
    private void stat(){
        
        // loop through all the blocks
        for(int i=0; i<this.blocks.size(); ++i){
            
            // get information of block
            int start = this.blocks.get(i).getStartAddress();
            int size = this.blocks.get(i).getSize();
            String status = this.blocks.get(i).getStatus();
            
            System.out.println("Addresses [" + start + ":" + (start+size-1) + "] " + status);   
        }
    }
    
//------------------------------------------------------------------------------------------------------------------------------------
    
       private void compact() {
        
        int sum = 0, start = 0;
        
        // get all holes in the memory
        for(int i=0; i<this.blocks.size(); ++i){
            
            if ("Unused".equalsIgnoreCase(this.blocks.get(i).getStatus())){
                sum += this.blocks.get(i).getSize();
            }
            
        }
        
        // remove holes
        for(int i=0; i<this.blocks.size(); ++i){
            
            if ("Unused".equalsIgnoreCase(this.blocks.get(i).getStatus())){
                
                this.blocks.remove(i);
                i--;
                
            }
            
        }
        
        // shift processes
        for(int i=0; i<this.blocks.size(); ++i){
        
            if (this.blocks.get(i).getStartAddress() != start){
                this.blocks.get(i).setStartAddress(start);
            }
            
            start = this.blocks.get(i).getStartAddress() + this.blocks.get(i).getSize();
            
        }

        
        this.blocks.add(new block(start, sum, "Unused"));
    } 

//------------------------------------------------------------------------------------------------------------------------------------ 
    
    private void combineholes() {
        
        // loop through all the blocks
        for(int i=0; i<this.blocks.size()-1; ++i){
            
            // check if there is a two hole next to each other
            if  ( "Unused".equalsIgnoreCase(this.blocks.get(i).getStatus()) && "Unused".equalsIgnoreCase(this.blocks.get(i+1).getStatus())){
                
                // increase the size of the hole by the next hole size
                this.blocks.get(i).increaseSize(this.blocks.get(i+1).getSize());
                // remove the next holl 
                this.blocks.remove(i+1);
                
                i--;
            }
        }
    }

}