package keisuke.util;

import java.io.BufferedOutputStream;
import java.io.PrintStream;

/**
 * Capturing System.out during a test.
 */
public class StdoutCapture extends StdxxxCapture {

	public StdoutCapture() {
		super();
		this.setSavedStream(System.out);
		System.setOut(new PrintStream(new BufferedOutputStream(this.actualMessage())));
	}

	/**
	 * get captured text which was printed to System.err.
	 * @return String captured text which was printed to System.err
	 */
	public String getCapturedString() {
		return super.getCapturedString(System.out);
	}

	/**
	 * Finalize of StdoutCapture.
	 * set saved original System.out stream to System.out.
	 * print the captured text into System.out.
	 * close the stream used for capturing.
	 */
	public void finish() {
		System.setOut(this.savedStream());
		super.finish(System.out);
	}
}
