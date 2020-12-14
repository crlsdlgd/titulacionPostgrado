/**************************************************************************
 *                (c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción distribución no autorizada de este programa, 
 * o cualquier porción de  él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán  procesadas con el grado  máximo contemplado 
 * por la ley.
 
 ************************************************************************* 
   
 ARCHIVO:     TramiteTituloServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla TramiteTitulo. 
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 26/02/2018				Daniel Albuja                      Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.titulacionPosgrado.ejb.servicios.impl;


import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

//import ec.edu.uce.titulacion.ejb.dtos.EstudianteAptitudOtrosMecanismosJdbcDto;
//import ec.edu.uce.titulacion.ejb.dtos.EstudianteReversaFasesDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.CarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteAptitudOtrosMecanismosJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudiantePostuladoJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteValidacionJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.TramiteTituloException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.TramiteTituloNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.TramiteTituloValidacionException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.TramiteTituloServicio;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.ProcesoTramiteConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.TramiteTituloConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.FichaEstudiante;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Persona;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.ProcesoTramite;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.TramiteTitulo;

/**
 * Clase (Bean)TramiteTituloServicioImpl.
 * Bean declarado como Stateless.
 * @author gmafla
 * @version 1.0
 */

@Stateless
public class TramiteTituloServicioImpl implements TramiteTituloServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad TramiteTitulo por su id
	 * @param id - de la TramiteTitulo a buscar
	 * @return TramiteTitulo con el id solicitado
	 * @throws TramiteTituloNoEncontradoException - Excepcion lanzada cuando no se encuentra una TramiteTitulo con el id solicitado
	 * @throws TramiteTituloException - Excepcion general
	 */
	@Override
	public TramiteTitulo buscarPorId(Integer id) throws TramiteTituloNoEncontradoException, TramiteTituloException {
		TramiteTitulo retorno = null;
		if (id != null) {
			try {
				retorno = em.find(TramiteTitulo.class, id);
			} catch (NoResultException e) {
				throw new TramiteTituloNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TramiteTitulo.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new TramiteTituloException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TramiteTitulo.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new TramiteTituloException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TramiteTitulo.buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	
	/**
	 * Lista todas las entidades TramiteTitulo existentes en la BD
	 * @return lista de todas las entidades TramiteTitulo existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TramiteTitulo> listarTodos() throws TramiteTituloNoEncontradoException{
		List<TramiteTitulo> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select trtt from TramiteTitulo trtt ");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		
		if(retorno.size()<=0){
			throw new TramiteTituloNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TramiteTitulo.buscar.todos.no.result.exception")));
		}
		
		return retorno;
		
	}
	
	

	
	
	/**
	 * Inserta la nueva entidad indicada 
	 * @param  entidad - nueva entidad a insertar
	 * @return la nueva entidad insertada
	 */
	@Override
	public TramiteTitulo anadir(TramiteTitulo entidad) throws TramiteTituloValidacionException{
		TramiteTitulo retorno = null;
		try {
			verificar(entidad, GeneralesConstantes.APP_NUEVO);
			em.persist(entidad);
			retorno = entidad;	
		} catch (TramiteTituloValidacionException e) {
			throw e;
		}
		return retorno;
	}
	
	/**
	 * Reliza verificaciones para determinar si existe la entidad la busca por el nombre
	 * @param entidad - entidad a validar antes de gestionar su edicion o insersion
	 * @param tipo - tipo de verificacion que necesita 0 nuevo 1 editar
	 * @throws TramiteTituloValidacionException  - Excepcion lanzada en el caso de que no finalizo todas las validaciones
	 */
	private void verificar(TramiteTitulo entidad, int tipo) throws TramiteTituloValidacionException {
		if(entidad==null){
			throw new TramiteTituloValidacionException("TramiteTitulor.validar.entidad.null");
		}
	}

	/**
	 * Busca una entidad TramiteTitulo por la identificacion del postulado
	 * @param String cedulaPostulado 
	 * @return TramiteTitulo con el id solicitado
	 * @throws TramiteTituloNoEncontradoException - Excepcion lanzada cuando no se encuentra una TramiteTitulo con el id solicitado
	 * @throws TramiteTituloException - Excepcion general
	 */
	public TramiteTitulo editarPorId(TramiteTitulo tramiteTitulo, CarreraDto carreraDto)
			throws TramiteTituloNoEncontradoException, TramiteTituloException {
		
		TramiteTitulo retorno = null;
		try {
			if (tramiteTitulo != null) {
				retorno = buscarPorId(tramiteTitulo.getTrttId());
						if (retorno != null) {
							verificar(tramiteTitulo, GeneralesConstantes.APP_EDITAR);
							retorno.setTrttCarreraId(carreraDto.getCrrId());
//							retorno.setTrttCarrera(carreraDto.getCrrCodSniese());
							
						}
					}
				} catch (Exception e) {
				}
		return retorno;
	}

	
//	public void ReversarValidacionXPostulacion(EstudianteReversaFasesDto estudiante, RolFlujoCarrera rolFCUsuario , Convocatoria convocatoria ) throws TramiteTituloException, TramiteTituloNoEncontradoException{
//		try {
//		//**********************************************************************
//		//********** actualizar el tramite actual  *****************************
//		//**********************************************************************
//		TramiteTitulo trttUpAux = em.find(TramiteTitulo.class, estudiante.getTrttId());
////		trttUpAux.setTrttObsValSecAbg(GeneralesUtilidades.eliminarEnterYEspaciosEnBlanco(tramiteDto.getTrttObsValSecAbg()));
//		trttUpAux.setTrttEstadoTramite(estudiante.getTrttEstadoTramite());
//		trttUpAux.setTrttObservacionDga(estudiante.getTrttObservacionDga().toUpperCase().replaceAll(" +", " ").trim());
////		trttUpAux.setTrttEstadoProceso(estudiante.getTrttEstadoProceso());
//
//		//**********************************************************************
//		//******** Creo un nuevo tramite para no perder el historial ***********
//		//**********************************************************************
//		
//		TramiteTitulo nuevoTrm = new TramiteTitulo();
//		
//	
//		nuevoTrm.setTrttEstadoTramite(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
//		nuevoTrm.setTrttEstadoProceso(estudiante.getTrttEstadoProceso());
//		nuevoTrm.setTrttTramiteTitulo(trttUpAux);
//		
//		nuevoTrm.setTrttCarrera(estudiante.getTrttCarrera());
//
//		nuevoTrm.setTrttCarreraId(estudiante.getTrttCarreraId());
//		
//		
//		nuevoTrm.setTrttMcttEstadistico(estudiante.getTrttMcttEstadistico());
//		nuevoTrm.setTrttConvocatoria(convocatoria);
//		
//		em.persist(nuevoTrm);
//
//		//**********************************************************************
//		//********** actualizar el número de trámite ***************************
//		//**********************************************************************
//		
//		nuevoTrm = em.find(TramiteTitulo.class, nuevoTrm.getTrttId());
//		nuevoTrm.setTrttNumTramite(GeneralesUtilidades.generarNumeroTramite(nuevoTrm.getTrttId(), estudiante.getPrtrFechaEjecucion()));
//		
//
//		//**********************************************************************
//		//********** crear un nuevo proceso tramite ****************************
//		//**********************************************************************
//		ProcesoTramite procesoTramite = new ProcesoTramite();
//		procesoTramite.setPrtrTipoProceso(ProcesoTramiteConstantes.TIPO_PROCESO_VALIDACION_ERRONEA_VALUE);
//		procesoTramite.setPrtrFechaEjecucion(estudiante.getPrtrFechaEjecucion());
//		procesoTramite.setPrtrRolFlujoCarrera(rolFCUsuario);
//		procesoTramite.setPrtrTramiteTitulo(nuevoTrm);
//		em.persist(procesoTramite);
//
//		//**********************************************************************
//		//****** actualizar las fichas estudiante con el nuevo tramite  ********
//		//**********************************************************************
//			 FichaEstudiante fcesUpAux = em.find(FichaEstudiante.class, estudiante.getFcesId());
//			 fcesUpAux.setFcesFechaEgresamiento(null);
//			 fcesUpAux.setFcesTramiteTitulo(nuevoTrm);
//
////		for (EstudianteJdbcDto item : listaEstudiantes) {
////		FichaEstudiante fcesUpAux = em.find(FichaEstudiante.class, item.getFcesId());
////		fcesUpAux.setFcesTramiteTitulo(nuevoTrm);
////		}
//// agregar mensajes y cambiar l
//		} catch (NoResultException e) {
//		throw new TramiteTituloNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TramiteTitulo.buscar.todos.no.result.exception")));
//		} catch (NonUniqueResultException e) {
//		throw new TramiteTituloException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TramiteTitulo.buscar.por.id.non.unique.result.exception")));
//		} catch (Exception e) {
//		throw new TramiteTituloException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TramiteTitulo.buscar.por.id.exception")));
//		}
//	}

	/**
	 * Desactiva el tramite_titulo requerido por id
	 * @param String cedulaPostulado 
	 * @return TramiteTitulo con el id solicitado
	 * @throws TramiteTituloNoEncontradoException - Excepcion lanzada cuando no se encuentra una TramiteTitulo con el id solicitado
	 * @throws TramiteTituloException - Excepcion general
	 */
	public TramiteTitulo desactivarPorId(EstudiantePostuladoJdbcDto estudiante)
			throws TramiteTituloNoEncontradoException, TramiteTituloException {
		TramiteTitulo retorno = null;
		try {
			if (estudiante != null) {
				retorno = buscarPorId(estudiante.getTrttId());
						if (retorno != null) {
//							retorno.setTrttObsDesactivarDga(estudiante.getTrttObsDesactivarDga());
							if(estudiante.getTrttEstadoTramite()==TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE){
								retorno.setTrttEstadoTramite(TramiteTituloConstantes.ESTADO_TRAMITE_INACTIVO_VALUE);	
							}else{
								retorno.setTrttEstadoTramite(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
							}
							
						}
					}
				} catch (Exception e) {
				}
		return retorno;
	}

	
//	/**
//	 * Modifica el estado del TramiteTitulo para permitir le edición del tribunal para los postulantes con OTROS MECANISMOS
//	 * @List<EstudianteAptitudOtrosMecanismosJdbcDto> entidad
//	 * @return Boolean
//	 * @throws TramiteTituloNoEncontradoException - Excepción lanzada cuando no se encuentra una TramiteTitulo con el id solicitado
//	 * @throws TramiteTituloException - Excepción general
//	 */
//	@Override
//	public Boolean habilitarEdicionTribunal(
//			List<EstudianteAptitudOtrosMecanismosJdbcDto> entidad)
//			throws TramiteTituloNoEncontradoException, TramiteTituloException {
//		Boolean retorno=false;
//		try {
//			TramiteTitulo trttAux = em.find(TramiteTitulo.class, entidad.get(0).getTrttId());
//			if(trttAux.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_VALUE){
//				trttAux.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_LISTO_EDICION_ACTA_VALUE);
//			}else if(trttAux.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_DEFENSA_VALUE){
//				trttAux.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_LISTO_EDICION_ACTA_DEFENSA_ORAL_VALUE);
//			}else if(trttAux.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_VALUE){
//				trttAux.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_LISTO_EDICION_ACTA_VALUE);
//			}else if(trttAux.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_DEFENSA_ORAL_VALUE){
//				trttAux.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_LISTO_EDICION_ACTA_DEFENSA_ORAL_VALUE);
//			}else if(trttAux.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_VALUE){
//				trttAux.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_LISTO_EDICION_ACTA_VALUE);
//			}else if(trttAux.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_VALUE){
//				trttAux.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_LISTO_EDICION_ACTA_DEFENSA_ORAL_VALUE);
//			}
////			trttAux.setTrttEstadoProceso(entidad.get(0).getTrttEstadoProceso()+15);
//			trttAux.setTrttObservacionDga(entidad.get(0).getTrttObservacionDga());
//		} catch (Exception e) {
//		}
//		return retorno;
//	}
	
	
	/**
	 * Modifica el estado del TramiteTitulo a Migrado a emisionTitulo
	 * @Integer trttId
	 * @return Boolean
	 * @throws TramiteTituloNoEncontradoException - Excepción lanzada cuando no se encuentra una TramiteTitulo con el id solicitado
	 * @throws TramiteTituloException - Excepción general
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) 
	public Boolean cambiarEstadoProcesoMigradoEmisionTitulo(
			Integer trttId)
			throws TramiteTituloException {
		Boolean retorno=false;
		try {
//			TramiteTitulo trttAux = em.find(TramiteTitulo.class, trttId);
			//**********************************************************************
			//********** crear un nuevo proceso tramite ****************************
			//**********************************************************************
			StringBuffer sbsql1 = new StringBuffer();
				sbsql1.append(" Update TramiteTitulo set trttEstadoProceso=111 where trttId = ");
				sbsql1.append(trttId);
			Query q1 = em.createQuery(sbsql1.toString());
			q1.executeUpdate();
			
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Update FichaEstudiante set fcesEstadoMigrado = 0 where fcesTramiteTitulo.trttId = ");
			sbsql.append(trttId);
			Query q = em.createQuery(sbsql.toString());
			q.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retorno;
	}
	
	/**
	 * Modifica el estado del TramiteTitulo a Edición de registro Migrado a emisionTitulo
	 * @String cedula
	 * @return Boolean
	 * @throws TramiteTituloNoEncontradoException - Excepción lanzada cuando no se encuentra una TramiteTitulo con el id solicitado
	 * @throws TramiteTituloException - Excepción general
	 */
	@Override
	public Boolean cambiarEstadoProcesoMigradoEmisionTituloRetornado(
			String cedula, Integer cncrId)
			throws TramiteTituloException {
		Boolean retorno=false;
		try {
			Persona prsAux = null;
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select prs from Persona prs where ");
			sbsql.append(" prs.prsIdentificacion =:identificacion ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("identificacion", cedula);
			prsAux = (Persona) q.getSingleResult();
			
			FichaEstudiante fcesAux = null;
			sbsql = new StringBuffer();
			sbsql.append(" Select fces from FichaEstudiante fces where ");
			sbsql.append(" fces.fcesPersona.prsId =:id ");
			sbsql.append(" and (fces.fcesTramiteTitulo.trttEstadoProceso =:estadoComplex ");
			sbsql.append(" or fces.fcesTramiteTitulo.trttEstadoProceso =:estadoDefensa ");
			sbsql.append(" ) ");
			sbsql.append(" and fces.fcesConfiguracionCarrera.cncrId =:cncrId ");
			q = null;
			q = em.createQuery(sbsql.toString());
			q.setParameter("id", prsAux.getPrsId());
			q.setParameter("estadoComplex", TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_VALUE);
			q.setParameter("estadoDefensa", TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_VALUE);
			q.setParameter("cncrId", cncrId);
			fcesAux = (FichaEstudiante) q.getSingleResult();
//			fcesAux.setFcesEstadoMigrado(FichaEstudianteConstantes.TIPO_FCES_ESTADO_EMISION_TITULO_RETORNADO_VALUE);
			
			TramiteTitulo trttAux = em.find(TramiteTitulo.class, fcesAux.getFcesTramiteTitulo().getTrttId());
			
			if(trttAux.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_VALUE){
				trttAux.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_EDITAR_PENDIENTE_VALUE);
			}else if(trttAux.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_VALUE){
				trttAux.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_EDITAR_PENDIENTE_VALUE);
			}
			//**********************************************************************
			//********** crear un nuevo proceso tramite ****************************
			//**********************************************************************
			ProcesoTramite procesoTramite = new ProcesoTramite();
			procesoTramite.setPrtrTipoProceso(ProcesoTramiteConstantes.TIPO_PROCESO_EMISION_TITULO_HABILITAR_EDICION_VALUE);
			procesoTramite.setPrtrFechaEjecucion(new Timestamp((new Date()).getTime()));
			procesoTramite.setPrtrTramiteTitulo(trttAux);
			em.persist(procesoTramite);
			retorno=true;
		} catch (Exception e) {
			throw new TramiteTituloException();
		}
		return retorno;
	}

//	@Override
//	public Boolean cambiarEstadoAprobadoTutor(EstudianteAptitudOtrosMecanismosJdbcDto estudiante, Integer estadoAprobacion)
//			throws TramiteTituloException {
//		TramiteTitulo trttAux = em.find(TramiteTitulo.class, estudiante.getTrttId());
//		if(estadoAprobacion==GeneralesConstantes.APP_SI_VALUE){
//			trttAux.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_APROBACION_TUTOR_TRIBUNAL_LECTOR_VALUE);	
//		}else{
//			trttAux.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_NO_APROBACION_TUTOR_TRIBUNAL_LECTOR_VALUE);
//		}
//		return null;
//	}
	
	
	@Override
	public TramiteTitulo editarEstadoProcesoPorId(EstudianteValidacionJdbcDto entidad)
			throws TramiteTituloNoEncontradoException, TramiteTituloException {
		TramiteTitulo retorno = null;
		try {
			if (entidad != null) {
				retorno = buscarPorId(entidad.getTrttId());
						if (retorno != null) {
							retorno.setTrttEstadoProceso(entidad.getTrttEstadoProceso());
							retorno.setTrttObservacion(entidad.getTrttObsValSec());
							
						}
					}
				} catch (Exception e) {
				}
		return retorno;
	}
	
	@Override
	public Boolean cambiarEstadoAprobadoTutor(EstudianteAptitudOtrosMecanismosJdbcDto estudiante, Integer estadoAprobacion)
			throws TramiteTituloException {
		TramiteTitulo trttAux = em.find(TramiteTitulo.class, estudiante.getTrttId());
		if(estadoAprobacion==GeneralesConstantes.APP_SI_VALUE){
			trttAux.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_APROBACION_TUTOR_TRIBUNAL_LECTOR_VALUE);	
		}else{
			trttAux.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_NO_APROBACION_TUTOR_TRIBUNAL_LECTOR_VALUE);
		}
		em.merge(trttAux);
		return null;
	}
	
	
	@Override
	public TramiteTitulo buscarTramiteTituloXPrsIdentificacionXCrrId(
			String cedula, Integer crrId)
			throws TramiteTituloException {
		TramiteTitulo retorno=null;
		try {
			retorno = new TramiteTitulo();
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select trtt from TramiteTitulo trtt, FichaEstudiante fces, Persona prs where ");
			sbsql.append(" trtt.trttCarreraId =:crrId ");
			sbsql.append(" and prs.prsIdentificacion =:cedula ");
			sbsql.append(" and prs.prsId = fces.fcesPersona.prsId ");
			sbsql.append(" and trtt.trttId = fces.fcesTramiteTitulo.trttId ");
			sbsql.append(" and trtt.trttEstadoTramite = 0 ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("crrId", crrId);
			q.setParameter("cedula", cedula);
			retorno = (TramiteTitulo) q.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			throw new TramiteTituloException();
		}
		return retorno;
	}
}
