package jperez2_Project1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class CS4432_Project1_jperez2 
{

	public static void main(String[] args) throws IOException 
	{
		/*
		File data_table = new File("Student"); //assuming we have a student table
		if(data_table.exists() == false)
		{
			//create the directory 
			data_table.mkdir();
		}
		for(int i = 0; i < 10; i++)
		{
			File newFile = new File("Student/F" + (i+1)+".txt"); //starts at 1
			newFile.createNewFile(); //create the file
			
			FileWriter writer = new FileWriter(newFile); 
			
			for (int j = 0; j < 100; j++) 
			{
				String s = "F";
				if(i < 10)
				{
					s = s + "0" + (i+1);
				}
				else
				{
					s = s + (i+1);
				}
				
				if (j < 10)
				{
					s = s + "-Rec"+i+"0"+(j+1)+", "+"Name"+i+"0"+(j+1)+", "+"address"+i+"0"+(j+1)+", " + "age"+i+"0"+(j+1)+".";
				}
				else
				{
					s = s + "-Rec"+i+""+(j+1)+", "+"Name"+i+""+(j+1)+", "+"address"+i+""+(j+1)+", " + "age"+i+""+(j+1)+".";
				}
				writer.write(s);
			}
			writer.close();
		}
		*/
		// Extracts the first argument given to allocate buffer size
		int bufferSize = Integer.parseInt(args[0]);
		
		//System.out.println("The buffer size is: " + bufferSize);
		
		BufferPool buffer = new BufferPool(bufferSize); //creates buffer with the specified size
		
		System.out.println("The program is ready "
				+ "for the next command. At any point, you can type STOP to stop.");
		//read the keyboard actions
		Scanner scan = new Scanner(System.in);
		
		//configure a mean to control running or not
		boolean isRunning = true;
		
		//while we are supposed to be running - monitor keyboard presses, call functions, etc 
		while(isRunning == true)
		{
			String input = scan.nextLine(); //needs to be in while loop to continue scanning
			String commands[] = input.split(" ", 3); //splits the arguments
			
			
			if(commands.length >= 2) // as long as we have greater than or equal to 2 arguments
			{
				int record = Integer.parseInt(commands[1]); //this is the desired record
				
				if (commands[0].equals("Get") || commands[0].toUpperCase().equals("GET"))
				{
					buffer.getRecord(record); //we call the get function
				}
				else if (commands[0].equals("Set") || commands[0].toUpperCase().equals("SET"))
				{
					//remove the quotations for insertion
					String parsedContent = commands[2].replaceAll("^\"+|\"+$", "");
					//call the set function
					buffer.setRecord(record, parsedContent);
				}
				else if (commands[0].equals("Pin") || commands[0].toUpperCase().equals("PIN"))
				{
					//call the pin function
					buffer.pinFrame(record);
				}
				else if (commands[0].equals("Unpin") || commands[0].toUpperCase().equals("UNPIN"))
				{
					//call the unpin function
					buffer.unpinFrame(record);
				}
				else if (commands[0].equals("STOP"))
				{
					//stop the program from monitoring keypresses
					isRunning = false;
				}
		
			}
			else if (commands[0].equals("STOP"))
			{
				//stop the program from monitoring keypresses
				isRunning = false;
			}
			else
			{
				//the number of arguments are not supported
				System.out.println("There as an invalid number of arguments given");
			}
			
			
			
		}

	}

}
