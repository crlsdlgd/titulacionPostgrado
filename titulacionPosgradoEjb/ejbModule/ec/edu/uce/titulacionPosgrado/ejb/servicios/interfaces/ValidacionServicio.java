/**************************************************************************
 *                (c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y está protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción o distribución no autorizada de este programa, 
 * o cualquier porción de él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán procesadas con el grado máximo contemplado 
 * por la ley.
 
 ************************************************************************* 
   
 ARCHIVO:     VigenciaServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Validación.  
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 06-04-2016	           Daniel Albuja                      Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces;


import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ValidacionException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ValidacionNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ValidacionValidacionException;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Validacion;

/**
 * Interface ValidacionServicio
 * Interfase que define las operaciones sobre la tabla Vigencia.
 * @author dalbuja
 * @version 1.0
 */
public interface ValidacionServicio {
	/**
	 * Busca una entidad Validación por su id
	 * @param id - de la Validación a buscar
	 * @return Validación con el id solicitado
	 * @throws ValidaciónNoEncontradoException - Excepcion lanzada cuando no se encuentra una Validación con el id solicitado
	 * @throws ValidaciónException - Excepcion general
	 */
	public Validacion buscarPorId(Integer id) throws ValidacionNoEncontradoException, ValidacionValidacionException, ValidacionException;
	
	/**
	 * Lista todas las entidades Vigencia existentes en la BD relacionadas al trttId
	 * @return lista de todas las entidades Vigencia existentes en la BD
	 */
	public Validacion buscarXtrttId(Integer trttId) throws ValidacionNoEncontradoException;

	public Validacion cambiarCulminoMalla(Integer vldId)
			throws ValidacionNoEncontradoException;

	public void modificarActualizarConocimientos(Integer vldId)
			throws ValidacionNoEncontradoException;

}
