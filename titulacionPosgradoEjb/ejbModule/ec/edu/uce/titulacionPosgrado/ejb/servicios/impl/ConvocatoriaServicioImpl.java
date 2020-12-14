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
   
 ARCHIVO:     ConvocatoriaServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla Convocatoria. 
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 20/02/2018           Daniel Albuja                      Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.titulacionPosgrado.ejb.servicios.impl;


import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ConvocatoriaException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ConvocatoriaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ConvocatoriaValidacionException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.ConvocatoriaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.ConvocatoriaConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Convocatoria;

/**
 * Clase (Bean)ConvocatoriaServicioImpl.
 * Bean declarado como Stateless.
 * @author dalbuja
 * @version 1.0
 */

@Stateless
public class ConvocatoriaServicioImpl implements ConvocatoriaServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad Convocatoria por su id
	 * @param id - de la Convocatoria a buscar
	 * @return Convocatoria con el id solicitado
	 * @throws ConvocatoriaNoEncontradoException - Excepcion lanzada cuando no se encuentra una Convocatoria con el id solicitado
	 * @throws ConvocatoriaException - Excepcion general
	 */
	@Override
	public Convocatoria buscarPorId(Integer id) throws ConvocatoriaNoEncontradoException, ConvocatoriaException {
		Convocatoria retorno = null;
		if (id != null) {
			try {
				retorno = em.find(Convocatoria.class, id);
			} catch (NoResultException e) {
				throw new ConvocatoriaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Convocatoria.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new ConvocatoriaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Convocatoria.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new ConvocatoriaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Convocatoria.buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	
	/**
	 * Lista todas las entidades Convocatoria existentes en la BD
	 * @return lista de todas las entidades Convocatoria existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Convocatoria> listarTodos() throws ConvocatoriaNoEncontradoException{
		List<Convocatoria> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select cnv from Convocatoria cnv ");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		
		if(retorno.size()<=0){
			throw new ConvocatoriaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Convocatoria.buscar.todos.no.result.exception")));
		}
		
		return retorno;
		
	}
	
	
	/**
	 * Lista todas las entidades Convocatoria existentes en la BD
	 * @return lista de todas las entidades Convocatoria existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Convocatoria> listarTodosActivas() throws ConvocatoriaNoEncontradoException{
		List<Convocatoria> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select cnv from Convocatoria cnv where ");
		sbsql.append(" cnv.cnvEstado = :idEstado ");
		Query q = em.createQuery(sbsql.toString());
		q.setParameter("idEstado", ConvocatoriaConstantes.ESTADO_ACTIVO_VALUE);
		retorno = q.getResultList();
		
		if(retorno.size()<=0){
			throw new ConvocatoriaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Convocatoria.buscar.todos.no.result.exception")));
		}
		
		return retorno;
		
	}
	
	/**
	 * Busca una entidad Convocatoria por su estado activo
	 * @param id - de la Convocatoria a buscar
	 * @return Convocatoria con el estado activo solicitado
	 * @throws ConvocatoriaNoEncontradoException - Excepcion lanzada cuando no se encuentra una Convocatoria con el id solicitado
	 * @throws ConvocatoriaException - Excepcion general
	 */
	@Override
	public Convocatoria buscarPorIdPorEstado(Integer idEstado) throws ConvocatoriaNoEncontradoException, ConvocatoriaException{
		Convocatoria retorno = null;
		try {
			StringBuilder ssql = new StringBuilder();
			ssql.append(" Select cnv from Convocatoria cnv where ");
			ssql.append(" cnv.cnvEstado = :idEstado ");
			
			Query q = em.createQuery(ssql.toString());
			q.setParameter("idEstado", idEstado.intValue());
			retorno = (Convocatoria)q.getSingleResult();
			
		} catch (NoResultException e) {
			throw new ConvocatoriaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Convocatoria.buscar.por.id.no.result.exception")));
		}catch (NonUniqueResultException e) {
			throw new ConvocatoriaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Convocatoria.buscar.por.id.non.unique.result.exception")));
		} catch (Exception e) {
			throw new ConvocatoriaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Convocatoria.buscar.por.id.exception")));
		}
		
		return retorno;
	}

	@Override
	public Convocatoria buscarPorIdPorEstadoFaseRegistro(Integer idEstado) throws ConvocatoriaNoEncontradoException, ConvocatoriaException{
		Convocatoria retorno = null;
		try {
			StringBuilder ssql = new StringBuilder();
			ssql.append(" Select cnv from Convocatoria cnv where ");
			ssql.append(" cnv.cnvEstado = :idEstado ");
			ssql.append(" and cnv.cnvEstadoFase = 0 ");
			Query q = em.createQuery(ssql.toString());
			q.setParameter("idEstado", idEstado.intValue());
			retorno = (Convocatoria)q.getSingleResult();
			
		} catch (NoResultException e) {
			throw new ConvocatoriaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Convocatoria.buscar.por.id.no.result.exception")));
		}catch (NonUniqueResultException e) {
			throw new ConvocatoriaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Convocatoria.buscar.por.id.non.unique.result.exception")));
		} catch (Exception e) {
			throw new ConvocatoriaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Convocatoria.buscar.por.id.exception")));
		}
		
		return retorno;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Convocatoria> buscarConvocatoriasPorIdPorEstado(Integer idEstado) throws ConvocatoriaNoEncontradoException, ConvocatoriaException{
		List<Convocatoria> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select cnv from Convocatoria cnv ");
		sbsql.append(" where cnv.cnvEstado = 0 ");
		sbsql.append(" and cnv.cnvEstadoFase = 0 ");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		
		if(retorno.size()<=0){
			throw new ConvocatoriaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Convocatoria.buscar.todos.no.result.exception")));
		}
		
		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Convocatoria> buscarConvocatoriasFaseTitulacionPorId() throws ConvocatoriaNoEncontradoException, ConvocatoriaException{
		List<Convocatoria> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select cnv from Convocatoria cnv ");
		sbsql.append(" where cnv.cnvEstado = 0 ");
		sbsql.append(" and cnv.cnvEstadoFase = 1 ");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		
		if(retorno.size()<=0){
			throw new ConvocatoriaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Convocatoria.buscar.todos.no.result.exception")));
		}
		
		return retorno;
	}
	
	/**
	 * Busca una entidad Convocatoria por su id
	 * @param id - de la Convocatoria a buscar
	 * @return Convocatoria con el estado activo solicitado
	 * @throws ConvocatoriaNoEncontradoException - Excepcion lanzada cuando no se encuentra una Convocatoria con el id solicitado
	 * @throws ConvocatoriaException - Excepcion general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Convocatoria> buscarXId(Integer idConvocatoria) throws ConvocatoriaNoEncontradoException, ConvocatoriaException{
		List<Convocatoria> retorno = null;
		try {
			StringBuilder ssql = new StringBuilder();
			ssql.append(" Select cnv from Convocatoria cnv where ");
			
			if(idConvocatoria.intValue()==GeneralesConstantes.APP_ID_BASE){
				ssql.append(" cnv.cnvId > :idConvocatoria ");
			}else{
				ssql.append(" cnv.cnvId = :idConvocatoria ");
			}
			ssql.append(" order by cnv.cnvEstado asc");
			Query q = em.createQuery(ssql.toString());
			q.setParameter("idConvocatoria", idConvocatoria.intValue());
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new ConvocatoriaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Convocatoria.buscar.x.id.no.result.exception")));
		} catch (Exception e) {
			throw new ConvocatoriaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Convocatoria.buscar.x.id.exception")));
		}
		
		return retorno;
	}
	
	/**
	 * Edita todos los atributos de la entidad Convocatoria
	 * @param entidad - entidad Convocatoria a editar
	 * @return null si no se encuentró la entidad a editar, la entidad Convocatoria editada de lo contrario
	 * @throws ConvocatoriaValidacionException - excepcion de validacion de edicion
	 */
	@Override
	public Convocatoria editar(Convocatoria entidad) throws ConvocatoriaValidacionException , ConvocatoriaNoEncontradoException , ConvocatoriaException{
		Convocatoria retorno = null;
		if(entidad != null)	{
			try {
				retorno = buscarPorId(entidad.getCnvId());
				
				if(retorno != null) {
					try {
						verificar(entidad, GeneralesConstantes.APP_EDITAR);
						retorno.setCnvDescripcion(entidad.getCnvDescripcion());
						retorno.setCnvFechaInicio(entidad.getCnvFechaInicio());
						retorno.setCnvFechaFin(entidad.getCnvFechaFin());
						retorno.setCnvEstado(entidad.getCnvEstado());
						retorno.setCnvEstadoFase(entidad.getCnvEstadoFase());
					} catch (ConvocatoriaValidacionException e) {
						throw e;
					}
				}else{
					throw new ConvocatoriaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Convocatoria.editar.validacion.exception")));
				}
				
			} catch (ConvocatoriaNoEncontradoException e1) {
				throw e1;
			} catch (ConvocatoriaException e1) {
				throw e1;
			}
			
			
		}
		return retorno;
	}

	/**
	 * Reliza verificaciones para el actualizar y el anadir
	 * @param entidad - entidad Convocatoria a ser verificado
	 * @param tipo - tipo de operacion 0.Nuevo   1.Editar
	 * @return true si se verificó correctamente, false de lo contrario
	 * @throws ConvocatoriaValidacionException - excepcion de validacion
	 */
	private void verificar(Convocatoria entidad , int tipo) throws ConvocatoriaValidacionException{
		entidad.setCnvDescripcion(entidad.getCnvDescripcion().toUpperCase().replaceAll(" +", " ").trim());
		if(buscarConvocatoriaXnombre(entidad, tipo) != null){
			throw new ConvocatoriaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Convocatoria.verificar.exception")));
		}
	}
	
	/**
	 * Busca la Convocatoria por nombre y por tipo de operacion 0.nuevo 1.editar
	 * @param convocatoria - entidad convocatoria de la que se extraera el nombre para la consulta
	 * @param tipo - tipo de operacion 0.Nuevo   1.Editar
	 * @return la convocatoria encontrado o null de lo contrario
	 */
	@Override
	public Convocatoria buscarConvocatoriaXnombre(Convocatoria convocatoria, int tipo) {
		Convocatoria retorno = null;
		
		if(convocatoria != null && convocatoria.getCnvDescripcion()!=null) {
			try {
				StringBuilder ssql = new StringBuilder();
				ssql.append(" Select cnv from Convocatoria cnv where ");
				ssql.append(" UPPER(cnv.cnvDescripcion) = :nombreConvocatoria ");
				if(tipo == GeneralesConstantes.APP_EDITAR){
					ssql.append(" AND cnv.cnvId != :idConvocatoria ");
				}
				ssql.append(" order by cnv.cnvId ");
				
				Query query = em.createQuery(ssql.toString());
				query.setParameter("nombreConvocatoria", convocatoria.getCnvDescripcion().toUpperCase());
				if(tipo == GeneralesConstantes.APP_EDITAR){
					query.setParameter("idConvocatoria", convocatoria.getCnvId());
				}
				
				retorno = (Convocatoria)query.getSingleResult();
			} catch (NoResultException e) {
				retorno = null;
			}
		}
		return retorno;
	}
	
	/**
	 * Crea una nueva convocatoria, pone en inactivo a las demás convocatorias
	 * @param convocatoria - entidad convocatoria de la que se extraera el nombre para la consulta
	 * @return la convocatoria encontrado o null de lo contrario
	 */
	@Override
	public Convocatoria crearNuevaConvocatoria(Convocatoria convocatoria){
		Convocatoria retorno = new Convocatoria();
		Query query = em.createQuery(
			      "UPDATE Convocatoria SET cnvEstado = 2, cnvEstadoFase = 1"
			      + " WHERE cnvEstado = 0 AND cnvEstadoFase = 1");
		query.executeUpdate();
		query = em.createQuery(
			      "UPDATE Convocatoria SET cnvEstado = 1, cnvEstadoFase = 1"
			      + " where cnvEstado = 0");
		query.executeUpdate();
		retorno.setCnvDescripcion(convocatoria.getCnvDescripcion());
		retorno.setCnvFechaInicio(convocatoria.getCnvFechaInicio());
		retorno.setCnvFechaFin(convocatoria.getCnvFechaFin());
		retorno.setCnvEstado(GeneralesConstantes.APP_NUEVO);
		retorno.setCnvEstadoFase(GeneralesConstantes.APP_NUEVO);
		em.persist(retorno);
		return retorno;
	}
	
	/**
	 * Busca la convocatoria que se encuentre activa y en estado de ideoneidad
	 * @param convocatoria - entidad convocatoria de la que se extraera el nombre para la consulta
	 * @return la convocatoria encontrado o null de lo contrario
	 */
	@Override
	public Convocatoria buscarConvocatoriaActivaFaseRegistro(){
		Convocatoria retorno = null;
		try {
			StringBuilder ssql = new StringBuilder();
			ssql.append(" Select cnv from Convocatoria cnv where ");
			ssql.append(" cnv.cnvEstado = :estadoConvocatoria ");
			ssql.append(" and cnv.cnvEstadoFase = :estadoFase ");
			
			Query query = em.createQuery(ssql.toString());
			query.setParameter("estadoConvocatoria", ConvocatoriaConstantes.ESTADO_ACTIVO_VALUE);
			query.setParameter("estadoFase", ConvocatoriaConstantes.ESTADO_FASE_REGISTRO_POSTULACION_VALUE);
			
			retorno = (Convocatoria)query.getSingleResult();
		} catch (NoResultException e) {
			retorno = null;
		}
		return retorno;
	}
	
	/**
	 * Busca la convocatoria que se encuentre activa y en estado de idoneidad y méritos
	 * @param convocatoria - entidad convocatoria de la que se extraera el nombre para la consulta
	 * @return la convocatoria encontrado o null de lo contrario
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Convocatoria> buscarConvocatoriasActivaFaseIdoneidad(){
		List<Convocatoria> retorno = null;
		try {
			retorno = new ArrayList<Convocatoria>();
			StringBuilder ssql = new StringBuilder();
			ssql.append(" Select cnv from Convocatoria cnv where ");
			ssql.append(" cnv.cnvEstado = :estadoConvocatoria ");
			ssql.append(" and cnv.cnvEstadoFase = :estadoFase ");
			Query query = em.createQuery(ssql.toString());
			query.setParameter("estadoConvocatoria", ConvocatoriaConstantes.ESTADO_ACTIVO_VALUE);
			query.setParameter("estadoFase", ConvocatoriaConstantes.ESTADO_FASE_TITULACION_VALUE);
			retorno = query.getResultList();
		} catch (NoResultException e) {
			retorno = null;
		}
		return retorno;
	}
	
	
	/**
	 * activa la convocatoria pendiente de activación y la coloca en estado de registro
	 * @param 
	 * @return la convocatoria encontrado o null de lo contrario
	 */
	@Override
	public Convocatoria activarEstadoFaseRegistro(){
		Convocatoria retorno = new Convocatoria();
		Query query = em.createQuery(
			      "UPDATE Convocatoria SET cnvEstadoFase = 0 , cnvEstado = 0"
			      + " WHERE cnvEstado = 1 AND cnvEstadoFase = -99");
		query.executeUpdate();
		return retorno;
	}
	
	/**
	 * modifica la convocatoria activa y en estado de registro a fase de idoneidad
	 * @param 
	 * @return la convocatoria encontrado o null de lo contrario
	 */
	@Override
	public Convocatoria modificarEstadoFaseIdoneidad(){
		Convocatoria retorno = new Convocatoria();
		Query query = em.createQuery(
			      "UPDATE Convocatoria SET cnvEstadoFase = 1"
			      + " WHERE cnvEstado = 0 AND cnvEstadoFase = 0");
		query.executeUpdate();
		return retorno;
	}
	
	
	/**
	 * modifica la convocatoria activa y en estado de oposicion a fase de resultados
	 * @param 
	 * @return la convocatoria encontrado o null de lo contrario
	 */
	@Override
	public Convocatoria modificarEstadoFaseResultados(){
		Convocatoria retorno = new Convocatoria();
		Query query = em.createQuery(
			      "UPDATE Convocatoria SET cnvEstadoFase = 3, cnvEstado = 1"
			      + " WHERE cnvEstado = 0 AND cnvEstadoFase = 2");
		query.executeUpdate();
		return retorno;		
		
	}

	
	/**
	 * Busca la convocatoria que se encuentre activa 
	 * @return la convocatoria encontrado o null de lo contrario
	 */
	@Override
	public Convocatoria buscarConvocatoriaActiva(){
		Convocatoria retorno = null;
		try {
			StringBuilder ssql = new StringBuilder();
			ssql.append(" Select cnv from Convocatoria cnv where ");
			ssql.append(" cnv.cnvEstado = :estadoConvocatoria ");
			
			Query query = em.createQuery(ssql.toString());
			query.setParameter("estadoConvocatoria", ConvocatoriaConstantes.ESTADO_ACTIVO_VALUE);
			
			retorno = (Convocatoria)query.getSingleResult();
		} catch (NoResultException e) {
			retorno = null;
		}
		return retorno;
	}
	
}
