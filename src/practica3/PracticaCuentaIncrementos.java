package practica3;

import java.util.concurrent.atomic.AtomicLong;

public class PracticaCuentaIncrementos {
    public static void main ( String args[] ) {
        long   tiempoInicial, tiempoFinal;
        double tiempoTotal;
        long   maximoHebra;
        int    numHebras;

        /***** SI os molesta pasar por argumentos para estos ejemplos sencillos lo quitamos
         if ( args.length != 2 ) {
         System.err.println ( "java CuentaIncrementos <numHebras> <tope>" );
         System.exit(-1);
         }

         numHebras = Integer.valueOf ( args [ 0 ] );
         tope	  = Long.valueOf( args [ 1 ] );

         *////

        numHebras = 4;
        maximoHebra = 1000000;

        /// Código original
        System.out.println ( "Código original ...." );
        System.out.println ( "numHebras:" + numHebras );
        System.out.println ( "tope:      " + maximoHebra );

        MiHebra listaHebras[] = new MiHebra [ numHebras ];
        CuentaIncrementos cuentaIncrementos = new CuentaIncrementos();
        tiempoInicial = System.nanoTime();

        System.out.println ( "Creando y arrancando " + numHebras + "hebras." );

        for ( int i = 0; i < numHebras; i++ ) {
            listaHebras[ i ] = new MiHebra ( maximoHebra, cuentaIncrementos );
            listaHebras[ i ].start();
        }

        for ( int i = 0; i < numHebras; i++ ) {
            try {
                listaHebras[ i ].join();
            } catch ( InterruptedException ex ) {
                ex.printStackTrace();
            }
        }

        tiempoFinal = System.nanoTime();
        tiempoTotal = ( ( double ) ( tiempoFinal -tiempoInicial ) ) / 1.0e9;
        System.out.println ( "Total de incrementos: " + cuentaIncrementos.dameNumIncrementos() );
        System.out.println ( "Tiempo Transcurrido en segs.:" + tiempoTotal );
        System.out.println();
    }
}


class CuentaIncrementos {
    AtomicLong atomicIncrementador = new AtomicLong(0);

    void incrementaNumIncrementos() {

        atomicIncrementador.getAndIncrement();
    }

    AtomicLong dameNumIncrementos() {
        return (atomicIncrementador);
    }
}


class MiHebra extends Thread {
    long tope;
    CuentaIncrementos c;

    public MiHebra(long tope, CuentaIncrementos c) {

        this.tope = tope;
        this.c = c;
    }

    public void run() {
        for (long i = 0; i < tope; i++) {
            c.incrementaNumIncrementos();
        }
    }
}