package org.example;

import org.example.tinkoff.accountInfo.Account;
import ru.tinkoff.piapi.core.InvestApi;
import org.example.reports.*;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        var sandboxApi = InvestApi.createSandbox("t.VqnElEfrPsAXQfuhO8JgcnOUhpz9dI3dNq6yFntZaSuRtU7eKgBNusIpTIifs14jsPfI5hRl47JXsByKqWGMFg");

        var accounts = sandboxApi.getUserService().getAccountsSync();
        var account = accounts.get(0).getId();
        var positions = sandboxApi.getOperationsService().getPositionsSync(account);

        ArrayList<String> stoncksPositionList = Account.getStocksPositions(positions);

        StocksReport stocksReport = new StocksReport(stoncksPositionList, sandboxApi);
    }
}