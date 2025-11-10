interface Handle<T> {

    private int indice;
    private T elemento;

    Handle(T elemento, int indice) {
        this.elemento = elemento;
        this.indice = indice;
    }

    public T obtenerElemento() {
        return this.elemento;
    }

    int obtenerIndice() {
        return this.indice;
    }

    void actualizarIndice(int nuevoIndice) {
        this.indice = nuevoIndice;
    }
}