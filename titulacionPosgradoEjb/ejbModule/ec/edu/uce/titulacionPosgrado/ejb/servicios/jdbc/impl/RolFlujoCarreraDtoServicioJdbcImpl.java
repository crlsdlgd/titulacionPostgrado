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
   
 ARCHIVO:     RolFlujoCarreraDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de la tabla titulo.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 05-11-2019		Daniel Albuja				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.RolDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RolException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RolNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.RolFlujoCarreraDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

/**
 * EJB RolFlujoCarreraDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla RolFlujoCarrera.
 * @author dalbuja
 * @version 1.0
 */
@Stateless
public class RolFlujoCarreraDtoServicioJdbcImpl implements RolFlujoCarreraDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	@Override
	public RolDto buscarRolXCedula(String cedula) throws RolException,
			RolNoEncontradoException {
		RolDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" , rol.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ");
			sbSql.append(" WHERE ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = ");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);sbSql.append(" = ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_IDENTIFICACION);sbSql.append(" = ?");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, cedula); //cargo la cedula
			rs = pstmt.executeQuery();
			rs.next();
			retorno = transformarResultSetADto(rs);
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
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
			throw new RolNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TituloJdbc.buscar.por.id.no.result.exception",cedula )));
		}	
		return retorno;
	} 
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */
	private RolDto transformarResultSetADto(ResultSet rs) throws SQLException{
		RolDto retorno = new RolDto();
		retorno.setRolId(rs.getInt(JdbcConstantes.ROL_ID));
		retorno.setRolDescripcion(rs.getString(JdbcConstantes.ROL_DESCRIPCION));
		return retorno;
	}

	
	
}
