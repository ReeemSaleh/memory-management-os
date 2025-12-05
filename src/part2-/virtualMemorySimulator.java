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

package part2;

import java.util.Scanner;
import java.io.*;
import java.util.Random;

public class virtualMemorySimulator {
    
// ************************* Global Variabls *************************
  static int[] pageTable = new int[256];
  static byte[] physMem = new byte[128*256];
  static int mask = 255;
  static int[] first100 = new int[100]; // To avoid sequential accesses
  static byte[] first100byte = new byte[100];// To avoid sequential accesses
  static int[] adres80 = new int[80]; // to implement Statistics
  static int[] checkF = new int[128];// to keep track of used frames
              
  // ************* Global Variabls for Replacement *************
  static int[] myTestAD = new int[133];
  static byte[] myTestVal = new byte[133];
  
  
  
  // **************************** Main Function ****************************
    public static void main(String[] args) throws FileNotFoundException {
        
        File addresses = new File(System.getProperty("user.dir") + "/Memory_Management_OS/addresses.txt");
        File values = new File(System.getProperty("user.dir") + "/Memory_Management_OS/correct.txt");
          if(!addresses.exists() || !values.exists()){
            System.out.println("The file dose not exist");
            System.exit(0);
        }
        
         Scanner read = new Scanner (addresses);
         Scanner readVal = new Scanner (values);
        
                
        
        //initialize page table and physical memory with -1
        initialaize();
        
        
        // ******* Populating the physical memory and page table *******
        int count=0;
         // Mark all frames as unused
        for (int i = 0; i < checkF.length; i++) {
              checkF[i]=-1;
        }
        
        while(count<100){
              int logical = read.nextInt();
              first100[count]=logical;
              byte val = readVal.nextByte();
              first100byte[count]=val;
              populating(logical,val);
              count++;
        }// *** end *** 
        
        
        // **** Call testing Function ****
        test(1);
        
        // ************** collect 80 adresses for Statistics **************
        int ind=0, j=0;
        while(j<80){
            int logical;
            //generat 0 or 1 randomly to choose adresse from first 100 or others
            int ran = (int)(Math.random()*2);
            if((ran==0 && ind<50) || j-ind==30){
                logical = first100[ind];
                ind++;
            }else 
                logical = read.nextInt();
                adres80[j]=logical;
                j++;
        } // *** end ***
        
        
        
        // **** Call Statistics Function ****
        stat();
        
        // *_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*
        // *_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_* Pounce Part *_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*
        // *_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*_*
        
        System.out.println("--------------------------------------------------------------------------------------------\n"+
                           "\t\t\tThe testing of the page replacement routine"+
                           "\n--------------------------------------------------------------------------------------------\n");
        // 1. The physical memory has already initialized with size 128*256
        // 2. Generate 133 logical address and assign random byte for each
        new Random().nextBytes(myTestVal);

        // Mark all pages as unused 
        int[] checkP = new int[133];
        for (int p = 0; p < checkP.length; p++) {
            checkP[p]=-1;
        }
            
        int mask2=0;
        for (int i = 0; i < 133; i++) {
            
            // generate random page from 0 to 132
            int page;
            do{
                 page = (int)(Math.random()*133);
            }while(checkP[page]==1);
            checkP[page]=1; // mark the page as used
            int logical = mask2 | page;
            logical = (logical<<8)| ((int)(Math.random()*256));
            myTestAD[i]=logical;
            if(myTestVal[i]==-1) myTestVal[i]=-2;
        }
        
        // 3. initilaiz page table and physical memory with -1
        initialaize();
        
        // 4. populate the physical memory with 128 vlues
        // a. Mark all frames as unused
        for (int i = 0; i < checkF.length; i++) {
            checkF[i]=-1;
        }
        
        // b. populate
        for (int i = 0; i < 128; i++) {
             populating(myTestAD[i],myTestVal[i]);
        }
        
        // 5. implement FIFO replacement algorithm to store the remaining 5 addresses
        fifo();
        
        // 6. Testing
        test(0);
        
        // 7. Close Resources
        read.close();
        readVal.close();
        
    } // ********** MAIN END **********
    
    
    // ************************ FIFO Replacement Algorithm Function ************************
    public static void fifo(){
        // Now all frames has ben allocated, so we wil implement FIFO replacement algorithm to store the remaining 5 addresses
        System.out.println("Step 1: running page replacement routine"+
                           "\n--------------------------------------------------------------------------------------------"+
                           "\nLogical Address\t\tNew Page #\t\tVictim Page #\t\tReused Frame"+
                           "\n--------------------------------------------------------------------------------------------");
        for (int i = 128; i < 133; i++) {
            int logical=myTestAD[i];
            int newPage = getPageNumber(logical);
            int victimPage = getPageNumber(myTestAD[i-128]);
            int frame = pageTable[victimPage];
            
            // update page table and physical memory
            physMem[translate(myTestAD[i-128])]=-1;
            pageTable[victimPage] = -1;
            pageTable[newPage] = frame;
            
            
            // store the value in the physical memory 
            physMem[translate(logical)] = myTestVal[i];
            
            System.out.println(logical+"\t\t\t"+newPage+"\t\t\t"+victimPage+"\t\t\t"+frame);
           
        }
       
        System.out.println("");
    }
   
    // *************** Function to initialize page table and physical memory ***************
    public static void initialaize(){
         for (int i = 0; i < pageTable.length; i++) {
            pageTable[i]=-1;
        }
        
        for (int i = 0; i < physMem.length; i++) {
            physMem[i]=-1;
        }
    }
    
    // *********************** Testing Function *************************
    public static void test(int x){
        // if x = 0 so we implement test after Replacement
        printHeader(x);
        // array to keep track of addresses that has been choosen befor
        int[] check=new int[133];
        
        // initialize with -1 
        for (int i = 0; i < check.length; i++) {
            check[i]=-1;
        }
        
        // Make five test cases
        for (int i = 0; i < 5; i++) {
            //choose address randomly
            int ran;
            do{
             ran = (x==0? (103+(int)(Math.random()*30)):((int)(Math.random()*100)));
            }while(check[ran]==1);
            
            // mark this address as chosen
            check[ran]=1;
            
            int logical = (x==0?myTestAD[ran]:first100[ran]), page=getPageNumber(logical), offset = getOffset(logical);
            int physical=translate(logical);
            byte val = physMem[physical];
            byte correctVal = (x==0?myTestVal[ran]:first100byte[ran]);
            System.out.println(logical+"\t\t\t"+page+"\t\t"+offset+"\t\t"+pageTable[page]+"\t\t"+val+"\t\t"+
                            (val==correctVal?"Yes":"No"));
        }
        
    }
    
    // ************ Header of Testing ************
    public static void printHeader(int x){
        if(x==0) System.out.print("Step 2: ");
        System.out.println("The resul of retriving the values of 5 logical addresses:"+
                "\n-------------------------------------------------------------------------------------------------------------------"+
                "\nLogical Address\t\tPage #\t\tOffset\t\tFrame #\t\tValue\t\tSame as model answer"
               +"\n-------------------------------------------------------------------------------------------------------------------");
    }
    
    // **************** Statistics Function ****************
     public static void stat(){
         System.out.println("\n-----------------------------------------------------------------------------------"+
                            "\n\t\t\t\tStatistics"+
                            "\n-----------------------------------------------------------------------------------");
         System.out.println("#\tLogical Address\t\tPage #\t\tState"+
                            "\n-----------------------------------------------------------------------------------");
         int fault=0;
         for (int i = 0; i < 80; i++) {
             int add = adres80[i];
             System.out.print((i+1)+"\t"+add+"\t\t\t"+getPageNumber(add)+"\t\t");
             // detecte page fault
             if(pageTable[getPageNumber(add)]==-1){
                 System.out.println("Page not found");
                 fault++; 
                 continue;
             }
             // else
             System.out.println("Page Found in the page table");
         }
         
         System.out.println("-----------------------------\nNumber of Page-fault is: "+fault+
                 "\n-----------------------------\n");
         
         
     }
     
     
    // **************** Translate Function ****************
    public static int translate(int logical){
         int d = getOffset(logical);
         int p = getPageNumber(logical);
         int physical = (256*pageTable[p])+d;
         return physical;
    }
    
    // ************* Page Number Extraction Function *************
    public static int getPageNumber(int logical){
        return (logical>>8)&mask;
    }
    
    // ************* Offset Extraction Function *************
    public static int getOffset(int logical){
        return logical&mask;
    }
    
    // ************* Populating Function *************
    public static void populating(int logical, byte val){
      
              int d = getOffset(logical);
              int p = getPageNumber(logical);
              int frame;
              
              // if this page is not in the page table
              if(pageTable[p]==-1){
                  do{
                    frame = (int)(Math.random()*128);
                  }while(checkF[frame]==1);
                  checkF[frame]=1; // mark this frame as used
                  pageTable[p]=frame; // update the page table
              }else // otherwise
                  frame=pageTable[p];
             
              int physical = (256*frame)+d;
              
              //store the signed byte
              physMem[physical]=val;
             
    }
    
}