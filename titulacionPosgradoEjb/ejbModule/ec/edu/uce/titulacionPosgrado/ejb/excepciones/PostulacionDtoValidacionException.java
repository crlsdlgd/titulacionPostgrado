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
   
 ARCHIVO:     PostulacionDtoValidacionException.java      
 DESCRIPCION: Clase encargada de gestionar las excepciones cuando no finaliza 
 			  todas las validaciones en el servicio de PostulacionDto. 
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 20/02/2018			Daniel Albuja                      Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.excepciones;


/**
 * Clase (Exception)PostulacionDtoValidacionException.
 * Clase encargada de gestionar las excepciones cuando no finaliza todas las validaciones en el servicio de PostulacionDto.  
 * @author dalbuja
 * @version 1.0
 */
public class PostulacionDtoValidacionException extends Exception {
	private static final long serialVersionUID = 181315003339767230L;

	/**
	 * Contructor nulo de la clase
	 */
	public PostulacionDtoValidacionException() {
		super();
	}

	/**
	 * Sobrecarga del constructor
	 * @param message - mensaje de error
	 */
	public PostulacionDtoValidacionException(String message) {
		super(message);
	}

	/**
	 * Sobrecarga del constructor
	 * @param message - mensaje en tiempo de ejecucion
	 */
	public PostulacionDtoValidacionException(Throwable message) {
		super(message);
	}

	/**
	 * Sobrecarga del constructor
	 * @param message - mensaje de error
	 * @param cause - mensaje en tiempo de ejecucion
	 */
	public PostulacionDtoValidacionException(String message, Throwable cause) {
		super(message, cause);
	}

}
