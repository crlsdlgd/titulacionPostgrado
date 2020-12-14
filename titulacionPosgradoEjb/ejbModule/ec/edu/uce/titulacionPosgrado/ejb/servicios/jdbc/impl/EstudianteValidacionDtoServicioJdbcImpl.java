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
 26/02/2018				Daniel Albuja				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.CarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteAptitudJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudiantePostuladoJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteValidacionJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudiantePostuladoJdbcDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudiantePostuladoJdbcDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EvaluadosDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EvaluadosDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.ProcesoTramiteServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.TramiteTituloServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.EstudianteValidacionDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.AptitudConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.ConvocatoriaConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.FichaEstudianteConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.InstitucionAcademicaConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.PersonaConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.ProcesoTramiteConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.TramiteTituloConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.ValidacionConstantes;
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
public class EstudianteValidacionDtoServicioJdbcImpl implements EstudianteValidacionDtoServicioJdbc {
	
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
	 * Método que busca un estudiante por identificacion, por carrera, por estado convocatoria
	 * @param idCarrera - id de la carrera que ha seleccionado en la búsqueda .
	 * @param listCarreras - lista de carreras en caso de que haya seleccionado todas las carreras disponibles para el ususario.
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudianteValidacionJdbcDtoException 
	 * @throws EstudianteValidacionJdbcDtoNoEncontradoException 
	 */
	public List<EstudianteValidacionJdbcDto> buscarPostulacionesEstudianteXFacultadXcarreraXIndetificacionXEstadoConvocatoria(String identificacion, int idFacultad, String idCarrera, int estadoConvocatoria  ) throws EstudiantePostuladoJdbcDtoException, EstudiantePostuladoJdbcDtoNoEncontradoException {
		List<EstudianteValidacionJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" DISTINCT(");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);
			sbSql.append("), ");
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
//			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_TITULO_BACHILLER);
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREVIOS); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREVIOS); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREVIOS);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREV_SNIESE);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_FECHA_CREACION);
			sbSql.append(" ,ubcp.");sbSql.append(JdbcConstantes.UBC_ID);sbSql.append(" AS paisid ");
			sbSql.append(" ,ubcp.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" AS paisdesc ");
			sbSql.append(" ,ubcc.");sbSql.append(JdbcConstantes.UBC_ID);sbSql.append(" AS cantonid ");
			sbSql.append(" ,ubcc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" AS cantondesc ");
			sbSql.append(" ,etn.");sbSql.append(JdbcConstantes.ETN_ID);
			sbSql.append(" ,etn.");sbSql.append(JdbcConstantes.ETN_DESCRIPCION);
			sbSql.append(" , CASE WHEN inac.");sbSql.append(JdbcConstantes.INAC_ID); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE inac.");sbSql.append(JdbcConstantes.INAC_ID); 
						sbSql.append(" END AS universidadid ");
			sbSql.append(" , CASE WHEN inac.");sbSql.append(JdbcConstantes.INAC_DESCRIPCION); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE inac.");sbSql.append(JdbcConstantes.INAC_DESCRIPCION); 
						sbSql.append(" END AS universidaddesc ");
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
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_MCTT_ESTADISTICO);
			sbSql.append(" ,prtr.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" ,prtr.");sbSql.append(JdbcConstantes.PRTR_TIPO_PROCESO);
			sbSql.append(" ,prtr.");sbSql.append(JdbcConstantes.PRTR_FECHA_EJECUCION);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ID);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ESTADO);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
//			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , CASE WHEN ttl.");sbSql.append(JdbcConstantes.TTL_ID); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
					sbSql.append(" ELSE ttl.");sbSql.append(JdbcConstantes.TTL_ID); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TTL_ID);
			sbSql.append(" , CASE WHEN ttl.");sbSql.append(JdbcConstantes.TTL_DESCRIPCION); sbSql.append(" IS NULL THEN ");sbSql.append("''");
					sbSql.append(" ELSE ttl.");sbSql.append(JdbcConstantes.TTL_DESCRIPCION); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TTL_DESCRIPCION);
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" RIGHT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ON ");
						sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" RIGHT JOIN ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ON ");
						sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr ON ");
						sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = ");sbSql.append(" prtr.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" RIGHT JOIN ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ON ");
						sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);sbSql.append(" = ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ON ");
						sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_INSTITUCION_ACADEMICA);sbSql.append(" inac ON ");
						sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_INAC_ID_INST_EST_PREVIOS);sbSql.append(" = ");sbSql.append(" inac.");sbSql.append(JdbcConstantes.INAC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ON ");
						sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append(" = ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" RIGHT JOIN ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubcc ON ");
						sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_UBC_CANTON_RESIDENCIA);sbSql.append(" = ");sbSql.append(" ubcc.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" RIGHT JOIN ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubcp ON ");
						sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_UBC_ID);sbSql.append(" = ");sbSql.append(" ubcp.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" RIGHT JOIN ");sbSql.append(JdbcConstantes.TABLA_ETNIA);sbSql.append(" etn ON ");
						sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ETN_ID);sbSql.append(" = ");sbSql.append(" etn.");sbSql.append(JdbcConstantes.ETN_ID);
			sbSql.append(" RIGHT JOIN ");sbSql.append(JdbcConstantes.TABLA_TITULO);sbSql.append(" ttl ON ");
						sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_TITULO_BACHILLER);sbSql.append(" = ");sbSql.append(" ttl.");sbSql.append(JdbcConstantes.TTL_ID);
						
						sbSql.append(" WHERE ");
			//identificacion
			sbSql.append(" UPPER(prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(") like ? ");
			//facultad
			if(idFacultad == GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" > ");sbSql.append(" ? ");
			}else{
				sbSql.append(" AND ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" = ");sbSql.append(" ? ");
			}
			//estado convocatoria
			sbSql.append(" AND ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ESTADO);sbSql.append(" = ");sbSql.append(" ? ");			
			//carrera
			if(idCarrera != null){
				sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA);sbSql.append(" = ");sbSql.append(" ? ");
			}
			sbSql.append(" ORDER BY ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
						sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);

			//FIN QUERY
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, "%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase())+"%"); //cargo la identificacion
			pstmt.setInt(2, idFacultad); //cargo la facultad
			pstmt.setInt(3, estadoConvocatoria); //cargo el estado
			if(idCarrera != null){
				pstmt.setString(4, idCarrera); //cargo la carrera
			}
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<EstudianteValidacionJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
			
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new EstudiantePostuladoJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteValidacionJdbcDto.sql.exception")));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new EstudiantePostuladoJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.identificacion.facultad.carrera.convocatoria.exception")));
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
			throw new EstudiantePostuladoJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.identificacion.facultad.carrera.convocatoria.no.result.exception")));
		}
		return retorno;
	}
	
	/**
	 * Método que busca un estudiante por identificacion y convocatoria de acuerdo a su carrera
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudianteValidacionJdbcDtoException 
	 * @throws EstudianteValidacionJdbcDtoNoEncontradoException 
	 */
	public List<EstudianteValidacionJdbcDto> buscarPostulacionesEstudianteXIndetificacionXCarreraXConvocatoria(String identificacion, String idSecretaria, CarreraDto carreraDto , int convocatoria) throws EstudiantePostuladoJdbcDtoException, EstudiantePostuladoJdbcDtoNoEncontradoException {
		List<EstudianteValidacionJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" DISTINCT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(", ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_REG_TTL_SENESCYT); sbSql.append(" IS NULL THEN ");sbSql.append("''");
			sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_REG_TTL_SENESCYT); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_REG_TTL_SENESCYT);
			
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);
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
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ID);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ESTADO);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
//			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,prtr.");sbSql.append(JdbcConstantes.PRTR_REG_TTL_SENECYT);
			sbSql.append(" ,prtr.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces , ");
						
			sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt , ");
						
			sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv , ");
						
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr , ");
			
			sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl , ");
						
			sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr  ");
												
			sbSql.append(" WHERE ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = ");sbSql.append(" prtr.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append(" = ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);sbSql.append(" = ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			// Si fue seleccionada la carrera
			if(carreraDto.getCrrId()!= GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND crr.crr_id = ");sbSql.append(carreraDto.getCrrId());sbSql.append(" AND ");	
			}else{
				sbSql.append(" AND crr.crr_id > ");sbSql.append(GeneralesConstantes.APP_ID_BASE);sbSql.append(" AND ");
			}
						
			//identificacion
			sbSql.append(" UPPER(prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(") like ? ");
			
			sbSql.append(" AND (trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" = ");
			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_POSTULADO_VALUE);
			sbSql.append(" OR trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" = ");
			sbSql.append(TramiteTituloConstantes.EST_PROC_ASIGNADO_REVERSADO_VALUE);sbSql.append(" ) ");
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");
			sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND prtr.");sbSql.append(JdbcConstantes.PRTR_TIPO_PROCESO);sbSql.append(" = ");sbSql.append(ProcesoTramiteConstantes.TIPO_PROCESO_POSTULACION_VALUE);
			
			if(convocatoria != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ?");
			}else{
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" > ?");
			}
				// Si no es la primera convocatoria se debe seleccionar el id de la carrera a la que se postuló
				sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" IN ( ");
				sbSql.append(" SELECT DISTINCT crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" from ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
				sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr, ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);
				sbSql.append(" usro, ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");sbSql.append(" WHERE ");
				sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ");
				sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" = ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");
							sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" AND ");sbSql.append(" usr.");
							sbSql.append(JdbcConstantes.USR_IDENTIFICACION);sbSql.append(" LIKE ? ");
			sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ");
			sbSql.append(RolFlujoCarreraConstantes.ROL_FLUJO_CARRERA_ESTADO_ACTIVO_VALUE);sbSql.append(" ) ");

			// PRUEBA PARA CONVOCATORIA ACTIVA Y EN ESTADO DE VALDIACION
			sbSql.append( " AND (cnv.");sbSql.append(JdbcConstantes.CNV_ESTADO);sbSql.append(" = ");
			sbSql.append(ConvocatoriaConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append( " OR cnv.");sbSql.append(JdbcConstantes.CNV_ESTADO);sbSql.append(" = ");
			sbSql.append(ConvocatoriaConstantes.ESTADO_PENDIENTE_VALUE);sbSql.append(" ) ");
			
			
			sbSql.append(" ORDER BY  ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			
			//FIN QUERY
			con = ds.getConnection();

			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, "%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase())+"%"); //cargo la identificacion
			
			pstmt.setInt(2, convocatoria);
			pstmt.setString(3, idSecretaria);

//			System.out.println(sbSql);
//			System.out.println(convocatoria);
//			System.out.println(idSecretaria);
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<EstudianteValidacionJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXCarrera(rs));
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
			throw new EstudiantePostuladoJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteValidacionJdbcDto.sql.exception")));
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
			throw new EstudiantePostuladoJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.identificacion.facultad.carrera.convocatoria.exception")));
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
			throw new EstudiantePostuladoJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.identificacion.facultad.carrera.convocatoria.no.result.exception")));
		}
		return retorno;
	}
	
	/**
	 * Método que busca un estudiante por identificacion y convocatoria
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudianteValidacionJdbcDtoException 
	 * @throws EstudianteValidacionJdbcDtoNoEncontradoException 
	 */
	public List<EstudianteValidacionJdbcDto> buscarPostulacionesEstudianteXIndetificacion(String identificacion) throws EstudiantePostuladoJdbcDtoException, EstudiantePostuladoJdbcDtoNoEncontradoException {
		List<EstudianteValidacionJdbcDto> retorno = null;
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
//			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_TITULO_BACHILLER);
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREVIOS); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE fces.");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREVIOS); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREVIOS);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_REC_ESTUD_PREV_SNIESE);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_FECHA_CREACION);
			sbSql.append(" ,ubcp.");sbSql.append(JdbcConstantes.UBC_ID);sbSql.append(" AS paisid ");
			sbSql.append(" ,ubcp.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" AS paisdesc ");
			sbSql.append(" ,ubcc.");sbSql.append(JdbcConstantes.UBC_ID);sbSql.append(" AS cantonid ");
			sbSql.append(" ,ubcc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" AS cantondesc ");
			sbSql.append(" ,etn.");sbSql.append(JdbcConstantes.ETN_ID);
			sbSql.append(" ,etn.");sbSql.append(JdbcConstantes.ETN_DESCRIPCION);
			sbSql.append(" , CASE WHEN inac.");sbSql.append(JdbcConstantes.INAC_ID); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE inac.");sbSql.append(JdbcConstantes.INAC_ID); 
						sbSql.append(" END AS universidadid ");
			sbSql.append(" , CASE WHEN inac.");sbSql.append(JdbcConstantes.INAC_DESCRIPCION); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE inac.");sbSql.append(JdbcConstantes.INAC_DESCRIPCION); 
						sbSql.append(" END AS universidaddesc ");
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
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_MCTT_ESTADISTICO);
			sbSql.append(" ,prtr.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" ,prtr.");sbSql.append(JdbcConstantes.PRTR_TIPO_PROCESO);
			sbSql.append(" ,prtr.");sbSql.append(JdbcConstantes.PRTR_FECHA_EJECUCION);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ID);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ESTADO);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
//			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , CASE WHEN ttl.");sbSql.append(JdbcConstantes.TTL_ID); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
					sbSql.append(" ELSE ttl.");sbSql.append(JdbcConstantes.TTL_ID); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TTL_ID);
			sbSql.append(" , CASE WHEN ttl.");sbSql.append(JdbcConstantes.TTL_DESCRIPCION); sbSql.append(" IS NULL THEN ");sbSql.append("''");
					sbSql.append(" ELSE ttl.");sbSql.append(JdbcConstantes.TTL_DESCRIPCION); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TTL_DESCRIPCION);
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" RIGHT JOIN ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ON ");
						sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" RIGHT JOIN ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ON ");
						sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr ON ");
						sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = ");sbSql.append(" prtr.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" RIGHT JOIN ");sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ON ");
						sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);sbSql.append(" = ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ON ");
						sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_INSTITUCION_ACADEMICA);sbSql.append(" inac ON ");
						sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_INAC_ID_INST_EST_PREVIOS);sbSql.append(" = ");sbSql.append(" inac.");sbSql.append(JdbcConstantes.INAC_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ON ");
						sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append(" = ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" RIGHT JOIN ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubcc ON ");
						sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_UBC_CANTON_RESIDENCIA);sbSql.append(" = ");sbSql.append(" ubcc.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" RIGHT JOIN ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubcp ON ");
						sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_UBC_ID);sbSql.append(" = ");sbSql.append(" ubcp.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" RIGHT JOIN ");sbSql.append(JdbcConstantes.TABLA_ETNIA);sbSql.append(" etn ON ");
						sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ETN_ID);sbSql.append(" = ");sbSql.append(" etn.");sbSql.append(JdbcConstantes.ETN_ID);
			sbSql.append(" RIGHT JOIN ");sbSql.append(JdbcConstantes.TABLA_TITULO);sbSql.append(" ttl ON ");
						sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_TITULO_BACHILLER);sbSql.append(" = ");sbSql.append(" ttl.");sbSql.append(JdbcConstantes.TTL_ID);
						
						sbSql.append(" WHERE ");
			//identificacion
			sbSql.append(" UPPER(prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(") like ? ");
			sbSql.append(" AND prtr.");sbSql.append(JdbcConstantes.PRTR_TIPO_PROCESO);sbSql.append(" = ");
			sbSql.append(ProcesoTramiteConstantes.TIPO_PROCESO_POSTULACION_VALUE);

			
			//FIN QUERY
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, "%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase())+"%"); //cargo la identificacion
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<EstudianteValidacionJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
			
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new EstudiantePostuladoJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteValidacionJdbcDto.sql.exception")));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new EstudiantePostuladoJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.identificacion.facultad.carrera.convocatoria.exception")));
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
			throw new EstudiantePostuladoJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.identificacion.facultad.carrera.convocatoria.no.result.exception")));
		}
		return retorno;
	}
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */
	private EstudianteValidacionJdbcDto transformarResultSetADto(ResultSet rs) throws SQLException{
		int valAux = 0;
		java.sql.Date fecha = null;
		EstudianteValidacionJdbcDto retorno = new EstudianteValidacionJdbcDto();
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		
		valAux = rs.getInt(JdbcConstantes.FCES_REC_ESTUD_PREVIOS);
		if(valAux != GeneralesConstantes.APP_ID_BASE.intValue()){
			retorno.setFcesRecEstuPrevios(valAux);
			retorno.setFcesRecEstuPreviosSt(FichaEstudianteConstantes.valueToLabelTipoRecUce(valAux));
		}else{
			retorno.setFcesRecEstuPreviosSt("");
		}
		
		retorno.setFcesRecEstuPreviosSniese(rs.getString(JdbcConstantes.FCES_REC_ESTUD_PREV_SNIESE));
		retorno.setFcesCrrEstudPrevios(rs.getString(JdbcConstantes.FCES_CRR_ESTUD_PREVIOS));
		
		valAux = rs.getInt(JdbcConstantes.FCES_TIPO_COLEGIO);
		if(valAux != GeneralesConstantes.APP_ID_BASE.intValue()){
			retorno.setFcesTipoColegio(valAux);
			retorno.setFcesTipoColegioSt(InstitucionAcademicaConstantes.valueToLabelTipoColegioUce(valAux));
		}
		
		retorno.setFcesTipoColegioSniese(rs.getString(JdbcConstantes.FCES_TIPO_COLEGIO_SNIESE));
//		retorno.setFcesTituloBachiller(rs.getString(JdbcConstantes.FCES_TITULO_BACHILLER));
		retorno.setFcesFechaCreacion(rs.getTimestamp(JdbcConstantes.FCES_FECHA_CREACION));
		
		retorno.setUbcPaisId(rs.getInt("paisid"));
		retorno.setUbcPaisDescripcion(rs.getString("paisdesc"));
		retorno.setUbcCantonId(rs.getInt("cantonid"));
		retorno.setUbcCantonDescripcion(rs.getString("cantondesc"));
		
		
		
		retorno.setEtnId(rs.getInt(JdbcConstantes.ETN_ID));
		retorno.setEtnDescripcion(rs.getString(JdbcConstantes.ETN_DESCRIPCION));
		
		valAux = rs.getInt("universidadid");
		if(valAux != GeneralesConstantes.APP_ID_BASE.intValue()){
			retorno.setInacUniversidadId(rs.getInt("universidadid"));
			retorno.setInacUniversidadDescripcion(rs.getString("universidaddesc"));
		}

		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsTipoIdentificacion(rs.getInt(JdbcConstantes.PRS_TIPO_IDENTIFICACION));
		retorno.setPrsTipoIdentificacionSt(PersonaConstantes.valueToLabelTipoIdentificacionUce(retorno.getPrsTipoIdentificacion()));
		retorno.setPrsTipoIdentificacionSniese(rs.getInt(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsSexo(rs.getInt(JdbcConstantes.PRS_SEXO));
		retorno.setPrsSexoSt(PersonaConstantes.valueToLabelTipoSexoUce(retorno.getPrsSexo()));
		retorno.setPrsSexoSniese(rs.getInt(JdbcConstantes.PRS_SEXO_SNIESE));
		retorno.setPrsMailPersonal(rs.getString(JdbcConstantes.PRS_MAIL_PERSONAL));
		retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
		retorno.setPrsTelefono(rs.getString(JdbcConstantes.PRS_TELEFONO));
		fecha = rs.getDate(JdbcConstantes.PRS_FECHA_NACIMIENTO);
		if(fecha != null ){
			retorno.setPrsFechaNacimiento(new Date(fecha.getTime()));
		}
		
		valAux = rs.getInt(JdbcConstantes.TRTT_ID);
		if(valAux != GeneralesConstantes.APP_ID_BASE.intValue()){
			retorno.setTrttId(valAux);
			retorno.setTrttNumTramite(rs.getString(JdbcConstantes.TRTT_NUM_TRAMITE));
		}
		
		valAux = rs.getInt(JdbcConstantes.TRTT_MCTT_ESTADISTICO);
		if(valAux == TramiteTituloConstantes.TIPO_MODALIDAD_ESTA_COMPLEXIVO_VALUE){
			retorno.setTrttMcttEstadisticoSt(TramiteTituloConstantes.TIPO_MODALIDAD_ESTA_COMPLEXIVO_LABEL);
		}
		if(valAux == TramiteTituloConstantes.TIPO_MODALIDAD_ESTA_OTRAS_MODALIDADES_VALUE){
			retorno.setTrttMcttEstadisticoSt(TramiteTituloConstantes.TIPO_MODALIDAD_ESTA_OTRAS_MODALIDADES_LABEL);
		}
		
		retorno.setFclId(rs.getInt(JdbcConstantes.FCL_ID));
		retorno.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
//		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setTtlDescripcion(rs.getString(JdbcConstantes.TTL_DESCRIPCION));
		return retorno;
	} 
	
	private EstudianteValidacionJdbcDto transformarResultSetADtoXCarrera(ResultSet rs) throws SQLException{
		int valAux = 0;
		java.sql.Date fecha = null;
		EstudianteValidacionJdbcDto retorno = new EstudianteValidacionJdbcDto();
		retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
		retorno.setFcesRegTTlSenescyt(rs.getString(JdbcConstantes.FCES_REG_TTL_SENESCYT));

		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsTipoIdentificacion(rs.getInt(JdbcConstantes.PRS_TIPO_IDENTIFICACION));
		retorno.setPrsTipoIdentificacionSt(PersonaConstantes.valueToLabelTipoIdentificacionUce(retorno.getPrsTipoIdentificacion()));
		retorno.setPrsTipoIdentificacionSniese(rs.getInt(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsSexo(rs.getInt(JdbcConstantes.PRS_SEXO));
		retorno.setPrsSexoSt(PersonaConstantes.valueToLabelTipoSexoUce(retorno.getPrsSexo()));
		retorno.setPrsSexoSniese(rs.getInt(JdbcConstantes.PRS_SEXO_SNIESE));
		retorno.setPrsMailPersonal(rs.getString(JdbcConstantes.PRS_MAIL_PERSONAL));
		retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
		retorno.setPrsTelefono(rs.getString(JdbcConstantes.PRS_TELEFONO));
		fecha = rs.getDate(JdbcConstantes.PRS_FECHA_NACIMIENTO);
		if(fecha != null ){
			retorno.setPrsFechaNacimiento(new Date(fecha.getTime()));
		}
		
		valAux = rs.getInt(JdbcConstantes.TRTT_ID);
		if(valAux != GeneralesConstantes.APP_ID_BASE.intValue()){
			retorno.setTrttId(valAux);
			retorno.setTrttNumTramite(rs.getString(JdbcConstantes.TRTT_NUM_TRAMITE));
		}
		retorno.setPrtrTtlRegSenescyt(rs.getString(JdbcConstantes.PRTR_REG_TTL_SENECYT));
		retorno.setPrtrId(rs.getInt(JdbcConstantes.PRTR_ID));
		retorno.setFclId(rs.getInt(JdbcConstantes.FCL_ID));
		retorno.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
		retorno.setTrttCarreraId(rs.getInt(JdbcConstantes.TRTT_CARRERA_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		
//		retorno.setTrttObsValSec(rs.getString(JdbcConstantes.TRTT_OBS_VAL_SEC_ABG));
		retorno.setCnvId(rs.getInt(JdbcConstantes.CNV_ID));
		retorno.setCnvDescripcion(rs.getString(JdbcConstantes.CNV_DESCRIPCION));
		return retorno;
	}

	/**
	 * Método que busca las carreras de acuerdo a la secretaria
	 * @param identificacion -identificacion de la secretaria
	 * @return Lista de carreras
	 * @throws SQLException 
	 */
	public List<CarreraDto> buscarCarrerasXSecretaria(String cedulaSecretaria) throws CarreraDtoJdbcNoEncontradoException, SQLException{
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
		sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" = roflcr.");	sbSql.append(JdbcConstantes.USRO_ID);
		sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
		sbSql.append("  AND fcl.");sbSql.append(JdbcConstantes.FCL_ID);	sbSql.append(" = crr.");sbSql.append(JdbcConstantes.FCL_ID);
		sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.USR_IDENTIFICACION);sbSql.append(" = ? ");
		sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ");sbSql.append(RolFlujoCarreraConstantes.ROL_FLUJO_CARRERA_ESTADO_ACTIVO_VALUE);
		
		sbSql.append(" ORDER BY crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, cedulaSecretaria);
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSet(rs));
			}
			
			return retorno;
		} catch (SQLException e) {
			if( rs != null){
				rs.close();
			}
			if (pstmt != null){
				pstmt.close();
			}
			if (con != null) {
				con.close();
			}
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Carrera.buscar.todos.no.result.exception")));
			
		}finally{
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
	 
	
	private CarreraDto transformarResultSet(ResultSet rs) throws SQLException{
		CarreraDto retorno = new CarreraDto();
		retorno.setCrrId(rs.getInt("id"));
		retorno.setCrrDescripcion(rs.getString("carrera"));
		retorno.setCrrFacultad(rs.getInt("facultad"));
		retorno.setCrrDetalle(rs.getString("detalle"));
		return retorno;
	}
	
	
	

	/**
	 * Método que busca la lista de estudiantes validados por identificacion y convocatoria de acuerdo a su carrera
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudianteValidacionJdbcDtoException 
	 * @throws EstudianteValidacionJdbcDtoNoEncontradoException 
	 */
	
	public List<EstudianteValidacionJdbcDto> buscarEstudiantesValidadosXIndetificacionXCarreraXConvocatoria(
			String identificacion, String idSecretaria, CarreraDto carreraDto,
			int convocatoria) throws EstudiantePostuladoJdbcDtoException,
			EstudiantePostuladoJdbcDtoNoEncontradoException {
		
		List<EstudianteValidacionJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" AS cedula");
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" apellido1");
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
			sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
			sbSql.append(" END AS ");sbSql.append("apellido2");
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(" AS nombres");
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO); sbSql.append(" IS NOT NULL THEN ");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
			sbSql.append(" ELSE NULL");	sbSql.append(" END AS ");sbSql.append(" egresamiento ");
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);sbSql.append(" AS ");sbSql.append(JdbcConstantes.TABLA_CARRERA);
			sbSql.append(" , fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);sbSql.append(" AS ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_OBSERVACION);sbSql.append(" AS ");sbSql.append("idoneidad");
			sbSql.append(" ,vld.vld_culmino_malla AS malla ");
			sbSql.append(" ,vld.vld_reprobo_complexivo AS complexivo ");
			sbSql.append(" ,vld.vld_asignado_tutor AS tutor ");
			sbSql.append(" ,vld.vld_ultimo_semestre AS semestre ");
			sbSql.append(" ,trtt.trtt_mctt_estadistico as mecanismo ");
//			sbSql.append(" ,vld.");sbSql.append(JdbcConstantes.VLD_RSL_ACT_CONOCIMIENTO);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces, ");
			sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt, ");
			sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr, ");
//			sbSql.append(JdbcConstantes.TABLA_VALIDACION);sbSql.append(" vld, ");
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
			sbSql.append(" AND prtr.");sbSql.append(JdbcConstantes.PRTR_TIPO_PROCESO);sbSql.append(" = 1");
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" = 1 ");sbSql.append(" AND ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			
			if(convocatoria != GeneralesConstantes.APP_ID_BASE){
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ?");
			}else{
				sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" > ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			}
			
			// CONDICIONES DE VALIDACION
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = ");;sbSql.append(" prtr.");;sbSql.append(JdbcConstantes.TRTT_ID);
//			sbSql.append(" AND prtr. ");sbSql.append(JdbcConstantes.PRTR_ID);sbSql.append(" = ");;sbSql.append(" vld.");;sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" AND prtr.prtr_id=vld.prtr_id ");
			sbSql.append(" AND prs.prs_id=fces.prs_id ");
			sbSql.append(" AND fcl.fcl_id=crr.fcl_id ");
			sbSql.append(" AND prtr.prtr_id=vld.prtr_id ");
			
			sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA);sbSql.append(" IN ( ");
			sbSql.append(" SELECT DISTINCT crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);sbSql.append(" from ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr, ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);
			sbSql.append(" usro, ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");sbSql.append(" WHERE ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" = ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");
						sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_IDENTIFICACION);
			sbSql.append(" LIKE ?) "); sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
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
			retorno = new ArrayList<EstudianteValidacionJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoValidadosXCarrera(rs));
			}
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new EstudiantePostuladoJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteValidacionJdbcDto.sql.exception")));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new EstudiantePostuladoJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.identificacion.facultad.carrera.convocatoria.exception")));
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
			throw new EstudiantePostuladoJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.identificacion.facultad.carrera.convocatoria.no.result.exception")));
		}
		return retorno;
	}
	
	private EstudianteValidacionJdbcDto transformarResultSetADtoValidadosXCarreraActualizacion(ResultSet rs) throws SQLException{
		
		EstudianteValidacionJdbcDto retorno = new EstudianteValidacionJdbcDto();
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsMailPersonal(rs.getString(JdbcConstantes.PRS_MAIL_PERSONAL));
		retorno.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		try {
			retorno.setFcesFechaEgresamiento(rs.getDate(JdbcConstantes.FCES_FECHA_EGRESAMIENTO));	
		} catch (Exception e) {
			
		}
		
		retorno.setTrttObsValSec(rs.getString(JdbcConstantes.TRTT_OBSERVACION));
		retorno.setTrttId(rs.getInt(JdbcConstantes.TRTT_ID));
		retorno.setCnvDescripcion(rs.getString(JdbcConstantes.CNV_DESCRIPCION));
		retorno.setTrttCarreraId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setVldId(rs.getInt(JdbcConstantes.VLD_ID));
		return retorno;
	}
	
	private EstudianteValidacionJdbcDto transformarResultSetADtoAptitudXCarreraActualizacion(ResultSet rs) throws SQLException{
		
		EstudianteValidacionJdbcDto retorno = new EstudianteValidacionJdbcDto();
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setFcesFechaEgresamiento(rs.getDate(JdbcConstantes.FCES_FECHA_EGRESAMIENTO));
		retorno.setTrttObsValSec(rs.getString(JdbcConstantes.TRTT_OBSERVACION));
		retorno.setTrttId(rs.getInt(JdbcConstantes.TRTT_ID));
		retorno.setCnvDescripcion(rs.getString(JdbcConstantes.CNV_DESCRIPCION));
		retorno.setTrttCarreraId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		return retorno;
	}
	
	
	private EstudianteValidacionJdbcDto transformarResultSetADtoValidadosXCarrera(ResultSet rs) throws SQLException{
		EstudianteValidacionJdbcDto retorno = new EstudianteValidacionJdbcDto();
		retorno.setPrsIdentificacion(rs.getString("cedula"));
		retorno.setPrsPrimerApellido(rs.getString("apellido1"));
		retorno.setPrsSegundoApellido(rs.getString("apellido2"));
		retorno.setPrsNombres(rs.getString("nombres"));
		retorno.setFclDescripcion(rs.getString("facultad"));
		retorno.setCrrDescripcion(rs.getString("carrera"));
		retorno.setFcesFechaEgresamiento(rs.getDate("egresamiento"));
//		retorno.setVldCulminoMalla(rs.getInt("malla"));
//		retorno.setVldUltimoSemestre(rs.getInt("semestre"));
//		retorno.setVldReproboComplexivo(rs.getInt("complexivo"));
//		retorno.setVldAsignadoTutor(rs.getInt("tutor"));
		retorno.setTrttObsValSec(rs.getString("idoneidad"));
//		retorno.setVldRslActConocimiento(rs.getInt(JdbcConstantes.VLD_RSL_ACT_CONOCIMIENTO));
		retorno.setCnvDescripcion(rs.getString(JdbcConstantes.CNV_DESCRIPCION));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		if(rs.getString("mecanismo").equals("1")){
			retorno.setTrttMcttEstadisticoSt("Otros Mecanismo");
		}else{
			retorno.setTrttMcttEstadisticoSt("Examen Complexivo");
		}
		return retorno;
	}
	
	/**
	 * Método que busca la lista de estudiantes validados por identificacion y convocatoria de acuerdo a su carrera y que deben actualizar conocimientos
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudianteValidacionJdbcDtoException 
	 * @throws EstudianteValidacionJdbcDtoNoEncontradoException 
	 */
	
	public List<EstudianteValidacionJdbcDto> buscarEstudiantesValidadosXIndetificacionXCarreraXConvocatoriaActualizar(
			String identificacion, String idSecretaria) throws EstudiantePostuladoJdbcDtoException,
			EstudiantePostuladoJdbcDtoNoEncontradoException {
		
		List<EstudianteValidacionJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
			sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO); 
			sbSql.append(" IS NOT NULL THEN ");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
			sbSql.append(" ELSE NULL ");	sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_OBSERVACION);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" ,vld.");sbSql.append(JdbcConstantes.VLD_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);
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
			sbSql.append(" crr.crr_id IN ");
			sbSql.append("(SELECT DISTINCT crr.crr_id from carrera crr, rol_flujo_carrera roflcr, usuario_rol usro, usuario usr ");  
			sbSql.append(" WHERE usr.usr_id =  usro.usr_id  AND  usro.usro_id =  roflcr.usro_id "); 
			sbSql.append(" AND  crr.crr_id =  roflcr.crr_id AND  usr.usr_identificacion LIKE '");
			sbSql.append(idSecretaria);sbSql.append("') AND ");
						
			sbSql.append(" UPPER(prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(") like ? ");
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = ");
			sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = ");
			sbSql.append(" prtr.");;sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND prtr.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" = vld.");sbSql.append(JdbcConstantes.VLD_PRTR_ID);
			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);
			sbSql.append(" AND vld.");sbSql.append(JdbcConstantes.VLD_RSL_ACT_CONOCIMIENTO);
			sbSql.append(" = ");sbSql.append(ValidacionConstantes.SI_DEBE_REALIZAR_ACTUALIZACION_CONOCIMIENTOS_VALUE);
			sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);
			sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ");
			sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
			
//			System.out.println(sbSql);
			//FIN QUERY
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase())); //cargo la identificacion
			
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<EstudianteValidacionJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoValidadosXCarreraActualizacion(rs));
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
			throw new EstudiantePostuladoJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteValidacionJdbcDto.sql.exception")));
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
			throw new EstudiantePostuladoJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.identificacion.facultad.carrera.convocatoria.exception")));
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
	 * Método que busca la lista de estudiantes validados por identificacion y convocatoria de acuerdo a su carrera y que deben actualizar conocimientos
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudianteValidacionJdbcDtoException 
	 * @throws EstudianteValidacionJdbcDtoNoEncontradoException 
	 */
	@Override
	public List<EstudianteValidacionJdbcDto> buscarEstudiantesValidadosXIndetificacionXCarreraXConvocatoria(
			String identificacion, String idSecretaria) throws EstudiantePostuladoJdbcDtoException,
			EstudiantePostuladoJdbcDtoNoEncontradoException {
		
		List<EstudianteValidacionJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
			sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO); sbSql.append(" IS NOT NULL THEN ");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
			sbSql.append(" ELSE NULL");	sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO); sbSql.append(" IS NOT NULL THEN ");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_OBSERVACION);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
//			sbSql.append(" ,vld.");sbSql.append(JdbcConstantes.VLD_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces, ");
			sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt, ");
			sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr, ");
//			sbSql.append(JdbcConstantes.TABLA_VALIDACION);sbSql.append(" vld, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv, ");
			sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" crr.crr_id IN ");
			sbSql.append("(SELECT DISTINCT crr.crr_id from carrera crr, rol_flujo_carrera roflcr, usuario_rol usro, usuario usr ");  
			sbSql.append(" WHERE usr.usr_id =  usro.usr_id  AND  usro.usro_id =  roflcr.usro_id "); 
			sbSql.append(" AND  crr.crr_id =  roflcr.crr_id AND  usr.usr_identificacion LIKE '");
			sbSql.append(idSecretaria);sbSql.append("') AND ");
						
			sbSql.append(" UPPER(prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(") like ? ");
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = ");
			sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = ");
			sbSql.append(" prtr.");;sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND prtr.");sbSql.append(JdbcConstantes.PRTR_ID);
//			sbSql.append(" = vld.");sbSql.append(JdbcConstantes.VLD_PRTR_ID);
			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);
			sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);
			sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);
			sbSql.append(" < ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_APTO_VALUE);
			sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_OBSERVACION);
			sbSql.append(" NOT LIKE '%");sbSql.append(ValidacionConstantes.NO_IDONEO_LABEL);sbSql.append("%'");
			sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ");
			sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
//			System.out.println(sbSql);
			//FIN QUERY
			con = ds.getConnection();

			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, "%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase())+"%"); //cargo la identificacion
			
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<EstudianteValidacionJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoValidadosXCarreraActualizacion(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new EstudiantePostuladoJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteValidacionJdbcDto.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new EstudiantePostuladoJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.identificacion.facultad.carrera.convocatoria.exception")));
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
	public List<EstudianteValidacionJdbcDto> buscarEstudiantesAptitudXIndetificacionXCarreraXConvocatoriaActualizar(
			String identificacion, String idSecretaria) throws EstudiantePostuladoJdbcDtoException,
			EstudiantePostuladoJdbcDtoNoEncontradoException {
		
		List<EstudianteValidacionJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
			sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO); sbSql.append(" IS NOT NULL THEN ");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
			sbSql.append(" ELSE NULL");	sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_OBSERVACION);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
//			sbSql.append(" ,apt.");sbSql.append(JdbcConstantes.APT_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces, ");
			sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt, ");
			sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr, ");
//			sbSql.append(JdbcConstantes.TABLA_APTITUD);sbSql.append(" apt, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv, ");
			sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" crr.crr_id IN ");
			sbSql.append("(SELECT DISTINCT crr.crr_id from carrera crr, rol_flujo_carrera roflcr, usuario_rol usro, usuario usr ");  
			sbSql.append(" WHERE usr.usr_id =  usro.usr_id  AND  usro.usro_id =  roflcr.usro_id "); 
			sbSql.append(" AND  crr.crr_id =  roflcr.crr_id AND  usr.usr_identificacion LIKE '");
			sbSql.append(idSecretaria);sbSql.append("') AND ");
						
			sbSql.append(" UPPER(prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(") like ? ");
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = ");
			sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = ");
			sbSql.append(" prtr.");;sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND prtr.");sbSql.append(JdbcConstantes.PRTR_ID);
//			sbSql.append(" = apt.");sbSql.append(JdbcConstantes.APT_PRTR_ID);
			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);
//			sbSql.append(" AND apt.");sbSql.append(JdbcConstantes.APT_APROBO_ACTUALIZAR);
//			sbSql.append(" = ");sbSql.append(AptitudConstantes.SI_APROBO_ACTUALIZAR_VALUE);
			sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);
			sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ")
			;sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
			
			//FIN QUERY
			con = ds.getConnection();

			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase())); //cargo la identificacion
			
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<EstudianteValidacionJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoAptitudXCarreraActualizacion(rs));
			}
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new EstudiantePostuladoJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteValidacionJdbcDto.sql.exception")));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new EstudiantePostuladoJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.identificacion.facultad.carrera.convocatoria.exception")));
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
	
	public EstudianteValidacionJdbcDto buscarValidacionXEstudiante(EstudiantePostuladoJdbcDto item) throws EstudiantePostuladoJdbcDtoException, EstudiantePostuladoJdbcDtoNoEncontradoException {
		EstudianteValidacionJdbcDto retorno=null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" CASE WHEN vld.");sbSql.append(JdbcConstantes.VLD_RSL_IDONEIDAD); sbSql.append(" IS NOT NULL THEN ");sbSql.append(JdbcConstantes.VLD_RSL_IDONEIDAD);
			sbSql.append(" ELSE ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
			sbSql.append(" END ");sbSql.append(JdbcConstantes.VLD_RSL_IDONEIDAD);
			sbSql.append(" , CASE WHEN vld.");sbSql.append(JdbcConstantes.VLD_RSL_PRORROGA); sbSql.append(" IS NOT NULL THEN ");sbSql.append(JdbcConstantes.VLD_RSL_PRORROGA);
			sbSql.append(" ELSE ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
			sbSql.append(" END  ");sbSql.append(JdbcConstantes.VLD_RSL_PRORROGA);
			sbSql.append(" , CASE WHEN vld.");sbSql.append(JdbcConstantes.VLD_SEGUNDA_PRORROGA); sbSql.append(" IS NOT NULL THEN ");sbSql.append(JdbcConstantes.VLD_SEGUNDA_PRORROGA);
			sbSql.append(" ELSE ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
			sbSql.append(" END ");sbSql.append(JdbcConstantes.VLD_SEGUNDA_PRORROGA);
			sbSql.append(" , CASE WHEN vld.");sbSql.append(JdbcConstantes.VLD_RSL_HOMOLOGACION); sbSql.append(" IS NOT NULL THEN ");sbSql.append(JdbcConstantes.VLD_RSL_HOMOLOGACION);
			sbSql.append(" ELSE ");	sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
			sbSql.append(" END ");sbSql.append(JdbcConstantes.VLD_RSL_HOMOLOGACION);
			sbSql.append(" , CASE WHEN vld.");sbSql.append(JdbcConstantes.VLD_RSL_ACT_CONOCIMIENTO); sbSql.append(" IS NOT NULL THEN ");sbSql.append(JdbcConstantes.VLD_RSL_ACT_CONOCIMIENTO);
			sbSql.append(" ELSE ");	sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
			sbSql.append(" END ");sbSql.append(JdbcConstantes.VLD_RSL_ACT_CONOCIMIENTO);
			sbSql.append(" , CASE WHEN vld.");sbSql.append(JdbcConstantes.VLD_RSL_HOMOLOGACION_APROB); sbSql.append(" IS NOT NULL THEN ");sbSql.append(JdbcConstantes.VLD_RSL_HOMOLOGACION_APROB);
			sbSql.append(" ELSE ");	sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
			sbSql.append(" END ");sbSql.append(JdbcConstantes.VLD_RSL_HOMOLOGACION_APROB);
			sbSql.append(" , CASE WHEN vld.");sbSql.append(JdbcConstantes.VLD_RSL_ACTUALIZ_APROB); sbSql.append(" IS NOT NULL THEN ");sbSql.append(JdbcConstantes.VLD_RSL_ACTUALIZ_APROB);
			sbSql.append(" ELSE ");	sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
			sbSql.append(" END ");sbSql.append(JdbcConstantes.VLD_RSL_ACTUALIZ_APROB);
			sbSql.append(" , CASE WHEN vld.");sbSql.append(JdbcConstantes.VLD_RSL_CURSO_ACT_APROB); sbSql.append(" IS NOT NULL THEN ");sbSql.append(JdbcConstantes.VLD_RSL_CURSO_ACT_APROB);
			sbSql.append(" ELSE ");	sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
			sbSql.append(" END ");sbSql.append(JdbcConstantes.VLD_RSL_CURSO_ACT_APROB);
			sbSql.append(" , CASE WHEN vld.");sbSql.append(JdbcConstantes.VLD_APROBACION_INGLES); sbSql.append(" IS NOT NULL THEN ");sbSql.append(JdbcConstantes.VLD_APROBACION_INGLES);
			sbSql.append(" ELSE ");	sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
			sbSql.append(" END ");sbSql.append(JdbcConstantes.VLD_APROBACION_INGLES);
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_FECHA_INICIO_COHORTE); sbSql.append(" IS NOT NULL THEN ");sbSql.append(JdbcConstantes.FCES_FECHA_INICIO_COHORTE);
			sbSql.append(" ELSE NULL");	sbSql.append(" END  ");sbSql.append(JdbcConstantes.FCES_FECHA_INICIO_COHORTE);
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_FECHA_FIN_COHORTE); sbSql.append(" IS NOT NULL THEN ");sbSql.append(JdbcConstantes.FCES_FECHA_FIN_COHORTE);
			sbSql.append(" ELSE NULL");	sbSql.append(" END  ");sbSql.append(JdbcConstantes.FCES_FECHA_FIN_COHORTE);
			sbSql.append(" FROM "); sbSql.append(JdbcConstantes.TABLA_VALIDACION);sbSql.append(" vld, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA); sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA); sbSql.append(" roflcr, ");
			sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE); sbSql.append(" prtr, ");
			sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO); sbSql.append(" trtt, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE); sbSql.append(" fces ");
			sbSql.append(" WHERE ");
			sbSql.append(" vld.");sbSql.append(JdbcConstantes.VLD_PRTR_ID);sbSql.append(" = prtr.");sbSql.append(JdbcConstantes.PRTR_ID); 
			sbSql.append(" AND ");sbSql.append(" prtr.");sbSql.append(JdbcConstantes.PRTR_ROFLCR_ID);sbSql.append(" = roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ID); 
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
			sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = prtr.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = ?");
//			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ?");
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, item.getTrttId()); //cargo el id de tramite_titulo
			retorno = new EstudianteValidacionJdbcDto();
//			pstmt.setInt(2, item.getTrttCarrera());
			rs = pstmt.executeQuery();
			while(rs.next()){
					retorno.setVldRslAprobacionIngles(rs.getInt(JdbcConstantes.VLD_APROBACION_INGLES));
					retorno.setVldRslCursoActAprob(rs.getInt(JdbcConstantes.VLD_RSL_CURSO_ACT_APROB));
					retorno.setVldRslActualizAprob(rs.getInt(JdbcConstantes.VLD_RSL_ACTUALIZ_APROB));
					retorno.setVldRslHomologacionAprob(rs.getInt(JdbcConstantes.VLD_RSL_HOMOLOGACION_APROB));
					retorno.setVldRslHomologacionAprob(rs.getInt(JdbcConstantes.VLD_RSL_ACT_CONOCIMIENTO));
					retorno.setVldRslHomologacionAprob(rs.getInt(JdbcConstantes.VLD_RSL_HOMOLOGACION));
					retorno.setVldRslHomologacionAprob(rs.getInt(JdbcConstantes.VLD_SEGUNDA_PRORROGA));
					retorno.setVldRslHomologacionAprob(rs.getInt(JdbcConstantes.VLD_RSL_PRORROGA));
					retorno.setVldRslHomologacionAprob(rs.getInt(JdbcConstantes.VLD_RSL_IDONEIDAD));
					
				if(rs.getDate(JdbcConstantes.FCES_FECHA_INICIO_COHORTE)==null){
					retorno.setFcesFechaEgresamiento(null);
				}else{
					retorno.setFcesFechaEgresamiento(rs.getDate(JdbcConstantes.FCES_FECHA_INICIO_COHORTE));
				}
				if(rs.getDate(JdbcConstantes.FCES_FECHA_FIN_COHORTE)==null){
					retorno.setFcesFechaEgresamiento(null);
				}else{
					retorno.setFcesFechaEgresamiento(rs.getDate(JdbcConstantes.FCES_FECHA_FIN_COHORTE));
				}
			}
		}catch( SQLException  e){
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new EstudiantePostuladoJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteValidacionJdbcDto.sql.exception")));
		}finally {
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

	
	public List<EstudianteValidacionJdbcDto> buscarValidacionXEstudianteActualizar(String cedula) throws EstudiantePostuladoJdbcDtoException, EstudiantePostuladoJdbcDtoNoEncontradoException {
		List<EstudianteValidacionJdbcDto> retorno=null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" ,");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" ,");
			sbSql.append(" CASE WHEN");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(" IS NOT NULL THEN ");
			sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(" ELSE ' '");
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);sbSql.append(" ,");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" ,");
			sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);sbSql.append(" ,");
//			sbSql.append(" CASE WHEN vld.");sbSql.append(JdbcConstantes.VLD_CULMINO_MALLA); sbSql.append(" IS NOT NULL THEN ");sbSql.append(JdbcConstantes.VLD_CULMINO_MALLA);
//			sbSql.append(" ELSE ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
//			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.VLD_CULMINO_MALLA);
//			sbSql.append(" , CASE WHEN vld.");sbSql.append(JdbcConstantes.VLD_REPROBO_COMPLEXIVO); sbSql.append(" IS NOT NULL THEN ");sbSql.append(JdbcConstantes.VLD_REPROBO_COMPLEXIVO);
//			sbSql.append(" ELSE ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
//			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.VLD_REPROBO_COMPLEXIVO);
//			sbSql.append(" , CASE WHEN vld.");sbSql.append(JdbcConstantes.VLD_ASIGNADO_TUTOR); sbSql.append(" IS NOT NULL THEN ");sbSql.append(JdbcConstantes.VLD_ASIGNADO_TUTOR);
//			sbSql.append(" ELSE ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());sbSql.append(" END AS ");sbSql.append(JdbcConstantes.VLD_ASIGNADO_TUTOR);
//			sbSql.append(" , CASE WHEN vld.");sbSql.append(JdbcConstantes.VLD_ULTIMO_SEMESTRE); sbSql.append(" IS NOT NULL THEN ");sbSql.append(JdbcConstantes.VLD_ULTIMO_SEMESTRE);
//			sbSql.append(" ELSE ");	sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());sbSql.append(" END AS ");sbSql.append(JdbcConstantes.VLD_ULTIMO_SEMESTRE);
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO); sbSql.append(" IS NOT NULL THEN ");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
			sbSql.append(" ELSE NULL");	sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
//			sbSql.append(" , vld.");sbSql.append(JdbcConstantes.VLD_RSL_ACT_CONOCIMIENTO);
//			sbSql.append(" ,vld.");	sbSql.append(JdbcConstantes.VLD_ID);
			sbSql.append(" ,trtt.");	sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" ,crr.");	sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,prtr.");	sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" ,trtt.");	sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" ,cnv.");	sbSql.append(JdbcConstantes.CNV_DESCRIPCION);
			sbSql.append(" ,fcl.");	sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
//			sbSql.append(" FROM "); sbSql.append(JdbcConstantes.TABLA_VALIDACION);sbSql.append(" vld, ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA); sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE); sbSql.append(" prtr, ");
			sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO); sbSql.append(" trtt, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE); sbSql.append(" fces, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA); sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_FACULTAD); sbSql.append(" fcl, ");
			sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA); sbSql.append(" cnv ");
			sbSql.append(" WHERE ");
//			sbSql.append(" vld.");sbSql.append(JdbcConstantes.VLD_PRTR_ID);sbSql.append(" = prtr.");sbSql.append(JdbcConstantes.PRTR_ID); 
			sbSql.append(" AND ");sbSql.append(" prtr.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_ID); 
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);sbSql.append(" = prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
//			sbSql.append(" AND ");sbSql.append(" prtr.");sbSql.append(JdbcConstantes.PRTR_ID);sbSql.append(" = vld.");sbSql.append(JdbcConstantes.VLD_PRTR_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append(" = fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
//			sbSql.append(" AND (");sbSql.append(" vld.");sbSql.append(JdbcConstantes.VLD_RSL_ACT_CONOCIMIENTO);sbSql.append(" = ");sbSql.append(ValidacionConstantes.SI_DEBE_REALIZAR_ACTUALIZACION_CONOCIMIENTOS_VALUE);
//			sbSql.append(" OR ");sbSql.append(" vld.");sbSql.append(JdbcConstantes.VLD_RSL_ACT_CONOCIMIENTO);sbSql.append(" = ");sbSql.append(ValidacionConstantes.ACTUALIZACION_CONOCIMIENTOS_PAGAR_VALUE);
			sbSql.append(" ) ");
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ?");
			con = ds.getConnection();
			
//			System.out.println(sbSql);
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, cedula); //cargo la cédula
			retorno = new ArrayList<EstudianteValidacionJdbcDto>();
			rs = pstmt.executeQuery();
			while(rs.next()){
				EstudianteValidacionJdbcDto aux = new EstudianteValidacionJdbcDto();
				aux.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
				aux.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
				aux.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
				aux.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
				aux.setPrsMailPersonal(rs.getString(JdbcConstantes.PRS_MAIL_PERSONAL));
				aux.setPrsTelefono(rs.getString(JdbcConstantes.PRS_TELEFONO));
				aux.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
				aux.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
				aux.setTrttCarreraId(rs.getInt(JdbcConstantes.TRTT_CARRERA_ID));
				aux.setCrrId(rs.getInt(JdbcConstantes.TRTT_CARRERA_ID));
				aux.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
				aux.setCnvDescripcion(rs.getString(JdbcConstantes.CNV_DESCRIPCION));
				aux.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
//				if(rs.getInt(JdbcConstantes.VLD_CULMINO_MALLA)==GeneralesConstantes.APP_ID_BASE.intValue()){
//					aux.setVldCulminoMalla(GeneralesConstantes.APP_ID_BASE.intValue());
//				}else{
////					aux.setVldCulminoMalla(rs.getInt(JdbcConstantes.VLD_CULMINO_MALLA));
//				}
////				if(rs.getInt(JdbcConstantes.VLD_REPROBO_COMPLEXIVO)==GeneralesConstantes.APP_ID_BASE.intValue()){
//					aux.setVldReproboComplexivo(GeneralesConstantes.APP_ID_BASE.intValue());
//				}else{
//					aux.setVldReproboComplexivo(rs.getInt(JdbcConstantes.VLD_REPROBO_COMPLEXIVO));
//				}
//				if(rs.getInt(JdbcConstantes.VLD_ASIGNADO_TUTOR)==GeneralesConstantes.APP_ID_BASE.intValue()){
//					aux.setVldAsignadoTutor(GeneralesConstantes.APP_ID_BASE.intValue());
//				}else{
//					aux.setVldAsignadoTutor(rs.getInt(JdbcConstantes.VLD_ASIGNADO_TUTOR));
//				}
//				if(rs.getInt(JdbcConstantes.VLD_ULTIMO_SEMESTRE)==GeneralesConstantes.APP_ID_BASE.intValue()){
//					aux.setVldUltimoSemestre(GeneralesConstantes.APP_ID_BASE.intValue());
//				}else{
//					aux.setVldUltimoSemestre(rs.getInt(JdbcConstantes.VLD_ULTIMO_SEMESTRE));
//				}
//				if(rs.getInt(JdbcConstantes.VLD_RSL_ACT_CONOCIMIENTO)==GeneralesConstantes.APP_ID_BASE.intValue()){
//					aux.setVldUltimoSemestre(GeneralesConstantes.APP_ID_BASE.intValue());
//				}else{
//					aux.setVldRslActConocimiento(rs.getInt(JdbcConstantes.VLD_RSL_ACT_CONOCIMIENTO));
//				}
//				aux.setVldId(rs.getInt(JdbcConstantes.VLD_ID));
//				aux.setPrtrId(rs.getInt(JdbcConstantes.PRTR_ID));
//				aux.setTrttId(rs.getInt(JdbcConstantes.TRTT_ID));
//				retorno.add(aux);
			}
		}catch( SQLException  e){
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new EstudiantePostuladoJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteValidacionJdbcDto.sql.exception")));
		}finally {
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
		
		Integer validador;
		TramiteTitulo trttAux = em.find(TramiteTitulo.class, item.getTrttId());
		if(item.getAptRequisitos()==AptitudConstantes.SI_CUMPLE_REQUISITOS_VALUE &&(item.getAptAproboActualizar()==null || item.getAptAproboActualizar()==AptitudConstantes.SI_APROBO_ACTUALIZAR_VALUE)){
			trttAux.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_APTO_VALUE);
//			TramiteTituloConstantes.ESTADO_PROCESO_APTO_VALUE
			nuevoPrtr.setPrtrTipoProceso(ProcesoTramiteConstantes.TIPO_PROCESO_APTO_VALUE);
			nuevoPrtr.setPrtrFechaEjecucion(item.getPrtrFechaEjecucion());
			nuevoPrtr.setPrtrRolFlujoCarrera(roflcrr);
			nuevoPrtr.setPrtrTramiteTitulo(trttAux);
			em.persist(nuevoPrtr);
			validador=ProcesoTramiteConstantes.TIPO_PROCESO_APTO_VALUE;
		}else{
			trttAux.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_NO_APTO_VALUE);
//			TramiteTituloConstantes.ESTADO_PROCESO_NO_APTO_VALUE
			nuevoPrtr.setPrtrTipoProceso(ProcesoTramiteConstantes.TIPO_PROCESO_NO_APTO_VALUE);
			nuevoPrtr.setPrtrFechaEjecucion(item.getPrtrFechaEjecucion());
			nuevoPrtr.setPrtrRolFlujoCarrera(roflcrr);
			nuevoPrtr.setPrtrTramiteTitulo(trttAux);
			em.persist(nuevoPrtr);
			validador=ProcesoTramiteConstantes.TIPO_PROCESO_NO_APTO_VALUE;
		}
		
		//Creamos el registro de la tabla aptitud
		
		Aptitud nuevoAptitud= new Aptitud();
		nuevoAptitud.setAptAproboActualizar(item.getAptAproboActualizar());
		nuevoAptitud.setAptRequisitos(item.getAptRequisitos());
		nuevoAptitud.setAptAproboTutor(item.getAptAproboTutor());
		nuevoAptitud.setAptFinMalla(item.getAptFinMalla());
		nuevoAptitud.setAptNotaDirector(item.getAptNotaDirector());
		nuevoAptitud.setAptSuficienciaIngles(item.getAptSuficienciaIngles());
		nuevoAptitud.setAptProcesoTramite(nuevoPrtr);
		em.persist(nuevoAptitud);
		return validador;
	}
	
	/**
	 * Método que guarda la edición de aptitud del postulante por parte de la DGA
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EvaluadosDtoJdbcException 
	 * @throws EvaluadosDtoJdbcNoEncontradoException 
	 */
//	@Override
//	public Integer guardarEdicionAptitud(
//			EstudianteAptitudJdbcDto item, String validadorIdentificacion) throws EvaluadosDtoJdbcException,
//			EvaluadosDtoJdbcNoEncontradoException {
//		//**********************************************************************
//		//********** actualizar el tramite actual  *****************************
//		//**********************************************************************
//		ProcesoTramite nuevoPrtr = em.find(ProcesoTramite.class, item.getPrtrId());
//		
//		Integer validador;
//		TramiteTitulo trttAux = em.find(TramiteTitulo.class, item.getTrttId());
//		if(item.getAptRequisitos()==AptitudConstantes.SI_CUMPLE_REQUISITOS_VALUE &&(item.getAptAproboActualizacion()==GeneralesConstantes.APP_ID_BASE || item.getAptAproboActualizacion()==AptitudConstantes.SI_APROBO_ACTUALIZAR_VALUE)){
//			trttAux.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_APTO_EVALUADO_VALUE);
//			trttAux.setTrttObservacionDga(item.getTrttObsDesactivarDga());
////			TramiteTituloConstantes.ESTADO_PROCESO_APTO_VALUE
//			nuevoPrtr.setPrtrTipoProceso(ProcesoTramiteConstantes.TIPO_PROCESO_APTO_EVALUADO_VALUE);
//			nuevoPrtr.setPrtrFechaEjecucion(item.getPrtrFechaEjecucion());
//			validador=ProcesoTramiteConstantes.TIPO_PROCESO_APTO_VALUE;
//		}else{
//			trttAux.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_NO_APTO_EVALUADO_VALUE);
//			trttAux.setTrttObservacionDga(item.getTrttObsDesactivarDga());
////			TramiteTituloConstantes.ESTADO_PROCESO_NO_APTO_VALUE
//			nuevoPrtr.setPrtrTipoProceso(ProcesoTramiteConstantes.TIPO_PROCESO_NO_APTO_EVALUADO_VALUE);
//			nuevoPrtr.setPrtrFechaEjecucion(item.getPrtrFechaEjecucion());
//			validador=ProcesoTramiteConstantes.TIPO_PROCESO_NO_APTO_VALUE;
//		}
//		
//		//Creamos el registro de la tabla aptitud
//		
//		Aptitud nuevoAptitud= em.find(Aptitud.class, item.getAptId());
//		if(item.getAptAproboActualizacion()!=GeneralesConstantes.APP_ID_BASE) {
//			nuevoAptitud.setAptAproboActualizar(item.getAptAproboActualizacion());
//		}else{
//			nuevoAptitud.setAptAproboActualizar(null);
//		}
//		if(nuevoAptitud.getAptRequisitos()!=item.getAptRequisitos()) nuevoAptitud.setAptRequisitos(item.getAptRequisitos());
//		if(nuevoAptitud.getAptReproboCreditos()!=item.getAptReproboCreditos()) nuevoAptitud.setAptReproboCreditos(item.getAptReproboCreditos());
//		if(nuevoAptitud.getAptSegundaCarrera()!=item.getAptSegundaCarrera()) nuevoAptitud.setAptSegundaCarrera(item.getAptSegundaCarrera());
//		nuevoAptitud.setAptProcesoTramite(nuevoPrtr);
//		trttAux.setTrttObservacionDga(item.getTrttObservacionDga());
//		return validador;
//	}
	
	public EstudianteValidacionJdbcDto buscarValidacionXEstudianteIdoneidad(EstudiantePostuladoJdbcDto item) throws EstudiantePostuladoJdbcDtoException, EstudiantePostuladoJdbcDtoNoEncontradoException {
		EstudianteValidacionJdbcDto retorno=null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
//			StringBuilder sbSql = new StringBuilder();
//			sbSql.append(" SELECT ");
//			sbSql.append(" CASE WHEN vld.");sbSql.append(JdbcConstantes.VLD_CULMINO_MALLA); sbSql.append(" IS NOT NULL THEN ");sbSql.append(JdbcConstantes.VLD_CULMINO_MALLA);
//			sbSql.append(" ELSE ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());sbSql.append(" END AS ");sbSql.append(JdbcConstantes.VLD_CULMINO_MALLA);
//			sbSql.append(" , CASE WHEN vld.");sbSql.append(JdbcConstantes.VLD_REPROBO_COMPLEXIVO); sbSql.append(" IS NOT NULL THEN ");sbSql.append(JdbcConstantes.VLD_REPROBO_COMPLEXIVO);
//			sbSql.append(" ELSE ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());sbSql.append(" END AS ");sbSql.append(JdbcConstantes.VLD_REPROBO_COMPLEXIVO);
//			sbSql.append(" , CASE WHEN vld.");sbSql.append(JdbcConstantes.VLD_ASIGNADO_TUTOR); sbSql.append(" IS NOT NULL THEN ");sbSql.append(JdbcConstantes.VLD_ASIGNADO_TUTOR);
//			sbSql.append(" ELSE ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());sbSql.append(" END AS ");sbSql.append(JdbcConstantes.VLD_ASIGNADO_TUTOR);
//			sbSql.append(" , CASE WHEN vld.");sbSql.append(JdbcConstantes.VLD_ULTIMO_SEMESTRE); sbSql.append(" IS NOT NULL THEN ");sbSql.append(JdbcConstantes.VLD_ULTIMO_SEMESTRE);
//			sbSql.append(" ELSE ");	sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());sbSql.append(" END AS ");sbSql.append(JdbcConstantes.VLD_ULTIMO_SEMESTRE);
//			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO); sbSql.append(" IS NOT NULL THEN ");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
//			sbSql.append(" ELSE NULL");	sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
//			sbSql.append(" , vld.");sbSql.append(JdbcConstantes.VLD_RSL_IDONEIDAD);
//			sbSql.append(" FROM "); sbSql.append(JdbcConstantes.TABLA_VALIDACION);sbSql.append(" vld, ");
//			sbSql.append(JdbcConstantes.TABLA_CARRERA); sbSql.append(" crr, ");
//			sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA); sbSql.append(" roflcr, ");
//			sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE); sbSql.append(" prtr, ");
//			sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO); sbSql.append(" trtt, ");
//			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE); sbSql.append(" fces ");
//			sbSql.append(" WHERE ");
//			sbSql.append(" vld.");sbSql.append(JdbcConstantes.VLD_PRTR_ID);sbSql.append(" = prtr.");sbSql.append(JdbcConstantes.PRTR_ID); 
//			sbSql.append(" AND ");sbSql.append(" prtr.");sbSql.append(JdbcConstantes.PRTR_ROFLCR_ID);sbSql.append(" = roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ID); 
//			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
//			sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = prtr.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
//			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
//			sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = ?");
////			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ?");
//			con = ds.getConnection();
//			
////			pstmt = con.prepareStatement(sbSql.toString());
//			pstmt.setInt(1, item.getTrttId()); //cargo el id de tramite_titulo
//			retorno = new EstudianteValidacionJdbcDto();
////			pstmt.setInt(2, item.getTrttCarrera());
//			rs = pstmt.executeQuery();
//			while(rs.next()){
//				if(rs.getInt(JdbcConstantes.VLD_CULMINO_MALLA)==GeneralesConstantes.APP_ID_BASE.intValue()){
//					retorno.setVldCulminoMalla(GeneralesConstantes.APP_ID_BASE.intValue());
//				}else{
//					retorno.setVldCulminoMalla(rs.getInt(JdbcConstantes.VLD_CULMINO_MALLA));
//				}
//				if(rs.getInt(JdbcConstantes.VLD_REPROBO_COMPLEXIVO)==GeneralesConstantes.APP_ID_BASE.intValue()){
//					retorno.setVldReproboComplexivo(GeneralesConstantes.APP_ID_BASE.intValue());
//				}else{
//					retorno.setVldReproboComplexivo(rs.getInt(JdbcConstantes.VLD_REPROBO_COMPLEXIVO));
//				}
//				if(rs.getInt(JdbcConstantes.VLD_ASIGNADO_TUTOR)==GeneralesConstantes.APP_ID_BASE.intValue()){
//					retorno.setVldAsignadoTutor(GeneralesConstantes.APP_ID_BASE.intValue());
//				}else{
//					retorno.setVldAsignadoTutor(rs.getInt(JdbcConstantes.VLD_ASIGNADO_TUTOR));
//				}
//				if(rs.getInt(JdbcConstantes.VLD_ULTIMO_SEMESTRE)==GeneralesConstantes.APP_ID_BASE.intValue()){
//					retorno.setVldUltimoSemestre(GeneralesConstantes.APP_ID_BASE.intValue());
//				}else{
//					retorno.setVldUltimoSemestre(rs.getInt(JdbcConstantes.VLD_ULTIMO_SEMESTRE));
//				}
//				if(rs.getDate(JdbcConstantes.FCES_FECHA_EGRESAMIENTO)==null){
//					retorno.setFcesFechaEgresamiento(null);
//				}else{
//					retorno.setFcesFechaEgresamiento(rs.getDate(JdbcConstantes.FCES_FECHA_EGRESAMIENTO));
//					}
//				retorno.setVldRslIdoneidad(rs.getInt(JdbcConstantes.VLD_RSL_IDONEIDAD));
//			}
		}catch( Exception  e){
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new EstudiantePostuladoJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteValidacionJdbcDto.sql.exception")));
		}finally {
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
	public List<EstudianteValidacionJdbcDto> buscarAptitudActualizacionConocimientos(String cedula) throws EstudiantePostuladoJdbcDtoException, EstudiantePostuladoJdbcDtoNoEncontradoException {
		List<EstudianteValidacionJdbcDto> retorno=null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" ,");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" ,");
			sbSql.append(" CASE WHEN");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(" IS NOT NULL THEN ");
			sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(" ELSE ' '");
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);sbSql.append(" ,");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" ,");
			sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);sbSql.append(" ,");
//			sbSql.append(" CASE WHEN apt.");sbSql.append(JdbcConstantes.APT_APROBO_ACTUALIZAR); sbSql.append(" IS NOT NULL THEN ");sbSql.append(JdbcConstantes.APT_APROBO_ACTUALIZAR);
//			sbSql.append(" ELSE ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
//			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.APT_APROBO_ACTUALIZAR);
			sbSql.append(" ,trtt.");	sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" ,crr.");	sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,prtr.");	sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" ,trtt.");	sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" ,cnv.");	sbSql.append(JdbcConstantes.CNV_DESCRIPCION);
			sbSql.append(" ,fcl.");	sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
//			sbSql.append(" FROM "); sbSql.append(JdbcConstantes.TABLA_APTITUD);sbSql.append(" apt, ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA); sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE); sbSql.append(" prtr, ");
			sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO); sbSql.append(" trtt, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE); sbSql.append(" fces, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA); sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_FACULTAD); sbSql.append(" fcl, ");
			sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA); sbSql.append(" cnv ");
			sbSql.append(" WHERE ");
			sbSql.append(" prtr.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_ID); 
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);sbSql.append(" = prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
//			sbSql.append(" AND ");sbSql.append(" prtr.");sbSql.append(JdbcConstantes.PRTR_ID);sbSql.append(" = apt.");sbSql.append(JdbcConstantes.APT_PRTR_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append(" = fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
//			sbSql.append(" AND ");sbSql.append(" apt.");sbSql.append(JdbcConstantes.APT_APROBO_ACTUALIZAR);sbSql.append(" = ");sbSql.append(AptitudConstantes.SI_APROBO_ACTUALIZAR_VALUE);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ?");
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, cedula); //cargo la cédula
			retorno = new ArrayList<EstudianteValidacionJdbcDto>();
			rs = pstmt.executeQuery();
			while(rs.next()){
				EstudianteValidacionJdbcDto aux = new EstudianteValidacionJdbcDto();
				aux.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
				aux.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
				aux.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
				aux.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
				aux.setPrsMailPersonal(rs.getString(JdbcConstantes.PRS_MAIL_PERSONAL));
				aux.setPrsTelefono(rs.getString(JdbcConstantes.PRS_TELEFONO));
				aux.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
				aux.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
				aux.setTrttCarreraId(rs.getInt(JdbcConstantes.TRTT_CARRERA_ID));
				aux.setCrrId(rs.getInt(JdbcConstantes.TRTT_CARRERA_ID));
				aux.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
				aux.setCnvDescripcion(rs.getString(JdbcConstantes.CNV_DESCRIPCION));
				aux.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
//				if(rs.getInt(JdbcConstantes.APT_APROBO_ACTUALIZAR)==GeneralesConstantes.APP_ID_BASE.intValue()){
//					aux.setAptAproboActualizacion(GeneralesConstantes.APP_ID_BASE.intValue());
//				}else{
//					aux.setAptAproboActualizacion(rs.getInt(JdbcConstantes.APT_APROBO_ACTUALIZAR));
//				}
				aux.setPrtrId(rs.getInt(JdbcConstantes.PRTR_ID));
				aux.setTrttId(rs.getInt(JdbcConstantes.TRTT_ID));
				retorno.add(aux);
			}
		}catch( SQLException  e){
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new EstudiantePostuladoJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteValidacionJdbcDto.sql.exception")));
		}finally {
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
	public List<EstudianteValidacionJdbcDto> buscarEstudiantesProcesoCambioActualizacion()  {
		List<EstudianteValidacionJdbcDto> retorno=null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , CASE WHEN");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(" IS NOT NULL THEN ");
			sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(" ELSE ' '");
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,trtt.");	sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" ,trtt.");	sbSql.append(JdbcConstantes.TRTT_OBSERVACION);
			sbSql.append(" ,crr.");	sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
//			sbSql.append(" ,vld.");	sbSql.append(JdbcConstantes.VLD_ID);
			sbSql.append(" ,trtt.");	sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" ,cnv.");	sbSql.append(JdbcConstantes.CNV_DESCRIPCION);
			sbSql.append(" ,fcl.");	sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
			sbSql.append(" FROM "); sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt, ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA); sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE); sbSql.append(" prtr, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE); sbSql.append(" fces, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA); sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_FACULTAD); sbSql.append(" fcl, ");
//			sbSql.append(JdbcConstantes.TABLA_VALIDACION); sbSql.append(" vld, ");
			sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA); sbSql.append(" cnv ");
			sbSql.append(" WHERE ");
			sbSql.append(" prtr.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_ID); 
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);sbSql.append(" = prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
//			sbSql.append(" AND ");sbSql.append(" prtr.");sbSql.append(JdbcConstantes.PRTR_ID);sbSql.append(" = vld.");sbSql.append(JdbcConstantes.VLD_PRTR_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append(" = fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.EST_PROC_ASIGNADO_MECANISMO_TITULACION_VALUE);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);sbSql.append(" IS NOT NULL ");
			sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_OBSERVACION);sbSql.append(" IN('");
			sbSql.append(ValidacionConstantes.OTROS_MECANISMOS_IDONEO_LABEL);sbSql.append("','");
			sbSql.append(ValidacionConstantes.SELECCIONAR_CUALQUIER_MECANISMO_IDONEO_LABEL);sbSql.append("','");
			sbSql.append(ValidacionConstantes.SI_DEBE_TITULARSE_POR_OTROS_MECANISMO_LABEL);sbSql.append("')");
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSql.toString());
			retorno = new ArrayList<EstudianteValidacionJdbcDto>();
			rs = pstmt.executeQuery();
			while(rs.next()){
				EstudianteValidacionJdbcDto aux = new EstudianteValidacionJdbcDto();
				aux.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
				aux.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
				aux.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
				aux.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
				aux.setPrsMailPersonal(rs.getString(JdbcConstantes.PRS_MAIL_PERSONAL));
				aux.setPrsTelefono(rs.getString(JdbcConstantes.PRS_TELEFONO));
				aux.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
				aux.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
				aux.setTrttCarreraId(rs.getInt(JdbcConstantes.TRTT_CARRERA_ID));
				aux.setCrrId(rs.getInt(JdbcConstantes.TRTT_CARRERA_ID));
				aux.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
				aux.setCnvDescripcion(rs.getString(JdbcConstantes.CNV_DESCRIPCION));
				aux.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
//				aux.setVldId(rs.getInt(JdbcConstantes.VLD_ID));
				aux.setFcesFechaEgresamiento(rs.getDate(JdbcConstantes.FCES_FECHA_EGRESAMIENTO));
				aux.setTrttId(rs.getInt(JdbcConstantes.TRTT_ID));
				aux.setTrttObsValSec(rs.getString(JdbcConstantes.TRTT_OBSERVACION));
				retorno.add(aux);
			}
		}catch( SQLException  e){
		}finally {
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
	public List<EstudianteValidacionJdbcDto> buscarEstudiantesProcesoCambioActualizacionRevision()  {
		List<EstudianteValidacionJdbcDto> retorno=null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , CASE WHEN");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(" IS NOT NULL THEN ");
			sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(" ELSE ' '");
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,trtt.");	sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" ,trtt.");	sbSql.append(JdbcConstantes.TRTT_OBSERVACION);
			sbSql.append(" ,crr.");	sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
//			sbSql.append(" ,vld.");	sbSql.append(JdbcConstantes.VLD_ID);
			sbSql.append(" ,trtt.");	sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" ,cnv.");	sbSql.append(JdbcConstantes.CNV_DESCRIPCION);
			sbSql.append(" ,fcl.");	sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
			sbSql.append(" FROM "); sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt, ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA); sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE); sbSql.append(" prtr, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE); sbSql.append(" fces, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA); sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_FACULTAD); sbSql.append(" fcl, ");
//			sbSql.append(JdbcConstantes.TABLA_VALIDACION); sbSql.append(" vld, ");
			sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA); sbSql.append(" cnv ");
			sbSql.append(" WHERE ");
			sbSql.append(" prtr.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_ID); 
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);sbSql.append(" = prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
//			sbSql.append(" AND ");sbSql.append(" prtr.");sbSql.append(JdbcConstantes.PRTR_ID);sbSql.append(" = vld.");sbSql.append(JdbcConstantes.VLD_PRTR_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append(" = fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);sbSql.append(" IS NOT NULL ");
			sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_OBSERVACION);sbSql.append(" IN('");
			sbSql.append(ValidacionConstantes.ACTUALIZACION_COMPLEXIVO_IDONEO_DOS_PERIODOS_LABEL);sbSql.append("','");
			sbSql.append(ValidacionConstantes.ACTUALIZACION_OTROS_MECANISMOS_IDONEO_DOS_PERIODOS_LABEL);sbSql.append("')");
			con = ds.getConnection();
			
			
			pstmt = con.prepareStatement(sbSql.toString());
			retorno = new ArrayList<EstudianteValidacionJdbcDto>();
			rs = pstmt.executeQuery();
			while(rs.next()){
				EstudianteValidacionJdbcDto aux = new EstudianteValidacionJdbcDto();
				aux.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
				aux.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
				aux.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
				aux.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
				aux.setPrsMailPersonal(rs.getString(JdbcConstantes.PRS_MAIL_PERSONAL));
				aux.setPrsTelefono(rs.getString(JdbcConstantes.PRS_TELEFONO));
				aux.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
				aux.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
				aux.setTrttCarreraId(rs.getInt(JdbcConstantes.TRTT_CARRERA_ID));
				aux.setCrrId(rs.getInt(JdbcConstantes.TRTT_CARRERA_ID));
				aux.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
				aux.setCnvDescripcion(rs.getString(JdbcConstantes.CNV_DESCRIPCION));
				aux.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
//				aux.setVldId(rs.getInt(JdbcConstantes.VLD_ID));
				aux.setFcesFechaEgresamiento(rs.getDate(JdbcConstantes.FCES_FECHA_EGRESAMIENTO));
				aux.setTrttId(rs.getInt(JdbcConstantes.TRTT_ID));
				aux.setTrttObsValSec(rs.getString(JdbcConstantes.TRTT_OBSERVACION));
				retorno.add(aux);
			}
		}catch( SQLException  e){
			e.printStackTrace();
		}finally {
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
	public List<EstudianteValidacionJdbcDto> buscarEstudiantesProcesoCambioActualizacionProcesoInactivo()  {
		List<EstudianteValidacionJdbcDto> retorno=null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" , prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , CASE WHEN");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(" IS NOT NULL THEN ");
			sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(" ELSE ' '");
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,trtt.");	sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" ,trtt.");	sbSql.append(JdbcConstantes.TRTT_OBSERVACION);
			sbSql.append(" ,crr.");	sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
//			sbSql.append(" ,vld.");	sbSql.append(JdbcConstantes.VLD_ID);
			sbSql.append(" ,trtt.");	sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" ,cnv.");	sbSql.append(JdbcConstantes.CNV_DESCRIPCION);
			sbSql.append(" ,fcl.");	sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
			sbSql.append(" FROM "); sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt, ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA); sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE); sbSql.append(" prtr, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE); sbSql.append(" fces, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA); sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_FACULTAD); sbSql.append(" fcl, ");
//			sbSql.append(JdbcConstantes.TABLA_VALIDACION); sbSql.append(" vld, ");
			sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA); sbSql.append(" cnv ");
			sbSql.append(" WHERE ");
			sbSql.append(" prtr.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_ID); 
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);sbSql.append(" = prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
//			sbSql.append(" AND ");sbSql.append(" prtr.");sbSql.append(JdbcConstantes.PRTR_ID);sbSql.append(" = vld.");sbSql.append(JdbcConstantes.VLD_PRTR_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append(" = fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.EST_PROC_VALIDADO_VALUE);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);sbSql.append(" IS NOT NULL ");
			sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_OBSERVACION);sbSql.append(" IN('");
			sbSql.append(ValidacionConstantes.OTROS_MECANISMOS_IDONEO_LABEL);sbSql.append("','");
			sbSql.append(ValidacionConstantes.SELECCIONAR_CUALQUIER_MECANISMO_IDONEO_LABEL);sbSql.append("','");
			sbSql.append(ValidacionConstantes.SI_DEBE_TITULARSE_POR_OTROS_MECANISMO_LABEL);sbSql.append("')");
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSql.toString());
			retorno = new ArrayList<EstudianteValidacionJdbcDto>();
			rs = pstmt.executeQuery();
			while(rs.next()){
				EstudianteValidacionJdbcDto aux = new EstudianteValidacionJdbcDto();
				aux.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
				aux.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
				aux.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
				aux.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
				aux.setPrsMailPersonal(rs.getString(JdbcConstantes.PRS_MAIL_PERSONAL));
				aux.setPrsTelefono(rs.getString(JdbcConstantes.PRS_TELEFONO));
				aux.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
				aux.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
				aux.setTrttCarreraId(rs.getInt(JdbcConstantes.TRTT_CARRERA_ID));
				aux.setCrrId(rs.getInt(JdbcConstantes.TRTT_CARRERA_ID));
				aux.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
				aux.setCnvDescripcion(rs.getString(JdbcConstantes.CNV_DESCRIPCION));
				aux.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
//				aux.setVldId(rs.getInt(JdbcConstantes.VLD_ID));
				aux.setFcesFechaEgresamiento(rs.getDate(JdbcConstantes.FCES_FECHA_EGRESAMIENTO));
				aux.setTrttId(rs.getInt(JdbcConstantes.TRTT_ID));
				aux.setTrttObsValSec(rs.getString(JdbcConstantes.TRTT_OBSERVACION));
				retorno.add(aux);
			}
		}catch( SQLException  e){
		}finally {
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
	 * Método que guarda la edición de aptitud del postulante por parte de la DGA
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EvaluadosDtoJdbcException 
	 * @throws EvaluadosDtoJdbcNoEncontradoException 
	 */
	@Override
	public Integer guardarEdicionAptitud(
			EstudianteAptitudJdbcDto item, String validadorIdentificacion) throws EvaluadosDtoJdbcException,
			EvaluadosDtoJdbcNoEncontradoException {
		//**********************************************************************
		//********** actualizar el tramite actual  *****************************
		//**********************************************************************
		ProcesoTramite nuevoPrtr = em.find(ProcesoTramite.class, item.getPrtrId());
		
		Integer validador;
		TramiteTitulo trttAux = em.find(TramiteTitulo.class, item.getTrttId());
		if(item.getAptRequisitos()==AptitudConstantes.SI_CUMPLE_REQUISITOS_VALUE &&(item.getAptAproboActualizar()==GeneralesConstantes.APP_ID_BASE || item.getAptAproboActualizar()==AptitudConstantes.SI_APROBO_ACTUALIZAR_VALUE)){
			trttAux.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_APTO_EVALUADO_VALUE);
//			trttAux.setTrttObservacionDga(item.getTrttObsDesactivarDga());
//			TramiteTituloConstantes.ESTADO_PROCESO_APTO_VALUE
			nuevoPrtr.setPrtrTipoProceso(ProcesoTramiteConstantes.TIPO_PROCESO_APTO_EVALUADO_VALUE);
			nuevoPrtr.setPrtrFechaEjecucion(item.getPrtrFechaEjecucion());
			validador=ProcesoTramiteConstantes.TIPO_PROCESO_APTO_VALUE;
		}else{
			trttAux.setTrttEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_NO_APTO_EVALUADO_VALUE);
//			trttAux.setTrttObservacionDga(item.getTrttObsDesactivarDga());
//			TramiteTituloConstantes.ESTADO_PROCESO_NO_APTO_VALUE
			nuevoPrtr.setPrtrTipoProceso(ProcesoTramiteConstantes.TIPO_PROCESO_NO_APTO_EVALUADO_VALUE);
			nuevoPrtr.setPrtrFechaEjecucion(item.getPrtrFechaEjecucion());
			validador=ProcesoTramiteConstantes.TIPO_PROCESO_NO_APTO_VALUE;
		}
		
		//Creamos el registro de la tabla aptitud
		
		Aptitud nuevoAptitud= em.find(Aptitud.class, item.getAptId());
		if(item.getAptAproboActualizar()!=GeneralesConstantes.APP_ID_BASE) {
			nuevoAptitud.setAptAproboActualizar(item.getAptAproboActualizar());
		}else{
			nuevoAptitud.setAptAproboActualizar(null);
		}
//		if(nuevoAptitud.getAptRequisitos()!=item.getAptRequisitos()) nuevoAptitud.setAptRequisitos(item.getAptRequisitos());
//		if(nuevoAptitud.getAptReproboCreditos()!=item.getAptReproboCreditos()) nuevoAptitud.setAptReproboCreditos(item.getAptReproboCreditos());
//		if(nuevoAptitud.getAptSegundaCarrera()!=item.getAptSegundaCarrera()) nuevoAptitud.setAptSegundaCarrera(item.getAptSegundaCarrera());
//		nuevoAptitud.setAptProcesoTramite(nuevoPrtr);
//		trttAux.setTrttObservacionDga(item.getTrttObservacionDga());
		return validador;
	}

}
