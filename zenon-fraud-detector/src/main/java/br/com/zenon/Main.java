package br.com.zenon;

import java.util.List;

public class Main {
    void main() {
        var transactionIngestor = new TransactionIngestor();
        List<Transaction> transactions = transactionIngestor.read("data/PS_20174392719_1491204439457_log.csv");
        IO.println(transactions.size());

        transactions.stream().limit(10).forEach(IO::println);
    }
}
