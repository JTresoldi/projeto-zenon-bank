package br.com.zenon;

import br.com.zenon.TransactionReport.Statistics;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.ResourceBundle;

public class ReportMain {
    void main() {

        var locale = Locale.of("ptq");

        var integerFormatter = NumberFormat.getIntegerInstance(locale);
        var currencyFormatter = DecimalFormat.getCurrencyInstance(locale);
        currencyFormatter.setCurrency(Currency.getInstance("USD"));

        var resourceBundle = ResourceBundle.getBundle("report", locale);

        var transactionReport = new TransactionReport();
        Statistics statistics = transactionReport.generateReport("data/PS_20174392719_1491204439457_log.csv");

        String formattedTotalTransactions = integerFormatter.format(statistics.totalTransactions());
        String formattedTotalFrauds = integerFormatter.format(statistics.totalFrauds());
        String formattedAmount = currencyFormatter.format(statistics.totalAmount());

        String msgTotalTransactions = resourceBundle.getString("label.total.transactions");
        String msgTotalFraud = resourceBundle.getString("label.total.frauds");
        String msgTotalAmount = resourceBundle.getString("label.total.amount");

        IO.println("""
                %s: %s
                %s: %s
                %s: %s
                """.formatted(
                        msgTotalTransactions, formattedTotalTransactions,
                        msgTotalFraud, formattedTotalFrauds,
                        msgTotalAmount, formattedAmount
        ));
    }
}
