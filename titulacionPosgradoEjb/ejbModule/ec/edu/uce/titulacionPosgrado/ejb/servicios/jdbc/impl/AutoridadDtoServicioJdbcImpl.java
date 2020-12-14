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
   
 ARCHIVO:     AutoridadDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc 
 de los métodos requeridos para el examen complexivo
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 15-12-2016			Daniel Albuja				       Emisión Inicial
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
import javax.sql.DataSource;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.AutoridadDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.AutoridadException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.AutoridadNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.AutoridadDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.AutoridadConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.JdbcConstantes;

/**
 * EJB AutoridadDtoServicioJdbcImpl.
 * Clase Clase donde se implementan los metodos para el servicio jdbc 
 * de los métodos requeridos para el asentamiento de notas de otros mecanismos
 * @author dalbuja
 * @version 1.0
 */
@Stateless
public class AutoridadDtoServicioJdbcImpl implements AutoridadDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@Override
	public List<AutoridadDto> buscarAutoridades(Integer carreraId)
			throws AutoridadNoEncontradoException, AutoridadException {
		List<AutoridadDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			retorno= new ArrayList<AutoridadDto>();
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT  ");
			sbSql.append(" atr.");sbSql.append(JdbcConstantes.ATR_NOMBRES);sbSql.append(" ,"); 
			sbSql.append(" atr.");sbSql.append(JdbcConstantes.ATR_PRIMER_APELLIDO);sbSql.append(" ,");
			sbSql.append(" CASE WHEN atr.");sbSql.append(JdbcConstantes.ATR_SEGUNDO_APELLIDO);sbSql.append(" IS NULL ");
			sbSql.append(" THEN ");sbSql.append("'");sbSql.append(GeneralesConstantes.APP_ID_BASE);sbSql.append("'");
			sbSql.append(" ELSE atr."); sbSql.append(JdbcConstantes.ATR_SEGUNDO_APELLIDO);sbSql.append(" END AS ");
			sbSql.append(JdbcConstantes.ATR_SEGUNDO_APELLIDO);sbSql.append(" ,");
			sbSql.append(" atr.");sbSql.append(JdbcConstantes.ATR_SEXO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_AUTORIDAD);sbSql.append(" atr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ");
			sbSql.append(" WHERE atr.");sbSql.append(JdbcConstantes.ATR_FCL_ID);
			sbSql.append(" = fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" AND fcl.");
			sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);
			sbSql.append(" AND atr.");sbSql.append(JdbcConstantes.ATR_TIPO);sbSql.append(" = ");
			sbSql.append(AutoridadConstantes.DECANO_FACULTAD_VALUE);
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");
			sbSql.append(carreraId);
			sbSql.append(" AND atr.");sbSql.append(JdbcConstantes.ATR_ESTADO);sbSql.append(" = ");
			sbSql.append(AutoridadConstantes.ESTADO_ACTIVO_VALUE);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			rs.next();
			AutoridadDto autoridadDtoAuxDecano = new AutoridadDto();
			autoridadDtoAuxDecano.setAtrNombres(rs.getString(JdbcConstantes.ATR_NOMBRES));
			autoridadDtoAuxDecano.setAtrPrimerApellido(rs.getString(JdbcConstantes.ATR_PRIMER_APELLIDO));
			autoridadDtoAuxDecano.setAtrSegundoApellido(rs.getString(JdbcConstantes.ATR_SEGUNDO_APELLIDO));
			autoridadDtoAuxDecano.setAtrTipo(AutoridadConstantes.DECANO_FACULTAD_VALUE);
			autoridadDtoAuxDecano.setAtrSexo(rs.getInt(JdbcConstantes.ATR_SEXO));
			retorno.add(autoridadDtoAuxDecano);
			rs.close();
			pstmt.close();
			pstmt = null;
			rs = null;
			sbSql=new StringBuilder();
			sbSql.append(" SELECT  ");
			sbSql.append(" atr.");sbSql.append(JdbcConstantes.ATR_NOMBRES);sbSql.append(" ,"); 
			sbSql.append(" atr.");sbSql.append(JdbcConstantes.ATR_PRIMER_APELLIDO);sbSql.append(" ,");
			sbSql.append(" CASE WHEN atr.");sbSql.append(JdbcConstantes.ATR_SEGUNDO_APELLIDO);sbSql.append(" IS NULL ");
			sbSql.append(" THEN ");sbSql.append("'");sbSql.append(GeneralesConstantes.APP_ID_BASE);sbSql.append("'");
			sbSql.append(" ELSE atr."); sbSql.append(JdbcConstantes.ATR_SEGUNDO_APELLIDO);sbSql.append(" END AS ");
			sbSql.append(JdbcConstantes.ATR_SEGUNDO_APELLIDO);sbSql.append(" ,");
			sbSql.append(" atr.");sbSql.append(JdbcConstantes.ATR_SEXO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_AUTORIDAD);sbSql.append(" atr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ");
			sbSql.append(" WHERE atr.");sbSql.append(JdbcConstantes.ATR_FCL_ID);
			sbSql.append(" = fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" AND fcl.");
			sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);
			sbSql.append(" AND atr.");sbSql.append(JdbcConstantes.ATR_TIPO);sbSql.append(" = ");
			sbSql.append(AutoridadConstantes.SUBDECANO_FACULTAD_VALUE);
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");
			sbSql.append(carreraId);
			sbSql.append(" AND atr.");sbSql.append(JdbcConstantes.ATR_ESTADO);sbSql.append(" = ");
			sbSql.append(AutoridadConstantes.ESTADO_ACTIVO_VALUE);
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			rs.next();
			AutoridadDto autoridadDtoAuxSubDecano = new AutoridadDto();
			autoridadDtoAuxSubDecano.setAtrNombres(rs.getString(JdbcConstantes.ATR_NOMBRES));
			autoridadDtoAuxSubDecano.setAtrPrimerApellido(rs.getString(JdbcConstantes.ATR_PRIMER_APELLIDO));
			autoridadDtoAuxSubDecano.setAtrSegundoApellido(rs.getString(JdbcConstantes.ATR_SEGUNDO_APELLIDO));
			autoridadDtoAuxSubDecano.setAtrTipo(AutoridadConstantes.SUBDECANO_FACULTAD_VALUE);
			autoridadDtoAuxSubDecano.setAtrSexo(rs.getInt(JdbcConstantes.ATR_SEXO));
			retorno.add(autoridadDtoAuxSubDecano);
			rs.close();
			pstmt.close();
			pstmt = null;
			rs = null;
			sbSql=new StringBuilder();
			sbSql.append(" SELECT  ");
			sbSql.append(" atr.");sbSql.append(JdbcConstantes.ATR_NOMBRES);sbSql.append(" ,"); 
			sbSql.append(" atr.");sbSql.append(JdbcConstantes.ATR_PRIMER_APELLIDO);sbSql.append(" ,");
			sbSql.append(" CASE WHEN atr.");sbSql.append(JdbcConstantes.ATR_SEGUNDO_APELLIDO);sbSql.append(" IS NULL ");
			sbSql.append(" THEN ");sbSql.append("'");sbSql.append(GeneralesConstantes.APP_ID_BASE);sbSql.append("'");
			sbSql.append(" ELSE atr."); sbSql.append(JdbcConstantes.ATR_SEGUNDO_APELLIDO);sbSql.append(" END AS ");
			sbSql.append(JdbcConstantes.ATR_SEGUNDO_APELLIDO);sbSql.append(" ,");
			sbSql.append(" atr.");sbSql.append(JdbcConstantes.ATR_SEXO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_AUTORIDAD);sbSql.append(" atr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ");
			sbSql.append(" WHERE atr.");sbSql.append(JdbcConstantes.ATR_FCL_ID);
			sbSql.append(" = fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" AND fcl.");
			sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);
			sbSql.append(" AND atr.");sbSql.append(JdbcConstantes.ATR_TIPO);sbSql.append(" = ");
			sbSql.append(AutoridadConstantes.SECRETARIO_FACULTAD_VALUE);
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");
			sbSql.append(carreraId);
			sbSql.append(" AND atr.");sbSql.append(JdbcConstantes.ATR_ESTADO);sbSql.append(" = ");
			sbSql.append(AutoridadConstantes.ESTADO_ACTIVO_VALUE);
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			rs.next();
			AutoridadDto autoridadDtoAuxSecretario = new AutoridadDto();
			autoridadDtoAuxSecretario.setAtrNombres(rs.getString(JdbcConstantes.ATR_NOMBRES));
			autoridadDtoAuxSecretario.setAtrPrimerApellido(rs.getString(JdbcConstantes.ATR_PRIMER_APELLIDO));
			autoridadDtoAuxSecretario.setAtrSegundoApellido(rs.getString(JdbcConstantes.ATR_SEGUNDO_APELLIDO));
			autoridadDtoAuxSecretario.setAtrTipo(AutoridadConstantes.SECRETARIO_FACULTAD_VALUE);
			autoridadDtoAuxSecretario.setAtrSexo(rs.getInt(JdbcConstantes.ATR_SEXO));
			retorno.add(autoridadDtoAuxSecretario);
		} catch (Exception e) {
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
	
	
}
