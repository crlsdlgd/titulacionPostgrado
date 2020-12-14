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
   
 ARCHIVO:     WebService.java	  
 DESCRIPCION: Clase encargada de presentar el resultado del WebService
  para enviar los datos a EmisionTitulo. 
 *************************************************************************
                           	MODIFICACIONES
                            
 FECHA         		     AUTOR          					COMENTARIOS
 12-JUN-2017		 Daniel Albuja 			          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.jsf.jaxrs;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteTitulacionJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaEstudianteException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.FichaEstudianteNoEncontradoException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.PersonaDtoJdbcException;
import ec.edu.uce.titulacionPosgrado.ejb.excepciones.TramiteTituloException;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.FichaEstudianteServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.TramiteTituloServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.EstudianteEmisionTituloDtoServicioJdbc;



/**
 * Clase WebService.
 * Clase encargada de presentar el WebService para enviar los datos a EmisionTitulo. 
 * @author dalbuja.
 * @version 1.0
 */
@Path("/consulta")
public class WebService   {
	
	@EJB
	private EstudianteEmisionTituloDtoServicioJdbc srvEstudianteEmisionTituloDtoServicioJdbc;
	@EJB
	private TramiteTituloServicio srvTramiteTituloServicio;
	@EJB
	private FichaEstudianteServicio srvFichaEstudianteServicio;
	
	@SuppressWarnings("null")
	@GET
	@Path("/titulados/{param}")
	@Produces("application/json")
	public Response enviarResultados(@PathParam("param") String pwd) {
		if(pwd.equals("Dtic2017")){
//			System.out.println("INICIO MIGRACION DATOS WEB SERVICE");
//			List<EstudianteTitulacionJdbcDto> listaRetorno = new ArrayList<EstudianteTitulacionJdbcDto>();
//			try {
//				listaRetorno=srvEstudianteEmisionTituloDtoServicioJdbc.listarPersonas();
//				if(listaRetorno!=null || listaRetorno.size()!=0){
//					Gson gson = new Gson();
//					String jsonListaTitulados = gson.toJson(listaRetorno);
//					for (EstudianteTitulacionJdbcDto item : listaRetorno) {
//						try {
//							srvTramiteTituloServicio.cambiarEstadoProcesoMigradoEmisionTitulo(item.getTrttId());
//						} catch (TramiteTituloException e) {
//						}
//					}
//					return Response.status(200).entity(jsonListaTitulados).build();	
//				}else{
					return Response.status(204).build();
//				}
//			} catch (PersonaDtoJdbcException e) {
//				return Response.status(204).build();
//			}	
		}else{
			return Response.status(401).build();
		}
	}
	
	
	@GET
	@Path("/retorno/{paramCedula}/{paramCncrId}")
	@Produces("application/json")
	public Response recepcionXEditar(@PathParam("paramCedula") String cedula, @PathParam("paramCncrId") Integer cncrId) {
		if(cedula!=null && cncrId !=null){
			boolean resultado = true;
			try {
				resultado=srvTramiteTituloServicio.cambiarEstadoProcesoMigradoEmisionTituloRetornado(cedula,cncrId);
			} catch (TramiteTituloException e) {
				resultado = false;
			}
			if(resultado){
				return Response.status(200).build();	
			}else{
				return Response.status(512).build();
			}
		}else{
			return Response.status(512).build();
		}
	}
	
	@SuppressWarnings("null")
	@GET
	@Path("/editados/{param}")
	@Produces("application/json")
	public Response enviarEditados(@PathParam("param") String pwd) {
		if(pwd.equals("Dtic2017")){
			List<EstudianteTitulacionJdbcDto> listaRetorno = new ArrayList<EstudianteTitulacionJdbcDto>();
			try {
				listaRetorno=srvEstudianteEmisionTituloDtoServicioJdbc.listarPersonasEditadas();
				if(listaRetorno!=null || listaRetorno.size()!=0){
					Gson gson = new Gson();
					String jsonListaTitulados = gson.toJson(listaRetorno);
					for (EstudianteTitulacionJdbcDto item : listaRetorno) {
						try {
							srvTramiteTituloServicio.cambiarEstadoProcesoMigradoEmisionTitulo(item.getTrttId());
						} catch (TramiteTituloException e) {
						}
					}
					return Response.status(200).entity(jsonListaTitulados).build();	
				}else{
					return Response.status(204).build();
				}
			} catch (PersonaDtoJdbcException e) {
				return Response.status(204).build();
			}	
		}else{
			return Response.status(401).build();
		}
	}
	
	
	@GET
	@Path("/nuevo/{paramCedula}/{param}")
	@Produces("application/json")
	public Response consultaPersonaFichaEstudianteXCedula(@PathParam("paramCedula") String cedula, @PathParam("param") String pwd) {
		boolean resultado = false;
		if(cedula!=null && pwd !=null){
			try {
				resultado=srvFichaEstudianteServicio.buscarFichaEstudiantePorPersonaId(cedula);
			} catch (FichaEstudianteNoEncontradoException | FichaEstudianteException e) {
			}
		}
		if(resultado){
			return Response.status(200).build();	
		}else{
			return Response.status(512).build();
		}
	}
	
	
	@SuppressWarnings("null")
	@GET
	@Path("/migrados/{param}")
	@Produces("application/json")
	public Response revisarMigrados(@PathParam("param") String pwd) {
		if(pwd.equals("Dtic2017")){
//			System.out.println("REVISION MIGRACION DATOS WEB SERVICE");
//			List<EstudianteTitulacionJdbcDto> listaRetorno = new ArrayList<EstudianteTitulacionJdbcDto>();
//			try {
//				listaRetorno=srvEstudianteEmisionTituloDtoServicioJdbc.listarPersonasRevision();
//				if(listaRetorno!=null || listaRetorno.size()!=0){
//					Gson gson = new Gson();
//					String jsonListaTitulados = gson.toJson(listaRetorno);
//					return Response.status(200).entity(jsonListaTitulados).build();	
//				}else{
//					return Response.status(204).build();
//				}
//			} catch (PersonaDtoJdbcException e) {
				return Response.status(204).build();
//			}	
		}else{
			return Response.status(401).build();
		}
	}
	
}
