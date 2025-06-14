package model.dataStructures;

public class Fila<T> {

    public No primeiro;
    public No ultimo;

    public Fila()
    {
        this.primeiro = null;
        this.ultimo = null;
    }

    public boolean isEmpty()
    {
        return primeiro == null && ultimo == null;
    }

    public void insert(T value)
    {
        No newNo = new No();
        newNo.dado = value;
        newNo.proximo = null;

        if (isEmpty())
        {
            this.primeiro = newNo;
            this.ultimo = newNo;
            return;
        }

        this.ultimo.proximo = newNo;
        this.ultimo = newNo;
    }

    public T remove() throws Exception
    {
        if (isEmpty())
        {
            throw new Exception("Não há elementos na fila.");
        }

        T valor = (T)this.primeiro.dado;

        if (this.primeiro == this.ultimo)
        {
            this.primeiro = null;
            this.ultimo = null;
        }
        else
        {
            this.primeiro = this.primeiro.proximo;
        }

        return valor;
    }

    public int size()
    {
        int count = 0;

        if (!isEmpty())
        {
            No aux = primeiro;

            while (aux != null)
            {
                count++;
                aux = aux.proximo;
            }
        }

        return count;
    }

    @Override
    public String toString()
    {
        if (isEmpty())
        {
            return "";
        }

        StringBuffer str = new StringBuffer();
        No aux = primeiro;

        while (aux != null)
        {
            str.append(aux.dado);
            str.append(" | ");
            aux = aux.proximo;
        }

        return str.toString();
    }
}