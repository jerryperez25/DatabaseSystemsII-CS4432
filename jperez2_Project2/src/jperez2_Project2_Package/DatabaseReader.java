package jperez2_Project2_Package;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

public class DatabaseReader implements Iterator<String> 
{
	private String directoryName = "Project2Dataset";
	private File directory = new File(directoryName);
	private int fileCount = 0;
	private int recCount = 100;
	private int currentFile = 0;
	private int currentRecord = 0;
	private char[] target; 
	
	public DatabaseReader(String directoryName, File directory, int fileCount, int recCount, int currentFile, int currentRecord, char[] target)
	{
		this.directoryName = directoryName;
		this.directory = directory;
		this.fileCount = fileCount;
		this.recCount = recCount;
		this.currentFile = currentFile;
		this.currentRecord = currentRecord;
		this.target = target;
	}
	
	public DatabaseReader()
	{
		File[] files = directory.listFiles();
		fileCount = files.length;
		currentFile = 1;
		currentRecord = 1;
		target = read(1);
	}
	//-----------Getters--------
	public String getDirectoryName()
	{
		return this.directoryName;
	}
	public File getDirectory()
	{
		return this.directory;
	}
	public int getFileCount()
	{
		return this.fileCount;
	}
	public int getRecordCount()
	{
		return this.recCount;
	}
	public int getCurrFile()
	{
		return this.currentFile;
	}
	public int getCurrRecord()
	{
		return this.currentRecord;
	}
	public char[] getTargetBlock()
	{
		return this.target;
	}
	public static void printString(String string)
	{
		String[] fileRec = string.split(";");

        for (String s: fileRec) 
        {
        	String stringSub = s.substring(0,2);
        	int intParse = Integer.parseInt(stringSub);
            s = s.substring(3);
            String[] records = s.split(",");
            int recordLength = records.length;
            int[] recordArr = new int[recordLength];

            for (int i = 0; i < records.length; i++) 
            {
            	String itrRecElement = records[i];
            	recordArr[i] = Integer.parseInt(itrRecElement);
            }

            Scanner scan; 
	        try 
	        {
	        	Scanner newScanner = new Scanner(new File("Project2Dataset/F" + intParse + ".txt"));
	        	scan = newScanner.useDelimiter("\\Z");
	            
	        	String targetBlock = scan.next();

	            for (int i : recordArr) 
	            {
	                int firstElement = (i-1) * 40 ;
	                int secondElement = i*40;
	                String targetSubstring = targetBlock.substring(firstElement, secondElement);
	                System.out.println(targetSubstring);
	            }

	        } 
	        catch (IOException e) 
	        {
	            System.out.println("The file cannot be found" + intParse);
	            e.printStackTrace();
	        }
        }
        System.out.println("The number of files read is: " + fileRec.length + ", which is: " + fileRec.length + " I/O(s)");

	}
	/**
	 * Reads the given file ID and returns it. 
	 * @param file
	 * @return
	 */
	public char[] read(int file)
	{
		Scanner scan; //create scanner object
		try
		{
			Scanner newScan = new Scanner(new File("Project2Dataset/F" + file + ".txt"));
			scan = newScan.useDelimiter("\\Z");
		}
		catch (IOException e)
		{
			this.currentFile = this.getFileCount();
            this.currentRecord = this.getRecordCount() - 1;
            System.out.println("The file cannot be found" + file);
            //e.printStackTrace(); //tidy up
            return null;

		}
		String nextStr = scan.next();
		char[] toChar = nextStr.toCharArray();
		return toChar;
	}
	/**
	 * Implemented from iterator 
	 */
	public String next()
	{
        char[] record = new char[40]; //designate the number of bytes in a given record
        int determinant = (currentRecord -1) * 40;
        int arrayFieldFour = 0;
        int arrayFieldFive = 40;
        System.arraycopy(target, determinant, record, arrayFieldFour, arrayFieldFive);
        if (this.getCurrRecord() == this.getRecordCount()) 
        {
            this.currentRecord = 1;
            this.currentFile++;
            this.target = read(this.getCurrFile());
        }
        this.currentRecord++;
        String returnStr = new String(record);
        return returnStr;

	}
	@Override
	public boolean hasNext() 
	{
		if (this.currentRecord == this.recCount) 
		{
            return this.getCurrFile() != this.getFileCount();
        }
        return true;
	}
}
