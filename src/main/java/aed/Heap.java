package aed;
import java.util.ArrayList;

public class Heap<T extends Comparable<T>> {
    
    private ArrayList<HandleHeap> _array;
    private int _cardinal;

    private class HandleHeap implements Handle<T> {
        private T valor;
        private int index; 

        public HandleHeap(T v, int i) {
            this.valor = v;
            this.index = i;
        }
        
        @Override
        public T valor() { 
            return this.valor; 
        }

        public void setValor(T v) {
            this.valor = v;
        }
        
        @Override
        public void eliminar() {
            int i = this.index;
            
            swap(i, _cardinal - 1);
            
            _array.remove(_cardinal - 1);
            _cardinal--;
            
            if (i < _cardinal) {
                flotar(i);
                hundir(i);
            }
        }
    }

    public Heap() {
        _array = new ArrayList<HandleHeap>();
        _cardinal = 0;
    }

    public int cardinal() {
        return _cardinal;
    }

    private int padre(int i) { 
        return (i - 1) / 2; 
    }
    private int hijoIzq(int i) { 
        return 2 * i + 1; 
    }
    private int hijoDer(int i) 
    { 
        return 2 * i + 2; 
    }


    private void swap(int i, int j) {
        HandleHeap h_i = _array.get(i);
        HandleHeap h_j = _array.get(j);

        _array.set(i, h_j);
        _array.set(j, h_i);
        
        h_i.index = j;
        h_j.index = i;
    }
    
    private void flotar(int i) {
        while (i > 0 && _array.get(i).valor.compareTo(_array.get(padre(i)).valor) < 0) {
            swap(i, padre(i));
            i = padre(i);
        }
    }

    private void hundir(int i) {
        while (hijoIzq(i) < _cardinal) { 
            int hijoMenor = hijoIzq(i);
            int der = hijoDer(i);
            
            if (der < _cardinal && _array.get(der).valor.compareTo(_array.get(hijoMenor).valor) < 0) {
                hijoMenor = der;
            }
            
            if (_array.get(i).valor.compareTo(_array.get(hijoMenor).valor) <= 0) {
                break; 
            }
            
            swap(i, hijoMenor);
            i = hijoMenor; 
        }
    }
    
    public void actualizarPrioridad(Handle<T> h) {
        HandleHeap hh = (HandleHeap) h;
        int i = hh.index;
        flotar(i);
        hundir(i);
    }
    
    public Handle<T> insertar(T elem) {
        HandleHeap nuevoHandle = new HandleHeap(elem, _cardinal);
        _array.add(nuevoHandle);
        _cardinal++;
        
        flotar(_cardinal - 1);
        
        return nuevoHandle;
    }
    
    public T obtenerMinimo() {
        if (_cardinal == 0) return null;
        return _array.get(0).valor;
    }

    public T extraerMinimo() {
        if (_cardinal == 0) return null;
        
        HandleHeap minHandle = _array.get(0);
        
        swap(0, _cardinal - 1);
        
        _array.remove(_cardinal - 1);
        _cardinal--;
        
        if (_cardinal > 0) {
            hundir(0);
        }
        
        return minHandle.valor;
    }
}