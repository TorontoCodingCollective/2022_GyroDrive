package com.torontocodingcollective;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TUtil {

	// Simple rounding routine from StackOverflow for using BigDecimal to round a double
	public static double round(double value, int decimals) {

		if (decimals < 0) {
			throw new IllegalArgumentException();
		}

		return BigDecimal
				.valueOf(value)
				.setScale(decimals, RoundingMode.HALF_UP)
				.doubleValue();
	}
}
