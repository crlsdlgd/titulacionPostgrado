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
   
 ARCHIVO:     MecanismoTitulacionDtoServicioJdbc.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de la tabla mecanismo_titulacion.
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

import ec.edu.uce.titulacionPosgrado.ejb.dtos.MecanismoTitulacionDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.MecanismoTitulacionDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.MecanismoTitulacionDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.MecanismoTitulacionDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

/**
 * EJB MecanismoTitulacionDtoServicioJdbc.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla mecanismo_titulacion.
 * @author dalbuja
 * @version 1.0
 */
@Stateless
public class MecanismoTitulacionDtoServicioJdbcImpl implements MecanismoTitulacionDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	/**
	 * Realiza la busqueda de un mecanismo de titulacion por id
	 * @param mecTitulId - id del mecanismo de titulacion
	 * @return Mecanismo de titulacion con el id solicitado 
	 * @throws MecanismoTitulacionDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MecanismoTitulacionDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public MecanismoTitulacionDto buscarXId(int mecTitulId) throws MecanismoTitulacionDtoJdbcException, MecanismoTitulacionDtoJdbcNoEncontradoException{
		MecanismoTitulacionDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
			sbSql.append(" , mctt.");sbSql.append(JdbcConstantes.MCTT_DESCRIPCION);
			sbSql.append(" , mctt.");sbSql.append(JdbcConstantes.MCTT_CODIGO_SNIESE);
			sbSql.append(" , mctt.");sbSql.append(JdbcConstantes.MCTT_ESTADO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION);sbSql.append(" mctt ");
			sbSql.append(" WHERE ");sbSql.append(" mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
			sbSql.append(" = ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, mecTitulId); //cargo el id del mecanismo de titulacion
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
			throw new MecanismoTitulacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MecanismoTitulacionJdbc.sql.exception")));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new MecanismoTitulacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MecanismoTitulacionJdbc.buscar.por.id.exception")));
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
			throw new MecanismoTitulacionDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MecanismoTitulacionJdbc.buscar.por.id.no.result.exception",mecTitulId)));
		}	
		
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todos los mecanismos de titulacion de la aplicacion
	 * @return Lista todos los mecanismos de titulacion de la aplicacion
	 * @throws MecanismoTitulacionDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MecanismoTitulacionDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MecanismoTitulacionDto> listarTodos() throws MecanismoTitulacionDtoJdbcException, MecanismoTitulacionDtoJdbcNoEncontradoException{
		List<MecanismoTitulacionDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" mctt.");sbSql.append(JdbcConstantes.MCTT_ID);
			sbSql.append(" , mctt.");sbSql.append(JdbcConstantes.MCTT_DESCRIPCION);
			sbSql.append(" , mctt.");sbSql.append(JdbcConstantes.MCTT_CODIGO_SNIESE);
			sbSql.append(" , mctt.");sbSql.append(JdbcConstantes.MCTT_ESTADO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION);sbSql.append(" mctt ");
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.MCTT_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MecanismoTitulacionDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new MecanismoTitulacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MecanismoTitulacionJdbc.sql.exception")));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new MecanismoTitulacionDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MecanismoTitulacionJdbc.buscar.todos.exception")));
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
			throw new MecanismoTitulacionDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MecanismoTitulacionJdbc.buscar.todos.no.result.exception")));
		}	
		return retorno;
	}

	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */
	private MecanismoTitulacionDto transformarResultSetADto(ResultSet rs) throws SQLException{
		MecanismoTitulacionDto retorno = new MecanismoTitulacionDto();
		retorno.setMcttId(rs.getInt(JdbcConstantes.MCTT_ID));
		retorno.setMcttDescripcion(rs.getString(JdbcConstantes.MCTT_DESCRIPCION));
		retorno.setMcttCodigoSniese(rs.getString(JdbcConstantes.MCTT_CODIGO_SNIESE));
		retorno.setMcttEstado(rs.getInt(JdbcConstantes.MCTT_ESTADO));
		return retorno;
	} 
	
}
