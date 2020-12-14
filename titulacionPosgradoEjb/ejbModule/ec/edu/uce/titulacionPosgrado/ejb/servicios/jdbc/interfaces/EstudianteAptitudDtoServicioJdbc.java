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
   
 ARCHIVO:     EstudiantePostuladoDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se consulta todos los datos del estudiante postulado.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 15-JUNIO-2016		Daniel Albuja				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteAptitudJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteAptitudOtrosMecanismosJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteActaGradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteActaGradoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EvaluadosDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EvaluadosDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.RolFlujoCarrera;



/**
 * Interface EstudiantePostuladoDtoServicioJdbc.
 * Interface Interface donde se consulta todos los datos del estudiante postulado.
 * @author dalbuja
 * @version 1.0
 */
public interface EstudianteAptitudDtoServicioJdbc {
	

	
	/**
	 * Método que guarda la aptitud del postulante por parte del validador
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EvaluadosDtoJdbcException 
	 * @throws EvaluadosDtoJdbcNoEncontradoException 
	 */
	public Integer guardarAptitud(EstudianteAptitudJdbcDto item, String validadorIdentificacion,RolFlujoCarrera roflcrr) throws EvaluadosDtoJdbcException, EvaluadosDtoJdbcNoEncontradoException ;

	List<EstudianteAptitudOtrosMecanismosJdbcDto> buscarEstudianteXIndetificacionXCarreraXConvocatoria(
			String identificacion, int carreraId, int convocatoria, int estado)
			throws EstudianteActaGradoException,
			EstudianteActaGradoNoEncontradoException;

	

	
}
