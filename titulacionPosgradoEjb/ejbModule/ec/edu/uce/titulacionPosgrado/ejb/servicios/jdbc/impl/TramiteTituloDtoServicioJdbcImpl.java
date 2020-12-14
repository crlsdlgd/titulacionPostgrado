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
   
 ARCHIVO:     TramiteTituloDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de la tabla TramiteTitulo.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 21/02/2018		Daniel Albuja					       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudiantePostuladoJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.TramiteTituloDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.TramiteTituloException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.TramiteTituloNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.TramiteTituloDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.TramiteTituloConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

/**
 * EJB TramiteTituloDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla TramiteTitulo.
 * @author dalbuja
 * @version 1.0
 */
@Stateless
public class TramiteTituloDtoServicioJdbcImpl implements TramiteTituloDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	/**
	 * Actualiza la entidad TramiteTitulo para los estudiantes validados
	 * @param EstudiantePostuladoJdbcDto - estudiante a editar
	 * @return Boolean 
	 * @throws TramiteTituloException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws TramiteTituloNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public Boolean actualizarTramiteTituloValidadosDinardapXId(EstudiantePostuladoJdbcDto entidad) throws TramiteTituloException, TramiteTituloNoEncontradoException{
		Boolean retorno=false;
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" UPDATE ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);
			sbSql.append(" SET ");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" = ");
			if(entidad.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_VALUE){
				sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_VALIDADO_VALUE);
			}else if(entidad.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_GRACIA_VALUE){
				sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_GRACIA_VALIDADO_VALUE);
			}else if(entidad.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_DEFENSA_ORAL_VALUE){
				sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_DEFENSA_ORAL_VALIDADO_VALUE);
			}
			sbSql.append(" WHERE ");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, entidad.getTrttId()); //cargo el id del tramiteTitulo
			pstmt.executeUpdate();
			retorno=true;
		} catch (NoResultException e) {
			throw new TramiteTituloNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TramiteTitulo.buscar.por.id.no.result.exception",entidad.getTrttId())));
		}catch (Exception e) {
			throw new TramiteTituloException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TramiteTitulo.buscar.por.id.exception")));
		}finally {
			try {
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

	/**
	 * Devuelve el tramiteTitulo del postulante por identificación y por carrera
	 * @param String cedula - estudiante a buscar
	 * @param int carreraId - carrera del tramiteTitulo
	 * @return TramiteTituloDto 
	 * @throws TramiteTituloException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws TramiteTituloNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	@Override
	public TramiteTituloDto buscarTramiteTituloXCedulaXCarrera(String cedula
			) throws TramiteTituloException,
			TramiteTituloNoEncontradoException {
		TramiteTituloDto retorno=null;
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces, ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" WHERE trtt.");
			sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);
			sbSql.append(" AND prs.");
			sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, cedula); //cargo cédula del postulante
			ResultSet rs = pstmt.executeQuery();
			retorno=new TramiteTituloDto();
			while(rs.next()){
				retorno.setTrttEstadoProceso(rs.getInt(JdbcConstantes.TRTT_ESTADO_PROCESO));
			}
			if( rs != null){
				rs.close();
			}
		} catch (NoResultException e) {
			retorno=new TramiteTituloDto();
			retorno.setTrttEstadoProceso(0);
		}catch (Exception e) {
			retorno=new TramiteTituloDto();
			retorno.setTrttEstadoProceso(0);
		}finally {
			try {
				
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
	
	/**
	 * Actualiza la entidad TramiteTitulo desactivando el trtt
	 * @param Integer trttId - id de trtt
	 * @return Boolean 
	 * @throws TramiteTituloException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws TramiteTituloNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public Boolean desactivarTramiteTituloXId(Integer trttId) throws TramiteTituloException, TramiteTituloNoEncontradoException{
		Boolean retorno=false;
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" UPDATE ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);
			sbSql.append(" SET ");sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(" = ");
			sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_INACTIVO_VALUE);
			sbSql.append(" WHERE ");sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, trttId); //cargo el id del tramiteTitulo
			pstmt.executeUpdate();
			retorno=true;
		} catch (NoResultException e) {
			throw new TramiteTituloNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TramiteTitulo.buscar.por.id.no.result.exception",trttId)));
		}catch (Exception e) {
			e.printStackTrace();
			throw new TramiteTituloException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TramiteTitulo.buscar.por.id.exception")));
		}finally {
			try {
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
	
	
	/**
	 * Busca el tramite_titulo desactivado de la cédula ingresada, si reprobó examen complexivo
	 * @param String cedula
	 * @return Integer 
	 * @throws TramiteTituloException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws TramiteTituloNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public Integer burcarTramiteTituloDesactivadoReproboComplex(String cedula) throws TramiteTituloException, TramiteTituloNoEncontradoException{
		
		TramiteTituloDto trttAux=null;
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces, ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" WHERE trtt.");
			sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);
			sbSql.append(" AND prs.");
			sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
//			sbSql.append(" AND trtt.");
//			sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_INACTIVO_VALUE);
			sbSql.append(" AND (trtt.");
			sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_FINAL_GRACIA_CARGADO_VALUE);
			sbSql.append(" OR ");
			sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(" = ");sbSql.append(TramiteTituloConstantes.ESTADO_PROCESO_COMPLEX_FINAL_CARGADO_VALUE);
			sbSql.append(" ) AND prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setString(1, cedula); //cargo cédula del postulante
			ResultSet rs = pstmt.executeQuery();
			trttAux=new TramiteTituloDto();
			while(rs.next()){
				trttAux.setTrttEstadoProceso(rs.getInt(JdbcConstantes.TRTT_ESTADO_PROCESO));
			}
			if( rs != null){
				rs.close();
			}
		} catch (NoResultException e) {
			trttAux=new TramiteTituloDto();
			trttAux.setTrttEstadoProceso(GeneralesConstantes.APP_ID_BASE);
		}catch (Exception e) {
			trttAux=new TramiteTituloDto();
			trttAux.setTrttEstadoProceso(GeneralesConstantes.APP_ID_BASE);
		}finally {
			try {
				if (pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		return trttAux.getTrttEstadoProceso();
	}
	
	/**
	 * Busca el tramite_titulo de la cédula ingresada, si ya pasó por la validación por la carrera seleccionada
	 * @param String cedula, int carreraId
	 * @return Integer 
	 * @throws TramiteTituloException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws TramiteTituloNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public Integer buscarTramiteTituloValidadoXCarrera(String cedula, int carreraId) throws TramiteTituloException, TramiteTituloNoEncontradoException{
		
		TramiteTituloDto trttAux=null;
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_TRAMITE_TITULO);sbSql.append(" trtt, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);sbSql.append(" fces, ");
			sbSql.append(JdbcConstantes.TABLA_PERSONA);sbSql.append(" prs ");
			sbSql.append(" WHERE trtt.");
			sbSql.append(JdbcConstantes.TRTT_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_TRTT_ID);
			sbSql.append(" AND prs.");
			sbSql.append(JdbcConstantes.PRS_ID);sbSql.append(" = fces.");sbSql.append(JdbcConstantes.FCES_PRS_ID);
			sbSql.append(" AND trtt.");
			sbSql.append(JdbcConstantes.TRTT_ESTADO_PROCESO);sbSql.append(TramiteTituloConstantes.EST_PROC_VALIDADO_VALUE);
			sbSql.append(" AND trtt.");
			sbSql.append(JdbcConstantes.TRTT_ESTADO_TRAMITE);sbSql.append(TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			sbSql.append(" AND trtt.");
			sbSql.append(JdbcConstantes.TRTT_CARRERA_ID);sbSql.append(" = ? ");
			sbSql.append(" AND prs.");sbSql.append(JdbcConstantes.PRS_IDENTIFICACION);sbSql.append(" = ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, carreraId); //cargo carrera del postulante
			pstmt.setString(2, cedula); //cargo cédula del postulante
			ResultSet rs = pstmt.executeQuery();
			trttAux=new TramiteTituloDto();
			while(rs.next()){
				trttAux.setTrttEstadoProceso(rs.getInt(JdbcConstantes.TRTT_ESTADO_PROCESO));
			}
			if( rs != null){
				rs.close();
			}
		} catch (NoResultException e) {
			trttAux=new TramiteTituloDto();
			trttAux.setTrttEstadoProceso(GeneralesConstantes.APP_ID_BASE);
		}catch (Exception e) {
			trttAux=new TramiteTituloDto();
			trttAux.setTrttEstadoProceso(GeneralesConstantes.APP_ID_BASE);
		}finally {
			try {
				if (pstmt != null){
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		return trttAux.getTrttEstadoProceso();
	}
	
}
