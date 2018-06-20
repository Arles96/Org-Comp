/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulacion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 *
 * @author Arles Cerrato, Jorge Morazán, Mario Raudales, Samir Facussé
 */
public class Simulador {
    
    //Tamaño de la memoria ram en bytes
    private int ram = 4096;
    //Tamaño de la memoria cache en bytes
    private int cache = 1024;
    //Tamaño del bloque en bytes
    private int bloque = 16;
    //Tamaño de la palabra en bytes
    private int palabra = 1;
    //Tamaño de lineas en el conjunto
    private int lineas_conjunto = 8;
    //Numero de lineas en la cache
    private int lineas_cache = cache / bloque;
    //Numero de bloques
    private int numeroBloques = ram / bloque;
    //Arreglo de la ram
    private int[] arreglo_ram = new int[ram];
    private int[][] memoriaCache = new int[lineas_cache][bloque + 3];
    private int NULL = -1;
    private double tiempo = 0;
    private int cantidad_conjunto = lineas_cache / lineas_conjunto;
    private int[] punteros = new int[cantidad_conjunto];
    private int puntero = 0;

    //Constructor
    public Simulador() {
        
    }
    
    //Setter 

    public void setRam(int ram) {
        this.ram = ram;
    }

    public void setCache(int cache) {
        this.cache = cache;
    }

    public void setBloque(int bloque) {
        this.bloque = bloque;
    }

    public void setPalabra(int palabra) {
        this.palabra = palabra;
    }

    public void setLineas_conjunto(int lineas_conjunto) {
        this.lineas_conjunto = lineas_conjunto;
    }

    public void setLineas_cache(int lineas_cache) {
        this.lineas_cache = lineas_cache;
    }

    public void setNumeroBloques(int numeroBloques) {
        this.numeroBloques = numeroBloques;
    }

    public void setArreglo_ram(int[] arreglo_ram) {
        this.arreglo_ram = arreglo_ram;
    }

    public void setMemoriaCache(int[][] memoriaCache) {
        this.memoriaCache = memoriaCache;
    }

    public void setNULL(int NULL) {
        this.NULL = NULL;
    }

    public void setTiempo(double tiempo) {
        this.tiempo = tiempo;
    }

    public void setCantidad_conjunto(int cantidad_conjunto) {
        this.cantidad_conjunto = cantidad_conjunto;
    }

    public void setPunteros(int[] punteros) {
        this.punteros = punteros;
    }

    public void setPuntero(int puntero) {
        this.puntero = puntero;
    }
    
    //Getter

    public int getRam() {
        return ram;
    }

    public int getCache() {
        return cache;
    }

    public int getBloque() {
        return bloque;
    }

    public int getPalabra() {
        return palabra;
    }

    public int getLineas_conjunto() {
        return lineas_conjunto;
    }

    public int getLineas_cache() {
        return lineas_cache;
    }

    public int getNumeroBloques() {
        return numeroBloques;
    }

    public int[] getArreglo_ram() {
        return arreglo_ram;
    }

    public int[][] getMemoriaCache() {
        return memoriaCache;
    }

    public int getNULL() {
        return NULL;
    }

    public double getTiempo() {
        return tiempo;
    }

    public int getCantidad_conjunto() {
        return cantidad_conjunto;
    }

    public int[] getPunteros() {
        return punteros;
    }

    public int getPuntero() {
        return puntero;
    }
    
    //Metodos administrativos
    
    public void cargar_cache(){
        for (int i = 0; i < memoriaCache.length; i++) {
            for (int j = 0; j < memoriaCache[i].length; j++) {
                memoriaCache[i][j] = NULL;
            }
        }
    }
    

    public void vaciar_cache_ram() {
        for (int i = 0; i < memoriaCache.length; i++) {
            if (memoriaCache[i][1] != NULL) {
                int linea = memoriaCache[i][1] * bloque;
                for (int j = 3; j < memoriaCache[i].length; j++) {
                    arreglo_ram[linea] = memoriaCache[i][j];
                    linea++;
                }
            }
        }
    }

    public int lsinCache(int direccion) {
        tiempo += 0.1;
        return arreglo_ram[direccion];
    }

    public void esinCache(int direccion, int valor) {
        try {

            arreglo_ram[direccion] = valor;
            tiempo += 0.1;
        } catch (Exception e) {
            System.out.println("Direccion no valida");
        }

    }

    public void ordenar(int tipo, int n) {
        int b, a;
        int cambios = 1;
        int limites = n - 1;
        while (cambios > 0) {
            cambios = 0;
            b = leer(0, tipo);
            for (int i = 0; i < limites; i++) {
                a = leer(i, tipo);
                if (a < b) {
                    escribir(i, tipo, b);
                    escribir(i - 1, tipo, a);
                    a = b;
                    cambios += 1;
                } else {
                    b = a;
                }
            }
            limites = limites - 1;
        }

    }

    public int lDirecto(int direccion) {
        int bloque = direccion / this.bloque;
        int linea = bloque % lineas_cache;
        int palabra = direccion % this.bloque;
        int etiqueta = bloque / lineas_cache;

        if (memoriaCache[linea][0] == 1) {
            if (memoriaCache[linea][1] == bloque) {
                tiempo += 0.01;
                return memoriaCache[linea][palabra + 3];
            } else if (memoriaCache[linea][2] == 1) {
                //Cache a Ram
                int bloqueEsta = memoriaCache[linea][3] / this.bloque;
                int direccionEnRam = bloqueEsta * this.bloque;

                for (int i = 0; i < this.bloque; i++) {
                    arreglo_ram[direccionEnRam] = memoriaCache[linea][i + 3];
                    direccionEnRam++;
                }
                //Ram a Cache
                int primeraDireccion = bloque * this.bloque;
                for (int i = 0; i < this.bloque; i++) {
                    memoriaCache[linea][i + 3] = arreglo_ram[primeraDireccion];
                    primeraDireccion++;
                }
                tiempo += 0.21;
                memoriaCache[linea][1] = bloque;
                memoriaCache[linea][2] = 0;
                return memoriaCache[linea][palabra + 3];
            } else {
                //Ram a Cache
                tiempo += 0.11;
                int primeraDireccion = bloque * this.bloque;
                for (int i = 0; i < this.bloque; i++) {
                    memoriaCache[linea][i + 3] = arreglo_ram[primeraDireccion];
                    primeraDireccion++;
                }
                memoriaCache[linea][1] = bloque;
                memoriaCache[linea][2] = 0;
                return memoriaCache[linea][palabra + 3];

            }
        } else {

            //Ram a Cache
            tiempo += 0.11;
            int primeraDireccion = bloque * this.bloque;
            for (int i = 0; i < this.bloque; i++) {
                memoriaCache[linea][i + 3] = arreglo_ram[primeraDireccion];
                primeraDireccion++;
            }
            memoriaCache[linea][1] = bloque;
            memoriaCache[linea][0] = 1;
            memoriaCache[linea][2] = 0;
            return memoriaCache[linea][palabra + 3];

        }

    }

    public int eDirecto(int direccion, int valor) {

        int bloque = direccion / this.bloque;
        int linea = bloque % lineas_cache;
        int palabra = direccion % this.bloque;
        int etiqueta = bloque / lineas_cache;

        if (memoriaCache[linea][0] == 1) {
            if (memoriaCache[linea][1] == bloque) {
                tiempo += 0.01;
                //Cambia Valor
                memoriaCache[linea][palabra + 3] = valor;
                //modificado true;
                memoriaCache[linea][2] = 1;
                return NULL;
            } else if (memoriaCache[linea][2] == 1) {
                tiempo += 0.21;
                //Cache a Ram
                int bloqueEsta = memoriaCache[linea][3] / this.bloque;
                int direccionEnRam = bloqueEsta * this.bloque;
                for (int i = 0; i < this.bloque; i++) {
                    arreglo_ram[direccionEnRam] = memoriaCache[linea][i + 3];
                    direccionEnRam++;
                }
                //Ram a Cache
                int primeraDireccion = bloque * this.bloque;
                for (int i = 0; i < this.bloque; i++) {
                    memoriaCache[linea][i + 3] = arreglo_ram[primeraDireccion];
                    primeraDireccion++;
                }
                //            memoriaCache[linea][1] = etiqueta;
                memoriaCache[linea][2] = 1;
                memoriaCache[linea][palabra + 3] = valor;
                return NULL;
            } else {
                //Ram a Cache
                tiempo += 0.11;
                int primeraDireccion = bloque * this.bloque;
                for (int i = 0; i < this.bloque; i++) {
                    memoriaCache[linea][i + 3] = arreglo_ram[primeraDireccion];
                    primeraDireccion++;
                }
                memoriaCache[linea][1] = bloque;
                memoriaCache[linea][2] = 1;
                memoriaCache[linea][palabra + 3] = valor;
                return NULL;
            }
        } else {

            //Ram a Cache
            tiempo += 0.11;
            int primeraDireccion = bloque * this.bloque;
            for (int i = 0; i < this.bloque; i++) {
                memoriaCache[linea][i + 3] = arreglo_ram[primeraDireccion];
                primeraDireccion++;
            }
            memoriaCache[linea][1] = bloque;
            memoriaCache[linea][0] = 1;
            memoriaCache[linea][2] = 1;
            memoriaCache[linea][palabra + 3] = valor;
            return NULL;

        }

    }

    public void cargarArrayRam() {
        File f = new File("datos.txt");
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(f);
            br = new BufferedReader(fr);
            for (int i = 0; i < ram; i++) {
                arreglo_ram[i] = Integer.parseInt(br.readLine());
            }
            br.close();
            fr.close();
        } catch (Exception e) {
        }
    }

    public int leer(int direccion, int tipo) {
        //System.out.println("Leer");
        switch (tipo) {
            case 0:
                return lsinCache(direccion);
            case 1:
                return lDirecto(direccion);
            case 3:
                return lAsociativaConjunto(direccion);
            case 2:
                return lAsociativa(direccion);
            default:
                return 0;
        }
    }

    public void escribir(int posicion, int tipo, int valor) {
        switch (tipo) {
            case 0:
                esinCache(posicion, valor);
                break;
            case 1:
                eDirecto(posicion, valor);
                break;
            case 3:
                eAsociativoConjunto(posicion, valor);
                break;
            case 2:
                eAsociativa(posicion, valor);
                break;
            default:
                break;
        }
    }

    public void inicioPuntero() {
        for (int i = 0; i < punteros.length; i++) {
            punteros[i] = i * lineas_conjunto;
        }

    }

    public int lAsociativaConjunto(int direccion) {
        int bloque = direccion / this.bloque;
        int conjunto = bloque % cantidad_conjunto;
        int palabra = direccion % this.bloque;
        //buscamos la linea asignada para esa direccion si no esta devuelve la primera
        int linea = linea_cache(bloque, conjunto);

        if (memoriaCache[linea][0] == 1) {
            //Esta en cahce
            if (estar_cache(bloque, conjunto)) {
                tiempo += 0.01;
                return memoriaCache[linea][palabra + 3];
            } else if (memoriaCache[linea][2] == 1) {
                //Cache a Ram
                int bloqueEsta = memoriaCache[linea][1];
                int direccionEnRam = bloqueEsta * this.bloque;

                for (int i = 0; i < this.bloque; i++) {
                    arreglo_ram[direccionEnRam] = memoriaCache[linea][i + 3];
                    direccionEnRam++;
                }
                //Ram a Cache
                int primeraDireccion = bloque * this.bloque;
                for (int i = 0; i < this.bloque; i++) {
                    memoriaCache[linea][i + 3] = arreglo_ram[primeraDireccion];
                    primeraDireccion++;
                }
                tiempo += 0.21;
                memoriaCache[linea][1] = bloque;
                memoriaCache[linea][2] = 0;
                return memoriaCache[linea][palabra + 3];
            } else {
                //Aumentamos el puntero de la siguiente direccion en el conjunto
                //Para poder a escribir alli si fuese necesario
                punteros[conjunto]++;
                //Se ha pasado del conjunto lo reinicia a su posicion inicial
                if (punteros[conjunto] % lineas_conjunto == 0) {
                    punteros[conjunto] -= 8;
                }
                //Ram a Cache
                tiempo += 0.11;
                int primeraDireccion = bloque * this.bloque;
                for (int i = 0; i < this.bloque; i++) {
                    memoriaCache[linea][i + 3] = arreglo_ram[primeraDireccion];
                    primeraDireccion++;
                }
                memoriaCache[linea][1] = bloque;
                memoriaCache[linea][0] = 1;
                memoriaCache[linea][2] = 0;
                return memoriaCache[linea][palabra + 3];

            }

        } else {
            //Aumentamos el puntero de la siguiente direccion en el conjunto
            //Para poder a escribir alli si fuese necesario
            punteros[conjunto]++;
            //Se ha pasado del conjunto lo reinicia a su posicion inicial
            if (punteros[conjunto] % lineas_conjunto == 0) {
                punteros[conjunto] -= 8;
            }
            //Ram a Cache
            tiempo += 0.11;
            int primeraDireccion = bloque * this.bloque;
            for (int i = 0; i < this.bloque; i++) {
                memoriaCache[linea][i + 3] = arreglo_ram[primeraDireccion];
                primeraDireccion++;
            }
            memoriaCache[linea][1] = bloque;
            memoriaCache[linea][0] = 1;
            memoriaCache[linea][2] = 0;
            return memoriaCache[linea][palabra + 3];
        }

    }

    public int eAsociativoConjunto(int direccion, int valor) {
        int bloque = direccion / this.bloque;
        int conjunto = bloque % cantidad_conjunto;
        int palabra = direccion % this.bloque;
        //buscamos la linea asignada para esa direccion si no esta devuelve la primera
        int linea = linea_cache(bloque, conjunto);

        if (memoriaCache[linea][0] == 1) {
            //Esta en cahce
            if (estar_cache(bloque, conjunto)) {
                tiempo += 0.01;
                memoriaCache[linea][palabra + 3] = valor;
                return memoriaCache[linea][palabra + 3];
            } else if (memoriaCache[linea][2] == 1) {
                //Cache a Ram
                int bloqueEsta = memoriaCache[linea][1];
                int direccionEnRam = bloqueEsta * this.bloque;

                for (int i = 0; i < this.bloque; i++) {
                    arreglo_ram[direccionEnRam] = memoriaCache[linea][i + 3];
                    direccionEnRam++;
                }
                //Ram a Cache
                int primeraDireccion = bloque * this.bloque;
                for (int i = 0; i < this.bloque; i++) {
                    memoriaCache[linea][i + 3] = arreglo_ram[primeraDireccion];
                    primeraDireccion++;
                }
                tiempo += 0.21;
                memoriaCache[linea][1] = bloque;
                memoriaCache[linea][2] = 0;
                memoriaCache[linea][palabra + 3] = valor;
                return memoriaCache[linea][palabra + 3];
            } else {
                //Aumentamos el puntero de la siguiente direccion en el conjunto
                //Para poder a escribir alli si fuese necesario
                punteros[conjunto]++;
                //Se ha pasado del conjunto lo reinicia a su posicion inicial
                if (punteros[conjunto] % lineas_conjunto == 0) {
                    punteros[conjunto] -= 8;
                }
                //Ram a Cache
                tiempo += 0.11;
                int primeraDireccion = bloque * this.bloque;
                for (int i = 0; i < this.bloque; i++) {
                    memoriaCache[linea][i + 3] = arreglo_ram[primeraDireccion];
                    primeraDireccion++;
                }
                memoriaCache[linea][1] = bloque;
                memoriaCache[linea][0] = 1;
                memoriaCache[linea][2] = 0;
                memoriaCache[linea][palabra + 3] = valor;
                return memoriaCache[linea][palabra + 3];

            }

        } else {
            //Aumentamos el puntero de la siguiente direccion en el conjunto
            //Para poder a escribir alli si fuese necesario
            punteros[conjunto]++;
            //Se ha pasado del conjunto lo reinicia a su posicion inicial
            if (punteros[conjunto] % lineas_conjunto == 0) {
                punteros[conjunto] -= 8;
            }
            //Ram a Cache
            tiempo += 0.11;
            int primeraDireccion = bloque * this.bloque;
            for (int i = 0; i < this.bloque; i++) {
                memoriaCache[linea][i + 3] = arreglo_ram[primeraDireccion];
                primeraDireccion++;
            }
            memoriaCache[linea][1] = bloque;
            memoriaCache[linea][0] = 1;
            memoriaCache[linea][2] = 0;
            memoriaCache[linea][palabra + 3] = valor;
            return memoriaCache[linea][palabra + 3];
        }

    }

    public int linea_cache(int bloque, int conjunto) {
        int lineaCont = conjunto * lineas_conjunto;
        for (int i = 0; i < lineas_conjunto; i++) {
            if (bloque == memoriaCache[lineaCont][1]) {
                return lineaCont;
            }
            lineaCont++;
        }
        return punteros[conjunto];
    }

    public boolean estar_cache(int bloque, int conjunto) {
        int lineaCont = conjunto * lineas_conjunto;
        for (int i = 0; i < lineas_conjunto; i++) {
            if (bloque == memoriaCache[lineaCont][1]) {
                return true;
            }
            lineaCont++;
        }
        return false;
    }

    public int lAsociativa(int direccion) {
        int bloque = direccion / this.bloque;
        int linea = lineacAsociativa(bloque);
        int palabra = direccion % this.bloque;
        if (memoriaCache[linea][0] == 1) {
            if (memoriaCache[linea][1] == bloque) {
                tiempo += 0.01;
                return memoriaCache[linea][palabra + 3];
            } else if (memoriaCache[linea][2] == 1) {
                //Cache a Ram
                int bloqueEsta = memoriaCache[linea][3] / this.bloque;
                int direccionEnRam = bloqueEsta * this.bloque;

                for (int i = 0; i < this.bloque; i++) {
                    arreglo_ram[direccionEnRam] = memoriaCache[linea][i + 3];
                    direccionEnRam++;
                }
                //Ram a Cache
                int primeraDireccion = bloque * this.bloque;
                for (int i = 0; i < this.bloque; i++) {
                    memoriaCache[linea][i + 3] = arreglo_ram[primeraDireccion];
                    primeraDireccion++;
                }
                tiempo += 0.21;
                memoriaCache[linea][1] = bloque;
                memoriaCache[linea][2] = 0;
                return memoriaCache[linea][palabra + 3];
            } else {
                //Ram a Cache
                tiempo += 0.11;
                int primeraDireccion = bloque * this.bloque;
                for (int i = 0; i < this.bloque; i++) {
                    memoriaCache[linea][i + 3] = arreglo_ram[primeraDireccion];
                    primeraDireccion++;
                }
                memoriaCache[linea][1] = bloque;
                memoriaCache[linea][2] = 0;
                puntero++;
                if (puntero == lineas_cache) {
                    puntero = 0;
                }
                return memoriaCache[linea][palabra + 3];

            }
        } else {

            //Ram a Cache
            tiempo += 0.11;
            int primeraDireccion = bloque * this.bloque;
            for (int i = 0; i < this.bloque; i++) {
                memoriaCache[linea][i + 3] = arreglo_ram[primeraDireccion];
                primeraDireccion++;
            }
            memoriaCache[linea][1] = bloque;
            memoriaCache[linea][0] = 1;
            memoriaCache[linea][2] = 0;
            puntero++;
            if (puntero == lineas_cache) {
                puntero = 0;
            }
            return memoriaCache[linea][palabra + 3];

        }

    }

    public int eAsociativa(int direccion, int valor) {
        int bloque = direccion / this.bloque;
        int linea = lineacAsociativa(bloque);
        int palabra = direccion % this.bloque;
        if (memoriaCache[linea][0] == 1) {
            if (memoriaCache[linea][1] == bloque) {
                tiempo += 0.01;
                //Cambia Valor
                memoriaCache[linea][palabra + 3] = valor;
                //modificado true;
                memoriaCache[linea][2] = 1;
                return NULL;
            } else if (memoriaCache[linea][2] == 1) {
                tiempo += 0.21;
                //Cache a Ram
                int bloqueEsta = memoriaCache[linea][3] / this.bloque;
                int direccionEnRam = bloqueEsta * this.bloque;
                for (int i = 0; i < this.bloque; i++) {
                    arreglo_ram[direccionEnRam] = memoriaCache[linea][i + 3];
                    direccionEnRam++;
                }
                //Ram a Cache
                int primeraDireccion = bloque * this.bloque;
                for (int i = 0; i < this.bloque; i++) {
                    memoriaCache[linea][i + 3] = arreglo_ram[primeraDireccion];
                    primeraDireccion++;
                }
                //            memoriaCache[linea][1] = etiqueta;
                memoriaCache[linea][2] = 1;
                memoriaCache[linea][palabra + 3] = valor;
                return NULL;
            } else {
                //Ram a Cache
                tiempo += 0.11;
                int primeraDireccion = bloque * this.bloque;
                for (int i = 0; i < this.bloque; i++) {
                    memoriaCache[linea][i + 3] = arreglo_ram[primeraDireccion];
                    primeraDireccion++;
                }
                memoriaCache[linea][1] = bloque;
                memoriaCache[linea][2] = 1;
                memoriaCache[linea][palabra + 3] = valor;
                puntero++;
                if (puntero == lineas_cache) {
                    puntero = 0;
                }
                return NULL;
            }
        } else {

            //Ram a Cache
            tiempo += 0.11;
            int primeraDireccion = bloque * this.bloque;
            for (int i = 0; i < this.bloque; i++) {
                memoriaCache[linea][i + 3] = arreglo_ram[primeraDireccion];
                primeraDireccion++;
            }
            memoriaCache[linea][1] = bloque;
            memoriaCache[linea][0] = 1;
            memoriaCache[linea][2] = 1;
            memoriaCache[linea][palabra + 3] = valor;
            puntero++;
            if (puntero == lineas_cache) {
                puntero = 0;
            }
            return NULL;

        }

    }

    public boolean estar_cacheAsociativa(int bloque) {
        for (int i = 0; i < lineas_cache; i++) {
            if (memoriaCache[i][1] == bloque) {
                return true;
            }
        }
        return false;
    }
    public int lineacAsociativa(int bloque) {
        for (int i = 0; i < lineas_cache; i++) {
            if (memoriaCache[i][1] == bloque) {
                return i;
            }
        }
        return puntero;
    }
    
}
