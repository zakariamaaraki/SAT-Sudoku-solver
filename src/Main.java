import java.io.*;
import java.util.StringTokenizer;

public class Main {
	
	public static int s(int x, int y, int z, int n) {
		return (n * n) * (x - 1) + n * (y - 1) + z;
	}
	
	public static String sat(int n, int[][] Matrix) {
		
		// Assigned
		StringBuilder assigned = new StringBuilder();
		for(int r = 0; r < n * n; r++) {
			for(int c = 0; c < n * n; c++) {
				if(Matrix[r][c] != 0)
					assigned.append(s(r + 1, c + 1, Matrix[r][c], n * n) + " 0\n");
			}
		}
		
		// There is at least one number in each entry
		StringBuilder cellDefinedness =  new StringBuilder();
		for(int r = 1; r <= n * n; r++) {
			for(int c = 1; c <= n * n; c++) {
				for(int v = 1; v < n * n; v++) {
					cellDefinedness.append(s(r, c, v, n * n) + " ");
				}
				cellDefinedness.append(s(r, c, n * n, n * n) + " 0\n");
			}
		}
		
		// Each number appears at most once in each row
		StringBuilder rowUniqueness = new StringBuilder();
		for(int r = 1; r <= n * n; r++) {
			for(int v = 1; v <= n * n; v++) {
				for(int ci = 1; ci <= n * n - 1; ci++) {
					for(int cj = ci + 1; cj <= n * n; cj++) {
						rowUniqueness.append("-" + s(ci, r, v, n * n) + " -" + s(cj, r, v, n * n) + " 0\n");
					}
				}
			}
		}
		
		// Each number appears at most once in each column
		StringBuilder colUniqueness = new StringBuilder();
		for(int c = 1; c <= n * n; c++) {
			for(int v = 1; v <= n * n; v++) {
				for(int ri = 1; ri <= n * n - 1; ri++) {
					for(int rj = ri + 1; rj <= n * n; rj++) {
						colUniqueness.append("-" + s(c, ri, v, n * n) + " -" + s(c, rj, v, n * n) + " 0\n");
					}
				}
			}
		}
		
		// Each number appears at most once in each n x n subgrid
		StringBuilder blockUniqueness = new StringBuilder();
		for(int z = 1; z <= n * n; z++) {
			for(int i = 0; i < n; i++) {
				for(int j = 0; j < n; j++) {
					for(int x = 1; x <= n; x++) {
						for(int y = 1; y <= n; y++) {
							for(int k = y + 1; k <= n; k++) {
								blockUniqueness.append("-" + s(n * i + x, n * j + y, z, n * n) + " -" +s(n * i + x, n * j + k, z, n * n) + " 0\n");
							}
						}
					}
				}
			}
		}
		for(int z = 1; z <= n * n; z++) {
			for(int i = 0; i < n; i++) {
				for(int j = 0; j < n; j++) {
					for(int x = 1; x <= n; x++) {
						for(int y = 1; y <= n; y++) {
							for(int k = x + 1; k <= n; k++) {
								for(int l = 1; l <= n; l++) {
									blockUniqueness.append("-" + s(n * i + x, n * j + y, z, n * n) + " -" +s(n * i + k, n * j + l, z, n * n) + " 0\n");
								}
							}
						}
					}
				}
			}
		}
		
		StringBuilder clauses = new StringBuilder();
		clauses.append(cellDefinedness);
		clauses.append(rowUniqueness);
		clauses.append(colUniqueness);
		clauses.append(blockUniqueness);
		clauses.append(assigned);
		
		return clauses.toString();
	}
	
	public static void fileWriter(String str) throws IOException {
		FileWriter myWriter = new FileWriter("minisatInput.txt");
		myWriter.write(str);
		myWriter.close();
	}
	
	public static int executeMinisat() throws IOException, InterruptedException {
		String[] command = new String[] {"minisat", "minisatInput.txt", "output.txt"};
		ProcessBuilder processbuilder = new ProcessBuilder(command);
		Process process = processbuilder.start();
		return process.waitFor();
	}
	
	private static String readFromInputStream(InputStream inputStream)
			throws IOException {
		StringBuilder resultStringBuilder = new StringBuilder();
		try (BufferedReader br
				     = new BufferedReader(new InputStreamReader(inputStream))) {
			String line;
			while ((line = br.readLine()) != null) {
				resultStringBuilder.append(line).append("\n");
			}
		}
		return resultStringBuilder.toString();
	}
	
	public static void outputResult(int n) throws IOException {
		String[] result = readFromInputStream(new FileInputStream("output.txt")).split("\n");
		
		if(result[0].equals("SAT")) {
			System.out.println("SATISFIABLE\n");
			String[] values = result[1].split(" ");
			for(int i = 0; i < n * n; i++) {
				for(int j = 0; j < n * n; j++) {
					for(int k = 0; k < n * n; k++) {
						int value = Integer.parseInt(values[(n * n * n * n * i) + (n * n * j) + k]);
						if(value > 0) {
							System.out.print((value - (n * n * n * n * i) - (n * n * j)) + " ");
							break;
						}
					}
				}
				System.out.println("\n");
			}
		} else
			System.out.println("UNSATISFIABLE\n");
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		// fast input
		InputStream inputStream = System.in;
		InputReader in = new InputReader(inputStream);
		
		int n = in.nextInt();
		if(n < 0)
			throw new IllegalArgumentException("Size must be positive !");
		
		int[][] Matrix = new int[n * n][n * n];
		
		for(int i = 0; i < n * n; i++) {
			for(int j = 0; j < n * n; j++) {
				int value = in.nextInt();
				if(value < 0 || value > n * n)
					throw new IllegalArgumentException("Each value must be between 0 and " + (n * n));
				Matrix[i][j] = value;
			}
		}
		
		String formula = sat(n, Matrix);
		int nbVariables =  (int)Math.pow(n,6);
		int nbClauses = formula.split("\n").length;
		String input = "p cnf " + nbVariables + " " + nbClauses + "\n" + formula;
		fileWriter(input);
		
		executeMinisat();
		
		outputResult(n);
		
	}
	
	static class InputReader {
		public BufferedReader reader;
		public StringTokenizer tokenizer;
		
		public InputReader(InputStream stream) {
			reader = new BufferedReader(new InputStreamReader(stream), 32768);
			tokenizer = null;
		}
		
		public String next() {
			while (tokenizer == null || !tokenizer.hasMoreTokens()) {
				try {
					tokenizer = new StringTokenizer(reader.readLine());
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			return tokenizer.nextToken();
		}
		
		public int nextInt() {
			return Integer.parseInt(next());
		}
		
	}
	
}

