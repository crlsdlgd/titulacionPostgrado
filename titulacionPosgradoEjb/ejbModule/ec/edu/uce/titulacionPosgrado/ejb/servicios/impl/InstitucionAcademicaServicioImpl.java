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
   
 ARCHIVO:     InstitucionAcademicaServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla InstitucionAcademica. 
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

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.InstitucionAcademicaException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.InstitucionAcademicaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.InstitucionAcademicaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.InstitucionAcademica;

/**
 * Clase (Bean)InstitucionAcademicaServicioImpl.
 * Bean declarado como Stateless.
 * @author gmafla
 * @version 1.0
 */

@Stateless
public class InstitucionAcademicaServicioImpl implements InstitucionAcademicaServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad InstitucionAcademica por su id
	 * @param id - de la InstitucionAcademica a buscar
	 * @return InstitucionAcademica con el id solicitado
	 * @throws InstitucionAcademicaNoEncontradoException - Excepcion lanzada cuando no se encuentra una InstitucionAcademica con el id solicitado
	 * @throws InstitucionAcademicaException - Excepcion general
	 */
	@Override
	public InstitucionAcademica buscarPorId(Integer id) throws InstitucionAcademicaNoEncontradoException, InstitucionAcademicaException {
		InstitucionAcademica retorno = null;
		if (id != null) {
			try {
				retorno = em.find(InstitucionAcademica.class, id);
			} catch (NoResultException e) {
				throw new InstitucionAcademicaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("InstitucionAcademica.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new InstitucionAcademicaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("InstitucionAcademica.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new InstitucionAcademicaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("InstitucionAcademica.buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	
	/**
	 * Lista todas las entidades InstitucionAcademica existentes en la BD
	 * @return lista de todas las entidades InstitucionAcademica existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<InstitucionAcademica> listarTodos() throws InstitucionAcademicaNoEncontradoException{
		List<InstitucionAcademica> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select inac from InstitucionAcademica etn ");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		
		if(retorno.size()<=0){
			throw new InstitucionAcademicaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("InstitucionAcademica.buscar.todos.no.result.exception")));
		}
		
		return retorno;
		
	}

}
