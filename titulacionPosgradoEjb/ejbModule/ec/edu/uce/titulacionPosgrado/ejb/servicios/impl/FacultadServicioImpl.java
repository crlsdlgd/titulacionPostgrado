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
   
 ARCHIVO:     FacultadServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla Facultad. 
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 20/02/2018            Daniel Albuja                      Emisión Inicial
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

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FacultadException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FacultadNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.FacultadServicio;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Facultad;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.RolFlujoCarrera;

/**
 * Clase (Bean)FacultadServicioImpl.
 * Bean declarado como Stateless.
 * @author dalbuja
 * @version 1.0
 */

@Stateless
public class FacultadServicioImpl implements FacultadServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad Facultad por su id
	 * @param id - de la Facultad a buscar
	 * @return Facultad con el id solicitado
	 * @throws FacultadNoEncontradoException - Excepcion lanzada cuando no se encuentra una Facultad con el id solicitado
	 * @throws FacultadException - Excepcion general
	 */
	@Override
	public Facultad buscarPorId(Integer id) throws FacultadNoEncontradoException, FacultadException {
		Facultad retorno = null;
		if (id != null) {
			try {
				retorno = em.find(Facultad.class, id);
			} catch (NoResultException e) {
				throw new FacultadNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Facultad.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new FacultadException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Facultad.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new FacultadException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Facultad.buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	
	/**
	 * Lista todas las entidades Facultad existentes en la BD
	 * @return lista de todas las entidades Facultad existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Facultad> listarTodos() throws FacultadNoEncontradoException{
		List<Facultad> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select fcl from Facultad fcl order by fclDescripcion");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		
		if(retorno.size()<=0){
			throw new FacultadNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Facultad.buscar.todos.no.result.exception")));
		}
		
		return retorno;
		
	}

	/**
	 * Lista todas las entidades Facultad existentes en la BD que estén activas
	 * @return lista de todas las entidades Facultad existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Facultad> listarTodosActivo() throws FacultadNoEncontradoException{
		List<Facultad> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select fcl from Facultad fcl where fclEstado=0 order by fclDescripcion");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		
		if(retorno.size()<=0){
			throw new FacultadNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Facultad.buscar.todos.no.result.exception")));
		}
		
		return retorno;
		
	}
	
	/**
	 * Lista todas las entidades Facultad existentes en la BD de acuerdo al RolFlujoCarrera
	 * @return lista de todas las entidades Facultad existentes en la BD
	 */
	@Override
	public List<Facultad> listarTodosXRolFlujoCarrera(List<RolFlujoCarrera> rolFlujoCarrera) throws FacultadNoEncontradoException{
		List<Facultad> retorno = null;
		retorno=new ArrayList<Facultad>();
		for (RolFlujoCarrera item : rolFlujoCarrera) {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select distinct fcl from Facultad fcl, Carrera crr, RolFlujoCarrera roflcr ");
			sbsql.append(" where fcl.fclId = crr.crrFacultad.fclId ");
			sbsql.append(" and roflcr.roflcrCarrera.crrId = crr.crrId ");
			sbsql.append(" and crr.crrId = :crrId ");
			sbsql.append(" order by fcl.fclId  ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("crrId", item.getRoflcrCarrera().getCrrId());
			retorno.add((Facultad)q.getSingleResult());
		}
		if(retorno.size()<=0){
			throw new FacultadNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Facultad.buscar.todos.no.result.exception")));
		}
		return retorno;
	}
	
}
