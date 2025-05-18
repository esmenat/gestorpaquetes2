import java.util.EmptyStackException;
/**
 * Implementación de una pila LIFO usando un array de tamaño fijo (estático).
 * Usada aquí para mantener un historial de paquetes añadidos (ejemplo de uso).
 */
public class PilaPaquetes {
    private Object[] pila;
    private int tamano;
    private int numeroElementos;

    public PilaPaquetes(int size) {
        this.tamano = size;
        this.numeroElementos = 0;
        this.pila = new Object[size];
    }

    public boolean Llena() {
        return numeroElementos == tamano;
    }

    public boolean Vacia() {
        return numeroElementos == 0;
    }

    public void Apilar(Object p) {
        if (!Llena()) {
            pila[numeroElementos++] = p;
        } else {
            throw new IllegalStateException("La pila está llena. No se puede añadir el paquete.");
        }
    }

    public Object Desapilar() {
        if (!Vacia()) {
            Object aux = pila[numeroElementos - 1];
            pila[--numeroElementos] = null;
            return aux;
        } else {
            throw new EmptyStackException();
        }
    }

    public int getNumElementos() {
        return numeroElementos;
    }

    public int getTamano() {
        return tamano;
    }

    public Object[] getArreglo() {
        return pila;
    }
}
