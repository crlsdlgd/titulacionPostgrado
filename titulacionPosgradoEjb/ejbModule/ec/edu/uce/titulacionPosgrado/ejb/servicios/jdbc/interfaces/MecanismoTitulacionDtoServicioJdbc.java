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
   
 ARCHIVO:     MecanismoTitulacionDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla mecanismo_titulacion.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 26/02/2018					       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.MecanismoTitulacionDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.MecanismoTitulacionDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.MecanismoTitulacionDtoJdbcNoEncontradoException;

/**
 * Interface MecanismoTitulacionDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla mecanismo_titulacion.
 * @author dalbuja
 * @version 1.0
 */
public interface MecanismoTitulacionDtoServicioJdbc {
	/**
	 * Realiza la busqueda de un mecanismo de titulacion por id
	 * @param mecTitulId - id del mecanismo de titulacion
	 * @return Mecanismo de titulacion con el id solicitado 
	 * @throws MecanismoTitulacionDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MecanismoTitulacionDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public MecanismoTitulacionDto buscarXId(int mecTitulId) throws MecanismoTitulacionDtoJdbcException, MecanismoTitulacionDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza la busqueda de todos los mecanismos de titulacion de la aplicacion
	 * @return Lista todos los mecanismos de titulacion de la aplicacion
	 * @throws MecanismoTitulacionDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MecanismoTitulacionDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MecanismoTitulacionDto> listarTodos() throws MecanismoTitulacionDtoJdbcException, MecanismoTitulacionDtoJdbcNoEncontradoException;


}
