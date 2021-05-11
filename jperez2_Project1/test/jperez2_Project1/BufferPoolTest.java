/**
 * 
 */
package jperez2_Project1;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

/**
 * @author jerryperez
 *
 */
public class BufferPoolTest 
{
//very brief testing of the buffer pool class
	@Test
	public void testBuffSlotSearcher()  
	{
		BufferPool testBuff = new BufferPool(3);
		testBuff.getBuffers()[1].setBlock(10); //set the first frame with blockID 10
		assertEquals(testBuff.searchForBuffSlot(10), 1); //the block is there
		assertEquals(testBuff.searchForBuffSlot(20), -1); //not there
	}
	@Test
	public void testNonExistentBlockAdd()
	{
		BufferPool testBuff = new BufferPool(3);
		testBuff.emptyBufferAdd(9); //the block doesnt exist so we should add in an empty location
		assertEquals(testBuff.getBuffers()[0].getBlock(), 9); //obtains the content for the block id
	}
	@Test
	public void testNonExistentBlockAddWithOtherEntries()
	{
		BufferPool testBuff = new BufferPool(3);
		testBuff.getBuffers()[0].setBlock(10); //first position is not free so we should add to second
		testBuff.emptyBufferAdd(9); //the block doesnt exist so we should add in an empty location
		assertEquals(testBuff.getBuffers()[1].getBlock(), 9); //obtains the content for the block id
		
	}
	@Test
	public void testGivingAnEmptyFrame()
	{
		BufferPool testBuff = new BufferPool(3);
		assertEquals(testBuff.giveMeEmptySlot(), 0); //should return 0 since all spaces are empty 
		
	}
	@Test
	public void testGivingAnEmptyFrameWithAllFull()
	{
		BufferPool testBuff = new BufferPool(3);
		testBuff.getBuffers()[0].setBlock(10); //fill space 0
		testBuff.getBuffers()[1].setBlock(11); //fill space 1
		testBuff.getBuffers()[2].setBlock(12); //fill space 2
		assertEquals(testBuff.giveMeEmptySlot(), -1); //should return -1 since all spaces are full 
		
	}
	
}
