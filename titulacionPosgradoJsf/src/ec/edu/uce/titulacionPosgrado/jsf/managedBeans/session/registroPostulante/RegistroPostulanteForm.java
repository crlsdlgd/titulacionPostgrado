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
   
 ARCHIVO:     RegistroPostulanteForm.java	  
 DESCRIPCION: Bean de sesion que maneja el registro de los usuarios. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 22/02/2018		 	Daniel Albuja			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.managedBeans.session.registroPostulante;


import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import ec.edu.uce.envioMail.excepciones.ValidacionMailException;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.RegistroPostulanteDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.UbicacionDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ConvocatoriaException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.ConvocatoriaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EtniaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RegistroPostulanteDtoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.RegistroPostulanteDtoValidacionException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UbicacionDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UbicacionDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.ConvocatoriaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.EtniaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.RegistroPostulanteDtoServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.UbicacionDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.ConvocatoriaConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UbicacionConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UsuarioConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.EncriptadorUtilidades;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Convocatoria;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Etnia;
import ec.edu.uce.titulacionPosgrado.jsf.spring.springSecurity.VerificarRecaptcha;
import ec.edu.uce.titulacionPosgrado.jsf.utilidades.FacesUtil;
import ec.edu.uce.titulacionPosgrado.jsf.utilidades.GeneradorMails;

/**
 * Clase (session bean) RegistroPostulanteForm.
 * Bean de sesion que maneja el registro de los usuarios. 
 * @author dalbuja.
 * @version 1.0
 */

@ManagedBean(name="registroPostulanteForm")
@ViewScoped
public class RegistroPostulanteForm implements Serializable {
	private static final long serialVersionUID = -7885786472838841602L;
	
	//****************************************************************/
	//*********************** VARIABLES ******************************/
	//****************************************************************/
	
	private RegistroPostulanteDto rpfPersonaRegistrar;
	private List<Etnia> rpfListEtnias;
	private List<UbicacionDto> rpfListPaises;
	private String rpfPassword;
	private List<Convocatoria> rpfListaCnvActiva;
	private Convocatoria rpfCnvActiva;
	private String rpfCorreoConformacion;
	private boolean rpfHabilitadorDiscapacidad;
	private boolean verificarBoton;
	private boolean verificarConvocatoria;
	private boolean verificarCedula;
	private Integer verificarClic;
	//****************************************************************/
	//******** METODO GENERAL DE INICIALIZACION DE VARIABLES *********/
	//****************************************************************/
	@PostConstruct
	public void inicializar(){
		rpfPersonaRegistrar= new RegistroPostulanteDto();
		iniciarParametros();
	}
	
	
	public void iniciarParametros(){
		rpfHabilitadorDiscapacidad=true;
		rpfPersonaRegistrar= null;
		rpfPersonaRegistrar= new RegistroPostulanteDto();
		rpfListEtnias= new ArrayList<Etnia>();
		rpfListPaises= new ArrayList<UbicacionDto>();
		rpfListaCnvActiva= new ArrayList<Convocatoria>();
		verificarBoton=true;
		verificarCedula=true;
		verificarClic=0;
		try {
			List<Convocatoria> cnvAux = servRpfConvocatoria.buscarConvocatoriasPorIdPorEstado(ConvocatoriaConstantes.ESTADO_FASE_REGISTRO_POSTULACION_VALUE);
			if(cnvAux.size()==0){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("No puede continuar, el sistema no está en fase de registro.");
				verificarConvocatoria=true;
			}else{
				verificarConvocatoria=false;
				iniciarRegistro();
			}
		} catch (Exception e) {
			verificarConvocatoria=true;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No puede continuar, el sistema no está en fase de registro.");
		}
		
	}
	//****************************************************************/
	//********************* SERVICIOS GENERALES **********************/
	//****************************************************************/
	@EJB
	EtniaServicio servRpfEtnia;
	@EJB
	UbicacionDtoServicioJdbc servRpfUbicacionJdbc;
	@EJB
	RegistroPostulanteDtoServicio servRpfRegistroPostulanteDto;
	@EJB
	ConvocatoriaServicio servRpfConvocatoria;
	
	//****************************************************************/
	//**************** METODOS GENERALES DE LA CLASE *****************/
	//****************************************************************/
	
	//**************************************************************//
	//***************** METODOS DE NEGOCIO *************************//
	//**************************************************************//
	/**
	 * Maneja de inicio del bean
	 * @return - cadena de navegacion a la pagina de registro
	 */
	public String iniciarRegistro(){
		rpfHabilitadorDiscapacidad=true;
		verificarBoton=true;
		if(verificarConvocatoria){
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No existe una convocatoria en fase de registro y postulación.");
			return null;
		}
		
		try {
			//busqueda de convocatoria activa
			rpfListaCnvActiva = servRpfConvocatoria.buscarConvocatoriasPorIdPorEstado(ConvocatoriaConstantes.ESTADO_ACTIVO_VALUE);
			for (Convocatoria item : rpfListaCnvActiva) {
				if(item.getCnvEstadoFase()  == null){
					FacesUtil.mensajeError("Error de convocatoria, por favor comuníquese con el administrador del sistema");
					return null;
				}
				if(item.getCnvEstadoFase().intValue() == ConvocatoriaConstantes.ESTADO_FASE_REGISTRO_POSTULACION_VALUE){
					//instancio el objeto de la persona a registrarse
					rpfPersonaRegistrar = new RegistroPostulanteDto();
					//genero la lista de etnias 
					rpfListEtnias = servRpfEtnia.listarTodos();
					//genero la lista de paises 
					rpfListPaises = servRpfUbicacionJdbc.listarXjerarquia(UbicacionConstantes.TIPO_JERARQUIA_PAIS_VALUE);
					//setar el tipo de indetificacion con el valor por defecto
					rpfPersonaRegistrar.setPrsTipoIdentificacion(GeneralesConstantes.APP_ID_BASE);
					//setear un nuevo objeto conformacion mail
					rpfCorreoConformacion = new String();
					rpfCnvActiva = item;
				}else{
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.postulante.iniciar.registro.no.fase.registro")));
					return null;
				}
			}
		} catch (EtniaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.postulante.iniciar.registro.etnia.no.encontrado.exception")));
			return null;
		} catch (UbicacionDtoJdbcException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.postulante.iniciar.registro.ubicacion.dto.exception")));
			return null;
		} catch (UbicacionDtoJdbcNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.postulante.iniciar.registro.ubicacion.dto.no.ncontrado.exception")));
			return null;
		} catch (ConvocatoriaNoEncontradoException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.postulante.iniciar.registro.convocatoria.no.ncontrado.exception")));
			return null;
		} catch (ConvocatoriaException e) {
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.postulante.iniciar.registro.convocatoria.exception")));
			return null;
		}catch (Exception e){
			FacesUtil.mensajeError(e.getMessage());
			return null;
		}
		rpfPersonaRegistrar= new RegistroPostulanteDto();
//		return "iniciarRegistro";
		return "null";
	}
	
	/**
	 * Maneja de cancelacion de registro
	 * @return - cadena de navegacion a la pagina de inicio
	 */
	public String cancelarRegistro(){
		rpfPersonaRegistrar = null;
		rpfListEtnias = null;
		rpfListPaises = null;
		rpfCorreoConformacion = null;
		rpfHabilitadorDiscapacidad=true;
		rpfPersonaRegistrar= new RegistroPostulanteDto();
		return "irInicio";
		
	}
	
	/**
	 * Maneja la funcionalidad para el registro del usuario
	 * @return - cadena de navegacion a la pagina de inicio
	 * @throws IOException 
	 */
	public String registrar() throws IOException{
//		if(verificarDia()){ //Solo cuando se desea con el último número de cédula número de la cédula
		if(rpfPersonaRegistrar.getPrsMailPersonal().equals(rpfCorreoConformacion)){  
			
			HttpServletRequest request = 
			        ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes())
			                .getRequest();
			String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
			boolean verificado = VerificarRecaptcha.verificar(gRecaptchaResponse);
			if(!verificado){
				FacesUtil.mensajeError("Error al verificar el Test de Turing público y automático (ReCaptcha).");
				return null;
				
			}
			
			try {
				
				//los datos de persona son validados a través de la vista y fotmateados en el servicio de registro
				
				/******************** se formatea los datos de usuario *********************************/
				rpfPersonaRegistrar.setUsrNick(rpfPersonaRegistrar.getPrsIdentificacion()); //el nick del usuario tiene que ser el mismo de la cédula
				rpfPassword = GeneralesUtilidades.generarClave();//generacion del password
				rpfPersonaRegistrar.setUsrPassword(EncriptadorUtilidades.resumirMensaje(rpfPassword, EncriptadorUtilidades.MD5));//se encripta el password generado
				rpfPersonaRegistrar.setUsrFechaCreacion(new Timestamp((new Date()).getTime()));//se asigna la fecha actual a la fecha de creacion
				rpfPersonaRegistrar.setUsrFechaCaducidad(rpfCnvActiva.getCnvFechaFin());
				rpfPersonaRegistrar.setUsrFechaCadPass(rpfCnvActiva.getCnvFechaFin());
				rpfPersonaRegistrar.setUsrEstado(UsuarioConstantes.ESTADO_ACTIVO_VALUE); // se asigna el estado de activo al usuario
				rpfPersonaRegistrar.setUsrActiveDirectory(UsuarioConstantes.ACTIVE_DIRECTORY_NO_VALUE); // se asigna que el usuario no es del active directory
				
				/******************** se formatea los datos de usuario_rol  *********************************/
				rpfPersonaRegistrar.setUsroRol(RolConstantes.ROL_BD_POSTULANTE_VALUE);// se asigna el rol de postulante al usuario
				rpfPersonaRegistrar.setUsroEstado(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);// se asigna que el usuario_rol que esta activo
				
				//se hace la insercion el la base de datos
				servRpfRegistroPostulanteDto.anadir(rpfPersonaRegistrar);
				
				//******************************************************************************
				//************************* ACA INICIA EL ENVIO DE MAIL ************************
				//******************************************************************************
				//defino los datos para la plantilla
				Map<String, Object> parametros = new HashMap<String, Object>();
				
				parametros.put("nombres", GeneralesUtilidades.generaStringParaCorreo(rpfPersonaRegistrar.getPrsNombres().toUpperCase()));
				parametros.put("apellidos", GeneralesUtilidades.generaStringParaCorreo(rpfPersonaRegistrar.getPrsPrimerApellido().toUpperCase())+" "
								+(rpfPersonaRegistrar.getPrsSegundoApellido().toUpperCase() == null?"":GeneralesUtilidades.generaStringParaCorreo(rpfPersonaRegistrar.getPrsSegundoApellido().toUpperCase())));
				
				SimpleDateFormat sdf = new SimpleDateFormat(GeneralesConstantes.APP_FORMATO_FECHA_HORA);
				parametros.put("fechaHora", sdf.format(new Date()));
				parametros.put("pagina", GeneralesConstantes.APP_URL_SISTEMA);
				
				parametros.put("usuario", rpfPersonaRegistrar.getPrsIdentificacion());
				parametros.put("clave", rpfPassword);
				
				//lista de correos a los que se enviara el mail
				List<String> correosTo = new ArrayList<>();
				
				correosTo.add(rpfPersonaRegistrar.getPrsMailPersonal());
				
				//path de la plantilla del mail
				String pathTemplate = "/ec/edu/uce/titulacionPosgrado/jsf/velocity/plantillas/template-registro.vm";
				
				//llamo al generador de mails
				GeneradorMails genMail = new GeneradorMails();
				String mailjsonSt = genMail.generarMailJson(correosTo, null, null, GeneralesConstantes.APP_MAIL_BASE, GeneralesConstantes.APP_ASUNTO_REGITRO, 
						GeneralesConstantes.APP_USUARIO_WS, GeneralesConstantes.APP_CLAVE_WS, parametros, pathTemplate, true);
				//****envio el mail a la cola
				//cliente web service
				Client client = ClientBuilder.newClient();
				WebTarget target = client.target("http://10.20.1.94:8080/cargaMails/serviciosUce/servicioNotificaciones/cargarMail");
				MultivaluedMap<String, String> postForm = new MultivaluedHashMap<String, String>();
				postForm.add("mail", mailjsonSt);
//				String responseData = target.request().post(Entity.form(postForm),String.class);
				target.request().post(Entity.form(postForm),String.class);
				
				//******************************************************************************
				//*********************** ACA FINALIZA EL ENVIO DE MAIL ************************
				//******************************************************************************
				rpfPersonaRegistrar = null;
				rpfListEtnias = null;
				rpfListPaises = null;
				rpfCorreoConformacion = null;
				rpfHabilitadorDiscapacidad=true;
				iniciarParametros();
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo("Registro exitoso, se ha enviado el usuario y contraseña de acceso al correo registrado.");
				return "confirmarRegistro";
			
			} catch (NoSuchAlgorithmException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.postulante.registrar.encriptar.contraseña.exception")));
			} catch (RegistroPostulanteDtoValidacionException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (ValidacionMailException e) {
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.postulante.registrar.envio.mail.validacion.exception")));
			} catch (RegistroPostulanteDtoException e) {
				FacesUtil.mensajeError(e.getMessage());
			} catch (Exception e){
				FacesUtil.mensajeError(e.getMessage());
			}
			return null;
		}else{
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("Los correos electrónicos ingresados no coinciden, por favor revise.");
			return null;
		}
		
		
	}

	
	
	public void confirmarCorreoConfirmacion(AjaxBehaviorEvent event){
		if(rpfPersonaRegistrar.getPrsMailPersonal()!=null && rpfPersonaRegistrar.getPrsMailPersonal().length()!=0){
			if(!rpfPersonaRegistrar.getPrsMailPersonal().equals(rpfCorreoConformacion)){
				verificarBoton=true;
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.postulante.registrar.confirmacion.correo.error")));
			}else{
				verificarBoton=false;
			}	
		}else{
			verificarBoton=true;
		}
		if(verificarConvocatoria){
			verificarBoton=true;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No puede continuar, el sistema no está en fase de registro.");
		}
		
	}
	
	public void confirmarCorreo(AjaxBehaviorEvent event){
		
		if(rpfCorreoConformacion!=null && rpfCorreoConformacion.length()!=0){
			if(!rpfPersonaRegistrar.getPrsMailPersonal().equals(rpfCorreoConformacion)){
				verificarBoton=true;
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Registro.postulante.registrar.confirmacion.correo.error")));
			}else{
				verificarBoton=false;
			}	
		}else{
			verificarBoton=true;
		}
		if(verificarConvocatoria){
			verificarBoton=true;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No puede continuar, el sistema no está en fase de registro.");
		}
	}
	
	
	//****************************************************************/
	//******************* METODOS AUXILIARES *************************/
	//****************************************************************/
	
	/**
	 * Maneja la navegación hacia la pantalla de error cuando el digito de verificacion es diferente al planificado
	 * @return - cadena de navegacion a la pagina de inicio
	 */
	public boolean verificarDia(){
		String rpfUltimoDigito = rpfPersonaRegistrar.getPrsIdentificacion().substring(rpfPersonaRegistrar.getPrsIdentificacion().length()-1);
		Calendar c = Calendar.getInstance();
		int diaDeLaSemana = c.get(Calendar.DAY_OF_WEEK);
		if(diaDeLaSemana!=1 || diaDeLaSemana!=7){
			switch (diaDeLaSemana) {
		    case Calendar.WEDNESDAY:
		        if(rpfUltimoDigito.equals("1")
		        		|| rpfUltimoDigito.equals("2")){
		        	return true;
		        }
		    	break;
		    case Calendar.THURSDAY:
		    	if(rpfUltimoDigito.equals("3")
		        		|| rpfUltimoDigito.equals("4")){
		        	return true;
		        }
		    	break;
		    case Calendar.FRIDAY:
		    	if(rpfUltimoDigito.equals("5")
		        		|| rpfUltimoDigito.equals("6")){
		        	return true;
		        }
		    	break;
		    case Calendar.SATURDAY:
		    	if(rpfUltimoDigito.equals("7")
		        		|| rpfUltimoDigito.equals("8")){
		        	return true;
		        }
		    	break;
		    case Calendar.SUNDAY:
		    	if(rpfUltimoDigito.equals("9")
		        		|| rpfUltimoDigito.equals("0")){
		        	return true;
		        }
		    	break;
		    case Calendar.MONDAY:
		        return true;
		    default:
		    	verificarCedula=false;
				verificarClic=1;
				iniciarParametros();
				return false;
			}
		}
		return false;
	}
	
	
	
	public void verificarConvocatoria(){
		try {
			List<Convocatoria> cnvAux = servRpfConvocatoria.buscarConvocatoriasPorIdPorEstado(ConvocatoriaConstantes.ESTADO_FASE_REGISTRO_POSTULACION_VALUE);
			if(cnvAux.size()==0){
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeError("No puede continuar, el sistema no está en fase de registro.");
				verificarConvocatoria=true;
			}else{
				verificarConvocatoria=false;
			}
		} catch (Exception e) {
			verificarConvocatoria=true;
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError("No puede continuar, el sistema no está en fase de registro.");
		}
	}
	//****************************************************************/
	//******************* GETTERS Y SETTERS **************************/
	//****************************************************************/
	public RegistroPostulanteDto getRpfPersonaRegistrar() {
		return rpfPersonaRegistrar;
	}

	public void setRpfPersonaRegistrar(RegistroPostulanteDto rpfPersonaRegistrar) {
		this.rpfPersonaRegistrar = rpfPersonaRegistrar;
	}

	public List<Etnia> getRpfListEtnias() {
		rpfListEtnias = rpfListEtnias==null?(new ArrayList<Etnia>()):rpfListEtnias;
		return rpfListEtnias;
	}

	public void setRpfListEtnias(List<Etnia> rpfListEtnias) {
		this.rpfListEtnias = rpfListEtnias;
	}

	public List<UbicacionDto> getRpfListPaises() {
		rpfListPaises = rpfListPaises==null?(new ArrayList<UbicacionDto>()):rpfListPaises;
		return rpfListPaises;
	}

	public void setRpfListPaises(List<UbicacionDto> rpfListPaises) {
		this.rpfListPaises = rpfListPaises;
	}

	public String getRpfCorreoConformacion() {
		return rpfCorreoConformacion;
	}

	public void setRpfCorreoConformacion(String rpfCorreoConformacion) {
		this.rpfCorreoConformacion = rpfCorreoConformacion;
	}

	public boolean isRpfHabilitadorDiscapacidad() {
		return rpfHabilitadorDiscapacidad;
	}

	public void setRpfHabilitadorDiscapacidad(boolean rpfHabilitadorDiscapacidad) {
		this.rpfHabilitadorDiscapacidad = rpfHabilitadorDiscapacidad;
	}


	public boolean isVerificarBoton() {
		return verificarBoton;
	}


	public void setVerificarBoton(boolean verificarBoton) {
		this.verificarBoton = verificarBoton;
	}


	public boolean isVerificarCedula() {
		return verificarCedula;
	}


	public void setVerificarCedula(boolean verificarCedula) {
		this.verificarCedula = verificarCedula;
	}


	public Integer getVerificarClic() {
		return verificarClic;
	}


	public void setVerificarClic(Integer verificarClic) {
		this.verificarClic = verificarClic;
	}


	public boolean isVerificarConvocatoria() {
		return verificarConvocatoria;
	}


	public void setVerificarConvocatoria(boolean verificarConvocatoria) {
		this.verificarConvocatoria = verificarConvocatoria;
	}

	
	
	
}
