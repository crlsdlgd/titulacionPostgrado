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
   
 ARCHIVO:     NuevoUsuarioForm.java	  
 DESCRIPCION: Bean que maneja las peticiones de la tabla Usuario , Usuario_rol , Rol_flujo_carrera.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 23-04-2018		Daniel Albuja                        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.managedBeans.session.superAdministracion;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.UbicacionDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.UsuarioCreacionDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.UsuarioRolJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EtniaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FacultadNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RegistroPostulanteDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RegistroPostulanteDtoValidacionException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RolFlujoCarreraException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RolFlujoCarreraNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UbicacionDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UbicacionDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolJdbcDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolJdbcDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.ConvocatoriaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.EtniaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.FacultadServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.RegistroPostulanteDtoServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.RolServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.UbicacionDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.UsuarioRolDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UbicacionConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UsuarioConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Carrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Etnia;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Facultad;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Rol;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Usuario;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.titulacionPosgrado.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) NuevoUsuarioForm.
 * Managed Bean que maneja las peticiones de la tabla Usuario , Usuario_rol , Rol_flujo_carrera.
 * @author dalbuja.
 * @version 1.0
 */

@ManagedBean(name="nuevoUsuarioForm")
@SessionScoped
public class NuevoUsuarioForm implements Serializable{

	private static final long serialVersionUID = 8026853400702897855L;
	
		
	// *****************************************************************/
	// ******************* Variables de AdministracionUsuarioForm********/
	// *****************************************************************/
	
	private Facultad nufFacultad;
	private List<Facultad> nufListFacultad;
	private Carrera nufCarrera;
	private List<Carrera> nufListCarrera;
	private Rol nufRol;
	private List<Rol> nufListRol;
	private UsuarioRolJdbcDto nufUsuarioRolJdbcDtoBuscar;
	private List<UsuarioRolJdbcDto> nufListUsuariosXCarreraXRol;
	private UsuarioCreacionDto nufUsuarioCreacionDtoCrear;
	private UsuarioRolJdbcDto nufUsuarioRolJdbcDtoCrear;
	private List<Etnia> nufListEtnias;
	private List<UbicacionDto> nufListPaises;
	
	private List<UsuarioRolJdbcDto> nufListaufUsuarioRolJdbcDtoBuscar;
	private boolean nufHabilitadorFacultad;
	private boolean nufHabilitadorCarrera;
	private boolean nufHabilitadorRol;
	private boolean nufHabilitadorGuardar;
	
	// ******************************************************************/
	// ******************* Servicios Generales **************************/
	// ******************************************************************/
	@EJB
	private ConvocatoriaServicio servConvocatoriaServicio;
	@EJB
	private UsuarioRolServicio servUsuarioRolServicio;
	@EJB
	private FacultadServicio servNufFacultadServicio;
	@EJB
	private CarreraServicio servNufCarreraServicio;
	@EJB
	private UsuarioRolDtoServicioJdbc servNufUsuarioRolDtoServicioJdbc;
	
	@EJB
	private RolServicio servNufRolServicio;
	@EJB
	private EtniaServicio servRpfEtnia;
	@EJB
	private UbicacionDtoServicioJdbc servRpfUbicacionJdbc;
	
	@EJB
	private	RolFlujoCarreraServicio servRolFlujoCarreraServicio;
	
	@EJB
	private RegistroPostulanteDtoServicio srvRegistroPostulanteDtoServicio;
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
	public String irNuevo(Usuario usuario){
			try {
				UsuarioRol usro = servUsuarioRolServicio.buscarPorIdentificacionActivos(usuario.getUsrIdentificacion());
				if(usro.getUsroEstado().intValue()==UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeInfo("Usuario se encuentra inactivo, por favor contáctese con soporte técnico");
					return null;
				}
				iniciarParametros();
				//genero la lista de etnias 
				nufListEtnias = servRpfEtnia.listarTodos();
				//genero la lista de paises 
				nufListPaises = servRpfUbicacionJdbc.listarXjerarquia(UbicacionConstantes.TIPO_JERARQUIA_PAIS_VALUE);
				return "irUsuarioNuevo";
			} catch (UsuarioRolNoEncontradoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Rol no encontrado para el usuario con identificacion " + usuario.getUsrIdentificacion());
			} catch (UsuarioRolException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Excepción al buscar el rol del usuario");
			} catch (UbicacionDtoJdbcException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Excepción al listar las ubicaciones");
			} catch (UbicacionDtoJdbcNoEncontradoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Excepción de ubicaciones no encontradas");
			} catch (EtniaNoEncontradoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Excepción al listar las etinias");
			} 
			return null;
	}

	/**
	 * Método que dirige a la página de Listar Usuarios 
	 * @param usuario
	 * @return
	 */
	public String irAdministrarCarreras(Usuario usuario){
		nufListUsuariosXCarreraXRol= new ArrayList<UsuarioRolJdbcDto>();
		try {
			nufListFacultad = servNufFacultadServicio.listarTodos();
			nufListRol=servNufRolServicio.listarTodos();
			ListIterator<Rol> iterator = nufListRol.listIterator(nufListRol.size());
			while(iterator.hasNext()){
			    Rol rolActual = iterator.next();
			    if(rolActual.getRolId()==RolConstantes.ROL_BD_ADMINISTRADOR_VALUE){
			        iterator.remove();// No se desea que agregue administradores
			    }
			    if(rolActual.getRolId()==RolConstantes.ROL_BD_POSTULANTE_VALUE){
			        iterator.remove();// No se desea que agregue postulantes
			    }
//			    if(rolActual.getRolId()==RolConstantes.ROL_BD_EDITOR_DGIP_VALUE){
//			    	rolActual.setRolDescripcion("DIRECCION DE POSGRADOS PROPESIONALES");
//			    	iterator.set(rolActual);
//			    }
			    if(rolActual.getRolId()==RolConstantes.ROL_BD_EDITOR_ACTA_VALUE){
			        iterator.remove();// No se desea que agregue editor_acta
			    }
//			    if(rolActual.getRolId()==RolConstantes.ROL_BD_CONSULTOR_VALUE){
//			        iterator.remove();// No se desea que agregue editor_acta
//			    }
			}
			for (Rol item : nufListRol) {
				if(item.getRolId()==RolConstantes.ROL_BD_POSTULANTE_VALUE){
			        item.setRolDescripcion("DIRECCION DE POSGRADOS PROPESIONALES");
			    }
			}
		} catch (FacultadNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("Excepción al listar las facultades");
		}
			try {
				UsuarioRol usro = servUsuarioRolServicio.buscarPorIdentificacionActivos(usuario.getUsrIdentificacion());
				if(usro.getUsroEstado().intValue()==UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeInfo("Usuario se encuentra inactivo, por favor contáctese con soporte técnico");
					return null;
				}
				iniciarParametros();
				return "irAdministrarcarreras";
			} catch (UsuarioRolNoEncontradoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Rol no encontrado para el usuario con identificacion " + usuario.getUsrIdentificacion());
			} catch (UsuarioRolException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Excepción al buscar el rol del usuario");
			} 
			return null;
	}
	
	/**
	 * Maneja de cancelacion del listar usuarios
	 * @return - cadena de navegacion a la pagina de inicio
	 */
	public String cancelarNuevo(){
//		limpiar();
		
		return "irInicio";
	}
	
	/**
	 * Maneja la navegación hacia nuevoAgregarCarreras.xhtml
	 * @return - cadena de navegacion a la pagina 
	 */
	public String irAgregarCarrerasNuevoUsuario(){
		nufUsuarioCreacionDtoCrear.setPrsMailPersonal(nufUsuarioCreacionDtoCrear.getPrsMailPersonal().toLowerCase());
		nufListUsuariosXCarreraXRol= new ArrayList<UsuarioRolJdbcDto>();
		try {
			nufListFacultad = servNufFacultadServicio.listarTodos();
			nufListRol=servNufRolServicio.listarTodos();
			ListIterator<Rol> iterator = nufListRol.listIterator(nufListRol.size());
			while(iterator.hasNext()){
			    Rol rolActual = iterator.next();
			    if(rolActual.getRolId()==RolConstantes.ROL_BD_ADMINISTRADOR_VALUE){
			        iterator.remove();// No se desea que agregue administradores
			    }
			    if(rolActual.getRolId()==RolConstantes.ROL_BD_POSTULANTE_VALUE){
			        iterator.remove();// No se desea que agregue postulantes
			    }
//			    if(rolActual.getRolId()==RolConstantes.ROL_BD_EDITOR_DGIP_VALUE){
//			    	rolActual.setRolDescripcion("DIRECCION DE POSGRADOS PROPESIONALES");
//			    	iterator.set(rolActual);
//			    }
			    if(rolActual.getRolId()==RolConstantes.ROL_BD_EDITOR_ACTA_VALUE){
			        iterator.remove();// No se desea que agregue editor_acta
			    }
//			    if(rolActual.getRolId()==RolConstantes.ROL_BD_CONSULTOR_VALUE){
//			        iterator.remove();// No se desea que agregue editor_acta
//			    }
			}
			for (Rol item : nufListRol) {
				if(item.getRolId()==RolConstantes.ROL_BD_POSTULANTE_VALUE){
			        item.setRolDescripcion("DIRECCION DE POSGRADOS PROPESIONALES");
			    }
			}
		} catch (FacultadNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("Excepción al listar las facultades");
		}
		
		nufListCarrera = new ArrayList<Carrera>();
		
		/******************** se formatea los datos de usuario *********************************/
		
		
		nufUsuarioCreacionDtoCrear.setUsrIdentificacion(nufUsuarioCreacionDtoCrear.getPrsIdentificacion());
		nufUsuarioCreacionDtoCrear.setUsrActiveDirectory(UsuarioConstantes.ACTIVE_DIRECTORY_SI_VALUE);
		nufUsuarioCreacionDtoCrear.setUsrEstado(UsuarioConstantes.ESTADO_ACTIVO_VALUE);
		nufUsuarioCreacionDtoCrear.setUsrEstSesion(UsuarioConstantes.ESTADO_SESSION_SI_LOGGEADO_VALUE);
		nufUsuarioCreacionDtoCrear.setUsrPassword("6334aea67c46e31f7efd924cc7c1fd36");
		nufUsuarioCreacionDtoCrear.setUsrFechaCreacion(new Timestamp((new Date()).getTime()));
		Date fechaActual = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(fechaActual);
		c.add(Calendar.YEAR, 1);
		Date fechaCaducidad = new Date();
		fechaCaducidad = c.getTime();
		nufUsuarioCreacionDtoCrear.setUsrFechaCaducidad(fechaCaducidad);
		nufUsuarioCreacionDtoCrear.setUsrFechaCadPass(fechaCaducidad);
		int indice = nufUsuarioCreacionDtoCrear.getPrsMailInstitucional().indexOf("@");
		String usrNick = nufUsuarioCreacionDtoCrear.getPrsMailInstitucional().substring(0,indice);
		nufUsuarioCreacionDtoCrear.setUsrNick(usrNick);
		nufUsuarioCreacionDtoCrear.setPrsEtn(null);
		nufUsuarioCreacionDtoCrear.setPrsFechaNacimiento(null);
		nufUsuarioCreacionDtoCrear.setPrsTelefono(null);
		nufUsuarioCreacionDtoCrear.setPrsUbcNac(null);
		nufUsuarioCreacionDtoCrear.setPrsTelefono(null);
		return "irAgregarCarrerasNuevoUsuario";
	}
	
	//****************************************************************/
	//******** METODOS DE LIMPIEZA DE VARIABLES **********************/
	//****************************************************************/
	
	/**
	 * Método para limpiar los componenetes del formulario
	 * 
	 */
	public void limpiar() {
		nufUsuarioCreacionDtoCrear= new UsuarioCreacionDto();
		nufListCarrera= new ArrayList<Carrera>();
		nufHabilitadorCarrera=true;
		nufListaufUsuarioRolJdbcDtoBuscar=new ArrayList<UsuarioRolJdbcDto>();
		nufHabilitadorCarrera=true;
		nufHabilitadorRol=true;
		nufHabilitadorGuardar=true;
		nufHabilitadorFacultad= true;
	}
	
	
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
			
	//iniciar parametros de busqueda
	private void iniciarParametros(){
		nufUsuarioCreacionDtoCrear =  null;
		nufUsuarioCreacionDtoCrear = new UsuarioCreacionDto();
		nufUsuarioRolJdbcDtoCrear = new UsuarioRolJdbcDto();
		nufCarrera = null;
		nufCarrera = new Carrera();
		nufFacultad = null;
		nufFacultad = new Facultad();
		nufRol = null;
		nufRol = new Rol();
		nufHabilitadorFacultad= true;
		nufHabilitadorCarrera=true;
		nufHabilitadorRol=true;
		nufHabilitadorGuardar=true;
		nufListUsuariosXCarreraXRol= new ArrayList<UsuarioRolJdbcDto>();
	}

	/**
	 * Método para buscar el usuario que se ingresa su identificación
	 * 
	 */
	public void buscarUsuario() {
		nufUsuarioRolJdbcDtoBuscar = null;
		try {
			this.nufUsuarioRolJdbcDtoBuscar=servNufUsuarioRolDtoServicioJdbc.buscarUsuariosXIdentificacion(nufUsuarioCreacionDtoCrear.getPrsIdentificacion());
			nufUsuarioCreacionDtoCrear.setPrsNombres(nufUsuarioRolJdbcDtoBuscar.getPrsNombres());
			nufUsuarioCreacionDtoCrear.setPrsPrimerApellido(nufUsuarioRolJdbcDtoBuscar.getPrsPrimerApellido());
			nufUsuarioCreacionDtoCrear.setPrsSegundoApellido(nufUsuarioRolJdbcDtoBuscar.getPrsSegundoApellido());
			if(nufUsuarioCreacionDtoCrear.getPrsPrimerApellido()!=null){
				nufHabilitadorFacultad= false;	
			}else{
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("No se encontraron usuarios con los parámetros ingresados");
			}
			
		}  catch (UsuarioRolJdbcDtoException e) {
			limpiar();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Excepción al buscar usuarios con los parámetros ingresados");
		} catch (UsuarioRolJdbcDtoNoEncontradoException e) {
			limpiar();
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No se encontraron usuarios con los parámetros ingresados");
		}catch (Exception e) {
		}
	}
	
	
	/**
	 * Método para cargar la lista de carreras de acuerdo a la facultad seleccionada
	 * 
	 */
	public void cambiarFacultad() {
		nufListUsuariosXCarreraXRol=null;
		nufListUsuariosXCarreraXRol = new ArrayList<UsuarioRolJdbcDto>();
		nufRol.setRolId(GeneralesConstantes.APP_ID_BASE);
		if(nufFacultad!=null && nufFacultad.getFclId()!=GeneralesConstantes.APP_ID_BASE ){
			try {
				nufListCarrera=servNufCarreraServicio.listarCarrerasXFacultad(nufFacultad);
				nufHabilitadorCarrera=false;
			} catch (CarreraNoEncontradoException e) {
				limpiar();
			}	
		}else{
			nufHabilitadorCarrera=true;
			nufHabilitadorRol=true;
			nufHabilitadorGuardar=true;
		}
	}

	/**
	 * Método para cargar la lista de usuarios de acuerdo a la carrera seleccionada
	 * 
	 */
	public void cambiarCarrera() {
		nufListUsuariosXCarreraXRol=null;
		nufListUsuariosXCarreraXRol=new ArrayList<UsuarioRolJdbcDto>();
		if(nufCarrera!=null && nufCarrera.getCrrId()!=GeneralesConstantes.APP_ID_BASE && nufCarrera.getCrrId()!=0){
			nufRol.setRolId(GeneralesConstantes.APP_ID_BASE);
			nufHabilitadorRol=false;	
		}else{
			nufRol.setRolId(GeneralesConstantes.APP_ID_BASE);
			nufHabilitadorRol=true;
			nufHabilitadorGuardar=true;
		}
	}
	
	/**
	 * Método para cargar la lista de usuarios de acuerdo a la carrera seleccionada
	 * 
	 */
	public void cambiarRol() {
		nufListUsuariosXCarreraXRol=null;
		nufListUsuariosXCarreraXRol=new ArrayList<UsuarioRolJdbcDto>();
		if(nufRol!=null && nufRol.getRolId()!=GeneralesConstantes.APP_ID_BASE && nufRol.getRolId()!=0){
			nufHabilitadorCarrera=false;
			try {
				nufListUsuariosXCarreraXRol=servNufUsuarioRolDtoServicioJdbc.buscarUsuarioXFacultadXCarreraXRol(nufRol.getRolId(), nufFacultad.getFclId(), nufCarrera.getCrrId());
			}  catch (UsuarioRolJdbcDtoException e) {
				limpiar();
			} catch (UsuarioRolJdbcDtoNoEncontradoException e) {
				limpiar();
			}	
		}else{
			nufHabilitadorGuardar=true;
		}
		if(nufListUsuariosXCarreraXRol.size()>1){
			nufHabilitadorGuardar=true;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Existen más de un usuario con el mismo rol asignado a la programa, comuníquese con la Dirección de Tecnologías");
		}else if (nufListUsuariosXCarreraXRol.size()==0){
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("No existen usuarios con el rol asignado para la programa seleccionada, puede continuar con el registro");
			nufHabilitadorGuardar=false;
		}else{
			nufHabilitadorGuardar=true;
		}
	}
	
	/**
	 * Método para desactivar la carrera del usuario seleccionado
	 * 
	 */
	public void desactivarUsuario() {
		boolean resultado = false;
		try {
			resultado = servRolFlujoCarreraServicio.desactivarRolFlujoCarreraXUsuarioRol(nufListUsuariosXCarreraXRol.get(0).getRoflcrId());
		} catch (RolFlujoCarreraException
				| RolFlujoCarreraNoEncontradoException e) {
		}
		if(resultado){
			nufListUsuariosXCarreraXRol.get(0).setRoflcrEstado(RolFlujoCarreraConstantes.ROL_FLUJO_CARRERA_ESTADO_INACTIVO_VALUE);
			
			
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("Se ha deshabilitado la carrera para el usuario");
		}else{
			nufHabilitadorGuardar=true;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error al deshabilitar la carrera para el usuario");
		}
		try {
			nufListUsuariosXCarreraXRol=servNufUsuarioRolDtoServicioJdbc.buscarUsuarioXFacultadXCarreraXRol(nufRol.getRolId(), nufFacultad.getFclId(), nufCarrera.getCrrId());
		}  catch (UsuarioRolJdbcDtoException e) {
		} catch (UsuarioRolJdbcDtoNoEncontradoException e) {
		}	
		if(nufListUsuariosXCarreraXRol.size()==0){
			nufHabilitadorGuardar=false;
		}
		
	}
	
	
	/**
	 * Método para guardar el rol y la carrera asignada al usuario
	 * 
	 */
	public String guardarUsuario() {
		//se hace la inserción en la base de datos
		try {
			srvRegistroPostulanteDtoServicio.anadirUsuarioRolFlujoCarrera(nufUsuarioCreacionDtoCrear,nufRol.getRolId(),nufCarrera.getCrrId());
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("Se ha creado el usuario con su rol respectivo en la carrera seleccionada");
		} catch (RegistroPostulanteDtoValidacionException
				| RegistroPostulanteDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			limpiar();
			return "irInicio";
		}
		limpiar();
		return "irInicio";
	}
	
	/**
	 * Método para guardar el rol y la carrera asignada al usuario
	 * 
	 */
	public String guardarRolFlujoCarrera() {
		//se hace la inserción en la base de datos
		
		try {
			srvRegistroPostulanteDtoServicio.anadirRolFlujoCarrera(nufUsuarioRolJdbcDtoBuscar,nufRol.getRolId(),nufCarrera.getCrrId());
		} catch (RegistroPostulanteDtoValidacionException
				| RegistroPostulanteDtoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			limpiar();
			return "irInicio";
		}
		FacesUtil.limpiarMensaje();
		FacesUtil.mensajeInfo("Se ha agregado la carrera seleccionada con el rol asignado al usuario actual");
		limpiar();
		return "irInicio";
	}
	
//	public String verificarClickAceptarTramite(){
//		aufValidadorClic = 1;
//		return null;
//	}
//	
//	public String verificarClickAceptarTramiteNo(){
//		aufValidadorClic = 0;
//		return null;
//	}

	//****************************************************************/
	//******************* METODOS GET / SET **************************/
	//****************************************************************/	

	public List<UsuarioRolJdbcDto> getNufListUsuariosXCarreraXRol() {
		nufListUsuariosXCarreraXRol = nufListUsuariosXCarreraXRol==null?(new ArrayList<UsuarioRolJdbcDto>()):nufListUsuariosXCarreraXRol;
		return nufListUsuariosXCarreraXRol;
	}

	public void setNufListUsuariosXCarreraXRol(
			List<UsuarioRolJdbcDto> nufListUsuariosXCarreraXRol) {
		this.nufListUsuariosXCarreraXRol = nufListUsuariosXCarreraXRol;
	}

	public Facultad getNufFacultad() {
		return nufFacultad;
	}

	public void setNufFacultad(Facultad nufFacultad) {
		this.nufFacultad = nufFacultad;
	}

	public List<Facultad> getNufListFacultad() {
		nufListFacultad = nufListFacultad==null?(new ArrayList<Facultad>()):nufListFacultad;
		return nufListFacultad;
	}

	public void setNufListFacultad(List<Facultad> nufListFacultad) {
		this.nufListFacultad = nufListFacultad;
	}

	public Carrera getNufCarrera() {
		return nufCarrera;
	}

	public void setNufCarrera(Carrera nufCarrera) {
		this.nufCarrera = nufCarrera;
	}

	public List<Carrera> getNufListCarrera() {
		nufListCarrera = nufListCarrera==null?(new ArrayList<Carrera>()):nufListCarrera;
		return nufListCarrera;
	}

	public void setNufListCarrera(List<Carrera> nufListCarrera) {
		this.nufListCarrera = nufListCarrera;
	}

	public UsuarioRolJdbcDto getNufUsuarioRolJdbcDtoBuscar() {
		return nufUsuarioRolJdbcDtoBuscar;
	}

	public void setNufUsuarioRolJdbcDtoBuscar(
			UsuarioRolJdbcDto nufUsuarioRolJdbcDtoBuscar) {
		this.nufUsuarioRolJdbcDtoBuscar = nufUsuarioRolJdbcDtoBuscar;
	}

	public List<UsuarioRolJdbcDto> getNufListaufUsuarioRolJdbcDtoBuscar() {
		nufListaufUsuarioRolJdbcDtoBuscar = nufListaufUsuarioRolJdbcDtoBuscar==null?(new ArrayList<UsuarioRolJdbcDto>()):nufListaufUsuarioRolJdbcDtoBuscar;
		return nufListaufUsuarioRolJdbcDtoBuscar;
	}

	public void setNufListaufUsuarioRolJdbcDtoBuscar(
			List<UsuarioRolJdbcDto> nufListaufUsuarioRolJdbcDtoBuscar) {
		this.nufListaufUsuarioRolJdbcDtoBuscar = nufListaufUsuarioRolJdbcDtoBuscar;
	}

	public boolean isNufHabilitadorCarrera() {
		return nufHabilitadorCarrera;
	}

	public void setNufHabilitadorCarrera(boolean nufHabilitadorCarrera) {
		this.nufHabilitadorCarrera = nufHabilitadorCarrera;
	}

	public ConvocatoriaServicio getServConvocatoriaServicio() {
		return servConvocatoriaServicio;
	}

	public void setServConvocatoriaServicio(
			ConvocatoriaServicio servConvocatoriaServicio) {
		this.servConvocatoriaServicio = servConvocatoriaServicio;
	}

	public UsuarioRolServicio getServUsuarioRolServicio() {
		return servUsuarioRolServicio;
	}

	public void setServUsuarioRolServicio(UsuarioRolServicio servUsuarioRolServicio) {
		this.servUsuarioRolServicio = servUsuarioRolServicio;
	}

	public FacultadServicio getServNufFacultadServicio() {
		return servNufFacultadServicio;
	}

	public void setServNufFacultadServicio(FacultadServicio servNufFacultadServicio) {
		this.servNufFacultadServicio = servNufFacultadServicio;
	}

	public CarreraServicio getServNufCarreraServicio() {
		return servNufCarreraServicio;
	}

	public void setServNufCarreraServicio(CarreraServicio servNufCarreraServicio) {
		this.servNufCarreraServicio = servNufCarreraServicio;
	}

	public UsuarioRolDtoServicioJdbc getServNufUsuarioRolDtoServicioJdbc() {
		return servNufUsuarioRolDtoServicioJdbc;
	}

	public void setServNufUsuarioRolDtoServicioJdbc(
			UsuarioRolDtoServicioJdbc servNufUsuarioRolDtoServicioJdbc) {
		this.servNufUsuarioRolDtoServicioJdbc = servNufUsuarioRolDtoServicioJdbc;
	}

	public UsuarioCreacionDto getNufUsuarioCreacionDtoCrear() {
		return nufUsuarioCreacionDtoCrear;
	}

	public void setNufUsuarioCreacionDtoCrear(
			UsuarioCreacionDto nufUsuarioCreacionDtoCrear) {
		this.nufUsuarioCreacionDtoCrear = nufUsuarioCreacionDtoCrear;
	}

	public UsuarioRolJdbcDto getNufUsuarioRolJdbcDtoCrear() {
		return nufUsuarioRolJdbcDtoCrear;
	}

	public void setNufUsuarioRolJdbcDtoCrear(
			UsuarioRolJdbcDto nufUsuarioRolJdbcDtoCrear) {
		this.nufUsuarioRolJdbcDtoCrear = nufUsuarioRolJdbcDtoCrear;
	}

	public Rol getNufRol() {
		return nufRol;
	}

	public void setNufRol(Rol nufRol) {
		this.nufRol = nufRol;
	}

	public List<Rol> getNufListRol() {
		nufListRol = nufListRol==null?(new ArrayList<Rol>()):nufListRol;
		return nufListRol;
	}

	public void setNufListRol(List<Rol> nufListRol) {
		this.nufListRol = nufListRol;
	}

	public List<Etnia> getNufListEtnias() {
		return nufListEtnias;
	}

	public void setNufListEtnias(List<Etnia> nufListEtnias) {
		this.nufListEtnias = nufListEtnias;
	}

	public List<UbicacionDto> getNufListPaises() {
		return nufListPaises;
	}

	public void setNufListPaises(List<UbicacionDto> nufListPaises) {
		this.nufListPaises = nufListPaises;
	}

	public boolean isNufHabilitadorRol() {
		return nufHabilitadorRol;
	}

	public void setNufHabilitadorRol(boolean nufHabilitadorRol) {
		this.nufHabilitadorRol = nufHabilitadorRol;
	}

	public boolean isNufHabilitadorGuardar() {
		return nufHabilitadorGuardar;
	}

	public void setNufHabilitadorGuardar(boolean nufHabilitadorGuardar) {
		this.nufHabilitadorGuardar = nufHabilitadorGuardar;
	}


	public boolean isNufHabilitadorFacultad() {
		return nufHabilitadorFacultad;
	}


	public void setNufHabilitadorFacultad(boolean nufHabilitadorFacultad) {
		this.nufHabilitadorFacultad = nufHabilitadorFacultad;
	}
	
	
	
	
	
}
