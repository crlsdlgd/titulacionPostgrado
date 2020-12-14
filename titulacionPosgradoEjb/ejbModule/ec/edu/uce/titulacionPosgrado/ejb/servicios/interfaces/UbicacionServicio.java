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
   
 ARCHIVO:     UbicacionServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Ubicacion.  
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 06-Mayo-2016           Gabriel Mafla                      Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces;


import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UbicacionException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UbicacionNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Ubicacion;

/**
 * Interface UbicacionServicio
 * Interfase que define las operaciones sobre la tabla Ubicacion.
 * @author gmafla
 * @version 1.0
 */
public interface UbicacionServicio {
	/**
	 * Busca una entidad Ubicacion por su id
	 * @param id - de la Ubicacion a buscar
	 * @return Ubicacion con el id solicitado
	 * @throws UbicacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una Ubicacion con el id solicitado
	 * @throws UbicacionException - Excepcion general
	 */
	public Ubicacion buscarPorId(Integer id) throws UbicacionNoEncontradoException, UbicacionException;
	
	/**
	 * Lista todas las entidades Ubicacion existentes en la BD
	 * @return lista de todas las entidades Ubicacion existentes en la BD
	 */
	public List<Ubicacion> listarTodos() throws UbicacionNoEncontradoException;

}
