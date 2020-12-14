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
   
 ARCHIVO:     ReAsignacionMecanismoForm.java	  
 DESCRIPCION: Bean que maneja las peticiones de reasignación de mecanismo de evaluador.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
24-04-2018			Daniel Albuja                        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.managedBeans.session.evaluacion;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.CarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.DocenteTutorTribunalLectorJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteAsignacionMecanismoDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ConvocatoriaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.DocenteTutorTribunalLectorJdbcDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.DocenteTutorTribunalLectorJdbcDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteAsignacionDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteAsignacionDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteValidacionDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EstudianteValidacionDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.MecanismoTitulacionCarreraException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.MecanismoTitulacionCarreraNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RolFlujoCarreraException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RolFlujoCarreraNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.CarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.ConvocatoriaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.EstudianteValidacionDtoServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.FacultadServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.MecanismoTitulacionCarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.DocenteDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.EstudianteAsignacionDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.ReportesDtoJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.MecanismoTitulacionCarreraConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.ValidacionConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Convocatoria;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.MecanismoCarrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Persona;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Usuario;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.titulacionPosgrado.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) ReAsignacionMecanismoForm.
 * Managed Bean que maneja las peticiones de reasignación de mecanismo de evaluador.
 * @author dalbuja.
 * @version 1.0
 */

@ManagedBean(name="reAsignacionMecanismoForm")
@SessionScoped
public class ReAsignacionMecanismoForm implements Serializable{

	private static final long serialVersionUID = 8026853400702897855L;
	
		
	// *****************************************************************/
	// ******************* Variables de AsignacionMecanismoForm * ** ***/
	// *****************************************************************/
	
	private EstudianteAsignacionMecanismoDto ramfEstudianteAsignacionMecanismoDtoBuscar;
	private EstudianteAsignacionMecanismoDto ramfEstudianteAsignacionMecanismoDtoEditar;
	private CarreraDto ramfCarrera;
	private List<CarreraDto> ramfListCarreras;
	private Convocatoria ramfConvocatoria;
	private List<Convocatoria> ramfListConvocatorias;
	private Usuario ramfUsuarioQueCambia;
	private List<EstudianteAsignacionMecanismoDto> ramfListEstudianteAsignacionMecanismoDto;
	private Persona ramfPersonaPostulante;
	private List<MecanismoCarrera> ramfListMecanismoTitulacionCarrera;
	private MecanismoCarrera ramfMecanismoTitulacionCarrera;
	private List<DocenteTutorTribunalLectorJdbcDto> ramfListDocentesDto;
	private String ramfMensajeTutor;
	private String cedula;
	private String ramfApellidoDocenteBuscar;
	
	private int fechaRegimenPostulacion;
	private int habilitarCampos;
	private Boolean ramfHabilitadorTutor;
	private Integer ramfValidadorClic;
	private Integer ramfValidadorClicTutor;

	private Boolean ramfDeshabilitadoTutor;
	private Boolean ramfHabilitadorGuardar;
	
	private Integer ramfExamenComplexivoValue;
	
	
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
	EstudianteAsignacionDtoServicioJdbc servAmfEstudianteAsignacionDtoServicioJdbc;
	
	@EJB
	EstudianteValidacionDtoServicio servAmfEstudianteValidacionDtoServicio;
	
	
	@EJB
	private MecanismoTitulacionCarreraServicio servAmfMecanismoTitulacionCarreraServicio;
	
	@EJB
	private DocenteDtoServicioJdbc srvDocenteDtoServicioJdbc;

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
	 * Método que dirige a la página de ReAsignación
	 * @param usuario
	 * @return
	 */
	public String irListarReAsignacionMecanismos(Usuario usuario){
	//vericación de que el usuario no pertenezca al active directory 0.-si 1.-no
		try {
			UsuarioRol usro = srvUsuarioRolServicio.buscarPorIdentificacionEvaluador(usuario.getUsrIdentificacion());
			if(usro.getUsroEstado().intValue()==UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
				FacesUtil.mensajeInfo("Usuario se encuentra inactivo, por favor contáctese con soporte técnico");
				return null;
			}
			iniciarParametros();
			ramfListConvocatorias =  srvConvocatoriaServicio.listarTodos();
			// Buscamos las carreras asignadas al evaluador
			ramfListCarreras=servAmfEstudianteAsignacionDtoServicioJdbc.buscarCarrerasXEvaluador(usuario.getUsrIdentificacion());
		} catch (CarreraDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		} catch (SQLException e) {
		} catch (ConvocatoriaNoEncontradoException e) {
			FacesUtil.mensajeInfo("No se encontró ninguna convocatoria ");
		} catch (UsuarioRolNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (UsuarioRolException e) {
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}
		ramfUsuarioQueCambia = usuario;
		ramfValidadorClic = 0;
		ramfValidadorClicTutor = 0;
		return "irListarReAsignacionMecanismos";
	}
	
	
	/**
	 * Maneja de cancelacion de la reasignción
	 * @return - cadena de navegación a la página de inicio
	 */
	public String cancelarReAsignacion(){
		ramfPersonaPostulante = null;
		return "irInicio";

	}
	
	//****************************************************************/
	//******** METODOS DE LIMPIEZA DE VARIABLES **********************/
	//****************************************************************/
	/** Método que limpia la lista de carreras
	 * 
	 */
	public void cambiarMecanismoTitulacion(){
		MecanismoCarrera mcttcrAux;
		try {
			mcttcrAux = servAmfMecanismoTitulacionCarreraServicio.buscarPorId(ramfEstudianteAsignacionMecanismoDtoEditar.getFcesMecanismoTitulacionCarrera());
			if(mcttcrAux.getMccrMecanismoTitulacion().getMcttId() == MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_VALUE){
				ramfEstudianteAsignacionMecanismoDtoEditar.setAssttTutor(null);
				ramfEstudianteAsignacionMecanismoDtoEditar.setAssttTemaTrabajo(null);
				ramfHabilitadorGuardar=false;
				ramfHabilitadorTutor=true;
			}else{
				ramfHabilitadorTutor=false;
			}
		} catch (MecanismoTitulacionCarreraNoEncontradoException
				| MecanismoTitulacionCarreraException e) {
		}
	}
	
	
	/** Método que limpia los componentes del form
	 * 
	 */
	public void limpiar(){
		ramfListEstudianteAsignacionMecanismoDto=null;
//		ramfExamenComplexivoValue=GeneralesConstantes.APP_ID_BASE;
//		amfValidadorClic = 0;
		ramfValidadorClicTutor = 0;
		ramfEstudianteAsignacionMecanismoDtoBuscar = null;
		ramfListDocentesDto=null;
		cedula=null;
		ramfApellidoDocenteBuscar=null;
		iniciarParametros();
	}
	/** Método que limpia los componentes del form
	 * 
	 */
	public void limpiarModal(){
		
		ramfValidadorClicTutor = 0;
		
		ramfListDocentesDto=null;
		cedula=null;
		ramfApellidoDocenteBuscar=null;
		
	}
	
	/**
	 * Lista los estudiantes evaluados segun los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscarEvaluados(Usuario usuario){
		try {
				ramfListEstudianteAsignacionMecanismoDto = servAmfEstudianteAsignacionDtoServicioJdbc.buscarEstudiantesEvaluadosXIndetificacionXCarreraXConvocatoria(ramfEstudianteAsignacionMecanismoDtoBuscar.getPrsIdentificacion(), usuario.getUsrIdentificacion(),ramfCarrera , ramfConvocatoria.getCnvId());	
		} catch (EstudianteValidacionDtoException e) {
			FacesUtil.mensajeInfo(e.getMessage());
		} catch (EstudianteValidacionDtoNoEncontradoException e) {
			FacesUtil.mensajeError(e.getMessage());
		}
	}
	
	public String irCancelarReAsignacion(){
		limpiar();
		return "irCancelarReAsignacion";
	}

	public String irReAsignacionMecanismo(EstudianteAsignacionMecanismoDto estudiante, Usuario usuario){
		this.ramfEstudianteAsignacionMecanismoDtoEditar = estudiante;
		ramfUsuarioQueCambia=usuario;
		ramfValidadorClic = 0;
		try {
			ramfListMecanismoTitulacionCarrera = servAmfMecanismoTitulacionCarreraServicio.listaMccttXcarrera(estudiante.getTrttCarreraId());
			// Recorro la lista para encontrar el id del Examen Complexivo para esa carrera
			for (MecanismoCarrera item : ramfListMecanismoTitulacionCarrera) {
				if(item.getMccrMecanismoTitulacion().getMcttDescripcion().equals(ValidacionConstantes.EXAMEN_COMPLEXIVO_LABEL)){
					// Guardo el valor del id del mecanismo de examen complexivo
					ramfExamenComplexivoValue=item.getMccrId();
				}
			}
			if(estudiante.getTrttObsValSecSplit().equals(ValidacionConstantes.EXAMEN_COMPLEXIVO_IDONEO_LABEL)){
				Iterator<MecanismoCarrera> listaIterator = ramfListMecanismoTitulacionCarrera.iterator();
				listaIterator = ramfListMecanismoTitulacionCarrera.iterator();
				while(listaIterator.hasNext()){
					MecanismoCarrera elemento = listaIterator.next();
					if(!elemento.getMccrMecanismoTitulacion().getMcttDescripcion().equals(MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_LABEL)){
						listaIterator.remove();
					}
				}	
			}
			if(estudiante.getTrttObsValSecSplit().equals(ValidacionConstantes.ACTUALIZACION_OTROS_MECANISMOS_IDONEO_LABEL)){
				Iterator<MecanismoCarrera> listaIterator = ramfListMecanismoTitulacionCarrera.iterator();
				listaIterator = ramfListMecanismoTitulacionCarrera.iterator();
				while(listaIterator.hasNext()){
					MecanismoCarrera elemento = listaIterator.next();
					if(elemento.getMccrMecanismoTitulacion().getMcttDescripcion().equals(MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_LABEL)){
						listaIterator.remove();
					}
				}	
			}
			if(estudiante.getTrttObsValSecSplit().equals(ValidacionConstantes.OTROS_MECANISMOS_IDONEO_LABEL)){
				Iterator<MecanismoCarrera> listaIterator = ramfListMecanismoTitulacionCarrera.iterator();
				listaIterator = ramfListMecanismoTitulacionCarrera.iterator();
				while(listaIterator.hasNext()){
					MecanismoCarrera elemento = listaIterator.next();
					if(elemento.getMccrMecanismoTitulacion().getMcttDescripcion().equals(MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_LABEL)){
						listaIterator.remove();
					}
				}	
			}
		} catch (MecanismoTitulacionCarreraNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MecanismoTitulacion.buscar.todos.no.result.exception")));
		} catch (MecanismoTitulacionCarreraException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("MecanismoTitulacion.buscar.por.id.non.unique.result.exception")));
		}
		ramfEstudianteAsignacionMecanismoDtoEditar.setFcesMecanismoTitulacionCarrera(ramfEstudianteAsignacionMecanismoDtoEditar.getMcttcrId());
		MecanismoCarrera mcttcrAux;
		try {
			mcttcrAux = servAmfMecanismoTitulacionCarreraServicio.buscarPorId(ramfEstudianteAsignacionMecanismoDtoEditar.getFcesMecanismoTitulacionCarrera());
			if(mcttcrAux.getMccrMecanismoTitulacion().getMcttId() == MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_VALUE){
				ramfHabilitadorTutor=true;
			}else{
				ramfHabilitadorTutor=false;
			}
		} catch (MecanismoTitulacionCarreraNoEncontradoException
				| MecanismoTitulacionCarreraException e) {
		}
		
//		ramfEstudianteAsignacionMecanismoDtoEditar.setAssttTutor(null);
		return "irReAsignacionMecanismo";
	}
	
	public String guardar(EstudianteAsignacionMecanismoDto estudiante) {
		try {
				ramfMecanismoTitulacionCarrera = servAmfMecanismoTitulacionCarreraServicio.buscarPorId(ramfEstudianteAsignacionMecanismoDtoEditar.getFcesMecanismoTitulacionCarrera());
				RolFlujoCarrera roflcrr = srvRolFlujoCarreraServicio.buscarPorCarrera(estudiante.getTrttCarreraId(), ramfUsuarioQueCambia.getUsrId() , RolConstantes.ROL_BD_EVALUADOR_VALUE);
				// Comparo si el mecanismo seleccionado es Examen Complexivo
				if(ramfMecanismoTitulacionCarrera.getMccrId() == ramfExamenComplexivoValue){
					estudiante.setAssttTemaTrabajo(MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_LABEL);
					estudiante.setAssttTutor(MecanismoTitulacionCarreraConstantes.MECANISMO_EXAMEN__COMPLEXIVO_LABEL);
					servAmfEstudianteAsignacionDtoServicioJdbc.editarReasignar(estudiante, ramfUsuarioQueCambia.getUsrIdentificacion() , roflcrr , ramfMecanismoTitulacionCarrera);
				}else{
					if(estudiante.getAssttTutor()!=null && estudiante.getAssttTemaTrabajo()!=null){
						servAmfEstudianteAsignacionDtoServicioJdbc.editarReasignar(estudiante, ramfUsuarioQueCambia.getUsrIdentificacion() , roflcrr , ramfMecanismoTitulacionCarrera);
					}else{
						FacesUtil.limpiarMensaje();
						FacesUtil.mensajeError("Por favor, llene toda la información requerida para otros mecanismos.");
						ramfValidadorClic = 0;
						ramfValidadorClicTutor = 0;
						return null;
					}
				}
				iniciarParametros();
				ramfValidadorClic = null;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReAsignacion.guardar.exitoso")));
				return "irListarReAsignacionMecanismos";
		} catch (RolFlujoCarreraNoEncontradoException e) {
			e.printStackTrace();
			FacesUtil.mensajeError(e.getMessage());
		} catch (RolFlujoCarreraException e) {
			e.printStackTrace();
			FacesUtil.mensajeError(e.getMessage());
		} catch (EstudianteAsignacionDtoNoEncontradoException
				| EstudianteAsignacionDtoException e) {
			e.printStackTrace();
			FacesUtil.mensajeError(e.getMessage());
		} catch (MecanismoTitulacionCarreraNoEncontradoException e) {
			e.printStackTrace();
			FacesUtil.mensajeError(e.getMessage());
		} catch (MecanismoTitulacionCarreraException e) {
			e.printStackTrace();
			FacesUtil.mensajeError(e.getMessage());
		}
		ramfValidadorClic = 0;
		ramfValidadorClicTutor = 0;
		return null;
	}
	
	/**
	 * Lista los docentes segun los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscarDocentes(Usuario usuario, String cedula){
		ramfHabilitadorGuardar=true;
		try {
			ramfListDocentesDto=srvDocenteDtoServicioJdbc.buscarDocenteXIdentificacionTitulacion(cedula,ramfApellidoDocenteBuscar);
			
			if(ramfListDocentesDto.size()>0){
				ramfMensajeTutor="No hay resultados con los parámetros seleccionados";
			}
			cedula=null;
			ramfApellidoDocenteBuscar=null;
		} catch (DocenteTutorTribunalLectorJdbcDtoException e) {
			ramfListDocentesDto = null;
			ramfMensajeTutor=(e.getMessage());
		} catch (DocenteTutorTribunalLectorJdbcDtoNoEncontradoException e) {
			ramfListDocentesDto = null;
			ramfMensajeTutor=(e.getMessage());
		}
	}
	
	
	
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
			
	//iniciar parametros de busqueda
	private void iniciarParametros(){
		//INICIALIZO EL DTO DE ESTUDIANTE BUSCAR
		ramfCarrera=new CarreraDto();
		ramfConvocatoria = new Convocatoria();
//		amfValidadorClic = 0;
		ramfHabilitadorTutor=false;
		ramfEstudianteAsignacionMecanismoDtoBuscar = new EstudianteAsignacionMecanismoDto();
		ramfListEstudianteAsignacionMecanismoDto = null;
		ramfListEstudianteAsignacionMecanismoDto= new ArrayList<EstudianteAsignacionMecanismoDto>();
		ramfEstudianteAsignacionMecanismoDtoEditar = new EstudianteAsignacionMecanismoDto();
		ramfListDocentesDto = new ArrayList<DocenteTutorTribunalLectorJdbcDto>();
	}
	
	/**
	 * asigna el docente a la variable que se mostrará en el JSF 
	 */
	public void asignarDocente(DocenteTutorTribunalLectorJdbcDto item, Usuario usuario){
		ramfListDocentesDto = null;
		ramfHabilitadorGuardar=false;
		ramfEstudianteAsignacionMecanismoDtoEditar.setAssttTutor(item.getPrsNombres()+" "+item.getPrsPrimerApellido()+" "+item.getPrsSegundoApellido());
		verificarClickAsignarTutorNo();
	}

	public void edicionTema(){
		if(ramfEstudianteAsignacionMecanismoDtoEditar.getAssttTutor()!=null){
			
			ramfHabilitadorGuardar=false;	
		}else{
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeWarn(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("ReAsignacion.postulacion.tutor.no.asignado.excepcion")));
		}
		
	}
	

	public String verificarClickAceptarTramite(){
		ramfValidadorClicTutor = 0;
		ramfValidadorClic = 1;
		return null;
	}
	
	public String verificarClickAceptarTramiteNo(){
		ramfValidadorClic = 0;
		return null;
	}
	
	public String verificarClickAsignarTutor(){
		ramfEstudianteAsignacionMecanismoDtoEditar.setAssttTutor(null);
		ramfValidadorClicTutor = 1;
		return null;
	}
	
	public String verificarClickAsignarTutorNo(){
		ramfValidadorClicTutor = 0;
		return null;
	}
	//****************************************************************/
	//******************* METODOS GETTER y SETTER ********************/
	//****************************************************************/


	public EstudianteAsignacionMecanismoDto getRamfEstudianteAsignacionMecanismoDtoBuscar() {
		return ramfEstudianteAsignacionMecanismoDtoBuscar;
	}


	public void setRamfEstudianteAsignacionMecanismoDtoBuscar(
			EstudianteAsignacionMecanismoDto ramfEstudianteAsignacionMecanismoDtoBuscar) {
		this.ramfEstudianteAsignacionMecanismoDtoBuscar = ramfEstudianteAsignacionMecanismoDtoBuscar;
	}


	public EstudianteAsignacionMecanismoDto getRamfEstudianteAsignacionMecanismoDtoEditar() {
		return ramfEstudianteAsignacionMecanismoDtoEditar;
	}


	public void setRamfEstudianteAsignacionMecanismoDtoEditar(
			EstudianteAsignacionMecanismoDto ramfEstudianteAsignacionMecanismoDtoEditar) {
		this.ramfEstudianteAsignacionMecanismoDtoEditar = ramfEstudianteAsignacionMecanismoDtoEditar;
	}


	public CarreraDto getRamfCarrera() {
		return ramfCarrera;
	}


	public void setRamfCarrera(CarreraDto ramfCarrera) {
		this.ramfCarrera = ramfCarrera;
	}


	public List<CarreraDto> getRamfListCarreras() {
		ramfListCarreras = ramfListCarreras==null?(new ArrayList<CarreraDto>()):ramfListCarreras;
		return ramfListCarreras;
	}


	public void setRamfListCarreras(List<CarreraDto> ramfListCarreras) {
		this.ramfListCarreras = ramfListCarreras;
	}


	public Convocatoria getRamfConvocatoria() {
		return ramfConvocatoria;
	}


	public void setRamfConvocatoria(Convocatoria ramfConvocatoria) {
		this.ramfConvocatoria = ramfConvocatoria;
	}


	public List<Convocatoria> getRamfListConvocatorias() {
		ramfListConvocatorias = ramfListConvocatorias==null?(new ArrayList<Convocatoria>()):ramfListConvocatorias;
		return ramfListConvocatorias;
	}


	public void setRamfListConvocatorias(List<Convocatoria> ramfListConvocatorias) {
		this.ramfListConvocatorias = ramfListConvocatorias;
	}


	public Usuario getRamfUsuarioQueCambia() {
		return ramfUsuarioQueCambia;
	}


	public void setRamfUsuarioQueCambia(Usuario ramfUsuarioQueCambia) {
		this.ramfUsuarioQueCambia = ramfUsuarioQueCambia;
	}


	public List<EstudianteAsignacionMecanismoDto> getRamfListEstudianteAsignacionMecanismoDto() {
		ramfListEstudianteAsignacionMecanismoDto = ramfListEstudianteAsignacionMecanismoDto==null?(new ArrayList<EstudianteAsignacionMecanismoDto>()):ramfListEstudianteAsignacionMecanismoDto;
		return ramfListEstudianteAsignacionMecanismoDto;
	}


	public void setRamfListEstudianteAsignacionMecanismoDto(
			List<EstudianteAsignacionMecanismoDto> ramfListEstudianteAsignacionMecanismoDto) {
		this.ramfListEstudianteAsignacionMecanismoDto = ramfListEstudianteAsignacionMecanismoDto;
	}


	public Persona getRamfPersonaPostulante() {
		return ramfPersonaPostulante;
	}


	public void setRamfPersonaPostulante(Persona ramfPersonaPostulante) {
		this.ramfPersonaPostulante = ramfPersonaPostulante;
	}


	public int getFechaRegimenPostulacion() {
		return fechaRegimenPostulacion;
	}


	public void setFechaRegimenPostulacion(int fechaRegimenPostulacion) {
		this.fechaRegimenPostulacion = fechaRegimenPostulacion;
	}


	public int getHabilitarCampos() {
		return habilitarCampos;
	}


	public void setHabilitarCampos(int habilitarCampos) {
		this.habilitarCampos = habilitarCampos;
	}


	public Integer getRamfValidadorClic() {
		return ramfValidadorClic;
	}


	public void setRamfValidadorClic(Integer ramfValidadorClic) {
		this.ramfValidadorClic = ramfValidadorClic;
	}


	public Boolean getRamfDeshabilitadoTutor() {
		return ramfDeshabilitadoTutor;
	}


	public void setRamfDeshabilitadoTutor(Boolean ramfDeshabilitadoTutor) {
		this.ramfDeshabilitadoTutor = ramfDeshabilitadoTutor;
	}


	public List<MecanismoCarrera> getRamfListMecanismoTitulacionCarrera() {
		ramfListMecanismoTitulacionCarrera = ramfListMecanismoTitulacionCarrera==null?(new ArrayList<MecanismoCarrera>()):ramfListMecanismoTitulacionCarrera;
		return ramfListMecanismoTitulacionCarrera;
	}


	public void setRamfListMecanismoTitulacionCarrera(
			List<MecanismoCarrera> ramfListMecanismoTitulacionCarrera) {
		this.ramfListMecanismoTitulacionCarrera = ramfListMecanismoTitulacionCarrera;
	}


	public MecanismoCarrera getRamfMecanismoTitulacionCarrera() {
		return ramfMecanismoTitulacionCarrera;
	}


	public void setRamfMecanismoTitulacionCarrera(
			MecanismoCarrera ramfMecanismoTitulacionCarrera) {
		this.ramfMecanismoTitulacionCarrera = ramfMecanismoTitulacionCarrera;
	}


	public Integer getRamfExamenComplexivoValue() {
		return ramfExamenComplexivoValue;
	}


	public void setRamfExamenComplexivoValue(Integer ramfExamenComplexivoValue) {
		this.ramfExamenComplexivoValue = ramfExamenComplexivoValue;
	}


	public Integer getRamfValidadorClicTutor() {
		return ramfValidadorClicTutor;
	}


	public void setRamfValidadorClicTutor(Integer ramfValidadorClicTutor) {
		this.ramfValidadorClicTutor = ramfValidadorClicTutor;
	}


	public Boolean getRamfHabilitadorTutor() {
		return ramfHabilitadorTutor;
	}


	public void setRamfHabilitadorTutor(Boolean ramfHabilitadorTutor) {
		this.ramfHabilitadorTutor = ramfHabilitadorTutor;
	}


	public Boolean getRamfHabilitadorGuardar() {
		return ramfHabilitadorGuardar;
	}


	public void setRamfHabilitadorGuardar(Boolean ramfHabilitadorGuardar) {
		this.ramfHabilitadorGuardar = ramfHabilitadorGuardar;
	}
	public List<DocenteTutorTribunalLectorJdbcDto> getRamfListDocentesDto() {
		ramfListDocentesDto = ramfListDocentesDto==null?(new ArrayList<DocenteTutorTribunalLectorJdbcDto>()):ramfListDocentesDto;
		return ramfListDocentesDto;
	}


	public void setRamfListDocentesDto(
			List<DocenteTutorTribunalLectorJdbcDto> ramfListDocentesDto) {
		this.ramfListDocentesDto = ramfListDocentesDto;
	}


	public String getRamfMensajeTutor() {
		return ramfMensajeTutor;
	}


	public void setRamfMensajeTutor(String ramfMensajeTutor) {
		this.ramfMensajeTutor = ramfMensajeTutor;
	}


	public String getCedula() {
		return cedula;
	}


	public void setCedula(String cedula) {
		this.cedula = cedula;
	}


	public String getRamfApellidoDocenteBuscar() {
		return ramfApellidoDocenteBuscar;
	}


	public void setRamfApellidoDocenteBuscar(String ramfApellidoDocenteBuscar) {
		this.ramfApellidoDocenteBuscar = ramfApellidoDocenteBuscar;
	}
	
	
	
	
	
}
