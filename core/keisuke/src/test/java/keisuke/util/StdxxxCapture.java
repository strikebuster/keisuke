package keisuke.util;

import static keisuke.util.StringUtil.SYSTEM_ENCODING;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * Capturing System.out/err during a test.
 */
public abstract class StdxxxCapture {
	private PrintStream savedStream;
	private ByteArrayOutputStream actualMessage = null;
	private String messageText = null;

	public StdxxxCapture() {
		this.actualMessage = new ByteArrayOutputStream();
	}

	/**
	 * getter of savedStream
	 * @return savedStream
	 */
	protected PrintStream savedStream() {
		return this.savedStream;
	}

	/**
	 * setter of savedStream
	 * @param stream System.out/err
	 */
	protected void setSavedStream(final PrintStream stream) {
		this.savedStream = stream;
	}
	/**
	 * getter of actualMessage
	 * @return actualMessage
	 */
	protected ByteArrayOutputStream actualMessage() {
		return this.actualMessage;
	}

	/**
	 * get captured text which was printed to PrintStream(System.out/err).
	 * @param stream System.out/err
	 * @return String captured text which was printed to PrintStream(System.out/err)
	 */
	public String getCapturedString(final PrintStream stream) {
		stream.flush();
		try {
			this.messageText = this.actualMessage.toString(SYSTEM_ENCODING);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		return this.messageText;
	}

	/**
	 * Finalize of StdxxxCapture.
	 * Before calling here, reset System.out/err from savedStream.
	 * print the captured text into System.out/err.
	 * close the stream used for capturing.
	 * @param stream System.out/err
	 */
	public void finish(final PrintStream stream) {
		if (this.messageText != null) {
			stream.print(this.messageText);
		}
		try {
			if (this.actualMessage != null) this.actualMessage.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
