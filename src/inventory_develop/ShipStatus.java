package inventory_develop;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import Enemy.PlayerGrowth;
import inventory_develop.NumberOfBullet;

public class ShipStatus {

    private int Speed_increase;
    private int SHOOTING_INTERVAL_increase;
    private int BULLET_SPEED_increase;
    private Double coin_increase;
    private int feverTime_score_increase;

    private Double pierce_probability;
    private Double hearth_probability;
    private Double bomb_probability;
    private Double shield_probability;
    private Double feverTime_probability;

    private int Speed_price;
    private int num_Bullet_price;
    private int Attack_Speed_price;
    private int Coin_Bonus_price;

    private NumberOfBullet numberOfBullet;
    private PlayerGrowth playerGrowth;



    Properties properties = new Properties();


    public ShipStatus() {}

    public void loadStatus(){
        try (InputStream inputStream = ShipStatus.class.getClassLoader().getResourceAsStream("StatusConfig.properties")) {
            if (inputStream == null) {
                System.out.println("FileNotFound");
                return;
            }

            properties.load(inputStream);

            Speed_increase = Integer.parseInt(properties.getProperty("Speed.increase"));
            SHOOTING_INTERVAL_increase = Integer.parseInt(properties.getProperty("SHOOTING_INTERVAL.increase"));
            BULLET_SPEED_increase = Integer.parseInt(properties.getProperty("BULLET_SPEED.increase"));
            coin_increase = Double.parseDouble(properties.getProperty("CoinBonus.increase"));
            //feverTime_score_increase = Integer.parseInt(properties.getProperty("feverTime.scoreMultiplier"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadProbability(){
        try (InputStream inputStream = ShipStatus.class.getClassLoader().getResourceAsStream("StatusConfig.properties")) {
            if (inputStream == null) {
                System.out.println("FileNotFound");
                return;
            }

            properties.load(inputStream);

            pierce_probability = Double.parseDouble(properties.getProperty("pierce.probability"));
            hearth_probability = Double.parseDouble(properties.getProperty("hearth.probability"));
            bomb_probability = Double.parseDouble(properties.getProperty("bomb.probability"));
            shield_probability = Double.parseDouble(properties.getProperty("shield.probability"));
            feverTime_probability = Double.parseDouble(properties.getProperty("feverTime.probability"));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPrice(){
        try (InputStream inputStream = ShipStatus.class.getClassLoader().getResourceAsStream("StatusConfig.properties")) {
            if (inputStream == null) {
                System.out.println("FileNotFound");
                return;
            }

            properties.load(inputStream);

            Speed_price = Integer.parseInt(properties.getProperty("Speed.price"));
            num_Bullet_price = Integer.parseInt(properties.getProperty("bullet_number.price"));
            Attack_Speed_price = Integer.parseInt(properties.getProperty("SHOOTING_INTERVAL.price"));
            Coin_Bonus_price = Integer.parseInt(properties.getProperty("CoinBonus.price"));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final int getSpeedIn(){
        return Speed_increase;
    }
    public final int getSuootingInIn(){
        return SHOOTING_INTERVAL_increase;
    }
    public final int getBulletSpeedIn(){
        return BULLET_SPEED_increase;
    }
    public final double getCoinIn(){
        return coin_increase;
    }
    public int getFeverScoreIn() { return feverTime_score_increase; }

    public final double getPierce_probability(){
        return pierce_probability;
    }
    public final double getHearth_probability(){
        return hearth_probability;
    }
    public final double getShield_probability(){
        return shield_probability;
    }
    public final double getBomb_probability(){
        return bomb_probability;
    }
    public Double getFeverTimeProbability() { return feverTime_probability; }

    public final int getSpeed_price(){
        return Speed_price;
    }
    public final int getCoinBonus_price(){
        return Coin_Bonus_price;
    }
    public final int getAttack_price(){
        return Attack_Speed_price;
    }
    public final int getBullet_price(){
        return num_Bullet_price;
    }







}
