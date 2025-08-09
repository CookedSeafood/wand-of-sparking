package net.cookedseafood.wandofsparking.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.mutable.MutableInt;

public abstract class WandOfSparkingConfig {
    public static final float MANA_CONSUMPTION = 2.0f;
    public static final double MOVEMENT_SPEED = 1.2;
    public static final short DISSIPATION_FUSE = 20;
    public static final float CASTING_DAMAGE = 3.0f;
    public static final byte PIERCE_COUNT = 1;
    public static final boolean IS_PARTICLE_VISIBLE = true;
    public static float manaConsumption;
    public static double movementSpeed;
    public static short dissipationFuse;
    public static float castingDamage;
    public static byte pierceCount;
    public static boolean isParticleVisible;

    public static int reload() {
        String configString;
        try {
            configString = FileUtils.readFileToString(new File("./config/wand-of-sparking.json"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            reset();
            return 1;
        }

        JsonObject config = new Gson().fromJson(configString, JsonObject.class);
        if (config == null) {
            reset();
            return 1;
        }

        return reload(config);
    }

    public static int reload(JsonObject config) {
        MutableInt counter = new MutableInt(0);

        if (config.has("manaConsumption")) {
            manaConsumption = config.get("manaConsumption").getAsFloat();
            counter.increment();
        } else {
            manaConsumption = MANA_CONSUMPTION;
        }

        if (config.has("movementSpeed")) {
            movementSpeed = config.get("movementSpeed").getAsDouble();
            counter.increment();
        } else {
            movementSpeed = MOVEMENT_SPEED;
        }

        if (config.has("dissipationFuse")) {
            dissipationFuse = config.get("dissipationFuse").getAsShort();
            counter.increment();
        } else {
            dissipationFuse = DISSIPATION_FUSE;
        }

        if (config.has("castingDamage")) {
            castingDamage = config.get("castingDamage").getAsFloat();
            counter.increment();
        } else {
            castingDamage = CASTING_DAMAGE;
        }

        if (config.has("pierceCount")) {
            pierceCount = config.get("pierceCount").getAsByte();
            counter.increment();
        } else {
            pierceCount = PIERCE_COUNT;
        }

        if (config.has("isParticleVisible")) {
            isParticleVisible = config.get("isParticleVisible").getAsBoolean();
            counter.increment();
        } else {
            isParticleVisible = IS_PARTICLE_VISIBLE;
        }

        return counter.intValue();
    }

    public static void reset() {
        manaConsumption = MANA_CONSUMPTION;
        movementSpeed = MOVEMENT_SPEED;
        dissipationFuse = DISSIPATION_FUSE;
        castingDamage = CASTING_DAMAGE;
        pierceCount = PIERCE_COUNT;
        isParticleVisible = IS_PARTICLE_VISIBLE;
    }
}
