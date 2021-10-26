package Practica4_apartado1;

import java.util.concurrent.atomic.AtomicInteger;

import static Practica4_apartado1.CalculoPrimosVector_a.esPrimo;
import static java.util.Collections.min;

public class CalculoPrimosVector_b {
    public static void main(String args[]) {
        int numHebras;
        long vectorNumeros[] = {
                200000033L, 4L, 4L, 4L, 4L, 4L, 4L, 4L,
                200000081L, 4L, 4L, 4L, 4L, 4L, 4L, 4L,
                200000039L, 4L, 4L, 4L, 4L, 4L, 4L, 4L,
                200000051L, 4L, 4L, 4L, 4L, 4L, 4L, 4L,
                200000069L, 4L, 4L, 4L, 4L, 4L, 4L, 4L,
                200000081L, 4L, 4L, 4L, 4L, 4L, 4L, 4L,
                200000083L, 4L, 4L, 4L, 4L, 4L, 4L, 4L,
                200000089L, 4L, 4L, 4L, 4L, 4L, 4L, 4L
        };


        numHebras = 4;

        //implementacionSecuencial
        implementacionSecuencial(vectorNumeros);

        //implementacionCiclica
        implementacionCiclica(vectorNumeros, numHebras);


        //implementacionBloques(vectorNumeros, numHebras);
        implementacionBloques(vectorNumeros, numHebras);


    }

    static void implementacionSecuencial(long[] vectorNumeros) {
        long t1;
        long t2;
        double tt;
        int contadorPrimos = 0;
        int contadorNoPrimos = 0;

        System.out.println("");
        System.out.println("Implementación secuencial.");

        t1 = System.nanoTime();
        //Escribe aquí la implementación secuencial
        for (int i = 0; i < vectorNumeros.length; i++) {
            if (esPrimo(vectorNumeros[i])) {
                contadorPrimos++;
                System.out.println(vectorNumeros[i] + " es primo");
            } else {
                contadorNoPrimos++;
                System.out.println(vectorNumeros[i] + " no es primo");
            }
        }

        //Fin de la implementación secuencial
        t2 = System.nanoTime();
        tt = ((double) (t2 - t1)) / 1.0e9;
        System.out.println();
        System.out.println("El total de primos es: "+contadorPrimos);
        System.out.println("El total de no primos es: "+contadorNoPrimos);
        System.out.println("Tiempo secuencial (seg.):\t\t\t" + tt);
    }

    static void implementacionCiclica(long[] vectorNumeros, int numHebras) {
        long t1;
        long t2;
        double tt;

        System.out.println("");
        System.out.println("Implementación cíclica.");

        MiHebraCiclica vectorHebras[] = new MiHebraCiclica[numHebras];
        CuentaPrimos contador = new CuentaPrimos();

        t1 = System.nanoTime();

        for (int idHebra = 0; idHebra < numHebras; idHebra++) {
            vectorHebras[idHebra] = new MiHebraCiclica(idHebra, numHebras, vectorNumeros,contador);
            vectorHebras[idHebra].start();
        }

        for (int i = 0; i < numHebras; i++) {
            try {
                vectorHebras[i].join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        t2 = System.nanoTime();
        tt = ((double) (t2 - t1)) / 1.0e9;
        System.out.println();
        System.out.println("El total de primos es: "+contador.muestraPrimos());
        System.out.println("El total de no primos es: "+contador.muestraNoPrimos());
        System.out.println("Tiempo cíclico (seg.):\t\t\t" + tt);
    }


//------------------------------------------------------------------------------------------------------------

    static void implementacionBloques(long[] vectorNumeros, int numHebras) {

        long t1;
        long t2;
        double tt;

        System.out.println("");
        System.out.println("Implementación por bloques.");

        MiHebraBloques v[] = new MiHebraBloques[numHebras];
        CuentaPrimos contador = new CuentaPrimos();

        t1 = System.nanoTime();

        for (int idHebra = 0; idHebra < numHebras; idHebra++) {
            v[idHebra] = new MiHebraBloques(idHebra, numHebras, vectorNumeros,contador);
            v[idHebra].start();
        }

        for (int i = 0; i < numHebras; i++) {
            try {
                v[i].join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        t2 = System.nanoTime();
        tt = ((double) (t2 - t1)) / 1.0e9;
        System.out.println();
        System.out.println("El total de primos es: "+contador.muestraPrimos());
        System.out.println("El total de no primos es: "+contador.muestraNoPrimos());
        System.out.println("Tiempo Bloques (seg.):\t\t\t" + tt);
    }


    static boolean esPrimo(long num) {
        boolean primo;
        if (num < 2) {
            primo = false;
        } else {
            primo = true;
            long i = 2;
            while ((i < num) && (primo)) {
                primo = (num % i != 0);
                i++;
            }
        }
        return (primo);
    }
}
class MiHebraCiclica extends Thread {
    int idHebra;
    int numHebras;
    long[] vectorNumeros;
    CuentaPrimos contador;

    public MiHebraCiclica(int idHebra, int numHebras, long[] vectorNumeros, CuentaPrimos contador) {
        this.idHebra = idHebra;
        this.numHebras = numHebras;
        this.vectorNumeros = vectorNumeros;
        this.contador = contador;

    }

    public void run() {
        for (int i = idHebra; i < vectorNumeros.length; i += numHebras) {
            if (esPrimo(vectorNumeros[i])) {
                contador.incrementaPrimo();
                System.out.println(vectorNumeros[i] + " es primo (hebra: "+idHebra+")");
            } else {
                contador.incrementaNoPrimo();
                System.out.println(vectorNumeros[i] + " no es primo (hebra: " + idHebra + ")");
            }
        }
    }

}

class MiHebraBloques extends Thread{
    int idHebra;
    int numHebra;
    long[] vectorNumeros;
    CuentaPrimos contador;

    public MiHebraBloques(int idHebra, int numHebra, long[] vectorNumeros, CuentaPrimos contador){
        this.idHebra = idHebra;
        this.numHebra = numHebra;
        this.vectorNumeros = vectorNumeros;
        this.contador = contador;
    }

    public void run(){
        int tamano;
        int ini;
        int fin;
        int n = vectorNumeros.length;
        tamano = (n+numHebra-1)/numHebra;
        ini =  idHebra * tamano;
        fin = Math.min(n,(idHebra+1)*tamano);
        for (int i = ini ; i < fin; i++){
            if (esPrimo(vectorNumeros[i])) {
                contador.incrementaPrimo();
                System.out.println(vectorNumeros[i] + " es primo (hebra: "+idHebra+")");
            } else {
                contador.incrementaNoPrimo();
                System.out.println(vectorNumeros[i] + " no es primo (hebra: " + idHebra + ")");
            }
        }
    }
}
class CuentaPrimos {
    AtomicInteger sumaPrimoAtomico = new AtomicInteger(0);

    AtomicInteger sumaNoPrimoAtomico = new AtomicInteger(0);

    void incrementaPrimo(){
        sumaPrimoAtomico.getAndIncrement();
    }

    void incrementaNoPrimo(){ sumaNoPrimoAtomico.getAndIncrement();}

    AtomicInteger muestraPrimos(){
        return sumaPrimoAtomico;
    }

    AtomicInteger muestraNoPrimos(){return sumaNoPrimoAtomico;}
}
