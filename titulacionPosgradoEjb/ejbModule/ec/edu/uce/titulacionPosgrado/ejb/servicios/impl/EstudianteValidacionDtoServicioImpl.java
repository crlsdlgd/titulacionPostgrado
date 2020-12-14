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
   
 ARCHIVO:     PostulacionDtoServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre el dto Postulacion dto. 
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 26/02/2018				Daniel Albuja                    Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.titulacionPosgrado.ejb.servicios.impl;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

//import ec.edu.uce.titulacion.jpa.entidades.publico.Validacion;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteValidacionJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudiantePostuladoJdbcDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudiantePostuladoJdbcDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteValidacionDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteValidacionDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.EstudianteValidacionDtoServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.FichaEstudianteServicio;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.ProcesoTramiteConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.TramiteTituloConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.ValidacionConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.FichaEstudiante;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.ProcesoTramite;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.TramiteTitulo;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Validacion;


/**
 * Clase (Bean)PostulacionDtoServicioImpl.
 * Bean declarado como Stateless.
 * @author dalbuja
 * @version 1.0
 */
@Stateless
public class EstudianteValidacionDtoServicioImpl implements EstudianteValidacionDtoServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;
	
	@EJB
	FichaEstudianteServicio servFichaEstudianteServicio;
	/**
	 * Inserta la nueva entidad indicada 
	 * @param  entidad - nueva entidad a insertar
	 * @return la nueva entidad insertada
	 * @throws  
	 */
	@Override
	public boolean editar(EstudianteValidacionJdbcDto entidad, String identificacionSecretaria, Integer idCarrera, RolFlujoCarrera rolFlujoCarrera) throws  EstudianteValidacionDtoNoEncontradoException , EstudianteValidacionDtoException{		
		boolean retorno = false;
		if (entidad != null && identificacionSecretaria != null && idCarrera != null && rolFlujoCarrera != null) {
			
			try {
				//**********************************************************************
				//********** actualizar el ficha_estudiante actual  ****************
				//**********************************************************************
				
				FichaEstudiante fcesAux = em.find(FichaEstudiante.class, entidad.getFcesId());
//				FichaEstudiante fcesAux = new FichaEstudiante();
//				fcesAux = servFichaEstudianteServicio.buscarPorIdPersona(entidad.getPrsId());
				fcesAux.setFcesFechaInicioCohorte(entidad.getFcesFechaInicioCohorte());
				fcesAux.setFcesFechaFinCohorte(entidad.getFcesFechaFinCohorte());
				fcesAux.setFcesRegTTlSenescyt(entidad.getPrtrTtlRegSenescyt());
				fcesAux.setFcesTituloBachiller(entidad.getFcesTituloBachiller());
				em.merge(fcesAux);
				em.flush();
								
				//**********************************************************************
				//********** actualizar el tramite actual               ****************
				//**********************************************************************
				
				TramiteTitulo trttAux = em.find(TramiteTitulo.class, entidad.getTrttId());
				
				trttAux.setTrttEstadoProceso(TramiteTituloConstantes.EST_PROC_VALIDADO_VALUE);
				trttAux.setTrttCarreraId(idCarrera);
				trttAux.setTrttObservacion(entidad.getTrttObsValSec());
				em.merge(trttAux);
				em.flush();
				
				//**********************************************************************
				//******** Creo un nuevo proceso_tramite  para no perder el histotial ***********
				//**********************************************************************
				
//				TramiteTituloConstantes.EST_PROC_VALIDADO_VALUE
				ProcesoTramite nuevoPrtr = new ProcesoTramite();
				nuevoPrtr.setPrtrTipoProceso(ProcesoTramiteConstantes.TIPO_PROCESO_VALIDACION_TIPO_IDONEIDAD_VALUE);
				nuevoPrtr.setPrtrFechaEjecucion(new Timestamp((new Date()).getTime()));
				nuevoPrtr.setPrtrRolFlujoCarrera(rolFlujoCarrera);
				nuevoPrtr.setPrtrTramiteTitulo(trttAux);
				em.persist(nuevoPrtr);
				em.flush();
				
				//**********************************************************************
				//******** Creo un nuevo validacion  para no perder el historial ***********
				//**********************************************************************
				
				Validacion nuevoVld = new Validacion();
//				nuevoVld.setVldCulminoMalla(entidad.getVldCulminoMalla());
//				nuevoVld.setVldReproboComplexivo(entidad.getVldReproboComplexivo());
//				nuevoVld.setVldAsignadoTutor(entidad.getVldAsignadoTutor());
				nuevoVld.setVldSegundaProrroga(entidad.getVldSegundaProrroga());
				nuevoVld.setVldRslProrroga(entidad.getVldRslProrroga());
				nuevoVld.setVldRslHomologacion(entidad.getVldRslHomologacion());
				nuevoVld.setVldRslIdoneidad(entidad.getVldRslIdoneidad());
				nuevoVld.setVldRslActConocimiento(entidad.getVldRslActConocimiento());
//				nuevoVld.setVldRslGratuidad(entidad.getVldRslGratuidad());
//				nuevoVld.setVldRslExamenComplexivo(entidad.getVldRslExamenComplexivo());
//				nuevoVld.setVldRslOtrosMecanismos(entidad.getVldRslOtrosMecanismos());
//				nuevoVld.setVldRslSlcMecanismo(entidad.getVldRslSlcMecanismo());
				if(entidad.getVldRslAprobacionIngles()!=null){
					nuevoVld.setVldRslAprobacionIngles(entidad.getVldRslAprobacionIngles());
				}
				nuevoVld.setVldProcesoTramite(nuevoPrtr);
				em.persist(nuevoVld);
				em.flush();
			} catch (NoResultException e) {
				e.printStackTrace();
				throw new EstudianteValidacionDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteValidacionDtoServicioImpl.buscar.por.id.no.result.exception")));
			}catch (NonUniqueResultException e) {
				e.printStackTrace();
				throw new EstudianteValidacionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteValidacionDtoServicioImpl.buscar.por.id.non.unique.result.exception")));
			} catch (Exception e) {
				e.printStackTrace();
				throw new EstudianteValidacionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteValidacionDtoServicioImpl.buscar.por.id.exception")));
			}
			
			
		}
		return retorno;
	}
	
	
	/**
	 * Modifica el registro de actualización de conocimientos para proceder con el pago
	 * @param  entidad - nueva entidad a insertar
	 * @return la nueva entidad insertada
	 * @throws  
	 */
	@Override
	public boolean registrarActualizarConocimientoPago(Integer vldId, Integer trttId, String comprobante, Integer roflcrId) throws  EstudianteValidacionDtoNoEncontradoException , EstudianteValidacionDtoException{		
		boolean retorno = false;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		if (vldId != null ) {
			try {
				//**********************************************************************
				//********** actualizar el validación actual  **************************
				//**********************************************************************
				if(roflcrId!=GeneralesConstantes.APP_ID_BASE){
					StringBuilder sbsql = new StringBuilder();
					sbsql.append(" Select vld_id from Validacion vld ");
					sbsql.append(" where vld_comprobante_pago = ? ");
					con = ds.getConnection();
					pstmt = con.prepareStatement(sbsql.toString());
					pstmt.setString(1, comprobante); //cargo el comprobante
					rs = pstmt.executeQuery();
					if(rs.next()){
						throw new EstudianteValidacionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteValidacionDtoServicioImpl.registrar.actualizacion.comprobante.duplicado")));
					}
				}
				
				
//				Validacion vldAux = em.find(Validacion.class, vldId);
//				
//				if(vldAux.getVldRslActConocimiento()==ValidacionConstantes.SI_DEBE_REALIZAR_ACTUALIZACION_CONOCIMIENTOS_VALUE){
//					vldAux.setVldRslActConocimiento(ValidacionConstantes.ACTUALIZACION_CONOCIMIENTOS_PAGAR_VALUE);
//				}else if (vldAux.getVldRslActConocimiento()==ValidacionConstantes.ACTUALIZACION_CONOCIMIENTOS_PAGAR_VALUE){
//					vldAux.setVldRslActConocimiento(ValidacionConstantes.ACTUALIZACION_CONOCIMIENTOS_CANCELADA_VALUE);
//					vldAux.setVldComprobantePago(comprobante);
//				}
				
				//**********************************************************************
				//********** actualizar el tramite actual   ****************************
				//**********************************************************************
				
//				TramiteTitulo trttAux = em.find(TramiteTitulo.class, trttId);
				
				//**********************************************************************
				//******** Creo un nuevo proceso_tramite  para no perder el histotial ***********
				//**********************************************************************
				
//				TramiteTituloConstantes.EST_PROC_VALIDADO_VALUE
//				ProcesoTramite nuevoPrtr = new ProcesoTramite();
//				if(vldAux.getVldRslActConocimiento()==ValidacionConstantes.SI_DEBE_REALIZAR_ACTUALIZACION_CONOCIMIENTOS_VALUE){
//					nuevoPrtr.setPrtrTipoProceso(ProcesoTramiteConstantes.TIPO_PROCESO_ACTUALIZACION_CONOCIMIENTOS_ACEPTA_VALUE);
//				}else if (vldAux.getVldRslActConocimiento()==ValidacionConstantes.ACTUALIZACION_CONOCIMIENTOS_PAGAR_VALUE){
//					nuevoPrtr.setPrtrTipoProceso(ProcesoTramiteConstantes.TIPO_PROCESO_ACTUALIZACION_CONOCIMIENTOS_PAGADO_VALUE);
//				}else if (vldAux.getVldRslActConocimiento()==ValidacionConstantes.ACTUALIZACION_CONOCIMIENTOS_CANCELADA_VALUE){
//					nuevoPrtr.setPrtrTipoProceso(ProcesoTramiteConstantes.TIPO_PROCESO_ACTUALIZACION_CONOCIMIENTOS_PAGADO_VALUE);
////				}
//				nuevoPrtr.setPrtrFechaEjecucion(new Timestamp((new Date()).getTime()));
//				if(roflcrId!=GeneralesConstantes.APP_ID_BASE){
//					RolFlujoCarrera rolFlujoCarrera = em.find(RolFlujoCarrera.class, roflcrId);
//					nuevoPrtr.setPrtrRolFlujoCarrera(rolFlujoCarrera);	
//				}
//				
//				nuevoPrtr.setPrtrTramiteTitulo(trttAux);
//				em.persist(nuevoPrtr);
			} catch (NoResultException e) {
				throw new EstudianteValidacionDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteValidacionDtoServicioImpl.buscar.por.id.no.result.exception")));
			}catch (NonUniqueResultException e) {
				throw new EstudianteValidacionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteValidacionDtoServicioImpl.buscar.por.id.non.unique.result.exception")));
			} catch (Exception e) {
				throw new EstudianteValidacionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteValidacionDtoServicioImpl.buscar.por.id.exception")));
			}
		}else{
			
			try {
				StringBuilder sbsql = new StringBuilder();
				sbsql.append(" Select vld from Validacion vld ");
				sbsql.append(" where vld.vldProcesoTramite.prtrTramiteTitulo.trttId = :trttIdentificacion ");
				con = ds.getConnection();
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("trttIdentificacion", trttId);
//				Validacion vldAux = (Validacion) q.getSingleResult();
//				vldAux.setVldRslActConocimiento(ValidacionConstantes.ACTUALIZACION_CONOCIMIENTOS_CANCELADA_VALUE);
//				vldAux.setVldComprobantePago(comprobante);
				TramiteTitulo trttAux = em.find(TramiteTitulo.class, trttId);
				ProcesoTramite nuevoPrtr = new ProcesoTramite();
				nuevoPrtr.setPrtrFechaEjecucion(new Timestamp((new Date()).getTime()));
				nuevoPrtr.setPrtrTipoProceso(ProcesoTramiteConstantes.TIPO_PROCESO_ACTUALIZACION_CONOCIMIENTOS_PAGADO_VALUE);
				RolFlujoCarrera rolFlujoCarrera = em.find(RolFlujoCarrera.class, roflcrId);
				nuevoPrtr.setPrtrRolFlujoCarrera(rolFlujoCarrera);	
				nuevoPrtr.setPrtrTramiteTitulo(trttAux);
				em.persist(nuevoPrtr);
			} catch (SQLException e) {
			}

		}
		
		
		return retorno;
	}
	
	/**
	 * Modifica el registro de actualización de conocimientos para proceder con el pago
	 * @param  entidad - nueva entidad a insertar
	 * @return la nueva entidad insertada
	 * @throws  
	 */
	@Override
	public void modificarActualizacionConocimientos(Integer vldId, Integer trttId){	
//		try {
//			TramiteTitulo trttAux = em.find(TramiteTitulo.class, trttId);
//			Validacion vldAux = em.find(Validacion.class, vldId);
//			if(trttAux.getTrttObsValSecAbg().equals(ValidacionConstantes.SELECCIONAR_CUALQUIER_MECANISMO_IDONEO_LABEL)){
//				trttAux.setTrttObsValSecAbg(ValidacionConstantes.ACTUALIZACION_COMPLEXIVO_IDONEO_DOS_PERIODOS_LABEL);
//			}else{
//				trttAux.setTrttObsValSecAbg(ValidacionConstantes.ACTUALIZACION_OTROS_MECANISMOS_IDONEO_DOS_PERIODOS_LABEL);
//			}
//			vldAux.setVldRslActConocimiento(ValidacionConstantes.SI_DEBE_REALIZAR_ACTUALIZACION_CONOCIMIENTOS_VALUE);
//		} catch (Exception e) {
//		}
	}
	
	@Override
	public void retirarActualizacionConocimientos(Integer vldId, Integer trttId){	
//		try {
//			TramiteTitulo trttAux = em.find(TramiteTitulo.class, trttId);
//			Validacion vldAux = em.find(Validacion.class, vldId);
//			if(trttAux.getTrttObsValSecAbg().equals(ValidacionConstantes.ACTUALIZACION_COMPLEXIVO_IDONEO_DOS_PERIODOS_LABEL)){
//				trttAux.setTrttObsValSecAbg(ValidacionConstantes.SELECCIONAR_CUALQUIER_MECANISMO_IDONEO_LABEL);
//			}else{
//				trttAux.setTrttObsValSecAbg(ValidacionConstantes.OTROS_MECANISMOS_IDONEO_LABEL);
//			}
//			vldAux.setVldRslActConocimiento(ValidacionConstantes.NO_DEBE_REALIZAR_ACTUALIZACION_CONOCIMIENTOS_VALUE);
//		} catch (Exception e) {
//		}
	}
	
	/**
	 * Modifica el registro de homologación para egresamientos superiores a diez años
	 * @param  entidad - nueva entidad a insertar
	 * @return la nueva entidad insertada
	 * @throws  
	 */
	@Override
	public void modificarHomologación(Integer vldId, Integer trttId){	
//		try {
//			TramiteTitulo trttAux = em.find(TramiteTitulo.class, trttId);
//			Validacion vldAux = em.find(Validacion.class, vldId);
//			trttAux.setTrttObsValSecAbg(ValidacionConstantes.HOMOLOGACION_NO_IDONEO_DOS_PERIODOS__LABEL);
//			
//			vldAux.setVldRslHomologacion(ValidacionConstantes.HOMOLOGACION_NO_IDONEO_VALUE);
//		} catch (Exception e) {
//		}
	}
	
	
	@Override
	public boolean modificarActualizacionConocimientos(EstudianteValidacionJdbcDto entidad , RolFlujoCarrera rolFlujoCarrera, String observacion) throws  EstudianteValidacionDtoNoEncontradoException , EstudianteValidacionDtoException{		
		boolean retorno = false;
//		if (entidad != null && rolFlujoCarrera != null) {
//			
//			try {
//								
//				//**********************************************************************
//				//********** actualizar el tramite actual     **************************
//				//**********************************************************************
//				TramiteTitulo trttAux = em.find(TramiteTitulo.class, entidad.getTrttId());
//				ProcesoTramite nuevoPrtr = new ProcesoTramite();
//				Validacion vldAux = em.find(Validacion.class, entidad.getVldId());
//				switch (trttAux.getTrttObsValSecAbg()) {
//				case ValidacionConstantes.EXAMEN_COMPLEXIVO_IDONEO_LABEL:
//				case ValidacionConstantes.SELECCIONAR_CUALQUIER_MECANISMO_IDONEO_LABEL:
//					
//					trttAux.setTrttObsValSecAbg(ValidacionConstantes.ACTUALIZACION_COMPLEXIVO_IDONEO_LABEL);
//					
//					nuevoPrtr.setPrtrTipoProceso(ProcesoTramiteConstantes.TIPO_PROCESO_ACTIVAR_ACTUALIZACION_VALUE);
//					nuevoPrtr.setPrtrFechaEjecucion(new Timestamp((new Date()).getTime()));
//					nuevoPrtr.setPrtrRolFlujoCarrera(rolFlujoCarrera);
//					nuevoPrtr.setPrtrTramiteTitulo(trttAux);
//					nuevoPrtr.setPrtrObsActualizacion(observacion);
//					em.persist(nuevoPrtr);
//					vldAux.setVldRslActConocimiento(ValidacionConstantes.SI_DEBE_REALIZAR_ACTUALIZACION_CONOCIMIENTOS_VALUE);
//					retorno=true;
//					break;
//				case ValidacionConstantes.OTROS_MECANISMOS_IDONEO_LABEL:
//					trttAux.setTrttObsValSecAbg(ValidacionConstantes.ACTUALIZACION_OTROS_MECANISMOS_IDONEO_LABEL);
//					
//					nuevoPrtr.setPrtrTipoProceso(ProcesoTramiteConstantes.TIPO_PROCESO_ACTIVAR_ACTUALIZACION_VALUE);
//					nuevoPrtr.setPrtrFechaEjecucion(new Timestamp((new Date()).getTime()));
//					nuevoPrtr.setPrtrRolFlujoCarrera(rolFlujoCarrera);
//					nuevoPrtr.setPrtrTramiteTitulo(trttAux);
//					nuevoPrtr.setPrtrObsActualizacion(observacion);
//					em.persist(nuevoPrtr);
//					vldAux.setVldRslActConocimiento(ValidacionConstantes.SI_DEBE_REALIZAR_ACTUALIZACION_CONOCIMIENTOS_VALUE);
//					retorno=true;
//					break;
//				case ValidacionConstantes.ACTUALIZACION_OTROS_MECANISMOS_IDONEO_LABEL:
//				case ValidacionConstantes.ACTUALIZACION_OTROS_MECANISMOS_IDONEO_DOS_PERIODOS_LABEL:
//				case ValidacionConstantes.ACTUALIZACION_CONOCIMIENTOS_OTROS_MECANISMOS_ACTIVA_DIRECTOR_LABEL:
//					
//					trttAux.setTrttObsValSecAbg(ValidacionConstantes.OTROS_MECANISMOS_IDONEO_LABEL);
//					
//					nuevoPrtr.setPrtrTipoProceso(ProcesoTramiteConstantes.TIPO_PROCESO_DESACTIVAR_ACTUALIZACION_VALUE);
//					nuevoPrtr.setPrtrFechaEjecucion(new Timestamp((new Date()).getTime()));
//					nuevoPrtr.setPrtrRolFlujoCarrera(rolFlujoCarrera);
//					nuevoPrtr.setPrtrTramiteTitulo(trttAux);
//					nuevoPrtr.setPrtrObsActualizacion(observacion);
//					em.persist(nuevoPrtr);
//					vldAux.setVldRslActConocimiento(ValidacionConstantes.NO_DEBE_REALIZAR_ACTUALIZACION_CONOCIMIENTOS_VALUE);
//					retorno=false;
//					break;
//				case ValidacionConstantes.ACTUALIZACION_COMPLEXIVO_ACTIVA_DIRECTOR_LABEL:
//				case ValidacionConstantes.ACTUALIZACION_COMPLEXIVO_IDONEO_DOS_PERIODOS_LABEL:
//				case ValidacionConstantes.ACTUALIZACION_COMPLEXIVO_IDONEO_LABEL:
//					
//					trttAux.setTrttObsValSecAbg(ValidacionConstantes.SELECCIONAR_CUALQUIER_MECANISMO_IDONEO_LABEL);
//					
//					nuevoPrtr.setPrtrTipoProceso(ProcesoTramiteConstantes.TIPO_PROCESO_DESACTIVAR_ACTUALIZACION_VALUE);
//					nuevoPrtr.setPrtrFechaEjecucion(new Timestamp((new Date()).getTime()));
//					nuevoPrtr.setPrtrRolFlujoCarrera(rolFlujoCarrera);
//					nuevoPrtr.setPrtrTramiteTitulo(trttAux);
//					nuevoPrtr.setPrtrObsActualizacion(observacion);
//					em.persist(nuevoPrtr);
//					vldAux.setVldRslActConocimiento(ValidacionConstantes.NO_DEBE_REALIZAR_ACTUALIZACION_CONOCIMIENTOS_VALUE);
//					retorno=false;
//					break;	
//				default:
//					break;
//				}
//				
//			} catch (NoResultException e) {
//				throw new EstudianteValidacionDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteValidacionDtoServicioImpl.buscar.por.id.no.result.exception")));
//			}catch (NonUniqueResultException e) {
//				throw new EstudianteValidacionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteValidacionDtoServicioImpl.buscar.por.id.non.unique.result.exception")));
//			} catch (Exception e) {
//				throw new EstudianteValidacionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteValidacionDtoServicioImpl.buscar.por.id.exception")));
//			}
//			
//			
//		}else{
//			throw new EstudianteValidacionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteValidacionDtoServicioImpl.buscar.por.id.exception")));
//		}
		return retorno;
	}
	
	
	/**
	 * Método que guarda el registro de la Senescyt
	 * @param int fcesId es el id de la tabla FichaEstudiante
	 * @param String registro es la cadena que contiene el registro de la Senescyt
	 * @return Lista de carreras
	 * @throws SQLException 
	 */
	@Override
	public boolean guardarRegistroSenescyt(int fcesId, String registro)
			throws EstudiantePostuladoJdbcDtoException, EstudiantePostuladoJdbcDtoNoEncontradoException {
		boolean retorno = false;
		try {
			FichaEstudiante fces = em.find(FichaEstudiante.class, fcesId);
			fces.setFcesRegTTlSenescyt(registro);
			em.persist(fces);
			em.flush();
//			StringBuffer sbsql = new StringBuffer();
//			sbsql.append(" Update FichaEstudiante set fcesRegTTlSenescyt =:registro ");
//			sbsql.append(" where fcesId =:fcesId ");
//			Query q = em.createQuery(sbsql.toString());
//			q.setParameter("registro", registro);
//			q.setParameter("fcesId", fcesId);
//			q.executeUpdate();
			retorno=true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return retorno;
	}
	
	
	@Override
	public boolean registrarActualizarConocimientoPago(Integer vldId,
			Integer trttId, String comprobante, Integer roflcrId,
			String identificacion)
			throws EstudianteValidacionDtoNoEncontradoException,
			EstudianteValidacionDtoException {
		boolean retorno = false;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		if (vldId != null) {
			try {
				// **********************************************************************
				// ********** actualizar el validación actual
				// **************************
				// **********************************************************************
				if (roflcrId != GeneralesConstantes.APP_ID_BASE) {
					StringBuilder sbsql = new StringBuilder();
					sbsql.append(" Select vld_id from Validacion vld ");
					sbsql.append(" where vld_comprobante_pago = ? ");
					con = ds.getConnection();
					pstmt = con.prepareStatement(sbsql.toString());
					pstmt.setString(1, comprobante); // cargo el comprobante
					rs = pstmt.executeQuery();
					if (rs.next()) {
						StringBuilder sbSql = new StringBuilder();
						sbSql.append(" SELECT ");
						sbSql.append(" prs.");
						sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
						sbSql.append(" FROM ");
						sbSql.append(JdbcConstantes.TABLA_PERSONA);
						sbSql.append(" prs, ");
						sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);
						sbSql.append(" fces, ");
						sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);
						sbSql.append(" trtt, ");
						sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);
						sbSql.append(" prtr, ");
						sbSql.append(JdbcConstantes.TABLA_VALIDACION);
						sbSql.append(" vld ");
						sbSql.append(" WHERE ");
						sbSql.append(" prs.");
						sbSql.append(JdbcConstantes.PRS_ID);
						sbSql.append(" = fces.");
						sbSql.append(JdbcConstantes.FCES_PRS_ID);
						sbSql.append(" AND trtt.");
						sbSql.append(JdbcConstantes.TRTT_ID);
						sbSql.append(" = fces.");
						sbSql.append(JdbcConstantes.FCES_TRTT_ID);
						sbSql.append(" AND trtt.");
						sbSql.append(JdbcConstantes.TRTT_ID);
						sbSql.append(" = prtr.");
						sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
						sbSql.append(" AND prtr.");
						sbSql.append(JdbcConstantes.PRTR_ID);
						sbSql.append(" = vld.");
						sbSql.append(JdbcConstantes.VLD_PRTR_ID);

						sbSql.append(" AND VLD.");
						sbSql.append(JdbcConstantes.VLD_COMPROBANTE_PAGO);
						sbSql.append(" = ? ");
						con = ds.getConnection();
						pstmt = con.prepareStatement(sbSql.toString());
						pstmt.setString(1, comprobante);
						rs = pstmt.executeQuery();
						if (rs.next()) {
							try {
								String dato = rs
										.getString(JdbcConstantes.PRS_IDENTIFICACION);
								if (dato.equals(identificacion)) {

								} else {
									try {
										if (rs != null) {
											rs.close();
										}
										if (pstmt != null) {
											pstmt.close();
										}
										if (con != null) {
											con.close();
										}
									} catch (SQLException e) {
									}
									throw new EstudianteValidacionDtoException(
											MensajeGeneradorUtilidades
													.getMsj(new MensajeGeneradorUtilidades(
															"EstudianteValidacionDtoServicioImpl.registrar.actualizacion.comprobante.duplicado")));
								}
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
						try {
							if (rs != null) {
								rs.close();
							}
							if (pstmt != null) {
								pstmt.close();
							}
							if (con != null) {
								con.close();
							}
						} catch (SQLException e) {
						}
					}
					try {
						if (rs != null) {
							rs.close();
						}
						if (pstmt != null) {
							pstmt.close();
						}
						if (con != null) {
							con.close();
						}
					} catch (SQLException e) {
					}
				}

				Validacion vldAux = em.find(Validacion.class, vldId);

					vldAux.setVldRslActConocimiento(ValidacionConstantes.ACTUALIZACION_CONOCIMIENTOS_CANCELADA_VALUE);
					vldAux.setVldComprobantePago(comprobante);

				// **********************************************************************
				// ********** actualizar el tramite actual
				// ****************************
				// **********************************************************************

				TramiteTitulo trttAux = em.find(TramiteTitulo.class, trttId);

				// **********************************************************************
				// ******** Creo un nuevo proceso_tramite para no perder el
				// histotial ***********
				// **********************************************************************

				// TramiteTituloConstantes.EST_PROC_VALIDADO_VALUE
				ProcesoTramite nuevoPrtr = new ProcesoTramite();
				if (vldAux.getVldRslActConocimiento() == ValidacionConstantes.SI_DEBE_REALIZAR_ACTUALIZACION_CONOCIMIENTOS_VALUE) {
					nuevoPrtr
							.setPrtrTipoProceso(ProcesoTramiteConstantes.TIPO_PROCESO_ACTUALIZACION_CONOCIMIENTOS_ACEPTA_VALUE);
				} else if (vldAux.getVldRslActConocimiento() == ValidacionConstantes.ACTUALIZACION_CONOCIMIENTOS_PAGAR_VALUE) {
					nuevoPrtr
							.setPrtrTipoProceso(ProcesoTramiteConstantes.TIPO_PROCESO_ACTUALIZACION_CONOCIMIENTOS_PAGADO_VALUE);
				} else if (vldAux.getVldRslActConocimiento() == ValidacionConstantes.ACTUALIZACION_CONOCIMIENTOS_CANCELADA_VALUE) {
					nuevoPrtr
							.setPrtrTipoProceso(ProcesoTramiteConstantes.TIPO_PROCESO_ACTUALIZACION_CONOCIMIENTOS_PAGADO_VALUE);
				}
				nuevoPrtr.setPrtrFechaEjecucion(new Timestamp((new Date())
						.getTime()));
				if (roflcrId != GeneralesConstantes.APP_ID_BASE) {
					RolFlujoCarrera rolFlujoCarrera = em.find(
							RolFlujoCarrera.class, roflcrId);
					nuevoPrtr.setPrtrRolFlujoCarrera(rolFlujoCarrera);
				}

				nuevoPrtr.setPrtrTramiteTitulo(trttAux);
				em.persist(nuevoPrtr);
			} catch (NoResultException e) {
				throw new EstudianteValidacionDtoNoEncontradoException(
						MensajeGeneradorUtilidades
								.getMsj(new MensajeGeneradorUtilidades(
										"EstudianteValidacionDtoServicioImpl.buscar.por.id.no.result.exception")));
			} catch (NonUniqueResultException e) {
				throw new EstudianteValidacionDtoException(
						MensajeGeneradorUtilidades
								.getMsj(new MensajeGeneradorUtilidades(
										"EstudianteValidacionDtoServicioImpl.buscar.por.id.non.unique.result.exception")));
			} catch (Exception e) {
				throw new EstudianteValidacionDtoException(
						MensajeGeneradorUtilidades
								.getMsj(new MensajeGeneradorUtilidades(
										"EstudianteValidacionDtoServicioImpl.buscar.por.id.exception")));
			}
		} else {

			try {
				StringBuilder sbsql = new StringBuilder();
				sbsql.append(" Select vld from Validacion vld ");
				sbsql.append(" where vld.vldProcesoTramite.prtrTramiteTitulo.trttId = :trttIdentificacion ");
				con = ds.getConnection();
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("trttIdentificacion", trttId);
				Validacion vldAux = (Validacion) q.getSingleResult();
				vldAux.setVldRslActConocimiento(ValidacionConstantes.ACTUALIZACION_CONOCIMIENTOS_CANCELADA_VALUE);
				vldAux.setVldComprobantePago(comprobante);
				TramiteTitulo trttAux = em.find(TramiteTitulo.class, trttId);
				ProcesoTramite nuevoPrtr = new ProcesoTramite();
				nuevoPrtr.setPrtrFechaEjecucion(new Timestamp((new Date())
						.getTime()));
				nuevoPrtr
						.setPrtrTipoProceso(ProcesoTramiteConstantes.TIPO_PROCESO_ACTUALIZACION_CONOCIMIENTOS_PAGADO_VALUE);
				RolFlujoCarrera rolFlujoCarrera = em.find(
						RolFlujoCarrera.class, roflcrId);
				nuevoPrtr.setPrtrRolFlujoCarrera(rolFlujoCarrera);
				nuevoPrtr.setPrtrTramiteTitulo(trttAux);
				em.persist(nuevoPrtr);
			} catch (SQLException e) {
			}

		}

		return retorno;
	}
}
