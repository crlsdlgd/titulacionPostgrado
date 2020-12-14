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
   
 ARCHIVO:     UbicacionDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de la tabla ubicacion.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 21/02/2018				Daniel Albuja					       Emisión Inicial
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

import ec.edu.uce.titulacionPosgrado.ejb.dtos.UbicacionDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UbicacionDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UbicacionDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.UbicacionDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UbicacionConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

/**
 * EJB UbicacionDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla ubicacion.
 * @author dalbuja
 * @version 1.0
 */
@Stateless
public class UbicacionDtoServicioJdbcImpl implements UbicacionDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	/**
	 * Realiza la busqueda de un ubicacion por id
	 * @param ubicacionId - id del ubicacion
	 * @return Ubicacion con el id solicitado 
	 * @throws UbicacionDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UbicacionDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public UbicacionDto buscarXId(int ubicacionId) throws UbicacionDtoJdbcException, UbicacionDtoJdbcNoEncontradoException{
		UbicacionDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_ID);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_JERARQUIA);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_GENTILICIO);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_COD_SNIESE);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_PADRE);sbSql.append(" , ");
			sbSql.append(" CASE WHEN ");sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
					sbSql.append(" IS NULL THEN ");sbSql.append(" '---' ");sbSql.append(" ELSE ");
					sbSql.append(" ubcPrv.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" END as padre_descripcion") ;
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubc ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubcPrv ");sbSql.append(" ON ");
			sbSql.append("ubc.");sbSql.append(JdbcConstantes.UBC_PADRE);sbSql.append(" = ");sbSql.append("ubcPrv.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" WHERE ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" = ? ");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, ubicacionId); //cargo el id de la ubicacion
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
			throw new UbicacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.sql.exception")));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new UbicacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.por.id.exception")));
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
			throw new UbicacionDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.por.id.no.result.exception",ubicacionId)));
		}	
		
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todos los ubicaciones de la aplicacion
	 * @return Lista todos las ubicaciones de la aplicacion
	 * @throws UbicacionDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UbicacionDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<UbicacionDto> listarTodos() throws UbicacionDtoJdbcException, UbicacionDtoJdbcNoEncontradoException{
		List<UbicacionDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_ID);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_JERARQUIA);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_GENTILICIO);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_COD_SNIESE);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_PADRE);sbSql.append(" , ");
			sbSql.append(" CASE WHEN ");sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
			sbSql.append(" IS NULL THEN ");sbSql.append(" '---' ");sbSql.append(" ELSE ");
			sbSql.append(" ubcPrv.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" END as padre_descripcion") ;
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubc ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubcPrv ");sbSql.append(" ON ");
			sbSql.append("ubc.");sbSql.append(JdbcConstantes.UBC_PADRE);sbSql.append(" = ");sbSql.append("ubcPrv.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" ORDER BY ");sbSql.append("ubc."); sbSql.append(JdbcConstantes.UBC_JERARQUIA);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<UbicacionDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new UbicacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.sql.exception")));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new UbicacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.todos.exception")));
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
			throw new UbicacionDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.todos.no.result.exception")));
		}	
		return retorno;
	}

	
	
	/**
	 * Realiza la busqueda de un ubicacion de pais por id
	 * @param ubicacionId - id del ubicacion de pais
	 * @return Ubicacion pais con el id solicitado 
	 * @throws UbicacionDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UbicacionDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public UbicacionDto buscarPaisXId(int ubicacionId) throws UbicacionDtoJdbcException, UbicacionDtoJdbcNoEncontradoException{
		UbicacionDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_ID);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_JERARQUIA);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_GENTILICIO);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_COD_SNIESE);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_PADRE);sbSql.append(" , ");
			sbSql.append(" CASE WHEN ");sbSql.append(" ubcPdr.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
				sbSql.append(" IS NULL THEN ");sbSql.append(" '---' ");sbSql.append(" ELSE ");
				sbSql.append(" ubcPdr.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" END as padre_descripcion ");
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubc ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubcPdr ");sbSql.append(" ON ");
			sbSql.append("ubc.");sbSql.append(JdbcConstantes.UBC_PADRE);sbSql.append(" = ");sbSql.append("ubcPdr.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" WHERE ");
			sbSql.append("ubc.");sbSql.append(JdbcConstantes.UBC_JERARQUIA);sbSql.append(" = ");sbSql.append(UbicacionConstantes.TIPO_JERARQUIA_PAIS_VALUE);
			sbSql.append(" AND ubc.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" = ? ");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, ubicacionId); //cargo el id de la ubicacion
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
			throw new UbicacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.sql.exception")));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new UbicacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.pais.por.id.exception")));
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
			throw new UbicacionDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.pais.por.id.no.result.exception",ubicacionId)));
		}	
		
		return retorno;
	}

	
	
	/**
	 * Realiza la busqueda de una ubicacion de provincia por id de pais
	 * @param ubicacionId - id del ubicacion de pais
	 * @return Ubicacion pais con el id solicitado 
	 * @throws UbicacionDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UbicacionDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<UbicacionDto> listaProvinciaXPais(int ubicacionId) throws UbicacionDtoJdbcException, UbicacionDtoJdbcNoEncontradoException{
		List<UbicacionDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_ID);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_JERARQUIA);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_GENTILICIO);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_COD_SNIESE);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_PADRE);sbSql.append(" , ");
			sbSql.append(" CASE WHEN ");sbSql.append(" ubcPadre.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
			sbSql.append(" IS NULL THEN ");sbSql.append(" '' ");sbSql.append(" ELSE ");
			sbSql.append(" ubcPadre.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" END as padre_descripcion");
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubc ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubcPadre ");sbSql.append(" ON ");
			sbSql.append("ubc.");sbSql.append(JdbcConstantes.UBC_PADRE);sbSql.append(" = ");sbSql.append("ubcPadre.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" WHERE ");
			sbSql.append("ubc.");sbSql.append(JdbcConstantes.UBC_JERARQUIA);sbSql.append(" = ");sbSql.append(UbicacionConstantes.TIPO_JERARQUIA_PROVINCIA_VALUE);
			sbSql.append(" AND ubc.");sbSql.append(JdbcConstantes.UBC_PADRE);
			sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");sbSql.append("ubc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, ubicacionId); //cargo el id de la ubicacion
			rs = pstmt.executeQuery();
			retorno = new ArrayList<UbicacionDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new UbicacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.sql.exception")));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new UbicacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.listar.provincia.por.pais.exeption")));
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
			throw new UbicacionDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.listar.provincia.por.pais.no.result.exception")));
		}	
		return retorno;
	}

	/**
	 * Realiza la busqueda de una ubicacion de canton por id de provincia 
	 * @param ubicacionId - id del ubicacion de provincia 
	 * @return Ubicacion canton con el id solicitado 
	 * @throws UbicacionDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UbicacionDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<UbicacionDto> listaCatonXProvincia(int ubicacionId) throws UbicacionDtoJdbcException, UbicacionDtoJdbcNoEncontradoException{
		List<UbicacionDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_ID);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_JERARQUIA);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_GENTILICIO);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_COD_SNIESE);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_PADRE);sbSql.append(" , ");
			sbSql.append(" CASE WHEN ");sbSql.append(" ubcPadre.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
			sbSql.append(" IS NULL THEN ");sbSql.append(" '' ");sbSql.append(" ELSE ");
			sbSql.append(" ubcPadre.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" END as padre_descripcion ");
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubc ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubcPadre ");sbSql.append(" ON ");
			sbSql.append("ubc.");sbSql.append(JdbcConstantes.UBC_PADRE);sbSql.append(" = ");sbSql.append("ubcPadre.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" WHERE ");
			sbSql.append("ubc.");sbSql.append(JdbcConstantes.UBC_JERARQUIA);sbSql.append(" = ");sbSql.append(UbicacionConstantes.TIPO_JERARQUIA_CANTON_VALUE);
			sbSql.append(" AND ubc.");sbSql.append(JdbcConstantes.UBC_PADRE);
			sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");sbSql.append("ubc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, ubicacionId); //cargo el id de la ubicacion
			rs = pstmt.executeQuery();
			retorno = new ArrayList<UbicacionDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new UbicacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.listar.ubicaciones.por.carrea.sql.exception")));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new UbicacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.listar.ubicaciones.por.carrea.buscar.ubicaciones.por.carrera.exeption")));
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
			throw new UbicacionDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.listar.ubicaciones.por.carrea.no.result.exception")));
		}	
		return retorno;
	}
	
	
	
	/**
	 * Realiza la busqueda de un ubicacion de pais por id
	 * @param ubicacionId - id del ubicacion de pais
	 * @return Ubicacion pais con el id solicitado 
	 * @throws UbicacionDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UbicacionDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public UbicacionDto buscarPadreXId(int ubicacionId) throws UbicacionDtoJdbcException, UbicacionDtoJdbcNoEncontradoException{
		UbicacionDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT ");
			sbSql.append(" ubc2.");sbSql.append(JdbcConstantes.UBC_ID);sbSql.append(" , ");
			sbSql.append(" ubc2.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" , ");
			sbSql.append(" ubc2.");sbSql.append(JdbcConstantes.UBC_JERARQUIA);sbSql.append(" , ");
			sbSql.append(" ubc2.");sbSql.append(JdbcConstantes.UBC_GENTILICIO);sbSql.append(" , ");
			sbSql.append(" ubc2.");sbSql.append(JdbcConstantes.UBC_COD_SNIESE);sbSql.append(" , ");
			sbSql.append(" ubc2.");sbSql.append(JdbcConstantes.UBC_PADRE);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubc1 , ");
			sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubc2 ");
			sbSql.append(" WHERE ");
			sbSql.append("ubc1.");sbSql.append(JdbcConstantes.UBC_PADRE);sbSql.append(" = ");sbSql.append("ubc2.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" AND ubc1.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" = ? ");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, ubicacionId); //cargo el id de la ubicacion
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADtoSoloUbicacion(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new UbicacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.sql.exception")));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new UbicacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.por.id.exception")));
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
			throw new UbicacionDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.por.id.no.result.exception",ubicacionId)));
		}	
		
		return retorno;
	}
	
	
	/**
	 * Realiza la busqueda de las ubicaciones por jerarquia
	 * @return Lista todos las ubicaciones de la aplicacion por jerarquia
	 * @throws UbicacionDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UbicacionDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<UbicacionDto> listarXjerarquia(int idJerarquia) throws UbicacionDtoJdbcException, UbicacionDtoJdbcNoEncontradoException{
		List<UbicacionDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_ID);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_JERARQUIA);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_GENTILICIO);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_COD_SNIESE);sbSql.append(" , ");
			sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_PADRE);sbSql.append(" , ");
			sbSql.append(" CASE WHEN ");sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
			sbSql.append(" IS NULL THEN ");sbSql.append(" '---' ");sbSql.append(" ELSE ");
			sbSql.append(" ubcPrv.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(" END as padre_descripcion") ;
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubc ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubcPrv ");sbSql.append(" ON ");
			sbSql.append("ubc.");sbSql.append(JdbcConstantes.UBC_PADRE);sbSql.append(" = ");sbSql.append("ubcPrv.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" WHERE ");sbSql.append(" ubc.");sbSql.append(JdbcConstantes.UBC_JERARQUIA);sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");sbSql.append("ubc."); sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idJerarquia); //cargo el id de la jerarquia
			rs = pstmt.executeQuery();
			retorno = new ArrayList<UbicacionDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new UbicacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.sql.exception")));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new UbicacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.todos.exception")));
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
			throw new UbicacionDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UbicacionJdbc.buscar.todos.no.result.exception")));
		}	
		return retorno;
	}
	
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */
	private UbicacionDto transformarResultSetADto(ResultSet rs) throws SQLException{
		UbicacionDto retorno = new UbicacionDto();
		retorno.setUbcId(rs.getInt(JdbcConstantes.UBC_ID));
		retorno.setUbcDescripcion(rs.getString(JdbcConstantes.UBC_DESCRIPCION));
		retorno.setUbcJerarquia(rs.getInt(JdbcConstantes.UBC_JERARQUIA));
		retorno.setUbcGentilicio(rs.getString(JdbcConstantes.UBC_GENTILICIO));
		retorno.setUbcCodSniese(rs.getString(JdbcConstantes.UBC_COD_SNIESE));
		retorno.setUbcPadre(rs.getInt(JdbcConstantes.UBC_PADRE));
		retorno.setUbcPadreDescripcion(rs.getString("padre_descripcion"));
		return retorno;
	}
	
	private UbicacionDto transformarResultSetADtoSoloUbicacion(ResultSet rs) throws SQLException{
		UbicacionDto retorno = new UbicacionDto();
		retorno.setUbcId(rs.getInt(JdbcConstantes.UBC_ID));
		retorno.setUbcDescripcion(rs.getString(JdbcConstantes.UBC_DESCRIPCION));
		retorno.setUbcJerarquia(rs.getInt(JdbcConstantes.UBC_JERARQUIA));
		retorno.setUbcGentilicio(rs.getString(JdbcConstantes.UBC_GENTILICIO));
		retorno.setUbcCodSniese(rs.getString(JdbcConstantes.UBC_COD_SNIESE));
		retorno.setUbcPadre(rs.getInt(JdbcConstantes.UBC_PADRE));
		return retorno;
	}
	
	
}
