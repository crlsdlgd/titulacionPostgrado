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
   
 ARCHIVO:     ProcesoTramiteServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla ProcesoTramite.  
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 26/02/2018				Daniel Albuja                      Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces;


import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ProcesoTramiteException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ProcesoTramiteNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.ProcesoTramite;

/**
 * Interface ProcesoTramiteServicio
 * Interfase que define las operaciones sobre la tabla ProcesoTramite.
 * @author dalbuja
 * @version 1.0
 */
public interface ProcesoTramiteServicio {
	/**
	 * Busca una entidad ProcesoTramite por su id
	 * @param id - de la ProcesoTramite a buscar
	 * @return ProcesoTramite con el id solicitado
	 * @throws ProcesoTramiteNoEncontradoException - Excepcion lanzada cuando no se encuentra una ProcesoTramite con el id solicitado
	 * @throws ProcesoTramiteException - Excepcion general
	 */
	public ProcesoTramite buscarPorId(Integer id) throws ProcesoTramiteNoEncontradoException, ProcesoTramiteException;
	
	/**
	 * Lista todas las entidades ProcesoTramite existentes en la BD
	 * @return lista de todas las entidades ProcesoTramite existentes en la BD
	 */
	public List<ProcesoTramite> listarTodos() throws ProcesoTramiteNoEncontradoException;
	
	/**
	 * Busca la entidades ProcesoTramite existente en la BD
	 * @return busca la entidad ProcesoTramite existente en la BD
	 */
	public ProcesoTramite buscarXTrttXTipoPorceso(Integer trttId , Integer tipoProceso) throws ProcesoTramiteNoEncontradoException, ProcesoTramiteException;

	

	
}
