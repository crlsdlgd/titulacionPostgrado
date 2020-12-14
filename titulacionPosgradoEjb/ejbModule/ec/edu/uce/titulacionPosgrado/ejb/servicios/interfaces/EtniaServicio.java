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
   
 ARCHIVO:     EtniaServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Etnia.  
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 21/02/2018				Daniel Albuja                      Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces;


import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EtniaException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EtniaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Etnia;

/**
 * Interface EtniaServicio
 * Interfase que define las operaciones sobre la tabla Etnia.
 * @author dalbuja
 * @version 1.0
 */
public interface EtniaServicio {
	/**
	 * Busca una entidad Etnia por su id
	 * @param id - de la Etnia a buscar
	 * @return Etnia con el id solicitado
	 * @throws EtniaNoEncontradoException - Excepcion lanzada cuando no se encuentra una Etnia con el id solicitado
	 * @throws EtniaException - Excepcion general
	 */
	public Etnia buscarPorId(Integer id) throws EtniaNoEncontradoException, EtniaException;
	
	/**
	 * Lista todas las entidades Etnia existentes en la BD
	 * @return lista de todas las entidades Etnia existentes en la BD
	 */
	public List<Etnia> listarTodos() throws EtniaNoEncontradoException;

}