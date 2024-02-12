package edu.escuelaing.arep.ASE.app.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import edu.escuelaing.arep.ASE.app.http.Registry;
import edu.escuelaing.arep.ASE.app.http.Request;
import edu.escuelaing.arep.ASE.app.http.Response;

import edu.escuelaing.arep.ASE.app.exception.HttpException;

public class MySparkController {

    private Registry registros = new Registry();

    public void get(String endPoint, BiFunction<Request, Response, ?> handler){
        registros.get(endPoint, handler);

    }

    public void post(String endPoint, BiFunction<Request, Response, ?> handler){
        registros.post(endPoint, handler);

    }

    public <R> R doGet(String endPoint, Request request, Response response){
        return registros.doGet(endPoint, request, response);
    }

    public <R> R doPost(String endPoint, Request request, Response response){
        return registros.doPost(endPoint, request, response);
    }

    public boolean tieneEndPoint(String endPoint){
        return registros.tieneEndPoint(endPoint);
    }
    
}
