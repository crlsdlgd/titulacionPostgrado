/**************************************************************************
 *                (c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción distribución no autorizada de este programa, 
 * o cualquier porción de  él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán  procesadas con el grado  máximo contemplado 
 * por la ley.
 
 ************************************************************************* 
   
 ARCHIVO:     TituloServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla Titulo. 
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 06-Mayo-2016           Gabriel Mafla                      Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.titulacionPosgrado.ejb.servicios.impl;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.RegistroSenescytDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.TituloException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.TituloNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.TituloServicio;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.JdbcConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.TituloConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Titulo;

/**
 * Clase (Bean)TituloServicioImpl.
 * Bean declarado como Stateless.
 * @author gmafla
 * @version 1.0
 */

@Stateless
public class TituloServicioImpl implements TituloServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;
	/**
	 * Busca una entidad Titulo por su id
	 * @param id - de la Titulo a buscar
	 * @return Titulo con el id solicitado
	 * @throws TituloNoEncontradoException - Excepcion lanzada cuando no se encuentra una Titulo con el id solicitado
	 * @throws TituloException - Excepcion general
	 */
	@Override
	public Titulo buscarPorId(Integer id) throws TituloNoEncontradoException, TituloException {
		Titulo retorno = null;
		if (id != null) {
			try {
				retorno = em.find(Titulo.class, id);
			} catch (NoResultException e) {
				throw new TituloNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Titulo.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new TituloException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Titulo.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new TituloException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Titulo.buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	
	/**
	 * Lista todas las entidades Titulo existentes en la BD
	 * @return lista de todas las entidades Titulo existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Titulo> listarTodos() throws TituloNoEncontradoException{
		List<Titulo> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select ttl from Titulo ttl ");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		
		if(retorno.size()<=0){
			throw new TituloNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Titulo.buscar.todos.no.result.exception")));
		}
		
		return retorno;
		
	}

	/**
	 * Busca una entidad Titulo por su id
	 * @param id - de la Titulo a buscar
	 * @return Titulo con el id solicitado
	 * @throws TituloNoEncontradoException - Excepcion lanzada cuando no se encuentra una Titulo con el id solicitado
	 * @throws TituloException - Excepcion general
	 */
	@Override
	public Titulo buscarPorDescripcion(String descripcion) throws TituloNoEncontradoException, TituloException {
		Titulo retorno = null;
		if (descripcion!=null) {
			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select ttl from Titulo ttl ");
				sbsql.append(" where  ttlDescripcion like :descripcion");
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("descripcion", descripcion);
				retorno = (Titulo) q.getSingleResult();
			}  catch (Exception e) {
				throw new TituloException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Titulo.buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	
	@Override
	public Titulo crearNuevoTitulo(RegistroSenescytDto nuevoTitulo) throws TituloNoEncontradoException, TituloException {
		Titulo retorno = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement pstmt1 = null;
			try {
				Titulo ttl = new Titulo();
				con = ds.getConnection();
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select max(ttl_id) as id from Titulo");
				pstmt = con.prepareStatement(sbsql.toString());
				rs = pstmt.executeQuery();
				rs.next();
				Integer id = rs.getInt("id");
				id=id+1;
				ttl.setTtlId(id);
				ttl.setTtlDescripcion(nuevoTitulo.getDetalleSenescyt());
				
				StringBuilder sbSql = new StringBuilder();
				sbSql.append(" INSERT INTO TITULO VALUES ");
				sbSql.append(" (");sbSql.append(ttl.getTtlId());
				sbSql.append(" , '");sbSql.append(nuevoTitulo.getDetalleSenescyt());
				sbSql.append("' , ");sbSql.append(nuevoTitulo.getSexo());
				sbSql.append(" , 0,");sbSql.append(TituloConstantes.TIPO_TITULO_UNIVERSIDAD_VALUE);
				sbSql.append(" ) ");
				System.out.println(sbSql);
				
				pstmt1 = con.prepareStatement(sbSql.toString());
				pstmt1.execute();
				
				
				retorno=ttl;
			}  catch (Exception e) {
				e.printStackTrace();
				try {
					if (rs != null){
						rs.close();
					}
					if (pstmt1 != null){
						pstmt1.close();
					}
					if (pstmt != null){
						pstmt.close();
					}
					if (con != null) {
						con.close();
					}
				} catch (SQLException e1) {
				}
				throw new TituloException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Titulo.buscar.por.id.exception")));
			}finally{
				try {
					if (rs != null){
						rs.close();
					}
					if (pstmt1 != null){
						pstmt1.close();
					}
					if (pstmt != null){
						pstmt.close();
					}
					if (con != null) {
						con.close();
					}
				} catch (SQLException e1) {
				}
			}
		return retorno;
	}
	
}
