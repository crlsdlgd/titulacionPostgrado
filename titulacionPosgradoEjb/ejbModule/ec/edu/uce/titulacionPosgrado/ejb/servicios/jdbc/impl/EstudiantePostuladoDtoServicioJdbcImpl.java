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
 21/08/2018				Daniel Albuja				       Emisión Inicial
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
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudiantePostuladoJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudiantePostuladoJdbcDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudiantePostuladoJdbcDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.EstudiantePostuladoDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.FichaEstudianteConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.InstitucionAcademicaConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.MecanismoTitulacionCarreraConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.PersonaConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.ProcesoTramiteConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.TramiteTituloConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

/**
 * EJB EstudiantePostuladoDtoServicioJdbcImpl.
 * Clase Clase donde se implementan los metodos para el servicio jdbc de la consulta estudiante postulado.
 * @author dalbuja
 * @version 1.0
 */
@Stateless
public class EstudiantePostuladoDtoServicioJdbcImpl implements EstudiantePostuladoDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	/**
	 * Método que busca un estudiante por identificacion, por carrera, por estado convocatoria
	 * @param idCarrera - id de la carrera que ha seleccionado en la búsqueda .
	 * @param listCarreras - lista de carreras en caso de que haya seleccionado todas las carreras disponibles para el ususario.
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudiantePostuladoJdbcDtoException 
	 * @throws EstudiantePostuladoJdbcDtoNoEncontradoException 
	 */
	public List<EstudiantePostuladoJdbcDto> buscarPostulacionesEstudianteXFacultadXcarreraXIndetificacionXEstadoConvocatoria(String identificacion, int idFacultad, String idCarrera, int estadoConvocatoria  ) throws EstudiantePostuladoJdbcDtoException, EstudiantePostuladoJdbcDtoNoEncontradoException {
		List<EstudiantePostuladoJdbcDto> retorno = null;
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
			
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
			sbSql.append(" ELSE ");sbSql.append("NULL"); 
							sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
							
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
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_OBSERVACION);
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
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);
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
			sbSql.append(" ORDER BY ");sbSql.append(" prtr.");sbSql.append(JdbcConstantes.PRTR_ID);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
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
			retorno = new ArrayList<EstudiantePostuladoJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
			
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new EstudiantePostuladoJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudiantePostuladoJdbcDto.sql.exception")));
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
	 * Método que busca un estudiante por identificacion y convocatoria
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudiantePostuladoJdbcDtoException 
	 * @throws EstudiantePostuladoJdbcDtoNoEncontradoException 
	 */
	public List<EstudiantePostuladoJdbcDto> buscarPostulacionesEstudianteXIndetificacion(String identificacion) throws EstudiantePostuladoJdbcDtoException, EstudiantePostuladoJdbcDtoNoEncontradoException {
		List<EstudiantePostuladoJdbcDto> retorno = null;
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
			sbSql.append(" , CASE WHEN trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
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
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);
			sbSql.append(" , CASE WHEN ttl.");sbSql.append(JdbcConstantes.TTL_ID); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
					sbSql.append(" ELSE ttl.");sbSql.append(JdbcConstantes.TTL_ID); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TTL_ID);
			sbSql.append(" , CASE WHEN ttl.");sbSql.append(JdbcConstantes.TTL_DESCRIPCION); sbSql.append(" IS NULL THEN ");sbSql.append("''");
					sbSql.append(" ELSE ttl.");sbSql.append(JdbcConstantes.TTL_DESCRIPCION); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TTL_DESCRIPCION);
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
			sbSql.append(" ELSE ");sbSql.append("NULL"); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
			sbSql.append(" , CASE WHEN trtt.");sbSql.append(JdbcConstantes.TRTT_OBSERVACION); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_OBSERVACION);
			sbSql.append(" ELSE ");sbSql.append("NULL"); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TRTT_OBSERVACION);
					
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
						sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
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
			sbSql.append(" AND (prtr.");sbSql.append(JdbcConstantes.PRTR_TIPO_PROCESO);sbSql.append(" = ");
			sbSql.append(ProcesoTramiteConstantes.TIPO_PROCESO_VALIDACION_ERRONEA_VALUE);
			sbSql.append(" OR prtr.");sbSql.append(JdbcConstantes.PRTR_TIPO_PROCESO);sbSql.append(" = ");
			sbSql.append(ProcesoTramiteConstantes.TIPO_PROCESO_POSTULACION_VALUE);sbSql.append(" ) ");
			sbSql.append(" AND (trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" = ");
			sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_POSTULADO_VALUE);sbSql.append(" ) ");
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");
			sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			
			//FIN QUERY
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSql.toString());
			
			pstmt.setString(1, "%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase())+"%"); //cargo la identificacion
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<EstudiantePostuladoJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new EstudiantePostuladoJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudiantePostuladoJdbcDto.sql.exception")));
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
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	/**
	 * Realiza la busqueda de del tipo proceso tramite por idendtificacion
	 * @param identificacion - id de la facultad
	 * @return EstudiantePostuladoJdbcDto con el idendtificacion solicitado 
	 * @throws EstudiantePostuladoJdbcDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws EstudiantePostuladoJdbcDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	
	public EstudiantePostuladoJdbcDto buscaTipoTramiteXIdentificacion(String identificacion) throws EstudiantePostuladoJdbcDtoException, EstudiantePostuladoJdbcDtoNoEncontradoException{
		EstudiantePostuladoJdbcDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces, ");
			sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ");
			sbSql.append(" WHERE ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" and trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);
			sbSql.append(" and ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ? ");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, identificacion); //cargo la identificacion
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADtoPostulate(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new EstudiantePostuladoJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudiantePostuladoJdbcDto.sql.exception")));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new EstudiantePostuladoJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Visualizacion.postulacion.persona.excepcion")));
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
		
		if(retorno == null){
			//TODO CAMBIAR MENSAJE
			throw new EstudiantePostuladoJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Facultad.buscar.por.id.no.result.exception",identificacion)));
		}	
		
		return retorno;
	}
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */
	private EstudiantePostuladoJdbcDto transformarResultSetADto(ResultSet rs) throws SQLException{
		int valAux = 0;
		java.sql.Date fecha = null;
		EstudiantePostuladoJdbcDto retorno = new EstudiantePostuladoJdbcDto();
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
		
//		valAux = rs.getInt(JdbcConstantes.FCES_TIPO_COLEGIO);
//		if(valAux != GeneralesConstantes.APP_ID_BASE.intValue()){
//			retorno.setFcesTipoColegio(valAux);
//			retorno.setFcesTipoColegioSt(InstitucionAcademicaConstantes.valueToLabelTipoColegioUce(valAux));
//		}
		
//		retorno.setFcesTipoColegioSniese(rs.getString(JdbcConstantes.FCES_TIPO_COLEGIO_SNIESE));
//		retorno.setFcesTituloBachiller(rs.getString(JdbcConstantes.FCES_TITULO_BACHILLER));
		retorno.setFcesFechaCreacion(rs.getTimestamp(JdbcConstantes.FCES_FECHA_CREACION));
		
//		retorno.setFcesFechaEgresamiento(rs.getDate(JdbcConstantes.FCES_FECHA_EGRESAMIENTO));
		
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
		
		valAux = rs.getInt(JdbcConstantes.TRTT_CARRERA_ID);
		if(valAux != GeneralesConstantes.APP_ID_BASE.intValue()){
			retorno.setTrttCarrera(rs.getInt(JdbcConstantes.TRTT_CARRERA_ID));
		}
		
		valAux = rs.getInt(JdbcConstantes.TRTT_ID);
		if(valAux != GeneralesConstantes.APP_ID_BASE.intValue()){
			retorno.setTrttId(valAux);
			retorno.setTrttNumTramite(rs.getString(JdbcConstantes.TRTT_NUM_TRAMITE));
		}
		
		
		
		retorno.setFclId(rs.getInt(JdbcConstantes.FCL_ID));
		retorno.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
		retorno.setCnvId(rs.getInt(JdbcConstantes.CNV_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setCnvDescripcion(rs.getString(JdbcConstantes.CNV_DESCRIPCION));
//		retorno.setTtlDescripcion(rs.getString(JdbcConstantes.TTL_DESCRIPCION));
		switch (rs.getInt(JdbcConstantes.TRTT_ESTADO_PROCESO)){
		case 0:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_POSTULADO_LABEL);
			break;
		case 1:
			retorno.setEstadoProceso(TramiteTituloConstantes.EST_PROC_VALIDADO_LABEL);
			break;
		case 3:
			retorno.setEstadoProceso(TramiteTituloConstantes.EST_PROC_ASIGNADO_MECANISMO_TITULACION_LABEL);
			break;
		case 4:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_APTO_LABEL);
			break;	
		case 5:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_NO_APTO_LABEL);
			break;
		case 6:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_APTO_EVALUADO_LABEL);
			
			break;
		case 7:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_NO_APTO_EVALUADO_LABEL);
			break;
		case 8:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_TRIBUNAL_LECTOR_PENDIENTE_LABEL);
			break;
		case 9:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_PRACTICO_CARGADO_LABEL);
			break;
		case 10:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_CARGADO_LABEL);
			break;
		case 11:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_FINAL_CARGADO_LABEL);
			break;	
		case 12:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_PRACTICO_GRACIA_LABEL);
			break;		
		case 13:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_TEORICO_GRACIA_LABEL);
			break;	
		case 14:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_FINAL_GRACIA_CARGADO_LABEL);
			break;
		case 15:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_DEFENSA_ORAL_LABEL);
			break;	
		case 16:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_DEFENSA_ORAL_VALIDADO_LABEL);
			break;	
		case 100:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_LABEL);
			break;		
		case 101:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_GRACIA_LABEL);
			break;	
		case 102:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_VALIDADO_LABEL);
			break;
		case 103:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_GRACIA_VALIDADO_LABEL);
			break;
		case 104:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_LABEL);
			break;
		case 105:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_DEFENSA_LABEL);
			break;	
		case 106:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_LISTO_EDICION_ACTA_LABEL);
			break;
		case 107:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_LISTO_EDICION_ACTA_DEFENSA_ORAL_LABEL);
			break;
		case 108:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_LABEL);
			break;	
		case 109:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_DEFENSA_ORAL_LABEL);
			break;
		}
		return retorno;
	} 
	
	private EstudiantePostuladoJdbcDto transformarResultSetADtoPostulate(ResultSet rs) throws SQLException{
		EstudiantePostuladoJdbcDto retorno = new EstudiantePostuladoJdbcDto();
		retorno.setTrttEstadoProceso(rs.getInt(JdbcConstantes.TRTT_ESTADO_PROCESO));
		return retorno;
	}
	
	/**
	 * Método que busca las postulaciones de un estudiante por identificacion, por carrera, por estado convocatoria
	 * @param idCarrera - id de la carrera que ha seleccionado en la búsqueda .
	 * @param listCarreras - lista de carreras en caso de que haya seleccionado todas las carreras disponibles para el ususario.
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudiantePostuladoJdbcDtoException 
	 * @throws EstudiantePostuladoJdbcDtoNoEncontradoException 
	 */
	public List<EstudiantePostuladoJdbcDto> buscarPostulacionesVistaEstudiante(String identificacion, int idFacultad, String idCarrera, int estadoConvocatoria  ) throws EstudiantePostuladoJdbcDtoException, EstudiantePostuladoJdbcDtoNoEncontradoException {
		List<EstudiantePostuladoJdbcDto> retorno = null;
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
			
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
			sbSql.append(" ELSE ");sbSql.append("NULL"); 
							sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
							
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
			sbSql.append(" , CASE WHEN trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
//			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA);
//			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_OBS_VAL_SEC_ABG);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ID);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ESTADO);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
//			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
//			sbSql.append(" , CASE WHEN ttl.");sbSql.append(JdbcConstantes.TTL_ID); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
//					sbSql.append(" ELSE ttl.");sbSql.append(JdbcConstantes.TTL_ID); 
//					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TTL_ID);
//			sbSql.append(" , CASE WHEN ttl.");sbSql.append(JdbcConstantes.TTL_DESCRIPCION); sbSql.append(" IS NULL THEN ");sbSql.append("''");
//					sbSql.append(" ELSE ttl.");sbSql.append(JdbcConstantes.TTL_DESCRIPCION); 
//					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TTL_DESCRIPCION);
//			// PROCESO DE VALIDACION
//					sbSql.append(" , CASE WHEN ttl.");sbSql.append(JdbcConstantes.TTL_DESCRIPCION); sbSql.append(" IS NULL THEN ");sbSql.append("''");
//					sbSql.append(" ELSE ttl.");sbSql.append(JdbcConstantes.TTL_DESCRIPCION); 
//					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TTL_DESCRIPCION);
					
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
//			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ON ");
//						sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ON ");
						sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
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
//			sbSql.append(" RIGHT JOIN ");sbSql.append(JdbcConstantes.TABLA_TITULO);sbSql.append(" ttl ON ");
//						sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_TITULO_BACHILLER);sbSql.append(" = ");sbSql.append(" ttl.");sbSql.append(JdbcConstantes.TTL_ID);
						
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
//			sbSql.append(" AND ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ESTADO);sbSql.append(" = ");sbSql.append(" ? ");			
			//carrera
			if(idCarrera != null){
				sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" = ");sbSql.append(" ? ");
			}
			// estado activo
			sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" ORDER BY ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" DESC  ");
			sbSql.append(" , fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
						sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);

			//FIN QUERY
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase())); //cargo la identificacion
			pstmt.setInt(2, idFacultad); //cargo la facultad
//			pstmt.setInt(3, estadoConvocatoria); //cargo el estado
			if(idCarrera != null){
				pstmt.setString(3, idCarrera); //cargo la carrera
			}
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<EstudiantePostuladoJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
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
			} catch (SQLException e2) {
			}
			throw new EstudiantePostuladoJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudiantePostuladoJdbcDto.sql.exception")));
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
			} catch (SQLException e2) {
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
	 * Método que busca las postulaciones de un estudiante por identificacionpor facultad y convocatoria
	 * @param idFacultad - lista de carreras en caso de que haya seleccionado todas las carreras disponibles para el ususario.
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudiantePostuladoJdbcDtoException 
	 * @throws EstudiantePostuladoJdbcDtoNoEncontradoException 
	 */
	public List<EstudiantePostuladoJdbcDto> buscarPostulantesParaDesactivar(String identificacion, int idFacultad, int convocatoria  ) throws EstudiantePostuladoJdbcDtoException, EstudiantePostuladoJdbcDtoNoEncontradoException {
		List<EstudiantePostuladoJdbcDto> retorno = null;
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
			
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
			sbSql.append(" ELSE ");sbSql.append("NULL"); 
							sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
							
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
			sbSql.append(" , CASE WHEN trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_OBSERVACION);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_MCTT_ESTADISTICO);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ID);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ESTADO);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
//			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , CASE WHEN ttl.");sbSql.append(JdbcConstantes.TTL_ID); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
					sbSql.append(" ELSE ttl.");sbSql.append(JdbcConstantes.TTL_ID); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TTL_ID);
			sbSql.append(" , CASE WHEN ttl.");sbSql.append(JdbcConstantes.TTL_DESCRIPCION); sbSql.append(" IS NULL THEN ");sbSql.append("''");
					sbSql.append(" ELSE ttl.");sbSql.append(JdbcConstantes.TTL_DESCRIPCION); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TTL_DESCRIPCION);
			// PROCESO DE VALIDACION
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
			//convocatoria
			if(convocatoria == GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" > ");sbSql.append(" ? ");
			}else{
				sbSql.append(" AND ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ");sbSql.append(" ? ");
			}
			sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" < ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_VALUE);
			sbSql.append(" ORDER BY ");
			sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
						
			//FIN QUERY
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, "%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase())+"%"); //cargo la identificacion
			pstmt.setInt(2, idFacultad); //cargo la facultad
			pstmt.setInt(3, convocatoria); //cargo el estado
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<EstudiantePostuladoJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoDesactivacion(rs));
			}
			
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new EstudiantePostuladoJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudiantePostuladoJdbcDto.sql.exception")));
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
	 * Método que busca las postulaciones de un estudiante por identificacionpor facultad y convocatoria
	 * @param idFacultad - lista de carreras en caso de que haya seleccionado todas las carreras disponibles para el ususario.
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudiantePostuladoJdbcDtoException 
	 * @throws EstudiantePostuladoJdbcDtoNoEncontradoException 
	 */
	@Override
	public List<EstudiantePostuladoJdbcDto> buscarPostulantesParaDesactivarPorCarrera(String identificacion, int idCarrera, int convocatoria, String idEvaluador  ) throws EstudiantePostuladoJdbcDtoException, EstudiantePostuladoJdbcDtoNoEncontradoException {
		List<EstudiantePostuladoJdbcDto> retorno = null;
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
			
			sbSql.append(" , CASE WHEN fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO); sbSql.append(" IS NOT NULL THEN ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
			sbSql.append(" ELSE ");sbSql.append("NULL"); 
							sbSql.append(" END AS ");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
							
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
			sbSql.append(" , CASE WHEN trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
						sbSql.append(" ELSE trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_OBSERVACION);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_MCTT_ESTADISTICO);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ID);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_DESCRIPCION);
			sbSql.append(" ,cnv.");sbSql.append(JdbcConstantes.CNV_ESTADO);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
//			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , CASE WHEN ttl.");sbSql.append(JdbcConstantes.TTL_ID); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
					sbSql.append(" ELSE ttl.");sbSql.append(JdbcConstantes.TTL_ID); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TTL_ID);
			sbSql.append(" , CASE WHEN ttl.");sbSql.append(JdbcConstantes.TTL_DESCRIPCION); sbSql.append(" IS NULL THEN ");sbSql.append("''");
					sbSql.append(" ELSE ttl.");sbSql.append(JdbcConstantes.TTL_DESCRIPCION); 
					sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TTL_DESCRIPCION);
			// PROCESO DE VALIDACION
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
			if(idCarrera == GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" IN ( ");
				sbSql.append(" SELECT DISTINCT crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" from ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
				sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr, ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);
				sbSql.append(" usro, ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");sbSql.append(" WHERE ");
				sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ");
				sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" = ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");
							sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" AND ");sbSql.append(" usr.");
							sbSql.append(JdbcConstantes.USR_IDENTIFICACION);sbSql.append(" LIKE ? )");
			}else{
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");sbSql.append(" ? ");
			}
			//convocatoria
			if(convocatoria == GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" > ");sbSql.append(" ? ");
			}else{
				sbSql.append(" AND ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ");sbSql.append(" ? ");
			}
			sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" < ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_VALUE);
			sbSql.append(" ORDER BY ");
			sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
						
			//FIN QUERY
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, "%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase())+"%"); //cargo la identificacion
			if(idCarrera == GeneralesConstantes.APP_ID_BASE.intValue()){
				pstmt.setString(2, idEvaluador); //cargo la carrera
			}else{
				pstmt.setInt(2, idCarrera); //cargo la carrera
			}
			pstmt.setInt(3, convocatoria); //cargo el estado
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<EstudiantePostuladoJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoDesactivacion(rs));
			}
			
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new EstudiantePostuladoJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudiantePostuladoJdbcDto.sql.exception")));
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
	 * Método que busca las postulaciones de un estudiante por identificacion por facultad y convocatoria
	 * @param idFacultad - lista de carreras en caso de que haya seleccionado todas las carreras disponibles para el ususario.
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de estudiantes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws EstudiantePostuladoJdbcDtoException 
	 * @throws EstudiantePostuladoJdbcDtoNoEncontradoException 
	 */
	public List<EstudiantePostuladoJdbcDto> buscarPostulantesParaValidar( int idFacultad, int idConvocatoria , int idCarrera ) throws EstudiantePostuladoJdbcDtoException, EstudiantePostuladoJdbcDtoNoEncontradoException {
		List<EstudiantePostuladoJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
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
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces, ");
			sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt, ");
			sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv ");
//			sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro, ");
//			sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);
			sbSql.append(" AND ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
//			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
//			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);sbSql.append(" = rol.");sbSql.append(JdbcConstantes.ROL_ID);
//			sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = ");sbSql.append(RolConstantes.ROL_BD_POSTULANTE_VALUE);
			
			//facultad
			if(idFacultad == GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" > ");sbSql.append(" ? ");
			}else{
				sbSql.append(" AND ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" = ");sbSql.append(" ? ");
			}
			//convocatoria
			if(idConvocatoria == GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" > ");sbSql.append(" ? ");
			}else{
				sbSql.append(" AND ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ");sbSql.append(" ? ");
			}
			//carrera
			if(idCarrera == 0 || idCarrera==GeneralesConstantes.APP_ID_BASE ){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" > ");sbSql.append(" ? ");
			}else{
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");sbSql.append(" ? ");
			}
			
			sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			
			sbSql.append(" AND (trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_VALUE);
			sbSql.append(" OR trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_GRACIA_VALUE);
			
			sbSql.append(" )");
			sbSql.append(" UNION ALL ");
			
			sbSql.append(" SELECT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
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
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);	
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);
			sbSql.append(" ,trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
					
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces, ");
			sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt, ");
			sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv, ");
			sbSql.append(JdbcConstantes.TABLA_MECANISMO_CARRERA);sbSql.append(" mcttcr, ");
			sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION);sbSql.append(" mctt ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);
			sbSql.append(" AND ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_MCCR_ID);sbSql.append(" = mcttcr.");sbSql.append(JdbcConstantes.MCCR_ID);
			sbSql.append(" AND ");sbSql.append(" mcttcr.");sbSql.append(JdbcConstantes.MCCR_MCTT_ID);sbSql.append(" = mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
			sbSql.append(" AND mctt.");sbSql.append(JdbcConstantes.MCTT_ID);sbSql.append(" <> ");sbSql.append(MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_VALUE);
			
			//facultad
			if(idFacultad == GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" > ");sbSql.append(" ? ");
			}else{
				sbSql.append(" AND ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" = ");sbSql.append(" ? ");
			}
			//convocatoria
			if(idConvocatoria == GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" > ");sbSql.append(" ? ");
			}else{
				sbSql.append(" AND ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ");sbSql.append(" ? ");
			}
			//carrera
			if(idCarrera == 0 || idCarrera==GeneralesConstantes.APP_ID_BASE ){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" > ");sbSql.append(" ? ");
			}else{
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");sbSql.append(" ? ");
			}
			sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_DEFENSA_ORAL_VALUE);
			
			//FIN QUERY
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idFacultad); //cargo la facultad para la primera consulta
			pstmt.setInt(2, idConvocatoria); //cargo la convocatoria para la primera consulta
			pstmt.setInt(3, idCarrera); //cargo la carrera para la primera consulta 
			pstmt.setInt(4, idFacultad); //cargo la facultad para la segunda consulta
			pstmt.setInt(5, idConvocatoria); //cargo la convocatoria para la segunda consulta
			pstmt.setInt(6, idCarrera); //cargo la carrera para la segunda consulta 
			rs = pstmt.executeQuery();
			retorno = new ArrayList<EstudiantePostuladoJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoXValidar(rs));
			}
			
		} catch (SQLException e) {
		} catch (Exception e) {
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
	private EstudiantePostuladoJdbcDto transformarResultSetADtoXValidar(ResultSet rs) throws SQLException{
		EstudiantePostuladoJdbcDto retorno = new EstudiantePostuladoJdbcDto();

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
		retorno.setTrttId(rs.getInt(JdbcConstantes.TRTT_ID));
		retorno.setTrttEstadoProceso(rs.getInt(JdbcConstantes.TRTT_ESTADO_PROCESO));
		retorno.setTrttEstadoTramite(rs.getInt(JdbcConstantes.TRTT_ESTADO_TRAMITE));
		return retorno;
	} 
	
	private EstudiantePostuladoJdbcDto transformarResultSetADtoDesactivacion(ResultSet rs) throws SQLException{
		int valAux = 0;
		java.sql.Date fecha = null;
		EstudiantePostuladoJdbcDto retorno = new EstudiantePostuladoJdbcDto();
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
		retorno.setCnvDescripcion(rs.getString(JdbcConstantes.CNV_DESCRIPCION));
		
		retorno.setFcesFechaEgresamiento(rs.getDate(JdbcConstantes.FCES_FECHA_EGRESAMIENTO));
		
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
		
		valAux = rs.getInt(JdbcConstantes.TRTT_CARRERA_ID);
		if(valAux != GeneralesConstantes.APP_ID_BASE.intValue()){
			retorno.setTrttCarrera(rs.getInt(JdbcConstantes.TRTT_CARRERA_ID));
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
		retorno.setTrttEstadoTramite(rs.getInt(JdbcConstantes.TRTT_ESTADO_TRAMITE));
		retorno.setTrttObsSecValidacion(rs.getString(JdbcConstantes.TRTT_OBSERVACION));
		
		retorno.setFclId(rs.getInt(JdbcConstantes.FCL_ID));
		retorno.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
//		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setTtlDescripcion(rs.getString(JdbcConstantes.TTL_DESCRIPCION));
		switch (rs.getInt(JdbcConstantes.TRTT_ESTADO_PROCESO)){
		case 0:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_POSTULADO_LABEL);
			break;
		case 1:
			retorno.setEstadoProceso(TramiteTituloConstantes.EST_PROC_VALIDADO_LABEL);
			break;
		case 3:
			retorno.setEstadoProceso(TramiteTituloConstantes.EST_PROC_ASIGNADO_MECANISMO_TITULACION_LABEL);
			break;
		case 4:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_APTO_LABEL);
			break;	
		case 5:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_NO_APTO_LABEL);
			break;
		case 6:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_APTO_EVALUADO_LABEL);
			
			break;
		case 7:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_NO_APTO_EVALUADO_LABEL);
			break;
		case 8:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_TRIBUNAL_LECTOR_PENDIENTE_LABEL);
			break;
		case 9:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_PRACTICO_CARGADO_LABEL);
			break;
		case 10:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_CARGADO_LABEL);
			break;
		case 11:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_FINAL_CARGADO_LABEL);
			break;	
		case 12:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_PRACTICO_GRACIA_LABEL);
			break;		
		case 13:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_TEORICO_GRACIA_LABEL);
			break;	
		case 14:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_FINAL_GRACIA_CARGADO_LABEL);
			break;
		case 15:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_DEFENSA_ORAL_LABEL);
			break;	
		case 16:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_DEFENSA_ORAL_VALIDADO_LABEL);
			break;	
		case 100:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_LABEL);
			break;		
		case 101:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_GRACIA_LABEL);
			break;	
		case 102:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_VALIDADO_LABEL);
			break;
		case 103:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_GRACIA_VALIDADO_LABEL);
			break;
		case 104:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_LABEL);
			break;
		case 105:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_DEFENSA_LABEL);
			break;	
		case 106:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_LISTO_EDICION_ACTA_LABEL);
			break;
		case 107:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_LISTO_EDICION_ACTA_DEFENSA_ORAL_LABEL);
			break;
		case 108:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_LABEL);
			break;	
		case 109:
			retorno.setEstadoProceso(TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_DEFENSA_ORAL_LABEL);
			break;
		}
		return retorno;
		
		
	}



	@Override
	public EstudiantePostuladoJdbcDto buscarCulminoMalla(
			String identificacion, int idConvocatoria, int idCarrera)
			throws EstudiantePostuladoJdbcDtoException,
			EstudiantePostuladoJdbcDtoNoEncontradoException {
		EstudiantePostuladoJdbcDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			retorno = new EstudiantePostuladoJdbcDto();
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
//			sbSql.append("  CASE WHEN vld.");sbSql.append(JdbcConstantes.VLD_CULMINO_MALLA); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
//			sbSql.append(" ELSE vld.");sbSql.append(JdbcConstantes.VLD_CULMINO_MALLA); 
//			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.VLD_CULMINO_MALLA);
			sbSql.append(", vld.");sbSql.append(JdbcConstantes.VLD_ID);
			sbSql.append(", fces.");sbSql.append(JdbcConstantes.FCES_ID);
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces, ");
			sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt, ");
			sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr, ");
//			sbSql.append(JdbcConstantes.TABLA_VALIDACION);sbSql.append(" vld, ");
			sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = prtr.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
//			sbSql.append(" AND ");sbSql.append(" prtr.");sbSql.append(JdbcConstantes.PRTR_ID);sbSql.append(" = vld.");sbSql.append(JdbcConstantes.VLD_PRTR_ID);
			//convocatoria
			if(idConvocatoria == GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" > ");sbSql.append(" ? ");
			}else{
				sbSql.append(" AND ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ");sbSql.append(" ? ");
			}
			//carrera
			if(idCarrera == 0 || idCarrera==GeneralesConstantes.APP_ID_BASE ){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" > ");sbSql.append(" ? ");
			}else{
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");sbSql.append(" ? ");
			}
			
			sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" UPPER(prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(") like ? ");
			
			//FIN QUERY
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idConvocatoria); //cargo la convocatoria para la primera consulta
			pstmt.setInt(2, idCarrera); //cargo la carrera para la primera consulta 
			pstmt.setString(3, identificacion); //cargo la convocatoria para la segunda consulta
			rs = pstmt.executeQuery();
			while(rs.next()){
//				retorno.setVldCulminoMalla(rs.getInt(JdbcConstantes.VLD_CULMINO_MALLA));
//				retorno.setVldId(rs.getInt(JdbcConstantes.VLD_ID));
				retorno.setFcesId(rs.getInt(JdbcConstantes.FCES_ID));
			}
			
			
		} catch (Exception e) {
		}finally {
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



	@Override
	public EstudiantePostuladoJdbcDto buscarFechaEgresamiento(
			String identificacion, int idConvocatoria, int idCarrera)
			throws EstudiantePostuladoJdbcDtoException,
			EstudiantePostuladoJdbcDtoNoEncontradoException {
		EstudiantePostuladoJdbcDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			retorno = new EstudiantePostuladoJdbcDto();
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_FECHA_EGRESAMIENTO);
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces, ");
			sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt, ");
			sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
			
			//convocatoria
			if(idConvocatoria == GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" > ");sbSql.append(" ? ");
			}else{
				sbSql.append(" AND ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ");sbSql.append(" ? ");
			}
			//carrera
			if(idCarrera == 0 || idCarrera==GeneralesConstantes.APP_ID_BASE ){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" > ");sbSql.append(" ? ");
			}else{
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");sbSql.append(" ? ");
			}
			
			sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" UPPER(prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(") like ? ");
			
			//FIN QUERY
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idConvocatoria); //cargo la convocatoria para la primera consulta
			pstmt.setInt(2, idCarrera); //cargo la carrera para la primera consulta 
			pstmt.setString(3, identificacion); //cargo la convocatoria para la segunda consulta
			rs = pstmt.executeQuery();
			while(rs.next()){
				retorno.setFcesFechaEgresamiento(rs.getDate(JdbcConstantes.FCES_FECHA_EGRESAMIENTO));
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
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



	@Override
	public EstudiantePostuladoJdbcDto buscarFechaAsignacionMecanismo(
			String identificacion, int idConvocatoria, int idCarrera)
			throws EstudiantePostuladoJdbcDtoException,
			EstudiantePostuladoJdbcDtoNoEncontradoException {
		EstudiantePostuladoJdbcDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			retorno = new EstudiantePostuladoJdbcDto();
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prtr.");sbSql.append(JdbcConstantes.PRTR_FECHA_EJECUCION);
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces, ");
			sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt, ");
			sbSql.append(JdbcConstantes.TABLA_CONVOCATORIA);sbSql.append(" cnv, ");
//			sbSql.append(JdbcConstantes.TABLA_ASIGNACION_TITULACION);sbSql.append(" astt, ");
			sbSql.append(JdbcConstantes.TABLA_PROCESO_TRAMITE);sbSql.append(" prtr, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = prtr.");sbSql.append(JdbcConstantes.PRTR_TRTT_ID);
			sbSql.append(" AND ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = trtt.");sbSql.append(JdbcConstantes.TRTT_CNV_ID);
//			sbSql.append(" AND ");sbSql.append(" prtr.");sbSql.append(JdbcConstantes.PRTR_ID);sbSql.append(" = astt.");sbSql.append(JdbcConstantes.ASTT_PRTR_ID);
			
			//convocatoria
			if(idConvocatoria == GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" > ");sbSql.append(" ? ");
			}else{
				sbSql.append(" AND ");sbSql.append(" cnv.");sbSql.append(JdbcConstantes.CNV_ID);sbSql.append(" = ");sbSql.append(" ? ");
			}
			//carrera
			if(idCarrera == 0 || idCarrera==GeneralesConstantes.APP_ID_BASE ){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" > ");sbSql.append(" ? ");
			}else{
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");sbSql.append(" ? ");
			}
			
			sbSql.append(" AND ");sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			
			sbSql.append(" AND ");sbSql.append(" UPPER(prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(") like ? ");
			
			//FIN QUERY
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idConvocatoria); //cargo la convocatoria para la primera consulta
			pstmt.setInt(2, idCarrera); //cargo la carrera para la primera consulta 
			pstmt.setString(3, identificacion); //cargo la convocatoria para la segunda consulta
			rs = pstmt.executeQuery();
			while(rs.next()){
				retorno.setPrtrFechaEjecucion(rs.getTimestamp(JdbcConstantes.PRTR_FECHA_EJECUCION));
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
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
