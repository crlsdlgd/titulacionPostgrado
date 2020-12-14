/**************************************************************************
 *                (c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción distribución no autorizada de este programa, 
 * o cualquier porción de  él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán  procesadas con el grado  máximo contemplado 
 * por la ley.
 
 ************************************************************************* 
   
 ARCHIVO:     GeneralesUtilidades.java      
 DESCRIPCION: Clase encargada de realizar operaciones generalizadas para el sistema.  
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 20/02/2018				Daniel Albuja                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.UrlValidator;

//import org.apache.commons.validator.routines.UrlValidator;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UsuarioConstantes;

/**
 * Clase GeneralesUtilidades.
 * Clase encargada de realizar operaciones generalizadas para el sistema.
 * @author dcollaguazo
 * @version 1.0
 */
public class GeneralesUtilidades {
	
	private static String[] UNIDADES = {"", "uno ", "dos ", "tres ", "cuatro ", "cinco ", "seis ", "siete ", "ocho ", "nueve "};
    private static String[] DECENAS = {"diez ", "once ", "doce ", "trece ", "catorce ", "quince ", "dieciseis ",
        "diecisiete ", "dieciocho ", "diecinueve", "veinte ", "treinta ", "cuarenta ",
        "cincuenta ", "sesenta ", "setenta ", "ochenta ", "noventa "};
    private static String[] CENTENAS = {"", "ciento ", "doscientos ", "trecientos ", "cuatrocientos ", "quinientos ", "seiscientos ",
        "setecientos ", "ochocientos ", "novecientos "};

	
	/**
	 * Transforma un decimal como string que contenga una ","
	 * a un decimal como string que contenga ".".
	 * @param st string a transformar
	 * @return El nuevo string ya tranformado.
	 */
	public static String transforma(String st){
		StringBuffer stb = new StringBuffer("");
		for(int i=0; i<st.length();i++){
			if(st.charAt(i)==','){
				stb.append('.');
			}else{
				stb.append(st.charAt(i));
			}
		}
		return stb.toString();
	}
	
	/**
	 * Valida el identificador ya sea RUC o CI.
	 * @param numero string que representa al identificador a ser verificado.
	 * @return true, si la validacion es correcta, caso contrario retorna false.
	 */
	public static boolean validarDocumento(String numero) {
		int[] digitos = new int[10];
		int sumaPar = 0;
		int sumaImpar = 0;
		int digitoVerificador;
		try {
			for (int i = 0; i < digitos.length; i++) {
				digitos[i] = Integer.parseInt("" + numero.charAt(i));
			}
			// Los primeros dos digitos corresponden al codigo de la provincia
			if ((Integer.parseInt("" + digitos[0] + "" + digitos[1])) <= 0
					|| (Integer.parseInt("" + digitos[0] + "" + digitos[1])) > 24) {
				return false;
			}
			// El tercer digito es:
			// 9 para sociedades privadas y extranjeros
			// 6 para sociedades publicas
			// menor que 6 (0,1,2,3,4,5) para personas naturales
			if (digitos[2] == 7 || digitos[2] == 8) {
				return false;
			}
			// Solo para personas naturales (modulo 10)
			if (digitos[2] < 6) {
				for (int i = 0; i < digitos.length - 1; i++) {
					if (((i + 1) % 2) == 0) {// par
						sumaPar += digitos[i];
					} else {// impar
						sumaImpar += digitos[i] * 2 > 9 ? (digitos[i] * 2) - 9 : digitos[i] * 2;
					}
				}
				digitoVerificador = (sumaPar + sumaImpar) % 10;
				digitoVerificador = digitoVerificador == 0 ? 0
						: 10 - digitoVerificador;
				if (digitoVerificador == digitos[9]) {
					// El ruc de las empresas del sector publico terminan con
					// 0001, 0002, etc
					if (numero.length() > 10) {
						if ((numero.substring(10, numero.length() - 1).equals("00"))
								&& (Integer.parseInt("" + numero.charAt(12)) >= 1)) {
							return true;
						} else {
							return false;
						}
					} else {
						return true;
					}
				} else {
					return false;
				}
			}

			// Solo para sociedades publicas (modulo 11)
			// Aqui el digito verficador esta en la posicion 9, en las otras 2
			// en la pos. 10
			if (digitos[2] == 6) {
				digitoVerificador = digitos[0] * 3 + digitos[1] * 2
						+ digitos[2] * 7 + digitos[3] * 6 + digitos[4] * 5
						+ digitos[5] * 4 + digitos[6] * 3 + digitos[7] * 2;
				digitoVerificador = digitoVerificador % 11;
				digitoVerificador = digitoVerificador == 0 ? 0
						: 11 - digitoVerificador;
				if (digitoVerificador == digitos[8]) {
					// El ruc de las empresas del sector publico terminan con
					// 0001, 0002, etc
					if ((numero.substring(9, numero.length() - 1).equals("000"))
							&& (Integer.parseInt("" + numero.charAt(12)) >= 1)) {
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			}

			/* Solo para entidades privadas (modulo 11) */
			if (digitos[2] == 9) {
				digitoVerificador = digitos[0] * 4 + digitos[1] * 3
						+ digitos[2] * 2 + digitos[3] * 7 + digitos[4] * 6
						+ digitos[5] * 5 + digitos[6] * 4 + digitos[7] * 3
						+ digitos[8] * 2;
				digitoVerificador = digitoVerificador % 11;
				digitoVerificador = digitoVerificador == 0 ? 0
						: 11 - digitoVerificador;
				if (digitoVerificador == digitos[9]) {
					// El ruc de las empresas del sector publico terminan con
					// 0001, 0002, etc
					if ((numero.substring(10, numero.length() - 1).equals("00"))
							&& (Integer.parseInt("" + numero.charAt(12)) >= 1)) {
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	
	
	public static String getFechaString(Date date) {
		SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("es", "EC"));
		return new String(formateador.format(date));
	}
	
	
	/**
	 * Completa la cadena recibida con el caracter comodin a la izq de la cadena original.
	 * @param cadena, string a ser completado
	 * @param numDigitos, numero de digitos que deberia tener el string al finalizar la tarea
	 * @param comodin, string con el que se completara la cadena original
	 * @return El string completado.
	 */
	public static String completarCadenaIzq(String cadena, int numDigitos, String comodin){
		int digFaltante;
		StringBuffer stb = new StringBuffer();
		if(cadena.length() < numDigitos){
			digFaltante = numDigitos - cadena.length();
			for(int i=0; i<digFaltante ; i++){
				stb.append(comodin);
			}
		}
		stb.append(cadena);
		return stb.toString();
	}
	
	/**
	 * Delimita la cadena recibida a numero de digitos y finaliza con el separador Final indicado.
	 * @param cadena, string a ser delimitado
	 * @param numDigitos, numero de digitos que debe tener el string al finalizar la tarea
	 * @param separadorFinal, string con el que se completara la cadena original
	 * @return El string delimitado.
	 */
	public static String delimitarCadena(String cadena, int numDigitos, String separadorFinal) {
		if(separadorFinal == null){
			separadorFinal = "";
		}
		if(cadena!=null){
			if(cadena.length() < numDigitos){
				return cadena;
			}else{
				cadena = cadena.substring(0, (numDigitos-separadorFinal.length()));
				return cadena+separadorFinal;
			}
		}else{
			return cadena;
		}
	}

	/**
	 * Verifica la edad minima de una persona (configurada en la constante)
	 * @param fechaNacimiento - fecha de nacimeinto 
	 * @return true- si la edad es correcta, false, de lo contrario
	 */
	public static boolean verificarEdad(Date fechaNacimiento){
		final long EDAD_MINIMA = 17;
	    return   calcularAnios(fechaNacimiento)  < EDAD_MINIMA? false: true;
	}
	
	/**
	 * Verifica la edad máxima de una persona (configurada en la constante)
	 * @param fechaNacimiento - fecha de nacimiento 
	 * @return true- si la edad es correcta, false, de lo contrario
	 */
	public static boolean verificarEdadMaxima(Date fechaNacimiento){
		final long EDAD_MAXIMA = 80;
	    return   calcularAnios(fechaNacimiento)  > EDAD_MAXIMA? false: true;
	}
	
	/**
	 * Cualcual cuantos anios tiene a partir de la fecha de nacimiento
	 * @param fechaNacimiento - fecha de nacimeinto 
	 * @return numero de años
	 */
	public static int calcularAnios(Date fechaNacimiento){
		Calendar fechaNacimientoCl = Calendar.getInstance();
		fechaNacimientoCl.setTime(fechaNacimiento);
		Calendar fechaActual = Calendar.getInstance();
		Date fechActual = new Date();
		fechaActual.setTime(fechActual);
		int anio = fechaActual.get(Calendar.YEAR) - fechaNacimientoCl.get(Calendar.YEAR);
		int mes = fechaActual.get(Calendar.MONTH) - fechaNacimientoCl.get(Calendar.MONTH);
		int dia = fechaActual.get(Calendar.DATE) - fechaNacimientoCl.get(Calendar.DATE);
		if (mes < 0 || (mes == 0 && dia < 0)) {
			anio--;
		}

		return anio;
	}
	
	/**
	 * Transformar String a Util.Date dependiendo del formato
	 * @param fecha - fecha como cadena
	 * @param formato - formato de fecha
	 * @return fecha como objeto Util.Date
	 * @throws ParseException - excepcion en la transformacion
	 */
	public static Date fechaCadenaToDate(String fecha, String formatoFecha) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat(formatoFecha);
		sdf.setLenient(false);
        return sdf.parse(fecha);
	}
	
	/**
	 * Transformar String sin separador a Util.Date dependiendo del formato de la fecha inicial
	 * @param fecha - fecha como cadena sin separador / o - y en el orden del formato ejemplo 20131231 para el formato yyyy-MM-dd
	 * @param formato - formato de fecha ejemplo yyyy-MM-dd
	 * @param separador - separador del formato ejemplo -
	 * @return fecha como objeto Util.Date
	 */
	public static Date fechaCadenaSinSepToDate(String fecha, String formatoFecha, String separador) throws ParseException{
		int contador = 0;
		fecha = fecha.toString().replace("-", "");
		fecha = fecha.toString().replace("/", "").trim();
		String formatoSplit[] = formatoFecha.split(separador);
		StringBuffer sb = new StringBuffer("");
		for (String item : formatoSplit) {
			sb.append(fecha.substring(contador, (contador + item.length())));
			sb.append(separador);
			contador = contador + item.length();
		}
		fecha = sb.toString();
		SimpleDateFormat sdf = new SimpleDateFormat(formatoFecha);
		sdf.setLenient(false);
        return sdf.parse(fecha);
	}
	
	/**
	 * Retorna la fecha del primer dia del mes en curso(de la fecha del servidor)
	 * @return fecha del primer dia del mes en curso
	 */
	public static Date getPrimerDiaDelMesActual() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.getActualMinimum(Calendar.DAY_OF_MONTH),
				cal.getMinimum(Calendar.HOUR_OF_DAY),
				cal.getMinimum(Calendar.MINUTE),
				cal.getMinimum(Calendar.SECOND));
		return cal.getTime();
	}

	/**
	 * Retorna la fecha del ultimo dia del mes en curso(de la fecha del servidor)
	 * @return fecha del ultimo dia del mes en curso
	 */
	public static Date getUltimoDiaDelMesActual() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.getActualMaximum(Calendar.DAY_OF_MONTH),
				cal.getMaximum(Calendar.HOUR_OF_DAY),
				cal.getMaximum(Calendar.MINUTE),
				cal.getMaximum(Calendar.SECOND));
		return cal.getTime();
	}
	
	/**
	 * Retorna la fecha del primer dia del mes de la fecha del parametro
	 * @param fecha - fecha de la que se calculara el primer dia de su mes.
	 * @return fecha del primer dia del mes en curso
	 */
	public static Date getPrimerDiaDelMes(Date fecha) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(fecha);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.getActualMinimum(Calendar.DAY_OF_MONTH),
				cal.getMinimum(Calendar.HOUR_OF_DAY),
				cal.getMinimum(Calendar.MINUTE),
				cal.getMinimum(Calendar.SECOND));
		return cal.getTime();
	}

	/**
	 * Retorna la fecha del ultimo dia del mes de la fecha del parametro.
	 * @param fecha - fecha de la que se calculara el ultimo dia de su mes.
	 * @return fecha del ultimo dia del mes en curso
	 */
	public static Date getUltimoDiaDelMes(Date fecha) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(fecha);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.getActualMaximum(Calendar.DAY_OF_MONTH),
				cal.getMaximum(Calendar.HOUR_OF_DAY),
				cal.getMaximum(Calendar.MINUTE),
				cal.getMaximum(Calendar.SECOND));
		return cal.getTime();
	}

	/**
	 * Elimina una palabra del texto original, luego lo corta al tamaï¿½o indicado.
	 * @param texto - texto original que se va a cortar
	 * @param palabra - palabra que se eliminarï¿½ del texto original
	 * @param tamanio - tamanio final que debe tener el texto al final del proceso
	 * @return cadena del texto cortado. 
	 */
	public static String cortarPalabra(String texto, String palabra, int tamanio) {
		return (texto.replace(palabra, "").substring(0, tamanio)) ;
	}
	
	/**
	 * Eliminar los saltos de linea de una cadena.
	 * @param texto - texto original que se va a cortar
	 * @return cadena del texto resultante. 
	 */
	public static String eliminarSaltos(String texto, String remplazo) {
		return (texto.replaceAll("[\n\r]",remplazo)) ;
	}
	
	/**
	 * Compara las fecha de un timestamp(fechaHora)
	 * @param fechaTSIni - fecha hora inicial
	 * @param fechaTSFin - fecha hora final
	 * @return -1 si fechaTSIni es menor que fechaTSFin, 0 si son iguales, 1 si fechaTSIni es mayor que fechaTSFin
	 */
	public static int compararTimeStamp(Timestamp fechaTSIni, Timestamp fechaTSFin){
		Date fechaIni = null;
		Date fechaFin = null;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			fechaIni = sdf.parse(sdf.format(fechaTSIni.getTime()));
			fechaFin = sdf.parse(sdf.format(fechaTSFin.getTime()));
		} catch (ParseException e) {
		}
		
		return fechaIni.compareTo(fechaFin);
	}
	
    /**
	 * Transforma las fechas de servipagos a timestamp
	 * @param stFecha - fecha como string
	 * @param stHora - hora como string
	 * @return Fecha y hora como timestamp
     * @throws ParseException 
	 */
	public static Timestamp fechaHoraStringATimeStamp(String stFecha, String stHora) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    	Date fecha = sdf.parse(stFecha+stHora);
    	Timestamp time = new Timestamp(fecha.getTime());
		return time;
	}

	 public static boolean isValidEmailAddress(String email) {
		 if(email==null || email.length()==0){
			 return false;
		 }else{
			 String PATRON_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
			 Pattern pattern = Pattern.compile(PATRON_EMAIL);
		     Matcher matcher = pattern.matcher(email);
		     return matcher.matches();
		 }
	 }
	 
	 
	 /**
	  * Extrae los ultimos caracteres de un string
	  * @param cadena - cadena de la que se extrae los caracteres
	  * @param numCaracteres - numero de caracteres a extraer
	  * @return - ultimos caracteres de la cadena
	  */
	 public static String extraeUltimosCaracteres(String cadena, int numCaracteres ){
		 if(cadena==null){
			 return "";
		 }
		 if(cadena.length()<=numCaracteres){
			 return cadena;
		 }
		 return cadena.substring((cadena.length()-4), cadena.length());
	 }
	 
	 
    /**
	 * Calcula la cantidad de dias de forma absoluta entre dos fechas
	 * @param fechaIni - fecha inicial para el calculo 
	 * @param fechaFin - fecha final para el calculo
	 * @return numero de dias de diferencia.
	 */
	public static int calcularDiferenciFechasEnDiasAbsolutos(Date fechaIni, Date fechaFin){
		final long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000;
		Calendar calIni = Calendar.getInstance();
		calIni.setTime(fechaIni);
		calIni.set(Calendar.HOUR, 0);
		calIni.set(Calendar.MINUTE, 0);
		calIni.set(Calendar.SECOND, 0);
		calIni.set(Calendar.MILLISECOND, 0);
		Calendar calFin = Calendar.getInstance();
		calFin.setTime(fechaFin);
		calFin.set(Calendar.HOUR, 0);
		calFin.set(Calendar.MINUTE, 0);
		calFin.set(Calendar.SECOND, 0);
		calFin.set(Calendar.MILLISECOND, 0);
		double diferencia= ( calFin.getTimeInMillis() - calIni.getTimeInMillis() )/ MILLSECS_PER_DAY;
		return (int)diferencia;
	}
	
	
	
	public static String stacktraceToString(Exception e){
//		StackTraceElement[] stack = e.getStackTrace();
//	    String exception = "";
//	    for (StackTraceElement s : stack) {
//	        exception = exception + s.toString() + "\n\t\t";
//	    }
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
		
	}
	
	/**
	 * Verifica si la fecha esta entre dos fechas 
	 * @param fechaIni - fecha inicial para el calculo 
	 * @param fechaFin - fecha final para el calculo
	 * @param fechaVerif - fecha que se necesita verificar
	 * @return true si esta entre flase caso contrario
	 */
	public static boolean verificarEntreFechas(Date fechaIni, Date fechaFin, Date fechaVerif){
		GregorianCalendar calendario = new GregorianCalendar();
		calendario.setTime(fechaFin);
		calendario.add(Calendar.HOUR, 3);
		fechaFin = calendario.getTime();
		
		return fechaVerif.after(fechaIni) && fechaVerif.before(fechaFin);
	}

	
	/**
	 *  Aproxima un BigDecimal a dos decimales 
	 * @param BigDecimal que se quiere aproximar
	 * @return BigDecimal aproximado a dos decimales
	 */
	public static BigDecimal aproximarDosDecimales(BigDecimal decimal){
		double redondeado = redondeoBasico(decimal.doubleValue(), 2);
		String cortado = cortarDecimales(redondeado, 2);
		return new BigDecimal(cortado);
	}
	
	/**
	 * Aproxima un double a dos decimales 
	 * @param double que se quiere aproximar
	 * @return double aproximado a dos decimales
	 */
	public static double aproximarDosDecimales(double decimal){
		double redondeado = redondeoBasico(decimal, 2);
		String cortado = cortarDecimales(redondeado, 2);
		return Double.parseDouble(cortado);
	}
	
	/**
	 * Aproxima un String a dos decimales 
	 * @param String que se quiere aproximar
	 * @return BigDecimal aproximado a dos decimales
	 */
	public static BigDecimal aproximarDosDecimales(String decimal){
		double redondeado = redondeoBasico(Double.parseDouble(decimal), 2);
		String cortado = cortarDecimales(redondeado, 2);
		return new BigDecimal(cortado);
	}
	
	/**
	 * Aproxima un String a dos decimales 
	 * @param String que se quiere aproximar
	 * @return BigDecimal aproximado a dos decimales
	 */
	public static double aproximarDosDecimalesDouble(String decimal){
		double redondeado = redondeoBasico(Double.parseDouble(decimal), 2);
		String cortado = cortarDecimales(redondeado, 2);
		return Double.parseDouble(cortado);
	}
	
	/**
	 * Aproxima un BigDecimal a dos decimales 
	 * @param BigDecimal que se quiere aproximar
	 * @return String aproximado a dos decimales
	 */
	public static String aproximarDosDecimalesStr(BigDecimal decimal){
		double redondeado = redondeoBasico(decimal.doubleValue(), 2);
		return cortarDecimales(redondeado, 2);
	}
	
	/**
	 *  Aproxima un BigDecimal a dos decimales 
	 * @param BigDecimal que se quiere aproximar
	 * @return double aproximado a dos decimales
	 */
	public static double aproximarDosDecimalesDouble(BigDecimal decimal){
		double redondeado = redondeoBasico(decimal.doubleValue(), 2);
		String cortado = cortarDecimales(redondeado, 2);
		return Double.parseDouble(cortado);
	}
	
	/**
	 * Aproxima un double a dos decimales 
	 * @param double que se quiere aproximar
	 * @return String aproximado a dos decimales
	 */
	public static String aproximarDosDecimalesStr(double decimal){
		double redondeado = redondeoBasico(decimal, 2);
		return cortarDecimales(redondeado, 2);
	}
	
	/**
	 *  Aproxima un BigDecimal a dos decimales 
	 * @param BigDecimal que se quiere aproximar
	 * @return BigDecimal aproximado a dos decimales
	 */
	public static BigDecimal aproximarDosDecimalesBigdc(double decimal){
		double redondeado = redondeoBasico(decimal, 2);
		String cortado = cortarDecimales(redondeado, 2);
		return new BigDecimal(cortado);
	}
	
	public static double redondeoBasico(double numero, int numDecimales){
		double potenciador = Math.pow(10.0, numDecimales);
		return (double) Math.round(numero * potenciador) / potenciador;
	}
	
	/**
	 * Aproxima un decimal a dos decimales ".".
	 * @param decimal double a aproximar
	 * @return El nuevo BigDecimal.
	 */
	public static String cortarDecimales(double decimal, int numDecimales){
		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
		simbolos.setDecimalSeparator('.');
		DecimalFormat formateador = new DecimalFormat("####.##",simbolos);
		formateador.setMaximumFractionDigits(numDecimales);
		formateador.setMinimumFractionDigits(numDecimales);
		return formateador.format(decimal);
	}
	
	public static String eliminarEspaciosEnBlanco(String cadena){
		return cadena.replaceAll(" +", " ").trim();
	}
	
	public static String eliminarEnterYEspaciosEnBlanco(String cadena){
		return cadena.replaceAll("[\n\r]", " ").replaceAll(" +", " ").trim().toUpperCase();
	}
	
	/**
	 * Genera una clave aleatoria
	 * @return - retorna la clave generada
	 */
	public static String generarClave(){
		String numeros = "123456789";
		String mayusculas = "ABCDEFGHIJKLMNPQRSTUVWXYZ";
		StringBuilder pswd = new StringBuilder();
		int tam = UsuarioConstantes.LARGO_CLAVE/2;
		for (int i = 0; i < tam; i++) {
			pswd.append(mayusculas.charAt((int)(Math.random() * mayusculas.length())));
		}
		pswd.append(".");
		int restante = UsuarioConstantes.LARGO_CLAVE - tam -1;
		for (int i = 0; i < restante; i++) {
			pswd.append(numeros.charAt((int)(Math.random() * numeros.length())));
		}
		return pswd.toString();
	}
	
	/**
	 * Genera el numero de tramite
	 * @param trmId - id del registro de tramite
	 * @param fecha - fecha de creacion del tramite
	 * @return
	 */
	public static String generarNumeroTramite(int trmId, Timestamp fecha){
		StringBuilder sb = new StringBuilder();
		sb.append("TRM_");
		sb.append(trmId);
		sb.append("_");
		SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy");
		sb.append(sdf.format(fecha.getTime()));
		return sb.toString();
	}
	
	/**
	 * Genera el string con codigos de caracteres especiales para poner en el correo electronico
	 * @param cadena - String cadena que se requiere transformar
	 * @return
	 */
	public static String generaStringParaCorreo(String cadena){
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<cadena.length();i++){
			switch (cadena.charAt(i)) {
			case 'Ñ':
				sb.append("&#209;");
				break;
			case 'Á':
				sb.append("&#193;");
				break;
			case 'É':
				sb.append("&#201;");
				break;
			case 'Í':
				sb.append("&#205;");
				break;
			case 'Ó':
				sb.append("&#211;");
				break;
			case 'Ú':
				sb.append("&#218;");
				break;
				
			case 'á':
				sb.append("&#225;");
				break;	
			case 'é':
				sb.append("&#233;");
				break;
			case 'í':
				sb.append("&#237;");
				break;
			case 'ó':
				sb.append("&#243;");
				break;
			case 'ú':
				sb.append("&#250;");
				break;

			default:
				sb.append(cadena.charAt(i));
				break;
			}
		}
		return sb.toString();
	}
	
	
	
	
	
	
	/**
	 * Obtiene si un campo es un entero
	 * 
	 * @param cadena
	 *            dato que se carga del archivo CSV
	 * @return boolean
	 * 
	 */

	public static boolean esEntero(String cadena) {
		if (cadena.matches("[0-9]*"))
		      return true;
		    else
		      return false;
	}

	/**
	 * Devuelve true si la cadena que llega es un numero decimal, false en caso
	 * contrario
	 * 
	 * @param cadena
	 *            dato que se carga del archivo CSV
	 * @return boolean
	 */
	public static boolean esDecimal(String cadena) {
		if (cadena.matches("[0-9]+.[0-9]+"))
			return true;
		else
			return false;
	}

	/**
	 * Devuelve true si la cadena que llega es un numero decimal, o un entero
	 * false en caso de no ser ninguno de los dos
	 * 
	 * @param cadena
	 *            cadena de texto que se validará
	 * @return boolean
	 */
	public static boolean validarFloat(String cadena){
		if(esDecimal(cadena)){
			return true;
		}
		if(esEntero(cadena)){
			return true;
		}
		return false;
	}
	
	/**
	 * Devuelve una cadena con el número enviado transformado a letras
	 * 
	 * 
	 * @param numero
	 *           cadena que contiene el número a convertir
	 * @return cadena del número convertido a letras
	 */
	 public static String convertirNumeroALetrasSinDecimales(String numero, boolean mayusculas) {
	        String literal = "";
//	        String parte_decimal;    
	        //si el numero utiliza (.) en lugar de (,) -> se reemplaza
	        numero = numero.replace(".", ",");
	        //si el numero no tiene parte decimal, se le agrega ,00
	        if(numero.indexOf(",")==-1){
	            numero = numero + ",00";
	        }
	        //se valida formato de entrada -> 0,00 y 999 999 999,00
	        if (Pattern.matches("\\d{1,9},\\d{1,2}", numero)) {
	            //se divide el numero 0000000,00 -> entero y decimal
	            String Num[] = numero.split(",");            
	            //se da formato al numero decimal
//	            parte_decimal = Num[1] + "/100";
	            //se convierte el numero a literal
	            if (Integer.parseInt(Num[0]) == 0) {//si el valor es cero
	                literal = "cero ";
	            } else if (Integer.parseInt(Num[0]) > 999999) {//si es millon
	                literal = getMillones(Num[0]);
	            } else if (Integer.parseInt(Num[0]) > 999) {//si es miles
	                literal = getMiles(Num[0]);
	            } else if (Integer.parseInt(Num[0]) > 99) {//si es centena
	                literal = getCentenas(Num[0]);
	            } else if (Integer.parseInt(Num[0]) > 9) {//si es decena
	                literal = getDecenas(Num[0]);
	            } else {//sino unidades -> 9
	                literal = getUnidades(Num[0]);
	            }
	            //devuelve el resultado en mayusculas o minusculas
	            if (mayusculas) {
	                return (literal ).toUpperCase();
	            } else {
	                return (literal );
	            }
	        } else {//error, no se puede convertir
	            return literal = null;
	        }
	 }
	
	/**
	 * Devuelve una cadena con el número enviado transformado a letras
	 * 
	 * 
	 * @param numero
	 *           cadena que contiene el número a convertir
	 * @return cadena del número convertido a letras
	 */
	 public static String convertirNumeroALetrasConDecimales(String numero, boolean mayusculas) {
	        String literal = "";
	        String parte_decimal;    
	        //si el numero utiliza (.) en lugar de (,) -> se reemplaza
	        numero = numero.replace(".", ",");
	        //si el numero no tiene parte decimal, se le agrega ,00
	        if(numero.indexOf(",")==-1){
	            numero = numero + ",00";
	        }
	        //se valida formato de entrada -> 0,00 y 999 999 999,00
	        if (Pattern.matches("\\d{1,9},\\d{1,2}", numero)) {
	            //se divide el numero 0000000,00 -> entero y decimal
	            String Num[] = numero.split(",");            
	            //se da formato al numero decimal
	            parte_decimal = Num[1] + "/100";
	            //se convierte el numero a literal
	            if (Integer.parseInt(Num[0]) == 0) {//si el valor es cero
	                literal = "cero ";
	            } else if (Integer.parseInt(Num[0]) > 999999) {//si es millon
	                literal = getMillones(Num[0]);
	            } else if (Integer.parseInt(Num[0]) > 999) {//si es miles
	                literal = getMiles(Num[0]);
	            } else if (Integer.parseInt(Num[0]) > 99) {//si es centena
	                literal = getCentenas(Num[0]);
	            } else if (Integer.parseInt(Num[0]) > 9) {//si es decena
	                literal = getDecenas(Num[0]);
	            } else {//sino unidades -> 9
	                literal = getUnidades(Num[0]);
	            }
	            //devuelve el resultado en mayusculas o minusculas
	            if (mayusculas) {
	                return (literal + parte_decimal).toUpperCase();
	            } else {
	                return (literal + parte_decimal);
	            }
	        } else {//error, no se puede convertir
	            return literal = null;
	        }
	 }

	 /* funciones para convertir los numeros a literales */
	 private static String getUnidades(String numero) {// 1 - 9
		 //si tuviera algun 0 antes se lo quita -> 09 = 9 o 009=9
	     String num = numero.substring(numero.length() - 1);
	     return UNIDADES[Integer.parseInt(num)];
	 }
	    
	 /* funciones para obtener las decenas */
	 private static String getDecenas(String num) {// 99                        
		 int n = Integer.parseInt(num);
	     if (n < 10) {//para casos como -> 01 - 09
	         return getUnidades(num);
	     } else if (n > 19) {//para 20...99
	         String u = getUnidades(num);
	         if (u.equals("")) { //para 20,30,40,50,60,70,80,90
	             return DECENAS[Integer.parseInt(num.substring(0, 1)) + 8];
	         } else {
	             return DECENAS[Integer.parseInt(num.substring(0, 1)) + 8] + "y " + u;
	         }
	     } else {//numeros entre 11 y 19
	         return DECENAS[n - 10];
	     }
	 }

	 /* funciones para obtener las centenas */
	 private static String getCentenas(String num) {// 999 o 099
	     if( Integer.parseInt(num)>99 ){//es centena
	         if (Integer.parseInt(num) == 100) {//caso especial
	             return " cien ";
	         } else {
	              return CENTENAS[Integer.parseInt(num.substring(0, 1))] + getDecenas(num.substring(1));
	         } 
	     }else{//por Ej. 099 
	         //se quita el 0 antes de convertir a decenas
	         return getDecenas(Integer.parseInt(num)+"");            
	     }        
	 }

	 /* funciones para obtener los miles */
	 private static String getMiles(String numero) {// 999 999
	     //obtiene las centenas
	     String c = numero.substring(numero.length() - 3);
	     //obtiene los miles
	     String m = numero.substring(0, numero.length() - 3);
	     String n="";
	     //se comprueba que miles tenga valor entero
	     if (Integer.parseInt(m) > 0) {
	         n = getCentenas(m);           
	         return n + "mil " + getCentenas(c);
	     } else {
	         return "" + getCentenas(c);
	     }
    }

	 /* funciones para obtener los millones */
	 private static String getMillones(String numero) { //000 000 000        
	     //se obtiene los miles
	     String miles = numero.substring(numero.length() - 6);
	     //se obtiene los millones
	     String millon = numero.substring(0, numero.length() - 6);
	     String n = "";
	     if(millon.length()>1){
	         n = getCentenas(millon) + "millones ";
	     }else{
	         n = getUnidades(millon) + "millon ";
	     }
	     return n + getMiles(miles);        
	 }
	 
	 /**
	 * Método que devuelve el nombre del mes correspondiente al entero enviado
	 * 
	 * @param cadena
	 *            cadena de texto que corresponde al número del mes
	 * @return retorno
	 * cadena que corresponde al nombre del mes
	 */
	public static String tranformaNumeroEnMesesMinusculas(int mes){
	String retorno=null;
		switch (mes) {
			case 1:
				retorno = "enero";
				break;
			case 2:
				retorno = "febrero";
				break;
			case 3:
				retorno = "marzo";
				break;
			case 4:
				retorno = "abril";
				break;
			case 5:
				retorno = "mayo";
				break;
			case 6:
				retorno = "junio";
				break;
			case 7:
				retorno = "julio";
				break;
			case 8:
				retorno = "agosto";
				break;
			case 9:
				retorno = "septiembre";
				break;
			case 10:
				retorno = "octubre";
				break;
			case 11:
				retorno = "noviembre";
				break;
			case 12:
				retorno = "diciembre";
				break;	
			default:
				break;
		}
		return retorno;
	}
		
	 /**
	 * Calcula la cantidad de dias entre do fechas
	 * @param fechaIni - fecha inicial para el calculo 
	 * @param fechaFin - fecha final para el calculo
	 * @return numero de dias de diferencia.
	 */
	public static int calcularDiferenciFechas(Date fechaIni, Date fechaFin){
		final long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000;
		double diferencia= ( fechaFin.getTime() - fechaIni.getTime() )/ MILLSECS_PER_DAY;
		return (int)diferencia;
	}

	public static String[] separarNombres(String nombres){
		String[] listaArticulos = {"el", "la", "de", "los", "las","del"};
		
		String[] retorno = new String[2];
		if(nombres == null ){
			return null;
		}
		String asd = nombres.replaceAll(" +", " ").trim();
		
		String[] aux = asd.split("\\s+");
		if(aux.length == 1){
			retorno[0] = aux[0];
			retorno[1] = null;
		}else{
			int contNombres = 0;
			
			StringBuilder sb = new StringBuilder();
			boolean esArticulo = false;
			for (String item : aux) {
				if(contNombres == 0){
					//verifico si es articulo
					if(item.length() <=3){
						esArticulo = false;
						for (String itemArt: listaArticulos) {
							if(item.equalsIgnoreCase(itemArt)){//es articulo
								esArticulo = true;
								break;
							}
						}
						
						if(!esArticulo){
							sb.append(item);
							sb.append(" ");
							contNombres++;
							retorno[0]= sb.toString().replaceAll(" +", " ").trim();
							sb = new StringBuilder();
						}else{
							sb.append(item);
							sb.append(" ");
						}
					}else{
						sb.append(item);
						sb.append(" ");
						retorno[0]= sb.toString().replaceAll(" +", " ").trim();
						contNombres++;
						sb = new StringBuilder();
					}
				}else{
					sb.append(item);
					sb.append(" ");
				}
			}
			retorno[1]= sb.toString().replaceAll(" +", " ").trim();
		}
		return retorno;
	}
	

	 /**
		 * Determina si un número entero está en un rango
		 * @param x - número entero a comparar 
		 * @param inferior - límite inferior
		 * @param superior - límite superior
		 * @return boolean.
		 */
	public static boolean estaEntre(int x, int inferior, int superior) {
		  return inferior <= x && x <= superior;
		}
	
	
	 /**
	 * Elimina tildes de una cadena
	 * @param campoEditar - cadena a editar 
	 * @return boolean.
	 */
	public static String eliminarTildes(String campoEditar) {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<campoEditar.length();i++){
			switch (campoEditar.charAt(i)) {
			case 'Á':
				sb.append("A");
				break;
			case 'É':
				sb.append("E");
				break;
			case 'Í':
				sb.append("I");
				break;
			case 'Ó':
				sb.append("O");
				break;
			case 'Ú':
				sb.append("U");
				break;
			default:
				sb.append(campoEditar.charAt(i));
				break;
			}
		}
		return sb.toString();
	}
	
	
	 public static String quitaEspacios(String texto) {
		 String sTexto = texto;
		 StringTokenizer tokens = new StringTokenizer(sTexto);
		 StringBuilder buff = new StringBuilder();
		 while (tokens.hasMoreTokens()) {
	            buff.append(" ").append(tokens.nextToken());
	     }
	     return buff.toString();
	 }
	 
	 public static boolean validarUrl(String texto) {
		boolean retorno = false;
	    UrlValidator validar = new UrlValidator();
	    if(validar.isValid(texto)) {
	    	retorno  = true;
	    	return retorno;
//	    	System.out.println("URL válida");
	    }else{
	    	return false;
//	    	System.out.println("URL no válida");
	    }
	 }
	 
	 /** Método que devuelve el contenido de un Mail correspondiente al funcionario o al administrador de nóminas
		 * 
		 * @param aux true -> funcionario ----- false -> administrador de nómina
		 * @param fecha mes y año que corresponde el rol
		 * @param cadena nombre del funcionario
		 * @return cadena con el mail
		 */
		  public static StringBuilder generarAsunto(String fecha,String nombres, String facultad, String carrera){
				StringBuilder sb = new StringBuilder();
				sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" >");
				sb.append("<html>");
				sb.append("<head>");
				sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
				sb.append("<title>UNIVERSIDAD CENTRAL DEL ECUADOR</title>");
				sb.append("</head>");
				sb.append("<body>");
				sb.append("<p><strong>Estimado(a). ");
				for(int i=0;i<nombres.length();i++){
					if (nombres.charAt(i)=='Ñ'){
						sb.append("&#209;");
					}else{
						sb.append(nombres.charAt(i));
					}
				}
				sb.append(".-</strong></p>");
				sb.append("<p><strong>Facultad: </strong>");sb.append(facultad);sb.append("</p>");
				sb.append("<p><strong>Carrera/Programa: </strong>");sb.append(carrera);sb.append("</p>");
				sb.append("<br/>");
				sb.append("La Unidad de Titulaci&#243;n  Especial de la Universidad Central del Ecuador le comunica que una vez que ha aceptado el registro en el curso de actualizaci&#243;n de conocimientos, usted deber&#225; seguir el siguiente proceso:");
				sb.append("<br/>");
				sb.append("Para continuar con el proceso deber&#225;: ");
				sb.append("<br/>");
				sb.append("1.- Imprimir la orden de cobro generada en la plataforma de Titulaci&#243;n. ");
				sb.append("<br/>");
				sb.append("2.- Acercarse a la oficina del Director de su carrera y hacer firmar la orden de cobro. ");
				sb.append("<br/>");
				sb.append("3.- Dirigirse al departamento financiero de su facultad para generar el voucher y poder proceder con el pago. ");
				sb.append("<br/>");
				sb.append("4.- Finalmente con el comprobante de pago, usted deber&#225; dirigirse a la Secretar&#237;a de su carrera para registrar el pago en la plataforma de Titulaci&#243;n. ");
				sb.append("<br/>");
				sb.append("NOTA.- Cabe resaltar que previo a la declaraci&#243;n de <strong>APTITUD</strong>, usted deber&#225; haber aprobado el proceso de <strong>ACTUALIZACION DE CONOCIMIENTOS.</strong>");
				sb.append("<br/>");
				sb.append("El proceso se realiz&#243; el ");sb.append(fecha);
				sb.append("</p></strong>");
				sb.append("<br/>");
				sb.append(" <strong>Informaci&#243;n del Correo: </strong> Mail generado autom&#225;ticamente. Por favor no responda a este mensaje.");
				sb.append("<br/>Universidad Central del Ecuador.");
				sb.append("<br/>");
				sb.append("<br/>");
				sb.append("------------------------------------------------------------------------------------------------------------------");
				sb.append("<table width=\"604\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" height=\"130\" style=\"border-collapse:collapse\">");
				sb.append("<tbody>");
				sb.append("<tr style=\"min-height:10.1pt\">");
				sb.append("<td width=\"132\" valign=\"top\" style=\"width:99.25pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
				sb.append("<br/><br/>");
				sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
				sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Sistema de titulaci&#243;n<br></span></b>");
				sb.append("</p>");
				sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
				sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">DTIC-UCE</span>");
				sb.append("<span style=\"font-size:10.0pt;color:#1f497d\"></span>");
				sb.append("</p>");
				sb.append("</td>");
				sb.append("<td width=\"96\" valign=\"top\" style=\"width:71.75pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 5.4pt 0cm 5.4pt;min-height:10.1pt\">");
				sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
				sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Universidad Central del Ecuador</span></b>");
				sb.append("</p>");
				sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
				sb.append("<img width=\"96\" height=\"93\" src=\"http://aka-cdn.uce.edu.ec/ares/tmp/logo3.png\" alt=\"Foto\" style=\"margin-left:0px;margin-top:0px\">");
				sb.append("</p>");
				sb.append("</td>");
				sb.append("<td width=\"169\" valign=\"top\" style=\"width:126.7pt;border:none;border-right:solid white 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
				sb.append("<br/>  <br/>");
				sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1.0pt\">");
				sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Ciudadela Universitaria, Av. Am&#233;rica </span>");
				sb.append("</p>");
				sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1pt\">");
				sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Quito - Ecuador</span>");
				sb.append("</p>");
				sb.append("</td>");
				sb.append("</tr>");
				sb.append("</tbody>");
				sb.append("</table>");
				sb.append("------------------------------------------------------------------------------------------------------------------");
				sb.append("</body>");
				sb.append("</html>");
				return sb;
			}
		  /**
			 * Genera el string con codigos de caracteres especiales para poner en el correo electronico
			 * @param cadena - String cadena que se requiere transformar
			 * @return
			 */
			public static String generaStringConTildes(String cadena){
				StringBuilder sb = new StringBuilder();
				for(int i=0;i<cadena.length();i++){
					switch (cadena.charAt(i)) {
					case 'Ñ':
						sb.append("&#209;");
						break;
					case 'Á':
						sb.append("&#193;");
						break;
					case 'É':
						sb.append("&#201;");
						break;
					case 'Í':
						sb.append("&#205;");
						break;
					case 'Ó':
						sb.append("&#211;");
						break;
					case 'Ú':
						sb.append("&#218;");
						break;

					case 'á':
						sb.append("&#225;");
						break;	
					case 'é':
						sb.append("&#233;");
						break;
					case 'í':
						sb.append("&#237;");
						break;
					case 'ó':
						sb.append("&#243;");
						break;
					case 'ú':
						sb.append("&#250;");
						break;

					default:
						sb.append(cadena.charAt(i));
						break;
					}
				}
				return sb.toString();
			}

			 /** Método que devuelve el contenido de un Mail correspondiente al funcionario o al administrador de nóminas
			 * 
			 * @param aux true -> funcionario ----- false -> administrador de nómina
			 * @param fecha mes y año que corresponde el rol
			 * @param cadena nombre del funcionario
			 * @return cadena con el mail
			 */
			  public static StringBuilder generarAsuntoDosPeriodosSelecciona(String fecha,String nombres, String facultad, String carrera){
					StringBuilder sb = new StringBuilder();
					sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" >");
					sb.append("<html>");
					sb.append("<head>");
					sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
					sb.append("<title>UNIVERSIDAD CENTRAL DEL ECUADOR</title>");
					sb.append("</head>");
					sb.append("<body>");
					sb.append("<p><strong>Estimado(a). ");
					for(int i=0;i<nombres.length();i++){
						if (nombres.charAt(i)=='Ñ'){
							sb.append("&#209;");
						}else{
							sb.append(nombres.charAt(i));
						}
					}
					sb.append(".-</strong></p>");
					sb.append("<p><strong>Facultad: </strong>");sb.append(facultad);sb.append("</p>");
					sb.append("<p><strong>Carrera/Programa: </strong>");sb.append(carrera);sb.append("</p>");
					sb.append("<br/>");
					sb.append("La Unidad de Titulaci&#243;n  Especial de la Universidad Central del Ecuador le comunica que una vez revisada su documentaci&#243;n se visualiza que la culminaci&#243;n de su malla curricular exceden los 12 meses establecidos para realizar y concluir con su trabajo de titulaci&#243;n, seg&#250;n lo establecido en las Disposiciones Tercera y Cuarta del Reglamento de R&#233;gimen Acad&#233;mico.");
					sb.append("<br/>");
					sb.append("En este sentido le informamos que debe: ");
					sb.append("<br/>");
					sb.append("1. Actualizar Conocimientos para lo cual usted tendr&#225; que cancelar un arancel establecido por la Universidad, mediante una orden de cobro emitida por la carrera.");
					sb.append("<br/>");
					sb.append("<br/>");
					sb.append("NOTA.- Cabe resaltar que previo a rendir el examen complexivo o a la defensa oral del trabajo de titulaci&#243;n tendr&#225; que ser declarado APTO, es decir, cuando cumpla con todos los requisitos previos a la titulaci&#243;n.");
					sb.append("<br/>");
					sb.append("El proceso se realiz&#243; el ");sb.append(fecha);
					sb.append("</p></strong>");
					sb.append("<br/>");
					sb.append(" <strong>Informaci&#243;n del Correo: </strong> Mail generado autom&#225;ticamente. Por favor no responda a este mensaje.");
					sb.append("<br/>Universidad Central del Ecuador.");
					sb.append("<br/>");
					sb.append("<br/>");
					sb.append("------------------------------------------------------------------------------------------------------------------");
					sb.append("<table width=\"604\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" height=\"130\" style=\"border-collapse:collapse\">");
					sb.append("<tbody>");
					sb.append("<tr style=\"min-height:10.1pt\">");
					sb.append("<td width=\"132\" valign=\"top\" style=\"width:99.25pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
					sb.append("<br/><br/>");
					sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
					sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Sistema de titulaci&#243;n<br></span></b>");
					sb.append("</p>");
					sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
					sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">DTIC-UCE</span>");
					sb.append("<span style=\"font-size:10.0pt;color:#1f497d\"></span>");
					sb.append("</p>");
					sb.append("</td>");
					sb.append("<td width=\"96\" valign=\"top\" style=\"width:71.75pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 5.4pt 0cm 5.4pt;min-height:10.1pt\">");
					sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
					sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Universidad Central del Ecuador</span></b>");
					sb.append("</p>");
					sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
					sb.append("<img width=\"96\" height=\"93\" src=\"http://aka-cdn.uce.edu.ec/ares/tmp/logo3.png\" alt=\"Foto\" style=\"margin-left:0px;margin-top:0px\">");
					sb.append("</p>");
					sb.append("</td>");
					sb.append("<td width=\"169\" valign=\"top\" style=\"width:126.7pt;border:none;border-right:solid white 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
					sb.append("<br/>  <br/>");
					sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1.0pt\">");
					sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Ciudadela Universitaria, Av. Am&#233;rica </span>");
					sb.append("</p>");
					sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1pt\">");
					sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Quito - Ecuador</span>");
					sb.append("</p>");
					sb.append("</td>");
					sb.append("</tr>");
					sb.append("</tbody>");
					sb.append("</table>");
					sb.append("------------------------------------------------------------------------------------------------------------------");
					sb.append("</body>");
					sb.append("</html>");
					return sb;
				}
			
			  
			  public static StringBuilder generarAsuntoDosPeriodosOtrosMecanismos(String fecha,String nombres, String facultad, String carrera){
					StringBuilder sb = new StringBuilder();
					sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" >");
					sb.append("<html>");
					sb.append("<head>");
					sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
					sb.append("<title>UNIVERSIDAD CENTRAL DEL ECUADOR</title>");
					sb.append("</head>");
					sb.append("<body>");
					sb.append("<p><strong>Estimado(a). ");
					for(int i=0;i<nombres.length();i++){
						if (nombres.charAt(i)=='Ñ'){
							sb.append("&#209;");
						}else{
							sb.append(nombres.charAt(i));
						}
					}
					sb.append(".-</strong></p>");
					sb.append("<p><strong>Facultad: </strong>");sb.append(facultad);sb.append("</p>");
					sb.append("<p><strong>Carrera/Programa: </strong>");sb.append(carrera);sb.append("</p>");
					sb.append("<br/>");
					sb.append("La Unidad de Titulaci&#243;n  Especial de la Universidad Central del Ecuador le comunica que una vez revisada su documentaci&#243;n se visualiza que usted excede los 12 meses de haber culminado su malla curricular.");
					sb.append("<br/>");
					sb.append("En este sentido le informamos que debe: ");
					sb.append("<br/>");
					sb.append("1. Actualizar Conocimientos para lo cual usted tendr&#225; que cancelar un arancel establecido por la Universidad, mediante una orden de cobro emitida por la carrera.");
					sb.append("<br/>");
					sb.append("<br/>");
					sb.append("NOTA.- Cabe resaltar que previo a la defensa oral del trabajo de titulaci&#243;n tendr&#225; que ser declarado APTO, es decir, cuando cumpla con todos los requisitos previos a la titulaci&#243;n.");
					sb.append("<br/>");
					sb.append("El proceso se realiz&#243; el ");sb.append(fecha);
					sb.append("</p></strong>");
					sb.append("<br/>");
					sb.append(" <strong>Informaci&#243;n del Correo: </strong> Mail generado autom&#225;ticamente. Por favor no responda a este mensaje.");
					sb.append("<br/>Universidad Central del Ecuador.");
					sb.append("<br/>");
					sb.append("<br/>");
					sb.append("------------------------------------------------------------------------------------------------------------------");
					sb.append("<table width=\"604\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" height=\"130\" style=\"border-collapse:collapse\">");
					sb.append("<tbody>");
					sb.append("<tr style=\"min-height:10.1pt\">");
					sb.append("<td width=\"132\" valign=\"top\" style=\"width:99.25pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
					sb.append("<br/><br/>");
					sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
					sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Sistema de titulaci&#243;n<br></span></b>");
					sb.append("</p>");
					sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
					sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">DTIC-UCE</span>");
					sb.append("<span style=\"font-size:10.0pt;color:#1f497d\"></span>");
					sb.append("</p>");
					sb.append("</td>");
					sb.append("<td width=\"96\" valign=\"top\" style=\"width:71.75pt;border:none;border-right:solid windowtext 1.5pt;padding:0cm 5.4pt 0cm 5.4pt;min-height:10.1pt\">");
					sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
					sb.append("<b><span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Universidad Central del Ecuador</span></b>");
					sb.append("</p>");
					sb.append("<p align=\"center\" style=\"text-align:center;line-height:10.1pt\">");
					sb.append("<img width=\"96\" height=\"93\" src=\"http://aka-cdn.uce.edu.ec/ares/tmp/logo3.png\" alt=\"Foto\" style=\"margin-left:0px;margin-top:0px\">");
					sb.append("</p>");
					sb.append("</td>");
					sb.append("<td width=\"169\" valign=\"top\" style=\"width:126.7pt;border:none;border-right:solid white 1.5pt;padding:0cm 0cm 0cm 0cm;min-height:10.1pt\">");
					sb.append("<br/>  <br/>");
					sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1.0pt\">");
					sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Ciudadela Universitaria, Av. Am&#233;rica </span>");
					sb.append("</p>");
					sb.append("<p align=\"right\" style=\"text-align:center;line-height:10.1pt\">");
					sb.append("<span lang=\"ES-CR\" style=\"font-size:10.0pt font-family:Serif\">Quito - Ecuador</span>");
					sb.append("</p>");
					sb.append("</td>");
					sb.append("</tr>");
					sb.append("</tbody>");
					sb.append("</table>");
					sb.append("------------------------------------------------------------------------------------------------------------------");
					sb.append("</body>");
					sb.append("</html>");
					return sb;
				}
}

