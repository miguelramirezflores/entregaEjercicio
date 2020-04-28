package modelo.controladores;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import modelo.Cliente;
import modelo.Controlador;
import modelo.Venta;





public class VentaControlador extends Controlador {

	private static VentaControlador controlador = null;

	public VentaControlador () {
		super(Venta.class, "coches");
	}
	
	/**
	 * 
	 * @return
	 */
	public static VentaControlador getControlador () {
		if (controlador == null) {
			controlador = new VentaControlador();
		}
		return controlador;
	}

	/**
	 *  
	 */
	public Venta find (int id) {
		return (Venta) super.find(id);
	}

	
	/**
	 * 
	 * @return
	 */
	public Venta findFirst () {
		try {
			EntityManager em = getEntityManagerFactory().createEntityManager();
			Query q = em.createQuery("SELECT c FROM Venta c order by c.id", Venta.class);
			Venta resultado = (Venta) q.setMaxResults(1).getSingleResult();
			em.close();
			return resultado;
		}
		catch (NoResultException nrEx) {
			return null;
		}
	}

	
	
	
	/**
	 * 
	 * @return
	 */
	public Venta findLast () {
		try {
			EntityManager em = getEntityManagerFactory().createEntityManager();
			Query q = em.createQuery("SELECT c FROM Venta c order by c.id desc", Venta.class);
			Venta resultado = (Venta) q.setMaxResults(1).getSingleResult();
			em.close();
			return resultado;
		}
		catch (NoResultException nrEx) {
			return null;
		}
	}

	
	
	
	/**
	 * 
	 * @return
	 */
	public Venta findNext (Venta c) {
		try {
			EntityManager em = getEntityManagerFactory().createEntityManager();
			Query q = em.createQuery("SELECT c FROM Venta c where c.id > :idActual order by c.id", Venta.class);
			q.setParameter("idActual", c.getId());
			Venta resultado = (Venta) q.setMaxResults(1).getSingleResult();
			em.close();
			return resultado;
		}
		catch (NoResultException nrEx) {
			return null;
		}
	}

	
	
	
	/**
	 * 
	 * @return
	 */
	public Venta findPrevious (Venta c) {
		try {
			EntityManager em = getEntityManagerFactory().createEntityManager();
			Query q = em.createQuery("SELECT c FROM Venta c where c.id < :idActual order by c.id desc", Venta.class);
			q.setParameter("idActual", c.getId());
			Venta resultado = (Venta) q.setMaxResults(1).getSingleResult();
			em.close();
			return resultado;
		}
		catch (NoResultException nrEx) {
			return null;
		}
	}

	
	
	
	public List<Venta> findAll () {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		Query q = em.createQuery("SELECT c FROM Venta c", Venta.class);
		List<Venta> resultado = (List<Venta>) q.getResultList();
		em.close();
		return resultado;
	}
	

	
	public static String toString (Cliente concesionario) {
		return "Id: " + concesionario.getId() + " - Nombre: " + concesionario.getNombre() +
				 " - Localidad: " + concesionario.getLocalidad(); 
	}

	
	
}
