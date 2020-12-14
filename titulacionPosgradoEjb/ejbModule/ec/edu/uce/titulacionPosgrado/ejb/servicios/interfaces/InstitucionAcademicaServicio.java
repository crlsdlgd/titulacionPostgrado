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
   
 ARCHIVO:     InstitucionAcademicaServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla InstitucionAcademica.  
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 06-Mayo-2016           Gabriel Mafla                      Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces;


import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.InstitucionAcademicaException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.InstitucionAcademicaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.InstitucionAcademica;

/**
 * Interface InstitucionAcademicaServicio
 * Interfase que define las operaciones sobre la tabla InstitucionAcademica.
 * @author gmafla
 * @version 1.0
 */
public interface InstitucionAcademicaServicio {
	/**
	 * Busca una entidad InstitucionAcademica por su id
	 * @param id - de la InstitucionAcademica a buscar
	 * @return InstitucionAcademica con el id solicitado
	 * @throws InstitucionAcademicaNoEncontradoException - Excepcion lanzada cuando no se encuentra una InstitucionAcademica con el id solicitado
	 * @throws InstitucionAcademicaException - Excepcion general
	 */
	public InstitucionAcademica buscarPorId(Integer id) throws InstitucionAcademicaNoEncontradoException, InstitucionAcademicaException;
	
	/**
	 * Lista todas las entidades InstitucionAcademica existentes en la BD
	 * @return lista de todas las entidades InstitucionAcademica existentes en la BD
	 */
	public List<InstitucionAcademica> listarTodos() throws InstitucionAcademicaNoEncontradoException;

}
