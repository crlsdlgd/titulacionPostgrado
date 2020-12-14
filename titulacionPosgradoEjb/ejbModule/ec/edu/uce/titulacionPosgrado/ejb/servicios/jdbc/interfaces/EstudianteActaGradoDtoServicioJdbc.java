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
   
 ARCHIVO:     EstudianteEvaluacionDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se consulta todos los datos del estudiante para el acta de grado.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 26-OCTUBRE-2016		Gabriel Mafla				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.CarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteActaGradoJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteEmisionActaJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteActaGradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteActaGradoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.ConfiguracionCarrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Etnia;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.InstitucionAcademica;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Titulo;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Ubicacion;




/**
 * Interface EstudianteActaGradoDtoServicioJdbc.
 * Interface Interface donde se consulta todos los datos del estudiante postulado.
 * @author gmafla
 * @version 1.0
 */
public interface EstudianteActaGradoDtoServicioJdbc {
	/**
	 * Método que busca un estudiante por identificacion y convocatoria de acuerdo a su carrera
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudianteActaGradoJdbcDtoException 
	 * @throws EstudianteActaGradoJdbcDtoNoEncontradoException 
	 */
	public List<EstudianteActaGradoJdbcDto> buscarEstudianteXIndetificacionXCarreraXConvocatoria(String identificacion, String idSecretaria, CarreraDto carreraDto , int convocatoria) throws EstudianteActaGradoException , EstudianteActaGradoNoEncontradoException ; 
	
	public boolean editarActaGrado(EstudianteActaGradoJdbcDto estudiante, Etnia etnia, Ubicacion ubicacionNac, Ubicacion ubicacionRes , InstitucionAcademica inacFces , ConfiguracionCarrera cncrFces , Titulo ttlFces , RolFlujoCarrera rolFlujoCarrera,int tipoActa) throws  EstudianteActaGradoNoEncontradoException , EstudianteActaGradoException;

	public boolean modificarEstadoActaGrado(EstudianteActaGradoJdbcDto estudiante, RolFlujoCarrera rolFlujoCarrera) throws  EstudianteActaGradoNoEncontradoException , EstudianteActaGradoException;
	
	/**
	 * Método que busca un estudiante por identificacion y convocatoria de acuerdo a su carrera
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudianteActaGradoException 
	 * @throws EstudianteActaGradoNoEncontradoException 
	 * @throws EstudianteActaGradoJdbcDtoException 
	 * @throws EstudianteActaGradoJdbcDtoNoEncontradoException 
	 */
	public List<EstudianteActaGradoJdbcDto> buscarEstudianteParaEdicionActaXIndetificacionXCarreraXConvocatoria(EstudianteActaGradoJdbcDto entidad, String idSecretaria) throws EstudianteActaGradoException, EstudianteActaGradoNoEncontradoException;



	public boolean modificarEstadoActaGradoFinal(
			EstudianteEmisionActaJdbcDto estudiante,
			RolFlujoCarrera rolFlujoCarrera )
			throws EstudianteActaGradoNoEncontradoException,
			EstudianteActaGradoException;


	public void actualizarAutoridadesComplexivo(Integer fcesId, String autoridad,
			Integer sexo, Integer tipo, Integer Porcentaje);
	
}
