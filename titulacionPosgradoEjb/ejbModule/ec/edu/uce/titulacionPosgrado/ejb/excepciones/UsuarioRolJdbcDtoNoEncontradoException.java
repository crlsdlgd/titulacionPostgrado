/**************************************************************************
 *				(c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción o distribución no autorizada de este programa, 
 * o cualquier porción de él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán procesadas con el grado máximo contemplado 
 * por la ley.
  ************************************************************************* 
 ARCHIVO:     ConfiguracionCarreraNoEncontradoDtoException.java	  
 DESCRIPCION: Clase de excepcion lanzada cuando no se encuentra la entidad ConfiguracionCarreraDto.
 *************************************************************************
                           	MODIFICACIONES
 FECHA         		     AUTOR          					COMENTARIOS
 20/02/2018			Daniel Albuja		Gabriel Mafla 				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.excepciones;


/**
 * Clase (Exception)ConfiguracionCarreraDtoNoEncontradoException.
 * Clase de excepcion lanzada cuando no se encuentra la entidad ConfiguracionCarreraDto.
 * @author dalbuja
 * @version 1.0
 */
public class UsuarioRolJdbcDtoNoEncontradoException extends Exception{
	private static final long serialVersionUID = -3995370847716513453L;

	/**
	 * Constructor por defecto de la excepcion.
	 */
	public UsuarioRolJdbcDtoNoEncontradoException() {
		super();
	}

	/**
	 * Constructor de la excepcion el cual acepta un mensaje de error como parametro del
	 * constructor.
	 * @param message El mensaje de error.
	 */
	public UsuarioRolJdbcDtoNoEncontradoException(String message) {
		super(message);
	}

	/**
	 * Constructor de la excepcion el cual acepta un mensaje de error y un objecto de tipo Throwable
	 * que representa la causa de esta excepcion.
	 * @param message El mensaje de error.
	 * @param cause La causa de la excepcion.
	 * 
	 * @see java.lang.Throwable
	 */
	public UsuarioRolJdbcDtoNoEncontradoException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor de la excepcion el cual acepta la causa de la excepcion.
	 * @param cause La causa de la excepcion.
	 * 
	 * @see java.lang.Throwable
	 */
	public UsuarioRolJdbcDtoNoEncontradoException(Throwable cause) {
		super(cause);
	}
}
