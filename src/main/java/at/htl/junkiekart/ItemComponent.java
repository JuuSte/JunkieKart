package at.htl.junkiekart;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.input.UserAction;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;

import static com.almasb.fxgl.dsl.FXGL.getInput;

public class ItemComponent extends Component {
    private ItemType heldItem = null;

    //kokain
    private double kDuration = 1.0; // seconds
    private double kTimer = 0;
    private boolean kActive = false;
    private double kBoost = 40.0;

    //benutzte Nadel

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
            //case LSD ->   doLSD();
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
        entity.getComponent(CarControlComponent.class).setCurrentSpeed(
                entity.getComponent(CarControlComponent.class).getCurrentSpeed() + kBoost
        );
        entity.getComponent(CarControlComponent.class).setMaxSpeed(
                entity.getComponent(CarControlComponent.class).getMaxSpeed() + kBoost
        );
        kActive = true;
        kTimer = kDuration;
    }

    private void doLSD(){}

    @Override
    public void onUpdate(double tpf) {
        //Kokain Code
        if (kActive == true) {
            kTimer -= tpf;
            if (kTimer <= 0) {
                entity.getComponent(CarControlComponent.class).setMaxSpeed(
                        entity.getComponent(CarControlComponent.class).getMaxSpeed() - kBoost
                );
                kActive = false;
            }
        }
    }
}
