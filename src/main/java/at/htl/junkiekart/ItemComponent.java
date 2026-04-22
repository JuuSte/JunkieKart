package at.htl.junkiekart;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.input.UserAction;
import javafx.scene.input.KeyCode;

import static com.almasb.fxgl.dsl.FXGL.getInput;

public class ItemComponent extends Component {
    private ItemType heldItem = null;

    //kokain
    private double kDuration = 1.0; // seconds
    private boolean kActive = false;
    private double kBoost = 40.0;
    private double ktimer;

    //pilz
    private boolean invincible = false;
    private double stimer;

    //Bier
    private int beerCounter = 0;

    @Override
    public void onAdded() {
        getInput().addAction(new UserAction("Use Item") {
            @Override
            protected void onActionBegin() { useItem(); }
        }, KeyCode.E);
    }


    public void giveItem(ItemType item) {
        if (heldItem == null){
            heldItem = item;
        }
    }

    private void useItem() {
        if (heldItem == null){
            return;
        }
        switch (heldItem) {
            case Benutzte_Nadel -> spawnNadel();
            case Kokain ->  doCokain();
            case Shroom ->   eatShroom();
            case Beer_Bottle -> throwBottle();
        }
        heldItem = null;
//        FXGL.getWorldProperties().setValue("heldItem", "none");

    }

    private void spawnNadel() {
        double angleRad = Math.toRadians(entity.getRotation());
        double offsetX = Math.sin(angleRad) * 60;
        double offsetY = -Math.cos(angleRad) * 60;

        FXGL.spawn("Nadel", new SpawnData(entity.getX() - offsetX, entity.getY() - offsetY)
                .put("type", "Nadel"));
    }

    private void doCokain()  {
        if (kActive == true){
            return;
        }
        entity.getComponent(EffectComponent.class).spawnCocainEffect();
        entity.getComponent(CarControlComponent.class).setCurrentSpeed(
                entity.getComponent(CarControlComponent.class).getCurrentSpeed() + kBoost
        );
        entity.getComponent(CarControlComponent.class).setMaxSpeed(
                entity.getComponent(CarControlComponent.class).getMaxSpeed() + kBoost
        );
        kActive = true;
        ktimer = kDuration;
    }

    private void eatShroom(){
        invincible = true;
        stimer = 6;
        entity.getComponent(EffectComponent.class).spawnShroomEffect(true);
    }

    private void throwBottle() {
        beerCounter++;
        double angleRad = Math.toRadians(entity.getRotation());
        double offsetX = Math.sin(angleRad) * 60;
        double offsetY = -Math.cos(angleRad) * 60;

        SpawnData data = new SpawnData(entity.getX() + offsetX, entity.getY() + offsetY);
        data.put("angleRad", angleRad);
        FXGL.spawn("Beer", data);
    }

    @Override
    public void onUpdate(double tpf) {
        //Kokain Code
        if (kActive == true) {
            ktimer -= tpf;
            if (ktimer <= 0) {
                entity.getComponent(CarControlComponent.class).setMaxSpeed(
                        entity.getComponent(CarControlComponent.class).getMaxSpeed() - kBoost
                );
                kActive = false;
            }
        }

        //Pilz Code
        if(invincible == true){
            stimer -= tpf;
            if(stimer <= 0){
                entity.getComponent(EffectComponent.class).spawnShroomEffect(false);
                invincible = false;
            }
        }

        //Bier Code
        if(beerCounter >= 2){
            beerCounter = 0;

            double angleRad = Math.toRadians(entity.getRotation());
            double offsetX = Math.sin(angleRad) * 60;
            double offsetY = -Math.cos(angleRad) * 60;

            FXGL.spawn("Vomit", new SpawnData(entity.getX() - offsetX, entity.getY() - offsetY));
        }
    }

    public boolean getInvincible(){
        return invincible;
    }

    public ItemType getHeldItem(){
        return heldItem;
    }
}
