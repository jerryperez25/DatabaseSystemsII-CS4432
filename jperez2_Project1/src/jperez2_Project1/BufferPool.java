/**
 * 
 */
package jperez2_Project1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author jerryperez
 *
 */
public class BufferPool 
{
	private Frame[] buffers;
	private int removableFrames;
	
	public BufferPool(int size)
	{
		this.buffers = new Frame[size];
		this.removableFrames = 0;
		
		//set the buffer with the desired size
		for(int i = 0; i < buffers.length; i++)
		{
			buffers[i] = new Frame(); //create a new frame for each 
		}
	}
	/**
	 * Takes in block ID and determines the slot in the buffer that has it
	 * If it doesnt then we will return -1.
	 * @param blockID
	 * @return i or -1
	 */
	public int searchForBuffSlot(int blockID)
	{
		//iterate through the buffer
		for(int i = 0; i < buffers.length; i++)
		{
			//if we have a matching blockID
			if(this.buffers[i].getBlock() == blockID)
			{
				return i; //return the buffer position that it exists in
			}
		}
		return -1; //the block ID is not in memory
	}
	/**
	 * Takes in the block and finds the associated content with the block 
	 * @param blockID
	 * @return content associated with the targeted block
	 */
	public String findContent(int blockID)
	{
		//find the slot in the buffer where the block is
		int bufferNum = searchForBuffSlot(blockID);
		//if it is not in memory
		if(bufferNum == -1)
		{
			return null; //the block is not in memory
		}
		return this.buffers[bufferNum].getContent(blockID); //it is in memory so we give its content
	}
	/**
	 * Checks to see if the block ID is in the bufferpool and if its not
	 * then we will add it.
	 * Scenario 1: Not in memory but there is an empty frame - add
	 * Scenario 2: No buffer space available
	 * Scenario 3: The block is already in memory
	 * @param blockID
	 * @return either 1, 2, or 3
	 */
	public int emptyBufferAdd(int blockID)
	{
		int bufferNum = searchForBuffSlot(blockID);
		
		if(bufferNum == -1) // not in memory - then add it if there is a free space
		{
			//the block is not in memory, we will find some place to put it
			for (int i = 0; i<this.buffers.length; i++)
			{
				//iterate through the buffer pool to find an empty one
				if(this.buffers[i].getBlock() == -1)
				{
					this.buffers[i].setBlock(blockID); //sets the block in an empty position
					return 1; //this adds the block to a new position
				}
			}
			return 2; //no buffer space available
			
		}
		else
		{
			return 3; //the block is already in memory
		} 
		
	}
	
	/**
	 * Checks the buffer pool to see if any elements are empty
	 * This is based off of the blockID being -1
	 * @return position of an emptySlot
	 */
	public int giveMeEmptySlot()
	{
		//iterate through buffer pool
		for (int i = 0; i < this.buffers.length; i++)
		{
			//if the block is set to -1
			if(this.buffers[i].getBlock() == -1 )
			{
				//return that index
				return i;
			}
		}
		return -1; //this would mean that all slots are full 
	}
	
	
	/**
	 * Follows the call command. There are 4 scenarios:
	 * 1. Calling get when the block is already in memory
	 * 2. The block is not in memory but there are empty slots
	 * 3. The block is not in memory, there are no empty slots, but some can be removed
	 * 4. The block is not in memory, there are no empty slots, and none can be removed
	 * @param recordID
	 */
	public void getRecord(int recordID)
	{
		int blockID = (recordID/100)+1; //take the record id and give corresponding blockID
		//find the location in the buffer with the corresponding block 
		int bufferLoc = searchForBuffSlot(blockID); // either a val or -1
		
		// handles scenario 2
		if(emptyBufferAdd(blockID) == 1) // it has been put in memory
		{
			//it has been put in memory at this point, but we need to determine the contents 
			if(recordID < 99)
			{
				//create the contents of the block since it wasnt already in memory 
				int bufferNum = searchForBuffSlot(blockID);
				String content = "F0"+blockID+"-Rec0"+recordID+", Name0" +recordID+", address0"
						+recordID+", age0"+recordID+".";
				this.buffers[bufferNum].setContent(content); //set the content 
				System.out.println("Output: "+this.buffers[bufferNum].getContent(recordID)+ "; Brought file " +
						+ blockID + " from disk; Placed in Frame " + (bufferNum+1));
			}
			else
			{
				//the recordID is greater than 99 so we handle differently
				int bufferNum = searchForBuffSlot(blockID);
				//determine where the matching block is in our buffer pool
				String content = "F0"+blockID+"-Rec"+recordID+", Name" +recordID+", address"
						+recordID+", age"+recordID+".";
				this.buffers[bufferNum].setContent(content); //set the content
				System.out.println("Output: "+this.buffers[bufferNum].getContent(recordID) +"; Brought file " +
						+ blockID + " from disk; Placed in Frame " + (bufferNum+1));
			}
		}
		//handles scenario 3 and 4
		else if(bufferLoc == -1)  
		{
			//scenario 3 - do we have the ability to remove 
			if(canWeRemove())
			{
				String content = "";
				//create new content for the block that is being added into the pool
				if(recordID <= 99)
				{
					content = "F0"+blockID+"-Rec0"+recordID+", Name0" +recordID+", address0"
							+recordID+", age0"+recordID+".";
				}
				else if(recordID >= 100)
				{
					content = "F0"+blockID+"-Rec"+recordID+", Name" +recordID+", address"
							+recordID+", age"+recordID+".";
				}
				int removalBlock = removeBlock(content); //obtains the block to remove
				int placement = this.removableFrames-1; //determines the placement of the removable frame
				//System.out.println("We want to remove this one: " + placement+1);
				this.buffers[placement].initialize(blockID); //this will remove it by setting blockID to -1
				this.buffers[placement].setContent(content); //set the content with the newly created after evicting
	
				System.out.println("Output: "+content+"; Brought file " + blockID
						+ " from disk; Placed in Frame "+(placement+1)+"; Evicted file "
						+ removalBlock + " from Frame " + (placement+1));
			}
			// scenario 4 - we dont have the ability to remove - we are full
			else
			{
				System.out.println("The corresponding block #" + blockID + " cannot be accessed"
						+ " from disk because the memory buffers are full");
			}
		}
		//handles scenario 1
		else // it is in memory
		{
			int bufferNum = searchForBuffSlot(blockID); //finds the corresponding location
			String content = "";
			if(this.buffers[bufferNum].getContent(recordID) == null)
			{
				//scans the content of the location and determine if it is null
				//if it is then we have the create the content
				if(recordID <= 99)
				{
					content = "F0"+blockID+"-Rec0"+recordID+", Name0" +recordID+", address0"
							+recordID+", age0"+recordID+".";
				}
				else if(recordID >= 100)
				{
					content = "F0"+blockID+"-Rec"+recordID+", Name" +recordID+", address"
							+recordID+", age"+recordID+".";
				}
				//set the content in that location
				this.buffers[bufferNum].setContent(content);
			}
			System.out.println("Output: " + this.buffers[bufferNum].getContent(recordID) + "; File " + blockID
					+ " already in memory; Located in Frame " + (bufferNum+1));
		}
		
	}
	/**
	 * This function handles all of the scenarios listed in the getRecord function.
	 * @param recordID
	 * @param content
	 */
	public void setRecord(int recordID, String content)
	{
		int blockID = (recordID/100)+1; //take the record id and give corresponding file
		int bufferNum = searchForBuffSlot(blockID); //finds the buffer location for the corresponding block id
		Frame targetLoc = null;
	
		//scenario 2 - we are not in memory so we add
		if(emptyBufferAdd(blockID) == 1)
		{
			//if it is not in memory then it will be added to a free slot, now we have to worry about the content
			int bufferLoc = searchForBuffSlot(blockID);
			//set the block with the right id
			this.buffers[bufferLoc].setBlock(blockID);
			//set the block to be dirty
			this.buffers[bufferLoc].setDirty(true);
			//set the content
			this.buffers[bufferLoc].setContent(content);
			System.out.println("Output: Write was successful; Brought File " 
			+ blockID + " from disk; Placed in Frame " + (bufferLoc+1));
		}
		//scenario 3 and 4 - we are not in memory but we may be able to remove
		else if(bufferNum == -1)
		{
			//determine if we can remove
			if(canWeRemove())
			{
				int removalBlock = removeBlock(content); //find the removal block
				int placement = this.removableFrames-1; //find the placement of removal
				//System.out.println("We want to remove this one: " + placement+1);
				this.buffers[placement].initialize(blockID); //remove that block by setting to -1
				this.buffers[placement].setContent(content); //set the contents
				this.buffers[placement].setDirty(true);
	
				System.out.println("Output: Write was successful; Brought file " + blockID
						+ " from disk; Placed in Frame "+(placement+1)+"; Evicted file "
						+ removalBlock + " from Frame " + (placement+1));
			}
			//we cannot remove
			else
			{
				System.out.println("Output: The corresponding block #" + blockID + " cannot be accessed"
						+ " from disk because the memory buffers are full; Write was unsuccessful");
			}
			
		}
		//scenario 1 - already in memory
		else 
		{
			//sets the block with the blockID
			this.buffers[bufferNum].setBlock(blockID);
			//sets to dirty
			this.buffers[bufferNum].setDirty(true);
			System.out.println("Output: Write was successful; File " + blockID
					+ " already in memory; Located in Frame " + (bufferNum+1));
		}
		
		//targetLoc.setContent(content);
		
	}
	/**
	 * This goes through and determines which block needs to be removed.
	 * It also determines if we are able to remove it and it was dirty
	 * then we ought to write it out
	 * @return
	 */
	public int removeBlock(String content)
	{
		//while we have the ability to remove 
		while (canWeRemove())
		{
			if(this.removableFrames == this.buffers.length)
			{
				//if the removable frames are the same length and the buffer pool then reset
				this.removableFrames = 0;
			}
			if(!this.buffers[this.removableFrames].getPinned())
			{
				//the block to be removed
				int previousBlock = this.buffers[this.removableFrames].getBlock();
				
				if(this.buffers[this.removableFrames].remove()==true) //remove it but check to see if its true
				{
					this.buffers[this.removableFrames].setContent(content);
					//if it is true then the flag was dirty and now we have to write out
					//writeOut(this.buffers[this.removableFrames].getContent(), this.buffers[this.removableFrames].getBlock());
				}
				
				this.removableFrames++; //increment
				return previousBlock; //return the previousBlock
			}
			this.removableFrames++; //the frame is pinned so we move to the next
		}
		return -1; //not a valid return - check output again
	}
	/**
	 * Determines if we can remove the block based off of its pin status
	 * @return
	 */
	public boolean canWeRemove()
	{
		//iterate through the buffer
		for (int i = 0; i < this.buffers.length; i++)
		{
			//if it is not pinned
			if(this.buffers[i].getPinned() == false)
			{
				//we get true
				return true;
			}
		}
		//all frames are pinned
		return false;
	}
	
	/**
	 * This pins the block that is being given to memory.
	 * If this is the case then it is not a candidate for removal.
	 * Scenario 1: The block is already in the buffer pool
	 * Scenario 2: The block is not in memory, but it has empty slots or one can be removed 
	 * Scenario 3: The block is not in memory, the buffer pool is full, and there are no removals
	 * @param blockID
	 */
	public void pinFrame(int blockID)
	{
		//find the location with the matching block id
		int bufferNum = searchForBuffSlot(blockID);
		//determines if it is in memory or it has been added already
		if(bufferNum != -1)
		{
			//check the pin status
			if(this.buffers[bufferNum].getPinned() == false) //not already pinned
			{
				//find the location of blockID
				int bufferLoc = searchForBuffSlot(blockID);
				this.buffers[bufferNum].setPinned(true); //it is now pinned
				System.out.println("Output: File " + blockID + " pinned in Frame "+(bufferLoc+1)
						+ "; Not already pinned");
			}
			else //already pinned
			{
				int bufferLoc = searchForBuffSlot(blockID);
				this.buffers[bufferNum].setPinned(true); //it is now pinned
				System.out.println("Output: File " + blockID + " pinned in Frame "+(bufferLoc+1)
						+ "; Already pinned");
			}
		}
		//if it is not in memory then we check if we can remove something 
		else
		{
			if(canWeRemove())
			{
				
				int removalBlock = removeBlock(null); //find the block to remove 
				int placement = this.removableFrames-1; //finds the placement
				this.buffers[placement].initialize(blockID); //removes
				this.buffers[placement].setContent(null); //sets the content to be null because there is no content to it
				Frame targetLoc = this.buffers[placement];
				
				if(targetLoc.getPinned()==false) //not already pinned
				{
					this.buffers[placement].setPinned(true); //set it to pinned
					//this.buffers[placement].eraseContent(); //erase the content of whatever was in there
					
					System.out.println("Output: File " + blockID + 
							" pinned in Frame " + (placement+1) +"; Not already pinned"
							+ "; Evicted file " + removalBlock + " from Frame " + (placement+1));
				}
				else //already pinned
				{	
					this.buffers[placement].setPinned(true); //set to pinned
					//this.buffers[placement].eraseContent(); //erase the content
					System.out.println("Output: File " + blockID + 
							" pinned in Frame " + (placement+1) +"; Already pinned"
							+ "; Evicted file " + removalBlock + " from Frame " + (placement+1));
				}
				
			}
			else //we cannot remove anything
			{
				System.out.println("The corresponding block " + blockID + " cannot be pinned because" +
			" the memory buffers are full");
			}
			
		}
	}
	/**
	 * This unpins the given block and makes it an eligible candidate for removal.
	 * Scenario 1: The block is in memory
	 * Scenario 2: The block is not in memory
	 * @param blockID
	 */
	public void unpinFrame(int blockID)
	{
		//find the corresponding buffer location 
		int bufferLoc = searchForBuffSlot(blockID);
		
		if(bufferLoc != -1) // it is in memory or has been added
		{
			Frame position = this.buffers[bufferLoc];
			//determine if it was already pinned
			boolean result = position.getPinned();
			String status = "";
			
			if(position.getPinned() == true)
			{
				System.out.println("File " + blockID + " in frame " + (bufferLoc+1) + " is unpinned; Frame " + (bufferLoc+1)+" was " +
						"already pinned");
			}
			else if (position.getPinned() == false)
			{
				System.out.println("File " + blockID + " in frame " + (bufferLoc+1) + " is unpinned; Frame " + (bufferLoc+1)+" was not already pinned");
			}

			position.setPinned(false); //no longer pinned
			
		}
		else //not in memory
		{
			System.out.println("The corresponding block "+blockID+" cannot be unpinned because it is"
					+ " not in memory");
		}
		
	}
	/**
	 * This writes the content of the specified block out to disk.
	 * @param content
	 * @param blockID
	 */
	public void writeOut(String content, int blockID)
	{
		try
		{
			//create the location
			BufferedWriter writeToFile = new BufferedWriter(new FileWriter("Dataset/Files/F" + blockID + ".txt"));
			//write the content
			writeToFile.write(content);
			//close it
			writeToFile.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	//-----------Getters-----------
	/**
	 * Obtains the array of buffers.
	 * @return
	 */
	public Frame[] getBuffers()
	{
		return this.buffers;
	}
}
