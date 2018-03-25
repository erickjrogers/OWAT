package com.ebp.owat.lib.datastructure.matrix.Linked.utils.nodePosition;

import com.ebp.owat.lib.datastructure.matrix.Linked.LinkedMatrix;
import com.ebp.owat.lib.datastructure.matrix.Linked.utils.Direction;
import com.ebp.owat.lib.datastructure.matrix.Linked.utils.LinkedMatrixNode;
import com.ebp.owat.lib.datastructure.matrix.utils.coordinate.Coordinate;

public class NodePosition<T> extends Coordinate {

	private LinkedMatrixNode<T> node = null;

	public NodePosition(LinkedMatrix<T> matrix) {
		super(matrix);
	}

	protected NodePosition(LinkedMatrix<T> matrix, LinkedMatrixNode<T> node) {
		this(matrix);
		this.node = node;
	}

	public NodePosition(LinkedMatrix<T> matrix, LinkedMatrixNode<T> node, long xIn, long yIn) {
		super(matrix, xIn, yIn);
	}



	/**
	 * Moves this position one node North.
	 * @return If the position actually moved or not.
	 */
	protected boolean moveNorth(){
		return false;
	}

	/**
	 * Moves this position one node South.
	 * @return If the position actually moved or not.
	 */
	protected boolean moveSouth(){
		return false;
	}

	/**
	 * Moves this position one node East.
	 * @return If the position actually moved or not.
	 */
	protected boolean moveEast(){
		return false;
	}

	/**
	 * Moves this position one node West.
	 * @return If the position actually moved or not.
	 */
	protected boolean moveWest(){
		return false;
	}

	/**
	 * Moves this position one node in the direction given.
	 * @param dir The direction to move the position.
	 * @return If the position actually moved or not.
	 */
	protected boolean move(Direction dir){
		switch (dir){
			case NORTH:
				return moveNorth();
			case SOUTH:
				return moveSouth();
			case EAST:
				return moveEast();
			case WEST:
				return moveWest();
		}
		throw new IllegalArgumentException();
	}

	/**
	 * Method to reset the position of the node, if the node needs to do so.
	 */
	public void resetPosition(){

	}
}
