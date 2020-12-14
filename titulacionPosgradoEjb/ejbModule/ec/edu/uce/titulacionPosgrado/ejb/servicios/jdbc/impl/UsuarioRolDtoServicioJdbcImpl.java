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
   
 ARCHIVO:     UsuarioRolDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de la tabla usuario_rol.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 23-04-2018			Daniel Albuja					       Emisión Inicial
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

import ec.edu.uce.titulacionPosgrado.ejb.dtos.UsuarioRolJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolJdbcDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolJdbcDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.UsuarioRolDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UsuarioConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;


/**
 * EJB UsuarioRolDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla ubicacion.
 * @author dalbuja
 * @version 1.0
 */
@Stateless
public class UsuarioRolDtoServicioJdbcImpl implements UsuarioRolDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	public List<UsuarioRolJdbcDto> buscarXIdentificacionXFacultadXCarrea(
			String identificacion, int facultadId, int carreraId)
			throws UsuarioRolJdbcDtoException,
			UsuarioRolJdbcDtoNoEncontradoException {
		List<UsuarioRolJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			
			sbSql.append(" SELECT ");
			//**** CAMPOS DE PERSONA ****/
			sbSql.append("distinct("); sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append("),");
			sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(",");
			sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);sbSql.append(",");
			sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(",");
			sbSql.append(" CASE WHEN ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
						sbSql.append(" END  ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(",");
			sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(",");
			sbSql.append(JdbcConstantes.PRS_SEXO);sbSql.append(",");
			sbSql.append(JdbcConstantes.PRS_SEXO_SNIESE);sbSql.append(",");
			sbSql.append(" CASE WHEN ");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE ");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL); 
						sbSql.append(" END  ");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);sbSql.append(",");
			sbSql.append(" CASE WHEN ");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE ");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); 
						sbSql.append(" END  ");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);sbSql.append(",");
			sbSql.append(JdbcConstantes.PRS_TELEFONO);sbSql.append(",");
			sbSql.append(JdbcConstantes.PRS_FECHA_NACIMIENTO);sbSql.append(",");
			sbSql.append("prsEtn");sbSql.append(",");
			sbSql.append("prsUbc");sbSql.append(",");
			//**** CAMPOS DE ETNIA ****/
			sbSql.append(JdbcConstantes.ETN_ID);sbSql.append(",");
			sbSql.append(JdbcConstantes.ETN_DESCRIPCION);sbSql.append(",");
			sbSql.append(JdbcConstantes.ETN_CODIGO_SNIESE);sbSql.append(",");
			//**** CAMPOS DE UBICACION NACIONALIDAD ****/
			sbSql.append(JdbcConstantes.UBC_ID);sbSql.append(",");
			sbSql.append(JdbcConstantes.UBC_DESCRIPCION);sbSql.append(",");
			sbSql.append(JdbcConstantes.UBC_JERARQUIA);sbSql.append(",");
			sbSql.append(JdbcConstantes.UBC_GENTILICIO);sbSql.append(",");
			sbSql.append(JdbcConstantes.UBC_COD_SNIESE);sbSql.append(",");
			//**** CAMPOS DE CARRERA ****/
//			sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(",");
////			sbSql.append(JdbcConstantes.CRR_COD_SNIESE);sbSql.append(",");
////			sbSql.append(JdbcConstantes.CRR_DESCRIPCION);sbSql.append(",");
//			sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(",");
////			sbSql.append("crrfcl");sbSql.append(",");
////			//**** CAMPOS DE FACULTAD ****/
//			sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(",");
//			sbSql.append(JdbcConstantes.FCL_DESCRIPCION);sbSql.append(",");
			//**** CAMPOS DE USUARIO ****/
			sbSql.append(JdbcConstantes.USR_ID);sbSql.append(",");
			sbSql.append(JdbcConstantes.USR_IDENTIFICACION);sbSql.append(",");
			sbSql.append(JdbcConstantes.USR_NICK);sbSql.append(",");
			sbSql.append(JdbcConstantes.USR_FECHA_CREACION);sbSql.append(",");
			sbSql.append(JdbcConstantes.USR_FECHA_CADUCIDAD);sbSql.append(",");
			sbSql.append(JdbcConstantes.USR_FECHA_CAD_PASS);sbSql.append(",");
			sbSql.append(JdbcConstantes.USR_ESTADO);sbSql.append(",");
			sbSql.append(JdbcConstantes.USR_EST_SESION);sbSql.append(",");
			sbSql.append(JdbcConstantes.USR_ACTIVE_DIRECTORY);sbSql.append(",");
			//**** CAMPOS DE USUARIO_ROL ****/
			sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(",");
			sbSql.append(JdbcConstantes.USRO_ESTADO);sbSql.append(",");
			//**** CAMPOS DE ROL ****/
			sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(",");
			sbSql.append(JdbcConstantes.ROL_DESCRIPCION);sbSql.append(",");
			sbSql.append(JdbcConstantes.ROL_TIPO);
//			sbSql.append(",");
			//**** CAMPOS DE ROL_FLUJO_CARRERA ****/
//			sbSql.append(JdbcConstantes.ROFLCR_ID);sbSql.append(",");
//			sbSql.append(JdbcConstantes.ROFLCR_ESTADO);
			
			sbSql.append(" FROM ( ");
			
			
			sbSql.append(" SELECT ");
			//**** CAMPOS DE PERSONA ****/
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
						sbSql.append(" END  ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEXO_SNIESE);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL); 
						sbSql.append(" END  ");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);
			sbSql.append(" , CASE WHEN prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE prs.");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL); 
						sbSql.append(" END  ");sbSql.append(JdbcConstantes.PRS_MAIL_INSTITUCIONAL);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_FECHA_NACIMIENTO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_ETN_ID);sbSql.append("  prsEtn");
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_UBC_ID);sbSql.append("  prsUbc");
			//**** CAMPOS DE ETNIA ****/
			sbSql.append(" ,etn.");sbSql.append(JdbcConstantes.ETN_ID);
			sbSql.append(" ,etn.");sbSql.append(JdbcConstantes.ETN_DESCRIPCION);
			sbSql.append(" ,etn.");sbSql.append(JdbcConstantes.ETN_CODIGO_SNIESE);
			//**** CAMPOS DE UBICACION NACIONALIDAD ****/
			sbSql.append(" ,ubcNacionalidad.");sbSql.append(JdbcConstantes.UBC_ID);
			sbSql.append(" ,ubcNacionalidad.");sbSql.append(JdbcConstantes.UBC_DESCRIPCION);
			sbSql.append(" ,ubcNacionalidad.");sbSql.append(JdbcConstantes.UBC_JERARQUIA);
			sbSql.append(" ,ubcNacionalidad.");sbSql.append(JdbcConstantes.UBC_GENTILICIO);
			sbSql.append(" ,ubcNacionalidad.");sbSql.append(JdbcConstantes.UBC_COD_SNIESE);
			//**** CAMPOS DE CARRERA ****/
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append("  crrfcl");
			//**** CAMPOS DE FACULTAD ****/
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
			//**** CAMPOS DE USUARIO ****/
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_IDENTIFICACION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_NICK);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CREACION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CADUCIDAD);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CAD_PASS);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ESTADO);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_EST_SESION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ACTIVE_DIRECTORY);
			//**** CAMPOS DE USUARIO_ROL ****/
			sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ESTADO);
			//**** CAMPOS DE ROL ****/
			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);
			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_TIPO);
			//**** CAMPOS DE ROL_FLUJO_CARRERA ****/
			sbSql.append(" ,roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ID);
			sbSql.append(" ,roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_ETNIA);sbSql.append(" etn ON ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ETN_ID);sbSql.append(" = ");sbSql.append(" etn.");sbSql.append(JdbcConstantes.ETN_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubcNacionalidad ON ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_UBC_ID);sbSql.append(" = ");sbSql.append(" ubcNacionalidad.");sbSql.append(JdbcConstantes.UBC_ID);			
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ON ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);			
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ON ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ON ");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);sbSql.append(" = ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr ON ");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" = ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ON ");
			sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ON ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append(" = ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);

			sbSql.append(" WHERE ");
			//identificacion
			sbSql.append(" UPPER(prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(") like ? ");
			sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" in (");
			sbSql.append(RolConstantes.ROL_BD_EVALUADOR_VALUE);sbSql.append(",");
			sbSql.append(RolConstantes.ROL_BD_VALIDADOR_VALUE);sbSql.append(",");
			sbSql.append(RolConstantes.ROL_BD_CONSULTOR_VALUE);
			sbSql.append(" ) ");
			//facultad
			if(facultadId == GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" > ");sbSql.append(" ? ");
			}else{
				sbSql.append(" AND ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" = ");sbSql.append(" ? ");
			}
			if(carreraId == GeneralesConstantes.APP_ID_BASE.intValue()){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" > ");sbSql.append(" ? ");
			}else{
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");sbSql.append(" ? ");
			}
			sbSql.append(" ORDER BY  crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
					
			sbSql.append(" )  TABLA ORDER BY  ");
			sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			
			
			
			//FIN QUERY
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, "%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase())+"%"); //cargo la identificacion
			pstmt.setInt(2, facultadId);
			pstmt.setInt(3, carreraId);
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<UsuarioRolJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (Exception e) {
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
		if(retorno == null){
			throw new UsuarioRolJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRolJdbc.buscar.por.identificacion.facultad.carrera.no.result.exception")));
		}
		return retorno;
		
		
	}

	/**
	 * Realiza la busqueda de una lista de usuario_rol por rol, id de facultad o id de carrera
	 * @param UsuarioRolDto - id del título
	 * @return usuarioRol con el id solicitado 
	 * @throws UsuarioRolJdbcDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws UsuarioRolJdbcDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<UsuarioRolJdbcDto> buscarUsuarioXFacultadXCarreraXRol(
			int rolId, int facultadId, int carreraId)
			throws UsuarioRolJdbcDtoException,
			UsuarioRolJdbcDtoNoEncontradoException {
		List<UsuarioRolJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			
			sbSql.append(" SELECT ");
			//**** CAMPOS DE PERSONA ****/
			sbSql.append("distinct( prs."); sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append("),");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(",");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);sbSql.append(",");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(",");
			sbSql.append(" CASE WHEN ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(",");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(",");
			
			//**** CAMPOS DE CARRERA ****/
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(",");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);sbSql.append(",");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);sbSql.append(",");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(",");
//			sbSql.append("crrfcl");sbSql.append(",");
//			//**** CAMPOS DE FACULTAD ****/
			sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(",");
			sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);sbSql.append(",");
			//**** CAMPOS DE USUARIO ****/
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(",");
			//**** CAMPOS DE USUARIO_ROL ****/
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(",");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ESTADO);sbSql.append(",");
			//**** CAMPOS DE ROL ****/
			sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(",");
			sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);sbSql.append(",");
			sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_TIPO);sbSql.append(",");
//			sbSql.append(",");
			//**** CAMPOS DE ROL_FLUJO_CARRERA ****/
			sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ID);sbSql.append(",");
			sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);
			
			sbSql.append(" FROM  ");
			
			
			
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ON ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);			
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ON ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ON ");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);sbSql.append(" = ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr ON ");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" = ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ON ");
			sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ON ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append(" = ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);

			sbSql.append(" WHERE ");
			
			if(rolId==GeneralesConstantes.APP_ID_BASE.intValue() || rolId==0){
				sbSql.append("  rol.");sbSql.append(JdbcConstantes.ROL_ID);
				sbSql.append(" > ?");
			}else{
				sbSql.append("  rol.");sbSql.append(JdbcConstantes.ROL_ID);
				sbSql.append(" = ?");
			}
			
			//facultad
			if(facultadId == GeneralesConstantes.APP_ID_BASE.intValue() || facultadId == 0){
				sbSql.append(" AND ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" > ");sbSql.append(" ? ");
			}else{
				sbSql.append(" AND ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" = ");sbSql.append(" ? ");
			}
			if(carreraId == GeneralesConstantes.APP_ID_BASE.intValue() || carreraId == 0){
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" > ");sbSql.append(" ? ");
			}else{
				sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");sbSql.append(" ? ");
			}
			sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" NOT IN ( ");
			sbSql.append(RolConstantes.ROL_BD_ADMINISTRADOR_VALUE);sbSql.append(" , ");
			sbSql.append(RolConstantes.ROL_BD_EDITOR_ACTA_VALUE);sbSql.append(" , ");
			sbSql.append(RolConstantes.ROL_BD_EDITOR_DGIP_VALUE);sbSql.append(" , ");
			sbSql.append(RolConstantes.ROL_BD_POSTULANTE_VALUE);sbSql.append(" ) ");
			sbSql.append(" AND ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ");sbSql.append(RolFlujoCarreraConstantes.ROL_FLUJO_CARRERA_ESTADO_ACTIVO_VALUE);
			sbSql.append(" ORDER BY  ");sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" , ");
			sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" , ");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);
			sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			
			//FIN QUERY
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, rolId); 
			pstmt.setInt(2, facultadId);
			pstmt.setInt(3, carreraId);
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<UsuarioRolJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoNuevoUsuario(rs));
			}
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
		if(retorno == null){
			throw new UsuarioRolJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRolJdbc.buscar.por.identificacion.facultad.carrera.no.result.exception")));
		}
		return retorno;
		
		
	}
	
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	public List<UsuarioRolJdbcDto> buscarCarrerasXIdentificacion(
			String identificacion)
			throws UsuarioRolJdbcDtoException,
			UsuarioRolJdbcDtoNoEncontradoException {
		List<UsuarioRolJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT ");
			//**** CAMPOS DE CARRERA ****/
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			//**** CAMPOS DE FACULTAD ****/
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
			//**** CAMPOS DE USUARIO ****/
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_IDENTIFICACION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_NICK);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CREACION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CADUCIDAD);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CAD_PASS);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ESTADO);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_EST_SESION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ACTIVE_DIRECTORY);
			//**** CAMPOS DE USUARIO_ROL ****/
			sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ESTADO);
			//**** CAMPOS DE ROL ****/
			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);
			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_TIPO);
			//**** CAMPOS DE ROL_FLUJO_CARRERA ****/
			sbSql.append(" ,roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ID);
			sbSql.append(" ,roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_ETNIA);sbSql.append(" etn ON ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ETN_ID);sbSql.append(" = ");sbSql.append(" etn.");sbSql.append(JdbcConstantes.ETN_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubcNacionalidad ON ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_UBC_ID);sbSql.append(" = ");sbSql.append(" ubcNacionalidad.");sbSql.append(JdbcConstantes.UBC_ID);			
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ON ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);			
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ON ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ON ");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);sbSql.append(" = ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr ON ");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" = ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ON ");
			sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ON ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append(" = ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);

			sbSql.append(" WHERE ");
			//identificacion
			sbSql.append(" UPPER(prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(") like ? ");
			sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" in (");
			sbSql.append(RolConstantes.ROL_BD_EVALUADOR_VALUE);sbSql.append(",");
			sbSql.append(RolConstantes.ROL_BD_VALIDADOR_VALUE);sbSql.append(",");
			sbSql.append(RolConstantes.ROL_BD_CONSULTOR_VALUE);
			sbSql.append(" ) ");
			sbSql.append(" ORDER BY  fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" , ");
			sbSql.append(JdbcConstantes.CRR_DESCRIPCION);sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			
			//FIN QUERY
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, "%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase())+"%"); //cargo la identificacion
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<UsuarioRolJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoListaCarrerasXidentificacion(rs));
			}
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
		if(retorno == null){
			throw new UsuarioRolJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRolJdbc.buscar.por.identificacion.facultad.carrera.no.result.exception")));
		}
		return retorno;
		
		
	}

	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	public List<UsuarioRolJdbcDto> buscarUsuariosActivosxCarreraXIdentificacion(
			String identificacion, int rolId , int crrId)
			throws UsuarioRolJdbcDtoException,
			UsuarioRolJdbcDtoNoEncontradoException {
		List<UsuarioRolJdbcDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT ");
			//**** CAMPOS DE FACULTAD ****/
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" ,fcl.");sbSql.append(JdbcConstantes.FCL_DESCRIPCION);
			//**** CAMPOS DE CARRERA ****/
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" ,crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			//**** CAMPOS DE USUARIO ****/
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ID);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_IDENTIFICACION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_NICK);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CREACION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CADUCIDAD);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_FECHA_CAD_PASS);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ESTADO);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_EST_SESION);
			sbSql.append(" ,usr.");sbSql.append(JdbcConstantes.USR_ACTIVE_DIRECTORY);
			//**** CAMPOS DE USUARIO_ROL ****/
			sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" ,usro.");sbSql.append(JdbcConstantes.USRO_ESTADO);
			//**** CAMPOS DE ROL ****/
			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);
			sbSql.append(" ,rol.");sbSql.append(JdbcConstantes.ROL_TIPO);
			//**** CAMPOS DE ROL_FLUJO_CARRERA ****/
			sbSql.append(" ,roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ID);
			sbSql.append(" ,roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_ETNIA);sbSql.append(" etn ON ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ETN_ID);sbSql.append(" = ");sbSql.append(" etn.");sbSql.append(JdbcConstantes.ETN_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_UBICACION);sbSql.append(" ubcNacionalidad ON ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_UBC_ID);sbSql.append(" = ");sbSql.append(" ubcNacionalidad.");sbSql.append(JdbcConstantes.UBC_ID);			
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ON ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);			
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ON ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ON ");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);sbSql.append(" = ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr ON ");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(" = ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ON ");
			sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);sbSql.append(" = ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ON ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);sbSql.append(" = ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);

			sbSql.append(" WHERE ");
			//identificacion
			sbSql.append(" UPPER(prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(") like ? ");
			sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = ");sbSql.append(" ? ");
			sbSql.append(" AND crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");sbSql.append(" ? ");
			sbSql.append(" ORDER BY  fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" , ");
			sbSql.append(JdbcConstantes.CRR_DESCRIPCION);sbSql.append(" , ");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			
			
			
			//FIN QUERY
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSql.toString());
			
//			System.out.println("Verificacion : " + sbSql.toString() );
			
			pstmt.setString(1, "%"+GeneralesUtilidades.eliminarEspaciosEnBlanco(identificacion.toUpperCase())+"%"); //cargo la identificacion
			pstmt.setInt( 2, rolId ); 
			pstmt.setInt( 3 , crrId );
			
			rs = pstmt.executeQuery();
			retorno = new ArrayList<UsuarioRolJdbcDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoListaCarrerasXidentificacion(rs));
			}
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
		if(retorno == null){
			throw new UsuarioRolJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRolJdbc.buscar.por.identificacion.facultad.carrera.no.result.exception")));
		}
		return retorno;
		
		
	}
	
	
	public UsuarioRolJdbcDto buscarUsuariosXIdentificacion(
			String identificacion)
			throws UsuarioRolJdbcDtoException,
			UsuarioRolJdbcDtoNoEncontradoException {
		UsuarioRolJdbcDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
			
			sbSql.append(" SELECT ");
			//**** CAMPOS DE PERSONA ****/
			sbSql.append("distinct( prs."); sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append("),");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(",");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_TIPO_IDENTIFICACION);sbSql.append(",");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(",");
			sbSql.append(" CASE WHEN ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); sbSql.append(" IS NULL THEN ");sbSql.append("''");
						sbSql.append(" ELSE ");sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO); 
						sbSql.append(" END AS ");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(",");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(",");
			
			//**** CAMPOS DE USUARIO ****/
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(",");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_NICK);sbSql.append(",");
			//**** CAMPOS DE USUARIO_ROL ****/
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ID);sbSql.append(",");
			//**** CAMPOS DE ROL ****/
			sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			
			sbSql.append(" FROM  ");
			
			
			
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr ON ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);			
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro ON ");
			sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" LEFT JOIN ");sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol ON ");
			sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);sbSql.append(" = ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);
			
			sbSql.append(" WHERE ");
			
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ");sbSql.append(" ? ");
			sbSql.append(" AND ");sbSql.append(" usr.");sbSql.append(JdbcConstantes.USR_ESTADO);sbSql.append(" = ");sbSql.append(UsuarioConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" usro.");sbSql.append(JdbcConstantes.USRO_ESTADO);sbSql.append(" = ");sbSql.append(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND ");sbSql.append(" rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" NOT IN ( ");
			sbSql.append(RolConstantes.ROL_BD_POSTULANTE_VALUE);sbSql.append(" , ");
			sbSql.append(RolConstantes.ROL_BD_ADMINISTRADOR_VALUE);sbSql.append(" ) ");
			
			//FIN QUERY
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, identificacion); 
			
			rs = pstmt.executeQuery();
			retorno = new UsuarioRolJdbcDto();
			while(rs.next()){
				retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
				retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
				retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
				retorno.setUsrId(rs.getInt(JdbcConstantes.USR_ID));
				retorno.setUsrNick(rs.getString(JdbcConstantes.USR_NICK));
				retorno.setUsroId(rs.getInt(JdbcConstantes.USRO_ID));
				retorno.setRolId(rs.getInt(JdbcConstantes.ROL_ID));
			}
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
		if(retorno == null){
			throw new UsuarioRolJdbcDtoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRolJdbc.buscar.por.identificacion.facultad.carrera.no.result.exception")));
		}
		return retorno;
		
		
		
	}
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */
	private UsuarioRolJdbcDto transformarResultSetADto(ResultSet rs) throws SQLException{
		UsuarioRolJdbcDto retorno = new UsuarioRolJdbcDto();
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsTipoIdentificacion(rs.getInt(JdbcConstantes.PRS_TIPO_IDENTIFICACION));
//		retorno.setPrsTipoIdentificacionSniese(rs.getInt(JdbcConstantes.PRS_TIPO_IDENTIFICACION_SNIESE));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsSexo(rs.getInt(JdbcConstantes.PRS_SEXO));
		retorno.setPrsSexoSniese(rs.getInt(JdbcConstantes.PRS_SEXO_SNIESE));
		retorno.setPrsMailPersonal(rs.getString(JdbcConstantes.PRS_MAIL_PERSONAL));
		retorno.setPrsMailInstitucional(rs.getString(JdbcConstantes.PRS_MAIL_INSTITUCIONAL));
		retorno.setPrsTelefono(rs.getString(JdbcConstantes.PRS_TELEFONO));
		retorno.setPrsEtnId(rs.getInt("prsEtn"));
		retorno.setPrsUbcId(rs.getInt("prsUbc"));
		
		//**** CAMPOS DE ETNIA ****/
		retorno.setEtnId(rs.getInt(JdbcConstantes.ETN_ID));
		retorno.setEtnDescripcion(rs.getString(JdbcConstantes.ETN_DESCRIPCION));
		retorno.setEtnCodigoSniese(rs.getString(JdbcConstantes.ETN_CODIGO_SNIESE));
		
		//**** CAMPOS DE UBICACION ****/
		retorno.setUbcId(rs.getInt(JdbcConstantes.UBC_ID));
		retorno.setUbcDescripcion(rs.getString(JdbcConstantes.UBC_DESCRIPCION));
		retorno.setUbcJerarquia(rs.getInt(JdbcConstantes.UBC_JERARQUIA));
		retorno.setUbcGentilicio(rs.getString(JdbcConstantes.UBC_GENTILICIO));
		retorno.setUbcCodSniese(rs.getString(JdbcConstantes.UBC_COD_SNIESE));
		
		//**** CAMPOS DE USUARIO ****//
		retorno.setUsrId(rs.getInt(JdbcConstantes.USR_ID));
		retorno.setUsrIdentificacion(rs.getString(JdbcConstantes.USR_IDENTIFICACION));
		retorno.setUsrNick(rs.getString(JdbcConstantes.USR_NICK));
		retorno.setUsrFechaCreacion(rs.getTimestamp(JdbcConstantes.USR_FECHA_CREACION));
		retorno.setUsrFechaCaducidad(rs.getDate(JdbcConstantes.USR_FECHA_CADUCIDAD));
		retorno.setUsrFechaCadPass(rs.getDate(JdbcConstantes.USR_FECHA_CAD_PASS));
		retorno.setUsrEstado(rs.getInt(JdbcConstantes.USR_ESTADO));
		retorno.setUsrEstSesion(rs.getInt(JdbcConstantes.USR_EST_SESION));
		retorno.setUsrActiveDirectory(rs.getInt(JdbcConstantes.USR_ACTIVE_DIRECTORY));
		//**** CAMPOS DE USUARIO_ROL ****//
		retorno.setUsroId(rs.getInt(JdbcConstantes.USRO_ID));
		retorno.setUsroEstado(rs.getInt(JdbcConstantes.USRO_ESTADO));
		//**** CAMPOS DE ROL ****//
		retorno.setRolId(rs.getInt(JdbcConstantes.ROL_ID));
		retorno.setRolDescripcion(rs.getString(JdbcConstantes.ROL_DESCRIPCION));
		retorno.setRolTipo(rs.getInt(JdbcConstantes.ROL_TIPO));
		//**** CAMPOS DE ROL ****//
//		retorno.setRoflcrId(rs.getInt(JdbcConstantes.ROFLCR_ID));
//		retorno.setRoflcrEstado(rs.getInt(JdbcConstantes.ROFLCR_ESTADO));
		
		//**** CAMPOS DE CARRERA ****//
//		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
//		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
//		retorno.setCrrCodSniese(rs.getString(JdbcConstantes.CRR_COD_SNIESE));
//		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		
		//**** CAMPOS FACULTAD ****//
//		retorno.setFclId(rs.getInt(JdbcConstantes.FCL_ID));
//		retorno.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
		
		return retorno;
	}
	
	
	private UsuarioRolJdbcDto transformarResultSetADtoListaCarrerasXidentificacion(ResultSet rs) throws SQLException{
		UsuarioRolJdbcDto retorno = new UsuarioRolJdbcDto();
		
		
		//**** CAMPOS DE USUARIO ****//
		retorno.setUsrId(rs.getInt(JdbcConstantes.USR_ID));
		retorno.setUsrIdentificacion(rs.getString(JdbcConstantes.USR_IDENTIFICACION));
		retorno.setUsrNick(rs.getString(JdbcConstantes.USR_NICK));
		retorno.setUsrFechaCreacion(rs.getTimestamp(JdbcConstantes.USR_FECHA_CREACION));
		retorno.setUsrFechaCaducidad(rs.getDate(JdbcConstantes.USR_FECHA_CADUCIDAD));
		retorno.setUsrFechaCadPass(rs.getDate(JdbcConstantes.USR_FECHA_CAD_PASS));
		retorno.setUsrEstado(rs.getInt(JdbcConstantes.USR_ESTADO));
		retorno.setUsrEstSesion(rs.getInt(JdbcConstantes.USR_EST_SESION));
		retorno.setUsrActiveDirectory(rs.getInt(JdbcConstantes.USR_ACTIVE_DIRECTORY));
		//**** CAMPOS DE USUARIO_ROL ****//
		retorno.setUsroId(rs.getInt(JdbcConstantes.USRO_ID));
		retorno.setUsroEstado(rs.getInt(JdbcConstantes.USRO_ESTADO));
		//**** CAMPOS DE ROL ****//
		retorno.setRolId(rs.getInt(JdbcConstantes.ROL_ID));
		retorno.setRolDescripcion(rs.getString(JdbcConstantes.ROL_DESCRIPCION));
		retorno.setRolTipo(rs.getInt(JdbcConstantes.ROL_TIPO));
		//**** CAMPOS DE ROL ****//
		retorno.setRoflcrId(rs.getInt(JdbcConstantes.ROFLCR_ID));
		retorno.setRoflcrEstado(rs.getInt(JdbcConstantes.ROFLCR_ESTADO));
		
		//**** CAMPOS DE CARRERA ****//
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrCodSniese(rs.getString(JdbcConstantes.CRR_COD_SNIESE));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		
		//**** CAMPOS FACULTAD ****//
		retorno.setFclId(rs.getInt(JdbcConstantes.FCL_ID));
		retorno.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
		
		return retorno;
	}
	
	private UsuarioRolJdbcDto transformarResultSetADtoNuevoUsuario(ResultSet rs) throws SQLException{
		UsuarioRolJdbcDto retorno = new UsuarioRolJdbcDto();
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsTipoIdentificacion(rs.getInt(JdbcConstantes.PRS_TIPO_IDENTIFICACION));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		
		//**** CAMPOS DE USUARIO_ROL ****//
		retorno.setUsroId(rs.getInt(JdbcConstantes.USRO_ID));
		retorno.setUsroEstado(rs.getInt(JdbcConstantes.USRO_ESTADO));
		//**** CAMPOS DE ROL ****//
		retorno.setRolId(rs.getInt(JdbcConstantes.ROL_ID));
		retorno.setRolDescripcion(rs.getString(JdbcConstantes.ROL_DESCRIPCION));
		retorno.setRolTipo(rs.getInt(JdbcConstantes.ROL_TIPO));
		//**** CAMPOS DE ROL ****//
		retorno.setRoflcrId(rs.getInt(JdbcConstantes.ROFLCR_ID));
		retorno.setRoflcrEstado(rs.getInt(JdbcConstantes.ROFLCR_ESTADO));
		
		//**** CAMPOS DE CARRERA ****//
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrCodSniese(rs.getString(JdbcConstantes.CRR_COD_SNIESE));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		
		//**** CAMPOS FACULTAD ****//
		retorno.setFclId(rs.getInt(JdbcConstantes.FCL_ID));
		retorno.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
		
		return retorno;
	}
	
}
