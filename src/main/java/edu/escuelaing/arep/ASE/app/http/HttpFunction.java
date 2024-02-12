package edu.escuelaing.arep.ASE.app.http;

import java.util.function.BiFunction;

public class HttpFunction {
    private String metodoHttp;
    private BiFunction<Request, Response, ?> handler;

    public HttpFunction(String metodoHttp, BiFunction<Request, Response, ?> handler) {
        this.metodoHttp = metodoHttp;
        this.handler = handler;
    }

    public <R> R execute(Request request, Response response) {
        return (R) handler.apply(request, response);
    }

    public boolean verificarMetodoHttp(String metodoHttp) {
        return this.metodoHttp.equals(metodoHttp);
    }
    
}
