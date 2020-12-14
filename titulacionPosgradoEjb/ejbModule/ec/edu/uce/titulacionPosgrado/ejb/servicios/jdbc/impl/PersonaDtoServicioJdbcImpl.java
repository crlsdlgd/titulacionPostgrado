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
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteValidacionJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.PersonaDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteValidacionDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.PersonaDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

/**
 * EJB PersonaDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla personaDto.
 * @author dcollaguazo
 * @version 1.0
 */
@Stateless
public class PersonaDtoServicioJdbcImpl implements PersonaDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;
//	@Resource(mappedName=GeneralesConstantes.APP_DATA_SOURCE_SIIU)
//	DataSource dsSiiu;
//	@Resource(mappedName=GeneralesConstantes.APP_DATA_SOURCE_EMISION_TITULO)
	DataSource dsEmision;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	@Override
	public List<PersonaDto> listaPersonaXIdentificacion(String identificacion) throws PersonaDtoJdbcException, PersonaDtoJdbcNoEncontradoException {
		List<PersonaDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con =  null;
		ResultSet rs =  null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" , ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" , ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append(" , ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(" , ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append(" , ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_MAIL_PERSONAL);sbSql.append(" , ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_TELEFONO);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" WHERE ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" LIKE ");sbSql.append("?");
			sbSql.append(" ORDER BY ");sbSql.append("prs."); sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, "%"+identificacion+"%"); //cargo el identificacion de la persona
			rs = pstmt.executeQuery();
			retorno = new ArrayList<PersonaDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new PersonaDtoJdbcException("UbicacionJdbc.sql.exception");
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new PersonaDtoJdbcException("UbicacionJdbc.buscar.todos.exception");
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
			throw new PersonaDtoJdbcNoEncontradoException("UbicacionJdbc.buscar.todos.no.result.exception");
		}	
		return retorno;
	}

	
	/**
	 * Realiza la busqueda de la persona en la aplicacion que sea evaluador en la carrera buscada
	 * @return persona evaluador
	 * @throws PersonaDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PersonaDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
//	@Override
	public PersonaDto buscarEvaluadorXCarrera(Integer carreraId)
			throws PersonaDtoJdbcException, PersonaDtoJdbcNoEncontradoException {
		PersonaDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con =  null;
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
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr, ");
			sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol, ");
			sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro, ");
			sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr ");
			sbSql.append(" WHERE ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = usr.");sbSql.append(JdbcConstantes.USR_PRS_ID);
			sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
			sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
			sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USRO_ID);
			sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_ID);sbSql.append(" = ");sbSql.append(RolConstantes.ROL_BD_EVALUADOR_VALUE);
			sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);sbSql.append(" = ");sbSql.append(carreraId);
			sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ");sbSql.append(RolFlujoCarreraConstantes.ROL_FLUJO_CARRERA_ESTADO_ACTIVO_VALUE);
			sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ESTADO);sbSql.append(" = ");sbSql.append(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			rs.next();
			retorno = new PersonaDto();
			retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
			retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
			retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
			retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
			retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
			retorno.setPrsSexo(rs.getInt(JdbcConstantes.PRS_SEXO));
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new PersonaDtoJdbcException("UbicacionJdbc.sql.exception");
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new PersonaDtoJdbcException("UbicacionJdbc.buscar.todos.exception");
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
		
		return retorno;
	}
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */
	private PersonaDto transformarResultSetADto(ResultSet rs) throws SQLException{
		PersonaDto retorno = new PersonaDto();
		retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
		retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
		retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
		retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
		retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
		retorno.setPrsMailPersonal(rs.getString(JdbcConstantes.PRS_MAIL_PERSONAL));
		retorno.setPrsTelefono(rs.getString(JdbcConstantes.PRS_TELEFONO));
		return retorno;
	}


	/**
	 * Realiza la busqueda de la persona de acuerdo a su nombre y apellidos
	 * @return persona 
	 * @throws PersonaDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws PersonaDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public Boolean buscarPersonaXApellidosNombres(String persona) throws PersonaDtoJdbcException, PersonaDtoJdbcNoEncontradoException{
		Boolean retorno = false;
		
//		PreparedStatement pstmt = null;
//		Connection con =  null;
//		ResultSet rs =  null;
//		try {
//			StringBuilder sbSql = new StringBuilder();
//			sbSql.append(" SELECT ");
//			sbSql.append(JdbcConstantes.PRS_ID);
//			sbSql.append(" FROM ");
//			sbSql.append(JdbcConstantes.TABLA_PERSONA);
//			sbSql.append(" WHERE ");
//			sbSql.append(JdbcConstantes.PRS_NOMBRES);sbSql.append("||' '||");
//			sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);sbSql.append("||' '||");
//			sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);sbSql.append(" = '");sbSql.append(persona);sbSql.append("'");
//			con = dsSiiu.getConnection();
//			pstmt = con.prepareStatement(sbSql.toString());
//			rs = pstmt.executeQuery();
//			if (!rs.next() ) {
//				retorno=true;
//			} 
//				
//		}catch(NoResultException e){
//			retorno=true;
//		} catch (Exception e) {
//			try {
//				if( rs != null){
//					rs.close();
//				}
//				if (pstmt != null){
//					pstmt.close();
//				}
//				if (con != null) {
//					con.close();
//				}
//			} catch (SQLException e1) {
//			}
//			
//		} finally {
//			try {
//				if( rs != null){
//					rs.close();
//				}
//				if (pstmt != null){
//					pstmt.close();
//				}
//				if (con != null) {
//					con.close();
//				}
//			} catch (SQLException e) {
//			}
//		}
		
		return retorno;
	}

	@Override	
	public EstudianteValidacionJdbcDto buscarPersonaXPrsId(String identificacion, Integer crrId) throws EstudianteValidacionDtoException{
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		EstudianteValidacionJdbcDto retorno = new EstudianteValidacionJdbcDto();
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_UBICACION_FOTO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_PRIMER_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_SEGUNDO_APELLIDO);
			sbSql.append(" ,prs.");sbSql.append(JdbcConstantes.PRS_NOMBRES);
			
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces, ");
			sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
						
			sbSql.append(" WHERE ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ?");
			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_CNCR_ID);sbSql.append(" = cncr.");sbSql.append(JdbcConstantes.CNCR_ID);
			sbSql.append(" AND cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);sbSql.append(" = ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, identificacion); //cargo el número de identificacion
			pstmt.setInt(2, crrId); //cargo el número de carrera
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
				retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
				retorno.setPrsUbicacionFoto(rs.getString(JdbcConstantes.PRS_UBICACION_FOTO));
				retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
				retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
				retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
			}else{
				return null;
			}

		} catch (SQLException e) {
			throw new EstudianteValidacionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.sql.exception")));
		} catch (Exception e) {
			throw new EstudianteValidacionDtoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.exception")));
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
		return retorno;
	}
	
	@Override
	public PersonaDto buscarXIdentificacionEmisionTitulo(String identificacion)
			throws PersonaDtoJdbcException, PersonaDtoJdbcNoEncontradoException {
		PersonaDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con =  null;
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
			con = dsEmision.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, identificacion);
			rs = pstmt.executeQuery();
			rs.next();
			retorno = new PersonaDto();
			retorno.setPrsId(rs.getInt(JdbcConstantes.PRS_ID));
			retorno.setPrsIdentificacion(rs.getString(JdbcConstantes.PRS_IDENTIFICACION));
			retorno.setPrsPrimerApellido(rs.getString(JdbcConstantes.PRS_PRIMER_APELLIDO));
			retorno.setPrsSegundoApellido(rs.getString(JdbcConstantes.PRS_SEGUNDO_APELLIDO));
			retorno.setPrsNombres(rs.getString(JdbcConstantes.PRS_NOMBRES));
			retorno.setPrsSexo(rs.getInt(JdbcConstantes.PRS_SEXO));
		} catch (SQLException e) {
			try {
				rs.close();
				pstmt.close();
				con.close();
			} catch (Exception e2) {
			}
		} catch (Exception e) {
			try {
				rs.close();
				pstmt.close();
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
		
		return retorno;
	}
	
	

	@Override	
	public Integer buscarFcesIdXPrsIdentificacion(String identificacion) throws Exception{
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
			
			con = dsEmision.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, identificacion); //cargo el número de identificacion
			rs = pstmt.executeQuery();
			if(rs.next()){
				return rs.getInt(JdbcConstantes.FCES_ID);
			}else{
				return 0;
			}

		} catch (SQLException e) {
			throw new Exception(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.sql.exception")));
		} catch (Exception e) {
			throw new Exception(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("EstudianteJdbc.buscar.por.id.exception")));
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
