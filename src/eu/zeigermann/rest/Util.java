package eu.zeigermann.rest;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Assorted static utility methods.
 * 
 */
public class Util {

	/**
	 * Transforms a file into a string.
	 * 
	 * @param file
	 *            the file to be transformed
	 * @param charsetName
	 *            encoding of the file
	 * @return the string containing the content of the file
	 */
	public static String fileToString(File file, String charsetName) {
		FileInputStream fileInputStream = null;
		try {
			try {
				fileInputStream = new FileInputStream(file);
				return streamToString(fileInputStream, charsetName);
			} finally {
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Transforms a file into a string.
	 * 
	 * @param fileName
	 *            name of the file to be transformed
	 * @param charsetName
	 *            encoding of the file
	 * @return the string containing the content of the file
	 */
	public static String fileToString(String fileName, String charsetName) {
		return fileToString(new File(fileName), charsetName);
	}

	/**
	 * Transforms a stream into a string.
	 * 
	 * @param is
	 *            the stream to be transformed
	 * @param charsetName
	 *            encoding of the file
	 * @return the string containing the content of the stream
	 */
	public static String streamToString(InputStream is, String charsetName) {
		try {
			Reader r = null;
			try {
				r = new BufferedReader(new InputStreamReader(is, charsetName));
				return readerToString(r);
			} finally {
				if (r != null) {
					try {
						r.close();
					} catch (IOException e) {
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Loads a stream from the classpath and transforms it into a string.
	 * 
	 * @param resourceName
	 *            the name of the resource to be transformed
	 * @param charsetName
	 *            encoding of the resource
	 * @return the string containing the content of the resource
	 * @see ClassLoader#getResourceAsStream(String)
	 */
	public static String resourceToString(String resourceName,
			String charsetName) {
		InputStream templateStream = Thread.currentThread()
				.getContextClassLoader().getResourceAsStream(resourceName);
		String template = Util.streamToString(templateStream, "UTF-8");
		return template;
	}

	/**
	 * Transforms a reader into a string.
	 * 
	 * @param reader
	 *            the reader to be transformed
	 * @return the string containing the content of the reader
	 */
	public static String readerToString(Reader reader) {
		try {
			StringBuilder sb = new StringBuilder();
			char[] buf = new char[1024];
			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				sb.append(buf, 0, numRead);
			}
			return sb.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public static byte[] streamToBa(InputStream is) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int numRead = 0;
			while ((numRead = is.read(buf)) != -1) {
				baos.write(buf, 0, numRead);
			}
			byte[] byteArray = baos.toByteArray();
			return byteArray;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Transforms any array to a matching list
	 * 
	 * @param value
	 *            something that might be an array
	 * @return List representation if passed in value was an array,
	 *         <code>null</code> otherwise
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Object> arrayAsList(Object value) {
		List list = null;
		if (value instanceof int[]) {
			list = new ArrayList();
			int[] array = (int[]) value;
			for (int i : array) {
				list.add(i);
			}
		} else if (value instanceof short[]) {
			list = new ArrayList();
			short[] array = (short[]) value;
			for (short i : array) {
				list.add(i);
			}
		} else if (value instanceof char[]) {
			list = new ArrayList();
			char[] array = (char[]) value;
			for (char i : array) {
				list.add(i);
			}
		} else if (value instanceof byte[]) {
			list = new ArrayList();
			byte[] array = (byte[]) value;
			for (byte i : array) {
				list.add(i);
			}
		} else if (value instanceof long[]) {
			list = new ArrayList();
			long[] array = (long[]) value;
			for (long i : array) {
				list.add(i);
			}
		} else if (value instanceof double[]) {
			list = new ArrayList();
			double[] array = (double[]) value;
			for (double i : array) {
				list.add(i);
			}
		} else if (value instanceof float[]) {
			list = new ArrayList();
			float[] array = (float[]) value;
			for (float i : array) {
				list.add(i);
			}
		} else if (value instanceof boolean[]) {
			list = new ArrayList();
			boolean[] array = (boolean[]) value;
			for (boolean i : array) {
				list.add(i);
			}
		} else if (value.getClass().isArray()) {
			Object[] array = (Object[]) value;
			list = Arrays.asList(array);
		}
		return list;
	}

	/**
	 * Trims off white space from the beginning of a string.
	 * 
	 * @param input
	 *            the string to be trimmed
	 * @return the trimmed string
	 */
	public static String trimFront(String input) {
		int i = 0;
		while (i < input.length() && Character.isWhitespace(input.charAt(i)))
			i++;
		return input.substring(i);
	}

	public static String unifyNewlines(String source) {
		final String regex = "\\r?\\n";
		final String clearedSource = source.replaceAll(regex, "\n");
		return clearedSource;
	}

}
