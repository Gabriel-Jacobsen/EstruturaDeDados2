package Resultados;

import java.util.Arrays;

public class ResumoExperimento {
    private int tamanho;
    private String ordem;
    private String estrutura;
    private String teste;

    private double medianaMilissegundos;
    private double medianaPorOperacaoNanos;

    public ResumoExperimento(int tamanho, String ordem, String estrutura,
                             String teste, long[] tempos,
                             int quantidadeOperacoes) {
        this.tamanho = tamanho;
        this.ordem = ordem;
        this.estrutura = estrutura;
        this.teste = teste;

        this.medianaMilissegundos = calcularMedianaMilissegundos(tempos);
        this.medianaPorOperacaoNanos =
                calcularMedianaPorOperacao(tempos, quantidadeOperacoes);
    }

    // Retorna a mediana em nanossegundos (terceiro menor dos cinco tempos).
    public double calcularMediana(long[] tempos) {
        validarTempos(tempos);

        long[] copia = Arrays.copyOf(tempos, tempos.length);
        Arrays.sort(copia);

        return copia[2];
    }

    // Converte a mediana de nanossegundos para milissegundos.
    public double calcularMedianaMilissegundos(long[] tempos) {
        return calcularMediana(tempos) / 1_000_000.0;
    }

    // Retorna a mediana do tempo por operação, em nanossegundos.
    public double calcularMedianaPorOperacao(long[] tempos,
                                             int quantidadeOperacoes) {
        if (quantidadeOperacoes <= 0) {
            throw new IllegalArgumentException(
                    "A quantidade de operações deve ser maior que zero.");
        }

        return (double) calcularMediana(tempos) / quantidadeOperacoes;
    }

    private void validarTempos(long[] tempos) {
        if (tempos == null || tempos.length != 5) {
            throw new IllegalArgumentException(
                    "É necessário informar exatamente cinco tempos.");
        }

        for (long tempo : tempos) {
            if (tempo < 0) {
                throw new IllegalArgumentException(
                        "Os tempos não podem ser negativos.");
            }
        }
    }

    public int getTamanho() { return tamanho; }
    public String getOrdem() { return ordem; }
    public String getEstrutura() { return estrutura; }
    public String getTeste() { return teste; }
    public double getMedianaMilissegundos() {
        return medianaMilissegundos;
    }
    public double getMedianaPorOperacaoNanos() {
        return medianaPorOperacaoNanos;
    }
}
