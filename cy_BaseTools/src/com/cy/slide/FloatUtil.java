package com.cy.slide;

public class FloatUtil {
	private static final double ZERO_FLOAT = 0.001;

	public static boolean isZero(float f) {
		return Math.abs(f) < ZERO_FLOAT;
	}

	public static boolean isPositive(float f) {
		return f > ZERO_FLOAT;
	}

	public static boolean isNegative(float f) {
		return f < -ZERO_FLOAT;
	}

	public static boolean isEqual(float f1, float f2) {
		return isZero(f1 - f2);
	}
}
