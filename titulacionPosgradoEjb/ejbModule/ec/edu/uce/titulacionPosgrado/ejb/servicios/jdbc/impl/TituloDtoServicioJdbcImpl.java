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
   
 ARCHIVO:     TituloDtoServicioJdbcImpl.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de la tabla titulo.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 26/02/2018				Daniel Albuja				       Emisión Inicial
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

import ec.edu.uce.titulacionPosgrado.ejb.dtos.CarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.TituloDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.TituloDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.TituloDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.TituloDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

/**
 * EJB TituloDtoServicioJdbcImpl.
 * Clase donde se implementan los metodos para el servicio jdbc de la tabla titulo.
 * @author dalbuja
 * @version 1.0
 */
@Stateless
public class TituloDtoServicioJdbcImpl implements TituloDtoServicioJdbc {
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA ********************************** *
	 * ********************************************************************************* */
	/**
	 * Realiza la busqueda de un regimen academico por id
	 * @param regimenId - id del regimen academico
	 * @return Regimen academico con el id solicitado 
	 * @throws TituloDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws TituloDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public TituloDto buscarXId(int tituloId) throws TituloDtoJdbcException, TituloDtoJdbcNoEncontradoException{
		TituloDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" ttl.");sbSql.append(JdbcConstantes.TTL_ID);
			sbSql.append(" , ttl.");sbSql.append(JdbcConstantes.TTL_DESCRIPCION);
			sbSql.append(" , ttl.");sbSql.append(JdbcConstantes.TTL_SEXO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_TITULO);sbSql.append(" ttl ");
			sbSql.append(" WHERE ");sbSql.append(" ttl.");sbSql.append(JdbcConstantes.TTL_ID);
			sbSql.append(" = ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, tituloId); //cargo el id del titulo
			rs = pstmt.executeQuery();
			retorno = transformarResultSetADto(rs);
			
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new TituloDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TituloJdbc.sql.exception")));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new TituloDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TituloJdbc.buscar.por.id.exeption")));
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
			throw new TituloDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TituloJdbc.buscar.por.id.no.result.exception",tituloId )));
		}	
		
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todos los títulos de la aplicacion
	 * @return Lista todos los títulos de la aplicacion
	 * @throws TituloDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws TituloDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<TituloDto> listarTodos() throws TituloDtoJdbcException, TituloDtoJdbcNoEncontradoException{
		List<TituloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" ttl.");sbSql.append(JdbcConstantes.TTL_ID);
			sbSql.append(" , ttl.");sbSql.append(JdbcConstantes.TTL_DESCRIPCION);
			sbSql.append(" , ttl.");sbSql.append(JdbcConstantes.TTL_SEXO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_TITULO);sbSql.append(" ttl ");
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.TTL_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<TituloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new TituloDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TituloJdbc.sql.exception")));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new TituloDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TituloJdbc.buscar.todos.exception")));
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
			throw new TituloDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TituloJdbc.buscar.todos.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todos los títulos por carrera de la aplicacion
	 * @return Lista todos los títulos, por carrera de la aplicacion
	 * @throws TituloDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws TituloDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<TituloDto> listarXCarrera(int carreraId, List<CarreraDto> listaCarreras) throws TituloDtoJdbcException, TituloDtoJdbcNoEncontradoException{
		List<TituloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		
		List<Integer> listaCarrerasConsulta = new ArrayList<Integer>();
		if(carreraId == GeneralesConstantes.APP_ID_BASE){
			for (CarreraDto item : listaCarreras) {
				listaCarrerasConsulta.add(item.getCrrId());
			}
		}else{
			listaCarrerasConsulta.add(carreraId);
		}
		
		try {
			StringBuilder sbSql = new StringBuilder();
			
			sbSql.append(" SELECT ");
			sbSql.append(" ttl.");sbSql.append(JdbcConstantes.TTL_ID);
			sbSql.append(" , ttl.");sbSql.append(JdbcConstantes.TTL_DESCRIPCION);
			sbSql.append(" , ttl.");sbSql.append(JdbcConstantes.TTL_SEXO);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_TITULO);sbSql.append(" ttl ");sbSql.append(" , ");
			sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr ");sbSql.append(" , ");
			sbSql.append(JdbcConstantes.TABLA_CARRERA);sbSql.append(" crr ");
			sbSql.append(" WHERE ");
			sbSql.append(" ttl.");sbSql.append(JdbcConstantes.TTL_ID);sbSql.append(" = ");
			sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_TTL_ID);
			sbSql.append(" AND ");
			sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" = ");
			sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);
			sbSql.append(" AND ");sbSql.append(" crr.");sbSql.append(JdbcConstantes.CRR_ID);sbSql.append(" IN ( ");
				for( int i = 0; i< listaCarrerasConsulta.size(); i++){
					sbSql.append(" ? ");
			        	if(i != listaCarrerasConsulta.size() -1) {
			        	 sbSql.append(","); 
			        	}
			     }
					sbSql.append(" ) ");
			
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.TTL_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, carreraId); //cargo el id del titulo
			int contador = 0;
		
			for (Integer item : listaCarrerasConsulta) {
				contador++;
				pstmt.setInt(contador, item); //cargo las carreras ids
			}
		
			rs = pstmt.executeQuery();
			retorno = new ArrayList<TituloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
			
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new TituloDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TituloJdbc.sql.exception")));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new TituloDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TituloJdbc.buscar.por.carrera.todos.exception")));
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
			throw new TituloDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TituloJdbc.buscar.por.carrera.no.result.exception")));
		}	
		return retorno;
	}

	/**
	 * Realiza la busqueda de todos los títulos por carrera, modalidad y vigencia de la aplicacion
	 * @return Lista todos los títulos por carrera, modalidad y vigencia de la aplicacion
	 * @throws TituloDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws TituloDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<TituloDto> listarTitulosXCarreraXModaldiadXVigencia(int carreraId,int modalidadId, int vigenciaId) throws TituloDtoJdbcException, TituloDtoJdbcNoEncontradoException{
		List<TituloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" ttl.");sbSql.append(JdbcConstantes.TTL_ID);
			sbSql.append(" , ttl.");sbSql.append(JdbcConstantes.TTL_DESCRIPCION);
			sbSql.append(" , ttl.");sbSql.append(JdbcConstantes.TTL_SEXO);
			sbSql.append(" , ttl.");sbSql.append(JdbcConstantes.TTL_ESTADO);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr , ");
			sbSql.append(JdbcConstantes.TABLA_TITULO);sbSql.append(" ttl ");
			sbSql.append(" WHERE ");
			sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_TTL_ID);sbSql.append(" = ");
			sbSql.append(" ttl.");sbSql.append(JdbcConstantes.TTL_ID);
			sbSql.append(" and cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);sbSql.append(" = ? ");
			sbSql.append(" and cncr.");sbSql.append(JdbcConstantes.CNCR_MDL_ID);sbSql.append(" = ? ");
			sbSql.append(" and cncr.");sbSql.append(JdbcConstantes.CNCR_VGN_ID);sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");sbSql.append(" ttl.");sbSql.append(JdbcConstantes.TTL_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, carreraId); //cargo el id del titulo
			pstmt.setInt(2, modalidadId); //cargo el id del titulo
			pstmt.setInt(3, vigenciaId); //cargo el id del titulo
			rs = pstmt.executeQuery();
			retorno = new ArrayList<TituloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new TituloDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TituloJdbc.sql.exception")));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new TituloDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TituloJdbc.buscar.por.crr.por.mdl.por.vgn.exception")));
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
			throw new TituloDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TituloJdbc.buscar.por.crr.por.mdl.por.vgn.no.result.exception")));
		}	
		return retorno;
	}
	
	
	/**
	 * Realiza la busqueda de todos los títulos por carrera, modalidad, vigencia y sexo de la aplicacion
	 * @return Lista todos los títulos por carrera, modalidad, vigencia y sexo de la aplicacion
	 * @throws TituloDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws TituloDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<TituloDto> listarTitulosXCarreraXModaldiadXVigenciaXSexo(int carreraId,int modalidadId, int vigenciaId, int tipoSexo) throws TituloDtoJdbcException, TituloDtoJdbcNoEncontradoException{
		List<TituloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" ttl.");sbSql.append(JdbcConstantes.TTL_ID);
			sbSql.append(" , ttl.");sbSql.append(JdbcConstantes.TTL_DESCRIPCION);
			sbSql.append(" , ttl.");sbSql.append(JdbcConstantes.TTL_SEXO);
			sbSql.append(" , ttl.");sbSql.append(JdbcConstantes.TTL_ESTADO);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_CONFIGURACION_CARRERA);sbSql.append(" cncr , ");
			sbSql.append(JdbcConstantes.TABLA_TITULO);sbSql.append(" ttl ");
			sbSql.append(" WHERE ");
			sbSql.append(" cncr.");sbSql.append(JdbcConstantes.CNCR_TTL_ID);sbSql.append(" = ");
			sbSql.append(" ttl.");sbSql.append(JdbcConstantes.TTL_ID);
			sbSql.append(" and cncr.");sbSql.append(JdbcConstantes.CNCR_CRR_ID);sbSql.append(" = ? ");
			sbSql.append(" and cncr.");sbSql.append(JdbcConstantes.CNCR_MDL_ID);sbSql.append(" = ? ");
			sbSql.append(" and cncr.");sbSql.append(JdbcConstantes.CNCR_VGN_ID);sbSql.append(" = ? ");
			sbSql.append(" and ttl.");sbSql.append(JdbcConstantes.TTL_SEXO);sbSql.append(" = ? ");
			sbSql.append(" ORDER BY ");sbSql.append(" ttl.");sbSql.append(JdbcConstantes.TTL_DESCRIPCION);
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, carreraId); //cargo el id de la carrera
			pstmt.setInt(2, modalidadId); //cargo el id de la modalidad
			pstmt.setInt(3, vigenciaId); //cargo el id de la vigencia
			pstmt.setInt(4, tipoSexo); //cargo el id del tipo de sexo
			rs = pstmt.executeQuery();
			retorno = new ArrayList<TituloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new TituloDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TituloJdbc.sql.exception")));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new TituloDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TituloJdbc.listar.titulos.por.crr.mdl.vgn.sexo.exception")));
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
			throw new TituloDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TituloJdbc.listar.titulos.por.crr.mdl.vgn.sexo.no.result.exception")));
		}	
		return retorno;
	}
	
	/**
	 * Realiza la busqueda de todos los títulos de la aplicacion por el tipo
	 * @param tipoId - id del tipo de titulo que se está listando
	 * @return Lista todos los títulos de la aplicacion
	 * @throws TituloDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws TituloDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<TituloDto> listarXtipo(int tipoId) throws TituloDtoJdbcException, TituloDtoJdbcNoEncontradoException{
		List<TituloDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" ttl.");sbSql.append(JdbcConstantes.TTL_ID);
			sbSql.append(" , ttl.");sbSql.append(JdbcConstantes.TTL_DESCRIPCION);
				sbSql.append(" , CASE WHEN ttl.");sbSql.append(JdbcConstantes.TTL_SEXO); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
				sbSql.append(" ELSE ttl.");sbSql.append(JdbcConstantes.TTL_SEXO); 
				sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TTL_SEXO);
				sbSql.append(" , CASE WHEN ttl.");sbSql.append(JdbcConstantes.TTL_TIPO); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
				sbSql.append(" ELSE ttl.");sbSql.append(JdbcConstantes.TTL_TIPO); 
				sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TTL_TIPO);
				sbSql.append(" , CASE WHEN ttl.");sbSql.append(JdbcConstantes.TTL_ESTADO); sbSql.append(" IS NULL THEN ");sbSql.append(GeneralesConstantes.APP_ID_BASE);
				sbSql.append(" ELSE ttl.");sbSql.append(JdbcConstantes.TTL_ESTADO); 
				sbSql.append(" END AS ");sbSql.append(JdbcConstantes.TTL_ESTADO);
			sbSql.append(" FROM ");sbSql.append(JdbcConstantes.TABLA_TITULO);sbSql.append(" ttl ");
			sbSql.append(" WHERE ");sbSql.append(" ttl.");sbSql.append(JdbcConstantes.TTL_TIPO);sbSql.append(" = ?");
			sbSql.append(" ORDER BY ");sbSql.append(JdbcConstantes.TTL_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, tipoId); //cargo el id del tipo de titulo
			rs = pstmt.executeQuery();
			retorno = new ArrayList<TituloDto>();
			while(rs.next()){
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new TituloDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TituloJdbc.sql.exception")));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new TituloDtoJdbcException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TituloJdbc.buscar.todos.exception")));
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
			throw new TituloDtoJdbcNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("TituloJdbc.buscar.todos.no.result.exception")));
		}	
		return retorno;
	}
	
	
	/* ********************************************************************************* *
	 * ************************** METODOS DE TRANSFORMACION **************************** *
	 * ********************************************************************************* */
	private TituloDto transformarResultSetADto(ResultSet rs) throws SQLException{
		TituloDto retorno = new TituloDto();
		retorno.setTtlId(rs.getInt(JdbcConstantes.TTL_ID));
		retorno.setTtlDescripcion(rs.getString(JdbcConstantes.TTL_DESCRIPCION));
		retorno.setTtlSexo((rs.getInt(JdbcConstantes.TTL_SEXO))==GeneralesConstantes.APP_ID_BASE?10:(rs.getInt(JdbcConstantes.TTL_SEXO)));
		retorno.setTtlEstado((rs.getInt(JdbcConstantes.TTL_ESTADO))==GeneralesConstantes.APP_ID_BASE?10:(rs.getInt(JdbcConstantes.TTL_ESTADO)));
		retorno.setTtlTipo(rs.getInt(JdbcConstantes.TTL_TIPO)==GeneralesConstantes.APP_ID_BASE?10:(rs.getInt(JdbcConstantes.TTL_TIPO)));
		return retorno;
	} 
	
}
