package com.example.nico.univerbiciandroid;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nico on 21-05-16.
 */
public class Estacionamiento {
    int idEstacionamiento;
    String nombreEstacionamiento;
    int cantidadEstacionamiento;
    int ocupados;
    float lat;
    float lon;
    LatLng ubicacion;

    public Estacionamiento() {

    }

    public Estacionamiento(JSONObject objJson) throws JSONException {
        this.idEstacionamiento = objJson.getInt("idestacionamiento");
        this.nombreEstacionamiento = objJson.getString("nombreEstacionamiento");
        this.cantidadEstacionamiento = objJson.getInt("capacidad");
        this.ocupados = objJson.getInt("ocupados");
        this.lat = objJson.getInt("ubi_x");
        this.lat = objJson.getInt("ubi_y");
    }

    public Estacionamiento(int idEstacionamiento, String nombreEstacionamiento, int cantidadEstacionamiento, int ocupados, float lat, float lon) {
        this.idEstacionamiento = idEstacionamiento;
        this.nombreEstacionamiento = nombreEstacionamiento;
        this.cantidadEstacionamiento = cantidadEstacionamiento;
        this.ocupados = ocupados;
        this.lat = lat;
        this.lon = lon;
        ubicacion = new LatLng(lat,lon);
    }

    public int getIdEstacionamiento() {
        return idEstacionamiento;
    }

    public void setIdEstacionamiento(int idEstacionamiento) {
        this.idEstacionamiento = idEstacionamiento;
    }

    public String getNombreEstacionamiento() {
        return nombreEstacionamiento;
    }

    public void setNombreEstacionamiento(String nombreEstacionamiento) {
        this.nombreEstacionamiento = nombreEstacionamiento;
    }

    public int getCantidadEstacionamiento() {
        return cantidadEstacionamiento;
    }

    public void setCantidadEstacionamiento(int cantidadEstacionamiento) {
        this.cantidadEstacionamiento = cantidadEstacionamiento;
    }

    public int getOcupados() {
        return ocupados;
    }

    public void setOcupados(int ocupados) {
        this.ocupados = ocupados;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public LatLng getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(LatLng ubicacion) {
        this.ubicacion = ubicacion;
    }
}
