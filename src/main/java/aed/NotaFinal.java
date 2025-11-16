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
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof NotaFinal)) {
            return false;}
        NotaFinal otra = (NotaFinal) obj;
        return this._id == otra._id && Double.doubleToLongBits(this._nota) == Double.doubleToLongBits(otra._nota);
    }
}
