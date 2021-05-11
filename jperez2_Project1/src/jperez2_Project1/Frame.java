package jperez2_Project1;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Frame 
{
	private String content[];
	private boolean dirtyFlag;
	private boolean pinnedFlag;
	private int blockId; //use -1 of empty
	
	public Frame()
	{
		this.content = new String[100]; //4000 bytes - 40 bytes each
		this.dirtyFlag = false;
		this.pinnedFlag = false;
		this.blockId = -1; //initially empty
	}
	/**
	 * This will primarily be used to reset the block ID to -1 in the buffer pool
	 * @param blockID
	 * @return
	 */
	public Frame initialize(int blockID)
	{
		this.dirtyFlag = false;
		this.pinnedFlag = false;
		this.blockId = blockID; //initially empty
		return this;
	}
	
	//--------GETTERS----------
	/**
	 * Since the content will be put in the next empty index, we want to iterate 
	 * backwards in order to get the most recent content
	 * @return content
	 */
	public String getContent(int recordID)
	{
		//iterate through our content list backwards
		for(int i=(this.content.length-1);i>=0; i--)
		{
			//if we are NOT null
			if(this.content[i]!=null)
			{
				//return the content
				String s = this.content[i];
				String tester [] = s.split("-");
				String testy = tester[0].substring(2,3);
				if(Integer.parseInt(testy) != this.blockId)
				{
					String content = "";
					if(recordID <= 99)
					{
						content = "F0"+this.blockId+"-Rec0"+recordID+", Name0" +recordID+", address0"
								+recordID+", age0"+recordID+".";
					}
					else if(recordID >= 100)
					{
						content = "F0"+this.blockId+"-Rec"+recordID+", Name" +recordID+", address"
								+recordID+", age"+recordID+".";
					}
					//set the content in that location
					this.setContent(content);
				}
				
				return this.content[i];
			}
		}
		return null; // it is empty
	}
	/**
	 * Allows the user to enter which record without having to worry about zero based indexing
	 * @param index
	 * @return content associated with the desired index
	 */
	public String getRecord(int index)
	{
		int removeZeroBased = index - 1;
		if(removeZeroBased >= 0 && removeZeroBased < this.content.length)
		{
			return this.content[removeZeroBased];
		}
		return null; // this will be if they violate conditions
	}
	/**
	 * Gets the dirtyFlag content of the frame
	 * @return
	 */
	public boolean getDirty()
	{
		return this.dirtyFlag;
	}
	/**
	 * Gets the pinnedFlag content of the frame
	 * @return
	 */
	public boolean getPinned()
	{
		return this.pinnedFlag;
	}
	/**
	 * Gets the block ID of the frame
	 * @return
	 */
	public int getBlock()
	{
		return this.blockId;
	}
	//--------SETTERS----------
	/**
	 * Sets the content of the frame 
	 * @param content
	 */
	public void setContent(String content)
	{
		//iterate through the content array
		for (int i = 0; i < this.content.length; i++)
		{
			//if the content is empty, then we put something in
			if(this.content[i]== null)
			{
				//enter the content
				this.content[i] = content;
				//return it to avoid filling all spots in array with the same content
				return;
			}
		}
		
	}
	/**
	 * Populates a new String array to erase the previous content.
	 * Will be useful when we evict a frame and we want to erase the content
	 */
	public void eraseContent()
	{
		this.content = new String[100];
	}
	/**
	 * This sets a particular record at an index
	 * @param index
	 * @param recordName
	 */
	public void setRecord(int index, String recordName)
	{
		int removeZeroBased = index - 1;
		if(index > -1 && removeZeroBased < this.content.length)
		{
			this.content[removeZeroBased] = recordName;
			this.dirtyFlag = true;
		}
		
	}
	/**
	 * Sets the dirty flag to the argument specified
	 * @param dirty
	 */
	public void setDirty(boolean dirty)
	{
		this.dirtyFlag = dirty;
	}
	/**
	 * Sets the pinned status to the argument specified
	 * @param pinned
	 */
	public void setPinned(boolean pinned)
	{
		this.pinnedFlag = pinned;
	}
	/**
	 * Sets the blockID to the argument specified
	 * @param block
	 */
	public void setBlock(int block)
	{
		this.blockId = block;
	}
	/**
	 * This determines if the pinnedFlag is true, if it is then we cannot remove. 
	 * If its not then we can remove it, but we have to remember what to do if its dirty
	 * @return
	 * @throws IOException 
	 */
	public boolean remove()
	{
		if(this.pinnedFlag == true)
		{
			return false; //cannot remove a pinnedFlag
		}
		if(this.dirtyFlag == true) // if its dirty, we need to write contents 
		{
			/*try
			{
				FileWriter writer = new FileWriter("Student/F"+this.blockId+".txt");
				for (String s : this.content)
				{
					writer.write(s);
				}
				writer.close();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			//System.out.print("We get to dirty Frame");*/
			this.dirtyFlag = false; // no longer dirty
		}
		this.blockId = -1; // it is now empty
		return true;
		
	}
	
	
}
