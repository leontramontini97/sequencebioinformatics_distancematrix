package assignment01;

import java.io.*;
import java.util.*;

/**
 * FastA_DANIEL_LEON
 * Author(s): DANIEL_LEON
 * Sequence Bioinformatics, WS 24/25
 */
public class FastA_DANIEL_LEON {

	public static ArrayList<Pair> read(String fileName) throws IOException {
		ArrayList<Pair> sequences = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String header = null;
		StringBuilder sequence = new StringBuilder();

		String line;
		while ((line = br.readLine()) != null) {
			if (line.startsWith(">")) { // Header line
				if (header != null) { // Add previous sequence when encountering a new header
					sequences.add(new Pair(header, sequence.toString()));
				}
				header = line; // Start new header
				sequence = new StringBuilder(); // Reset sequence
			}
			else {
				sequence.append(line); // Append sequence lines
			}
		}
		if (header != null) { // Add the last sequence
			sequences.add(new Pair(header, sequence.toString()));
		}
		br.close();
		return sequences;
	}

	public static void write(ArrayList<Pair> list, String fileName) throws IOException {
		if (fileName == null) { // Print to console if no filename is provided
			for (Pair pair : list) {
				System.out.println(pair.header());
				System.out.println(pair.sequence());
			}
		}
		else {
			// Write to file
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
			for (Pair pair : list) {
				writer.write(pair.header());
				writer.newLine();
				writer.write(pair.sequence());
				writer.newLine();
			}
			writer.close();
		}
	}

	// Pair class to hold header and sequence
	public record Pair(String header, String sequence) {
	}
}
