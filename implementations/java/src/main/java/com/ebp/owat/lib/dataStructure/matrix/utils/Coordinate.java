package com.ebp.owat.lib.dataStructure.matrix.utils;

import com.ebp.owat.lib.dataStructure.matrix.Matrix;
import com.ebp.owat.lib.dataStructure.matrix.OwatMatrixException;

/**
 * Describes a coordinate on a matrix.
 *
 * Created by Greg Stewart on 4/4/17.
 */
public class Coordinate {
	/** The matrix this coordinate is on. */
	private final Matrix matrix;
	/** The X value (which col) of this coordinate. */
	private long x;
	/** The Y value (which row) of this coordinate. */
	private long y;
	
	/**
	 * Constructs a new coordinate with a matrix.
	 * @param matrix The matrix to give to this Coordinate.
	 */
	public Coordinate(Matrix matrix){
		this.matrix = matrix;
	}
	
	/**
	 * Constructs a new coordinate with all the values set.
	 *
	 * @param matrix The matrix this coordinate is on.
	 * @param xIn The X value (which col) of this coordinate.
	 * @param yIn The Y value (which row) of this coordinate.
	 * @throws OwatMatrixException If the values in are out of bounds.
	 */
	public Coordinate(Matrix matrix, long xIn, long yIn) throws OwatMatrixException{
		this(matrix);
		this.setX(xIn).setY(yIn);
	}
	
	/**
	 * Sets the X value (which col) of this coordinate.
	 * @param xIn The X value (which col) of this coordinate.
	 * @return This Coordinate.
	 * @throws OwatMatrixException If the value in is out of bounds.
	 */
	public Coordinate setX(long xIn) throws OwatMatrixException{
		matrix.checkValidColNumber(xIn);
		return this;
	}
	
	/**
	 * Sets the Y value (which row) of this coordinate.
	 * @param yIn The Y value (which row) of this coordinate.
	 * @return This Coordinate.
	 * @throws OwatMatrixException If the value in is out of bounds.
	 */
	public Coordinate setY(long yIn) throws OwatMatrixException{
		matrix.checkValidColNumber(yIn);
		return this;
	}
	
	/**
	 * Gets the X value (which col) of this coordinate.
	 * @return The X value (which col) of this coordinate.
	 */
	public long getX(){
		return this.x;
	}
	
	/**
	 * Gets the y value (which row) of this coordinate.
	 * @return The y value (which row) of this coordinate.
	 */
	public long getY(){
		return this.y;
	}
}
