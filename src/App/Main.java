package App;

import dados.CenarioExperimento;
import dados.GeradorDados;
import dados.GeradorOperacoes;
import dados.Operacoes;

import experimentos.ExecutorExperimentos;

import Estrutura.EstruturaDados;

import Resultados.ResultadoExperimento;
import Resultados.ResumoExperimento;
import Resultados.ExportadorCSV;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Main {

    private static final int[] TAMANHOS = {
        5000, 10000, 100000
    };

    private static final String[] ORDENS = {
        "CRESCENTE",
        "DECRESCENTE",
        "ALEATORIA"
    };

    private static final long SEMENTE = 42L;

    public static void main(String[] args) {

        System.out.println("==================================");
        System.out.println(" COMPARAÇÃO DE ESTRUTURAS DE DADOS");
        System.out.println("==================================");

        // Listas que armazenarão os resultados.
        List<ResultadoExperimento> resultados =
            new ArrayList<>();

        List<ResumoExperimento> resumos =
            new ArrayList<>();

        // Geradores de dados e operações.
        GeradorDados geradorDados = new GeradorDados();

        GeradorOperacoes geradorOperacoes =
            new GeradorOperacoes();

        // Percorre os tamanhos.
        for (int tamanho : TAMANHOS) {

            // Percorre as ordens de entrada.
            for (String ordem : ORDENS) {

                System.out.println("\n----------------------------------");
                System.out.println("Tamanho: " + tamanho);
                System.out.println("Ordem: " + ordem);
                System.out.println("----------------------------------");

                // Cria um cenário com dados e operações fixos.
                CenarioExperimento cenario =
                    new CenarioExperimento(
                        tamanho,
                        ordem,
                        SEMENTE,
                        geradorDados,
                        geradorOperacoes
                    );

                executarCenario(
                    cenario,
                    resultados,
                    resumos
                );
            }
        }

        // Exporta os arquivos CSV.
        exportarResultados(resultados, resumos);

        System.out.println("\n==================================");
        System.out.println(" EXPERIMENTOS FINALIZADOS");
        System.out.println("==================================");

        System.out.println(
            "Execuções registradas: " + resultados.size()
        );

        System.out.println(
            "Resumos calculados: " + resumos.size()
        );
    }

    private static void executarCenario(
        CenarioExperimento cenario,
        List<ResultadoExperimento> resultados,
        List<ResumoExperimento> resumos
    ) {

        int tamanho = cenario.getTamanho();

        int[] dados = cenario.getDadosIniciais();

        Operacoes operacoes = cenario.getOperacoes();

        // Percorre as sete estruturas.
        for (String tipo : FabricaEstruturas.getTipos()) {

            System.out.println("\nExecutando: " + tipo);

            // Capacidade para n + 10%.
            int capacidade = tamanho + tamanho / 10;

            // Fábrica da estrutura.
            Supplier<EstruturaDados> fabrica = () ->
                FabricaEstruturas.criar(tipo, capacidade);

            // Cria o executor.
            ExecutorExperimentos executor =
                new ExecutorExperimentos(
                    fabrica,
                    dados,
                    operacoes.getBuscasPresentes(),
                    operacoes.getBuscasAusentes(),
                    operacoes.getInsercoes(),
                    operacoes.getRemocoes(),
                    operacoes.getIntervalos()
                );

            // Executa os seis testes.
            executor.executarConstrucao();

            executor.executarBuscaPresente();

            executor.executarBuscaAusente();

            executor.executarInsercao();

            executor.executarRemocao();

            executor.executarConsultaIntervalo();

            // Recupera os tempos.
            Map<String, long[]> tempos =
                executor.getTodosTempos();

            // Registra os resultados e calcula as medianas.
            registrarResultados(
                tamanho,
                cenario.getOrdem(),
                tipo,
                tempos,
                dados.length,
                operacoes,
                resultados,
                resumos
            );

            System.out.println("Concluído: " + tipo);
        }
    }

    private static void registrarResultados(
        int tamanho,
        String ordem,
        String estrutura,
        Map<String, long[]> tempos,
        int quantidadeInicial,
        Operacoes operacoes,
        List<ResultadoExperimento> resultados,
        List<ResumoExperimento> resumos
    ) {

        // Quantidade de operações de cada teste.
        String[] testes = {
            "construcao",
            "busca_presente",
            "busca_ausente",
            "insercao",
            "remocao",
            "consulta_intervalo"
        };

        int[] quantidades = {
            quantidadeInicial,
            operacoes.getBuscasPresentes().length,
            operacoes.getBuscasAusentes().length,
            operacoes.getInsercoes().length,
            operacoes.getRemocoes().length,
            operacoes.getIntervalos().length
        };

        for (int i = 0; i < testes.length; i++) {

            String teste = testes[i];

            int quantidadeOperacoes = quantidades[i];

            long[] temposTeste = tempos.get(teste);

            if (temposTeste == null) {
                throw new IllegalStateException(
                    "Tempos não encontrados para: " + teste
                );
            }

            // Registra cada uma das cinco repetições.
            for (int repeticao = 0;
                 repeticao < temposTeste.length;
                 repeticao++) {

                ResultadoExperimento resultado =
                    new ResultadoExperimento(
                        tamanho,
                        ordem,
                        estrutura,
                        teste,
                        repeticao + 1,
                        quantidadeOperacoes,
                        temposTeste[repeticao]
                    );

                resultados.add(resultado);
            }

            // Calcula a mediana dos cinco tempos.
            ResumoExperimento resumo =
                new ResumoExperimento(
                    tamanho,
                    ordem,
                    estrutura,
                    teste,
                    temposTeste,
                    quantidadeOperacoes
                );

            resumos.add(resumo);
        }
    }

    private static void exportarResultados(
        List<ResultadoExperimento> resultados,
        List<ResumoExperimento> resumos
    ) {

        try {

            ExportadorCSV exportador =
                new ExportadorCSV(resultados, resumos);

            exportador.exportarResultadosBrutos();

            exportador.exportarResumo();

            System.out.println("\nArquivos CSV exportados com sucesso!");

        } catch (IOException e) {

            System.err.println(
                "Erro ao exportar os arquivos: " + e.getMessage()
            );

            e.printStackTrace();
        }
    }
}
