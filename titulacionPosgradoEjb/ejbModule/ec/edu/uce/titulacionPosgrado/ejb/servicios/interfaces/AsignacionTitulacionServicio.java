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
   
 ARCHIVO:     AsignacionTitulacionServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla AsignacionTitulacion.  
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 23-Septiembre-2016           Gabriel Mafla                      Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces;


import ec.edu.uce.titulacionPosgrado.ejb.excepciones.AsignacionTitulacionException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.AsignacionTitulacionNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.AsignacionTitulacion;

/**
 * Interface AsignacionTitulacionServicio
 * Interfase que define las operaciones sobre la tabla AsignacionTitulacion.
 * @author dalbuja
 * @version 1.0
 */
public interface AsignacionTitulacionServicio {
	/**
	 * Busca una entidad AsignacionTitulacion por su id
	 * @param id - de la AsignacionTitulacion a buscar
	 * @return AsignacionTitulacion con el id solicitado
	 * @throws AsignacionTitulacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una AsignacionTitulacion con el id solicitado
	 * @throws AsignacionTitulacionException - Excepcion general
	 */
	public AsignacionTitulacion buscarPorId(Integer id) throws AsignacionTitulacionNoEncontradoException, AsignacionTitulacionException;
	
	/**
	 * Busca una entidad AsignacionTitulacion por su id para su actualización
	 * @param id - de la AsignacionTitulacion a buscar
	 * @return AsignacionTitulacion con el id solicitado
	 * @throws AsignacionTitulacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una AsignacionTitulacion con el id solicitado
	 * @throws AsignacionTitulacionException - Excepcion general
	 */
	public AsignacionTitulacion actualizarPorId(Integer id,String nombreTutor,String temaTrabajo,Integer aprobacion) throws AsignacionTitulacionNoEncontradoException, AsignacionTitulacionException;

	AsignacionTitulacion buscarTemaTutorXTrttId(Integer trttId);
	
	
	
	
	
}
