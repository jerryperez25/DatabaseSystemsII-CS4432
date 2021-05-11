package jperez2_Project2_Package;

public class ArrayBasedIndex 
{
	private String[] array; //creates the array of strings
	
	public ArrayBasedIndex(String[] arr)
	{
		//do nothing up until there is a purpose for this
	}
	
	public ArrayBasedIndex()
	{
		array = new String[5000]; //designates the space
	}
	//----------Getter---------
	public String[] getArray()
	{
		return this.array;
	}
	/**
	 * Adds the file and record string to the array index
	 * @param randomV - the value to add to
	 * @param recFile - the string that has the record and the file Id's
	 */
	public void addToArray(int randomV, String recFile)
	{
		randomV = randomV - 1; // start the index at 0
		if(this.getArray()[randomV] != null)
		{
			
			String valueCheck = recFile.substring(0,3); //obtains the values that we need to add
			//checks to see file ID is already in values
			String arrayElement = this.getArray()[randomV];
			//this function checks to see if the value is in the element
			if(!containCheck(arrayElement, valueCheck))
			{
				//if not separate files with semicolon
				this.getArray()[randomV] = this.getArray()[randomV] + ";" + recFile;
			}
			else
			{
				//if it is then add it to first available loc
				int indexLoc = this.getArray()[randomV].indexOf(valueCheck);
				String thirdSub = recFile.substring(3); //obtain the string
				String indexItr = this.getArray()[randomV].substring(indexLoc + 3);
				this.getArray()[randomV] = this.getArray()[randomV].substring(0, indexLoc + 3) + thirdSub + "," + indexItr;
			}
		}
		else
		{
			this.getArray()[randomV] = recFile;
		}
	}
	/**
	 * Determines the records with randomV that is greater than the given greater than
	 * value and less than the given less than value.
	 * @param greater
	 * @param less
	 */
	public void printRange(int greater, int less)
	{
		//if less is greater than 5000 - programmers decision
		if(less>5000)
		{
			//inform the user
			System.out.println("The less than value entered is greater than 5000. Adjusting to maximum value of 5000.");
			//set the less than value to 5000
			less = 5000;
		}
		//determines whether or not the greater than or less than value is valid
		if (boundCheck(greater, less))
		{
			System.out.println("There are no records found with the given criteria.");
			return;
		}
		
		String fileRec = "";
		int lessThanItr = less - 1;
		//iterate through
		for (int i = greater; i < lessThanItr; i++) 
		{
			//as long as we are not null
            if (array[i] != null) 
            {
            	String arrayElmnt = this.getArray()[i];
                String[] newFile = arrayElmnt.split(";"); //split the string
                //for each string in our new file
                for (String str : newFile) 
                {
                	String newFileRange = str.substring(0,3);
                	//if it is not there
                    if (!containCheck(fileRec, newFileRange)) 
                    {
                    	//separate with semicolon
                    	fileRec = fileRec + ";" + str;
                    } 
                    else //if it is there
                    {
                    	int index = fileRec.indexOf(newFileRange); //determines the index of first occurrence 
                        fileRec = fileRec.substring(0, index + 3) + str.substring(3) + "," + fileRec.substring(index + 3);
                    }
                }
            }
        }
		//as long as we are not empty
		if (fileRec.length() != 0) 
		{
			fileRec = fileRec.substring(1);
            DatabaseReader.printString(fileRec); 
        } 
		//if we are empty then we dont have any matches
		else 
		{
			System.out.println("There are no records found with the given criteria.");
        }


	}
	/**
	 * This function serves as a helper to determine whether or not the randomV value 
	 * is a valid determinant or not.
	 * @param greater
	 * @param less
	 * @return true if value is bad, false otherwise
	 */
	public boolean boundCheck(int greater, int less)
	{
		//less than value lower than lower bound 0
		if (less < 0) 
		{
			return true;
		}
		//greater than value is greater than max bound
		else if(greater > 5000)
		{
			return true;
		}
		//greater than value is lower than lower bound 
		else if(greater < 0)
		{
			return true;
		}
		//valid constraints
		else
		{
			return false;
		}
	}
	/**
	 * This function is a helper to determine whether or not the given array of strings
	 * contains the specified value.
	 * @param valueCheck
	 * @return
	 */
	public boolean containCheck(String isItIn, String valueCheck)
	{
		//if the value exists in the structure
		if(isItIn.contains(valueCheck))
		{
			return true;
		}
		//it does not exist in it
		else
		{
			return false;
		}
	}
}
