package jperez2_Project2_Package;

import java.util.Iterator;

public class DBManager implements Iterable<String>
{
	private HashBasedIndex hashBasedIndex;
	private ArrayBasedIndex arrayBasedIndex;
	
	//-----------Getters--------
	public HashBasedIndex getHashBasedIndex()
	{
		return this.hashBasedIndex;
	}
	public ArrayBasedIndex getArrayBasedIndex()
	{
		return this.arrayBasedIndex;
	}
	/**
	 * this is what creates the indexes
	 */
	public void create()
	{
		hashBasedIndex = new HashBasedIndex();
		arrayBasedIndex = new ArrayBasedIndex();
        for (String s : this) 
        {
        	String sSubstring = s.substring(33, 37);
            int randomV = Integer.parseInt(sSubstring);
            String sSubstringOnetoThree = s.substring(1, 3);
            String sSubstringSeventoTen = s.substring(7,10);
            String fileRec = sSubstringOnetoThree + ":" + sSubstringSeventoTen;
            hashBasedIndex.addToHash(randomV, fileRec); //adds to hash
            arrayBasedIndex.addToArray(randomV, fileRec); //adds to array based
        }
        System.out.println("The hash-based and array-based indexes are built successfully. Program is ready and waiting for user command");

	}
	/*
	 * Determines the type of search to be performed. 
	 */
	public void whichSearch(int searchType, int randomVOne, int randomVTwo)
	{
		if(hashBasedIndex == null)
		{
			searchType = searchType + 3;
		}
		long start = getSysTime();
		if(searchType == 5)
		{
			inequalityScan(randomVOne);
            System.out.println("No Index available, table scan used.");
		}
		else if( searchType == 4)
		{
			rangeScan(randomVOne, randomVTwo);
			System.out.println("Range table scan Used");
		}
		else if(searchType == 3)
		{
			equalityScan(randomVOne);
			System.out.println("Equality Table Scan Used");
		}
		else if(searchType == 2)
		{
			inequalityScan(randomVOne);
            System.out.println("Inequality Table Scan Used");
		}
		else if(searchType == 1)
		{
			rangeSearch(randomVOne, randomVTwo);
			System.out.println("Range Search Used");
		}
		else if (searchType == 0)
		{
			equalitySearch(randomVOne);
			System.out.println("Equality Search Used");
		}
		long timeTaken = getSysTime() - start;
		System.out.println("The search took: " + timeTaken + " milliseconds.");
	}
	/**
	 * Performs inequality table scan
	 * @param randomV
	 */
	public void inequalityScan(int randomV) 
	{
        for (String record : this) 
        {
        	String recordSub = record.substring(33, 37);
        	int integerParse = Integer.parseInt(recordSub);
            if (integerParse != randomV) 
            {
            	recordPrinter(record);
            }
        }
        System.out.println("All 99 files read");
    }
	/**
	 * Performs range table scan
	 * @param greaterThan
	 * @param lessThan
	 */
	private void rangeScan(int greater, int less) 
	{
        for (String record : this) 
        {
        	String recordSub = record.substring(33, 37);
        	int integerParse = Integer.parseInt(recordSub);
            if (rangeCheck(integerParse, greater, less)) 
            {
            	recordPrinter(record);
            }
        }
        printStatements();
    }
	/**
	 * Performs equality search using hash based index
	 * @param randomV
	 */
	public void equalitySearch(int randomV) 
	{
        hashBasedIndex.readElement(randomV);
        System.out.println("Hash-based Index was used.");
    }
	/**
	 * Performs a range search using array index
	 * @param greaterThan
	 * @param lessThan
	 */
	public void rangeSearch(int greater, int less) 
	{
        arrayBasedIndex.printRange(greater, less);
        System.out.println("Array-based Index was used");
    }
	/**
	 * performs an equality table scan
	 * @param randomV
	 */
	private void equalityScan(int randomV) 
	{
        for (String record : this) 
        {
        	String recordSub = record.substring(33, 37);
        	int integerParse = Integer.parseInt(recordSub);
            if (integerParse == randomV) 
            {
            	recordPrinter(record);
            }
        }
        printStatements();
    }
	public Iterator<String> iterator() 
	{
        return new DatabaseReader();
    }
	/**
	 * Determines whether or not the given value falls within the boundaries 
	 * of less than and greater than
	 * @param valCheck
	 * @param greater
	 * @param less
	 * @return
	 */
	public boolean rangeCheck (int valCheck, int greater, int less)
	{
		if (valCheck < less && valCheck > greater)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	/**
	 * This is a helper function for printing statements
	 */
	public void printStatements()
	{
		System.out.println("There is no index available. A full table scan is finished.");
        System.out.println("All 99 files read");
	}
	/**
	 * This is a helper function for printing records
	 */
	public void recordPrinter(String record)
	{
		System.out.println(record);
	}
	/**
	 * This helper function calculates the system time of the process
	 */
	public long getSysTime()
	{
		return System.currentTimeMillis();
	}





}
