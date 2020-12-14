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
   
 ARCHIVO:     ConfiguracionCarreraDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla ConfiguracionCarrera.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 21-NOVIEMBRE-2016		Gabriel Mafla 				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.ConfiguracionCarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ConfiguracionCarreraDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ConfiguracionCarreraDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.TituloDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.TituloDtoJdbcNoEncontradoException;


/**
 * Interface TituloDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla ConfiguracionCarrera.
 * @author gmafla
 * @version 1.0
 */
public interface ConfiguracionCarreraDtoServicioJdbc {


	/**
	 * Realiza la busqueda de todas las configuraciones carrea con títulos por carrera por sexo en la aplicacion
	 * @return Lista todos las configuraciones carrea con títulos por carrera por sexo de la aplicacion
	 * @throws TituloDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws TituloDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<ConfiguracionCarreraDto> listarTitulosXCarreraXSexo(Integer carreraId, Integer sexoId) throws ConfiguracionCarreraDtoException, ConfiguracionCarreraDtoNoEncontradoException;
}
