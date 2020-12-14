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
   
 ARCHIVO:     PostulacionDtoServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre el dto Postulacion dto. 
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 20/02/2018            Daniel Albuja                    Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.titulacionPosgrado.ejb.servicios.impl;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.CarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.PostulacionDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PostulacionDtoValidacionException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.PostulacionDtoServicio;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.TramiteTituloConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Convocatoria;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.FichaEstudiante;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Persona;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.ProcesoTramite;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.TramiteTitulo;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Ubicacion;


/**
 * Clase (Bean)PostulacionDtoServicioImpl.
 * Bean declarado como Stateless.
 * @author dalbuja
 * @version 1.0
 */
@Stateless
public class PostulacionDtoServicioImpl implements PostulacionDtoServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Inserta la nueva entidad indicada 
	 * @param  entidad - nueva entidad a insertar
	 * @return la nueva entidad insertada
	 * @throws  
	 */
	@Override
	public void anadir(PostulacionDto entidad, CarreraDto carreraPostulacion, Persona persona) throws PostulacionDtoValidacionException{
		if (entidad != null) {
			try {
			//*************************************************************
			//********************* VALIDACIONES **************************
			//*************************************************************
			Persona prsAux = em.find(Persona.class, entidad.getFcesPrsId());
			prsAux.setPrsUbicacionFoto(entidad.getPrsUbicacionFoto());
			em.merge(prsAux);
			em.flush();
			
			// creacion del objeto ficha_estudiante
			FichaEstudiante fichaEstudiante = new FichaEstudiante();
			fichaEstudiante.setFcesFechaCreacion(entidad.getFcesFechaCreacion());
			fichaEstudiante.setFcesUbcCantonResidencia(new Ubicacion(entidad.getFcesUbicacionResidencia()));
			fichaEstudiante.setFcesPersona(new Persona(entidad.getFcesPrsId()));
			
			//creacion del objeto tramite_titulo
			TramiteTitulo tramiteTitulo = new TramiteTitulo();
			tramiteTitulo.setTrttNumTramite(entidad.getTrttNumTramite());
			tramiteTitulo.setTrttEstadoTramite(entidad.getTrttEstadoTramite());
			tramiteTitulo.setTrttEstadoProceso(entidad.getTrttEstadoProceso());
			tramiteTitulo.setTrttConvocatoria(new Convocatoria(entidad.getTrttCnvId()));
			tramiteTitulo.setTrttCarreraId(carreraPostulacion.getCrrId());

			//validacion de que solo puede postularse 1 vez a la carrera 
			if(verificarCarreraEnTramite(tramiteTitulo, persona)){
				throw new PostulacionDtoValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postulacion.anadir.pst.carrera.validacion.exception")));
			}
			
			//creacion del objeto proceso_tramite
			ProcesoTramite procesoTramite = new ProcesoTramite();
			procesoTramite.setPrtrTipoProceso(entidad.getPrtrTipoProceso());
			procesoTramite.setPrtrFechaEjecucion(entidad.getPrtrFechaEjecucion());
			procesoTramite.setPrtrRegTtlSenecyt(entidad.getPrtrRegTtlSenecyt());
			
			//*************************************************************
			//************************** INSERCIONES **********************
			//*************************************************************
			//*********      Insercion de tramite titulo
			em.persist(tramiteTitulo);
			em.flush();
			
			tramiteTitulo = em.find(TramiteTitulo.class, tramiteTitulo.getTrttId());
//			//actualizo el numero de tramite
			tramiteTitulo.setTrttNumTramite(GeneralesUtilidades.generarNumeroTramite(tramiteTitulo.getTrttId(), entidad.getPrtrFechaEjecucion()));
			em.merge(tramiteTitulo);
			em.flush();
			
			//*********      Insercion de ficha estudiante
			//asignacion del tramite titulo a la ficha estudiante
			fichaEstudiante.setFcesTramiteTitulo(tramiteTitulo);
			em.persist(fichaEstudiante);
			em.flush();
			
			//*********      Insercion de proceso_tramite
			//asignacion del tramite titulo al proceso tramite
			procesoTramite.setPrtrTramiteTitulo(tramiteTitulo);
			em.persist(procesoTramite);
			em.flush();
			
			}  catch (PostulacionDtoValidacionException e) {
				e.printStackTrace();
				throw new PostulacionDtoValidacionException(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				throw new PostulacionDtoValidacionException(e.getMessage());
			} 
		}
	}
	

	/**
	 * Metodo que verifica la existencia de que una persona ya tenga una postulación a la carrera seleccionada 
	 * @param entidad - usuario a que se quiere verificar
	 * @param tipo - indica si se esta editando(1) o insertando(0)
	 * @return true si existe, false de lo contrario
	 */
	public boolean verificarCarreraEnTramite(TramiteTitulo tramiteTitulo, Persona persona){
		boolean retorno = false;
		TramiteTitulo trttAux = null;
		try{
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT tt from TramiteTitulo tt , FichaEstudiante fch , Persona p ");
			sbSql.append(" WHERE fch.fcesTramiteTitulo.trttId = tt.trttId ");
			sbSql.append(" AND fch.fcesPersona.prsId = p.prsId");
			sbSql.append(" AND tt.trttCarreraId = :carrera ");
			sbSql.append(" AND p.prsIdentificacion = :personaid ");
			sbSql.append(" AND tt.trttEstadoTramite = :activo ");
			
			Query q = em.createQuery(sbSql.toString());
			q.setParameter("carrera",tramiteTitulo.getTrttCarreraId());
			q.setParameter("personaid",persona.getPrsIdentificacion());
			q.setParameter("activo",TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			trttAux = (TramiteTitulo)q.getSingleResult();
			
		}catch(NoResultException nre){
			trttAux = null;
		}catch(NonUniqueResultException nure){
			trttAux = new TramiteTitulo();
		}
		
		if(trttAux!=null){
			retorno = true;
		}
		
		return retorno;
	}
	
	/**
	 * Metodo que verifica la existencia de que una persona ya tenga una postulacion a la carrera seleccionada 
	 * @param entidad - usuarioa que se quiere verificar
	 * @param tipo - indica si se esta editando(1) o insertando(0)
	 * @return true si existe, false de lo contrario
	 */
	public boolean verificarCarreraEnTramiteXCedula(Integer crrId, String cedula){
		boolean retorno = false;
		TramiteTitulo trttAux = null;
		try{
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT tt from TramiteTitulo tt , FichaEstudiante fch , Persona p ");
			sbSql.append(" WHERE fch.fcesTramiteTitulo.trttId = tt.trttId ");
			sbSql.append(" AND fch.fcesPersona.prsId = p.prsId");
			sbSql.append(" AND tt.trttCarreraId = :carrera ");
			sbSql.append(" AND p.prsIdentificacion = :personaid ");
			sbSql.append(" AND tt.trttEstadoTramite = :activo ");
			
			Query q = em.createQuery(sbSql.toString());
			q.setParameter("carrera",crrId);
			q.setParameter("personaid",cedula);
			q.setParameter("activo",TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			trttAux = (TramiteTitulo)q.getSingleResult();
			
		}catch(NoResultException nre){
			trttAux = null;
		}catch(NonUniqueResultException nure){
			trttAux = new TramiteTitulo();
		}
		
		if(trttAux!=null){
			retorno = true;
		}
		
		return retorno;
	}


	@Override
	public boolean verificarPostulacionActivaXIdentificacion(String cedula) {
		boolean retorno = false;
		TramiteTitulo trttAux = null;
		try{
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" SELECT tt from TramiteTitulo tt , FichaEstudiante fch , Persona p ");
			sbSql.append(" WHERE fch.fcesTramiteTitulo.trttId = tt.trttId ");
			sbSql.append(" AND fch.fcesPersona.prsId = p.prsId");
			sbSql.append(" AND p.prsIdentificacion = :personaid ");
			sbSql.append(" AND tt.trttEstadoTramite = :activo ");
			
			Query q = em.createQuery(sbSql.toString());
			q.setParameter("personaid",cedula);
			q.setParameter("activo",TramiteTituloConstantes.ESTADO_TRAMITE_ACTIVO_VALUE);
			trttAux = (TramiteTitulo)q.getSingleResult();
			
		}catch(NoResultException nre){
			trttAux = null;
		}catch(NonUniqueResultException nure){
			trttAux = new TramiteTitulo();
		}
		
		if(trttAux!=null){
			retorno = true;
		}
		
		return retorno;
			
	}
	
}
