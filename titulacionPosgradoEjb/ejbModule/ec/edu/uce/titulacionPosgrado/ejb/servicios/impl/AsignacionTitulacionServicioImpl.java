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
   
 ARCHIVO:     AsignacionTitulacionServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla AsignacionTitulacion. 
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 23-09-2016           Daniel Albuja                      Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.titulacionPosgrado.ejb.servicios.impl;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.AsignacionTitulacionException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.AsignacionTitulacionNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.AsignacionTitulacionServicio;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.AsignacionTitulacion;

/**
 * Clase (Bean)AsignacionTitulacionServicioImpl.
 * Bean declarado como Stateless.
 * @author dalbuja
 * @version 1.0
 */

@Stateless
public class AsignacionTitulacionServicioImpl implements AsignacionTitulacionServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad AsignacionTitulacion por su id
	 * @param id - de la AsignacionTitulacion a buscar
	 * @return AsignacionTitulacion con el id solicitado
	 * @throws AsignacionTitulacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una AsignacionTitulacion con el id solicitado
	 * @throws AsignacionTitulacionException - Excepcion general
	 */
	@Override
	public AsignacionTitulacion buscarPorId(Integer id) throws AsignacionTitulacionNoEncontradoException, AsignacionTitulacionException {
		AsignacionTitulacion retorno = null;
		if (id != null) {
			try {
				retorno = em.find(AsignacionTitulacion.class, id);
			} catch (NoResultException e) {
				throw new AsignacionTitulacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AsignacionTitulacion.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new AsignacionTitulacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AsignacionTitulacion.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new AsignacionTitulacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AsignacionTitulacion.buscar.por.id.exception")));
			}
		}
		return retorno;
	}


	@Override
	public AsignacionTitulacion actualizarPorId(Integer id,String nombreTutor,String temaTrabajo,Integer aprobacion)
			throws AsignacionTitulacionNoEncontradoException,
			AsignacionTitulacionException {
		AsignacionTitulacion retorno = null;
		if (id != null) {
			try {
				retorno = em.find(AsignacionTitulacion.class, id);
				retorno.setAsttTemaTrabajo(temaTrabajo);
				retorno.setAsttDirectorCientifico(nombreTutor);
				retorno.setAsttAprobacionTutor(aprobacion);
				em.merge(retorno);
			} catch (NoResultException e) {
				throw new AsignacionTitulacionNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AsignacionTitulacion.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new AsignacionTitulacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AsignacionTitulacion.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new AsignacionTitulacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("AsignacionTitulacion.buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	
	
	@Override
	public AsignacionTitulacion buscarTemaTutorXTrttId(Integer trttId){

		AsignacionTitulacion retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select astt from AsignacionTitulacion astt where ");
		sbsql.append(" astt.asttProcesoTramite.prtrTramiteTitulo.trttId =:trttId ");
		Query q = em.createQuery(sbsql.toString());
		q.setParameter("trttId", trttId);
		retorno = (AsignacionTitulacion) q.getSingleResult();

		return retorno;

	}
}
