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
   
 ARCHIVO:     FacultadServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Facultad.  
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 20/02/2018           Daniel Albuja                      Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces;


import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FacultadException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FacultadNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Facultad;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.RolFlujoCarrera;

/**
 * Interface FacultadServicio
 * Interface que define las operaciones sobre la tabla Facultad.
 * @author dalbuja
 * @version 1.0
 */
public interface FacultadServicio {
	/**
	 * Busca una entidad Facultad por su id
	 * @param id - de la Facultad a buscar
	 * @return Facultad con el id solicitado
	 * @throws FacultadNoEncontradoException - Excepcion lanzada cuando no se encuentra una Facultad con el id solicitado
	 * @throws FacultadException - Excepcion general
	 */
	public Facultad buscarPorId(Integer id) throws FacultadNoEncontradoException, FacultadException;
	
	/**
	 * Lista todas las entidades Facultad existentes en la BD
	 * @return lista de todas las entidades Facultad existentes en la BD
	 */
	public List<Facultad> listarTodos() throws FacultadNoEncontradoException;

	/**
	 * Lista todas las entidades Facultad existentes en la BD de acuerdo al RolFlujoCarrera
	 * @return lista de todas las entidades Facultad existentes en la BD
	 */
	public List<Facultad> listarTodosXRolFlujoCarrera(List<RolFlujoCarrera> rolFlujoCarrera) throws FacultadNoEncontradoException;

	public List<Facultad> listarTodosActivo() throws FacultadNoEncontradoException;
	
}
