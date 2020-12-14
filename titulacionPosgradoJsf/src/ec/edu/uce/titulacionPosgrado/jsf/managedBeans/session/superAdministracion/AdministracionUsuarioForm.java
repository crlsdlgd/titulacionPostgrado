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
   
 ARCHIVO:     AdministracionUsuarioForm.java	  
 DESCRIPCION: Bean que maneja las peticiones de la tabla convocatoria.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
15 - Febrero - 2017		Daniel Albuja                        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.managedBeans.session.superAdministracion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.UsuarioRolDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.UsuarioRolJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FacultadNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaValidacionException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RolFlujoCarreraException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RolFlujoCarreraNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RolFlujoCarreraValidacionException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolJdbcDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolJdbcDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.ConvocatoriaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.FacultadServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Carrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Facultad;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Usuario;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.titulacionPosgrado.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) AdministracionUsuarioForm.
 * Managed Bean que maneja las peticiones de la tabla Usuario , Usuario_rol , Rol_flujo_carrera.
 * @author dalbuja.
 * @version 1.0
 */

@ManagedBean(name="administracionUsuarioForm")
@SessionScoped
public class AdministracionUsuarioForm implements Serializable{

	private static final long serialVersionUID = 8026853400702897855L;
	
	// *****************************************************************/
	// ******************* Variables de AdministracionUsuarioForm********/
	// *****************************************************************/
	
	private Facultad aufFacultad;
	private List<Facultad> aufListFacultad;
	private Carrera aufCarrera;
	private List<Carrera> aufListCarrera;
	private UsuarioRolJdbcDto aufUsuarioRolJdbcDtoBuscar;
	private List<UsuarioRolJdbcDto> aufListCarrerasXIdentificacion;
	private UsuarioRolJdbcDto aufUsuarioRolJdbcDtoEditar;
	private List<UsuarioRolJdbcDto> aufListaufUsuarioRolJdbcDtoBuscar;
	private boolean aufHabilitadorCarrera;
	private Integer aufValidadorClic;
	private String mensajeModal;
	private String mensajeTituloModal;

	// ******************************************************************/
	// ******************* Servicios Generales **************************/
	// ******************************************************************/
	@EJB
	private ConvocatoriaServicio servConvocatoriaServicio;
	@EJB
	private UsuarioRolServicio servUsuarioRolServicio;
	@EJB
	private FacultadServicio servAufFacultadServicio;
	@EJB
	private CarreraServicio servAufCarreraServicio;
	@EJB
	private PersonaServicio servAufPersonaServicio;
	@EJB
	private UsuarioRolDtoServicioJdbc servAufUsuarioRolDtoServicioJdbc;
	@EJB
	private	RolFlujoCarreraServicio servRolFlujoCarreraServicio;
	
	//****************************************************************/
	//******** METODO GENERAL DE INICIALIZACION DE VARIABLES *********/
	//****************************************************************/
	@PostConstruct
	public void inicializar(){
	}
	
	
	//****************************************************************/
	//******** METODOS DE NAVEGACION ********** **********************/
	//****************************************************************/
	
	/**
	 * Método que dirige a la página de Listar Usuarios 
	 * @param usuario
	 * @return
	 */
	public String irListar(Usuario usuario){
			try {
				UsuarioRol usro = servUsuarioRolServicio.buscarPorIdentificacionActivos(usuario.getUsrIdentificacion());
				if(usro.getUsroEstado().intValue()==UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
					FacesUtil.mensajeInfo("Usuario se encuentra inactivo, por favor contáctese con soporte técnico");
					return null;
				}
				aufListaufUsuarioRolJdbcDtoBuscar=null;
				iniciarParametros();
				aufListFacultad = servAufFacultadServicio.listarTodos();
				aufListCarrera = new ArrayList<Carrera>();
			} catch (UsuarioRolNoEncontradoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Error al buscar el usuario, comuníquese con la Dirección de Tecnologías.");
				return null;
			} catch (UsuarioRolException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Error al buscar el usuario, comuníquese con la Dirección de Tecnologías.");
				return null;
			} catch (FacultadNoEncontradoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Error al buscar el usuario, comuníquese con la Dirección de Tecnologías.");
				return null;
			}
		return "irListarAdmUsuario";
	}

	/**
	 * Maneja de cancelacion del listar usuarios
	 * @return - cadena de navegación a la página de inicio
	 */
	public String cancelarListar(){
		aufListaufUsuarioRolJdbcDtoBuscar=null;
		return "irInicio";
	}
	
	//****************************************************************/
	//******** METODOS DE LIMPIEZA DE VARIABLES **********************/
	//****************************************************************/
	
	/**
	 * Método para limpiar los componentes del formulario
	 * 
	 */
	public void limpiar() {
		aufListCarrera= new ArrayList<Carrera>();
		aufHabilitadorCarrera=true;
		aufUsuarioRolJdbcDtoBuscar.setPrsIdentificacion(null);
		aufListaufUsuarioRolJdbcDtoBuscar=new ArrayList<UsuarioRolJdbcDto>();
		aufUsuarioRolJdbcDtoEditar=null;
		aufFacultad.setFclId(GeneralesConstantes.APP_ID_BASE);
		aufValidadorClic = 0;
	}
	
	/**
	 * Lista los estudiantes validados segun los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscarUsuarios(){
		if(aufFacultad.getFclId()==GeneralesConstantes.APP_ID_BASE){
			aufCarrera.setCrrId(GeneralesConstantes.APP_ID_BASE);
		}
		try {
			aufListaufUsuarioRolJdbcDtoBuscar = servAufUsuarioRolDtoServicioJdbc.buscarXIdentificacionXFacultadXCarrea(aufUsuarioRolJdbcDtoBuscar.getPrsIdentificacion(), aufFacultad.getFclId(), aufCarrera.getCrrId());
			if(aufListaufUsuarioRolJdbcDtoBuscar.size()==0){
				limpiar();
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("No se encontraron usuarios con los parámetros ingresados");	
			}
		} catch (UsuarioRolJdbcDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Excepción al buscar usuarios con los parámetros ingresados");
		} catch (UsuarioRolJdbcDtoNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se encontraron usuarios con los parámetros ingresados");
		}
	}
	
	/**
	 * Lista los estudiantes validados segun los parámetros ingresados en el panel de búsqueda 
	 */
	public void activarDesactivarCarrera(UsuarioRolJdbcDto entidad){
		this.aufUsuarioRolJdbcDtoBuscar = entidad;
		try {
			if(aufUsuarioRolJdbcDtoBuscar.getRoflcrEstado()==RolFlujoCarreraConstantes.ROL_FLUJO_CARRERA_ESTADO_ACTIVO_VALUE){
				servRolFlujoCarreraServicio.desactivarRolFlujoCarreraXUsuarioRol(aufUsuarioRolJdbcDtoBuscar.getRoflcrId());	
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Se ha desactivado la carrera seleccionada para el usuario actual");
				irVerUsuario(aufUsuarioRolJdbcDtoEditar);
			}else if(aufUsuarioRolJdbcDtoBuscar.getRoflcrEstado()==RolFlujoCarreraConstantes.ROL_FLUJO_CARRERA_ESTADO_INACTIVO_VALUE){
				servRolFlujoCarreraServicio.activarRolFlujoCarreraXUsuarioRol(aufUsuarioRolJdbcDtoBuscar.getRoflcrId(),aufUsuarioRolJdbcDtoBuscar.getRolId() );
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Se ha activado la carrera seleccionada para el usuario actual");
				irVerUsuario(aufUsuarioRolJdbcDtoEditar);
			}
		}  catch (RolFlujoCarreraException e) {
			irVerUsuario(aufUsuarioRolJdbcDtoEditar);
		} catch (RolFlujoCarreraNoEncontradoException e) {
		} catch (RolFlujoCarreraValidacionException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}catch (Exception e){
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	public String desactivar(){
		try {
			if(aufUsuarioRolJdbcDtoEditar.getUsroEstado()==UsuarioRolConstantes.ESTADO_ACTIVO_VALUE){
				servUsuarioRolServicio.desactivarUsuarioRolXid(aufUsuarioRolJdbcDtoEditar.getUsroId(), aufUsuarioRolJdbcDtoEditar.getUsrId());
				servRolFlujoCarreraServicio.desactivarRolFlujoCarrerasXListaUsuarioRol(aufUsuarioRolJdbcDtoEditar.getUsroId());	
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("El usuario ha sido deshabilitado del sistema");
			}else if(aufUsuarioRolJdbcDtoEditar.getUsroEstado()==UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
				servUsuarioRolServicio.activarUsuarioRolXid(aufUsuarioRolJdbcDtoEditar.getUsroId(), aufUsuarioRolJdbcDtoEditar.getUsrId());
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("El usuario ha sido habilitado en el sistema");
			}
		} catch (Exception e) {
		}
		aufListaufUsuarioRolJdbcDtoBuscar = null;
		verificarClickAceptarTramiteNo();
		return "irInicio";
	}
	
	
	public String irVerUsuario(UsuarioRolJdbcDto entidad){
		this.aufUsuarioRolJdbcDtoBuscar = entidad;
		try {
			aufListCarrerasXIdentificacion = servAufUsuarioRolDtoServicioJdbc.buscarCarrerasXIdentificacion(aufUsuarioRolJdbcDtoBuscar.getPrsIdentificacion());
		} catch (UsuarioRolJdbcDtoException e) {
		} catch (UsuarioRolJdbcDtoNoEncontradoException e) {
		}
		return "irVerUsuario";
	}
	
	
	public String irCancelarVer(){
		limpiar();
		return "irCancelarVer";
	}
	
	
	public String irEditar(UsuarioRolJdbcDto entidad){
		this.aufUsuarioRolJdbcDtoEditar = entidad;
		try {
			aufListCarrerasXIdentificacion = servAufUsuarioRolDtoServicioJdbc.buscarCarrerasXIdentificacion(aufUsuarioRolJdbcDtoEditar.getPrsIdentificacion());
		} catch (UsuarioRolJdbcDtoException e) {
		} catch (UsuarioRolJdbcDtoNoEncontradoException e) {
		} 
		return "irEditarUsuario";
	}
	
	public String irCancelarEdicion(){
		limpiar();
		return "irCancelarEdicion";
	}

	/**
	 * Guarda el Usuario a editar
	 * @return navegacion a la pagina de informacion general
	 */
		public String guardarUsuario(){
			try {
				servAufPersonaServicio.editarXId(aufUsuarioRolJdbcDtoEditar);
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Usuario.actualizar.datos.exitosamente")));
			} catch (PersonaValidacionException | PersonaNoEncontradoException
					| PersonaException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Usuario.actualizar.datos.exception")));
			}
			return "irInicio";
	}
	
	
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
			
	//iniciar parametros de busqueda
	private void iniciarParametros(){
		aufUsuarioRolJdbcDtoBuscar =  null;
		aufUsuarioRolJdbcDtoBuscar  = new UsuarioRolJdbcDto();
		aufCarrera = null;
		aufCarrera = new Carrera();
		aufFacultad = null;
		aufFacultad = new Facultad();
		aufHabilitadorCarrera=true;
		aufValidadorClic = 0;
	}

	/**
	 * Método para cargar la lista de carreras de acuerdo a la facultad seleccionada
	 * 
	 */
	public void cambiarFacultad() {
		if(aufFacultad!=null || aufFacultad.getFclId()!=GeneralesConstantes.APP_ID_BASE){
			try {
				aufListCarrera=servAufCarreraServicio.listarCarrerasXFacultad(aufFacultad);
				aufHabilitadorCarrera=false;
			} catch (CarreraNoEncontradoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeWarn("No se encontraron Programas de Posgrado en la Facultad seleccionada.");
				limpiar();
			}	
		}else{
			aufListCarrera= new ArrayList<Carrera>();
		}
	}

	public String verificarClickAceptarTramite(UsuarioRolJdbcDto entidad){
		this.aufUsuarioRolJdbcDtoEditar=entidad;
		aufValidadorClic = 1;
		if(aufUsuarioRolJdbcDtoEditar.getUsroEstado()==UsuarioRolConstantes.ESTADO_ACTIVO_VALUE){
			mensajeTituloModal="Desactivar usuario";
			mensajeModal="¿Está seguro que desea desactivar el usuario? Recuerde... desactivará el usuario del sistema";
		}else{
			mensajeTituloModal="Activar usuario";
			mensajeModal="¿Está seguro que desea activar el usuario? Recuerde... deberá asignar carreras";
		}
		
		return null;
	}
	
	public String verificarClickAceptarTramiteNo(){
		aufValidadorClic = 0;
		return null;
	}

	//****************************************************************/
	//******************* METODOS GET / SET **************************/
	//****************************************************************/	

	public Facultad getAufFacultad() {
		return aufFacultad;
	}


	public void setAufFacultad(Facultad aufFacultad) {
		this.aufFacultad = aufFacultad;
	}


	public List<Facultad> getAufListFacultad() {
		aufListFacultad = aufListFacultad==null?(new ArrayList<Facultad>()):aufListFacultad;
		return aufListFacultad;
	}


	public void setAufListFacultad(List<Facultad> aufListFacultad) {
		this.aufListFacultad = aufListFacultad;
	}


	public Carrera getAufCarrera() {
		return aufCarrera;
	}


	public void setAufCarrera(Carrera aufCarrera) {
		this.aufCarrera = aufCarrera;
	}


	public List<Carrera> getAufListCarrera() {
		aufListCarrera = aufListCarrera==null?(new ArrayList<Carrera>()):aufListCarrera;
		return aufListCarrera;
	}


	public void setAufListCarrera(List<Carrera> aufListCarrera) {
		this.aufListCarrera = aufListCarrera;
	}


	public UsuarioRolJdbcDto getAufUsuarioRolJdbcDtoBuscar() {
		return aufUsuarioRolJdbcDtoBuscar;
	}


	public void setAufUsuarioRolJdbcDtoBuscar(
			UsuarioRolJdbcDto aufUsuarioRolJdbcDtoBuscar) {
		this.aufUsuarioRolJdbcDtoBuscar = aufUsuarioRolJdbcDtoBuscar;
	}


	public UsuarioRolJdbcDto getAufUsuarioRolJdbcDtoEditar() {
		return aufUsuarioRolJdbcDtoEditar;
	}


	public void setAufUsuarioRolJdbcDtoEditar(
			UsuarioRolJdbcDto aufUsuarioRolJdbcDtoEditar) {
		this.aufUsuarioRolJdbcDtoEditar = aufUsuarioRolJdbcDtoEditar;
	}


	public List<UsuarioRolJdbcDto> getAufListaufUsuarioRolJdbcDtoBuscar() {
		aufListaufUsuarioRolJdbcDtoBuscar = aufListaufUsuarioRolJdbcDtoBuscar==null?(new ArrayList<UsuarioRolJdbcDto>()):aufListaufUsuarioRolJdbcDtoBuscar;
		return aufListaufUsuarioRolJdbcDtoBuscar;
	}


	public void setAufListaufUsuarioRolJdbcDtoBuscar(
			List<UsuarioRolJdbcDto> aufListaufUsuarioRolJdbcDtoBuscar) {
		this.aufListaufUsuarioRolJdbcDtoBuscar = aufListaufUsuarioRolJdbcDtoBuscar;
	}


	public boolean isAufHabilitadorCarrera() {
		return aufHabilitadorCarrera;
	}


	public void setAufHabilitadorCarrera(boolean aufHabilitadorCarrera) {
		this.aufHabilitadorCarrera = aufHabilitadorCarrera;
	}


	public List<UsuarioRolJdbcDto> getAufListCarrerasXIdentificacion() {
		aufListCarrerasXIdentificacion = aufListCarrerasXIdentificacion==null?(new ArrayList<UsuarioRolJdbcDto>()):aufListCarrerasXIdentificacion;
		return aufListCarrerasXIdentificacion;
	}


	public void setAufListCarrerasXIdentificacion(
			List<UsuarioRolJdbcDto> aufListCarrerasXIdentificacion) {
		this.aufListCarrerasXIdentificacion = aufListCarrerasXIdentificacion;
	}
	
	
	
	public Integer getAufValidadorClic() {
		return aufValidadorClic;
	}


	public void setAufValidadorClic(Integer aufValidadorClic) {
		this.aufValidadorClic = aufValidadorClic;
	}


	public String getMensajeModal() {
		return mensajeModal;
	}


	public void setMensajeModal(String mensajeModal) {
		this.mensajeModal = mensajeModal;
	}


	public String getMensajeTituloModal() {
		return mensajeTituloModal;
	}


	public void setMensajeTituloModal(String mensajeTituloModal) {
		this.mensajeTituloModal = mensajeTituloModal;
	}
	

}
