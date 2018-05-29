/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulacion;

/**
 *
 * @author arles
 */
public class Cache {
    
    //Memoria de la cache e bytes
    private final int memoria = 1024;
    //tamaño del bloque en bytes
    private final int bloque = 16;
    //Tipo de cache
    private int tipo;
    //Tamaño del conjunto si la cache es asociativa por conjunto, en lineas
    private final int conjunto = 8;

    public Cache(int tipo) {
        this.tipo = tipo;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getMemoria() {
        return memoria;
    }

    public int getBloque() {
        return bloque;
    }

    public int getConjunto() {
        return conjunto;
    }    
    
}
