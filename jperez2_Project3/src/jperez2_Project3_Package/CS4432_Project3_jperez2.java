package jperez2_Project3_Package;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CS4432_Project3_jperez2 
{

	public static void main(String[] args) throws IOException 
	{
		InputStreamReader newStream = new InputStreamReader(System.in);
		BufferedReader read = new BufferedReader(newStream);
		DatabaseManager database = new DatabaseManager();
		System.out.println("The program is ready for user input. You can type and enter STOP at any time to stop running.");

		while (true) 
		{
			String userInput = read.readLine().toLowerCase(); 
			userInput = userInput.replace(" ", ""); //remove spaces

			int conditional = 0; // 0 if from or where and 1 if groupby
			boolean cannotRead = false; //determines whether or not the argument is supported
			String[] supportedCmds = new String[3]; //SELECT, FROM/WHERE, GROUP BY

			// stop condition
			if(userInput.contains("stop"))
			{
				break;
			}
			//Start with select
			else if (userInput.contains("select") && conditional == 0) 
			{
				//replace it
				userInput = userInput.replace("select", "");
			}
			else 
			{
				//doesnt have select so we cant read
				cannotRead = true;
			}

			if (cannotRead) 
			{
				//if we cannot read then we tell the user
				System.out.println("Cannot read the user input, double check the input is a valid command.");
			}
			else 
			{
				//we can read the input
				//check to see if we have from
				if (userInput.contains("from") && !cannotRead && userInput != null) 
				{
					//split at from
					String[] whichArgs = userInput.split("from");
					//designate the argument
					supportedCmds[0] = whichArgs[0];
					userInput = userInput.substring(userInput.indexOf("from") + 4);
					
					//if the next argument has where
					if (whichArgs[1].contains("where")) 
					{
						//split at the where
						whichArgs = userInput.split("where");
						//set our conditional to 0
						conditional = 0;
						supportedCmds[1] = whichArgs[0];
						supportedCmds[2] = whichArgs[1];

					} 
					//checks groupby
					else if (whichArgs[1].contains("groupby")) 
					{
						//splits at groupby
						whichArgs = userInput.split("groupby");
						conditional = 1;
						supportedCmds[1] = whichArgs[0];
						supportedCmds[2] = whichArgs[1];

					}
					else 
					{
						//there is nothing else that is supported, so we cannot read
						cannotRead = true;
					}
				} 
				else 
				{
					//there is nothing else that is supported, so we cannot read
					cannotRead = true;
				}
				//do the work
				database.initiate(supportedCmds, conditional);
			}
		}
	}


}
