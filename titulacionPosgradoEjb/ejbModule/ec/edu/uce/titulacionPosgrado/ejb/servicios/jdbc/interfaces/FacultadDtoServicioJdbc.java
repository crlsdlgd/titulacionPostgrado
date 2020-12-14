/**************************************************************************
 *				(c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción o distribución no autorizada de este programa, 
 * o cualquier porción de él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán procesadas con el grado máximo contemplado 
 * por la ley.
  ************************************************************************* 
   
 ARCHIVO:     FacultadDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla facultad.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 26/02/2018				Daniel Albuja 							Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.FacultadDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FacultadDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FacultadDtoJdbcNoEncontradoException;


/**
 * Interface FacultadDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla facultad.
 * @author gmafla
 * @version 1.0
 */
public interface FacultadDtoServicioJdbc {

	/**
	 * Realiza la busqueda de una Facultad por id
	 * @param facultadId - id de la facultad
	 * @return FacultadDto con el id solicitado 
	 * @throws FacultadDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws FacultadDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public FacultadDto buscarXId(int facultadId) throws FacultadDtoJdbcException, FacultadDtoJdbcNoEncontradoException;
	
	
	/**
	 * Realiza la busqueda de todas las facultades de la aplicacion
	 * @return Lista todos las facultades  de la aplicacion
	 * @throws FacultadDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws FacultadDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<FacultadDto> listarTodos() throws FacultadDtoJdbcException, FacultadDtoJdbcNoEncontradoException;
	
}
