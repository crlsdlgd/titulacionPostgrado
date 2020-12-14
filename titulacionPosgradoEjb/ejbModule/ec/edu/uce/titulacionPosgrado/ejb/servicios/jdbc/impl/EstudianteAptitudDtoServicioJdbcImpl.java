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
   
 ARCHIVO:     EstudiantePostuladoDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de la consulta estudiante postulado.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 15-JUNIO-2016		Daniel Albuja				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteAptitudJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteAptitudOtrosMecanismosJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteActaGradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteActaGradoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EvaluadosDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EvaluadosDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.ProcesoTramiteServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.TramiteTituloServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.EstudianteAptitudDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.AptitudConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.ProcesoTramiteConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.TramiteTituloConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Aptitud;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.ProcesoTramite;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.TramiteTitulo;

/**
 * EJB EstudiantePostuladoDtoServicioJdbcImpl.
 * Clase Clase donde se implementan los metodos para el servicio jdbc de la consulta estudiante postulado.
 * @author dalbuja
 * @version 1.0
 */
@Stateless
public class EstudianteAptitudDtoServicioJdbcImpl implements EstudianteAptitudDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;
//
	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	
	@EJB
	private TramiteTituloServicio srvTramiteTitulo;
	@EJB
	private RolFlujoCarreraServicio srvRolFlujoCarreraServicio;
	@EJB
	private ProcesoTramiteServicio srvProcesoTramiteServicio;
	
	

	/**
	 * Método que guarda la aptitud del postulante por parte del validador
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EvaluadosDtoJdbcException 
	 * @throws EvaluadosDtoJdbcNoEncontradoException 
	 */
	@Override
	public Integer guardarAptitud(
			EstudianteAptitudJdbcDto item, String validadorIdentificacion,RolFlujoCarrera roflcrr) throws EvaluadosDtoJdbcException,
			EvaluadosDtoJdbcNoEncontradoException {
		//**********************************************************************
		//********** actualizar el tramite actual  *****************************
		//**********************************************************************
		
		ProcesoTramite nuevoPrtr = new ProcesoTramite();
		
		Integer validador=null;
		TramiteTitulo trttAux = em.find(TramiteTitulo.class, item.getTrttId());
		if(item.getAptRequisitos()==AptitudConstantes.SI_CUMPLE_REQUISITOS_VALUE &&(item.getAptAproboActualizar()==null || item.getAptAproboActualizar()==AptitudConstantes.SI_APROBO_ACTUALIZAR_VALUE)){
			if(roflcrr.getRoflcrUsuarioRol().getUsroRol().getRolId()==RolConstantes.ROL_BD_VALIDADOR_VALUE){
				trttAux.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_APTO_VALUE);
//				TramiteTituloConstantes.ESTADO_PROCESO_APTO_VALUE
				nuevoPrtr.setPrtrTipoProceso(ProcesoTramiteConstantes.TIPO_PROCESO_APTO_VALUE);
				nuevoPrtr.setPrtrFechaEjecucion(item.getPrtrFechaEjecucion());
				nuevoPrtr.setPrtrRolFlujoCarrera(roflcrr);
				nuevoPrtr.setPrtrTramiteTitulo(trttAux);
				em.persist(nuevoPrtr);
				validador=ProcesoTramiteConstantes.TIPO_PROCESO_APTO_VALUE;
			}else if(roflcrr.getRoflcrUsuarioRol().getUsroRol().getRolId()==RolConstantes.ROL_BD_EVALUADOR_VALUE){
				trttAux.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_APTO_EVALUADO_VALUE);
//				TramiteTituloConstantes.ESTADO_PROCESO_APTO_VALUE
				nuevoPrtr.setPrtrTipoProceso(ProcesoTramiteConstantes.TIPO_PROCESO_APTO_EVALUADO_VALUE);
				nuevoPrtr.setPrtrFechaEjecucion(item.getPrtrFechaEjecucion());
				nuevoPrtr.setPrtrRolFlujoCarrera(roflcrr);
				nuevoPrtr.setPrtrTramiteTitulo(trttAux);
				em.persist(nuevoPrtr);
				validador=ProcesoTramiteConstantes.TIPO_PROCESO_APTO_VALUE;
			}
		}else{
			if(roflcrr.getRoflcrUsuarioRol().getUsroRol().getRolId()==RolConstantes.ROL_BD_VALIDADOR_VALUE){
				trttAux.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_NO_APTO_VALUE);
//				TramiteTituloConstantes.ESTADO_PROCESO_NO_APTO_VALUE
				nuevoPrtr.setPrtrTipoProceso(ProcesoTramiteConstantes.TIPO_PROCESO_NO_APTO_VALUE);
				nuevoPrtr.setPrtrFechaEjecucion(item.getPrtrFechaEjecucion());
				nuevoPrtr.setPrtrRolFlujoCarrera(roflcrr);
				nuevoPrtr.setPrtrTramiteTitulo(trttAux);
				em.persist(nuevoPrtr);
				validador=ProcesoTramiteConstantes.TIPO_PROCESO_NO_APTO_VALUE;
			}else if(roflcrr.getRoflcrUsuarioRol().getUsroRol().getRolId()==RolConstantes.ROL_BD_EVALUADOR_VALUE){
				trttAux.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_NO_APTO_EVALUADO_VALUE);
//				TramiteTituloConstantes.ESTADO_PROCESO_NO_APTO_VALUE
				nuevoPrtr.setPrtrTipoProceso(ProcesoTramiteConstantes.TIPO_PROCESO_NO_APTO_EVALUADO_VALUE);
				nuevoPrtr.setPrtrFechaEjecucion(item.getPrtrFechaEjecucion());
				nuevoPrtr.setPrtrRolFlujoCarrera(roflcrr);
				nuevoPrtr.setPrtrTramiteTitulo(trttAux);
				em.persist(nuevoPrtr);
				validador=ProcesoTramiteConstantes.TIPO_PROCESO_NO_APTO_EVALUADO_VALUE;
			}
		}
		
		//Creamos el registro de la tabla aptitud
		if(roflcrr.getRoflcrUsuarioRol().getUsroRol().getRolId()==RolConstantes.ROL_BD_VALIDADOR_VALUE){
			Aptitud nuevoAptitud= new Aptitud();
			nuevoAptitud.setAptRequisitos(item.getAptRequisitos());
			nuevoAptitud.setAptAproboActualizar(item.getAptAproboActualizar());
			nuevoAptitud.setAptProcesoTramite(nuevoPrtr);
			em.persist(nuevoAptitud);
		}else if(roflcrr.getRoflcrUsuarioRol().getUsroRol().getRolId()==RolConstantes.ROL_BD_EVALUADOR_VALUE){
			Aptitud aptitud=em.find(Aptitud.class,item.getAptId());
			aptitud.setAptProcesoTramite(nuevoPrtr);
		}
		
		return validador;
	}
	
	
	
	/**
	 * Método que busca un estudiante por identificacion y convocatoria de acuerdo a su carrera
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudianteActaGradoException 
	 * @throws EstudianteActaGradoNoEncontradoException 
	 * @throws EstudianteActaGradoJdbcDtoException 
	 * @throws EstudianteActaGradoJdbcDtoNoEncontradoException 
	 */
	@Override
	public List<EstudianteAptitudOtrosMecanismosJdbcDto> 
	buscarEstudianteXIndetificacionXCarreraXConvocatoria(String identificacion, int carreraId , int convocatoria, int estado) 
			throws EstudianteActaGradoException, EstudianteActaGradoNoEncontradoException  {
		List<EstudianteAptitudOtrosMecanismosJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			//**** CAMPOS DE PERSONA ****/
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO_SNIESE);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_FECHA_NACIMIENTO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			
			//**** CAMPOS DE MECANISMO TITULACION CARRERA ****/
			sbSql.append(" ,mcttcr.");sbSql.append(JdbcConstantes.MCCR_ID);
			sbSql.append(" ,mcttcr.");sbSql.append(JdbcConstantes.MCCR_ESTADO);
			sbSql.append(" ,mcttcr.");sbSql.append(JdbcConstantes.MCCR_CRR_ID);
			sbSql.append(" ,mcttcr.");sbSql.append(JdbcConstantes.MCCR_MCTT_ID);
			sbSql.append(" ,mcttcr.");sbSql.append(JdbcConstantes.MCCR_PORCENTAJE);
			
			//**** CAMPOS DE MECANISMO TITULACION  ****/
			sbSql.append(" ,mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
			sbSql.append(" ,mctt.");sbSql.append(JdbcConstantes.MCTT_ESTADO);
			sbSql.append(" ,mctt.");sbSql.append(JdbcConstantes.MCTT_DESCRIPCION);
			sbSql.append(" ,mctt.");sbSql.append(JdbcConstantes.MCTT_CODIGO_SNIESE);
			
			//**** CAMPOS DE TRAMITE TITULO  ****/
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
			
			
			//**** CAMPOS DE CARRERA ****/
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);
			
			
			//**** CAMPOS DE FACULTAD ****/
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
			
			//**** CAMPOS DE CONVOCATORIA ****/
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ID);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ESTADO);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ESTADO_FASE);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_FECHA_INICIO);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_FECHA_FIN);
			
			//************ CONDICIONES *******************/
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ON ");
						sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ON ");
						sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ON ");
						sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ON ");
						sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append(" = ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);						
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ON ");
						sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);sbSql.append(" = ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_CARRERA);sbSql.append(" mcttcr ON ");
						sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_MCCR_ID);sbSql.append(" = ");sbSql.append(" mcttcr.");sbSql.append(JdbcConstantes.MCCR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION);sbSql.append(" mctt ON ");
						sbSql.append(" mcttcr.");sbSql.append(JdbcConstantes.MCCR_MCTT_ID);sbSql.append(" = ");sbSql.append(" mctt.");sbSql.append(JdbcConstantes.MCTT_ID);					

			sbSql.append(" WHERE ");
			
			//*************AGREGAR LOS ESTADOS DE TRAMITE PROCESO DE APROBACION DE OTROS MECANISMO ****************/
			sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" in (");
			sbSql.append(estado);
			sbSql.append(" ) ");
			
			
			// Si fue seleccionada la carrera
			if(carreraId!= GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND crr.crr_id = ");sbSql.append(carreraId);sbSql.append(" AND ");	
			}else{
				sbSql.append(" AND crr.crr_id > ");sbSql.append(GeneralesConstantes.APP_ID_BASE);sbSql.append(" AND ");
			}
						
			//identificacion
			sbSql.append(" UPPER(prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(") like ? ");
			
			if(convocatoria != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ? ");
			}else{
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" > ? ");
			}
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);

			sbSql.append(" ORDER BY  ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			//FIN QUERY
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, "%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase())+"%"); //cargo la identificacion
			pstmt.setInt(2, convocatoria);
			
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<EstudianteAptitudOtrosMecanismosJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoBuscarEstudianteXIndetificacionXCarreraXConvocatoria(rs));	
			}
		} catch (SQLException e) {
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
			} catch (SQLException e1) {
			}
			throw new EstudianteActaGradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteValidacionJdbcDto.sql.exception")));
		} catch (Exception e) {
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
			} catch (SQLException e1) {
			}
			throw new EstudianteActaGradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.identificacion.facultad.carrera.convocatoria.exception")));
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
		if(retorno == null || retorno.size()<=0){
			throw new EstudianteActaGradoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.identificacion.facultad.carrera.convocatoria.no.result.exception")));
		}
		return retorno;
	}
	
//	@Override
//	public List<EstudianteAptitudOtrosMecanismosJdbcDto> 
//	buscarEstudianteXIndetificacionXCarreraXConvocatoria(String identificacion, int carreraId , int convocatoria, int estado) 
//			throws EstudianteActaGradoException, EstudianteActaGradoNoEncontradoException  {
//		List<EstudianteAptitudOtrosMecanismosJdbcDto> retorno = null;
//		PreparedStatement pstmt = null;
//		Connection con = null;
//		ResultSet rs = null;
//		try {
//			
//			StringBuilder sbSql = new StringBuilder();
//			sbSql.append(" SELECT ");
//			//**** CAMPOS DE PERSONA ****/
//			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
//			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);
//			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE);
//			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
//			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
//			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
//						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
//						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
//			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
//			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO);
//			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO_SNIESE);
//			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL); sbSql.append(" IS NULL THEN ");sbSql.append("''");
//						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL); 
//						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
//			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); sbSql.append(" IS NULL THEN ");sbSql.append("''");
//						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); 
//						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
//			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);
//			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_FECHA_NACIMIENTO);
//			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ETN_ID);
//			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_UBC_ID);
//			
//			
//			//**** CAMPOS DE MECANISMO TITULACION CARRERA ****/
//			sbSql.append(" ,mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_ID);
//			sbSql.append(" ,mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_ESTADO);
//			sbSql.append(" ,mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_CRR_ID);
//			sbSql.append(" ,mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_MCTT_ID);
//			sbSql.append(" ,mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_PORCENTAJE);
//			sbSql.append(" ,mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_OPCION);
//			
//			//**** CAMPOS DE MECANISMO TITULACION  ****/
//			sbSql.append(" ,mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
//			sbSql.append(" ,mctt.");sbSql.append(JdbcConstantes.MCTT_ESTADO);
//			sbSql.append(" ,mctt.");sbSql.append(JdbcConstantes.MCTT_DESCRIPCION);
//			sbSql.append(" ,mctt.");sbSql.append(JdbcConstantes.MCTT_CODIGO_SNIESE);
//			
//			//**** CAMPOS DE TRAMITE TITULO  ****/
//			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
//			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_NUM_TRAMITE);
//			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);
//			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);
//			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_SUB_ID);
//			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA);
//			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
//			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_MCTT_ESTADISTICO);
//			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
//			
//			
//			//**** CAMPOS DE CARRERA ****/
//			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
//			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
//			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
//			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
//			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);
//			
//			
//			//**** CAMPOS DE FACULTAD ****/
//			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_ID);
//			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
//			
//			//**** CAMPOS DE CONVOCATORIA ****/
//			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ID);
//			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);
//			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ESTADO);
//			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ESTADO_FASE);
//			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_FECHA_INICIO);
//			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_FECHA_FIN);
//			
//			//************ CONDICIONES *******************/
//			sbSql.append(" FROM ");
//			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
//			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ON ");
//						sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
//			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ON ");
//						sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
//			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_ETNIA);sbSql.append(" etn ON ");
//						sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ETN_ID);sbSql.append(" = ");sbSql.append(" etn.");sbSql.append(JdbcConstantes.ETN_ID);
//			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubcNacionalidad ON ");
//						sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_UBC_ID);sbSql.append(" = ");sbSql.append(" ubcNacionalidad.");sbSql.append(JdbcConstantes.UBC_ID);			
//			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubcCanton ON ");
//						sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_UBC_CANTON_RESIDENCIA);sbSql.append(" = ");sbSql.append(" ubcCanton.");sbSql.append(JdbcConstantes.UBC_ID);
//			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubcProv ON ");
//						sbSql.append(" ubcCanton.");sbSql.append(JdbcConstantes.UBC_PADRE);sbSql.append(" = ");sbSql.append(" ubcProv.");sbSql.append(JdbcConstantes.UBC_ID);
//			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubcPais ON ");
//						sbSql.append(" ubcProv.");sbSql.append(JdbcConstantes.UBC_PADRE);sbSql.append(" = ");sbSql.append(" ubcPais.");sbSql.append(JdbcConstantes.UBC_ID);
//			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_TITULO);sbSql.append(" ttlBachiller ON ");
//						sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_TTL_TITULO_BACHILLER);sbSql.append(" = ");sbSql.append(" ttlBachiller.");sbSql.append(JdbcConstantes.TTL_ID);						
//			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ON ");
//						sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
//			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ON ");
//						sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append(" = ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);						
//			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ON ");
//						sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);sbSql.append(" = ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);
//			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_INSTITUCION_ACADEMICA);sbSql.append(" inac ON ");
//						sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_INAC_ID_INST_EST_PREVIOS);sbSql.append(" = ");sbSql.append(" inac.");sbSql.append(JdbcConstantes.INAC_ID);
//			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION_CARRERA);sbSql.append(" mcttcr ON ");
//						sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_MCTTCR_ID);sbSql.append(" = ");sbSql.append(" mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_ID);
//			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION);sbSql.append(" mctt ON ");
//						sbSql.append(" mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_MCTT_ID);sbSql.append(" = ");sbSql.append(" mctt.");sbSql.append(JdbcConstantes.MCTT_ID);					
//
//			sbSql.append(" WHERE ");
//			
//			//*************AGREGAR LOS ESTADOS DE TRAMITE PROCESO DE APROBACION DE OTROS MECANISMO ****************/
//			sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" in (");
//			sbSql.append(estado);
//			sbSql.append(" ) ");
//			
//			
//			// Si fue seleccionada la carrera
//			if(carreraId!= GeneralesConstantes.APP_ID_BASE){
//				sbSql.append(" AND crr.crr_id = ");sbSql.append(carreraId);sbSql.append(" AND ");	
//			}else{
//				sbSql.append(" AND crr.crr_id > ");sbSql.append(GeneralesConstantes.APP_ID_BASE);sbSql.append(" AND ");
//			}
//						
//			//identificacion
//			sbSql.append(" UPPER(prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(") like ? ");
//			
//			if(convocatoria != GeneralesConstantes.APP_ID_BASE){
//				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ? ");
//			}else{
//				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" > ? ");
//			}
//			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
//
//			sbSql.append(" ORDER BY  ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
////			System.out.println(sbSql);
//			//FIN QUERY
//			con = ds.getConnection();
//			pstmt = con.prepareStatement(sbSql.toString());
//			pstmt.setString(1, "%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase())+"%"); //cargo la identificacion
//			pstmt.setInt(2, convocatoria);
//			
//			rs = pstmt.executeQuery();
//			retorno = new ArrayList<EstudianteAptitudOtrosMecanismosJdbcDto>();
//			while(rs.next()){
//				try {
//					int comparador = (rs.getBigDecimal(JdbcConstantes.FCES_NOTA_TRAB_TITULACION)).compareTo(new BigDecimal(14));
//					if(comparador== 0 || comparador==1){
//						retorno.add(transformarResultSetADtoBuscarEstudianteXIndetificacionXCarreraXConvocatoria(rs));	
//					}	
//				} catch (Exception e) {
//				}
//				
//			}
//			
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new EstudianteActaGradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteValidacionJdbcDto.sql.exception")));
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new EstudianteActaGradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.identificacion.facultad.carrera.convocatoria.exception")));
//		} finally {
//			try {
//				if( rs != null){
//					rs.close();
//				}
//				if (pstmt != null){
//					pstmt.close();
//				}
//				if (con != null) {
//					con.close();
//				}
//			} catch (SQLException e) {
//			}
//		}
//		if(retorno == null || retorno.size()<=0){
//			throw new EstudianteActaGradoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.identificacion.facultad.carrera.convocatoria.no.result.exception")));
//		}
//		return retorno;
//	}
	
	private EstudianteAptitudOtrosMecanismosJdbcDto transformarResultSetADtoBuscarEstudianteXIndetificacionXCarreraXConvocatoria(ResultSet rs) throws SQLException{
//		int valAux = 0;
		EstudianteAptitudOtrosMecanismosJdbcDto retorno = new EstudianteAptitudOtrosMecanismosJdbcDto();
		
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsTipoIdentificacion(rs.getInt(JdbcConstantes.PRS_TIPO_IDENTIFICACION));
		retorno.setPrsTipoIdentificacionSniese(rs.getInt(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsSexo(rs.getInt(JdbcConstantes.PRS_SEXO));
		retorno.setPrsSexoSniese(rs.getInt(JdbcConstantes.PRS_SEXO_SNIESE));
		retorno.setPrsMailPersonal(rs.getString(JdbcConstantes.PRS_MAIL_PERSONAL));
		retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
		retorno.setPrsTelefono(rs.getString(JdbcConstantes.PRS_TELEFONO));
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		

		//**** CAMPOS DE FICHA_ESTUDIANTE MECANISMO TITULACION CARRERA ****/
		retorno.setMcttcrId(rs.getInt(JdbcConstantes.MCCR_ID));
		retorno.setMcttcrEstado(rs.getInt(JdbcConstantes.MCCR_ESTADO));
		retorno.setMcttcrCrrId(rs.getInt(JdbcConstantes.MCCR_CRR_ID));
		
		//**** CAMPOS DE FICHA_ESTUDIANTE MECANISMO TITULACION ****/		
		retorno.setMcttId(rs.getInt(JdbcConstantes.MCTT_ID));
		retorno.setMcttCodigoSniese(rs.getString(JdbcConstantes.MCTT_CODIGO_SNIESE));
		retorno.setMcttEstado(rs.getInt(JdbcConstantes.MCTT_ESTADO));
		retorno.setMcttdescripcion(rs.getString(JdbcConstantes.MCTT_DESCRIPCION));
		
		//**** CAMPOS DE FICHA_ESTUDIANTE TRAMITE TITULO ****/
		retorno.setTrttId(rs.getInt(JdbcConstantes.TRTT_ID));
		retorno.setTrttEstadoProceso(rs.getInt(JdbcConstantes.TRTT_ESTADO_PROCESO));
		retorno.setTrttEstadoTramite(rs.getInt(JdbcConstantes.TRTT_ESTADO_TRAMITE));
		retorno.setTrttCarreraId(rs.getInt(JdbcConstantes.TRTT_CARRERA_ID));
		
		//**** CAMPOS DE FICHA_ESTUDIANTE - TRAMITE_TITULO - CONVOCATORIA ****/
		retorno.setCnvId(rs.getInt(JdbcConstantes.CNV_ID));
		retorno.setCnvDescripcion(rs.getString(JdbcConstantes.CNV_DESCRIPCION));
		retorno.setCnvEstado(rs.getInt(JdbcConstantes.CNV_ESTADO));
		retorno.setCnvEstadoFase(rs.getInt(JdbcConstantes.CNV_ESTADO_FASE));
		retorno.setCnvFechaInicio(rs.getDate(JdbcConstantes.CNV_FECHA_INICIO));
		retorno.setCnvFechaFin(rs.getDate(JdbcConstantes.CNV_FECHA_FIN));
		
		//**** CAMPOS DE FICHA_ESTUDIANTE - TRAMITE_TITULO - CARRERA ****//
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		
		//**** CAMPOS DE FICHA_ESTUDIANTE - TRAMITE_TITULO - CARRERA - FACULTAD ****//
		retorno.setFclId(rs.getInt(JdbcConstantes.FCL_ID));
		retorno.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
		
		return retorno;
	}
}
