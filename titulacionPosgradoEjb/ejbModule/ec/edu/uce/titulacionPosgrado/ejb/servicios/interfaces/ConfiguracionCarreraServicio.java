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
   
 ARCHIVO:     ConfiguracionCarreraServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla ConfiguracionCarrera.  
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 06-Mayo-2016           Gabriel Mafla                      Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces;


import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ConfiguracionCarreraException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ConfiguracionCarreraNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.ConfiguracionCarrera;

/**b
 * Interface ConfiguracionCarreraServicio
 * Interfase que define las operaciones sobre la tabla ConfiguracionCarrera.
 * @author gmafla
 * @version 1.0
 */
public interface ConfiguracionCarreraServicio {
	/**
	 * Busca una entidad ConfiguracionCarrera por su id
	 * @param id - de la ConfiguracionCarrera a buscar
	 * @return ConfiguracionCarrera con el id solicitado
	 * @throws ConfiguracionCarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una ConfiguracionCarrera con el id solicitado
	 * @throws ConfiguracionCarreraException - Excepcion general
	 */
	public ConfiguracionCarrera buscarPorId(Integer id) throws ConfiguracionCarreraNoEncontradoException, ConfiguracionCarreraException;
	
	/**
	 * Lista todas las entidades ConfiguracionCarrera existentes en la BD
	 * @return lista de todas las entidades ConfiguracionCarrera existentes en la BD
	 */
	public List<ConfiguracionCarrera> listarTodos() throws ConfiguracionCarreraNoEncontradoException;

}
