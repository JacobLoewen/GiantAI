package com.Giant.GiantAI;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GiantZombie extends EntityGiantZombie {

    public GiantZombie(World world) {
        super(world);
        this.setSize(1.95F, 6.0F);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        EntityPlayer nearestPlayer = world.getClosestPlayer(posX, posY, posZ, 64, false);
        if (nearestPlayer != null && !nearestPlayer.isDead) {
            this.getNavigator().tryMoveToEntityLiving(nearestPlayer, 1.0);
        }
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(50.0D);
    }

    @Override
    public boolean isChild() {
        return false;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    public boolean getCanSpawnHere() {
        return super.getCanSpawnHere() && this.world.checkNoEntityCollision(this.getEntityBoundingBox())
                && this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty()
                && !this.world.containsAnyLiquid(this.getEntityBoundingBox());
    }

//    @Override
//    public boolean canSpawnAtDifficulty() {
//        return true;
//    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.setAttackTarget(this.world.getNearestAttackablePlayer(this, 32.0D, 32.0D));
    }


//    public boolean attackEntityAsMob(EntityLivingBase entity) {
//        boolean success = super.attackEntityAsMob(entity);
//        if (success && entity instanceof EntityPlayer) {
//            EntityPlayer player = (EntityPlayer) entity;
//            player.addExperience(50);
//        }
//        return success;
//    }

}