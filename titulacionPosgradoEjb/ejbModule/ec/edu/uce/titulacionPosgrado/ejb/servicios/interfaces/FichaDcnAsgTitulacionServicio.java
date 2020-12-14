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
   
 ARCHIVO:     FichaDocenteServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Ficha_docente.  
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 23-09-2016           Daniel Albuja                      Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces;

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaDcnAsgTitulacionException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaDcnAsgTitulacionNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.FichaDcnAsgTitulacion;


/**
 * Interface FichaDocenteServicio
 * Interfase que define las operaciones sobre la tabla Ficha_docente.
 * @author dalbuja
 * @version 1.0
 */
public interface FichaDcnAsgTitulacionServicio {
	
	/**
	 * Busca una entidad FichaDcnAsgTitulacion por su id
	 * @param id - de la FichaDcnAsgTitulacion a buscar
	 * @return FichaDcnAsgTitulacion con el id solicitado
	 * @throws FichaDcnAsgTitulacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una FichaDocente con el id solicitado
	 * @throws FichaDcnAsgTitulacionException - Excepcion general
	 */
	
	public FichaDcnAsgTitulacion buscarPorId(Integer id) throws FichaDcnAsgTitulacionNoEncontradoException, FichaDcnAsgTitulacionException;
	
	public boolean buscarXFichaDocenteID(int fichaDocenteID)  throws FichaDcnAsgTitulacionNoEncontradoException, FichaDcnAsgTitulacionException;
	
}
