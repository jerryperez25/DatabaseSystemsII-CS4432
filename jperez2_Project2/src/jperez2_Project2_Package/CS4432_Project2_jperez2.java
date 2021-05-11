package jperez2_Project2_Package;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CS4432_Project2_jperez2 
{

	public static void main(String[] args) throws IOException 
	{
		InputStreamReader newStream = new InputStreamReader(System.in);
		BufferedReader read = new BufferedReader(newStream);
        DBManager database = new DBManager();
        System.out.println("Program is ready and waiting for user command.");
        boolean switcher = true;
        while (switcher) 
        {
            String userInput = read.readLine().toUpperCase();
            userInput = userInput.replace(" ", "");
            int integerParser;
            int integerRangeParserOne;
            int integerRangeParserTwo;
            if (userInput.contains("CREATEINDEXONRANDOMV")) 
            {
                database.create(); //create the indexes

            }
            else if (userInput.contains("SELECT*FROMPROJECT2DATASETWHERERANDOMV=")) 
            {
            	userInput = userInput.replace("SELECT*FROMPROJECT2DATASETWHERERANDOMV=","");
            	integerParser = Integer.parseInt(userInput);
                database.whichSearch(0, integerParser, 0);

            }
            else if (userInput.contains("SELECT*FROMPROJECT2DATASETWHERERANDOMV>") && userInput.contains("ANDRANDOMV<")) 
            {
            	userInput = userInput.replace("SELECT*FROMPROJECT2DATASETWHERERANDOMV>", "");
                String[] range = userInput.split("ANDRANDOMV<");
                integerRangeParserOne = Integer.parseInt(range[0]);
                integerRangeParserTwo = Integer.parseInt(range[1]);
                database.whichSearch(1, integerRangeParserOne, integerRangeParserTwo);

            }
            else if (userInput.contains("SELECT*FROMPROJECT2DATASETWHERERANDOMV!=")) 
            {
            	userInput = userInput.replace("SELECT*FROMPROJECT2DATASETWHERERANDOMV!=", "");
            	integerParser = Integer.parseInt(userInput);
                database.whichSearch(2, integerParser, 0);

            }
            else if (userInput.contains("STOP")) 
            {
                System.out.println("Stopping...");
                switcher = false;
                break;
            }
        }

	}

}
