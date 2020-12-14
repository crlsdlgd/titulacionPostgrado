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
   
 ARCHIVO:     RolServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla Rol. 
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 23-04-2018	           Daniel Albuja                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.titulacionPosgrado.ejb.servicios.impl;


import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RolException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RolNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.RolServicio;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Rol;

/**
 * Clase (Bean)RolServicioImpl.
 * Bean declarado como Stateless.
 * @author dalbuja
 * @version 1.0
 */

@Stateless
public class RolServicioImpl implements RolServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad Rol por su id
	 * @param id - del Rol a buscar
	 * @return Rol con el id solicitado
	 * @throws RolNoEncontradoException - Excepcion lanzada cuando no se encuentra un Rol con el id solicitado
	 * @throws RolException - Excepcion general
	 */
	@Override
	public Rol buscarPorId(Integer id) throws RolNoEncontradoException, RolException {
		Rol retorno = null;
		if (id != null) {
			try {
				retorno = em.find(Rol.class, id);
			} catch (NoResultException e) {
				throw new RolNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rol.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new RolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rol.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new RolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rol.buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	
	/**
	 * Lista todas las entidades Rol existentes en la BD
	 * @return lista de todas las entidades Rol existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Rol> listarTodos() {
		List<Rol> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select rol from Rol rol ");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		return retorno;
		
	}
	
	/**
	 * Busca el rol por descripcion
	 * @return rol por descripcion
	 */
	@Override
	public Rol buscarRolXDescripcion(String rolDesc) {
		Rol retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select r from Rol r where ");
		sbsql.append(" UPPER(r.rolDescripcion) =:rolDesc ");
		Query q = em.createQuery(sbsql.toString());
		q.setParameter("rolDesc", rolDesc.toUpperCase());
		retorno = (Rol)q.getSingleResult();
		return retorno;
		
	}

	
	
	
}
