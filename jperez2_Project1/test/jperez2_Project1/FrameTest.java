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
public class FrameTest 
{
//very brief testing of the frame class
	@Test
	public void testSetAndGetDirty() 
	{
		Frame testFrame = new Frame();
		assertEquals(testFrame.getDirty(), false);
		testFrame.setDirty(true);
		assertEquals(testFrame.getDirty(), true);
		testFrame.setDirty(false);
		assertEquals(testFrame.getDirty(), false);
	}
	@Test
	public void testSetAndGetPinned() 
	{
		Frame testFrame = new Frame();
		assertEquals(testFrame.getPinned(), false);
		testFrame.setPinned(true);
		assertEquals(testFrame.getPinned(), true);
		testFrame.setPinned(false);
		assertEquals(testFrame.getPinned(), false);
	}
	@Test
	public void testSetAndGetBlock() 
	{
		Frame testFrame = new Frame();
		assertEquals(testFrame.getBlock(), -1);
		testFrame.setBlock(10);
		assertEquals(testFrame.getBlock(), 10);
		testFrame.setBlock(-1);
		assertEquals(testFrame.getBlock(), -1);
	}
	@Test
	public void testGetRecord() 
	{
		Frame testFrame = new Frame();
		String content[] = {"test1", "test2"};
		testFrame.setContent(content[0]);
		assertEquals(testFrame.getRecord(1), "test1");
		testFrame.setContent(content[1]);
		assertEquals(testFrame.getRecord(2), "test2");
		
	}
	@Test
	public void testSetRecord() 
	{
		Frame testFrame = new Frame();
		String content[] = {"test1", "test2"};
		testFrame.setContent(content[0]);
		assertEquals(testFrame.getRecord(1), "test1");
		testFrame.setContent(content[1]);
		assertEquals(testFrame.getRecord(2), "test2");
		testFrame.setRecord(2, "test3");
		assertEquals(testFrame.getRecord(2), "test3");
		assertEquals(testFrame.getDirty(), true);
		
	}


}
