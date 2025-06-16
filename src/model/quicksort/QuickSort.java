package model.quicksort;

import model.dataStructures.Fila;
import model.dataStructures.No;
import model.professor.Professor;

public class QuickSort {
    //Deixar especifico pra professores
    public static void quickSortFila(Fila<Professor> fila)
    {
        fila.primeiro = quickSortRec(fila.primeiro, getUltimo(fila.primeiro));
        fila.ultimo = getUltimo(fila.primeiro);
    }

    private static No<Professor> quickSortRec(No<Professor> inicio, No<Professor> fim)
    {
        if (inicio == null || inicio == fim || inicio == fim.proximo)
        {
            return inicio;
        }

        No<Professor>[] particao = particionar(inicio, fim);
        No<Professor> novaCabeca = particao[0];
        No<Professor> novoPivo = particao[1];
        No<Professor> novaCauda = particao[2];

        if (novaCabeca != novoPivo)
        {
            No<Professor> temp = novaCabeca;
            while (temp.proximo != novoPivo) {
                temp = temp.proximo;
            }
            temp.proximo = null;

            novaCabeca = quickSortRec(novaCabeca, temp);

            getUltimo(novaCabeca).proximo = novoPivo;
        }

        novoPivo.proximo = quickSortRec(novoPivo.proximo, novaCauda);

        return novaCabeca;
    }

    private static No<Professor>[] particionar(No<Professor> inicio, No<Professor> fim)
    {
        Professor pivo = fim.dado;
        No<Professor> primeiro = null, ultimo = fim;
        No<Professor> anterior = null, atual = inicio, novoPivo = fim;

        while (atual != fim) {
            if (atual.dado.getPontos() > pivo.getPontos()) {
                if (primeiro == null) primeiro = atual;

                anterior = atual;
                atual = atual.proximo;
            } else {
                No<Professor> temp = atual.proximo;

                if (anterior != null) {
                    anterior.proximo = atual.proximo;
                } else {
                    inicio = atual.proximo;
                }

                atual.proximo = null;
                ultimo.proximo = atual;
                ultimo = atual;

                atual = temp;
            }
        }

        if (primeiro == null) primeiro = fim;

        No<Professor>[] resultado = new No[3];
        resultado[0] = primeiro;
        resultado[1] = fim;
        resultado[2] = ultimo;
        return resultado;
    }

    private static No<Professor> getUltimo(No<Professor> no)
    {
        while (no != null && no.proximo != null) {
            no = no.proximo;
        }
        return no;
    }
}