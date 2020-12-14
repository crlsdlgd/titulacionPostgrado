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
   
 ARCHIVO:     ProcesoTramiteServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla ProcesoTramite. 
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 26/02/2018					Daniel Albuja                      Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.titulacionPosgrado.ejb.servicios.impl;


import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ProcesoTramiteException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ProcesoTramiteNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.ProcesoTramiteServicio;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.ProcesoTramite;

/**
 * Clase (Bean)ProcesoTramiteServicioImpl.
 * Bean declarado como Stateless.
 * @author dalbuja
 * @version 1.0
 */

@Stateless
public class ProcesoTramiteServicioImpl implements ProcesoTramiteServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad ProcesoTramite por su id
	 * @param id - de la ProcesoTramite a buscar
	 * @return ProcesoTramite con el id solicitado
	 * @throws ProcesoTramiteNoEncontradoException - Excepcion lanzada cuando no se encuentra una ProcesoTramite con el id solicitado
	 * @throws ProcesoTramiteException - Excepcion general
	 */
	@Override
	public ProcesoTramite buscarPorId(Integer id) throws ProcesoTramiteNoEncontradoException, ProcesoTramiteException {
		ProcesoTramite retorno = null;
		if (id != null) {
			try {
				retorno = em.find(ProcesoTramite.class, id);
			} catch (NoResultException e) {
				throw new ProcesoTramiteNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ProcesoTramite.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new ProcesoTramiteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ProcesoTramite.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new ProcesoTramiteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ProcesoTramite.buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	
	/**
	 * Lista todas las entidades ProcesoTramite existentes en la BD
	 * @return lista de todas las entidades ProcesoTramite existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProcesoTramite> listarTodos() throws ProcesoTramiteNoEncontradoException{
		List<ProcesoTramite> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select prtr from ProcesoTramite prtr ");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		
		if(retorno.size()<=0){
			throw new ProcesoTramiteNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ProcesoTramite.buscar.todos.no.result.exception")));
		}
		
		return retorno;
		
	}
	
	/**
	 * Lista todas las entidades TramiteTitulo existentes en la BD
	 * @return lista de todas las entidades TramiteTitulo existentes en la BD
	 */
	@Override
	public ProcesoTramite buscarXTrttXTipoPorceso(Integer trttId , Integer tipoProceso) throws ProcesoTramiteNoEncontradoException, ProcesoTramiteException{
		ProcesoTramite retorno = null;
		if(trttId != null && tipoProceso != null ){
			try{
		
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select prtr from ProcesoTramite prtr ");
				sbsql.append(" where prtr.prtrTipoProceso = :tipoProceso  ");
				sbsql.append(" and prtr.prtrTramiteTitulo.trttId =:trttId ");
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("tipoProceso",tipoProceso );
				q.setParameter("trttId",trttId );
				retorno = (ProcesoTramite)q.getSingleResult();
				
			} catch (NoResultException e) {
				throw new ProcesoTramiteNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ProcesoTramite.buscar.por.trtt.por.tipoPorceso.result.exception",trttId,tipoProceso)));
			}catch (NonUniqueResultException e) {
				throw new ProcesoTramiteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ProcesoTramite.buscar.por.trtt.por.tipoPorceso.non.unique.result.exception",trttId,tipoProceso)));
			} catch (Exception e) {
				throw new ProcesoTramiteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ProcesoTramite.buscar.por.trtt.por.tipoPorceso.exception")));
			}
		}
		return retorno;
	}

}
