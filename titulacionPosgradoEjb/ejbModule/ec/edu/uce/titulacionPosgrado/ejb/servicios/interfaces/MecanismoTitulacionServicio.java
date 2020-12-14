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
   
 ARCHIVO:     MecanismoTitulacionServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla MecanismoTitulacion.  
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 06-Mayo-2016           Gabriel Mafla                      Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces;


import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.MecanismoTitulacionException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.MecanismoTitulacionNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.MecanismoTitulacion;

/**
 * Interface MecanismoTitulacionServicio
 * Interfase que define las operaciones sobre la tabla MecanismoTitulacion.
 * @author gmafla
 * @version 1.0
 */
public interface MecanismoTitulacionServicio {
	/**
	 * Busca una entidad MecanismoTitulacion por su id
	 * @param id - de la MecanismoTitulacion a buscar
	 * @return MecanismoTitulacion con el id solicitado
	 * @throws MecanismoTitulacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una MecanismoTitulacion con el id solicitado
	 * @throws MecanismoTitulacionException - Excepcion general
	 */
	public MecanismoTitulacion buscarPorId(Integer id) throws MecanismoTitulacionNoEncontradoException, MecanismoTitulacionException;
	
	/**
	 * Lista todas las entidades MecanismoTitulacion existentes en la BD
	 * @return lista de todas las entidades MecanismoTitulacion existentes en la BD
	 */
	public List<MecanismoTitulacion> listarTodos() throws MecanismoTitulacionNoEncontradoException;
	
	/**
	 * Busca los Mecanismos de Titulación que coinciden con el patrón ingresado. 
	 * @param patron -> String que contiene el Mecanismo de Titulación que desea buscar.
	 * @return Lista de Mecanismos de titulación que coincidieron en la busqueda
	 * @throws MecanismoTitulacionNoEncontradoException -  Excepcion lanzada cuando no se encuentra un Mecanismo de Titulacion con el patrón solicitado
	 */
	public List<MecanismoTitulacion> buscar(String patron) throws MecanismoTitulacionNoEncontradoException ;

	List<MecanismoTitulacion> buscarMecanismoXCarrera(Integer carreraId)
			throws MecanismoTitulacionNoEncontradoException;
}
