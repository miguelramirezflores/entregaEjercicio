package interfaz;

import java.util.List;

import modelo.Concesionario;
import modelo.controladores.ConcesionarioControlador;

public class PruebaConexion {

	public static void main(String[] args) {
		List<Concesionario> concesionarios = ConcesionarioControlador.getControlador().findAll();
		for (Concesionario concesionario : concesionarios) {
			System.out.println(concesionario.getNombre());
		}
	}

}
