/////////////////////////////////////////////////////////////////////////////
// Semester:         CS367 Fall 2017 
// PROJECT:          Running Median
// FILE:             MedianStream.java
//
// TEAM:    individual
// Author:  Zhenyu Zou
// email: zzou24@wisc.edu
// netID: 907 593 6980
// Lecture number: 001
//
//
// ---------------- OTHER ASSISTANCE CREDITS 
// Persons: Identify persons by name, relationship to you, and email. 
// Describe in detail the the ideas and help they provided. 
// 
// Online sources: Piazza Discussions
//////////////////////////// 80 columns wide //////////////////////////////////

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Currency;
import java.util.Scanner;

public class MedianStream
{

    private static final String PROMPT_NEXT_VALUE = "Enter next value or q to quit: ";
    private static final String MEDIAN = "Current median: ";
    private static final String EXIT_MESSAGE = "That wasn't a double or 'q'. Goodbye!";
    private static final String FNF_MESSAGE = " not found.";
    
 
    /**
     * Use this format to ensure that double values are formatted correctly.
     * Double doubleValue = 1412.1221132
     * System.out.printf(DOUBLE_FORMAT, doubleValue);
     */
    private static final String DOUBLE_FORMAT = "%8.3f\n";

    private Double currentMedian;
    private MaxPQ<Double> maxHeap;
    private MinPQ<Double> minHeap;

    /**
     * Override Default Constructor
     *
     *  Initialize the currentMedian = 0.0
     *  Create a new MaxPQ and MinPQ.
     */
    public MedianStream()
    {
        this.currentMedian = 0.0;
        this.maxHeap = new MaxPQ<Double>();
        this.minHeap = new MinPQ<Double>();
    }

    /**
     * This method is called if the user passes NO command line arguments.
     * The method prompts the user for a double value on each iteration.
     *
     * If the input received is a double, the current median is updated.
     * After each iteration, print the new current median using MEDIAN string
     * as declared and initialized with the data members above.
     *
     * If the input is the character 'q', return from the method.
     *
     * If the input is anything else, then you print an error using EXIT_MESSAGE
     * string as declared and initialized with the data members above and
     * then return from the method.
     *
     * For the purposes of calculating the median, every input received since
     * the beginning of the method execution is part of the same stream.
     */
    private static void runInteractiveMode()
    {
    	// initialize a temporary median in this class
		MedianStream tempMedian = new MedianStream();
		
		// initialize the scanner input
		Scanner input = null;
		
		// set a median variable
		double median = 0.0;
		
		// boolean statement to control the loop
    	boolean isDone = false;
    	
    	// continues while the isDone is false
    	while(!isDone)
    	{
    		// pass the incoming input stream
    	  	input = new Scanner(System.in);
    	  	
    	  	// print out a message
	    	System.out.print(PROMPT_NEXT_VALUE);
	    	
	    	// if the next item is in double
    		if(input.hasNextDouble())
    		{
    			// get the median value
    			median = tempMedian.getMedian(input.nextDouble());
    			
    			// print the message
    			System.out.printf(MEDIAN + DOUBLE_FORMAT , median);
    		}
    		// if the next input is invalid
    		else if(input.hasNext())
    		{
    			// if the input is "q"
    			if(input.next().equalsIgnoreCase("q"))
    			{
    				// exit the loop and return
    				isDone = true;
    				break;
    			}
    			// if the input is something else
    			else
	    		{
    				// print the message
    				System.out.println(EXIT_MESSAGE);
    				
    				// exit the loop and return
	    			isDone = true;
	    			break;
	    		}
    		}
    	}
    	// close the scanner
    	input.close();
    }

    /**
     * This method is called if the user passes command line arguments.
     * The method is called once for every filename passed by the user.
     *
     * The method reads values from the given file and writes the new median
     * after reading each new double value to the output file.
     *
     * The name of the output file follows a format specified in the write-up.
     *
     * If the input file contains a non-double value, the program SHOULD NOT
     * throw an exception, instead it should just read the values up to that
     * point, write medians after each value up to that point and then
     * return from the method.
     *
     * If a FileNotFoundException occurs, just print the string FNF_MESSAGE
     * as declared and initialized with the data members above.
     */
    private static void findMedianForFile(String filename)
    {
    	// handle the exception that the file does not exist
		try{
			// initialize the file
			File aFile = new File(filename);
			
			// use scanner to receive the file content
			Scanner input = new Scanner(aFile);
			
			// initialize the temporary median in this class
			MedianStream tempMedian = new MedianStream();
			
			// set the median
			double median = 0.0;
			
			// find the position that is dot of the file name
			int pos =  filename.lastIndexOf(".");
			
			// attach the original file name to the new output file name
			String outname =  filename.substring(0,pos)+ "_out.txt";
			
			// create a new output file
			File outFile = new File(outname);
			
			// write the file
			PrintWriter writer = new PrintWriter(outFile);
			
			// continues while the input has next item
			while(input.hasNext())
			{
				// initialize a value
				Double value = null;
				
				// handle the condition that the next item is not valid
				try{
					// parse the incoming to string
					String inComing = input.next();
					
					// parse the string to double, exception may occur here
					value = Double.parseDouble(inComing);
					
				// if the exception is found, continue to the next item
				}catch(IllegalArgumentException e)
				{
					continue;
				}
				
				// get median from the get median method
				median = tempMedian.getMedian(value);
				
				// write the result in a correct format
				writer.printf(DOUBLE_FORMAT , median);
			}
			
			// close the writing and scanning
			writer.close();
			input.close();
			
		// catch when the file is not found
		}catch(FileNotFoundException e)
		{
			// print the message
			System.out.println(FNF_MESSAGE);
		}
    }

    /**
     * YOU ARE NOT COMPULSORILY REQUIRED TO IMPLEMENT THIS METHOD.
     *
     * That said, we found it useful to implement.
     *
     * Adds the new temperature reading to the corresponding
     * maxPQ or minPQ depending upon the current state.
     *
     * Then calculates and returns the updated median.
     *
     * @param newReading - the new reading to be added.
     * @return the updated median.
     */
    private Double getMedian(Double newReading)
    {
    	// if the incoming value is smaller than current median
		if(newReading > currentMedian)
		{
			// if the size is equal
    		if(minHeap.size() == maxHeap.size())
    		{
    			// insert the number to minPQ
    			minHeap.insert(newReading);
    			
    			// current median will be the first item in minPQ
    			currentMedian = minHeap.getMax();
    			
    			// return the updated median
    			return currentMedian;
    		}
    		// if the minPQ is smaller than maxPQ
    		else if(minHeap.size() == maxHeap.size() - 1)
    		{
    			// insert the incoming number to minPQ
    			minHeap.insert(newReading);
    			
    			// the current median is the half of max in minPQ and maxPQ
    			currentMedian = (maxHeap.getMax() + minHeap.getMax()) / 2;
    			
    			// return the updated median
    			return currentMedian;
    		}
    		// if the minPQ is bigger than maxPQ
    		else if(minHeap.size() == maxHeap.size() + 1)
    		{
    			// insert the max item in minPQ to maxPQ
    			maxHeap.insert(minHeap.removeMax()); 
    			
    			// insert the incoming number to minPQ
    			minHeap.insert(newReading);
    			
    			// the current median is the half of max in minPQ and maxPQ
    			currentMedian = (maxHeap.getMax() + minHeap.getMax()) / 2;
    			
    			// return the updated median
    			return currentMedian;
    		}
		}
		// if the incoming number is smaller than current median
		else if(newReading < currentMedian)
		{
			// if the size of minPQ and maxPQ is equal
			if(maxHeap.size() == minHeap.size())
			{
				// insert the incoming number to maxPQ
				maxHeap.insert(newReading);
				
				// the current median is the max in maxPQ
				currentMedian = maxHeap.getMax();
				
				// return the updated median
				return currentMedian;
			}
			
			// if the size of maxPQ is smaller than minPQ
			else if(maxHeap.size() == minHeap.size() - 1)
			{
				// insert the incoming number to maxPQ
				maxHeap.insert(newReading);
				
				// the current median is the half of max in minPQ and maxPQ
				currentMedian = (maxHeap.getMax() + minHeap.getMax()) / 2;
				
				// return the updated median
				return currentMedian;
			}
			else if(maxHeap.size() == minHeap.size() + 1)
			{
				// insert the max item in maxPQ to minPQ
				minHeap.insert(maxHeap.removeMax());
				
				// insert the incoming number to maxPQ
				maxHeap.insert(newReading);
				
				// the current median is the half of max in minPQ and maxPQ
				currentMedian = (maxHeap.getMax() + minHeap.getMax()) / 2;
				
				// return the updated median
				return currentMedian;
			}
		}
		return 0.0;
    }

    // DO NOT EDIT THE main METHOD.
    public static void main(String[] args)
    {
        // Check if files have been passed in the command line.
        // If no files are passed, run an infinite interactive loop taking a double
        // input each time until "q" is entered by the user.
        // After each iteration of the loop, update and display the new median.
        if ( args.length == 0 )
        {
            runInteractiveMode();
        }

        // If files are passed in the command line, open each file.
        // For each file, iterate over all the double values in the file.
        // After reading each new double value, write the new median to the
        // corresponding output file whose name will be inputFilename_out.txt
        // Stop reading the file at the moment a non-double value is detected.
        else
        {
            for ( int i=0 ; i < args.length ; i++ )
            {
                findMedianForFile(args[i]);
            }
        }
    }
}
