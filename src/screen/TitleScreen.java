package screen;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

import engine.Cooldown;
import engine.Core;
// Sound Operator
import Sound_Operator.SoundManager;
import engine.Score;
import inventory_develop.ShipStatus;

/**
 * Implements the title screen.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public class TitleScreen extends Screen {

	/** Milliseconds between changes in user selection. */
	private static final int SELECTION_TIME = 200;

	/** Time between changes in user selection. */
	private Cooldown selectionCooldown;

	// CtrlS
	private int coin;
	private int gem;
	// select One player or Two player
	private int pnumSelectionCode; //produced by Starter
	private int merchantState;
	//inventory
	private ShipStatus shipStatus;

	/**
	 * Constructor, establishes the properties of the screen.
	 *
	 * @param width
	 *            Screen width.
	 * @param height
	 *            Screen height.
	 * @param fps
	 *            Frames per second, frame rate at which the game is run.
	 */
	public TitleScreen(final int width, final int height, final int fps) {
		super(width, height, fps);

		// Defaults to play.
		this.merchantState = 0;
		this.pnumSelectionCode = 0;
		this.returnCode = 2;
		this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
		this.selectionCooldown.reset();

		// CtrlS: Set user's coin, gem
        try {
            this.coin = Core.getCurrencyManager().getCoin();
			this.gem = Core.getCurrencyManager().getGem();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Sound Operator
		SoundManager.getInstance().playBGM("mainMenu_bgm");

		// inventory load upgrade price
		shipStatus = new ShipStatus();
		shipStatus.loadPrice();
	}

	/**
	 * Starts the action.
	 *
	 * @return Next screen code.
	 */
	public final int run() {
		super.run();

		//produced by starter team
		if (this.pnumSelectionCode == 1 && this.returnCode == 2){
			return 4; //return 4 instead of 2
		}

		return this.returnCode;
	}

	/**
	 * Updates the elements on screen and checks for events.
	 */
	protected final void update() {
		super.update();

		draw();
		if (this.selectionCooldown.checkFinished()
				&& this.inputDelay.checkFinished()) {
			if (inputManager.isKeyDown(KeyEvent.VK_UP)
					|| inputManager.isKeyDown(KeyEvent.VK_W)) {
				previousMenuItem();
				this.selectionCooldown.reset();
				// Sound Operator
				SoundManager.getInstance().playES("menuSelect_es");
			}
			if (inputManager.isKeyDown(KeyEvent.VK_DOWN)
					|| inputManager.isKeyDown(KeyEvent.VK_S)) {
				nextMenuItem();
				this.selectionCooldown.reset();
				// Sound Operator
				SoundManager.getInstance().playES("menuSelect_es");
			}

			// produced by Starter
			if (returnCode == 2) {
				if (inputManager.isKeyDown(KeyEvent.VK_LEFT)
						|| inputManager.isKeyDown(KeyEvent.VK_A)) {
					moveMenuLeft();
					this.selectionCooldown.reset();
					// Sound Operator
					SoundManager.getInstance().playES("menuSelect_es");
				}
				if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)
						|| inputManager.isKeyDown(KeyEvent.VK_D)) {
					moveMenuRight();
					this.selectionCooldown.reset();
					// Sound Operator
					SoundManager.getInstance().playES("menuSelect_es");
				}
			}

			if(returnCode == 4) {
				if (inputManager.isKeyDown(KeyEvent.VK_LEFT)
						|| inputManager.isKeyDown(KeyEvent.VK_A)) {
					nextMerchantState();
					this.selectionCooldown.reset();
					// Sound Operator
					SoundManager.getInstance().playES("menuSelect_es");
				}
				if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)
						|| inputManager.isKeyDown(KeyEvent.VK_D)) {
					previousMerchantState();
					this.selectionCooldown.reset();
					// Sound Operator
					SoundManager.getInstance().playES("menuSelect_es");
				}

			}

			if (inputManager.isKeyDown(KeyEvent.VK_SPACE))
				if(returnCode == 4) {
					testStatUpgrade();
                    this.selectionCooldown.reset();
				}
				else this.isRunning = false;
		}
	}
	// Use later if needed. -Starter
	// public int getPnumSelectionCode() {return this.pnumSelectionCode;}

	/**
	 * runs when player do buying things
	 * when store system is ready -- unwrap annotated code and rename this method
	 */
//	private void testCoinDiscounter(){
//		if(this.currentCoin > 0){
//			this.currentCoin -= 50;
//		}

//		try{
//			Core.getFileManager().saveCurrentCoin();
//		} catch (IOException e) {
//			logger.warning("Couldn't save current coin!");
//		}
//	}

	/**
	 * Shifts the focus to the next menu item.
	 */
	
	private void testStatUpgrade() {
		// CtrlS: testStatUpgrade should only be called after coins are spent
		if (this.merchantState == 1) { // bulletCount
			try {
				if (Core.getUpgradeManager().LevelCalculation(Core.getUpgradeManager().getBulletCount()) > 3){
					Core.getLogger().info("The level is already Max!");
				}

				else if (!(Core.getUpgradeManager().getBulletCount() % 2 == 0)
						&& Core.getCurrencyManager().spendCoin(Core.getUpgradeManager().Price(1))) {

					Core.getUpgradeManager().addBulletNum();
					Core.getLogger().info("Bullet Number: " + Core.getUpgradeManager().getBulletNum());

					Core.getUpgradeManager().addBulletCount();

				} else if ((Core.getUpgradeManager().getBulletCount() % 2 == 0)
						&& Core.getCurrencyManager().spendGem(Core.getUpgradeManager().getBulletCount() + 1)) {

					Core.getUpgradeManager().addBulletCount();
					Core.getLogger().info("Upgrade has been unlocked");

				} else {
					Core.getLogger().info("you don't have enough");
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		} else if (this.merchantState == 2) { // shipSpeed
			try {
				if (Core.getUpgradeManager().LevelCalculation(Core.getUpgradeManager().getSpeedCount()) > 10){
					Core.getLogger().info("The level is already Max!");
				}

				else if (!(Core.getUpgradeManager().getSpeedCount() % 4 == 0)
						&& Core.getCurrencyManager().spendCoin(Core.getUpgradeManager().Price(2))) {

					Core.getUpgradeManager().addMovementSpeed();
					Core.getLogger().info("Movement Speed: " + Core.getUpgradeManager().getMovementSpeed());

					Core.getUpgradeManager().addSpeedCount();

				} else if ((Core.getUpgradeManager().getSpeedCount() % 4 == 0)
						&& Core.getCurrencyManager().spendGem(Core.getUpgradeManager().getSpeedCount() / 4)) {

					Core.getUpgradeManager().addSpeedCount();
					Core.getLogger().info("Upgrade has been unlocked");

				} else {
					Core.getLogger().info("you don't have enough");
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		} else if (this.merchantState == 3) { // attackSpeed
			try {
				if (Core.getUpgradeManager().LevelCalculation(Core.getUpgradeManager().getAttackCount()) > 10){
					Core.getLogger().info("The level is already Max!");
				}

				else if (!(Core.getUpgradeManager().getAttackCount() % 4 == 0)
						&& Core.getCurrencyManager().spendCoin(Core.getUpgradeManager().Price(3))) {

					Core.getUpgradeManager().addAttackSpeed();
					Core.getLogger().info("Attack Speed: " + Core.getUpgradeManager().getAttackSpeed());

					Core.getUpgradeManager().addAttackCount();

				} else if ((Core.getUpgradeManager().getAttackCount() % 4 == 0)
						&& Core.getCurrencyManager().spendGem(Core.getUpgradeManager().getAttackCount() / 4)) {

					Core.getUpgradeManager().addAttackCount();
					Core.getLogger().info("Upgrade has been unlocked");

				} else {
					Core.getLogger().info("you don't have enough");
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		} else if (this.merchantState == 4) { // coinGain
			try {
				if (Core.getUpgradeManager().LevelCalculation(Core.getUpgradeManager().getCoinCount()) > 10){
					Core.getLogger().info("The level is already Max!");
				}

				else if (!(Core.getUpgradeManager().getCoinCount() % 4 == 0)
						&& Core.getCurrencyManager().spendCoin(Core.getUpgradeManager().Price(4))) {

					Core.getUpgradeManager().addCoinAcquisitionMultiplier();
					Core.getLogger().info("CoinBonus: " + Core.getUpgradeManager().getCoinAcquisitionMultiplier());

					Core.getUpgradeManager().addCoinCount();

				} else if ((Core.getUpgradeManager().getCoinCount() % 4 == 0)
						&& Core.getCurrencyManager().spendGem(Core.getUpgradeManager().getCoinCount() / 4)) {

					Core.getUpgradeManager().addCoinCount();
					Core.getLogger().info("Upgrade has been unlocked");

				} else {
					Core.getLogger().info("you don't have enough");
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}
		try{
			this.coin = Core.getCurrencyManager().getCoin();
			this.gem = Core.getCurrencyManager().getGem();

		} catch (IOException e){
			throw new RuntimeException(e);
		}
	}
	private void nextMenuItem() {
		if (this.returnCode == 5) // Team Clover changed values because recordMenu added
			this.returnCode = 0; // from '2 player mode' to 'Exit' (starter)
		else if (this.returnCode == 0)
			this.returnCode = 2; // from 'Exit' to 'Play' (starter)
		else
			this.returnCode++; // go next (starter)
	}

	/**
	 * Shifts the focus to the previous menu item.
	 */
	private void previousMenuItem() {
		this.merchantState =0;
		if (this.returnCode == 0)
			this.returnCode = 5; // from 'Exit' to '2 player mode' (starter) // Team Clover changed values because recordMenu added
		else if (this.returnCode == 2)
			this.returnCode = 0; // from 'Play' to 'Exit' (starter)
		else
			this.returnCode--; // go previous (starter)
	}

	// left and right move -- produced by Starter
	private void moveMenuLeft() {
		if (this.returnCode == 2 ) {
			if (this.pnumSelectionCode == 0)
				this.pnumSelectionCode++;
			else
				this.pnumSelectionCode--;
		}

	}

	private void moveMenuRight() {
		if (this.returnCode == 2) {
			if (this.pnumSelectionCode == 0)
				this.pnumSelectionCode++;
			else
				this.pnumSelectionCode--;
		}
	}

	private void nextMerchantState() {
		if (this.returnCode == 4) {
			if (this.merchantState == 4)
				this.merchantState = 0;
			else
				this.merchantState++;
		}
	}

	private void previousMerchantState() {
		if (this.returnCode == 4) {
			if (this.merchantState == 0)
				this.merchantState = 4;
			else
				this.merchantState--;
		}
	}

	/**
	 * Draws the elements associated with the screen.
	 */
	private void draw() {
		drawManager.initDrawing(this);

		drawManager.drawTitle(this);
		drawManager.drawMenu(this, this.returnCode, this.pnumSelectionCode, this.merchantState);
		// CtrlS
		drawManager.drawCurrentCoin(this, coin);
		drawManager.drawCurrentGem(this, gem);

		super.drawPost();
		drawManager.completeDrawing(this);
	}

}
