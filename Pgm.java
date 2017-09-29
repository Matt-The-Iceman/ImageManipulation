/*
Joseph Jackels
9/19/17

PGM file manipulation, can flip horizontally, vertically, and rotate clockwise or counter clockwise
reduces input to simplist terms of operations in fashion /$ java Pgm manipulationString inputFile.pgm outputFile.pgm;

ex: 

0-original
h-horizontal operation on original
v-vertical operation on original
r-clockwise rotation of original

  0       h       v      r
1 2 3 | 3 2 1 | 7 8 9 | 7 4 1
4 5 6 | 6 5 4 | 4 5 6 | 8 5 2
7 8 9 | 9 8 7 | 1 2 3 | 9 6 3

*/
import java.util.Scanner;
import java.io.*;

public class Pgm {
	public static int[][] manipulate(int[][] array, char dir){
		int height = array.length;
		int width = array[0].length;
		int[][] temp = new int[0][0];
		//flip horizontally
		if(dir == 'H'){
			temp = new int[height][width];
			for(int i = 0; i < height; i++){
				for(int j = 0; j < width; j++){
					temp[i][j] = array[i][width - j - 1];
				}
			}
		}
		//flip vertically
		if(dir == 'V'){
			temp = new int[height][width];
			for(int i = 0; i < height; i++){
				for(int j = 0; j < width; j++){
					temp[i][j] = array[height - i - 1][j];
				}
			}
		}
		//rotate 90 degrees clockwise
		if(dir == 'R'){
			temp = new int[width][height];
			for(int i = 0; i < width; i++){
				for(int j = 0; j < height; j++){
					temp[i][j] = array[height - j - 1][i];
				}
			}
		}
		//rotate 90 degrees counter clockwise
		if(dir == 'L'){
			temp = new int[width][height];
			for(int i = 0; i < width; i++){
				for(int j = 0; j < height; j++){
					temp[i][j] = array[j][width - i - 1];
				}
			}
		}

		return temp;
	}
	
	public static String condense(String input){
		//condenses to 0, H, V, HR, R, HR, VR, or L
		int hCount = 0;
		int vCount = 0;
		int rCount = 0;
		for(int i = 0; i < input.length(); i++){
			char ch = Character.toUpperCase(input.charAt(i));
			if(ch == 'H' || ch == 'V'){
				if(rCount%2 == 0){
					if(ch == 'H'){
						hCount++;
					}
					else{
						vCount++;
					}
				}
				else{
					if(ch == 'H'){
						vCount++;
					}
					else{
						hCount++;
					}
				}
			}
				else{
					if(ch == 'R'){
						rCount++;
					}
					else{
						rCount--;
					}
				}
		}
		hCount = hCount%2;
		vCount = vCount%2;
		rCount = rCount%4;
		String output = "";
		if(hCount > 0){
			output += "H";
		}
		if(vCount > 0){
			output += "V";
		}
		if(rCount == 1){
			output += "R";
		}
		else if(rCount == 2){//HV == RR
			output += "HV";
			output = condense(output);
		}
		else if(rCount == 3 || rCount == -1){//HVR == L, 3 R = 1 L
			output += "HVR";
			output = condense(output);
		}
		if(output == "HVR"){//HVR == L
			output = "L";
		}
		return output;
	}

	public static void main(String[] args) throws IOException {
		//first string = manipulations
		//second string = input file
		//third string = output file
		String manip = args[0];
		File inFile = new File(args[1]);
		File outFile = new File(args[2]);
		outFile.createNewFile();

		Scanner in = new Scanner(inFile);
		PrintWriter writer = new PrintWriter(outFile);

		String magicnum = in.next();
		int width = in.nextInt();
		int height = in.nextInt();
		int maxval = in.nextInt();

		int[][] map= new int[height][width];
		//System.out.println("Original: ");
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				map[i][j] = in.nextInt();
				//System.out.print(map[i][j] + " ");
			}
			//System.out.println();
		}
		//System.out.println();

		System.out.println("Original Manipulation: " + manip + " condensed to: " + condense(manip));
		manip = condense(manip);

		int[][] test = new int[height][width];
		test = manipulate(map, manip.charAt(0));
		for(int i = 1; i < manip.length(); i++){
			test = manipulate(test, manip.charAt(i));
		}
		writer.println("P2 " + test.length + " " + test[0].length + " " + maxval);
		//writer.flush();
		//System.out.println("Post manipulation of " + manip + ": ");
		//System.out.println();
		for(int i = 0; i < test.length; i++){
			for(int j = 0; j < test[0].length; j++){
				//System.out.print(test[i][j] + " ");
				writer.println(test[i][j]);
			}
			//System.out.println();
		}

		in.close();
		//writer.flush();
		writer.close();
		writer.flush();
	}
}