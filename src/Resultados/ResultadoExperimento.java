package Resultados;

public class ResultadoExperimento {
    private int tamanho;
    private String ordem;
    private String estrutura;
    private String teste;
    private int repeticao;
    private int quantidadeOperacoes;
    private long tempoNanos;

    public ResultadoExperimento(int tamanho, String ordem, String estrutura,
                                String teste, int repeticao,
                                int quantidadeOperacoes, long tempoNanos) {
        this.tamanho = tamanho;
        this.ordem = ordem;
        this.estrutura = estrutura;
        this.teste = teste;
        this.repeticao = repeticao;
        this.quantidadeOperacoes = quantidadeOperacoes;
        this.tempoNanos = tempoNanos;
    }

    public int getTamanho() { return tamanho; }
    public String getOrdem() { return ordem; }
    public String getEstrutura() { return estrutura; }
    public String getTeste() { return teste; }
    public int getRepeticao() { return repeticao; }
    public int getQuantidadeOperacoes() { return quantidadeOperacoes; }
    public long getTempoNanos() { return tempoNanos; }
}
