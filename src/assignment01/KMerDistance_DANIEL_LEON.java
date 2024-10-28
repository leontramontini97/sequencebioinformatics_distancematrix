package assignment01;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import static java.lang.Math.log;

/**
 * KMerDistance_DANIEL_LEON.java
 * Author(s): DANIEL_LEON
 * Sequence Bioinformatics, WS 24/25
 */
public class KMerDistance_DANIEL_LEON {
	/**
	 * run your code
	 *
	 * @param args
	 */
	public static void main(String[] args) throws IOException {


		if (args.length != 3) // changed to three to control filename variable, different fasta file and k-mer lenght
			throw new IllegalArgumentException("Wrong number of arguments. Usage: <input_fasta> <k> <output_file>");

		String fileName1 = args[0]; // Input FASTA file
		int k = Integer.parseInt(args[1]); // k-mer length
		String outputFileName = args[2]; // Output file to save the matrix


		ArrayList<FastA_DANIEL_LEON.Pair> sequences = FastA_DANIEL_LEON.read(fileName1);

		ArrayList<Set<String>> kMersList = new ArrayList<>();
		for (FastA_DANIEL_LEON.Pair pair : sequences) {
			Set<String> kMers = extractKMers(pair.sequence(), k);
			kMersList.add(kMers);
		}

		distance_matrix(sequences, kMersList, outputFileName);


	}

	/**
	 * extract all k-mer
	 *
	 * @param sequence
	 * @param k
	 * @return
	 */
	public static Set<String> extractKMers(String sequence, int k) {
		int n = sequence.length();
		Set<String> kMers = new HashSet<>();
		for (int i = 0; i <= n - k; i++) {
			String kMer = sequence.substring(i, i + k);
			kMers.add(kMer);
		}

		return kMers;
	}

	/**
	 * compute the Jaccard index
	 *
	 * @param set1
	 * @param set2
	 * @return Jaccard index
	 */
	public static double computeJaccardIndex(Set<String> set1, Set<String> set2) {
		int numerator = 0;
		int denominator = 0;

		for (String wordA : set1) {
			if (set2.contains(wordA)) {
				numerator++;
			}
			denominator++;
		}


		for (String wordB : set2) {
			if (!set1.contains(wordB)) {
				denominator++;
			}
		}
		if (denominator == 0) {
			throw new IllegalArgumentException("Cannot compute Jaccard index: at least one of the files contains no k-mers.");
		}

		return (double) numerator / denominator;

	}

	public static double computeDistance(double jaccardIndex) {

		return -log(jaccardIndex); // Use Math.log() for natural logarithm
	}



	public static void distance_matrix(ArrayList<FastA_DANIEL_LEON.Pair> sequences, ArrayList<Set<String>> kMersList, String outputFileName) throws IOException {
		int numSequences = sequences.size();

		// Print to console
		System.out.println(numSequences); // Print the number of sequences

		// Write to file
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName));
		writer.write(numSequences + "\n"); // Write the number of sequences to the file

		// Calculate the distance matrix
		for (int i = 0; i < numSequences; i++) {
			String header1 = sequences.get(i).header(); // Retrieve the sequence header

			// Print to console
			System.out.print(header1 + " ");
			writer.write(header1 + " "); // Write to file

			for (int j = 0; j < numSequences; j++) {
				double jaccardIndex = computeJaccardIndex(kMersList.get(i), kMersList.get(j));
				double distance = computeDistance(jaccardIndex);

				// Print the distance with two decimal places
				System.out.printf("%.2f ", distance);
				writer.write(String.format("%.2f ", distance)); // Write the distance to the file
			}

			// End of the row
			System.out.println();
			writer.newLine(); // Write a new line to the file
		}

		writer.close(); // Close the file writer
	}
}
