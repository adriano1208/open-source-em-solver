import static org.junit.Assert.*;

import org.junit.Test;

import nec_backend.Global;
import nec_backend.Helper;

public class HelperTest {

	@Test
	public void getStringTest() {
		String[] inStr = {"V08", "hello", "29.8", "MiLo", "end"};
		String expectedOutput;
		String actualOutput;
		
		expectedOutput = " V08 hello 29.8 MiLo end";
		actualOutput = Helper.getString(inStr);
		assertEquals(expectedOutput, actualOutput);
		
		expectedOutput = " 29.8 MiLo end";
		actualOutput = Helper.getString(inStr, 2);
		assertEquals(expectedOutput, actualOutput);
		
		expectedOutput = "";
		actualOutput = Helper.getString(inStr, 5);
		assertEquals(expectedOutput, actualOutput);
	}
	
	@Test
	public void substituteVariableTest() {
		double[] parameters = {0,1,2,3,4};
		String expectedOutput;
		String actualOutput;
		
		expectedOutput = "7.0";
		actualOutput = Helper.substituteVariable("7", parameters);
		assertEquals(expectedOutput, actualOutput);
		
		expectedOutput = "1.0";
		actualOutput = Helper.substituteVariable("V01", parameters);
		assertEquals(expectedOutput, actualOutput);
		
		expectedOutput = "0.0";
		actualOutput = Helper.substituteVariable("V00000000", parameters);
		assertEquals(expectedOutput, actualOutput);
		
		expectedOutput = "4.0";
		actualOutput = Helper.substituteVariable("V4", parameters);
		assertEquals(expectedOutput, actualOutput);
		
		Global.setFrequency(300);
		expectedOutput = "300.0";
		actualOutput = Helper.substituteVariable("f", parameters);
		assertEquals(expectedOutput, actualOutput);
	}
	
	@Test
	public void bracketsTest() {
//		String inStr;
		double[] parameters = {0,1,2,3,4};
		String expectedOutput;
		String actualOutput;
		
		expectedOutput = "12.0";
		actualOutput = Helper.performOperation("12", parameters);
		assertEquals(expectedOutput, actualOutput);
		
		expectedOutput = "1.0";
		actualOutput = Helper.performOperation("(1)", parameters);
		assertEquals(expectedOutput, actualOutput);
	}
	
	@Test
	public void solveLineTest() {
		String[] inStr = new String[5];
		String[] outStr = new String[5];
		double[] parameters = {0,1,2,3,4,5};
		String expectedOutput;
		String actualOutput;
		
			inStr[0] = "-5+3";;
			inStr[1] = " 5 + 3 ";
			inStr[2] = "5-3";
			inStr[3] = "5 * 3";
			inStr[4] = "9/3";
			outStr = Helper.solveLine(inStr,parameters);
		expectedOutput = "-8.0";
		actualOutput = outStr[0]; 
		assertEquals(expectedOutput, actualOutput);
		expectedOutput = "8.0";
		actualOutput = outStr[1]; 
		assertEquals(expectedOutput, actualOutput);
		expectedOutput = "2.0";
		actualOutput = outStr[2]; 
		assertEquals(expectedOutput, actualOutput);
		expectedOutput = "15.0";
		actualOutput = outStr[3]; 
		assertEquals(expectedOutput, actualOutput);
		expectedOutput = "3.0";
		actualOutput = outStr[4];
		assertEquals(expectedOutput, actualOutput);
		
			inStr[0] = "(5+3)*2";;
			inStr[1] = "1-2+4*8/16";
			inStr[2] = "(((5*3)+5)/4)";
			inStr[3] = "(1+2)*(3+4)";
			inStr[4] = "(1+2)*1+2";
			outStr = Helper.solveLine(inStr,parameters);
		expectedOutput = "16.0";
		actualOutput = outStr[0]; 
		assertEquals(expectedOutput, actualOutput);
		expectedOutput = "-3.0";
		actualOutput = outStr[1]; 
		assertEquals(expectedOutput, actualOutput);
		expectedOutput = "5.0";
		actualOutput = outStr[2]; 
		assertEquals(expectedOutput, actualOutput);
		expectedOutput = "21.0";
		actualOutput = outStr[3]; 
		assertEquals(expectedOutput, actualOutput);
		expectedOutput = "5.0";
		actualOutput = outStr[4];
		assertEquals(expectedOutput, actualOutput);
		
			inStr[0] = "(5+V03)*V02";;
			inStr[1] = "V01-V02+V04*8/16";
			inStr[2] = "(((5*V03)+5)/V04)";
			inStr[3] = "-(V01+V02)*(V03+V04)";
			inStr[4] = "-V01";
			outStr = Helper.solveLine(inStr,parameters);
		expectedOutput = "16.0";
		actualOutput = outStr[0]; 
		assertEquals(expectedOutput, actualOutput);
		expectedOutput = "-3.0";
		actualOutput = outStr[1]; 
		assertEquals(expectedOutput, actualOutput);
		expectedOutput = "5.0";
		actualOutput = outStr[2]; 
		assertEquals(expectedOutput, actualOutput);
		expectedOutput = "-21.0";
		actualOutput = outStr[3]; 
		assertEquals(expectedOutput, actualOutput);
		expectedOutput = "-1.0";
		actualOutput = outStr[4];
		assertEquals(expectedOutput, actualOutput);
	}
	
	@Test
	public void getLengthTest() {
		double expectedOutput;
		double actualOutput;
		
		expectedOutput = 3.0;
		actualOutput = Helper.getLength(1,-5,2,2,-3,0);
		assertEquals(expectedOutput, actualOutput,0.0001);
	}
	
	@Test
	public void getUnitVectorTest() {
		double[] expectedOutput = new double[3];
		double[] actualOutput = new double[3];
		double[] badInput = {2,3};
		
		expectedOutput[0] = 0.0;
		expectedOutput[1] = 0.0;
		expectedOutput[2] = 0.0;
		actualOutput = Helper.getUnitVector(badInput,expectedOutput);
		assertEquals(actualOutput[0], expectedOutput[0], 0.0001);
		assertEquals(actualOutput[1], expectedOutput[1], 0.0001);
		assertEquals(actualOutput[2], expectedOutput[2], 0.0001);
		
		expectedOutput[0] = 2.0/3.0;
		expectedOutput[1] = 2.0/3.0;
		expectedOutput[2] = 1.0/3.0;
		actualOutput = Helper.getUnitVector(0,0,0,2,2,1);
		assertEquals(actualOutput[0], expectedOutput[0], 0.0001);
		assertEquals(actualOutput[1], expectedOutput[1], 0.0001);
		assertEquals(actualOutput[2], expectedOutput[2], 0.0001);
	}

//	@Test
//	public void moveCoordTest() {
//		double[] expectedOutput = new double[3];
//		double[] actualOutput = new double[3];
//		double[] parameters = {0,0,0,0,0,0};	
//		double[] p = {1,1,-1};
//		
//		parameters[3] = -90;
//		parameters[4] = 45;
//		parameters[5] = 0;
//		expectedOutput[0] = Math.sqrt(3);
//		expectedOutput[1] = 1;
//		expectedOutput[2] = 1;
//		actualOutput = Helper.moveCoord(p, parameters);
//		assertEquals(actualOutput[0], expectedOutput[0], 0.0001);
//		assertEquals(actualOutput[1], expectedOutput[1], 0.0001);
//		assertEquals(actualOutput[2], expectedOutput[2], 0.0001);		
//	}


}

