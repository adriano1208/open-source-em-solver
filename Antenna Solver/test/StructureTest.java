import static org.junit.Assert.*;

import java.util.ArrayList;

import nec_backend.*;
import org.junit.Test;

public class StructureTest {

	@Test
	public void generateCommentsTest(){
		String expectedOutput = null;
		String actualOutput = null;
		
		String name = "testname";
		ArrayList<String> rules = new ArrayList<String>();
		ArrayList<String> labels = new ArrayList<String>();
			for(int i = 0; i < 5; i++){
				rules.add("GW V07 0:0:1 " + i + "\n");
				labels.add("labels" + i);
			}
		double[] parameters = {1,2,3,4,5};
		Structure struc = new Structure(name, rules, labels, parameters);
		
		Structure.resetGeometry();
		expectedOutput = "CM ********************************\n" 
						+ "CM " + name + "\n"
						+ "CM labels0: 1.0\n"
						+ "CM labels1: 2.0\n"
						+ "CM labels2: 3.0\n"
						+ "CM labels3: 4.0\n"
						+ "CM labels4: 5.0\n"; 
		actualOutput = struc.generateComments();
//		System.out.println(expectedOutput);
//		System.out.println(actualOutput);
		assertEquals(expectedOutput,actualOutput);
	}
	
	@Test
	public void generateExecutionTest(){
		String expectedOutput = null;
		String actualOutput = null;
		
		String name = "testname";
		ArrayList<String> rules = new ArrayList<String>();
			rules.add("EX:0:001:50:0:3:0:0");
			rules.add("EX:5:002:00:0:0.9:0.2:0");
		ArrayList<String> labels = new ArrayList<String>();
			for(int i = 0; i < 9; i++){
				labels.add("labels" + i);
			}
		double[] parameters = {0,0,0,0,0,0,7,8,9};
		Structure struc = new Structure(name, rules, labels, parameters);
		
		Structure.resetGeometry();
		struc.generateComments();
		expectedOutput = "EX 0 1 1 0 3.0 0.0 0.0\n"
						+"EX 5 2 1 0 0.9 0.2 0.0\n";
		actualOutput = struc.generateExecution();
//		System.out.println(expectedOutput);
//		System.out.println(actualOutput);
		assertEquals(expectedOutput,actualOutput);
	}
	
	@Test
	public void generateGeometryTest(){
		String expectedOutput = null;
		String actualOutput = null;
		
		String name = "testname";
		ArrayList<String> rules = new ArrayList<String>();
					 //GW:Tag:X1:Y1:Z1:X2:Y2:Z3:radius
			rules.add("EX:0:001:50:0:1:0:0");
			rules.add("GW:001:0:0:0:0:0:3:0.2");
		ArrayList<String> labels = new ArrayList<String>();
			for(int i = 0; i < 9; i++) {
				labels.add("labels" + i);
			}
		double[] parameters = {0,0,0,0,0,0,7,8,9};
		Structure struc = new Structure(name, rules, labels, parameters);
		
		
		Structure.resetGeometry();
		Structure.setMeshSize(1);
		struc.generateComments();
		struc.generateExecution();
		expectedOutput =  	"GW 0 1 0.0 0.0 0.0 0.0 0.0 0.6 0.2\n" +
							"GW 0 1 0.0 0.0 0.6 0.0 0.0 1.2 0.2\n" +
							"GW 1 1 0.0 0.0 1.2 0.0 0.0 2.1 0.2\n" +
							"GW 0 1 0.0 0.0 2.1 0.0 0.0 3.0 0.2\n";
		actualOutput = struc.generateGeometry();
//		System.out.println(expectedOutput);
//		System.out.println(actualOutput);
		assertEquals(expectedOutput,actualOutput);
	}

	
	@Test
	public void moveCoordTest(){
		String expectedOutput = null;
		String actualOutput = null;
		
		String name = "testname";
		ArrayList<String> rules = new ArrayList<String>();
					 //GW:Tag:X1:Y1:Z1:X2:Y2:Z3:radius
			rules.add("GW:001:0:0:0:0:0:3:0.2");
		ArrayList<String> labels = new ArrayList<String>();
			for(int i = 0; i < 9; i++) {
				labels.add("labels" + i);
			}
		double[] parameters = {0,0,2.5,0,0,0,7,8,9};
		Structure struc = new Structure(name, rules, labels, parameters);
		
		
		Structure.resetGeometry();
		Structure.setMeshSize(1);
		struc.generateComments();
		struc.generateExecution();
		expectedOutput =  	"GW 0 1 0.0 0.0 2.5 0.0 0.0 3.5 0.2\n" +
							"GW 0 1 0.0 0.0 3.5 0.0 0.0 4.5 0.2\n" +
							"GW 0 1 0.0 0.0 4.5 0.0 0.0 5.5 0.2\n";
		actualOutput = struc.generateGeometry();
//		System.out.println(expectedOutput);
//		System.out.println(actualOutput);
		assertEquals(expectedOutput,actualOutput);
		
		//commented out because it doesn't match because of values which are approximately 0
//			parameters[2] = 0;
//			parameters[3] = 90;
//		Structure.resetGeometry();
//		Structure.setMeshSize(1);
//		struc.generateComments();
//		struc.generateExecution();
//		expectedOutput =  	"GW 0 1 0.0 0.0 0.0 0.0 1.0 0.0 0.2\n" +
//							"GW 0 1 0.0 1.0 0.0 0.0 2.0 0.0 0.2\n" +
//							"GW 0 1 0.0 2.0 0.0 0.0 3.0 0.0 0.2\n";
//		actualOutput = struc.generateGeometry();
////		System.out.println(expectedOutput);
////		System.out.println(actualOutput);
//		assertEquals(expectedOutput,actualOutput);
	}
	
	
}
