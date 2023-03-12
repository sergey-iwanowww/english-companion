package ru.isg.englishcompanion.common.utils;

import org.springframework.transaction.support.TransactionSynchronization;

import static org.springframework.transaction.support.TransactionSynchronizationManager.registerSynchronization;

public class TransactionUtils {

    public static void executeAfterCommit(Runnable r) {
        registerSynchronization(new TransactionSynchronization() {
            public void afterCommit() {
                r.run();
            }
        });
    }
}
