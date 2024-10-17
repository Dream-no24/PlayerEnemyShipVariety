package CtrlS;

import engine.Core;
import engine.FileManager;
import engine.GameState;

import java.io.IOException;
import java.util.logging.Logger;



public final class CurrencyManager {

    /** Singleton instance of the class. */
    private static CurrencyManager instance;
    /** Application logger. */
    private static Logger logger;
    private static FileManager fileManager;

    /**
     * private constructor.
     */
    private CurrencyManager() {
        fileManager = Core.getFileManager();
        logger = Core.getLogger();
    }

    /**
     * Returns shared instance of FileManager.
     *
     * @return Shared instance of FileManager.
     */
    public static CurrencyManager getInstance() {
        if (instance == null)
             instance = new CurrencyManager();
        return instance;
    }

    /**
     * Add an amount of money to the current coin.
     */
    public void addCoin(int amount) throws IOException {
        int current_coin = fileManager.loadCoin();
        amount += current_coin;
        fileManager.saveCoin(amount);
    }

    /**
     * Consume as much coin as the amount you have (cannot spend more than you currently have).
     */
    public boolean spendCoin(int amount) throws IOException {
        int current_coin = fileManager.loadCoin();
        if (amount <= current_coin) {
            current_coin -= amount;
            fileManager.saveCoin(current_coin);
            return true;
        }
        else {
            return false;
        }
    }

    public int getCoin() throws IOException {
        return fileManager.loadCoin();
    }

    /**
     * Add an amount of gem to the current gem.
     */
    // Team-Ctrl-S(Currency)
    public void addGem(int amount) throws IOException {
        int current_gem = fileManager.loadGem();
        amount += current_gem;
        fileManager.saveGem(amount);
    }

    /**
     * Consume as much gem as the amount you have (cannot spend more than you currently have).
     */
    // Team-Ctrl-S(Currency)
    public boolean spendGem(int amount) throws IOException {
        int current_gem = fileManager.loadGem();
        if (amount <= current_gem) {
            current_gem -= amount;
            fileManager.saveGem(current_gem);
            return true;
        }
        else {
            return false;
        }
    }

    // Team-Ctrl-S(Currency)
    public int getGem() throws IOException {
        return fileManager.loadGem();
    }

}
