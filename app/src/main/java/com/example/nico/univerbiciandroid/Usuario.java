package com.example.nico.univerbiciandroid;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by nico on 22-05-16.
 */
public class Usuario {
    int idUser;
    String nombreUser;
    String apellidoUser;
    String nicknameUser;
    String passUser;
    String emailUser;
    String direccionUser;
    int sexoUser;
    int telefonoUser;
    Date fechaNacUser;
    int idEst;

    ArrayList<Usuario> usuarios;

    public Usuario() {
        idEst = 1;
    }

    public Usuario(String nombreUser, String apellidoUser, String nicknameUser, String passUser, String emailUser, String direccionUser, int sexoUser, int telefonoUser, Date fechaNacUser) {
        this.nombreUser = nombreUser;
        this.apellidoUser = apellidoUser;
        this.nicknameUser = nicknameUser;
        this.passUser = passUser;
        this.emailUser = emailUser;
        this.direccionUser = direccionUser;
        this.sexoUser = sexoUser;
        this.telefonoUser = telefonoUser;
        this.fechaNacUser = fechaNacUser;
        idEst=1;
    }


    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getNombreUser() {
        return nombreUser;
    }

    public void setNombreUser(String nombreUser) {
        this.nombreUser = nombreUser;
    }

    public String getApellidoUser() {
        return apellidoUser;
    }

    public void setApellidoUser(String apellidoUser) {
        this.apellidoUser = apellidoUser;
    }

    public String getNicknameUser() {
        return nicknameUser;
    }

    public void setNicknameUser(String nicknameUser) {
        this.nicknameUser = nicknameUser;
    }

    public String getPassUser() {
        return passUser;
    }

    public void setPassUser(String passUser) {
        this.passUser = passUser;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getDireccionUser() {
        return direccionUser;
    }

    public void setDireccionUser(String direccionUser) {
        this.direccionUser = direccionUser;
    }

    public int getSexoUser() {
        return sexoUser;
    }

    public void setSexoUser(int sexoUser) {
        this.sexoUser = sexoUser;
    }

    public int getTelefonoUser() {
        return telefonoUser;
    }

    public void setTelefonoUser(int telefonoUser) {
        this.telefonoUser = telefonoUser;
    }

    public int getIdEst() {
        return idEst;
    }

    public void setIdEst(int idEst) {
        this.idEst = idEst;
    }

    public Date getFechaNacUser() {
        return fechaNacUser;
    }

    public void setFechaNacUser(Date fechaNacUser) {
        this.fechaNacUser = fechaNacUser;
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
