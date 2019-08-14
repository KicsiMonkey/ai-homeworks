import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrixChangingVisitor;
import org.apache.commons.math3.linear.RealVector;

public class Main {
	static boolean DEBUG=false;
	public static void main(String[] args) throws InterruptedException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			String[] info = reader.readLine().split("\t");
			int ratingsSize = Integer.valueOf(info[0]);
			int usersSize = Integer.valueOf(info[1]);
			int moviesSize = Integer.valueOf(info[2]);
			
			int K = 20;
			
			Array2DRowRealMatrix R = new Array2DRowRealMatrix(usersSize, moviesSize);
			R.walkInOptimizedOrder(new RealMatrixChangingVisitor() {
				@Override
				public double visit(int arg0, int arg1, double arg2) {
					return 0.0;
				}
				@Override
				public double end() { return 0; }
				@Override
				public void start(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {}
			});
			for (int i = 0; i < ratingsSize; ++i) {
				String[] rating = reader.readLine().split("\t");
				int userId = Integer.valueOf(rating[0]);
				int movieId = Integer.valueOf(rating[1]);
				int ratingValue = Integer.valueOf(rating[2]);
				R.setEntry(userId, movieId, ratingValue);
			}
			
			Array2DRowRealMatrix P = new Array2DRowRealMatrix(usersSize, K);
			P.walkInOptimizedOrder(new RealMatrixChangingVisitor() {
				@Override
				public double visit(int arg0, int arg1, double arg2) {
					return Math.random();
				}
				@Override
				public double end() { return 0; }
				@Override
				public void start(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {}
			});
			
			Array2DRowRealMatrix Q = new Array2DRowRealMatrix(moviesSize, K);
			Q.walkInOptimizedOrder(new RealMatrixChangingVisitor() {
				@Override
				public double visit(int arg0, int arg1, double arg2) {
					return Math.random();
				}
				@Override
				public double end() { return 0; }
				@Override
				public void start(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {}
			});

			
			double size = R.getRowDimension()*R.getColumnDimension();
			
			factorizeMatrix(R, P, Q, K
					, 0.0002 //0.004
					, 0.02//0.0000002
					, 0.001, 1500, 39);
			if(DEBUG) System.out.println("Size of matrix: " + size);
			
			Array2DRowRealMatrix resultR = (Array2DRowRealMatrix) P.multiply(Q.transpose());
			
			if(DEBUG) { System.out.println(); printMatrix(resultR); System.out.println(); }
			
			if(!DEBUG) {
				for (int i = 0; i < R.getRowDimension(); ++i) {
					ArrayList<UnratedMovie> topRecs= getTopRecommendations(R.getRowVector(i), resultR.getRowVector(i));
					for (int j = 0; j < topRecs.size() && j < 10; ++j) {
						System.out.print(topRecs.get(j).movieId);
						if (j+1 < topRecs.size() && j+1 < 10) System.out.print("\t");
					}
					if (i+1 < R.getRowDimension()) System.out.print("\n");
				}				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public static void factorizeMatrix(Array2DRowRealMatrix R, Array2DRowRealMatrix P, Array2DRowRealMatrix Q,
			int K, double alpha, double beta, double errLimit, int cycleLimit, int secLimit) throws InterruptedException {

		long T = System.currentTimeMillis();
		// sum of errors after correction
		double errSum;
		// previous cycle error
		double prevErrSum = 0.0;
		// previous state of matrices
		Array2DRowRealMatrix Pcopy = (Array2DRowRealMatrix) P.copy();
		Array2DRowRealMatrix Qcopy = (Array2DRowRealMatrix) Q.copy();
		// cycle counter
		int cycleN = 0;
		do {
			// initialize correction cycle error
			errSum = 0.0;
			// iterate our rating matrix
			for (int i = 0; i < R.getRowDimension(); ++i) {
				for (int j = 0; j < R.getColumnDimension(); ++j) {
					// only estimate from existing ratings
					if (R.getEntry(i, j) > 0.0) {
						// this will store error after correction
						double errHere = 0.0;
						// calculate current error without regularization
						double errWoReg = (R.getEntry(i, j) - P.getRowVector(i).dotProduct(Q.getRowVector(j)));
						// correct error
						for (int k = 0; k < K; ++k) {
							P.addToEntry(i, k, alpha*(2.0*errWoReg*Q.getEntry(j, k)-beta*P.getEntry(i, k)));
							Q.addToEntry(j, k, alpha*(2.0*errWoReg*P.getEntry(i, k)-beta*Q.getEntry(j, k)));
							
							// accumulate post-correction error's regularization part
							errHere += (beta/2.0) * (Math.pow(P.getEntry(i, k), 2.0) + Math.pow(Q.getEntry(j, k), 2.0)); 
						}
						// accumulate post-correction error's default part
						errHere += Math.pow(R.getEntry(i, j) - P.getRowVector(i).dotProduct(Q.getRowVector(j)), 2.0);
						errSum += errHere;
					}
				}
			}
			if(DEBUG) System.out.println("Cycle-" + cycleN + "\nError: " + errSum + String.format(" | %.5f", errSum));
			
			
			Pcopy = (Array2DRowRealMatrix) P.copy();
			Qcopy = (Array2DRowRealMatrix) Q.copy();
			
			if (cycleN > 0) {
				// did we mess up?
				if (errSum - prevErrSum > 0.0) {
					// yep, backin up
					P = Pcopy;
					Q = Qcopy;
					break;
				}
			}
			
			cycleN++;
			prevErrSum = errSum;
			
		} while (errSum > errLimit
				&& cycleN < cycleLimit
				&& (System.currentTimeMillis() - T) < secLimit * 1000);
		
		if(DEBUG) System.out.println("Minimized errorSum: " + prevErrSum + "\nReached error: " + errSum + String.format(" | %.5f", errSum));
	}
	
	public static void printMatrix(Array2DRowRealMatrix m) {
		for (int i = 0; i < m.getRowDimension(); ++i) {
			for (int j = 0; j < m.getColumnDimension(); ++j) {
				System.out.print(String.format(Locale.ROOT, "%.2f", m.getEntry(i, j)));
				if (j+1 < m.getColumnDimension()) System.out.print('\t');
			}
			if(i+1 < m.getRowDimension()) System.out.print('\n');
		}
	}
	
	public static ArrayList<UnratedMovie> getTopRecommendations(RealVector initMRow, RealVector resultMRow) {
		double[] initArray = initMRow.toArray();
		double[] resultArray = resultMRow.toArray();
		ArrayList<UnratedMovie> unratedMovies = new ArrayList<UnratedMovie>();
		
		for (int j = 0; j < initArray.length; ++j) {
			if (initArray[j] == 0.0) unratedMovies.add(new UnratedMovie(j, resultArray[j]));
		}
		
		Collections.sort(unratedMovies, Collections.reverseOrder());
		
		return unratedMovies;
	}
}
