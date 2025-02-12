/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.service;

import java.util.List;
import server.db.DBBroker;
import server.model.Profesor;

/**
 *
 * @author Korisnik
 */
public class ProfesorService {
    

    // Metoda za pronalaženje profesora po imenu
    public List<Profesor> findProfesorsByName(String name) {
        DBBroker.getIsntance().connect();
        List<Profesor> profesors = DBBroker.getIsntance().getProfesorsByName(name);
        DBBroker.getIsntance().closeConnection();
        return profesors;
    }

    // Metoda za brisanje profesora
    public boolean deleteProfesor(int id) {
        DBBroker.getIsntance().connect();
        boolean success = DBBroker.getIsntance().deleteProfesor(id);
        DBBroker.getIsntance().closeConnection();
        return success;
    }

    // Metoda za ažuriranje podataka profesora
    public boolean updateProfesor(Profesor profesor) {
        DBBroker.getIsntance().connect();
        boolean success = DBBroker.getIsntance().updateProfesor(profesor);
        DBBroker.getIsntance().closeConnection();
        return success;
    }


}
