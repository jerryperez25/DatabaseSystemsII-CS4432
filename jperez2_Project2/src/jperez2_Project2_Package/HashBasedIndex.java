package jperez2_Project2_Package;

import java.util.HashMap;

public class HashBasedIndex 
{
	private HashMap<Integer, String> hash; 
	public HashBasedIndex(HashMap<Integer, String> hash)
	{
		this.hash = hash;
	}
	public HashBasedIndex()
	{
		hash = new HashMap<Integer, String>();
	}
	//-----------Getters----------
	public HashMap<Integer, String> getHash()
	{
		return this.hash;
	}
	/**
	 * Adds the record and file IDs based on the randomV
	 * @param randomV
	 * @param recFile
	 */
	public void addToHash(int randomV, String recFile)
	{
		if (!keyCheck(randomV)) 
		{
			//put it in
			hash.put(randomV,  recFile);
        } 
		else 
		{
			//if not....
			String obtainRandomV = hash.get(randomV);
			String recFileSub = recFile.substring(0,3);
            if (!containCheck(obtainRandomV, recFileSub)) 
            {
            	hash.replace(randomV, hash.get(randomV) + ";" + recFile);
            } 
            else 
            {
            	int index = obtainRandomV.indexOf(recFile.substring(0,3));
            	String indexBasedRangeSub = obtainRandomV.substring(0, index + 3);
            	String indexBasedSub = obtainRandomV.substring(index + 3);
            	recFileSub = recFile.substring(3);
            	
                obtainRandomV = indexBasedRangeSub + recFileSub + "," + indexBasedSub;
                hash.replace(randomV, obtainRandomV);
            }
        }
	}
	/**
	 * Prints the records with the randomV value
	 * @param randomV
	 */
	public void readElement(int randomV)
	{
		if (keyCheck(randomV)) 
		{
            String value = hash.get(randomV);
            DatabaseReader.printString(value);
        }
		else 
		{
			System.out.println("There are no records with the given RandomV: " + randomV);
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
	/**
	 * Determines if the hash contains the randomV value
	 * @param randomV
	 * @return
	 */
	public boolean keyCheck(int randomV)
	{
		if (hash.containsKey(randomV))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

}
