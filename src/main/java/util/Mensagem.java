/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rubia
 */
public class Mensagem implements Serializable{
    private String operacao;
    private Status status;
    
    Map<String, Object> params;
    
    public Mensagem(String operacao){
      this.operacao = operacao;
      params = new HashMap<>();
    }
    
    public void setStatus(Status s){
        this.status = s;
        
    }
    
    public Status getStatus(){
        return status;
        
    }
    
    public String getOperacao(){
        return operacao;
    }
    
    public void setParam(String chave, Object valor){
        params.put(chave,valor);
    }
    
    public Object getParam(String chave){
       return params.get(chave);
    }
    
    public String toString(){
        String m = "\nOperacao: " + operacao;
        if(status != null)
            m += "\nStatus: " + status;
        
        //m += "\nParametros: ";
        for(Object p: params.keySet()){
             m += "\n " + p + " : " + params.get(p);
        }
        return m;
    }
    
}
