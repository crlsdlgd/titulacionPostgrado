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
   
 ARCHIVO:     DocenteDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de la consulta de docentes
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 20-SEPTIEMBRE-2016		Daniel Albuja				       Emisión Inicial
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

import ec.edu.uce.titulacionPosgrado.ejb.dtos.DocenteTutorTribunalLectorJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.DocenteTutorTribunalLectorJdbcDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.DocenteTutorTribunalLectorJdbcDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.DocenteDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.FichaDocenteConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Carrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Persona;

/**
 * EJB DocenteDtoServicioJdbcImpl.
 * Clase Clase donde se implementan los metodos para el servicio jdbc de la consulta de docentes
 * @author dalbuja
 * @version 1.0
 */
@Stateless
public class DocenteDtoServicioJdbcImpl implements DocenteDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SOURCE_SIIU)
	DataSource dsSiiu;
	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	/**
	 * Método que busca un docente por identificacion
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de docentes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws DocenteTutorTribunalLectorJdbcDtoJdbcDtoException 
	 * @throws DocenteTutorTribunalLectorJdbcDtoJdbcDtoNoEncontradoException 
	 */
	public List<DocenteTutorTribunalLectorJdbcDto> buscarDocenteXIdentificacion(String identificacion, String apellido) throws DocenteTutorTribunalLectorJdbcDtoException, DocenteTutorTribunalLectorJdbcDtoNoEncontradoException{
		List<DocenteTutorTribunalLectorJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
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
//			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ETN_ID);
//			
//			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); sbSql.append(" IS NULL THEN ");sbSql.append("''");
//						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); 
//						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
//			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);
//			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_FECHA_NACIMIENTO);
//			sbSql.append(" ,dtps.dtps_id");
//			sbSql.append(" ,crhr.crhr_id");
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
//			sbSql.append(" detalle_puesto dtps, ");
//			sbSql.append(" carga_horaria crhr ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
//			sbSql.append(" AND fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);sbSql.append(" = dtps.");sbSql.append(JdbcConstantes.FCDC_ID);
//			sbSql.append(" AND dtps.dtps_id = crhr.dtps_id ");
			
			if(identificacion != null  ){
				sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" LIKE ? ");
				
			}else{
				sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" > ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			}
			if(apellido != null ){
				sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" LIKE ? ");
			}
			
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , ");
			sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			//FIN QUERY
//			con = dsSiiu.getConnection();
			con = ds.getConnection();
			
//			System.out.println("sql : " + sbSql.toString()); 
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase())+"%"); //cargo la identificacion
			if(apellido != null ){
				pstmt.setString(2, GeneralesUtilidades.eliminarEspaciosEnBlanco(apellido.toUpperCase())+"%"); //cargo el apellido
			}
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteTutorTribunalLectorJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoDocentes(rs));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocenteTutorTribunalLectorJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocenteTutorTribunalLectorJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.por.identificacion.exception")));
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
			throw new DocenteTutorTribunalLectorJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.por.identificacion.no.result.exception")));
		}
		return retorno;
	}
	
	/**
	 * Método que busca un docente por identificacion
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de docentes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws DocenteTutorTribunalLectorJdbcDtoJdbcDtoException 
	 * @throws DocenteTutorTribunalLectorJdbcDtoJdbcDtoNoEncontradoException 
	 */
	@Override
	public List<DocenteTutorTribunalLectorJdbcDto> buscarDocenteXIdentificacionTitulacion(String identificacion, String apellido) throws DocenteTutorTribunalLectorJdbcDtoException, DocenteTutorTribunalLectorJdbcDtoNoEncontradoException{
		List<DocenteTutorTribunalLectorJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT DISTINCT");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(" IS NULL THEN ");sbSql.append("''");
			sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEXO);sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEXO); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEXO);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEXO_SNIESE);sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEXO_SNIESE); 
			sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEXO_SNIESE);
//			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_ETN_ID);sbSql.append(" IS NULL THEN ");sbSql.append("''");
//			sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_ETN_ID); 
//			sbSql.append(" END AS prs.");sbSql.append(JdbcConstantes.PRS_ETN_ID);
//			
//			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); sbSql.append(" IS NULL THEN ");sbSql.append("''");
//						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); 
//						sbSql.append(" END AS prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
//			sbSql.append(" ,CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);sbSql.append(" IS NULL THEN ");sbSql.append("''");
//			sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO); 
//			sbSql.append(" END AS prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);
//			sbSql.append(" ,CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_FECHA_NACIMIENTO);sbSql.append(" IS NULL THEN ");sbSql.append("''");
//			sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_FECHA_NACIMIENTO); 
//			sbSql.append(" END AS prs.");sbSql.append(JdbcConstantes.PRS_FECHA_NACIMIENTO);
//			sbSql.append(" ,dtps.dtps_id");
//			sbSql.append(" ,crhr.crhr_id");
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
//			sbSql.append(" detalle_puesto dtps, ");
//			sbSql.append(" carga_horaria crhr ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
//			sbSql.append(" AND fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);sbSql.append(" = dtps.");sbSql.append(JdbcConstantes.FCDC_ID);
//			sbSql.append(" AND dtps.dtps_id = crhr.dtps_id ");
			
			if(identificacion != null  ){
				sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" LIKE ? ");
				
			}else{
				sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" > ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			}
			if(apellido != null ){
				sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" LIKE ? ");
			}
			
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , ");
			sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			//FIN QUERY
			con = ds.getConnection();
			
			
//			System.out.println("sql : " + sbSql.toString()); 
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase())+"%"); //cargo la identificacion
			if(apellido != null ){
				pstmt.setString(2, GeneralesUtilidades.eliminarEspaciosEnBlanco(apellido.toUpperCase())+"%"); //cargo el apellido
			}
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteTutorTribunalLectorJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoDocentes(rs));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocenteTutorTribunalLectorJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocenteTutorTribunalLectorJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.por.identificacion.exception")));
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
			throw new DocenteTutorTribunalLectorJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.por.identificacion.no.result.exception")));
		}
		return retorno;
	}
	/**
	 * Método que busca un docente por identificacion
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de docentes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws DocenteTutorTribunalLectorJdbcDtoJdbcDtoException 
	 * @throws DocenteTutorTribunalLectorJdbcDtoJdbcDtoNoEncontradoException 
	 */
	public List<DocenteTutorTribunalLectorJdbcDto> listarDocenteXIdentificacionXApellidoPaterno(String identificacion, String apellido) throws DocenteTutorTribunalLectorJdbcDtoException, DocenteTutorTribunalLectorJdbcDtoNoEncontradoException{
		List<DocenteTutorTribunalLectorJdbcDto> retorno = null;
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
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO_SNIESE);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ETN_ID);
			
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_FECHA_NACIMIENTO);
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" IN (");
			
				sbSql.append(" SELECT ");
					sbSql.append(" prs1.");sbSql.append(JdbcConstantes.PRS_ID);
					sbSql.append(" FROM ");
						sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs1, ");
						sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
					sbSql.append(" WHERE ");
						sbSql.append(" prs1.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");
						sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			if(identificacion != null && apellido != null ){
				sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" LIKE ? ");
				sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" LIKE ? ");
			}else{
				sbSql.append(" AND prs1.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" > ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			}
			sbSql.append(" )");
			
			
			sbSql.append(" ORDER BY ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			//FIN QUERY
			con = ds.getConnection();
			
			
//			System.out.println("sql 123: " + sbSql.toString()); 
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, "%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase())+"%"); //cargo la identificacion
			pstmt.setString(2, GeneralesUtilidades.eliminarEspaciosEnBlanco(apellido.toUpperCase())+"%"); //cargo el apellido
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteTutorTribunalLectorJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoListaDocentes(rs));
			}
			
		} catch (SQLException e) {
			throw new DocenteTutorTribunalLectorJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.sql.exception")));
		} catch (Exception e) {
			throw new DocenteTutorTribunalLectorJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.por.identificacion.exception")));
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
			throw new DocenteTutorTribunalLectorJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.por.identificacion.no.result.exception")));
		}
		return retorno;
	}
	
	
	/**
	 * Método que busca un docente por identificacion
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de docentes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws DocenteTutorTribunalLectorJdbcDtoJdbcDtoException 
	 * @throws DocenteTutorTribunalLectorJdbcDtoJdbcDtoNoEncontradoException 
	 */
	public List<DocenteTutorTribunalLectorJdbcDto> listarCarrerasXIdentificacion(String identificacion) throws DocenteTutorTribunalLectorJdbcDtoException, DocenteTutorTribunalLectorJdbcDtoNoEncontradoException{
		List<DocenteTutorTribunalLectorJdbcDto> retorno = null;
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
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO_SNIESE);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ETN_ID);
			
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_FECHA_NACIMIENTO);
			
			sbSql.append(" ,fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" ,fcdc.");sbSql.append(JdbcConstantes.FCDC_ESTADO);
			sbSql.append(" ,fcdc.");sbSql.append(JdbcConstantes.FCDC_ABREVIATURA_TITULO);
			sbSql.append(" ,fcdc.");sbSql.append(JdbcConstantes.FCDC_CRR_ID);


			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);
			
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
			
						
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
			sbSql.append(" AND fcdc.");sbSql.append(JdbcConstantes.FCDC_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);
			
			if(identificacion != null  ){
				sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" LIKE ? ");
			}else{
				sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" > ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
			}
			
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			
			//FIN QUERY
			con = ds.getConnection();
			
			
//			System.out.println("sql : " + sbSql.toString()); 
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, "%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase())+"%"); //cargo la identificacion
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<DocenteTutorTribunalLectorJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
			
		} catch (SQLException e) {
			throw new DocenteTutorTribunalLectorJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.sql.exception")));
		} catch (Exception e) {
			throw new DocenteTutorTribunalLectorJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.por.identificacion.exception")));
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
			throw new DocenteTutorTribunalLectorJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.por.identificacion.no.result.exception")));
		}
		return retorno;
	}
	
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	/**
	 * Método que busca un docente por identificacion
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de docentes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws DocenteTutorTribunalLectorJdbcDtoJdbcDtoException 
	 * @throws DocenteTutorTribunalLectorJdbcDtoJdbcDtoNoEncontradoException 
	 */
	public DocenteTutorTribunalLectorJdbcDto buscarXIdentificacion(String identificacion) throws DocenteTutorTribunalLectorJdbcDtoException, DocenteTutorTribunalLectorJdbcDtoNoEncontradoException{
		DocenteTutorTribunalLectorJdbcDto retorno = null;
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
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO_SNIESE);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ETN_ID);
			
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_FECHA_NACIMIENTO);
			
			sbSql.append(" ,fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
//			sbSql.append(" ,fcdc.");sbSql.append(JdbcConstantes.FCDC_ESTADO);
//			sbSql.append(" ,fcdc.");sbSql.append(JdbcConstantes.FCDC_ABREVIATURA_TITULO);
//			sbSql.append(" ,fcdc.");sbSql.append(JdbcConstantes.FCDC_CRR_ID);


//			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
//			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
//			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
//			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);
//			
//			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_ID);
//			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
			
						
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
//			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
//			sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
//			sbSql.append(" AND fcdc.");sbSql.append(JdbcConstantes.FCDC_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
//			sbSql.append(" AND fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);
			
			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" ? ");
			
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			
			//FIN QUERY
			con = ds.getConnection();
			
			
//			System.out.println("SQL : " + sbSql.toString()  );
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase())); //cargo la identificacion
			
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADto(rs);
			}else{
				retorno = null;
			}
			
		} catch (SQLException e) {
			throw new DocenteTutorTribunalLectorJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.sql.exception")));
		} catch (Exception e) {
			throw new DocenteTutorTribunalLectorJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.por.identificacion.exception")));
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
			throw new DocenteTutorTribunalLectorJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.por.identificacion.no.result.exception")));
		}
		return retorno;
	}
	
	/**
	 * Método que busca un docente por identificacion
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de docentes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws DocenteTutorTribunalLectorJdbcDtoJdbcDtoException 
	 * @throws DocenteTutorTribunalLectorJdbcDtoJdbcDtoNoEncontradoException 
	 */
	public boolean buscarDocenteXMailInstitucional(String mail) throws DocenteTutorTribunalLectorJdbcDtoException, DocenteTutorTribunalLectorJdbcDtoNoEncontradoException{
		boolean retorno = false;
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
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO_SNIESE);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ETN_ID);
			
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_FECHA_NACIMIENTO);
			
			sbSql.append(" ,fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" ,fcdc.");sbSql.append(JdbcConstantes.FCDC_ESTADO);
			sbSql.append(" ,fcdc.");sbSql.append(JdbcConstantes.FCDC_ABREVIATURA_TITULO);
			sbSql.append(" ,fcdc.");sbSql.append(JdbcConstantes.FCDC_CRR_ID);


			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);
			
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
			
						
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc, ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr, ");
			sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ");
			
			sbSql.append(" WHERE ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			
			sbSql.append(" AND fcdc.");sbSql.append(JdbcConstantes.FCDC_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" AND fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);
			
			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);sbSql.append(" = ");sbSql.append(" ? ");
			
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			
			//FIN QUERY
			con = ds.getConnection();
			
			
//			System.out.println("SQL : " + sbSql.toString()  );
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, GeneralesUtilidades.eliminarEspaciosEnBlanco(mail)); //cargo la identificacion
			
			rs = pstmt.executeQuery();
			
		} catch (SQLException e) {
			throw new DocenteTutorTribunalLectorJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.sql.exception")));
		} catch (Exception e) {
			throw new DocenteTutorTribunalLectorJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.por.identificacion.exception")));
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
		if (rs == null){
			retorno = true;
		}
		
		return retorno;
	}
	
	/**
	 * Método que busca un docente por identificacion y devuelve el primer registro encontrado
	 * @param identificacion -identificacion del postulante que se requiere buscar.
	 * @return Lista de docentes que se ha encontrado con los parámetros ingresados en la búsqueda.
	 * @throws DocenteTutorTribunalLectorJdbcDtoJdbcDtoException 
	 * @throws DocenteTutorTribunalLectorJdbcDtoJdbcDtoNoEncontradoException 
	 */
	public Integer buscarFcdcIdXIdentificacion(String cedula) throws DocenteTutorTribunalLectorJdbcDtoException, DocenteTutorTribunalLectorJdbcDtoNoEncontradoException{
		Integer retorno = 0;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_ID);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);sbSql.append(" fcdc ");
			sbSql.append(" WHERE ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" fcdc.");sbSql.append(JdbcConstantes.FCDC_PRS_ID);
			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" ? ");
			sbSql.append(" LIMIT 1 ");
			
			//FIN QUERY
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, cedula); //cargo la identificacion
			
			rs = pstmt.executeQuery();
			
			while (rs.next()){
				retorno=rs.getInt(JdbcConstantes.FCDC_ID);
			}
			
		} catch (SQLException e) {
			throw new DocenteTutorTribunalLectorJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.sql.exception")));
		} catch (Exception e) {
			throw new DocenteTutorTribunalLectorJdbcDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("DocenteJdbcDto.buscar.por.identificacion.exception")));
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
		if (rs == null){
			retorno = 0;
		}
		
		return retorno;
	}
	
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */
	private DocenteTutorTribunalLectorJdbcDto transformarResultSetADto(ResultSet rs) throws SQLException{
		DocenteTutorTribunalLectorJdbcDto retorno = new DocenteTutorTribunalLectorJdbcDto();
		
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsTipoIdentificacion(rs.getInt(JdbcConstantes.PRS_TIPO_IDENTIFICACION));
		retorno.setPrsTipoIdentificacion(rs.getInt(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		
		retorno.setPrsSexo(rs.getInt(JdbcConstantes.PRS_SEXO));
		retorno.setPrsSexoSniese(rs.getInt(JdbcConstantes.PRS_SEXO_SNIESE));
		retorno.setPrsEtnia(rs.getInt(JdbcConstantes.PRS_ETN_ID));
		retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
		retorno.setPrsTelefono(rs.getString(JdbcConstantes.PRS_TELEFONO));
		retorno.setPrsFechaNacimiento(rs.getDate(JdbcConstantes.PRS_FECHA_NACIMIENTO));
		
		retorno.setFcdcId(rs.getInt(JdbcConstantes.FCDC_ID));
//		retorno.setFcdcEstado(rs.getInt(JdbcConstantes.FCDC_ESTADO));
//		retorno.setFcdcAbreviaturaTitulo(rs.getString(JdbcConstantes.FCDC_ABREVIATURA_TITULO));
//		retorno.setFcdcCrr_id(rs.getInt(JdbcConstantes.FCDC_CRR_ID));
//		
//		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
//		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
//		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
//		retorno.setCrrFcl_id(rs.getInt(JdbcConstantes.CRR_FCL_ID));
//		
//		retorno.setFclId(rs.getInt(JdbcConstantes.FCL_ID));
//		retorno.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
		
		return retorno;
	} 
	
	
	private DocenteTutorTribunalLectorJdbcDto transformarResultSetADtoDocentes(ResultSet rs) throws SQLException{
		DocenteTutorTribunalLectorJdbcDto retorno = new DocenteTutorTribunalLectorJdbcDto();
		
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsTipoIdentificacion(rs.getInt(JdbcConstantes.PRS_TIPO_IDENTIFICACION));
		retorno.setPrsTipoIdentificacion(rs.getInt(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		
		retorno.setPrsSexo(rs.getInt(JdbcConstantes.PRS_SEXO));
		retorno.setPrsSexoSniese(rs.getInt(JdbcConstantes.PRS_SEXO_SNIESE));
//		retorno.setPrsEtnia(rs.getInt(JdbcConstantes.PRS_ETN_ID));
//		retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
//		retorno.setPrsTelefono(rs.getString(JdbcConstantes.PRS_TELEFONO));
//		retorno.setPrsFechaNacimiento(rs.getDate(JdbcConstantes.PRS_FECHA_NACIMIENTO));
//		retorno.setDtpsId(rs.getInt("dtps_id"));
//		retorno.setCrhrId(rs.getInt("crhr_id"));
//		retorno.setFcdcEstado(rs.getInt(JdbcConstantes.FCDC_ESTADO));
//		retorno.setFcdcAbreviaturaTitulo(rs.getString(JdbcConstantes.FCDC_ABREVIATURA_TITULO));
//		retorno.setFcdcCrr_id(rs.getInt(JdbcConstantes.FCDC_CRR_ID));
//		
//		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
//		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
//		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
//		retorno.setCrrFcl_id(rs.getInt(JdbcConstantes.CRR_FCL_ID));
//		
//		retorno.setFclId(rs.getInt(JdbcConstantes.FCL_ID));
//		retorno.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
		
		return retorno;
	} 
	
	/**
	 * @throws SQLException 
	 * Inserta la nueva entidad indicada 
	 * @param  entidad - nueva entidad a insertar
	 * @return la nueva entidad insertada
	 * @throws  
	 */
	@Override
	public boolean guardarDocente(Persona persona, Integer carrera) throws SQLException{		
		boolean retorno = false;
		if (persona != null && carrera != null) {
			PreparedStatement pstmt = null;
			Connection con = null;
			ResultSet rs = null;
			StringBuilder sbSqlPersona = new StringBuilder();
			sbSqlPersona.append(" SELECT ");
			sbSqlPersona.append(" max(");sbSqlPersona.append(JdbcConstantes.PRS_ID);
			sbSqlPersona.append(") AS id FROM ");
			sbSqlPersona.append(JdbcConstantes.TABLA_PERSONA);
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSqlPersona.toString());
			
			rs = pstmt.executeQuery();
			Integer prsId=0;
			while (rs.next()){
				prsId=rs.getInt("id");
			}
			sbSqlPersona = new StringBuilder();
			sbSqlPersona.append(" INSERT INTO PERSONA (prs_id, prs_tipo_identificacion,prs_identificacion,prs_nombres, prs_primer_apellido, prs_segundo_apellido, prs_mail_institucional) VALUES( ");
			sbSqlPersona.append(" ? , ? , ? , ? , ? , ? , ? )");
			PreparedStatement pstmt2 = null;
			pstmt2 = con.prepareStatement(sbSqlPersona.toString());
			pstmt2.setInt(1, prsId+1);
			pstmt2.setInt(2, persona.getPrsTipoIdentificacion());
			pstmt2.setString(3, persona.getPrsIdentificacion());
			pstmt2.setString(4, GeneralesUtilidades.eliminarEspaciosEnBlanco(persona.getPrsNombres()).toUpperCase());
			pstmt2.setString(5, GeneralesUtilidades.eliminarEspaciosEnBlanco(persona.getPrsPrimerApellido()).toUpperCase());
			pstmt2.setString(6, GeneralesUtilidades.eliminarEspaciosEnBlanco(persona.getPrsSegundoApellido()).toUpperCase());
			pstmt2.setString(7, persona.getPrsMailInstitucional());
			
			pstmt2.executeUpdate();
			if (pstmt2 != null){
				pstmt2.close();
			}
				//**********************************************************************
				//********** Creo Nueva Pesona  ****************
				//**********************************************************************
				Persona nuevaPrs = new Persona();
				nuevaPrs = em.find(Persona.class, prsId+1);
//				nuevaPrs.setPrsTipoIdentificacion(persona.getPrsTipoIdentificacion());
//				nuevaPrs.setPrsIdentificacion(persona.getPrsIdentificacion());
//				nuevaPrs.setPrsNombres(GeneralesUtilidades.eliminarEspaciosEnBlanco(persona.getPrsNombres()).toUpperCase());
//				nuevaPrs.setPrsPrimerApellido(GeneralesUtilidades.eliminarEspaciosEnBlanco(persona.getPrsPrimerApellido()).toUpperCase());
//				nuevaPrs.setPrsSegundoApellido(GeneralesUtilidades.eliminarEspaciosEnBlanco(persona.getPrsSegundoApellido()).toUpperCase());
//				nuevaPrs.setPrsMailInstitucional(persona.getPrsMailInstitucional());
//				em.persist(nuevaPrs);
								
				//**********************************************************************
				//********** Creo Nueva Ficha Docente            ****************
				//**********************************************************************
				Carrera carreraAux = em.find(Carrera.class, carrera.intValue() );
				
				
				Integer fcdcId=0;
				try {
					StringBuilder sbSql = new StringBuilder();
					sbSql.append(" SELECT ");
					sbSql.append(" max(");sbSql.append(JdbcConstantes.FCDC_ID);
					sbSql.append(") AS id FROM ");
					sbSql.append(JdbcConstantes.TABLA_FICHA_DOCENTE);
					
					//FIN QUERY
					con = ds.getConnection();
					
					pstmt = con.prepareStatement(sbSql.toString());
					
					rs = pstmt.executeQuery();
					
					while (rs.next()){
						fcdcId=rs.getInt("id");
					}
					sbSql = new StringBuilder();
					sbSql.append(" INSERT INTO FICHA_DOCENTE (fcdc_id, fcdc_estado,crr_id, prs_id) VALUES( ");
					sbSql.append(fcdcId+1);sbSql.append(",");
					sbSql.append(FichaDocenteConstantes.ACTIVO_VALUE);sbSql.append(",");
					sbSql.append(carreraAux.getCrrId());sbSql.append(",");
					sbSql.append(nuevaPrs.getPrsId());sbSql.append(")");
					PreparedStatement pstmt1 = null;
					pstmt1 = con.prepareStatement(sbSql.toString());
					pstmt1.executeUpdate();
					if (pstmt1 != null){
						pstmt1.close();
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
		}
		return retorno;
	}
	
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */
	private DocenteTutorTribunalLectorJdbcDto transformarResultSetADtoListaDocentes(ResultSet rs) throws SQLException{
		DocenteTutorTribunalLectorJdbcDto retorno = new DocenteTutorTribunalLectorJdbcDto();
		
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsTipoIdentificacion(rs.getInt(JdbcConstantes.PRS_TIPO_IDENTIFICACION));
		retorno.setPrsTipoIdentificacion(rs.getInt(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		
		retorno.setPrsSexo(rs.getInt(JdbcConstantes.PRS_SEXO));
		retorno.setPrsSexoSniese(rs.getInt(JdbcConstantes.PRS_SEXO_SNIESE));
		retorno.setPrsEtnia(rs.getInt(JdbcConstantes.PRS_ETN_ID));
		retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
		retorno.setPrsTelefono(rs.getString(JdbcConstantes.PRS_TELEFONO));
		retorno.setPrsFechaNacimiento(rs.getDate(JdbcConstantes.PRS_FECHA_NACIMIENTO));
		
		
		return retorno;
	} 
	
	
}

