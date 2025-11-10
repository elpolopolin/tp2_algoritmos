package aed;
public class ListaEnlazada<T> {
    private Nodo primero;
    private Nodo ultimo;

    private class Nodo {
        private Nodo ant;
        private T elem;
        private Nodo sig;

        public Nodo(Nodo ant, Nodo sig, T elem){
            this.ant=ant;
            this.sig=sig;
            this.elem=elem;
        }
    }

    public ListaEnlazada(){
        primero=null;
    }

    public int longitud() {
        int res=0;
        Nodo nodo_a_contar=this.primero;
        while (nodo_a_contar != null){
            res+=1;
            nodo_a_contar = nodo_a_contar.sig;
        }
        return res;
    }

    public void agregarAdelante(T elem) {
        Nodo nodo_a_poner = new Nodo(null, primero, elem);
        if (primero != null){primero.ant = nodo_a_poner;}
        primero = nodo_a_poner;
        if (ultimo == null){ultimo = nodo_a_poner;}
    }

    public void agregarAtras(T elem) {
        Nodo nodo_a_poner = new Nodo(ultimo, null, elem);
        if (ultimo!=null){ultimo.sig=nodo_a_poner;}
        ultimo=nodo_a_poner;
        if (primero == null){primero = nodo_a_poner;};
    }

    public T obtener(int i) {
        Nodo res = primero;
        int j=0;
        while(j!=i)
            {res=res.sig;
            j+=1;}
        return res.elem;
    }

    public void eliminar(int i) {
        Nodo res = primero;
        int j=0;
        if(i==0){
            primero=primero.sig;
            return;}
        while(j!=i)
            {res=res.sig;
            j+=1;}
        if (i!=longitud()-1){
            res.sig.ant=res.ant;
            res.ant.sig=res.sig;}
        else{res.ant.sig=null;}
        res= null;
    }

    public void modificarPosicion(int indice, T elem) {
        Nodo res = this.primero;
        int j=0;
        while(j!=indice)
            {res=res.sig;
            j+=1;}
        res.elem=elem;
    }

    public ListaEnlazada(ListaEnlazada<T> lista) {
        Nodo nodo_a_copiar = lista.primero;
        while(nodo_a_copiar != null)
            { agregarAtras(nodo_a_copiar.elem);
            nodo_a_copiar = nodo_a_copiar.sig;
            }
    }
    
    @Override
    public String toString() {
        String lista="";
        lista+=("[");
        Nodo minodo = primero;
        int i = 0;
        while (i != longitud()-1){
            lista+=(minodo.elem.toString() + ", ");
            minodo= minodo.sig;
            i+=1;
        }
        lista+=(minodo.elem.toString()+"]");
        return lista;
    }

    public class ListaIterador{
    	int indice = 0;

        public boolean haySiguiente() {
	        return (indice != longitud());
        }
        
        public boolean hayAnterior() {
	        return (indice > 0 );
        }

        public T siguiente() {
        if (haySiguiente()){
            int i;
            i = indice;
            indice  = indice + 1;
            return obtener(i);
        }
        else {return null;}
        }
        

        public T anterior() {
	    if (hayAnterior()){
            int i;
            i = indice -1;
            indice  = indice - 1;
            return obtener(i);
        }
        else {return null;}
        }
    }

    public ListaIterador iterador() {
	    return new ListaIterador();
    }

}