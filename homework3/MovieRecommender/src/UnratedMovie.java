
public class UnratedMovie implements Comparable<UnratedMovie> {
	int movieId;
	double estimatedRating;
	
	public UnratedMovie(int id, double rating) {
		movieId = id;
		estimatedRating = rating;
	}
	@Override
	public int compareTo(UnratedMovie o) {
		if (this.estimatedRating > o.estimatedRating) return 1;
		if (this.estimatedRating < o.estimatedRating) return -1;
		return 0;
	}
}
