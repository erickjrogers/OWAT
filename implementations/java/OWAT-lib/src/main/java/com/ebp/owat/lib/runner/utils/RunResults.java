package com.ebp.owat.lib.runner.utils;

import com.ebp.owat.lib.datastructure.value.NodeMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Results of a run, keeps information about it for reporting.
 *
 * TODO:: finish javadocs
 */
public class RunResults {
	private static final Logger LOGGER = LoggerFactory.getLogger(RunResults.class);

	/**
	 * Constructor to set the scrambleMode of this scramble scrambleMode
	 * @param scrambleMode The scrambleMode of this run.
	 */
	public RunResults(ScrambleMode scrambleMode){
		this.scrambleMode = scrambleMode;
		if(this.scrambleMode == ScrambleMode.SCRAMBLING){
			this.curStep = Step.NOT_STARTED_SCRAMBLE;
		}
		if(this.scrambleMode == ScrambleMode.DESCRAMBLING){
			this.curStep = Step.NOT_STARTED_DESCRAMBLE;
		}

	}

	public RunResults(ScrambleMode scrambleMode, NodeMode nodeMode) {
		this(scrambleMode);
		this.setNodeMode(nodeMode);
	}

		/** The scrambleMode of this run. */
	private final ScrambleMode scrambleMode;
	/** The number of bytes read in for the data. */
	private long numBytesIn = -1;
	/** The number of bytes in the data written out. */
	private long numBytesOut = -1;

	/** The node type that was used. */
	private NodeMode nodeMode;
	/** The type of matrix used. */
	private MatrixMode matrixMode;

	/** The map of timing data. */
	private LinkedHashMap<Step, Long> timingMap = new LinkedHashMap<>();

	private Step curStep;

	private long curStepProg = 0;

	private long curStepProgMax = 0;

	public synchronized NodeMode getNodeMode(){
		return this.nodeMode;
	}

	public synchronized void setNodeMode(NodeMode nodeMode){
		this.nodeMode = nodeMode;
	}

	public ScrambleMode getScrambleMode(){
		return this.scrambleMode;
	}

	public synchronized MatrixMode getMatrixMode(){
		return this.matrixMode;
	}
	public synchronized void setMatrixMode(MatrixMode matrixMode){
		this.matrixMode = matrixMode;
	}

	public synchronized Step getCurStep(){
		return this.curStep;
	}

	public synchronized void setCurStep(Step curStep){
		this.curStep = curStep;
		this.resetStepProg();
	}

	private synchronized void setTimingMap(LinkedHashMap<Step,Long> timingMap){
		this.timingMap = timingMap;
	}

	public synchronized long getCurStepProg(){
		return curStepProg;
	}

	public synchronized void setCurStepProg(long curStepProg){
		this.curStepProg = curStepProg;
	}

	public synchronized long getCurStepProgMax(){
		return curStepProgMax;
	}

	public synchronized void setCurStepProgMax(long curStepProgMax){
		this.setCurStepProg(0);
		this.curStepProgMax = curStepProgMax;
	}

	private synchronized void resetStepProg(){
		this.setCurStepProgMax(0);
	}

	public synchronized byte getStepPercentDone(){
		if(this.getCurStepProgMax() < 1){
			return 0;
		}
		return (byte) (((double)this.getCurStepProg() / (double) this.getCurStepProgMax()) * 100.0);
	}

	/**
	 * Sets the time it took to complete a specific step.
	 * @param step The step.
	 * @param timeTook How long it took to complete the step.
	 */
	public synchronized void setElapsedTime(Step step, long timeTook){
		if(this.timingMap.containsKey(step)){
			throw new IllegalStateException("Cannot overwrite step timing.");
		}
		this.timingMap.put(step, timeTook);
	}

	/**
	 *
	 * @param step The step that took place.
	 * @param startTime Time in milliseconds when the step started.
	 * @param endTime Time in milliseconds when the step ended.
	 */
	public synchronized void setElapsedTime(Step step, long startTime, long endTime){
		this.setElapsedTime(step, endTime - startTime);
	}

	/**
	 * Gets the number of milliseconds it took for the step to be completed.
	 * @param step The step to get the data for.
	 * @return The number of milliseconds it took to complete the step.
	 */
	public synchronized long getStepTiming(Step step){
		return this.timingMap.get(step);
	}

	/**
	 * Gets the timing data from the runner.
	 * @return The timing data from the runner. Null if not run.
	 */
	public synchronized LinkedHashMap<Step, Long> getTimingMap(){
		if(this.timingMap == null){
			return null;
		}
		return (LinkedHashMap<Step, Long>) this.timingMap.clone();
	}

	public synchronized void setNumBytesIn(long numBytesIn){
		this.numBytesIn = numBytesIn;
	}

	public synchronized long getNumBytesIn(){
		return this.numBytesIn;
	}

	public synchronized void setNumBytesOut(long numBytesOut){
		this.numBytesOut = numBytesOut;
	}

	public synchronized long getNumBytesOut(){
		return this.numBytesOut;
	}

	public void logOutTimingData(){
		HashMap<Step, Long> timingMap = this.getTimingMap();

		LOGGER.info("Step Timing data: (how log it took to do each step)");
		for(Map.Entry<Step, Long> curStep : timingMap.entrySet()){
			LOGGER.info("\t{}: {}s", curStep.getKey(), (double)curStep.getValue()/1000.0);
		}
	}

	@Override
	public synchronized RunResults clone(){
		RunResults output = new RunResults(this.scrambleMode, this.nodeMode);
		output.setMatrixMode(this.getMatrixMode());
		output.setCurStep(this.curStep);
		output.setCurStepProg(this.getCurStepProg());
		output.setCurStepProgMax(this.getCurStepProgMax());
		output.setTimingMap(this.getTimingMap());
		output.setNumBytesIn(this.getNumBytesIn());
		output.setNumBytesOut(this.getNumBytesOut());

		return output;
	}

	/**
	 * Gets the header for a CSV file.
	 * @return The header for a CSV file.
	 */
	public String getCsvHead(){
		StringBuilder sb = new StringBuilder("scrambleMode,nodeMode,matrixMode,lastStep,numBytesIn,numBytesOut");

		for (Step curStep : Step.getStepsIn(this.scrambleMode)) {
			sb.append(",");
			sb.append(curStep.stepName);
		}

		return sb.toString();
	}

	/**
	 * Gets a csv line
	 * @param includeHead If the header line is to be included.
	 * @return A csv line.
	 */
	public String getCsvLine(boolean includeHead){
		StringBuilder sb = new StringBuilder();
		if(includeHead){
			sb.append(
				String.format(this.getCsvHead()+"%n")
			);
		}

		sb.append(this.scrambleMode.name);
		sb.append(",");
		sb.append(this.nodeMode.typeStr);
		sb.append(",");
		sb.append(this.matrixMode.name);
		sb.append(",");
		sb.append(this.getCurStep().stepName);
		sb.append(",");
		sb.append(this.getNumBytesIn());
		sb.append(",");
		sb.append(this.getNumBytesOut());

		LinkedHashMap<Step, Long> steps = this.getTimingMap();
		for (Step curStep : Step.getStepsIn(this.scrambleMode)) {
			Long val = steps.get(curStep);
			sb.append(",");
			sb.append(
				(val == null ? "<undefined>" : val)
			);
		}

		return sb.toString();
	}

	/**
	 * Gets a csv line for this result.
	 * @return a csv line representing this result.
	 */
	public String getCsvLine(){
		return this.getCsvLine(false);
	}

	/**
	 * Gets all the csv lines of the results in the collection.
	 * @param results The run results to use
	 * @return The string of csv's from the results.
	 * @throws IllegalArgumentException If the results are of mixed scrambling modes.
	 */
	public static String getCsvLines(Collection<RunResults> results) throws IllegalArgumentException{
		StringBuilder sb = new StringBuilder();
		ScrambleMode mode = null;

		for(RunResults curResults : results){
			if(mode == null){
				mode = curResults.getScrambleMode();
				sb.append(curResults.getCsvHead());
			}

			if(curResults.getScrambleMode() != mode){
				throw new IllegalArgumentException("The list of run results were not all the same scrambling mode.");
			}
			sb.append(curResults.getCsvLine());
		}
		return sb.toString();
	}
}
