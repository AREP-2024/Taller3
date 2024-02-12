package edu.escuelaing.arep.ASE.app.http;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import edu.escuelaing.arep.ASE.app.http.Request;
import edu.escuelaing.arep.ASE.app.http.Response;
import edu.escuelaing.arep.ASE.app.exception.HttpException;

public class Registry {

    private Map<String, Collection <HttpFunction>> registros;

    public Registry() {
        this.registros = new HashMap<>();
    }



    public <R> void get(String endPoint, BiFunction<Request, Response, R> handler){
        if (!verificarEndPoint(endPoint, true)){
            throw new HttpException ();
        }

        if (!registros.containsKey(endPoint)) {
            registros.put(endPoint, new ArrayList<>());            
        }
        registros.get(endPoint).add(new HttpFunction("GET", handler));
    }

    public <R> R doGet(String endPoint, Request request, Response response){
        String endPointFinal = buscarEndPoint(endPoint);
        if (endPointFinal.equals("")){
            throw new HttpException ();
        }
        HttpFunction funcion = registros.get(endPointFinal).stream()
            .filter((httpFunction) -> httpFunction.verificarMetodoHttp("GET"))
            .findFirst()
            .orElseThrow(HttpException::new);
        return funcion.execute(request, response);
    } 

    public <R> void post(String endPoint, BiFunction<Request, Response, R> handler){
        if (!verificarEndPoint(endPoint, true)){
            throw new HttpException ();
        }

        if (!registros.containsKey(endPoint)) {
            registros.put(endPoint, new ArrayList<>());
            
        }
        registros.get(endPoint).add(new HttpFunction("POST", handler));
    }

    public <R> R doPost(String endPoint, Request request, Response response){
        String endPointFinal = buscarEndPoint(endPoint);
        if (endPointFinal.equals("")){
            throw new HttpException ();
        }
        HttpFunction funcion = registros.get(endPointFinal).stream()
            .filter((httpFunction) -> httpFunction.verificarMetodoHttp("POST"))
            .findFirst()
            .orElseThrow(HttpException::new);
        return funcion.execute(request, response);
    } 

    /*
     * Resuelve problema de prioridades
     */
    private String buscarEndPoint (String endPoint){
        if (!verificarEndPoint(endPoint, false)) {
            throw new HttpException ();
        }
        String [] resultado = new String[0];
        String [] compararEndPoint = endPoint.substring(1).split("/");        
        String resultadoFinal = "";
        for (String key : registros.keySet()) {
            String [] compararKey = key.substring(1).split("/");
            if(compararEndPoint.length != compararKey.length){
                continue;
            }

            boolean esIgual = true;
            for (int i = 0; i < compararEndPoint.length; i++) {

                if(!compararKey[i].startsWith(":") && !compararEndPoint[i].equals(compararKey[i])) {
                    esIgual = false;
                    break;
                }
            }
            if (esIgual && compararKey.length > resultado.length) {
                resultado = compararKey;
                resultadoFinal = key;                
            }

        }
        return resultadoFinal;

    }

    public boolean verificarEndPoint(String endPoint, boolean esRegistro){
        if (!endPoint.startsWith("/")) {
            return false;            
        } else if (endPoint.equals("/")) {
            return true;
        }

        return Arrays.asList(endPoint.substring(1).split("/")).parallelStream()
            .allMatch((seccion)->
                seccion != null && 
                !(seccion.equals("")) && 
                !(seccion.contains("*+¨^$# ")) && 
                (esRegistro || !seccion.startsWith(":"))
    
            );

    }

    public boolean tieneEndPoint (String endPoint){
        String resultBuscarEndPoint = this.buscarEndPoint(endPoint);

        return resultBuscarEndPoint != null && !resultBuscarEndPoint.equals("");
    }


    
}
