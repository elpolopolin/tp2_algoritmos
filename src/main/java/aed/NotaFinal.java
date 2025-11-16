package aed;

public class NotaFinal implements Comparable<NotaFinal> {
    public double _nota;
    public int _id;

    public NotaFinal(double nota, int id){
        _nota = nota;
        _id = id;
    }

    public int compareTo(NotaFinal otra){
        if (otra._id != this._id){
            return this._id - otra._id;
        }
        return Double.compare(this._nota, otra._nota);
    }

    @Override
    public boolean equals(Object obj) { // O(1)
        if (this == obj) return true; // O(1)
        if (obj == null || !(obj instanceof NotaFinal)) return false; // O(1)
        NotaFinal otra = (NotaFinal) obj; // O(1)
        return this._id == otra._id && Double.doubleToLongBits(this._nota) == Double.doubleToLongBits(otra._nota); // O(1)
    }

    @Override
    public int hashCode() { // O(1)
        return java.util.Objects.hash(_id, _nota); // O(1)
    }
}
