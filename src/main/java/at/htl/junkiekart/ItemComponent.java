package at.htl.junkiekart;

import com.almasb.fxgl.dsl.FXGL;
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

    //pilz
    private boolean invincible = false;

    //Algemain
    private double timer = 0;

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
        }
        heldItem = null;
    }

    private void spawnNadel() {
        FXGL.spawn("Nadel", entity.getX()-50, entity.getY()-50);
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
        timer = kDuration;
    }

    private void eatShroom(){
        invincible = true;
        timer = 15;
        entity.getComponent(EffectComponent.class).spawnShroomEffect(true);
    }

    @Override
    public void onUpdate(double tpf) {
        //Kokain Code
        if (kActive == true) {
            timer -= tpf;
            if (timer <= 0) {
                entity.getComponent(CarControlComponent.class).setMaxSpeed(
                        entity.getComponent(CarControlComponent.class).getMaxSpeed() - kBoost
                );
                kActive = false;
            }
        }
        //Pilz Code

        if(invincible == true){
            timer -= tpf;
            if(timer <= 0){
                entity.getComponent(EffectComponent.class).spawnShroomEffect(false);
                invincible = false;
            }
        }
    }

    public boolean getInvincible(){
        return invincible;
    }
}
