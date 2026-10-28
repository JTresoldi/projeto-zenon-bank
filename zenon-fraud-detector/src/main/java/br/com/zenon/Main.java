package br.com.zenon;

import br.com.zenon.enums.TransactionTypeEnum;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class Main {
    void main() {
        var transactionIngestor = new TransactionIngestor();
        List<Transaction> transactions = transactionIngestor.read("data/PS_20174392719_1491204439457_log.csv");
        IO.println(transactions.size());

        transactions.stream().limit(10).forEach(IO::println);

        IO.println("--------------------------------------------------");

        List<Transaction> transactionBadData = transactionIngestor.read("data/doc.csv");
        IO.println(transactionBadData.size());

        transactionBadData.forEach(IO::println);

        IO.println("--------------------------------------------------");

        // Apenas transaçoes onde isFraud == true, imprima o tamanho da lista.
        var fraudAnalyzer = new FraudAnalyzer(transactions);
        long fraudCount = fraudAnalyzer.countFrauds();
        IO.println("Total de fraudes: " + fraudCount);

        // Imprima as 3 fruades de maior valor
        List<BigDecimal> highestFraudsAmounts = fraudAnalyzer.findHighestValueFrauds(3);
        IO.println("Top 3 fraudes de maior valor:");
        highestFraudsAmounts.forEach(amount -> IO.println("- %.2f".formatted(amount)));

        /* Obter apenas os nomes dos clientes de origem (nameOrigin) dessas fraudes e depois gere
        uma lista sem repetiçoes (Set ou distinct) com os 5 maiores clientes suspeitos.
         */
        List<String> suspiciousClients = fraudAnalyzer.findTopSuspiciousClients(5);
        IO.println("Top 5 clientes suspeitos:");
        suspiciousClients.forEach(IO::println);

        // Calcule o prejuizo total causado pelas fraudes (soma dos amount)
        BigDecimal totalFraudLoss = fraudAnalyzer.calculateTotalFraudLoss();
        IO.println("Prejuizo total: " + totalFraudLoss);

        // Conte quantas fraudes ocorreram por tipo de transaçao (CASH_OUT, TRANSFER, etc)
        Map<TransactionTypeEnum, Long> fraudCountByType = fraudAnalyzer.countFraudsByType();
        IO.println("Fraudes por tipo:");
        fraudCountByType.forEach((type, count) -> IO.println("- %s: %d".formatted(type, count)));

        IO.println("--------------------------------------------------");

        TransactionRepository transactionRepository;

        transactionRepository = new TransactionListRepository(transactions);

        String notFoundOriginName = "C12345";
        transactionRepository.findByOriginName(notFoundOriginName)
                .ifPresentOrElse(IO::println, () -> IO.println("Transação não encontrada para: " + notFoundOriginName));

        String existingOriginName = "C1231006815";

        long startTimeList = System.nanoTime();
        transactionRepository.findByOriginName(existingOriginName)
                .ifPresentOrElse(IO::println, () -> IO.println("Transação não encontrada para: " + existingOriginName));
        long endTimeList = System.nanoTime();

        IO.println("Tempo de busca - List (ms): " + (endTimeList - startTimeList) / 1_000_000.0);

        transactionRepository = new TransactionMapRepository(transactions);

        startTimeList = System.nanoTime();
        transactionRepository.findByOriginName(existingOriginName)
                .ifPresentOrElse(IO::println, () -> IO.println("Transação não encontrada para: " + existingOriginName));
        endTimeList = System.nanoTime();

        IO.println("Tempo de busca - Map (ms): " + (endTimeList - startTimeList) / 1_000_000.0);
    }
}
