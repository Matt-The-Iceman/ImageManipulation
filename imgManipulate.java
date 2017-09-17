import java.util.Scanner;
import java.awt.Desktop;
import java.io.*;

/**
 * @title Image Manipulation
 * @author mattj
 * @created 9/17/2017
 * @description:
 * This program will take the name of a .pgm image & a set of commands from the terminal that will
 * instruct the manipulation of the original image.
 * The commands are defined as:
 * 	L: rotate image ccw
 * 	R: rotate image cw
 *  H: flip image horizontally
 *  V: flip image vertically
 * 
 * Please see the README for further details
 * 
 */
public class imgManipulate{
	public static void main(String args[]) {
		//variable initiation
		String inFileName;
		Scanner in = new Scanner(System.in);
		BufferedReader br = null;
		int numRows = 0;
		int numCols = 0;
		String input_rotate; //this will take the user input
		//char[] input_rotate_char; //this will be used to review the input char-by-char
		
		//get file name
		System.out.print("Enter a filename: ");
		inFileName = in.nextLine();
		System.out.println("Opening " + inFileName + ".pgm" + "...");
		
		try {
			br = new BufferedReader(new FileReader(inFileName + ".pgm")); //open the file
			String fileDetails = br.readLine(); //the first line of .pgm are the file details (type numCols numRows maxVal)
			String[] fileDetailsSplit = fileDetails.split(" "); //splits the first line by using " " as delimiter
			//System.out.println(fileDetails); //test line to print file first line
			
			numCols = Integer.parseInt(fileDetailsSplit[1]);
			numRows = Integer.parseInt(fileDetailsSplit[2]);
			
			int[][] imageMatrix = new int[numRows][numCols]; //initialize a 2D matrix of original image
			
			//populates the input file into the 2D matrix
			for(int i = 0; i < numRows; i++) {
				for(int j = 0; j < numCols; j++) {
					if((fileDetails = br.readLine()) != null){
						int nextVal = Integer.parseInt(fileDetails);
						imageMatrix[i][j] = nextVal;
					}
				}
			}

			System.out.println("Original Image Size (Row x Col): " + imageMatrix.length + " " + imageMatrix[0].length);
			
			System.out.print("Enter a command of L/R/H/V (without spaces between): ");
			input_rotate = in.nextLine().toUpperCase(); //takes user input and catches any lowercase inputs
			
			/*
			 * the following segment will manipulate the given matrix by updating the imageMatrix variable after
			 * each manipulation. For example, RVH would rotate right, return that matrix... then flip vert, return image...
			 * then flip horizontal, and return that matrix
			 */
			for(int i = 0; i < input_rotate.length(); i++) {
				System.out.println("Character processed: " + input_rotate.charAt(i)); //gives status to user at which command in being processed
				
				//calls counterclosewise function
				if(input_rotate.charAt(i) == 'L') {
					imageMatrix = CounterClockWise(imageMatrix, numRows, numCols);
					numRows = imageMatrix.length; //updates the row variable to the new matrix details
					numCols = imageMatrix[0].length; //updates the column variable to the new matrix details
					
				}
				//calls clockwise function
				else if(input_rotate.charAt(i) == 'R') {
					imageMatrix = ClockWise(imageMatrix, numRows, numCols);
					numRows = imageMatrix.length; //updates the row varibale to the new matrix size
					numCols = imageMatrix[0].length; //updates the column variable to the new matrix size
					
				}
				//calls horizontal function
				else if(input_rotate.charAt(i) == 'H') {
					imageMatrix = Horizontal(imageMatrix, numRows, numCols);
				}
				//calls vertical function
				else if(input_rotate.charAt(i) == 'V') {
					imageMatrix = Vertical(imageMatrix, numRows, numCols);
				}
				//skips any invalid chars
				else System.out.println("Invalid character '" + input_rotate.charAt(i) + "'. This has been skipped.");
			}
			
			br.close();
			
			/*
			 * the following segment will save the new matrix under the original title + "_edit"
			 */
			
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(inFileName + "_edit.pgm"));
				PrintWriter writerPrint = new PrintWriter(writer); //makes file writing easier
				writerPrint.println(fileDetailsSplit[0] + " " + Integer.toString(numCols) + " " + Integer.toString(numRows) + " " + fileDetailsSplit[3]); //saves first line that contains file details
				for(int x = 0; x < numRows; x++) {
					for(int y = 0; y < numCols; y++) {
						writerPrint.println(imageMatrix[x][y]); //print matrix line by line to file
					}
				}
				writerPrint.close();
				writer.close();
				
				//open file to desktop
				System.out.print("Would you like to open the new image after saving? (y/n): ");
				String cont = in.nextLine();
				
				if(cont.equalsIgnoreCase("y") || cont.equalsIgnoreCase("yes")) {
					File file = new File(inFileName + "_edit.pgm");
					Desktop desk = Desktop.getDesktop();;
					desk.open(file);
				}
				
				//notify user of successful save
				System.out.println("Saved " + inFileName + "_edit.pgm at: " + System.getProperty("user.dir")); //notifies user of successful save
				
			} catch (IOException e) {
				System.err.println("Error saving file. Try again.");
			}
			
			
			
		} catch (FileNotFoundException e) {
			System.err.println("Unable to read the filename: " + inFileName);
		} catch (IOException e) {
			System.err.println("Unable to read the filename: " + inFileName);
		}

	}
	
	/*
	 * NOTE:
	 * The following segment are the method defs for the 4 manipulation possibilities
	 * They could probably be condensed into fewer functions, especially CW/CCW and Horiz/Vert
	 * since they are identical loops/changes to the size of the new matrix.
	 */

	
	/**
	 * This function will rotate the matrix clockwise by creating a new matrix w/ the size being
	 * the row/col size flipped from the original. I.E. orig matrix: 4x3, new matrix: 3x4
	 * 
	 * @param matrix
	 * @param rows
	 * @param cols
	 * @return
	 * 
	 */
	public static int[][] ClockWise(int matrix[][], int rows, int cols){
		int[][] matrix_cw = new int[cols][rows];

		for(int x = 0; x < rows; x++) {
			for( int y = 0; y < cols; y++) {
				matrix_cw[y][x] = matrix[rows - 1 - x][y];
			}
		}
		return matrix_cw;
	}
	/**
	 * This function will rotate the matrix counterclockwise by creating a new matrix w/ the size being
	 * the row/col size flipped from the original. I.E. orig matrix: 4x3, new matrix: 3x4
	 * The new matrix will then be returned for the next evaluation
	 * @param matrix
	 * @param rows
	 * @param cols
	 * @return
	 */
	public static int[][] CounterClockWise(int matrix[][], int rows, int cols){
		int[][] matrix_ccw = new int[cols][rows];
		
		for(int x = 0; x < rows; x++) {
			for( int y = 0; y < cols; y++) {
				matrix_ccw[y][x] = matrix[x][cols - y - 1];
			}
		}
		return matrix_ccw;
	}

	/**
	 * 
	 * @param matrix
	 * @param rows
	 * @param cols
	 * @return
	 */
	public static int[][] Horizontal(int matrix[][], int rows, int cols){
		int[][] matrix_hor = new int[rows][cols];

		for(int y = 0; y < cols; y++) {
			for(int x = 0; x < rows; x++) {
				matrix_hor[x][y] = matrix[rows - 1 - x][y];
			}
		}
	
		return matrix_hor;
	}
	
	/**
	 * 
	 * @param matrix
	 * @param rows
	 * @param cols
	 * @return
	 */
	public static int[][] Vertical(int matrix[][], int rows, int cols){
		int[][] matrix_vert = new int[rows][cols];
		
		for(int x = 0; x < rows; x++) {
			for(int y = 0; y < cols; y++) {
				matrix_vert[x][y] = matrix[x][cols - 1 - y];
			}
		}
		
		return matrix_vert;
	}
}