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
   
 ARCHIVO:     AdministracionAutoridadForm.java	  
 DESCRIPCION: Bean que maneja las peticiones de la tabla autoridad.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
28-Abril-2017		  Diego García                        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.managedBeans.session.administracion.dgpp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.titulacionPosgrado.ejb.excepciones.AutoridadException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.AutoridadNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FacultadNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.AutoridadServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.FacultadServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Autoridad;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Facultad;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Persona;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Usuario;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.titulacionPosgrado.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) AdministracionAutoridadesForm. Managed Bean que maneja
 * las peticiones de la tabla autoridades.
 * @author dpgarciar.
 * @version 1.0
 */

@ManagedBean(name = "administracionAutoridadesForm")
@SessionScoped
public class AdministracionAutoridadesForm implements Serializable {

	private static final long serialVersionUID = 8026853400702897855L;

	// *****************************************************************/
	// ************* Variables de AdministracionAutoridadesForm********/
	// *****************************************************************/

	private Autoridad aafAutoridadBuscar;
	private List<Autoridad> aafListAutoridad;
	private List<Autoridad> aafListAutoridadBuscar;
	private Autoridad aafAutoridadEditar;
	private Persona aafPersonaBuscar;
	private List<Persona> aafListPersona;
	private List<Persona> aafListPersonaBuscar;
	private List<Facultad> aafListFacultad;
	private int aafAutoridadValor = GeneralesConstantes.APP_NUEVO;
	private Integer aafValidadorClic;

	// ******************************************************************/
	// ******************* Servicios Generales **************************/
	// ******************************************************************/
	@EJB
	private UsuarioRolServicio servUsuarioRolServicio;
	@EJB
	private AutoridadServicio servAutoridadServicio;
	@EJB
	private PersonaServicio servPersonaServicio;
	@EJB
	private FacultadServicio servFacultadServicio;
	// ****************************************************************/
	// ******** METODO GENERAL DE INICIALIZACION DE VARIABLES *********/
	// ****************************************************************/
	@PostConstruct
	public void inicializar() {
	}

	// ****************************************************************/
	// ******** METODOS DE NAVEGACION ********** **********************/
	// ****************************************************************/
	
	/**
	 * Método que dirige a la página de Autoridades de la DGA
	 * @param usuario
	 * @return Usuario Activo/Inactivo si es activo va a "irListarAutoridad"
	 */
	public String irListarAutoridad(Usuario usuario) {
		// vericacion de que el usuario no pertenezca al active dectory 0.-si
		// 1.-no
		try {
			UsuarioRol usro = servUsuarioRolServicio.buscarPorIdentificacionDga(usuario.getUsrIdentificacion());
			if (usro.getUsroEstado().intValue() == UsuarioRolConstantes.ESTADO_INACTIVO_VALUE) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Usuario se encuentra inactivo, por favor contáctese con soporte técnico");
				return null;
			}
			aafListAutoridad = servAutoridadServicio.listarTodos();
			aafListFacultad = servFacultadServicio.listarTodos();
			iniciarParametros();
		} catch (FacultadNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Facultad.listar.por.id.no.result.exception")));
		} catch (AutoridadNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Autoridad.listar.por.id.no.result.exception")));
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Usuario.buscar.por.id.no.result.exception")));
		} catch (UsuarioRolException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Usuario.buscar.por.id.exception")));
		}
		return "irListarAutoridad";
	}

	/**
	 * Maneja de cancelacion de la validación
	 * @return - cadena de navegacion a la pagina de inicio
	 */
	public String cancelarListar() {
		limpiar();
		return "irInicio";
	}

	// ****************************************************************/
	// ******** METODOS DE LIMPIEZA DE VARIABLES **********************/
	// ****************************************************************/

	/**
	 * Método que limpia los componentes del form
	 */
	public void limpiar() {
		aafAutoridadBuscar = null;
		aafListAutoridadBuscar = null;
		aafListPersonaBuscar = null;
		aafAutoridadEditar = null;
		aafPersonaBuscar = new Persona();
		iniciarParametros();
	}
	
	/**
	 * Lista las autoridades validados segun los parámetros ingresados en el
	 * panel de búsqueda
	 */
	public void buscarAutoridad() {
			try {
				aafListAutoridadBuscar = servAutoridadServicio.buscarXTipoXEstado(
						aafAutoridadBuscar.getAtrTipo(),
						aafAutoridadBuscar.getAtrEstado(),
						aafAutoridadBuscar.getAtrFacultad().getFclId());
			} catch (AutoridadNoEncontradoException e) {
				MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Autordad.buscar.por.id.no.result.exception"));
			} catch (AutoridadException e) {
				MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Autoridad.buscar.por.id.exception"));
			} 
	}
	
	/**
	 * Edita la autoridad validada segun los parámetros ingresados en el
	 * panel de búsqueda y si hay otra autoridad activa la pone en inactivo
	 */
	public String editarAutoridad(){
		 try {
			 servAutoridadServicio.editarAutoridad(aafAutoridadBuscar);
			 FacesUtil.mensajeInfo("Autoridad editada exitosamente");
		 } catch (AutoridadNoEncontradoException e) {
				MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Autordad.buscar.por.id.no.result.exception"));
	     } catch (AutoridadException e) {
				MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Autoridad.buscar.por.id.exception"));
		}
		 limpiar();
		 return "irListarAutoridad";
	}
	
	/**
	 * Guarda la autoridad validada segun los parámetros ingresados en el
	 * panel de búsqueda y si hay otra autoridad activa la pone en inactivo
	 * @return "irListarAutoridad" lista de las autoridades
	 * @throws Exception 
	 */
	public String guardarAutoridad() throws Exception {
		aafAutoridadEditar = new Autoridad();
		/* Validación de campos nulos */
		try{
		
		if(aafAutoridadBuscar.getAtrFacultad().getFclId() == aafAutoridadValor ){
			throw new Exception();
		}	
		/* seteo los parametros para mi nueva autoridad */
		aafAutoridadEditar.setAtrIdentificacion(aafPersonaBuscar.getPrsIdentificacion());
		aafAutoridadEditar.setAtrNombres(aafPersonaBuscar.getPrsNombres());
		aafAutoridadEditar.setAtrPrimerApellido(aafPersonaBuscar.getPrsPrimerApellido());
		aafAutoridadEditar.setAtrSegundoApellido(aafPersonaBuscar.getPrsSegundoApellido());
		aafAutoridadEditar.setAtrFacultad(aafAutoridadBuscar.getAtrFacultad());
		aafAutoridadEditar.setAtrTipo(aafAutoridadBuscar.getAtrTipo());
		aafAutoridadEditar.setAtrEstado(GeneralesConstantes.APP_NUEVO);
		aafAutoridadEditar.setAtrSexo(aafAutoridadBuscar.getAtrSexo());
		try {
				servAutoridadServicio.crearAutoridad(aafAutoridadEditar);
				FacesUtil.mensajeInfo("Autoridad creada exitosamente");
				iniciarParametros();
			} catch (AutoridadNoEncontradoException e) {
				FacesUtil.mensajeError("Autoridad no se creo");
			} catch (AutoridadException e) {
				FacesUtil.mensajeError("Autoridad no se creo");
			}
		}catch(Exception e){
			 FacesUtil.mensajeError("Campos obligatorios");
			 return verificarClickAceptarNo();
		}
		limpiar();
		return "irListarAutoridad";
	}	
	
	/**
	 * Lista las personas validados segun los parámetros ingresados en el
	 * panel de búsqueda
	 */
	
	public void buscarPersona(){
		try {
			aafListPersonaBuscar = servPersonaServicio.listarPorIdentificacion(aafPersonaBuscar.getPrsIdentificacion());
		} catch (PersonaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.buscar.por.cedula.no.result.exception")));
		} catch (PersonaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Persona.buscar.por.cedula.exception")));
		}
	}
	

	public String irVerAutoridad(Autoridad autoridad) {
		this.aafAutoridadBuscar = autoridad;
		return "irVerAutoridad";
	}
	
	public String irEditarAutoridad(Autoridad autoridad) {
		this.aafAutoridadBuscar = autoridad;
		return "irEditarAutoridad";
	}
	
	public String irNuevaAutoridad() {
		limpiar();
		aafPersonaBuscar = new Persona();
		return "irNuevaAutoridad";
	}

	public String irCancelarVer() {
		limpiar();
		return "irCancelarVer";
	}
	
	public String irCrearNuevaAutoridad(Persona persona) {
		this.aafPersonaBuscar = persona;
		return "irCrearNuevaAutoridad";
	}
	
	 
	public void asignarEntidad(Autoridad entidad){
		aafAutoridadEditar = entidad;
	}
	
	
	// ****************************************************************/
	// ******************* METODOS AUXILIARES *************************/
	// ****************************************************************/

	// iniciar parametros de busqueda
	private void iniciarParametros() {
		aafAutoridadBuscar = new Autoridad();
		aafAutoridadBuscar.setAtrFacultad(new Facultad());
		aafAutoridadEditar = new Autoridad();
	}
	
	public String verificarClickAceptar(){
		aafValidadorClic = 1;
		return null;
	}
	
	public String verificarClickAceptarNo(){
		aafValidadorClic = 0;
		return null;
	}


	// ****************************************************************/
	// ******************* METODOS GETTER y SETTER ********************/
	// ****************************************************************/

	public Autoridad getAafAutoridadBuscar() {
		return aafAutoridadBuscar;
	}

	public void setAafAutoridadBuscar(Autoridad aafAutoridadBuscar) {
		this.aafAutoridadBuscar = aafAutoridadBuscar;
	}

	public List<Autoridad> getAafListAutoridad() {
		aafListAutoridad = aafListAutoridad == null?(new ArrayList<Autoridad>()):aafListAutoridad;
		return aafListAutoridad;
	}
	
	public void setAafListAutoridad(List<Autoridad> aafListAutoridad) {
		this.aafListAutoridad = aafListAutoridad;
	}

	public List<Autoridad> getAafListAutoridadBuscar() {
		aafListAutoridadBuscar = aafListAutoridadBuscar == null?(new ArrayList<Autoridad>()):aafListAutoridadBuscar;
		return aafListAutoridadBuscar;
	}

	public void setAafListAutoridadBuscar(List<Autoridad> aafListAutoridadBuscar) {
		this.aafListAutoridadBuscar = aafListAutoridadBuscar;
	}

	public Autoridad getAafAutoridadEditar() {
		return aafAutoridadEditar;
	}

	public void setAafAutoridadEditar(Autoridad aafAutoridadEditar) {
		this.aafAutoridadEditar = aafAutoridadEditar;
	}

	public Persona getAafPersonaBuscar() {
		return aafPersonaBuscar;
	}

	public void setAafPersonaBuscar(Persona aafPersonaBuscar) {
		this.aafPersonaBuscar = aafPersonaBuscar;
	}

	public List<Persona> getAafListPersona() {
		aafListPersona = aafListPersona == null?(new ArrayList<Persona>()):aafListPersona;
		return aafListPersona;
	}

	public void setAafListPersona(List<Persona> aafListPersona) {
		this.aafListPersona = aafListPersona;
	}

	public List<Persona> getAafListPersonaBuscar() {
		aafListPersonaBuscar = aafListPersonaBuscar == null?(new ArrayList<Persona>()):aafListPersonaBuscar;
		return aafListPersonaBuscar;
	}

	public void setAafListPersonaBuscar(List<Persona> aafListPersonaBuscar) {
		this.aafListPersonaBuscar = aafListPersonaBuscar;
	}

	public UsuarioRolServicio getServUsuarioRolServicio() {
		return servUsuarioRolServicio;
	}

	public void setServUsuarioRolServicio(UsuarioRolServicio servUsuarioRolServicio) {
		this.servUsuarioRolServicio = servUsuarioRolServicio;
	}

	public AutoridadServicio getServAutoridadServicio() {
		return servAutoridadServicio;
	}

	public void setServAutoridadServicio(AutoridadServicio servAutoridadServicio) {
		this.servAutoridadServicio = servAutoridadServicio;
	}

	public PersonaServicio getServPersonaServicio() {
		return servPersonaServicio;
	}

	public void setServPersonaServicio(PersonaServicio servPersonaServicio) {
		this.servPersonaServicio = servPersonaServicio;
	}

	public List<Facultad> getAafListFacultad() {
		aafListFacultad = aafListFacultad == null?(new ArrayList<Facultad>()):aafListFacultad;
		return aafListFacultad;
	}

	public void setAafListFacultad(List<Facultad> aafListFacultad) {
		this.aafListFacultad = aafListFacultad;
	}

	public Integer getaafValidadorClic() {
		return aafValidadorClic;
	}

	public void setaafValidadorClic(Integer aafValidadorClic) {
		this.aafValidadorClic = aafValidadorClic;
	}
	
	
}
