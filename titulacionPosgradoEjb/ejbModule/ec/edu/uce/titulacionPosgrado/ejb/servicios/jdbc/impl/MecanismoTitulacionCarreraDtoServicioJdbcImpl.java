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
   
 ARCHIVO:     MecanismoTitulacionCarreraDtoServicioJdbc.java	  
 DESCRIPCION: Clase donde se implementan los metodos para el servicio jdbc de la tabla mecanismo_titulacion_carrera.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 03-AGO-2016		Daniel Albuja					       Emisión Inicial
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

import ec.edu.uce.titulacionPosgrado.ejb.dtos.MecanismoTitulacionCarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.MecanismoTitulacionCarreraDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.MecanismoTitulacionCarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.MecanismoTitulacionCarreraException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.MecanismoTitulacionCarreraNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.MecanismoTitulacionCarreraDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.MecanismoTitulacionCarreraConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;

/**
 * EJB MecanismoTitulacionCarreraDtoServicioJdbc. Clase donde se implementan los
 * metodos para el servicio jdbc de la tabla mecanismo_titulacion_carrera.
 * 
 * @author dalbuja
 * @version 1.0
 */
@Stateless
public class MecanismoTitulacionCarreraDtoServicioJdbcImpl implements
		MecanismoTitulacionCarreraDtoServicioJdbc {

	@Resource(mappedName = GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;

	@PersistenceContext(unitName = GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/* ********************************************************************************* *
	 * ************************** METODOS DE CONSULTA
	 * ********************************** *
	 * *************************************
	 * ********************************************
	 */
	/**
	 * Realiza la busqueda de un mecanismo de titulacion carrera por id
	 * 
	 * @param mecTitulId
	 *            - id del mecanismo de titulacion carrera
	 * @return Mecanismo de titulacion con el id solicitado
	 * @throws MecanismoTitulacionCarreraDtoJdbcException
	 *             - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MecanismoTitulacionCarreraDtoJdbcNoEncontradoException
	 *             - Lanzada cuando la busqueda no retorna resultados
	 */
	public MecanismoTitulacionCarreraDto buscarXId(int mecTitulCarreraId)
			throws MecanismoTitulacionCarreraDtoJdbcException,
			MecanismoTitulacionCarreraDtoJdbcNoEncontradoException {
		MecanismoTitulacionCarreraDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" mcttcr.");
			sbSql.append(JdbcConstantes.MCCR_ID);
			sbSql.append(" , mcttcr.");
			sbSql.append(JdbcConstantes.MCCR_ESTADO);
			sbSql.append(" , mcttcr.");
			sbSql.append(JdbcConstantes.MCCR_CRR_ID);
			sbSql.append(" , mcttcr.");
			sbSql.append(JdbcConstantes.MCCR_MCTT_ID);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION);
			sbSql.append(" mcttcr ");
			sbSql.append(" WHERE ");
			sbSql.append(" mcttcr.");
			sbSql.append(JdbcConstantes.MCCR_ID);
			sbSql.append(" = ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, mecTitulCarreraId); // cargo el id del mecanismo de
												// titulacion carrera
			rs = pstmt.executeQuery();
			if (rs.next()) {
				retorno = transformarResultSetADto(rs);
			} else {
				retorno = null;
			}
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new MecanismoTitulacionCarreraDtoJdbcException(
					MensajeGeneradorUtilidades
							.getMsj(new MensajeGeneradorUtilidades(
									"MecanismoTitulacionCarreraJdbc.sql.exception")));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new MecanismoTitulacionCarreraDtoJdbcException(
					MensajeGeneradorUtilidades
							.getMsj(new MensajeGeneradorUtilidades(
									"MecanismoTitulacionCarreraJdbc.buscar.por.id.exception")));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}

		if (retorno == null) {
			throw new MecanismoTitulacionCarreraDtoJdbcNoEncontradoException(
					MensajeGeneradorUtilidades
							.getMsj(new MensajeGeneradorUtilidades(
									"MecanismoTitulacionCarreraJdbc.buscar.por.id.no.result.exception",
									mecTitulCarreraId)));
		}

		return retorno;
	}

	/**
	 * Realiza la busqueda de todos los mecanismos de titulacion carrera de la
	 * aplicacion
	 * 
	 * @return Lista todos los mecanismos de titulacion de la aplicacion
	 * @throws MecanismoTitulacionCarreraDtoJdbcException
	 *             - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MecanismoTitulacionCarreraDtoJdbcNoEncontradoException
	 *             - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<MecanismoTitulacionCarreraDto> listarTodos()
			throws MecanismoTitulacionCarreraDtoJdbcException,
			MecanismoTitulacionCarreraDtoJdbcNoEncontradoException {
		List<MecanismoTitulacionCarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" mcttcr.");
			sbSql.append(JdbcConstantes.MCCR_ID);
			sbSql.append(" , mcttcr.");
			sbSql.append(JdbcConstantes.MCCR_ESTADO);
			sbSql.append(" , mcttcr.");
			sbSql.append(JdbcConstantes.MCCR_CRR_ID);
			sbSql.append(" , mcttcr.");
			sbSql.append(JdbcConstantes.MCCR_MCTT_ID);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION);
			sbSql.append(" mcttcr ");
			sbSql.append(" ORDER BY ");
			sbSql.append(JdbcConstantes.MCTT_DESCRIPCION);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			rs = pstmt.executeQuery();
			retorno = new ArrayList<MecanismoTitulacionCarreraDto>();
			while (rs.next()) {
				retorno.add(transformarResultSetADto(rs));
			}
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new MecanismoTitulacionCarreraDtoJdbcException(
					MensajeGeneradorUtilidades
							.getMsj(new MensajeGeneradorUtilidades(
									"MecanismoTitulacionCarreraJdbc.sql.exception")));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new MecanismoTitulacionCarreraDtoJdbcException(
					MensajeGeneradorUtilidades
							.getMsj(new MensajeGeneradorUtilidades(
									"MecanismoTitulacionCarreraJdbc.buscar.todos.exception")));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}

		if (retorno == null || retorno.size() <= 0) {
			throw new MecanismoTitulacionCarreraDtoJdbcNoEncontradoException(
					MensajeGeneradorUtilidades
							.getMsj(new MensajeGeneradorUtilidades(
									"MecanismoTitulacionCarreraJdbc.buscar.todos.no.result.exception")));
		}
		return retorno;
	}

	/**
	 * Realiza la busqueda de un mecanismo de titulacion carrera por id de la
	 * carrera
	 * 
	 * @param mecTitulId
	 *            - id del mecanismo de titulacion carrera
	 * @return Mecanismo de titulacion con el id solicitado
	 * @throws MecanismoTitulacionCarreraDtoJdbcException
	 *             - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws MecanismoTitulacionCarreraDtoJdbcNoEncontradoException
	 *             - Lanzada cuando la busqueda no retorna resultados
	 */
	public MecanismoTitulacionCarreraDto buscarXCarreraMecanismoExamenComplexivo(
			int carreraId) throws MecanismoTitulacionCarreraDtoJdbcException,
			MecanismoTitulacionCarreraDtoJdbcNoEncontradoException {
		MecanismoTitulacionCarreraDto retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" mcttcr.");
			sbSql.append(JdbcConstantes.MCCR_ID);
			sbSql.append(" , mcttcr.");
			sbSql.append(JdbcConstantes.MCCR_ESTADO);
			sbSql.append(" , mcttcr.");
			sbSql.append(JdbcConstantes.MCCR_CRR_ID);
			sbSql.append(" , mcttcr.");
			sbSql.append(JdbcConstantes.MCCR_MCTT_ID);
			sbSql.append(" , CASE WHEN mcttcr.");
			sbSql.append(JdbcConstantes.MCCR_PORCENTAJE);
			sbSql.append(" IS NULL THEN ");
			sbSql.append(GeneralesConstantes.APP_ID_BASE.intValue());
			sbSql.append(" ELSE mcttcr.");
			sbSql.append(JdbcConstantes.MCCR_PORCENTAJE);
			sbSql.append(" END AS ");
			sbSql.append(JdbcConstantes.MCCR_PORCENTAJE);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MECANISMO_CARRERA);
			sbSql.append(" mcttcr ");
			sbSql.append(" WHERE ");
			sbSql.append(" mcttcr.");
			sbSql.append(JdbcConstantes.MCCR_CRR_ID);
			sbSql.append(" = ? ");
			sbSql.append(" AND mcttcr.");
			sbSql.append(JdbcConstantes.MCCR_MCTT_ID);
			sbSql.append(" = ");
			sbSql.append(MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_VALUE);
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, carreraId); // cargo el id del mecanismo de
										// titulacion carrera
			rs = pstmt.executeQuery();
			if (rs.next()) {
				retorno = transformarResultSetADtoMCTTCR(rs);
			} else {
				retorno = null;
			}
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new MecanismoTitulacionCarreraDtoJdbcException(
					MensajeGeneradorUtilidades
							.getMsj(new MensajeGeneradorUtilidades(
									"MecanismoTitulacionCarreraJdbc.sql.exception")));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new MecanismoTitulacionCarreraDtoJdbcException(
					MensajeGeneradorUtilidades
							.getMsj(new MensajeGeneradorUtilidades(
									"MecanismoTitulacionCarreraJdbc.buscar.por.id.exception")));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}

		if (retorno == null) {
			throw new MecanismoTitulacionCarreraDtoJdbcNoEncontradoException(
					MensajeGeneradorUtilidades
							.getMsj(new MensajeGeneradorUtilidades(
									"MecanismoTitulacionCarreraJdbc.buscar.por.carrera.no.result.exception",
									carreraId)));
		}

		return retorno;
	}

	@Override
	public Integer buscarOpcionMecanismoXfcesId(int fcesId)
			throws MecanismoTitulacionCarreraDtoJdbcException,
			MecanismoTitulacionCarreraDtoJdbcNoEncontradoException {
		Integer retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT ");
			sbSql.append(" mcttcr.");
			sbSql.append(JdbcConstantes.MCCR_ID);
			sbSql.append(" FROM ");
			sbSql.append(JdbcConstantes.TABLA_MECANISMO_CARRERA);
			sbSql.append(" mcttcr, ");
			sbSql.append(JdbcConstantes.TABLA_FICHA_ESTUDIANTE);
			sbSql.append(" fces ");
			sbSql.append(" WHERE ");
			sbSql.append(" mcttcr.");
			sbSql.append(JdbcConstantes.MCCR_ID);
			sbSql.append(" = ");
			sbSql.append(" fces.");
			sbSql.append(JdbcConstantes.FCES_MCCR_ID);
			sbSql.append(" AND ");
			sbSql.append(" fces.");
			sbSql.append(JdbcConstantes.FCES_ID);
			sbSql.append(" = ? ");
			con = ds.getConnection();
			pstmt = con.prepareStatement(sbSql.toString());
			pstmt.setInt(1, fcesId); // cargo el id del mecanismo de
												// titulacion carrera
			rs = pstmt.executeQuery();
			if (rs.next()) {
				retorno = 1;
			} else {
				retorno = null;
			}
		} catch (SQLException e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new MecanismoTitulacionCarreraDtoJdbcException(
					MensajeGeneradorUtilidades
							.getMsj(new MensajeGeneradorUtilidades(
									"MecanismoTitulacionCarreraJdbc.sql.exception")));
		} catch (Exception e) {
			try {
				con.close();
			} catch (Exception e2) {
			}
			throw new MecanismoTitulacionCarreraDtoJdbcException(
					MensajeGeneradorUtilidades
							.getMsj(new MensajeGeneradorUtilidades(
									"MecanismoTitulacionCarreraJdbc.buscar.por.id.exception")));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}

		if (retorno == null) {
			throw new MecanismoTitulacionCarreraDtoJdbcNoEncontradoException(
					MensajeGeneradorUtilidades
							.getMsj(new MensajeGeneradorUtilidades(
									"MecanismoTitulacionCarreraJdbc.buscar.por.id.no.result.exception",
									fcesId)));
		}

		return retorno;
	}
	
	public List<MecanismoTitulacionCarreraDto> listar(Integer crrId)
			throws MecanismoTitulacionCarreraNoEncontradoException,
			MecanismoTitulacionCarreraException {
		List<MecanismoTitulacionCarreraDto> retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;

		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT ");
			sql.append(" mcttcr.");
			sql.append(JdbcConstantes.MCCR_ID);
			sql.append(" ,mcttcr.");
			sql.append(JdbcConstantes.MCCR_ESTADO);
			sql.append(" ,mcttcr.");
			sql.append(JdbcConstantes.MCCR_CRR_ID);
			sql.append(" ,crr.");
			sql.append(JdbcConstantes.CRR_DESCRIPCION);
			sql.append(" ,crr.");
			sql.append(JdbcConstantes.CRR_NIVEL);
			sql.append(" ,crr.");
			sql.append(JdbcConstantes.CRR_DETALLE);
			sql.append(" ,fcl.");
			sql.append(JdbcConstantes.FCL_ID);
			sql.append(" ,fcl.");
			sql.append(JdbcConstantes.FCL_DESCRIPCION);
			sql.append(" ,mctt.");
			sql.append(JdbcConstantes.MCTT_ID);
			sql.append(" ,mctt.");
			sql.append(JdbcConstantes.MCTT_CODIGO_SNIESE);
			sql.append(" ,mctt.");
			sql.append(JdbcConstantes.MCTT_DESCRIPCION);
			sql.append(" ,mctt.");
			sql.append(JdbcConstantes.MCTT_ESTADO);

			sql.append(" FROM ");
			sql.append(JdbcConstantes.TABLA_MECANISMO_CARRERA);
			sql.append(" mcttcr ");
			sql.append(" RIGHT JOIN ");
			sql.append(JdbcConstantes.TABLA_MECANISMO_TITULACION);
			sql.append(" mctt ON mcttcr.");
			sql.append(JdbcConstantes.MCCR_MCTT_ID);
			sql.append(" =  mctt.");
			sql.append(JdbcConstantes.MCTT_ID);
			sql.append(" RIGHT JOIN ");
			sql.append(JdbcConstantes.TABLA_CARRERA);
			sql.append(" crr on mcttcr.");
			sql.append(JdbcConstantes.MCCR_CRR_ID);
			sql.append(" = crr.");
			sql.append(JdbcConstantes.CRR_ID);
			sql.append(" RIGHT JOIN ");
			sql.append(JdbcConstantes.TABLA_FACULTAD);
			sql.append(" fcl on crr.");
			sql.append(JdbcConstantes.CRR_FCL_ID);
			sql.append(" = fcl.");
			sql.append(JdbcConstantes.FCL_ID);
			sql.append(" WHERE ");
			sql.append(" crr.");
			sql.append(JdbcConstantes.CRR_ID);
			sql.append(" = ?");
			sql.append(" ORDER BY mctt.");
			sql.append(JdbcConstantes.MCTT_DESCRIPCION);

			con = ds.getConnection();
			pstmt = con.prepareStatement(sql.toString());

			pstmt.setInt(1, crrId.intValue());

			rs = pstmt.executeQuery();
			retorno = new ArrayList<MecanismoTitulacionCarreraDto>();
			while (rs.next()) {
				retorno.add(transformarResultSetADtoListMecanismos(rs));
			}

		} catch (SQLException e) {
		} catch (Exception e) {
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
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
	 * ************************** METODOS DE TRANSFORMACION
	 * **************************** *
	 * *******************************************
	 * **************************************
	 */
	private MecanismoTitulacionCarreraDto transformarResultSetADto(ResultSet rs)
			throws SQLException {
		MecanismoTitulacionCarreraDto retorno = new MecanismoTitulacionCarreraDto();
		retorno.setMcttcrId(rs.getInt(JdbcConstantes.MCCR_ID));
		retorno.setMcttcrEstado(rs.getInt(JdbcConstantes.MCCR_ESTADO));
		retorno.setMcttcrCrrId(rs.getInt(JdbcConstantes.MCCR_CRR_ID));
		retorno.setMcttcrMcttId(rs.getInt(JdbcConstantes.MCCR_MCTT_ID));
		return retorno;
	}

	private MecanismoTitulacionCarreraDto transformarResultSetADtoMCTTCR(
			ResultSet rs) throws SQLException {
		MecanismoTitulacionCarreraDto retorno = new MecanismoTitulacionCarreraDto();
		retorno.setMcttcrId(rs.getInt(JdbcConstantes.MCCR_ID));
		retorno.setMcttcrEstado(rs.getInt(JdbcConstantes.MCCR_ESTADO));
		retorno.setMcttcrCrrId(rs.getInt(JdbcConstantes.MCCR_CRR_ID));
		retorno.setMcttcrMcttId(rs.getInt(JdbcConstantes.MCCR_MCTT_ID));
		retorno.setMcttcrPorcentajeComplexivo(rs
				.getInt(JdbcConstantes.MCCR_PORCENTAJE));
		return retorno;
	}

	private MecanismoTitulacionCarreraDto transformarResultSetADtoListMecanismos(
			ResultSet rs) throws SQLException {

		MecanismoTitulacionCarreraDto retorno = new MecanismoTitulacionCarreraDto();
		retorno.setMcttcrId(rs.getInt(JdbcConstantes.MCCR_ID));
		retorno.setMcttcrEstado(rs.getInt(JdbcConstantes.MCCR_ESTADO));
		retorno.setMcttcrCrrId(rs.getInt(JdbcConstantes.MCCR_CRR_ID));
		retorno.setCrrDescripcion(rs.getString(JdbcConstantes.CRR_DESCRIPCION));
		retorno.setCrrNivel(rs.getInt(JdbcConstantes.CRR_NIVEL));
		retorno.setCrrDetalle(rs.getString(JdbcConstantes.CRR_DETALLE));
		retorno.setFclId(rs.getInt(JdbcConstantes.FCL_ID));
		retorno.setFclDescripcion(rs.getString(JdbcConstantes.FCL_DESCRIPCION));
		retorno.setMcttId(rs.getInt(JdbcConstantes.MCTT_ID));
		retorno.setMcttCodigoSniese(rs
				.getString(JdbcConstantes.MCTT_CODIGO_SNIESE));
		retorno.setMcttDescripcion(rs
				.getString(JdbcConstantes.MCTT_DESCRIPCION));
		retorno.setMcttEstado(rs.getInt(JdbcConstantes.MCTT_ESTADO));

		return retorno;
	}

}
