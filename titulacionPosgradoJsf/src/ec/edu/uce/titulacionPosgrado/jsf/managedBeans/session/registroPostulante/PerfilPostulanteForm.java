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
   
 ARCHIVO:     PerfilPostulanteForm.java	  
 DESCRIPCION: Bean de sesion que maneja el perfil del postulante.
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 21/02/2018                         Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.managedBeans.session.registroPostulante;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudiantePostuladoJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.TramiteTituloDto;
import ec.edu.uce.titulacionPosgrado.ejb.dtos.UbicacionDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.EtniaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaValidacionException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UbicacionDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.UbicacionDtoJdbcNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.EtniaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.FichaEstudianteServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.PersonaServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.EstudiantePostuladoDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.TramiteTituloDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.UbicacionDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.TramiteTituloConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.UbicacionConstantes;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.GeneralesUtilidades;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Etnia;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Persona;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Usuario;
import ec.edu.uce.titulacionPosgrado.jsf.utilidades.FacesUtil;



/**
 * Clase (session bean) PerfilPostulanteForm.
 * Bean de sesion que maneja el perfil del postulante.
 * @author mmfernandez.
 * @version 1.0
 */

@ManagedBean(name="perfilPostulanteForm")
@SessionScoped
public class PerfilPostulanteForm implements Serializable {
	private static final long serialVersionUID = -6676596605312607279L;
	private Usuario ppfUsuario;
	private Persona ppfPersona;
	private Integer ppfNacionalidad;
	private List<UbicacionDto> ppfListUbicacionPaises;
	private List<Etnia> ppfListEtnias;
	private TramiteTituloDto ppfTramiteTituloDto;
	private DefaultStreamedContent image;
	
	@EJB
	private FichaEstudianteServicio servPpfFichaEstudiante;
	
	@EJB
	private PersonaServicio servPpfPersona;
	
	@EJB
	private UbicacionDtoServicioJdbc servPpfUbicacionDto;
	
	@EJB
	private EtniaServicio servPpfEtnia;
	
	@EJB
	private EstudiantePostuladoDtoServicioJdbc servEstudiantePostuladoDtoServicioJdbc;
	
	@EJB
	private TramiteTituloDtoServicioJdbc servTramiteTituloDtoServicioJdbc;
	
	
	//**************************************************************//
	//***************** METODOS DE NEGOCIO *************************//
	//**************************************************************//
	/**
	 * Maneja de inicio del bean
	 * @return - cadena de navegacion a la pagina de registro
	 */
	public String iniciarPerfil(Usuario usuario){
		ppfUsuario = new Usuario();
		ppfUsuario= usuario;
			//verifico si el usuario es o no del active directory
//			if(usuario.getUsrActiveDirectory().intValue() == UsuarioConstantes.ACTIVE_DIRECTORY_SI_VALUE){//si es del active es 0
//				FacesUtil.mensajeInfo(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Perfil.postulante.error.active")));
//				return null;
//			}
			try {
				//verifico si el usuario terminó el proceso de titulacion
				EstudiantePostuladoJdbcDto estudiante = new EstudiantePostuladoJdbcDto(); 
				estudiante = servEstudiantePostuladoDtoServicioJdbc.buscaTipoTramiteXIdentificacion(usuario.getUsrIdentificacion());
				if(GeneralesUtilidades.estaEntre(estudiante.getTrttEstadoProceso(), TramiteTituloConstantes.ESTADO_PROCESO_APROBADO_PROCESO_TITULACION_VALUE, 
						TramiteTituloConstantes.ESTADO_PROCESO_EMISION_TITULO_DEFENSA_ORAL_EDICION_VALUE)){
					FacesUtil.limpiarMensaje();
					FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Perfil.postulante.error.bloqueo.aprobado.proceso")));
					return null;
				}
			} catch (Exception e) {
			}
		
			
			
			byte[] data;
			try {
				ppfPersona = servPpfPersona.buscarPorIdentificacion(ppfUsuario.getUsrIdentificacion());
				
				String[] partesNotas =  ppfPersona.getPrsUbicacionFoto().split("\\.");
				Path path = Paths.get(GeneralesConstantes.APP_PATH_ARCHIVOS+"fotografias/"+"Fotografia_"+ppfPersona.getPrsIdentificacion()+"."+partesNotas[1]);
//				Path path = Paths.get("d:/archivos/"+"Fotografia_1714991989.jpg");
				data = Files.readAllBytes(path);
				image = new DefaultStreamedContent(new ByteArrayInputStream(data), Files.probeContentType(path));
			} catch (IOException e) {
			} catch (Exception e) {
			}
			
			
			
			
		return "irEditarPerfilPostulante";
	}
	
	/**
	 * Maneja de cancelacion de la visualizacion
	 * @return - cadena de navegacion a la pagina de inicio
	 */
	public String cancelarVisualizacion(){
		ppfPersona = null;
		return "irInicio";
	}
	
	
	public String irEditarPerfil(){
		
		
		byte[] data;
		try {
			ppfPersona = servPpfPersona.buscarPorIdentificacion(ppfUsuario.getUsrIdentificacion());
			
			String[] partesNotas =  ppfPersona.getPrsUbicacionFoto().split("\\.");
			Path path = Paths.get(GeneralesConstantes.APP_PATH_ARCHIVOS+"fotografias/"+"Fotografia_"+ppfUsuario.getUsrIdentificacion()+"."+partesNotas[1].toString());//			Path path = Paths.get("D:\\archivos\\Fotografia_PRUEBAS123.jpg");
//			Path path = Paths.get("d:/archivos/"+"Fotografia_1714991989.jpg");
			data = Files.readAllBytes(path);
			image = new DefaultStreamedContent(new ByteArrayInputStream(data), Files.probeContentType(path));
		} catch (IOException e) {
		} catch (Exception e) {
			
		}
		try {
			ppfListUbicacionPaises =  servPpfUbicacionDto.listarXjerarquia(UbicacionConstantes.TIPO_JERARQUIA_PAIS_VALUE);
			ppfListEtnias = servPpfEtnia.listarTodos();
		} catch (UbicacionDtoJdbcException| UbicacionDtoJdbcNoEncontradoException e) {
		} catch (EtniaNoEncontradoException e) {
		}
		
			return "irEditarPerfil";
	}
	

	public void cargarImagen(FileUploadEvent event){
		
		byte[] file = event.getFile().getContents();
		String contentType = event.getFile().getContentType();
		String extension =  event.getFile().getFileName().substring(event.getFile().getFileName().lastIndexOf('.'), event.getFile().getFileName().length()); ;
//		ppfPersona.setPrsFoto(file);
		ppfPersona.setPrsUbicacionFoto(GeneralesConstantes.APP_PATH_ARCHIVOS+"fotografias/"+"Fotografia_"+ppfUsuario.getUsrIdentificacion()+extension) ;
		image = new DefaultStreamedContent(new ByteArrayInputStream(file), contentType);
		try {
			File f = new File(GeneralesConstantes.APP_PATH_ARCHIVOS + "fotografias/"+"Fotografia_"
					+ ppfUsuario.getUsrIdentificacion() + extension);
			if(f.exists() && !f.isDirectory()) { 
			    f.delete();
			}
				FileOutputStream fos = new FileOutputStream(GeneralesConstantes.APP_PATH_ARCHIVOS + "fotografias/"+"Fotografia_"
						+ ppfUsuario.getUsrIdentificacion() + extension); 

			fos.write(file);
			fos.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} catch (Exception e) {
		}
	}
	
	/**
	 * Maneja de cancelacion de la edición
	 * @return - cadena de navegacion a la pagina de inicio
	 */
	public String cancelarEdicion(){
		ppfPersona = null;
		ppfListEtnias = null;
		return "irInicio";
	}
	
	/**
	 * Maneja el guardado de datos de la edicion
	 * @return - cadena de navegacion a la pagina de inicio
	 */
	public String guardar(){
		try {
			
			
	        if (GeneralesUtilidades.quitaEspacios(ppfPersona.getPrsPrimerApellido()).length()==0 &&
	        		GeneralesUtilidades.quitaEspacios(ppfPersona.getPrsNombres()).length()==0){
	        	FacesUtil.limpiarMensaje();
	        	FacesUtil.mensajeError("No debe guardar espacios en blanco");
	        	return null;
	        }else{
	        	ppfPersona.setPrsPrimerApellido(ppfPersona.getPrsPrimerApellido());
				servPpfPersona.editar(ppfPersona);
				FacesUtil.limpiarMensaje();
				FacesUtil.mensajeInfo(GeneralesConstantes.APP_MSG_ACTUALIZAR_REGISTRO_EXITOSO);
	        }
		} catch (PersonaValidacionException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(e.getMessage());
			return null;
		} catch (PersonaNoEncontradoException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Perfil.postulante.error.actualizar")));
			return null;
		} catch (PersonaException e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Perfil.postulante.error.actualizar")));
			return null;
		} catch (Exception e) {
			FacesUtil.limpiarMensaje();
			FacesUtil.mensajeError(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Perfil.postulante.error.actualizar")));
			return null;
		}
		return "irInicio";
		
	}
	
	

	//**************************************************************//
	//************** METODOS ACCESORES Y MUTADORES *****************//
	//**************************************************************//
	public Persona getPpfPersona() {
		return ppfPersona;
	}

	public void setPpfPersona(Persona prfPstFrmPersona) {
		this.ppfPersona = prfPstFrmPersona;
	}

	public Integer getPpfNacionalidad() {
		return ppfNacionalidad;
	}

	public void setPpfNacionalidad(Integer ppfNacionalidad) {
		this.ppfNacionalidad = ppfNacionalidad;
	}

	public List<UbicacionDto> getPpfListUbicacionPaises() {
		ppfListUbicacionPaises = ppfListUbicacionPaises==null?(new ArrayList<UbicacionDto>()):ppfListUbicacionPaises;
		return ppfListUbicacionPaises;
	}

	public void setPpfListUbicacionPaises(List<UbicacionDto> ppfListUbicacionPaises) {
		this.ppfListUbicacionPaises = ppfListUbicacionPaises;
	}
	
	public List<Etnia> getPpfListEtnias() {
		ppfListEtnias = ppfListEtnias == null ?(new ArrayList<Etnia>()):ppfListEtnias;
		return ppfListEtnias;
	}

	public void setPpfListEtnias(List<Etnia> ppfListEtnias) {
		this.ppfListEtnias = ppfListEtnias;
	}

	public TramiteTituloDto getPpfTramiteTituloDto() {
		return ppfTramiteTituloDto;
	}

	public void setPpfTramiteTituloDto(TramiteTituloDto ppfTramiteTituloDto) {
		this.ppfTramiteTituloDto = ppfTramiteTituloDto;
	}

	public DefaultStreamedContent getImage() {
		return image;
	}

	public void setImage(DefaultStreamedContent image) {
		this.image = image;
	}
	
	
	
}
