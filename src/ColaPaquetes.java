import java.util.NoSuchElementException;

/**
 * Implementación de una cola FIFO usando un array de tamaño fijo (estático).
 * Utiliza un enfoque de array circular.
 */

public class ColaPaquetes {
    private Object[] cola;
    private int inicio;
    private int fin;
    private int numeroElementos;
    private final int tamano;

    public ColaPaquetes(int tamano) {
        if (tamano <= 0) throw new IllegalArgumentException("La capacidad debe ser positiva.");
        this.tamano = tamano;
        cola = new Object[tamano];
        numeroElementos= 0;
        inicio = 0;
        fin = 0;
    }

    public boolean Llena() {
        return numeroElementos == tamano;
    }

    public boolean Vacia() {
        return numeroElementos == 0;
    }

    public void Encolar(Object p) {

        if (!Llena()) {
            cola[fin++] = p;
            numeroElementos++;
            if (fin == tamano){
                fin = 0;
            }                
        } else {
            throw new IllegalStateException("La cola está llena. No se puede añadir el paquete: ");
        }
    }

    public Object Desencolar() {
        if (!Vacia()) {
            Object aux = cola [inicio];
            cola[inicio] = null; // Limpieza (opcional)
            inicio++;
            numeroElementos--;
            if (inicio == tamano)
                inicio = 0;
            return aux;
        } else {
            throw new NoSuchElementException("La cola está vacía. No se puede desencolar.");
        }
    }

    public Object Primero() {
        if (!Vacia()) {
            return cola[inicio];
        } else {
            throw new NoSuchElementException("La cola está vacía. No se puede mirar el frente.");
        }
    }

    public int getNumeroElementos() {
        return numeroElementos;
    }

    public int getTamano() {
        return tamano;
    }

    public int getInicio() {
        return inicio;
    }

    public Object[] getArreglo() {
        return cola;
    }
}
