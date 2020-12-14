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
   
 ARCHIVO:     FacultadDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de la tabla facultad.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 26/02/2018		Daniel Albuja					       Emisión Inicial
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

import ec.edu.uce.titulacionPosgrado.ejb.dtos.FacultadDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FacultadDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FacultadDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.FacultadDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

/**
 * EJB FacultadDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla facultad.
 * @author dalbuja
 * @version 1.0
 */
@Stateless
public class FacultadDtoServicioJdbcImpl implements FacultadDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	/**
	 * Realiza la busqueda de una Facultad por id
	 * @param facultadId - id de la facultad
	 * @return FacultadDto con el id solicitado 
	 * @throws FacultadDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws FacultadDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public FacultadDto buscarXId(int facultadId) throws FacultadDtoJdbcException, FacultadDtoJdbcNoEncontradoException{
		FacultadDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" , ");
			sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);sbSql.append(" , ");
			sbSql.append(" CASE WHEN ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
					sbSql.append(" IS NULL THEN ");sbSql.append(" '---' ");sbSql.append(" ELSE ");
					sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);sbSql.append(" END as facultad_descripcion") ;
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ");
			sbSql.append(" WHERE ");
			sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" = ? ");
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, facultadId); //cargo el id de la facultad
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
			throw new FacultadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FacultadDto.buscar.por.id.sql.exception")));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new FacultadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FacultadDto.buscar.por.id.exception")));
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
			throw new FacultadDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Facultad.buscar.por.id.no.result.exception",facultadId)));
		}	
		
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todas las facultades de la aplicacion
	 * @return Lista todos las facultades  de la aplicacion
	 * @throws FacultadDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws FacultadDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<FacultadDto> listarTodos() throws FacultadDtoJdbcException, FacultadDtoJdbcNoEncontradoException{
		List<FacultadDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" , ");
			sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);sbSql.append(" , ");
			sbSql.append(" CASE WHEN ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
					sbSql.append(" IS NULL THEN ");sbSql.append(" '---' ");sbSql.append(" ELSE ");
					sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);sbSql.append(" END as facultad_descripcion") ;
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ");
			sbSql.append(" ORDER BY ");sbSql.append("fcl."); sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<FacultadDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new FacultadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FacultadDto.buscar.todos.sql.exception")));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new FacultadDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FacultadDto.buscar.todos.exception" )));
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
			throw new FacultadDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FacultadDto.buscar.todos.no.result.exception")));
		}	
		return retorno;
	}

	
	
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */
	private FacultadDto transformarResultSetADto(ResultSet rs) throws SQLException{
		FacultadDto retorno = new FacultadDto();
		retorno.setFclId(rs.getInt(JdbcConstantes.FCL_ID));
		retorno.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
		return retorno;
	}
	
	
}
