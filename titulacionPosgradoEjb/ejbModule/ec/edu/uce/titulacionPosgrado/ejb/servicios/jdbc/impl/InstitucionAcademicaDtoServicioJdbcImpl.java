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
   
 ARCHIVO:     InstitucionAcademicaDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de la tabla institucion_academica.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 26/02/2018				Daniel Albuja					       Emisión Inicial
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

import ec.edu.uce.titulacionPosgrado.ejb.dtos.InstitucionAcademicaDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.UbicacionDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.InstitucionAcademicaDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.InstitucionAcademicaDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.InstitucionAcademicaDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

/**
 * EJB InstitucionAcademicaDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla institucion_academica.
 * @author dalbuja
 * @version 1.0
 */
@Stateless
public class InstitucionAcademicaDtoServicioJdbcImpl implements InstitucionAcademicaDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	/**
	 * Realiza la busqueda de una institucion academica por id
	 * @param instAcademicaId - id del institucion academica
	 * @return Institucion academica con el id solicitado 
	 * @throws InstitucionAcademicaDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws InstitucionAcademicaDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public InstitucionAcademicaDto buscarXId(int instAcademicaId) throws InstitucionAcademicaDtoJdbcException, InstitucionAcademicaDtoJdbcNoEncontradoException{
		InstitucionAcademicaDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" inac.");sbSql.append(JdbcConstantes.INAC_ID);
			sbSql.append(" , inac.");sbSql.append(JdbcConstantes.INAC_CODIGO_SNIESE);
			sbSql.append(" , inac.");sbSql.append(JdbcConstantes.INAC_DESCRIPCION);
			sbSql.append(" , inac.");sbSql.append(JdbcConstantes.INAC_NIVEL);
			sbSql.append(" , inac.");sbSql.append(JdbcConstantes.INAC_TIPO);
			sbSql.append(" , inac.");sbSql.append(JdbcConstantes.INAC_TIPO_SNIESE);
			sbSql.append(" , inac.");sbSql.append(JdbcConstantes.INAC_UBC_ID);
			sbSql.append(" , ubc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_INSTITUCION_ACADEMICA);sbSql.append(" inac,  ");
								   sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubc ");
			sbSql.append(" WHERE ");sbSql.append(" inac.");sbSql.append(JdbcConstantes.INAC_UBC_ID);
									sbSql.append(" = ubc.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" AND inac.");sbSql.append(JdbcConstantes.INAC_ID);sbSql.append(" = ? ");
			
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, instAcademicaId); //cargo el id de institucion academica
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADto(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new InstitucionAcademicaDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("InstitucionAcademicaJdbc.sql.exception")));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new InstitucionAcademicaDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("InstitucionAcademicaJdbc.buscar.por.id.exception")));
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
			throw new InstitucionAcademicaDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("InstitucionAcademicaJdbc.buscar.por.id.no.result.exception",instAcademicaId)));
		}	
		
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todas las instituciones academicas de la aplicacion
	 * @return Lista todas las instituciones academicas de la aplicacion
	 * @throws InstitucionAcademicaDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws InstitucionAcademicaDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<InstitucionAcademicaDto> listarTodos() throws InstitucionAcademicaDtoJdbcException, InstitucionAcademicaDtoJdbcNoEncontradoException{
		List<InstitucionAcademicaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" inac.");sbSql.append(JdbcConstantes.INAC_ID);
			sbSql.append(" , inac.");sbSql.append(JdbcConstantes.INAC_CODIGO_SNIESE);
			sbSql.append(" , inac.");sbSql.append(JdbcConstantes.INAC_DESCRIPCION);
			sbSql.append(" , inac.");sbSql.append(JdbcConstantes.INAC_NIVEL);
			sbSql.append(" , inac.");sbSql.append(JdbcConstantes.INAC_TIPO);
			sbSql.append(" , inac.");sbSql.append(JdbcConstantes.INAC_TIPO_SNIESE);
			sbSql.append(" , inac.");sbSql.append(JdbcConstantes.INAC_UBC_ID);
			sbSql.append(" , ubc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_INSTITUCION_ACADEMICA);sbSql.append(" inac,  ");
								   sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubc ");
			sbSql.append(" WHERE ");sbSql.append(" inac.");sbSql.append(JdbcConstantes.INAC_UBC_ID);
									sbSql.append(" = ubc.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.INAC_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<InstitucionAcademicaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new InstitucionAcademicaDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("InstitucionAcademicaJdbc.sql.exception")));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new InstitucionAcademicaDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("InstitucionAcademicaJdbc.buscar.todos.exception")));
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
			throw new InstitucionAcademicaDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("InstitucionAcademicaJdbc.buscar.todos.no.result.exception")));
		}	
		return retorno;
	}

	/**
	 * Realiza la busqueda de las instituciones academicas por nivel, tipo y descripcion
	 * @param nivel - nivel de la institucion
	 * @param niveles- niveles de la institucion
	 * @param tipo - tipo de la institucion
	 * @param tipos - tipos de la institucion
	 * @param ubicacionId - id de la unicacion de la institucion
	 * @param ubicaciones - ubicaciones de la institucion
	 * @param descripcion - descripcion o nombre de la institucion
	 * @return list de las instituciones academicas por nivel, tipo y descripcion
	 * @throws InstitucionAcademicaDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws InstitucionAcademicaDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<InstitucionAcademicaDto> listarXNivelXTipoXUbicacionXDescripcion(int nivel, List<Integer> niveles, int tipo, List<Integer> tipos
			                             , int ubicacionId, List<UbicacionDto>  ubicaciones, String descripcion) throws InstitucionAcademicaDtoJdbcException, InstitucionAcademicaDtoJdbcNoEncontradoException{
		List<InstitucionAcademicaDto> retorno = null;
		List<Integer> listaNivelIn = new ArrayList<Integer>();
		List<Integer> listaTipoIn = new ArrayList<Integer>();
		List<Integer> listaUbicacionIn = new ArrayList<Integer>();
		//cargo los nivels para el IN
		if(nivel == GeneralesConstantes.APP_ID_BASE.intValue()){
			for (Integer item : niveles) {
				listaNivelIn.add(item);
			}
		}else{
			listaNivelIn.add(nivel);
		}		
		
		//cargo los tipos para el IN
		if(tipo == GeneralesConstantes.APP_ID_BASE.intValue()){
			for (Integer item : tipos) {
				listaTipoIn.add(item);
			}
		}else{
			listaTipoIn.add(tipo);
		}		
		
		//cargo las ubicaciones para el IN
		if(ubicacionId == GeneralesConstantes.APP_ID_BASE.intValue()){
			for (UbicacionDto item : ubicaciones) {
				listaUbicacionIn.add(item.getUbcId());
			}
		}else{
			listaUbicacionIn.add(ubicacionId);
		}	
		
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" inac.");sbSql.append(JdbcConstantes.INAC_ID);
			sbSql.append(" , inac.");sbSql.append(JdbcConstantes.INAC_CODIGO_SNIESE);
			sbSql.append(" , inac.");sbSql.append(JdbcConstantes.INAC_DESCRIPCION);
			sbSql.append(" , inac.");sbSql.append(JdbcConstantes.INAC_NIVEL);
			sbSql.append(" , inac.");sbSql.append(JdbcConstantes.INAC_TIPO);
			sbSql.append(" , inac.");sbSql.append(JdbcConstantes.INAC_TIPO_SNIESE);
			sbSql.append(" , inac.");sbSql.append(JdbcConstantes.INAC_UBC_ID);
			sbSql.append(" , ubc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_INSTITUCION_ACADEMICA);sbSql.append(" inac,  ");
								   sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubc ");
			sbSql.append(" WHERE ");sbSql.append(" inac.");sbSql.append(JdbcConstantes.INAC_UBC_ID);
									sbSql.append(" = ubc.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" AND ");sbSql.append(" inac.");sbSql.append(JdbcConstantes.INAC_NIVEL);sbSql.append(" IN ( ");
				for( int i = 0; i< listaNivelIn.size(); i++){
					sbSql.append(" ? ");
					if(i != listaNivelIn.size() -1) {
						sbSql.append(","); 
					}
				}
				sbSql.append(" ) ");
			sbSql.append(" AND inac.");sbSql.append(JdbcConstantes.INAC_TIPO);sbSql.append(" IN ( ");
				for( int i = 0; i< listaTipoIn.size(); i++){
					sbSql.append(" ? ");
					if(i != listaTipoIn.size() -1) {
						sbSql.append(","); 
					}
				}
				sbSql.append(" ) ");						
			sbSql.append(" AND inac.");sbSql.append(JdbcConstantes.INAC_UBC_ID);sbSql.append(" IN ( ");
				for( int i = 0; i< listaUbicacionIn.size(); i++){
					sbSql.append(" ? ");
					if(i != listaUbicacionIn.size() -1) {
						sbSql.append(","); 
					}
				}
				sbSql.append(" ) ");						
			sbSql.append(" AND UPPER(inac.");sbSql.append(JdbcConstantes.INAC_DESCRIPCION);sbSql.append(") LIKE ? ");
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.INAC_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			int contador = 0;
			for (Integer item : listaNivelIn) {
				contador++;
				pstmt.setInt(contador, item); 
			}
			for (Integer item : listaTipoIn) {
				contador++;
				pstmt.setInt(contador, item); 
			}
			for (Integer item : listaUbicacionIn) {
				contador++;
				pstmt.setInt(contador, item); 
			}
			contador++;
			pstmt.setString(contador, "%"+descripcion.toUpperCase()+"%");
			rs = pstmt.executeQuery();
			retorno = new ArrayList<InstitucionAcademicaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new InstitucionAcademicaDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("InstitucionAcademicaJdbc.sql.exception")));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new InstitucionAcademicaDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("InstitucionAcademicaJdbc.buscar.por.niv.tip.ubic.descr.exception")));
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
			throw new InstitucionAcademicaDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("InstitucionAcademicaJdbc.buscar.por.niv.tip.ubic.descr.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de una institucion academica por Nivel
	 * @param Nivel - Nivel de la institucion academica
	 * @return Lista de Institucion academica con el nivel solicitado
	 * @throws InstitucionAcademicaDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws InstitucionAcademicaDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<InstitucionAcademicaDto> listarXNivel(int nivel) throws InstitucionAcademicaDtoJdbcException, InstitucionAcademicaDtoJdbcNoEncontradoException{
		List<InstitucionAcademicaDto> retorno = new ArrayList<InstitucionAcademicaDto>();
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" inac.");sbSql.append(JdbcConstantes.INAC_ID);
			sbSql.append(" , inac.");sbSql.append(JdbcConstantes.INAC_CODIGO_SNIESE);
			sbSql.append(" , inac.");sbSql.append(JdbcConstantes.INAC_DESCRIPCION);
			sbSql.append(" , inac.");sbSql.append(JdbcConstantes.INAC_NIVEL);
			sbSql.append(" , inac.");sbSql.append(JdbcConstantes.INAC_TIPO);
			sbSql.append(" , inac.");sbSql.append(JdbcConstantes.INAC_TIPO_SNIESE);
			sbSql.append(" , inac.");sbSql.append(JdbcConstantes.INAC_UBC_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_INSTITUCION_ACADEMICA);sbSql.append(" inac ");
			sbSql.append(" WHERE ");sbSql.append(" inac.");sbSql.append(JdbcConstantes.INAC_NIVEL);
									sbSql.append(" = ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, nivel); //cargo el tipo de  la institucion academica
			rs = pstmt.executeQuery();
			while(rs.next()){
				retorno.add(transformarResultSetADtoSinUbicacion(rs));
			}
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new InstitucionAcademicaDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("InstitucionAcademicaJdbc.sql.exception")));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new InstitucionAcademicaDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("InstitucionAcademicaJdbc.buscar.por.id.exception")));
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
			throw new InstitucionAcademicaDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("InstitucionAcademicaJdbc.buscar.por.id.no.result.exception")));
		}	
		
		return retorno;
	}
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */
	private InstitucionAcademicaDto transformarResultSetADto(ResultSet rs) throws SQLException{
		InstitucionAcademicaDto retorno = new InstitucionAcademicaDto();
		retorno.setInacId(rs.getInt(JdbcConstantes.INAC_ID));
		retorno.setInacCodigoSniese(rs.getString(JdbcConstantes.INAC_CODIGO_SNIESE));
		retorno.setInacDescripcion(rs.getString(JdbcConstantes.INAC_DESCRIPCION));
		retorno.setInacNivel(rs.getInt(JdbcConstantes.INAC_NIVEL));
		retorno.setInacTipo(rs.getInt(JdbcConstantes.INAC_TIPO));
		retorno.setInacTipoSniese(rs.getString(JdbcConstantes.INAC_TIPO_SNIESE));
		retorno.setInacUbcId(rs.getInt(JdbcConstantes.INAC_UBC_ID));
		retorno.setInacUbcDescripcion(rs.getString(JdbcConstantes.UBC_DESCRIPCION));
		
		return retorno;
	} 
	private InstitucionAcademicaDto transformarResultSetADtoSinUbicacion(ResultSet rs) throws SQLException{
		InstitucionAcademicaDto retorno = new InstitucionAcademicaDto();
		retorno.setInacId(rs.getInt(JdbcConstantes.INAC_ID));
		retorno.setInacCodigoSniese(rs.getString(JdbcConstantes.INAC_CODIGO_SNIESE));
		retorno.setInacDescripcion(rs.getString(JdbcConstantes.INAC_DESCRIPCION));
		retorno.setInacNivel(rs.getInt(JdbcConstantes.INAC_NIVEL));
		retorno.setInacTipo(rs.getInt(JdbcConstantes.INAC_TIPO));
		retorno.setInacTipoSniese(rs.getString(JdbcConstantes.INAC_TIPO_SNIESE));
		retorno.setInacUbcId(rs.getInt(JdbcConstantes.INAC_UBC_ID));
		return retorno;
	} 
	
}
