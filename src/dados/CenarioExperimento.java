package dados;


import java.util.Random;

public class CenarioExperimento {

    private int tamanho;
    private String ordem;
    private long semente;

    private int[] dadosIniciais;
    private Operacoes operacoes;

    public CenarioExperimento(int tamanho, String ordem, long semente,
                              GeradorDados geradorDados,
                              GeradorOperacoes geradorOperacoes) {

        this.tamanho = tamanho;
        this.ordem = ordem;
        this.semente = semente;

        // Gera os dados iniciais conforme a ordem escolhida
        switch (ordem.toUpperCase()) {

            case "CRESCENTE":
                dadosIniciais = geradorDados.gerarCrescente(tamanho);
                break;

            case "DECRESCENTE":
                dadosIniciais = geradorDados.gerarDecrescente(tamanho);
                break;

            case "ALEATORIA":
                dadosIniciais = geradorDados.embaralhar(
                    geradorDados.gerarCrescente(tamanho),
                    semente
                );
                break;

            default:
                throw new IllegalArgumentException("Ordem inválida: " + ordem);
        }

        // Gera todas as operações antes dos experimentos
        operacoes = new Operacoes(
            geradorOperacoes.gerarBuscasPresentes(dadosIniciais, 2000),
            geradorOperacoes.gerarBuscasAusentes(tamanho, 2000),
            geradorOperacoes.gerarInsercoes(tamanho),
            geradorOperacoes.gerarRemocoes(dadosIniciais, tamanho / 10),
            geradorOperacoes.gerarIntervalos(tamanho, 200)
        );
    }

    public int getTamanho() {
        return tamanho;
    }

    public String getOrdem() {
        return ordem;
    }

    public long getSemente() {
        return semente;
    }

    public int[] getDadosIniciais() {
        return dadosIniciais;
    }

    public Operacoes getOperacoes() {
        return operacoes;
    }
}
