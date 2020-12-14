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
   
 ARCHIVO:     ConfiguracionCarreraServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla ConfiguracionCarrera. 
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

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ConfiguracionCarreraException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ConfiguracionCarreraNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.ConfiguracionCarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.ConfiguracionCarrera;

/**
 * Clase (Bean)ConfiguracionCarreraServicioImpl.
 * Bean declarado como Stateless.
 * @author gmafla
 * @version 1.0
 */

@Stateless
public class ConfiguracionCarreraServicioImpl implements ConfiguracionCarreraServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad ConfiguracionCarrera por su id
	 * @param id - de la ConfiguracionCarrera a buscar
	 * @return ConfiguracionCarrera con el id solicitado
	 * @throws ConfiguracionCarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una ConfiguracionCarrera con el id solicitado
	 * @throws ConfiguracionCarreraException - Excepcion general
	 */
	@Override
	public ConfiguracionCarrera buscarPorId(Integer id) throws ConfiguracionCarreraNoEncontradoException, ConfiguracionCarreraException {
		ConfiguracionCarrera retorno = null;
		if (id != null) {
			try {
				retorno = em.find(ConfiguracionCarrera.class, id);
			} catch (NoResultException e) {
				throw new ConfiguracionCarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ConfiguracionCarrera.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new ConfiguracionCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ConfiguracionCarrera.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new ConfiguracionCarreraException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ConfiguracionCarrera.buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	
	/**
	 * Lista todas las entidades ConfiguracionCarrera existentes en la BD
	 * @return lista de todas las entidades ConfiguracionCarrera existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ConfiguracionCarrera> listarTodos() throws ConfiguracionCarreraNoEncontradoException{
		List<ConfiguracionCarrera> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select cncr from ConfiguracionCarrera cncr ");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		
		if(retorno.size()<=0){
			throw new ConfiguracionCarreraNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ConfiguracionCarrera.buscar.todos.no.result.exception")));
		}
		
		return retorno;
		
	}

}
