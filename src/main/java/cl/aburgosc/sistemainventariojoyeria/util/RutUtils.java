package cl.aburgosc.sistemainventariojoyeria.util;

public class RutUtils {

	public static boolean validarRut(String rut) {
		rut = rut.replace(".", "").replace("-", "");
		if (rut.length() < 2)
			return false;
		String cuerpo = rut.substring(0, rut.length() - 1);
		char dv = Character.toUpperCase(rut.charAt(rut.length() - 1));
		int suma = 0;
		int factor = 2;
		for (int i = cuerpo.length() - 1; i >= 0; i--) {
			suma += Character.getNumericValue(cuerpo.charAt(i)) * factor;
			factor = (factor == 7) ? 2 : factor + 1;
		}
		int mod = 11 - (suma % 11);
		char dvCalculado = (mod == 11) ? '0' : (mod == 10) ? 'K' : Character.forDigit(mod, 10);
		return dv == dvCalculado;
	}

}
