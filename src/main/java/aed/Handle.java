package aed;

/**
 * Interface Handle para referencias a elementos en el Heap.
 * Permite acceso O(1) y eliminación O(log n) de elementos.
 */
public interface Handle<T> {
    /**
     * Obtiene el valor del elemento referenciado por este handle.
     * @return el valor del elemento
     */
    public T valor();
    
    /**
     * Elimina el elemento del heap.
     * Complejidad: O(log n)
     */
    public void eliminar();
}