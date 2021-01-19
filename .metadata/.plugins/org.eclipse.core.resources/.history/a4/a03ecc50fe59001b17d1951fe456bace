/**************************************************************************
 *                (c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y está protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción o distribución no autorizada de este programa, 
 * o cualquier porción de él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán procesadas con el grado máximo contemplado 
 * por la ley.
 
 ************************************************************************* 
   
 ARCHIVO:     AutoridadServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre los programas.  
 *************************************************************************
                               MODIFICACIONES
                            
 FECHA                      AUTOR                              COMENTARIOS
 12/1/2021             Carlos Delgado                         Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.titulacionPosgrado.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Carrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.ConfiguracionCarrera;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Duracion;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Modalidad;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.TipoFormacion;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.TipoSede;
import ec.edu.uce.titulacionPosgrado.jpa.entidades.publico.Vigencia;

public interface ProgramaServicio {
	//El tipo de sede, Tipo de Formacion se filtran x defecto por el regimen academico 2009 y 2013 (rgacId in (1,2))
	public List<TipoSede> ListarTodosTipoSede();
	public List<TipoFormacion> ListarTodosTipoFormacion();
	public List<Modalidad> ListarTodosModalidad();
	public List<Vigencia> ListarTodosVigencia();
	public List<Duracion> ListarTodosDuracion();
	public void guardarPrograma(Carrera crr, ConfiguracionCarrera cncr);
}
