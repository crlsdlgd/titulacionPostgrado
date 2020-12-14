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
   
 ARCHIVO:     ReportesDtoJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de los reportes.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 24-04-2018		Daniel Albuja					       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteDetalleComplexivoDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteEstadoJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.ReporteActualizacionConocDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.ReporteEvaluadosDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.ReporteRegistradosDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.ReporteValidadosDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.TotalesDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteDetalleComplexivoDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteDetalleComplexivoDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EvaluadosDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EvaluadosDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PostuladosDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PostuladosDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RegistradosDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RegistradosDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.ReportesDtoJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.AptitudConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.MecanismoTitulacionCarreraConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.ProcesoTramiteConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.TramiteTituloConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Usuario;

/**
 * EJB ReportesDtoJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de los reportes.
 * @author dalbuja
 * @version 1.0
 */
@Stateless
public class ReportesDtoJdbcImpl implements ReportesDtoJdbc{

	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	// *****************************************************************/
	// ******************* Variables de ReportesForm *******************/
	// *****************************************************************/
	
	private Integer sumaRegistrados;
	
	@Override
	public List<EstudianteEstadoJdbcDto> listaPersonaEstado(Integer convocatoria,Integer facultad, String cedula) throws RegistradosDtoJdbcException, RegistradosDtoJdbcNoEncontradoException {
		List<EstudianteEstadoJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		sumaRegistrados=0;
			// Todas las convocatorias
			try {
				StringBuilder sbSql = new StringBuilder();
				sbSql.append(" SELECT DISTINCT ");
				sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
				sbSql.append(" , fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
				sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);
				sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_REG_TTL_SENESCYT); sbSql.append(" IS NULL THEN ");sbSql.append("''");
				sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_REG_TTL_SENESCYT); 
				sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_REG_TTL_SENESCYT);
				sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);
				sbSql.append(" , cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);
				sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol");
				sbSql.append(" WHERE prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
				sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
				sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);sbSql.append(" = ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
				sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = ");sbSql.append(RolConstantes.ROL_BD_POSTULANTE_VALUE);
				sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
				sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
				sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);sbSql.append(" = ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);
				sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append(" = ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);
				sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
				if(convocatoria==GeneralesConstantes.APP_ID_BASE){
					sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" > ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
				}else{
					sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ");sbSql.append(convocatoria);
				}
				if(facultad==GeneralesConstantes.APP_ID_BASE ){
					sbSql.append(" AND fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" > ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
				}else{
					sbSql.append(" AND fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" = ");sbSql.append(facultad);
				}
				sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" LIKE '%");
				sbSql.append(GeneralesUtilidades.eliminarEspaciosEnBlanco(cedula));sbSql.append("%'");
				sbSql.append(" ORDER BY fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
				sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
				con = ds.getConnection();
//				System.out.println(sbSql);
//				System.out.println(facultad);
//				System.out.println(convocatoria);
				pstmt = con.prepareStatement(sbSql.toString());
				rs = pstmt.executeQuery();
				retorno = new ArrayList<EstudianteEstadoJdbcDto>();
				while(rs.next()){
					sumaRegistrados++;
					retorno.add((EstudianteEstadoJdbcDto)transformarResultSetADto(rs,1));
				}
			} catch (SQLException e) {
				e.printStackTrace();
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
				throw new RegistradosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.exception",convocatoria)));
			} catch (Exception e) {
				e.printStackTrace();
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
				throw new RegistradosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.exception",convocatoria)));
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
				throw new RegistradosDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.no.result.exception")));
			}	
			return retorno;
	}
	
	
	@Override
	public List<ReporteActualizacionConocDto> listaActualizacionConocimientosXConvocatoriaCarrera(Integer cnvId, String crrId) throws PostuladosDtoJdbcException, PostuladosDtoJdbcNoEncontradoException {
		List<ReporteActualizacionConocDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
			
		try {
				
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" CASE ");
//			sbSql.append(" (SELECT ");sbSql.append(" apt.");sbSql.append(JdbcConstantes.APT_APROBO_ACTUALIZAR);
//			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr_aux,");sbSql.append(JdbcConstantes.TABLA_APTITUD);sbSql.append(" apt");
			sbSql.append(" WHERE ");
			sbSql.append(" prtr_aux.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" = prtr.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND ");
			sbSql.append(" prtr_aux.");sbSql.append(JdbcConstantes.PRTR_ID);sbSql.append(" = apt.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" ) WHEN 0 THEN '");sbSql.append(AptitudConstantes.SI_APROBO_ACTUALIZAR_LABEL);
			sbSql.append("' WHEN 1 THEN '");;sbSql.append(AptitudConstantes.NO_APROBO_ACTUALIZAR_LABEL);
			sbSql.append("' END AS aptitud");
			sbSql.append(" , cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);
			sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);
//			sbSql.append(" , vld.");sbSql.append(JdbcConstantes.VLD_RSL_ACT_CONOCIMIENTO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr ON trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = prtr.");sbSql.append(JdbcConstantes.TRTT_ID);
//			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_APTITUD);sbSql.append(" apt ON prtr.");sbSql.append(JdbcConstantes.PRTR_ID);sbSql.append(" = apt.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ON trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ON crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append(" = fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ON trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ON fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);sbSql.append(" = prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ON trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);sbSql.append(" = cnv.");sbSql.append(JdbcConstantes.CNV_ID);
//			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_VALIDACION);sbSql.append(" vld ON prtr.");sbSql.append(JdbcConstantes.PRTR_ID);sbSql.append(" = vld.");sbSql.append(JdbcConstantes.PRTR_ID);

			sbSql.append(" WHERE ");

			sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
//			sbSql.append(" AND ");sbSql.append(" vld.");sbSql.append(JdbcConstantes.VLD_RSL_ACT_CONOCIMIENTO);sbSql.append(" <> ");sbSql.append(ValidacionConstantes.NO_DEBE_REALIZAR_ACTUALIZACION_CONOCIMIENTOS_VALUE);

			if (cnvId == GeneralesConstantes.APP_ID_BASE ) {
				sbSql.append(" AND ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" > ? ");
			}else {
				sbSql.append(" AND ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ? ");
			}				
			
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" IN (");sbSql.append(crrId);sbSql.append(") ");

			sbSql.append(" ORDER BY ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);

			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, cnvId.intValue());
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ReporteActualizacionConocDto>();
				
				while(rs.next()){
					retorno.add(transformarResulSetAreportePostuladoDto(rs));
				}
				
			} catch (SQLException e) {
				try {
					con.close();
				} catch (Exception e2) {
				}
				throw new PostuladosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.exception", cnvId)));
			} catch (Exception e) {
				try {
					con.close();
				} catch (Exception e2) {
				}
				throw new PostuladosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.exception", cnvId)));
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
				throw new PostuladosDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.no.result.exception")));
			}	
			return retorno;
	}
	
	@Override
	public List<ReporteActualizacionConocDto> listaActualizacionConocimientosXConvocatoriaFacultad(Integer cnvId, Integer fclId) throws PostuladosDtoJdbcException, PostuladosDtoJdbcNoEncontradoException {
		List<ReporteActualizacionConocDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
			
		try {
				
				StringBuilder sbSql = new StringBuilder();
				sbSql.append(" SELECT ");
				sbSql.append(" CASE ");
//				sbSql.append(" (SELECT ");sbSql.append(" apt.");sbSql.append(JdbcConstantes.APT_APROBO_ACTUALIZAR);
//				sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr_aux,");sbSql.append(JdbcConstantes.TABLA_APTITUD);sbSql.append(" apt");
				sbSql.append(" WHERE ");
				sbSql.append(" prtr_aux.");sbSql.append(JdbcConstantes.TRTT_ID);
				sbSql.append(" = prtr.");sbSql.append(JdbcConstantes.TRTT_ID);
				sbSql.append(" AND ");
				sbSql.append(" prtr_aux.");sbSql.append(JdbcConstantes.PRTR_ID);sbSql.append(" = apt.");sbSql.append(JdbcConstantes.PRTR_ID);
				sbSql.append(" ) WHEN 0 THEN '");sbSql.append(AptitudConstantes.SI_APROBO_ACTUALIZAR_LABEL);
				sbSql.append("' WHEN 1 THEN '");;sbSql.append(AptitudConstantes.NO_APROBO_ACTUALIZAR_LABEL);
				sbSql.append("' END AS aptitud");
				sbSql.append(" , cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);
				sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);
				sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);
//				sbSql.append(" , vld.");sbSql.append(JdbcConstantes.VLD_RSL_ACT_CONOCIMIENTO);
				sbSql.append(" , fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
				sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ");
				sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr ON trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = prtr.");sbSql.append(JdbcConstantes.TRTT_ID);
//				sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_APTITUD);sbSql.append(" apt ON prtr.");sbSql.append(JdbcConstantes.PRTR_ID);sbSql.append(" = apt.");sbSql.append(JdbcConstantes.PRTR_ID);
				sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ON trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
				sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ON crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append(" = fcl.");sbSql.append(JdbcConstantes.FCL_ID);
				sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ON trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);
				sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ON fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);sbSql.append(" = prs.");sbSql.append(JdbcConstantes.PRS_ID);
				sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ON trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);sbSql.append(" = cnv.");sbSql.append(JdbcConstantes.CNV_ID);
//				sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_VALIDACION);sbSql.append(" vld ON prtr.");sbSql.append(JdbcConstantes.PRTR_ID);sbSql.append(" = vld.");sbSql.append(JdbcConstantes.PRTR_ID);

				sbSql.append(" WHERE ");

				sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
//				sbSql.append(" AND ");sbSql.append(" vld.");sbSql.append(JdbcConstantes.VLD_RSL_ACT_CONOCIMIENTO);sbSql.append(" <> ");sbSql.append(ValidacionConstantes.NO_DEBE_REALIZAR_ACTUALIZACION_CONOCIMIENTOS_VALUE);

				if (cnvId == GeneralesConstantes.APP_ID_BASE ) {
					sbSql.append(" AND ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" > ? ");
				}else {
					sbSql.append(" AND ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ? ");
				}				
				
				sbSql.append(" AND ");
				
				if (fclId == GeneralesConstantes.APP_ID_BASE) {
					sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" > ? ");
				}else {
					sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" = ? ");
				}

				sbSql.append(" ORDER BY ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
				
				con = ds.getConnection();
				pstmt = con.prepareStatement(sbSql.toString());
				pstmt.setInt(1, cnvId.intValue());
				pstmt.setInt(2, fclId.intValue());
				
				rs = pstmt.executeQuery();
				retorno = new ArrayList<ReporteActualizacionConocDto>();
				
				while(rs.next()){
					retorno.add(transformarResulSetAreportePostuladoDto(rs));
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
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
				} catch (Exception e2) {
				}
				throw new PostuladosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.exception", cnvId)));
			} catch (Exception e) {
				e.printStackTrace();
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
				} catch (Exception e2) {
				}
				throw new PostuladosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.exception", cnvId)));
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
				throw new PostuladosDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.no.result.exception")));
			}	
			return retorno;
	}
	
	@Override
	public List<ReporteRegistradosDto> listaPersonaXConvocatoria(String convocatoria) throws RegistradosDtoJdbcException, RegistradosDtoJdbcNoEncontradoException {
		List<ReporteRegistradosDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		sumaRegistrados=0;
		if(convocatoria.equals(GeneralesConstantes.APP_ID_BASE.toString())){
			// Todas las convocatorias
			try {
				StringBuilder sbSql = new StringBuilder();
				sbSql.append(" SELECT ");
				sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" AS identificacion");
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" AS apellido1");
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(" AS apellido2");
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(" AS nombres");
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);sbSql.append(" AS mail");
				sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs,");
				sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr,");
				sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro,");
				sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol");
				sbSql.append(" WHERE prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
				sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
				sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);sbSql.append(" = ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
				sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = ");sbSql.append(RolConstantes.ROL_BD_POSTULANTE_VALUE);
				sbSql.append(" ORDER BY prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
				con = ds.getConnection();
				pstmt = con.prepareStatement(sbSql.toString());
				rs = pstmt.executeQuery();
				retorno = new ArrayList<ReporteRegistradosDto>();
				while(rs.next()){
					sumaRegistrados++;
					retorno.add((ReporteRegistradosDto)transformarResultSetADto(rs,1));
				}
			} catch (SQLException e) {
				try {
					con.close();
				} catch (Exception e2) {
				}
				throw new RegistradosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.exception",convocatoria)));
			} catch (Exception e) {
				try {
					con.close();
				} catch (Exception e2) {
				}
				throw new RegistradosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.exception",convocatoria)));
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
				throw new RegistradosDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.no.result.exception")));
			}	
			return retorno;
		}else{
			// Por convocatoria
			try {
				StringBuilder sbSql = new StringBuilder();
				sbSql.append(" SELECT ");
				sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" AS identificacion");
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" AS apellido1");
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(" AS apellido2");
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(" AS nombres");
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);sbSql.append(" AS mail");
				sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs,");
				sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr,");
				sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro,");
				sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol");
				sbSql.append(" WHERE usr.");sbSql.append(JdbcConstantes.USR_FECHA_CREACION);sbSql.append(" BETWEEN");
				sbSql.append(" (SELECT cnv.");sbSql.append(JdbcConstantes.CNV_FECHA_INICIO);
				sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv WHERE cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);sbSql.append(" LIKE ?)");
				sbSql.append("AND (SELECT cnv.");sbSql.append(JdbcConstantes.CNV_FECHA_FIN);
				sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv WHERE cnv.");
				sbSql.append(JdbcConstantes.CNV_DESCRIPCION); sbSql.append(" LIKE ?)");
				sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
				sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
				sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);sbSql.append(" = rol.");sbSql.append(JdbcConstantes.ROL_ID);
				sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = ");sbSql.append(RolConstantes.ROL_BD_POSTULANTE_VALUE);
				sbSql.append(" ORDER BY prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
				con = ds.getConnection();
				pstmt = con.prepareStatement(sbSql.toString());
				pstmt.setString(1, convocatoria);
				pstmt.setString(2, convocatoria);
				rs = pstmt.executeQuery();
				retorno = new ArrayList<ReporteRegistradosDto>();
				while(rs.next()){
					retorno.add((ReporteRegistradosDto)transformarResultSetADto(rs,1));
				}
			} catch (SQLException e) {
				try {
					con.close();
				} catch (Exception e2) {
				}
				throw new RegistradosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.exception")));
			} catch (Exception e) {
				try {
					con.close();
				} catch (Exception e2) {
				}
				throw new RegistradosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.exception")));
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
		}
		if(retorno == null || retorno.size()<=0){
			throw new RegistradosDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.no.result.exception")));
		}	
		return retorno;
	}

	/** Método que busca los postulados por la convocatoria y la facultad para los reportes de la DGA
	 * 
	 * return List<ReportePostuladosDto> Lista de Postulados
	 */
	@Override
	public List<ReporteActualizacionConocDto> listaPostuladosXConvocatoriaFacultad(String convocatoria, String facultad)
			throws PostuladosDtoJdbcException,
			PostuladosDtoJdbcNoEncontradoException {
		List<ReporteActualizacionConocDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
			try {
				if(convocatoria.equals(GeneralesConstantes.APP_ID_BASE.toString())){
					convocatoria="%";
				}
				if(facultad.equals(GeneralesConstantes.APP_ID_BASE.toString())){
					facultad="%";
				}
				StringBuilder sbSql = new StringBuilder();
				sbSql.append(" SELECT crr.");
				sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" as detalle");
				sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_NIVEL);sbSql.append(" as nivel");
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" as identificacion");
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" as apellido1");
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(" as apellido2");
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(" as nombres");
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);sbSql.append(" as mail");
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);sbSql.append(" as telefono");
				sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_MCTT_ESTADISTICO);sbSql.append(" as mecanismo");
				sbSql.append(" , cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);sbSql.append(" as ");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);
				sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
				sbSql.append(" RIGHT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ON prs.");
				sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = fces. ");sbSql.append(JdbcConstantes.FCES_PRS_ID);
				sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ON fces.");
				sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
				sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ON trtt.");
				sbSql.append(JdbcConstantes.TRTT_CNV_ID);sbSql.append(" = cnv.");sbSql.append(JdbcConstantes.CNV_ID);
				sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ON trtt.");
				sbSql.append(JdbcConstantes.TRTT_CARRERA);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
				sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr ON trtt.");
				sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = prtr.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
				sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ON crr.");
				sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append(" = fcl.");sbSql.append(JdbcConstantes.FCL_ID);
				
				sbSql.append(" WHERE fcl.");
				sbSql.append(JdbcConstantes.FCL_DESCRIPCION);sbSql.append(" LIKE ? ");
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);sbSql.append(" LIKE ? ");
				sbSql.append(" AND (prtr.");sbSql.append(JdbcConstantes.PRTR_TIPO_PROCESO);sbSql.append(" = ");
				sbSql.append(ProcesoTramiteConstantes.TIPO_PROCESO_POSTULACION_VALUE);
				sbSql.append(" OR prtr.");sbSql.append(JdbcConstantes.PRTR_TIPO_PROCESO);sbSql.append(" = ");
				sbSql.append(ProcesoTramiteConstantes.TIPO_PROCESO_VALIDACION_ERRONEA_VALUE);sbSql.append(" ) ");
				sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
				
				sbSql.append(" ORDER BY detalle, apellido1");
				
				System.err.println(sbSql.toString());
				
				con = ds.getConnection();
				pstmt = con.prepareStatement(sbSql.toString());
				pstmt.setString(1, facultad);
				pstmt.setString(2, convocatoria);
				
				
				rs = pstmt.executeQuery();
				retorno = new ArrayList<ReporteActualizacionConocDto>();
				while(rs.next()){
					retorno.add((ReporteActualizacionConocDto)transformarResultSetADto(rs,2));
				}
			} catch (SQLException e) {
				try {
					con.close();
				} catch (Exception e2) {
				}
				throw new PostuladosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.exception",convocatoria)));
			} catch (Exception e) {
				try {
					con.close();
				} catch (Exception e2) {
				}
				throw new PostuladosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.exception",convocatoria)));
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
				throw new PostuladosDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.no.result.exception")));
			}	
			return retorno;
		
	} 
	
	/** Método que busca los postulados no activos por la convocatoria y la facultad para los reportes de la DGA
	 * 
	 * return List<ReportePostuladosDto> Lista de Postulados
	 */
	@Override
	public List<ReporteActualizacionConocDto> listaNoActivosXConvocatoriaFacultad(String convocatoria, String facultad, String cedula)
			throws PostuladosDtoJdbcException,
			PostuladosDtoJdbcNoEncontradoException {
		List<ReporteActualizacionConocDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
			try {
				if(convocatoria.equals(GeneralesConstantes.APP_ID_BASE.toString())){
					convocatoria="%";
				}
				if(facultad.equals(GeneralesConstantes.APP_ID_BASE.toString())){
					facultad="%";
				}
				StringBuilder sbSql = new StringBuilder();
				sbSql.append(" SELECT crr.");
				sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" as detalle");
				sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_NIVEL);sbSql.append(" as nivel");
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" as identificacion");
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" as apellido1");
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(" as apellido2");
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(" as nombres");
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);sbSql.append(" as mail");
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);sbSql.append(" as telefono");
				sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_MCTT_ESTADISTICO);sbSql.append(" as mecanismo");
//				sbSql.append(" , CASE WHEN trtt.");sbSql.append(JdbcConstantes.TRTT_OBS_DESACTIVAR_DGA);sbSql.append(" IS NULL ");
				sbSql.append(" THEN ");sbSql.append("'");sbSql.append(TramiteTituloConstantes.OBSERVACION_DGA_DESACTIVAR_LABEL);sbSql.append("'");
//				sbSql.append(" ELSE trtt."); sbSql.append(JdbcConstantes.TRTT_OBS_DESACTIVAR_DGA);sbSql.append(" END AS observacion");
				sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
				sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces,");
				sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt,");
				sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv,");
				sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr,");
				sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl");
				sbSql.append(" WHERE fcl.");
				sbSql.append(JdbcConstantes.FCL_DESCRIPCION);sbSql.append(" LIKE ? ");
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);sbSql.append(" LIKE ? ");
				sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = trtt.");
				sbSql.append(JdbcConstantes.TRTT_ID);
				sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);sbSql.append(" = prs.");
				sbSql.append(JdbcConstantes.PRS_ID);
				sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA);sbSql.append(" = crr.");
				sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
				sbSql.append(" AND fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" = crr.");
				sbSql.append(JdbcConstantes.CRR_FCL_ID);
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
				sbSql.append(" AND UPPER(prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(") like ? ");
				sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_INACTIVO_VALUE);
				sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" NOT IN ( ");
				sbSql.append(" SELECT ");sbSql.append(JdbcConstantes.TRTT_SUB_ID);sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);
				sbSql.append(" WHERE ");sbSql.append(JdbcConstantes.TRTT_SUB_ID);sbSql.append(" IS NOT NULL) ");
				sbSql.append(" ORDER BY detalle, apellido1");
				
				
				con = ds.getConnection();
				pstmt = con.prepareStatement(sbSql.toString());
				pstmt.setString(1, facultad);
				pstmt.setString(2, convocatoria);
				pstmt.setString(3, "%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(cedula.toUpperCase())+"%"); //cargo la identificacion
				rs = pstmt.executeQuery();
				retorno = new ArrayList<ReporteActualizacionConocDto>();
				while(rs.next()){
					retorno.add((ReporteActualizacionConocDto)transformarResultSetADto(rs,5));
				}
			} catch (SQLException e) {
				try {
					con.close();
				} catch (Exception e2) {
				}
				throw new PostuladosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.no.activos.buscar.por.convocatoria.exception",convocatoria)));
			} catch (Exception e) {
				try {
					con.close();
				} catch (Exception e2) {
				}
				throw new PostuladosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.no.activos.buscar.por.convocatoria.exception",convocatoria)));
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
				throw new PostuladosDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.no.activos.buscar.por.convocatoria.no.result.exception")));
			}	
			return retorno;
		
	} 
	
	@Override
	public List<TotalesDto> listaTotales(String convocatoria, String facultad)
			throws PostuladosDtoJdbcException,
			PostuladosDtoJdbcNoEncontradoException,
			RegistradosDtoJdbcException,
			RegistradosDtoJdbcNoEncontradoException {
		List<TotalesDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		// Si se seleccionó en los dos combos TODAS
		if(convocatoria.equals(GeneralesConstantes.APP_ID_BASE.toString())&& facultad.equals(GeneralesConstantes.APP_ID_BASE.toString())){
			// todas las convocatorias y facultades
			try {
				StringBuilder sbSql = new StringBuilder();
				sbSql.append(" SELECT DISTINCT crr.");
				sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" AS detalle");
				sbSql.append(" , fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);sbSql.append(" AS facultad");
				sbSql.append(" ,count (prs.prs_identificacion) as conteo");
				sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ");
				sbSql.append(" WHERE prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
				sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
				sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);sbSql.append(" = cnv.");sbSql.append(JdbcConstantes.CNV_ID);
				sbSql.append(" AND (trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA);sbSql.append(" LIKE crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
				sbSql.append(" OR trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(")");
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);sbSql.append(" LIKE ?");
				sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append(" = fcl.");sbSql.append(JdbcConstantes.FCL_ID);
				sbSql.append(" AND fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);sbSql.append(" LIKE ?");
				sbSql.append(" AND fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);
				sbSql.append(" GROUP BY fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);sbSql.append(",crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
				sbSql.append(" ORDER BY crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
				con = ds.getConnection();
				pstmt = con.prepareStatement(sbSql.toString());
				pstmt.setString(1, "%");
				pstmt.setString(2, "%");
				rs = pstmt.executeQuery();
				retorno = new ArrayList<TotalesDto>();
				while(rs.next()){
					retorno.add((TotalesDto)transformarResultSetADto(rs,3));
				}
			} catch (SQLException e) {
				try {
					con.close();
				} catch (Exception e2) {
				}
				throw new PostuladosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.exception")));
			} catch (Exception e) {
				try {
					con.close();
				} catch (Exception e2) {
				}
				throw new PostuladosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.exception")));
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
		}else if(convocatoria.equals(GeneralesConstantes.APP_ID_BASE.toString())&& !facultad.equals(GeneralesConstantes.APP_ID_BASE.toString())){
				// Todas las Convocatorias y Facultad seleccionada
				try {
					
					StringBuilder sbSql = new StringBuilder();
					sbSql.append(" SELECT DISTINCT crr.");
					sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" AS detalle");
					sbSql.append(" , fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);sbSql.append(" AS facultad");
					sbSql.append(" ,count (prs.prs_identificacion) as conteo");
					sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
					sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ");
					sbSql.append(" WHERE prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
					sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
					sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);sbSql.append(" = cnv.");sbSql.append(JdbcConstantes.CNV_ID);
					sbSql.append(" AND (trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA);sbSql.append(" LIKE crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
					sbSql.append(" OR trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(")");
					sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);sbSql.append(" LIKE ?");
					sbSql.append(" AND fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);
					sbSql.append(" AND fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);sbSql.append(" LIKE ?");
					sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append(" = fcl.");sbSql.append(JdbcConstantes.FCL_ID);
					sbSql.append(" GROUP BY fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);sbSql.append(",crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
					sbSql.append(" ORDER BY crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
					con = ds.getConnection();
					pstmt = con.prepareStatement(sbSql.toString());
					pstmt.setString(1, "%");
					pstmt.setString(2, facultad);
					rs = pstmt.executeQuery();
					retorno = new ArrayList<TotalesDto>();
					while(rs.next()){
						retorno.add((TotalesDto)transformarResultSetADto(rs,3));
					}
				} catch (SQLException e) {
					try {
						con.close();
					} catch (Exception e2) {
					}
					throw new PostuladosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.exception")));
				} catch (Exception e) {
					try {
						con.close();
					} catch (Exception e2) {
					}
					throw new PostuladosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.exception")));
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
		}else if(!convocatoria.equals(GeneralesConstantes.APP_ID_BASE.toString())&& facultad.equals(GeneralesConstantes.APP_ID_BASE.toString())){
			// Todas las Facultades y convocatoria seleccionada
			try {
				StringBuilder sbSql = new StringBuilder();
				sbSql.append(" SELECT DISTINCT crr.");
				sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" AS detalle");
				sbSql.append(" , fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);sbSql.append(" AS facultad");
				sbSql.append(" ,count (prs.prs_identificacion) as conteo");
				sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ");
				sbSql.append(" WHERE prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
				sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
				sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);sbSql.append(" = cnv.");sbSql.append(JdbcConstantes.CNV_ID);
				sbSql.append(" AND (trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA);sbSql.append(" LIKE crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
				sbSql.append(" OR trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(")");
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);sbSql.append(" LIKE ?");
				sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append(" = fcl.");sbSql.append(JdbcConstantes.FCL_ID);
				sbSql.append(" AND fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);sbSql.append(" LIKE ?");
				sbSql.append(" AND fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);
				sbSql.append(" GROUP BY fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);sbSql.append(",crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
				sbSql.append(" ORDER BY crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
				con = ds.getConnection();
				pstmt = con.prepareStatement(sbSql.toString());
				pstmt.setString(1, convocatoria);
				pstmt.setString(2, "%");
				rs = pstmt.executeQuery();
				retorno = new ArrayList<TotalesDto>();
				while(rs.next()){
					retorno.add((TotalesDto)transformarResultSetADto(rs,3));
				}
			} catch (SQLException e) {
				try {
					con.close();
				} catch (Exception e2) {
				}
				throw new PostuladosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.exception")));
			} catch (Exception e) {
				try {
					con.close();
				} catch (Exception e2) {
				}
				throw new PostuladosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.exception")));
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
		}else{
			// Por convocatoria y facultad
			try {
				StringBuilder sbSql = new StringBuilder();
				sbSql.append(" SELECT DISTINCT crr.");
				sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" AS detalle");
				sbSql.append(" , fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);sbSql.append(" AS facultad");
				sbSql.append(" ,count (prs.prs_identificacion) as conteo");
				sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
				sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ");
				sbSql.append(" WHERE prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
				sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
				sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);sbSql.append(" = cnv.");sbSql.append(JdbcConstantes.CNV_ID);
				sbSql.append(" AND (trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA);sbSql.append(" LIKE crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
				sbSql.append(" OR trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(")");
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);sbSql.append(" LIKE ?");
				sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append(" = fcl.");sbSql.append(JdbcConstantes.FCL_ID);
				sbSql.append(" AND fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);sbSql.append(" LIKE ?");
				sbSql.append(" AND fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);
				sbSql.append(" GROUP BY fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);sbSql.append(",crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
				sbSql.append(" ORDER BY crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
				con = ds.getConnection();
				pstmt = con.prepareStatement(sbSql.toString());
				pstmt.setString(1, convocatoria);
				pstmt.setString(2, facultad);
				rs = pstmt.executeQuery();
				retorno = new ArrayList<TotalesDto>();
				while(rs.next()){
					retorno.add((TotalesDto)transformarResultSetADto(rs,3));
				}
			} catch (SQLException e) {
				try {
					con.close();
				} catch (Exception e2) {
				}
				throw new PostuladosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.exception")));
			} catch (Exception e) {
				try {
					con.close();
				} catch (Exception e2) {
				}
				throw new PostuladosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.exception")));
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
		}
		if(retorno == null || retorno.size()<=0){
			throw new PostuladosDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.no.result.exception")));
		}	
		return retorno;
	}
	
	@Override
	public List<ReporteActualizacionConocDto> listaPostuladosXSecretaria(
			String convocatoria, String carrera, Usuario usuario)
			throws PostuladosDtoJdbcException,
			PostuladosDtoJdbcNoEncontradoException {
		List<ReporteActualizacionConocDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		// Si se seleccionó en los dos combos TODAS
		// Todas las Facultades y convocatoria seleccionada
			try {
				if(convocatoria.equals(GeneralesConstantes.APP_ID_BASE.toString())){
					convocatoria="%";
				}
				if(carrera.equals(GeneralesConstantes.APP_ID_BASE.toString())){
					carrera="%";
				}
				StringBuilder sbSql = new StringBuilder();
				sbSql.append(" SELECT crr.");
				sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" as detalle");
				sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_NIVEL);sbSql.append(" as nivel");
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" as identificacion");
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" as apellido1");
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(" as apellido2");
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(" as nombres");
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);sbSql.append(" as mail");
				sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);sbSql.append(" as telefono");
				sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_MCTT_ESTADISTICO);sbSql.append(" as mecanismo");
				sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" AS proceso");
				sbSql.append(" , cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);
				sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
				sbSql.append(" RIGHT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ON prs. ");
				sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = fces. ");sbSql.append(JdbcConstantes.FCES_PRS_ID);
				sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ON fces. ");
				sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = trtt. ");sbSql.append(JdbcConstantes.TRTT_ID);
				sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ON trtt. ");
				sbSql.append(JdbcConstantes.TRTT_CNV_ID);sbSql.append(" = cnv. ");sbSql.append(JdbcConstantes.CNV_ID);
				sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ON trtt. ");
				sbSql.append(JdbcConstantes.TRTT_CARRERA);sbSql.append(" = crr. ");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
				sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ON crr. ");
				sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append(" = fcl. ");sbSql.append(JdbcConstantes.FCL_ID);
				
				sbSql.append(" WHERE crr.");
				sbSql.append(JdbcConstantes.CRR_DESCRIPCION);sbSql.append(" LIKE ? ");
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);sbSql.append(" LIKE ? ");
				sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
				sbSql.append(" AND crr.crr_id IN ");
				sbSql.append("(SELECT DISTINCT crr.crr_id from carrera crr, rol_flujo_carrera roflcr, usuario_rol usro, usuario usr ");  
				sbSql.append(" WHERE usr.usr_id =  usro.usr_id  AND  usro.usro_id =  roflcr.usro_id "); 
				sbSql.append(" AND  crr.crr_id =  roflcr.crr_id AND  usr.usr_identificacion LIKE '");
				sbSql.append(usuario.getUsrIdentificacion());sbSql.append("') ");
				sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
				sbSql.append(" ORDER BY detalle, apellido1");
				
				con = ds.getConnection();
				pstmt = con.prepareStatement(sbSql.toString());
				pstmt.setString(1, carrera);
				pstmt.setString(2, convocatoria);
				rs = pstmt.executeQuery();
				retorno = new ArrayList<ReporteActualizacionConocDto>();
				while(rs.next()){
					retorno.add((ReporteActualizacionConocDto)transformarResultSetADto(rs,2));
				}
			} catch (SQLException e) {
				try {
					con.close();
				} catch (Exception e2) {
				}
				throw new PostuladosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.exception",convocatoria)));
			} catch (Exception e) {
				try {
					con.close();
				} catch (Exception e2) {
				}
				throw new PostuladosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.exception",convocatoria)));
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
				throw new PostuladosDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.no.result.exception")));
			}	
			return retorno;						
					
	
	}
	
	@Override
	public List<ReporteEvaluadosDto> listaEvaluadosXCarreraXConvocatoriaXDirector(
			String convocatoria, String carrera, Usuario usuario)
			throws EvaluadosDtoJdbcException,
			EvaluadosDtoJdbcNoEncontradoException  {
		List<ReporteEvaluadosDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		// Todas las Facultades y convocatoria seleccionada
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT crr.");
			sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" AS detalle");
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_NIVEL);sbSql.append(" AS nivel");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" AS identificacion");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" AS apellido1");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(" AS apellido2");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(" AS nombres");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);sbSql.append(" AS mail");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);sbSql.append(" AS telefono");
			sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_MCTT_ESTADISTICO);sbSql.append(" AS mecanismo");
//			sbSql.append(" , CASE WHEN astt.");sbSql.append(JdbcConstantes.ASTT_TEMA_TRABAJO);sbSql.append(" IS NULL ");
			sbSql.append(" THEN ");sbSql.append("'N/A'");
//			sbSql.append(" ELSE astt."); sbSql.append(JdbcConstantes.ASTT_TEMA_TRABAJO);sbSql.append(" END AS tema");
//			sbSql.append(" , CASE WHEN astt.");sbSql.append(JdbcConstantes.ASTT_TUTOR);sbSql.append(" IS NULL ");
			sbSql.append(" THEN ");sbSql.append("'N/A'");
//			sbSql.append(" ELSE astt."); sbSql.append(JdbcConstantes.ASTT_TUTOR);sbSql.append(" END AS tutor");
			sbSql.append(" , mctt."); sbSql.append(JdbcConstantes.MCTT_DESCRIPCION);sbSql.append(" AS mecanismo_asignado");
			sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" AS proceso");
			sbSql.append(" FROM ");
				
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" RIGHT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ON prs.");
			sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ON fces.");
			sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ON trtt.");
			sbSql.append(JdbcConstantes.TRTT_CNV_ID);sbSql.append(" = cnv.");sbSql.append(JdbcConstantes.CNV_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ON trtt.");
			sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr ON trtt.");
			sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = prtr.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ON crr.");
			sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append(" = fcl.");sbSql.append(JdbcConstantes.FCL_ID);
				
//			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_ASIGNACION_TITULACION);sbSql.append(" astt ON prtr.");
//			sbSql.append(JdbcConstantes.PRTR_ID);sbSql.append(" = astt.");sbSql.append(JdbcConstantes.ASTT_PRTR_ID);
//			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION_CARRERA);sbSql.append(" mcttcr ON fces.");
//			sbSql.append(JdbcConstantes.FCES_MCTTCR_ID);sbSql.append(" = mcttcr. ");sbSql.append(JdbcConstantes.MCTTCR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION);sbSql.append(" mctt ON mcttcr.");
//			sbSql.append(JdbcConstantes.MCTTCR_MCTT_ID);sbSql.append(" = mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
			sbSql.append(" WHERE crr.");
			sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" LIKE ? ");
			sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);sbSql.append(" LIKE ? ");
			sbSql.append(" AND prtr.");sbSql.append(JdbcConstantes.PRTR_TIPO_PROCESO);sbSql.append(" = ");
			sbSql.append(ProcesoTramiteConstantes.TIPO_PROCESO_ASIGNADO_MECANISMO_VALUE);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
				
						
			sbSql.append(" AND crr.crr_id IN ");
			sbSql.append("(SELECT DISTINCT crr.crr_id from carrera crr, rol_flujo_carrera roflcr, usuario_rol usro, usuario usr ");  
			sbSql.append(" WHERE usr.usr_id =  usro.usr_id  AND  usro.usro_id =  roflcr.usro_id "); 
			sbSql.append(" AND  crr.crr_id =  roflcr.crr_id AND  usr.usr_identificacion LIKE '");
			sbSql.append(usuario.getUsrIdentificacion());sbSql.append("') ");
				
			sbSql.append(" ORDER BY crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
					
					
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			if(convocatoria.equals(GeneralesConstantes.APP_ID_BASE.toString())){
				convocatoria="%";
			}
			if(carrera.equals(GeneralesConstantes.APP_ID_BASE.toString())){
				carrera="%";
			}
			pstmt.setString(1, carrera);
			pstmt.setString(2, convocatoria);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ReporteEvaluadosDto>();
			while(rs.next()){
				retorno.add((ReporteEvaluadosDto)transformarResultSetADtoEvaluados(rs));
			}
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new EvaluadosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.evaluados.buscar.por.convocatoria.exception",convocatoria)));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new EvaluadosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.evaluados.buscar.por.convocatoria.exception",convocatoria)));
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
					throw new EvaluadosDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.evaluados.buscar.por.convocatoria.no.result.exception")));
				}	
				return retorno;
		
		
	
	}
	
	@Override
	public List<ReporteEvaluadosDto> listaEvaluadosXFacultadXConvocatoriaXDGA(
			String convocatoria, String facultad)
			throws EvaluadosDtoJdbcException,
			EvaluadosDtoJdbcNoEncontradoException  {
		
		List<ReporteEvaluadosDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		// Todas las Facultades y convocatoria seleccionada
			try {
						StringBuilder sbSql = new StringBuilder();
						sbSql.append(" SELECT DISTINCT crr.");
						sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" AS detalle");
						sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_NIVEL);sbSql.append(" AS nivel");
						sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" AS identificacion");
						sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" AS apellido1");
						sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(" AS apellido2");
						sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(" AS nombres");
						sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);sbSql.append(" AS mail");
						sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);sbSql.append(" AS telefono");
						sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_MCTT_ESTADISTICO);sbSql.append(" AS mecanismo");
//						sbSql.append(" , CASE WHEN astt.");sbSql.append(JdbcConstantes.ASTT_TEMA_TRABAJO);sbSql.append(" IS NULL ");
//						sbSql.append(" THEN ");sbSql.append("'N/A'");
//						sbSql.append(" ELSE astt."); sbSql.append(JdbcConstantes.ASTT_TEMA_TRABAJO);sbSql.append(" END AS tema");
//						sbSql.append(" , CASE WHEN astt.");sbSql.append(JdbcConstantes.ASTT_TUTOR);sbSql.append(" IS NULL ");
//						sbSql.append(" THEN ");sbSql.append("'N/A'");
//						sbSql.append(" ELSE astt."); sbSql.append(JdbcConstantes.ASTT_TUTOR);sbSql.append(" END AS tutor");
						sbSql.append(" , mctt."); sbSql.append(JdbcConstantes.MCTT_DESCRIPCION);sbSql.append(" AS mecanismo_asignado");
						sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" AS proceso");
						sbSql.append(" FROM ");
						
						sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
						sbSql.append(" RIGHT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ON prs.");
						sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
						sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ON fces.");
						sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
						sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ON trtt.");
						sbSql.append(JdbcConstantes.TRTT_CNV_ID);sbSql.append(" = cnv.");sbSql.append(JdbcConstantes.CNV_ID);
						sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ON trtt.");
						sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
						sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr ON trtt.");
						sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = prtr.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
						sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ON crr.");
						sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append(" = fcl.");sbSql.append(JdbcConstantes.FCL_ID);
						
//						sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_ASIGNACION_TITULACION);sbSql.append(" astt ON prtr.");
//						sbSql.append(JdbcConstantes.PRTR_ID);sbSql.append(" = astt.");sbSql.append(JdbcConstantes.ASTT_PRTR_ID);
//						sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION_CARRERA);sbSql.append(" mcttcr ON fces.");
//						sbSql.append(JdbcConstantes.FCES_MCTTCR_ID);sbSql.append(" = mcttcr. ");sbSql.append(JdbcConstantes.MCTTCR_ID);
//						sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION);sbSql.append(" mctt ON mcttcr.");
//						sbSql.append(JdbcConstantes.MCTTCR_MCTT_ID);sbSql.append(" = mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
						sbSql.append(" WHERE fcl.");
						sbSql.append(JdbcConstantes.FCL_DESCRIPCION);sbSql.append(" LIKE ? ");
						sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);sbSql.append(" LIKE ? ");
						sbSql.append(" AND prtr.");sbSql.append(JdbcConstantes.PRTR_TIPO_PROCESO);sbSql.append(" = ");
						sbSql.append(ProcesoTramiteConstantes.TIPO_PROCESO_ASIGNADO_MECANISMO_VALUE);
						sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
						sbSql.append(" ORDER BY crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
						sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
						
						
						con = ds.getConnection();
						pstmt = con.prepareStatement(sbSql.toString());
						if(convocatoria.equals(GeneralesConstantes.APP_ID_BASE.toString())){
							convocatoria="%";
						}
						if(facultad.equals(GeneralesConstantes.APP_ID_BASE.toString())){
							facultad="%";
						}
						pstmt.setString(1, facultad);
						pstmt.setString(2, convocatoria);
						rs = pstmt.executeQuery();
						retorno = new ArrayList<ReporteEvaluadosDto>();
						while(rs.next()){
							retorno.add((ReporteEvaluadosDto)transformarResultSetADtoEvaluados(rs));
						}
					} catch (SQLException e) {
						try {
							con.close();
						} catch (Exception e2) {
						}
						throw new EvaluadosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.evaluados.buscar.por.convocatoria.exception",convocatoria)));
					} catch (Exception e) {
						try {
							con.close();
						} catch (Exception e2) {
						}
						throw new EvaluadosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.evaluados.buscar.por.convocatoria.exception",convocatoria)));
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
					throw new EvaluadosDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.evaluados.buscar.por.convocatoria.no.result.exception")));
				}	
				return retorno;
			
	
	}
	
	
	@Override
	public List<ReporteValidadosDto> listaValidadosXSecretaria(
			String convocatoria, String carrera, Usuario usuario)
			throws PostuladosDtoJdbcException,
			PostuladosDtoJdbcNoEncontradoException {
		
		List<ReporteValidadosDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			if(convocatoria.equals(GeneralesConstantes.APP_ID_BASE.toString())){
				convocatoria="%";
			}
			if(carrera.equals(GeneralesConstantes.APP_ID_BASE.toString())){
				carrera="%";
			}
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT crr.");
			sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" as detalle");
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_NIVEL);sbSql.append(" as nivel");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" as identificacion");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" as apellido1");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(" as apellido2");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(" as nombres");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);sbSql.append(" as mail");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);sbSql.append(" as telefono");
			sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_MCTT_ESTADISTICO);sbSql.append(" as mecanismo");
			sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_OBSERVACION);sbSql.append(" as validacion");
			sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" AS proceso");
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" RIGHT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ON prs.");
			sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ON fces.");
			sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ON trtt.");
			sbSql.append(JdbcConstantes.TRTT_CNV_ID);sbSql.append(" = cnv.");sbSql.append(JdbcConstantes.CNV_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ON trtt.");
			sbSql.append(JdbcConstantes.TRTT_CARRERA);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr ON trtt.");
			sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = prtr.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ON crr.");
			sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append(" = fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			
			sbSql.append(" WHERE crr.");
			sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" LIKE ? ");
			sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);sbSql.append(" LIKE ? ");
			sbSql.append(" AND prtr.");sbSql.append(JdbcConstantes.PRTR_TIPO_PROCESO);sbSql.append(" = ");
			sbSql.append(ProcesoTramiteConstantes.TIPO_PROCESO_VALIDACION_TIPO_IDONEIDAD_VALUE);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = "); sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE); 
			
			sbSql.append(" AND crr.crr_id IN ");
			sbSql.append("(SELECT DISTINCT crr.crr_id from carrera crr, rol_flujo_carrera roflcr, usuario_rol usro, usuario usr ");  
			sbSql.append(" WHERE usr.usr_id =  usro.usr_id  AND  usro.usro_id =  roflcr.usro_id "); 
			sbSql.append(" AND  crr.crr_id =  roflcr.crr_id AND  usr.usr_identificacion LIKE '");
			sbSql.append(usuario.getUsrIdentificacion());sbSql.append("') ");
			
			
			
			sbSql.append(" ORDER BY crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, carrera);
			pstmt.setString(2, convocatoria);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ReporteValidadosDto>();
			while(rs.next()){
				retorno.add((ReporteValidadosDto)transformarResultSetADto(rs,4));
			}
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new PostuladosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.exception",convocatoria)));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new PostuladosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.exception",convocatoria)));
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
		throw new PostuladosDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.no.result.exception")));
	}	
	return retorno;
		
	
	
	}
	
	@Override
	public List<ReporteValidadosDto> listaValidadosXDGA(String convocatoria,
			String facultad) throws PostuladosDtoJdbcException,
			PostuladosDtoJdbcNoEncontradoException {
		List<ReporteValidadosDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			if(convocatoria.equals(GeneralesConstantes.APP_ID_BASE.toString())){
				convocatoria="%";
			}
			if(facultad.equals(GeneralesConstantes.APP_ID_BASE.toString())){
				facultad="%";
			}
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT crr.");
			sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" as detalle");
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_NIVEL);sbSql.append(" as nivel");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" as identificacion");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" as apellido1");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(" as apellido2");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(" as nombres");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);sbSql.append(" as mail");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);sbSql.append(" as telefono");
			sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_MCTT_ESTADISTICO);sbSql.append(" as mecanismo");
			sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_OBSERVACION);sbSql.append(" as validacion");
			sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" AS proceso");
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" RIGHT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ON prs. ");
			sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = fces. ");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ON fces. ");
			sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = trtt. ");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ON trtt. ");
			sbSql.append(JdbcConstantes.TRTT_CNV_ID);sbSql.append(" = cnv. ");sbSql.append(JdbcConstantes.CNV_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ON trtt. ");
			sbSql.append(JdbcConstantes.TRTT_CARRERA);sbSql.append(" = crr. ");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr ON trtt. ");
			sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = prtr. ");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ON crr. ");
			sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append(" = fcl. ");sbSql.append(JdbcConstantes.FCL_ID);
			
			sbSql.append(" WHERE fcl.");
			sbSql.append(JdbcConstantes.FCL_DESCRIPCION);sbSql.append(" LIKE ? ");
			sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);sbSql.append(" LIKE ? ");
			sbSql.append(" AND prtr.");sbSql.append(JdbcConstantes.PRTR_TIPO_PROCESO);sbSql.append(" = ");
			sbSql.append(ProcesoTramiteConstantes.TIPO_PROCESO_VALIDACION_TIPO_IDONEIDAD_VALUE);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" ORDER BY crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, facultad);
			pstmt.setString(2, convocatoria);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ReporteValidadosDto>();
			while(rs.next()){
				retorno.add((ReporteValidadosDto)transformarResultSetADto(rs,4));
			}
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new PostuladosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.exception",convocatoria)));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new PostuladosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.exception",convocatoria)));
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
		throw new PostuladosDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.registrados.buscar.por.convocatoria.no.result.exception")));
	}	
	return retorno;
}
	
	
	@Override
	public List<ReporteEvaluadosDto> listaEvaluadosExamenComplexivoXCarreraXConvocatoriaXDirector(
			String convocatoria, String carrera, Usuario usuario)
			throws EvaluadosDtoJdbcException,
			EvaluadosDtoJdbcNoEncontradoException  {
		List<ReporteEvaluadosDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		// Todas las Facultades y convocatoria seleccionada
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT crr.");
			sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" AS detalle");
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_NIVEL);sbSql.append(" AS nivel");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" AS identificacion");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" AS apellido1");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(" AS apellido2");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(" AS nombres");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);sbSql.append(" AS mail");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);sbSql.append(" AS telefono");
			sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_MCTT_ESTADISTICO);sbSql.append(" AS mecanismo");
			sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" AS proceso");
//			sbSql.append(" , CASE WHEN astt.");sbSql.append(JdbcConstantes.ASTT_TEMA_TRABAJO);sbSql.append(" IS NULL ");
//			sbSql.append(" THEN ");sbSql.append("'N/A'");
//			sbSql.append(" ELSE astt."); sbSql.append(JdbcConstantes.ASTT_TEMA_TRABAJO);sbSql.append(" END AS tema");
//			sbSql.append(" , CASE WHEN astt.");sbSql.append(JdbcConstantes.ASTT_TUTOR);sbSql.append(" IS NULL ");
//			sbSql.append(" THEN ");sbSql.append("'N/A'");
//			sbSql.append(" ELSE astt."); sbSql.append(JdbcConstantes.ASTT_TUTOR);sbSql.append(" END AS tutor");
			sbSql.append(" , mctt."); sbSql.append(JdbcConstantes.MCTT_DESCRIPCION);sbSql.append(" AS mecanismo_asignado");
			sbSql.append(" FROM ");
				
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" RIGHT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ON prs.");
			sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ON fces.");
			sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ON trtt.");
			sbSql.append(JdbcConstantes.TRTT_CNV_ID);sbSql.append(" = cnv.");sbSql.append(JdbcConstantes.CNV_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ON trtt.");
			sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr ON trtt.");
			sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = prtr.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ON crr.");
			sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append(" = fcl.");sbSql.append(JdbcConstantes.FCL_ID);
				
//			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_ASIGNACION_TITULACION);sbSql.append(" astt ON prtr.");
//			sbSql.append(JdbcConstantes.PRTR_ID);sbSql.append(" = astt.");sbSql.append(JdbcConstantes.ASTT_PRTR_ID);
//			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION_CARRERA);sbSql.append(" mcttcr ON fces.");
//			sbSql.append(JdbcConstantes.FCES_MCTTCR_ID);sbSql.append(" = mcttcr. ");sbSql.append(JdbcConstantes.MCTTCR_ID);
//			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION);sbSql.append(" mctt ON mcttcr.");
//			sbSql.append(JdbcConstantes.MCTTCR_MCTT_ID);sbSql.append(" = mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
			sbSql.append(" WHERE crr.");
			sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" LIKE ? ");
			sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);sbSql.append(" LIKE ? ");
			sbSql.append(" AND prtr.");sbSql.append(JdbcConstantes.PRTR_TIPO_PROCESO);sbSql.append(" = ");
			sbSql.append(ProcesoTramiteConstantes.TIPO_PROCESO_APTO_EVALUADO_VALUE);
//			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_SUB_ID);sbSql.append(" IS null ");
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");
			sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);	
			sbSql.append(" AND mctt.");sbSql.append(JdbcConstantes.MCTT_ID);sbSql.append(" = ");
			sbSql.append(MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_VALUE);
			
			sbSql.append(" AND crr.crr_id IN ");
			sbSql.append("(SELECT DISTINCT crr.crr_id from carrera crr, rol_flujo_carrera roflcr, usuario_rol usro, usuario usr ");  
			sbSql.append(" WHERE usr.usr_id =  usro.usr_id  AND  usro.usro_id =  roflcr.usro_id "); 
			sbSql.append(" AND  crr.crr_id =  roflcr.crr_id AND  usr.usr_identificacion LIKE '");
			sbSql.append(usuario.getUsrIdentificacion());sbSql.append("') ");
				
			sbSql.append(" ORDER BY crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
					
					
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			if(convocatoria.equals(GeneralesConstantes.APP_ID_BASE.toString())){
				convocatoria="%";
			}
			if(carrera.equals(GeneralesConstantes.APP_ID_BASE.toString())){
				carrera="%";
			}
			pstmt.setString(1, carrera);
			pstmt.setString(2, convocatoria);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ReporteEvaluadosDto>();
			while(rs.next()){
				retorno.add((ReporteEvaluadosDto)transformarResultSetADtoEvaluados(rs));
			}
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new EvaluadosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.evaluados.buscar.por.convocatoria.exception",convocatoria)));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new EvaluadosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.evaluados.buscar.por.convocatoria.exception",convocatoria)));
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
					throw new EvaluadosDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.evaluados.buscar.por.convocatoria.no.result.exception")));
				}	
				return retorno;
		
		
	
	}
	
	
	/**Método que devuelve los resultados del examen complexivo para los directores
	 * 
	 */
	@Override
	public List<EstudianteDetalleComplexivoDto> listaResultadosExamenComplexivoXCarreraXConvocatoriaXDirector(
			Integer convocatoria, String carrera, Usuario usuario)
			throws EstudianteDetalleComplexivoDtoNoEncontradoException,
			EstudianteDetalleComplexivoDtoException  {
		List<EstudianteDetalleComplexivoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT crr.");
			sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" AS detalle");
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_NIVEL);sbSql.append(" AS nivel");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" AS identificacion");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" AS apellido1");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(" AS apellido2");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(" AS nombres");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);sbSql.append(" AS mail");
//			sbSql.append(" , asno.");sbSql.append(JdbcConstantes.ASNO_COMPLEXIVO_FINAL);sbSql.append(" AS complexivo");
//			sbSql.append(" , asno.");sbSql.append(JdbcConstantes.ASNO_CMP_GRACIA_FINAL);sbSql.append(" AS complexivoFinal");
			sbSql.append(" , fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);sbSql.append(" AS facultad");
			sbSql.append(" FROM ");
				
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asno ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION_CARRERA);sbSql.append(" mcttcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION);sbSql.append(" mctt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ");
			
			sbSql.append(" WHERE crr.");
			sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" LIKE ? ");
			if(convocatoria.equals(GeneralesConstantes.APP_ID_BASE.toString())){
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" > ? ");
			}else{
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ? ");	
			}
			
//			sbSql.append(" AND prtr.");sbSql.append(JdbcConstantes.PRTR_TIPO_PROCESO);sbSql.append(" = ");
//			sbSql.append(ProcesoTramiteConstantes.TIPO_PROCESO_COMPLEX_FINAL_CARGADO_VALUE);
			sbSql.append("  AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");
			sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);	
			sbSql.append(" AND mctt.");sbSql.append(JdbcConstantes.MCTT_ID);sbSql.append(" = ");
			sbSql.append(MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_VALUE);
			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
//			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_MCTTCR_ID);sbSql.append(" = mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_ID);
//			sbSql.append(" AND mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_MCTT_ID);sbSql.append(" = mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
//			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = prtr.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
//			sbSql.append(" AND prtr.");sbSql.append(JdbcConstantes.PRTR_ID);sbSql.append(" = asno.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID);
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append(" = fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
			
			sbSql.append(" AND crr.crr_id IN ");
			sbSql.append("(SELECT DISTINCT crr.crr_id from carrera crr, rol_flujo_carrera roflcr, usuario_rol usro, usuario usr ");  
			sbSql.append(" WHERE usr.usr_id =  usro.usr_id  AND  usro.usro_id =  roflcr.usro_id "); 
			sbSql.append(" AND  crr.crr_id =  roflcr.crr_id AND  usr.usr_identificacion LIKE '");
			sbSql.append(usuario.getUsrIdentificacion());sbSql.append("') ");
				
			sbSql.append(" ORDER BY crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
					
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
//			if(convocatoria.equals(GeneralesConstantes.APP_ID_BASE.toString())){
//				convocatoria="%";
//			}
			if(carrera.equals(GeneralesConstantes.APP_ID_BASE.toString())){
				carrera="%";
			}
			pstmt.setString(1, carrera);
			pstmt.setInt(2, Integer.valueOf(convocatoria));
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<EstudianteDetalleComplexivoDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoExamenCOmplexivo(rs));
			}
		} catch (SQLException e) {
			throw new EstudianteDetalleComplexivoDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.evaluados.buscar.por.convocatoria.exception",convocatoria)));
		} catch (Exception e) {
			e.printStackTrace();
			throw new EstudianteDetalleComplexivoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.evaluados.buscar.por.convocatoria.exception",convocatoria)));
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
					throw new EstudianteDetalleComplexivoDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.evaluados.buscar.por.convocatoria.no.result.exception")));
				}	
				return retorno;
		
		
	
	}
	
	/**Método que devuelve los resultados del examen complexivo para los directores
	 * 
	 */
	@Override
	public List<EstudianteDetalleComplexivoDto> listaResultadosExamenComplexivoGraciaXCarreraXConvocatoriaXDirector(
			String convocatoria, String carrera, Usuario usuario)
			throws EstudianteDetalleComplexivoDtoNoEncontradoException,
			EstudianteDetalleComplexivoDtoException  {
		List<EstudianteDetalleComplexivoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT crr.");
			sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" AS detalle");
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_NIVEL);sbSql.append(" AS nivel");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" AS identificacion");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" AS apellido1");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(" AS apellido2");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(" AS nombres");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);sbSql.append(" AS mail");
//			sbSql.append(" , asno.");sbSql.append(JdbcConstantes.ASNO_COMPLEXIVO_FINAL);sbSql.append(" AS complexivo");
//			sbSql.append(" , asno.");sbSql.append(JdbcConstantes.ASNO_CMP_GRACIA_FINAL);sbSql.append(" AS complexivoFinal");
			sbSql.append(" , fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);sbSql.append(" AS facultad");
			sbSql.append(" FROM ");
				
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asno ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION_CARRERA);sbSql.append(" mcttcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION);sbSql.append(" mctt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ");
			
			sbSql.append(" WHERE crr.");
			sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" LIKE ? ");
			if(convocatoria.equals(GeneralesConstantes.APP_ID_BASE.toString())){
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" > ? ");
			}else{
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ? ");	
			}
//			sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);sbSql.append(" LIKE ? ");
			sbSql.append(" AND prtr.");sbSql.append(JdbcConstantes.PRTR_TIPO_PROCESO);sbSql.append(" = ");
			sbSql.append(ProcesoTramiteConstantes.TIPO_PROCESO_COMPLEX_FINAL_GRACIA_CARGADO_VALUE);
			sbSql.append("  AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");
			sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);	
			sbSql.append(" AND mctt.");sbSql.append(JdbcConstantes.MCTT_ID);sbSql.append(" = ");
			sbSql.append(MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_VALUE);
			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
//			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_MCTTCR_ID);sbSql.append(" = mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_ID);
//			sbSql.append(" AND mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_MCTT_ID);sbSql.append(" = mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
//			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = prtr.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
//			sbSql.append(" AND prtr.");sbSql.append(JdbcConstantes.PRTR_ID);sbSql.append(" = asno.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID);
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append(" = fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			
			sbSql.append(" AND crr.crr_id IN ");
			sbSql.append("(SELECT DISTINCT crr.crr_id from carrera crr, rol_flujo_carrera roflcr, usuario_rol usro, usuario usr ");  
			sbSql.append(" WHERE usr.usr_id =  usro.usr_id  AND  usro.usro_id =  roflcr.usro_id "); 
			sbSql.append(" AND  crr.crr_id =  roflcr.crr_id AND  usr.usr_identificacion LIKE '");
			
			sbSql.append(usuario.getUsrIdentificacion());sbSql.append("') ");
			sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
			sbSql.append(" ORDER BY crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
					
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
//			if(convocatoria.equals(GeneralesConstantes.APP_ID_BASE.toString())){
//				convocatoria="%";
//			}
			if(carrera.equals(GeneralesConstantes.APP_ID_BASE.toString())){
				carrera="%";
			}
			pstmt.setString(1, carrera);
			pstmt.setInt(2, Integer.valueOf(convocatoria));
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<EstudianteDetalleComplexivoDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoExamenCOmplexivo(rs));
			}
		} catch (SQLException e) {
			throw new EstudianteDetalleComplexivoDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.evaluados.buscar.por.convocatoria.exception",convocatoria)));
		} catch (Exception e) {
			throw new EstudianteDetalleComplexivoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.evaluados.buscar.por.convocatoria.exception",convocatoria)));
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
					throw new EstudianteDetalleComplexivoDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.evaluados.buscar.por.convocatoria.no.result.exception")));
				}	
				return retorno;
		
		
	
	}
	
	
	/**Método que devuelve los resultados de los exámenes complexivos para la DGA
	 * 
	 */
	@Override
	public List<EstudianteDetalleComplexivoDto> listaResultadosExamenComplexivoDGA(
			Integer convocatoria, Integer carrera, Integer tipo, Integer facultad)
			throws EstudianteDetalleComplexivoDtoNoEncontradoException,
			EstudianteDetalleComplexivoDtoException  {
		List<EstudianteDetalleComplexivoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT crr.");
			sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" AS detalle");
			sbSql.append(" , cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);sbSql.append(" AS convocatoria");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" AS identificacion");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" AS apellido1");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(" AS apellido2");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(" AS nombres");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);sbSql.append(" AS mail");
//			sbSql.append(" ,CASE WHEN asno.");sbSql.append(JdbcConstantes.ASNO_COMPLEXIVO_TEORICO);sbSql.append("  IS NULL THEN ");
//			sbSql.append(GeneralesConstantes.APP_ID_BASE);
//			sbSql.append(" ELSE ");sbSql.append(JdbcConstantes.ASNO_COMPLEXIVO_TEORICO);
//			sbSql.append(" END AS complexivoTeorico");
//			sbSql.append(" ,CASE WHEN asno.");sbSql.append(JdbcConstantes.ASNO_COMPLEXIVO_PRACTICO);sbSql.append("  IS NULL THEN ");
//			sbSql.append(GeneralesConstantes.APP_ID_BASE);
//			sbSql.append(" ELSE ");sbSql.append(JdbcConstantes.ASNO_COMPLEXIVO_PRACTICO);
//			sbSql.append(" END AS complexivoPractico");
//			sbSql.append(" ,CASE WHEN asno.");sbSql.append(JdbcConstantes.ASNO_CMP_GRACIA_TEORICO);sbSql.append("  IS NULL THEN ");
//			sbSql.append(GeneralesConstantes.APP_ID_BASE);
//			sbSql.append(" ELSE ");sbSql.append(JdbcConstantes.ASNO_CMP_GRACIA_TEORICO);
//			sbSql.append(" END AS complexivoGraciaTeorico");
//			sbSql.append(" ,CASE WHEN asno.");sbSql.append(JdbcConstantes.ASNO_CMP_GRACIA_PRACTICO);sbSql.append("  IS NULL THEN ");
//			sbSql.append(GeneralesConstantes.APP_ID_BASE);
//			sbSql.append(" ELSE ");sbSql.append(JdbcConstantes.ASNO_CMP_GRACIA_PRACTICO);
//			sbSql.append(" END AS complexivoGraciaPractico");
//			sbSql.append(" , asno.");sbSql.append(JdbcConstantes.ASNO_COMPLEXIVO_FINAL);sbSql.append(" AS complexivo");
//			sbSql.append(" ,CASE WHEN asno.");sbSql.append(JdbcConstantes.ASNO_CMP_GRACIA_FINAL);sbSql.append("  IS NULL THEN ");
//			sbSql.append(GeneralesConstantes.APP_ID_BASE);
//			sbSql.append(" ELSE ");sbSql.append(JdbcConstantes.ASNO_CMP_GRACIA_FINAL);
//			sbSql.append(" END AS complexivoFinal");
//			sbSql.append(" , fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);sbSql.append(" AS facultad");
//			sbSql.append(" , mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_PORCENTAJE);sbSql.append(" AS porcentaje");
			sbSql.append(" FROM ");
				
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asno ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION_CARRERA);sbSql.append(" mcttcr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION);sbSql.append(" mctt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ");
			
			sbSql.append(" WHERE ");
			if(facultad==GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" > ? ");
			}else{
				sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");	
			}
			sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ? ");
//			sbSql.append(" AND prtr.");sbSql.append(JdbcConstantes.PRTR_TIPO_PROCESO);sbSql.append(" = ");
//			sbSql.append(ProcesoTramiteConstantes.TIPO_PROCESO_COMPLEX_FINAL_CARGADO_VALUE);
//			sbSql.append("  AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");
//			sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);	
			sbSql.append(" AND mctt.");sbSql.append(JdbcConstantes.MCTT_ID);sbSql.append(" = ");
			sbSql.append(MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_VALUE);
			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
//			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_MCTTCR_ID);sbSql.append(" = mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_ID);
//			sbSql.append(" AND mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_MCTT_ID);sbSql.append(" = mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
//			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = prtr.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
//			sbSql.append(" AND prtr.");sbSql.append(JdbcConstantes.PRTR_ID);sbSql.append(" = asno.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID);
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append(" = fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
			
			sbSql.append(" ORDER BY crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
					
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, carrera);
			pstmt.setInt(2, convocatoria);
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<EstudianteDetalleComplexivoDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoExamenCOmplexivoDGA(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new EstudianteDetalleComplexivoDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.evaluados.buscar.por.convocatoria.exception",convocatoria)));
		} catch (Exception e) {
			e.printStackTrace();
			throw new EstudianteDetalleComplexivoDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.evaluados.buscar.por.convocatoria.exception",convocatoria)));
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
					throw new EstudianteDetalleComplexivoDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.evaluados.buscar.por.convocatoria.no.result.exception")));
				}	
				return retorno;
		
		
	
	}
	
	
	@Override
	public List<ReporteEvaluadosDto> listaEvaluadosOtrosMecanismosXCarreraXConvocatoriaXDirector(
			String convocatoria, String carrera, Usuario usuario)
			throws EvaluadosDtoJdbcException,
			EvaluadosDtoJdbcNoEncontradoException  {
		List<ReporteEvaluadosDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		// Todas las Facultades y convocatoria seleccionada
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT crr.");
			sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" AS detalle");
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_NIVEL);sbSql.append(" AS nivel");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" AS identificacion");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" AS apellido1");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(" AS apellido2");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(" AS nombres");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);sbSql.append(" AS mail");
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);sbSql.append(" AS telefono");
			sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_MCTT_ESTADISTICO);sbSql.append(" AS mecanismo");
			sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" AS proceso");
//			sbSql.append(" , CASE WHEN astt.");sbSql.append(JdbcConstantes.ASTT_TEMA_TRABAJO);sbSql.append(" IS NULL ");
//			sbSql.append(" THEN ");sbSql.append("'N/A'");
//			sbSql.append(" ELSE astt."); sbSql.append(JdbcConstantes.ASTT_TEMA_TRABAJO);sbSql.append(" END AS tema");
//			sbSql.append(" , CASE WHEN astt.");sbSql.append(JdbcConstantes.ASTT_TUTOR);sbSql.append(" IS NULL ");
//			sbSql.append(" THEN ");sbSql.append("'N/A'");
//			sbSql.append(" ELSE astt."); sbSql.append(JdbcConstantes.ASTT_TUTOR);sbSql.append(" END AS tutor");
			sbSql.append(" , mctt."); sbSql.append(JdbcConstantes.MCTT_DESCRIPCION);sbSql.append(" AS mecanismo_asignado");
			sbSql.append(" FROM ");
				
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" RIGHT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ON prs.");
			sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ON fces.");
			sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ON trtt.");
			sbSql.append(JdbcConstantes.TRTT_CNV_ID);sbSql.append(" = cnv.");sbSql.append(JdbcConstantes.CNV_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ON trtt.");
			sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr ON trtt.");
			sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = prtr.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ON crr.");
			sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append(" = fcl.");sbSql.append(JdbcConstantes.FCL_ID);
				
//			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_ASIGNACION_TITULACION);sbSql.append(" astt ON prtr.");
//			sbSql.append(JdbcConstantes.PRTR_ID);sbSql.append(" = astt.");sbSql.append(JdbcConstantes.ASTT_PRTR_ID);
//			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION_CARRERA);sbSql.append(" mcttcr ON fces.");
//			sbSql.append(JdbcConstantes.FCES_MCTTCR_ID);sbSql.append(" = mcttcr. ");sbSql.append(JdbcConstantes.MCTTCR_ID);
//			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION);sbSql.append(" mctt ON mcttcr.");
//			sbSql.append(JdbcConstantes.MCTTCR_MCTT_ID);sbSql.append(" = mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
			sbSql.append(" WHERE crr.");
			sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" LIKE ? ");
			sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);sbSql.append(" LIKE ? ");
			sbSql.append(" AND prtr.");sbSql.append(JdbcConstantes.PRTR_TIPO_PROCESO);sbSql.append(" = ");
			sbSql.append(ProcesoTramiteConstantes.TIPO_PROCESO_ASIGNADO_MECANISMO_VALUE);
//			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_SUB_ID);sbSql.append(" IS null ");
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");
			sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);	
			sbSql.append(" AND mctt.");sbSql.append(JdbcConstantes.MCTT_ID);sbSql.append(" <> ");
			sbSql.append(MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_VALUE);	
						
			sbSql.append(" AND crr.crr_id IN ");
			sbSql.append("(SELECT DISTINCT crr.crr_id from carrera crr, rol_flujo_carrera roflcr, usuario_rol usro, usuario usr ");  
			sbSql.append(" WHERE usr.usr_id =  usro.usr_id  AND  usro.usro_id =  roflcr.usro_id "); 
			sbSql.append(" AND  crr.crr_id =  roflcr.crr_id AND  usr.usr_identificacion LIKE '");
			sbSql.append(usuario.getUsrIdentificacion());sbSql.append("') ");
				
			sbSql.append(" ORDER BY crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
					
					
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			if(convocatoria.equals(GeneralesConstantes.APP_ID_BASE.toString())){
				convocatoria="%";
			}
			if(carrera.equals(GeneralesConstantes.APP_ID_BASE.toString())){
				carrera="%";
			}
			pstmt.setString(1, carrera);
			pstmt.setString(2, convocatoria);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<ReporteEvaluadosDto>();
			while(rs.next()){
				retorno.add((ReporteEvaluadosDto)transformarResultSetADtoEvaluados(rs));
			}
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new EvaluadosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.evaluados.buscar.por.convocatoria.exception",convocatoria)));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new EvaluadosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.evaluados.buscar.por.convocatoria.exception",convocatoria)));
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
					throw new EvaluadosDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Reportes.evaluados.buscar.por.convocatoria.no.result.exception")));
				}	
				return retorno;
		
		
	
	}
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */

	private ReporteActualizacionConocDto transformarResulSetAreportePostuladoDto(ResultSet rs) throws SQLException{
		
		ReporteActualizacionConocDto retorno = new ReporteActualizacionConocDto();

		retorno.setCarrera(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setFacultad(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
		retorno.setConvocatoria(rs.getString(JdbcConstantes.CNV_DESCRIPCION));
		retorno.setCedula(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setApellido1(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setApellido2(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setMail(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
		retorno.setTelefono(rs.getString(JdbcConstantes.PRS_TELEFONO));
		retorno.setEstadoProceso(TramiteTituloConstantes.getEstadoProcesoEstudiante(rs.getInt(JdbcConstantes.TRTT_ESTADO_PROCESO)));
		
//		switch (rs.getInt(JdbcConstantes.VLD_RSL_ACT_CONOCIMIENTO)) {
//			case 0:
//					retorno.setActualizacionConocimiento(ValidacionConstantes.SI_DEBE_REALIZAR_ACTUALIZACION_CONOCIMIENTOS_LABEL);	
//				break;
//			case 1:
//				retorno.setActualizacionConocimiento(ValidacionConstantes.NO_DEBE_REALIZAR_ACTUALIZACION_CONOCIMIENTOS_LABEL );	
//			break;
//			case 2:
//				retorno.setActualizacionConocimiento(ValidacionConstantes.ACTUALIZACION_CONOCIMIENTOS_PAGAR_LABEL);	
//			break;
//			case 3:
//				retorno.setActualizacionConocimiento(ValidacionConstantes.ACTUALIZACION_CONOCIMIENTOS_CANCELADA_LABEL);	
//			break;
//			default:
//				break;
//			}
		
		String aux = rs.getString("aptitud");
		
		if (aux == null) {
			retorno.setAptAproboActualizar(AptitudConstantes.NO_APLICA_ACTUALIZAR_LABEL);
		}else {
			retorno.setAptAproboActualizar(rs.getString("aptitud"));
		}

		return retorno;		
	}
	
	
	private Object transformarResultSetADto(ResultSet rs,int i) throws SQLException{
		if(i==1){
			EstudianteEstadoJdbcDto retorno = new EstudianteEstadoJdbcDto();
			retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
			retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
			retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
			retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
			retorno.setPrsMailPersonal(rs.getString(JdbcConstantes.PRS_MAIL_PERSONAL));
			retorno.setPrsMailPersonal(rs.getString(JdbcConstantes.PRS_MAIL_PERSONAL));
			retorno.setPrsTelefono(rs.getString(JdbcConstantes.PRS_TELEFONO));
			retorno.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
			retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
			retorno.setTrttEstadoProceso(rs.getInt(JdbcConstantes.TRTT_ESTADO_PROCESO));
			retorno.setCnvDescripcion(rs.getString(JdbcConstantes.CNV_DESCRIPCION));
			if(retorno.getTrttEstadoProceso()>0){
				retorno.setFcesRegTtlSenecyt(rs.getString(JdbcConstantes.FCES_REG_TTL_SENESCYT));	
			}else{
//				retorno.setFcesRegTtlSenecyt(rs.getString(JdbcConstantes.PRTR_REG_TTL_SENECYT));
			}
			
			return retorno;
		}else if(i==2){
			ReporteActualizacionConocDto retorno=new ReporteActualizacionConocDto();
			retorno.setCarrera(rs.getString("detalle"));
			if(rs.getString("nivel").equals(CarreraConstantes.TIPO_NIVEL_CARRERA_TERCER_NIVEL_VALUE)){
				retorno.setNivel(CarreraConstantes.TIPO_NIVEL_CARRERA_TERCER_NIVEL_LABEL);
			}else if(rs.getString("nivel").equals(CarreraConstantes.TIPO_NIVEL_CARRERA_CUARTO_NIVEL_VALUE)){
				retorno.setNivel(CarreraConstantes.TIPO_NIVEL_CARRERA_CUARTO_NIVEL_LABEL);
			}
			retorno.setCedula(rs.getString("identificacion"));
			retorno.setApellido1(rs.getString("apellido1"));
			retorno.setApellido2(rs.getString("apellido2"));
			retorno.setNombres(rs.getString("nombres"));
			retorno.setMail(rs.getString("mail"));
			retorno.setTelefono(rs.getString("telefono"));
			retorno.setConvocatoria(rs.getString(JdbcConstantes.CNV_DESCRIPCION));
			if(rs.getString("mecanismo").equals("0")){
				retorno.setMctt_estadistico(MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_LABEL);
			}else{
				retorno.setMctt_estadistico(MecanismoTitulacionCarreraConstantes.MECANISMO_OTROS_MECANISMOS_LABEL);
			}
			
			return retorno;
		}else if(i==3){
			TotalesDto retorno=new TotalesDto();
			retorno.setCarrera(rs.getString("detalle"));
			retorno.setFacultad(rs.getString("facultad"));
			retorno.setTotalPostulados(rs.getString("conteo"));
			return retorno;
		}else if(i==4){
			ReporteValidadosDto retorno=new ReporteValidadosDto();
			retorno.setCarrera(rs.getString("detalle"));
			if(rs.getString("nivel").equals(CarreraConstantes.TIPO_NIVEL_CARRERA_TERCER_NIVEL_VALUE)){
				retorno.setNivel(CarreraConstantes.TIPO_NIVEL_CARRERA_TERCER_NIVEL_LABEL);
			}else if(rs.getString("nivel").equals(CarreraConstantes.TIPO_NIVEL_CARRERA_CUARTO_NIVEL_VALUE)){
				retorno.setNivel(CarreraConstantes.TIPO_NIVEL_CARRERA_CUARTO_NIVEL_LABEL);
			}
			retorno.setCedula(rs.getString("identificacion"));
			retorno.setApellido1(rs.getString("apellido1"));
			retorno.setApellido2(rs.getString("apellido2"));
			retorno.setNombres(rs.getString("nombres"));
			retorno.setMail(rs.getString("mail"));
			retorno.setTelefono(rs.getString("telefono"));
			if(rs.getString("mecanismo").equals("0")){
				retorno.setMctt_estadistico(MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_LABEL);
			}else{
				retorno.setMctt_estadistico(MecanismoTitulacionCarreraConstantes.MECANISMO_OTROS_MECANISMOS_LABEL);
			}
			retorno.setValidacion(rs.getString("validacion"));
			return retorno;
		}else if(i==5){
			ReporteActualizacionConocDto retorno=new ReporteActualizacionConocDto();
			retorno.setCarrera(rs.getString("detalle"));
			if(rs.getString("nivel").equals(CarreraConstantes.TIPO_NIVEL_CARRERA_TERCER_NIVEL_VALUE)){
				retorno.setNivel(CarreraConstantes.TIPO_NIVEL_CARRERA_TERCER_NIVEL_LABEL);
			}else if(rs.getString("nivel").equals(CarreraConstantes.TIPO_NIVEL_CARRERA_CUARTO_NIVEL_VALUE)){
				retorno.setNivel(CarreraConstantes.TIPO_NIVEL_CARRERA_CUARTO_NIVEL_LABEL);
			}
			retorno.setCedula(rs.getString("identificacion"));
			retorno.setApellido1(rs.getString("apellido1"));
			retorno.setApellido2(rs.getString("apellido2"));
			retorno.setNombres(rs.getString("nombres"));
			retorno.setMail(rs.getString("mail"));
			retorno.setTelefono(rs.getString("telefono"));
			if(rs.getString("mecanismo").equals("0")){
				retorno.setMctt_estadistico(MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_LABEL);
			}else{
				retorno.setMctt_estadistico(MecanismoTitulacionCarreraConstantes.MECANISMO_OTROS_MECANISMOS_LABEL);
			}
			retorno.setObservacionDesactivar(rs.getString("observacion"));
			return retorno;
		}
		
		
		return null;
	}
	
	private Object transformarResultSetADtoEvaluados(ResultSet rs) throws SQLException{
	
		ReporteEvaluadosDto retorno=new ReporteEvaluadosDto();
		retorno.setCarrera(rs.getString("detalle"));
		
		if(rs.getString("nivel").equals(CarreraConstantes.TIPO_NIVEL_CARRERA_TERCER_NIVEL_VALUE)){
			retorno.setNivel(CarreraConstantes.TIPO_NIVEL_CARRERA_TERCER_NIVEL_LABEL);
		}else if(rs.getString("nivel").equals(CarreraConstantes.TIPO_NIVEL_CARRERA_CUARTO_NIVEL_VALUE)){
			retorno.setNivel(CarreraConstantes.TIPO_NIVEL_CARRERA_CUARTO_NIVEL_LABEL);
		}
		
		retorno.setCedula(rs.getString("identificacion"));
		retorno.setApellido1(rs.getString("apellido1"));
		retorno.setApellido2(rs.getString("apellido2"));
		retorno.setNombres(rs.getString("nombres"));
		retorno.setMail(rs.getString("mail"));
		retorno.setTelefono(rs.getString("telefono"));
		retorno.setMctt_estadistico(rs.getString("mecanismo"));
		retorno.setAsttTemaTrabajo(rs.getString("tema"));
		retorno.setAsttTutor(rs.getString("tutor"));
		retorno.setMcttDescripcion(rs.getString("mecanismo_asignado"));
		
		retorno.setTrttId(rs.getInt("proceso"));
		if(rs.getInt("proceso")==TramiteTituloConstantes.ESTADO_PROCESO_APTO_VALUE){
			retorno.setAptitud(TramiteTituloConstantes.ESTADO_PROCESO_APTO_LABEL);
		}else if (rs.getInt("proceso")==TramiteTituloConstantes.ESTADO_PROCESO_NO_APTO_VALUE){
			retorno.setAptitud(TramiteTituloConstantes.ESTADO_PROCESO_NO_APTO_LABEL);
		}else if (rs.getInt("proceso")==TramiteTituloConstantes.ESTADO_PROCESO_APTO_EVALUADO_VALUE){
			retorno.setAptitud(TramiteTituloConstantes.ESTADO_PROCESO_APTO_EVALUADO_LABEL);
		}else if (rs.getInt("proceso")==TramiteTituloConstantes.ESTADO_PROCESO_NO_APTO_EVALUADO_VALUE){
			retorno.setAptitud(TramiteTituloConstantes.ESTADO_PROCESO_NO_APTO_EVALUADO_LABEL);
		}else{
			retorno.setAptitud("Aptitud aún no declarada");
		}
		return retorno;
	}

	private EstudianteDetalleComplexivoDto transformarResultSetADtoExamenCOmplexivo(ResultSet rs) throws SQLException{
		EstudianteDetalleComplexivoDto retorno =new EstudianteDetalleComplexivoDto();
		retorno.setCrrDetalle(rs.getString("detalle"));
		retorno.setPrsIdentificacion(rs.getString("identificacion"));
		retorno.setPrsPrimerApellido(rs.getString("apellido1"));
		retorno.setPrsSegundoApellido(rs.getString("apellido2"));
		retorno.setPrsNombres(rs.getString("nombres"));
		retorno.setFclDescripcion(rs.getString("facultad"));
		retorno.setAsnoComplexivoFinal(rs.getBigDecimal("complexivo"));
		retorno.setAsnoCmpGraciaFinal(rs.getBigDecimal("complexivoFinal"));
		return retorno;
	}
	private EstudianteDetalleComplexivoDto transformarResultSetADtoExamenCOmplexivoDGA(ResultSet rs) throws SQLException{
		
		EstudianteDetalleComplexivoDto retorno =new EstudianteDetalleComplexivoDto();
		retorno.setCnvDescripcion(rs.getString("convocatoria"));
		retorno.setCrrDetalle(rs.getString("detalle"));
		retorno.setPrsIdentificacion(rs.getString("identificacion"));
		retorno.setPrsPrimerApellido(rs.getString("apellido1"));
		retorno.setPrsSegundoApellido(rs.getString("apellido2"));
		retorno.setPrsNombres(rs.getString("nombres"));
		retorno.setFclDescripcion(rs.getString("facultad"));
		retorno.setAsnoComplexivoFinal(rs.getBigDecimal("complexivo"));
		retorno.setAsnoComplexivoTeorico(rs.getBigDecimal("complexivoTeorico"));
		retorno.setAsnoComplexivoPractico(rs.getBigDecimal("complexivoPractico"));
		retorno.setAsnoCmpGraciaTeorico(rs.getBigDecimal("complexivoGraciaTeorico"));
		retorno.setAsnoCmpGraciaPractico(rs.getBigDecimal("complexivoGraciaPractico"));
		retorno.setMcttcrPorcentaje(rs.getInt("porcentaje"));
		retorno.setAsnoCmpGraciaFinal(rs.getBigDecimal("complexivoFinal"));
		return retorno;
	}


}


	

