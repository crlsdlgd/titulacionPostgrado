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
   
 ARCHIVO:     TituloDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla Titulo.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 26/02/2018				Daniel Albuja 				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.CarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.TituloDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.TituloDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.TituloDtoJdbcNoEncontradoException;

/**
 * Interface TituloDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla Titulo.
 * @author dalbuja
 * @version 1.0
 */
public interface TituloDtoServicioJdbc {
	/**
	 * Realiza la busqueda de un título por id
	 * @param tituloId - id del título
	 * @return Título con el id solicitado 
	 * @throws TítuloDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws TítuloDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public TituloDto buscarXId(int tituloId) throws TituloDtoJdbcException, TituloDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza la busqueda de todos los títulos de la aplicacion
	 * @return Lista todos los títulos de la aplicacion
	 * @throws TituloDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws TituloDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<TituloDto> listarTodos() throws TituloDtoJdbcException, TituloDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza la busqueda de todos los títulos de la aplicacion, de acuerdo a la carrera
	 * @return Lista todos los títulos de la aplicacion, de acuerdo a la carrera
	 * @throws TituloDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws TituloDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<TituloDto> listarXCarrera(int carreraId, List<CarreraDto> listaCarreras) throws TituloDtoJdbcException, TituloDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza la busqueda de todos los títulos por carrera, modalidad y vigencia de la aplicacion
	 * @return Lista todos los títulos por carrera, modalidad y vigencia de la aplicacion
	 * @throws TituloDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws TituloDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<TituloDto> listarTitulosXCarreraXModaldiadXVigencia(int carreraId,int modalidadId, int vigenciaId) throws TituloDtoJdbcException, TituloDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza la busqueda de todos los títulos por carrera, modalidad, vigencia y sexo de la aplicacion
	 * @return Lista todos los títulos por carrera, modalidad, vigencia y sexo de la aplicacion
	 * @throws TituloDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws TituloDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<TituloDto> listarTitulosXCarreraXModaldiadXVigenciaXSexo(int carreraId,int modalidadId, int vigenciaId, int tipoSexo) throws TituloDtoJdbcException, TituloDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza la busqueda de todos los títulos de la aplicacion por el tipo
	 * @param tipoId - id del tipo de titulo que se está listando
	 * @return Lista todos los títulos de la aplicacion
	 * @throws TituloDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws TituloDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<TituloDto> listarXtipo(int tipoId) throws TituloDtoJdbcException, TituloDtoJdbcNoEncontradoException;
}
