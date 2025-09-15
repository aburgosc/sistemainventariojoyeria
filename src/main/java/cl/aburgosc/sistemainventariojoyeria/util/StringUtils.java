package cl.aburgosc.sistemainventariojoyeria.util;

import java.math.BigDecimal;

/**
 *
 * @author aburgosc
 */
public class StringUtils {

	public static String safe(Object obj) {
		return obj != null ? obj.toString() : "";
	}

	public static BigDecimal ToBigDecimal(String text) {
		if (text == null || text.trim().isEmpty()) {
			return BigDecimal.ZERO;
		}
		try {
			String normalized = text.replaceAll("[^\\d.]", "");
			if (normalized.isEmpty()) {
				return BigDecimal.ZERO;
			}
			return new BigDecimal(normalized);
		} catch (NumberFormatException e) {
			return BigDecimal.ZERO;
		}
	}
}
