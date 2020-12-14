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
 24-04-2018		Daniel Albuja				       Emisión Inicial
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
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.CarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteAptitudJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteAptitudOtrosMecanismosJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteAsignacionMecanismoDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteDetalleComplexivoDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.FacultadDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteAsignacionDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteAsignacionDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteValidacionDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteValidacionDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EvaluadosDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EvaluadosDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.ProcesoTramiteServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.TramiteTituloServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.EstudianteAsignacionDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.MecanismoTitulacionCarreraConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.ProcesoTramiteConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.TramiteTituloConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.ValidacionConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.AsignacionTitulacion;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.FichaEstudiante;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.MecanismoCarrera;
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
public class EstudianteAsignacionDtoServicioJdbcImpl implements EstudianteAsignacionDtoServicioJdbc {
	
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
	 * Método que busca las carreras de acuerdo al Evaluador
	 * @param identificacion -identificacion del Evaluador
	 * @return Lista de carreras
	 * @throws SQLException 
	 */
	public List<CarreraDto> buscarCarrerasXEvaluador(String cedulaEvaluador) throws CarreraDtoJdbcNoEncontradoException, SQLException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		StringBuilder sbSql = new StringBuilder();
		sbSql.append(" SELECT ");
		sbSql.append(" distinct crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" AS id, crr.");
		sbSql.append(JdbcConstantes.CRR_DESCRIPCION);sbSql.append(" AS carrera, crr.");
		sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" AS facultad, crr.");
		sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" AS detalle");
		sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
		sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr,");
		sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro, ");sbSql.append(JdbcConstantes.TABLA_USUARIO);
		sbSql.append(" usr, ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl");
		sbSql.append(" WHERE usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USR_ID);
		sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" = roflcr.");
		sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.CRR_ID);
		sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append("  AND fcl.");sbSql.append(JdbcConstantes.FCL_ID);
		sbSql.append(" =crr.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" AND usr.");
		sbSql.append(JdbcConstantes.USR_IDENTIFICACION);sbSql.append(" = ? ");
		
		sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ");
		sbSql.append(RolFlujoCarreraConstantes.ROL_FLUJO_CARRERA_ESTADO_ACTIVO_VALUE);
		
		sbSql.append(" ORDER BY crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, cedulaEvaluador);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSet(rs));
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
			return retorno;
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.buscar.todos.no.result.exception")));
		}finally{
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
	}
		
	 
	
	private CarreraDto transformarResultSet(ResultSet rs) throws SQLException{
		CarreraDto retorno = new CarreraDto();
		retorno.setCrrId(rs.getInt("id"));
		retorno.setCrrDescripcion(rs.getString("carrera"));
		retorno.setCrrFacultad(rs.getInt("facultad"));
		retorno.setCrrDetalle(rs.getString("detalle"));
		return retorno;
	}
	
	
	/**PARA ASIGNACION DE MECANISMO
	 * Método que busca la lista de estudiantes validados por identificacion y convocatoria de acuerdo a su carrera
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudianteValidacionJdbcDtoException 
	 * @throws EstudianteValidacionJdbcDtoNoEncontradoException 
	 */
	
	public List<EstudianteAsignacionMecanismoDto> buscarEstudiantesXAsignarXIndetificacionXCarreraXConvocatoria(
			String identificacion, String idSecretaria, CarreraDto carreraDto,
			int convocatoria) throws EstudianteValidacionDtoException,
			EstudianteValidacionDtoNoEncontradoException {
		
		
		List<EstudianteAsignacionMecanismoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" DISTINCT ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FECHA_FIN_COHORTE);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_FECHA_INICIO_COHORTE);
			
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" , CASE WHEN trtt.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE trtt.");sbSql.append(JdbcConstantes.TRTT_ID); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" , CASE WHEN trtt.");sbSql.append(JdbcConstantes.TRTT_NUM_TRAMITE); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE trtt.");sbSql.append(JdbcConstantes.TRTT_NUM_TRAMITE); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TRTT_NUM_TRAMITE);
			sbSql.append(" , CASE WHEN trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);
			sbSql.append(" , CASE WHEN trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_OBSERVACION);
			
			
			sbSql.append(" ,prtr.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" ,prtr.");sbSql.append(JdbcConstantes.PRTR_TIPO_PROCESO);
			sbSql.append(" ,prtr.");sbSql.append(JdbcConstantes.PRTR_FECHA_EJECUCION);
			sbSql.append(" ,prtr.");sbSql.append(JdbcConstantes.PRTR_ROFLCR_ID);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ID);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ESTADO);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);			
			sbSql.append(" ,vld.vld_rsl_aprobacion_ingles ");
			
			
//			sbSql.append(" ,vld.");sbSql.append(JdbcConstantes.VLD_ID);
//			sbSql.append(" , CASE WHEN vld.");sbSql.append(JdbcConstantes.VLD_APROBACION_INGLES); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
//			sbSql.append(" ELSE vld.");sbSql.append(JdbcConstantes.VLD_APROBACION_INGLES); 
//			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.VLD_APROBACION_INGLES);
//			sbSql.append(" , CASE WHEN vld.");sbSql.append(JdbcConstantes.VLD_RSL_IDONEIDAD); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
//			sbSql.append(" ELSE vld.");sbSql.append(JdbcConstantes.VLD_RSL_IDONEIDAD); 
//			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.VLD_RSL_IDONEIDAD);
//			sbSql.append(" , CASE WHEN vld.");sbSql.append(JdbcConstantes.VLD_RSL_ACT_CONOCIMIENTO); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
//			sbSql.append(" ELSE vld.");sbSql.append(JdbcConstantes.VLD_RSL_ACT_CONOCIMIENTO); 
//			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.VLD_RSL_ACT_CONOCIMIENTO);
//			sbSql.append(" , CASE WHEN vld.");sbSql.append(JdbcConstantes.VLD_RSL_ACTUALIZ_APROB); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
//			sbSql.append(" ELSE vld.");sbSql.append(JdbcConstantes.VLD_RSL_ACTUALIZ_APROB); 
//			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.VLD_RSL_ACTUALIZ_APROB);
//			sbSql.append(" , CASE WHEN vld.");sbSql.append(JdbcConstantes.VLD_RSL_CURSO_ACT_APROB); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
//			sbSql.append(" ELSE vld.");sbSql.append(JdbcConstantes.VLD_RSL_CURSO_ACT_APROB); 
//			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.VLD_RSL_CURSO_ACT_APROB);
//			sbSql.append(" , CASE WHEN vld.");sbSql.append(JdbcConstantes.VLD_RSL_HOMOLOGACION); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
//			sbSql.append(" ELSE vld.");sbSql.append(JdbcConstantes.VLD_RSL_HOMOLOGACION); 
//			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.VLD_RSL_HOMOLOGACION);
//			sbSql.append(" , CASE WHEN vld.");sbSql.append(JdbcConstantes.VLD_RSL_HOMOLOGACION_APROB); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
//			sbSql.append(" ELSE vld.");sbSql.append(JdbcConstantes.VLD_RSL_HOMOLOGACION_APROB); 
//			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.VLD_RSL_HOMOLOGACION_APROB);
//			sbSql.append(" , CASE WHEN vld.");sbSql.append(JdbcConstantes.VLD_RSL_PRORROGA); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
//			sbSql.append(" ELSE vld.");sbSql.append(JdbcConstantes.VLD_RSL_PRORROGA); 
//			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.VLD_RSL_PRORROGA);
//			sbSql.append(" , CASE WHEN vld.");sbSql.append(JdbcConstantes.VLD_SEGUNDA_PRORROGA); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
//			sbSql.append(" ELSE vld.");sbSql.append(JdbcConstantes.VLD_SEGUNDA_PRORROGA); 
//			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.VLD_SEGUNDA_PRORROGA);
//			sbSql.append(" , CASE WHEN vld.");sbSql.append(JdbcConstantes.VLD_RSL_IDONEIDAD); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
//			sbSql.append(" ELSE vld.");sbSql.append(JdbcConstantes.VLD_RSL_IDONEIDAD); 
//			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.VLD_RSL_IDONEIDAD);
			
		sbSql.append(" FROM ");
		sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
		sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces, ");
		sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt, ");
		sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr, ");
		sbSql.append(JdbcConstantes.TABLA_VALIDACION);sbSql.append(" vld, ");
		sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
		sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv, ");
		sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ");
			sbSql.append(" WHERE ");
//			
			// Si fue seleccionada la carrera
			if(carreraDto.getCrrId()!= -99){
				sbSql.append(" crr.crr_id = ");sbSql.append(carreraDto.getCrrId());sbSql.append(" AND ");	
			}else{
				sbSql.append(" crr.crr_id IN ");
				sbSql.append("(SELECT DISTINCT crr.crr_id from carrera crr, rol_flujo_carrera roflcr, usuario_rol usro, usuario usr ");  
				sbSql.append(" WHERE usr.usr_id =  usro.usr_id  AND  usro.usro_id =  roflcr.usro_id "); 
				sbSql.append(" AND  crr.crr_id =  roflcr.crr_id AND  usr.usr_identificacion LIKE '");
				sbSql.append(idSecretaria);sbSql.append("') AND ");
			}
						
			//identificacion
			
			sbSql.append(" UPPER(prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(") like ? ");
			sbSql.append(" AND prtr.");sbSql.append(JdbcConstantes.PRTR_TIPO_PROCESO);sbSql.append(" = ");sbSql.append(ProcesoTramiteConstantes.TIPO_PROCESO_VALIDACION_TIPO_IDONEIDAD_VALUE);
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.EST_PROC_VALIDADO_VALUE);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND vld.");sbSql.append(JdbcConstantes.VLD_RSL_IDONEIDAD);sbSql.append(" =  ");sbSql.append(ValidacionConstantes.SI_ES_IDONEO_VALUE);

			sbSql.append(" AND ");
			
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			
			
			if(convocatoria != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ?");
			}else{
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" > ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			}
			
			// CONDICIONES DE VALIDACION
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = ");;sbSql.append(" prtr.");;sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND prtr.");sbSql.append(JdbcConstantes.PRTR_ID);sbSql.append(" = vld.");sbSql.append(JdbcConstantes.VLD_PRTR_ID);
			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);
			sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
//			sbSql.append(" AND prtr.");sbSql.append(JdbcConstantes.PRTR_ID);sbSql.append(" = astt.");sbSql.append(JdbcConstantes.ASTT_PRTR_ID);
//			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_MCTTCR_ID);sbSql.append(" = mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_ID);
//			sbSql.append(" AND mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_MCTT_ID);sbSql.append(" = mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
			sbSql.append(" ORDER BY  ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			
			//FIN QUERY
			con = ds.getConnection();

			int contador = 0;
			
			pstmt = con.prepareStatement(sbSql.toString());
			contador++;
			pstmt.setString(contador, "%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase())+"%"); //cargo la identificacion
			
			if(convocatoria != GeneralesConstantes.APP_ID_BASE){
				pstmt.setInt(++contador, convocatoria);
			}
			
//			System.out.println(sbSql);
//			pstmt.setString(++contador, idSecretaria);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<EstudianteAsignacionMecanismoDto>();
			String auxIdoneidad;
			int longitudMatrizIdoneidad;
			while(rs.next()){
				// Evito que se llene la lista con NO IDONEOS
				auxIdoneidad=rs.getString(JdbcConstantes.TRTT_OBSERVACION);
				String[] idoneidad =auxIdoneidad.split("-");
				longitudMatrizIdoneidad=idoneidad.length;
				if (idoneidad[longitudMatrizIdoneidad-1].equals(ValidacionConstantes.NO_IDONEO_LABEL)||idoneidad[longitudMatrizIdoneidad-1].equals(" "+ValidacionConstantes.NO_IDONEO_LABEL)){
					continue;
				}
				retorno.add(transformarResultSetADtoValidadosXAsignar(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
			} catch (SQLException ex) {
			}
			throw new EstudianteValidacionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Asignacion.postulacion.persona.no.encontrada.excepcion")));
			
		} catch (Exception e) {
			e.printStackTrace();
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
			} catch (SQLException ex) {
			}
			throw new EstudianteValidacionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Asignacion.postulacion.persona.no.encontrada.excepcion")));
			
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
		if(retorno == null || retorno.size()<=0){
			throw new EstudianteValidacionDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Asignacion.postulacion.persona.no.encontrada.excepcion")));
		}
		return retorno;
	}
	
	
	
	
	/**PARA APTITUD EXAMEN COMPLEXIVO
	 * Método que busca la lista de estudiantes evaluados por identificacion y convocatoria de acuerdo a su carrera
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudianteValidacionJdbcDtoException 
	 * @throws EstudianteValidacionJdbcDtoNoEncontradoException 
	 */
	
	public List<EstudianteAptitudJdbcDto> buscarEstudiantesAsignadoMecanismosXEstudianteXValidadorXCarreraXconvocatoria(
			String identificacion, String idSecretaria, CarreraDto carreraDto,
			int convocatoria) throws EvaluadosDtoJdbcException,
			EvaluadosDtoJdbcNoEncontradoException {
		
		List<EstudianteAptitudJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" DISTINCT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);
			sbSql.append(", ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS);
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_TIEMPO_ESTUD_REC); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_TIEMPO_ESTUD_REC); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_TIEMPO_ESTUD_REC);
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_TIPO_DURAC_REC); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_TIPO_DURAC_REC); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_TIPO_DURAC_REC);
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_TIPO_COLEGIO); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_TIPO_COLEGIO);
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_TIPO_COLEGIO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_TIPO_COLEGIO_SNIESE);			
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_TITULO_BACHILLER);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREVIOS); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREVIOS); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREVIOS);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREV_SNIESE);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_FECHA_CREACION);
			
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_FECHA_NACIMIENTO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO_SNIESE);
			sbSql.append(" , CASE WHEN trtt.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE trtt.");sbSql.append(JdbcConstantes.TRTT_ID); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" , CASE WHEN trtt.");sbSql.append(JdbcConstantes.TRTT_NUM_TRAMITE); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE trtt.");sbSql.append(JdbcConstantes.TRTT_NUM_TRAMITE); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TRTT_NUM_TRAMITE);
			sbSql.append(" , CASE WHEN trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);
			sbSql.append(" , CASE WHEN trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_OBSERVACION);
			
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_MCTT_ESTADISTICO);
			
			sbSql.append(" ,prtr.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" ,prtr.");sbSql.append(JdbcConstantes.PRTR_TIPO_PROCESO);
			sbSql.append(" ,prtr.");sbSql.append(JdbcConstantes.PRTR_FECHA_EJECUCION);
			sbSql.append(" ,prtr.");sbSql.append(JdbcConstantes.PRTR_ROFLCR_ID);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ID);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ESTADO);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
//			
//			sbSql.append(" ,astt.");sbSql.append(JdbcConstantes.ASTT_ID);	
//			sbSql.append(" ,astt.");sbSql.append(JdbcConstantes.ASTT_TEMA_TRABAJO);			
//			sbSql.append(" ,astt.");sbSql.append(JdbcConstantes.ASTT_TUTOR);
//			sbSql.append(" ,mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_ID);
//			sbSql.append(" ,mctt.");sbSql.append(JdbcConstantes.MCTT_DESCRIPCION);
			sbSql.append(" ,mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces, ");
			sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt, ");
			sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv, ");
//			sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl, ");
//			sbSql.append(JdbcConstantes.TABLA_ASIGNACION_TITULACION);sbSql.append(" astt, ");
//			sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION);sbSql.append(" mctt, ");
//			sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION_CARRERA);sbSql.append(" mcttcr ");
//			
			sbSql.append(" WHERE ");
//			
			// Si fue seleccionada la carrera
			if(carreraDto.getCrrId()!= -99){
				sbSql.append(" crr.crr_id = ");sbSql.append(carreraDto.getCrrId());sbSql.append(" AND ");	
			}else{
				sbSql.append(" crr.crr_id IN ");
				sbSql.append("(SELECT DISTINCT crr.crr_id from carrera crr, rol_flujo_carrera roflcr, usuario_rol usro, usuario usr ");  
				sbSql.append(" WHERE usr.usr_id =  usro.usr_id  AND  usro.usro_id =  roflcr.usro_id "); 
				sbSql.append(" AND  crr.crr_id =  roflcr.crr_id AND  usr.usr_identificacion LIKE '");
				sbSql.append(idSecretaria);sbSql.append("') AND ");
			}
						
			//identificacion
			
			sbSql.append(" UPPER(prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(") like ? ");
			sbSql.append(" AND prtr.");sbSql.append(JdbcConstantes.PRTR_TIPO_PROCESO);sbSql.append(" = ");sbSql.append(ProcesoTramiteConstantes.TIPO_PROCESO_ASIGNADO_MECANISMO_VALUE);
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.EST_PROC_ASIGNADO_MECANISMO_TITULACION_VALUE);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			
			
			if(convocatoria != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ?");
			}else{
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" > ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			}
			sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
			
			// CONDICIONES DE VALIDACION
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = ");;sbSql.append(" prtr.");;sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);
			
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" IN ( ");
			sbSql.append(" SELECT DISTINCT crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" from ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr, ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);
			sbSql.append(" usro, ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");sbSql.append(" WHERE ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" = ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");
						sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_IDENTIFICACION);
			sbSql.append(" LIKE ?) "); 
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			
//			
//			sbSql.append(" AND astt.");sbSql.append(JdbcConstantes.ASTT_PRTR_ID);sbSql.append(" = prtr.");sbSql.append(JdbcConstantes.PRTR_ID);
//			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_MCTTCR_ID);sbSql.append(" = mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_ID);
//			sbSql.append(" AND mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_MCTT_ID);sbSql.append(" = mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
//			sbSql.append(" AND mctt.");sbSql.append(JdbcConstantes.MCTT_ID);sbSql.append(" = ");sbSql.append(MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_VALUE);
			
			sbSql.append(" ORDER BY  ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			
			//FIN QUERY
			con = ds.getConnection();

			int contador = 0;
			
			
			pstmt = con.prepareStatement(sbSql.toString());
			contador++;
			pstmt.setString(contador, "%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase())+"%"); //cargo la identificacion
			
			if(convocatoria != GeneralesConstantes.APP_ID_BASE){
				pstmt.setInt(++contador, convocatoria);
			}
			
			pstmt.setString(++contador, idSecretaria);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<EstudianteAptitudJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoAsignadosMecanismos(rs));
			}
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new EvaluadosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReAsignacion.postulacion.persona.no.encontrada.excepcion")));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new EvaluadosDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReAsignacion.postulacion.persona.no.encontrada.excepcion")));
		} finally {
			try {
				if(rs != null){
					rs.close();
				}
				if(pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		if(retorno == null || retorno.size()<=0){
			throw new EvaluadosDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReAsignacion.postulacion.persona.no.encontrada.excepcion")));
		}
		return retorno;
		
		
	}
	
	
	/**PARA REASIGNACION
	 * Método que busca la lista de estudiantes evaluados por identificacion y convocatoria de acuerdo a su carrera
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudianteValidacionJdbcDtoException 
	 * @throws EstudianteValidacionJdbcDtoNoEncontradoException 
	 */
	public List<EstudianteAsignacionMecanismoDto> buscarEstudiantesEvaluadosXIndetificacionXCarreraXConvocatoria(
			String identificacion, String idSecretaria, CarreraDto carreraDto,
			int convocatoria) throws EstudianteValidacionDtoException,
			EstudianteValidacionDtoNoEncontradoException {
		
		List<EstudianteAsignacionMecanismoDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" DISTINCT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);
			sbSql.append(", ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS);
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_TIEMPO_ESTUD_REC); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_TIEMPO_ESTUD_REC); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_TIEMPO_ESTUD_REC);
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_TIPO_DURAC_REC); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_TIPO_DURAC_REC); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_TIPO_DURAC_REC);
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_TIPO_COLEGIO); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_TIPO_COLEGIO);
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_TIPO_COLEGIO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_TIPO_COLEGIO_SNIESE);			
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_TITULO_BACHILLER);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREVIOS); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREVIOS); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREVIOS);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREV_SNIESE);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_FECHA_CREACION);
			
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_FECHA_NACIMIENTO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO_SNIESE);
			sbSql.append(" , CASE WHEN trtt.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE trtt.");sbSql.append(JdbcConstantes.TRTT_ID); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" , CASE WHEN trtt.");sbSql.append(JdbcConstantes.TRTT_NUM_TRAMITE); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE trtt.");sbSql.append(JdbcConstantes.TRTT_NUM_TRAMITE); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TRTT_NUM_TRAMITE);
			sbSql.append(" , CASE WHEN trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);
			sbSql.append(" , CASE WHEN trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_OBSERVACION);
			
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_MCTT_ESTADISTICO);
			
			sbSql.append(" ,prtr.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" ,prtr.");sbSql.append(JdbcConstantes.PRTR_TIPO_PROCESO);
			sbSql.append(" ,prtr.");sbSql.append(JdbcConstantes.PRTR_FECHA_EJECUCION);
			sbSql.append(" ,prtr.");sbSql.append(JdbcConstantes.PRTR_ROFLCR_ID);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ID);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ESTADO);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
						
			
//			sbSql.append(" ,astt.");sbSql.append(JdbcConstantes.ASTT_ID);
//			sbSql.append(" , astt.");sbSql.append(JdbcConstantes.ASTT_TEMA_TRABAJO); 
//			sbSql.append(" ,  astt.");sbSql.append(JdbcConstantes.ASTT_TUTOR); 
//			
//			
//			
//			sbSql.append(" ,mctt.");sbSql.append(JdbcConstantes.MCTT_DESCRIPCION);
//			sbSql.append(" ,mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_ID);
//			
//			sbSql.append(" FROM ");
//			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
//			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces, ");
//			sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt, ");
//			sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr, ");
//			sbSql.append(JdbcConstantes.TABLA_ASIGNACION_TITULACION);sbSql.append(" astt, ");
//			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
//			sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv, ");
//			sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl, ");
//			sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION);sbSql.append(" mctt, ");
//			sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION_CARRERA);sbSql.append(" mcttcr ");
			sbSql.append(" WHERE ");
//			
			// Si fue seleccionada la carrera
			if(carreraDto.getCrrId()!= -99){
				sbSql.append(" crr.crr_id = ");sbSql.append(carreraDto.getCrrId());sbSql.append(" AND ");	
			}else{
				sbSql.append(" crr.crr_id IN ");
				sbSql.append("(SELECT DISTINCT crr.crr_id from carrera crr, rol_flujo_carrera roflcr, usuario_rol usro, usuario usr ");  
				sbSql.append(" WHERE usr.usr_id =  usro.usr_id  AND  usro.usro_id =  roflcr.usro_id "); 
				sbSql.append(" AND  crr.crr_id =  roflcr.crr_id AND  usr.usr_identificacion LIKE '");
				sbSql.append(idSecretaria);sbSql.append("') AND ");
			}
						
			//identificacion
			
			sbSql.append(" UPPER(prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(") like ? ");
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" >=  ");sbSql.append(TramiteTituloConstantes.EST_PROC_ASIGNADO_MECANISMO_TITULACION_VALUE);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" <  ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_VALUE);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" <>  ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_ACTA_GENERADA_MANUALMENTE_VALUE);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" <>  ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_FINAL_CARGADO_VALUE);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" <>  ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_FINAL_GRACIA_CARGADO_VALUE);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" <>  ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_NO_APTO_EVALUADO_VALUE);
			
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			
			if(convocatoria != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ?");
			}else{
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" > ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			}
			sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
			// CONDICIONES DE VALIDACION
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = ");;sbSql.append(" prtr.");;sbSql.append(JdbcConstantes.TRTT_ID);
//			sbSql.append(" AND prtr.");sbSql.append(JdbcConstantes.PRTR_ID);sbSql.append(" = astt.");sbSql.append(JdbcConstantes.ASTT_PRTR_ID);
//			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
//			sbSql.append(" AND fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);
//			
//			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_MCTTCR_ID);sbSql.append(" = ");
//			sbSql.append(" mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_ID);
//			sbSql.append(" AND mctt.");sbSql.append(JdbcConstantes.MCTT_ID);sbSql.append(" = ");
//			sbSql.append(" mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_MCTT_ID);
//			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" ORDER BY  ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			
			//FIN QUERY
			con = ds.getConnection();

			int contador = 0;
			
			
			pstmt = con.prepareStatement(sbSql.toString());
			contador++;
			pstmt.setString(contador, "%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase())+"%"); //cargo la identificacion
			
			if(convocatoria != GeneralesConstantes.APP_ID_BASE){
				pstmt.setInt(++contador, convocatoria);
			}
			
			
//			pstmt.setString(++contador, idSecretaria);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<EstudianteAsignacionMecanismoDto>();
			String auxIdoneidad;
			int longitudMatrizIdoneidad;
			while(rs.next()){
				// Evito que se llene la lista con NO IDONEOS
				auxIdoneidad=rs.getString(JdbcConstantes.TRTT_OBSERVACION);
				String[] idoneidad =auxIdoneidad.split("-");
				longitudMatrizIdoneidad=idoneidad.length;
				if (idoneidad[longitudMatrizIdoneidad-1].equals(ValidacionConstantes.NO_IDONEO_LABEL)||idoneidad[longitudMatrizIdoneidad-1].equals(" "+ValidacionConstantes.NO_IDONEO_LABEL)){
					continue;
				}
				retorno.add(transformarResultSetADtoEvaluadosXAsignar(rs));
			}
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new EstudianteValidacionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Asignacion.postulacion.persona.no.encontrada.excepcion")));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new EstudianteValidacionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Asignacion.postulacion.persona.no.encontrada.excepcion")));
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
		if(retorno == null || retorno.size()<=0){
			throw new EstudianteValidacionDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Asignacion.postulacion.persona.no.encontrada.excepcion")));
		}
		return retorno;
	}
	
	
	/**PARA APTITUD OTROS MECANISMOS
	 * Método que busca la lista de estudiantes evaluados por identificacion y convocatoria de acuerdo a su carrera
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudianteValidacionJdbcDtoException 
	 * @throws EstudianteValidacionJdbcDtoNoEncontradoException 
	 */
	
	public List<EstudianteAptitudOtrosMecanismosJdbcDto> buscarEstudiantesAsignadoMecanismosOtrosMecanismos(
			String identificacion, String idSecretaria, CarreraDto carreraDto,
			int convocatoria) throws EvaluadosDtoJdbcException,
			EvaluadosDtoJdbcNoEncontradoException {
		
		
		List<EstudianteAptitudOtrosMecanismosJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" DISTINCT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);
			sbSql.append(", ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS);
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_TIEMPO_ESTUD_REC); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_TIEMPO_ESTUD_REC); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_TIEMPO_ESTUD_REC);
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_TIPO_DURAC_REC); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_TIPO_DURAC_REC); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_TIPO_DURAC_REC);
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_TIPO_COLEGIO); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_TIPO_COLEGIO);
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_TIPO_COLEGIO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_TIPO_COLEGIO_SNIESE);			
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_TITULO_BACHILLER);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREVIOS); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREVIOS); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREVIOS);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREV_SNIESE);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_FECHA_CREACION);
			
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_FECHA_NACIMIENTO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO_SNIESE);
			sbSql.append(" , CASE WHEN trtt.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE trtt.");sbSql.append(JdbcConstantes.TRTT_ID); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" , CASE WHEN trtt.");sbSql.append(JdbcConstantes.TRTT_NUM_TRAMITE); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE trtt.");sbSql.append(JdbcConstantes.TRTT_NUM_TRAMITE); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TRTT_NUM_TRAMITE);
			sbSql.append(" , CASE WHEN trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);
			sbSql.append(" , CASE WHEN trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_OBSERVACION);
			
			
			sbSql.append(" ,prtr.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" ,prtr.");sbSql.append(JdbcConstantes.PRTR_TIPO_PROCESO);
			sbSql.append(" ,prtr.");sbSql.append(JdbcConstantes.PRTR_FECHA_EJECUCION);
			sbSql.append(" ,prtr.");sbSql.append(JdbcConstantes.PRTR_ROFLCR_ID);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ID);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ESTADO);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			
			sbSql.append(" ,astt.");sbSql.append(JdbcConstantes.ASTT_ID);	
			sbSql.append(" ,astt.");sbSql.append(JdbcConstantes.ASTT_TEMA_TRABAJO);			
			sbSql.append(" ,astt.");sbSql.append(JdbcConstantes.ASTT_DIRECTOR_CIENTIFICO);
			sbSql.append(" , CASE WHEN astt.");sbSql.append(JdbcConstantes.ASTT_APROBACION_TUTOR); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
			sbSql.append(" ELSE astt.");sbSql.append(JdbcConstantes.ASTT_APROBACION_TUTOR); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.ASTT_APROBACION_TUTOR);
			
			sbSql.append(" ,mcttcr.");sbSql.append(JdbcConstantes.MCCR_ID);
			sbSql.append(" ,mctt.");sbSql.append(JdbcConstantes.MCTT_DESCRIPCION);
			sbSql.append(" ,mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces, ");
			sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt, ");
			sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv, ");
			sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl, ");
			sbSql.append(JdbcConstantes.TABLA_ASIGNACION_TITULACION);sbSql.append(" astt, ");
			sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION);sbSql.append(" mctt, ");
			sbSql.append(JdbcConstantes.TABLA_MECANISMO_CARRERA);sbSql.append(" mcttcr ");
			
			sbSql.append(" WHERE ");
//			
			// Si fue seleccionada la carrera
			if(carreraDto.getCrrId()!= -99){
				sbSql.append(" crr.crr_id = ");sbSql.append(carreraDto.getCrrId());sbSql.append(" AND ");	
			}else{
				sbSql.append(" crr.crr_id IN ");
				sbSql.append("(SELECT DISTINCT crr.crr_id from carrera crr, rol_flujo_carrera roflcr, usuario_rol usro, usuario usr ");  
				sbSql.append(" WHERE usr.usr_id =  usro.usr_id  AND  usro.usro_id =  roflcr.usro_id "); 
				sbSql.append(" AND  crr.crr_id =  roflcr.crr_id AND  usr.usr_identificacion LIKE '");
				sbSql.append(idSecretaria);sbSql.append("') AND ");
			}
						
			//identificacion
			
			sbSql.append(" UPPER(prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(") like ? ");
			sbSql.append(" AND prtr.");sbSql.append(JdbcConstantes.PRTR_TIPO_PROCESO);sbSql.append(" = ");sbSql.append(ProcesoTramiteConstantes.TIPO_PROCESO_ASIGNADO_MECANISMO_VALUE);
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" in (");
			sbSql.append(TramiteTituloConstantes.EST_PROC_ASIGNADO_MECANISMO_TITULACION_VALUE);sbSql.append(" , ");
			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_APROBACION_TUTOR_TRIBUNAL_LECTOR_VALUE);sbSql.append(" , ");
			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_NO_APROBACION_TUTOR_TRIBUNAL_LECTOR_VALUE);sbSql.append(" ) ");
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			
			
			if(convocatoria != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ?");
			}else{
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" > ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			}
			sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
			// CONDICIONES DE VALIDACION
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = ");;sbSql.append(" prtr.");;sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);
			
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" IN ( ");
			sbSql.append(" SELECT DISTINCT crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" from ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr, ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);
			sbSql.append(" usro, ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");sbSql.append(" WHERE ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" = ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");
						sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_IDENTIFICACION);
			sbSql.append(" LIKE ?) "); 
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			
			
			sbSql.append(" AND astt.");sbSql.append(JdbcConstantes.ASTT_PRTR_ID);sbSql.append(" = prtr.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_MCCR_ID);sbSql.append(" = mcttcr.");sbSql.append(JdbcConstantes.MCCR_ID);
			sbSql.append(" AND mcttcr.");sbSql.append(JdbcConstantes.MCCR_MCTT_ID);sbSql.append(" = mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
			sbSql.append(" AND mctt.");sbSql.append(JdbcConstantes.MCTT_ID);sbSql.append(" <> ");sbSql.append(MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_VALUE);
			
			sbSql.append(" ORDER BY  ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			//FIN QUERY
			con = ds.getConnection();
			
			int contador = 0;
			
			pstmt = con.prepareStatement(sbSql.toString());
			contador++;
			pstmt.setString(contador, "%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase())+"%"); //cargo la identificacion
			
			if(convocatoria != GeneralesConstantes.APP_ID_BASE){
				pstmt.setInt(++contador, convocatoria);
			}
			
			pstmt.setString(++contador, idSecretaria);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<EstudianteAptitudOtrosMecanismosJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoAsignadosMecanismosParaAptitud(rs));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new EvaluadosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReAsignacion.postulacion.persona.no.encontrada.excepcion")));
		} catch (Exception e) {
			e.printStackTrace();
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new EvaluadosDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReAsignacion.postulacion.persona.no.encontrada.excepcion")));
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
		if(retorno == null || retorno.size()<=0){
			throw new EvaluadosDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReAsignacion.postulacion.persona.no.encontrada.excepcion")));
		}
		return retorno;
		
		
	}
	
	///////////////////////////////////
	
	/**PARA APTITUD EDICION DGA
	 * Método que busca la lista de estudiantes evaluados por identificacion y convocatoria de acuerdo a su carrera
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudianteValidacionJdbcDtoException 
	 * @throws EstudianteValidacionJdbcDtoNoEncontradoException 
	 */
	
	public List<EstudianteAptitudJdbcDto> buscarEstudiantesAsignadoMecanismosDGA(
			String identificacion, String idSecretaria, FacultadDto facultadDto,
			int convocatoria) throws EvaluadosDtoJdbcException,
			EvaluadosDtoJdbcNoEncontradoException {
		
		
		List<EstudianteAptitudJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" DISTINCT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);
			sbSql.append(", ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS);
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_TIEMPO_ESTUD_REC); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_TIEMPO_ESTUD_REC); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_TIEMPO_ESTUD_REC);
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_TIPO_DURAC_REC); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_TIPO_DURAC_REC); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_TIPO_DURAC_REC);
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_TIPO_COLEGIO); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_TIPO_COLEGIO);
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_TIPO_COLEGIO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_TIPO_COLEGIO_SNIESE);			
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_TITULO_BACHILLER);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREVIOS); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREVIOS); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREVIOS);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREV_SNIESE);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_FECHA_CREACION);
			
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ID);
			
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_FECHA_NACIMIENTO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO_SNIESE);
			sbSql.append(" , CASE WHEN trtt.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE trtt.");sbSql.append(JdbcConstantes.TRTT_ID); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" , CASE WHEN trtt.");sbSql.append(JdbcConstantes.TRTT_NUM_TRAMITE); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE trtt.");sbSql.append(JdbcConstantes.TRTT_NUM_TRAMITE); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TRTT_NUM_TRAMITE);
			sbSql.append(" , CASE WHEN trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);
			sbSql.append(" , CASE WHEN trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_OBSERVACION);
			
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_MCTT_ESTADISTICO);
			
			sbSql.append(" ,prtr.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" ,prtr.");sbSql.append(JdbcConstantes.PRTR_TIPO_PROCESO);
			sbSql.append(" ,prtr.");sbSql.append(JdbcConstantes.PRTR_FECHA_EJECUCION);
			sbSql.append(" ,prtr.");sbSql.append(JdbcConstantes.PRTR_ROFLCR_ID);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ID);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ESTADO);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			
//			sbSql.append(" ,apt.");sbSql.append(JdbcConstantes.APT_ID);
//			sbSql.append(" ,apt.");sbSql.append(JdbcConstantes.APT_APROBO_ACTUALIZAR);	
//			sbSql.append(" ,apt.");sbSql.append(JdbcConstantes.APT_REPROBO_CREDITOS);			
//			sbSql.append(" ,apt.");sbSql.append(JdbcConstantes.APT_REQUISITOS);
//			sbSql.append(" ,apt.");sbSql.append(JdbcConstantes.APT_SEGUNDA_CARRERA);
//			sbSql.append(" ,mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_ID);
			sbSql.append(" ,mctt.");sbSql.append(JdbcConstantes.MCTT_DESCRIPCION);
			sbSql.append(" ,mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces, ");
			sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt, ");
			sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv, ");
			sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl, ");
//			sbSql.append(JdbcConstantes.TABLA_APTITUD);sbSql.append(" apt, ");
//			sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION);sbSql.append(" mctt, ");
//			sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION_CARRERA);sbSql.append(" mcttcr ");
//			
//			sbSql.append(" WHERE ");
//			
			// Si fue seleccionada la carrera
			if(facultadDto.getFclId()!= -99){
				sbSql.append(" fcl.fcl_id = ");sbSql.append(facultadDto.getFclId());sbSql.append(" AND ");	
			}else{
				sbSql.append(" fcl.fcl_id > ");sbSql.append(facultadDto.getFclId());sbSql.append(" AND ");	
			}
						
			//identificacion
			sbSql.append(" UPPER(prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(") like ? ");
			sbSql.append(" AND (prtr.");sbSql.append(JdbcConstantes.PRTR_TIPO_PROCESO);sbSql.append(" = ");sbSql.append(ProcesoTramiteConstantes.TIPO_PROCESO_APTO_EVALUADO_VALUE);
			sbSql.append(" OR prtr.");sbSql.append(JdbcConstantes.PRTR_TIPO_PROCESO);sbSql.append(" = ");sbSql.append(ProcesoTramiteConstantes.TIPO_PROCESO_NO_APTO_EVALUADO_VALUE);sbSql.append(")");
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND (trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_APTO_EVALUADO_VALUE);
			sbSql.append(" OR trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" =  ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_NO_APTO_EVALUADO_VALUE);sbSql.append(")");
			sbSql.append(" AND ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			
			sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
			if(convocatoria != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ?");
			}else{
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" > ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			}
			sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
			// CONDICIONES DE VALIDACION
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = ");;sbSql.append(" prtr.");;sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);
			
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" IN ( ");
			sbSql.append(" SELECT DISTINCT crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" from ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr, ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);
			sbSql.append(" usro, ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");sbSql.append(" WHERE ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" = ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");
						sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_IDENTIFICACION);
			sbSql.append(" LIKE ?) "); 
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			
//			
//			sbSql.append(" AND apt.");sbSql.append(JdbcConstantes.APT_PRTR_ID);sbSql.append(" = prtr.");sbSql.append(JdbcConstantes.PRTR_ID);
//			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_MCTTCR_ID);sbSql.append(" = mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_ID);
//			sbSql.append(" AND mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_MCTT_ID);sbSql.append(" = mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
//			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_OBS_DESACTIVAR_DGA);sbSql.append(" IS NULL");
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" ORDER BY  ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			
			
//			System.out.println(sbSql);
			//FIN QUERY
			con = ds.getConnection();

			int contador = 0;
			
			pstmt = con.prepareStatement(sbSql.toString());
			contador++;
			pstmt.setString(contador, "%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase())+"%"); //cargo la identificacion
			
			if(convocatoria != GeneralesConstantes.APP_ID_BASE){
				pstmt.setInt(++contador, convocatoria);
			}
			
			pstmt.setString(++contador, idSecretaria);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<EstudianteAptitudJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoAsignadosMecanismosDGA(rs));
			}
		} catch (SQLException e) {
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
			} catch (Exception e2) {
			}
			throw new EvaluadosDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.aprobar.evaluador.no.resultados")));
		} catch (Exception e) {
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
			} catch (Exception e2) {
			}
			throw new EvaluadosDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.aprobar.evaluador.no.resultados")));
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
		if(retorno == null || retorno.size()<=0){
			throw new EvaluadosDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.aprobar.evaluador.no.resultados")));
		}
		return retorno;
		
		
	}
	
	
	private EstudianteAsignacionMecanismoDto transformarResultSetADtoValidadosXAsignar(ResultSet rs) throws SQLException{
		EstudianteAsignacionMecanismoDto retorno = new EstudianteAsignacionMecanismoDto();
//		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_TIPO_IDENTIFICACION));
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
//		retorno.setFcesCrrEstudPrevios(rs.getString(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS));
//		retorno.setFcesTiempoEstudRec(rs.getInt(JdbcConstantes.FCES_TIEMPO_ESTUD_REC));
//		retorno.setFcesTipoDuracionRec(rs.getInt(JdbcConstantes.FCES_TIPO_DURAC_REC));
		
//		retorno.setFcesTipoColegio(rs.getInt(JdbcConstantes.FCES_TIPO_COLEGIO));
//		retorno.setFcesTipoColegioSniese(rs.getString(JdbcConstantes.FCES_TIPO_COLEGIO_SNIESE));
//		retorno.setFcesTituloBachillerId(rs.getInt(JdbcConstantes.FCES_TITULO_BACHILLER));
		
//		retorno.setFcesFechaEgresamiento(rs.getDate(JdbcConstantes.FCES_FECHA_EGRESAMIENTO));
		retorno.setFcesFechaFinCohorte(rs.getDate(JdbcConstantes.FCES_FECHA_FIN_COHORTE));
		retorno.setFcesFechaInicioCohorte(rs.getDate(JdbcConstantes.FCES_FECHA_INICIO_COHORTE));
//		retorno.setFcesRecEstuPrevios(rs.getInt(JdbcConstantes.FCES_REC_ESTUD_PREVIOS));
//		retorno.setFcesRecEstuPreviosSniese(rs.getString(JdbcConstantes.FCES_REC_ESTUD_PREV_SNIESE));
		
//		retorno.setFcesFechaCreacion(rs.getTimestamp(JdbcConstantes.FCES_FECHA_CREACION));
		
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
//		retorno.setPrsTipoIdentificacion(rs.getInt(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsMailPersonal(rs.getString(JdbcConstantes.PRS_MAIL_PERSONAL));
		retorno.setTrttId(rs.getInt(JdbcConstantes.TRTT_ID));
		retorno.setTrttEstadoTramite(rs.getInt(JdbcConstantes.TRTT_ESTADO_TRAMITE));
//		retorno.setTrttCarrera(rs.getInt(JdbcConstantes.TRTT_CARRERA));
		retorno.setTrttCarreraId(rs.getInt(JdbcConstantes.TRTT_CARRERA_ID));
		
		String auxIdoneidad;
		auxIdoneidad=rs.getString(JdbcConstantes.TRTT_OBSERVACION);
		String[] idoneidad =auxIdoneidad.split("-");
		retorno.setTrttObsValSec(idoneidad[0]);
		
		// Solo para validar los combos
		retorno.setTrttObsValSecSplit(rs.getString(JdbcConstantes.TRTT_OBSERVACION));
		
//		retorno.setTrttMcttEstadistico(rs.getInt(JdbcConstantes.TRTT_MCTT_ESTADISTICO));
		
//		if (rs.getInt(JdbcConstantes.TRTT_MCTT_ESTADISTICO)==TramiteTituloConstantes.TIPO_MODALIDAD_ESTA_COMPLEXIVO_VALUE){
//			retorno.setTrttMcttEstadisticoSt(TramiteTituloConstantes.TIPO_MODALIDAD_ESTA_COMPLEXIVO_LABEL);
//		}else {
//			retorno.setTrttMcttEstadisticoSt(TramiteTituloConstantes.TIPO_MODALIDAD_ESTA_OTRAS_MODALIDADES_LABEL);
//			
//		}
		
		
		retorno.setPrtrId(rs.getInt(JdbcConstantes.PRTR_ID));
		retorno.setPrtrTipoProceso(rs.getInt(JdbcConstantes.PRTR_TIPO_PROCESO));
		retorno.setPrtrFechaEjecucion(rs.getTimestamp(JdbcConstantes.PRTR_FECHA_EJECUCION));
		retorno.setPrtrRoflcrId(rs.getInt(JdbcConstantes.PRTR_ROFLCR_ID));
		retorno.setCnvId(rs.getInt(JdbcConstantes.CNV_ID));
		retorno.setCnvDescripcion(rs.getString(JdbcConstantes.CNV_DESCRIPCION));
		
		retorno.setCnvEstado(rs.getInt(JdbcConstantes.CNV_ESTADO));
		retorno.setFclId(rs.getInt(JdbcConstantes.FCL_ID));
		
		retorno.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
		
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		
		retorno.setVldAproboIngles(rs.getInt("vld_rsl_aprobacion_ingles"));
//		retorno.setVldAproboIngles(rs.getInt(JdbcConstantes.VLD_APROBO_INGLES));
//		retorno.setVldCulminoMalla(rs.getInt(JdbcConstantes.VLD_CULMINO_MALLA));
//		retorno.setVldReproboComplexivo(rs.getInt(JdbcConstantes.VLD_REPROBO_COMPLEXIVO));
//		retorno.setVldAsignadoTutor(rs.getInt(JdbcConstantes.VLD_ASIGNADO_TUTOR));
//		retorno.setVldUltimoSemestre(rs.getInt(JdbcConstantes.VLD_ULTIMO_SEMESTRE));
//		retorno.setVldRslHomologacion(rs.getInt(JdbcConstantes.VLD_RSL_HOMOLOGACION));
//		
//		retorno.setVldRslActConocimiento(rs.getInt(JdbcConstantes.VLD_RSL_ACT_CONOCIMIENTO));
//		retorno.setVldRslGratuidad(rs.getInt(JdbcConstantes.VLD_RSL_GRATUIDAD));
//		
//		
//		retorno.setVldRslIdoneidad(rs.getInt(JdbcConstantes.VLD_RSL_IDONEIDAD));
//		
//		retorno.setVldRslExamenComplexivo(rs.getInt(JdbcConstantes.VLD_RSL_EXAMEN_COMPLEXIVO));
//		
//		retorno.setVldRslOtrosMecanismos(rs.getInt(JdbcConstantes.VLD_RSL_OTROS_MECANISMOS));
//		retorno.setVldRslSlcMecanismo(rs.getInt(JdbcConstantes.VLD_RSL_SLC_MECANISMO));
//		retorno.setVldProcesoTramite(rs.getInt(JdbcConstantes.VLD_PRTR_ID));
		
		
		return retorno;
	}
	
	
	
	private EstudianteAsignacionMecanismoDto transformarResultSetADtoEvaluadosXAsignar(ResultSet rs) throws SQLException{
		EstudianteAsignacionMecanismoDto retorno = new EstudianteAsignacionMecanismoDto();
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_TIPO_IDENTIFICACION));
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setFcesCrrEstudPrevios(rs.getString(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS));
		retorno.setFcesTiempoEstudRec(rs.getInt(JdbcConstantes.FCES_TIEMPO_ESTUD_REC));
		retorno.setFcesTipoDuracionRec(rs.getInt(JdbcConstantes.FCES_TIPO_DURAC_REC));
		
		retorno.setFcesTipoColegio(rs.getInt(JdbcConstantes.FCES_TIPO_COLEGIO));
		retorno.setFcesTipoColegioSniese(rs.getString(JdbcConstantes.FCES_TIPO_COLEGIO_SNIESE));
		retorno.setFcesTituloBachillerId(rs.getInt(JdbcConstantes.FCES_TITULO_BACHILLER));
		
		retorno.setFcesFechaEgresamiento(rs.getDate(JdbcConstantes.FCES_FECHA_EGRESAMIENTO));
		
		retorno.setFcesRecEstuPrevios(rs.getInt(JdbcConstantes.FCES_REC_ESTUD_PREVIOS));
		retorno.setFcesRecEstuPreviosSniese(rs.getString(JdbcConstantes.FCES_REC_ESTUD_PREV_SNIESE));
		
		retorno.setFcesFechaCreacion(rs.getTimestamp(JdbcConstantes.FCES_FECHA_CREACION));
		
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsTipoIdentificacion(rs.getInt(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsMailPersonal(rs.getString(JdbcConstantes.PRS_MAIL_PERSONAL));
		retorno.setTrttId(rs.getInt(JdbcConstantes.TRTT_ID));
		retorno.setTrttEstadoTramite(rs.getInt(JdbcConstantes.TRTT_ESTADO_TRAMITE));
		retorno.setTrttEstadoProceso(rs.getInt(JdbcConstantes.TRTT_ESTADO_PROCESO));
		retorno.setTrttCarrera(rs.getInt(JdbcConstantes.TRTT_CARRERA));
		retorno.setTrttCarreraId(rs.getInt(JdbcConstantes.TRTT_CARRERA_ID));
		
		String auxIdoneidad;
		auxIdoneidad=rs.getString(JdbcConstantes.TRTT_OBSERVACION);
		String[] idoneidad =auxIdoneidad.split("-");
		retorno.setTrttObsValSec(idoneidad[0]);
		
		
//		retorno.setAssttId(rs.getInt(JdbcConstantes.ASTT_ID));
//		retorno.setAssttTemaTrabajo(rs.getString(JdbcConstantes.ASTT_TEMA_TRABAJO));
//		retorno.setAssttTutor(rs.getString(JdbcConstantes.ASTT_TUTOR));
//		retorno.setMcttcrId(rs.getInt(JdbcConstantes.MCTTCR_ID));
//		retorno.setMcttDescripcion(rs.getString(JdbcConstantes.MCTT_DESCRIPCION));
		
		// Solo para validar los combos
		retorno.setTrttObsValSecSplit(rs.getString(JdbcConstantes.TRTT_OBSERVACION));
		
		retorno.setTrttMcttEstadistico(rs.getInt(JdbcConstantes.TRTT_MCTT_ESTADISTICO));
		
//		if (rs.getInt(JdbcConstantes.TRTT_MCTT_ESTADISTICO)==TramiteTituloConstantes.TIPO_MODALIDAD_ESTA_COMPLEXIVO_VALUE){
//			retorno.setTrttMcttEstadisticoSt(TramiteTituloConstantes.TIPO_MODALIDAD_ESTA_COMPLEXIVO_LABEL);
//		}else {
//			retorno.setTrttMcttEstadisticoSt(TramiteTituloConstantes.TIPO_MODALIDAD_ESTA_OTRAS_MODALIDADES_LABEL);
//			
//		}
		
		
		retorno.setPrtrId(rs.getInt(JdbcConstantes.PRTR_ID));
		retorno.setPrtrTipoProceso(rs.getInt(JdbcConstantes.PRTR_TIPO_PROCESO));
		retorno.setPrtrFechaEjecucion(rs.getTimestamp(JdbcConstantes.PRTR_FECHA_EJECUCION));
		retorno.setPrtrRoflcrId(rs.getInt(JdbcConstantes.PRTR_ROFLCR_ID));
		retorno.setCnvId(rs.getInt(JdbcConstantes.CNV_ID));
		retorno.setCnvDescripcion(rs.getString(JdbcConstantes.CNV_DESCRIPCION));
		
		retorno.setCnvEstado(rs.getInt(JdbcConstantes.CNV_ESTADO));
		retorno.setFclId(rs.getInt(JdbcConstantes.FCL_ID));
		
		retorno.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
		
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		

		return retorno;
	}
	
	private EstudianteAptitudJdbcDto transformarResultSetADtoAsignadosMecanismos(ResultSet rs) throws SQLException{
		EstudianteAptitudJdbcDto retorno = new EstudianteAptitudJdbcDto();
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_TIPO_IDENTIFICACION));
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setFcesCrrEstudPrevios(rs.getString(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS));
		retorno.setFcesTiempoEstudRec(rs.getInt(JdbcConstantes.FCES_TIEMPO_ESTUD_REC));
		retorno.setFcesTipoDuracionRec(rs.getInt(JdbcConstantes.FCES_TIPO_DURAC_REC));
		
		retorno.setFcesTipoColegio(rs.getInt(JdbcConstantes.FCES_TIPO_COLEGIO));
		retorno.setFcesTipoColegioSniese(rs.getString(JdbcConstantes.FCES_TIPO_COLEGIO_SNIESE));
		retorno.setFcesTituloBachillerId(rs.getInt(JdbcConstantes.FCES_TITULO_BACHILLER));
		
		retorno.setFcesFechaEgresamiento(rs.getDate(JdbcConstantes.FCES_FECHA_EGRESAMIENTO));
		
		retorno.setFcesRecEstuPrevios(rs.getInt(JdbcConstantes.FCES_REC_ESTUD_PREVIOS));
		retorno.setFcesRecEstuPreviosSniese(rs.getString(JdbcConstantes.FCES_REC_ESTUD_PREV_SNIESE));
		
		retorno.setFcesFechaCreacion(rs.getTimestamp(JdbcConstantes.FCES_FECHA_CREACION));
		
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsTipoIdentificacion(rs.getInt(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsMailPersonal(rs.getString(JdbcConstantes.PRS_MAIL_PERSONAL));
		retorno.setTrttId(rs.getInt(JdbcConstantes.TRTT_ID));
		retorno.setTrttEstadoTramite(rs.getInt(JdbcConstantes.TRTT_ESTADO_TRAMITE));
		retorno.setTrttCarrera(rs.getInt(JdbcConstantes.TRTT_CARRERA));
		retorno.setTrttCarreraId(rs.getInt(JdbcConstantes.TRTT_CARRERA_ID));
		
		
		// Solo para validar los combos
		retorno.setTrttObsValSec(rs.getString(JdbcConstantes.TRTT_OBSERVACION));
		
		retorno.setTrttMcttEstadistico(rs.getInt(JdbcConstantes.TRTT_MCTT_ESTADISTICO));
		
//		if (rs.getInt(JdbcConstantes.TRTT_MCTT_ESTADISTICO)==TramiteTituloConstantes.TIPO_MODALIDAD_ESTA_COMPLEXIVO_VALUE){
//			retorno.setTrttMcttEstadisticoSt(TramiteTituloConstantes.TIPO_MODALIDAD_ESTA_COMPLEXIVO_LABEL);
//		}else {
//			retorno.setTrttMcttEstadisticoSt(TramiteTituloConstantes.TIPO_MODALIDAD_ESTA_OTRAS_MODALIDADES_LABEL);
//			
//		}
		
		
		retorno.setPrtrId(rs.getInt(JdbcConstantes.PRTR_ID));
		retorno.setPrtrTipoProceso(rs.getInt(JdbcConstantes.PRTR_TIPO_PROCESO));
		retorno.setPrtrFechaEjecucion(rs.getTimestamp(JdbcConstantes.PRTR_FECHA_EJECUCION));
		retorno.setPrtrRoflcrId(rs.getInt(JdbcConstantes.PRTR_ROFLCR_ID));
		retorno.setCnvId(rs.getInt(JdbcConstantes.CNV_ID));
		retorno.setCnvDescripcion(rs.getString(JdbcConstantes.CNV_DESCRIPCION));
		
		retorno.setCnvEstado(rs.getInt(JdbcConstantes.CNV_ESTADO));
		retorno.setFclId(rs.getInt(JdbcConstantes.FCL_ID));
		
		retorno.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
		
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
//		retorno.setAsttId(rs.getInt(JdbcConstantes.ASTT_ID));
//		retorno.setAsttTemaTrabajo(rs.getString(JdbcConstantes.ASTT_TEMA_TRABAJO));
//		retorno.setAsttTutor(rs.getString(JdbcConstantes.ASTT_TUTOR));
//		retorno.setMcttcrId(rs.getInt(JdbcConstantes.MCTTCR_ID));
//		retorno.setMcttdescripcion(rs.getString(JdbcConstantes.MCTT_DESCRIPCION));
		retorno.setMcttId(rs.getInt(JdbcConstantes.MCTT_ID));
		
		
		
		return retorno;
	}
	
	private EstudianteAptitudOtrosMecanismosJdbcDto transformarResultSetADtoAsignadosMecanismosParaAptitud(ResultSet rs) throws SQLException{
		EstudianteAptitudOtrosMecanismosJdbcDto retorno = new EstudianteAptitudOtrosMecanismosJdbcDto();
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_TIPO_IDENTIFICACION));
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setFcesCrrEstudPrevios(rs.getString(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS));
		retorno.setFcesTiempoEstudRec(rs.getInt(JdbcConstantes.FCES_TIEMPO_ESTUD_REC));
		retorno.setFcesTipoDuracionRec(rs.getInt(JdbcConstantes.FCES_TIPO_DURAC_REC));
		
		retorno.setFcesTipoColegio(rs.getInt(JdbcConstantes.FCES_TIPO_COLEGIO));
		retorno.setFcesTipoColegioSniese(rs.getString(JdbcConstantes.FCES_TIPO_COLEGIO_SNIESE));
		retorno.setFcesTituloBachiller(rs.getString(JdbcConstantes.FCES_TITULO_BACHILLER));
		
		retorno.setFcesFechaEgresamiento(rs.getDate(JdbcConstantes.FCES_FECHA_EGRESAMIENTO));
		
		retorno.setFcesRecEstuPrevios(rs.getInt(JdbcConstantes.FCES_REC_ESTUD_PREVIOS));
		retorno.setFcesRecEstuPreviosSniese(rs.getString(JdbcConstantes.FCES_REC_ESTUD_PREV_SNIESE));
		
		retorno.setFcesFechaCreacion(rs.getTimestamp(JdbcConstantes.FCES_FECHA_CREACION));
		
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsTipoIdentificacion(rs.getInt(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsMailPersonal(rs.getString(JdbcConstantes.PRS_MAIL_PERSONAL));
		retorno.setTrttId(rs.getInt(JdbcConstantes.TRTT_ID));
		retorno.setTrttEstadoTramite(rs.getInt(JdbcConstantes.TRTT_ESTADO_TRAMITE));
		retorno.setTrttCarreraId(rs.getInt(JdbcConstantes.TRTT_CARRERA_ID));
		retorno.setTrttEstadoProceso(rs.getInt(JdbcConstantes.TRTT_ESTADO_PROCESO));
		
		// Solo para validar los combos
		retorno.setTrttObsValSec(rs.getString(JdbcConstantes.TRTT_OBSERVACION));
		
		
//		if (rs.getInt(JdbcConstantes.TRTT_MCTT_ESTADISTICO)==TramiteTituloConstantes.TIPO_MODALIDAD_ESTA_COMPLEXIVO_VALUE){
//			retorno.setTrttMcttEstadisticoSt(TramiteTituloConstantes.TIPO_MODALIDAD_ESTA_COMPLEXIVO_LABEL);
//		}else {
//			retorno.setTrttMcttEstadisticoSt(TramiteTituloConstantes.TIPO_MODALIDAD_ESTA_OTRAS_MODALIDADES_LABEL);
//			
//		}
		
		
		retorno.setPrtrId(rs.getInt(JdbcConstantes.PRTR_ID));
		retorno.setPrtrTipoProceso(rs.getInt(JdbcConstantes.PRTR_TIPO_PROCESO));
		retorno.setPrtrFechaEjecucion(rs.getTimestamp(JdbcConstantes.PRTR_FECHA_EJECUCION));
		retorno.setPrtrRoflcrId(rs.getInt(JdbcConstantes.PRTR_ROFLCR_ID));
		retorno.setCnvId(rs.getInt(JdbcConstantes.CNV_ID));
		retorno.setCnvDescripcion(rs.getString(JdbcConstantes.CNV_DESCRIPCION));
		
		retorno.setCnvEstado(rs.getInt(JdbcConstantes.CNV_ESTADO));
		retorno.setFclId(rs.getInt(JdbcConstantes.FCL_ID));
		
		retorno.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
		
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setAsttId(rs.getInt(JdbcConstantes.ASTT_ID));
		retorno.setAsttTemaTrabajo(rs.getString(JdbcConstantes.ASTT_TEMA_TRABAJO));
		retorno.setAsttTutor(rs.getString(JdbcConstantes.ASTT_DIRECTOR_CIENTIFICO));
		retorno.setAsttAprobaciontutor(rs.getInt(JdbcConstantes.ASTT_APROBACION_TUTOR));
		retorno.setMcttcrId(rs.getInt(JdbcConstantes.MCCR_ID));
		retorno.setMcttdescripcion(rs.getString(JdbcConstantes.MCTT_DESCRIPCION));
		retorno.setMcttId(rs.getInt(JdbcConstantes.MCTT_ID));
		
		
		
		return retorno;
	}
	
	/**
	 * Inserta la nueva entidad indicada 
	 * @param  entidad - nueva entidad a insertar
	 * @return la nueva entidad insertada
	 * @throws  
	 */
	@Override
	public boolean editar(EstudianteAsignacionMecanismoDto entidad, String identificacionEvaluador, RolFlujoCarrera rolFlujoCarrera, MecanismoCarrera mecanismo) throws  EstudianteAsignacionDtoNoEncontradoException , EstudianteAsignacionDtoException{
		boolean retorno = false;
		if (entidad != null && identificacionEvaluador != null && rolFlujoCarrera != null) {
			try {	
				//**********************************************************************
				//********** actualizar el ficha_estudiante actual  ****************
				//**********************************************************************
				
				FichaEstudiante fcesAux = em.find(FichaEstudiante.class, entidad.getFcesId());
				
				fcesAux.setFcesMecanismoCarrera(mecanismo);
				
				//**********************************************************************
				//********** actualizar el tramite actual ******************************
				//**********************************************************************
				
				TramiteTitulo trttAux = em.find(TramiteTitulo.class, entidad.getTrttId());
				
				trttAux.setTrttEstadoProceso(TramiteTituloConstantes.EST_PROC_ASIGNADO_MECANISMO_TITULACION_VALUE);
				
				//*******************************************************************************
				//******** Creo un nuevo proceso_tramite  para no perder el historial ***********
				//*******************************************************************************
				
				ProcesoTramite nuevoPrtr = new ProcesoTramite();
				nuevoPrtr.setPrtrTipoProceso(TramiteTituloConstantes.EST_PROC_ASIGNADO_MECANISMO_TITULACION_VALUE);
				nuevoPrtr.setPrtrFechaEjecucion(entidad.getPrtrFechaEjecucion());
				nuevoPrtr.setPrtrRolFlujoCarrera(rolFlujoCarrera);
				nuevoPrtr.setPrtrTramiteTitulo(trttAux);
				em.persist(nuevoPrtr);
			
				//**********************************************************************
				//******** Creo un nuevo AsignacionTitulacion **************************
				//**********************************************************************
				if(entidad.getAssttTemaTrabajo() == null && entidad.getAssttTutor() == null){
					AsignacionTitulacion nuevoAsgTtl = new AsignacionTitulacion();
					nuevoAsgTtl.setAsttProcesoTramite(nuevoPrtr);
					em.persist(nuevoAsgTtl);
				}else{
					AsignacionTitulacion nuevoAsgTtl = new AsignacionTitulacion();
					nuevoAsgTtl.setAsttTutorMetodologico(entidad.getAssttTutor().toUpperCase().replaceAll(" +", " ").trim());
					nuevoAsgTtl.setAsttDirectorCientifico(entidad.getAssttTutor().toUpperCase().replaceAll(" +", " ").trim());
					nuevoAsgTtl.setAsttTemaTrabajo(entidad.getAssttTemaTrabajo().toUpperCase().replaceAll(" +", " ").trim());
					nuevoAsgTtl.setAsttProcesoTramite(nuevoPrtr);
					nuevoAsgTtl.setAsttAprobacionTutor(0);
					em.persist(nuevoAsgTtl);
				}
			}catch (NoResultException e) {
				throw new EstudianteAsignacionDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteAsignacionDtoServicioImpl.buscar.por.id.no.result.exception")));
			}catch (NonUniqueResultException e) {
				throw new EstudianteAsignacionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteAsignacionDtoServicioImpl.buscar.por.id.non.unique.result.exception")));
			} catch (Exception e) {
				throw new EstudianteAsignacionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteAsignacionDtoServicioImpl.buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	
	/**
	 * Actualiza la nueva entidad indicada en la tabla asignacion_titulacion 
	 * @param  entidad - nueva entidad a insertar
	 * @return la nueva entidad insertada
	 * @throws  
	 */
	@Override
	public boolean editarReasignar(EstudianteAsignacionMecanismoDto entidad, String identificacionEvaluador, RolFlujoCarrera rolFlujoCarrera, MecanismoCarrera mecanismo) throws  EstudianteAsignacionDtoNoEncontradoException , EstudianteAsignacionDtoException{
		boolean retorno = false;
		if (entidad != null && identificacionEvaluador != null && rolFlujoCarrera != null) {
			try {	
				//**********************************************************************
				//********** actualizar el ficha_estudiante actual  ****************
				//**********************************************************************
				FichaEstudiante fcesAux = em.find(FichaEstudiante.class, entidad.getFcesId());
				fcesAux.setFcesMecanismoCarrera(mecanismo);
				//**********************************************************************
				//******** Busco asignacion_titulacion del postulante ******************
				//**********************************************************************
				AsignacionTitulacion asttAux=em.find(AsignacionTitulacion.class, entidad.getAssttId());
				if(entidad.getAssttTemaTrabajo().equals(MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_LABEL)
						&& entidad.getAssttTutor().equals(MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_LABEL)){
					asttAux.setAsttTemaTrabajo(null);
					asttAux.setAsttTutorMetodologico(null);
				}else{
					asttAux.setAsttTemaTrabajo(entidad.getAssttTemaTrabajo().toUpperCase().replaceAll(" +", " ").trim());
					asttAux.setAsttTutorMetodologico(entidad.getAssttTutor().toUpperCase().replaceAll(" +", " ").trim());
				}
				TramiteTitulo trttAux = em.find(TramiteTitulo.class, entidad.getTrttId());
				ProcesoTramite nuevoPrtr = new ProcesoTramite();
				nuevoPrtr.setPrtrTipoProceso(TramiteTituloConstantes.ESTADO_PROCESO_CAMBIO_MECANISMO_VALUE);
				nuevoPrtr.setPrtrFechaEjecucion(entidad.getPrtrFechaEjecucion());
				nuevoPrtr.setPrtrRolFlujoCarrera(rolFlujoCarrera);
				nuevoPrtr.setPrtrTramiteTitulo(trttAux);
				em.persist(nuevoPrtr);
			}catch (NoResultException e) {
				throw new EstudianteAsignacionDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteAsignacionDtoServicioImpl.buscar.por.id.no.result.exception")));
			}catch (NonUniqueResultException e) {
				throw new EstudianteAsignacionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteAsignacionDtoServicioImpl.buscar.por.id.non.unique.result.exception")));
			} catch (Exception e) {
				e.printStackTrace();
				throw new EstudianteAsignacionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteAsignacionDtoServicioImpl.buscar.por.id.exception")));
			}
		}
		return retorno;
		
	}
	
	/**
	 * Método que devuelve una lista de estudiantes por carrera que tenga complexivo y convocatoria
	 * @param idCarrera
	 * @param idConvocatoria
	 * @return
	 * @throws CarreraDtoJdbcException
	 * @throws CarreraDtoJdbcNoEncontradoException
	 */
	@Override
	public List<EstudianteDetalleComplexivoDto> listarEstudiantesXIdCarreraXComplexivoXConvocatoria(int idCarrera, int idConvocatoria)
			throws EstudianteAsignacionDtoNoEncontradoException , EstudianteAsignacionDtoException {
		List<EstudianteDetalleComplexivoDto> retorno = new ArrayList<EstudianteDetalleComplexivoDto>();
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append("  prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
//			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_MCTTCR_ID);
//			sbSql.append(" , mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_PORCENTAJE);
//			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION_CARRERA);sbSql.append(" mcttcr ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION);sbSql.append(" mctt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ");
			sbSql.append(" RIGHT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ON trtt.");
			sbSql.append(JdbcConstantes.TRTT_CARRERA_ID); sbSql.append(" =  crr.");sbSql.append(JdbcConstantes.CRR_ID);
			
			sbSql.append(" WHERE ");
			
//			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
//			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.MCTTCR_ID);sbSql.append(" = ");sbSql.append(" mcttcr.");sbSql.append(JdbcConstantes.FCES_MCTTCR_ID);
//			sbSql.append(" AND mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_MCTT_ID);sbSql.append(" = ");sbSql.append(" mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
//			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_APTO_EVALUADO_VALUE);
			sbSql.append(" AND mctt.");sbSql.append(JdbcConstantes.MCTT_ID);sbSql.append(" = ");sbSql.append(MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_VALUE);
			
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
			sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idCarrera); //cargo el código de carrera
			pstmt.setInt(2, idConvocatoria); //cargo el código de carrera
			rs = pstmt.executeQuery();
			while(rs.next()){
				retorno.add(transformarResultSetADtoAsignadosMecanismosComplexivo(rs));
			}
		} catch (SQLException e) {
		} catch (Exception e) {
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
	
	
	
	/**
	 * Método que devuelve una lista de estudiantes por carrera que tenga complexivo de gracia y convocatoria
	 * @param idCarrera
	 * @param idConvocatoria
	 * @return
	 * @throws CarreraDtoJdbcException
	 * @throws CarreraDtoJdbcNoEncontradoException
	 */
	@Override
	public List<EstudianteDetalleComplexivoDto> listarEstudiantesXIdCarreraXComplexivoGraciaXConvocatoria(int idCarrera, int idConvocatoria)
			throws EstudianteAsignacionDtoNoEncontradoException , EstudianteAsignacionDtoException {
		List<EstudianteDetalleComplexivoDto> retorno = new ArrayList<EstudianteDetalleComplexivoDto>();
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append("  prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
//			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_MCTTCR_ID);
//			sbSql.append(" , CASE WHEN mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_PORCENTAJE); sbSql.append(" IS NULL THEN ");
//			sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
//			sbSql.append(" ELSE mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_PORCENTAJE); 
//			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.MCTTCR_PORCENTAJE);
//			
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);
			sbSql.append(" , fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
//			sbSql.append(" , asno.");sbSql.append(JdbcConstantes.ASNO_COMPLEXIVO_PRACTICO);
//			sbSql.append(" , asno.");sbSql.append(JdbcConstantes.ASNO_ID);
//			sbSql.append(" , prtr.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION_CARRERA);sbSql.append(" mcttcr ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION);sbSql.append(" mctt ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ");
			sbSql.append(" WHERE ");
			
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
//			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append(" = ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);
//			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.MCTTCR_ID);sbSql.append(" = ");sbSql.append(" mcttcr.");sbSql.append(JdbcConstantes.FCES_MCTTCR_ID);
//			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
//			sbSql.append(" AND mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_MCTT_ID);sbSql.append(" = ");sbSql.append(" mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
//			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND (trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_FINAL_CARGADO_VALUE);
			sbSql.append(" OR trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_PRACTICO_GRACIA_VALUE);
			sbSql.append(" OR trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_PRACTICO_CARGADO_VALUE);
			sbSql.append(" OR trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_APTO_EVALUADO_VALUE);
			sbSql.append(" ) ");
			sbSql.append(" AND mctt.");sbSql.append(JdbcConstantes.MCTT_ID);sbSql.append(" = ");sbSql.append(MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_VALUE);
			
//			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = ");sbSql.append(" prtr.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
//			sbSql.append(" AND prtr.");sbSql.append(JdbcConstantes.PRTR_ID);sbSql.append(" = ");sbSql.append(" asno.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID);
//			
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
			sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ? ");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idCarrera); //cargo el código de carrera
			pstmt.setInt(2, idConvocatoria); //cargo el código de convocatoria
			rs = pstmt.executeQuery();
			while(rs.next()){
				retorno.add(transformarResultSetADtoAsentadosNotasPractico(rs));
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
	
	private EstudianteDetalleComplexivoDto transformarResultSetADtoAsentadosNotasPractico(ResultSet rs) throws SQLException{
		EstudianteDetalleComplexivoDto retorno = new EstudianteDetalleComplexivoDto();
		retorno.setTrttId(rs.getInt(JdbcConstantes.TRTT_ID));
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
//		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
//		retorno.setFcesMecanismoTitulacionCarrera(rs.getInt(JdbcConstantes.FCES_MCTTCR_ID));
//		retorno.setMcttcrPorcentaje(rs.getInt(JdbcConstantes.MCTTCR_PORCENTAJE));
//		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setTrttEstadoProceso(rs.getInt(JdbcConstantes.TRTT_ESTADO_PROCESO));
		retorno.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
		retorno.setPrsMailPersonal(rs.getString(JdbcConstantes.PRS_MAIL_PERSONAL));
		PreparedStatement pstmt = null;
		Connection con = null;
		
		@SuppressWarnings("unused")
		ResultSet resultSet = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
//			sbSql.append(" asno.");sbSql.append(JdbcConstantes.ASNO_ID);
//			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_ASENTAMIENTO_NOTA);sbSql.append(" asno ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ");
			sbSql.append(" WHERE ");
//			sbSql.append(" asno.");sbSql.append(JdbcConstantes.ASNO_PRTR_ID);sbSql.append(" = ");sbSql.append(" prtr.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" AND prtr.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = ?");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, retorno.getTrttId()); //cargo el código de carrera
			rs = pstmt.executeQuery();
//			retorno.setAsnoId(rs.getInt(JdbcConstantes.ASNO_ID));
		} catch (NoResultException e) {
			retorno.setAsnoId(GeneralesConstantes.APP_ID_BASE);
		} catch (Exception e) {
			retorno.setAsnoId(GeneralesConstantes.APP_ID_BASE);
		}
		finally {
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
	 * Método que devuelve una lista de estudiantes por carrera que tenga complexivo práctico y convocatoria
	 * @param idCarrera
	 * @param idConvocatoria
	 * @return
	 * @throws CarreraDtoJdbcException
	 * @throws CarreraDtoJdbcNoEncontradoException
	 */
	@Override
	public List<EstudianteDetalleComplexivoDto> listarEstudiantesAptosXIdCarreraXComplexivoXConvocatoria(int idCarrera, int idConvocatoria)
			throws EstudianteAsignacionDtoNoEncontradoException , EstudianteAsignacionDtoException {
		List<EstudianteDetalleComplexivoDto> retorno = new ArrayList<EstudianteDetalleComplexivoDto>();
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append("  prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
//			sbSql.append(" , fces.");sbSql.append(JdbcConstantes.FCES_MCTTCR_ID);
//			sbSql.append(" , mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_PORCENTAJE);
//			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
//			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION_CARRERA);sbSql.append(" mcttcr ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION);sbSql.append(" mctt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ");
			sbSql.append(" RIGHT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ON trtt.");
			sbSql.append(JdbcConstantes.TRTT_CARRERA_ID); sbSql.append(" =  crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" WHERE ");
//			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
//			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.MCTTCR_ID);sbSql.append(" = ");sbSql.append(" mcttcr.");sbSql.append(JdbcConstantes.FCES_MCTTCR_ID);
//			sbSql.append(" AND mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_MCTT_ID);sbSql.append(" = ");sbSql.append(" mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
//			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_APTO_EVALUADO_VALUE);
			sbSql.append(" AND mctt.");sbSql.append(JdbcConstantes.MCTT_ID);sbSql.append(" = ");sbSql.append(MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_VALUE);
			if(idCarrera==GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" > ? ");
			}else{
				sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ? ");
			}
			if(idCarrera==GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" > ? ");
			}else{
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ? ");
			}
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idCarrera); //cargo el código de carrera
			pstmt.setInt(2, idConvocatoria); //cargo el código de carrera
			rs = pstmt.executeQuery();
			while(rs.next()){
				retorno.add(transformarResultSetADtoAsignadosMecanismosComplexivo(rs));
			}
		} catch (SQLException e) {
		} catch (Exception e) {
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
	
	
	/**
	 * Método que devuelve una lista de estudiantes por carrera que hayan sido declarados su aptitud
	 * @param idCarrera
	 * @param idConvocatoria
	 * @return
	 * @throws CarreraDtoJdbcException
	 * @throws CarreraDtoJdbcNoEncontradoException
	 */
	@Override
	public List<EstudianteAptitudJdbcDto> listarEstudiantesAptosNoAptosXIdCarreraXConvocatoria(
			String cedula, String cedulaEvaluador,int idCarrera, int idConvocatoria)
			throws EstudianteAsignacionDtoNoEncontradoException , EstudianteAsignacionDtoException {
		List<EstudianteAptitudJdbcDto> retorno = new ArrayList<EstudianteAptitudJdbcDto>();
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			if(cedula.equals("")) cedula="%%";
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);
			sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);
			sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" , mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
//			sbSql.append(" , mctt.");sbSql.append(JdbcConstantes.MCTT_DESCRIPCION);
//			sbSql.append(" , apt.");sbSql.append(JdbcConstantes.APT_ID);
//			sbSql.append(" , cnv.");sbSql.append(JdbcConstantes.CNV_ID);
//			sbSql.append(" , CASE WHEN apt.");sbSql.append(JdbcConstantes.APT_APROBO_ACTUALIZAR); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
//			sbSql.append(" ELSE apt.");sbSql.append(JdbcConstantes.APT_APROBO_ACTUALIZAR); 
//			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.APT_APROBO_ACTUALIZAR); 
//			sbSql.append(" , apt.");sbSql.append(JdbcConstantes.APT_REQUISITOS);
//			sbSql.append(" , apt.");sbSql.append(JdbcConstantes.APT_REPROBO_CREDITOS);
//			sbSql.append(" , apt.");sbSql.append(JdbcConstantes.APT_SEGUNDA_CARRERA);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_APTITUD);sbSql.append(" apt ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION_CARRERA);sbSql.append(" mcttcr ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION);sbSql.append(" mctt ");
//			sbSql.append(" WHERE ");
//			sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
//			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_MCTTCR_ID); sbSql.append(" =  mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_ID);
//			sbSql.append(" AND mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_MCTT_ID); sbSql.append(" =  mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
//			
//			sbSql.append(" AND apt.");sbSql.append(JdbcConstantes.APT_PRTR_ID); sbSql.append(" =  prtr.");sbSql.append(JdbcConstantes.PRTR_ID);
//			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID); sbSql.append(" =  crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" = fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" AND (trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_APTO_VALUE);
			sbSql.append(" OR trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_NO_APTO_VALUE);
			sbSql.append(" ) ");
			sbSql.append(" AND mctt.");sbSql.append(JdbcConstantes.MCTT_ID);sbSql.append(" = ");sbSql.append(MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_VALUE);
			if(idConvocatoria==GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" > ? ");
			}else{
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ? ");
			}
			
			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" LIKE ? ");
			sbSql.append(" AND crr.crr_id IN ");
			sbSql.append("(SELECT DISTINCT crr.crr_id from carrera crr, rol_flujo_carrera roflcr, usuario_rol usro, usuario usr ");  
			sbSql.append(" WHERE usr.usr_id =  usro.usr_id  AND  usro.usro_id =  roflcr.usro_id "); 
			sbSql.append(" AND  crr.crr_id =  roflcr.crr_id AND  usr.usr_identificacion LIKE '");
			sbSql.append(cedulaEvaluador);sbSql.append("') ");
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idConvocatoria); //cargo el código de carrera
			pstmt.setString(2, cedula); //cargo la identificacion del estudiante
			rs = pstmt.executeQuery();
			while(rs.next()){
				retorno.add(transformarResultSetADtoAptosNoAptos(rs));
			}
		} catch (SQLException e) {
		} catch (Exception e) {
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
	
	/**
	 * Método que devuelve una lista de estudiantes por carrera que hayan sido declarados su aptitud
	 * @param idCarrera
	 * @param idConvocatoria
	 * @return
	 * @throws CarreraDtoJdbcException
	 * @throws CarreraDtoJdbcNoEncontradoException
	 */
	@Override
	public List<EstudianteAptitudJdbcDto> listarEstudiantesAptosNoAptosXIdCarreraXConvocatoriaOtrosMecanismos(
			String cedula, String cedulaEvaluador,int idCarrera, int idConvocatoria)
			throws EstudianteAsignacionDtoNoEncontradoException , EstudianteAsignacionDtoException {
		List<EstudianteAptitudJdbcDto> retorno = new ArrayList<EstudianteAptitudJdbcDto>();
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			if(cedula.equals("")) cedula="%%";
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);
			sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);
			sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" , mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
			sbSql.append(" , mctt.");sbSql.append(JdbcConstantes.MCTT_DESCRIPCION);
//			sbSql.append(" , apt.");sbSql.append(JdbcConstantes.APT_ID);
//			sbSql.append(" , cnv.");sbSql.append(JdbcConstantes.CNV_ID);
//			sbSql.append(" , CASE WHEN apt.");sbSql.append(JdbcConstantes.APT_APROBO_ACTUALIZAR); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
//			sbSql.append(" ELSE apt.");sbSql.append(JdbcConstantes.APT_APROBO_ACTUALIZAR); 
//			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.APT_APROBO_ACTUALIZAR); 
//			sbSql.append(" , apt.");sbSql.append(JdbcConstantes.APT_REQUISITOS);
//			sbSql.append(" , apt.");sbSql.append(JdbcConstantes.APT_REPROBO_CREDITOS);
//			sbSql.append(" , apt.");sbSql.append(JdbcConstantes.APT_SEGUNDA_CARRERA);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_APTITUD);sbSql.append(" apt ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION_CARRERA);sbSql.append(" mcttcr ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION);sbSql.append(" mctt ");
			sbSql.append(" WHERE ");
//			sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID); sbSql.append(" =  prtr.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
//			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_MCTTCR_ID); sbSql.append(" =  mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_ID);
//			sbSql.append(" AND mcttcr.");sbSql.append(JdbcConstantes.MCTTCR_MCTT_ID); sbSql.append(" =  mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
//			
//			sbSql.append(" AND apt.");sbSql.append(JdbcConstantes.APT_PRTR_ID); sbSql.append(" =  prtr.");sbSql.append(JdbcConstantes.PRTR_ID);
//			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID); sbSql.append(" =  crr.");sbSql.append(JdbcConstantes.CRR_ID);
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
			if(idConvocatoria==GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" > ? ");
			}else{
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ? ");
			}
			
			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" LIKE ? ");
			sbSql.append(" AND crr.crr_id IN ");
			sbSql.append("(SELECT DISTINCT crr.crr_id from carrera crr, rol_flujo_carrera roflcr, usuario_rol usro, usuario usr ");  
			sbSql.append(" WHERE usr.usr_id =  usro.usr_id  AND  usro.usro_id =  roflcr.usro_id "); 
			sbSql.append(" AND  crr.crr_id =  roflcr.crr_id AND  usr.usr_identificacion LIKE '");
			sbSql.append(cedulaEvaluador);sbSql.append("') ");
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idConvocatoria); //cargo el código de carrera
			pstmt.setString(2, cedula); //cargo la identificacion del estudiante
			rs = pstmt.executeQuery();
			while(rs.next()){
				retorno.add(transformarResultSetADtoAptosNoAptos(rs));
			}
			
		} catch (SQLException e) {
		} catch (Exception e) {
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
	
	private EstudianteAptitudJdbcDto transformarResultSetADtoAsignadosMecanismosDGA(ResultSet rs) throws SQLException{
		EstudianteAptitudJdbcDto retorno = new EstudianteAptitudJdbcDto();
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_TIPO_IDENTIFICACION));
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setFcesCrrEstudPrevios(rs.getString(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS));
		retorno.setFcesTiempoEstudRec(rs.getInt(JdbcConstantes.FCES_TIEMPO_ESTUD_REC));
		retorno.setFcesTipoDuracionRec(rs.getInt(JdbcConstantes.FCES_TIPO_DURAC_REC));
		
		retorno.setFcesTipoColegio(rs.getInt(JdbcConstantes.FCES_TIPO_COLEGIO));
		retorno.setFcesTipoColegioSniese(rs.getString(JdbcConstantes.FCES_TIPO_COLEGIO_SNIESE));
		retorno.setFcesTituloBachillerId(rs.getInt(JdbcConstantes.FCES_TITULO_BACHILLER));
		
		retorno.setFcesFechaEgresamiento(rs.getDate(JdbcConstantes.FCES_FECHA_EGRESAMIENTO));
		
		retorno.setFcesRecEstuPrevios(rs.getInt(JdbcConstantes.FCES_REC_ESTUD_PREVIOS));
		retorno.setFcesRecEstuPreviosSniese(rs.getString(JdbcConstantes.FCES_REC_ESTUD_PREV_SNIESE));
		
		retorno.setFcesFechaCreacion(rs.getTimestamp(JdbcConstantes.FCES_FECHA_CREACION));
		
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsTipoIdentificacion(rs.getInt(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsMailPersonal(rs.getString(JdbcConstantes.PRS_MAIL_PERSONAL));
		retorno.setTrttId(rs.getInt(JdbcConstantes.TRTT_ID));
		retorno.setTrttEstadoTramite(rs.getInt(JdbcConstantes.TRTT_ESTADO_TRAMITE));
		retorno.setTrttCarrera(rs.getInt(JdbcConstantes.TRTT_CARRERA));
		retorno.setTrttCarreraId(rs.getInt(JdbcConstantes.TRTT_CARRERA_ID));
		
		
		// Solo para validar los combos
		retorno.setTrttObsValSec(rs.getString(JdbcConstantes.TRTT_OBSERVACION));
		
		retorno.setTrttMcttEstadistico(rs.getInt(JdbcConstantes.TRTT_MCTT_ESTADISTICO));
		
//		if (rs.getInt(JdbcConstantes.TRTT_MCTT_ESTADISTICO)==TramiteTituloConstantes.TIPO_MODALIDAD_ESTA_COMPLEXIVO_VALUE){
//			retorno.setTrttMcttEstadisticoSt(TramiteTituloConstantes.TIPO_MODALIDAD_ESTA_COMPLEXIVO_LABEL);
//		}else {
//			retorno.setTrttMcttEstadisticoSt(TramiteTituloConstantes.TIPO_MODALIDAD_ESTA_OTRAS_MODALIDADES_LABEL);
//			
//		}
		
		
		retorno.setPrtrId(rs.getInt(JdbcConstantes.PRTR_ID));
		retorno.setPrtrTipoProceso(rs.getInt(JdbcConstantes.PRTR_TIPO_PROCESO));
		retorno.setPrtrFechaEjecucion(rs.getTimestamp(JdbcConstantes.PRTR_FECHA_EJECUCION));
		retorno.setPrtrRoflcrId(rs.getInt(JdbcConstantes.PRTR_ROFLCR_ID));
		retorno.setCnvId(rs.getInt(JdbcConstantes.CNV_ID));
		retorno.setCnvDescripcion(rs.getString(JdbcConstantes.CNV_DESCRIPCION));
		
		retorno.setCnvEstado(rs.getInt(JdbcConstantes.CNV_ESTADO));
		retorno.setFclId(rs.getInt(JdbcConstantes.FCL_ID));
		
		retorno.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
		
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
//		retorno.setAptId(rs.getInt(JdbcConstantes.APT_ID));
//		retorno.setAptAproboActualizacion(rs.getInt(JdbcConstantes.APT_APROBO_ACTUALIZAR));
//		retorno.setAptReproboCreditos(rs.getInt(JdbcConstantes.APT_REPROBO_CREDITOS));
//		retorno.setAptRequisitos(rs.getInt(JdbcConstantes.APT_REQUISITOS));
//		retorno.setAptSegundaCarrera(rs.getInt(JdbcConstantes.APT_SEGUNDA_CARRERA));
//		retorno.setAptResultado(getLabelAptitud(rs.getInt(JdbcConstantes.APT_REQUISITOS)));
//		retorno.setMcttcrId(rs.getInt(JdbcConstantes.MCTTCR_ID));
		retorno.setMcttdescripcion(rs.getString(JdbcConstantes.MCTT_DESCRIPCION));
		retorno.setMcttId(rs.getInt(JdbcConstantes.MCTT_ID));
		
		
		return retorno;
	}
	
	
	private EstudianteDetalleComplexivoDto transformarResultSetADtoAsignadosMecanismosComplexivo(ResultSet rs) throws SQLException{
		EstudianteDetalleComplexivoDto retorno = new EstudianteDetalleComplexivoDto();
		
		retorno.setTrttId(rs.getInt(JdbcConstantes.TRTT_ID));
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
//		retorno.setFcesMecanismoTitulacionCarrera(rs.getInt(JdbcConstantes.FCES_MCTTCR_ID));
//		retorno.setMcttcrPorcentaje(rs.getInt(JdbcConstantes.MCTTCR_PORCENTAJE));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		return retorno;
	}
	
	private EstudianteAptitudJdbcDto transformarResultSetADtoAptosNoAptos(ResultSet rs) throws SQLException{
		EstudianteAptitudJdbcDto retorno = new EstudianteAptitudJdbcDto();
		
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsMailPersonal(rs.getString(JdbcConstantes.PRS_MAIL_PERSONAL));
		retorno.setTrttEstadoProceso(rs.getInt(JdbcConstantes.TRTT_ESTADO_PROCESO));
		retorno.setTrttId(rs.getInt(JdbcConstantes.TRTT_ID));
		retorno.setMcttId(rs.getInt(JdbcConstantes.MCTT_ID));
		retorno.setMcttdescripcion(rs.getString(JdbcConstantes.MCTT_DESCRIPCION));
//		retorno.setTrttCarreraId(rs.getInt(JdbcConstantes.TRTT_CARRERA_ID));
//		retorno.setAptId(rs.getInt(JdbcConstantes.APT_ID));
//		retorno.setAptAproboActualizacion(rs.getInt(JdbcConstantes.APT_APROBO_ACTUALIZAR));
//		retorno.setAptReproboCreditos(rs.getInt(JdbcConstantes.APT_REPROBO_CREDITOS));
//		retorno.setAptSegundaCarrera(rs.getInt(JdbcConstantes.APT_SEGUNDA_CARRERA));
//		retorno.setAptRequisitos(rs.getInt(JdbcConstantes.APT_REQUISITOS));
		retorno.setCnvId(rs.getInt(JdbcConstantes.CNV_ID));
		return retorno;
	}
	
	
	
	
	
	
	/**
	 * Obtiente el label del resultado de la aptitud del postulante
	 * @param aptRequisitos - resultado de la aptitud
	 * @return label del valor del usuario
	 */
	public String getLabelAptitud(int aptRequisitos){
		switch (aptRequisitos) {
		case 0:
			return "APTO";
		case 1:
			return "NO APTO";
		} 
		return null;
	}
}
