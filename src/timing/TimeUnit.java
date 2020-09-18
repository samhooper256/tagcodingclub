package timing;

public enum TimeUnit {
	NANOSECONDS(1e9), MICROSECONDS(1e6), MILLISECONDS(1e3), SECONDS(1), MINUTES(1d/60), HOURS(1d/3600);
	
	private final double conversion;
	
	TimeUnit(double conversion) {
		this.conversion = conversion;
	}
	
	public static double convert(double duration, TimeUnit sourceUnit, TimeUnit destinationUnit) {
		return duration * (destinationUnit.conversion / sourceUnit.conversion);
	}
	
	public double convert(double sourceDuration, TimeUnit sourceUnit) {
		return convert(sourceDuration, sourceUnit, this);
	}
}
