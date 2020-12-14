/**************************************************************************
 *                (c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción distribución no autorizada de este programa, 
 * o cualquier porción de  él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán  procesadas con el grado  máximo contemplado 
 * por la ley.
 
 ************************************************************************* 
   
 ARCHIVO:     ScheduleServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar los procesos del schedule. 
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 27-12-2017       	 Daniel Albuja                          Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.schedule.impl;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timer;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import ec.edu.uce.titulacionPosgrado.ejb.dtos.EstudianteTitulacionJdbcDto;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces.TramiteTituloServicio;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.jdbc.interfaces.EstudianteEmisionTituloDtoServicioJdbc;
import ec.edu.uce.titulacionPosgrado.ejb.servicios.schedule.interfaces.ScheduleServicio;
import ec.edu.uce.titulacionPosgrado.ejb.utilidades.constantes.GeneralesConstantes;
/**
 * Clase (Implementacion) ScheduleServicioImpl.
 * Bean sin estado encargado de gestionar los procesos del schedule.
 * @author dalbuja.
 * @version 1.0
 */

@Singleton
@Startup
public class ScheduleServicioImpl implements ScheduleServicio{
	
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SOURCE_EMISION_TITULO)
	
	
	@EJB private EstudianteEmisionTituloDtoServicioJdbc srvEstudianteEmisionTituloDtoServicioJdbc;
	@EJB
	private TramiteTituloServicio srvTramiteTituloServicio;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) 
	@Override
//	@Schedule(second="00", minute="27", hour="15", persistent=false)
	@Schedule(second="00", minute="37", hour="22,23", persistent=false)
	public void procesoMigracionEmisionTitulo(Timer timer) {
		System.out.println("INICIO MIGRACION");
		
		try {
			List<EstudianteTitulacionJdbcDto> listaRetorno = new ArrayList<EstudianteTitulacionJdbcDto>();
			listaRetorno=srvEstudianteEmisionTituloDtoServicioJdbc.listarPersonas();
			if(listaRetorno.size()!=0){
				for (EstudianteTitulacionJdbcDto item : listaRetorno) {
					try {
						srvEstudianteEmisionTituloDtoServicioJdbc.crearActualizarPersona(item);	
						srvEstudianteEmisionTituloDtoServicioJdbc.crearActualizarFichaEstudiante(item);
						srvTramiteTituloServicio.cambiarEstadoProcesoMigradoEmisionTitulo(item.getTrttId());
//						System.out.println(item.getPrsIdentificacion());
//						break;
					} catch (Exception e) {
					}
				}
			}
		} catch (Exception e) {
		}
		System.out.println("FIN MIGRACION");
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) 
	@Override
//	@Schedule(second="00", minute="35", hour="11", persistent=false)
//	@Schedule(second="00", minute="30", hour="11,15", persistent=false )
	public void procesoMigracionEmisionTituloUnico(Timer timer) {
		System.out.println("INICIO MIGRACION REVISION");
		try {
			List<EstudianteTitulacionJdbcDto> listaRetorno = new ArrayList<EstudianteTitulacionJdbcDto>();
			listaRetorno=srvEstudianteEmisionTituloDtoServicioJdbc.listarPersonasMigradasPorFecha();
			if(listaRetorno.size()!=0){
				for (EstudianteTitulacionJdbcDto item : listaRetorno) {
					try {
						srvEstudianteEmisionTituloDtoServicioJdbc.crearActualizarPersona(item);	
						srvEstudianteEmisionTituloDtoServicioJdbc.crearActualizarFichaEstudiante(item);
//							srvTramiteTituloServicio.cambiarEstadoProcesoMigradoEmisionTitulo(item.getTrttId());
//							System.out.println(item.getPrsIdentificacion());
//							break;	
					} catch (Exception e) {
					}
					
				}
			}
		} catch (Exception e) {
		}
		System.out.println("FIN MIGRACION REVISION");
	}
}







	

