import java.util.Scanner;
import java.io.*;


public class ValidateAndSplit {
	
	File file;
	int rowcount;
	BufferedReader reader;
	
	
	public void countrows()
	{
		int linenumber = 0;
		try{
		     if((this.file).exists()){
		         FileReader fr = new FileReader(this.file);
		         LineNumberReader lnr = new LineNumberReader(fr);

	             while (lnr.readLine() != null)
	               linenumber++;
	            
	            System.out.println("Total number of lines : " + linenumber);
                lnr.close();
		      }
		   else
			 System.out.println("File does not exists!");  
		}
	
	  catch(IOException e){
		  e.printStackTrace();
	      }
		
		this.rowcount= linenumber;
	}
	
	
	
	public int validate(FileWriter fw){
		int validcount=0;
		int validGPID=0;
		int validIDFA=0;
		
		try{
		     if((this.file).exists()){
		         FileReader fr = new FileReader(this.file);
		         int len;
		         
		 outerloop: for(int i=0;i<this.rowcount;i++){
		 
		        	 boolean flag=false;
		        	 //count if the number of characters in the row are 36 : if not then continue to next row
		        	 String currentLine=(this.reader).readLine();
		        	 len=currentLine.length();
		        	 
		        	 
		        	 if(len!=36){continue outerloop;}
		        	 
		        	// check if the characters have - at 9th, 14th,19th and 24th position of the row: if not then continue to next row
		        	 String s1=Character.toString(currentLine.charAt(8));
		        	 String s2=Character.toString(currentLine.charAt(13));
		        	 String s3=Character.toString(currentLine.charAt(18));
		        	 String s4=Character.toString(currentLine.charAt(23));
		        	 
		             if(s1.equals("-") && s2.equals("-") && s3.equals("-") && s4.equals("-"))
		            		{flag=true;}
		             else{continue outerloop;}
		              		 
		             // check if all the other characters are alphanumeric with no special characters: if not then continue to next row
		             
		             
		        innerloop1: for( int j = 0; j<len; j++ )
		             {
		               if(j!=8 && j!=13 && j!=18 && j!=23){	 
		                 char  temp = currentLine.charAt(j);
		                 if(Character.isDigit(temp) || Character.isLetter(temp)){
		            	   continue innerloop1;
		                  }
		                 else{
		            	      flag=false;
		            	      break;  
		                     }
		                 }
		               
		               else{continue innerloop1;}
		             }
		        		
		            // check if all the alphabets in the characters are either lower case or upper case: : if not then continue to next row
		            if (flag==false){continue;}
		            int cntupper =0;
		        	int cntlower=0;  
		        	int alpha=0;
		innerloop2: for( int j = 0; j<len; j++ )
		             {
			            if(j!=8 && j!=13 && j!=18 && j!=23){	 
		                  char temp = currentLine.charAt(j);
		                  if(Character.isLetter(temp)){
		            	    alpha++;
		            	    if(Character.isUpperCase(temp)){cntupper++;}
		            	    else{cntlower++;}
		                    }
		                  else{continue innerloop2;}
		                 }
		              else{continue innerloop2;}
		             }		               
		              
		              if(alpha==cntupper || alpha==cntlower){
		            	  flag=true;
		            	 copystring(fw,currentLine);
		            	  if(alpha==cntupper)
		            		  validIDFA++;
		            	  else
		            		  validGPID++;
		              }
		              else{
		            	  flag=false;
		            	  continue outerloop;
		              }
		            
		       } 
		         
		     }
		     
		else{
			  System.out.println("File does not exists!");  
			}
		     
		}
	  
		catch(IOException e){
		        	 e.printStackTrace();
			      }
		
		System.out.println("The number of valid GPID codes are: "+validGPID);
		System.out.println("The number of valid IDFA codes are: "+validIDFA);
		validcount=validGPID+validIDFA;
		return validcount;  
		
	}
	
	public void copystring(FileWriter fw,String str) throws IOException{
		
		fw.write(str);
		fw.write("\r");
		
	}
	
	public void splitfile() throws IOException{
		int rows=this.rowcount;
		// As 1MB=1048576 bytes => 10MB=10485760 bytes
		int BytesPerChunk = 10485760;
		
		BufferedReader reader = null;
        
        try {
            reader = new BufferedReader(new FileReader(this.file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String currentLine;
        int key=1;
        
        String newfile="c:\\Files\\temp" + "_" + key + ".txt";
        File destfile = new File(newfile);
        FileWriter fw= new FileWriter(destfile,true);
        
		byte[] bytes=null;
		double currentlinesize=0;
		long destfilesize = 0;
		
		
		destfilesize = destfile.length();
		double totalsize=destfilesize;
		for(int i=0;i<this.rowcount;i++){
			  currentLine=reader.readLine();
			  bytes= currentLine.getBytes("UTF-8"); 
			  currentlinesize=bytes.length;
			  totalsize=currentlinesize+totalsize;
			  
			  if(totalsize<=BytesPerChunk)
				  fw.write(currentLine);
			  else{
				  fw.close();
				  key++;
				  newfile="c:\\Files\\temp" + "_" + key + ".txt";
			      destfile = new File(newfile);
			      fw= new FileWriter(destfile,true);
				  totalsize=currentlinesize;
				  fw.write(currentLine);
			  }
		      
		}
		
		fw.close();
		reader.close();
		        
		}        	 
		
	

	public static void main(String[] args) throws IOException{
		
			Scanner sc=new Scanner(System.in);
			
			System.out.println("Enter the path of the input file");
			String path=sc.next();
			
			ValidateAndSplit obj1=new ValidateAndSplit();
			
			FileWriter fw=new FileWriter("C:\\temp.txt",true);
			
    		obj1.file =new File(path);
            obj1.countrows();
            try{
            obj1.reader=new BufferedReader(new FileReader(path));
            }
            catch (IOException e){
            	System.out.println("File not found");
            }
    		
    		int validrows=obj1.validate(fw);
    		System.out.println("The number of valid codes are "+validrows);
    		fw.close();
    		
    		ValidateAndSplit obj2=new ValidateAndSplit();
    		obj2.file=new File("C:\\temp.txt");
    		obj2.countrows();
    		obj2.splitfile();
    		System.out.println("File successfully split in 10MB chunks. Files can be found in c:\\Files folder");
	}

}
