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
   
 ARCHIVO:     PersonaDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de la tabla personaDto.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 12-MAY-2016		Gabriel Mafla					       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.sql.DataSource;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.PersonaDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.MigracionDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

/**
 * EJB PersonaDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla personaDto.
 * @author dcollaguazo
 * @version 1.0
 */
@Stateless
public class MigracionDtoServicioJdbcImpl implements MigracionDtoServicioJdbc {
	
	//@Resource(mappedName=GeneralesConstantes.APP_DATA_SOURCE_EMISION_TITULO)
	DataSource dsEmision;

//	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
//	private EntityManager em;
	
	
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) 
	public PersonaDto buscarXIdentificacionEmisionTitulo(String identificacion,Connection con)
			throws PersonaDtoJdbcException, PersonaDtoJdbcNoEncontradoException {
		PersonaDto retorno = null;
		PreparedStatement pstmt = null;
		ResultSet rs =  null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" , ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" , ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" , ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(" , ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(" , ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_SEXO);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" WHERE ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ? ");
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, identificacion);
			rs = pstmt.executeQuery();
			rs.next();
			retorno = new PersonaDto();
			retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if(rs!=null){
					rs.close();	
				}
				if(pstmt!=null){
					pstmt.close();	
				}
			} catch (Exception e2) {
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if(rs!=null){
					rs.close();	
				}
				if(pstmt!=null){
					pstmt.close();	
				}
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
			} catch (SQLException e) {
			}
		}
		
		return retorno;
	}
	
	

	@Override	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) 
	public Integer buscarFcesIdXPrsIdentificacion(String identificacion, Integer cncrId) throws Exception{
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_ID);
			
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
						
			sbSql.append(" WHERE ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ?");
			sbSql.append(" AND ");sbSql.append(" fces.");sbSql.append(JdbcConstantes.CNCR_ID);sbSql.append(" = ?");
			
			con = dsEmision.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, identificacion); //cargo el número de identificacion
			pstmt.setInt(2, cncrId); //cargo el número de identificacion
			rs = pstmt.executeQuery();
			if(rs.next()){
				return rs.getInt(JdbcConstantes.FCES_ID);
			}else{
				return 0;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
			
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
			
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
		
	}
	
}
