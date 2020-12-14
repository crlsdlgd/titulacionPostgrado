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
   
 ARCHIVO:     ImprimirActaGradoSecretariaForm.java	  
 DESCRIPCION: Managed Bean que maneja las peticiones del rol Secretaria para imprimir el acta de grado.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 14-DICIEMBRE-2016		Daniel Albuja                         Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.managedBeans.session.validacion;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.AutoridadDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.CarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.ConfiguracionCarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteEmisionActaJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.InstitucionAcademicaDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.MecanismoTitulacionCarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.PersonaDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.TituloDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.UbicacionDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.AsentamientoNotaException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.AsentamientoNotaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.AutoridadException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.AutoridadNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.CarreraDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ConvocatoriaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EtniaException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EtniaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaDcnAsgTitulacionException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaDcnAsgTitulacionNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaEstudianteException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaEstudianteNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.InstitucionAcademicaException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.InstitucionAcademicaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.TituloException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.TituloNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UbicacionDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UbicacionDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.ConfiguracionCarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.ConvocatoriaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.EstudianteValidacionDtoServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.EtniaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.FacultadServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.FichaEstudianteServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.InstitucionAcademicaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.TituloServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.TramiteTituloServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.UbicacionServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.AsentamientoNotaDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.AutoridadDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.ConfiguracionCarreraDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.EstudianteActaGradoDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.EstudianteValidacionDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.InstitucionAcademicaDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.MecanismoTitulacionCarreraDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.PersonaDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.ReportesDtoJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.RolDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.TituloDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.UbicacionDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.AutoridadConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.MecanismoTitulacionCarreraConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.PersonaConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.RegimenAcademicoConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.TramiteTituloConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.AsentamientoNota;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Convocatoria;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Etnia;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.InstitucionAcademica;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Persona;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Titulo;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.TramiteTitulo;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Usuario;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.titulacionPosgrado.jsf.utilidades.FacesUtil;


/**
 * Clase (managed bean) ImprimirActaGradoSecretariaForm.
 * Managed Bean que maneja las peticiones del rol Secretaria para imprimir el acta de grado.
 * @author dalbuja.
 * @version 1.0
 */

@ManagedBean(name="imprimirActaGradoSecretariaForm")
@SessionScoped
public class ImprimirActaGradoSecretariaForm implements Serializable{

	private static final long serialVersionUID = 8026853400702897855L;
		
	// *****************************************************************/
	// ************** Variables de ActaGradoSecretariaForm**************/
	// *****************************************************************/
	
	//COMPONENTES DE BUSQUEDA
	private Convocatoria iagfConvocatoria;
	private List<Convocatoria> iagfListConvocatorias;
	private CarreraDto iagfCarrera;
	private List<CarreraDto> iagfListCarreras;
	private EstudianteEmisionActaJdbcDto iagfEstudianteActaGradoDtoServicioJdbcDtoBuscar;
	private List<EstudianteEmisionActaJdbcDto> iagfListEstudiantesActasGrado;
	private UbicacionDto agfnacionalidad;
	private Etnia agfEtnia;
	private Titulo agfTitulo;
	private InstitucionAcademica agfInstitucionAcademica;
	private String agfTipoReconocimiento;
	private Usuario agfUsuarioQueGenera;
	private Integer agftipoImpresion;
	
	//COMPONENTES DE EDITAR
	private EstudianteEmisionActaJdbcDto agfEstudianteActaGradoJdbcDtoEditar;
	private String fechaNacimientoLabel;
	private List<TituloDto> agfListTitulosBachilleres;
	private List<InstitucionAcademicaDto> agfListUniversidades;
	private Boolean iagfEstadoComplexivo;
	private Usuario iagfUsuarioQueCambia;
	private Persona agfPersonaPostulante;
	private ConfiguracionCarreraDto agfConfiguracionCarrera;
	private List<ConfiguracionCarreraDto> agfListConfiguracionesCarrera;
	private int habilitarCampos;
	private Integer iagfValidadorClic;
	private Boolean iagfDeshabilitadoExportar;
	private StringBuilder presidenteTribunal;
	private StringBuilder miembro1Tribunal;
	private StringBuilder miembro2Tribunal;
	private BigDecimal calculoFinal;
	private String iagfCadenaServlet;
	private Integer iagfCarreraImprimir;
	private Boolean iagfTipoCarrera;
	// ******************************************************************/
	// ******************* Servicios Generales **************************/
	// ******************************************************************/
	@EJB 
	private ConvocatoriaServicio srvConvocatoriaServicio;
		
	@EJB 
	private ReportesDtoJdbc srvReportesDtoJdbc;
		
	@EJB 
	private UsuarioRolServicio srvUsuarioRolServicio;

	@EJB 
	private FacultadServicio srvFacultadServicio;
		
	@EJB
	private CarreraServicio srvCarreraServicio;
	
	@EJB 
	private RolFlujoCarreraServicio srvRolFlujoCarreraServicio;
		
	@EJB 
	private EstudianteValidacionDtoServicioJdbc servAgfEstudianteValidacionDtoServicioJdbc;
	
	@EJB 
	private EstudianteValidacionDtoServicio servAgfEstudianteValidacionDtoServicio;
	
	@EJB 
	private EstudianteActaGradoDtoServicioJdbc servAgfEstudianteActaGradoDtoServicioJdbc;
	
	@EJB 
	private TituloDtoServicioJdbc servAgfTituloDtoJdbc;
	
	@EJB 
	private InstitucionAcademicaDtoServicioJdbc servAgfIntitucionAcademicaDtoJdbc;
	
	@EJB 
	private ConfiguracionCarreraDtoServicioJdbc servAgfConfiguracionCarreraDtoJdbc;
	
	@EJB 
	private EtniaServicio servAgfEtniaServicio;
	
	@EJB 
	private UbicacionDtoServicioJdbc servAgfUbicacionDtoServicioJdbc;
	
	@EJB 
	private ConfiguracionCarreraServicio servAgfConfiguracionCarreraServicio;
	
	@EJB 
	private UbicacionServicio servAgfUbicacionServicio;
	
	@EJB 
	private TituloServicio servAgfTituloServicio;
	
	@EJB 
	private InstitucionAcademicaServicio servAgfInstitucionAcademicaServicio;
	
	@EJB 
	private AsentamientoNotaDtoServicioJdbc servAsentamientoNotaDtoServicioJdbc;
	
	@EJB 
	private PersonaDtoServicioJdbc servPersonaDtoServicioJdbc;
	
	@EJB 
	private AutoridadDtoServicioJdbc servAutoridadDtoServicioJdbc;
	
	@EJB
	private EstudianteActaGradoDtoServicioJdbc servEstudianteActaGradoDtoServicioJdbc;
	
	@EJB 
	private FichaEstudianteServicio servFichaEstudianteServicio;
	@EJB 
	private TramiteTituloServicio servTramiteTituloServicio;
	@EJB private RolDtoServicioJdbc servRolDtoServicioJdbc;
	@EJB private RolFlujoCarreraServicio servRolFlujoCarreraServicio;
	@EJB private  InstitucionAcademicaServicio servInstitucionAcademicaServicio;
	@EJB MecanismoTitulacionCarreraDtoServicioJdbc servMecanismoTitulacionCarreraDtoServicioJdbc;
	@EJB PersonaServicio srvPersonaServivio;
	@EJB private CarreraDtoServicioJdbc srvCarreraDtoServicioJdbc;
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
	 * Método que dirige a la página de Acta Grado
	 * @param usuario
	 * @return
	 */
	public String irImprimirActaGrado(Usuario usuario){
		this.agfUsuarioQueGenera=usuario;
		agftipoImpresion=0;
	//vericacion de que el usuario no pertenezca al active directory 0.-si 1.-no
		iagfDeshabilitadoExportar=true;
		try {
			UsuarioRol usro = srvUsuarioRolServicio.buscarPorIdentificacion(usuario.getUsrIdentificacion());
			if(usro.getUsroEstado().intValue()==UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Autenticacion.usuario.desactivado.estado.usuario.rol")));
				return null;
			}
			iniciarParametros();
			iagfListConvocatorias =  srvConvocatoriaServicio.listarTodos();
			iagfListCarreras=servAgfEstudianteValidacionDtoServicioJdbc.buscarCarrerasXSecretaria(usuario.getUsrIdentificacion());
			if(iagfListCarreras.size()==0){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Autenticacion.usuario.desactivado.rol.flujo.carrera")));
				return null;
			}
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (SQLException e) {
		} catch (ConvocatoriaNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("No se encontró ninguna convocatoria ");
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (UsuarioRolException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}
		iagfUsuarioQueCambia = usuario;
//		RolDto rolBuscar = new RolDto();
//		try {
//			rolBuscar= servRolDtoServicioJdbc.buscarRolXCedula(usuario.getUsrIdentificacion());
//		} catch (RolException | RolNoEncontradoException e) {
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeInfo("Rol de usuario no encontrado");
//			return null;
//		}
//		boolean op=false;
//		List<RolFlujoCarrera> rolFlujoCarreraAux;
//		try {
//			rolFlujoCarreraAux = servRolFlujoCarreraServicio.buscarPorIdUsuario(usuario);
//			for (RolFlujoCarrera rolFlujoCarrera : rolFlujoCarreraAux) {
//				if(rolFlujoCarrera.getRoflcrCarrera().getCrrFacultad().getFclId()==FacultadConstantes.FACULTAD_INGENIERIA_CFM_VALUE){
//					op=true;
//				}
//			}
//		} catch (RolFlujoCarreraNoEncontradoException
//				| RolFlujoCarreraException e) {
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeInfo("No se encontraron carreras asignadas al usuario");
//			return null;
//		}
//		if(rolBuscar.getRolId()==RolConstantes.ROL_BD_EDITOR_ACTA_VALUE || !op){
			return "irImprimirActaGrado";	
//		}else{
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeInfo("No puede acceder a este menú, esta tarea la ejecuta otro usuario");
//			return null;
//		}
	}
	
	/**
	 * Método que dirige a la página de Acta Grado para su reimpresión
	 * @param usuario
	 * @return
	 */
	public String irReImprimirActaGrado(Usuario usuario){
		agftipoImpresion=1;
		this.agfUsuarioQueGenera=usuario;
	//vericacion de que el usuario no pertenezca al active directory 0.-si 1.-no
		iagfDeshabilitadoExportar=true;
		try {
			UsuarioRol usro = srvUsuarioRolServicio.buscarPorIdentificacion(usuario.getUsrIdentificacion());
			if(usro.getUsroEstado().intValue()==UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Autenticacion.usuario.desactivado.estado.usuario.rol")));
				return null;
			}
			iniciarParametros();
			iagfListConvocatorias =  srvConvocatoriaServicio.listarTodos();
			iagfListCarreras=servAgfEstudianteValidacionDtoServicioJdbc.buscarCarrerasXSecretaria(usuario.getUsrIdentificacion());
			if(iagfListCarreras.size()==0){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Autenticacion.usuario.desactivado.rol.flujo.carrera")));
				return null;
			}
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
		} catch (SQLException e) {
		} catch (ConvocatoriaNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo("No se encontró ninguna convocatoria ");
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (UsuarioRolException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}
		iagfUsuarioQueCambia = usuario;
//		RolDto rolBuscar = new RolDto();
//		try {
//			rolBuscar= servRolDtoServicioJdbc.buscarRolXCedula(usuario.getUsrIdentificacion());
//		} catch (RolException | RolNoEncontradoException e) {
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeInfo("Rol de usuario no encontrado");
//			return null;
//		}
//		boolean op=false;
//		List<RolFlujoCarrera> rolFlujoCarreraAux;
//		try {
//			rolFlujoCarreraAux = servRolFlujoCarreraServicio.buscarPorIdUsuario(usuario);
//			for (RolFlujoCarrera rolFlujoCarrera : rolFlujoCarreraAux) {
//				if(rolFlujoCarrera.getRoflcrCarrera().getCrrFacultad().getFclId()==FacultadConstantes.FACULTAD_INGENIERIA_CFM_VALUE){
//					op=true;
//				}
//			}
//		} catch (RolFlujoCarreraNoEncontradoException
//				| RolFlujoCarreraException e) {
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeInfo("No se encontraron carreras asignadas al usuario");
//			return null;
//		}
//		if(rolBuscar.getRolId()==RolConstantes.ROL_BD_EDITOR_ACTA_VALUE || !op){
			return "irReImprimirActaGrado";	
//		}else{
//			FacesUtil.limpiarMensaje();
//			FacesUtil.mensajeInfo("No puede acceder a este menú, esta tarea la ejecuta otro usuario");
//			return null;
//		}
	}
	
	
	/**
	 * Maneja de cancelacion de la validación
	 * @return - cadena de navegacion a la pagina de inicio
	 */
	public String cancelar(){
		iagfCarrera =  null;
		iagfConvocatoria = null;
		limpiar();
		return "irInicio";
	}

	/**
	 * Maneja de cancelacion de la validación
	 * @return - cadena de navegacion a la pagina de inicio
	 */
	public String irGenerarActa(){
		iagfCarrera =  null;
		iagfConvocatoria = null;
		RolFlujoCarrera roflcrr;
		try {
			TramiteTitulo trttAux =servTramiteTituloServicio.buscarPorId(agfEstudianteActaGradoJdbcDtoEditar.getTrttId());
			if(trttAux.getTrttEstadoProceso()==TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_DEFENSA_VALUE){
				agfEstudianteActaGradoJdbcDtoEditar.setTrttId(trttAux.getTrttId());
				roflcrr = srvRolFlujoCarreraServicio.buscarPorCarrera(trttAux.getTrttCarreraId(),agfUsuarioQueGenera.getUsrId(), RolConstantes.ROL_BD_VALIDADOR_VALUE);
				agfEstudianteActaGradoJdbcDtoEditar.setPrtrFechaEjecucion(new Timestamp((new Date()).getTime()));
				servEstudianteActaGradoDtoServicioJdbc.modificarEstadoActaGradoFinal(agfEstudianteActaGradoJdbcDtoEditar, roflcrr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		limpiar();
		return "irInicio";

	}

	/** Método que limpia la lista de carreras
	 * 
	 */
	public void cambiarCarrera(){
		iagfListEstudiantesActasGrado=null;
	}
	
	/** Método que limpia los componentes del form
	 * 
	 */
	public void limpiar(){
		iagfListEstudiantesActasGrado=null;
		iagfEstudianteActaGradoDtoServicioJdbcDtoBuscar = null;
		iniciarParametros();
	}
	
	/**
	 * Lista los postulantes graduados segun los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscarGraduados(Usuario usuario){
		try {
			iagfListEstudiantesActasGrado = servAsentamientoNotaDtoServicioJdbc.buscarPostulantesImprimirActa(iagfEstudianteActaGradoDtoServicioJdbcDtoBuscar.getPrsIdentificacion(), iagfConvocatoria.getCnvId(),iagfCarrera.getCrrId() ,iagfUsuarioQueCambia.getUsrIdentificacion() );
			iagfCadenaServlet="ACTACOMPLEXIVO";
			if(!(iagfListEstudiantesActasGrado.size()>0)){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ImprimirActaGradoSecretaria.listar.graduados.facultad.carrera.identificacion.estado.no.resultados")));
			}
		}  catch (AsentamientoNotaNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ImprimirActaGradoSecretaria.listar.graduados.facultad.carrera.identificacion.estado.no.encontrado.exception")));
		} catch (AsentamientoNotaException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ImprimirActaGradoSecretaria.listar.graduados.facultad.carrera.identificacion.estado.no.encontrado.exception")));
		} catch (FichaDcnAsgTitulacionNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ImprimirActaGradoSecretaria.listar.graduados.facultad.carrera.identificacion.estado.no.encontrado.exception")));
		} catch (FichaDcnAsgTitulacionException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ImprimirActaGradoSecretaria.listar.graduados.facultad.carrera.identificacion.estado.no.encontrado.exception")));
		}
	}
	
	/**
	 * Lista los postulantes graduados segun los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscarGraduadosReimprimir(Usuario usuario){
		try {
			iagfListEstudiantesActasGrado = servAsentamientoNotaDtoServicioJdbc.buscarPostulantesReImprimirActa(iagfEstudianteActaGradoDtoServicioJdbcDtoBuscar.getPrsIdentificacion(), iagfConvocatoria.getCnvId(),iagfCarrera.getCrrId() ,iagfUsuarioQueCambia.getUsrIdentificacion() );
			if(!(iagfListEstudiantesActasGrado.size()>0)){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ImprimirActaGradoSecretaria.listar.graduados.facultad.carrera.identificacion.estado.no.resultados")));
			}
		}  catch (AsentamientoNotaNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ImprimirActaGradoSecretaria.listar.graduados.facultad.carrera.identificacion.estado.no.encontrado.exception")));
		} catch (AsentamientoNotaException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ImprimirActaGradoSecretaria.listar.graduados.facultad.carrera.identificacion.estado.no.encontrado.exception")));
		} catch (FichaDcnAsgTitulacionNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ImprimirActaGradoSecretaria.listar.graduados.facultad.carrera.identificacion.estado.no.encontrado.exception")));
		} catch (FichaDcnAsgTitulacionException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ImprimirActaGradoSecretaria.listar.graduados.facultad.carrera.identificacion.estado.no.encontrado.exception")));
		}
	}
	
	public String imprimirActaGrado(EstudianteEmisionActaJdbcDto estudiante) {
		iagfTipoCarrera=false;
		this.agfEstudianteActaGradoJdbcDtoEditar =estudiante;
			iagfValidadorClic = 0;
			try {
				agfEtnia = servAgfEtniaServicio.buscarPorId(agfEstudianteActaGradoJdbcDtoEditar.getPrsEtnId());
				agfnacionalidad = servAgfUbicacionDtoServicioJdbc.buscarXId(agfEstudianteActaGradoJdbcDtoEditar.getPrsUbcId());
				agfTitulo = servAgfTituloServicio.buscarPorId(agfEstudianteActaGradoJdbcDtoEditar.getFcesTituloBachillerId());
				try {
					if(agfEstudianteActaGradoJdbcDtoEditar.getFcesInacEstPrevios()==0){
						agfEstudianteActaGradoJdbcDtoEditar.setFcesInacEstPrevios(null);
					}else{
						agfInstitucionAcademica =  servAgfInstitucionAcademicaServicio.buscarPorId(agfEstudianteActaGradoJdbcDtoEditar.getFcesInacEstPrevios());
					}
				} catch (Exception e) {
				}
				if(agfEstudianteActaGradoJdbcDtoEditar.getFcesTipoDuracionRec()==0){
					agfEstudianteActaGradoJdbcDtoEditar.setFcesTipoDuracionRec(GeneralesConstantes.APP_ID_BASE);
				}
				iagfCarreraImprimir=agfEstudianteActaGradoJdbcDtoEditar.getCrrId();
			} catch (EtniaNoEncontradoException e1) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e1.getMessage());
			} catch (EtniaException e1) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e1.getMessage());
			} catch (UbicacionDtoJdbcException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			} catch (UbicacionDtoJdbcNoEncontradoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			} catch (TituloNoEncontradoException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			} catch (TituloException e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(e.getMessage());
			} 
				if(agfEstudianteActaGradoJdbcDtoEditar.getTrttEstadoProceso().intValue()==TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_VALUE
						|| agfEstudianteActaGradoJdbcDtoEditar.getTrttEstadoProceso().intValue()==TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_VALUE
						|| agfEstudianteActaGradoJdbcDtoEditar.getTrttEstadoProceso().intValue()==TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_VALUE){
					iagfEstadoComplexivo=true;
				}else if (agfEstudianteActaGradoJdbcDtoEditar.getTrttEstadoProceso().intValue()==TramiteTituloConstantes.ESTADO_PROCESO_EMITIDO_ACTA_DE_GRADO_DEFENSA_VALUE
						|| agfEstudianteActaGradoJdbcDtoEditar.getTrttEstadoProceso().intValue()==TramiteTituloConstantes.ESTADO_PROCESO_ACTA_IMPRESA_DEFENSA_ORAL_VALUE
						|| agfEstudianteActaGradoJdbcDtoEditar.getTrttEstadoProceso().intValue()==TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_VALUE){
					iagfEstadoComplexivo=false;
				}
					try {
						agfEstudianteActaGradoJdbcDtoEditar=servAsentamientoNotaDtoServicioJdbc.buscarGraduadosOtrosMecanismos(agfEstudianteActaGradoJdbcDtoEditar.getPrsIdentificacion(), agfEstudianteActaGradoJdbcDtoEditar.getCnvId(), agfEstudianteActaGradoJdbcDtoEditar.getCrrId());
						try {
							if(agfEstudianteActaGradoJdbcDtoEditar.getFcesInacEstPrevios()==0){
								agfEstudianteActaGradoJdbcDtoEditar.setFcesInacEstPrevios(null);
							}else{
								agfInstitucionAcademica =  servAgfInstitucionAcademicaServicio.buscarPorId(agfEstudianteActaGradoJdbcDtoEditar.getFcesInacEstPrevios());
							}
							if(agfEstudianteActaGradoJdbcDtoEditar.getFcesTipoDuracionRec()==0){
								agfEstudianteActaGradoJdbcDtoEditar.setFcesTipoDuracionRec(GeneralesConstantes.APP_ID_BASE);
							}
						} catch (Exception e) {
						}
						presidenteTribunal=new StringBuilder();
						presidenteTribunal.append(agfEstudianteActaGradoJdbcDtoEditar.getListaDocentesTribunal().get(0).getPrsNombres());
						presidenteTribunal.append(" ");
						presidenteTribunal.append(agfEstudianteActaGradoJdbcDtoEditar.getListaDocentesTribunal().get(0).getPrsPrimerApellido());
						try {
							if(agfEstudianteActaGradoJdbcDtoEditar.getListaDocentesTribunal().get(0).getPrsSegundoApellido()!=null){
								presidenteTribunal.append(" ");
								presidenteTribunal.append(agfEstudianteActaGradoJdbcDtoEditar.getListaDocentesTribunal().get(0).getPrsSegundoApellido());
							}
						} catch (Exception e) {
						}
						miembro1Tribunal=new StringBuilder();
						miembro1Tribunal.append(agfEstudianteActaGradoJdbcDtoEditar.getListaDocentesTribunal().get(1).getPrsNombres());
						miembro1Tribunal.append(" ");
						miembro1Tribunal.append(agfEstudianteActaGradoJdbcDtoEditar.getListaDocentesTribunal().get(1).getPrsPrimerApellido());
						try {
							if(agfEstudianteActaGradoJdbcDtoEditar.getListaDocentesTribunal().get(1).getPrsSegundoApellido()!=null){
								miembro1Tribunal.append(" ");
								miembro1Tribunal.append(agfEstudianteActaGradoJdbcDtoEditar.getListaDocentesTribunal().get(1).getPrsSegundoApellido());
							}
						} catch (Exception e) {
						}
						try {
								miembro2Tribunal=new StringBuilder();
								miembro2Tribunal.append(agfEstudianteActaGradoJdbcDtoEditar.getListaDocentesTribunal().get(2).getPrsNombres());
								miembro2Tribunal.append(" ");
								miembro2Tribunal.append(agfEstudianteActaGradoJdbcDtoEditar.getListaDocentesTribunal().get(2).getPrsPrimerApellido());
								try {
									if(agfEstudianteActaGradoJdbcDtoEditar.getListaDocentesTribunal().get(2).getPrsSegundoApellido()!=null){
										miembro2Tribunal.append(" ");
										miembro2Tribunal.append(agfEstudianteActaGradoJdbcDtoEditar.getListaDocentesTribunal().get(2).getPrsSegundoApellido());
									}
								} catch (Exception e) {
								}	
//							
						} catch (Exception e) {
						}
						CarreraDto crr = new CarreraDto();
						try {
							crr = srvCarreraDtoServicioJdbc.buscarXId(iagfCarreraImprimir);
						} catch (CarreraDtoJdbcException | CarreraDtoJdbcNoEncontradoException e) {
						}
						
						calculoFinal = BigDecimal.ZERO;
						BigDecimal componente1 = BigDecimal.ZERO;
						BigDecimal componente2 = BigDecimal.ZERO;
						BigDecimal trabajoEscrito = BigDecimal.ZERO;
						trabajoEscrito = servAsentamientoNotaDtoServicioJdbc.buscarNotaTrabajoEscrito(agfEstudianteActaGradoJdbcDtoEditar.getTrttId());
						agfEstudianteActaGradoJdbcDtoEditar.setAsnoPrmTrbEscrito(trabajoEscrito.floatValue());
						BigDecimal trabajoOral = BigDecimal.ZERO;
						trabajoOral = servAsentamientoNotaDtoServicioJdbc.buscarNotaTrabajoOral(agfEstudianteActaGradoJdbcDtoEditar.getTrttId());
						
						
						agfEstudianteActaGradoJdbcDtoEditar.setAsnoPrmDfnOral(trabajoOral.floatValue());
						AsentamientoNota asno = new AsentamientoNota();
						asno = servAsentamientoNotaDtoServicioJdbc.buscarNotasOral(agfEstudianteActaGradoJdbcDtoEditar.getTrttId());
						try {
							agfEstudianteActaGradoJdbcDtoEditar.setAsnoDfnOral1(asno.getAsnoDfnOral1().floatValue());	
						} catch (Exception e) {
							agfEstudianteActaGradoJdbcDtoEditar.setAsnoDfnOral1(null);
						}
						
						agfEstudianteActaGradoJdbcDtoEditar.setAsnoDfnOral2(asno.getAsnoDfnOral2().floatValue());
						try {
							agfEstudianteActaGradoJdbcDtoEditar.setAsnoDfnOral3(asno.getAsnoDfnOral3().floatValue());
						} catch (Exception e) {
							agfEstudianteActaGradoJdbcDtoEditar.setAsnoDfnOral3(null);
						}
						
						
						if(crr.getCrrTipoEvaluacion()==CarreraConstantes.CARRERA_EVALUACION_DEFENSA_ORAL_VALUE){
							componente1 = agfEstudianteActaGradoJdbcDtoEditar.getFcesNotaPromAcumulado().multiply(new BigDecimal(60)).divide(new BigDecimal(100), 2,RoundingMode.DOWN);
							componente2 = agfEstudianteActaGradoJdbcDtoEditar.getFcesNotaTrabTitulacion().multiply(new BigDecimal(40)).divide(new BigDecimal(100), 2,RoundingMode.DOWN);
							calculoFinal=componente1.add(componente2.setScale(2, RoundingMode.DOWN));
							generarActaOtrosMecanismos(agfEstudianteActaGradoJdbcDtoEditar);
							return "irImprimirActaGradoOtrosMecanismos";
						}else if(crr.getCrrTipoEvaluacion()==CarreraConstantes.CARRERA_EVALUACION_DEFENSA_ESCRITO_ORAL_VALUE){
							agfEstudianteActaGradoJdbcDtoEditar.setAsnoPrmTrbEscrito(new BigDecimal(agfEstudianteActaGradoJdbcDtoEditar.getAsnoPrmTrbEscrito()).setScale(2, RoundingMode.HALF_UP).floatValue());	
							componente1 = agfEstudianteActaGradoJdbcDtoEditar.getFcesNotaPromAcumulado();
							componente2 = agfEstudianteActaGradoJdbcDtoEditar.getFcesNotaTrabTitulacion();
							calculoFinal=(componente1.add(componente2)).divide(new BigDecimal(2),2,RoundingMode.DOWN);
							generarActaOtrosMecanismos3Comp(agfEstudianteActaGradoJdbcDtoEditar);
							return "irImprimirActaGradoOtrosMecanismosTresComponentes";
						}else {
							agfEstudianteActaGradoJdbcDtoEditar.setAsnoPrmTrbEscrito(new BigDecimal(agfEstudianteActaGradoJdbcDtoEditar.getAsnoPrmTrbEscrito()).setScale(2, RoundingMode.HALF_UP).floatValue());	
							componente1 = agfEstudianteActaGradoJdbcDtoEditar.getFcesNotaPromAcumulado();
							componente2 = new BigDecimal(agfEstudianteActaGradoJdbcDtoEditar.getAsnoPrmDfnOral()).setScale(2,RoundingMode.DOWN);
							BigDecimal componente3 = new BigDecimal(agfEstudianteActaGradoJdbcDtoEditar.getAsnoPrmTrbEscrito()).setScale(2,RoundingMode.DOWN);
							calculoFinal=(componente1.add(componente2).add(componente3)).divide(new BigDecimal(3),2,RoundingMode.HALF_UP);
							calculoFinal=(componente1.add(componente2)).divide(new BigDecimal(2),2,RoundingMode.DOWN);
							generarActaOtrosMecanismosCalculo3(agfEstudianteActaGradoJdbcDtoEditar);
							return "irImprimirActaGradoOtrosMecanismosTresComponentes";
						}
						
						
					} catch (AsentamientoNotaNoEncontradoException
							| AsentamientoNotaException
							| FichaDcnAsgTitulacionNoEncontradoException
							| FichaDcnAsgTitulacionException e) {
						return null;
					} 
					
	}

	
	public String traerDescripcionInac(){
		String retorno= " ";
		try {
			InstitucionAcademica aux = servInstitucionAcademicaServicio.buscarPorId(agfEstudianteActaGradoJdbcDtoEditar.getFcesInacEstPrevios());
			retorno= aux.getInacDescripcion();
		} catch (InstitucionAcademicaNoEncontradoException e) {
		} catch (InstitucionAcademicaException e) {
		}catch (Exception e) {
		}
		return retorno;
	}
	/**
	* Genera el acta para los graduados por examen complexivo y lo establece en la sesión como atributo
	* @param List<RegistradosDto> frmRfRegistradosDto
	* @return String
	*/
	public String generarActa(EstudianteEmisionActaJdbcDto entidad){
		 List<Map<String, Object>> frmCrpCampos = null;
		 Map<String, Object> frmCrpParametros = null;
		 String frmCrpNombreReporte = null;
		// ****************************************************************//
		// ***************** GENERACION DEL ACTA DE GRADO  ****************//
		// ****************************************************************//
		// ****************************************************************//
		frmCrpParametros = new HashMap<String, Object>();
		frmCrpParametros.put("usuario",agfUsuarioQueGenera.getUsrNick());
		
		StringBuilder sb = new StringBuilder();
		sb.append("En tal virtud, la <style isBold=\"true\">");
		
		switch (entidad.getFclId()) {
		case 18:
			frmCrpParametros.put("tipoSede", "Sede:");
			frmCrpNombreReporte = "ActaDeGradoComplexivoSedes"+"_"+entidad.getPrsPrimerApellido()+"_"+entidad.getPrsSegundoApellido()+"_"+entidad.getPrsNombres();
			frmCrpParametros.put("facultad",entidad.getFclDescripcion());
			sb.append(entidad.getFclDescripcion());
			break;
		case 19:
			frmCrpParametros.put("tipoSede", "Sede:");
			frmCrpNombreReporte = "ActaDeGradoComplexivoSedes"+"_"+entidad.getPrsPrimerApellido()+"_"+entidad.getPrsSegundoApellido()+"_"+entidad.getPrsNombres();
			frmCrpParametros.put("facultad",entidad.getFclDescripcion());
			sb.append(entidad.getFclDescripcion());
			break;
		case 20:
			frmCrpParametros.put("tipoSede", "Sede:");
			frmCrpNombreReporte = "ActaDeGradoComplexivoSedes"+"_"+entidad.getPrsPrimerApellido()+"_"+entidad.getPrsSegundoApellido()+"_"+entidad.getPrsNombres();
			frmCrpParametros.put("facultad",entidad.getFclDescripcion());
			sb.append(entidad.getFclDescripcion());
			break;
		default:
			frmCrpNombreReporte = "ActaDeGradoComplexivoSedes"+"_"+entidad.getPrsPrimerApellido()+"_"+entidad.getPrsSegundoApellido()+"_"+entidad.getPrsNombres();
			frmCrpParametros.put("tipoSede", "Facultad:");
			frmCrpParametros.put("facultad",entidad.getFclDescripcion());
			sb.append("FACULTAD DE ");
			sb.append(entidad.getFclDescripcion());
			break;
		}
		sb.append("</style> de la <style isBold=\"true\">UNIVERSIDAD CENTRAL DEL ECUADOR</style> en su nombre y por autoridad de la ley");
		
		frmCrpParametros.put("facultadCadena",sb.toString());
		SimpleDateFormat formateador = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("ES"));
		String fecha=formateador.format(entidad.getFcesFechaActaGrado());
		frmCrpCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> dato = null;
			dato = new HashMap<String, Object>();
			
			frmCrpParametros.put("carrera", entidad.getCrrDescripcion());
			frmCrpParametros.put("modalidad", entidad.getMdlDescripcion());
			frmCrpParametros.put("folio", entidad.getFcesNumActaGrado());
			StringBuilder pathGeneralReportes = new StringBuilder();
			pathGeneralReportes.append(FacesContext.getCurrentInstance()
					.getExternalContext().getRealPath("/"));
			pathGeneralReportes.append("/titulacion/reportes/archivosJasper/actasGrado");
			frmCrpParametros.put("imagenCabecera", pathGeneralReportes+"/cabeceraActa.png");
			frmCrpParametros.put("imagenPie", pathGeneralReportes+"/pieActa.png");
			
			//texto que requiere construir de acuerdo a los datos de postulante
			StringBuilder texto = new StringBuilder(); 
			texto.append("En ");
//			if(entidad.getUbcDescripcion().equals("QUITO")){
				texto.append(" Quito D.M.,");	
//			}else{
//				texto.append(entidad.getUbcDescripcion());
//			}
			texto.append(" hoy ");texto.append(fecha);
			texto.append(" a las ");texto.append(entidad.getFcesHoraActaGrado().replace("H", ":"));texto.append("h");
			texto.append(", se procedió a receptar el Examen Complexivo, como mecanismo de titulación ");
			if(entidad.getPrsSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
				texto.append( "del señor ");
			}else if(entidad.getPrsSexo()==PersonaConstantes.SEXO_MUJER_VALUE){
				texto.append( "de la señorita ");
			}else{
				texto.append( "de ");
			}
			texto.append(entidad.getPrsNombres()+" "+entidad.getPrsPrimerApellido()+" "+entidad.getPrsSegundoApellido());
			texto.append(" con identificación N.° ");texto.append(entidad.getPrsIdentificacion());
			if(agfnacionalidad.getUbcGentilicio()!=null){
				texto.append(" de nacionalidad ");texto.append(agfnacionalidad.getUbcGentilicio());	texto.append(",");
			}
			texto.append(" de conformidad con el marco legal vigente del Reglamento de Régimen Académico expedido por el Consejo de Educación Superior el ");
			texto.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(RegimenAcademicoConstantes.DIA_REGIMEN_ACADEMICO,false));
			texto.append(" de ");texto.append(GeneralesUtilidades.tranformaNumeroEnMesesMinusculas(RegimenAcademicoConstantes.MES_REGIMEN_ACADEMICO));texto.append(" de ");
			texto.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(RegimenAcademicoConstantes.ANIO_REGIMEN_ACADEMICO,false));
			texto.append(" para la aplicación del examen complexivo, habiendo obtenido las calificaciones que a continuación se detallan:");
			
			frmCrpParametros.put("texto", texto.toString());
			frmCrpParametros.put("fecha_actual", fecha);
			frmCrpParametros.put("postulante", entidad.getPrsNombres()+" "+entidad.getPrsPrimerApellido()+" "+entidad.getPrsSegundoApellido());
			// Nota record Académico 
			StringBuilder recordAcademico = new StringBuilder();
			String nota=entidad.getFcesNotaPromAcumulado().toString();
			String[] partesNotas =  nota.split("\\.");
			recordAcademico.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[0],true));
			recordAcademico.append(" PUNTO ");
			if(Integer.valueOf(partesNotas[1])<10 && Integer.valueOf(partesNotas[1])!=0) recordAcademico.append("CERO ");
			recordAcademico.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[1],true));
			frmCrpParametros.put("recordAcademico",recordAcademico.toString());
			// Nota trabajo titulación
			StringBuilder trabajoTitulacion = new StringBuilder();
			String notaTitulacion=entidad.getFcesNotaTrabTitulacion().toString();
			partesNotas = notaTitulacion.split("\\.");
			trabajoTitulacion.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[0],true));
			trabajoTitulacion.append(" PUNTO ");
			if(Integer.valueOf(partesNotas[1])<10 && Integer.valueOf(partesNotas[1])!=0) trabajoTitulacion.append("CERO ");
			trabajoTitulacion.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[1],true));
			frmCrpParametros.put("trabajoTitulacion",trabajoTitulacion.toString());
			// Nota promedio
			StringBuilder promedioFinal = new StringBuilder();
			// Solo para las carreras de Medicina y Odontología se permite las notas con decimales
			BigDecimal notaFinal = new BigDecimal(0.0);
//			if(entidad.getCrrId()==CarreraConstantes.CARRERA_MEDICINA_VALUE
//					|| entidad.getCrrId()==CarreraConstantes.CARRERA_ODONTOLOGIA_VALUE
//					|| entidad.getCrrId()==CarreraConstantes.CARRERA_OBSTETRICIA_VALUE
//					|| entidad.getCrrId()==CarreraConstantes.CARRERA_ENFERMERIA_VALUE
//					|| entidad.getCrrId()==CarreraConstantes.CARRERA_ENFERMERIA_NO_VIGENTE_TECNICO_VALUE
//					|| entidad.getCrrId()==CarreraConstantes.CARRERA_ENFERMERIA_NO_VIGENTE_TERCER_NIVEL_VALUE){
				notaFinal=(entidad.getFcesNotaPromAcumulado().add(entidad.getFcesNotaTrabTitulacion())).divide(new BigDecimal(2.0), 2,RoundingMode.UP);
				partesNotas = notaFinal.toString().split("\\.");
				promedioFinal.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[0].toString(),true));
				promedioFinal.append(" PUNTO ");
				if(Integer.valueOf(partesNotas[1])<10 && Integer.valueOf(partesNotas[1])!=0) promedioFinal.append("CERO ");
				promedioFinal.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[1].toString(),true));
				frmCrpParametros.put("promedioFinal",promedioFinal.toString());
				frmCrpParametros.put("notaPromedio",notaFinal.toString());

				try {
					servFichaEstudianteServicio.guardarNotaFinalGraduacion(entidad.getFcesId(), notaFinal, entidad.getTrttId());
				} catch (FichaEstudianteNoEncontradoException e1) {
				} catch (FichaEstudianteException e1) {
					FacesUtil.mensajeError(e1.getMessage());
				}
//			}else{
//				notaFinal=(entidad.getFcesNotaPromAcumulado().add(entidad.getFcesNotaTrabTitulacion())).divide(new BigDecimal(2.0), 0, RoundingMode.HALF_UP);
//				promedioFinal.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(notaFinal.toString(),true));
//				promedioFinal.append(" PUNTO CERO");
//				frmCrpParametros.put("promedioFinal",promedioFinal.toString());
//				frmCrpParametros.put("notaPromedio",notaFinal.toString()+".00");
//				try {
//					servFichaEstudianteServicio.guardarNotaFinalGraduacion(entidad.getFcesId(), notaFinal,entidad.getTrttId());
//				} catch (FichaEstudianteNoEncontradoException e1) {
//				} catch (FichaEstudianteException e1) {
//					FacesUtil.mensajeError(e1.getMessage());
//				}
//			}
			frmCrpParametros.put("notaRecord",nota);
			frmCrpParametros.put("notaTrabajo",notaTitulacion);
			int res= notaFinal.compareTo(new BigDecimal(19.50));
			String resultado=null;
			if(res==0 || res==1){
				resultado="EXCELENTE";
			}else {
				int res1= notaFinal.compareTo(new BigDecimal(17.50));
				if(res1==1 || res1==0){
					resultado="MUY BUENO";
				}else{
					int res2= notaFinal.compareTo(new BigDecimal(15.50));
					if(res2==1 || res2==0){
						resultado="BUENO";
					}else {
						int res3= notaFinal.compareTo(new BigDecimal(14.00));
						if(res3==1 || res3==0){
							resultado="REGULAR";
						}else{
							resultado="DEFICIENTE";
						}
					}
				}
			}
			
			
			try {
				List<AutoridadDto> listaAutoridades = new ArrayList<AutoridadDto>();
				listaAutoridades=servAutoridadDtoServicioJdbc.buscarAutoridades(entidad.getCrrId());
				StringBuilder autoridadDecano = new StringBuilder();
				StringBuilder autoridadSecretario = new StringBuilder();
				if(entidad.getFcesDecano().equals(GeneralesConstantes.APP_ID_BASE.toString())){
					PersonaDto director = servPersonaDtoServicioJdbc.buscarEvaluadorXCarrera(entidad.getCrrId());
					autoridadDecano.append(listaAutoridades.get(0).getAtrNombres());autoridadDecano.append(" ");
					autoridadDecano.append(listaAutoridades.get(0).getAtrPrimerApellido());autoridadDecano.append(" ");
					if(!listaAutoridades.get(0).getAtrSegundoApellido().equals(GeneralesConstantes.APP_ID_BASE.toString())){
						autoridadDecano.append(listaAutoridades.get(0).getAtrSegundoApellido());
					}
					autoridadSecretario.append(listaAutoridades.get(2).getAtrNombres());autoridadSecretario.append(" ");
					autoridadSecretario.append(listaAutoridades.get(2).getAtrPrimerApellido());autoridadSecretario.append(" ");
					if(!listaAutoridades.get(2).getAtrSegundoApellido().equals(GeneralesConstantes.APP_ID_BASE.toString())){
						autoridadSecretario.append(listaAutoridades.get(2).getAtrSegundoApellido());
					}
					MecanismoTitulacionCarreraDto mcttcrAux = new MecanismoTitulacionCarreraDto();
					mcttcrAux = servMecanismoTitulacionCarreraDtoServicioJdbc.buscarXCarreraMecanismoExamenComplexivo(entidad.getCrrId());
					switch (entidad.getFclId()) {
					case 18:
						if(director.getPrsSegundoApellido()!=null){
							frmCrpParametros.put("director",director.getPrsNombres()+" "+director.getPrsPrimerApellido()+" "+director.getPrsSegundoApellido());	
						}else{
							frmCrpParametros.put("director",director.getPrsNombres()+" "+director.getPrsPrimerApellido());
						}
						
						frmCrpParametros.put("decano",GeneralesConstantes.APP_NOMBRES_DIRECTOR_DGA);
						frmCrpParametros.put("secretario",autoridadSecretario.toString());
						if(director.getPrsSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
							frmCrpParametros.put("sexoDirector","Director de Carrera");
						}else{
							frmCrpParametros.put("sexoDirector","Directora de Carrera");
						}
							frmCrpParametros.put("sexoDecano","Decana");
							if(listaAutoridades.get(2).getAtrSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
//								frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con el infrascrito Secretario Abogado que certifica. ");
								frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firma la presente acta el infrascrito Secretario Abogado que certifica. ");
								frmCrpParametros.put("sexoSecretario","Secretario Abogado");
							}else{
//								frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con la infrascrita Secretaria Abogada que certifica. ");
								frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firma la presente acta la infrascrita Secretaria Abogada que certifica. ");
								frmCrpParametros.put("sexoSecretario","Secretaria Abogada");
							}
							
							servAgfEstudianteActaGradoDtoServicioJdbc.actualizarAutoridadesComplexivo(entidad.getFcesId(), GeneralesConstantes.APP_NOMBRES_DIRECTOR_DGA, 1, 1,mcttcrAux.getMcttcrPorcentajeComplexivo());
							servAgfEstudianteActaGradoDtoServicioJdbc.actualizarAutoridadesComplexivo(entidad.getFcesId(), autoridadSecretario.toString(), listaAutoridades.get(2).getAtrSexo(), 3,mcttcrAux.getMcttcrPorcentajeComplexivo());
							if(director.getPrsSegundoApellido()!=null){
								servAgfEstudianteActaGradoDtoServicioJdbc.actualizarAutoridadesComplexivo(entidad.getFcesId(), director.getPrsNombres()+" "+director.getPrsPrimerApellido()+" "+director.getPrsSegundoApellido(),  director.getPrsSexo(), 4,mcttcrAux.getMcttcrPorcentajeComplexivo());	
							}else{
								servAgfEstudianteActaGradoDtoServicioJdbc.actualizarAutoridadesComplexivo(entidad.getFcesId(), director.getPrsNombres()+" "+director.getPrsPrimerApellido(),  director.getPrsSexo(), 4,mcttcrAux.getMcttcrPorcentajeComplexivo());
							}
							
						break;
					case 19:
						if(director.getPrsSegundoApellido()!=null){
							frmCrpParametros.put("director",director.getPrsNombres()+" "+director.getPrsPrimerApellido()+" "+director.getPrsSegundoApellido());	
						}else{
							frmCrpParametros.put("director",director.getPrsNombres()+" "+director.getPrsPrimerApellido());
						}
						frmCrpParametros.put("decano",GeneralesConstantes.APP_NOMBRES_DIRECTOR_DGA);
						frmCrpParametros.put("secretario",autoridadSecretario.toString());
						if(director.getPrsSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
							frmCrpParametros.put("sexoDirector","Director de Carrera");
						}else{
							frmCrpParametros.put("sexoDirector","Directora de Carrera");
						}
							frmCrpParametros.put("sexoDecano","Decana");
							if(listaAutoridades.get(2).getAtrSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
//								frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con el infrascrito Secretario Abogado que certifica. ");
								frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firma la presente acta el infrascrito Secretario Abogado que certifica. ");
								frmCrpParametros.put("sexoSecretario","Secretario Abogado");
							}else{
//								frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con la infrascrita Secretaria Abogada que certifica. ");
								frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firma la presente acta la infrascrita Secretaria Abogada que certifica. ");
								frmCrpParametros.put("sexoSecretario","Secretaria Abogada");
							}
							servAgfEstudianteActaGradoDtoServicioJdbc.actualizarAutoridadesComplexivo(entidad.getFcesId(), GeneralesConstantes.APP_NOMBRES_DIRECTOR_DGA, 1, 1,mcttcrAux.getMcttcrPorcentajeComplexivo());
							servAgfEstudianteActaGradoDtoServicioJdbc.actualizarAutoridadesComplexivo(entidad.getFcesId(), autoridadSecretario.toString(), listaAutoridades.get(2).getAtrSexo(), 3,mcttcrAux.getMcttcrPorcentajeComplexivo());
							if(director.getPrsSegundoApellido()!=null){
								servAgfEstudianteActaGradoDtoServicioJdbc.actualizarAutoridadesComplexivo(entidad.getFcesId(), director.getPrsNombres()+" "+director.getPrsPrimerApellido()+" "+director.getPrsSegundoApellido(),  director.getPrsSexo(), 4,mcttcrAux.getMcttcrPorcentajeComplexivo());	
							}else{
								servAgfEstudianteActaGradoDtoServicioJdbc.actualizarAutoridadesComplexivo(entidad.getFcesId(), director.getPrsNombres()+" "+director.getPrsPrimerApellido(),  director.getPrsSexo(), 4,mcttcrAux.getMcttcrPorcentajeComplexivo());
							}
						break;
					case 20:
						if(director.getPrsSegundoApellido()!=null){
							frmCrpParametros.put("director",director.getPrsNombres()+" "+director.getPrsPrimerApellido()+" "+director.getPrsSegundoApellido());	
						}else{
							frmCrpParametros.put("director",director.getPrsNombres()+" "+director.getPrsPrimerApellido());
						}
						frmCrpParametros.put("decano",GeneralesConstantes.APP_NOMBRES_DIRECTOR_DGA);
						frmCrpParametros.put("secretario",autoridadSecretario.toString());
						if(director.getPrsSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
							frmCrpParametros.put("sexoDirector","Director de Carrera");
						}else{
							frmCrpParametros.put("sexoDirector","Directora de Carrera");
						}
							frmCrpParametros.put("sexoDecano","Decana");
							if(listaAutoridades.get(2).getAtrSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
//								frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con el infrascrito Secretario Abogado que certifica. ");
								frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firma la presente acta el infrascrito Secretario Abogado que certifica. ");
								frmCrpParametros.put("sexoSecretario","Secretario Abogado");
							}else{
//								frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con la infrascrita Secretaria Abogada que certifica. ");
								frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firma la presente acta la infrascrita Secretaria Abogada que certifica. ");
								frmCrpParametros.put("sexoSecretario","Secretaria Abogada");
							}
							servAgfEstudianteActaGradoDtoServicioJdbc.actualizarAutoridadesComplexivo(entidad.getFcesId(), GeneralesConstantes.APP_NOMBRES_DIRECTOR_DGA, 1, 1,mcttcrAux.getMcttcrPorcentajeComplexivo());
							servAgfEstudianteActaGradoDtoServicioJdbc.actualizarAutoridadesComplexivo(entidad.getFcesId(), autoridadSecretario.toString(), listaAutoridades.get(2).getAtrSexo(), 3,mcttcrAux.getMcttcrPorcentajeComplexivo());
							if(director.getPrsSegundoApellido()!=null){
								servAgfEstudianteActaGradoDtoServicioJdbc.actualizarAutoridadesComplexivo(entidad.getFcesId(), director.getPrsNombres()+" "+director.getPrsPrimerApellido()+" "+director.getPrsSegundoApellido(),  director.getPrsSexo(), 4,mcttcrAux.getMcttcrPorcentajeComplexivo());	
							}else{
								servAgfEstudianteActaGradoDtoServicioJdbc.actualizarAutoridadesComplexivo(entidad.getFcesId(), director.getPrsNombres()+" "+director.getPrsPrimerApellido(),  director.getPrsSexo(), 4,mcttcrAux.getMcttcrPorcentajeComplexivo());
							}
						break;
					default:
						if(director.getPrsSegundoApellido()!=null){
							frmCrpParametros.put("director",director.getPrsNombres()+" "+director.getPrsPrimerApellido()+" "+director.getPrsSegundoApellido());	
						}else{
							frmCrpParametros.put("director",director.getPrsNombres()+" "+director.getPrsPrimerApellido());
						}
						frmCrpParametros.put("decano",autoridadDecano.toString());
						frmCrpParametros.put("secretario",autoridadSecretario.toString());
						if(director.getPrsSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
							frmCrpParametros.put("sexoDirector","Director de Carrera");
						}else{
							frmCrpParametros.put("sexoDirector","Directora de Carrera");
						}
						if(listaAutoridades.get(0).getAtrSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
							frmCrpParametros.put("sexoDecano","Decano");
						}else{
							frmCrpParametros.put("sexoDecano","Decana");
						}
						if(listaAutoridades.get(2).getAtrSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
//							frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con el infrascrito Secretario Abogado que certifica. ");
							frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firma la presente acta el infrascrito Secretario Abogado que certifica. ");
							frmCrpParametros.put("sexoSecretario","Secretario Abogado");
						}else{
//							frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con la infrascrita Secretaria Abogada que certifica. ");
							frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firma la presente acta la infrascrita Secretaria Abogada que certifica. ");
							frmCrpParametros.put("sexoSecretario","Secretaria Abogada");
						}
						servAgfEstudianteActaGradoDtoServicioJdbc.actualizarAutoridadesComplexivo(entidad.getFcesId(), autoridadDecano.toString(), listaAutoridades.get(0).getAtrSexo(), 1,mcttcrAux.getMcttcrPorcentajeComplexivo());
						servAgfEstudianteActaGradoDtoServicioJdbc.actualizarAutoridadesComplexivo(entidad.getFcesId(), autoridadSecretario.toString(), listaAutoridades.get(2).getAtrSexo(), 3,mcttcrAux.getMcttcrPorcentajeComplexivo());
						if(director.getPrsSegundoApellido()!=null){
							servAgfEstudianteActaGradoDtoServicioJdbc.actualizarAutoridadesComplexivo(entidad.getFcesId(), director.getPrsNombres()+" "+director.getPrsPrimerApellido()+" "+director.getPrsSegundoApellido(),  director.getPrsSexo(), 4,mcttcrAux.getMcttcrPorcentajeComplexivo());	
						}else{
							servAgfEstudianteActaGradoDtoServicioJdbc.actualizarAutoridadesComplexivo(entidad.getFcesId(), director.getPrsNombres()+" "+director.getPrsPrimerApellido(),  director.getPrsSexo(), 4,mcttcrAux.getMcttcrPorcentajeComplexivo());
						}
						break;
					}
				}else{
					autoridadDecano.append(entidad.getFcesDecano());
					autoridadSecretario.append(entidad.getFcesSecretario());
						frmCrpParametros.put("director",entidad.getFcesDirector());	
						
						frmCrpParametros.put("decano",autoridadDecano.toString());
						frmCrpParametros.put("secretario",autoridadSecretario.toString());
						if(entidad.getFcesDirectorSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
							frmCrpParametros.put("sexoDirector","Director de Carrera");
						}else{
							frmCrpParametros.put("sexoDirector","Directora de Carrera");
						}
						if(entidad.getFcesDecanoSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
							frmCrpParametros.put("sexoDecano","Decano");
						}else{
							frmCrpParametros.put("sexoDecano","Decana");
						}
						if(entidad.getFcesSecretarioSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
//							frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con el infrascrito Secretario Abogado que certifica. ");
							frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firma la presente acta el infrascrito Secretario Abogado que certifica. ");
							frmCrpParametros.put("sexoSecretario","Secretario Abogado");
						}else{
//							frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con la infrascrita Secretaria Abogada que certifica. ");
							frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firma la presente acta la infrascrita Secretaria Abogada que certifica. ");
							frmCrpParametros.put("sexoSecretario","Secretaria Abogada");
						}
				}
//				PersonaDto director = servPersonaDtoServicioJdbc.buscarEvaluadorXCarrera(entidad.getCrrId());
//				autoridadDecano.append(listaAutoridades.get(0).getAtrNombres());autoridadDecano.append(" ");
//				autoridadDecano.append(listaAutoridades.get(0).getAtrPrimerApellido());autoridadDecano.append(" ");
//				if(!listaAutoridades.get(0).getAtrSegundoApellido().equals(GeneralesConstantes.APP_ID_BASE.toString())){
//					autoridadDecano.append(listaAutoridades.get(0).getAtrSegundoApellido());
//				}
//				autoridadSecretario.append(listaAutoridades.get(2).getAtrNombres());autoridadSecretario.append(" ");
//				autoridadSecretario.append(listaAutoridades.get(2).getAtrPrimerApellido());autoridadSecretario.append(" ");
//				if(!listaAutoridades.get(2).getAtrSegundoApellido().equals(GeneralesConstantes.APP_ID_BASE.toString())){
//					autoridadSecretario.append(listaAutoridades.get(2).getAtrSegundoApellido());
//				}
//				switch (entidad.getFclId()) {
//				case 18:
//					if(director.getPrsSegundoApellido()!=null){
//						frmCrpParametros.put("director",director.getPrsNombres()+" "+director.getPrsPrimerApellido()+" "+director.getPrsSegundoApellido());	
//					}else{
//						frmCrpParametros.put("director",director.getPrsNombres()+" "+director.getPrsPrimerApellido());
//					}
//					
//					frmCrpParametros.put("decano",GeneralesConstantes.APP_NOMBRES_DIRECTOR_DGA);
//					frmCrpParametros.put("secretario",autoridadSecretario.toString());
//					if(director.getPrsSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
//						frmCrpParametros.put("sexoDirector","Director de Carrera");
//					}else{
//						frmCrpParametros.put("sexoDirector","Directora de Carrera");
//					}
//						frmCrpParametros.put("sexoDecano","Decana");
//						if(listaAutoridades.get(2).getAtrSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
//							frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con el infrascrito Secretario Abogado que certifica. ");
//							frmCrpParametros.put("sexoSecretario","Secretario Abogado");
//						}else{
//							frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con la infrascrita Secretaria Abogada que certifica. ");
//							frmCrpParametros.put("sexoSecretario","Secretaria Abogada");
//						}
//					break;
//				case 19:
//					if(director.getPrsSegundoApellido()!=null){
//						frmCrpParametros.put("director",director.getPrsNombres()+" "+director.getPrsPrimerApellido()+" "+director.getPrsSegundoApellido());	
//					}else{
//						frmCrpParametros.put("director",director.getPrsNombres()+" "+director.getPrsPrimerApellido());
//					}
//					frmCrpParametros.put("decano",GeneralesConstantes.APP_NOMBRES_DIRECTOR_DGA);
//					frmCrpParametros.put("secretario",autoridadSecretario.toString());
//					if(director.getPrsSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
//						frmCrpParametros.put("sexoDirector","Director de Carrera");
//					}else{
//						frmCrpParametros.put("sexoDirector","Directora de Carrera");
//					}
//						frmCrpParametros.put("sexoDecano","Decana");
//						if(listaAutoridades.get(2).getAtrSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
//							frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con el infrascrito Secretario Abogado que certifica. ");
//							frmCrpParametros.put("sexoSecretario","Secretario Abogado");
//						}else{
//							frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con la infrascrita Secretaria Abogada que certifica. ");
//							frmCrpParametros.put("sexoSecretario","Secretaria Abogada");
//						}
//					break;
//				case 20:
//					if(director.getPrsSegundoApellido()!=null){
//						frmCrpParametros.put("director",director.getPrsNombres()+" "+director.getPrsPrimerApellido()+" "+director.getPrsSegundoApellido());	
//					}else{
//						frmCrpParametros.put("director",director.getPrsNombres()+" "+director.getPrsPrimerApellido());
//					}
//					frmCrpParametros.put("decano",GeneralesConstantes.APP_NOMBRES_DIRECTOR_DGA);
//					frmCrpParametros.put("secretario",autoridadSecretario.toString());
//					if(director.getPrsSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
//						frmCrpParametros.put("sexoDirector","Director de Carrera");
//					}else{
//						frmCrpParametros.put("sexoDirector","Directora de Carrera");
//					}
//						frmCrpParametros.put("sexoDecano","Decana");
//						if(listaAutoridades.get(2).getAtrSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
//							frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con el infrascrito Secretario Abogado que certifica. ");
//							frmCrpParametros.put("sexoSecretario","Secretario Abogado");
//						}else{
//							frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con la infrascrita Secretaria Abogada que certifica. ");
//							frmCrpParametros.put("sexoSecretariao","Secretaria Abogada");
//						}
//					break;
//				default:
//					if(director.getPrsSegundoApellido()!=null){
//						frmCrpParametros.put("director",director.getPrsNombres()+" "+director.getPrsPrimerApellido()+" "+director.getPrsSegundoApellido());	
//					}else{
//						frmCrpParametros.put("director",director.getPrsNombres()+" "+director.getPrsPrimerApellido());
//					}
//					frmCrpParametros.put("decano",autoridadDecano.toString());
//					frmCrpParametros.put("secretario",autoridadSecretario.toString());
//					if(director.getPrsSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
//						frmCrpParametros.put("sexoDirector","Director de Carrera");
//					}else{
//						frmCrpParametros.put("sexoDirector","Directora de Carrera");
//					}
//					if(listaAutoridades.get(0).getAtrSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
//						frmCrpParametros.put("sexoDecano","Decano");
//					}else{
//						frmCrpParametros.put("sexoDecano","Decana");
//					}
//					if(listaAutoridades.get(2).getAtrSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
//						frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con el infrascrito Secretario Abogado que certifica. ");
//						frmCrpParametros.put("sexoSecretario","Secretario Abogado");
//					}else{
//						frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con la infrascrita Secretaria Abogada que certifica. ");
//						frmCrpParametros.put("sexoSecretario","Secretaria Abogada");
//					}
//					break;
//				}
				
			}catch (Exception e) {
			}
//			frmCrpParametros.put("equivalencia",resultado);
			frmCrpParametros.put("titulo",entidad.getTtlDescripcion());
			frmCrpCampos.add(dato);
		// Establecemos en el atributo de la sesión la lista de mapas de
		// datos frmCrpCampos y parámetros frmCrpParametros
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmCrpCampos);
		httpSession.setAttribute("frmCargaNombreReporte",frmCrpNombreReporte);
		httpSession.setAttribute("frmCargaParametros",frmCrpParametros);
		// ******************FIN DE GENERACION DE REPORTE ************//
		agfPersonaPostulante = null;
		return "irInicio";
	}
	
	/**
	* Genera el acta para los graduados por examen complexivo y lo establece en la sesión como atributo
	* @param List<RegistradosDto> frmRfRegistradosDto
	* @return String
	*/
	public String generarActaNoVigente(EstudianteEmisionActaJdbcDto entidad){
		 List<Map<String, Object>> frmCrpCampos = null;
		 Map<String, Object> frmCrpParametros = null;
		 String frmCrpNombreReporte = null;
		// ****************************************************************//
		// ***************** GENERACION DEL ACTA DE GRADO  ****************//
		// ****************************************************************//
		// ****************************************************************//
		frmCrpNombreReporte = "ActaDeGradoComplexivo"+"_"+entidad.getPrsPrimerApellido()+"_"+entidad.getPrsSegundoApellido()+"_"+entidad.getPrsNombres();
		frmCrpParametros = new HashMap<String, Object>();
		frmCrpParametros.put("usuario",agfUsuarioQueGenera.getUsrNick());
		StringBuilder sb = new StringBuilder();
		sb.append("En tal virtud, la <style isBold=\"true\">");
		switch (entidad.getFclId()) {
		case 18:
			frmCrpParametros.put("tipoSede", "Sede:");
			sb.append(entidad.getFclDescripcion());
			break;
		case 19:
			frmCrpParametros.put("tipoSede", "Sede:");
			sb.append(entidad.getFclDescripcion());
			break;
		case 20:
			frmCrpParametros.put("tipoSede", "Sede:");
			sb.append(entidad.getFclDescripcion());
			break;
		default:
			frmCrpParametros.put("tipoSede", "Facultad:");
			sb.append("FACULTAD DE ");
			sb.append(entidad.getFclDescripcion());
			break;
		}
		frmCrpParametros.put("facultad",entidad.getFclDescripcion());
		sb.append("</style> de la <style isBold=\"true\">UNIVERSIDAD CENTRAL DEL ECUADOR</style> en su nombre y por autoridad de la ley");
		
		frmCrpParametros.put("facultadCadena",sb.toString());
		SimpleDateFormat formateador = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("ES"));
		String fecha=formateador.format(entidad.getFcesFechaActaGrado());
		frmCrpCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> dato = null;
		dato = new HashMap<String, Object>();
		frmCrpParametros.put("carrera", entidad.getCrrDescripcion());
		frmCrpParametros.put("modalidad", entidad.getMdlDescripcion());
		frmCrpParametros.put("folio", entidad.getFcesNumActaGrado());
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance()
				.getExternalContext().getRealPath("/"));
		pathGeneralReportes.append("/titulacion/reportes/archivosJasper/actasGrado");
		frmCrpParametros.put("imagenCabecera", pathGeneralReportes+"/cabeceraActa.png");
		frmCrpParametros.put("imagenPie", pathGeneralReportes+"/pieActa.png");
		
		//texto que requiere construir de acuerdo a los datos de postulante
		StringBuilder texto = new StringBuilder(); 
		texto.append("En ");
		if(entidad.getUbcDescripcion().equals("QUITO")){
			texto.append("Quito D.M.,");	
		}else{
			texto.append(entidad.getUbcDescripcion());
		}
		texto.append(" hoy ");texto.append(fecha);
		texto.append(" a las ");texto.append(entidad.getFcesHoraActaGrado());
		texto.append(", se procedió a receptar el Examen Complexivo, como mecanismo de titulación ");
		if(entidad.getPrsSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
			texto.append( "del señor ");
		}else if(entidad.getPrsSexo()==PersonaConstantes.SEXO_MUJER_VALUE){
			texto.append( "de la señorita ");
		}else{
			texto.append( "de ");
		}
		texto.append(entidad.getPrsNombres()+" "+entidad.getPrsPrimerApellido()+" "+entidad.getPrsSegundoApellido());
		texto.append(" con identificación N.° ");texto.append(entidad.getPrsIdentificacion());
		texto.append(" de conformidad con el marco legal vigente del Reglamento de Régimen Académico expedido por el Consejo de Educación Superior el ");
		texto.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(RegimenAcademicoConstantes.DIA_REGIMEN_ACADEMICO,false));
		texto.append(" de ");texto.append(GeneralesUtilidades.tranformaNumeroEnMesesMinusculas(RegimenAcademicoConstantes.MES_REGIMEN_ACADEMICO));texto.append(" de ");
		texto.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(RegimenAcademicoConstantes.ANIO_REGIMEN_ACADEMICO,false));
		texto.append(" para la aplicación del examen complexivo, habiendo obtenido las calificaciones que a continuación se detallan:");
		
		frmCrpParametros.put("texto", texto.toString());
		frmCrpParametros.put("fecha_actual", fecha);
		frmCrpParametros.put("postulante", entidad.getPrsNombres()+" "+entidad.getPrsPrimerApellido()+" "+entidad.getPrsSegundoApellido());
		// Nota record Académico 
		StringBuilder recordAcademico = new StringBuilder();
		String nota=entidad.getFcesNotaPromAcumulado().toString();
		String[] partesNotas =  nota.split("\\.");
		recordAcademico.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[0],true));
		recordAcademico.append(" PUNTO ");
		if(Integer.valueOf(partesNotas[1])<10) recordAcademico.append("CERO ");
		recordAcademico.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[1],true));
		frmCrpParametros.put("recordAcademico",recordAcademico.toString());
		// Nota trabajo titulación
		StringBuilder trabajoTitulacion = new StringBuilder();
		String notaTitulacion=entidad.getFcesNotaTrabTitulacion().toString();
		partesNotas = notaTitulacion.split("\\.");
		trabajoTitulacion.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[0],true));
		trabajoTitulacion.append(" PUNTO ");
		if(Integer.valueOf(partesNotas[1])<10) trabajoTitulacion.append("CERO ");
		trabajoTitulacion.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[1],true));
		frmCrpParametros.put("trabajoTitulacion",trabajoTitulacion.toString());
		// Nota promedio
//		StringBuilder promedioFinal = new StringBuilder();
		BigDecimal notaFinal = new BigDecimal(0.0);
		StringBuilder promedioFinal = new StringBuilder();
		notaFinal=(entidad.getFcesNotaPromAcumulado().add(entidad.getFcesNotaTrabTitulacion())).divide(new BigDecimal(2.0), 2,RoundingMode.UP);
		partesNotas = notaFinal.toString().split("\\.");
		promedioFinal.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[0].toString(),true));
		promedioFinal.append(" PUNTO ");
		if(Integer.valueOf(partesNotas[1])<10 && Integer.valueOf(partesNotas[1])!=0) promedioFinal.append("CERO ");
		promedioFinal.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[1].toString(),true));
		frmCrpParametros.put("promedioFinal",promedioFinal.toString());
		frmCrpParametros.put("notaPromedio",notaFinal.toString());
		
//		BigDecimal notaFinal=(entidad.getFcesNotaPromAcumulado().add(entidad.getFcesNotaTrabTitulacion())).divide(new BigDecimal(2.0), 0, RoundingMode.HALF_UP);
//		promedioFinal.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(notaFinal.toString(),true));
//		promedioFinal.append(" PUNTO CERO");
//		frmCrpParametros.put("promedioFinal",promedioFinal.toString());
		
		frmCrpParametros.put("notaRecord",nota);
		frmCrpParametros.put("notaTrabajo",notaTitulacion);
//		frmCrpParametros.put("notaPromedio",notaFinal.toString()+".00");
		try {
			servFichaEstudianteServicio.guardarNotaFinalGraduacion(entidad.getFcesId(), notaFinal, entidad.getTrttId());
		} catch (FichaEstudianteNoEncontradoException e1) {
		} catch (FichaEstudianteException e1) {
			FacesUtil.mensajeError(e1.getMessage());
		}
		int res= notaFinal.compareTo(new BigDecimal(19.50));
		String resultado=null;
		if(res==0 || res==1){
			resultado="EXCELENTE";
		}else {
			int res1= notaFinal.compareTo(new BigDecimal(17.50));
			if(res1==1 || res1==0){
				resultado="MUY BUENO";
			}else{
				int res2= notaFinal.compareTo(new BigDecimal(15.50));
				if(res2==1 || res2==0){
					resultado="BUENO";
				}else {
					int res3= notaFinal.compareTo(new BigDecimal(14.00));
					if(res3==1 || res3==0){
						resultado="REGULAR";
					}else{
						resultado="DEFICIENTE";
					}
				}
			}
		}
			
		try {
			List<AutoridadDto> listaAutoridades = new ArrayList<AutoridadDto>();
			listaAutoridades=servAutoridadDtoServicioJdbc.buscarAutoridades(entidad.getCrrId());
			StringBuilder autoridadDecano = new StringBuilder();
			StringBuilder autoridadSubDecano = new StringBuilder();
			StringBuilder autoridadSecretario = new StringBuilder();
			autoridadDecano.append(listaAutoridades.get(0).getAtrNombres());autoridadDecano.append(" ");
			autoridadDecano.append(listaAutoridades.get(0).getAtrPrimerApellido());autoridadDecano.append(" ");
			if(!listaAutoridades.get(0).getAtrSegundoApellido().equals(GeneralesConstantes.APP_ID_BASE.toString())){
				autoridadDecano.append(listaAutoridades.get(0).getAtrSegundoApellido());
			}
			autoridadSubDecano.append(listaAutoridades.get(1).getAtrNombres());autoridadSubDecano.append(" ");
			autoridadSubDecano.append(listaAutoridades.get(1).getAtrPrimerApellido());autoridadSubDecano.append(" ");
			if(!listaAutoridades.get(1).getAtrSegundoApellido().equals(GeneralesConstantes.APP_ID_BASE.toString())){
				autoridadSubDecano.append(listaAutoridades.get(1).getAtrSegundoApellido());
			}
			autoridadSecretario.append(listaAutoridades.get(2).getAtrNombres());autoridadSecretario.append(" ");
			autoridadSecretario.append(listaAutoridades.get(2).getAtrPrimerApellido());autoridadSecretario.append(" ");
			if(!listaAutoridades.get(2).getAtrSegundoApellido().equals(GeneralesConstantes.APP_ID_BASE.toString())){
				autoridadSecretario.append(listaAutoridades.get(2).getAtrSegundoApellido());
			}
			
			frmCrpParametros.put("director",autoridadSubDecano.toString());
			frmCrpParametros.put("decano",autoridadDecano.toString());
			frmCrpParametros.put("secretario",autoridadSecretario.toString());
		} catch (AutoridadNoEncontradoException e) {
		} catch (AutoridadException e) {
		}catch (Exception e){
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error al buscar autoridades de la facultad, por favor comuníquese con la Dirección de Tecnologías");	
		}
//		frmCrpParametros.put("equivalencia",resultado);
		frmCrpParametros.put("titulo",entidad.getTtlDescripcion());
		
		
		
		frmCrpCampos.add(dato);
		// Establecemos en el atributo de la sesión la lista de mapas de
		// datos frmCrpCampos y parámetros frmCrpParametros
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmCrpCampos);
		httpSession.setAttribute("frmCargaNombreReporte",frmCrpNombreReporte);
		httpSession.setAttribute("frmCargaParametros",frmCrpParametros);
		// ******************FIN DE GENERACION DE REPORTE ************//
		agfPersonaPostulante = null;
		return "irInicio";
	}
	
	
	/**
	* Genera el acta para los graduados por otros mecanismos y lo establece en la sesión como atributo
	* @param List<RegistradosDto> frmRfRegistradosDto
	* @return String
	*/
	public String generarActaOtrosMecanismos(EstudianteEmisionActaJdbcDto entidad){
		 List<Map<String, Object>> frmCrpCampos = null;
		 Map<String, Object> frmCrpParametros = null;
		 String frmCrpNombreReporte = null;
		// ****************************************************************//
		// ***************** GENERACION DEL ACTA DE GRADO  ****************//
		// ****************************************************************//
		// ****************************************************************//
		 if(entidad.getPrsSegundoApellido()!=null){
			 frmCrpNombreReporte = "ActaDeGradoOtrosMecanismos"+"_"+entidad.getPrsPrimerApellido()+"_"+entidad.getPrsSegundoApellido()+"_"+entidad.getPrsNombres();	 
		 }else{
			 frmCrpNombreReporte = "ActaDeGradoOtrosMecanismos"+"_"+entidad.getPrsPrimerApellido()+"_"+entidad.getPrsNombres();
		 }
		
		frmCrpParametros = new HashMap<String, Object>();
		
//		String myCodeText = "https://crunchify.com/";
//		String filePath = GeneralesConstantes.APP_PATH_ARCHIVOS+"codigo_"+entidad.getPrsIdentificacion()+".png";
//		int size = 250;
//		String fileType = "png";
//		File myFile = new File(filePath);
//		try {
//			
//			Map<EncodeHintType, Object> hintMap = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
//			hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
//			
//			// Now with zxing version 3.2.1 you could change border size (white border size to just 1)
//			hintMap.put(EncodeHintType.MARGIN, 1); /* default = 4 */
//			hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
// 
//			QRCodeWriter qrCodeWriter = new QRCodeWriter();
//			BitMatrix byteMatrix = qrCodeWriter.encode(myCodeText, BarcodeFormat.QR_CODE, size,
//					size, hintMap);
//			int CrunchifyWidth = byteMatrix.getWidth();
//			BufferedImage image = new BufferedImage(CrunchifyWidth, CrunchifyWidth,
//					BufferedImage.TYPE_INT_RGB);
//			image.createGraphics();
// 
//			Graphics2D graphics = (Graphics2D) image.getGraphics();
//			graphics.setColor(Color.WHITE);
//			graphics.fillRect(0, 0, CrunchifyWidth, CrunchifyWidth);
//			graphics.setColor(Color.BLACK);
// 
//			for (int i = 0; i < CrunchifyWidth; i++) {
//				for (int j = 0; j < CrunchifyWidth; j++) {
//					if (byteMatrix.get(i, j)) {
//						graphics.fillRect(i, j, 1, 1);
//					}
//				}
//			}
//			ImageIO.write(image, fileType, myFile);
//		} catch (WriterException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (com.google.zxing.WriterException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		frmCrpParametros.put("imagenQR",filePath);
		frmCrpParametros.put("usuario",agfUsuarioQueGenera.getUsrNick());
		StringBuilder sb = new StringBuilder();
		sb.append("En tal virtud, la <style isBold=\"true\">");
		switch (entidad.getFclId()) {
		case 18:
			frmCrpParametros.put("tipoSede", "Sede");
			frmCrpParametros.put("facultad",entidad.getFclDescripcion());
			sb.append(entidad.getFclDescripcion());;
			break;
		case 19:
			frmCrpParametros.put("tipoSede", "Sede");
			frmCrpParametros.put("facultad",entidad.getFclDescripcion());
			sb.append(entidad.getFclDescripcion());
			break;
		case 20:
			frmCrpParametros.put("tipoSede", "Sede");
			frmCrpParametros.put("facultad",entidad.getFclDescripcion());
			sb.append(entidad.getFclDescripcion());
			break;
		default:
			frmCrpParametros.put("tipoSede", "Facultad");
			frmCrpParametros.put("facultad",entidad.getFclDescripcion());
			sb.append("FACULTAD DE ");
			sb.append(entidad.getFclDescripcion());
			break;
		}
		sb.append("</style> de la <style isBold=\"true\">UNIVERSIDAD CENTRAL DEL ECUADOR</style> en su nombre y por autoridad de la ley");
		frmCrpParametros.put("formacion","CUARTO NIVEL");
		frmCrpParametros.put("facultadCadena",sb.toString());
		SimpleDateFormat formateador = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("ES"));
		String fecha=formateador.format(entidad.getFcesFechaActaGrado());
		frmCrpCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> dato = null;
		dato = new HashMap<String, Object>();
		frmCrpParametros.put("carrera", entidad.getCrrDescripcion());
		frmCrpParametros.put("modalidad", entidad.getMdlDescripcion());
		frmCrpParametros.put("folio", entidad.getFcesNumActaGrado());
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance()
				.getExternalContext().getRealPath("/"));
		pathGeneralReportes.append("/titulacionPosgrado/reportes/archivosJasper/actasGrado");
		frmCrpParametros.put("imagenCabecera", pathGeneralReportes+"/cabeceraActa.png");
		frmCrpParametros.put("imagenPie", pathGeneralReportes+"/pieActa.png");
		
		try {
			Persona prs = srvPersonaServivio.buscarPorIdentificacion(agfEstudianteActaGradoJdbcDtoEditar.getPrsIdentificacion());
			frmCrpParametros.put("imagenQR", prs.getPrsUbicacionFoto());
//			frmCrpParametros.put("imagenQR", "d:/archivos/"+"Fotografia_1712707882.jpg");
		} catch (Exception e2) {
		}
		
		
		
		//texto que requiere construir de acuerdo a los datos de postulante
		StringBuilder texto = new StringBuilder(); 
		texto.append("En ");
//		if(entidad.getUbcDescripcion().equals("QUITO")){
			texto.append(" Quito D.M., ");	
//		}else{
//			texto.append(entidad.getUbcDescripcion());texto.append(",");
//		}
		texto.append(" hoy ");texto.append(fecha);
		texto.append(" a las ");texto.append(entidad.getFcesHoraActaGrado().replaceAll("H", ":"));texto.append("h");
		texto.append(", se instala el Tribunal de Grado presidido por ");
		if(agfEstudianteActaGradoJdbcDtoEditar.getListaDocentesTribunal().get(0).getPrsSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
			texto.append("el señor ");
		}else if(agfEstudianteActaGradoJdbcDtoEditar.getListaDocentesTribunal().get(0).getPrsSexo()==PersonaConstantes.SEXO_MUJER_VALUE){
			texto.append("la señora/ita ");
		}
		
		texto.append(presidenteTribunal.toString());
		if(agfEstudianteActaGradoJdbcDtoEditar.getCrrId()==80
				|| agfEstudianteActaGradoJdbcDtoEditar.getCrrId()==42
				|| agfEstudianteActaGradoJdbcDtoEditar.getCrrId()==43
				|| agfEstudianteActaGradoJdbcDtoEditar.getCrrId()==44){
			texto.append(" e integrado por los/as señores/as docentes: ");
		}else{
			texto.append(" e integrado por los/as señores/as docentes: ");
		}
		texto.append(miembro1Tribunal.toString());
		if(entidad.getAsnoDfnOral3()!=GeneralesConstantes.APP_ID_BASE.floatValue()){
			texto.append(" , ");
			texto.append(miembro2Tribunal.toString());	
		}	
		texto.append(" , para la sustentación del trabajo de titulación ");
		if(entidad.getPrsSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
			texto.append( "del señor ");
		}else if(entidad.getPrsSexo()==PersonaConstantes.SEXO_MUJER_VALUE){
			texto.append( "de la señorita ");
		}else{
			texto.append( "de ");
		}
		if(entidad.getPrsSegundoApellido()!=null){
			texto.append(entidad.getPrsNombres()+" "+entidad.getPrsPrimerApellido()+" "+entidad.getPrsSegundoApellido());	
		}else{
			texto.append(entidad.getPrsNombres()+" "+entidad.getPrsPrimerApellido());
		}
		
		if(!entidad.getUbcGentilicio().equals("N/A")){
			texto.append(" de nacionalidad ");
			texto.append(entidad.getUbcGentilicio());
		}
		texto.append(" con identificación N.° ");texto.append(entidad.getPrsIdentificacion());
		texto.append(" de conformidad con el Reglamento de Régimen Académico y el mecanismo de titulación ");
		texto.append(entidad.getMcttdescripcion());
		frmCrpParametros.put("texto", texto.toString());
//		System.out.println(entidad.getAsttTemaTrabajo());
//		String temaFinal =  entidad.getAsttTemaTrabajo().replace("æ", "\u00E6");
//		String temaFinal2 =  temaFinal.replace("ɑ", "\u03B1");
//		String temaFinal3 =  temaFinal2.replace("ə", "\u01DD");
//		String temaFinal4 =  temaFinal3.replace("ε", "\u025B");
//		String temaFinal5 =  temaFinal4.replace("ɪ", "\u026A");
//		String temaFinal6 =  temaFinal5.replace("ɔ", "\u0254");
//		String temaFinal7 =  temaFinal6.replace("ʊ", "\u028A");
//		System.out.println(temaFinal7);
			frmCrpParametros.put("tema", entidad.getAsttTemaTrabajo());	
		if(entidad.getPrsSegundoApellido()!=null){
			frmCrpParametros.put("postulante", entidad.getPrsNombres()+" "+entidad.getPrsPrimerApellido()+" "+entidad.getPrsSegundoApellido());	
		}else{
			frmCrpParametros.put("postulante", entidad.getPrsNombres()+" "+entidad.getPrsPrimerApellido());
		}
		
		// Nota record Académico 
		StringBuilder recordAcademico = new StringBuilder();
		String nota=entidad.getFcesNotaPromAcumulado().toString();
		try {
			Integer.parseInt(nota);
			nota=nota.concat(".00");
		} catch (Exception e) {
		}
		String[] partesNotas =  nota.split("\\.");
		recordAcademico.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[0],true));
		recordAcademico.append(" PUNTO ");
		try {
			if(Integer.valueOf(partesNotas[1])<10 && Integer.valueOf(partesNotas[1])!=0 && (String.valueOf(partesNotas[1]).length()>1)) recordAcademico.append("CERO ");	
		} catch (Exception e) {
			recordAcademico.append("CERO ");
		}
		try {
			recordAcademico.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[1],true));
		} catch (Exception e) {
		}
		
		frmCrpParametros.put("recordAcademico",recordAcademico.toString());
		// Nota trabajo titulación
		StringBuilder trabajoTitulacion = new StringBuilder();
		String notaTitulacion=entidad.getFcesNotaTrabTitulacion().setScale(2, RoundingMode.HALF_UP).toString();
		try {
			Integer.parseInt(notaTitulacion);
			notaTitulacion=notaTitulacion.concat(".00");
		} catch (Exception e) {
		}
		partesNotas =  notaTitulacion.split("\\.");
		trabajoTitulacion.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[0],true));
		trabajoTitulacion.append(" PUNTO ");
		try {
			if(Integer.valueOf(partesNotas[1])<10 && Integer.valueOf(partesNotas[1])!=0 && (String.valueOf(partesNotas[1]).length()>1)){
				trabajoTitulacion.append("CERO ");
				notaTitulacion=notaTitulacion.concat("0");
			} 
					
		} catch (Exception e) {
			trabajoTitulacion.append("CERO ");
		}
		try {
			trabajoTitulacion.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[1],true));
		} catch (Exception e) {
		}
//		partesNotas = notaTitulacion.split("\\.");
//		trabajoTitulacion.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[0],true));
//		trabajoTitulacion.append(" PUNTO ");
//		try {
//			if(Integer.valueOf(partesNotas[1])<10 && Integer.valueOf(partesNotas[1])!=0 && (String.valueOf(partesNotas[1]).length()>1)) {
//				trabajoTitulacion.append("CERO ");	
//			}else{
//				notaTitulacion=entidad.getFcesNotaTrabTitulacion().toString()+"0";
//				partesNotas[1]=partesNotas[1].concat(".00");
//			}
//			trabajoTitulacion.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[1],true));
//		} catch (Exception e) {
//			trabajoTitulacion.append("CERO ");	
//		}
		
		
		
		frmCrpParametros.put("trabajoTitulacion",trabajoTitulacion.toString());
		// Nota promedio
		BigDecimal notaFinal = new BigDecimal(0.0);
//		if(entidad.getCrrId()==CarreraConstantes.CARRERA_MEDICINA_VALUE
//				|| entidad.getCrrId()==CarreraConstantes.CARRERA_ODONTOLOGIA_VALUE
//				|| entidad.getCrrId()==CarreraConstantes.CARRERA_OBSTETRICIA_VALUE
//				|| entidad.getCrrId()==CarreraConstantes.CARRERA_ENFERMERIA_VALUE
//				|| entidad.getCrrId()==CarreraConstantes.CARRERA_ENFERMERIA_NO_VIGENTE_TECNICO_VALUE
//				|| entidad.getCrrId()==CarreraConstantes.CARRERA_ENFERMERIA_NO_VIGENTE_TERCER_NIVEL_VALUE){
			StringBuilder promedioFinal = new StringBuilder();
//			if(entidad.getCnvId()>=4){
				BigDecimal componente1 = agfEstudianteActaGradoJdbcDtoEditar.getFcesNotaPromAcumulado().multiply(new BigDecimal(60)).divide(new BigDecimal(100), 2,RoundingMode.HALF_UP);
				BigDecimal componente2 = agfEstudianteActaGradoJdbcDtoEditar.getFcesNotaTrabTitulacion().multiply(new BigDecimal(40)).divide(new BigDecimal(100), 2,RoundingMode.HALF_UP);
				notaFinal=componente1.add(componente2);
//			}else{
//				notaFinal=(entidad.getFcesNotaPromAcumulado().add(entidad.getFcesNotaTrabTitulacion())).divide(new BigDecimal(2.0), 2,RoundingMode.HALF_UP);	
//			}
			
			partesNotas = notaFinal.toString().split("\\.");
			promedioFinal.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[0].toString(),true));
			promedioFinal.append(" PUNTO ");
			try {
				if(Integer.valueOf(partesNotas[1])<10 && Integer.valueOf(partesNotas[1])!=0 && (String.valueOf(partesNotas[1]).length()>1)) {
					promedioFinal.append("CERO ");	
					
				}
//				else{
//					notaTitulacion=entidad.getFcesNotaTrabTitulacion().toString().concat(".0");
//					partesNotas[1]=partesNotas[1]+"0";
//				}
				promedioFinal.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[1].toString(),true));
			} catch (Exception e) {
				promedioFinal.append("CERO ");	
			}
			
			
			
			frmCrpParametros.put("promedioFinal",promedioFinal.toString());
			frmCrpParametros.put("notaPromedio",notaFinal.setScale(2,RoundingMode.DOWN).toString());
			try {
				servFichaEstudianteServicio.guardarNotaFinalGraduacion(entidad.getFcesId(), notaFinal, entidad.getTrttId());
			} catch (FichaEstudianteNoEncontradoException e1) {
			} catch (FichaEstudianteException e1) {
				FacesUtil.mensajeError(e1.getMessage());
			}
//		}else{
//			StringBuilder promedioFinal = new StringBuilder();
//			notaFinal=(entidad.getFcesNotaPromAcumulado().add(entidad.getFcesNotaTrabTitulacion())).divide(new BigDecimal(2.0), 0, RoundingMode.HALF_UP);
//			promedioFinal.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(notaFinal.toString(),true));
//			promedioFinal.append(" PUNTO CERO");
//			frmCrpParametros.put("promedioFinal",promedioFinal.toString());
//			frmCrpParametros.put("notaPromedio",notaFinal.toString()+".00");
//		}
		try {
			servFichaEstudianteServicio.guardarNotaFinalGraduacion(entidad.getFcesId(), notaFinal,entidad.getTrttId());
		} catch (FichaEstudianteNoEncontradoException e1) {
		} catch (FichaEstudianteException e1) {
			FacesUtil.mensajeError(e1.getMessage());
		}
		frmCrpParametros.put("notaRecord",nota);
		frmCrpParametros.put("notaTrabajo",notaTitulacion);
		int res= notaFinal.compareTo(new BigDecimal(10));
		String resultado=null;
		if(res==0 || res==1){
			resultado="EXCELENTE";
		}else {
			int res1= notaFinal.compareTo(new BigDecimal(9));
			if(res1==1 || res1==0){
				resultado="MUY BUENO";
			}else{
				int res2= notaFinal.compareTo(new BigDecimal(7));
				if(res2==1 || res2==0){
					resultado="BUENO";
				}else {
					int res3= notaFinal.compareTo(new BigDecimal(4));
					if(res3==1 || res3==0){
						resultado="REGULAR";
					}else{
						resultado="DEFICIENTE";
					}
				}
			}
		}
		try {
			List<AutoridadDto> listaAutoridades = new ArrayList<AutoridadDto>();
			listaAutoridades=servAutoridadDtoServicioJdbc.buscarAutoridades(entidad.getCrrId());
			StringBuilder autoridadSecretario = new StringBuilder();
			autoridadSecretario.append(listaAutoridades.get(2).getAtrNombres());autoridadSecretario.append(" ");
			autoridadSecretario.append(listaAutoridades.get(2).getAtrPrimerApellido());autoridadSecretario.append(" ");
			if(!listaAutoridades.get(2).getAtrSegundoApellido().equals(GeneralesConstantes.APP_ID_BASE.toString())){
				autoridadSecretario.append(listaAutoridades.get(2).getAtrSegundoApellido());
			}
			
			frmCrpParametros.put("secretario",autoridadSecretario.toString());
			frmCrpParametros.put("secretario",autoridadSecretario.toString());
			if(listaAutoridades.get(2).getAtrSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
//				frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con el infrascrito Secretario Abogado que certifica. ");
				frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firma la presente acta el infrascrito Secretario Abogado que certifica. ");
				frmCrpParametros.put("sexoSecretario","Secretario Abogado");
			}else{
//				frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con la infrascrita Secretaria Abogada que certifica. ");
				frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firma la presente acta la infrascrita Secretaria Abogada que certifica. ");
				frmCrpParametros.put("sexoSecretario","Secretaria Abogada");
			}
			
			}  catch (AutoridadNoEncontradoException e) {
			} catch (AutoridadException e) {
			}catch (Exception e){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Error al buscar autoridades de la facultad, por favor comuníquese con la Dirección de Tecnologías");	
			}
//		frmCrpParametros.put("equivalencia",resultado);
		frmCrpParametros.put("titulo",entidad.getTtlDescripcion());
		frmCrpParametros.put("presidente",presidenteTribunal.toString());
		frmCrpParametros.put("miembro1",miembro1Tribunal.toString());
		if(agfEstudianteActaGradoJdbcDtoEditar.getAsnoDfnOral3()==GeneralesConstantes.APP_ID_BASE.floatValue()){
			frmCrpParametros.put("miembro2"," ");
		}else{
			frmCrpParametros.put("miembro2",miembro2Tribunal.toString());
		}
		frmCrpCampos.add(dato);
		// Establecemos en el atributo de la sesión la lista de mapas de
		// 	datos frmCrpCampos y parámetros frmCrpParametros
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmCrpCampos);
		httpSession.setAttribute("frmCargaNombreReporte",frmCrpNombreReporte);
		httpSession.setAttribute("frmCargaParametros",frmCrpParametros);
		// ******************FIN DE GENERACION DE REPORTE ************//
		agfPersonaPostulante = null;
		return "irInicio";
	}
	
	public String generarActaOtrosMecanismos3Comp(EstudianteEmisionActaJdbcDto entidad){
		 List<Map<String, Object>> frmCrpCampos = null;
		 Map<String, Object> frmCrpParametros = null;
		 String frmCrpNombreReporte = null;
		// ****************************************************************//
		// ***************** GENERACION DEL ACTA DE GRADO  ****************//
		// ****************************************************************//
		// ****************************************************************//
		 if(entidad.getPrsSegundoApellido()!=null){
			 frmCrpNombreReporte = "ActaDeGradoOtrosMecanismos"+"_"+entidad.getPrsPrimerApellido()+"_"+entidad.getPrsSegundoApellido()+"_"+entidad.getPrsNombres();	 
		 }else{
			 frmCrpNombreReporte = "ActaDeGradoOtrosMecanismos"+"_"+entidad.getPrsPrimerApellido()+"_"+entidad.getPrsNombres();
		 }
		
		frmCrpParametros = new HashMap<String, Object>();
		frmCrpParametros.put("usuario",agfUsuarioQueGenera.getUsrNick());
		frmCrpParametros.put("tipoSede", "Facultad");
		frmCrpParametros.put("facultad",entidad.getFclDescripcion());
		StringBuilder sb = new StringBuilder();
		sb.append("En tal virtud, la <style isBold=\"true\">");
			
			sb.append("FACULTAD DE ");
			sb.append(entidad.getFclDescripcion());
		sb.append("</style> de la <style isBold=\"true\">UNIVERSIDAD CENTRAL DEL ECUADOR</style> en su nombre y por autoridad de la ley");
		frmCrpParametros.put("formacion","CUARTO NIVEL");
		frmCrpParametros.put("facultadCadena",sb.toString());
		SimpleDateFormat formateador = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("ES"));
		String fecha=formateador.format(entidad.getFcesFechaActaGrado());
		frmCrpCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> dato = null;
		dato = new HashMap<String, Object>();
		frmCrpParametros.put("carrera", entidad.getCrrDescripcion());
		frmCrpParametros.put("modalidad", entidad.getMdlDescripcion());
		frmCrpParametros.put("folio", entidad.getFcesNumActaGrado());
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance()
				.getExternalContext().getRealPath("/"));
		pathGeneralReportes.append("/titulacionPosgrado/reportes/archivosJasper/actasGrado");
		frmCrpParametros.put("imagenCabecera", pathGeneralReportes+"/cabeceraActa.png");
		frmCrpParametros.put("imagenPie", pathGeneralReportes+"/pieActa.png");
		
		try {
			Persona prs = srvPersonaServivio.buscarPorIdentificacion(agfEstudianteActaGradoJdbcDtoEditar.getPrsIdentificacion());
			frmCrpParametros.put("imagenQR", prs.getPrsUbicacionFoto());
//			frmCrpParametros.put("imagenQR", "d:/archivos/"+"Fotografia_"+agfEstudianteActaGradoJdbcDtoEditar.getPrsIdentificacion()+".jpg");
//			frmCrpParametros.put("imagenQR", "d:/archivos/"+"Fotografia_1712707882.jpg");
		} catch (Exception e2) {
		}
		
		//texto que requiere construir de acuerdo a los datos de postulante
		StringBuilder texto = new StringBuilder(); 
		texto.append("En ");
//		if(entidad.getUbcDescripcion().equals("QUITO")){
			texto.append(" Quito D.M., ");	
//		}else{
//			texto.append(entidad.getUbcDescripcion());texto.append(",");
//		}
		texto.append(" hoy ");texto.append(fecha);
		texto.append(" a las ");texto.append(entidad.getFcesHoraActaGrado().replaceAll("H", ":"));texto.append("h");
		texto.append(", se instala el Tribunal de Grado presidido por ");
		if(agfEstudianteActaGradoJdbcDtoEditar.getListaDocentesTribunal().get(0).getPrsSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
			texto.append("el señor ");
		}else if(agfEstudianteActaGradoJdbcDtoEditar.getListaDocentesTribunal().get(0).getPrsSexo()==PersonaConstantes.SEXO_MUJER_VALUE){
			texto.append("la señora/ita ");
		}
		
		texto.append(presidenteTribunal.toString());
		if(agfEstudianteActaGradoJdbcDtoEditar.getCrrId()==80
				|| agfEstudianteActaGradoJdbcDtoEditar.getCrrId()==42
				|| agfEstudianteActaGradoJdbcDtoEditar.getCrrId()==43
				|| agfEstudianteActaGradoJdbcDtoEditar.getCrrId()==44){
			texto.append(" e integrado por los/as señores/as docentes: ");
		}else{
			texto.append(" e integrado por los/as señores/as docentes: ");
		}
		texto.append(miembro1Tribunal.toString());
		if(entidad.getPrsIdentificacion().equals("1803407624")
				||entidad.getPrsIdentificacion().equals("1600348138")){
			miembro2Tribunal= new StringBuilder();
			miembro2Tribunal.append("JOSE ARNULFO CONDOR TACO");
		}
		texto.append(" , ");
		try {
			texto.append(miembro2Tribunal.toString());
			texto.append(" , ");
		} catch (Exception e) {
		}
			
		texto.append("para la sustentación del trabajo de titulación ");
		if(entidad.getPrsSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
			texto.append( "del señor ");
		}else if(entidad.getPrsSexo()==PersonaConstantes.SEXO_MUJER_VALUE){
			texto.append( "de la señorita ");
		}else{
			texto.append( "de ");
		}
		if(entidad.getPrsSegundoApellido()!=null){
			texto.append(entidad.getPrsNombres()+" "+entidad.getPrsPrimerApellido()+" "+entidad.getPrsSegundoApellido());	
		}else{
			texto.append(entidad.getPrsNombres()+" "+entidad.getPrsPrimerApellido());
		}
		
		if(!entidad.getUbcGentilicio().equals("N/A")){
			texto.append(" de nacionalidad ");
			texto.append(entidad.getUbcGentilicio());
		}
		texto.append(" con identificación N.° ");texto.append(entidad.getPrsIdentificacion());
		texto.append(" de conformidad con el Reglamento de Régimen Académico y el mecanismo de titulación ");
		texto.append(entidad.getMcttdescripcion());
		frmCrpParametros.put("texto", texto.toString());
			frmCrpParametros.put("tema", entidad.getAsttTemaTrabajo());	
		if(entidad.getPrsSegundoApellido()!=null){
			frmCrpParametros.put("postulante", entidad.getPrsNombres()+" "+entidad.getPrsPrimerApellido()+" "+entidad.getPrsSegundoApellido());	
		}else{
			frmCrpParametros.put("postulante", entidad.getPrsNombres()+" "+entidad.getPrsPrimerApellido());
		}
		
		// Nota record Académico 
		StringBuilder recordAcademico = new StringBuilder();
		String nota=entidad.getFcesNotaPromAcumulado().toString();
		try {
			Integer.parseInt(nota);
			nota=nota.concat(".00");
		} catch (Exception e) {
		}
		String[] partesNotas =  nota.split("\\.");
		recordAcademico.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[0],true));
		recordAcademico.append(" PUNTO ");
		try {
			if(Integer.valueOf(partesNotas[1])<10 && Integer.valueOf(partesNotas[1])!=0 && (String.valueOf(partesNotas[1]).length()>1)) recordAcademico.append("CERO ");	
		} catch (Exception e) {
			recordAcademico.append("CERO ");
		}
		try {
			recordAcademico.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[1],true));
		} catch (Exception e) {
		}
		
		frmCrpParametros.put("recordAcademico",recordAcademico.toString());
		// Nota trabajo titulación
		StringBuilder trabajoTitulacion = new StringBuilder();
		String notaTitulacion=entidad.getFcesNotaTrabTitulacion().toString();
		try {
			Integer.parseInt(notaTitulacion);
			notaTitulacion=notaTitulacion.concat(".00");
		} catch (Exception e) {
		}
		partesNotas =  notaTitulacion.split("\\.");
		trabajoTitulacion.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[0],true));
		trabajoTitulacion.append(" PUNTO ");
		try {
			if(Integer.valueOf(partesNotas[1])<10 && Integer.valueOf(partesNotas[1])!=0 && (String.valueOf(partesNotas[1]).length()>1)) trabajoTitulacion.append("CERO ");	
		} catch (Exception e) {
			trabajoTitulacion.append("CERO ");
		}
		try {
			trabajoTitulacion.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[1],true));
		} catch (Exception e) {
		}
		
		
		
		frmCrpParametros.put("trabajoTitulacion",trabajoTitulacion.toString());
		
		
		// Nota promedio
		BigDecimal notaFinal = new BigDecimal(0.0);
//		if(entidad.getCrrId()==CarreraConstantes.CARRERA_MEDICINA_VALUE
//				|| entidad.getCrrId()==CarreraConstantes.CARRERA_ODONTOLOGIA_VALUE
//				|| entidad.getCrrId()==CarreraConstantes.CARRERA_OBSTETRICIA_VALUE
//				|| entidad.getCrrId()==CarreraConstantes.CARRERA_ENFERMERIA_VALUE
//				|| entidad.getCrrId()==CarreraConstantes.CARRERA_ENFERMERIA_NO_VIGENTE_TECNICO_VALUE
//				|| entidad.getCrrId()==CarreraConstantes.CARRERA_ENFERMERIA_NO_VIGENTE_TERCER_NIVEL_VALUE){
			StringBuilder promedioFinal = new StringBuilder();
			if(iagfTipoCarrera){
				BigDecimal componente1 = agfEstudianteActaGradoJdbcDtoEditar.getFcesNotaPromAcumulado().multiply(new BigDecimal(60)).divide(new BigDecimal(100), 2,RoundingMode.DOWN);
				BigDecimal componente2 = agfEstudianteActaGradoJdbcDtoEditar.getFcesNotaTrabTitulacion().multiply(new BigDecimal(40)).divide(new BigDecimal(100), 2,RoundingMode.DOWN);
				notaFinal=componente1.add(componente2);
			}else{
				StringBuilder notaEscrito = new StringBuilder();
				BigDecimal promEscrito = new BigDecimal(entidad.getAsnoPrmTrbEscrito());
				String notaEscritoString=promEscrito.setScale(2,RoundingMode.HALF_UP).toString();
				try {
					Integer.parseInt(notaEscritoString);
					notaEscritoString=notaEscritoString.concat(".00");
				} catch (Exception e) {
				}
				partesNotas =  notaEscritoString.split("\\.");
				notaEscrito.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[0],true));
				notaEscrito.append(" PUNTO ");
				try {
					if(Integer.valueOf(partesNotas[1])<10 && Integer.valueOf(partesNotas[1])!=0 && (String.valueOf(partesNotas[1]).length()>1)) notaEscrito.append("CERO ");	
				} catch (Exception e) {
					notaEscrito.append("CERO ");
				}
				try {
					notaEscrito.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[1],true));
				} catch (Exception e) {
				}
				frmCrpParametros.put("tribunalEscrito",notaEscrito.toString());
				frmCrpParametros.put("notaEscrito",notaEscritoString);
				StringBuilder notaOral = new StringBuilder();
				String notaOralString=entidad.getAsnoPrmDfnOral().toString();
				try {
					Integer.parseInt(notaOralString);
					notaOralString=notaOralString.concat(".00");
				} catch (Exception e) {
				}
				partesNotas =  notaOralString.split("\\.");
				notaOral.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[0],true));
				notaOral.append(" PUNTO ");
				try {
					if(Integer.valueOf(partesNotas[1])<10 && Integer.valueOf(partesNotas[1])!=0 && (String.valueOf(partesNotas[1]).length()>1)) notaOral.append("CERO ");	
				} catch (Exception e) {
					notaOral.append("CERO ");
					notaOralString=entidad.getAsnoPrmDfnOral().toString()+"00";
				}
				try {
					notaOral.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[1],true));
				} catch (Exception e) {
				}
				frmCrpParametros.put("notaOral",notaOralString);
				frmCrpParametros.put("tribunalOral",notaOral.toString());
				BigDecimal componente1 = agfEstudianteActaGradoJdbcDtoEditar.getFcesNotaPromAcumulado();
				BigDecimal componente2 = agfEstudianteActaGradoJdbcDtoEditar.getFcesNotaPromAcumulado();
				componente2 = agfEstudianteActaGradoJdbcDtoEditar.getFcesNotaTrabTitulacion();
				notaFinal=(componente1.add(componente2)).divide(new BigDecimal(2),2,RoundingMode.HALF_UP);
			}
			
			partesNotas = notaFinal.toString().split("\\.");
			promedioFinal.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[0].toString(),true));
			promedioFinal.append(" PUNTO ");
			try {
				if(Integer.valueOf(partesNotas[1])<10 && Integer.valueOf(partesNotas[1])!=0 && (String.valueOf(partesNotas[1]).length()>1)) {
					promedioFinal.append("CERO ");	
					
				}
//				else{
//					notaTitulacion=entidad.getFcesNotaTrabTitulacion().toString().concat(".0");
//					partesNotas[1]=partesNotas[1]+"0";
//				}
				promedioFinal.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[1].toString(),true));
			} catch (Exception e) {
				promedioFinal.append("CERO ");	
			}
			
			
			
			frmCrpParametros.put("promedioFinal",promedioFinal.toString());
			frmCrpParametros.put("notaPromedio",notaFinal.toString());
			try {
				servFichaEstudianteServicio.guardarNotaFinalGraduacion(entidad.getFcesId(), notaFinal, entidad.getTrttId());
			} catch (FichaEstudianteNoEncontradoException e1) {
			} catch (FichaEstudianteException e1) {
				FacesUtil.mensajeError(e1.getMessage());
			}
//		}else{
//			StringBuilder promedioFinal = new StringBuilder();
//			notaFinal=(entidad.getFcesNotaPromAcumulado().add(entidad.getFcesNotaTrabTitulacion())).divide(new BigDecimal(2.0), 0, RoundingMode.HALF_UP);
//			promedioFinal.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(notaFinal.toString(),true));
//			promedioFinal.append(" PUNTO CERO");
//			frmCrpParametros.put("promedioFinal",promedioFinal.toString());
//			frmCrpParametros.put("notaPromedio",notaFinal.toString()+".00");
//		}
//		try {
//			servFichaEstudianteServicio.guardarNotaFinalGraduacion(entidad.getFcesId(), notaFinal,entidad.getTrttId());
//		} catch (FichaEstudianteNoEncontradoException e1) {
//		} catch (FichaEstudianteException e1) {
//			FacesUtil.mensajeError(e1.getMessage());
//		}
		frmCrpParametros.put("notaRecord",nota);
		frmCrpParametros.put("notaTrabajo",notaTitulacion);
		int res= notaFinal.compareTo(new BigDecimal(10));
		String resultado=null;
		if(res==0 || res==1){
			resultado="EXCELENTE";
		}else {
			int res1= notaFinal.compareTo(new BigDecimal(9));
			if(res1==1 || res1==0){
				resultado="MUY BUENO";
			}else{
				int res2= notaFinal.compareTo(new BigDecimal(7));
				if(res2==1 || res2==0){
					resultado="BUENO";
				}else {
					int res3= notaFinal.compareTo(new BigDecimal(4));
					if(res3==1 || res3==0){
						resultado="REGULAR";
					}else{
						resultado="DEFICIENTE";
					}
				}
			}
		}
		try {
			List<AutoridadDto> listaAutoridades = new ArrayList<AutoridadDto>();
			listaAutoridades=servAutoridadDtoServicioJdbc.buscarAutoridades(entidad.getCrrId());
			StringBuilder autoridadSecretario = new StringBuilder();
			autoridadSecretario.append(listaAutoridades.get(2).getAtrNombres());autoridadSecretario.append(" ");
			autoridadSecretario.append(listaAutoridades.get(2).getAtrPrimerApellido());autoridadSecretario.append(" ");
			if(!listaAutoridades.get(2).getAtrSegundoApellido().equals(GeneralesConstantes.APP_ID_BASE.toString())){
				autoridadSecretario.append(listaAutoridades.get(2).getAtrSegundoApellido());
			}
			
			frmCrpParametros.put("secretario",autoridadSecretario.toString());
			frmCrpParametros.put("secretario",autoridadSecretario.toString());
			if(listaAutoridades.get(2).getAtrSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
//				frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con el infrascrito Secretario Abogado que certifica. ");
				frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firma la presente acta el infrascrito Secretario Abogado que certifica. ");
				frmCrpParametros.put("sexoSecretario","Secretario Abogado");
			}else{
//				frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con la infrascrita Secretaria Abogada que certifica. ");
				frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firma la presente acta la infrascrita Secretaria Abogada que certifica. ");
				frmCrpParametros.put("sexoSecretario","Secretaria Abogada");
			}
			
			}  catch (AutoridadNoEncontradoException e) {
			} catch (AutoridadException e) {
			}catch (Exception e){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Error al buscar autoridades de la facultad, por favor comuníquese con la Dirección de Tecnologías");	
			}
//		frmCrpParametros.put("equivalencia",resultado);
		frmCrpParametros.put("titulo",entidad.getTtlDescripcion());
		frmCrpParametros.put("presidente",presidenteTribunal.toString());
		frmCrpParametros.put("miembro1",miembro1Tribunal.toString());
		try {
			if(miembro2Tribunal!=null){
				frmCrpParametros.put("miembro2",miembro2Tribunal.toString());
			}else{
				if(agfEstudianteActaGradoJdbcDtoEditar.getListaDocentesTribunal().size()==3){
					frmCrpParametros.put("miembro2",miembro2Tribunal.toString());
					
				}else{
					frmCrpParametros.put("miembro2"," ");
				}		
			}
			
		} catch (Exception e) {
			frmCrpParametros.put("miembro2"," ");
		}
		
		frmCrpCampos.add(dato);
		// Establecemos en el atributo de la sesión la lista de mapas de
		// 	datos frmCrpCampos y parámetros frmCrpParametros
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmCrpCampos);
		httpSession.setAttribute("frmCargaNombreReporte",frmCrpNombreReporte);
		httpSession.setAttribute("frmCargaParametros",frmCrpParametros);
		// ******************FIN DE GENERACION DE REPORTE ************//
		agfPersonaPostulante = null;
		return "irInicio";
	}
	
	
	public String generarActaOtrosMecanismosCalculo3(EstudianteEmisionActaJdbcDto entidad){
		 List<Map<String, Object>> frmCrpCampos = null;
		 Map<String, Object> frmCrpParametros = null;
		 String frmCrpNombreReporte = null;
		// ****************************************************************//
		// ***************** GENERACION DEL ACTA DE GRADO  ****************//
		// ****************************************************************//
		// ****************************************************************//
		 if(entidad.getPrsSegundoApellido()!=null){
			 frmCrpNombreReporte = "ActaDeGradoOtrosMecanismos"+"_"+entidad.getPrsPrimerApellido()+"_"+entidad.getPrsSegundoApellido()+"_"+entidad.getPrsNombres();	 
		 }else{
			 frmCrpNombreReporte = "ActaDeGradoOtrosMecanismos"+"_"+entidad.getPrsPrimerApellido()+"_"+entidad.getPrsNombres();
		 }
		
		frmCrpParametros = new HashMap<String, Object>();
		frmCrpParametros.put("usuario",agfUsuarioQueGenera.getUsrNick());
		frmCrpParametros.put("tipoSede", "Facultad");
		frmCrpParametros.put("facultad",entidad.getFclDescripcion());
		StringBuilder sb = new StringBuilder();
		sb.append("En tal virtud, la <style isBold=\"true\">");
			
			sb.append("FACULTAD DE ");
			sb.append(entidad.getFclDescripcion());
		sb.append("</style> de la <style isBold=\"true\">UNIVERSIDAD CENTRAL DEL ECUADOR</style> en su nombre y por autoridad de la ley");
		frmCrpParametros.put("formacion","CUARTO NIVEL");
		frmCrpParametros.put("facultadCadena",sb.toString());
		SimpleDateFormat formateador = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("ES"));
		String fecha=formateador.format(entidad.getFcesFechaActaGrado());
		frmCrpCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> dato = null;
		dato = new HashMap<String, Object>();
		frmCrpParametros.put("carrera", entidad.getCrrDescripcion());
		frmCrpParametros.put("modalidad", entidad.getMdlDescripcion());
		frmCrpParametros.put("folio", entidad.getFcesNumActaGrado());
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance()
				.getExternalContext().getRealPath("/"));
		pathGeneralReportes.append("/titulacionPosgrado/reportes/archivosJasper/actasGrado");
		frmCrpParametros.put("imagenCabecera", pathGeneralReportes+"/cabeceraActa.png");
		frmCrpParametros.put("imagenPie", pathGeneralReportes+"/pieActa.png");
		
		try {
			Persona prs = srvPersonaServivio.buscarPorIdentificacion(agfEstudianteActaGradoJdbcDtoEditar.getPrsIdentificacion());
			frmCrpParametros.put("imagenQR", prs.getPrsUbicacionFoto());
//			frmCrpParametros.put("imagenQR", "d:/archivos/"+"Fotografia_1712707882.jpg");
		} catch (Exception e2) {
		}
		
		
		
		//texto que requiere construir de acuerdo a los datos de postulante
		StringBuilder texto = new StringBuilder(); 
		texto.append("En ");
//		if(entidad.getUbcDescripcion().equals("QUITO")){
			texto.append(" Quito D.M., ");	
//		}else{
//			texto.append(entidad.getUbcDescripcion());texto.append(",");
//		}
		texto.append(" hoy ");texto.append(fecha);
		texto.append(" a las ");texto.append(entidad.getFcesHoraActaGrado().replaceAll("H", ":"));texto.append("h");
		texto.append(", se instala el Tribunal de Grado presidido por ");
		if(agfEstudianteActaGradoJdbcDtoEditar.getListaDocentesTribunal().get(0).getPrsSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
			texto.append("el señor ");
		}else if(agfEstudianteActaGradoJdbcDtoEditar.getListaDocentesTribunal().get(0).getPrsSexo()==PersonaConstantes.SEXO_MUJER_VALUE){
			texto.append("la señora/ita ");
		}
		
		texto.append(presidenteTribunal.toString());
		if(agfEstudianteActaGradoJdbcDtoEditar.getCrrId()==80
				|| agfEstudianteActaGradoJdbcDtoEditar.getCrrId()==42
				|| agfEstudianteActaGradoJdbcDtoEditar.getCrrId()==43
				|| agfEstudianteActaGradoJdbcDtoEditar.getCrrId()==44){
			texto.append(" e integrado por los/as señores/as docentes: ");
		}else{
			texto.append(" e integrado por los/as señores/as docentes: ");
		}
		texto.append(miembro1Tribunal.toString());
		if(entidad.getPrsIdentificacion().equals("1803407624")
				||entidad.getPrsIdentificacion().equals("1600348138")){
			miembro2Tribunal= new StringBuilder();
			miembro2Tribunal.append("JOSE ARNULFO CONDOR TACO");
		}
		texto.append(" , ");
		try {
			texto.append(miembro2Tribunal.toString());
			texto.append(" , ");
		} catch (Exception e) {
		}
			
		texto.append("para la sustentación del trabajo de titulación ");
		if(entidad.getPrsSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
			texto.append( "del señor ");
		}else if(entidad.getPrsSexo()==PersonaConstantes.SEXO_MUJER_VALUE){
			texto.append( "de la señorita ");
		}else{
			texto.append( "de ");
		}
		if(entidad.getPrsSegundoApellido()!=null){
			texto.append(entidad.getPrsNombres()+" "+entidad.getPrsPrimerApellido()+" "+entidad.getPrsSegundoApellido());	
		}else{
			texto.append(entidad.getPrsNombres()+" "+entidad.getPrsPrimerApellido());
		}
		
		if(!entidad.getUbcGentilicio().equals("N/A")){
			texto.append(" de nacionalidad ");
			texto.append(entidad.getUbcGentilicio());
		}
		texto.append(" con identificación N.° ");texto.append(entidad.getPrsIdentificacion());
		texto.append(" de conformidad con el Reglamento de Régimen Académico y el mecanismo de titulación ");
		texto.append(entidad.getMcttdescripcion());
		frmCrpParametros.put("texto", texto.toString());
			frmCrpParametros.put("tema", entidad.getAsttTemaTrabajo());	
		if(entidad.getPrsSegundoApellido()!=null){
			frmCrpParametros.put("postulante", entidad.getPrsNombres()+" "+entidad.getPrsPrimerApellido()+" "+entidad.getPrsSegundoApellido());	
		}else{
			frmCrpParametros.put("postulante", entidad.getPrsNombres()+" "+entidad.getPrsPrimerApellido());
		}
		
		// Nota record Académico 
		StringBuilder recordAcademico = new StringBuilder();
		String nota=entidad.getFcesNotaPromAcumulado().toString();
		try {
			Integer.parseInt(nota);
			nota=nota.concat(".00");
		} catch (Exception e) {
		}
		String[] partesNotas =  nota.split("\\.");
		recordAcademico.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[0],true));
		recordAcademico.append(" PUNTO ");
		try {
			if(Integer.valueOf(partesNotas[1])<10 && Integer.valueOf(partesNotas[1])!=0 && (String.valueOf(partesNotas[1]).length()>1)) recordAcademico.append("CERO ");	
		} catch (Exception e) {
			recordAcademico.append("CERO ");
		}
		try {
			recordAcademico.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[1],true));
		} catch (Exception e) {
		}
		
		frmCrpParametros.put("recordAcademico",recordAcademico.toString());
		// Nota trabajo titulación
		StringBuilder trabajoTitulacion = new StringBuilder();
		String notaTitulacion=entidad.getFcesNotaTrabTitulacion().toString();
		try {
			Integer.parseInt(notaTitulacion);
			notaTitulacion=notaTitulacion.concat(".00");
		} catch (Exception e) {
		}
		partesNotas =  notaTitulacion.split("\\.");
		trabajoTitulacion.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[0],true));
		trabajoTitulacion.append(" PUNTO ");
		try {
			if(Integer.valueOf(partesNotas[1])<10 && Integer.valueOf(partesNotas[1])!=0 && (String.valueOf(partesNotas[1]).length()>1)) trabajoTitulacion.append("CERO ");	
		} catch (Exception e) {
			trabajoTitulacion.append("CERO ");
		}
		try {
			trabajoTitulacion.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[1],true));
		} catch (Exception e) {
		}
		
		
		
		frmCrpParametros.put("trabajoTitulacion",trabajoTitulacion.toString());
		
		
		// Nota promedio
		BigDecimal notaFinal = new BigDecimal(0.0);
//		if(entidad.getCrrId()==CarreraConstantes.CARRERA_MEDICINA_VALUE
//				|| entidad.getCrrId()==CarreraConstantes.CARRERA_ODONTOLOGIA_VALUE
//				|| entidad.getCrrId()==CarreraConstantes.CARRERA_OBSTETRICIA_VALUE
//				|| entidad.getCrrId()==CarreraConstantes.CARRERA_ENFERMERIA_VALUE
//				|| entidad.getCrrId()==CarreraConstantes.CARRERA_ENFERMERIA_NO_VIGENTE_TECNICO_VALUE
//				|| entidad.getCrrId()==CarreraConstantes.CARRERA_ENFERMERIA_NO_VIGENTE_TERCER_NIVEL_VALUE){
 			StringBuilder promedioFinal = new StringBuilder();
			if(iagfTipoCarrera){
				BigDecimal componente1 = agfEstudianteActaGradoJdbcDtoEditar.getFcesNotaPromAcumulado().multiply(new BigDecimal(60)).divide(new BigDecimal(100), 2,RoundingMode.DOWN);
				BigDecimal componente2 = agfEstudianteActaGradoJdbcDtoEditar.getFcesNotaTrabTitulacion().multiply(new BigDecimal(40)).divide(new BigDecimal(100), 2,RoundingMode.DOWN);
				notaFinal=componente1.add(componente2);
			}else{
				StringBuilder notaEscrito = new StringBuilder();
				BigDecimal promEscrito = new BigDecimal(entidad.getAsnoPrmTrbEscrito());
				String notaEscritoString=promEscrito.setScale(2,RoundingMode.HALF_UP).toString();
				try {
					Integer.parseInt(notaEscritoString);
					notaEscritoString=notaEscritoString.concat(".00");
				} catch (Exception e) {
				}
				partesNotas =  notaEscritoString.split("\\.");
				notaEscrito.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[0],true));
				notaEscrito.append(" PUNTO ");
				try {
					if(Integer.valueOf(partesNotas[1])<10 && Integer.valueOf(partesNotas[1])!=0 && (String.valueOf(partesNotas[1]).length()>1)) notaEscrito.append("CERO ");	
				} catch (Exception e) {
					notaEscrito.append("CERO ");
				}
				try {
					notaEscrito.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[1],true));
				} catch (Exception e) {
				}
				frmCrpParametros.put("tribunalEscrito",notaEscrito.toString());
				frmCrpParametros.put("notaEscrito",notaEscritoString);
				StringBuilder notaOral = new StringBuilder();
				String notaOralString=new BigDecimal(entidad.getAsnoPrmDfnOral()).setScale(2, RoundingMode.HALF_UP).toString();
				try {
					Integer.parseInt(notaOralString);
					notaOralString=notaOralString.concat(".00");
				} catch (Exception e) {
				}
				partesNotas =  notaOralString.split("\\.");
				notaOral.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[0],true));
				notaOral.append(" PUNTO ");
				try {
					if(Integer.valueOf(partesNotas[1])<10 && Integer.valueOf(partesNotas[1])!=0 && (String.valueOf(partesNotas[1]).length()>1)) notaOral.append("CERO ");	
				} catch (Exception e) {
					notaOral.append("CERO ");
					notaOralString=entidad.getAsnoPrmDfnOral().toString()+"00";
				}
				try {
					notaOral.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[1],true));
				} catch (Exception e) {
				}
				frmCrpParametros.put("notaOral",notaOralString);
				frmCrpParametros.put("tribunalOral",notaOral.toString());
				BigDecimal componente1 = agfEstudianteActaGradoJdbcDtoEditar.getFcesNotaPromAcumulado();
				BigDecimal componente2 = new BigDecimal(agfEstudianteActaGradoJdbcDtoEditar.getAsnoPrmDfnOral()).setScale(2,RoundingMode.DOWN);
				BigDecimal componente3 = new BigDecimal(agfEstudianteActaGradoJdbcDtoEditar.getAsnoPrmTrbEscrito()).setScale(2,RoundingMode.DOWN);
				notaFinal=(componente1.add(componente2).add(componente3)).divide(new BigDecimal(3),2,RoundingMode.HALF_UP);
			}
			
			partesNotas = notaFinal.toString().split("\\.");
			promedioFinal.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[0].toString(),true));
			promedioFinal.append(" PUNTO ");
			try {
				if(Integer.valueOf(partesNotas[1])<10 && Integer.valueOf(partesNotas[1])!=0 && (String.valueOf(partesNotas[1]).length()>1)) {
					promedioFinal.append("CERO ");	
					
				}
//				else{
//					notaTitulacion=entidad.getFcesNotaTrabTitulacion().toString().concat(".0");
//					partesNotas[1]=partesNotas[1]+"0";
//				}
				promedioFinal.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[1].toString(),true));
			} catch (Exception e) {
				promedioFinal.append("CERO ");	
			}
			
			
			
			frmCrpParametros.put("promedioFinal",promedioFinal.toString());
			frmCrpParametros.put("notaPromedio",notaFinal.toString());
			try {
				servFichaEstudianteServicio.guardarNotaFinalGraduacion(entidad.getFcesId(), notaFinal, entidad.getTrttId());
			} catch (FichaEstudianteNoEncontradoException e1) {
			} catch (FichaEstudianteException e1) {
				FacesUtil.mensajeError(e1.getMessage());
			}
//		}else{
//			StringBuilder promedioFinal = new StringBuilder();
//			notaFinal=(entidad.getFcesNotaPromAcumulado().add(entidad.getFcesNotaTrabTitulacion())).divide(new BigDecimal(2.0), 0, RoundingMode.HALF_UP);
//			promedioFinal.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(notaFinal.toString(),true));
//			promedioFinal.append(" PUNTO CERO");
//			frmCrpParametros.put("promedioFinal",promedioFinal.toString());
//			frmCrpParametros.put("notaPromedio",notaFinal.toString()+".00");
//		}
//		try {
//			servFichaEstudianteServicio.guardarNotaFinalGraduacion(entidad.getFcesId(), notaFinal,entidad.getTrttId());
//		} catch (FichaEstudianteNoEncontradoException e1) {
//		} catch (FichaEstudianteException e1) {
//			FacesUtil.mensajeError(e1.getMessage());
//		}
		frmCrpParametros.put("notaRecord",nota);
		frmCrpParametros.put("notaTrabajo",notaTitulacion);
		int res= notaFinal.compareTo(new BigDecimal(10));
		String resultado=null;
		if(res==0 || res==1){
			resultado="EXCELENTE";
		}else {
			int res1= notaFinal.compareTo(new BigDecimal(9));
			if(res1==1 || res1==0){
				resultado="MUY BUENO";
			}else{
				int res2= notaFinal.compareTo(new BigDecimal(7));
				if(res2==1 || res2==0){
					resultado="BUENO";
				}else {
					int res3= notaFinal.compareTo(new BigDecimal(4));
					if(res3==1 || res3==0){
						resultado="REGULAR";
					}else{
						resultado="DEFICIENTE";
					}
				}
			}
		}
		try {
			List<AutoridadDto> listaAutoridades = new ArrayList<AutoridadDto>();
			listaAutoridades=servAutoridadDtoServicioJdbc.buscarAutoridades(entidad.getCrrId());
			StringBuilder autoridadSecretario = new StringBuilder();
			autoridadSecretario.append(listaAutoridades.get(2).getAtrNombres());autoridadSecretario.append(" ");
			autoridadSecretario.append(listaAutoridades.get(2).getAtrPrimerApellido());autoridadSecretario.append(" ");
			if(!listaAutoridades.get(2).getAtrSegundoApellido().equals(GeneralesConstantes.APP_ID_BASE.toString())){
				autoridadSecretario.append(listaAutoridades.get(2).getAtrSegundoApellido());
			}
			
			frmCrpParametros.put("secretario",autoridadSecretario.toString());
			frmCrpParametros.put("secretario",autoridadSecretario.toString());
			if(listaAutoridades.get(2).getAtrSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
//				frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con el infrascrito Secretario Abogado que certifica. ");
				frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firma la presente acta el infrascrito Secretario Abogado que certifica. ");
				frmCrpParametros.put("sexoSecretario","Secretario Abogado");
			}else{
//				frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con la infrascrita Secretaria Abogada que certifica. ");
				frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firma la presente acta la infrascrita Secretaria Abogada que certifica. ");
				frmCrpParametros.put("sexoSecretario","Secretaria Abogada");
			}
			
			}  catch (AutoridadNoEncontradoException e) {
			} catch (AutoridadException e) {
			}catch (Exception e){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Error al buscar autoridades de la facultad, por favor comuníquese con la Dirección de Tecnologías");	
			}
//		frmCrpParametros.put("equivalencia",resultado);
		frmCrpParametros.put("titulo",entidad.getTtlDescripcion());
		frmCrpParametros.put("presidente",presidenteTribunal.toString());
		frmCrpParametros.put("miembro1",miembro1Tribunal.toString());
		try {
			if(miembro2Tribunal!=null){
				frmCrpParametros.put("miembro2",miembro2Tribunal.toString());
			}else{
				if(agfEstudianteActaGradoJdbcDtoEditar.getListaDocentesTribunal().size()==3){
					frmCrpParametros.put("miembro2",miembro2Tribunal.toString());
					
				}else{
					frmCrpParametros.put("miembro2"," ");
				}		
			}
			
		} catch (Exception e) {
			frmCrpParametros.put("miembro2"," ");
		}
		
		frmCrpCampos.add(dato);
		// Establecemos en el atributo de la sesión la lista de mapas de
		// 	datos frmCrpCampos y parámetros frmCrpParametros
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmCrpCampos);
		httpSession.setAttribute("frmCargaNombreReporte",frmCrpNombreReporte);
		httpSession.setAttribute("frmCargaParametros",frmCrpParametros);
		// ******************FIN DE GENERACION DE REPORTE ************//
		agfPersonaPostulante = null;
		return "irInicio";
	}
	
	/**
	* Genera el acta para los graduados por otros mecanismos y lo establece en la sesión como atributo
	* @param List<RegistradosDto> frmRfRegistradosDto
	* @return String
	*/
	public String generarActaOtrosMecanismos2018(EstudianteEmisionActaJdbcDto entidad){
		 List<Map<String, Object>> frmCrpCampos = null;
		 Map<String, Object> frmCrpParametros = null;
		 String frmCrpNombreReporte = null;
		// ****************************************************************//
		// ***************** GENERACION DEL ACTA DE GRADO  ****************//
		// ****************************************************************//
		// ****************************************************************//
		 if(entidad.getPrsSegundoApellido()!=null){
			 frmCrpNombreReporte = "ActaDeGradoOtrosMecanismos"+"_"+entidad.getPrsPrimerApellido()+"_"+entidad.getPrsSegundoApellido()+"_"+entidad.getPrsNombres();	 
		 }else{
			 frmCrpNombreReporte = "ActaDeGradoOtrosMecanismos"+"_"+entidad.getPrsPrimerApellido()+"_"+entidad.getPrsNombres();
		 }
		
		frmCrpParametros = new HashMap<String, Object>();
		frmCrpParametros.put("usuario",agfUsuarioQueGenera.getUsrNick());
		StringBuilder sb = new StringBuilder();
		sb.append("En tal virtud, la <style isBold=\"true\">");
		switch (entidad.getFclId()) {
		case 18:
			frmCrpParametros.put("tipoSede", "Sede");
			frmCrpParametros.put("facultad",entidad.getFclDescripcion());
			sb.append(entidad.getFclDescripcion());;
			break;
		case 19:
			frmCrpParametros.put("tipoSede", "Sede");
			frmCrpParametros.put("facultad",entidad.getFclDescripcion());
			sb.append(entidad.getFclDescripcion());
			break;
		case 20:
			frmCrpParametros.put("tipoSede", "Sede");
			frmCrpParametros.put("facultad",entidad.getFclDescripcion());
			sb.append(entidad.getFclDescripcion());
			break;
		default:
			frmCrpParametros.put("tipoSede", "Facultad");
			frmCrpParametros.put("facultad",entidad.getFclDescripcion());
			sb.append("FACULTAD DE ");
			sb.append(entidad.getFclDescripcion());
			break;
		}
		sb.append("</style> de la <style isBold=\"true\">UNIVERSIDAD CENTRAL DEL ECUADOR</style> en su nombre y por autoridad de la ley");
		
		frmCrpParametros.put("facultadCadena",sb.toString());
		SimpleDateFormat formateador = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("ES"));
		String fecha=formateador.format(entidad.getFcesFechaActaGrado());
		frmCrpCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> dato = null;
		dato = new HashMap<String, Object>();
		frmCrpParametros.put("carrera", entidad.getCrrDescripcion());
		frmCrpParametros.put("modalidad", entidad.getMdlDescripcion());
		frmCrpParametros.put("folio", entidad.getFcesNumActaGrado());
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance()
				.getExternalContext().getRealPath("/"));
		pathGeneralReportes.append("/titulacionPosgrado/reportes/archivosJasper/actasGrado");
		frmCrpParametros.put("imagenCabecera", pathGeneralReportes+"/cabeceraActa.png");
		frmCrpParametros.put("imagenPie", pathGeneralReportes+"/pieActa.png");
		frmCrpParametros.put("imagenQR", entidad.getPrsUbicacionFoto());
		
		
		//texto que requiere construir de acuerdo a los datos de postulante
		StringBuilder texto = new StringBuilder(); 
		texto.append("En ");
		if(entidad.getUbcDescripcion().equals("Quito")){
			texto.append(" Quito D.M., ");	
		}else{
			texto.append(entidad.getUbcDescripcion());texto.append(",");
		}
		texto.append(" hoy ");texto.append(fecha);
		texto.append(" a las ");texto.append(entidad.getFcesHoraActaGrado().replaceAll("H", ":"));texto.append("h");
		texto.append(", se instala el Tribunal de Grado");
//		if(agfEstudianteActaGradoJdbcDtoEditar.getListaDocentesTribunal().get(0).getPrsSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
//			texto.append("el señor ");
//		}else if(agfEstudianteActaGradoJdbcDtoEditar.getListaDocentesTribunal().get(0).getPrsSexo()==PersonaConstantes.SEXO_MUJER_VALUE){
//			texto.append("la señora/ita ");
//		}
		
		
		texto.append(" integrado por los/as señores/as docentes: ");
		texto.append(presidenteTribunal.toString());
		texto.append(" y ");
		texto.append(miembro1Tribunal.toString());
		if(entidad.getAsnoDfnOral3()!=GeneralesConstantes.APP_ID_BASE.floatValue()){
			texto.append(" , ");
			texto.append(miembro2Tribunal.toString());	
		}	
		texto.append(" , para la sustentación ");
		if(entidad.getMcttcrOpcion()==MecanismoTitulacionCarreraConstantes.SELECCION_DEFENSA_ESCRITA_VALUE){
			texto.append("escrita ");
		}else{
			texto.append("oral ");
		}
		texto.append("del trabajo de titulación ");
		if(entidad.getPrsSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
			texto.append( "del señor ");
		}else if(entidad.getPrsSexo()==PersonaConstantes.SEXO_MUJER_VALUE){
			texto.append( "de la señorita ");
		}else{
			texto.append( "de ");
		}
		texto.append(entidad.getPrsNombres()+" "+entidad.getPrsPrimerApellido()+" "+entidad.getPrsSegundoApellido());
		if(!entidad.getUbcGentilicio().equals("N/A")){
			texto.append(" de nacionalidad ");
			texto.append(entidad.getUbcGentilicio());
		}
		texto.append(" con identificación N.° ");texto.append(entidad.getPrsIdentificacion());
		texto.append(" de conformidad con el Reglamento de Régimen Académico y el mecanismo de titulación ");
		texto.append(entidad.getMcttdescripcion());
		frmCrpParametros.put("texto", texto.toString());
//		System.out.println(entidad.getAsttTemaTrabajo());
//		String temaFinal =  entidad.getAsttTemaTrabajo().replace("æ", "\u00E6");
//		String temaFinal2 =  temaFinal.replace("ɑ", "\u03B1");
//		String temaFinal3 =  temaFinal2.replace("ə", "\u01DD");
//		String temaFinal4 =  temaFinal3.replace("ε", "\u025B");
//		String temaFinal5 =  temaFinal4.replace("ɪ", "\u026A");
//		String temaFinal6 =  temaFinal5.replace("ɔ", "\u0254");
//		String temaFinal7 =  temaFinal6.replace("ʊ", "\u028A");
//		System.out.println(temaFinal7);
		if(entidad.getPrsIdentificacion().equals("1720038940")){
			/*
	           Because font metrics is based on a graphics context, we need to create
	           a small, temporary image so we can ascertain the width and height
	           of the final image
	         */
	        int width = 2600;
	        int height = 300;

	        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	        Graphics2D g2d = img.createGraphics();
	        Font font = new Font("Arial", Font.ITALIC, 48);
	        g2d.setFont(font);
	        FontMetrics fm = g2d.getFontMetrics();

	        g2d.dispose();

	        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	        g2d = img.createGraphics();
	        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
	        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
	        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
	        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
	        g2d.setFont(font);
	        //fm = g2d.getFontMetrics();
	        g2d.setColor(Color.BLACK);
//	        String text = entidad.getAsttTemaTrabajo();

//	        BufferedReader br=null;
	        int nextLinePosition=100;
	        int fontSize = 48;
	        try {
//	            br = new BufferedReader(new FileReader(file));
//
//	            String line;
//	            while ((line = br.readLine()) != null) {
//	               g2d.drawString(line, 0, nextLinePosition);
//	               nextLinePosition = nextLinePosition + fontSize;
//	            }
	            int contadorCaracteres = 0;
	            StringBuilder sb1 = new StringBuilder();
	            for (char ch: entidad.getAsttTemaTrabajo().toCharArray()) {
	            	sb1.append(ch);
//	            	System.out.println(ch);
	            	if(contadorCaracteres==101){
	            		g2d.drawString(sb1.toString(), 0, nextLinePosition);
	            		nextLinePosition = nextLinePosition + fontSize;
	            		sb1 = new StringBuilder();
//	            		System.out.println(sb1);
	            		contadorCaracteres=0;
	            	}
	            	contadorCaracteres++;
	            }
	            g2d.drawString(sb1.toString(), 0, nextLinePosition);
        		nextLinePosition = nextLinePosition + fontSize;
//	        br.close();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        } 


	        g2d.dispose();
	        try {
	            ImageIO.write(img, "png", new File("d://imagenTema.png"));
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
	        
			
			frmCrpParametros.put("imagenTema","d://imagenTema.png");
		}else{
			frmCrpParametros.put("tema", entidad.getAsttTemaTrabajo()+". Con la tutoría de "+entidad.getAsttTutor()+".");	
		}
		if(entidad.getPrsSegundoApellido()!=null){
			frmCrpParametros.put("postulante", entidad.getPrsNombres()+" "+entidad.getPrsPrimerApellido()+" "+entidad.getPrsSegundoApellido());	
		}else{
			frmCrpParametros.put("postulante", entidad.getPrsNombres()+" "+entidad.getPrsPrimerApellido());
		}
		
		// Nota record Académico 
		StringBuilder recordAcademico = new StringBuilder();
		
		String nota=entidad.getFcesNotaPromAcumulado().toString();
		try {
			Integer.parseInt(nota);
			nota=nota.concat(".00");
		} catch (Exception e) {
			// TODO: handle exception
		}
		String[] partesNotas =  nota.split("\\.");
//		recordAcademico.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[0],true));
//		recordAcademico.append(" PUNTO ");
//		if(Integer.valueOf(partesNotas[1])<10 && Integer.valueOf(partesNotas[1])!=0) recordAcademico.append("CERO ");
//		recordAcademico.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[1],true));
//		frmCrpParametros.put("recordAcademico",recordAcademico.toString());
		
		recordAcademico.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[0],true));
		recordAcademico.append(" PUNTO ");
		try {
			if(Integer.valueOf(partesNotas[1])<10 && Integer.valueOf(partesNotas[1])!=0) recordAcademico.append("CERO ");	
		} catch (Exception e) {
			recordAcademico.append("CERO ");
		}
		try {
			recordAcademico.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[1],true));
		} catch (Exception e) {
		}
		// Nota trabajo titulación
		StringBuilder trabajoTitulacion = new StringBuilder();
		String notaTitulacion=entidad.getFcesNotaTrabTitulacion().toString();
		partesNotas = notaTitulacion.split("\\.");
		trabajoTitulacion.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[0],true));
		trabajoTitulacion.append(" PUNTO ");
//		System.out.println(partesNotas[1]);
		if(Integer.valueOf(partesNotas[1])<10 && Integer.valueOf(partesNotas[1])!=0 && (String.valueOf(partesNotas[1]).length()>1)) trabajoTitulacion.append("CERO ");
		trabajoTitulacion.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[1],true));
		frmCrpParametros.put("trabajoTitulacion",trabajoTitulacion.toString());
		// Nota promedio
		BigDecimal notaFinal = new BigDecimal(0.0);
//		if(entidad.getCrrId()==CarreraConstantes.CARRERA_MEDICINA_VALUE
//				|| entidad.getCrrId()==CarreraConstantes.CARRERA_ODONTOLOGIA_VALUE
//				|| entidad.getCrrId()==CarreraConstantes.CARRERA_OBSTETRICIA_VALUE
//				|| entidad.getCrrId()==CarreraConstantes.CARRERA_ENFERMERIA_VALUE
//				|| entidad.getCrrId()==CarreraConstantes.CARRERA_ENFERMERIA_NO_VIGENTE_TECNICO_VALUE
//				|| entidad.getCrrId()==CarreraConstantes.CARRERA_ENFERMERIA_NO_VIGENTE_TERCER_NIVEL_VALUE){
			StringBuilder promedioFinal = new StringBuilder();
			notaFinal=(calculoFinal);
			partesNotas = notaFinal.toString().split("\\.");
			promedioFinal.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[0].toString(),true));
			promedioFinal.append(" PUNTO ");
			if(Integer.valueOf(partesNotas[1])<10 && Integer.valueOf(partesNotas[1])!=0) promedioFinal.append("CERO ");
			promedioFinal.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[1].toString(),true));
			frmCrpParametros.put("promedioFinal",promedioFinal.toString());
			frmCrpParametros.put("notaPromedio",notaFinal.toString());
			try {
				servFichaEstudianteServicio.guardarNotaFinalGraduacion(entidad.getFcesId(), notaFinal, entidad.getTrttId());
			} catch (FichaEstudianteNoEncontradoException e1) {
			} catch (FichaEstudianteException e1) {
				FacesUtil.mensajeError(e1.getMessage());
			}
//		}else{
//			StringBuilder promedioFinal = new StringBuilder();
//			notaFinal=(entidad.getFcesNotaPromAcumulado().add(entidad.getFcesNotaTrabTitulacion())).divide(new BigDecimal(2.0), 0, RoundingMode.HALF_UP);
//			promedioFinal.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(notaFinal.toString(),true));
//			promedioFinal.append(" PUNTO CERO");
//			frmCrpParametros.put("promedioFinal",promedioFinal.toString());
//			frmCrpParametros.put("notaPromedio",notaFinal.toString()+".00");
//		}
		try {
			servFichaEstudianteServicio.guardarNotaFinalGraduacion(entidad.getFcesId(), notaFinal,entidad.getTrttId());
		} catch (FichaEstudianteNoEncontradoException e1) {
		} catch (FichaEstudianteException e1) {
			FacesUtil.mensajeError(e1.getMessage());
		}
		frmCrpParametros.put("notaRecord",nota);
		frmCrpParametros.put("notaTrabajo",notaTitulacion);
		int res= notaFinal.compareTo(new BigDecimal(10));
		String resultado=null;
		if(res==0 || res==1){
			resultado="EXCELENTE";
		}else {
			int res1= notaFinal.compareTo(new BigDecimal(9));
			if(res1==1 || res1==0){
				resultado="MUY BUENO";
			}else{
				int res2= notaFinal.compareTo(new BigDecimal(7));
				if(res2==1 || res2==0){
					resultado="BUENO";
				}else {
					int res3= notaFinal.compareTo(new BigDecimal(4));
					if(res3==1 || res3==0){
						resultado="REGULAR";
					}else{
						resultado="DEFICIENTE";
					}
				}
			}
		}
		if(entidad.getFcesSecretario().equals(GeneralesConstantes.APP_ID_BASE.toString())){
			try {
				List<AutoridadDto> listaAutoridades = new ArrayList<AutoridadDto>();
				listaAutoridades=servAutoridadDtoServicioJdbc.buscarAutoridades(entidad.getCrrId());
				StringBuilder autoridadSecretario = new StringBuilder();
				autoridadSecretario.append(listaAutoridades.get(2).getAtrNombres());autoridadSecretario.append(" ");
				autoridadSecretario.append(listaAutoridades.get(2).getAtrPrimerApellido());autoridadSecretario.append(" ");
				if(!listaAutoridades.get(2).getAtrSegundoApellido().equals(GeneralesConstantes.APP_ID_BASE.toString())){
					autoridadSecretario.append(listaAutoridades.get(2).getAtrSegundoApellido());
				}
				
				frmCrpParametros.put("secretario",autoridadSecretario.toString());
				if(listaAutoridades.get(2).getAtrSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
//					frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con el infrascrito Secretario Abogado que certifica. ");
					frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firma la presente acta el infrascrito Secretario Abogado que certifica. ");
					frmCrpParametros.put("sexoSecretario","Secretario Abogado");
				}else{
//					frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con la infrascrita Secretaria Abogada que certifica. ");
					frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firma la presente acta la infrascrita Secretaria Abogada que certifica. ");
					
					frmCrpParametros.put("sexoSecretario","Secretaria Abogada");
				}
				servFichaEstudianteServicio.guardarAutoridad(entidad.getFcesId(), autoridadSecretario.toString(), listaAutoridades.get(2).getAtrSexo(), AutoridadConstantes.SECRETARIO_FACULTAD_VALUE);
			}  catch (AutoridadNoEncontradoException e) {
			} catch (AutoridadException e) {
			}catch (Exception e){
				e.printStackTrace();
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Error al buscar autoridades de la facultad, por favor comuníquese con la Dirección de Tecnologías");	
			}
		}else{
			try {
				frmCrpParametros.put("secretario",entidad.getFcesSecretario());
				if(entidad.getFcesSecretarioSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
					frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con el infrascrito Secretario Abogado que certifica. ");
					frmCrpParametros.put("sexoSecretario","Secretario Abogado");
				}else{
					frmCrpParametros.put("textoSecretario","Para constancia de lo actuado, firman la presente acta las autoridades de la Facultad, conjuntamente con la infrascrita Secretaria Abogada que certifica. ");
					frmCrpParametros.put("sexoSecretario","Secretaria Abogada");
				}
			}catch (Exception e){
				e.printStackTrace();
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Error al buscar autoridades de la facultad, por favor comuníquese con la Dirección de Tecnologías");	
			}
		}
		frmCrpParametros.put("equivalencia",resultado);
		frmCrpParametros.put("titulo",entidad.getTtlDescripcion());
		frmCrpParametros.put("presidente",presidenteTribunal.toString());
		frmCrpParametros.put("miembro1",miembro1Tribunal.toString());
		if(agfEstudianteActaGradoJdbcDtoEditar.getAsnoDfnOral3()==GeneralesConstantes.APP_ID_BASE.floatValue()){
			frmCrpParametros.put("miembro2"," ");
		}else{
			frmCrpParametros.put("miembro2",miembro2Tribunal.toString());
		}
		frmCrpCampos.add(dato);
		// Establecemos en el atributo de la sesión la lista de mapas de
		// 	datos frmCrpCampos y parámetros frmCrpParametros
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmCrpCampos);
		httpSession.setAttribute("frmCargaNombreReporte",frmCrpNombreReporte);
		httpSession.setAttribute("frmCargaParametros",frmCrpParametros);
		// ******************FIN DE GENERACION DE REPORTE ************//
		agfPersonaPostulante = null;
		return "irInicio";
	}
	
	/**
	* Genera el acta para los graduados por otros mecanismos y lo establece en la sesión como atributo
	* @param List<RegistradosDto> frmRfRegistradosDto
	* @return String
	*/
	public String generarActaOtrosMecanismosEconomia(EstudianteEmisionActaJdbcDto entidad){
		 List<Map<String, Object>> frmCrpCampos = null;
		 Map<String, Object> frmCrpParametros = null;
		 String frmCrpNombreReporte = null;
		// ****************************************************************//
		// ***************** GENERACION DEL ACTA DE GRADO  ****************//
		// ****************************************************************//
		// ****************************************************************//
		frmCrpNombreReporte = "ActaDeGradoOtrosMecanismos"+"_"+entidad.getPrsPrimerApellido()+"_"+entidad.getPrsSegundoApellido()+"_"+entidad.getPrsNombres();
		frmCrpParametros = new HashMap<String, Object>();
		frmCrpParametros.put("usuario",agfUsuarioQueGenera.getUsrNick());
		StringBuilder sb = new StringBuilder();
		sb.append("En tal virtud, la <style isBold=\"true\">");
		switch (entidad.getFclId()) {
		case 18:
			frmCrpParametros.put("tipoSede", "Sede");
			frmCrpParametros.put("facultad",entidad.getFclDescripcion());
			sb.append(entidad.getFclDescripcion());;
			break;
		case 19:
			frmCrpParametros.put("tipoSede", "Sede");
			frmCrpParametros.put("facultad",entidad.getFclDescripcion());
			sb.append(entidad.getFclDescripcion());
			break;
		case 20:
			frmCrpParametros.put("tipoSede", "Sede");
			frmCrpParametros.put("facultad",entidad.getFclDescripcion());
			sb.append(entidad.getFclDescripcion());
			break;
		default:
			frmCrpParametros.put("tipoSede", "Facultad");
			frmCrpParametros.put("facultad",entidad.getFclDescripcion());
			sb.append("FACULTAD DE ");
			sb.append(entidad.getFclDescripcion());
			break;
		}
		sb.append("</style> de la <style isBold=\"true\">UNIVERSIDAD CENTRAL DEL ECUADOR</style> en su nombre y por autoridad de la ley");
		
		frmCrpParametros.put("facultadCadena",sb.toString());
		SimpleDateFormat formateador = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("ES"));
		String fecha=formateador.format(entidad.getFcesFechaActaGrado());
		frmCrpCampos = new ArrayList<Map<String, Object>>();
		Map<String, Object> dato = null;
		dato = new HashMap<String, Object>();
		frmCrpParametros.put("carrera", entidad.getCrrDescripcion());
		frmCrpParametros.put("modalidad", entidad.getMdlDescripcion());
		frmCrpParametros.put("folio", entidad.getFcesNumActaGrado());
		StringBuilder pathGeneralReportes = new StringBuilder();
		pathGeneralReportes.append(FacesContext.getCurrentInstance()
				.getExternalContext().getRealPath("/"));
		pathGeneralReportes.append("/titulacion/reportes/archivosJasper/actasGrado");
		frmCrpParametros.put("imagenCabecera", pathGeneralReportes+"/cabeceraActa.png");
		frmCrpParametros.put("imagenPie", pathGeneralReportes+"/pieActa.png");
		
		//texto que requiere construir de acuerdo a los datos de postulante
		StringBuilder texto = new StringBuilder(); 
		texto.append("En ");
		if(entidad.getUbcDescripcion().equals("QUITO")){
			texto.append(" Quito D.M., ");	
		}else{
			texto.append(entidad.getUbcDescripcion());texto.append(",");
		}
		texto.append(" hoy ");texto.append(fecha);
		texto.append(", una vez que ");
		if(agfEstudianteActaGradoJdbcDtoEditar.getListaDocentesTribunal().get(0).getPrsSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
			texto.append("el tutor del proyecto de investigación, señor ");
		}else if(agfEstudianteActaGradoJdbcDtoEditar.getListaDocentesTribunal().get(0).getPrsSexo()==PersonaConstantes.SEXO_MUJER_VALUE){
			texto.append("la tutora del proyecto de investigación, señora/ita ");
		}
		texto.append(presidenteTribunal.toString());
		texto.append(" y los/as docentes lectores/as ciegos/as: ");
		texto.append(miembro1Tribunal.toString());
		texto.append(" y ");
		texto.append(miembro2Tribunal.toString());	
		texto.append(" , han presentado sus calificaciones que certifican la aprobación del proyecto de investigación; ");
		texto.append(" a las ");texto.append(entidad.getFcesHoraActaGrado());
		texto.append(" se instala la presentación pública ");
		if(entidad.getPrsSexo()==PersonaConstantes.SEXO_HOMBRE_VALUE){
			texto.append( "del señor ");
		}else if(entidad.getPrsSexo()==PersonaConstantes.SEXO_MUJER_VALUE){
			texto.append( "de la señorita ");
		}else{
			texto.append( "de ");
		}
		texto.append(entidad.getPrsNombres()+" "+entidad.getPrsPrimerApellido()+" "+entidad.getPrsSegundoApellido());
		if(!entidad.getUbcGentilicio().equals("N/A")){
			texto.append(" de nacionalidad ");
			texto.append(entidad.getUbcGentilicio());
		}
		texto.append(" con identificación N.° ");texto.append(entidad.getPrsIdentificacion());
		texto.append(" de conformidad con el Reglamento de Régimen Académico vigente e instructivo para ");
		texto.append(entidad.getMcttdescripcion());
		frmCrpParametros.put("texto", texto.toString());
		frmCrpParametros.put("tema", entidad.getAsttTemaTrabajo());
		frmCrpParametros.put("postulante", entidad.getPrsNombres()+" "+entidad.getPrsPrimerApellido()+" "+entidad.getPrsSegundoApellido());
		// Nota record Académico 
		StringBuilder recordAcademico = new StringBuilder();
		String nota=entidad.getFcesNotaPromAcumulado().toString();
		String[] partesNotas =  nota.split("\\.");
		recordAcademico.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[0],true));
		recordAcademico.append(" PUNTO ");
		if(Integer.valueOf(partesNotas[1])<10 && Integer.valueOf(partesNotas[1])!=0) recordAcademico.append("CERO ");
		recordAcademico.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[1],true));
		frmCrpParametros.put("recordAcademico",recordAcademico.toString());
		// Nota trabajo titulación
		StringBuilder trabajoTitulacion = new StringBuilder();
		String notaTitulacion=entidad.getFcesNotaTrabTitulacion().toString();
		partesNotas = notaTitulacion.split("\\.");
		trabajoTitulacion.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[0],true));
		trabajoTitulacion.append(" PUNTO ");
		if(Integer.valueOf(partesNotas[1])<10 && Integer.valueOf(partesNotas[1])!=0) recordAcademico.append("CERO ");
		trabajoTitulacion.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(partesNotas[1],true));
		frmCrpParametros.put("trabajoTitulacion",trabajoTitulacion.toString());
		// Nota promedio
		StringBuilder promedioFinal = new StringBuilder();
		BigDecimal notaFinal=(entidad.getFcesNotaPromAcumulado().add(entidad.getFcesNotaTrabTitulacion())).divide(new BigDecimal(2.0), 2, RoundingMode.HALF_UP);
		promedioFinal.append(GeneralesUtilidades.convertirNumeroALetrasSinDecimales(notaFinal.toString(),true));
		promedioFinal.append(" PUNTO CERO");
		frmCrpParametros.put("promedioFinal",promedioFinal.toString());
		try {
			servFichaEstudianteServicio.guardarNotaFinalGraduacion(entidad.getFcesId(), notaFinal,entidad.getTrttId());
		} catch (FichaEstudianteNoEncontradoException e1) {
		} catch (FichaEstudianteException e1) {
			FacesUtil.mensajeError(e1.getMessage());
		}
		frmCrpParametros.put("notaRecord",nota);
		frmCrpParametros.put("notaTrabajo",notaTitulacion);
		frmCrpParametros.put("notaPromedio",notaFinal.toString()+".00");
		int res= notaFinal.compareTo(new BigDecimal(19.50));
		String resultado=null;
		if(res==0 || res==1){
			resultado="EXCELENTE";
		}else {
			int res1= notaFinal.compareTo(new BigDecimal(17.50));
			if(res1==1 || res1==0){
				resultado="MUY BUENO";
			}else{
				int res2= notaFinal.compareTo(new BigDecimal(15.50));
				if(res2==1 || res2==0){
					resultado="BUENO";
				}else {
					int res3= notaFinal.compareTo(new BigDecimal(14.00));
					if(res3==1 || res3==0){
						resultado="REGULAR";
					}else{
						resultado="DEFICIENTE";
					}
				}
			}
		}
		try {
			List<AutoridadDto> listaAutoridades = new ArrayList<AutoridadDto>();
			listaAutoridades=servAutoridadDtoServicioJdbc.buscarAutoridades(entidad.getCrrId());
			StringBuilder autoridadSecretario = new StringBuilder();
			autoridadSecretario.append(listaAutoridades.get(2).getAtrNombres());autoridadSecretario.append(" ");
			autoridadSecretario.append(listaAutoridades.get(2).getAtrPrimerApellido());autoridadSecretario.append(" ");
			if(!listaAutoridades.get(2).getAtrSegundoApellido().equals(GeneralesConstantes.APP_ID_BASE.toString())){
				autoridadSecretario.append(listaAutoridades.get(2).getAtrSegundoApellido());
			}
			
			frmCrpParametros.put("secretario",autoridadSecretario.toString());
			
			
			}  catch (AutoridadNoEncontradoException e) {
			} catch (AutoridadException e) {
			}catch (Exception e){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("Error al buscar autoridades de la facultad, por favor comuníquese con la Dirección de Tecnologías");	
			}
		frmCrpParametros.put("equivalencia",resultado);
		frmCrpParametros.put("titulo",entidad.getTtlDescripcion());
		frmCrpParametros.put("presidente",presidenteTribunal.toString());
		frmCrpParametros.put("miembro1",miembro1Tribunal.toString());
		if(agfEstudianteActaGradoJdbcDtoEditar.getAsnoDfnOral3()==GeneralesConstantes.APP_ID_BASE.floatValue()){
			frmCrpParametros.put("miembro2"," ");
		}else{
			frmCrpParametros.put("miembro2",miembro2Tribunal.toString());
		}
		frmCrpCampos.add(dato);
		// Establecemos en el atributo de la sesión la lista de mapas de
		// 	datos frmCrpCampos y parámetros frmCrpParametros
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute("frmCargaCampos", frmCrpCampos);
		httpSession.setAttribute("frmCargaNombreReporte",frmCrpNombreReporte);
		httpSession.setAttribute("frmCargaParametros",frmCrpParametros);
		// ******************FIN DE GENERACION DE REPORTE ************//
		agfPersonaPostulante = null;
		return "irInicio";
	}
	
	
	public String irRegresarActaGrado(){
		agfListTitulosBachilleres = null;
		iagfEstadoComplexivo=true;
		limpiar();
		if(agftipoImpresion==0){
			return "irRegresarActaGrado";	
		}else{
			return "irRegresarActaGradoReImprimir";
		}
		
	}
	
	

	/** 
	 * Método que muestra el mensaje que direciona a la página de Reportes de Postulados para las secretarias
	 * @param usuario que realiza la petición de la página
	 * @return irReportesPostulados
	 */
	public String irReportesPostuladosSecretaria(Usuario usuario){
		iagfListCarreras=new ArrayList<CarreraDto>();
		return "irReportesPostuladosSecretaria";
	}
	
	/**
	 * Valida la fecha en el componente de primefaces que no sea mayor a la actual
	 * @return
	 */
	public Date getValidaFechaNoMayor() {
		SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
		Date fecha = null;
		try {
			fecha = formato.parse("19-02-2016");
		} catch (ParseException e) {
			fecha = new Date();
		}
		return fecha;
    }
	
	
	
	
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
			
	//iniciar parametros de busqueda
	private void iniciarParametros(){
		//INICIALIZO EL DTO DE ESTUDIANTE BUSCAR
		iagfCarrera=new CarreraDto();
		iagfConvocatoria = new Convocatoria();
		iagfEstudianteActaGradoDtoServicioJdbcDtoBuscar = new EstudianteEmisionActaJdbcDto();
		iagfListEstudiantesActasGrado = null;
		iagfListEstudiantesActasGrado= new ArrayList<EstudianteEmisionActaJdbcDto>();
		iagfEstadoComplexivo=true;//ingresa como complexivo
		miembro1Tribunal=null;
		miembro2Tribunal=null;
		presidenteTribunal=null;
	}
	
	
	public String verificarClickAceptarTramite(){
		
		iagfValidadorClic = 1;
		return null;
	}
	
	public String verificarClickAceptarTramiteNo(){
		iagfValidadorClic = 0;
		return null;
	}
	
	//****************************************************************/
	//******************* METODOS GETTER y SETTER ********************/
	//****************************************************************/
	

	public Convocatoria getIagfConvocatoria() {
		return iagfConvocatoria;
	}


	public void setIagfConvocatoria(Convocatoria iagfConvocatoria) {
		this.iagfConvocatoria = iagfConvocatoria;
	}


	public List<Convocatoria> getIagfListConvocatorias() {
		iagfListConvocatorias = iagfListConvocatorias==null?(new ArrayList<Convocatoria>()):iagfListConvocatorias;
		return iagfListConvocatorias;
	}


	public void setIagfListConvocatorias(List<Convocatoria> iagfListConvocatorias) {
		this.iagfListConvocatorias = iagfListConvocatorias;
	}


	public CarreraDto getIagfCarrera() {
		return iagfCarrera;
	}


	public void setIagfCarrera(CarreraDto iagfCarrera) {
		this.iagfCarrera = iagfCarrera;
	}


	public List<CarreraDto> getIagfListCarreras() {
		iagfListCarreras = iagfListCarreras==null?(new ArrayList<CarreraDto>()):iagfListCarreras;
		return iagfListCarreras;
	}


	public void setIagfListCarreras(List<CarreraDto> iagfListCarreras) {
		this.iagfListCarreras = iagfListCarreras;
	}


	public EstudianteEmisionActaJdbcDto getIagfEstudianteActaGradoDtoServicioJdbcDtoBuscar() {
		return iagfEstudianteActaGradoDtoServicioJdbcDtoBuscar;
	}


	public void setIagfEstudianteActaGradoDtoServicioJdbcDtoBuscar(
			EstudianteEmisionActaJdbcDto iagfEstudianteActaGradoDtoServicioJdbcDtoBuscar) {
		this.iagfEstudianteActaGradoDtoServicioJdbcDtoBuscar = iagfEstudianteActaGradoDtoServicioJdbcDtoBuscar;
	}


	public List<EstudianteEmisionActaJdbcDto> getIagfListEstudiantesActasGrado() {
		iagfListEstudiantesActasGrado = iagfListEstudiantesActasGrado==null?(new ArrayList<EstudianteEmisionActaJdbcDto>()):iagfListEstudiantesActasGrado;
		return iagfListEstudiantesActasGrado;
	}


	public void setIagfListEstudiantesActasGrado(
			List<EstudianteEmisionActaJdbcDto> iagfListEstudiantesActasGrado) {
		this.iagfListEstudiantesActasGrado = iagfListEstudiantesActasGrado;
	}


	public EstudianteEmisionActaJdbcDto getAgfEstudianteActaGradoJdbcDtoEditar() {
		return agfEstudianteActaGradoJdbcDtoEditar;
	}


	public void setAgfEstudianteActaGradoJdbcDtoEditar(
			EstudianteEmisionActaJdbcDto agfEstudianteActaGradoJdbcDtoEditar) {
		this.agfEstudianteActaGradoJdbcDtoEditar = agfEstudianteActaGradoJdbcDtoEditar;
	}


	public String getFechaNacimientoLabel() {
		return fechaNacimientoLabel;
	}


	public void setFechaNacimientoLabel(String fechaNacimientoLabel) {
		this.fechaNacimientoLabel = fechaNacimientoLabel;
	}


	public List<TituloDto> getAgfListTitulosBachilleres() {
		return agfListTitulosBachilleres;
	}


	public void setAgfListTitulosBachilleres(
			List<TituloDto> agfListTitulosBachilleres) {
		this.agfListTitulosBachilleres = agfListTitulosBachilleres;
	}


	public List<InstitucionAcademicaDto> getAgfListUniversidades() {
		return agfListUniversidades;
	}


	public void setAgfListUniversidades(
			List<InstitucionAcademicaDto> agfListUniversidades) {
		this.agfListUniversidades = agfListUniversidades;
	}


	public Boolean getIagfEstadoComplexivo() {
		return iagfEstadoComplexivo;
	}


	public void setIagfEstadoComplexivo(Boolean iagfEstadoComplexivo) {
		this.iagfEstadoComplexivo = iagfEstadoComplexivo;
	}


	public Usuario getIagfUsuarioQueCambia() {
		return iagfUsuarioQueCambia;
	}


	public void setIagfUsuarioQueCambia(Usuario iagfUsuarioQueCambia) {
		this.iagfUsuarioQueCambia = iagfUsuarioQueCambia;
	}


	public Persona getAgfPersonaPostulante() {
		return agfPersonaPostulante;
	}


	public void setAgfPersonaPostulante(Persona agfPersonaPostulante) {
		this.agfPersonaPostulante = agfPersonaPostulante;
	}


	public ConfiguracionCarreraDto getAgfConfiguracionCarrera() {
		return agfConfiguracionCarrera;
	}


	public void setAgfConfiguracionCarrera(
			ConfiguracionCarreraDto agfConfiguracionCarrera) {
		this.agfConfiguracionCarrera = agfConfiguracionCarrera;
	}


	public List<ConfiguracionCarreraDto> getAgfListConfiguracionesCarrera() {
		return agfListConfiguracionesCarrera;
	}


	public void setAgfListConfiguracionesCarrera(
			List<ConfiguracionCarreraDto> agfListConfiguracionesCarrera) {
		this.agfListConfiguracionesCarrera = agfListConfiguracionesCarrera;
	}


	public int getHabilitarCampos() {
		return habilitarCampos;
	}


	public void setHabilitarCampos(int habilitarCampos) {
		this.habilitarCampos = habilitarCampos;
	}


	public Integer getIagfValidadorClic() {
		return iagfValidadorClic;
	}


	public void setIagfValidadorClic(Integer iagfValidadorClic) {
		this.iagfValidadorClic = iagfValidadorClic;
	}


	public Boolean getIagfDeshabilitadoExportar() {
		return iagfDeshabilitadoExportar;
	}


	public void setIagfDeshabilitadoExportar(Boolean iagfDeshabilitadoExportar) {
		this.iagfDeshabilitadoExportar = iagfDeshabilitadoExportar;
	}

	public StringBuilder getPresidenteTribunal() {
		return presidenteTribunal;
	}


	public void setPresidenteTribunal(StringBuilder presidenteTribunal) {
		this.presidenteTribunal = presidenteTribunal;
	}


	public StringBuilder getMiembro1Tribunal() {
		return miembro1Tribunal;
	}


	public void setMiembro1Tribunal(StringBuilder miembro1Tribunal) {
		this.miembro1Tribunal = miembro1Tribunal;
	}


	public StringBuilder getMiembro2Tribunal() {
		return miembro2Tribunal;
	}


	public void setMiembro2Tribunal(StringBuilder miembro2Tribunal) {
		this.miembro2Tribunal = miembro2Tribunal;
	}

	public BigDecimal getCalculoFinal() {
		return calculoFinal;
	}

	public void setCalculoFinal(BigDecimal calculoFinal) {
		this.calculoFinal = calculoFinal;
	}

	public Etnia getAgfEtnia() {
		return agfEtnia;
	}

	public void setAgfEtnia(Etnia agfEtnia) {
		this.agfEtnia = agfEtnia;
	}

	public UbicacionDto getAgfnacionalidad() {
		return agfnacionalidad;
	}

	public void setAgfnacionalidad(UbicacionDto agfnacionalidad) {
		this.agfnacionalidad = agfnacionalidad;
	}

	public Titulo getAgfTitulo() {
		return agfTitulo;
	}

	public void setAgfTitulo(Titulo agfTitulo) {
		this.agfTitulo = agfTitulo;
	}

	public InstitucionAcademica getAgfInstitucionAcademica() {
		return agfInstitucionAcademica;
	}

	public void setAgfInstitucionAcademica(
			InstitucionAcademica agfInstitucionAcademica) {
		this.agfInstitucionAcademica = agfInstitucionAcademica;
	}

	public String getAgfTipoReconocimiento() {
		return agfTipoReconocimiento;
	}

	public void setAgfTipoReconocimiento(String agfTipoReconocimiento) {
		this.agfTipoReconocimiento = agfTipoReconocimiento;
	}

	public Usuario getAgfUsuarioQueGenera() {
		return agfUsuarioQueGenera;
	}

	public void setAgfUsuarioQueGenera(Usuario agfUsuarioQueGenera) {
		this.agfUsuarioQueGenera = agfUsuarioQueGenera;
	}

	public Integer getAgftipoImpresion() {
		return agftipoImpresion;
	}

	public void setAgftipoImpresion(Integer agftipoImpresion) {
		this.agftipoImpresion = agftipoImpresion;
	}

	public String getIagfCadenaServlet() {
		return iagfCadenaServlet;
	}

	public void setIagfCadenaServlet(String iagfCadenaServlet) {
		this.iagfCadenaServlet = iagfCadenaServlet;
	}

	
}
