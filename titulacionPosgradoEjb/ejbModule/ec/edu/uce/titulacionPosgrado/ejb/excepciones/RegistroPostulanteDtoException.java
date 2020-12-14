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
   
 ARCHIVO:     RegistroPostulanteDtoException.java      
 DESCRIPCION: Clase encargada de gestionar las excepciones generales del servicio de carrera. 
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 20/02/2018			Daniel Albuja                      Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.excepciones;


/**
 * Clase (Exception)RegistroPostulanteDtoException.
 * Clase encargada de gestionar las excepciones generales del servicio de RegistroPostulanteDto. 
 * @author dalbuja
 * @version 1.0
 */
public class RegistroPostulanteDtoException extends Exception {
	private static final long serialVersionUID = -800530230844770228L;

	/**
	 * Contructor nulo de la clase
	 */
	public RegistroPostulanteDtoException() {
		super();
	}

	/**
	 * Sobrecarga del constructor
	 * @param message - mensaje de error
	 */
	public RegistroPostulanteDtoException(String message) {
		super(message);
	}

	/**
	 * Sobrecarga del constructor
	 * @param message - mensaje en tiempo de ejecucion
	 */
	public RegistroPostulanteDtoException(Throwable message) {
		super(message);
	}

	/**
	 * Sobrecarga del constructor
	 * @param message - mensaje de error
	 * @param cause - mensaje en tiempo de ejecucion
	 */
	public RegistroPostulanteDtoException(String message, Throwable cause) {
		super(message, cause);
	}

}
