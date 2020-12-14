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
   
 ARCHIVO:     AsentamientoNotaDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se trabaja con la tabla asentamiento_nota
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 23-09-2016		Daniel Albuja				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces;

import java.math.BigDecimal;
import java.util.List;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteActaGradoJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteAptitudOtrosMecanismosJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteEmisionActaJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.AsentamientoNotaException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.AsentamientoNotaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaDcnAsgTitulacionException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaDcnAsgTitulacionNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.AsentamientoNota;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.FichaDocente;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.RolFlujoCarrera;



	/**
	 * Interface AsentamientoNotaDtoServicioJdbc.
	 * Interface Interface donde se trabaja con la tabla asentamiento_nota
	 * @author dalbuja
	 * @version 1.0
	 */
	public interface AsentamientoNotaDtoServicioJdbc {
	/**
	 * Método que permite buscar Actas de Grado por Convocatoria y Mes.
	 * @param  mesId - Integer que identifica al mes que se necesita buscar.
	 * @return Lista de Actas de Grado.
	 * @throws AsentamientoNotaNoEncontradoException, excepcion lanzada cuando no encuentra ninguna acta.
	 * @throws AsentamientoNotaException, excepcion general en caso de producir algun error.
	 */
		public List<EstudianteEmisionActaJdbcDto> buscarActaBloqueComplexivo(Integer crrId,Integer mesId) throws AsentamientoNotaNoEncontradoException, AsentamientoNotaException;
	/**
	 * Inserta la nueva entidad indicada 
	 * @param  entidad - nueva entidad a insertar
	 * @return la nueva entidad insertada
	 * @throws  
	 */
		public boolean guardarAsentamientoNotaPendienteAptitud(EstudianteAptitudOtrosMecanismosJdbcDto entidad,  RolFlujoCarrera rolFlujoCarrera, List<FichaDocente> listaDocentes, Integer estado) throws  AsentamientoNotaNoEncontradoException , AsentamientoNotaException;
	

	/**
	 * Inserta la nueva entidad indicada 
	 * @param  entidad - nueva entidad a insertar
	 * @return la nueva entidad insertada
	 * @throws  
	 */
		public  boolean guardarAsentamientoNotaGrado(EstudianteAptitudOtrosMecanismosJdbcDto entidad,  RolFlujoCarrera rolFlujoCarrera, List<FichaDocente> listaDocentes, Integer estado, Integer totalTribunal) throws  AsentamientoNotaNoEncontradoException , AsentamientoNotaException;
	
	
	/**
	 * Inserta la nueva entidad indicada 
	 * @param  entidad - nueva entidad a insertar
	 * @return la nueva entidad insertada
	 * @throws  
	 */
		public boolean guardarAsentamientoNotaAptitud(EstudianteAptitudOtrosMecanismosJdbcDto entidad,  RolFlujoCarrera rolFlujoCarrera, List<FichaDocente> listaDocentes, Integer estado) throws  AsentamientoNotaNoEncontradoException , AsentamientoNotaException;
	
	/**
	 * Busca la entidad asentamiento_nota correspondiente a la cédula del postulante apto o no apto 
	 * otros mecanismos y carrera del evaluador 
	 * @param  cedulaPostulante - cédula a buscar
	 * @param  cedulaEvaluador - cédula del director de carrera
	 * @param  convocatoria - convocatoria a buscar
	 * @return la entidad que se busca
	 * @throws  
	 */
		public AsentamientoNota buscarAsentamientoNotaXPostulanteAptoNoAptoXEvaluadorOtrosMecanismos(String cedulaPostulante ,  String cedulaEvaluador, Integer convocatoria) throws  AsentamientoNotaNoEncontradoException , AsentamientoNotaException;
	
	/**
	 * Busca la entidad asentamiento_nota, asignacion_titulacion, ficha_dcn_asg_titulacion 
	 * @param  cedulaPostulante - cédula a buscar
	 * @param  convocatoria - convocatoria a buscar
	 * @return la entidad que se busca
	 * @throws  
	 */
		public List<EstudianteEmisionActaJdbcDto> buscarPostulantesImprimirActa(String cedulaPostulante ,  Integer convocatoria, Integer carrera, String cedulaValidador) throws  AsentamientoNotaNoEncontradoException , AsentamientoNotaException,FichaDcnAsgTitulacionNoEncontradoException,FichaDcnAsgTitulacionException ;
	
	/**
	 * Busca la entidad asentamiento_nota, asignacion_titulacion, ficha_dcn_asg_titulacion 
	 * @param  cedulaPostulante - cédula a buscar
	 * @param  cedulaEvaluador - cédula del director de carrera
	 * @param  convocatoria - convocatoria a buscar
	 * @return la entidad que se busca
	 * @throws  
	 */
		public List<EstudianteAptitudOtrosMecanismosJdbcDto> buscarPostulantesNotasTribunalDocentesXCedulaPostulante(String cedulaPostulante, Integer convocatoria, Integer carrera) throws AsentamientoNotaNoEncontradoException,	AsentamientoNotaException,FichaDcnAsgTitulacionNoEncontradoException,FichaDcnAsgTitulacionException ;
	
	/**
	 * Busca la entidad asentamiento_nota, asignacion_titulacion, ficha_dcn_asg_titulacion
	 * que hayan aprobado el proceso de titulación y emitido el acta de grado 
	 * @param  cedulaPostulante - cédula a buscar
	 * @param  convocatoria - convocatoria a buscar
	 * @return la entidad que se busca
	 * @throws  
	 */
		public EstudianteEmisionActaJdbcDto buscarGraduadosOtrosMecanismos(String cedulaPostulante, Integer convocatoria, Integer carrera) throws AsentamientoNotaNoEncontradoException, AsentamientoNotaException,FichaDcnAsgTitulacionNoEncontradoException,FichaDcnAsgTitulacionException;


		public List<EstudianteAptitudOtrosMecanismosJdbcDto> buscarPostulantesNotasTribunalXCedulaPostulante(String cedulaPostulante) throws AsentamientoNotaNoEncontradoException, AsentamientoNotaException,FichaDcnAsgTitulacionNoEncontradoException,	FichaDcnAsgTitulacionException;


		public List<EstudianteAptitudOtrosMecanismosJdbcDto> buscarPostulantesNotasTribunalXCedulaPostulanteEdicion(String cedulaPostulante) throws AsentamientoNotaNoEncontradoException, AsentamientoNotaException,FichaDcnAsgTitulacionNoEncontradoException, FichaDcnAsgTitulacionException;
	
	
	
		public List<EstudianteEmisionActaJdbcDto> buscarPostulantesReImprimirActa(
			String cedulaPostulante, Integer convocatoria, Integer carrera,
			String cedulaValidador)
			throws AsentamientoNotaNoEncontradoException,
			AsentamientoNotaException,
			FichaDcnAsgTitulacionNoEncontradoException,
			FichaDcnAsgTitulacionException;
		public boolean guardarAptitud2018(EstudianteAptitudOtrosMecanismosJdbcDto entidad,
			RolFlujoCarrera rolFlujoCarrera, Integer estado)
			throws AsentamientoNotaNoEncontradoException,
			AsentamientoNotaException;
		EstudianteActaGradoJdbcDto buscarAsentamientoNotaEstudianteAptoXIdentificacionXCarrera(String cedulaPostulante,
				Integer carrera) throws AsentamientoNotaNoEncontradoException, AsentamientoNotaException;
		BigDecimal buscarNotaTrabajoEscrito(Integer trttId);
		BigDecimal buscarNotaTrabajoOral(Integer trttId);
		AsentamientoNota buscarNotasOral(Integer trttId);
	
}
