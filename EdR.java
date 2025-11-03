import java.util.Comparator; 
// Nota: Solo se usa como referencia. Debes implementar tu propio comparador y Heap/AVL

public class EdR {
    
    // --- Estructuras Clave para la Complejidad ---
    
    // 1. Almacenamiento Principal y O(log E) por ID:
    // Almacena a todos los estudiantes y permite búsqueda por ID en O(log E).
    private AVL<Estudiante> estudiantesPorId; 

    // 2. Control de Posición en el Aula (O(1) por ID):
    // Permite acceso directo por ID (índice), crucial para O(1) en algunas operaciones.
    private Estudiante[] estudiantesArray; // Usar Arreglo o Arreglo Redimensionable

    // 3. Control de Peor Puntaje (O(log E)):
    // Almacena a los estudiantes, ordenados por puntaje (y desempate por ID).
    // Necesario para consultarDarkWeb en O(k(R + log E)).
    private Heap<Estudiante> estudiantesPorPuntaje; // Heap de Mínimos

    // 4. Datos del Sistema:
    private int ladoAula;
    private int[] examenCanonico; // Arreglo de Respuestas Canónicas
    private int entregasRestantes;
    private ListaEnlazada<Integer> sospechososCopia;
    
    // ---------------------------------------------
    
    // Constructor (Operación 1)
    /**
     * Inicializa el sistema EdR. Complejidad: O(E * R)
     */
    public EdR(int ladoAula, int cantidadEstudiantes, int[] examenCanonico) {
        this.ladoAula = ladoAula;
        this.examenCanonico = examenCanonico;
        this.entregasRestantes = cantidadEstudiantes;
        this.estudiantesPorId = new AVL<>();
        this.estudiantesArray = new Estudiante[cantidadEstudiantes];
        this.estudiantesPorPuntaje = new Heap<>();
        this.sospechososCopia = new ListaEnlazada<>();
        
        // Inicialización de E estudiantes (O(E)) y sus R respuestas (O(R))
        // Bucle E: for (int id = 0; id < cantidadEstudiantes; id++)
            // Inicializar Estudiante (O(R))
            // Insertar en AVL (O(log E)
            // Insertar en Arreglo (O(1))
            // Insertar en Heap (O(log E))
        // El costo dominante es E * R.
    }

    // --- Operaciones a Implementar ---
    
    /**
     * El estudiante se copia. Complejidad: O(R + log(E))
     */
    public void copiarse(int idEstudiante) {
        // 1. Buscar estudiante en AVL: O(log E) 
        // 2. Determinar vecino (usando la estructura de proximidad): O(1) o O(log E)
        // 3. Comparar R respuestas: O(R)
        // 4. Actualizar examen del estudiante: O(1)
    }

    /**
     * El estudiante resuelve un ejercicio. Complejidad: O(log(E))
     */
    public void resolver(int idEstudiante, int nroEjercicio, int respuestaEjercicio) {
        // 1. Buscar estudiante en AVL: O(log E)
        // 2. Actualizar la respuesta en su arreglo de respuestas: O(1)
        // 3. Recalcular puntaje (si es necesario) y actualizar la posición en el Heap (O(log E))
    }

    /**
     * K estudiantes reemplazan su examen. Complejidad: O(k(R + log(E))
     */
    public void consultarDarkWeb(int k, int[] examenDW) {
        // Bucle k veces:
        // 1. Obtener el peor estudiante (Heap de Mínimos): O(1)
        // 2. Eliminarlo del Heap: O(log E)
        // 3. Actualizar examen (R respuestas): O(R)
        // 4. Reinsertar en el Heap con nuevo puntaje: O(log E)
        // Costo total: k * (O(log E) + O(R)) -> O(k(R + log E))
    }

    /**
     * Devuelve una secuencia de notas ordenada por ID. Complejidad: O(E)
     */
    public int[] notas() {
        // 1. Recorrer el arreglo estudiantesArray (ordenado por ID): O(E)
        // 2. Obtener la nota de cada estudiante (asumiendo que está precalculada o es O(1))
    }

    /**
     * El estudiante entrega su examen. Complejidad: O(1)
     */
    public void entregar(int idEstudiante) {
        // 1. Buscar estudiante en estudiantesArray: O(1)
        // 2. Marcar como entregado (cambiar un booleano): O(1)
        // 3. Decrementar entregasRestantes: O(1)
    }

    /**
     * Devuelve la lista de sospechosos ordenada por ID. Complejidad: O(E * R)
     */
    public int[] chequearCopias() {
        // NOTA: Requiere que todos los estudiantes hayan entregado.
        // Bucle anidado para verificar la condición de copia (E x R):
        // Bucle E: por cada estudiante (sospechoso)
        //   Bucle R: por cada respuesta
        //     Verificar si la respuesta coincide con el 25% de los demás (O(E))
        // El costo dominante es O(E * R).
        // Devolver la lista 'sospechososCopia' ordenada por ID (ya lo estará si se inserta al final de una lista o se usa el orden natural).
    }

    /**
     * Devuelve las notas de los no copiados, ordenadas por NotaFinal.nota (decreciente).
     * Complejidad: O(E log(E))
     */
    public float[] corregir() {
        // NOTA: Requiere que previamente se haya realizado chequearCopias.
        // 1. Iterar sobre todos los estudiantes (O(E)).
        // 2. Crear una lista/arreglo con las NotaFinales de los no copiados (O(E)).
        // 3. Ordenar la lista/arreglo resultante por nota (decreciente) y desempate por mayor ID.
        //    Esto es un ordenamiento por comparación: O(N log N) donde N <= E.
        //    Usar tu implementación de HeapSort o MergeSort (O(E log E)).
    }
}