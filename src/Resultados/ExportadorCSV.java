package Resultados;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ExportadorCSV {
    private final List<ResultadoExperimento> resultados;
    private final List<ResumoExperimento> resumos;
    private final Path diretorio;

    public ExportadorCSV(List<ResultadoExperimento> resultados,
                         List<ResumoExperimento> resumos) {
        this(resultados, resumos, Path.of("."));
    }

    public ExportadorCSV(List<ResultadoExperimento> resultados,
                         List<ResumoExperimento> resumos,
                         Path diretorio) {
        if (resultados == null || resumos == null || diretorio == null) {
            throw new IllegalArgumentException(
                    "As listas e o diretório não podem ser nulos.");
        }

        this.resultados = resultados;
        this.resumos = resumos;
        this.diretorio = diretorio;
    }

    public void exportarResultadosBrutos() throws IOException {
        Files.createDirectories(diretorio);
        Path arquivo = diretorio.resolve("resultados_brutos.csv");

        try (BufferedWriter writer = Files.newBufferedWriter(
                arquivo, StandardCharsets.UTF_8)) {
            writer.write("tamanho;ordem;estrutura;teste;repeticao;operacoes;tempo_ns");
            writer.newLine();

            for (ResultadoExperimento resultado : resultados) {
                writer.write(resultado.getTamanho() + ";"
                        + resultado.getOrdem() + ";"
                        + resultado.getEstrutura() + ";"
                        + resultado.getTeste() + ";"
                        + resultado.getRepeticao() + ";"
                        + resultado.getQuantidadeOperacoes() + ";"
                        + resultado.getTempoNanos());
                writer.newLine();
            }
        }
    }

    public void exportarResumo() throws IOException {
        Files.createDirectories(diretorio);
        Path arquivo = diretorio.resolve("resumo.csv");

        try (BufferedWriter writer = Files.newBufferedWriter(
                arquivo, StandardCharsets.UTF_8)) {
            writer.write("tamanho;ordem;estrutura;teste;mediana_ms;mediana_ns_operacao");
            writer.newLine();

            for (ResumoExperimento resumo : resumos) {
                writer.write(resumo.getTamanho() + ";"
                        + resumo.getOrdem() + ";"
                        + resumo.getEstrutura() + ";"
                        + resumo.getTeste() + ";"
                        + Double.toString(resumo.getMedianaMilissegundos()) + ";"
                        + Double.toString(resumo.getMedianaPorOperacaoNanos()));
                writer.newLine();
            }
        }
    }
}
