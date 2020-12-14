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
   
 ARCHIVO:     EstudianteDetalleComplexivoDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc 
 de los métodos requeridos para el examen complexivo
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 01-SEPTIEMBRE-2016		Daniel Albuja				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteActaGradoJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteAptitudOtrosMecanismosJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteEmisionActaJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.FichaDocenteAsignacionTitulacionDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.AsentamientoNotaException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.AsentamientoNotaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaDcnAsgTitulacionException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaDcnAsgTitulacionNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.ProcesoTramiteServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.TramiteTituloServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.AsentamientoNotaDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.AsentamientoNotaConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.FichaDocenteAsignaciontitulacionConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.MecanismoTitulacionCarreraConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.ProcesoTramiteConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.TramiteTituloConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Aptitud;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.AsentamientoNota;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.AsignacionTitulacion;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.FichaDcnAsgTitulacion;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.FichaDocente;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.FichaEstudiante;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.ProcesoTramite;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.TramiteTitulo;

/**
 * EJB EstudianteDetalleComplexivoDtoServicioJdbcImpl.
 * Clase Clase donde se implementan los metodos para el servicio jdbc 
 * de los métodos requeridos para el asentamiento de notas de otros mecanismos
 * @author dalbuja
 * @version 1.0
 */
@Stateless
public class AsentamientoNotaDtoServicioJdbcImpl implements AsentamientoNotaDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	
	@EJB
	private TramiteTituloServicio srvTramiteTitulo;
	@EJB
	private RolFlujoCarreraServicio srvRolFlujoCarreraServicio;
	@EJB
	private ProcesoTramiteServicio srvProcesoTramiteServicio;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	
	
	
	/**
	 * Inserta la nueva entidad indicada 
	 * @param  entidad - nueva entidad a insertar
	 * @return la nueva entidad insertada
	 * @throws  
	 */
	@Override
	public boolean guardarAsentamientoNotaPendienteAptitud(EstudianteAptitudOtrosMecanismosJdbcDto entidad,  RolFlujoCarrera rolFlujoCarrera, List<FichaDocente> listaDocentes,Integer estado) throws  AsentamientoNotaNoEncontradoException , AsentamientoNotaException{
		boolean retorno = false;
		if (entidad != null  && rolFlujoCarrera != null) {
			try {	
				//**********************************************************************
				//********** nuevo ficha_dcn_asg_titulacion ******************************
				//**********************************************************************
				int contadorLectores=0;
				for (FichaDocente fichaDocente : listaDocentes) {
					FichaDcnAsgTitulacion nuevoFichaDcnAsgTitulacion = new FichaDcnAsgTitulacion();
					AsignacionTitulacion asttAux = em.find(AsignacionTitulacion.class, entidad.getAsttId());
					nuevoFichaDcnAsgTitulacion.setFcdcasttTipotribunal(FichaDocenteAsignaciontitulacionConstantes.LECTOR_VALUE);
					nuevoFichaDcnAsgTitulacion.setFcdcasttAsignacionTitulacion(asttAux);
					nuevoFichaDcnAsgTitulacion.setFcdcasttFichaDocente(fichaDocente);
					if(contadorLectores==0){
						nuevoFichaDcnAsgTitulacion.setFcdcasttNota(new BigDecimal(entidad.getAsnoTrbLector1()));
						nuevoFichaDcnAsgTitulacion.setFcdcasttFechaAsentamiento(entidad.getPrtrFechaEjecucion());
					}else if(contadorLectores==1){
						nuevoFichaDcnAsgTitulacion.setFcdcasttNota(new BigDecimal(entidad.getAsnoTrbLector2()));
						nuevoFichaDcnAsgTitulacion.setFcdcasttFechaAsentamiento(entidad.getPrtrFechaEjecucion());
					}else if(contadorLectores==1){
						nuevoFichaDcnAsgTitulacion.setFcdcasttNota(new BigDecimal(entidad.getAsnoTrbLector3()));
						nuevoFichaDcnAsgTitulacion.setFcdcasttFechaAsentamiento(entidad.getPrtrFechaEjecucion());
					}
					em.persist(nuevoFichaDcnAsgTitulacion);
					contadorLectores++;
				}
				
				//**********************************************************************
				//********** actualizar el tramite actual ******************************
				//**********************************************************************
				
				TramiteTitulo trttAux = em.find(TramiteTitulo.class, entidad.getTrttId());
				
				trttAux.setTrttEstadoProceso(estado);
				em.merge(trttAux);
				//*******************************************************************************
				//******** Creo un nuevo proceso_tramite  para no perder el histotial ***********
				//*******************************************************************************
				
				ProcesoTramite nuevoPrtr = new ProcesoTramite();
				nuevoPrtr.setPrtrTipoProceso(estado);
				nuevoPrtr.setPrtrFechaEjecucion(entidad.getPrtrFechaEjecucion());
				nuevoPrtr.setPrtrRolFlujoCarrera(rolFlujoCarrera);
				nuevoPrtr.setPrtrTramiteTitulo(trttAux);
				em.persist(nuevoPrtr);
			
				//**********************************************************************
				//******** Creo un nuevo asentamiento_nota *****************************
				//**********************************************************************
				AsentamientoNota nuevoAsno = new AsentamientoNota();
//				
				nuevoAsno.setAsnoTrbLector1(new BigDecimal(entidad.getAsnoTrbLector1()));
				nuevoAsno.setAsnoTrbLector2(new BigDecimal(entidad.getAsnoTrbLector2()));
				if(contadorLectores==3){
					nuevoAsno.setAsnoTrbLector3(new BigDecimal(entidad.getAsnoTrbLector3()));
				}
				nuevoAsno.setAsnoPrmTrbEscrito(new BigDecimal(entidad.getAsnoPrmTrbEscrito()));
				nuevoAsno.setAsnoFechaTrbEscrito(entidad.getPrtrFechaEjecucion());
				nuevoAsno.setAsnoProcesoTramite(nuevoPrtr);
				em.persist(nuevoAsno);
			}catch (NoResultException e) {
				throw new AsentamientoNotaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.otros.mecanismos.guardar.pendiente.buscar.por.id.no.result.exception")));
			}catch (NonUniqueResultException e) {
				throw new AsentamientoNotaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.otros.mecanismos.guardar.pendiente.buscar.por.id.non.unique.result.exception")));
			} catch (Exception e) {
				throw new AsentamientoNotaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.otros.mecanismos.guardar.pendiente.buscar.por.id.exception")));
			}
		}else{
			throw new AsentamientoNotaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.otros.mecanismos.guardar.pendiente.buscar.por.id.exception")));
		}
		return retorno;
		
	}
	
	
	/**
	 * Inserta la nueva entidad indicada 
	 * @param  entidad - nueva entidad a insertar
	 * @return la nueva entidad insertada
	 * @throws  
	 */
	@Override
	public boolean guardarAsentamientoNotaGrado(EstudianteAptitudOtrosMecanismosJdbcDto entidad,  RolFlujoCarrera rolFlujoCarrera, List<FichaDocente> listaDocentes,Integer estado, Integer totalTribunal) throws  AsentamientoNotaNoEncontradoException , AsentamientoNotaException{
		boolean retorno = false;
		if (entidad != null  && rolFlujoCarrera != null) {
			try {	
				//**********************************************************************
				//********** nuevo ficha_dcn_asg_titulacion ******************************
				//**********************************************************************
				
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select astt from AsignacionTitulacion astt ");
				sbsql.append(" where astt.asttProcesoTramite.prtrTramiteTitulo.trttId = :tramite");
				sbsql.append(" and astt.asttAprobacionTutor = ");sbsql.append(AsentamientoNotaConstantes.SI_APROBO_TEMA_TUTOR_VALUE);
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("tramite", entidad.getTrttId() );
				AsignacionTitulacion asttAux= (AsignacionTitulacion)q.getSingleResult();
				for (FichaDocente fichaDocente : listaDocentes) {
					FichaDcnAsgTitulacion nuevoFichaDcnAsgTitulacion = new FichaDcnAsgTitulacion();
					nuevoFichaDcnAsgTitulacion.setFcdcasttTipotribunal(FichaDocenteAsignaciontitulacionConstantes.ORAL_VALUE);
					nuevoFichaDcnAsgTitulacion.setFcdcasttAsignacionTitulacion(asttAux);
					nuevoFichaDcnAsgTitulacion.setFcdcasttFichaDocente(fichaDocente);
					em.persist(nuevoFichaDcnAsgTitulacion);
				}
				//**********************************************************************
				//********** actualizar el tramite actual ******************************
				//**********************************************************************
				
				TramiteTitulo trttAux = em.find(TramiteTitulo.class, entidad.getTrttId());
				
				trttAux.setTrttEstadoProceso(estado);
				
				//*******************************************************************************
				//******** Creo un nuevo proceso_tramite  para no perder el histotial ***********
				//*******************************************************************************
				
				ProcesoTramite nuevoPrtr = new ProcesoTramite();
				nuevoPrtr.setPrtrTipoProceso(ProcesoTramiteConstantes.TIPO_PROCESO_DEFENSA_ORAL_CARGADO_VALUE);
				nuevoPrtr.setPrtrFechaEjecucion(entidad.getPrtrFechaEjecucion());
				nuevoPrtr.setPrtrRolFlujoCarrera(rolFlujoCarrera);
				nuevoPrtr.setPrtrTramiteTitulo(trttAux);
				em.persist(nuevoPrtr);
			
				//**********************************************************************
				//******** Creo un nuevo asentamiento_nota *****************************
				//**********************************************************************
				AsentamientoNota nuevoAsno = null;
				if(entidad.getAsnoId()==GeneralesConstantes.APP_ID_BASE){
					nuevoAsno = new AsentamientoNota();
					nuevoAsno.setAsnoProcesoTramite(nuevoPrtr);
				}else{
					nuevoAsno = em.find(AsentamientoNota.class, entidad.getAsnoId());	
				}
				
				try {
					nuevoAsno.setAsnoDfnOral1(entidad.getAsnoDfnOral1());	
				} catch (Exception e) {
					// TODO: handle exception
				}
				try {
					nuevoAsno.setAsnoDfnOral2(entidad.getAsnoDfnOral2());	
				} catch (Exception e) {
					// TODO: handle exception
				}
				try {
					nuevoAsno.setAsnoDfnOral3(entidad.getAsnoDfnOral3());
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				nuevoAsno.setAsnoPrmDfnOral(entidad.getAsnoPrmDfnOral());
				nuevoAsno.setAsnoFechaDfnOral(entidad.getPrtrFechaEjecucion());
						// si no es economía
						BigDecimal suma1 = (entidad.getAsnoPrmDfnOral()).setScale(2,RoundingMode.DOWN);
//						if(entidad.getCrrTipoEvaluacion()==CarreraConstantes.CARRERA_EVALUACION_DEFENSA_ORAL_VALUE){
//							nuevoAsno.setAsnoTrbTitulacionFinal(suma1);
//							try {
//								FichaEstudiante fcesAux = em.find(FichaEstudiante.class, entidad.getFcesId());
//								fcesAux.setFcesNotaTrabTitulacion(suma1);
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//						}else{
							BigDecimal suma=suma1.add(nuevoAsno.getAsnoPrmTrbEscrito());
							suma=suma.divide(new BigDecimal(2),2,RoundingMode.DOWN);
							nuevoAsno.setAsnoTrbTitulacionFinal(suma);
							try {
								FichaEstudiante fcesAux = em.find(FichaEstudiante.class, entidad.getFcesId());
								fcesAux.setFcesNotaTrabTitulacion(suma);
							} catch (Exception e) {
								e.printStackTrace();
							}
//						}
			}catch (NoResultException e) {
				e.printStackTrace();
				throw new AsentamientoNotaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.otros.mecanismos.guardar.pendiente.buscar.por.id.no.result.exception")));
			}catch (NonUniqueResultException e) {
				e.printStackTrace();
				throw new AsentamientoNotaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.otros.mecanismos.guardar.pendiente.buscar.por.id.non.unique.result.exception")));
			} catch (Exception e) {
				e.printStackTrace();
				throw new AsentamientoNotaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.otros.mecanismos.guardar.pendiente.buscar.por.id.exception")));
			}
		}else{
			
			throw new AsentamientoNotaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.otros.mecanismos.guardar.pendiente.buscar.por.id.exception")));
		}
		return retorno;
		
	}
	
	/**
	 * Inserta la nueva entidad indicada 
	 * @param  entidad - nueva entidad a insertar
	 * @return la nueva entidad insertada
	 * @throws  
	 */
	@Override
	public boolean guardarAsentamientoNotaAptitud(EstudianteAptitudOtrosMecanismosJdbcDto entidad,  RolFlujoCarrera rolFlujoCarrera, List<FichaDocente> listaDocentes,Integer estado) throws  AsentamientoNotaNoEncontradoException , AsentamientoNotaException{
		boolean retorno = false;
		if (entidad != null  && rolFlujoCarrera != null) {
			try {	
				//**********************************************************************
				//********** nuevo ficha_dcn_asg_titulacion ******************************
				//**********************************************************************
				int contadorLectores=0;
				for (FichaDocente fichaDocente : listaDocentes) {
					FichaDcnAsgTitulacion nuevoFichaDcnAsgTitulacion = new FichaDcnAsgTitulacion();
					AsignacionTitulacion asttAux = em.find(AsignacionTitulacion.class, entidad.getAsttId());
					nuevoFichaDcnAsgTitulacion.setFcdcasttTipotribunal(FichaDocenteAsignaciontitulacionConstantes.LECTOR_VALUE);
					nuevoFichaDcnAsgTitulacion.setFcdcasttAsignacionTitulacion(asttAux);
					nuevoFichaDcnAsgTitulacion.setFcdcasttFichaDocente(fichaDocente);
					if(contadorLectores==0){
						nuevoFichaDcnAsgTitulacion.setFcdcasttNota(new BigDecimal(entidad.getAsnoTrbLector1()));
						nuevoFichaDcnAsgTitulacion.setFcdcasttFechaAsentamiento(entidad.getPrtrFechaEjecucion());
					}else if(contadorLectores==1){
						nuevoFichaDcnAsgTitulacion.setFcdcasttNota(new BigDecimal(entidad.getAsnoTrbLector2()));
						nuevoFichaDcnAsgTitulacion.setFcdcasttFechaAsentamiento(entidad.getPrtrFechaEjecucion());
					}else if(contadorLectores==2){
						nuevoFichaDcnAsgTitulacion.setFcdcasttNota(new BigDecimal(entidad.getAsnoTrbLector3()));
						nuevoFichaDcnAsgTitulacion.setFcdcasttFechaAsentamiento(entidad.getPrtrFechaEjecucion());
					}
					em.persist(nuevoFichaDcnAsgTitulacion);
					em.flush();
					contadorLectores++;
				}
				
				//**********************************************************************
				//********** actualizar el tramite actual ******************************
				//**********************************************************************
				
				TramiteTitulo trttAux = em.find(TramiteTitulo.class, entidad.getTrttId());
				
				trttAux.setTrttEstadoProceso(estado);
				em.merge(trttAux);
				em.flush();
				//*******************************************************************************
				//******** Creo un nuevo proceso_tramite  para no perder el histotial ***********
				//*******************************************************************************
				
				ProcesoTramite nuevoPrtr = new ProcesoTramite();
				nuevoPrtr.setPrtrTipoProceso(estado);
				nuevoPrtr.setPrtrFechaEjecucion(entidad.getPrtrFechaEjecucion());
				nuevoPrtr.setPrtrRolFlujoCarrera(rolFlujoCarrera);
				nuevoPrtr.setPrtrTramiteTitulo(trttAux);
				em.persist(nuevoPrtr);
				em.flush();
			
				//**********************************************************************
				//******** Creo un nuevo asentamiento_nota *****************************
				//**********************************************************************
				AsentamientoNota nuevoAsno = new AsentamientoNota();
//				
				nuevoAsno.setAsnoTrbLector1(new BigDecimal(entidad.getAsnoTrbLector1()));
				nuevoAsno.setAsnoTrbLector2(new BigDecimal(entidad.getAsnoTrbLector2()));
				if(contadorLectores==3){
					nuevoAsno.setAsnoTrbLector3(new BigDecimal(entidad.getAsnoTrbLector3()));
				}
				nuevoAsno.setAsnoPrmTrbEscrito(new BigDecimal(entidad.getAsnoPrmTrbEscrito()));
				nuevoAsno.setAsnoFechaTrbEscrito(entidad.getPrtrFechaEjecucion());
				nuevoAsno.setAsnoProcesoTramite(nuevoPrtr);
				em.persist(nuevoAsno);
				em.flush();
				
				Aptitud nuevoAptitud = new Aptitud();
				nuevoAptitud.setAptAproboActualizar(entidad.getAptAproboActualizar());
				nuevoAptitud.setAptRequisitos(entidad.getAptRequisitos());
				nuevoAptitud.setAptAproboTutor(entidad.getAptAproboTutor());
				nuevoAptitud.setAptFinMalla(entidad.getAptFinMalla());
//				nuevoAptitud.setAptNotaDirector(entidad.getAptNotaDirector());
				nuevoAptitud.setAptSuficienciaIngles(entidad.getAptSuficienciaIngles());
				nuevoAptitud.setAptProcesoTramite(nuevoPrtr);
				em.persist(nuevoAptitud);
				em.flush();
					
			}catch (NoResultException e) {
				throw new AsentamientoNotaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.otros.mecanismos.guardar.pendiente.buscar.por.id.no.result.exception")));
			}catch (NonUniqueResultException e) {
				throw new AsentamientoNotaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.otros.mecanismos.guardar.pendiente.buscar.por.id.non.unique.result.exception")));
			} catch (Exception e) {
				throw new AsentamientoNotaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.otros.mecanismos.guardar.pendiente.buscar.por.id.exception")));
			}
		}else{
			throw new AsentamientoNotaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.otros.mecanismos.guardar.pendiente.buscar.por.id.exception")));
		}
		return retorno;
		
	}
	
	
	@Override
	public boolean guardarAptitud2018(EstudianteAptitudOtrosMecanismosJdbcDto entidad,  RolFlujoCarrera rolFlujoCarrera, Integer estado) throws  AsentamientoNotaNoEncontradoException , AsentamientoNotaException{
		boolean retorno = false;
		if (entidad != null  && rolFlujoCarrera != null) {
			try {	
				//**********************************************************************
				//********** actualizar el tramite actual ******************************
				//**********************************************************************
				
				TramiteTitulo trttAux = em.find(TramiteTitulo.class, entidad.getTrttId());
				
				trttAux.setTrttEstadoProceso(estado);
				
				//*******************************************************************************
				//******** Creo un nuevo proceso_tramite  para no perder el histotial ***********
				//*******************************************************************************
				
				ProcesoTramite nuevoPrtr = new ProcesoTramite();
				nuevoPrtr.setPrtrTipoProceso(estado);
				nuevoPrtr.setPrtrFechaEjecucion(entidad.getPrtrFechaEjecucion());
				nuevoPrtr.setPrtrRolFlujoCarrera(rolFlujoCarrera);
				nuevoPrtr.setPrtrTramiteTitulo(trttAux);
				em.persist(nuevoPrtr);
			
				
				Aptitud nuevoAptitud = new Aptitud();
				nuevoAptitud.setAptAproboActualizar(entidad.getAptAproboActualizar());
				nuevoAptitud.setAptRequisitos(entidad.getAptRequisitos());
				nuevoAptitud.setAptAproboTutor(entidad.getAptAproboTutor());
				nuevoAptitud.setAptFinMalla(entidad.getAptFinMalla());
				nuevoAptitud.setAptNotaDirector(entidad.getAptNotaDirector());
				nuevoAptitud.setAptSuficienciaIngles(entidad.getAptSuficienciaIngles());
				nuevoAptitud.setAptProcesoTramite(nuevoPrtr);
				em.persist(nuevoAptitud);
					
			}catch (NoResultException e) {
				throw new AsentamientoNotaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.otros.mecanismos.guardar.pendiente.buscar.por.id.no.result.exception")));
			}catch (NonUniqueResultException e) {
				throw new AsentamientoNotaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.otros.mecanismos.guardar.pendiente.buscar.por.id.non.unique.result.exception")));
			} catch (Exception e) {
				throw new AsentamientoNotaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.otros.mecanismos.guardar.pendiente.buscar.por.id.exception")));
			}
		}else{
			throw new AsentamientoNotaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.otros.mecanismos.guardar.pendiente.buscar.por.id.exception")));
		}
		return retorno;
		
	}
	
	/**
	 * Busca la entidad asentamiento_nota correspondiente a la cédula del postulante apto o no apto 
	 * otros mecanismos y carrera del evaluador 
	 * @param  entidad - nueva entidad a insertar
	 * @return la nueva entidad insertada
	 * @throws  
	 */
	public AsentamientoNota buscarAsentamientoNotaXPostulanteAptoNoAptoXEvaluadorOtrosMecanismos(String cedulaPostulante ,  String cedulaEvaluador, Integer convocatoria) throws  AsentamientoNotaNoEncontradoException , AsentamientoNotaException{
		AsentamientoNota retorno=new AsentamientoNota();
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" asno.");sbSql.append(JdbcConstantes.ASNO_TRB_LECTOR1);
			sbSql.append(" ,asno.");sbSql.append(JdbcConstantes.ASNO_TRB_LECTOR2);
			sbSql.append(" , CASE WHEN asno.");sbSql.append(JdbcConstantes.ASNO_TRB_LECTOR3);sbSql.append(" IS NULL ");
			sbSql.append(" THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE asno."); sbSql.append(JdbcConstantes.ASNO_TRB_LECTOR3);sbSql.append(" END AS "); sbSql.append(JdbcConstantes.ASNO_TRB_LECTOR3);
			sbSql.append(" ,asno.");sbSql.append(JdbcConstantes.ASNO_PRM_TRB_ESCRITO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asno ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_CARRERA);sbSql.append(" mcttcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION);sbSql.append(" mctt ");
			sbSql.append(" WHERE ");
			sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_MCCR_ID); sbSql.append(" =  mcttcr.");sbSql.append(JdbcConstantes.MCCR_ID);
			sbSql.append(" AND mcttcr.");sbSql.append(JdbcConstantes.MCCR_MCTT_ID); sbSql.append(" =  mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
			
			sbSql.append(" AND asno.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID); sbSql.append(" =  prtr.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID); sbSql.append(" =  crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" = fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" AND (trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_APTO_VALUE);
			sbSql.append(" OR trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_NO_APTO_VALUE);
			sbSql.append(" ) ");
			sbSql.append(" AND mctt.");sbSql.append(JdbcConstantes.MCTT_ID);sbSql.append(" <> ");sbSql.append(MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_VALUE);
			if(convocatoria==GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" > ? ");
			}else{
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ? ");
			}
			
			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" LIKE  '");sbSql.append(cedulaPostulante);sbSql.append("'");
			sbSql.append(" AND crr.crr_id IN ");
			sbSql.append("(SELECT DISTINCT crr.crr_id from carrera crr, rol_flujo_carrera roflcr, usuario_rol usro, usuario usr ");  
			sbSql.append(" WHERE usr.usr_id =  usro.usr_id  AND  usro.usro_id =  roflcr.usro_id "); 
			sbSql.append(" AND  crr.crr_id =  roflcr.crr_id AND  usr.usr_identificacion LIKE '");
			sbSql.append(cedulaEvaluador);sbSql.append("') ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, convocatoria); //cargo la convocatoria
			rs = pstmt.executeQuery();
			while(rs.next()){
				retorno.setAsnoTrbLector1(rs.getBigDecimal(JdbcConstantes.ASNO_TRB_LECTOR1));
				retorno.setAsnoTrbLector2(rs.getBigDecimal(JdbcConstantes.ASNO_TRB_LECTOR2));
				retorno.setAsnoPrmTrbEscrito(rs.getBigDecimal(JdbcConstantes.ASNO_PRM_TRB_ESCRITO));
			}
		} catch (SQLException e) {
		} catch (Exception e) {
		} finally {
			try {
				if( rs != null){
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		return retorno;
	}

	@Override
	public EstudianteActaGradoJdbcDto buscarAsentamientoNotaEstudianteAptoXIdentificacionXCarrera(String cedulaPostulante ,  Integer carrera) throws  AsentamientoNotaNoEncontradoException , AsentamientoNotaException{
		EstudianteActaGradoJdbcDto retorno=new EstudianteActaGradoJdbcDto();
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" asno.");sbSql.append(JdbcConstantes.ASNO_ID);
			sbSql.append(" ,asno.");sbSql.append(JdbcConstantes.ASNO_PRM_TRB_ESCRITO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asno ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_CARRERA);sbSql.append(" mcttcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION);sbSql.append(" mctt ");
			sbSql.append(" WHERE ");
			sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_MCCR_ID); sbSql.append(" =  mcttcr.");sbSql.append(JdbcConstantes.MCCR_ID);
			sbSql.append(" AND mcttcr.");sbSql.append(JdbcConstantes.MCCR_MCTT_ID); sbSql.append(" =  mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
			
			sbSql.append(" AND asno.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID); sbSql.append(" =  prtr.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID); sbSql.append(" =  crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" = fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" AND (trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_APTO_VALUE);
			sbSql.append(" OR trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_NO_APTO_VALUE);
			sbSql.append(" ) ");
			sbSql.append(" AND mctt.");sbSql.append(JdbcConstantes.MCTT_ID);sbSql.append(" <> ");sbSql.append(MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_VALUE);
				sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
			
			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" LIKE  '");sbSql.append(cedulaPostulante);sbSql.append("'");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, carrera); //cargo la convocatoria
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				retorno.setAsnoPrmDfnEscrito(rs.getBigDecimal(JdbcConstantes.ASNO_PRM_TRB_ESCRITO));
				retorno.setAsnoId(rs.getInt(JdbcConstantes.ASNO_ID));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if( rs != null){
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		return retorno;
	}
	
	/**
	 * Busca la entidad asentamiento_nota, asignacion_titulacion, ficha_dcn_asg_titulacion 
	 * de los postulantes pendientes nota para pasar la aptitud
	 * @param  cedulaPostulante - cédula a buscar
	 * @param  cedulaEvaluador - cédula del director de carrera
	 * @param  convocatoria - convocatoria a buscar
	 * @return la entidad que se busca
	 * @throws  
	 */
	@Override
	public List<EstudianteEmisionActaJdbcDto> buscarPostulantesImprimirActa(
			String cedulaPostulante, 
			Integer convocatoria, Integer carrera, String cedulaValidador) throws AsentamientoNotaNoEncontradoException,
			AsentamientoNotaException,FichaDcnAsgTitulacionNoEncontradoException,FichaDcnAsgTitulacionException {
		List<EstudianteEmisionActaJdbcDto> retorno=null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			retorno= new ArrayList<EstudianteEmisionActaJdbcDto>();
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);
			
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_FECHA_NACIMIENTO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_UBC_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ETN_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_UBICACION_FOTO);
			
			
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,mctt.");sbSql.append(JdbcConstantes.MCTT_DESCRIPCION);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_FECHA_INICIO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREVIOS);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_TIPO_DURAC_REC);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_TIEMPO_ESTUD_REC);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_TIPO_COLEGIO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_INAC_ID_INST_EST_PREVIOS);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_TITULO_BACHILLER);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_LINK_TESIS);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ID);
			
			
			
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_NOTA_TRAB_TITULACION);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_CNCR_ID);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_NOTA_PROM_ACUMULADO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_NOTA_TRAB_TITULACION);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_FECHA_ACTA_GRADO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_HORA_ACTA_GRADO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_NUM_ACTA_GRADO);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" ,mdl.");sbSql.append(JdbcConstantes.MDL_DESCRIPCION);
			sbSql.append(" ,ttl.");sbSql.append(JdbcConstantes.TTL_DESCRIPCION);
			sbSql.append(" ,ubc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
			sbSql.append(" FROM ");
			
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION);sbSql.append(" mctt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_CARRERA);sbSql.append(" mcttcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MODALIDAD);sbSql.append(" mdl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TITULO);sbSql.append(" ttl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubc ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_MCCR_ID); sbSql.append(" =  mcttcr.");sbSql.append(JdbcConstantes.MCCR_ID);
			sbSql.append(" AND mcttcr.");sbSql.append(JdbcConstantes.MCCR_MCTT_ID); sbSql.append(" =  mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
			sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID); sbSql.append(" =  trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
			
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_CNCR_ID); sbSql.append(" =  cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND mdl.");sbSql.append(JdbcConstantes.MDL_ID); sbSql.append(" =  cncr.");sbSql.append(JdbcConstantes.CNCR_MDL_ID);
			sbSql.append(" AND ubc.");sbSql.append(JdbcConstantes.UBC_ID); sbSql.append(" =  cncr.");sbSql.append(JdbcConstantes.CNCR_UBC_ID);
			sbSql.append(" AND ttl.");sbSql.append(JdbcConstantes.TTL_ID); sbSql.append(" =  cncr.");sbSql.append(JdbcConstantes.CNCR_TTL_ID);
			if(carrera==GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID); sbSql.append(" IN (");
				sbSql.append(" SELECT ");
				sbSql.append(" distinct crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
				sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr,");
				sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro, ");sbSql.append(JdbcConstantes.TABLA_USUARIO);
				sbSql.append(" usr ");
				sbSql.append(" WHERE usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USR_ID);
				sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" = roflcr.");
				sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" AND usr.");
				sbSql.append(JdbcConstantes.USR_IDENTIFICACION);sbSql.append(" = '");sbSql.append(cedulaValidador);sbSql.append("') ");
			}else{
				sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID); sbSql.append(" =  ");sbSql.append(carrera);
			}
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID); sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID); sbSql.append(" = fcl.");sbSql.append(JdbcConstantes.FCL_ID);
						// solo cargo los estudiantes que han pasado por la generación del acta
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND (trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO); sbSql.append(" >= ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_VALUE);
			sbSql.append(" OR trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO); sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_DEFENSA_VALUE);
			sbSql.append(" OR trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO); sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_DEFENSA_ORAL_VALUE);
			sbSql.append(" OR trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO); sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_VALUE);
			sbSql.append(" ) ");
			
			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION); sbSql.append(" LIKE ? ");
			if(convocatoria==GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID); sbSql.append(" > ? ");
			}else{
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID); sbSql.append(" = ? ");	
			}
			sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" , ");
			sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" , ");
			sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			con = ds.getConnection();
			
			
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, "%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(cedulaPostulante)+"%"); //cargo la cédula del postulante
			pstmt.setInt(2, convocatoria); //cargo la convocatoria
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				EstudianteEmisionActaJdbcDto estudianteAux = new EstudianteEmisionActaJdbcDto();
				estudianteAux.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
				estudianteAux.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
				estudianteAux.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
				estudianteAux.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
				estudianteAux.setPrsTipoIdentificacion(rs.getInt(JdbcConstantes.PRS_TIPO_IDENTIFICACION));
				estudianteAux.setPrsMailPersonal(rs.getString(JdbcConstantes.PRS_MAIL_PERSONAL));
				estudianteAux.setPrsSexo(rs.getInt(JdbcConstantes.PRS_SEXO));
				estudianteAux.setPrsFechaNacimiento(rs.getDate(JdbcConstantes.PRS_FECHA_NACIMIENTO));
				estudianteAux.setPrsUbicacionFoto(rs.getString(JdbcConstantes.PRS_UBICACION_FOTO));
				estudianteAux.setPrsUbcId(rs.getInt(JdbcConstantes.PRS_UBC_ID));
				estudianteAux.setPrsEtnId(rs.getInt(JdbcConstantes.PRS_ETN_ID));
				estudianteAux.setTrttId(rs.getInt(JdbcConstantes.TRTT_ID));
				estudianteAux.setTrttEstadoProceso(rs.getInt(JdbcConstantes.TRTT_ESTADO_PROCESO));
				estudianteAux.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
				estudianteAux.setMcttdescripcion(rs.getString(JdbcConstantes.MCTT_DESCRIPCION));
				estudianteAux.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
				estudianteAux.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
				estudianteAux.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
				estudianteAux.setFcesCncrId(rs.getInt(JdbcConstantes.FCES_CNCR_ID));
				
				estudianteAux.setFcesFechaInicio(rs.getDate(JdbcConstantes.FCES_FECHA_INICIO));
				estudianteAux.setFcesFechaEgresamiento(rs.getDate(JdbcConstantes.FCES_FECHA_EGRESAMIENTO));
				estudianteAux.setFcesRecEstuPrevios(rs.getInt(JdbcConstantes.FCES_REC_ESTUD_PREVIOS));
				estudianteAux.setFcesCrrEstudPrevios(rs.getString(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS));
				
				estudianteAux.setFcesTipoColegio(rs.getInt(JdbcConstantes.FCES_TIPO_COLEGIO));
				estudianteAux.setFcesTipoDuracionRec(rs.getInt(JdbcConstantes.FCES_TIPO_DURAC_REC));
				estudianteAux.setFcesTiempoEstudRec(rs.getInt(JdbcConstantes.FCES_TIEMPO_ESTUD_REC));
				estudianteAux.setFcesInacEstPrevios(rs.getInt(JdbcConstantes.FCES_INAC_ID_INST_EST_PREVIOS));
				estudianteAux.setFcesTituloBachiller(rs.getString(JdbcConstantes.FCES_TITULO_BACHILLER));
				estudianteAux.setFcesLinkTesis(rs.getString(JdbcConstantes.FCES_LINK_TESIS));
				
				estudianteAux.setFcesNotaPromAcumulado(rs.getBigDecimal(JdbcConstantes.FCES_NOTA_PROM_ACUMULADO));
				estudianteAux.setFcesNotaTrabTitulacion(rs.getBigDecimal(JdbcConstantes.FCES_NOTA_TRAB_TITULACION));
				estudianteAux.setFcesHoraActaGrado(rs.getString(JdbcConstantes.FCES_HORA_ACTA_GRADO));
				estudianteAux.setFcesFechaActaGrado(rs.getDate(JdbcConstantes.FCES_FECHA_ACTA_GRADO));
				estudianteAux.setFcesNumActaGrado(rs.getString(JdbcConstantes.FCES_NUM_ACTA_GRADO));
				estudianteAux.setFclId(rs.getInt(JdbcConstantes.FCL_ID));
				estudianteAux.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
				estudianteAux.setMdlDescripcion(rs.getString(JdbcConstantes.MDL_DESCRIPCION));
				estudianteAux.setTtlDescripcion(rs.getString(JdbcConstantes.TTL_DESCRIPCION));
				estudianteAux.setUbcDescripcion(rs.getString(JdbcConstantes.UBC_DESCRIPCION));
				estudianteAux.setCnvId(rs.getInt(JdbcConstantes.CNV_ID));
				retorno.add(estudianteAux);
				
			}
		} catch (Exception e) {
		} finally {
			try {
				if( rs != null){
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		return retorno;
	}
	
	
	/**
	 * Busca la entidad asentamiento_nota, asignacion_titulacion, ficha_dcn_asg_titulacion 
	 * de los postulantes pendientes nota para pasar la aptitud
	 * @param  cedulaPostulante - cédula a buscar
	 * @param  cedulaEvaluador - cédula del director de carrera
	 * @param  convocatoria - convocatoria a buscar
	 * @return la entidad que se busca
	 * @throws  
	 */
	@Override
	public List<EstudianteEmisionActaJdbcDto> buscarPostulantesReImprimirActa(
			String cedulaPostulante, 
			Integer convocatoria, Integer carrera, String cedulaValidador) throws AsentamientoNotaNoEncontradoException,
			AsentamientoNotaException,FichaDcnAsgTitulacionNoEncontradoException,FichaDcnAsgTitulacionException {
		List<EstudianteEmisionActaJdbcDto> retorno=null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			retorno= new ArrayList<EstudianteEmisionActaJdbcDto>();
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);
			
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_FECHA_NACIMIENTO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_UBC_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ETN_ID);
			
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,mctt.");sbSql.append(JdbcConstantes.MCTT_DESCRIPCION);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_FECHA_INICIO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREVIOS);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_TIPO_DURAC_REC);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_TIEMPO_ESTUD_REC);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_TIPO_COLEGIO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_INAC_ID_INST_EST_PREVIOS);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_TITULO_BACHILLER);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_LINK_TESIS);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ID);
			
			
			
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_NOTA_TRAB_TITULACION);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_CNCR_ID);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_NOTA_PROM_ACUMULADO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_NOTA_TRAB_TITULACION);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_FECHA_ACTA_GRADO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_HORA_ACTA_GRADO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_NUM_ACTA_GRADO);
			sbSql.append(",  CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_DECANO); sbSql.append(" IS NULL THEN '");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append("' ELSE fces.");sbSql.append(JdbcConstantes.FCES_DECANO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_DECANO);
			sbSql.append(",  CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_SUBDECANO); sbSql.append(" IS NULL THEN '");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append("' ELSE fces.");sbSql.append(JdbcConstantes.FCES_SUBDECANO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_SUBDECANO);
			sbSql.append(",  CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_SECRETARIO); sbSql.append(" IS NULL THEN '");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append("' ELSE fces.");sbSql.append(JdbcConstantes.FCES_SECRETARIO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_SECRETARIO);
			sbSql.append(",  CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_DIRECTOR); sbSql.append(" IS NULL THEN '");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append("' ELSE fces.");sbSql.append(JdbcConstantes.FCES_DIRECTOR); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_DIRECTOR);
			sbSql.append(",  CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_PORCENTAJE_COMPLEX); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_PORCENTAJE_COMPLEX); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_PORCENTAJE_COMPLEX);
			sbSql.append(",  CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_DECANO_SEXO); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_DECANO_SEXO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_DECANO_SEXO);
			sbSql.append(",  CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_SUBDECANO_SEXO); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_SUBDECANO_SEXO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_SUBDECANO_SEXO);
			sbSql.append(",  CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_SECRETARIO_SEXO); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_SECRETARIO_SEXO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_SECRETARIO_SEXO);
			sbSql.append(",  CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_DIRECTOR_SEXO); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_DIRECTOR_SEXO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_DIRECTOR_SEXO);
			
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" ,mdl.");sbSql.append(JdbcConstantes.MDL_DESCRIPCION);
			sbSql.append(" ,ttl.");sbSql.append(JdbcConstantes.TTL_DESCRIPCION);
			sbSql.append(" ,ubc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
			sbSql.append(",  CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_DECANO); sbSql.append(" IS NULL THEN '");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append("' ELSE fces.");sbSql.append(JdbcConstantes.FCES_DECANO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_DECANO);
			sbSql.append(",  CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_SUBDECANO); sbSql.append(" IS NULL THEN '");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append("' ELSE fces.");sbSql.append(JdbcConstantes.FCES_SUBDECANO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_SUBDECANO);
			sbSql.append(",  CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_SECRETARIO); sbSql.append(" IS NULL THEN '");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append("' ELSE fces.");sbSql.append(JdbcConstantes.FCES_SECRETARIO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_SECRETARIO);
			sbSql.append(",  CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_DIRECTOR); sbSql.append(" IS NULL THEN '");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append("' ELSE fces.");sbSql.append(JdbcConstantes.FCES_DIRECTOR); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_DIRECTOR);
			sbSql.append(",  CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_PORCENTAJE_COMPLEX); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_PORCENTAJE_COMPLEX); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_PORCENTAJE_COMPLEX);
			sbSql.append(",  CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_DECANO_SEXO); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_DECANO_SEXO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_DECANO_SEXO);
			sbSql.append(",  CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_SUBDECANO_SEXO); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_SUBDECANO_SEXO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_SUBDECANO_SEXO);
			sbSql.append(",  CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_SECRETARIO_SEXO); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_SECRETARIO_SEXO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_SECRETARIO_SEXO);
			sbSql.append(",  CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_DIRECTOR_SEXO); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_DIRECTOR_SEXO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_DIRECTOR_SEXO);
			sbSql.append(" FROM ");
			
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION);sbSql.append(" mctt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_CARRERA);sbSql.append(" mcttcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MODALIDAD);sbSql.append(" mdl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TITULO);sbSql.append(" ttl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubc ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_MCCR_ID); sbSql.append(" =  mcttcr.");sbSql.append(JdbcConstantes.MCCR_ID);
			sbSql.append(" AND mcttcr.");sbSql.append(JdbcConstantes.MCCR_MCTT_ID); sbSql.append(" =  mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
			sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID); sbSql.append(" =  trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
			
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_CNCR_ID); sbSql.append(" =  cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND mdl.");sbSql.append(JdbcConstantes.MDL_ID); sbSql.append(" =  cncr.");sbSql.append(JdbcConstantes.CNCR_MDL_ID);
			sbSql.append(" AND ubc.");sbSql.append(JdbcConstantes.UBC_ID); sbSql.append(" =  cncr.");sbSql.append(JdbcConstantes.CNCR_UBC_ID);
			sbSql.append(" AND ttl.");sbSql.append(JdbcConstantes.TTL_ID); sbSql.append(" =  cncr.");sbSql.append(JdbcConstantes.CNCR_TTL_ID);
			if(carrera==GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID); sbSql.append(" IN (");
				sbSql.append(" SELECT ");
				sbSql.append(" distinct crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
				sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr,");
				sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro, ");sbSql.append(JdbcConstantes.TABLA_USUARIO);
				sbSql.append(" usr ");
				sbSql.append(" WHERE usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USR_ID);
				sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" = roflcr.");
				sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" AND usr.");
				sbSql.append(JdbcConstantes.USR_IDENTIFICACION);sbSql.append(" = '");sbSql.append(cedulaValidador);sbSql.append("') ");
			}else{
				sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID); sbSql.append(" =  ");sbSql.append(carrera);
			}
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID); sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID); sbSql.append(" = fcl.");sbSql.append(JdbcConstantes.FCL_ID);
						// solo cargo los estudiantes que han pasado por la generación del acta
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND (");
			sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO); sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_DEFENSA_ORAL_VALUE);
			sbSql.append(" OR trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO); sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_VALUE);
			sbSql.append(" OR trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO); sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_VALUE);
			sbSql.append(" OR trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO); sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_VALUE);
			sbSql.append(" ) ");
			
			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION); sbSql.append(" LIKE ? ");
			if(convocatoria==GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID); sbSql.append(" > ? ");
			}else{
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID); sbSql.append(" = ? ");	
			}
			sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" , ");
			sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" , ");
			sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			con = ds.getConnection();
			
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, "%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(cedulaPostulante)+"%"); //cargo la cédula del postulante
			pstmt.setInt(2, convocatoria); //cargo la convocatoria
			
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				EstudianteEmisionActaJdbcDto estudianteAux = new EstudianteEmisionActaJdbcDto();
				estudianteAux.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
				estudianteAux.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
				estudianteAux.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
				estudianteAux.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
				estudianteAux.setPrsTipoIdentificacion(rs.getInt(JdbcConstantes.PRS_TIPO_IDENTIFICACION));
				estudianteAux.setPrsMailPersonal(rs.getString(JdbcConstantes.PRS_MAIL_PERSONAL));
				estudianteAux.setPrsSexo(rs.getInt(JdbcConstantes.PRS_SEXO));
				estudianteAux.setPrsFechaNacimiento(rs.getDate(JdbcConstantes.PRS_FECHA_NACIMIENTO));
				estudianteAux.setPrsUbcId(rs.getInt(JdbcConstantes.PRS_UBC_ID));
				estudianteAux.setPrsEtnId(rs.getInt(JdbcConstantes.PRS_ETN_ID));
				estudianteAux.setTrttId(rs.getInt(JdbcConstantes.TRTT_ID));
				estudianteAux.setTrttEstadoProceso(rs.getInt(JdbcConstantes.TRTT_ESTADO_PROCESO));
				estudianteAux.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
				estudianteAux.setMcttdescripcion(rs.getString(JdbcConstantes.MCTT_DESCRIPCION));
				estudianteAux.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
				estudianteAux.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
				estudianteAux.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
				estudianteAux.setFcesCncrId(rs.getInt(JdbcConstantes.FCES_CNCR_ID));
				
				estudianteAux.setFcesFechaInicio(rs.getDate(JdbcConstantes.FCES_FECHA_INICIO));
				estudianteAux.setFcesFechaEgresamiento(rs.getDate(JdbcConstantes.FCES_FECHA_EGRESAMIENTO));
				estudianteAux.setFcesRecEstuPrevios(rs.getInt(JdbcConstantes.FCES_REC_ESTUD_PREVIOS));
				estudianteAux.setFcesCrrEstudPrevios(rs.getString(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS));
				
				estudianteAux.setFcesTipoColegio(rs.getInt(JdbcConstantes.FCES_TIPO_COLEGIO));
				estudianteAux.setFcesTipoDuracionRec(rs.getInt(JdbcConstantes.FCES_TIPO_DURAC_REC));
				estudianteAux.setFcesTiempoEstudRec(rs.getInt(JdbcConstantes.FCES_TIEMPO_ESTUD_REC));
				estudianteAux.setFcesInacEstPrevios(rs.getInt(JdbcConstantes.FCES_INAC_ID_INST_EST_PREVIOS));
				estudianteAux.setFcesTituloBachillerId(rs.getInt(JdbcConstantes.FCES_TITULO_BACHILLER));
				estudianteAux.setFcesLinkTesis(rs.getString(JdbcConstantes.FCES_LINK_TESIS));
				
				estudianteAux.setFcesNotaPromAcumulado(rs.getBigDecimal(JdbcConstantes.FCES_NOTA_PROM_ACUMULADO));
				estudianteAux.setFcesNotaTrabTitulacion(rs.getBigDecimal(JdbcConstantes.FCES_NOTA_TRAB_TITULACION));
				estudianteAux.setFcesHoraActaGrado(rs.getString(JdbcConstantes.FCES_HORA_ACTA_GRADO));
				estudianteAux.setFcesFechaActaGrado(rs.getDate(JdbcConstantes.FCES_FECHA_ACTA_GRADO));
				estudianteAux.setFcesNumActaGrado(rs.getString(JdbcConstantes.FCES_NUM_ACTA_GRADO));
				estudianteAux.setFclId(rs.getInt(JdbcConstantes.FCL_ID));
				estudianteAux.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
				estudianteAux.setMdlDescripcion(rs.getString(JdbcConstantes.MDL_DESCRIPCION));
				estudianteAux.setTtlDescripcion(rs.getString(JdbcConstantes.TTL_DESCRIPCION));
				estudianteAux.setUbcDescripcion(rs.getString(JdbcConstantes.UBC_DESCRIPCION));
				estudianteAux.setCnvId(rs.getInt(JdbcConstantes.CNV_ID));
				estudianteAux.setFcesDecano(rs.getString(JdbcConstantes.FCES_DECANO));
				estudianteAux.setFcesDecanoSexo(rs.getInt(JdbcConstantes.FCES_DECANO_SEXO));
				estudianteAux.setFcesSecretario(rs.getString(JdbcConstantes.FCES_SECRETARIO));
				estudianteAux.setFcesSecretarioSexo(rs.getInt(JdbcConstantes.FCES_SECRETARIO_SEXO));
				estudianteAux.setFcesDirector(rs.getString(JdbcConstantes.FCES_DIRECTOR));
				estudianteAux.setFcesDirectorSexo(rs.getInt(JdbcConstantes.FCES_DIRECTOR_SEXO));
				estudianteAux.setFcesSubdecano(rs.getString(JdbcConstantes.FCES_SUBDECANO));
				estudianteAux.setFcesSubdecanoSexo(rs.getInt(JdbcConstantes.FCES_SUBDECANO_SEXO));
				estudianteAux.setFcesPorcentajeComplex(rs.getInt(JdbcConstantes.FCES_PORCENTAJE_COMPLEX));
				retorno.add(estudianteAux);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if( rs != null){
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		return retorno;
	}
	
	
	/**
	 * Busca la entidad asentamiento_nota, asignacion_titulacion, ficha_dcn_asg_titulacion
	 * que hayan aprobado el proceso de titulación 
	 * @param  cedulaPostulante - cédula a buscar
	 * @param  convocatoria - convocatoria a buscar
	 * @return la entidad que se busca
	 * @throws  
	 */
	@Override
	public List<EstudianteAptitudOtrosMecanismosJdbcDto> buscarPostulantesNotasTribunalDocentesXCedulaPostulante(
			String cedulaPostulante, 
			Integer convocatoria, Integer carrera) throws AsentamientoNotaNoEncontradoException,
			AsentamientoNotaException,FichaDcnAsgTitulacionNoEncontradoException,FichaDcnAsgTitulacionException {
		List<EstudianteAptitudOtrosMecanismosJdbcDto> retorno=null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			retorno= new ArrayList<EstudianteAptitudOtrosMecanismosJdbcDto>();
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT cedula, apellido1, apellido2,nombres, id, nota1, nota2, nota3, notaTrabajoTitulacion, cedulaEst, apellido1Est,cnvId, ");
			sbSql.append(" email, apellido2Est, nombresEst, trtt_id , asnoId , carreraDetalle , mecanismoDetalle, tema , tutor, fcesId, carreraId,convocatoria"); 
			sbSql.append(" FROM (");
			sbSql.append(" SELECT prsDocente.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" cedula, ");
			sbSql.append(" prsDocente.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" apellido1, ");
			sbSql.append(" prsDocente.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(" apellido2, ");
			sbSql.append(" prsDocente.");sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(" nombres, ");
			sbSql.append(" fcdcastt.");sbSql.append(JdbcConstantes.FCDCASTT_ID);sbSql.append(" id, ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" cedulaEst, ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" apellido1Est, ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(" apellido2Est, ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(" nombresEst, ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);sbSql.append(" email, ");
			sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" trtt_id, ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" carreraDetalle, ");
			sbSql.append(" mctt.");sbSql.append(JdbcConstantes.MCTT_DESCRIPCION);sbSql.append(" mecanismoDetalle, ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" fcesId, ");
			sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" carreraId, ");
			sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);sbSql.append(" convocatoria, ");
			sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" cnvId, ");
			
			sbSql.append(" (");
			sbSql.append(" SELECT astt.");sbSql.append(JdbcConstantes.ASTT_TEMA_TRABAJO);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASIGNACION_TITULACION);sbSql.append(" astt ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND astt.");sbSql.append(JdbcConstantes.ASTT_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" = ");sbSql.append(carrera);
			sbSql.append(" ) tema,");
			
			sbSql.append(" (");
			sbSql.append(" SELECT astt.");sbSql.append(JdbcConstantes.ASTT_DIRECTOR_CIENTIFICO);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASIGNACION_TITULACION);sbSql.append(" astt ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND astt.");sbSql.append(JdbcConstantes.ASTT_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" = ");sbSql.append(carrera);
			sbSql.append(" ) tutor,");
			
			sbSql.append(" (");
			sbSql.append(" SELECT asno1.");sbSql.append(JdbcConstantes.ASNO_ID);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asno1 ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND asno1.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
			
			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" = ");sbSql.append(carrera);
			sbSql.append(" ) asnoId,");
			sbSql.append(" (");
			
			sbSql.append(" SELECT asno1.");sbSql.append(JdbcConstantes.ASNO_TRB_LECTOR1);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asno1 ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND asno1.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" = ");sbSql.append(carrera);
			sbSql.append(" ) nota1,");
			sbSql.append(" (");
			sbSql.append(" SELECT asno1.");sbSql.append(JdbcConstantes.ASNO_TRB_LECTOR2);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asno1 ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND asno1.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" = ");sbSql.append(carrera);
			sbSql.append(" ) nota2,");
			sbSql.append(" (");
			sbSql.append(" SELECT asno1.");sbSql.append(JdbcConstantes.ASNO_TRB_LECTOR3);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asno1 ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND asno1.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" = ");sbSql.append(carrera);
			sbSql.append(" ) nota3, ");
			sbSql.append(" (");
			sbSql.append(" SELECT asno1.");sbSql.append(JdbcConstantes.ASNO_PRM_TRB_ESCRITO);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asno1 ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND asno1.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" = ");sbSql.append(carrera);
			sbSql.append(" ) notaTrabajoTitulacion ");
			sbSql.append(" FROM ");
			
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASIGNACION_TITULACION);sbSql.append(" astt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DCN_ASG_TITULACION);sbSql.append(" fcdcastt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prsDocente ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION);sbSql.append(" mctt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_CARRERA);sbSql.append(" mcttcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND prtr.");sbSql.append(JdbcConstantes.PRTR_ID); sbSql.append(" =  astt.");sbSql.append(JdbcConstantes.ASTT_PRTR_ID);
			sbSql.append(" AND astt.");sbSql.append(JdbcConstantes.ASTT_ID); sbSql.append(" =  fcdcastt.");sbSql.append(JdbcConstantes.FCDCASTT_ASTT_ID);
			sbSql.append(" AND fcdc.");sbSql.append(JdbcConstantes.FCDC_ID); sbSql.append(" =  fcdcastt.");sbSql.append(JdbcConstantes.FCDCASTT_FCDC_ID);
			sbSql.append(" AND fcdcastt.");sbSql.append(JdbcConstantes.FCDCASTT_TIPO_TRIBUNAL); sbSql.append(" = ");sbSql.append(FichaDocenteAsignaciontitulacionConstantes.LECTOR_VALUE);
			sbSql.append(" AND fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID); sbSql.append(" =  prsDocente.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_MCCR_ID); sbSql.append(" =  mcttcr.");sbSql.append(JdbcConstantes.MCCR_ID);
			sbSql.append(" AND mcttcr.");sbSql.append(JdbcConstantes.MCCR_MCTT_ID); sbSql.append(" =  mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
			sbSql.append(" AND mctt.");sbSql.append(JdbcConstantes.MCTT_ID); sbSql.append(" <> ");sbSql.append(MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_VALUE);
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID); sbSql.append(" =  ");sbSql.append(carrera);
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID); sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID); sbSql.append(" = ? ");
			// solo cargo los estudiantes que han aprobado
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO); sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_APTO_VALUE);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID); sbSql.append(" = cnv.");sbSql.append(JdbcConstantes.CNV_ID);
			
			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION); sbSql.append(" LIKE ? ");
			sbSql.append(" GROUP BY prsDocente.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION); sbSql.append(" , prsDocente.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prsDocente.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prsDocente.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , fcdcastt.");sbSql.append(JdbcConstantes.FCDCASTT_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , mctt.");sbSql.append(JdbcConstantes.MCTT_DESCRIPCION);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" , cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);
			sbSql.append(" , cnv.");sbSql.append(JdbcConstantes.CNV_ID);
			sbSql.append(" ) docente ORDER BY carreraDetalle, apellido1Est , apellido2Est, id");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, carrera);
			pstmt.setString(2, "%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(cedulaPostulante)+"%"); //cargo la cédula del postulante
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				FichaDocenteAsignacionTitulacionDto fcdcAsttAux = new FichaDocenteAsignacionTitulacionDto();
				EstudianteAptitudOtrosMecanismosJdbcDto estudianteAux = new EstudianteAptitudOtrosMecanismosJdbcDto();
				fcdcAsttAux.setPrsIdentificacion(rs.getString("cedula"));
				fcdcAsttAux.setPrsPrimerApellido(rs.getString("apellido1"));
				fcdcAsttAux.setPrsSegundoApellido(rs.getString("apellido2"));
				fcdcAsttAux.setPrsNombres(rs.getString("nombres"));
				fcdcAsttAux.setFcdcasttId(rs.getInt("id"));
				estudianteAux.getListaDocentesTribunal().add(fcdcAsttAux);
				rs.next();
				fcdcAsttAux = new FichaDocenteAsignacionTitulacionDto();
				fcdcAsttAux.setPrsIdentificacion(rs.getString("cedula"));
				fcdcAsttAux.setPrsPrimerApellido(rs.getString("apellido1"));
				fcdcAsttAux.setPrsSegundoApellido(rs.getString("apellido2"));
				fcdcAsttAux.setPrsNombres(rs.getString("nombres"));
				fcdcAsttAux.setPrsMailPersonal(rs.getString("email"));
				fcdcAsttAux.setFcdcasttId(rs.getInt("id"));
				estudianteAux.getListaDocentesTribunal().add(fcdcAsttAux);
				try {
					if(rs.getBigDecimal("nota3")!=null){
						rs.next();
						fcdcAsttAux = new FichaDocenteAsignacionTitulacionDto();
						fcdcAsttAux.setPrsIdentificacion(rs.getString("cedula"));
						fcdcAsttAux.setPrsPrimerApellido(rs.getString("apellido1"));
						fcdcAsttAux.setPrsSegundoApellido(rs.getString("apellido2"));
						fcdcAsttAux.setPrsNombres(rs.getString("nombres"));
						fcdcAsttAux.setFcdcasttId(rs.getInt("id"));
						estudianteAux.getListaDocentesTribunal().add(fcdcAsttAux);	
					}
				} catch (Exception e) {
				}
				estudianteAux.setPrsIdentificacion(rs.getString("cedulaEst"));
				estudianteAux.setPrsPrimerApellido(rs.getString("apellido1Est"));
				estudianteAux.setPrsSegundoApellido(rs.getString("apellido2Est"));
				estudianteAux.setPrsNombres(rs.getString("nombresEst"));
				estudianteAux.setAsnoTrbLector1(rs.getBigDecimal("nota1").floatValue());
				estudianteAux.setAsnoTrbLector2(rs.getBigDecimal("nota2").floatValue());
				try {
					estudianteAux.setAsnoTrbLector3(rs.getBigDecimal("nota3").floatValue());
				} catch (Exception e) {
					estudianteAux.setAsnoTrbLector3((float)GeneralesConstantes.APP_ID_BASE);
				}
				estudianteAux.setTrttId(rs.getInt("trtt_id"));
				estudianteAux.setAsnoPrmTrbEscrito(rs.getBigDecimal("notaTrabajoTitulacion").floatValue());
				estudianteAux.setAsnoId(rs.getInt("asnoId"));
				estudianteAux.setCrrDetalle(rs.getString("carreraDetalle"));
				estudianteAux.setMcttdescripcion(rs.getString("mecanismoDetalle"));
				estudianteAux.setAsttTemaTrabajo(rs.getString("tema"));
				estudianteAux.setAsttTutor(rs.getString("tutor"));
				estudianteAux.setFcesId(rs.getInt("fcesId"));
				estudianteAux.setTrttCarreraId(rs.getInt("carreraId"));
				estudianteAux.setCnvDescripcion(rs.getString("convocatoria"));
				estudianteAux.setCnvId(rs.getInt("cnvId"));
				retorno.add(estudianteAux);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if( rs != null){
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		return retorno;
	}
	
	/**
	 * Busca la entidad asentamiento_nota, asignacion_titulacion, ficha_dcn_asg_titulacion
	 * que hayan aprobado el proceso de titulación 
	 * @param  cedulaPostulante - cédula a buscar
	 * @param  convocatoria - convocatoria a buscar
	 * @return la entidad que se busca
	 * @throws  
	 */
	@Override
	public List<EstudianteAptitudOtrosMecanismosJdbcDto> buscarPostulantesNotasTribunalXCedulaPostulante(
			String cedulaPostulante) throws AsentamientoNotaNoEncontradoException,
			AsentamientoNotaException,FichaDcnAsgTitulacionNoEncontradoException,FichaDcnAsgTitulacionException {
		List<EstudianteAptitudOtrosMecanismosJdbcDto> retorno=null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			retorno= new ArrayList<EstudianteAptitudOtrosMecanismosJdbcDto>();
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT cedula, apellido1, apellido2,nombres, id, nota1, nota2, nota3, notaTrabajoTitulacion, trtt_estado, fcdc_id,"
					+ " notaOral1, notaOral2, notaOral3, notaFinal, cedulaEst, apellido1Est,cnvId, fcdcasttTipoTribunal, fcesRecodAcademico,");
			sbSql.append(" email, apellido2Est, nombresEst, trtt_id , asnoId , carreraDetalle , mecanismoDetalle, tema , tutor, fcesId, carreraId,convocatoria"); 
			sbSql.append(" FROM (");
			sbSql.append(" SELECT prsDocente.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" cedula, ");
			sbSql.append(" prsDocente.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" apellido1, ");
			sbSql.append(" prsDocente.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(" apellido2, ");
			sbSql.append(" prsDocente.");sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(" nombres, ");
			sbSql.append(" fcdcastt.");sbSql.append(JdbcConstantes.FCDCASTT_ID);sbSql.append(" id, ");
			sbSql.append(" fcdcastt.");sbSql.append(JdbcConstantes.FCDCASTT_TIPO_TRIBUNAL);sbSql.append(" fcdcasttTipoTribunal, ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" cedulaEst, ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" apellido1Est, ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(" apellido2Est, ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(" nombresEst, ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);sbSql.append(" email, ");
			sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" trtt_id, ");
			sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" trtt_estado, ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" carreraDetalle, ");
			sbSql.append(" mctt.");sbSql.append(JdbcConstantes.MCTT_DESCRIPCION);sbSql.append(" mecanismoDetalle, ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" fcesId, ");
			sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);sbSql.append(" fcdc_id, ");
			
			sbSql.append(" CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_NOTA_PROM_ACUMULADO); sbSql.append(" IS NOT NULL THEN ");sbSql.append(JdbcConstantes.FCES_NOTA_PROM_ACUMULADO);
			sbSql.append(" ELSE ");sbSql.append(GeneralesConstantes.APP_ID_BASE);sbSql.append(" END AS ");sbSql.append("fcesRecodAcademico,");

			sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" carreraId, ");
			sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);sbSql.append(" convocatoria, ");
			sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" cnvId, ");
			
			sbSql.append(" (");
			sbSql.append(" SELECT astt.");sbSql.append(JdbcConstantes.ASTT_TEMA_TRABAJO);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASIGNACION_TITULACION);sbSql.append(" astt ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND astt.");sbSql.append(JdbcConstantes.ASTT_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ) tema,");
			
			sbSql.append(" (");
			sbSql.append(" SELECT astt.");sbSql.append(JdbcConstantes.ASTT_DIRECTOR_CIENTIFICO);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASIGNACION_TITULACION);sbSql.append(" astt ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND astt.");sbSql.append(JdbcConstantes.ASTT_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ) tutor,");
			
			sbSql.append(" (");
			sbSql.append(" SELECT asno1.");sbSql.append(JdbcConstantes.ASNO_ID);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asno1 ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND asno1.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
			
			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ) asnoId,");
			sbSql.append(" (");
			
			sbSql.append(" SELECT asno1.");sbSql.append(JdbcConstantes.ASNO_TRB_LECTOR1);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asno1 ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND asno1.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ) nota1,");
			sbSql.append(" (");
			sbSql.append(" SELECT asno1.");sbSql.append(JdbcConstantes.ASNO_TRB_LECTOR2);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asno1 ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND asno1.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ) nota2,");
			sbSql.append(" (");
			sbSql.append(" SELECT CASE WHEN asno1.");sbSql.append(JdbcConstantes.ASNO_TRB_LECTOR3);sbSql.append(" IS NOT NULL THEN ");
			sbSql.append(JdbcConstantes.ASNO_TRB_LECTOR3);sbSql.append(" ELSE ");sbSql.append(GeneralesConstantes.APP_ID_BASE);sbSql.append(" END AS ");
			sbSql.append(JdbcConstantes.ASNO_TRB_LECTOR3);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asno1 ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND asno1.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ) nota3, ");
			sbSql.append(" (");
			sbSql.append(" SELECT asno1.");sbSql.append(JdbcConstantes.ASNO_PRM_TRB_ESCRITO);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asno1 ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND asno1.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ) notaTrabajoTitulacion, ");
			
			sbSql.append(" (");
			
			sbSql.append(" SELECT CASE WHEN asno1.");sbSql.append(JdbcConstantes.ASNO_DFN_ORAL1);sbSql.append(" IS NOT NULL THEN ");
			sbSql.append(JdbcConstantes.ASNO_DFN_ORAL1);sbSql.append(" ELSE ");sbSql.append(GeneralesConstantes.APP_ID_BASE);sbSql.append(" END AS ");
			sbSql.append("notaOral1");
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asno1 ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND asno1.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ) notaOral1,");
			sbSql.append(" (");
			sbSql.append(" SELECT CASE WHEN asno1.");sbSql.append(JdbcConstantes.ASNO_DFN_ORAL2);sbSql.append(" IS NOT NULL THEN ");
			sbSql.append(JdbcConstantes.ASNO_DFN_ORAL2);sbSql.append(" ELSE ");sbSql.append(GeneralesConstantes.APP_ID_BASE);sbSql.append(" END AS ");
			sbSql.append("notaOral2");
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asno1 ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND asno1.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ) notaOral2,");
			sbSql.append(" (");
			sbSql.append(" SELECT CASE WHEN asno1.");sbSql.append(JdbcConstantes.ASNO_DFN_ORAL3);sbSql.append(" IS NOT NULL THEN ");
			sbSql.append(JdbcConstantes.ASNO_DFN_ORAL3);sbSql.append(" ELSE ");sbSql.append(GeneralesConstantes.APP_ID_BASE);sbSql.append(" END AS ");
			sbSql.append("notaOral3");
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asno1 ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND asno1.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ) notaOral3, ");
			sbSql.append(" (");
			sbSql.append(" SELECT CASE WHEN asno1.");sbSql.append(JdbcConstantes.ASNO_PRM_DFN_ORAL);sbSql.append(" IS NOT NULL THEN ");
			sbSql.append(JdbcConstantes.ASNO_PRM_DFN_ORAL);sbSql.append(" ELSE ");sbSql.append(GeneralesConstantes.APP_ID_BASE);sbSql.append(" END AS ");
			sbSql.append("notaFinal");
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asno1 ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND asno1.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ) notaFinal ");
			sbSql.append(" FROM ");
			
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASIGNACION_TITULACION);sbSql.append(" astt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DCN_ASG_TITULACION);sbSql.append(" fcdcastt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prsDocente ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION);sbSql.append(" mctt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_CARRERA);sbSql.append(" mcttcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND prtr.");sbSql.append(JdbcConstantes.PRTR_ID); sbSql.append(" =  astt.");sbSql.append(JdbcConstantes.ASTT_PRTR_ID);
			sbSql.append(" AND astt.");sbSql.append(JdbcConstantes.ASTT_ID); sbSql.append(" =  fcdcastt.");sbSql.append(JdbcConstantes.FCDCASTT_ASTT_ID);
			sbSql.append(" AND fcdc.");sbSql.append(JdbcConstantes.FCDC_ID); sbSql.append(" =  fcdcastt.");sbSql.append(JdbcConstantes.FCDCASTT_FCDC_ID);
			sbSql.append(" AND fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID); sbSql.append(" =  prsDocente.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_MCCR_ID); sbSql.append(" =  mcttcr.");sbSql.append(JdbcConstantes.MCCR_ID);
			sbSql.append(" AND mcttcr.");sbSql.append(JdbcConstantes.MCCR_MCTT_ID); sbSql.append(" =  mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
			sbSql.append(" AND mctt.");sbSql.append(JdbcConstantes.MCTT_ID); sbSql.append(" <> ");sbSql.append(MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_VALUE);
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID); sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			
			// solo cargo los estudiantes que han aprobado
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID); sbSql.append(" = cnv.");sbSql.append(JdbcConstantes.CNV_ID);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO); sbSql.append(" in ( ");
//			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_APTO_EVALUADO_EDICION_VALUE);sbSql.append(" , ");
//			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_APTO_EDICION_VALUE);sbSql.append(" , ");
//			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_DEFENSA_ORAL_EDICION_VALUE);sbSql.append(" , ");
//			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_DEFENSA_ORAL_VALIDADO_EDICION_VALUE);sbSql.append(" , ");
//			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_DEFENSA_EDICION_VALUE);sbSql.append(" , ");
//			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_DEFENSA_ORAL_EDICION_VALUE);sbSql.append(" , ");
			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_EDICION_VALUE);sbSql.append(" , ");
			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_LISTO_EDICION_ACTA_DEFENSA_ORAL_VALUE);
			sbSql.append(" )");
			
			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION); sbSql.append(" LIKE ? ");
			sbSql.append(" GROUP BY prsDocente.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION); sbSql.append(" , prsDocente.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prsDocente.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prsDocente.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , fcdcastt.");sbSql.append(JdbcConstantes.FCDCASTT_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , mctt.");sbSql.append(JdbcConstantes.MCTT_DESCRIPCION);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);
			sbSql.append(" , cnv.");sbSql.append(JdbcConstantes.CNV_ID);
			sbSql.append(" , fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" ) docente ORDER BY id,carreraDetalle, apellido1Est , apellido2Est");
					
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, "%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(cedulaPostulante)+"%"); //cargo la cédula del postulante
			rs = pstmt.executeQuery();
			while(rs.next()){
				FichaDocenteAsignacionTitulacionDto fcdcAsttAux = new FichaDocenteAsignacionTitulacionDto();
				EstudianteAptitudOtrosMecanismosJdbcDto estudianteAux = new EstudianteAptitudOtrosMecanismosJdbcDto();
				fcdcAsttAux.setPrsIdentificacion(rs.getString("cedula"));
				fcdcAsttAux.setPrsPrimerApellido(rs.getString("apellido1"));
				fcdcAsttAux.setPrsSegundoApellido(rs.getString("apellido2"));
				fcdcAsttAux.setPrsNombres(rs.getString("nombres"));
				fcdcAsttAux.setFcdcasttId(rs.getInt("id"));
				fcdcAsttAux.setFcdcasttTipotribunal(rs.getInt("fcdcasttTipoTribunal"));
				fcdcAsttAux.setFcdcId(rs.getInt("fcdc_id"));
				estudianteAux.getListaDocentesTribunal().add(fcdcAsttAux);
				rs.next();
				fcdcAsttAux = new FichaDocenteAsignacionTitulacionDto();
				fcdcAsttAux.setPrsIdentificacion(rs.getString("cedula"));
				fcdcAsttAux.setPrsPrimerApellido(rs.getString("apellido1"));
				fcdcAsttAux.setPrsSegundoApellido(rs.getString("apellido2"));
				fcdcAsttAux.setPrsNombres(rs.getString("nombres"));
				fcdcAsttAux.setPrsMailPersonal(rs.getString("email"));
				fcdcAsttAux.setFcdcasttId(rs.getInt("id"));
				fcdcAsttAux.setFcdcasttTipotribunal(rs.getInt("fcdcasttTipoTribunal"));
				fcdcAsttAux.setFcdcId(rs.getInt("fcdc_id"));
				estudianteAux.getListaDocentesTribunal().add(fcdcAsttAux);
					if(!rs.getBigDecimal("nota3").equals(new BigDecimal(GeneralesConstantes.APP_ID_BASE))){
						rs.next();
						fcdcAsttAux = new FichaDocenteAsignacionTitulacionDto();
						fcdcAsttAux.setPrsIdentificacion(rs.getString("cedula"));
						fcdcAsttAux.setPrsPrimerApellido(rs.getString("apellido1"));
						fcdcAsttAux.setPrsSegundoApellido(rs.getString("apellido2"));
						fcdcAsttAux.setPrsNombres(rs.getString("nombres"));
						fcdcAsttAux.setFcdcasttId(rs.getInt("id"));
						fcdcAsttAux.setFcdcasttTipotribunal(rs.getInt("fcdcasttTipoTribunal"));
						fcdcAsttAux.setFcdcId(rs.getInt("fcdc_id"));
						estudianteAux.getListaDocentesTribunal().add(fcdcAsttAux);	
					}
				if(!rs.getBigDecimal("notaOral1").equals(new BigDecimal(GeneralesConstantes.APP_ID_BASE))){
					rs.next();
					fcdcAsttAux = new FichaDocenteAsignacionTitulacionDto();
					fcdcAsttAux.setPrsIdentificacion(rs.getString("cedula"));
					fcdcAsttAux.setPrsPrimerApellido(rs.getString("apellido1"));
					fcdcAsttAux.setPrsSegundoApellido(rs.getString("apellido2"));
					fcdcAsttAux.setPrsNombres(rs.getString("nombres"));
					fcdcAsttAux.setFcdcasttId(rs.getInt("id"));
					fcdcAsttAux.setFcdcasttTipotribunal(rs.getInt("fcdcasttTipoTribunal"));
					fcdcAsttAux.setFcdcId(rs.getInt("fcdc_id"));
					estudianteAux.getListaDocentesTribunal().add(fcdcAsttAux);
					rs.next();
					fcdcAsttAux = new FichaDocenteAsignacionTitulacionDto();
					fcdcAsttAux.setPrsIdentificacion(rs.getString("cedula"));
					fcdcAsttAux.setPrsPrimerApellido(rs.getString("apellido1"));
					fcdcAsttAux.setPrsSegundoApellido(rs.getString("apellido2"));
					fcdcAsttAux.setPrsNombres(rs.getString("nombres"));
					fcdcAsttAux.setPrsMailPersonal(rs.getString("email"));
					fcdcAsttAux.setFcdcasttId(rs.getInt("id"));
					fcdcAsttAux.setFcdcasttTipotribunal(rs.getInt("fcdcasttTipoTribunal"));
					fcdcAsttAux.setFcdcId(rs.getInt("fcdc_id"));
					estudianteAux.getListaDocentesTribunal().add(fcdcAsttAux);
						if(!rs.getBigDecimal("notaOral3").equals(new BigDecimal(GeneralesConstantes.APP_ID_BASE))){
							rs.next();
							fcdcAsttAux = new FichaDocenteAsignacionTitulacionDto();
							fcdcAsttAux.setPrsIdentificacion(rs.getString("cedula"));
							fcdcAsttAux.setPrsPrimerApellido(rs.getString("apellido1"));
							fcdcAsttAux.setPrsSegundoApellido(rs.getString("apellido2"));
							fcdcAsttAux.setPrsNombres(rs.getString("nombres"));
							fcdcAsttAux.setFcdcasttId(rs.getInt("id"));
							fcdcAsttAux.setFcdcasttTipotribunal(rs.getInt("fcdcasttTipoTribunal"));
							fcdcAsttAux.setFcdcId(rs.getInt("fcdc_id"));
							estudianteAux.getListaDocentesTribunal().add(fcdcAsttAux);
						}
				} 
				estudianteAux.setPrsIdentificacion(rs.getString("cedulaEst"));
				estudianteAux.setPrsPrimerApellido(rs.getString("apellido1Est"));
				estudianteAux.setPrsSegundoApellido(rs.getString("apellido2Est"));
				estudianteAux.setPrsNombres(rs.getString("nombresEst"));
				estudianteAux.setAsnoTrbLector1(rs.getBigDecimal("nota1").floatValue());
				estudianteAux.setAsnoTrbLector2(rs.getBigDecimal("nota2").floatValue());
				try {
					estudianteAux.setAsnoTrbLector3(rs.getBigDecimal("nota3").floatValue());
				} catch (Exception e) {
					estudianteAux.setAsnoTrbLector3((float)GeneralesConstantes.APP_ID_BASE);
				}
				try {
					estudianteAux.setAsnoDfnOral1(rs.getBigDecimal("notaOral1"));
					estudianteAux.setAsnoDfnOral2(rs.getBigDecimal("notaOral2"));
					try {
						
						estudianteAux.setAsnoDfnOral3(rs.getBigDecimal("notaOral3"));
					} catch (Exception e) {
						estudianteAux.setAsnoDfnOral3(new BigDecimal (GeneralesConstantes.APP_ID_BASE));
					}
				} catch (Exception e) {
				}
				try {
					estudianteAux.setAsnoPrmDfnOral(rs.getBigDecimal("notaFinal"));
				} catch (Exception e) {
				}
				estudianteAux.setFcesNotaPromAcumulado(rs.getBigDecimal("fcesRecodAcademico"));
				estudianteAux.setTrttId(rs.getInt("trtt_id"));
				estudianteAux.setAsnoPrmTrbEscrito(rs.getBigDecimal("notaTrabajoTitulacion").floatValue());
				try {
					estudianteAux.setAsnoPrmDfnOral(rs.getBigDecimal("notaFinal"));
				} catch (Exception e) {
				}
				estudianteAux.setAsnoId(rs.getInt("asnoId"));
				estudianteAux.setCrrDetalle(rs.getString("carreraDetalle"));
				estudianteAux.setMcttdescripcion(rs.getString("mecanismoDetalle"));
				estudianteAux.setAsttTemaTrabajo(rs.getString("tema"));
				estudianteAux.setAsttTutor(rs.getString("tutor"));
				estudianteAux.setFcesId(rs.getInt("fcesId"));
				estudianteAux.setTrttCarreraId(rs.getInt("carreraId"));
				estudianteAux.setTrttEstadoProceso(rs.getInt("trtt_estado"));
				estudianteAux.setCnvDescripcion(rs.getString("convocatoria"));
				estudianteAux.setCnvId(rs.getInt("cnvId"));
				retorno.add(estudianteAux);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			try {
				if( rs != null){
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		return retorno;
	}
	
	
	/**
	 * Busca la entidad asentamiento_nota, asignacion_titulacion, ficha_dcn_asg_titulacion
	 * que hayan aprobado el proceso de titulación 
	 * @param  cedulaPostulante - cédula a buscar
	 * @param  convocatoria - convocatoria a buscar
	 * @return la entidad que se busca
	 * @throws  
	 */
	public List<EstudianteAptitudOtrosMecanismosJdbcDto> buscarPostulantesNotasTribunalXCedulaPostulanteEdicion(
			String cedulaPostulante) throws AsentamientoNotaNoEncontradoException,
			AsentamientoNotaException,FichaDcnAsgTitulacionNoEncontradoException,FichaDcnAsgTitulacionException {
		List<EstudianteAptitudOtrosMecanismosJdbcDto> retorno=null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			retorno= new ArrayList<EstudianteAptitudOtrosMecanismosJdbcDto>();
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT cedula, apellido1, apellido2,nombres, id, nota1, nota2, nota3, notaTrabajoTitulacion, trtt_estado, fcdc_id,"
					+ " notaOral1, notaOral2, notaOral3, notaFinal, cedulaEst, apellido1Est,cnvId, fcdcasttTipoTribunal, fcesRecodAcademico,");
			sbSql.append(" email, apellido2Est, nombresEst, trtt_id , asnoId , carreraDetalle , mecanismoDetalle, tema , tutor, fcesId, carreraId,convocatoria"); 
			sbSql.append(" FROM (");
			sbSql.append(" SELECT prsDocente.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" cedula, ");
			sbSql.append(" prsDocente.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" apellido1, ");
			sbSql.append(" prsDocente.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(" apellido2, ");
			sbSql.append(" prsDocente.");sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(" nombres, ");
			sbSql.append(" fcdcastt.");sbSql.append(JdbcConstantes.FCDCASTT_ID);sbSql.append(" id, ");
			sbSql.append(" fcdcastt.");sbSql.append(JdbcConstantes.FCDCASTT_TIPO_TRIBUNAL);sbSql.append(" fcdcasttTipoTribunal, ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" cedulaEst, ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" apellido1Est, ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(" apellido2Est, ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(" nombresEst, ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);sbSql.append(" email, ");
			sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" trtt_id, ");
			sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" trtt_estado, ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" carreraDetalle, ");
			sbSql.append(" mctt.");sbSql.append(JdbcConstantes.MCTT_DESCRIPCION);sbSql.append(" mecanismoDetalle, ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" fcesId, ");
			sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);sbSql.append(" fcdc_id, ");
			
			sbSql.append(" CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_NOTA_PROM_ACUMULADO); sbSql.append(" IS NOT NULL THEN ");sbSql.append(JdbcConstantes.FCES_NOTA_PROM_ACUMULADO);
			sbSql.append(" ELSE ");sbSql.append(GeneralesConstantes.APP_ID_BASE);sbSql.append(" END AS ");sbSql.append("fcesRecodAcademico,");

			sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" carreraId, ");
			sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);sbSql.append(" convocatoria, ");
			sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" cnvId, ");
			
			sbSql.append(" (");
			sbSql.append(" SELECT astt.");sbSql.append(JdbcConstantes.ASTT_TEMA_TRABAJO);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASIGNACION_TITULACION);sbSql.append(" astt ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND astt.");sbSql.append(JdbcConstantes.ASTT_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ) tema,");
			
			sbSql.append(" (");
			sbSql.append(" SELECT astt.");sbSql.append(JdbcConstantes.ASTT_DIRECTOR_CIENTIFICO);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASIGNACION_TITULACION);sbSql.append(" astt ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND astt.");sbSql.append(JdbcConstantes.ASTT_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ) tutor,");
			
			sbSql.append(" (");
			sbSql.append(" SELECT asno1.");sbSql.append(JdbcConstantes.ASNO_ID);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asno1 ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND asno1.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
			
			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ) asnoId,");
			sbSql.append(" (");
			
			sbSql.append(" SELECT asno1.");sbSql.append(JdbcConstantes.ASNO_TRB_LECTOR1);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asno1 ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND asno1.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ) nota1,");
			sbSql.append(" (");
			sbSql.append(" SELECT asno1.");sbSql.append(JdbcConstantes.ASNO_TRB_LECTOR2);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asno1 ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND asno1.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ) nota2,");
			sbSql.append(" (");
			sbSql.append(" SELECT CASE WHEN asno1.");sbSql.append(JdbcConstantes.ASNO_TRB_LECTOR3);sbSql.append(" IS NOT NULL THEN ");
			sbSql.append(JdbcConstantes.ASNO_TRB_LECTOR3);sbSql.append(" ELSE ");sbSql.append(GeneralesConstantes.APP_ID_BASE);sbSql.append(" END AS ");
			sbSql.append(JdbcConstantes.ASNO_TRB_LECTOR3);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asno1 ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND asno1.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ) nota3, ");
			sbSql.append(" (");
			sbSql.append(" SELECT asno1.");sbSql.append(JdbcConstantes.ASNO_PRM_TRB_ESCRITO);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asno1 ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND asno1.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ) notaTrabajoTitulacion, ");
			
			sbSql.append(" (");
			
			sbSql.append(" SELECT CASE WHEN asno1.");sbSql.append(JdbcConstantes.ASNO_DFN_ORAL1);sbSql.append(" IS NOT NULL THEN ");
			sbSql.append(JdbcConstantes.ASNO_DFN_ORAL1);sbSql.append(" ELSE ");sbSql.append(GeneralesConstantes.APP_ID_BASE);sbSql.append(" END AS ");
			sbSql.append("notaOral1");
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asno1 ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND asno1.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ) notaOral1,");
			sbSql.append(" (");
			sbSql.append(" SELECT CASE WHEN asno1.");sbSql.append(JdbcConstantes.ASNO_DFN_ORAL2);sbSql.append(" IS NOT NULL THEN ");
			sbSql.append(JdbcConstantes.ASNO_DFN_ORAL2);sbSql.append(" ELSE ");sbSql.append(GeneralesConstantes.APP_ID_BASE);sbSql.append(" END AS ");
			sbSql.append("notaOral2");
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asno1 ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND asno1.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ) notaOral2,");
			sbSql.append(" (");
			sbSql.append(" SELECT CASE WHEN asno1.");sbSql.append(JdbcConstantes.ASNO_DFN_ORAL3);sbSql.append(" IS NOT NULL THEN ");
			sbSql.append(JdbcConstantes.ASNO_DFN_ORAL3);sbSql.append(" ELSE ");sbSql.append(GeneralesConstantes.APP_ID_BASE);sbSql.append(" END AS ");
			sbSql.append("notaOral3");
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asno1 ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND asno1.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ) notaOral3, ");
			sbSql.append(" (");
			sbSql.append(" SELECT CASE WHEN asno1.");sbSql.append(JdbcConstantes.ASNO_PRM_DFN_ORAL);sbSql.append(" IS NOT NULL THEN ");
			sbSql.append(JdbcConstantes.ASNO_PRM_DFN_ORAL);sbSql.append(" ELSE ");sbSql.append(GeneralesConstantes.APP_ID_BASE);sbSql.append(" END AS ");
			sbSql.append("notaFinal");
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asno1 ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND asno1.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ) notaFinal ");
			sbSql.append(" FROM ");
			
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASIGNACION_TITULACION);sbSql.append(" astt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DCN_ASG_TITULACION);sbSql.append(" fcdcastt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prsDocente ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION);sbSql.append(" mctt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_CARRERA);sbSql.append(" mcttcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ");
			sbSql.append(" WHERE ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND prtr.");sbSql.append(JdbcConstantes.PRTR_ID); sbSql.append(" =  astt.");sbSql.append(JdbcConstantes.ASTT_PRTR_ID);
			sbSql.append(" AND astt.");sbSql.append(JdbcConstantes.ASTT_ID); sbSql.append(" =  fcdcastt.");sbSql.append(JdbcConstantes.FCDCASTT_ASTT_ID);
			sbSql.append(" AND fcdc.");sbSql.append(JdbcConstantes.FCDC_ID); sbSql.append(" =  fcdcastt.");sbSql.append(JdbcConstantes.FCDCASTT_FCDC_ID);
			sbSql.append(" AND fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID); sbSql.append(" =  prsDocente.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_MCCR_ID); sbSql.append(" =  mcttcr.");sbSql.append(JdbcConstantes.MCCR_ID);
			sbSql.append(" AND mcttcr.");sbSql.append(JdbcConstantes.MCCR_MCTT_ID); sbSql.append(" =  mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
			sbSql.append(" AND mctt.");sbSql.append(JdbcConstantes.MCTT_ID); sbSql.append(" <> ");sbSql.append(MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_VALUE);
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID); sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			// solo cargo los estudiantes que han aprobado
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID); sbSql.append(" = cnv.");sbSql.append(JdbcConstantes.CNV_ID);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO); sbSql.append(" IN ( ");
			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_APTO_VALUE);sbSql.append(" , ");
			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_APTO_EVALUADO_VALUE);sbSql.append(" , ");
			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_DEFENSA_ORAL_VALUE);sbSql.append(" , ");
			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_DEFENSA_ORAL_VALUE);sbSql.append(" , ");
			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_DEFENSA_VALUE);sbSql.append(" , ");
			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_DEFENSA_ORAL_VALIDADO_VALUE);sbSql.append(" , ");
			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_VALUE);sbSql.append(" ) ");
//			1500788656
			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION); sbSql.append(" LIKE ? ");
			sbSql.append(" GROUP BY prsDocente.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION); sbSql.append(" , prsDocente.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prsDocente.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prsDocente.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , fcdcastt.");sbSql.append(JdbcConstantes.FCDCASTT_ID);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , mctt.");sbSql.append(JdbcConstantes.MCTT_DESCRIPCION);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);
			sbSql.append(" , cnv.");sbSql.append(JdbcConstantes.CNV_ID);
			sbSql.append(" , fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" ) docente ORDER BY id,carreraDetalle, apellido1Est , apellido2Est");
					
//			System.out.println(sbSql);
//			System.out.println(cedulaPostulante);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, "%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(cedulaPostulante)+"%"); //cargo la cédula del postulante
			rs = pstmt.executeQuery();
			while(rs.next()){
				FichaDocenteAsignacionTitulacionDto fcdcAsttAux = new FichaDocenteAsignacionTitulacionDto();
				EstudianteAptitudOtrosMecanismosJdbcDto estudianteAux = new EstudianteAptitudOtrosMecanismosJdbcDto();
				fcdcAsttAux.setPrsIdentificacion(rs.getString("cedula"));
				fcdcAsttAux.setPrsPrimerApellido(rs.getString("apellido1"));
				fcdcAsttAux.setPrsSegundoApellido(rs.getString("apellido2"));
				fcdcAsttAux.setPrsNombres(rs.getString("nombres"));
				fcdcAsttAux.setFcdcasttId(rs.getInt("id"));
				fcdcAsttAux.setFcdcasttTipotribunal(rs.getInt("fcdcasttTipoTribunal"));
				fcdcAsttAux.setFcdcId(rs.getInt("fcdc_id"));
				estudianteAux.getListaDocentesTribunal().add(fcdcAsttAux);
				rs.next();
				fcdcAsttAux = new FichaDocenteAsignacionTitulacionDto();
				fcdcAsttAux.setPrsIdentificacion(rs.getString("cedula"));
				fcdcAsttAux.setPrsPrimerApellido(rs.getString("apellido1"));
				fcdcAsttAux.setPrsSegundoApellido(rs.getString("apellido2"));
				fcdcAsttAux.setPrsNombres(rs.getString("nombres"));
				fcdcAsttAux.setPrsMailPersonal(rs.getString("email"));
				fcdcAsttAux.setFcdcasttId(rs.getInt("id"));
				fcdcAsttAux.setFcdcasttTipotribunal(rs.getInt("fcdcasttTipoTribunal"));
				fcdcAsttAux.setFcdcId(rs.getInt("fcdc_id"));
				estudianteAux.getListaDocentesTribunal().add(fcdcAsttAux);
					if(!rs.getBigDecimal("nota3").equals(new BigDecimal(GeneralesConstantes.APP_ID_BASE))){
						rs.next();
						fcdcAsttAux = new FichaDocenteAsignacionTitulacionDto();
						fcdcAsttAux.setPrsIdentificacion(rs.getString("cedula"));
						fcdcAsttAux.setPrsPrimerApellido(rs.getString("apellido1"));
						fcdcAsttAux.setPrsSegundoApellido(rs.getString("apellido2"));
						fcdcAsttAux.setPrsNombres(rs.getString("nombres"));
						fcdcAsttAux.setFcdcasttId(rs.getInt("id"));
						fcdcAsttAux.setFcdcasttTipotribunal(rs.getInt("fcdcasttTipoTribunal"));
						fcdcAsttAux.setFcdcId(rs.getInt("fcdc_id"));
						estudianteAux.getListaDocentesTribunal().add(fcdcAsttAux);	
					}
				if(!rs.getBigDecimal("notaOral1").equals(new BigDecimal(GeneralesConstantes.APP_ID_BASE))){
					rs.next();
					fcdcAsttAux = new FichaDocenteAsignacionTitulacionDto();
					fcdcAsttAux.setPrsIdentificacion(rs.getString("cedula"));
					fcdcAsttAux.setPrsPrimerApellido(rs.getString("apellido1"));
					fcdcAsttAux.setPrsSegundoApellido(rs.getString("apellido2"));
					fcdcAsttAux.setPrsNombres(rs.getString("nombres"));
					fcdcAsttAux.setFcdcasttId(rs.getInt("id"));
					fcdcAsttAux.setFcdcasttTipotribunal(rs.getInt("fcdcasttTipoTribunal"));
					fcdcAsttAux.setFcdcId(rs.getInt("fcdc_id"));
					estudianteAux.getListaDocentesTribunal().add(fcdcAsttAux);
					rs.next();
					fcdcAsttAux = new FichaDocenteAsignacionTitulacionDto();
					fcdcAsttAux.setPrsIdentificacion(rs.getString("cedula"));
					fcdcAsttAux.setPrsPrimerApellido(rs.getString("apellido1"));
					fcdcAsttAux.setPrsSegundoApellido(rs.getString("apellido2"));
					fcdcAsttAux.setPrsNombres(rs.getString("nombres"));
					fcdcAsttAux.setPrsMailPersonal(rs.getString("email"));
					fcdcAsttAux.setFcdcasttId(rs.getInt("id"));
					fcdcAsttAux.setFcdcasttTipotribunal(rs.getInt("fcdcasttTipoTribunal"));
					fcdcAsttAux.setFcdcId(rs.getInt("fcdc_id"));
					estudianteAux.getListaDocentesTribunal().add(fcdcAsttAux);
						if(!rs.getBigDecimal("notaOral3").equals(new BigDecimal(GeneralesConstantes.APP_ID_BASE))){
							rs.next();
							fcdcAsttAux = new FichaDocenteAsignacionTitulacionDto();
							fcdcAsttAux.setPrsIdentificacion(rs.getString("cedula"));
							fcdcAsttAux.setPrsPrimerApellido(rs.getString("apellido1"));
							fcdcAsttAux.setPrsSegundoApellido(rs.getString("apellido2"));
							fcdcAsttAux.setPrsNombres(rs.getString("nombres"));
							fcdcAsttAux.setFcdcasttId(rs.getInt("id"));
							fcdcAsttAux.setFcdcasttTipotribunal(rs.getInt("fcdcasttTipoTribunal"));
							fcdcAsttAux.setFcdcId(rs.getInt("fcdc_id"));
							estudianteAux.getListaDocentesTribunal().add(fcdcAsttAux);
						}
				} 
				estudianteAux.setPrsIdentificacion(rs.getString("cedulaEst"));
				estudianteAux.setPrsPrimerApellido(rs.getString("apellido1Est"));
				estudianteAux.setPrsSegundoApellido(rs.getString("apellido2Est"));
				estudianteAux.setPrsNombres(rs.getString("nombresEst"));
				estudianteAux.setAsnoTrbLector1(rs.getBigDecimal("nota1").floatValue());
				estudianteAux.setAsnoTrbLector2(rs.getBigDecimal("nota2").floatValue());
				try {
					estudianteAux.setAsnoTrbLector3(rs.getBigDecimal("nota3").floatValue());
				} catch (Exception e) {
					estudianteAux.setAsnoTrbLector3((float)GeneralesConstantes.APP_ID_BASE);
				}
				try {
					estudianteAux.setAsnoDfnOral1(rs.getBigDecimal("notaOral1"));
					estudianteAux.setAsnoDfnOral2(rs.getBigDecimal("notaOral2"));
					try {
						
						estudianteAux.setAsnoDfnOral3(rs.getBigDecimal("notaOral3"));
					} catch (Exception e) {
						estudianteAux.setAsnoDfnOral3(new BigDecimal(GeneralesConstantes.APP_ID_BASE));
					}
				} catch (Exception e) {
				}
				try {
					estudianteAux.setAsnoPrmDfnOral(rs.getBigDecimal("notaFinal"));
				} catch (Exception e) {
				}
				estudianteAux.setFcesNotaPromAcumulado(rs.getBigDecimal("fcesRecodAcademico"));
				estudianteAux.setTrttId(rs.getInt("trtt_id"));
				estudianteAux.setAsnoPrmTrbEscrito(rs.getBigDecimal("notaTrabajoTitulacion").floatValue());
				try {
					estudianteAux.setAsnoPrmDfnOral(rs.getBigDecimal("notaFinal"));
				} catch (Exception e) {
				}
				estudianteAux.setAsnoId(rs.getInt("asnoId"));
				estudianteAux.setCrrDetalle(rs.getString("carreraDetalle"));
				estudianteAux.setMcttdescripcion(rs.getString("mecanismoDetalle"));
				estudianteAux.setAsttTemaTrabajo(rs.getString("tema"));
				estudianteAux.setAsttTutor(rs.getString("tutor"));
				estudianteAux.setFcesId(rs.getInt("fcesId"));
				estudianteAux.setTrttCarreraId(rs.getInt("carreraId"));
				estudianteAux.setTrttEstadoProceso(rs.getInt("trtt_estado"));
				estudianteAux.setCnvDescripcion(rs.getString("convocatoria"));
				estudianteAux.setCnvId(rs.getInt("cnvId"));
				retorno.add(estudianteAux);
			}
		} catch (Exception e) {
		} finally {
			try {
				if( rs != null){
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		return retorno;
	}
	
	/**
	 * Busca la entidad asentamiento_nota, asignacion_titulacion, ficha_dcn_asg_titulacion
	 * que hayan aprobado el proceso de titulación y emitido el acta de grado 
	 * @param  cedulaPostulante - cédula a buscar
	 * @param  convocatoria - convocatoria a buscar
	 * @return la entidad que se busca
	 * @throws  
	 */
	@Override
	public EstudianteEmisionActaJdbcDto buscarGraduadosOtrosMecanismos(
			String cedulaPostulante, 
			Integer convocatoria, Integer carrera) throws AsentamientoNotaNoEncontradoException,
			AsentamientoNotaException,FichaDcnAsgTitulacionNoEncontradoException,FichaDcnAsgTitulacionException {
		EstudianteEmisionActaJdbcDto estudianteAux=null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			estudianteAux= new EstudianteEmisionActaJdbcDto();
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT prsDocente.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" idDocente, ");
			sbSql.append(" prsDocente.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" apellido1, ");
			sbSql.append(" prsDocente.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(" apellido2, ");
			sbSql.append(" prsDocente.");sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(" nombresD, ");
			sbSql.append(" prsDocente.");sbSql.append(JdbcConstantes.PRS_SEXO);sbSql.append(" sexoD, ");
			sbSql.append(" fcdcastt.");sbSql.append(JdbcConstantes.FCDCASTT_ID);sbSql.append(" , ");
			sbSql.append(" fcdcastt.");sbSql.append(JdbcConstantes.FCDCASTT_TIPO_TRIBUNAL);sbSql.append(" , ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" idEstudiante, ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" apellido1E, ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(" apellido2E, ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(" nombresE, ");
			
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_SEXO);sbSql.append(" sexoE, ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_FECHA_NACIMIENTO);sbSql.append(" nacimientoE, ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ETN_ID);sbSql.append(" etnE, ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_UBC_ID);sbSql.append(" ubcId, ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);sbSql.append(" mail, ");
			
			sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" , ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" , ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" , ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);sbSql.append(" , ");
			sbSql.append(" mctt.");sbSql.append(JdbcConstantes.MCTT_DESCRIPCION);sbSql.append(" , ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);sbSql.append(" , ");

			sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" , ");

			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FECHA_INICIO);sbSql.append(" , ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);sbSql.append(" , ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREVIOS);sbSql.append(" , ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS);sbSql.append(" , ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_TIPO_COLEGIO);sbSql.append(" , ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_TIPO_DURAC_REC);sbSql.append(" , ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_TIEMPO_ESTUD_REC);sbSql.append(" , ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_INAC_ID_INST_EST_PREVIOS);sbSql.append(" , ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_TITULO_BACHILLER);sbSql.append(" , ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_LINK_TESIS);sbSql.append(" , ");
			sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" , ");
			
			
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_NUM_ACTA_GRADO);sbSql.append(" , ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_NOTA_TRAB_TITULACION);sbSql.append(" , ");
			
			
			
			sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" , ");
			
			
			sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);sbSql.append(" , ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_HORA_ACTA_GRADO);sbSql.append(" , ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FECHA_ACTA_GRADO);sbSql.append(" , ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_NOTA_PROM_ACUMULADO);sbSql.append(" , ");
			sbSql.append(" mdl.");sbSql.append(JdbcConstantes.MDL_DESCRIPCION);sbSql.append(" , ");
			sbSql.append(" ttl.");sbSql.append(JdbcConstantes.TTL_DESCRIPCION);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" , ");
			sbSql.append(" astt.");sbSql.append(JdbcConstantes.ASTT_TEMA_TRABAJO);sbSql.append(" , ");
			sbSql.append(" astt.");sbSql.append(JdbcConstantes.ASTT_DIRECTOR_CIENTIFICO);sbSql.append(" , ");
			sbSql.append(" etn.");sbSql.append(JdbcConstantes.ETN_ID);sbSql.append(" etniaE, ");
			
			sbSql.append("  CASE WHEN ubc.");sbSql.append(JdbcConstantes.UBC_GENTILICIO); sbSql.append(" IS NULL THEN ");sbSql.append("'N/A'");
			sbSql.append(" ELSE ubc.");sbSql.append(JdbcConstantes.UBC_GENTILICIO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.UBC_GENTILICIO);
			
			sbSql.append(" from persona prs, ficha_estudiante fces, tramite_titulo trtt, proceso_tramite prtr, asignacion_titulacion astt," 
			+" ficha_dcn_asg_titulacion fcdcastt, ficha_docente fcdc, persona prsDocente, Ubicacion ubc, configuracion_carrera cncr, modalidad mdl,titulo ttl, carrera crr, facultad fcl, mecanismo_carrera mccr, mecanismo_titulacion mctt,convocatoria cnv, etnia etn"
			+" where prs.prs_id=fces.prs_id and fces.trtt_id = trtt.trtt_id and prtr.trtt_id=trtt.trtt_id and prtr.prtr_id=astt.prtr_id" 
			+" and astt.astt_id=fcdcastt.astt_id and fcdcastt.fcdc_id=fcdc.fcdc_id and fcdc.prs_id=prsDocente.prs_id and prs.ubc_id=ubc.ubc_id and prs.etn_id=etn.etn_id and crr.crr_id=trtt.trtt_carrera_id and crr.fcl_id=fcl.fcl_id and fces.cncr_id=cncr.cncr_id and cncr.ttl_id=ttl.ttl_id and cncr.mdl_id=mdl.mdl_id and fces.mccr_id=mccr.mccr_id and mctt.mctt_id=mccr.mctt_id and trtt.cnv_id=cnv.cnv_id "
			+" and prs.prs_identificacion ='"+GeneralesUtilidades.eliminarEspaciosEnBlanco(cedulaPostulante)+"' and fcdcastt_tipo_tribunal=1 order by fcdcastt.fcdcastt_id,fcdcastt.fcdcastt_tipo_tribunal");
//System.out.println(sbSql);
//			sbSql.append(" (");
//			sbSql.append(" SELECT astt.");sbSql.append(JdbcConstantes.ASTT_TEMA_TRABAJO);
//			sbSql.append(" FROM ");
//			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASIGNACION_TITULACION);sbSql.append(" astt ");
//			sbSql.append(" WHERE ");
//			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
//			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
//			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
//			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
//			sbSql.append(" AND astt.");sbSql.append(JdbcConstantes.ASTT_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
//			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
//			sbSql.append(" ) tema,");
//			
//			sbSql.append(" (");
//			sbSql.append(" SELECT astt.");sbSql.append(JdbcConstantes.ASTT_DIRECTOR_CIENTIFICO);
//			sbSql.append(" FROM ");
//			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASIGNACION_TITULACION);sbSql.append(" astt ");
//			sbSql.append(" WHERE ");
//			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
//			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
//			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
//			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
//			sbSql.append(" AND astt.");sbSql.append(JdbcConstantes.ASTT_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
//			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
//			sbSql.append(" ) tutor,");
//			
//			sbSql.append(" (");
//			sbSql.append(" SELECT asno1.");sbSql.append(JdbcConstantes.ASNO_ID);
//			sbSql.append(" FROM ");
//			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asno1 ");
//			sbSql.append(" WHERE ");
//			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
//			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
//			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
//			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
//			sbSql.append(" AND asno1.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
//			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
//			sbSql.append(" ) asnoId,");
//			sbSql.append(" (");
//			
//			sbSql.append(" SELECT asno1.");sbSql.append(JdbcConstantes.ASNO_PRM_DFN_ORAL);
//			sbSql.append(" FROM ");
//			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asno1 ");
//			sbSql.append(" WHERE ");
//			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
//			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
//			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
//			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
//			sbSql.append(" AND asno1.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
//			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
//			sbSql.append(" ) notaDefensa,");
//			sbSql.append(" (");
//			
//			
//			sbSql.append(" SELECT asno1.");sbSql.append(JdbcConstantes.ASNO_DFN_ORAL1);
//			sbSql.append(" FROM ");
//			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asno1 ");
//			sbSql.append(" WHERE ");
//			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
//			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
//			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
//			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
//			sbSql.append(" AND asno1.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
//			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
//			sbSql.append(" ) nota1,");
//			sbSql.append(" (");
//			sbSql.append(" SELECT asno1.");sbSql.append(JdbcConstantes.ASNO_DFN_ORAL2);
//			sbSql.append(" FROM ");
//			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asno1 ");
//			sbSql.append(" WHERE ");
//			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
//			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
//			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
//			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
//			sbSql.append(" AND asno1.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
//			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
//			sbSql.append(" ) nota2,");
//			sbSql.append(" (");
//			sbSql.append(" SELECT ");
//			
//			sbSql.append("  CASE WHEN asno1.");sbSql.append(JdbcConstantes.ASNO_DFN_ORAL3); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
//			sbSql.append(" ELSE asno1.");sbSql.append(JdbcConstantes.ASNO_DFN_ORAL3); 
//			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.ASNO_DFN_ORAL3);
//			
//			sbSql.append(" FROM ");
//			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asno1 ");
//			sbSql.append(" WHERE ");
//			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
//			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
//			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
//			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
//			sbSql.append(" AND asno1.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
//			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
//			sbSql.append(" ) nota3, ");
//			sbSql.append(" (");
//			sbSql.append(" SELECT asno1.");sbSql.append(JdbcConstantes.ASNO_PRM_TRB_ESCRITO);
//			sbSql.append(" FROM ");
//			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1 ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces1 ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt1 ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr1 ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asno1 ");
//			sbSql.append(" WHERE ");
//			sbSql.append(" fces1.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs1.");sbSql.append(JdbcConstantes.PRS_ID);
//			sbSql.append(" AND fces1.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt1.");sbSql.append(JdbcConstantes.TRTT_ID);
//			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
//			sbSql.append(" AND trtt1.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
//			sbSql.append(" AND asno1.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID); sbSql.append(" =  prtr1.");sbSql.append(JdbcConstantes.PRTR_ID);
//			sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
//			sbSql.append(" ) notaTrabajoTitulacion ");
//			sbSql.append(" FROM ");
//			
//			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASIGNACION_TITULACION);sbSql.append(" astt ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DCN_ASG_TITULACION);sbSql.append(" fcdcastt ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prsDocente ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION);sbSql.append(" mctt ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_CARRERA);sbSql.append(" mcttcr ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MODALIDAD);sbSql.append(" mdl ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TITULO);sbSql.append(" ttl ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubc ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubcP ");
//			sbSql.append(" WHERE ");
//			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID); sbSql.append(" =  prs.");sbSql.append(JdbcConstantes.PRS_ID);
//			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID); sbSql.append(" =  trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
//			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
//			sbSql.append(" AND prtr.");sbSql.append(JdbcConstantes.PRTR_ID); sbSql.append(" =  astt.");sbSql.append(JdbcConstantes.ASTT_PRTR_ID);
//			sbSql.append(" AND astt.");sbSql.append(JdbcConstantes.ASTT_ID); sbSql.append(" =  fcdcastt.");sbSql.append(JdbcConstantes.FCDCASTT_ASTT_ID);
//			sbSql.append(" AND fcdc.");sbSql.append(JdbcConstantes.FCDC_ID); sbSql.append(" =  fcdcastt.");sbSql.append(JdbcConstantes.FCDCASTT_FCDC_ID);
//			sbSql.append(" AND fcdcastt.");sbSql.append(JdbcConstantes.FCDCASTT_TIPO_TRIBUNAL); sbSql.append(" = ");sbSql.append(FichaDocenteAsignaciontitulacionConstantes.ORAL_VALUE);
//			sbSql.append(" AND fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID); sbSql.append(" =  prsDocente.");sbSql.append(JdbcConstantes.PRS_ID);
//			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_MCCR_ID); sbSql.append(" =  mcttcr.");sbSql.append(JdbcConstantes.MCCR_ID);
//			sbSql.append(" AND mcttcr.");sbSql.append(JdbcConstantes.MCCR_MCTT_ID); sbSql.append(" =  mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
//			sbSql.append(" AND mctt.");sbSql.append(JdbcConstantes.MCTT_ID); sbSql.append(" <> ");sbSql.append(MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_VALUE);
//			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID); sbSql.append(" =  ");sbSql.append(carrera);
//			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID); sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
//			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID); sbSql.append(" = fcl.");sbSql.append(JdbcConstantes.FCL_ID);
//			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_CNCR_ID); sbSql.append(" = cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
//			sbSql.append(" AND cncr.");sbSql.append(JdbcConstantes.CNCR_MDL_ID); sbSql.append(" = mdl.");sbSql.append(JdbcConstantes.MDL_ID);
//			sbSql.append(" AND cncr.");sbSql.append(JdbcConstantes.CNCR_TTL_ID); sbSql.append(" = ttl.");sbSql.append(JdbcConstantes.TTL_ID);
//			sbSql.append(" AND cncr.");sbSql.append(JdbcConstantes.CNCR_UBC_ID); sbSql.append(" = ubc.");sbSql.append(JdbcConstantes.UBC_ID);
//			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID); sbSql.append(" = cnv.");sbSql.append(JdbcConstantes.CNV_ID);
//			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_UBC_ID); sbSql.append(" = ubcP.");sbSql.append(JdbcConstantes.UBC_ID);
//			// solo cargo los estudiantes que han aprobado
//			sbSql.append(" AND (trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO); sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_DEFENSA_VALUE);
//			sbSql.append(" OR trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO); sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_DEFENSA_ORAL_VALUE);
//			sbSql.append(" OR trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO); sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_VALUE);
//			sbSql.append(" ) ");
//			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
//			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION); sbSql.append(" LIKE ? ");
//			sbSql.append(" ORDER BY id ");
//			sbSql.append(JdbcConstantes.PRS_IDENTIFICACION); sbSql.append(" , prsDocente.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
//			sbSql.append(" , prsDocente.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
//			sbSql.append(" , prsDocente.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
//			sbSql.append(" , prsDocente.");sbSql.append(JdbcConstantes.PRS_SEXO);
//			sbSql.append(" , fcdcastt.");sbSql.append(JdbcConstantes.FCDCASTT_ID);
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
//			
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEXO);
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_FECHA_NACIMIENTO);
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_ETN_ID);
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_UBC_ID);
//			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
//			
//			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_FECHA_INICIO);
//			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
//			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREVIOS);
//			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS);
//			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_TIPO_COLEGIO);
//			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_TIPO_DURAC_REC);
//			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_TIEMPO_ESTUD_REC);
//			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_INAC_ID_INST_EST_PREVIOS);
//			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_TITULO_BACHILLER);
//			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_LINK_TESIS);
//			
//			sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
//			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
//			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
//			sbSql.append(" , mctt.");sbSql.append(JdbcConstantes.MCTT_DESCRIPCION);
//			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ID);
//			sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
//			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_NUM_ACTA_GRADO);
//			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_NOTA_TRAB_TITULACION);
//			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_NOTA_PROM_ACUMULADO);
//			sbSql.append(" , fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
//			sbSql.append(" , fcl.");sbSql.append(JdbcConstantes.FCL_ID);
//			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_HORA_ACTA_GRADO);
//			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_FECHA_ACTA_GRADO);
//			sbSql.append(" ,");sbSql.append(JdbcConstantes.MDL_DESCRIPCION);
//			sbSql.append(" ,");sbSql.append(JdbcConstantes.TTL_DESCRIPCION);
//			sbSql.append(" ,ubc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
//			sbSql.append(" ,ubcP.");sbSql.append(JdbcConstantes.UBC_GENTILICIO);
//			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ID);
//			sbSql.append(" ) docente ORDER BY id,carreraDetalle, apellido1Est , apellido2Est");

			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			boolean op=true;
			List<FichaDocenteAsignacionTitulacionDto> lista = new ArrayList<FichaDocenteAsignacionTitulacionDto>();
			while(rs.next()){
				if(op){
					estudianteAux.setPrsIdentificacion(rs.getString("idEstudiante"));
					estudianteAux.setPrsPrimerApellido(rs.getString("apellido1E"));
					estudianteAux.setPrsSegundoApellido(rs.getString("apellido2E"));
					estudianteAux.setPrsNombres(rs.getString("nombresE"));
					estudianteAux.setFcesFechaInicio(rs.getDate(JdbcConstantes.FCES_FECHA_INICIO));
					estudianteAux.setFcesFechaEgresamiento(rs.getDate(JdbcConstantes.FCES_FECHA_EGRESAMIENTO));
					estudianteAux.setFcesRecEstuPrevios(rs.getInt(JdbcConstantes.FCES_REC_ESTUD_PREVIOS));
					estudianteAux.setFcesCrrEstudPrevios(rs.getString(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS));
					estudianteAux.setFcesTipoColegio(rs.getInt(JdbcConstantes.FCES_TIPO_COLEGIO));
					estudianteAux.setFcesTipoDuracionRec(rs.getInt(JdbcConstantes.FCES_TIPO_DURAC_REC));
					estudianteAux.setFcesTiempoEstudRec(rs.getInt(JdbcConstantes.FCES_TIEMPO_ESTUD_REC));
					estudianteAux.setFcesInacEstPrevios(rs.getInt(JdbcConstantes.FCES_INAC_ID_INST_EST_PREVIOS));
					estudianteAux.setFcesTituloBachiller(rs.getString(JdbcConstantes.FCES_TITULO_BACHILLER));
					try {
						estudianteAux.setFcesLinkTesis(rs.getString(JdbcConstantes.FCES_LINK_TESIS));	
					} catch (Exception e) {
					}
					
					estudianteAux.setCnvId(rs.getInt(JdbcConstantes.CNV_ID));
					
					
					estudianteAux.setPrsSexo(rs.getInt("sexoE"));
					estudianteAux.setPrsFechaNacimiento(rs.getDate("nacimientoE"));
					estudianteAux.setPrsMailPersonal(rs.getString("mail"));
					estudianteAux.setPrsEtnId(rs.getInt("etniaE"));
					estudianteAux.setPrsUbcId(rs.getInt("ubcId"));
					estudianteAux.setUbcGentilicio(rs.getString(JdbcConstantes.UBC_GENTILICIO));
					
					estudianteAux.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
					estudianteAux.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
					estudianteAux.setMcttdescripcion(rs.getString(JdbcConstantes.MCTT_DESCRIPCION));
					
					estudianteAux.setTrttId(rs.getInt(JdbcConstantes.TRTT_ID));
					estudianteAux.setTrttCarreraId(rs.getInt(JdbcConstantes.TRTT_CARRERA_ID));
					estudianteAux.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
					estudianteAux.setFcesNumActaGrado(rs.getString(JdbcConstantes.FCES_NUM_ACTA_GRADO));
					estudianteAux.setFcesNotaTrabTitulacion(rs.getBigDecimal(JdbcConstantes.FCES_NOTA_TRAB_TITULACION));
					estudianteAux.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
					estudianteAux.setFclId(rs.getInt(JdbcConstantes.FCL_ID));
					estudianteAux.setFcesHoraActaGrado(rs.getString(JdbcConstantes.FCES_HORA_ACTA_GRADO));
					estudianteAux.setFcesFechaActaGrado(rs.getDate(JdbcConstantes.FCES_FECHA_ACTA_GRADO));
					estudianteAux.setMdlDescripcion(rs.getString(JdbcConstantes.MDL_DESCRIPCION));
					estudianteAux.setFcesNotaPromAcumulado(rs.getBigDecimal(JdbcConstantes.FCES_NOTA_PROM_ACUMULADO));
					estudianteAux.setTtlDescripcion(rs.getString(JdbcConstantes.TTL_DESCRIPCION));
					estudianteAux.setUbcDescripcion(rs.getString(JdbcConstantes.UBC_DESCRIPCION));
					estudianteAux.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
					estudianteAux.setAsttTemaTrabajo(rs.getString(JdbcConstantes.ASTT_TEMA_TRABAJO));
					estudianteAux.setAsttTutor(rs.getString(JdbcConstantes.ASTT_DIRECTOR_CIENTIFICO));
					op=false;
				}
				FichaDocenteAsignacionTitulacionDto fcdcAsttAux = new FichaDocenteAsignacionTitulacionDto();
				fcdcAsttAux.setPrsIdentificacion(rs.getString("idDocente"));
				fcdcAsttAux.setPrsPrimerApellido(rs.getString("apellido1"));
				fcdcAsttAux.setPrsSegundoApellido(rs.getString("apellido2"));
				fcdcAsttAux.setPrsNombres(rs.getString("nombresD"));
				fcdcAsttAux.setPrsSexo(rs.getInt("sexoD"));
				
				fcdcAsttAux.setFcdcasttId(rs.getInt(JdbcConstantes.FCDCASTT_ID));
				fcdcAsttAux.setFcdcasttTipotribunal(rs.getInt(JdbcConstantes.FCDCASTT_TIPO_TRIBUNAL));
				lista.add(fcdcAsttAux);
				
				
				
			
				

			}
			
			estudianteAux.setListaDocentesTribunal(lista);
			
//			
//			
//			estudianteAux.setAsnoId(rs.getInt("asnoId"));
//			try {
//				int res=rs.getBigDecimal("nota3").compareTo(new BigDecimal(GeneralesConstantes.APP_ID_BASE));
//				if(res==1){
//					rs.next();
//					fcdcAsttAux = new FichaDocenteAsignacionTitulacionDto();
//					fcdcAsttAux.setPrsIdentificacion(rs.getString("cedula"));
//					fcdcAsttAux.setPrsPrimerApellido(rs.getString("apellido1"));
//					fcdcAsttAux.setPrsSegundoApellido(rs.getString("apellido2"));
//					fcdcAsttAux.setPrsNombres(rs.getString("nombres"));
//					fcdcAsttAux.setPrsSexo(rs.getInt("sexoDcn"));
//					fcdcAsttAux.setFcdcasttId(rs.getInt("id"));
//					estudianteAux.getListaDocentesTribunal().add(fcdcAsttAux);
//				}
//			} catch (Exception e) {
//			}
//			
//			
//			try {
//				estudianteAux.setAsnoDfnOral1(rs.getBigDecimal("nota1").floatValue());	
//			} catch (Exception e) {
//			}
//			try {
//				estudianteAux.setAsnoDfnOral2(rs.getBigDecimal("nota2").floatValue());	
//			} catch (Exception e) {
//			}
//			
//			try {
//				estudianteAux.setAsnoDfnOral3(rs.getBigDecimal("nota3").floatValue());
//			} catch (Exception e) {
//				estudianteAux.setAsnoTrbLector3((float)GeneralesConstantes.APP_ID_BASE);
//			}
//			estudianteAux.setAsnoPrmDfnOral(rs.getBigDecimal("notaDefensa").floatValue());
//			estudianteAux.setTrttId(rs.getInt("trtt_id"));
//			try {
//				estudianteAux.setAsnoPrmTrbEscrito(rs.getBigDecimal("notaTrabajoTitulacion").floatValue());	
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if( rs != null){
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		return estudianteAux;
	}
	
	@Override
	public BigDecimal buscarNotaTrabajoEscrito(Integer trttId){
		BigDecimal retorno = BigDecimal.ZERO;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");sbSql.append(JdbcConstantes.ASNO_PRM_TRB_ESCRITO);
			sbSql.append(" from asentamiento_nota asno, proceso_tramite prtr, tramite_titulo trtt"
					+ " where asno.prtr_id=prtr.prtr_id and trtt.trtt_id=prtr.trtt_id and trtt.trtt_id = ");sbSql.append(trttId);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			rs.next();
			retorno=rs.getBigDecimal(JdbcConstantes.ASNO_PRM_TRB_ESCRITO);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if( rs != null){
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		
		return retorno;
		
		
	}
	
	
	@Override
	public BigDecimal buscarNotaTrabajoOral(Integer trttId){
		BigDecimal retorno = BigDecimal.ZERO;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");sbSql.append(JdbcConstantes.ASNO_PRM_DFN_ORAL);
			sbSql.append(" from asentamiento_nota asno, proceso_tramite prtr, tramite_titulo trtt"
					+ " where asno.prtr_id=prtr.prtr_id and trtt.trtt_id=prtr.trtt_id and trtt.trtt_id = ");sbSql.append(trttId);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			rs.next();
			retorno=rs.getBigDecimal(JdbcConstantes.ASNO_PRM_DFN_ORAL);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if( rs != null){
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		
		return retorno;
		
		
	}
	
	
	@Override
	public AsentamientoNota buscarNotasOral(Integer trttId){
		AsentamientoNota retorno = new AsentamientoNota();
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");sbSql.append(JdbcConstantes.ASNO_DFN_ORAL1);
			sbSql.append(" , ");sbSql.append(JdbcConstantes.ASNO_DFN_ORAL2);
			sbSql.append(" , CASE WHEN ");sbSql.append(JdbcConstantes.ASNO_DFN_ORAL3); sbSql.append(" IS NOT NULL THEN ");sbSql.append(JdbcConstantes.ASNO_DFN_ORAL3);
			sbSql.append(" ELSE ");sbSql.append(GeneralesConstantes.APP_ID_BASE);sbSql.append(" END AS ");sbSql.append(JdbcConstantes.ASNO_DFN_ORAL3);
			sbSql.append(" from asentamiento_nota asno, proceso_tramite prtr, tramite_titulo trtt"
					+ " where asno.prtr_id=prtr.prtr_id and trtt.trtt_id=prtr.trtt_id and trtt.trtt_id = ");sbSql.append(trttId);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			rs.next();
			retorno.setAsnoDfnOral1(rs.getBigDecimal(JdbcConstantes.ASNO_DFN_ORAL1));
			retorno.setAsnoDfnOral2(rs.getBigDecimal(JdbcConstantes.ASNO_DFN_ORAL2));
			retorno.setAsnoDfnOral3(rs.getBigDecimal(JdbcConstantes.ASNO_DFN_ORAL3));
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if( rs != null){
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		
		return retorno;
		
		
	}
	
	@Override
	public List<EstudianteEmisionActaJdbcDto> buscarActaBloqueComplexivo(Integer crrId,Integer mesId)throws AsentamientoNotaNoEncontradoException, AsentamientoNotaException {
		List<EstudianteEmisionActaJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		try {
			retorno = new ArrayList<EstudianteEmisionActaJdbcDto>();
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_FECHA_NACIMIENTO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_UBC_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ETN_ID);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,mctt.");sbSql.append(JdbcConstantes.MCTT_DESCRIPCION);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_FECHA_INICIO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREVIOS);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_TIPO_DURAC_REC);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_TIEMPO_ESTUD_REC);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_TIPO_COLEGIO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_INAC_ID_INST_EST_PREVIOS);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_TITULO_BACHILLER);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_LINK_TESIS);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ID);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_NOTA_TRAB_TITULACION);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_CNCR_ID);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_NOTA_PROM_ACUMULADO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_NOTA_TRAB_TITULACION);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_FECHA_ACTA_GRADO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_HORA_ACTA_GRADO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_NUM_ACTA_GRADO);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" ,md.");sbSql.append(JdbcConstantes.MDL_DESCRIPCION);
			sbSql.append(" ,tt.");sbSql.append(JdbcConstantes.TTL_DESCRIPCION);
			sbSql.append(" ,ub.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
			sbSql.append(",  CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_DECANO); sbSql.append(" IS NULL THEN '");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append("' ELSE fces.");sbSql.append(JdbcConstantes.FCES_DECANO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_DECANO);
			sbSql.append(",  CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_SUBDECANO); sbSql.append(" IS NULL THEN '");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append("' ELSE fces.");sbSql.append(JdbcConstantes.FCES_SUBDECANO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_SUBDECANO);
			sbSql.append(",  CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_SECRETARIO); sbSql.append(" IS NULL THEN '");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append("' ELSE fces.");sbSql.append(JdbcConstantes.FCES_SECRETARIO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_SECRETARIO);
			sbSql.append(",  CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_DIRECTOR); sbSql.append(" IS NULL THEN '");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append("' ELSE fces.");sbSql.append(JdbcConstantes.FCES_DIRECTOR); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_DIRECTOR);
			sbSql.append(",  CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_PORCENTAJE_COMPLEX); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_PORCENTAJE_COMPLEX); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_PORCENTAJE_COMPLEX);
			sbSql.append(",  CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_DECANO_SEXO); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_DECANO_SEXO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_DECANO_SEXO);
			sbSql.append(",  CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_SUBDECANO_SEXO); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_SUBDECANO_SEXO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_SUBDECANO_SEXO);
			sbSql.append(",  CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_SECRETARIO_SEXO); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_SECRETARIO_SEXO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_SECRETARIO_SEXO);
			sbSql.append(",  CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_DIRECTOR_SEXO); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_DIRECTOR_SEXO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_DIRECTOR_SEXO);
			
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" right join ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");sbSql.append(" on prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" left join ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ub ");sbSql.append(" on prs.");sbSql.append(JdbcConstantes.UBC_ID);sbSql.append(" = ub.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" left join ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");sbSql.append(" on cncr.");sbSql.append(JdbcConstantes.CNCR_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" left join ");sbSql.append(JdbcConstantes.TABLA_TITULO);sbSql.append(" tt ");sbSql.append(" on cncr.");sbSql.append(JdbcConstantes.TTL_ID);sbSql.append(" = tt.");sbSql.append(JdbcConstantes.TTL_ID);
			sbSql.append(" left join ");sbSql.append(JdbcConstantes.TABLA_MODALIDAD);sbSql.append(" md ");sbSql.append(" on md.");sbSql.append(JdbcConstantes.MDL_ID);sbSql.append(" = cncr.");sbSql.append(JdbcConstantes.MDL_ID);
			sbSql.append(" right join ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_CARRERA);sbSql.append(" mcttcr ");sbSql.append(" on fces.");sbSql.append(JdbcConstantes.MCCR_ID);sbSql.append(" = mcttcr.");sbSql.append(JdbcConstantes.MCCR_ID);
			sbSql.append(" left join ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION);sbSql.append(" mctt ");sbSql.append(" on mcttcr.");sbSql.append(JdbcConstantes.MCTT_ID);sbSql.append(" = mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
			sbSql.append(" left join ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");sbSql.append(" on crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = mcttcr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" left join ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ");sbSql.append(" on fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" left join ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ");sbSql.append(" on fces.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" left join ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ");sbSql.append(" on cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.CNV_ID);
			sbSql.append(" right join ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr ");sbSql.append(" on trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = prtr.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" left join ");sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asnt ");sbSql.append(" on prtr.");sbSql.append(JdbcConstantes.PRTR_ID);sbSql.append(" = asnt.");sbSql.append(JdbcConstantes.PRTR_ID);
		
			sbSql.append(" WHERE mcttcr.");sbSql.append(JdbcConstantes.MCTT_ID);sbSql.append(" = ");sbSql.append(MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_VALUE);
			sbSql.append(" and  trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" in( ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_VALUE);
			sbSql.append(" , ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_VALUE);
			sbSql.append(" , ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_VALUE);sbSql.append(" ) ");
			sbSql.append(" and  prtr.");sbSql.append(JdbcConstantes.PRTR_TIPO_PROCESO);sbSql.append(" = ");sbSql.append(ProcesoTramiteConstantes.TIPO_PROCESO_EMISION_ACTA_VALUE);
			sbSql.append(" and  trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" = ");sbSql.append(crrId);
			sbSql.append(" and  trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
//			sbSql.append(" LIMIT 5");
			
			if (mesId.intValue() != GeneralesConstantes.APP_ID_BASE) {
				sbSql.append(" and prtr.");sbSql.append(JdbcConstantes.PRTR_FECHA_EJECUCION);
				sbSql.append(" between ?  ");
				sbSql.append(" and ? ");
			}
			sbSql.append(" ORDER BY fces.");sbSql.append(JdbcConstantes.FCES_NUM_ACTA_GRADO);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());

			if (mesId.intValue() != GeneralesConstantes.APP_ID_BASE) {
				Calendar fecha = new GregorianCalendar();
				fecha.set(fecha.get(Calendar.YEAR), mesId, 0);
				pstmt.setTimestamp(1, Timestamp.valueOf(GeneralesUtilidades.getFechaString(GeneralesUtilidades.getPrimerDiaDelMes(fecha.getTime()))));					
				pstmt.setTimestamp(2, Timestamp.valueOf(GeneralesUtilidades.getFechaString(GeneralesUtilidades.getUltimoDiaDelMes(fecha.getTime()))));
			}
				
			rs = pstmt.executeQuery();
			while (rs.next()) {
				EstudianteEmisionActaJdbcDto estudianteAux = new EstudianteEmisionActaJdbcDto();
				estudianteAux.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
				estudianteAux.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
				estudianteAux.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
				estudianteAux.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
				estudianteAux.setPrsTipoIdentificacion(rs.getInt(JdbcConstantes.PRS_TIPO_IDENTIFICACION));
				estudianteAux.setPrsMailPersonal(rs.getString(JdbcConstantes.PRS_MAIL_PERSONAL));
				estudianteAux.setPrsSexo(rs.getInt(JdbcConstantes.PRS_SEXO));
				estudianteAux.setPrsFechaNacimiento(rs.getDate(JdbcConstantes.PRS_FECHA_NACIMIENTO));
				estudianteAux.setPrsUbcId(rs.getInt(JdbcConstantes.PRS_UBC_ID));
				estudianteAux.setPrsEtnId(rs.getInt(JdbcConstantes.PRS_ETN_ID));
				estudianteAux.setTrttId(rs.getInt(JdbcConstantes.TRTT_ID));
				estudianteAux.setTrttEstadoProceso(rs.getInt(JdbcConstantes.TRTT_ESTADO_PROCESO));
				estudianteAux.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
				estudianteAux.setMcttdescripcion(rs.getString(JdbcConstantes.MCTT_DESCRIPCION));
				estudianteAux.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
				estudianteAux.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
				estudianteAux.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
				estudianteAux.setFcesCncrId(rs.getInt(JdbcConstantes.FCES_CNCR_ID));
				estudianteAux.setFcesFechaInicio(rs.getDate(JdbcConstantes.FCES_FECHA_INICIO));
				estudianteAux.setFcesFechaEgresamiento(rs.getDate(JdbcConstantes.FCES_FECHA_EGRESAMIENTO));
				estudianteAux.setFcesRecEstuPrevios(rs.getInt(JdbcConstantes.FCES_REC_ESTUD_PREVIOS));
				estudianteAux.setFcesCrrEstudPrevios(rs.getString(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS));
				estudianteAux.setFcesTipoColegio(rs.getInt(JdbcConstantes.FCES_TIPO_COLEGIO));
				estudianteAux.setFcesTipoDuracionRec(rs.getInt(JdbcConstantes.FCES_TIPO_DURAC_REC));
				estudianteAux.setFcesTiempoEstudRec(rs.getInt(JdbcConstantes.FCES_TIEMPO_ESTUD_REC));
				estudianteAux.setFcesInacEstPrevios(rs.getInt(JdbcConstantes.FCES_INAC_ID_INST_EST_PREVIOS));
				estudianteAux.setFcesTituloBachillerId(rs.getInt(JdbcConstantes.FCES_TITULO_BACHILLER));
				estudianteAux.setFcesLinkTesis(rs.getString(JdbcConstantes.FCES_LINK_TESIS));
				estudianteAux.setFcesNotaPromAcumulado(rs.getBigDecimal(JdbcConstantes.FCES_NOTA_PROM_ACUMULADO));
				estudianteAux.setFcesNotaTrabTitulacion(rs.getBigDecimal(JdbcConstantes.FCES_NOTA_TRAB_TITULACION));
				estudianteAux.setFcesHoraActaGrado(rs.getString(JdbcConstantes.FCES_HORA_ACTA_GRADO));
				estudianteAux.setFcesFechaActaGrado(rs.getDate(JdbcConstantes.FCES_FECHA_ACTA_GRADO));
				estudianteAux.setFcesNumActaGrado(rs.getString(JdbcConstantes.FCES_NUM_ACTA_GRADO));
				estudianteAux.setFclId(rs.getInt(JdbcConstantes.FCL_ID));
				estudianteAux.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
				estudianteAux.setMdlDescripcion(rs.getString(JdbcConstantes.MDL_DESCRIPCION));
				estudianteAux.setTtlDescripcion(rs.getString(JdbcConstantes.TTL_DESCRIPCION));
				estudianteAux.setUbcDescripcion(rs.getString(JdbcConstantes.UBC_DESCRIPCION));
				estudianteAux.setCnvId(rs.getInt(JdbcConstantes.CNV_ID));
				estudianteAux.setFcesDecano(rs.getString(JdbcConstantes.FCES_DECANO));
				estudianteAux.setFcesSubdecano(rs.getString(JdbcConstantes.FCES_SUBDECANO));
				estudianteAux.setFcesDirector(rs.getString(JdbcConstantes.FCES_DIRECTOR));
				estudianteAux.setFcesSecretario(rs.getString(JdbcConstantes.FCES_SECRETARIO));
				estudianteAux.setFcesPorcentajeComplex(rs.getInt(JdbcConstantes.FCES_PORCENTAJE_COMPLEX));
				estudianteAux.setFcesDecanoSexo(rs.getInt(JdbcConstantes.FCES_DECANO_SEXO));
				estudianteAux.setFcesSubdecanoSexo(rs.getInt(JdbcConstantes.FCES_SUBDECANO_SEXO));
				estudianteAux.setFcesDirectorSexo(rs.getInt(JdbcConstantes.FCES_DIRECTOR_SEXO));
				estudianteAux.setFcesSecretarioSexo(rs.getInt(JdbcConstantes.FCES_SECRETARIO_SEXO));
				retorno.add(estudianteAux);

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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
		return retorno;
	}

}
