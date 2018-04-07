package renderEngine;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import engineTester.Config;

public class FileInterpreter {

	private final String dtmFileName = Config.dtmFileName;//"C:/Users/Daniel Wilkinson/Documents/Uni/Year 3/Project/data/nz2045_DTM_1M.asc";
	private final String dsmFileName = Config.dsmFileName;//"C:/Users/Daniel Wilkinson/Documents/Uni/Year 3/Project/data/LIDAR-DSM-1M-NZ24nw/nz2045_DSM_1M.asc";

	private int NCOLS;
	private int NROWS;
	private int XLLCORNER;
	private int YLLCORNER;
	private int CELLSIZE;
	private float NODATA_VALUE;
	private int lineCounter;
	private float[] vertices;
	private int[] indices;
	
	public FileInterpreter(boolean getDTM){
		try{
			FileReader fileReader;
			if(getDTM){
				fileReader = new FileReader(dtmFileName);
			} else {
				fileReader = new FileReader(dsmFileName);
			}
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			this.lineCounter = 0;
			String line;
			while((line = bufferedReader.readLine()) != null){
				interpretLine(line);
			}
			bufferedReader.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	public float[] getVertices(){
		return this.vertices;
	}
	
	public int[] getIndices(){
		return this.indices;
	}
	
	public int getNROWS(){
		return this.NROWS;
	}
	
	public int getNCOLS(){
		return this.NCOLS;
	}
	
	public float getNODATA_VALUE(){
		return this.NODATA_VALUE;
	}
	
	private void interpretLine(String line){
		String[] lineSplit = line.split("\\s+");
		boolean isInfo;
		try{
			Float.parseFloat(lineSplit[0]);
			isInfo = false;
		}catch(NumberFormatException e){
			isInfo = true;
		}
		if(isInfo){
			storeInfo(lineSplit);
		} else {
			if(this.lineCounter == 0){
				initVertexBuffers();
			}
			storeData(lineSplit);
			this.lineCounter++;
		}
	}
	
	private void initVertexBuffers(){
		this.vertices = new float[this.NROWS * this.NCOLS * 3];
		this.indices = new int[6 + 3*(2*(this.NROWS + this.NCOLS)-4) + 6*(this.NROWS-2)*(this.NCOLS-2)];
		int startingIndex = 0;
		for(int y = 0; y < this.NROWS-1; y++){
			for(int x = 0; x < this.NCOLS-1; x ++){
				this.indices[startingIndex] = y*this.NCOLS + x;
				this.indices[startingIndex + 1] = y*this.NCOLS + x+1;
				this.indices[startingIndex + 2] = (y+1)*this.NCOLS + x+1;
				
				this.indices[startingIndex + 3] = y*this.NCOLS + x;
				this.indices[startingIndex + 4] = (y+1)*this.NCOLS + x;
				this.indices[startingIndex + 5] = (y+1)*this.NCOLS + x + 1;
				startingIndex += 6;
			}
		}
	}
	
	private void storeInfo(String[] lineSplit){
		int intData = Integer.MIN_VALUE;
		float floatData = Float.MIN_VALUE;
		try{
			intData = Integer.parseInt(lineSplit[1]);
		} catch (NumberFormatException e){
		}
		try{
			floatData = Float.parseFloat(lineSplit[1]);
		}
		catch(NumberFormatException e2){
			e2.printStackTrace();
		}
		switch(lineSplit[0]){
			case "ncols":	this.NCOLS = intData;
							break;
			case "nrows":	this.NROWS = intData;
							break;
			case "xllcorner":	this.XLLCORNER = intData;
								break;
			case "yllcorner":	this.YLLCORNER = intData;
								break;
			case "cellsize":	this.CELLSIZE = intData;
								break;
			case "NODATA_value":	this.NODATA_VALUE = floatData;
									break;
		}
	}
		
	private void storeData(String[] lineSplit){
		for(int x = 0; x < lineSplit.length; x++){
			try{
				float currentHeight = Float.parseFloat(lineSplit[x]);
				int startingIndex = (this.lineCounter * this.NCOLS + x) * 3;
				this.vertices[startingIndex] = (float)x;
				this.vertices[startingIndex + 2] = (float)this.lineCounter;
				this.vertices[startingIndex + 1] = (float)currentHeight;
			}
			catch(NumberFormatException e){
				e.printStackTrace();
			}
		}
	}
}
