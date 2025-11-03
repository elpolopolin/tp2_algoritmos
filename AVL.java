public class AVL<T extends Comparable<T>> {
    private Nodo _raiz;
    private int _cardinal;

    private class Nodo {
        T valor;
        Nodo izquierdo;
        Nodo derecho;
        Nodo padre;
        int altura;

        Nodo(T v) {
            this.valor = v;
            this.izquierdo = null;
            this.derecho = null;
            this.padre = null;
            this.altura = 1;
        }
    }

    public AVL(){
        _raiz = null;
        _cardinal = 0;
    }

    public int cardinal(){
        return _cardinal;
    }

    private int altura(Nodo n) {
        return (n == null) ? 0 : n.altura;
    }

    private void actualizarAltura(Nodo n) {
        if (n != null) {
            n.altura = 1 + Math.max(altura(n.izquierdo), altura(n.derecho));
        }
    }

    private int factorBalanceo(Nodo n) {
        return (n == null) ? 0 : altura(n.izquierdo) - altura(n.derecho);
    }

    private Nodo rotacionDerecha(Nodo y) {
        Nodo x = y.izquierdo;
        Nodo T2 = x.derecho;

        x.derecho = y;
        y.izquierdo = T2;

        x.padre = y.padre;
        if (y.padre == null) {
            _raiz = x;
        } else if (y == y.padre.izquierdo) {
            y.padre.izquierdo = x;
        } else {
            y.padre.derecho = x;
        }
        
        y.padre = x;
        if (T2 != null) {
            T2.padre = y;
        }

        actualizarAltura(y);
        actualizarAltura(x);

        return x;
    }

    private Nodo rotacionIzquierda(Nodo x) {
        Nodo y = x.derecho;
        Nodo T2 = y.izquierdo;

        y.izquierdo = x;
        x.derecho = T2;
        
        y.padre = x.padre;
        if (x.padre == null) {
            _raiz = y;
        } else if (x == x.padre.izquierdo) {
            x.padre.izquierdo = y;
        } else {
            x.padre.derecho = y;
        }

        x.padre = y;
        if (T2 != null) {
            T2.padre = x;
        }

        actualizarAltura(x);
        actualizarAltura(y);

        return y;
    }

    private void balancear(Nodo n) {
        Nodo actual = n;
        while (actual != null) {
            actualizarAltura(actual);
            int fb = factorBalanceo(actual);
            Nodo proximoPadre = actual.padre;

            if (fb > 1) { 
                if (factorBalanceo(actual.izquierdo) < 0) {
                    actual.izquierdo = rotacionIzquierda(actual.izquierdo);
                }
                actual = rotacionDerecha(actual);
            } else if (fb < -1) {
                if (factorBalanceo(actual.derecho) > 0) {
                    actual.derecho = rotacionDerecha(actual.derecho);
                }
                actual = rotacionIzquierda(actual);
            }
            actual = proximoPadre;
        }
    }

    public T minimo(){
        if (_raiz == null) {
            return null;
        }
        Nodo actual = _raiz;
        while (actual.izquierdo != null) {
            actual = actual.izquierdo;
        }
        return actual.valor;
    }
    
    public T maximo(){
        if (_raiz == null) {
            return null;
        }
        Nodo actual = _raiz;
        while (actual.derecho != null){
            actual = actual.derecho;
        }
        return actual.valor;
    }
    
    public void insertar(T elem){
        Nodo nuevoNodo = new Nodo(elem);
        _cardinal++;

        if (_raiz == null){
            _raiz = nuevoNodo;
            return;
        }

        Nodo actual = _raiz;
        Nodo padre = null;

        while (actual != null){
            padre = actual;
            if (elem.compareTo(actual.valor) < 0) {
                actual = actual.izquierdo;
            } else {
                actual = actual.derecho;
            }
        }

        nuevoNodo.padre = padre;
        if (elem.compareTo(padre.valor) < 0){
            padre.izquierdo = nuevoNodo;
        } else {
            padre.derecho = nuevoNodo;
        }
        
        balancear(nuevoNodo.padre);
    }

    public boolean pertenece(T elem){
        Nodo actual = _raiz;
        while (actual != null){
            int comp = elem.compareTo(actual.valor);
            if (comp == 0) {
                return true;
            } else if (comp < 0){
                actual = actual.izquierdo;
            } else {
                actual = actual.derecho;
            }
        }
        return false;
    }

    public void eliminar(T elem){
        Nodo n = _raiz;
        while (n != null){
            int comp = elem.compareTo(n.valor);
            if (comp == 0){
                break;
            } else if (comp < 0){
                n = n.izquierdo;
            } else {
                n = n.derecho;
            }
        }

        if (n != null){
            Nodo nodoParaBalancear = null;
            
            if (n.izquierdo == null){
                nodoParaBalancear = n.padre;
                Nodo v = n.derecho;
                if (n.padre == null){
                    _raiz = v;
                } else if (n == n.padre.izquierdo){
                    n.padre.izquierdo = v;
                } else {
                    n.padre.derecho = v;
                }
                if (v != null){
                    v.padre = n.padre;
                }} 
            else if (n.derecho == null){
                nodoParaBalancear = n.padre;
                Nodo v = n.izquierdo;
                if (n.padre == null){
                    _raiz = v;
                } else if (n == n.padre.izquierdo){
                    n.padre.izquierdo = v;
                } else {
                    n.padre.derecho = v;
                }
                if (v != null){
                    v.padre = n.padre;
                }} 
            else {
                Nodo sucesor = n.derecho;
                while (sucesor.izquierdo != null){
                    sucesor = sucesor.izquierdo;
                }
                
                if(sucesor.padre != n){
                    nodoParaBalancear = sucesor.padre;
                    Nodo v_suc = sucesor.derecho;
                    if (sucesor == sucesor.padre.izquierdo){
                        sucesor.padre.izquierdo = v_suc;
                    } else {
                        sucesor.padre.derecho = v_suc;
                    }
                    if (v_suc != null){
                        v_suc.padre = sucesor.padre;
                    }
                    
                    sucesor.derecho = n.derecho;
                    sucesor.derecho.padre = sucesor;}
                else {
                    nodoParaBalancear = sucesor;
                }

                Nodo v_n = sucesor;
                if (n.padre == null) {
                    _raiz = v_n;
                } else if (n == n.padre.izquierdo){
                    n.padre.izquierdo = v_n;
                } else {
                    n.padre.derecho = v_n;
                }
                v_n.padre = n.padre;
                
                sucesor.izquierdo = n.izquierdo;
                sucesor.izquierdo.padre = sucesor;
            }
            _cardinal--;
            
            if (nodoParaBalancear != null) {
                balancear(nodoParaBalancear);
            }
        }
    }

    @Override
    public String toString(){
        String s = "{";
        AVL_Iterador iterador = this.iterador(); 
        if (iterador.haySiguiente()){
            s += iterador.siguiente().toString();
        }
        while (iterador.haySiguiente()){
            s += ", ";
            s += iterador.siguiente().toString();
        }
        s += "}";
        return s;
    }

    public class AVL_Iterador{
        private Nodo _actual;

        public AVL_Iterador(){
            _actual = _raiz;
            if (_actual != null){
                while (_actual.izquierdo != null){
                    _actual = _actual.izquierdo;
                }
            }
        }

        public boolean haySiguiente(){
            return _actual != null;
        }
        public T siguiente(){
            T valor = _actual.valor;
            if (_actual.derecho != null){
                _actual = _actual.derecho;
                while (_actual.izquierdo != null){
                    _actual = _actual.izquierdo;
                }
            } else {
                Nodo p = _actual.padre;
                while (p != null && _actual == p.derecho){
                    _actual = p;
                    p = p.padre;
                }
                _actual = p;
            }
            return valor;
        }
    }

    public AVL_Iterador iterador(){
        return new AVL_Iterador();
    }
}