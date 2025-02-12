/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import server.db.DBBroker;
import server.model.Profesor;
import server.model.Student;
import server.service.ProfesorService;
import server.service.StudentService;

/**
 *
 * @author Korisnik
 */
// controller = obrada klijentskih zahteva
public class Controller {
    private Socket clientSocket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    
    private StudentService studentService;
    private ProfesorService profesorService;
    
    public Controller(Socket socket) throws IOException {
        ois = new ObjectInputStream(socket.getInputStream());
        oos = new ObjectOutputStream(socket.getOutputStream());
        studentService = new StudentService();
        profesorService = new ProfesorService();
    }
    
    // obrada klijentskih zahteva
    // ucitaj zahtev
    // izvrsi zahtev
    // posalji odogvor
    public void comunicate() throws IOException, ClassNotFoundException {
        System.out.println("(server) cekam da mi klijent posalje operaciju"
                    + "da bih znao sta da radim");
        // ucitaj zahtev
        String operation = ois.readUTF();
        

        // izvrsi zahtev
        switch (operation) {
            case "getAllStudents":
                List<Student> students = studentService.getAllStudents();
                // send Response
                oos.writeObject(students);
                oos.flush();
                System.out.println("poslao sam studente klijentu");
                break;
            case "updateStudent":
                Student student = (Student) ois.readObject();
                boolean successStudentUpdate = studentService.updateStudent(student);
                oos.writeBoolean(successStudentUpdate);
                oos.flush();
                break;
            case "deleteStudent": 
                Student studentDelete = (Student) ois.readObject();
                boolean successDeleteStudent = studentService.deleteStudent(studentDelete);
                oos.writeBoolean(successDeleteStudent);
                oos.flush();
            // TODO izmeniti ovo da ide preko profesor service klase
                // i dodati metode (pronadji profesore po imenu)
                // obrisi profesora
                // update profesor
            case "getAllProfesors":
                List<Profesor> profesors = DBBroker.getIsntance().getAllProfesors();
                // posalji odogvor
                oos.writeObject(profesors);
                oos.flush();
                System.out.println("poslao sam profesore klijentu");
                break;
            case "addProfesor":
                Profesor profesor = (Profesor) ois.readObject();
                boolean success = DBBroker.getIsntance().addProfesor(profesor);
                // posalji odogvor
                oos.writeBoolean(success);
                oos.flush();
                break;
                case "findProfesorsByName":
                String name = ois.readUTF(); // Preuzimanje imena profesora
                List<Profesor> profesorsByName = profesorService.findProfesorsByName(name);
                oos.writeObject(profesorsByName);
                oos.flush();
                break;

            // Brisanje profesora
            case "deleteProfesor":
                int profesorIdToDelete = ois.readInt(); // Preuzimanje ID-a profesora za brisanje
                boolean successDeleteProfesor = profesorService.deleteProfesor(profesorIdToDelete);
                oos.writeBoolean(successDeleteProfesor);
                oos.flush();
                break;

            // Ažuriranje profesora
            case "updateProfesor":
                Profesor profesorToUpdate = (Profesor) ois.readObject();
                boolean successUpdateProfesor = profesorService.updateProfesor(profesorToUpdate);
                oos.writeBoolean(successUpdateProfesor);
                oos.flush();
                break;

            case "Exit":
                System.out.println("klijent se diskonektovao");
                break;
            default:
                throw new AssertionError();
        }
    }
}
