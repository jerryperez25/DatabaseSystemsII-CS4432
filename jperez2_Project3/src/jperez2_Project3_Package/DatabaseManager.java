/**
 * 
 */
package jperez2_Project3_Package;

import java.util.Iterator;

/**
 * @author jerryperez
 *
 */
public class DatabaseManager 
{
	/**
	 * This initiate function handles the columns and determines which operator to use.
	 * @param string
	 * @param conditional
	 */
	public DatabaseManager()
	{
		//doesnt do anything.
	}
	public void initiate(String[] string, int conditional) 
	{
		//the conditional will determine what operator to use
		//because it tells us if we have from/where or groupby
		switch(conditional)
		{
		case 0: //from or where 
			switch(fromOrWhereConditionals(string))
			{
			case -1: return; //do nothing because we shouldnt be getting here 
			case 0: //randomv = 
				hashBasedJoin();
				return;
			case 1: //randomv >
				//split at >
				String[] datasets = string[2].split(">");
				//call block-level
				blockLevelNestedLoopJoin(datasets[0].substring(0, 1).toUpperCase(),
						datasets[1].substring(0, 1).toUpperCase());
				return;
			case 2: //randomv <
				//split at <
				String[] datasetsTwo = string[2].split("<");
				//call block-level
				blockLevelNestedLoopJoin(datasetsTwo[1].substring(0, 1).toUpperCase(),
						datasetsTwo[2].substring(0, 1).toUpperCase());
				return;
			}
		case 1: //groupby
			//if we satisfy the groupBy conditions
			if (groupByConditionals(string)) 
			{
				//replace
				string[0] = string[0].replace("col2,", "");
				//split
				String[] columns = string[0].split(",");
				//call hash-based aggregation
				hashBasedAggregation(columns, string[1].toUpperCase());
				return;
			}
		}
	}
	/**
	 * HELPER FUNCTION
	 * This function is intended to see if the first string contains the second
	 * @param str1
	 * @param str2
	 * @return true if first string contains second, false otherwise 
	 */
	public boolean containsCheck(String str1, String str2)
	{
		if(str1.contains(str2) && str1.contains(str2))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	/**
	 * HELPER FUNCTION
	 * This function is intended to see if the first string equals the second 
	 * @param str1
	 * @param str2
	 * @return true if the first string contains the second, false otherwise
	 */
	public boolean equalCheck(String str1, String str2)
	{
		if(str1.equals(str2))
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
	/**
	 * HELPER FUNCTION
	 * This function checks to see if the groupBy column conditions are satisfied.
	 * @param string
	 * @return true if the conditions are satisfied, false otherwise.
	 */
	public boolean groupByConditionals(String[] string)
	{
		
		if(containsCheck(string[0], "col2") && containsCheck(string[2], "col2")
				&& (equalCheck(string[1],"a") || equalCheck(string[1],"b")))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	/**
	 * This function checks to see if the from or where column conditions are satisfied.
	 * @param string
	 * @return true if the conditions are satisfied, false otherwise.
	 */
	public int fromOrWhereConditionals(String[] string)
	{
		if (containsCheck(string[1], "a") && containsCheck(string[1], "b")) 
		{
			if (containsCheck(string[0], "a.col1") && containsCheck(string[0], "a.col2") && containsCheck(string[0], "b.col1")
					&& containsCheck(string[0], "b.col2")) 
			{

				if (containsCheck(string[2], "a.randomv")&& containsCheck(string[2], "=") && containsCheck(string[2], "b.randomv")) 
				{
					return 0;
				}

			}
			else if (containsCheck(string[0], "count(*)")) 
			{
				if (containsCheck(string[2], "a.randomv") && containsCheck(string[2], "b.randomv")) 
				{
					if (containsCheck(string[2], ">")) 
					{
						return 1;
					}
					if (containsCheck(string[2], "<")) 
					{
						return 2;
					}
				}
			}
		}
		System.out.println("You shouldnt be getting here...");
		return -1;
	}
	

	/**
	 * Prints the Joined dataset on randomV column 
	 */
	public void hashBasedJoin() 
	{
		long start = System.currentTimeMillis();
		System.out.println("The columns specified are as follows: ");
		System.out.println(" A.Col1     A.Col2     B.Col1     B.Col2");

		HashTableBuilder table = new HashTableBuilder(500);
		Iterator<String> dbAIterator = new DatabaseReader('A');
		Iterator<String> dbBIterator = new DatabaseReader('B');
		iterateDatabase(table, dbAIterator, dbBIterator);
		long end = System.currentTimeMillis() - start;
		System.out.println("Hash-Based Join takes " + end + " ms");
	}
	/**
	 * HELPER FUNCTION
	 * Populates table with key value pairs from A and then checks against dataset B 
	 * @param table
	 * @param dbAIterator
	 * @param dbBIterator
	 */
	public void iterateDatabase(HashTableBuilder table, Iterator<String> dbAIterator, Iterator<String> dbBIterator)
	{
		while (dbAIterator.hasNext()) 
		{
			String recordA = dbAIterator.next();
			table.addKeyVal(recordA.substring(33, 37), recordA);
		}
		while (dbBIterator.hasNext()) 
		{
			String recordB = dbBIterator.next();
			int intParser = Integer.parseInt(recordB.substring(33, 37));
			String[] matches = table.get(intParser).split(";");
			for (String record : matches) 
			{
				System.out.println(record.substring(0, 10) + "\t| " + record.substring(12, 19) + "\t| "
						+ recordB.substring(0, 10) + "\t| " + recordB.substring(12, 19));
			}
		}
	}
	/**
	 * Uses nested while loops to iterate through both datasets and compare 
	 * @param datasetOne
	 * @param datasetTwo
	 */
	public void blockLevelNestedLoopJoin(String datasetOne, String datasetTwo) 
	{
		long start = System.currentTimeMillis();
		DatabaseReader iterator = new DatabaseReader(datasetOne.charAt(0));
		Iterator<String> firstSetIter = new DatabaseReader(datasetOne.charAt(0));
		Iterator<String> secSetIter = new DatabaseReader(datasetTwo.charAt(0));
		int counter = obtainCount(firstSetIter, secSetIter);
		long end = System.currentTimeMillis() - start;
		System.out.println("Building Block-Level Nested-Loop Join takes " + end + " ms");
		System.out.println("Count(*): " + counter);

	}
	/**
	 * HELPER FUNCTION
	 * This function performs the iterations and takes the count of the records satisfying the condition
	 * @param firstIterator
	 * @param secondIterator
	 * @return
	 */
	public int obtainCount(Iterator<String> firstIterator, Iterator<String> secondIterator)
	{
		int counter = 0;
		while (firstIterator.hasNext()) 
		{
			String firstSetRec = firstIterator.next();
			int intParser = Integer.parseInt(firstSetRec.substring(33, 37));

			while (secondIterator.hasNext()) 
			{
				String secSetRec = secondIterator.next();
				int secIntParse = Integer.parseInt(secSetRec.substring(33, 37));
				if (intParser > secIntParse) 
				{
					counter++;
				}
			}
		}
		return counter;
	}

	/**
	 * Determines if a list of aggregates are present
	 * @param agg
	 * @param dataset
	 */
	public void hashBasedAggregation(String[] agg, String dataset) 
	{
		long start = System.currentTimeMillis();

		//initially set the tables to be null
		HashTableBuilder sumCalcTable = null;
		HashTableBuilder averageCalcTable = null;
		for (String string : agg) 
		{
			
			if (equalCheck(string,"sum(randomv)")) 
			{
				//create the table
				sumCalcTable = new HashTableBuilder(100);
			}
			else if (equalCheck(string,"avg(randomv)")) 
			{
				//create the table
				averageCalcTable = new HashTableBuilder(100);
			}
		}
		
		Iterator<String> d = new DatabaseReader(dataset.charAt(0));
		//iterate
		while (d.hasNext()) 
		{
			String record = d.next();
			
			if (sumCalcTable != null) 
			{
				//adds key value pair
				sumCalcTable.addKeyVal(record.substring(16, 19), record.substring(33, 37));
			}
			if (averageCalcTable != null) 
			{
				//adds key value pair
				averageCalcTable.addKeyVal(record.substring(16, 19), record.substring(33, 37));
			}
		}

		// The following will do the Printing
		String[] sumVal = null;
		//initially set to null
		String[] avgVal = null;
		if (sumCalcTable != null) 
		{
			sumVal = sumCalcTable.getTable(); //obtain the table
		}
		if (averageCalcTable != null) 
		{
			avgVal = averageCalcTable.getTable(); //obtain the table
		}
		
		System.out.print("Col2" + " 	");
		
		//as long as we are not null, we want to format the output
		if (sumVal != null) 
		{
			System.out.println(" SUM(RandomV)"); 
		}
		if (avgVal != null) 
		{
			System.out.println(" AVG(RandomV)");
		}

		for (int i = 0; i < 100; i++) 
		{
			int nameNum = i+1;
			System.out.print("Name" + nameNum + "	");

			//as long as we are not null then obtain the sum
			if (sumVal != null) 
			{
				int sumTotal = randomvSumCalculator(sumVal[i]);
				System.out.print(" " + sumTotal);
			}
			//as long as we are not null then obtain the average
			if (avgVal != null) 
			{
				int sumTotal = randomvSumCalculator(avgVal[i]);
				float length = (float) avgVal[i].split(";").length; 
				float avg = sumTotal/length;
				double round = Math.round(avg * 100.0) / 100.0;
				System.out.printf(" %.2f\t", round);
			}

			System.out.println();
		}
		long end = System.currentTimeMillis() - start;
		System.out.println("Hash Aggregation takes " + end + " ms");
	}

	/**
	 * HELPER FUNCTION
	 * Obtains the sum 
	 * @param randomV
	 * @return the sum of values in the record
	 */
	public int randomvSumCalculator(String randomV) 
	{
		String[] values = randomV.split(";");
		int sumTotal = 0;
		for (String str : values) 
		{
			int integerParse = Integer.parseInt(str);
			sumTotal = sumTotal + integerParse;
		}
		return sumTotal;
	}
}
