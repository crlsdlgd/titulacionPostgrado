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
   
 ARCHIVO:     MecanismoTitulacionServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla MecanismoTitulacion. 
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 06-Mayo-2016           Gabriel Mafla                      Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.titulacionPosgrado.ejb.servicios.impl;


import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.MecanismoTitulacionException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.MecanismoTitulacionNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.MecanismoTitulacionServicio;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.MecanismoTitulacion;

/**
 * Clase (Bean)MecanismoTitulacionServicioImpl.
 * Bean declarado como Stateless.
 * @author gmafla
 * @version 1.0
 */

@Stateless
public class MecanismoTitulacionServicioImpl implements MecanismoTitulacionServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad MecanismoTitulacion por su id
	 * @param id - de la MecanismoTitulacion a buscar
	 * @return MecanismoTitulacion con el id solicitado
	 * @throws MecanismoTitulacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una MecanismoTitulacion con el id solicitado
	 * @throws MecanismoTitulacionException - Excepcion general
	 */
	@Override
	public MecanismoTitulacion buscarPorId(Integer id) throws MecanismoTitulacionNoEncontradoException, MecanismoTitulacionException {
		MecanismoTitulacion retorno = null;
		if (id != null) {
			try {
				retorno = em.find(MecanismoTitulacion.class, id);
			} catch (NoResultException e) {
				throw new MecanismoTitulacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MecanismoTitulacion.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new MecanismoTitulacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MecanismoTitulacion.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new MecanismoTitulacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MecanismoTitulacion.buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	
	/**
	 * Lista todas las entidades MecanismoTitulacion existentes en la BD
	 * @return lista de todas las entidades MecanismoTitulacion existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<MecanismoTitulacion> listarTodos() throws MecanismoTitulacionNoEncontradoException{
		List<MecanismoTitulacion> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select mctt from MecanismoTitulacion mctt ");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		
		if(retorno.size()<=0){
			throw new MecanismoTitulacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MecanismoTitulacion.buscar.todos.no.result.exception")));
		}
		
		return retorno;
		
	}

	/**
	 * Busca los Mecanismos de Titulación que coinciden con el patrón ingresado. 
	 * @param patron -> String que contiene el Mecanismo de Titulación que desea buscar.
	 * @return Lista de Mecanismos de titulación que coincidieron en la busqueda
	 * @throws MecanismoTitulacionNoEncontradoException -  Excepcion lanzada cuando no se encuentra un Mecanismo de Titulacion con el patrón solicitado
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<MecanismoTitulacion> buscar(String patron) throws MecanismoTitulacionNoEncontradoException {
		List<MecanismoTitulacion> retorno = null;
		StringBuffer sbsql = new StringBuffer();
	
		if(patron != null){
			sbsql.append(" SELECT mctt ");
			sbsql.append(" FROM MecanismoTitulacion mctt ");
			sbsql.append(" WHERE mcttDescripcion ");
			sbsql.append(" LIKE :patron");
			
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("patron" , patron + "%");
			retorno = q.getResultList();
		}
		
		if(retorno.size()<=0){
			throw new MecanismoTitulacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MecanismoTitulacion.buscar.todos.no.result.exception")));
		}
		
		return retorno;
	}

	
	
	/**
	 * Busca los Mecanismos de Titulación que coinciden con el patrón ingresado. 
	 * @param patron -> String que contiene el Mecanismo de Titulación que desea buscar.
	 * @return Lista de Mecanismos de titulación que coincidieron en la busqueda
	 * @throws MecanismoTitulacionNoEncontradoException -  Excepcion lanzada cuando no se encuentra un Mecanismo de Titulacion con el patrón solicitado
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<MecanismoTitulacion> buscarMecanismoXCarrera(Integer carreraId) throws MecanismoTitulacionNoEncontradoException {
		List<MecanismoTitulacion> retorno = null;
		StringBuffer sbsql = new StringBuffer();
	
		if(carreraId != null){
			sbsql.append(" SELECT mctt");
			sbsql.append(" FROM MecanismoTitulacion mctt, MecanismoTitulacionCarrera mcttcr ");
			sbsql.append(" WHERE mctt.mcttId = mcttcr.mcttcrMecanismoTitulacion.mcttId ");
			sbsql.append(" AND mcttcr.mcttcrCarrera.crrId = :carreraId ");
			
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("carreraId" , carreraId);
			retorno = q.getResultList();
		}
		
		if(retorno.size()<=0){
			throw new MecanismoTitulacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MecanismoTitulacion.buscar.todos.no.result.exception")));
		}
		
		return retorno;
	}
}
