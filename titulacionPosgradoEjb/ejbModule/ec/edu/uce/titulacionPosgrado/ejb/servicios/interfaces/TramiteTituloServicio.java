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
   
 ARCHIVO:     TramiteTituloServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla TramiteTitulo.  
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 26/02/2018                      Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces;


import java.util.List;

//import ec.edu.uce.titulacion.ejb.dtos.EstudianteAptitudOtrosMecanismosJdbcDto;
//import ec.edu.uce.titulacion.ejb.dtos.EstudianteReversaFasesDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.CarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteAptitudOtrosMecanismosJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudiantePostuladoJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteValidacionJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.TramiteTituloException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.TramiteTituloNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.TramiteTituloValidacionException;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.TramiteTitulo;

/**
 * Interface TramiteTituloServicio
 * Interfase que define las operaciones sobre la tabla TramiteTitulo.
 * @author dalbuja
 * @version 1.0
 */
public interface TramiteTituloServicio {
	/**
	 * Busca una entidad TramiteTitulo por su id
	 * @param id - de la TramiteTitulo a buscar
	 * @return TramiteTitulo con el id solicitado
	 * @throws TramiteTituloNoEncontradoException - Excepcion lanzada cuando no se encuentra una TramiteTitulo con el id solicitado
	 * @throws TramiteTituloException - Excepcion general
	 */
	public TramiteTitulo buscarPorId(Integer id) throws TramiteTituloNoEncontradoException, TramiteTituloException;
	
	/**
	 * Lista todas las entidades TramiteTitulo existentes en la BD
	 * @return lista de todas las entidades TramiteTitulo existentes en la BD
	 */
	public List<TramiteTitulo> listarTodos() throws TramiteTituloNoEncontradoException;

	/**
	 * Busca una entidad TramiteTitulo por la identificacion del postulado
	 * @param String cedulaPostulado 
	 * @return TramiteTitulo con el id solicitado
	 * @throws TramiteTituloNoEncontradoException - Excepcion lanzada cuando no se encuentra una TramiteTitulo con el id solicitado
	 * @throws TramiteTituloException - Excepcion general
	 */
	public TramiteTitulo editarPorId(TramiteTitulo tramiteTitulo, CarreraDto carreraDto)
			throws TramiteTituloNoEncontradoException, TramiteTituloException;
	
	
	
	
	/**
	 * Inserta la nueva entidad indicada 
	 * @param  entidad - nueva entidad a insertar
	 * @return la nueva entidad insertada
	 */
	public TramiteTitulo anadir(TramiteTitulo entidad) throws TramiteTituloValidacionException;
	
//	public void ReversarValidacionXPostulacion(EstudianteReversaFasesDto estudiante, RolFlujoCarrera rolFCUsuario , Convocatoria convocatoria ) throws TramiteTituloException, TramiteTituloNoEncontradoException;
	
	
	/**
	 * Desactiva el tramite_titulo requerido por id
	 * @param String cedulaPostulado 
	 * @return TramiteTitulo con el id solicitado
	 * @throws TramiteTituloNoEncontradoException - Excepcion lanzada cuando no se encuentra una TramiteTitulo con el id solicitado
	 * @throws TramiteTituloException - Excepcion general
	 */
	public TramiteTitulo desactivarPorId(EstudiantePostuladoJdbcDto estudiante)
			throws TramiteTituloNoEncontradoException, TramiteTituloException;
	
	
	/**
	 * Modifica el estado del TramiteTitulo para permitir le edición del tribunal para los postulantes con OTROS MECANISMOS
	 * @List<EstudianteAptitudOtrosMecanismosJdbcDto> entidad
	 * @return Boolean
	 * @throws TramiteTituloNoEncontradoException - Excepción lanzada cuando no se encuentra una TramiteTitulo con el id solicitado
	 * @throws TramiteTituloException - Excepción general
	 */
//	public Boolean habilitarEdicionTribunal(List<EstudianteAptitudOtrosMecanismosJdbcDto> entidad)
//			throws TramiteTituloNoEncontradoException, TramiteTituloException;

	/**
	 * Modifica el estado del TramiteTitulo a Migrado a emisionTitulo
	 * @Integer trttId
	 * @return Boolean
	 * @throws TramiteTituloNoEncontradoException - Excepción lanzada cuando no se encuentra una TramiteTitulo con el id solicitado
	 * @throws TramiteTituloException - Excepción general
	 */
	public Boolean cambiarEstadoProcesoMigradoEmisionTitulo(Integer trttId)
			throws TramiteTituloException;

	/**
	 * Modifica el estado del TramiteTitulo a Edición de registro Migrado a emisionTitulo
	 * @String cedula
	 * @Integer carrera
	 * @return Boolean
	 * @throws TramiteTituloNoEncontradoException - Excepción lanzada cuando no se encuentra una TramiteTitulo con el id solicitado
	 * @throws TramiteTituloException - Excepción general
	 */
	public Boolean cambiarEstadoProcesoMigradoEmisionTituloRetornado(String cedula, Integer cncrId)
			throws TramiteTituloException;

	public TramiteTitulo editarEstadoProcesoPorId(EstudianteValidacionJdbcDto entidad)
			throws TramiteTituloNoEncontradoException, TramiteTituloException;

	Boolean cambiarEstadoAprobadoTutor(EstudianteAptitudOtrosMecanismosJdbcDto estudiante, Integer estadoAprobacion)
			throws TramiteTituloException;


	TramiteTitulo buscarTramiteTituloXPrsIdentificacionXCrrId(String cedula, Integer crrId)
			throws TramiteTituloException;
	
	
	/**
	 * Modifica el estado del TramiteTitulo a Aprobación tutor
	 * @String cedula
	 * @Integer carrera
	 * @return Boolean
	 * @throws TramiteTituloNoEncontradoException - Excepción lanzada cuando no se encuentra una TramiteTitulo con el id solicitado
	 * @throws TramiteTituloException - Excepción general
	 */
//	public Boolean cambiarEstadoAprobadoTutor(EstudianteAptitudOtrosMecanismosJdbcDto estudiante, Integer estadoAprobacion)
//			throws TramiteTituloException;
}
