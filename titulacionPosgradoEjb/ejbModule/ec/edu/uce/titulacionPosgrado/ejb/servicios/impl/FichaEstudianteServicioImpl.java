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
   
 ARCHIVO:     FichaEstudianteServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla FichaEstudiante. 
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 21/02/2018				Daniel Albuja                      Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.titulacionPosgrado.ejb.servicios.impl;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaEstudianteException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaEstudianteNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.TramiteTituloValidacionException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.FichaEstudianteServicio;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.AutoridadConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.FichaEstudiante;

/**
 * Clase (Bean)FichaEstudianteServicioImpl.
 * Bean declarado como Stateless.
 * @author dalbuja
 * @version 1.0
 */

@Stateless
public class FichaEstudianteServicioImpl implements FichaEstudianteServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad FichaEstudiante por su id
	 * @param id - de la FichaEstudiante a buscar
	 * @return FichaEstudiante con el id solicitado
	 * @throws FichaEstudianteNoEncontradoException - Excepcion lanzada cuando no se encuentra una FichaEstudiante con el id solicitado
	 * @throws FichaEstudianteException - Excepcion general
	 */
	@Override
	public FichaEstudiante buscarPorId(Integer id) throws FichaEstudianteNoEncontradoException, FichaEstudianteException {
		FichaEstudiante retorno = null;
		if (id != null) {
			try {
				retorno = em.find(FichaEstudiante.class, id);
			} catch (NoResultException e) {
				throw new FichaEstudianteNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudiante.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new FichaEstudianteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudiante.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new FichaEstudianteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudiante.buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	
	/**
	 * Lista todas las entidades FichaEstudiante existentes en la BD
	 * @return lista de todas las entidades FichaEstudiante existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<FichaEstudiante> listarTodos() throws FichaEstudianteNoEncontradoException{
		List<FichaEstudiante> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select fces from FichaEstudiante fces ");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		
		if(retorno.size()<=0){
			throw new FichaEstudianteNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudiante.buscar.todos.no.result.exception")));
		}
		
		return retorno;
		
	}

	
	/**
	 * Busca la ficha estudiante por id de persona
	 * @param idPersona - id de persona con el que se va a buscar
	 * @return lista ficha estudiante por id de persona
	 */
	@Override
	public FichaEstudiante buscarPorIdPersona(Integer idPersona) throws FichaEstudianteNoEncontradoException,FichaEstudianteException {
		FichaEstudiante retorno = null;
		try {
			StringBuilder ssql = new StringBuilder();
			ssql.append(" SELECT fchEst from FichaEstudiante fchEst where ");
			ssql.append(" fchEst.fcesPersona.prsId = :idPersona ");
			retorno = (FichaEstudiante)em.createQuery(ssql.toString())
				    .setParameter("idPersona",idPersona)
				    .getSingleResult();
			
		} catch (NoResultException e) {
			throw new FichaEstudianteNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudiante.buscar.por.id.persona.no.result.exception")));
		}catch (NonUniqueResultException e) {
			throw new FichaEstudianteNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudiante.buscar.por.id.persona.non.unique.result.exception")));
		}catch (Exception e) {
			throw new FichaEstudianteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudiante.buscar.por.id.persona.exception")));
		}
		
		return retorno;
	}
	
	/**
	 * Reliza verificaciones para determinar si existe la entidad la busca por el nombre
	 * @param entidad - entidad a validar antes de gestionar su edicion o insersion
	 * @param tipo - tipo de verificacion que necesita 0 nuevo 1 editar
	 * @throws TramiteTituloValidacionException  - Excepcion lanzada en el caso de que no finalizo todas las validaciones
	 */
	private void verificar(FichaEstudiante entidad, int tipo) throws FichaEstudianteException {
		if(entidad==null){
			throw new FichaEstudianteException("TramiteTitulor.validar.entidad.null");
		}
	}

	/**
	 * Busca una entidad FichaEstudiante por el id de FichaEstudiante del postulado
	 * @param String cedulaPostulado 
	 * @return FichaEstudiante con el id solicitado
	 * @throws FichaEstudianteNoEncontradoException - Excepcion lanzada cuando no se encuentra una FichaEstudiante con el id solicitado
	 * @throws FichaEstudianteException - Excepcion general
	 */
	public FichaEstudiante editarPorId(FichaEstudiante fichaEstudiante)
			throws FichaEstudianteNoEncontradoException, FichaEstudianteException {
		
		FichaEstudiante retorno = null;
		try {
			if (fichaEstudiante != null) {
				retorno = buscarPorId(fichaEstudiante.getFcesId());
						if (retorno != null) {
							verificar(fichaEstudiante, GeneralesConstantes.APP_EDITAR);
							retorno.setFcesFechaInicio(fichaEstudiante.getFcesFechaInicio());
							retorno.setFcesFechaEgresamiento(fichaEstudiante.getFcesFechaEgresamiento());
							retorno.setFcesFechaActaGrado(fichaEstudiante.getFcesFechaActaGrado());
							retorno.setFcesNumActaGrado(fichaEstudiante.getFcesNumActaGrado());
							retorno.setFcesFechaRefrendacion(fichaEstudiante.getFcesFechaRefrendacion());
							retorno.setFcesNumRefrendacion(fichaEstudiante.getFcesNumRefrendacion());
							retorno.setFcesCrrEstudPrevios(fichaEstudiante.getFcesCrrEstudPrevios());
							retorno.setFcesTiempoEstudRec(fichaEstudiante.getFcesTiempoEstudRec());
							retorno.setFcesTipoDuracRec(fichaEstudiante.getFcesTipoDuracRec());
							retorno.setFcesTipoColegio(fichaEstudiante.getFcesTipoColegio());
							retorno.setFcesTipoColegioSniese(fichaEstudiante.getFcesTipoColegioSniese());
							retorno.setFcesNotaPromAcumulado(fichaEstudiante.getFcesNotaPromAcumulado());
							retorno.setFcesNotaTrabTitulacion(fichaEstudiante.getFcesNotaTrabTitulacion());
							retorno.setFcesLinkTesis(fichaEstudiante.getFcesLinkTesis());
							retorno.setFcesRecEstudPrevios(fichaEstudiante.getFcesRecEstudPrevios());
							retorno.setFcesRecEstudPrevSniese(fichaEstudiante.getFcesRecEstudPrevSniese());
							retorno.setFcesFechaCreacion(fichaEstudiante.getFcesFechaCreacion());
							
							retorno.setFcesUbcCantonResidencia(fichaEstudiante.getFcesUbcCantonResidencia());
							retorno.setFcesMecanismoCarrera(fichaEstudiante.getFcesMecanismoCarrera());
							retorno.setFcesInacEstPrevios(fichaEstudiante.getFcesInacEstPrevios());

							retorno.setFcesTramiteTitulo(fichaEstudiante.getFcesTramiteTitulo());
							retorno.setFcesConfiguracionCarrera(fichaEstudiante.getFcesConfiguracionCarrera());
							retorno.setFcesTituloBachiller(fichaEstudiante.getFcesTituloBachiller());
							retorno.setFcesPersona(fichaEstudiante.getFcesPersona());
						}
					}
				} catch (Exception e) {
				}
		return retorno;
	}
	
	
	/**
	 * Actualiza la ficha_estudiante, guardando la nota final de graduación
	 * @param idPersona - id de ficha_estudiante con el que se va a buscar
	 * @return boolean
	 */
	public void guardarNotaFinalGraduacion(Integer fcesId,BigDecimal notaFinal, Integer trttId) throws FichaEstudianteNoEncontradoException,FichaEstudianteException{
		try {
			FichaEstudiante fcesAux = em.find(FichaEstudiante.class, fcesId);
//			TramiteTitulo trttAux = em.find(TramiteTitulo.class, trttId);
//			if(trttAux.getTrttEstadoProceso() == TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_VALUE
//					|| trttAux.getTrttEstadoProceso() == TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_VALUE){
//				if(trttAux.getTrttCarreraId() == CarreraConstantes.CARRERA_ODONTOLOGIA_VALUE){
//					fcesAux.setFcesNotaFinalGraduacion(notaFinal);	
//				}
//			}else{
				fcesAux.setFcesNotaFinalGraduacion(notaFinal);	
//			}
		} catch (Exception e) {
			throw new FichaEstudianteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudiante.buscar.por.id.exception")));
		}
	}
	
	/**
	 * Lista todas las entidades FichaEstudiante existentes en la BD, que coincida su persona 
	 * @return lista de todas las entidades FichaEstudiante existentes en la BD, por prs_id 
	 * @throws FichaEstudianteNoEncontradoException - FichaEstudianteNoEncontradoException Excepcion lanzada cuando no se encuentra fichas estudiante por id de trámite
	 * @throws FichaEstudianteException - FichaEstudianteException Excepción general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean buscarFichaEstudiantePorPersonaId(String prsIdentificacion) throws FichaEstudianteNoEncontradoException, FichaEstudianteException{
		boolean retorno = false;
		List<FichaEstudiante> listaFces = null;
		if( prsIdentificacion != null){
			try {
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select fces from FichaEstudiante fces ");
				sbsql.append(" Where fces.fcesPersona.prsIdentificacion = :idPersona");
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("idPersona",prsIdentificacion);
				listaFces = q.getResultList();
				if(listaFces.size()>0){
					retorno = true;
				}
			} catch (NoResultException e) {
				throw new FichaEstudianteNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudiante.listarPorTramite.no.result.exception")));
			}catch (Exception e) {
				throw new FichaEstudianteException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("FichaEstudiante.listarPorTramite.exception.general")));
			}
		}
		return retorno;
	}

	@Override
	public boolean actualizarFichaEstudianteFechaEgresamiento(
			Integer  fcesId, Date fecha)
			throws FichaEstudianteNoEncontradoException,
			FichaEstudianteException {
		boolean retorno = false;
		try {
			FichaEstudiante fcesAux = em.find(FichaEstudiante.class, fcesId);
			fcesAux.setFcesFechaEgresamiento(fecha);
			retorno = true;
		} catch (Exception e) {
		}
		return retorno;
	}
	
	
	@Override
	public boolean guardarAutoridad(
			Integer  fcesId, String autoridad, Integer sexo, Integer tipo)
			throws FichaEstudianteNoEncontradoException,
			FichaEstudianteException {
		boolean retorno = false;
		try {
			FichaEstudiante fcesAux = em.find(FichaEstudiante.class, fcesId);
			if(tipo == AutoridadConstantes.DECANO_FACULTAD_VALUE){
//				fcesAux.setFcesDecano(autoridad);
//				fcesAux.setFcesDecanoSexo(sexo);
			}else if(tipo == AutoridadConstantes.SUBDECANO_FACULTAD_VALUE){
//				fcesAux.setFcesSubDecano(autoridad);
//				fcesAux.setFcesSubDecanoSexo(sexo);
			}else if(tipo == AutoridadConstantes.SECRETARIO_FACULTAD_VALUE){
//				fcesAux.setFcesSecretario(autoridad);
//				fcesAux.setFcesSecretarioSexo(sexo);
			}
			
			retorno = true;
		} catch (Exception e) {
		}
		return retorno;
	}
}
