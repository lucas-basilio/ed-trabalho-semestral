package model.dataStructures;

public class HashTable<T> {

    Lista<T>[] hashTable;

    public HashTable(int size)
    {
        this.hashTable = new Lista[size];
        inicializar();
    }

    private void inicializar()
    {
        for (int x = 0; x < this.hashTable.length; x++)
        {
            this.hashTable[x] = new Lista<>();
        }
    }

    public void add(T item) throws Exception
    {
        this.hashTable[item.hashCode()].addLast(item);
    }

    public T search(T item) throws Exception
    {
        int pos = item.hashCode();
        int size = this.hashTable[pos].size();

        for (int x = 0; x < size; x++)
        {
            T temp = (T)this.hashTable[pos].get(x).dado;

            if (temp.equals(item))
            {
                return temp;
            }
        }

        throw new Exception("Dado não encontrado.");
    }

    public void remove(T item) throws Exception
    {
        int pos = item.hashCode();
        int size = this.hashTable[pos].size();

        for (int x = 0; x < size; x++)
        {
            T temp = (T)this.hashTable[pos].get(x).dado;

            if (temp.equals(item))
            {
                this.hashTable[pos].remove(x);
                return;
            }
        }

        throw new Exception("Dado não encontrado.");
    }


}
