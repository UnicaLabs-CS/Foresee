

/*
 * 
 */
public class Statistics
{	
	/**
	 * Calculates the mean.
	 */
	public double mean(double[] x)
	{
		double sum = 0;
		for(double i : x)
		{
			sum += i;
		}
		return sum / (x.length - 1);
	}
	
	/**
	 * Calculates the covariance. Naive algorithm.
	 */
	public double sampleCovariance(double[] x, double[] y)
	{
		double meanX = mean(x);
		double meanY = mean(y);
	
		double sum = 0;
		for (int i = 0; i < x.length; i++)
		{
			sum += (x[i] - meanX) * (y[i] - meanY);
		}	
		return sum / (x.length - 1);
	}
	
	/**
	 * Direct algorithm based on definition: rho = cov / (s_x * s_y)
	 */
	public double naivePearson(double[] x, double[] y)
	{
		return sampleCovariance(x, y) / (sampleCovariance(x, x) * sampleCovariance(y, y));
	}

	/**
	 * Accurately determine the Pearson correlation coefficient.
	 *
	 * Based on Expression 1 from John D. Cook which should
	 * guarantee a better stability.
	 * <a href="http://www.johndcook.com/blog/2008/11/05/how-to-calculate-pearson-correlation-accurately/">Reference</a>
	 */
	public float accuratePearson(double[] x, double[] y)
	{
		double meanX = mean(x);
		double meanY = mean(y);
	
		double varX = sampleCovariance(x, x);
		double varY = sampleCovariance(y, y);
	
		double sum = 0;
		for (int i = 0; i < x.length; i++)
		{
			sum += ((x[i] - meanX) / varX) * ((y[i] - meany) / varY);
		}
		return sum / (x.length - 1);
	}
}
