/**************************************************************************
 *				(c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción o distribución no autorizada de este programa, 
 * o cualquier porción de él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán procesadas con el grado máximo contemplado 
 * por la ley.
 
 ************************************************************************* 
   
 ARCHIVO:     EdicionDocentesForm.java	  
 DESCRIPCION: Managed Bean que maneja las peticiones de edición de docentes
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
26-septiembre-2016		Daniel Albuja                        Emisión Inicial
13-Octubre -2016		Gabriel Malfa                        Agregar, Eliminar docentes y carreras
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.managedBeans.session.validacion;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.CarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.DocenteTutorTribunalLectorJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.FacultadDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.CarreraException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.DocenteTutorTribunalLectorJdbcDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.DocenteTutorTribunalLectorJdbcDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FacultadDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FacultadDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaDcnAsgTitulacionException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaDcnAsgTitulacionNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaDocenteException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaDocenteNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaDocenteValidacionException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaValidacionException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.EtniaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.FacultadServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.FichaDocenteServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.UsuarioServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.DocenteDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.FacultadDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.FichaDocenteConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Carrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Etnia;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.FichaDocente;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Persona;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Usuario;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.titulacionPosgrado.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) EdicionDocentesForm.
 * Managed Bean que maneja las peticiones de de edición de docentes
 * @author dalbuja.
 * @version 1.0
 */

@ManagedBean(name="edicionDocentesForm")
@SessionScoped
public class EdicionDocentesForm {
	// *****************************************************************/
	// ******************* Variables de EdicionDocentesForm **********/
	// *****************************************************************/
	private DocenteTutorTribunalLectorJdbcDto edfDocente;
	private List<DocenteTutorTribunalLectorJdbcDto> dpfListDocenteDto;
	private List<DocenteTutorTribunalLectorJdbcDto> dpfListCarrerasDocente;

	private DocenteTutorTribunalLectorJdbcDto edfDocenteEditar;
	private DocenteTutorTribunalLectorJdbcDto edfDocenteEditarCarrera;
	private List<Etnia> edfListEtnias;
	private FacultadDto pfFacultad;
	private CarreraDto pfCarrera;
	private List<FacultadDto> pfListFacultades;
	private List<CarreraDto> pfListCarreras;
	
	private Integer edfDocenteBandera;  /* 0-nuevo  1-editar*/
	private Integer edfValidadorClic;
	
	
	//*********************************************************************/
	//******** METODO GENERAL DE INICIALIZACION DE VARIABLES **************/
	//*********************************************************************/
	@PostConstruct
	public void inicializar(){
	}
	
	// ********************************************************************/
	// ******************* SERVICIOS GENERALES ****************************/
	// ********************************************************************/	
	
	@EJB private DocenteDtoServicioJdbc srvDocenteDtoServicioJdbc;
	@EJB private EtniaServicio servEdfEtnia;
	@EJB private UsuarioRolServicio srvUsuarioRolServicio;
	@EJB private FacultadServicio servEdfFacultad;
	@EJB private CarreraServicio servEdfCarrera;
	@EJB private UsuarioServicio servEdfUsuario;
	@EJB private PersonaServicio servEdfPersona;
	@EJB private FacultadDtoServicioJdbc servEdfFacultadDtoJdbc;
	@EJB private CarreraDtoServicioJdbc servEdfCarreraDtoJdbc;
	@EJB private FichaDocenteServicio servEdfFichaDocenteServicio;
	
	//****************************************************************/
	//******** METODOS DE NAVEGACION ********** **********************/
	//****************************************************************/
	
	/**
	 * Método que dige a la página de listar docentes
	 * @param usuario
	 * @return
	 */
	public String ListarDocentes(Usuario usuario){
	//vericacion de que el usuario no pertenezca al active dectory 0.-si 1.-no
		try {
			UsuarioRol usro = srvUsuarioRolServicio.buscarPorIdentificacionValidador(usuario.getUsrIdentificacion());
			if(usro.getUsroEstado().intValue()==UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
				FacesUtil.mensajeInfo("Usuario se encuentra inactivo, por favor contáctese con soporte técnico");
				return null;
			}
			iniciarParametros();
		}   catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} 
		return "irListarDocentes";
	}
	
	//iniciar parametros de busqueda
		private void iniciarParametros(){
			edfDocente=null;
			dpfListDocenteDto= null;
			dpfListCarrerasDocente =  null;
			limpiarAgredarCarrera();
			edfDocente =  new DocenteTutorTribunalLectorJdbcDto();
			edfValidadorClic = 0;
		}
		
		
	
	/**
	 * Maneja la navegacion de regesar a la pagina de inicio
	 * @return - cadena de navegacion a la pagina de inicio
	 */
	public String irInicio(){
		return "irInicio";
	}
	

	/**
	 * Maneja la navegacion de ir a crear nuevo docente
	 * @return - cadena de navegacion a la pagina de editar docente
	 */
	public String irNuevo(){
		//a la variable le asigno 0.- si es nuevo 
		edfDocenteBandera = 0; 
		//Instancio el objeto DocenteDto
		edfDocenteEditar = new DocenteTutorTribunalLectorJdbcDto();
		//Instancio el objeto facultad
		pfFacultad = new FacultadDto();
		//Instancio el objeto carrera
		pfCarrera = new CarreraDto();
		try {
			pfListFacultades = servEdfFacultadDtoJdbc.listarTodos();
		} catch (FacultadDtoJdbcException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (FacultadDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		return "irEdiciónDocente";
	}
	
	/**
	 * Maneja de cancelacion de la edición del docente
	 * @return - cadena de navegacion a la pagina de listar docentes
	 */
	public String irCancelar(){
		iniciarParametros();
		return "irCancelarEdicionDocentes";
	}
	
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
			
	/**
	 * Lista los docentes segun los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscarDocentes(){
		try {
			dpfListDocenteDto=srvDocenteDtoServicioJdbc.listarDocenteXIdentificacionXApellidoPaterno(edfDocente.getPrsIdentificacion(),edfDocente.getPrsPrimerApellido());
		} catch (DocenteTutorTribunalLectorJdbcDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Edicion.docente.buscar.por.identificacion.exception")));
		} catch (DocenteTutorTribunalLectorJdbcDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Edicion.docente.buscar.por.identificacion.no.result.exception")));
		}
	}
	
	/**
	 * Método que limpia los componentes del form de la busqueda del docente
	 * 
	 */
	public void limpiar(){
		iniciarParametros();
	}
	
	/**
	 * Método de navegación a la pagina de editar docente
	 * @param docente tipo DTO contiene los datos a editar
	 * @return cadena de navegacion a la pagina de editar docente
	 */
	public String editarDocente(DocenteTutorTribunalLectorJdbcDto docente){
		try {
			//asigano ala variable 1.-si es nuevo docente
			edfDocenteBandera = 1; 
			//agrego a la variable el docente que se vaa editar
			edfDocenteEditar = srvDocenteDtoServicioJdbc.buscarXIdentificacion(docente.getPrsIdentificacion());
		} catch (DocenteTutorTribunalLectorJdbcDtoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (DocenteTutorTribunalLectorJdbcDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}
		return "irEdiciónDocente";
	}
	
	
	/**
	 * Dirige a la página de asignar carreras al docente en las cuales imparte clases 
	 */
	public String irAsignarCarreraDocente(DocenteTutorTribunalLectorJdbcDto docente){
		try {
				//Seteo la lista de carreras a null
				pfListCarreras = null;
				//instancion el objeto DocenteDTO
				edfDocenteEditarCarrera  = new DocenteTutorTribunalLectorJdbcDto();
				//asigno al objeto DocenteDto el docente que voy a asignar carreras
				edfDocenteEditarCarrera = docente;
				//Instancio el objeto facultad
				pfFacultad = new FacultadDto();
				//Instancio el objeto carrera
				pfCarrera = new CarreraDto();
				//Cargo la lista de facultades
				
				pfListFacultades = servEdfFacultadDtoJdbc.listarTodos();
				//busco la lista de carreras por facultad
				dpfListCarrerasDocente = srvDocenteDtoServicioJdbc.listarCarrerasXIdentificacion(docente.getPrsIdentificacion());
			} catch (FacultadDtoJdbcException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (FacultadDtoJdbcNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (DocenteTutorTribunalLectorJdbcDtoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (DocenteTutorTribunalLectorJdbcDtoNoEncontradoException e) {
				FacesUtil.mensajeError(e.getMessage());
				dpfListCarrerasDocente=null;
			}
		return "irAsignarCarreraDocente";
	}

	
	/**
	 * permite buscar carreras por la facultad seleccionada
	 * @param facultadId - parametro de busqueda que selecciona el usuario
	 */
	
	public void buscarCarreraXfacultad(int facultadId){
		try {
			pfListCarreras =  new ArrayList<CarreraDto>();
			
			pfListCarreras = servEdfCarreraDtoJdbc.buscarXfacultad(facultadId);
			
		} catch (CarreraDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postulacion.llenar.carreras.por.facultad.exception")));
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			pfListCarreras = null;
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Postulacion.llenar.carreras.por.facultad.no.encontrado.exception")));
		}
	
	}
	
	/**
	 * Método que limpia los componentes del form
	 * 
	 */
	public void cambiarFacultad(){
		//Instancio el objeto facultad
		pfListCarreras = null;
	}
	
	
	
	/**
	 * Método que limpia los componentes del form
	 * 
	 */
	public void limpiarAgredarCarrera(){
		pfFacultad = null;
		pfCarrera = null;
		pfListCarreras = null;
		//Instancio el objeto facultad
		pfFacultad = new FacultadDto();
		
	}
	
	
	
	/**
	 * Maneja de cancelacion de la asignacion carrera al docente
	 * @return - cadena de navegacion a la pagina de listar docentes
	 */
	public String irListarCarrerasDocentes(){
		pfListCarreras =  null;
		pfFacultad =  null;
		pfCarrera =  null;
		edfDocenteEditarCarrera =  null;
		dpfListDocenteDto =  null;
		edfDocenteEditar = new DocenteTutorTribunalLectorJdbcDto();
		return "irListarCarrerasDocentes";
	}
	
	
	/**
	 * Dirige a la página de edición de docente 
	 */
	public String guardarEdicion(){
		
		
		try {
			if(edfDocenteBandera == GeneralesConstantes.APP_NUEVO){
				Persona persona = new Persona();
				persona.setPrsTipoIdentificacion(edfDocenteEditar.getPrsTipoIdentificacion());
				persona.setPrsIdentificacion(edfDocenteEditar.getPrsIdentificacion());
				persona.setPrsNombres(GeneralesUtilidades.eliminarEspaciosEnBlanco(edfDocenteEditar.getPrsNombres()).toUpperCase());
				persona.setPrsPrimerApellido(GeneralesUtilidades.eliminarEspaciosEnBlanco(edfDocenteEditar.getPrsPrimerApellido()).toUpperCase());
				persona.setPrsSegundoApellido(GeneralesUtilidades.eliminarEspaciosEnBlanco(edfDocenteEditar.getPrsSegundoApellido()).toUpperCase());
				persona.setPrsMailInstitucional(edfDocenteEditar.getPrsMailInstitucional());
				if(servEdfPersona.verificarConstraintIdenficador(persona, edfDocenteBandera)!= false){
					if(servEdfPersona.verificarConstraintMailInstitucional(persona,edfDocenteBandera)!= false){
						if(!ValidarDocente(persona.getPrsIdentificacion(), pfCarrera.getCrrId())){
								FacesUtil.mensajeError("Error el docente ya tiene asignado la carrera que intentó agregarle");
							}else{
								try {
									srvDocenteDtoServicioJdbc.guardarDocente(persona, pfCarrera.getCrrId());
									FacesUtil.limpiarMensaje();
									FacesUtil.mensajeInfo("Docente guardado exitosamente");
								} catch (Exception e) {
									e.printStackTrace();
									FacesUtil.limpiarMensaje();
									FacesUtil.mensajeError("Error al guardar el docente, por favor comúniquese con el Administrador del Sistema.");
								}
								
							}
					}else{
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeInfo(" Existe una persona con el correo electrónico ingresado");
					}	
				}else{
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeInfo(" Existe una persona con la identificación ingresada ");
				}
			}	
				
			if (edfDocenteBandera == GeneralesConstantes.APP_EDITAR){
				Persona persona = new Persona();
				persona.setPrsId(edfDocenteEditar.getPrsId());
				persona.setPrsTipoIdentificacion(edfDocenteEditar.getPrsTipoIdentificacion());
				persona.setPrsIdentificacion(edfDocenteEditar.getPrsIdentificacion());
				persona.setPrsNombres(GeneralesUtilidades.eliminarEspaciosEnBlanco(edfDocenteEditar.getPrsNombres()).toUpperCase());
				persona.setPrsPrimerApellido(GeneralesUtilidades.eliminarEspaciosEnBlanco(edfDocenteEditar.getPrsPrimerApellido()).toUpperCase());
				persona.setPrsSegundoApellido(GeneralesUtilidades.eliminarEspaciosEnBlanco(edfDocenteEditar.getPrsSegundoApellido()).toUpperCase());
				persona.setPrsMailInstitucional(edfDocenteEditar.getPrsMailInstitucional());
				if(servEdfPersona.verificarConstraintIdenficador(persona, edfDocenteBandera)!= false){
							servEdfPersona.editarDocente(persona);
							FacesUtil.limpiarMensaje();
							FacesUtil.mensajeInfo("Docente guardado exitosamente");
				}else{
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeInfo(" Existe una persona con la identificación ingresada ");
				}
			}
			
		} catch (PersonaValidacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (Exception e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Edicion.docente.guardar.edicion.exeption")));
		}
		iniciarParametros();
		return "irListarDocentes";
	}
	
	
	public String verificarClickAceptarTramite(){
		edfValidadorClic = 1;
		return null;
	}
	
	public String verificarClickAceptarTramiteNo(){
		edfValidadorClic = 0;
		return null;
	}
	
	
	public Boolean ValidarDocente(String identificacion, Integer carreraId){
		Boolean retorno =  false; 
		try {
			servEdfFichaDocenteServicio.buscarXIdentificacionXCarrera(identificacion, carreraId);
		} catch (FichaDocenteNoEncontradoException e) {
			retorno = true;
		} catch (FichaDocenteException e) {
			retorno = false;
		} catch (FichaDocenteValidacionException e) {
			retorno = false;
		}
		return retorno;
	}
	
	
	/**
	 * Dirige a la página de edición de docente 
	 */
	public String guardarAsignarCarrera(int carreraId){
		
		try {
			if(!ValidarDocente(edfDocenteEditarCarrera.getPrsIdentificacion(), carreraId)){
				FacesUtil.mensajeInfo("El docente ya tiene asignado la carrera que intentó agregar");
				
			}else{
				Carrera carreraAux = new Carrera();
				carreraAux = servEdfCarrera.buscarPorId(carreraId);
				Persona personaAux =  new Persona();
				personaAux = servEdfPersona.buscarPorId(edfDocenteEditarCarrera.getPrsId());
				FichaDocente fcdcAux = new FichaDocente();
				fcdcAux.setFcdcEstado(FichaDocenteConstantes.ACTIVO_VALUE);
				fcdcAux.setFcdcCarrera(carreraAux);
				fcdcAux.setFcdcPersona(personaAux);
				servEdfFichaDocenteServicio.anadir(fcdcAux);
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("La carrera se guardó correctamente");
			}
			irAsignarCarreraDocente(edfDocenteEditarCarrera);
		} catch (FichaDocenteException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (CarreraException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (PersonaException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (FichaDocenteValidacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		return null;
		
	}
	
	/**
	 * Dirige a la página de edición de docente 
	 */
	public String elimiarCarrera(Integer carreraId){
		try {
			FichaDocente fcdcAux = new FichaDocente();
			fcdcAux =  servEdfFichaDocenteServicio.buscarXIdentificacionXCarrera(edfDocenteEditarCarrera.getPrsIdentificacion(), carreraId);
			if(dpfListCarrerasDocente.size()==1){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("El docente debe tener al menos una carrera");
			}else{
				servEdfFichaDocenteServicio.eliminar(fcdcAux);
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("La carrera se eliminó correctamente");
			}
			
			
			irAsignarCarreraDocente(edfDocenteEditarCarrera);
		} catch (FichaDocenteValidacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (FichaDocenteNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (FichaDocenteException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (FichaDcnAsgTitulacionNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (FichaDcnAsgTitulacionException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
		return null;
		
	}
	
	
	
	
	//****************************************************************/
	//******************* METODOS GETTER y SETTER ********************/
	//****************************************************************/
	
	public DocenteTutorTribunalLectorJdbcDto getEdfDocente() {
		return edfDocente;
	}

	public void setEdfDocente(DocenteTutorTribunalLectorJdbcDto edfDocente) {
		this.edfDocente = edfDocente;
	}

	public Integer getEdfValidadorClic() {
		return edfValidadorClic;
	}

	public void setEdfValidadorClic(Integer edfValidadorClic) {
		this.edfValidadorClic = edfValidadorClic;
	}

	public List<DocenteTutorTribunalLectorJdbcDto> getDpfListDocenteDto() {
		dpfListDocenteDto = dpfListDocenteDto==null?(new ArrayList<DocenteTutorTribunalLectorJdbcDto>()):dpfListDocenteDto;
		return dpfListDocenteDto;
	}

	public void setDpfListDocenteDto(List<DocenteTutorTribunalLectorJdbcDto> dpfListDocenteDto) {
		this.dpfListDocenteDto = dpfListDocenteDto;
	}

	public DocenteTutorTribunalLectorJdbcDto getEdfDocenteEditar() {
		return edfDocenteEditar;
	}

	public void setEdfDocenteEditar(
			DocenteTutorTribunalLectorJdbcDto edfDocenteEditar) {
		this.edfDocenteEditar = edfDocenteEditar;
	}

	public List<Etnia> getEdfListEtnias() {
		edfListEtnias = edfListEtnias==null?(new ArrayList<Etnia>()):edfListEtnias;
		return edfListEtnias;
	}

	public void setEdfListEtnias(List<Etnia> edfListEtnias) {
		this.edfListEtnias = edfListEtnias;
	}

	public Integer getEdfDocenteBandera() {
		return edfDocenteBandera;
	}

	public void setEdfDocenteBandera(Integer edfDocenteBandera) {
		this.edfDocenteBandera = edfDocenteBandera;
	}

	public List<DocenteTutorTribunalLectorJdbcDto> getDpfListCarrerasDocente() {
		dpfListCarrerasDocente = dpfListCarrerasDocente==null?(new ArrayList<DocenteTutorTribunalLectorJdbcDto>()):dpfListCarrerasDocente;
		return dpfListCarrerasDocente;
	}

	public void setDpfListCarrerasDocente(
			List<DocenteTutorTribunalLectorJdbcDto> dpfListCarrerasDocente) {
		this.dpfListCarrerasDocente = dpfListCarrerasDocente;
	}

	public FacultadDto getPfFacultad() {
		return pfFacultad;
	}

	public void setPfFacultad(FacultadDto pfFacultad) {
		this.pfFacultad = pfFacultad;
	}

	public CarreraDto getPfCarrera() {
		return pfCarrera;
	}

	public void setPfCarrera(CarreraDto pfCarrera) {
		this.pfCarrera = pfCarrera;
	}

	public List<FacultadDto> getPfListFacultades() {
		return pfListFacultades;
	}

	public void setPfListFacultades(List<FacultadDto> pfListFacultades) {
		this.pfListFacultades = pfListFacultades;
	}

	public List<CarreraDto> getPfListCarreras() {
		return pfListCarreras;
	}

	public void setPfListCarreras(List<CarreraDto> pfListCarreras) {
		this.pfListCarreras = pfListCarreras;
	}

	public DocenteTutorTribunalLectorJdbcDto getEdfDocenteEditarCarrera() {
		return edfDocenteEditarCarrera;
	}

	public void setEdfDocenteEditarCarrera(
			DocenteTutorTribunalLectorJdbcDto edfDocenteEditarCarrera) {
		this.edfDocenteEditarCarrera = edfDocenteEditarCarrera;
	}

	
	
}
