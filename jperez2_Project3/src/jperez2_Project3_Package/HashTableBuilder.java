/**
 * 
 */
package jperez2_Project3_Package;

import java.util.Map;

/**
 * @author jerryperez
 *
 */
public class HashTableBuilder 
{
	private String[] hashTable;
	private Map<String, String>hash;

	public HashTableBuilder(int size) 
	{
		this.hashTable = new String[size];
	}
	public HashTableBuilder(String[] hashTable) 
	{
		this.hashTable = hashTable;
	}
	
	//----------Getters------
	
	public String[] getTable() 
	{
		return this.hashTable;
	}
	public Map<String, String> getHash()
	{
		return this.hash;
	}
	/**
	 * MIGHT DITCH THIS IDEA
	 * Attempts to use Map for adding key value pair 
	 * @param key
	 * @param value
	 */
	public void addToHash(String key, String value)
	{
		this.hash.put(key, value);
	}
	
	/**
	 * MIGHT DITCH THIS IDEA
	 * Attempts to use Map for getting desired record using randomV
	 * @param randomV
	 */
	public void getHashElement(int randomV)
	{
		this.hash.get(randomV);
	}

	/**
	 * Idea: hash it myself with array manipulation
	 * Adds the key value pair to the array 
	 * @param key
	 * @param value
	 */
	public void addKeyVal(String key, String value) 
	{
		int keyHashOptOne = Integer.parseInt(key)*5 - 4;
		int keyHashOptTwo = Integer.parseInt(key);
		int hashInt = keyHashOptTwo - 1;
		
		if (this.getTable()[hashInt] != null && keyHashOptTwo > 0) 
		{
			this.hashTable[hashInt] = this.getTable()[hashInt] + ";" + value;
		}
		else 
		{
			this.hashTable[hashInt] = value;
		}
	}

	/**
	 * Obtains the desired record using randomV value 
	 * @param randomV
	 * @return
	 */
	public String get(int randomV) 
	{
		if (!conditionChecker(randomV)) 
		{
			return null;
		}
		else 
		{
			return this.getTable()[randomV - 1];
		}
	}
	/**
	 * HELPER FUNCTION
	 * determines if randomV is greater than 0 and less than 501 
	 * @param randomV
	 * @return true if the condition is satisfied and false otherwise
	 */
	public boolean conditionChecker(int randomV)
	{
		if(randomV>0 && randomV < 501)
		{
			return true;
		}
		return false;
	}

}
