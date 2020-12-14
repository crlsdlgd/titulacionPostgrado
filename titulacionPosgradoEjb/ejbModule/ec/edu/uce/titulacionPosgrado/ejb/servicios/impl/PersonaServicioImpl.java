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
   
 ARCHIVO:     PersonaServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla Persona. 
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 20/02/2018				Daniel Albuja                      Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.titulacionPosgrado.ejb.servicios.impl;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteValidacionJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.UsuarioRolJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaValidacionException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.PersonaConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Persona;

/**
 * Clase (Bean)PersonaServicioImpl.
 * Bean declarado como Stateless.
 * @author dalbuja
 * @version 1.0
 */

@Stateless
public class PersonaServicioImpl implements PersonaServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
//	@Resource(mappedName=GeneralesConstantes.APP_DATA_SOURCE_EMISION_TITULO)
	DataSource dsEmision;
	/**
	 * Busca una entidad Persona por su id
	 * @param id - de la Persona a buscar
	 * @return Persona con el id solicitado
	 * @throws PersonaNoEncontradoException - Excepcion lanzada cuando no se encuentra una Persona con el id solicitado
	 * @throws PersonaException - Excepcion general
	 */
	@Override
	public Persona buscarPorId(Integer id) throws PersonaNoEncontradoException, PersonaException {
		Persona retorno = null;
		if (id != null) {
			try {
				retorno = em.find(Persona.class, id);
			} catch (NoResultException e) {
				throw new PersonaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new PersonaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new PersonaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	
	/**
	 * Lista todas las entidades Persona existentes en la BD
	 * @return lista de todas las entidades Persona existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Persona> listarTodos() throws PersonaNoEncontradoException{
		List<Persona> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select prs from Persona prs ");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		
		if(retorno.size()<=0){
			throw new PersonaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.buscar.todos.no.result.exception")));
		}
		
		return retorno;
		
	}
	
	/**
	 * Busca Persona por Identificacion
	 * @param indentificacion - identificacion de la persona que se va a buscar
	 * @return entidad persona
	 * @throws PersonaNoEncontradoException
	 * @throws PersonaException
	 */
	@Override
	public Persona buscarPorIdentificacion(String indentificacion)  throws PersonaNoEncontradoException, PersonaException{
		Persona retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select p from Persona p where ");
			sbsql.append(" p.prsIdentificacion =:indentificacion ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("indentificacion", indentificacion);
			retorno = (Persona)q.getSingleResult();
		} catch (NoResultException e) {
			e.printStackTrace();
			throw new PersonaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.buscar.por.cedula.no.result.exception",indentificacion)));
		} catch (NonUniqueResultException e) {
			e.printStackTrace();
			throw new PersonaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.buscar.por.cedula.non.unique.result.exception",indentificacion)));
		} catch (Exception e) {
			e.printStackTrace();
			throw new PersonaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.buscar.por.cedula.exception")));
		}
		return retorno;
		
	}

	
	/**
	 * Busca Persona por Identificacion
	 * @param indentificacion - identificacion de la persona que se va a buscar
	 * @return entidad persona
	 * @throws PersonaNoEncontradoException
	 * @throws PersonaException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Persona> listarPorIdentificacion(String indentificacion)  throws PersonaNoEncontradoException, PersonaException{
		List<Persona> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select p from Persona p where ");
			sbsql.append(" p.prsIdentificacion =:indentificacion ");
			Query q = em.createQuery(sbsql.toString());
			q.setParameter("indentificacion", indentificacion);
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new PersonaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.buscar.por.cedula.no.result.exception",indentificacion)));
		} catch (Exception e) {
			throw new PersonaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.buscar.por.cedula.exception")));
		}
		return retorno;
		
	}
	
	/**
	 * Edita todos los atributos de la entidad indicada
	 * @param entidad - entidad a editar
	 * @return la entidad editada
	 * @throws PersonaValidacionException - Excepcion lanzada en el caso de que no finalizo todas las validaciones
	 * @throws PersonaNoEncontradoException - Excepcion lanzada si no se encontro la entidad a editar
	 * @throws PersonaException - Excepcion general
	 */
	@Override
	public Persona editar(Persona entidad) throws PersonaValidacionException, PersonaNoEncontradoException, PersonaException {
		Persona retorno = null;
		try {
			if (entidad != null) {
				retorno = buscarPorId(entidad.getPrsId());
				if (retorno != null) {
					//validacion de constraint de mail
					if(!verificarConstraintMailPersona(entidad, GeneralesConstantes.APP_EDITAR)){
						throw new PersonaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.editar.verificar.constraint.mail.error")));
					}	
//					verificar(entidad, GeneralesConstantes.APP_EDITAR);
					retorno.setPrsTipoIdentificacion(entidad.getPrsTipoIdentificacion());
					if(entidad.getPrsTipoIdentificacion() == PersonaConstantes.TIPO_IDENTIFICACION_CEDULA_VALUE){
						retorno.setPrsTipoIdentificacionSniese(PersonaConstantes.TIPO_IDENTIFICACION_CEDULA_VALUE);
					}
					if(entidad.getPrsTipoIdentificacion() == PersonaConstantes.TIPO_IDENTIFICACION_PASAPORTE_VALUE){
						retorno.setPrsTipoIdentificacionSniese(PersonaConstantes.TIPO_IDENTIFICACION_PASAPORTE_VALUE);
					}
					retorno.setPrsIdentificacion(entidad.getPrsIdentificacion().toUpperCase());
					retorno.setPrsPrimerApellido(entidad.getPrsPrimerApellido().replaceAll(" +", " ").trim().toUpperCase());
					retorno.setPrsSegundoApellido(entidad.getPrsSegundoApellido().replaceAll(" +", " ").trim().toUpperCase());
					retorno.setPrsNombres(entidad.getPrsNombres().replaceAll(" +", " ").trim().toUpperCase());
					retorno.setPrsMailPersonal(entidad.getPrsMailPersonal().replaceAll(" +", " ").trim());
					retorno.setPrsMailInstitucional(entidad.getPrsMailInstitucional());
					retorno.setPrsTelefono(entidad.getPrsTelefono().replaceAll(" +", " ").trim());
					retorno.setPrsFechaNacimiento(entidad.getPrsFechaNacimiento());
					retorno.setPrsSexo(entidad.getPrsSexo());
					retorno.setPrsSexoSniese(entidad.getPrsSexoSniese());
					retorno.setPrsEtnia(entidad.getPrsEtnia());
					retorno.setPrsUbicacionNacionalidad(entidad.getPrsUbicacionNacionalidad());
					retorno.setPrsUbicacionFoto(entidad.getPrsUbicacionFoto());
					em.merge(retorno);
					em.flush();
				}
			}
		} catch (PersonaNoEncontradoException e) {
			throw e;
		} catch (PersonaValidacionException e) {
			throw new PersonaValidacionException(e.getMessage());
		} catch (PersonaException e) {
			throw e;
		}
		return retorno;
	}
	
	/**
	 * Edita todos los atributos de la entidad indicada
	 * @param entidad - entidad a editar
	 * @return la entidad editada
	 * @throws PersonaValidacionException - Excepcion lanzada en el caso de que no finalizo todas las validaciones
	 * @throws PersonaNoEncontradoException - Excepcion lanzada si no se encontro la entidad a editar
	 * @throws PersonaException - Excepcion general
	 */
	@Override
	public Persona editarXId(UsuarioRolJdbcDto entidad) throws PersonaValidacionException, PersonaNoEncontradoException, PersonaException {
		Persona retorno = null;
		try {
			if (entidad != null) {
				retorno = buscarPorId(entidad.getPrsId());
				if (retorno != null) {
					retorno.setPrsTipoIdentificacion(entidad.getPrsTipoIdentificacion());
					if(entidad.getPrsTipoIdentificacion() == PersonaConstantes.TIPO_IDENTIFICACION_CEDULA_VALUE){
						retorno.setPrsTipoIdentificacionSniese(PersonaConstantes.TIPO_IDENTIFICACION_CEDULA_VALUE);
					}
					if(entidad.getPrsTipoIdentificacion() == PersonaConstantes.TIPO_IDENTIFICACION_PASAPORTE_VALUE){
						retorno.setPrsTipoIdentificacionSniese(PersonaConstantes.TIPO_IDENTIFICACION_PASAPORTE_VALUE);
					}
					retorno.setPrsIdentificacion(entidad.getPrsIdentificacion().toUpperCase());
					retorno.setPrsPrimerApellido(entidad.getPrsPrimerApellido().replaceAll(" +", " ").trim().toUpperCase());
					retorno.setPrsSegundoApellido(entidad.getPrsSegundoApellido().replaceAll(" +", " ").trim().toUpperCase());
					retorno.setPrsNombres(entidad.getPrsNombres().replaceAll(" +", " ").trim().toUpperCase());
					retorno.setPrsMailPersonal(entidad.getPrsMailPersonal().replaceAll(" +", " ").trim());
				}
			}
		} catch (PersonaNoEncontradoException e) {
			throw e;
		}  catch (PersonaException e) {
			throw e;
		}
		return retorno;
	}
	
	
	
	/**
	 * Edita todos los atributos de la entidad indicada
	 * @param entidad - entidad a editar
	 * @return la entidad editada
	 * @throws PersonaValidacionException - Excepcion lanzada en el caso de que no finalizo todas las validaciones
	 * @throws PersonaNoEncontradoException - Excepcion lanzada si no se encontro la entidad a editar
	 * @throws PersonaException - Excepcion general
	 */
	@Override
	public Persona editarDocente(Persona entidad) throws PersonaValidacionException, PersonaNoEncontradoException, PersonaException {
		Persona retorno = null;
		try {
			if (entidad != null) {
				retorno = buscarPorId(entidad.getPrsId());
				if (retorno != null) {
					//validacion de constraint de mail
					if(!verificarConstraintMailInstitucional(entidad, GeneralesConstantes.APP_EDITAR)){
						throw new PersonaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.editar.verificar.constraint.mail.error")));
					}	
//					verificar(entidad, GeneralesConstantes.APP_EDITAR);
					retorno.setPrsTipoIdentificacion(entidad.getPrsTipoIdentificacion());
					if(entidad.getPrsTipoIdentificacion() == PersonaConstantes.TIPO_IDENTIFICACION_CEDULA_VALUE){
						retorno.setPrsTipoIdentificacionSniese(PersonaConstantes.TIPO_IDENTIFICACION_CEDULA_VALUE);
					}
					if(entidad.getPrsTipoIdentificacion() == PersonaConstantes.TIPO_IDENTIFICACION_PASAPORTE_VALUE){
						retorno.setPrsTipoIdentificacionSniese(PersonaConstantes.TIPO_IDENTIFICACION_PASAPORTE_VALUE);
					}
					retorno.setPrsIdentificacion(entidad.getPrsIdentificacion().toUpperCase());
					retorno.setPrsPrimerApellido(entidad.getPrsPrimerApellido().toUpperCase());
					retorno.setPrsSegundoApellido(entidad.getPrsSegundoApellido().toUpperCase());
					retorno.setPrsNombres(entidad.getPrsNombres().toUpperCase());
					retorno.setPrsMailPersonal(entidad.getPrsMailPersonal());
					retorno.setPrsMailInstitucional(entidad.getPrsMailInstitucional());
					retorno.setPrsTelefono(entidad.getPrsTelefono());
					retorno.setPrsFechaNacimiento(entidad.getPrsFechaNacimiento());
					retorno.setPrsSexo(entidad.getPrsSexo());
					retorno.setPrsSexoSniese(entidad.getPrsSexoSniese());
					retorno.setPrsEtnia(entidad.getPrsEtnia());
					retorno.setPrsUbicacionNacionalidad(entidad.getPrsUbicacionNacionalidad());
					em.persist(retorno);
					em.flush();
				}
			}
		} catch (PersonaNoEncontradoException e) {
			throw new PersonaNoEncontradoException(e.getMessage());
		} catch (PersonaValidacionException e) {
			throw new PersonaValidacionException(e.getMessage());
		} catch (PersonaException e) {
			throw new PersonaException(e.getMessage());
		}
		return retorno;
	}
	
	
	/**
	 * Metodo que verifica la existencia de un mail en la BD
	 * @param entidad - usuarioa que se quiere verificar
	 * @param tipo - indica si se esta editando(1) o insertando(0)
	 * @return true si existe, false de lo contrario
	 */
	public boolean verificarConstraintMailPersona(Persona entidad, int tipo){
		boolean retorno = false;
		Persona perAux = null;
		try{
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" Select p from Persona p where ");
			sbSql.append(" upper(p.prsMailPersonal)=:mailPersonal ");
			if(tipo == GeneralesConstantes.APP_EDITAR.intValue()){
				sbSql.append(" AND p.prsId != :personaId ");
			}
			
			Query q = em.createQuery(sbSql.toString());
			q.setParameter("mailPersonal",entidad.getPrsMailPersonal().toUpperCase());
			if(tipo == GeneralesConstantes.APP_EDITAR.intValue()){
				q.setParameter("personaId",entidad.getPrsId());
			}
			perAux = (Persona)q.getSingleResult();
		}catch(NoResultException nre){
			perAux = null;
		}catch(NonUniqueResultException nure){
			perAux = new Persona();
		}
		
		if(perAux==null){
			retorno = true;
		}
		
		return retorno;
	}
	
	/**
	 * Metodo que verifica la existencia de un mail en la BD
	 * @param entidad - usuarioa que se quiere verificar
	 * @param tipo - indica si se esta editando(1) o insertando(0)
	 * @return true si existe, false de lo contrario
	 */
	public boolean verificarConstraintMailInstitucional(Persona entidad, int tipo){
		boolean retorno = false;
		Persona perAux = null;
		try{
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" Select p from Persona p where ");
			sbSql.append(" upper(p.prsMailPersonal)= :mailPersonal ");
			if(tipo == GeneralesConstantes.APP_EDITAR.intValue()){
				sbSql.append(" AND p.prsId !=:personaId ");
			}
			
			Query q = em.createQuery(sbSql.toString());
			q.setParameter("mailPersonal",entidad.getPrsMailInstitucional().toUpperCase());
			if(tipo == GeneralesConstantes.APP_EDITAR.intValue()){
				q.setParameter("personaId",entidad.getPrsId());
			}
			perAux = (Persona)q.getSingleResult();
		}catch(NoResultException nre){
			perAux = null;
		}catch(NonUniqueResultException nure){
			perAux = new Persona();
		}
		
		if(perAux==null){
			retorno = true;
		}
		
		return retorno;
	}
	
	/**
	 * Metodo que verifica la existencia de un identificador en la BD
	 * @param entidad - usuario que se quiere verificar
	 * @param tipo - indica si se esta editando(1) o insertando(0)
	 * @return true si existe, false de lo contrario
	 */
	@Override
	public boolean verificarConstraintIdenficador(Persona entidad, int tipo){
		boolean retorno = false;
		Persona prsAux = null;
		try{
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" Select prs from Persona prs where ");
			sbSql.append(" upper(prs.prsIdentificacion)=:identificacion ");
			if(tipo == GeneralesConstantes.APP_EDITAR.intValue()){
				sbSql.append(" AND prs.prsId !=:usuarioId ");
			}
			
			Query q = em.createQuery(sbSql.toString());
			q.setParameter("identificacion",entidad.getPrsIdentificacion().toUpperCase());
			if(tipo == GeneralesConstantes.APP_EDITAR.intValue()){
				q.setParameter("usuarioId",entidad.getPrsId());
			}
			prsAux = (Persona)q.getSingleResult();
		}catch(NoResultException nre){
			prsAux = null;
		}catch(NonUniqueResultException nure){
			prsAux = new Persona();
		}
		
		if(prsAux==null){
			retorno = true;
		}
		
		return retorno;
	}
	/**
     * Añade una Persona en la BD
     * @return lista de todas las entidades Persona existentes en la BD
     * @throws PersonaValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
     * @throws PersonaException - Excepción general
     */
    @Override
    public boolean anadir(Persona entidad) throws PersonaValidacionException, PersonaException {
        Boolean retorno = false;
        if (entidad != null) {
            try {
//                if (verificarDescripcion(entidad, GeneralesConstantes.APP_NUEVO)) {
                    em.persist(entidad);
                    retorno = true;
//                } else {
//                    throw new PersonaValidacionException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.añadir.duplicado.error")));
//                }
            } catch (Exception e) {
                throw new PersonaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.añadir.exception")));
            }
        } else {
            throw new PersonaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.añadir.null.exception")));
        }
        return retorno;
    }
	
    @Override
	public Persona buscarPersonaXRolXCarrera(Integer rol, Integer carreraId){
		Persona prsAux = null;
		try{
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" Select prs from Persona prs, ");
			sbSql.append(" Usuario usr, ");
			sbSql.append(" UsuarioRol usro, ");
			sbSql.append(" Rol rol, ");
			sbSql.append(" RolFlujoCarrera roflcr ");
			sbSql.append( " where ");
			sbSql.append(" prs.prsId = usr.usrPersona.prsId ");
			sbSql.append( " and ");
			sbSql.append(" usro.usroUsuario.usrId = usr.usrId ");
			sbSql.append( " and ");
			sbSql.append(" rol.rolId = usro.usroRol.rolId ");
			sbSql.append( " and ");
			sbSql.append(" roflcr.roflcrUsuarioRol.usroId = usro.usroId ");
			sbSql.append( " and ");
			sbSql.append(" roflcr.roflcrCarrera.crrId = :carreraId ");
			sbSql.append( " and ");
			sbSql.append(" rol.rolId = :rol ");
			
			Query q = em.createQuery(sbSql.toString());
			q.setParameter("rol",rol);
			q.setParameter("carreraId",carreraId);
			prsAux = (Persona)q.getSingleResult();
			
		}catch(NoResultException nre){
			prsAux = null;
		}catch(NonUniqueResultException nure){
			prsAux = null;
		}
		
		return prsAux;
	}
    
    @Override
	public boolean actualizarFotoXPrsId(EstudianteValidacionJdbcDto estudiante) throws PersonaNoEncontradoException, PersonaException {
		boolean retorno = false;
		if (estudiante != null) {
			try {
				Persona prs = em.find(Persona.class, estudiante.getPrsId());
				prs.setPrsUbicacionFoto(estudiante.getPrsUbicacionFoto());
				
				
				
				retorno=true;
			} catch (Exception e) {
				throw new PersonaException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.buscar.por.id.exception")));
			}
		}else{
			throw new PersonaNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Error al buscar por identificación.")));
		}
		return retorno;
	}
    
    
    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean actualizarFotoXPrsIdEmisionTitulo(EstudianteValidacionJdbcDto estudiante, String ubicacion) throws PersonaNoEncontradoException, PersonaException {
    	Connection con = null;
		PreparedStatement psmt = null;
    	try {
    		 con = dsEmision.getConnection();
    		StringBuffer ssql = new StringBuffer();
			ssql.append(" UPDATE PERSONA SET PRS_UBICACION_FOTO = ? ");
			ssql.append(" WHERE PRS_IDENTIFICACION = ? ");
			psmt = con.prepareStatement(ssql.toString());
			psmt.setString(1, ubicacion);
			psmt.setString(2, estudiante.getPrsIdentificacion());
			psmt.executeQuery();
    		
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			try {
				if(psmt!=null) psmt.close();
				if(con!=null) con.close();
			} catch (Exception e2) {
			}
		}
		return true;
	}
    
}
