package nec_backend;

import java.util.ArrayList;

public class Helper {
	
	public static String padRight(String s, int n) {
	     return String.format("%1$-" + n + "s", s);  
	}
	public static String padRight(double d) {
		return padRight(Double.toString(Math.round(d*100000000.0)/100000000.0), 9);
	}

	public static String getString(String[] inStr, int from){
		String returnStr = "";
		for (int i = from; i < inStr.length; i++){
			returnStr = returnStr.concat(" " + inStr[i]);
		}
		return returnStr;
	}
	public static String getString(String[] inStr){
		return getString(inStr, 0);
	}
	
	public static String[] solveLine(String[] inStr, double[] parameters){
		for (int i=0; i < inStr.length; i++){
			if (inStr[i].contains("(") || inStr[i].contains(")") ||
					inStr[i].contains("+") || inStr[i].contains("-") ||
					inStr[i].contains("*") || inStr[i].contains("/")){
				inStr[i] = performOperation(inStr[i], parameters);
			} else //case where there is only one variable or value
				inStr[i] = substituteVariable(inStr[i], parameters); 
		}
		return inStr;
	}

	public static double substituteVariableD(String inStr, double[] parameters) {
		inStr.trim();
		if (inStr.contains("V")){
			inStr = inStr.split("V",3)[1]; //1 or 0?
			return parameters[Integer.parseInt(inStr)];
		} else if (inStr.contains("f")) {
			return Global.f_high();
		} else {
			inStr = inStr.replaceAll("[a-zA-Z]", "0");
			return Double.parseDouble(inStr);
		}
	}
	
	public static String substituteVariable(String inStr, double[] parameters) {
		return Double.toString(substituteVariableD(inStr, parameters));
	}
		
	public static String performOperation(String inStr, double[] parameters) {
		int counter = 0;
		int index1;
		int index2;
		int index_moving;
		String regex = null;
		String replacement = null;
		
		while (inStr.contains("(") || inStr.contains(")")){
			if (inStr.contains("(") && inStr.contains(")")){
				counter = 1;
				index1 = inStr.indexOf("(");
				index2 = 0;
				index_moving = index1;
				while (counter > 0){			
					index2 = inStr.indexOf(")",index2+1);
					index_moving = inStr.indexOf("(",index_moving+1);
					if (index_moving > index2 || index_moving == -1){
						counter--;
					}
				}
				regex = inStr.substring(index1+1,index2);
				replacement = performOperation(regex, parameters);
				inStr = inStr.substring(0, index1) + replacement 
						+ inStr.substring(index2+1);
			} //else
		//		throw new IOException(); //uneven brackets
		}
		char[] str = inStr.toCharArray();
		ArrayList<String> operators = new ArrayList<String>();
		ArrayList<String> operands = new ArrayList<String>();
		ArrayList<Double> values = new ArrayList<Double>();
		String temp = "";
		for (int i=0; i < str.length; i++){
			if ( str[i] == '/' || str[i] == '*' || str[i] == '+' || str[i] == '-'){
				//to account for case where first operator is not a bracket
				//and is the first element in the input String (e.g. "-V07")
				if (i == 0)
					temp = "0"; 
				operators.add(String.valueOf(str[i]));
				operands.add(temp);
				temp = "";
			} else {
				temp = temp.concat(String.valueOf(str[i]));
			}
		}
		operands.add(temp);
		//convert variables (VXX) to doubles for calculations
		for (String operand : operands) {
			values.add(substituteVariableD(operand, parameters));
		}
		if (!operators.isEmpty()) {
			for (int i=0; i < operators.size(); i++) {
				if (operators.get(i).contains("/")) {
					values.set(i, values.get(i)/values.get(i+1));
					values.remove(i+1);
					operators.remove(i);
					i--;
				} 
			}
		}
		if (!operators.isEmpty()) {
			for (int i=0; i < operators.size(); i++) {
				if (operators.get(i).contains("*")) {
					values.set(i, values.get(i)*values.get(i+1));
					values.remove(i+1);
					operators.remove(i);
					i--;
				} 
			}
		}
		if (!operators.isEmpty()) {
			for (int i=0; i < operators.size(); i++) {
				if (operators.get(i).contains("+")) {
					values.set(i, values.get(i)+values.get(i+1));
					values.remove(i+1);
					operators.remove(i);
					i--;
				} 
			}
		}
		if (!operators.isEmpty()) {
			for (int i=0; i < operators.size(); i++) {
				if (operators.get(i).contains("-")) {
					values.set(i, values.get(i)-values.get(i+1));
					values.remove(i+1);
					operators.remove(i);
					i--;
				} 
			}
		}
		
//		System.out.println("Calculated Value: " + values.get(0) + "\n");
		
		return Double.toString(values.get(0));
	}
	
	//public static void performLoop(ArrayList<String[]> inStr, int iterations){
	//	//TODO
	//}
	
	public static double getLength(double x1, double y1, double z1,
							double x2, double y2, double z2){
		return Math.sqrt( (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1) + (z2-z1)*(z2-z1) );
	}
	public static double getLength(double[] pos1, double[] pos2){
		if (pos1.length < 3 || pos2.length < 3)
			return -1;
		double x1 = pos1[0];
		double y1 = pos1[1];
		double z1 = pos1[2];
		double x2 = pos2[0];
		double y2 = pos2[1];
		double z2 = pos2[2];
		return getLength(x1, y1, z1, x2, y2, z2);
	}
	
	public static double[] getUnitVector(double x1, double y1, double z1,
								double x2, double y2, double z2){
		double norm = getLength(x1,y1,z1,x2,y2,z2);
		double[] vector = {(x2-x1)/norm,(y2-y1)/norm,(z2-z1)/norm};
		return vector;
	}
	public static double[] getUnitVector(double[] pos1, double[] pos2){
		if (pos1.length < 3 || pos2.length < 3){
			double[] noDirection = {0,0,0};
			return noDirection;
		}
		double x1 = pos1[0];
		double y1 = pos1[1];
		double z1 = pos1[2];
		double x2 = pos2[0];
		double y2 = pos2[1];
		double z2 = pos2[2];
		return getUnitVector(x1, y1, z1, x2, y2, z2);
	}
	
	
	  public static double[] BubbleSort(double[] pos) {
		    double temp;
		    for (int i = pos.length; i >= 0; i--) {
		    	for (int j=0; j < pos.length-1; j++){
		    		if (pos[j] > pos[j+1]){
		    			temp = pos[j];
		    			pos[j] = pos[j+1];
		    			pos[j+1] = temp;
		    		}
		    	}
		    }
		    return pos;
		 }
	  public static int[] BubbleSort(double[] pos, int[] tag) {
		    double temp;
		    for (int i = pos.length; i >= 0; i--) {
		    	for (int j=0; j < pos.length-1; j++){
		    		if (pos[j] > pos[j+1]){
		    			temp = pos[j];
		    			pos[j] = pos[j+1];
		    			pos[j+1] = temp;
		    			
		    			temp = tag[j];
		    			tag[j] = tag[j+1];
		    			tag[j+1] = (int)Math.round(temp);
		    		}
		    	}
		    }
		    return tag;
		 }
	  
	  public static double[] coordAdd(double[] a, double[] b) {
		  int length = a.length;
		  if (b.length < length)
			  length = b.length;
		  double[] ans = new double[length];
		  for (int i=0; i < length; i++) {
			  ans[i] = a[i] + b[i];
		  }
		  return ans;
	  }
	  public static double[] coordMult(double[] a, double[] b) {
		  int length = a.length;
		  if (b.length < length)
			  length = b.length;
		  double[] ans = new double[length];
		  for (int i=0; i < length; i++) {
			  ans[i] = a[i] * b[i];
		  }
		  return ans;
	  }
	  public static double[] coordAdd(double[] a, double b) {
		  double[] ans = new double[a.length];
		  for (int i=0; i < a.length; i++) {
			  ans[i] = a[i] + b;
		  }
		  return ans;
	  }
	  public static double[] coordMult(double[] a, double b) {
		  double[] ans = new double[a.length];
		  for (int i=0; i < a.length; i++) {
			  ans[i] = a[i] * b;
		  }
		  return ans;
	  }
	  
		public static double[] moveCoord(double[] p, double[] parameters) {
			double x0 = p[0];
			double y0 = p[1];
			double z0 = p[2];
			double ax = Math.toRadians(parameters[3]);
			double ay = Math.toRadians(parameters[4]);
			double az = Math.toRadians(parameters[5]);
			//individual rotations don't seem to work when combined
//			//rotation about x-axis
//			angle = Math.toRadians(_parameters[3]);
//			x = x0;
//			y = y0*Math.cos(angle) - z0*Math.sin(angle);
//			z = y0*Math.sin(angle) + z0*Math.cos(angle);
//			x0 = x; y0 = y; z0 = z;
//			//rotation about y-axis
//			angle = Math.toRadians(_parameters[4]);
//			x = x0*Math.cos(angle) + z0*Math.sin(angle);
//			y = y0;
//			z = - x0*Math.sin(angle) + z0*Math.cos(angle);
//			x0 = x; y0 = y; z0 = z;
//			//rotation about z-axis
//			angle = Math.toRadians(_parameters[5]);
//			x = x0*Math.cos(angle) - y0*Math.sin(angle);
//			y = x0*Math.sin(angle) + y0*Math.cos(angle);
//			z = z0;
			//combined rotation matrix
			double x = x0*(Math.cos(ay)*Math.cos(az)) + 
					y0*(Math.cos(ax)*Math.sin(az) + Math.sin(ax)*Math.sin(ay)*Math.cos(az)) + 
					z0*(Math.sin(ax)*Math.sin(az) - Math.cos(ax)*Math.sin(ay)*Math.cos(az));
			double y = x0*(-Math.cos(ay)*Math.sin(az)) + 
					y0*(Math.cos(ax)*Math.cos(az) - Math.sin(ax)*Math.sin(ay)*Math.sin(az)) + 
					z0*(Math.sin(ax)*Math.cos(az) + Math.cos(ax)*Math.sin(ay)*Math.sin(az));
			double z = x0*(Math.sin(ay)) + y0*(-Math.sin(ax)*Math.cos(ay)) + z0*(Math.cos(ax)*Math.cos(ay));
			//translation
			p[0] = x + parameters[0];
			p[1] = y + parameters[1];
			p[2] = z + parameters[2];
			return p;
		}
}
