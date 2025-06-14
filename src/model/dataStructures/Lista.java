package model.dataStructures;

public class Lista<T> {
    public No primeiro;
    private int size;

    public Lista ()
    {
        this.primeiro = null;
        this.size = 0;
    }

    public No get(int index) throws Exception
    {
        if (isEmpty())
        {
            throw new Exception("Lista vazia.");
        }

        if (index < 0 || index > size)
        {
            throw new Exception("Posição inválida");
        }

        No temp = this.primeiro;
        int count = 0;

        while (count < index)
        {
            temp = temp.proximo;
            count++;
        }

        return temp;
    }

    //region Add
    public void addFirst(T valor)
    {
        No newNo = new No();
        newNo.dado = valor;
        newNo.proximo = this.primeiro;
        this.primeiro = newNo;
        this.size++;
    }

    public void addLast(T valor) throws Exception
    {
        if (isEmpty())
        {
            addFirst(valor);
            return;
        }

        No newNo = new No();
        newNo.dado = valor;
        newNo.proximo = null;

        No ultimo = get(size - 1);
        ultimo.proximo = newNo;
        this.size++;
    }

    public void add(T value, int index) throws Exception
    {
        if (index > this.size || index < 0)
        {
            throw new Exception("Posição inválida.");
        }

        if (index == 0)
        {
            addFirst(value);
        }
        else if (index == this.size)
        {
            addLast(value);
        }
        else
        {
            No newNo = new No();
            newNo.dado = value;

            No anterior = get(index - 1);

            newNo.proximo = anterior.proximo;
            anterior.proximo = newNo;
            this.size++;
        }
    }
    //endregion

    //region Remove
    public void removeFirst() throws Exception
    {
        if (isEmpty())
        {
            throw new Exception("Lista vazia.");
        }

        this.primeiro = this.primeiro.proximo;
        this.size--;
    }

    public void removeLast() throws Exception
    {
        if (isEmpty())
        {
            throw new Exception("Lista vazia.");
        }

        if (this.size == 1)
        {
            removeFirst();
            return;
        }

        No penultimo = get(this.size - 2);
        penultimo.proximo = null;
        this.size--;
    }

    public void remove(int index) throws Exception
    {
        if (isEmpty())
        {
            throw new Exception("Lista vazia.");
        }

        if (index < 0 || index > this.size - 1)
        {
            throw new Exception("Posição inválida.");
        }

        if (index == 0)
        {
            removeFirst();
        }
        else if (index == this.size - 1)
        {
            removeLast();
        }
        else
        {
            No anterior = get(index - 1);
            No atual = get(index);

            anterior.proximo = atual.proximo;
        }
    }
    //endregion

    //region Utils
    public boolean isEmpty()
    {
        return this.primeiro == null;
    }

    public int size()
    {
        return this.size;
    }

    @Override
    public String toString()
    {
        if (!isEmpty())
        {
            boolean isNull = false;
            StringBuffer str = new StringBuffer();
            No temp = this.primeiro;

            while (!isNull)
            {
                str.append(temp.dado.toString());

                if (temp.proximo != null)
                {
                    str.append(" L> ");
                    temp = temp.proximo;
                }
                else
                {
                    isNull = true;
                }
            }

            return str.toString();
        }

        return null;
    }
    //endregion
}
