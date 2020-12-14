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
   
 ARCHIVO:     ModificarNotasTribunalSecretariaForm.java	  
 DESCRIPCION: Bean que maneja las peticiones de modificación de notas de tribunal lector.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
31 - Octubre - 2016		Daniel Albuja                        Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.managedBeans.session.validacion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.CarreraDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.DocenteTutorTribunalLectorJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteActaGradoJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteAptitudJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteAptitudOtrosMecanismosJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.FacultadDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.AsentamientoNotaException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.AsentamientoNotaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.CarreraDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ConvocatoriaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.DocenteTutorTribunalLectorJdbcDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.DocenteTutorTribunalLectorJdbcDtoNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RolFlujoCarreraException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RolFlujoCarreraNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.AsignacionTitulacionServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.ConvocatoriaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.FichaDocenteServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.RolFlujoCarreraServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.AsentamientoNotaDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.CarreraDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.DocenteDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.EstudianteAptitudDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.EstudianteAsignacionDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.EstudianteValidacionDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.FacultadDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.CarreraConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.MecanismoTitulacionCarreraConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.TramiteTituloConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.AsignacionTitulacion;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Convocatoria;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.FichaDocente;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.MecanismoCarrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Persona;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Usuario;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.UsuarioRol;
import ec.edu.uce.titulacionPosgrado.jsf.utilidades.FacesUtil;

/**
 * Clase (managed bean) ModificarNotasTribunalSecretariaForm.
 * Bean que maneja las peticiones de modificación de notas de tribunal lector.
 * @author dalbuja.
 * @version 1.0
 */



@ManagedBean(name="modificarNotasTribunalSecretariaForm")
@SessionScoped
public class ModificarNotasTribunalSecretariaForm implements Serializable{

	private static final long serialVersionUID = 8026853400702897855L;
		
	// ********************************************************************************/
	// ******************* Variables de ModificarNotasTribunalSecretariaForm **********/
	// ********************************************************************************/
	
	private EstudianteAptitudJdbcDto mntsfEstudianteAptitudDtoBuscar;
	private EstudianteAptitudJdbcDto mntsfEstudianteAptitudDtoEditar;
	private EstudianteAptitudOtrosMecanismosJdbcDto mntsfEstudianteAptitudOtrosMecanismosDtoEditar;
	private FacultadDto mntsfFacultad;
	private CarreraDto mntsfCarrera;
	private List<CarreraDto> mntsfListCarreras;
	private List<FacultadDto> mntsfListFacultades;
	private Convocatoria mntsfConvocatoria;
	private List<Convocatoria> mntsfListConvocatorias;
	private Usuario mntsfUsuarioQueCambia;
	private List<EstudianteAptitudJdbcDto> mntsfListEstudianteAptitudDto;
	private List<EstudianteAptitudOtrosMecanismosJdbcDto> mntsfListEstudiantePendienteTribunal;
	private Persona mntsfPersonaPostulante;
	private List<MecanismoCarrera> mntsfListMecanismoTitulacionCarrera;
	private MecanismoCarrera mntsfMecanismoTitulacionCarrera;
	private Integer mntsfRequisitos;
	private Integer mntsfReprobo;
	private Integer mntsfSegundaCarrera;
	private int habilitarCampos;
	private Integer mntsfValidadorClic;
	private Integer mntsfTutorClic;
	private Integer mntsfLectoresClic;
	private Boolean mntsfDeshabilitadoTutor;
	private Integer mntsfActualizaConocimientos;
	private boolean mntsfHabilitadorActualizarConocimientos;
	private boolean mntsfHabilitadorPanelAptitud;
	private String mntsfCedulaDocenteBuscar;
	private String mntsfApellidoDocenteBuscar;
	private String mntsfCedulaDocenteLectorBuscar;
	private String mntsfMensajeTutor;
	private String mntsfNota1Cadena;
	private String mntsfNota2Cadena;
	private String mntsfNota3Cadena;
	private String mntsfCadenaJsf;
	
	private BigDecimal mntsfNota1;
	private BigDecimal mntsfNota2;
	private BigDecimal mntsfNota3;
	private BigDecimal sumaNotas;
	private BigDecimal promedioNotas;

	private String mntsfPrimerLector;
	private String mntsfSegundoLector;
	private String mntsfTercerLector;
	private String mntsfCedulaPrimerLector;
	private String mntsfCedulaSegundoLector;
	private String mntsfCedulaTercerLector;
	private String mntsfPrimerOral;
	private String mntsfSegundoOral;
	private String mntsfTercerOral;
	private String mntsfCedulaPrimerOral;
	private String mntsfCedulaSegundoOral;
	private String mntsfCedulaTercerOral;
	private List<DocenteTutorTribunalLectorJdbcDto> mntsfListDocentesDto;
	private List<DocenteTutorTribunalLectorJdbcDto> mntsfListDocentesDtoVisualizar;
	private List<DocenteTutorTribunalLectorJdbcDto>  mntsfListDocentesLectoresDto;
	private boolean mntsfHabilitadorAsignar;
	private boolean mntsfHabilitadorTribunalMayorATres;
	private String mntsfMensajeLectores;
	private boolean mntsfHabilitadorNotasTribunal;
	private boolean mntsfHabilitadorNotasTribunal3;
	private boolean mntsfHabilitadorTribunal3;
	private boolean mntsfHabilitadorGuardarModalTribunal;
	private boolean mntsfHabilitadorBtnAsignar;
	private boolean mntsfHabilitadorBtnAsignarLectores;
	private boolean mntsfHabilitadorRegistrarNotas;
	private boolean mntsfHabilitadorRequisitos;
	private boolean mntsfHabilitadorTipoEvaluacion;

	private boolean mntsfHabilitadorApruebaTutor;
	private Integer mntsfAproboTemaTutor;
	private boolean mntsfHabilitadorPanelTribunal;
	private boolean mntsfHabilitadorGuardar;
	private Integer faseAptitud;
	private Integer mntsfTotalTribunal;
	private boolean mntsfHabilitadorLectorEconomia;
	private String mntsfCadenaModalJsf;
	private EstudianteActaGradoJdbcDto notas;
	
	// ******************************************************************/
	// ******************* Servicios Generales **************************/
	// ******************************************************************/
	@EJB
	private ConvocatoriaServicio srvConvocatoriaServicio;
	
	@EJB
	private UsuarioRolServicio srvUsuarioRolServicio;
	
	@EJB
	private FacultadDtoServicioJdbc srvFacultadServicio;
	
	@EJB
	private EstudianteAsignacionDtoServicioJdbc servAmfEstudianteAsignacionDtoServicioJdbc;
	
	@EJB
	private EstudianteValidacionDtoServicioJdbc servAsfEstudianteValidacionDtoServicioJdbc;
	
	@EJB
	private RolFlujoCarreraServicio srvRolFlujoCarreraServicio;
	
	@EJB
	private DocenteDtoServicioJdbc srvDocenteDtoServicioJdbc;
	
	@EJB
	private AsignacionTitulacionServicio srvAsignacionTitulacionServicio;
	
	@EJB
	private FichaDocenteServicio srvFichaDocenteServicio;
	
	@EJB
	private AsentamientoNotaDtoServicioJdbc srvAsentamientoNotaDtoServicioJdbc;
	
	@EJB
	private EstudianteAptitudDtoServicioJdbc srvEstudianteAptitudDtoServicioJdbc;

	@EJB
	private CarreraDtoServicioJdbc srvCarreraDtoServicioJdbc; 
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
	 * Método que dige a la página de Modificar notas tribunal lector de examen complexivo
	 * @param usuario
	 * @return
	 */
	public String ListarPendientesTribunalLector(Usuario usuario){
	//vericacion de que el usuario no pertenezca al active dectory 0.-si 1.-no
		try {
			UsuarioRol usro = srvUsuarioRolServicio.buscarPorIdentificacion(usuario.getUsrIdentificacion());
			if(usro.getUsroEstado().intValue()==UsuarioRolConstantes.ESTADO_INACTIVO_VALUE){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Autenticacion.usuario.desactivado.estado.usuario.rol")));
				return null;
			}
			iniciarParametros();
			mntsfListConvocatorias =  srvConvocatoriaServicio.listarTodos();
			mntsfListCarreras=servAsfEstudianteValidacionDtoServicioJdbc.buscarCarrerasXSecretaria(usuario.getUsrIdentificacion());
			if(mntsfListCarreras.size()==0){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Autenticacion.usuario.desactivado.rol.flujo.carrera")));
				return null;
			}
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
		this.mntsfUsuarioQueCambia = usuario;
		mntsfValidadorClic = 0;
		mntsfLectoresClic=0;
		mntsfListEstudiantePendienteTribunal=null;
//		iniciarParametros();
		return "irListarPendientesLector";
	}
	
	
	/**
	 * Método que busca los postulantes que estén en estado pendiente tribunal
	 * @return - cadena de navegacion a la pagina de inicio
	 */
	public void buscarPendientesTribunal(Usuario usuario){
		if(mntsfConvocatoria.getCnvId()==GeneralesConstantes.APP_ID_BASE){
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeWarn(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistrarNotasDefensa.seleccionar.convocatoria")));
		}else if(mntsfCarrera.getCrrId()==GeneralesConstantes.APP_ID_BASE){
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeWarn(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistrarNotasDefensa.seleccionar.carrera")));
		}else {
			try {
				mntsfListEstudiantePendienteTribunal=srvEstudianteAptitudDtoServicioJdbc.buscarEstudianteXIndetificacionXCarreraXConvocatoria(mntsfEstudianteAptitudDtoBuscar.getPrsIdentificacion(),mntsfCarrera.getCrrId(),mntsfConvocatoria.getCnvId(), TramiteTituloConstantes.ESTADO_PROCESO_APTO_VALUE);
				if(!(mntsfListEstudiantePendienteTribunal.size()>0)){
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistrarNotasDefensa.listar.postulantes.defensa.oral.no.resultados")));
				}
			} catch (Exception e) {
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistrarNotasDefensa.listar.postulantes.defensa.oral.no.resultados")));
			} 
		}
		
	}
	
	/**
	 * Maneja de cancelacion del listado de pendientes de modificar notas
	 * @return - cadena de navegacion a la pagina de inicio
	 */
	public String irInicio(){
		iniciarParametros();
		return "irInicio";
	}
	
	/**
	 * Maneja de cancelacion de la modificación
	 * @return - cadena de navegacion a la pagina de listar pendientes
	 */
	public String irCancelarModificarNotas(){
		mntsfListEstudiantePendienteTribunal= new ArrayList<EstudianteAptitudOtrosMecanismosJdbcDto>();
		iniciarParametros();
		return "irCancelarModificarNotas";
	}

	public String irModificarNotasTribunal(EstudianteAptitudOtrosMecanismosJdbcDto estudiante, Usuario usuario){
		mntsfEstudianteAptitudOtrosMecanismosDtoEditar=new EstudianteAptitudOtrosMecanismosJdbcDto();
		this.mntsfEstudianteAptitudOtrosMecanismosDtoEditar=estudiante;
		mntsfListDocentesLectoresDto=null;
		mntsfHabilitadorNotasTribunal=true;
		mntsfPrimerOral=null;
		mntsfSegundoOral=null;
		mntsfTercerOral=null;
		mntsfCedulaPrimerOral=null;
		mntsfCedulaSegundoOral=null;
		mntsfCedulaTercerOral=null;
		mntsfHabilitadorLectorEconomia=true;
		CarreraDto crr = new CarreraDto();
		try {
			crr = srvCarreraDtoServicioJdbc.buscarXId(mntsfCarrera.getCrrId());
		} catch (Exception e) {
		} 
		if(crr.getCrrTipoEvaluacion()==CarreraConstantes.CARRERA_EVALUACION_DEFENSA_ESCRITO_ORAL_VALUE){
			mntsfEstudianteAptitudOtrosMecanismosDtoEditar.setCrrTipoEvaluacion(CarreraConstantes.CARRERA_EVALUACION_DEFENSA_ESCRITO_ORAL_VALUE);
			mntsfHabilitadorTipoEvaluacion=true;
		}else{
			mntsfEstudianteAptitudOtrosMecanismosDtoEditar.setCrrTipoEvaluacion(CarreraConstantes.CARRERA_EVALUACION_DEFENSA_ORAL_VALUE);
			mntsfHabilitadorTipoEvaluacion=false;
		}
//		if(estudiante.getCnvId()>=4){
			notas = new EstudianteActaGradoJdbcDto();
			try {
				notas = srvAsentamientoNotaDtoServicioJdbc.buscarAsentamientoNotaEstudianteAptoXIdentificacionXCarrera(mntsfEstudianteAptitudOtrosMecanismosDtoEditar.getPrsIdentificacion(), mntsfCarrera.getCrrId());
				mntsfEstudianteAptitudOtrosMecanismosDtoEditar.setAsnoId(notas.getAsnoId());
			} catch (Exception e) {
				mntsfEstudianteAptitudOtrosMecanismosDtoEditar.setAsnoId(GeneralesConstantes.APP_ID_BASE);
			} 
				mntsfCadenaJsf = "Otros mecanismos / Ingreso notas "+ MecanismoTitulacionCarreraConstantes.SELECCION_DEFENSA_ORAL_LABEL+" / Edición";	
			AsignacionTitulacion asttAux = new AsignacionTitulacion();
			asttAux = srvAsignacionTitulacionServicio.buscarTemaTutorXTrttId(estudiante.getTrttId());
			mntsfEstudianteAptitudOtrosMecanismosDtoEditar.setAsttTemaTrabajo(asttAux.getAsttTemaTrabajo());
			mntsfEstudianteAptitudOtrosMecanismosDtoEditar.setAsttTutor(asttAux.getAsttDirectorCientifico());
			mntsfHabilitadorTribunal3=false;
			return "irModificarNotasTribunalLector2018";
//		}
//		if(mntsfEstudianteAptitudOtrosMecanismosDtoEditar.getListaDocentesTribunal().size()==2){
//			mntsfHabilitadorTribunal3=true;
//			mntsfNota3=null;
//		}else{
//			mntsfHabilitadorTribunal3=false;
//			mntsfTercerLector=mntsfEstudianteAptitudOtrosMecanismosDtoEditar.getListaDocentesTribunal().get(2).getPrsPrimerApellido()+" "
//					+mntsfEstudianteAptitudOtrosMecanismosDtoEditar.getListaDocentesTribunal().get(2).getPrsSegundoApellido() 
//					+" "+mntsfEstudianteAptitudOtrosMecanismosDtoEditar.getListaDocentesTribunal().get(2).getPrsNombres();
//			mntsfNota3=new BigDecimal(mntsfEstudianteAptitudOtrosMecanismosDtoEditar.getAsnoTrbLector3()).setScale(2,BigDecimal.ROUND_DOWN);
//		}
//		mntsfHabilitadorTribunal3=true;
//		if((mntsfEstudianteAptitudOtrosMecanismosDtoEditar.getTrttCarreraId()==CarreraConstantes.CARRERA_ECONOMIA_DISTANCIA_VALUE
//				||mntsfEstudianteAptitudOtrosMecanismosDtoEditar.getTrttCarreraId()==CarreraConstantes.CARRERA_ECONOMIA_VALUE)
//				){
//			mntsfHabilitadorLectorEconomia=false;
//		}
//		return "irModificarNotasTribunalLector";
	}
	
	public boolean isMntsfHabilitadorLectorEconomia() {
		return mntsfHabilitadorLectorEconomia;
	}

	public void setMntsfHabilitadorLectorEconomia(
			boolean mntsfHabilitadorLectorEconomia) {
		this.mntsfHabilitadorLectorEconomia = mntsfHabilitadorLectorEconomia;
	}

	/**
	 * Lista los docentes segun los parámetros ingresados en el panel de búsqueda 
	 */
	public void buscarDocentes(Usuario usuario, String cedula){
		mntsfHabilitadorGuardar=true;
		mntsfCedulaDocenteLectorBuscar=null;
		mntsfHabilitadorTribunalMayorATres=false;
		mntsfMensajeLectores="No se puede asignar más lectores, máximo tres integrantes del tribunal.";
		mntsfListDocentesDtoVisualizar=null;
		mntsfListDocentesDtoVisualizar=new ArrayList<DocenteTutorTribunalLectorJdbcDto>();
		try {
			mntsfListDocentesDto=srvDocenteDtoServicioJdbc.buscarDocenteXIdentificacionTitulacion(cedula,mntsfApellidoDocenteBuscar);
			if(mntsfListDocentesDto.size()>0){
				mntsfHabilitadorAsignar=false;
				mntsfMensajeTutor="No hay resultados con los parámetros seleccionados";
				
				for (int i=0;i<mntsfListDocentesDto.size();i++) {
					boolean op=true;
					for (DocenteTutorTribunalLectorJdbcDto item : mntsfListDocentesLectoresDto) {
						if(item.getPrsIdentificacion().equals(mntsfListDocentesDto.get(i).getPrsIdentificacion())) op=false;
					}
					if(op){
						mntsfListDocentesDtoVisualizar.add(mntsfListDocentesDto.get(i));
					}
				}
			}
			mntsfCedulaDocenteBuscar=null;
			mntsfApellidoDocenteBuscar=null;
		} catch (DocenteTutorTribunalLectorJdbcDtoException e) {
			mntsfListDocentesDto = null;
			mntsfListDocentesDtoVisualizar=null;
			mntsfMensajeTutor=(e.getMessage());
		} catch (DocenteTutorTribunalLectorJdbcDtoNoEncontradoException e) {
			mntsfListDocentesDto = null;
			mntsfListDocentesDtoVisualizar=null;
			mntsfMensajeTutor=(e.getMessage());
		}
	}
	
	public void guardarTribunal(){
		
			mntsfHabilitadorBtnAsignarLectores=true;
			mntsfHabilitadorNotasTribunal=false;
			mntsfHabilitadorRegistrarNotas=false;
			mntsfPrimerOral=mntsfListDocentesLectoresDto.get(0).getPrsPrimerApellido()+" "+mntsfListDocentesLectoresDto.get(0).getPrsSegundoApellido()+" "+mntsfListDocentesLectoresDto.get(0).getPrsNombres();
			mntsfSegundoOral=mntsfListDocentesLectoresDto.get(1).getPrsPrimerApellido()+" "+mntsfListDocentesLectoresDto.get(1).getPrsSegundoApellido()+" "+mntsfListDocentesLectoresDto.get(1).getPrsNombres();
			if(mntsfTotalTribunal==3){
				mntsfTercerOral=mntsfListDocentesLectoresDto.get(2).getPrsPrimerApellido()+" "+mntsfListDocentesLectoresDto.get(2).getPrsSegundoApellido()+" "+mntsfListDocentesLectoresDto.get(2).getPrsNombres();
				mntsfCedulaTercerOral=mntsfListDocentesLectoresDto.get(2).getPrsIdentificacion();
				mntsfHabilitadorTribunal3=false;
			}else{
				mntsfHabilitadorTribunal3=true;
				mntsfTercerOral=null;	
			}
			mntsfCedulaPrimerOral=mntsfListDocentesLectoresDto.get(0).getPrsIdentificacion();
			mntsfCedulaSegundoOral=mntsfListDocentesLectoresDto.get(1).getPrsIdentificacion();
			verificarClickAsignarLectoresNo();	
	}
	
	public void registrarNotas(){
		try {
			sumaNotas = new BigDecimal(0.0);
			Integer numTribunal = 1;
			if (mntsfEstudianteAptitudOtrosMecanismosDtoEditar.getAsnoDfnOral1() != null) {
				sumaNotas = sumaNotas.add(mntsfEstudianteAptitudOtrosMecanismosDtoEditar.getAsnoDfnOral1())
						.setScale(2, RoundingMode.DOWN);
				numTribunal++;
			}
			if (mntsfEstudianteAptitudOtrosMecanismosDtoEditar.getAsnoDfnOral2() != null) {
				sumaNotas = sumaNotas.add(mntsfEstudianteAptitudOtrosMecanismosDtoEditar.getAsnoDfnOral2())
						.setScale(2, RoundingMode.DOWN);
				numTribunal++;
			}
			if (mntsfEstudianteAptitudOtrosMecanismosDtoEditar.getAsnoDfnOral3() != null) {
				sumaNotas = sumaNotas.add(mntsfEstudianteAptitudOtrosMecanismosDtoEditar.getAsnoDfnOral3())
						.setScale(2, RoundingMode.DOWN);
				numTribunal++;
			}
			mntsfEstudianteAptitudOtrosMecanismosDtoEditar
					.setAsnoPrmDfnOral((sumaNotas.divide(new BigDecimal(numTribunal-1),2, RoundingMode.DOWN)));
			mntsfHabilitadorGuardar = false;
			mntsfHabilitadorRegistrarNotas = true;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RegistrarNotasDefensa.registrar.notas.exitosamente")));
		} catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Error al registrar las calificaciones, por favor revise.");
		}
	}

	public String guardarNotas(){
		// En caso de solo guardar el tribunal lector y las notas sin aprobar la nota mínima de 14
		List<FichaDocente> listaCedulaDocentes = new ArrayList<FichaDocente>();
		try {
			try {
				listaCedulaDocentes.add(srvFichaDocenteServicio.buscarPorIdentificacion(mntsfCedulaPrimerOral));	
			} catch (Exception e) {
			}
			try {
				listaCedulaDocentes.add(srvFichaDocenteServicio.buscarPorIdentificacion(mntsfCedulaSegundoOral));	
			} catch (Exception e) {
			}
			try {
				if(mntsfTotalTribunal==3){
					listaCedulaDocentes.add(srvFichaDocenteServicio.buscarPorIdentificacion(mntsfCedulaTercerOral));	
				}	
			} catch (Exception e) {
			}
			RolFlujoCarrera roflcrr = srvRolFlujoCarreraServicio.buscarPorCarrera(mntsfEstudianteAptitudOtrosMecanismosDtoEditar.getTrttCarreraId(), mntsfUsuarioQueCambia.getUsrId() , RolConstantes.ROL_BD_VALIDADOR_VALUE);
			mntsfEstudianteAptitudOtrosMecanismosDtoEditar.setPrtrFechaEjecucion(new Timestamp((new Date()).getTime()));
			srvAsentamientoNotaDtoServicioJdbc.guardarAsentamientoNotaGrado(mntsfEstudianteAptitudOtrosMecanismosDtoEditar, roflcrr, listaCedulaDocentes,TramiteTituloConstantes.ESTADO_PROCESO_DEFENSA_ORAL_VALUE, mntsfTotalTribunal);
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.otros.mecanismos.guardar.pendiente")));
		} catch (RolFlujoCarreraNoEncontradoException e) {
		} catch (RolFlujoCarreraException e) {
		} catch (AsentamientoNotaNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.otros.mecanismos.guardar.pendiente.error.exception")));
		} catch (AsentamientoNotaException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.otros.mecanismos.guardar.pendiente.error.exception")));
		}
		verificarClickAceptarTramiteNo();
		iniciarParametros();
		mntsfListEstudiantePendienteTribunal=null;
		mntsfListEstudiantePendienteTribunal=new ArrayList<EstudianteAptitudOtrosMecanismosJdbcDto>();
		return "irListarPendientesLector";
	}
	
	public String guardarNotas2018(){
		// En caso de solo guardar el tribunal lector y las notas sin aprobar la nota mínima de 14
		List<FichaDocente> listaCedulaDocentes = new ArrayList<FichaDocente>();
		try {
			try {
				listaCedulaDocentes.add(srvFichaDocenteServicio.buscarPorIdentificacion(mntsfCedulaPrimerOral));	
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				listaCedulaDocentes.add(srvFichaDocenteServicio.buscarPorIdentificacion(mntsfCedulaSegundoOral));	
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				listaCedulaDocentes.add(srvFichaDocenteServicio.buscarPorIdentificacion(mntsfCedulaTercerOral));
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
			
			RolFlujoCarrera roflcrr = srvRolFlujoCarreraServicio.buscarPorCarrera(mntsfEstudianteAptitudOtrosMecanismosDtoEditar.getTrttCarreraId(), mntsfUsuarioQueCambia.getUsrId() , RolConstantes.ROL_BD_VALIDADOR_VALUE);
			mntsfEstudianteAptitudOtrosMecanismosDtoEditar.setPrtrFechaEjecucion(new Timestamp((new Date()).getTime()));
			srvAsentamientoNotaDtoServicioJdbc.guardarAsentamientoNotaGrado(mntsfEstudianteAptitudOtrosMecanismosDtoEditar, roflcrr, listaCedulaDocentes,TramiteTituloConstantes.ESTADO_PROCESO_DEFENSA_ORAL_VALUE, mntsfTotalTribunal);
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.otros.mecanismos.guardar.pendiente")));
		} catch (RolFlujoCarreraNoEncontradoException e) {
		} catch (RolFlujoCarreraException e) {
		} catch (AsentamientoNotaNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.otros.mecanismos.guardar.pendiente.error.exception")));
		} catch (AsentamientoNotaException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Aptitud.otros.mecanismos.guardar.pendiente.error.exception")));
		}
		verificarClickAceptarTramiteNo();
		iniciarParametros();
		mntsfListEstudiantePendienteTribunal=null;
		mntsfListEstudiantePendienteTribunal=new ArrayList<EstudianteAptitudOtrosMecanismosJdbcDto>();
		return "irListarPendientesLector";
	}
	
	public String verificarClickAsignarLectoresNo(){
		mntsfListDocentesDto=null;
		mntsfLectoresClic = 0;
		if(mntsfListDocentesLectoresDto.size()==3){
			mntsfHabilitadorNotasTribunal=true;	
		}
		return null;
	}
	
	public void verificarClickAsignarLectores(){
		mntsfListDocentesDtoVisualizar=new ArrayList<DocenteTutorTribunalLectorJdbcDto>();
		mntsfListDocentesLectoresDto= new ArrayList<DocenteTutorTribunalLectorJdbcDto>();
		mntsfLectoresClic = 1;
	}
	
	public void asignarDocenteLector(DocenteTutorTribunalLectorJdbcDto item){
		mntsfNota1 = null;
		mntsfNota2 = null;
		mntsfHabilitadorTribunalMayorATres=false;
		mntsfHabilitadorTribunal3=true;
		if(mntsfListDocentesLectoresDto.size()>2){
			
			mntsfMensajeLectores="No se puede asignar más miembros, máximo tres integrantes del tribunal.";
			  mntsfHabilitadorTribunalMayorATres=true;
		}else{
				
				boolean op=false;
				for (DocenteTutorTribunalLectorJdbcDto elemento : mntsfListDocentesLectoresDto) {
					if(item.getPrsIdentificacion().equals(elemento.getPrsIdentificacion())){
						op=true;
					}
				}
				if(op){
					mntsfHabilitadorTribunalMayorATres=true;
					mntsfMensajeLectores="El docente ya fue asignado como lector.";
				}else{
					mntsfListDocentesLectoresDto.add(item);
					mntsfTotalTribunal++;
					Iterator<DocenteTutorTribunalLectorJdbcDto> itr = mntsfListDocentesDtoVisualizar.iterator();
			        while (itr.hasNext()) {
				       	Object o = itr.next();
				       	if(item.equals(o))
				       	itr.remove();
				    }
				}
				if(mntsfListDocentesLectoresDto.size()==3){
					mntsfHabilitadorGuardarModalTribunal=false;
					mntsfHabilitadorTribunal3=true;	
				}
				
		}
	}
	
	public void eliminarDocente(DocenteTutorTribunalLectorJdbcDto item){
		Iterator<DocenteTutorTribunalLectorJdbcDto> itr = mntsfListDocentesLectoresDto.iterator();
        while (itr.hasNext()) {
	       	Object o = itr.next();
	       	if(item.equals(o))
	       	itr.remove();
	       	mntsfTotalTribunal=mntsfTotalTribunal-1;
	    }
        mntsfHabilitadorTribunalMayorATres=false;
        if(mntsfListDocentesLectoresDto.size()>1){
        	mntsfHabilitadorGuardarModalTribunal=false;	
		}else{
			mntsfHabilitadorGuardarModalTribunal=true;
		}
        mntsfHabilitadorApruebaTutor=false;
	}
	
	public String verificarClickAceptarTramite(){
		try {
//			if(mntsfEstudianteAptitudOtrosMecanismosDtoEditar.getMcttcrOpcion()==MecanismoTitulacionCarreraConstantes.SELECCION_DEFENSA_ESCRITA_VALUE){
//				mntsfCadenaModalJsf = MecanismoTitulacionCarreraConstantes.SELECCION_DEFENSA_ESCRITA_LABEL;
//			}else{
				mntsfCadenaModalJsf = MecanismoTitulacionCarreraConstantes.SELECCION_DEFENSA_ORAL_LABEL;	
//			}	
		} catch (Exception e) {
		}
		
		mntsfCedulaDocenteBuscar=null;
		mntsfValidadorClic = 1;
		return null;
	}
	
	public String verificarClickAceptarTramiteNo(){
		mntsfValidadorClic = 0;
		return null;
	}
	
	//****************************************************************/
	//******** METODOS DE LIMPIEZA DE VARIABLES **********************/
	//****************************************************************/
	
	/** Método que limpia la lista de carreras
	 * 
	 */
	public void cambiarCarrera(){
		limpiar();
	}
	
	
	/** Método que limpia los componentes del form
	 * 
	 */
	public void limpiar(){
		mntsfListEstudiantePendienteTribunal=null;
		mntsfHabilitadorTribunal3=false;
		mntsfLectoresClic = 0;
		iniciarParametros();
	}
	
	public void limpiarTablaDocentes(){
		
		mntsfCedulaDocenteLectorBuscar=null;
		mntsfApellidoDocenteBuscar=null;
		mntsfListDocentesDto=null;
		mntsfListDocentesDtoVisualizar=null;
	}
	
	
	
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
			
	

	//iniciar parametros de busqueda
	private void iniciarParametros(){
		//INICIALIZO EL DTO DE ESTUDIANTE BUSCAR
		mntsfCarrera=new CarreraDto();
		mntsfConvocatoria = new Convocatoria();
		mntsfHabilitadorActualizarConocimientos=false;
		mntsfFacultad=new FacultadDto();
//		amfValidadorClic = 0;
		mntsfActualizaConocimientos=null;
		mntsfEstudianteAptitudDtoBuscar = new EstudianteAptitudJdbcDto();
		mntsfListEstudianteAptitudDto = null;
		mntsfListEstudianteAptitudDto= new ArrayList<EstudianteAptitudJdbcDto>();
		mntsfEstudianteAptitudDtoEditar = new EstudianteAptitudJdbcDto();
		mntsfHabilitadorPanelAptitud=false;
		mntsfCedulaDocenteBuscar=null;
		mntsfHabilitadorAsignar=true;
		mntsfApellidoDocenteBuscar=null;
		mntsfHabilitadorApruebaTutor=true;
		mntsfHabilitadorPanelTribunal=false;
		mntsfHabilitadorGuardar=true;
		mntsfHabilitadorTribunalMayorATres=false;
		mntsfHabilitadorNotasTribunal=true;
		mntsfHabilitadorNotasTribunal3=true;
		mntsfHabilitadorGuardarModalTribunal=true;
		mntsfHabilitadorRegistrarNotas=true;
		mntsfHabilitadorBtnAsignar=false;
		mntsfHabilitadorBtnAsignarLectores=false;
		mntsfAproboTemaTutor=1;
		mntsfLectoresClic = 0;
		mntsfTotalTribunal=0;
		mntsfHabilitadorTribunal3=true;
	}

	
	
	//****************************************************************/
	//******************* METODOS GETTER y SETTER ********************/
	//****************************************************************/
			
	
	
	public EstudianteAptitudJdbcDto getMntsfEstudianteAptitudDtoBuscar() {
		return mntsfEstudianteAptitudDtoBuscar;
	}

	public void setMntsfEstudianteAptitudDtoBuscar(
			EstudianteAptitudJdbcDto mntsfEstudianteAptitudDtoBuscar) {
		this.mntsfEstudianteAptitudDtoBuscar = mntsfEstudianteAptitudDtoBuscar;
	}

	public EstudianteAptitudJdbcDto getMntsfEstudianteAptitudDtoEditar() {
		return mntsfEstudianteAptitudDtoEditar;
	}

	public void setMntsfEstudianteAptitudDtoEditar(
			EstudianteAptitudJdbcDto mntsfEstudianteAptitudDtoEditar) {
		this.mntsfEstudianteAptitudDtoEditar = mntsfEstudianteAptitudDtoEditar;
	}

	public EstudianteAptitudOtrosMecanismosJdbcDto getMntsfEstudianteAptitudOtrosMecanismosDtoEditar() {
		return mntsfEstudianteAptitudOtrosMecanismosDtoEditar;
	}

	public void setMntsfEstudianteAptitudOtrosMecanismosDtoEditar(
			EstudianteAptitudOtrosMecanismosJdbcDto mntsfEstudianteAptitudOtrosMecanismosDtoEditar) {
		this.mntsfEstudianteAptitudOtrosMecanismosDtoEditar = mntsfEstudianteAptitudOtrosMecanismosDtoEditar;
	}

	public FacultadDto getMntsfFacultad() {
		return mntsfFacultad;
	}

	public void setMntsfFacultad(FacultadDto mntsfFacultad) {
		this.mntsfFacultad = mntsfFacultad;
	}

	public CarreraDto getMntsfCarrera() {
		return mntsfCarrera;
	}

	public void setMntsfCarrera(CarreraDto mntsfCarrera) {
		this.mntsfCarrera = mntsfCarrera;
	}

	public List<CarreraDto> getMntsfListCarreras() {
		return mntsfListCarreras;
	}

	public void setMntsfListCarreras(List<CarreraDto> mntsfListCarreras) {
		this.mntsfListCarreras = mntsfListCarreras;
	}

	public List<FacultadDto> getMntsfListFacultades() {
		return mntsfListFacultades;
	}

	public void setMntsfListFacultades(List<FacultadDto> mntsfListFacultades) {
		this.mntsfListFacultades = mntsfListFacultades;
	}

	public Convocatoria getMntsfConvocatoria() {
		return mntsfConvocatoria;
	}

	public void setMntsfConvocatoria(Convocatoria mntsfConvocatoria) {
		this.mntsfConvocatoria = mntsfConvocatoria;
	}

	public List<Convocatoria> getMntsfListConvocatorias() {
		return mntsfListConvocatorias;
	}

	public void setMntsfListConvocatorias(List<Convocatoria> mntsfListConvocatorias) {
		this.mntsfListConvocatorias = mntsfListConvocatorias;
	}

	public Usuario getMntsfUsuarioQueCambia() {
		return mntsfUsuarioQueCambia;
	}

	public void setMntsfUsuarioQueCambia(Usuario mntsfUsuarioQueCambia) {
		this.mntsfUsuarioQueCambia = mntsfUsuarioQueCambia;
	}

	public List<EstudianteAptitudJdbcDto> getMntsfListEstudianteAptitudDto() {
		return mntsfListEstudianteAptitudDto;
	}

	public void setMntsfListEstudianteAptitudDto(
			List<EstudianteAptitudJdbcDto> mntsfListEstudianteAptitudDto) {
		this.mntsfListEstudianteAptitudDto = mntsfListEstudianteAptitudDto;
	}

	public List<EstudianteAptitudOtrosMecanismosJdbcDto> getMntsfListEstudiantePendienteTribunal() {
		mntsfListEstudiantePendienteTribunal = mntsfListEstudiantePendienteTribunal==null?(new ArrayList<EstudianteAptitudOtrosMecanismosJdbcDto>()):mntsfListEstudiantePendienteTribunal;
		return mntsfListEstudiantePendienteTribunal;
	}

	public void setMntsfListEstudiantePendienteTribunal(
			List<EstudianteAptitudOtrosMecanismosJdbcDto> mntsfListEstudiantePendienteTribunal) {
		this.mntsfListEstudiantePendienteTribunal = mntsfListEstudiantePendienteTribunal;
	}

	public Persona getMntsfPersonaPostulante() {
		return mntsfPersonaPostulante;
	}

	public void setMntsfPersonaPostulante(Persona mntsfPersonaPostulante) {
		this.mntsfPersonaPostulante = mntsfPersonaPostulante;
	}

	public List<MecanismoCarrera> getMntsfListMecanismoTitulacionCarrera() {
		return mntsfListMecanismoTitulacionCarrera;
	}

	public void setMntsfListMecanismoTitulacionCarrera(
			List<MecanismoCarrera> mntsfListMecanismoTitulacionCarrera) {
		this.mntsfListMecanismoTitulacionCarrera = mntsfListMecanismoTitulacionCarrera;
	}

	public MecanismoCarrera getMntsfMecanismoTitulacionCarrera() {
		return mntsfMecanismoTitulacionCarrera;
	}

	public void setMntsfMecanismoTitulacionCarrera(
			MecanismoCarrera mntsfMecanismoTitulacionCarrera) {
		this.mntsfMecanismoTitulacionCarrera = mntsfMecanismoTitulacionCarrera;
	}

	public Integer getMntsfRequisitos() {
		return mntsfRequisitos;
	}

	public void setMntsfRequisitos(Integer mntsfRequisitos) {
		this.mntsfRequisitos = mntsfRequisitos;
	}

	public Integer getMntsfReprobo() {
		return mntsfReprobo;
	}

	public void setMntsfReprobo(Integer mntsfReprobo) {
		this.mntsfReprobo = mntsfReprobo;
	}

	public Integer getMntsfSegundaCarrera() {
		return mntsfSegundaCarrera;
	}

	public void setMntsfSegundaCarrera(Integer mntsfSegundaCarrera) {
		this.mntsfSegundaCarrera = mntsfSegundaCarrera;
	}

	public int getHabilitarCampos() {
		return habilitarCampos;
	}

	public void setHabilitarCampos(int habilitarCampos) {
		this.habilitarCampos = habilitarCampos;
	}

	public Integer getMntsfValidadorClic() {
		return mntsfValidadorClic;
	}

	public void setMntsfValidadorClic(Integer mntsfValidadorClic) {
		this.mntsfValidadorClic = mntsfValidadorClic;
	}

	public Integer getMntsfTutorClic() {
		return mntsfTutorClic;
	}

	public void setMntsfTutorClic(Integer mntsfTutorClic) {
		this.mntsfTutorClic = mntsfTutorClic;
	}

	public Integer getMntsfLectoresClic() {
		return mntsfLectoresClic;
	}

	public void setMntsfLectoresClic(Integer mntsfLectoresClic) {
		this.mntsfLectoresClic = mntsfLectoresClic;
	}

	public Boolean getMntsfDeshabilitadoTutor() {
		return mntsfDeshabilitadoTutor;
	}

	public void setMntsfDeshabilitadoTutor(Boolean mntsfDeshabilitadoTutor) {
		this.mntsfDeshabilitadoTutor = mntsfDeshabilitadoTutor;
	}

	public Integer getMntsfActualizaConocimientos() {
		return mntsfActualizaConocimientos;
	}

	public void setMntsfActualizaConocimientos(Integer mntsfActualizaConocimientos) {
		this.mntsfActualizaConocimientos = mntsfActualizaConocimientos;
	}

	public boolean isMntsfHabilitadorActualizarConocimientos() {
		return mntsfHabilitadorActualizarConocimientos;
	}

	public void setMntsfHabilitadorActualizarConocimientos(
			boolean mntsfHabilitadorActualizarConocimientos) {
		this.mntsfHabilitadorActualizarConocimientos = mntsfHabilitadorActualizarConocimientos;
	}

	public boolean isMntsfHabilitadorPanelAptitud() {
		return mntsfHabilitadorPanelAptitud;
	}

	public void setMntsfHabilitadorPanelAptitud(boolean mntsfHabilitadorPanelAptitud) {
		this.mntsfHabilitadorPanelAptitud = mntsfHabilitadorPanelAptitud;
	}

	public String getMntsfCedulaDocenteBuscar() {
		return mntsfCedulaDocenteBuscar;
	}

	public void setMntsfCedulaDocenteBuscar(String mntsfCedulaDocenteBuscar) {
		this.mntsfCedulaDocenteBuscar = mntsfCedulaDocenteBuscar;
	}

	public String getMntsfApellidoDocenteBuscar() {
		return mntsfApellidoDocenteBuscar;
	}

	public void setMntsfApellidoDocenteBuscar(String mntsfApellidoDocenteBuscar) {
		this.mntsfApellidoDocenteBuscar = mntsfApellidoDocenteBuscar;
	}

	public String getMntsfCedulaDocenteLectorBuscar() {
		return mntsfCedulaDocenteLectorBuscar;
	}

	public void setMntsfCedulaDocenteLectorBuscar(
			String mntsfCedulaDocenteLectorBuscar) {
		this.mntsfCedulaDocenteLectorBuscar = mntsfCedulaDocenteLectorBuscar;
	}

	public String getMntsfMensajeTutor() {
		return mntsfMensajeTutor;
	}

	public void setMntsfMensajeTutor(String mntsfMensajeTutor) {
		this.mntsfMensajeTutor = mntsfMensajeTutor;
	}

	public String getMntsfNota1Cadena() {
		return mntsfNota1Cadena;
	}

	public void setMntsfNota1Cadena(String mntsfNota1Cadena) {
		this.mntsfNota1Cadena = mntsfNota1Cadena;
	}

	public String getMntsfNota2Cadena() {
		return mntsfNota2Cadena;
	}

	public void setMntsfNota2Cadena(String mntsfNota2Cadena) {
		this.mntsfNota2Cadena = mntsfNota2Cadena;
	}

	public String getMntsfNota3Cadena() {
		return mntsfNota3Cadena;
	}

	public void setMntsfNota3Cadena(String mntsfNota3Cadena) {
		this.mntsfNota3Cadena = mntsfNota3Cadena;
	}

	public BigDecimal getMntsfNota1() {
		return mntsfNota1;
	}

	public void setMntsfNota1(BigDecimal mntsfNota1) {
		this.mntsfNota1 = mntsfNota1;
	}

	public BigDecimal getMntsfNota2() {
		return mntsfNota2;
	}

	public void setMntsfNota2(BigDecimal mntsfNota2) {
		this.mntsfNota2 = mntsfNota2;
	}

	public BigDecimal getMntsfNota3() {
		return mntsfNota3;
	}

	public void setMntsfNota3(BigDecimal mntsfNota3) {
		this.mntsfNota3 = mntsfNota3;
	}

	public BigDecimal getSumaNotas() {
		return sumaNotas;
	}

	public void setSumaNotas(BigDecimal sumaNotas) {
		this.sumaNotas = sumaNotas;
	}

	public BigDecimal getPromedioNotas() {
		return promedioNotas;
	}

	public void setPromedioNotas(BigDecimal promedioNotas) {
		this.promedioNotas = promedioNotas;
	}

	public String getMntsfPrimerLector() {
		return mntsfPrimerLector;
	}

	public void setMntsfPrimerLector(String mntsfPrimerLector) {
		this.mntsfPrimerLector = mntsfPrimerLector;
	}

	public String getMntsfSegundoLector() {
		return mntsfSegundoLector;
	}

	public void setMntsfSegundoLector(String mntsfSegundoLector) {
		this.mntsfSegundoLector = mntsfSegundoLector;
	}

	public String getMntsfTercerLector() {
		return mntsfTercerLector;
	}

	public void setMntsfTercerLector(String mntsfTercerLector) {
		this.mntsfTercerLector = mntsfTercerLector;
	}

	public String getMntsfCedulaPrimerLector() {
		return mntsfCedulaPrimerLector;
	}

	public void setMntsfCedulaPrimerLector(String mntsfCedulaPrimerLector) {
		this.mntsfCedulaPrimerLector = mntsfCedulaPrimerLector;
	}

	public String getMntsfCedulaSegundoLector() {
		return mntsfCedulaSegundoLector;
	}

	public void setMntsfCedulaSegundoLector(String mntsfCedulaSegundoLector) {
		this.mntsfCedulaSegundoLector = mntsfCedulaSegundoLector;
	}

	public String getMntsfCedulaTercerLector() {
		return mntsfCedulaTercerLector;
	}

	public void setMntsfCedulaTercerLector(String mntsfCedulaTercerLector) {
		this.mntsfCedulaTercerLector = mntsfCedulaTercerLector;
	}

	public List<DocenteTutorTribunalLectorJdbcDto> getMntsfListDocentesDto() {
		mntsfListDocentesDto = mntsfListDocentesDto==null?(new ArrayList<DocenteTutorTribunalLectorJdbcDto>()):mntsfListDocentesDto;
		return mntsfListDocentesDto;
	}

	public void setMntsfListDocentesDto(
			List<DocenteTutorTribunalLectorJdbcDto> mntsfListDocentesDto) {
		this.mntsfListDocentesDto = mntsfListDocentesDto;
	}

	public List<DocenteTutorTribunalLectorJdbcDto> getMntsfListDocentesDtoVisualizar() {
		mntsfListDocentesDtoVisualizar = mntsfListDocentesDtoVisualizar==null?(new ArrayList<DocenteTutorTribunalLectorJdbcDto>()):mntsfListDocentesDtoVisualizar;
		return mntsfListDocentesDtoVisualizar;
	}

	public void setMntsfListDocentesDtoVisualizar(
			List<DocenteTutorTribunalLectorJdbcDto> mntsfListDocentesDtoVisualizar) {
		this.mntsfListDocentesDtoVisualizar = mntsfListDocentesDtoVisualizar;
	}

	public List<DocenteTutorTribunalLectorJdbcDto> getMntsfListDocentesLectoresDto() {
		return mntsfListDocentesLectoresDto;
	}

	public void setMntsfListDocentesLectoresDto(
			List<DocenteTutorTribunalLectorJdbcDto> mntsfListDocentesLectoresDto) {
		this.mntsfListDocentesLectoresDto = mntsfListDocentesLectoresDto;
	}

	public boolean isMntsfHabilitadorAsignar() {
		return mntsfHabilitadorAsignar;
	}

	public void setMntsfHabilitadorAsignar(boolean mntsfHabilitadorAsignar) {
		this.mntsfHabilitadorAsignar = mntsfHabilitadorAsignar;
	}

	public boolean isMntsfHabilitadorTribunalMayorATres() {
		return mntsfHabilitadorTribunalMayorATres;
	}

	public void setMntsfHabilitadorTribunalMayorATres(
			boolean mntsfHabilitadorTribunalMayorATres) {
		this.mntsfHabilitadorTribunalMayorATres = mntsfHabilitadorTribunalMayorATres;
	}

	public String getMntsfMensajeLectores() {
		return mntsfMensajeLectores;
	}

	public void setMntsfMensajeLectores(String mntsfMensajeLectores) {
		this.mntsfMensajeLectores = mntsfMensajeLectores;
	}

	public boolean isMntsfHabilitadorNotasTribunal() {
		return mntsfHabilitadorNotasTribunal;
	}

	public void setMntsfHabilitadorNotasTribunal(
			boolean mntsfHabilitadorNotasTribunal) {
		this.mntsfHabilitadorNotasTribunal = mntsfHabilitadorNotasTribunal;
	}

	public boolean isMntsfHabilitadorNotasTribunal3() {
		return mntsfHabilitadorNotasTribunal3;
	}

	public void setMntsfHabilitadorNotasTribunal3(
			boolean mntsfHabilitadorNotasTribunal3) {
		this.mntsfHabilitadorNotasTribunal3 = mntsfHabilitadorNotasTribunal3;
	}

	public boolean isMntsfHabilitadorTribunal3() {
		return mntsfHabilitadorTribunal3;
	}

	public void setMntsfHabilitadorTribunal3(boolean mntsfHabilitadorTribunal3) {
		this.mntsfHabilitadorTribunal3 = mntsfHabilitadorTribunal3;
	}

	public boolean isMntsfHabilitadorGuardarModalTribunal() {
		return mntsfHabilitadorGuardarModalTribunal;
	}

	public void setMntsfHabilitadorGuardarModalTribunal(
			boolean mntsfHabilitadorGuardarModalTribunal) {
		this.mntsfHabilitadorGuardarModalTribunal = mntsfHabilitadorGuardarModalTribunal;
	}

	public boolean isMntsfHabilitadorBtnAsignar() {
		return mntsfHabilitadorBtnAsignar;
	}

	public void setMntsfHabilitadorBtnAsignar(boolean mntsfHabilitadorBtnAsignar) {
		this.mntsfHabilitadorBtnAsignar = mntsfHabilitadorBtnAsignar;
	}

	public boolean isMntsfHabilitadorBtnAsignarLectores() {
		return mntsfHabilitadorBtnAsignarLectores;
	}

	public void setMntsfHabilitadorBtnAsignarLectores(
			boolean mntsfHabilitadorBtnAsignarLectores) {
		this.mntsfHabilitadorBtnAsignarLectores = mntsfHabilitadorBtnAsignarLectores;
	}

	public boolean isMntsfHabilitadorRegistrarNotas() {
		return mntsfHabilitadorRegistrarNotas;
	}

	public void setMntsfHabilitadorRegistrarNotas(
			boolean mntsfHabilitadorRegistrarNotas) {
		this.mntsfHabilitadorRegistrarNotas = mntsfHabilitadorRegistrarNotas;
	}

	public boolean isMntsfHabilitadorRequisitos() {
		return mntsfHabilitadorRequisitos;
	}

	public void setMntsfHabilitadorRequisitos(boolean mntsfHabilitadorRequisitos) {
		this.mntsfHabilitadorRequisitos = mntsfHabilitadorRequisitos;
	}

	public boolean isMntsfHabilitadorApruebaTutor() {
		return mntsfHabilitadorApruebaTutor;
	}

	public void setMntsfHabilitadorApruebaTutor(boolean mntsfHabilitadorApruebaTutor) {
		this.mntsfHabilitadorApruebaTutor = mntsfHabilitadorApruebaTutor;
	}

	public Integer getMntsfAproboTemaTutor() {
		return mntsfAproboTemaTutor;
	}

	public void setMntsfAproboTemaTutor(Integer mntsfAproboTemaTutor) {
		this.mntsfAproboTemaTutor = mntsfAproboTemaTutor;
	}

	public boolean isMntsfHabilitadorPanelTribunal() {
		return mntsfHabilitadorPanelTribunal;
	}

	public void setMntsfHabilitadorPanelTribunal(
			boolean mntsfHabilitadorPanelTribunal) {
		this.mntsfHabilitadorPanelTribunal = mntsfHabilitadorPanelTribunal;
	}

	public boolean isMntsfHabilitadorGuardar() {
		return mntsfHabilitadorGuardar;
	}

	public void setMntsfHabilitadorGuardar(boolean mntsfHabilitadorGuardar) {
		this.mntsfHabilitadorGuardar = mntsfHabilitadorGuardar;
	}

	public Integer getFaseAptitud() {
		return faseAptitud;
	}

	public void setFaseAptitud(Integer faseAptitud) {
		this.faseAptitud = faseAptitud;
	}

	public ConvocatoriaServicio getSrvConvocatoriaServicio() {
		return srvConvocatoriaServicio;
	}

	public void setSrvConvocatoriaServicio(
			ConvocatoriaServicio srvConvocatoriaServicio) {
		this.srvConvocatoriaServicio = srvConvocatoriaServicio;
	}

	public UsuarioRolServicio getSrvUsuarioRolServicio() {
		return srvUsuarioRolServicio;
	}

	public void setSrvUsuarioRolServicio(UsuarioRolServicio srvUsuarioRolServicio) {
		this.srvUsuarioRolServicio = srvUsuarioRolServicio;
	}

	public FacultadDtoServicioJdbc getSrvFacultadServicio() {
		return srvFacultadServicio;
	}

	public void setSrvFacultadServicio(FacultadDtoServicioJdbc srvFacultadServicio) {
		this.srvFacultadServicio = srvFacultadServicio;
	}

	public EstudianteAsignacionDtoServicioJdbc getServAmfEstudianteAsignacionDtoServicioJdbc() {
		return servAmfEstudianteAsignacionDtoServicioJdbc;
	}

	public void setServAmfEstudianteAsignacionDtoServicioJdbc(
			EstudianteAsignacionDtoServicioJdbc servAmfEstudianteAsignacionDtoServicioJdbc) {
		this.servAmfEstudianteAsignacionDtoServicioJdbc = servAmfEstudianteAsignacionDtoServicioJdbc;
	}

	public EstudianteValidacionDtoServicioJdbc getServAsfEstudianteValidacionDtoServicioJdbc() {
		return servAsfEstudianteValidacionDtoServicioJdbc;
	}

	public void setServAsfEstudianteValidacionDtoServicioJdbc(
			EstudianteValidacionDtoServicioJdbc servAsfEstudianteValidacionDtoServicioJdbc) {
		this.servAsfEstudianteValidacionDtoServicioJdbc = servAsfEstudianteValidacionDtoServicioJdbc;
	}

	public RolFlujoCarreraServicio getSrvRolFlujoCarreraServicio() {
		return srvRolFlujoCarreraServicio;
	}

	public void setSrvRolFlujoCarreraServicio(
			RolFlujoCarreraServicio srvRolFlujoCarreraServicio) {
		this.srvRolFlujoCarreraServicio = srvRolFlujoCarreraServicio;
	}

	public DocenteDtoServicioJdbc getSrvDocenteDtoServicioJdbc() {
		return srvDocenteDtoServicioJdbc;
	}

	public void setSrvDocenteDtoServicioJdbc(
			DocenteDtoServicioJdbc srvDocenteDtoServicioJdbc) {
		this.srvDocenteDtoServicioJdbc = srvDocenteDtoServicioJdbc;
	}

	public AsignacionTitulacionServicio getSrvAsignacionTitulacionServicio() {
		return srvAsignacionTitulacionServicio;
	}

	public void setSrvAsignacionTitulacionServicio(
			AsignacionTitulacionServicio srvAsignacionTitulacionServicio) {
		this.srvAsignacionTitulacionServicio = srvAsignacionTitulacionServicio;
	}

	public FichaDocenteServicio getSrvFichaDocenteServicio() {
		return srvFichaDocenteServicio;
	}

	public void setSrvFichaDocenteServicio(
			FichaDocenteServicio srvFichaDocenteServicio) {
		this.srvFichaDocenteServicio = srvFichaDocenteServicio;
	}

	public AsentamientoNotaDtoServicioJdbc getSrvAsentamientoNotaDtoServicioJdbc() {
		return srvAsentamientoNotaDtoServicioJdbc;
	}

	public void setSrvAsentamientoNotaDtoServicioJdbc(
			AsentamientoNotaDtoServicioJdbc srvAsentamientoNotaDtoServicioJdbc) {
		this.srvAsentamientoNotaDtoServicioJdbc = srvAsentamientoNotaDtoServicioJdbc;
	}

	public String getMntsfPrimerOral() {
		return mntsfPrimerOral;
	}

	public void setMntsfPrimerOral(String mntsfPrimerOral) {
		this.mntsfPrimerOral = mntsfPrimerOral;
	}

	public String getMntsfSegundoOral() {
		return mntsfSegundoOral;
	}

	public void setMntsfSegundoOral(String mntsfSegundoOral) {
		this.mntsfSegundoOral = mntsfSegundoOral;
	}

	public String getMntsfTercerOral() {
		return mntsfTercerOral;
	}

	public void setMntsfTercerOral(String mntsfTercerOral) {
		this.mntsfTercerOral = mntsfTercerOral;
	}

	public String getMntsfCedulaPrimerOral() {
		return mntsfCedulaPrimerOral;
	}

	public void setMntsfCedulaPrimerOral(String mntsfCedulaPrimerOral) {
		this.mntsfCedulaPrimerOral = mntsfCedulaPrimerOral;
	}

	public String getMntsfCedulaSegundoOral() {
		return mntsfCedulaSegundoOral;
	}

	public void setMntsfCedulaSegundoOral(String mntsfCedulaSegundoOral) {
		this.mntsfCedulaSegundoOral = mntsfCedulaSegundoOral;
	}

	public String getMntsfCedulaTercerOral() {
		return mntsfCedulaTercerOral;
	}

	public void setMntsfCedulaTercerOral(String mntsfCedulaTercerOral) {
		this.mntsfCedulaTercerOral = mntsfCedulaTercerOral;
	}

	public String getMntsfCadenaJsf() {
		return mntsfCadenaJsf;
	}

	public void setMntsfCadenaJsf(String mntsfCadenaJsf) {
		this.mntsfCadenaJsf = mntsfCadenaJsf;
	}

	public String getMntsfCadenaModalJsf() {
		return mntsfCadenaModalJsf;
	}

	public void setMntsfCadenaModalJsf(String mntsfCadenaModalJsf) {
		this.mntsfCadenaModalJsf = mntsfCadenaModalJsf;
	}

	public boolean isMntsfHabilitadorTipoEvaluacion() {
		return mntsfHabilitadorTipoEvaluacion;
	}

	public void setMntsfHabilitadorTipoEvaluacion(boolean mntsfHabilitadorTipoEvaluacion) {
		this.mntsfHabilitadorTipoEvaluacion = mntsfHabilitadorTipoEvaluacion;
	}

	public EstudianteActaGradoJdbcDto getNotas() {
		return notas;
	}

	public void setNotas(EstudianteActaGradoJdbcDto notas) {
		this.notas = notas;
	}

	
}
