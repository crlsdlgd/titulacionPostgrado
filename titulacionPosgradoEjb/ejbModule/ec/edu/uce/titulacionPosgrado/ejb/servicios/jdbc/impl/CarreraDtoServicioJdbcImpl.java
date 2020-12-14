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
   
 ARCHIVO:     CarreraDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de la tabla carrera.
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
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.CarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudiantePostuladoJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.TramiteTituloConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

/**
 * EJB CarreraDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla carrera.
 * @author dalbuja
 * @version 1.0
 */
@Stateless
public class CarreraDtoServicioJdbcImpl implements CarreraDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	/**
	 * Realiza la busqueda de un carrera por id
	 * @param carreraId - id del carrera
	 * @return Carrera con el id solicitado 
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public CarreraDto buscarXId(int carreraId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		CarreraDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO_EVALUACION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" = ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, carreraId); //cargo el id de la carrera
			rs = pstmt.executeQuery();
			if(rs.next()){
				retorno = transformarResultSetADto(rs);
			}else{
				retorno = null;
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.id.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.id.no.result.exception",carreraId)));
		}	
		
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todas las carreras de la aplicacion
	 * @return Lista todas las carreras de la aplicacion
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarTodos() throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO_EVALUACION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.todos.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.todos.no.result.exception")));
		}	
		return retorno;
	}

	/**
	 * Realiza la busqueda de las carreras por id de usuario, por la descripcion del rol y por el estado del rol del flujo 
	 * @param idUsuario - id del usuario asignado a una carrera
	 * @param descRol - descripcion del rol del usuario
	 * @param estadoRolFlujo - estado del rol del flujo
	 * @return Lista de las carreras por id de usuario, por la descripcion del rol y por el estado del rol del flujo
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> listarXIdUsuarioXDescRolXEstadoRolFl(int idUsuario, String descRol, int estadoRolFlujo) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_ROL_FLUJO_CARRERA);sbSql.append(" roflcr , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO_ROL);sbSql.append(" usro , ");	
								   sbSql.append(JdbcConstantes.TABLA_ROL);sbSql.append(" rol , ");
								   sbSql.append(JdbcConstantes.TABLA_USUARIO);sbSql.append(" usr , ");
								   sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" roflcr.");sbSql.append(JdbcConstantes.ROFLCR_CRR_ID);
										sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_USRO_ID);
										sbSql.append(" = usro.");sbSql.append(JdbcConstantes.USRO_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_USR_ID);
										sbSql.append(" = usr.");sbSql.append(JdbcConstantes.USR_ID);
									sbSql.append(" AND usro.");sbSql.append(JdbcConstantes.USRO_ROL_ID);
										sbSql.append(" = rol.");sbSql.append(JdbcConstantes.ROL_ID);
									sbSql.append(" AND usr.");sbSql.append(JdbcConstantes.USR_ID);sbSql.append(" = ? ");
									sbSql.append(" AND rol.");sbSql.append(JdbcConstantes.ROL_DESCRIPCION);sbSql.append(" = ? ");	
									sbSql.append(" AND roflcr.");sbSql.append(JdbcConstantes.ROFLCR_ESTADO);sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
					
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, idUsuario); //cargo el id del usuario
			pstmt.setString(2, descRol); //cargo la descripcion del rol
			pstmt.setInt(3, estadoRolFlujo); //cargo el estado del rol del flujo
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.idusuario.descrol.estrol.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.idusuario.descrol.estrol.no.result.exception",idUsuario)));
		}	
		return retorno;
	}
	
	public List<CarreraDto> listarXTipoFormacion(int tipoFormacionId, List<CarreraDto> listaCarrerasAsignadas) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		List<Integer> listaCarrIn = new ArrayList<Integer>();
		for (CarreraDto item : listaCarrerasAsignadas) {
			listaCarrIn.add(item.getCrrId());
		}
		
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" distinct(crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(") , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr , ");
								   sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);
										sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" AND cncr.");sbSql.append(JdbcConstantes.CNCR_TIFR_ID);sbSql.append(" = ? ");
									sbSql.append(" AND cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);sbSql.append(" IN ( ");
									for( int i = 0; i< listaCarrIn.size(); i++){
										sbSql.append(" ? ");
										if(i != listaCarrIn.size() -1) {
											sbSql.append(","); 
										}
									}
									sbSql.append(" ) ");
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
					
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, tipoFormacionId); //cargo el id del tipo de formacion
			int contador = 1;
			for (Integer item : listaCarrIn) {
				contador++;
				pstmt.setInt(contador, item); 
			}
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.tipo.formacion.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.tipo.formacion.no.result.exception")));
		}	
		return retorno;
	}
	
	public List<CarreraDto> listarXTipoFormacionXSede(int tipoFormacionId, int tipoSede, List<CarreraDto> listaCarrerasAsignadas) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		List<Integer> listaCarrIn = new ArrayList<Integer>();
		for (CarreraDto item : listaCarrerasAsignadas) {
			listaCarrIn.add(item.getCrrId());
		}
		
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" distinct(crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(") , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr , ");
								   sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);
										sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_ID);
									sbSql.append(" AND cncr.");sbSql.append(JdbcConstantes.CNCR_TIFR_ID);sbSql.append(" = ? ");
									sbSql.append(" AND cncr.");sbSql.append(JdbcConstantes.CNCR_TISE_ID);sbSql.append(" = ? ");
									sbSql.append(" AND cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);sbSql.append(" IN ( ");
									for( int i = 0; i< listaCarrIn.size(); i++){
										sbSql.append(" ? ");
										if(i != listaCarrIn.size() -1) {
											sbSql.append(","); 
										}
									}
									sbSql.append(" ) ");
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
					
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, tipoFormacionId); //cargo el id del tipo de formacion
			pstmt.setInt(2, tipoSede); //cargo el id del tipo de sede
			int contador = 2;
			for (Integer item : listaCarrIn) {
				contador++;
				pstmt.setInt(contador, item); 
			}
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.sql.exception")));
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.tipo.formacion.sede.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.tipo.formacion.sede.no.result.exception")));
		}	
		return retorno;
	}	
	
	
	/**
	 * Realiza la busqueda de un carrera por id
	 * @param carreraId - id del carrera
	 * @return Carrera con el id solicitado 
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<CarreraDto> buscarXfacultad(int facultadId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
		
//			sbSql.append(" SELECT ");
//			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
//			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
//			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
//			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO_DURACION);
//			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr , ");
//			sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ");
//			sbSql.append(" WHERE ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);
//									sbSql.append(" = fcl.");sbSql.append(JdbcConstantes.FCL_ID);
//			sbSql.append(" AND ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);
//			sbSql.append(" = ? ");
			
			
			sbSql.append(" SELECT ");
//			sbSql.append(" distinct(");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);sbSql.append(" ) AS ");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
//			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
//			sbSql.append(" || ' ('|| ");sbSql.append("mdl.");sbSql.append(JdbcConstantes.MDL_DESCRIPCION);sbSql.append(" || ')'");sbSql.append("AS nombrecarrera");
			sbSql.append("  crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MODALIDAD);sbSql.append(" mdl ");
			sbSql.append(" WHERE ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);
//			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = cncr.");sbSql.append(JdbcConstantes.CRR_ID);
//			sbSql.append(" AND ");sbSql.append(" mdl.");sbSql.append(JdbcConstantes.MDL_ID);sbSql.append(" = cncr.");sbSql.append(JdbcConstantes.CNCR_MDL_ID);
			sbSql.append(" AND ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" = ? ");
//			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_NIVEL);sbSql.append(" = 0 ");
			sbSql.append(" ORDER BY  crr_detalle ");
			
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, facultadId); //cargo el id de la FACULTAD
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
//				retorno.add(transformarResultSetADto(rs));
				retorno.add(transformarResultSetADtoCrrXFcl(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.tipo.formacion.sede.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.tipo.formacion.sede.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de un carrera por id
	 * @param carreraId - id del carrera
	 * @return Carrera con el id solicitado 
	 * @throws CarreraDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CarreraDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public List<CarreraDto> buscarXfacultadId(int facultadId) throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		List<CarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			
		
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);
//			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
//			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_TIPO_DURACION);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr , ");
			sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ");
			sbSql.append(" WHERE ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);
									sbSql.append(" = fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" AND ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);
			sbSql.append(" = ? ");
			sbSql.append(" ORDER BY  crr_descripcion ");
			
			
//			sbSql.append(" SELECT ");
//			sbSql.append(" distinct(");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);sbSql.append(" ) AS ");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
//			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
//			sbSql.append(" || ' ('|| ");sbSql.append("mdl.");sbSql.append(JdbcConstantes.MDL_DESCRIPCION);sbSql.append(" || ')'");sbSql.append("AS nombrecarrera");
//			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" AS crr_id");
//			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" AS crr_detalle");
//			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);
//			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_FACULTAD);sbSql.append(" fcl ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
//			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MODALIDAD);sbSql.append(" mdl ");
//			sbSql.append(" WHERE ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" = crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);
//			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = cncr.");sbSql.append(JdbcConstantes.CRR_ID);
//			sbSql.append(" AND ");sbSql.append(" mdl.");sbSql.append(JdbcConstantes.MDL_ID);sbSql.append(" = cncr.");sbSql.append(JdbcConstantes.CNCR_MDL_ID);
//			sbSql.append(" AND ");sbSql.append(" fcl.");sbSql.append(JdbcConstantes.FCL_ID);sbSql.append(" = ? ");
//			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_NIVEL);sbSql.append(" = 0 ");
//			sbSql.append(" ORDER BY  nombrecarrera ");
			
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, facultadId); //cargo el id de la FACULTAD
			rs = pstmt.executeQuery();
			retorno = new ArrayList<CarreraDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADtoSimple(rs));
//				retorno.add(transformarResultSetADtoCrrXFcl(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.sql.exception")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.tipo.formacion.sede.exception")));
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
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.tipo.formacion.sede.no.result.exception")));
		}	
		return retorno;
	}
	
	@Override
	public CarreraDto buscarXCodSniese(String cod_sniese)
			throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException {
		CarreraDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);sbSql.append("  AS ");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" || ' ('|| ");sbSql.append("mdl.");sbSql.append(JdbcConstantes.MDL_DESCRIPCION);sbSql.append(" || ')'");sbSql.append("AS nombrecarrera");
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" AS crr_id");
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append(" AS crr_detalle");
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_FCL_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_MODALIDAD);sbSql.append(" mdl ");
			sbSql.append(" WHERE ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_COD_SNIESE);sbSql.append(" LIKE ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, cod_sniese); //cargo el código sniese
			rs = pstmt.executeQuery();
			retorno = new CarreraDto();
			while(rs.next()){
				retorno=transformarResultSetADtoCrrXFcl(rs);
			}
		} catch (SQLException e) {
		} catch (Exception e) {
			throw new CarreraDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.codigo.sniese.exception")));
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
			
		if(retorno == null ){
			throw new CarreraDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("CarreraJdbc.buscar.por.codigo.sniese.no.result.exception")));
		}	
		return retorno;
	} 
	
	/**
	 * Método que devuelve una lista de carreras en las que la persona se ha postulado
	 * @param cedula
	 * @return
	 * @throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException
	 */
	@Override
	public EstudiantePostuladoJdbcDto buscarCarrerasPostulacionesXCedula(String cedula, int carreraId)
			throws CarreraDtoJdbcException, CarreraDtoJdbcNoEncontradoException{
		EstudiantePostuladoJdbcDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);sbSql.append("  AS ");sbSql.append(JdbcConstantes.CRR_DESCRIPCION);
			sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append("  AS ");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);
			sbSql.append(" , crr.");sbSql.append(JdbcConstantes.CRR_DETALLE);sbSql.append("  AS ");sbSql.append(JdbcConstantes.CRR_DETALLE);
			sbSql.append(" , trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append("  AS ");sbSql.append(JdbcConstantes.TRTT_ID);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces ");
			sbSql.append(" , ");sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" WHERE ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");
			sbSql.append(" trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = ");
			sbSql.append(" fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);
			sbSql.append(" AND fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);sbSql.append(" = ");
			sbSql.append(" prs.");sbSql.append(JdbcConstantes.PRS_ID);
			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ?");
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" = ?");
			sbSql.append(" AND trtt.");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, cedula); //cargo la identificación del postulante
			pstmt.setInt(2, carreraId); //cargo el id de la carrera
			rs = pstmt.executeQuery();
			retorno = new EstudiantePostuladoJdbcDto();
			while(rs.next()){
				retorno=(transformarResultSetAEstudiantePostuladoJdbcDto(rs));
			}
		}catch (NoResultException e) {
			throw new CarreraDtoJdbcNoEncontradoException();
		}catch (SQLException e) {
		} catch (Exception e) {
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
		if(retorno == null ){
			throw new CarreraDtoJdbcNoEncontradoException();
		}	
		return retorno;
	}
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */
	
	private EstudiantePostuladoJdbcDto transformarResultSetAEstudiantePostuladoJdbcDto(ResultSet rs) throws SQLException{
		EstudiantePostuladoJdbcDto retorno= new EstudiantePostuladoJdbcDto();
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setTrttEstadoProceso(rs.getInt(JdbcConstantes.TRTT_ESTADO_PROCESO));
		retorno.setTrttId(rs.getInt(JdbcConstantes.TRTT_ID));
		return retorno;
	}
	
	private CarreraDto transformarResultSetADto(ResultSet rs) throws SQLException{
		CarreraDto retorno = new CarreraDto();
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrCodSniese(rs.getString(JdbcConstantes.CRR_COD_SNIESE));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		try {
			retorno.setCrrTipoEvaluacion(rs.getInt(JdbcConstantes.CRR_TIPO_EVALUACION));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return retorno;
	} 
	
	private CarreraDto transformarResultSetADtoSimple(ResultSet rs) throws SQLException{
		CarreraDto retorno = new CarreraDto();
		retorno.setCrrId(rs.getInt(JdbcConstantes.CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		return retorno;
	}
	
	private CarreraDto transformarResultSetADtoCrrXFcl(ResultSet rs) throws SQLException{
		CarreraDto retorno = new CarreraDto();
		retorno.setCrrId(rs.getInt("crr_id"));
		retorno.setCrrDescripcion(rs.getString("crr_detalle"));
//		retorno.setCrrCodSniese(rs.getString(JdbcConstantes.CRR_COD_SNIESE));
//		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setCrrFacultad(rs.getInt(JdbcConstantes.CRR_FCL_ID));
		return retorno;
	}

	
	
}
